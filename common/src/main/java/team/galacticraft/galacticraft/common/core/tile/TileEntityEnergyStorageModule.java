package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.item.IItemElectricBase;
import team.galacticraft.galacticraft.common.api.transmission.NetworkType;
import team.galacticraft.galacticraft.common.api.transmission.tile.IConnector;
import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.blocks.BlockMachineBase;
import team.galacticraft.galacticraft.common.core.energy.item.ItemElectricBase;
import team.galacticraft.galacticraft.common.core.energy.tile.TileBaseUniversalElectricalSource;
import team.galacticraft.galacticraft.common.core.inventory.ContainerEnergyStorageModule;
import team.galacticraft.galacticraft.common.core.inventory.IInventoryDefaults;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public abstract class TileEntityEnergyStorageModule extends TileBaseUniversalElectricalSource implements WorldlyContainer, IInventoryDefaults, IConnector, IMachineSides, MenuProvider
{
    public static class TileEntityEnergyStorageModuleT1 extends TileEntityEnergyStorageModule
    {
        @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.energyStorage)
        public static BlockEntityType<TileEntityEnergyStorageModuleT1> TYPE;

        public TileEntityEnergyStorageModuleT1()
        {
            super(TYPE);
            this.storage.setCapacity(BASE_CAPACITY);
            this.storage.setMaxExtract(300);
        }
    }

    public static class TileEntityEnergyStorageModuleT2 extends TileEntityEnergyStorageModule
    {
        @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.energyStorage)
        public static BlockEntityType<TileEntityEnergyStorageModuleT2> TYPE;

        public TileEntityEnergyStorageModuleT2()
        {
            super(TYPE);
            setTier2();
        }
    }

    private final static float BASE_CAPACITY = 500000;
    private final static float TIER2_CAPACITY = 2500000;

    public final Set<Player> playersUsing = new HashSet<Player>();
    public int scaledEnergyLevel;
    public int lastScaledEnergyLevel;
    private float lastEnergy = 0;

    private boolean initialised = false;

    /*
     * @param tier: 1 = Electric Furnace  2 = Electric Arc Furnace
     */
    public TileEntityEnergyStorageModule(BlockEntityType<?> type)
    {
        super(type);
        this.initialised = true;
        this.inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    }

    protected void setTier2()
    {
        this.storage.setCapacity(TIER2_CAPACITY);
        this.storage.setMaxExtract(1800);
        this.setTierGC(2);
    }

    @Override
    public void tick()
    {
        if (!this.initialised)
        {
            //for version tick compatibility
//            Block b = this.world.getBlockState(this.getPos()).getBlock();
//            if (b == GCBlocks.machineBase)
//            {
//                this.world.setBlockState(this.getPos(), GCBlocks.machineTiered.getDefaultState(), 2);
//            }
//            else if (metadata >= 8)
//            {
//                this.setTier2();
//            }
            this.initialised = true;
        }

        float energy = this.storage.getEnergyStoredGC();
        if (this.getTierGC() == 1 && !this.level.isClientSide)
        {
            if (this.lastEnergy - energy > this.storage.getMaxExtract() - 1)
            {
                //Deplete faster if being drained at maximum output
                this.storage.extractEnergyGC(25, false);
            }
        }
        this.lastEnergy = energy;

        super.tick();

        this.scaledEnergyLevel = (int) Math.floor((this.getEnergyStoredGC() + 49) * 16 / this.getMaxEnergyStoredGC());

        if (this.scaledEnergyLevel != this.lastScaledEnergyLevel)
        {
//            this.world.notifyLightSet(this.getPos());
            level.getChunkSource().getLightEngine().checkBlock(this.getBlockPos());
        }

        if (!this.level.isClientSide)
        {
            this.recharge(this.getInventory().get(0));
            this.discharge(this.getInventory().get(1));
        }

        if (!this.level.isClientSide)
        {
            this.produce();
        }

        this.lastScaledEnergyLevel = this.scaledEnergyLevel;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        if (this.storage.getEnergyStoredGC() > BASE_CAPACITY)
        {
            this.setTier2();
            this.initialised = true;
        }
        else
        {
            this.initialised = false;
        }

        this.readMachineSidesFromNBT(nbt);  //Needed by IMachineSides
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        if (this.tierGC == 1 && this.storage.getEnergyStoredGC() > BASE_CAPACITY)
        {
            this.storage.setEnergyStored(BASE_CAPACITY);
        }

        super.save(nbt);

        this.addMachineSidesToNBT(nbt);  //Needed by IMachineSides

        return nbt;
    }

    @Override
    public int getMaxStackSize()
    {
        return 1;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    public boolean canPlaceItem(int slotID, ItemStack itemstack)
    {
        return ItemElectricBase.isElectricItem(itemstack.getItem());
    }

//    @Override
//    public int[] getAccessibleSlotsFromSide(int slotID)
//    {
//        return new int[] { 0, 1 };
//    }

    @Override
    public boolean canPlaceItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        if (itemstack.getItem() instanceof IItemElectricBase)
        {
            if (slotID == 0)
            {
                return ((IItemElectricBase) itemstack.getItem()).getTransfer(itemstack) > 0;
            }
            else if (slotID == 1)
            {
                return ((IItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) > 0;
            }
        }
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        if (itemstack.getItem() instanceof IItemElectricBase)
        {
            if (slotID == 0)
            {
                return ((IItemElectricBase) itemstack.getItem()).getTransfer(itemstack) <= 0;
            }
            else if (slotID == 1)
            {
                return ((IItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) <= 0 || this.getEnergyStoredGC() >= this.getMaxEnergyStoredGC();
            }
        }

        return false;

    }

    @Override
    public EnumSet<Direction> getElectricalInputDirections()
    {
        return EnumSet.of(getElectricInputDirection());
    }

    @Override
    public EnumSet<Direction> getElectricalOutputDirections()
    {
        return EnumSet.of(getElectricOutputDirection());
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        if (direction == null || type != NetworkType.POWER)
        {
            return false;
        }

        return getElectricalInputDirections().contains(direction) || getElectricalOutputDirections().contains(direction);
    }

    @Override
    public Direction getFront()
    {
        return BlockMachineBase.getFront(this.level.getBlockState(getBlockPos()));
    }

    @Override
    public Direction getElectricInputDirection()
    {
        switch (this.getSide(MachineSide.ELECTRIC_IN))
        {
        case LEFT:
            return getFront().getClockWise();
        case REAR:
            return getFront().getOpposite();
        case TOP:
            return Direction.UP;
        case BOTTOM:
            return Direction.DOWN;
        case RIGHT:
        default:
            return getFront().getCounterClockWise();
        }
    }

    @Override
    public Direction getElectricOutputDirection()
    {
        switch (this.getSide(MachineSide.ELECTRIC_OUT))
        {
        case RIGHT:
            return getFront().getCounterClockWise();
        case REAR:
            return getFront().getOpposite();
        case TOP:
            return Direction.UP;
        case BOTTOM:
            return Direction.DOWN;
        case LEFT:
        default:
            return getFront().getClockWise();
        }
    }

    //------------------
    //Added these methods and field to implement IMachineSides properly 
    //------------------
    @Override
    public MachineSide[] listConfigurableSides()
    {
        return new MachineSide[]{MachineSide.ELECTRIC_IN, MachineSide.ELECTRIC_OUT};
    }

    @Override
    public Face[] listDefaultFaces()
    {
        return new Face[]{Face.RIGHT, Face.LEFT};
    }

    private MachineSidePack[] machineSides;

    @Override
    public synchronized MachineSidePack[] getAllMachineSides()
    {
        if (this.machineSides == null)
        {
            this.initialiseSides();
        }

        return this.machineSides;
    }

    @Override
    public void setupMachineSides(int length)
    {
        this.machineSides = new MachineSidePack[length];
    }

    @Override
    public void onLoad()
    {
        this.clientOnLoad();
    }

    @Override
    public IMachineSidesProperties getConfigurationType()
    {
        return IMachineSidesProperties.TWOFACES_HORIZ;
    }
    //------------------END OF IMachineSides implementation

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerEnergyStorageModule(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.storage_module");
    }
}
