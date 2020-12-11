package team.galacticraft.galacticraft.common.core.energy.tile;

import team.galacticraft.galacticraft.common.api.item.ElectricItemHelper;
import team.galacticraft.galacticraft.common.api.item.IItemElectric;
import team.galacticraft.galacticraft.common.api.transmission.NetworkType;
import team.galacticraft.galacticraft.common.api.transmission.grid.IElectricityNetwork;
import team.galacticraft.galacticraft.common.api.transmission.tile.IConductor;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import team.galacticraft.galacticraft.common.core.energy.EnergyConfigHandler;
import team.galacticraft.galacticraft.common.core.energy.EnergyUtil;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.capabilities.Capability;
import team.galacticraft.galacticraft.common.api.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.EnumSet;

public abstract class TileBaseUniversalElectricalSource extends TileBaseUniversalElectrical
{
    public TileBaseUniversalElectricalSource(BlockEntityType<?> type)
    {
        super(type);
    }

    /*
     * The main function to output energy each tick from a source.
     *
     * The source will attempt to produce into its outputDirections whatever energy
     * it has available, and will reduce its stored energy by the amount which is in fact used.
     *
     * Max output = this.storage.maxExtract.
     *
     * @return The amount of energy that was used.
     */
    public float produce()
    {
        this.storage.maxExtractRemaining = this.storage.maxExtract;
        float produced = this.extractEnergyGC(null, this.produce(false), false);
        this.storage.maxExtractRemaining -= produced;
        if (this.storage.maxExtractRemaining < 0)
        {
            this.storage.maxExtractRemaining = 0;
        }
        return produced;
    }

    /*
     * Function to produce energy each tick into the outputs of a source.
     * If simulate is true, no energy is in fact transferred.
     *
     * Note: even if simulate is false this does NOT reduce the source's own
     * energy storage by the amount produced, that needs to be done elsewhere
     * See this.produce() for an example.
     */
    public float produce(boolean simulate)
    {
        float amountProduced = 0;

        if (!this.level.isClientSide)
        {
            EnumSet<Direction> outputDirections = this.getElectricalOutputDirections();
//            outputDirections.remove(EnumFacing.UNKNOWN);

            BlockVec3 thisVec = new BlockVec3(this);
            for (Direction direction : outputDirections)
            {
                BlockEntity tileAdj = thisVec.getTileEntityOnSide(this.level, direction);

                if (tileAdj != null)
                {
                    float toSend = this.extractEnergyGC(null, Math.min(this.getEnergyStoredGC() - amountProduced, this.getEnergyStoredGC() / outputDirections.size()), true);
                    if (toSend <= 0)
                    {
                        continue;
                    }

                    if (tileAdj instanceof TileBaseConductor && ((TileBaseConductor) tileAdj).canConnect(direction.getOpposite(), NetworkType.POWER))
                    {
                        IElectricityNetwork network = ((IConductor) tileAdj).getNetwork();
                        if (network != null)
                        {
                            amountProduced += (toSend - network.produce(toSend, !simulate, this.tierGC, this));
                        }
                    }
                    else if (tileAdj instanceof TileBaseUniversalElectrical)
                    {
                        amountProduced += ((TileBaseUniversalElectrical) tileAdj).receiveElectricity(direction.getOpposite(), toSend, this.tierGC, !simulate);
                    }
                    else
                    {
                        amountProduced += EnergyUtil.otherModsEnergyTransfer(tileAdj, direction.getOpposite(), toSend, simulate);
                    }
                }
            }
        }

        return amountProduced;
    }

