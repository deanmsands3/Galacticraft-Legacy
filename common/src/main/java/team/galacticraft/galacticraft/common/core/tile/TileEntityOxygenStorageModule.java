package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.item.IItemOxygenSupply;
import team.galacticraft.galacticraft.core.GCBlockNames;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.blocks.BlockMachineBase;
import team.galacticraft.galacticraft.core.inventory.ContainerOxygenStorageModule;
import team.galacticraft.galacticraft.core.inventory.IInventoryDefaults;
import team.galacticraft.galacticraft.core.util.FluidUtil;
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
import net.minecraft.world.level.material.Fluid;
import me.shedaniel.architectury.fluid.FluidStack;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;
import net.minecraftforge.registries.ObjectHolder;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class TileEntityOxygenStorageModule extends TileEntityOxygen implements IInventoryDefaults, WorldlyContainer, IMachineSides, MenuProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.oxygenStorageModule)
    public static BlockEntityType<TileEntityOxygenStorageModule> TYPE;

    public final Set<Player> playersUsing = new HashSet<Player>();
    public int scaledOxygenLevel;
    private int lastScaledOxygenLevel;

    public static final int OUTPUT_PER_TICK = 500;
    public static final int OXYGEN_CAPACITY = 60000;

    public TileEntityOxygenStorageModule()
    {
        super(TYPE, OXYGEN_CAPACITY, 40);
        this.storage.setCapacity(0);
        this.storage.setMaxExtract(0);
        inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    public void tick()
    {
        if (!this.level.isClientSide)
        {
            ItemStack oxygenItemStack = this.getItem(0);
            if (oxygenItemStack != null && oxygenItemStack.getItem() instanceof IItemOxygenSupply)
            {
                IItemOxygenSupply oxygenItem = (IItemOxygenSupply) oxygenItemStack.getItem();
                int oxygenDraw = (int) Math.floor(Math.min(this.oxygenPerTick * 2.5F, this.getMaxOxygenStored() - this.getOxygenStored()));
                this.setOxygenStored(getOxygenStored() + oxygenItem.discharge(oxygenItemStack, oxygenDraw));
                if (this.getOxygenStored() > this.getMaxOxygenStored())
                {
                    this.setOxygenStored(this.getOxygenStored());
                }
            }
        }

        super.tick();

        this.scaledOxygenLevel = this.getScaledOxygenLevel(16);

        if (this.scaledOxygenLevel != this.lastScaledOxygenLevel)
        {
            this.level.getChunkSource().getLightEngine().checkBlock(this.getBlockPos());
//            this.world.notifyLightSet(this.getPos());
        }

        this.lastScaledOxygenLevel = this.scaledOxygenLevel;

        this.produceOxygen(getFront().getClockWise().getOpposite());

        // if (!this.world.isRemote)
        // {
        // int gasToSend = Math.min(this.storedOxygen,
        // GCCoreTileEntityOxygenStorageModule.OUTPUT_PER_TICK);
        // GasStack toSend = new GasStack(GalacticraftCore.gasOxygen,
        // gasToSend);
        // this.storedOxygen -= GasTransmission.emitGasToNetwork(toSend, this,
        // this.getOxygenOutputDirection());
        //
        // Vector3 thisVec = new Vector3(this);
        // TileEntity tileEntity =
        // thisVec.modifyPositionFromSide(this.getOxygenOutputDirection()).getTileEntity(this.world);
        //
        // if (tileEntity instanceof IGasAcceptor)
        // {
        // if (((IGasAcceptor)
        // tileEntity).canReceiveGas(this.getOxygenInputDirection(),
        // GalacticraftCore.gasOxygen))
        // {
        // double sendingGas = 0;
        //
        // if (this.storedOxygen >=
        // GCCoreTileEntityOxygenStorageModule.OUTPUT_PER_TICK)
        // {
        // sendingGas = GCCoreTileEntityOxygenStorageModule.OUTPUT_PER_TICK;
        // }
        // else
        // {
        // sendingGas = this.storedOxygen;
        // }
        //
        // this.storedOxygen -= sendingGas - ((IGasAcceptor)
        // tileEntity).receiveGas(new GasStack(GalacticraftCore.gasOxygen, (int)
        // Math.floor(sendingGas)));
        // }
        // }
        // }

        this.lastScaledOxygenLevel = this.scaledOxygenLevel;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);

        this.readMachineSidesFromNBT(nbt);  //Needed by IMachineSides
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        this.addMachineSidesToNBT(nbt);  //Needed by IMachineSides
        return nbt;
    }

    @Override
    public EnumSet<Direction> getElectricalInputDirections()
    {
        return EnumSet.noneOf(Direction.class);
    }

    @Override
    public EnumSet<Direction> getElectricalOutputDirections()
    {
        return EnumSet.noneOf(Direction.class);
    }

    @Override
    public boolean shouldPullEnergy()
    {
        return false;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return false;
    }

    @Override
    public Direction getElectricInputDirection()
    {
        return null;
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean shouldUseOxygen()
    {
        return false;
    }

    @Override
    public int getOxygenProvide(Direction direction)
    {
        return this.getOxygenOutputDirections().contains(direction) ? Math.min(TileEntityOxygenStorageModule.OUTPUT_PER_TICK, this.getOxygenStored()) : 0;
    }

    @Override
    public Direction getFront()
    {
        return BlockMachineBase.getFront(this.level.getBlockState(getBlockPos()));
    }

    @Override
    public boolean canPlaceItem(int slotID, ItemStack itemstack)
    {
        return slotID == 0 && itemstack != null && itemstack.getItem() instanceof IItemOxygenSupply;
    }

    //ISidedInventory
    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        if (slotID == 0 && this.canPlaceItem(slotID, itemstack))
        {
            return itemstack.getDamageValue() < itemstack.getItem().getMaxDamage();
        }
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        if (slotID == 0 && !itemstack.isEmpty())
        {
            return FluidUtil.isEmptyContainer(itemstack);
        }
        return false;
    }

    //IFluidHandler methods - to allow this to accept Liquid Oxygen
    @Override
    public boolean canDrain(Direction from, Fluid fluid)
    {
        return super.canDrain(from, fluid);
    }

    @Override
    public FluidStack drain(Direction from, FluidStack resource, ActionType action)
    {
        return super.drain(from, resource, action);
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, ActionType action)
    {
        return super.drain(from, maxDrain, action);
    }

    @Override
    public boolean canFill(Direction from, Fluid fluid)
    {
//        if (from.ordinal() == this.getBlockMetadata() - BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA + 2 && GalacticraftCore.isPlanetsLoaded)
//        {
//            //Can fill with LOX only
//            return fluid != null && fluid.getName().equals(AsteroidsModule.fluidLiquidOxygen.getName());
//        }

        return super.canFill(from, fluid);
    }

    @Override
    public int fill(Direction from, FluidStack resource, ActionType action)
    {
//        int used = 0;
//
//        if (resource != null && this.canFill(from, resource.getFluid()))
//        {
//            used = (int) (this.receiveOxygen((int) Math.floor(resource.amount / Constants.LOX_GAS_RATIO), action) * Constants.LOX_GAS_RATIO);
//        }

        return super.fill(from, resource, action);
    }

