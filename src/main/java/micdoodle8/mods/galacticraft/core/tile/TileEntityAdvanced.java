package micdoodle8.mods.galacticraft.core.tile;

import io.netty.buffer.ByteBuf;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.NetworkUtil;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;

public abstract class TileEntityAdvanced extends TileEntityInventory implements IPacketReceiver, ITickable
{

    public int ticks = 0;
    private LinkedHashSet<Field> fieldCacheClient;
    private LinkedHashSet<Field> fieldCacheServer;
    private Map<Field, Object> lastSentData = new HashMap<Field, Object>(4, 1F);
    private boolean networkDataChanged = false;

    public TileEntityAdvanced(String tileName)
    {
        super(tileName);
    }

    @Override
    public void update()
    {
        if (this.ticks == 0)
        {
            this.initiate();

            if (this.isNetworkedTile())
            {
                if (this.fieldCacheClient == null || this.fieldCacheServer == null)
                {
                    this.initFieldCache();
                }

                if (this.world != null && this.world.isRemote && this.fieldCacheClient.size() > 0)
                {
                    // Request any networked information from server on first
                    // client update (maybe client just logged on, but server
                    // networkdata didn't change recently)
                    GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
                }
            }
        }

        this.ticks++;

        if (this.isNetworkedTile() && this.ticks % this.getPacketCooldown() == 0)
        {
            if (this.world.isRemote && this.fieldCacheServer.size() > 0)
            {
                PacketDynamic packet = new PacketDynamic(this);
                if (networkDataChanged)
                {
                    GalacticraftCore.packetPipeline.sendToServer(packet);
                }
            } else if (!this.world.isRemote && this.fieldCacheClient.size() > 0)
            {
                PacketDynamic packet = new PacketDynamic(this);
                if (networkDataChanged)
                {
                    GalacticraftCore.packetPipeline.sendToAllAround(packet,
                        new TargetPoint(GCCoreUtil.getDimensionID(this.world), getPos().getX(), getPos().getY(), getPos().getZ(), this.getPacketRange()));
                }
            }
        }
    }

    private void initFieldCache()
    {
        try
        {
            this.fieldCacheClient = new LinkedHashSet<Field>();
            this.fieldCacheServer = new LinkedHashSet<Field>();

            for (Field field : this.getClass().getFields())
            {
                if (field.isAnnotationPresent(NetworkedField.class))
                {
                    NetworkedField f = field.getAnnotation(NetworkedField.class);

                    if (f.targetSide() == Side.CLIENT)
                    {
                        this.fieldCacheClient.add(field);
                    } else
                    {
                        this.fieldCacheServer.add(field);
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public abstract double getPacketRange();

    public abstract int getPacketCooldown();

    public abstract boolean isNetworkedTile();

    public void addExtraNetworkedData(List<Object> networkedList)
    {
    }

    public void readExtraNetworkedData(ByteBuf dataStream)
    {
    }

    public void initiate()
    {
    }

    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        Set<Field> fieldList = null;
        boolean changed = false;

        if (this.fieldCacheClient == null || this.fieldCacheServer == null)
        {
            this.initFieldCache();
        }

        if (this.world.isRemote)
        {
            fieldList = this.fieldCacheServer;
        } else
        {
            fieldList = this.fieldCacheClient;
        }

        for (Field f : fieldList)
        {
            boolean fieldChanged = false;
            try
            {
                Object data = f.get(this);
                Object lastData = lastSentData.get(f);

                if (!NetworkUtil.fuzzyEquals(lastData, data))
                {
                    fieldChanged = true;
                }

                sendData.add(data);

                if (fieldChanged)
                {
                    lastSentData.put(f, NetworkUtil.cloneNetworkedObject(data));
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            changed |= fieldChanged;
        }

        if (changed)
        {
            this.addExtraNetworkedData(sendData);
        } else
        {
            ArrayList<Object> prevSendData = new ArrayList<Object>(sendData);

            this.addExtraNetworkedData(sendData);

            if (!prevSendData.equals(sendData))
            {
                changed = true;
            }
        }

        networkDataChanged = changed;
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        if (this.world == null)
        {
            GalacticraftCore.logger.error("World is NULL! Connot decode packet data!");
            return;
        }

        if (this.fieldCacheClient == null || this.fieldCacheServer == null)
        {
            this.initFieldCache();
        }
        Set<Field> fieldSet = null;

        if (this.world.isRemote)
        {
            fieldSet = this.fieldCacheClient;
        } else
        {
            fieldSet = this.fieldCacheServer;
        }

        for (Field field : fieldSet)
        {
            try
            {
                Object obj = NetworkUtil.getFieldValueFromStream(field, buffer, this.world);
                field.set(this, obj);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        this.readExtraNetworkedData(buffer);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }
}
