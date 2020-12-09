package team.galacticraft.galacticraft.common.core.tile;

import io.netty.buffer.ByteBuf;
import team.galacticraft.galacticraft.core.Annotations.NetworkedField;
import team.galacticraft.galacticraft.core.GalacticraftCore;
import team.galacticraft.galacticraft.core.network.IPacketReceiver;
import team.galacticraft.galacticraft.core.network.NetworkUtil;
import team.galacticraft.galacticraft.core.network.PacketDynamic;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import team.galacticraft.galacticraft.core.util.GCLog;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;

import java.lang.reflect.Field;
import java.util.*;

public abstract class TileEntityAdvanced extends TileEntityInventory implements IPacketReceiver, TickableBlockEntity
{
    public int ticks = 0;
    private LinkedHashSet<Field> fieldCacheClient;
    private LinkedHashSet<Field> fieldCacheServer;
    private final Map<Field, Object> lastSentData = new HashMap<Field, Object>(4, 1F);
    private boolean networkDataChanged = false;

    public TileEntityAdvanced(BlockEntityType<?> type)
    {
        super(type);
    }

    @Override
    public void tick()
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

                if (this.level != null && this.level.isClientSide && this.fieldCacheClient.size() > 0)
                {
                    //Request any networked information from server on first client tick (maybe client just logged on, but server networkdata didn't change recently)
                    GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
                }
            }
        }

        this.ticks++;

        if (this.isNetworkedTile() && this.ticks % this.getPacketCooldown() == 0)
        {
            if (this.level.isClientSide && this.fieldCacheServer.size() > 0)
            {
                PacketDynamic packet = new PacketDynamic(this);
                if (networkDataChanged)
                {
                    GalacticraftCore.packetPipeline.sendToServer(packet);
                }
            }
            else if (!this.level.isClientSide && this.fieldCacheClient.size() > 0)
            {
                PacketDynamic packet = new PacketDynamic(this);
                if (networkDataChanged)
                {
                    GalacticraftCore.packetPipeline.sendToAllAround(packet, new TargetPoint(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), this.getPacketRange(), GCCoreUtil.getDimensionType(this.level)));
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

                    if (f.targetSide() == LogicalSide.CLIENT)
                    {
                        this.fieldCacheClient.add(field);
                    }
                    else
                    {
                        this.fieldCacheServer.add(field);
                    }
                }
            }
        }
        catch (Exception e)
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

        if (this.level.isClientSide)
        {
            fieldList = this.fieldCacheServer;
        }
        else
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
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            changed |= fieldChanged;
        }

        if (changed)
        {
            this.addExtraNetworkedData(sendData);
        }
        else
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
        if (this.level == null)
        {
            GCLog.severe("World is NULL! Connot decode packet data!");
            return;
        }

        if (this.fieldCacheClient == null || this.fieldCacheServer == null)
        {
            this.initFieldCache();
        }

//        if (this.world.isRemote && this.fieldCacheClient.size() == 0)
//        {
//            return;
//        }
//        else if (!this.world.isRemote && this.fieldCacheServer.size() == 0)
//        {
//            return;
//        }

        Set<Field> fieldSet = null;

        if (this.level.isClientSide)
        {
            fieldSet = this.fieldCacheClient;
        }
        else
        {
            fieldSet = this.fieldCacheServer;
        }

        for (Field field : fieldSet)
        {
            try
            {
                Object obj = NetworkUtil.getFieldValueFromStream(field, buffer, this.level);
                field.set(this, obj);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        this.readExtraNetworkedData(buffer);
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }
}