package team.galacticraft.galacticraft.common.core.tile;

import io.netty.buffer.ByteBuf;
import team.galacticraft.galacticraft.common.core.Annotations.NetworkedField;
import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraftforge.registries.ObjectHolder;

import java.util.List;

public class TileEntityFallenMeteor extends TileEntityAdvanced
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.fallenMeteor)
    public static BlockEntityType<TileEntityFallenMeteor> TYPE;

    public static final int MAX_HEAT_LEVEL = 5000;
    @NetworkedField(targetSide = EnvType.CLIENT)
    public int heatLevel = TileEntityFallenMeteor.MAX_HEAT_LEVEL;
    private boolean sentOnePacket = false;

    public TileEntityFallenMeteor()
    {
        super(TYPE);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.level.isClientSide && this.heatLevel > 0)
        {
            this.heatLevel--;
        }
    }

    public int getHeatLevel()
    {
        return this.heatLevel;
    }

    public void setHeatLevel(int heatLevel)
    {
        this.heatLevel = heatLevel;
    }

    public float getScaledHeatLevel()
    {
        return (float) this.heatLevel / TileEntityFallenMeteor.MAX_HEAT_LEVEL;
    }

    @Override
    public void readExtraNetworkedData(ByteBuf dataStream)
    {
        if (this.level.isClientSide)
        {
//            this.world.notifyLightSet(this.getPos()); TODO Lighting
            level.getChunkSource().getLightEngine().checkBlock(this.getBlockPos());
        }
    }

    @Override
    public void addExtraNetworkedData(List<Object> networkedList)
    {
        this.sentOnePacket = true;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.heatLevel = nbt.getInt("MeteorHeatLevel");
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putInt("MeteorHeatLevel", this.heatLevel);
        return nbt;
    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        return this.save(new CompoundTag());
    }

    @Override
    public double getPacketRange()
    {
        return 50;
    }

    @Override
    public int getPacketCooldown()
    {
        return this.sentOnePacket ? 100 : 1;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return true;
    }
}