    /**
     * Recharges electric item.
     */
    public void recharge(ItemStack itemStack)
    {
        if (itemStack != null && itemStack.getCount() == 1)
        {
            Item item = itemStack.getItem();
            float maxExtractSave = this.storage.getMaxExtract();
            if (this.tierGC > 1)
            {
                this.storage.setMaxExtract(maxExtractSave * 2.5F);
            }
            float energyToCharge = this.storage.extractEnergyGC(this.storage.getMaxExtract(), true);

            if (item instanceof IItemElectric)
            {
                this.storage.extractEnergyGC(ElectricItemHelper.chargeItem(itemStack, energyToCharge), false);
            }
//            else if (EnergyConfigHandler.isRFAPILoaded() && item instanceof IEnergyContainerItem)
//            {
//                this.storage.extractEnergyGC(((IEnergyContainerItem)item).receiveEnergy(itemStack, (int) (energyToCharge * EnergyConfigHandler.TO_RF_RATIO), false) / EnergyConfigHandler.TO_RF_RATIO, false);
//            }
//            else if (EnergyConfigHandler.isMekanismLoaded() && item instanceof IEnergizedItem && ((IEnergizedItem) item).canReceive(itemStack))
//            {
//                this.storage.extractEnergyGC((float) EnergizedItemManager.charge(itemStack, energyToCharge * EnergyConfigHandler.TO_MEKANISM_RATIO) / EnergyConfigHandler.TO_MEKANISM_RATIO, false);
//            }
//            else if (EnergyConfigHandler.isIndustrialCraft2Loaded())
//            {
//                if (item instanceof ISpecialElectricItem)
//                {
//                    ISpecialElectricItem specialElectricItem = (ISpecialElectricItem) item;
//                    IElectricItemManager manager = specialElectricItem.getManager(itemStack);
//                    double result = manager.charge(itemStack, (double) (energyToCharge * EnergyConfigHandler.TO_IC2_RATIO), this.tierGC + 1, false, false);
//                    float energy = (float) result / EnergyConfigHandler.TO_IC2_RATIO;
//                    this.storage.extractEnergyGC(energy, false);
//                }
//                else if (item instanceof IElectricItem)
//                {
//                    double result = ElectricItem.manager.charge(itemStack, (double) (energyToCharge * EnergyConfigHandler.TO_IC2_RATIO), this.tierGC + 1, false, false);
//                    float energy = (float) result / EnergyConfigHandler.TO_IC2_RATIO;
//                    this.storage.extractEnergyGC(energy, false);
//                }
//            }
            //			else if (GCCoreCompatibilityManager.isTELoaded() && itemStack.getItem() instanceof IEnergyContainerItem)
            //			{
            //				int accepted = ((IEnergyContainerItem) itemStack.getItem()).receiveEnergy(itemStack, (int) Math.floor(this.getProvide(EnumFacing.UNKNOWN) * EnergyConfigHandler.TO_TE_RATIO), false);
            //				this.provideElectricity(accepted * EnergyConfigHandler.TE_RATIO, true);
            //			}

            if (this.tierGC > 1)
            {
                this.storage.setMaxExtract(maxExtractSave);
            }
        }
    }

    //    @Annotations.RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergyEmitter", modID = CompatibilityManager.modidIC2)
//    public boolean emitsEnergyTo(IEnergyAcceptor receiver, Direction direction)
//    {
//        if (this.tileEntityInvalid) return false;
//
//        //Don't add connection to IC2 grid if it's a Galacticraft tile
//        if (receiver instanceof IElectrical || receiver instanceof IConductor || !(receiver instanceof IEnergyTile))
//        {
//            return false;
//        }
//
//        return this.getElectricalOutputDirections().contains(direction);
//    }
//
//    @Annotations.RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySource", modID = CompatibilityManager.modidIC2)
//    public double getOfferedEnergy()
//    {
//        if (EnergyConfigHandler.disableIC2Output)
//        {
//            return 0.0;
//        }
//
//        return this.getProvide(null) * EnergyConfigHandler.TO_IC2_RATIO;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySource", modID = CompatibilityManager.modidIC2)
//    public void drawEnergy(double amount)
//    {
//        if (EnergyConfigHandler.disableIC2Output)
//        {
//            return;
//        }
//
//        this.storage.extractEnergyGC((float) amount / EnergyConfigHandler.TO_IC2_RATIO, false);
//    }
//
//    @Annotations.RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySource", modID = CompatibilityManager.modidIC2)
//    public int getSourceTier()
//    {
//        return this.tierGC + 1;
//    }
//
//    @Override
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyOutputter", modID = CompatibilityManager.modidMekanism)
    public boolean canOutputEnergy(Direction side)
    {
        return this.getElectricalOutputDirections().contains(side);
    }
//
//    @Override
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyOutputter", modID = CompatibilityManager.modidMekanism)
//    public double pullEnergy(Direction side, double amount, boolean simulate)
//    {
//        if (this.canOutputEnergy(EnvType))
//        {
//            float amountGC = (float) amount / EnergyConfigHandler.TO_MEKANISM_RATIO;
//            return this.storage.extractEnergyGC(amountGC, simulate) * EnergyConfigHandler.TO_MEKANISM_RATIO;
//        }
//        return 0D;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyStorage", modID = CompatibilityManager.modidMekanism)
//    public double getEnergy()
//    {
//        return this.storage.getEnergyStoredGC() * EnergyConfigHandler.TO_MEKANISM_RATIO;
//    }
//
//    @Override
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyStorage", modID = CompatibilityManager.modidMekanism)
//    public double getMaxEnergy()
//    {
//        return this.storage.getCapacityGC() * EnergyConfigHandler.TO_MEKANISM_RATIO;
//    }
//
//    @Override
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyStorage", modID = CompatibilityManager.modidMekanism)
//    public void setEnergy(double energy)
//    {
//        this.storage.setEnergyStored((float) energy / EnergyConfigHandler.TO_MEKANISM_RATIO);
//    }

//    @Override
//    public boolean hasCapability(Capability<?> cap, Direction side)
//    {
//        if (cap == CapabilityEnergy.ENERGY && this.canOutputEnergy(EnvType)) return true;
//        if (cap == EnergyUtil.mekCableOutput || cap == EnergyUtil.mekEnergyStorage)
//        {
//            return this.canOutputEnergy(EnvType);
//        }
//        if (EnergyConfigHandler.isBuildcraftLoaded() && cap == MjAPI.CAP_CONNECTOR && this.canOutputEnergy(EnvType))
//        {
//            return true;
//        }
//        return super.hasCapability(cap, EnvType);
//    }

