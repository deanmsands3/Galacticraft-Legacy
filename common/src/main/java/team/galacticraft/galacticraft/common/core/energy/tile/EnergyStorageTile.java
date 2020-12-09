package team.galacticraft.galacticraft.common.core.energy.tile;

import team.galacticraft.galacticraft.common.api.power.EnergySource;
import team.galacticraft.galacticraft.common.api.power.EnergySource.EnergySourceAdjacent;
import team.galacticraft.galacticraft.common.api.power.IEnergyHandlerGC;
import team.galacticraft.galacticraft.common.api.transmission.NetworkType;
import team.galacticraft.galacticraft.common.api.transmission.tile.IElectrical;
import team.galacticraft.galacticraft.core.Annotations.NetworkedField;
import team.galacticraft.galacticraft.core.tile.ReceiverMode;
import team.galacticraft.galacticraft.core.tile.TileEntityAdvanced;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.LogicalSide;

public abstract class EnergyStorageTile extends TileEntityAdvanced implements IEnergyHandlerGC, IElectrical
{
    public static final float STANDARD_CAPACITY = 16000F;

    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public EnergyStorage storage = new EnergyStorage(STANDARD_CAPACITY, 10);
    public int tierGC = 1;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int poweredByTierGC = 1;

    public EnergyStorageTile(BlockEntityType<?> type)
    {
        super(type);
    }

    @Override
    public void load(CompoundTag nbt)
    {

        super.load(nbt);
        this.storage.readFromNBT(nbt);
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        this.storage.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        return this.save(new CompoundTag());
    }

    public abstract ReceiverMode getModeFromDirection(Direction direction);

    @Override
    public float receiveEnergyGC(EnergySource from, float amount, boolean simulate)
    {
        return this.storage.receiveEnergyGC(amount, simulate);
    }

    @Override
    public float extractEnergyGC(EnergySource from, float amount, boolean simulate)
    {
        return this.storage.extractEnergyGC(amount, simulate);
    }

    @Override
    public boolean nodeAvailable(EnergySource from)
    {
        if (!(from instanceof EnergySourceAdjacent))
        {
            return false;
        }

        return this.getModeFromDirection(((EnergySourceAdjacent) from).direction) != ReceiverMode.UNDEFINED;
    }

    @Override
    public float getEnergyStoredGC(EnergySource from)
    {
        return this.storage.getEnergyStoredGC();
    }

    public float getEnergyStoredGC()
    {
        return this.storage.getEnergyStoredGC();
    }

    @Override
    public float getMaxEnergyStoredGC(EnergySource from)
    {
        return this.storage.getCapacityGC();
    }

    public float getMaxEnergyStoredGC()
    {
        return this.storage.getCapacityGC();
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        return false;
    }

    //Five methods for compatibility with basic electricity
    @Override
    public float receiveElectricity(Direction from, float receive, int tier, boolean doReceive)
    {
        this.poweredByTierGC = (tier < 6) ? tier : 6;
        return this.storage.receiveEnergyGC(receive, !doReceive);
    }

    @Override
    public float provideElectricity(Direction from, float request, boolean doProvide)
    {
        return this.storage.extractEnergyGC(request, !doProvide);
    }

    @Override
    public float getRequest(Direction direction)
    {
        return Math.min(this.storage.getCapacityGC() - this.storage.getEnergyStoredGC(), this.storage.getMaxReceive());
    }

    @Override
    public float getProvide(Direction direction)
    {
        return 0;
    }

    @Override
    public int getTierGC()
    {
        return this.tierGC;
    }

    public void setTierGC(int newTier)
    {
        this.tierGC = newTier;
    }
}