//    @Override
//    public FluidTankInfo[] getTankInfo(Direction from)
//    {
////        FluidTankInfo[] tankInfo = new FluidTankInfo[] {};
////        int metaside = this.getBlockMetadata() - BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA + 2;
////        int LogicalSide = from.ordinal();
////
////        if (metaside == LogicalSide && GalacticraftCore.isPlanetsLoaded)
////        {
////            tankInfo = new FluidTankInfo[] { new FluidTankInfo(new FluidStack(AsteroidsModule.fluidLiquidOxygen, (int) (this.getOxygenStored() * Constants.LOX_GAS_RATIO)), (int) (OXYGEN_CAPACITY * Constants.LOX_GAS_RATIO)) };
////        }
////        return tankInfo;
//        return super.getTankInfo(from);
//    }

    @Override
    public EnumSet<Direction> getOxygenInputDirections()
    {
        Direction dir;
        switch (this.getSide(MachineSide.ELECTRIC_IN))
        {
        case REAR:
            dir = getFront().getOpposite();
            break;
        case TOP:
            dir = Direction.UP;
            break;
        case BOTTOM:
            dir = Direction.DOWN;
            break;
        case RIGHT:
            dir = getFront().getCounterClockWise();
            break;
        case LEFT:
        default:
            dir = getFront().getClockWise();
        }
        return EnumSet.of(dir);
    }

    @Override
    public EnumSet<Direction> getOxygenOutputDirections()
    {
        Direction dir;
        switch (this.getSide(MachineSide.PIPE_OUT))
        {
        case REAR:
            dir = getFront().getOpposite();
            break;
        case TOP:
            dir = Direction.UP;
            break;
        case BOTTOM:
            dir = Direction.DOWN;
            break;
        case LEFT:
            dir = getFront().getClockWise();
            break;
        case RIGHT:
        default:
            dir = getFront().getCounterClockWise();
        }
        return EnumSet.of(dir);
    }

    //------------------
    //Added these methods and field to implement IMachineSides properly 
    //------------------
    @Override
    public MachineSide[] listConfigurableSides()
    {
        //Have to use Electric_In for compatibility with other BlockMachine2, as all use same blockstate
        return new MachineSide[]{MachineSide.ELECTRIC_IN, MachineSide.PIPE_OUT};
    }

    @Override
    public Face[] listDefaultFaces()
    {
        return new Face[]{Face.LEFT, Face.RIGHT};
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
        return new ContainerOxygenStorageModule(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.oxygen_storage");
    }
}