    private LazyOptional<IEnergyStorage> holder = null;

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityEnergy.ENERGY)
        {
            if (holder == null)
            {
                holder = LazyOptional.of(new NonNullSupplier<IEnergyStorage>()
                {
                    @NotNull
                    @Override
                    public IEnergyStorage get()
                    {
                        return new ForgeEmitter(TileBaseUniversalElectricalSource.this);
                    }
                });
            }
            return holder.cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public float getProvide(Direction direction)
    {
        if (direction == null && EnergyConfigHandler.isIndustrialCraft2Loaded())
        {
            BlockEntity tile = new BlockVec3(this).getTileEntityOnSide(this.level, this.getElectricOutputDirection());
            if (tile instanceof IConductor)
            {
                //No power provide to IC2 mod if it's a Galacticraft wire on the output.  Galacticraft network will provide the power.
                return 0.0F;
            }
            return this.storage.extractEnergyGC(Float.MAX_VALUE, true);
        }

        if (this.getElectricalOutputDirections().contains(direction))
        {
            return this.storage.extractEnergyGC(Float.MAX_VALUE, true);
        }

        return 0F;
    }

    public Direction getElectricOutputDirection()
    {
        return null;
    }

//    @Annotations.RuntimeInterface(clazz = "cofh.redstoneflux.api.IEnergyProvider", modID = "")
//    public int extractEnergy(Direction from, int maxExtract, boolean simulate)
//    {
//        if (EnergyConfigHandler.disableRFOutput)
//        {
//            return 0;
//        }
//
//        if (!this.getElectricalOutputDirections().contains(from))
//        {
//            return 0;
//        }
//
//        return MathHelper.floor(this.storage.extractEnergyGC(maxExtract / EnergyConfigHandler.TO_RF_RATIO, !simulate) * EnergyConfigHandler.TO_RF_RATIO);
//    }

    private static class ForgeEmitter implements net.minecraftforge.energy.IEnergyStorage
    {
        private final TileBaseUniversalElectrical tile;

        public ForgeEmitter(TileBaseUniversalElectrical tileElectrical)
        {
            this.tile = tileElectrical;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate)
        {
            return 0;
        }

        @Override
        public boolean canReceive()
        {
            return false;
        }

        @Override
        public int getEnergyStored()
        {
            if (EnergyConfigHandler.disableFEOutput.get())
            {
                return 0;
            }

            return Mth.floor(tile.getEnergyStoredGC() / EnergyConfigHandler.RF_RATIO.get());
        }

        @Override
        public int getMaxEnergyStored()
        {
            if (EnergyConfigHandler.disableFEOutput.get())
            {
                return 0;
            }

            return Mth.floor(tile.getMaxEnergyStoredGC() / EnergyConfigHandler.RF_RATIO.get());
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate)
        {
            if (!canExtract())
            {
                return 0;
            }

            return Mth.floor(tile.storage.extractEnergyGC(maxExtract / EnergyConfigHandler.TO_RF_RATIO, !simulate) * EnergyConfigHandler.TO_RF_RATIO);
        }

        @Override
        public boolean canExtract()
        {
            return !EnergyConfigHandler.disableFEOutput.get();
        }
    }
}
