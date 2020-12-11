package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.recipe.CircuitFabricatorRecipes;
import team.galacticraft.galacticraft.common.api.world.IZeroGDimension;
import team.galacticraft.galacticraft.common.core.Annotations.NetworkedField;
import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GCItems;
import team.galacticraft.galacticraft.common.core.energy.item.ItemElectricBase;
import team.galacticraft.galacticraft.common.core.energy.tile.TileBaseElectricBlockWithInventory;
import team.galacticraft.galacticraft.common.core.inventory.ContainerCircuitFabricator;
import team.galacticraft.galacticraft.common.core.util.ConfigManagerCore;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;

public class TileEntityCircuitFabricator extends TileBaseElectricBlockWithInventory implements WorldlyContainer, IMachineSides, MenuProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.circuitFabricator)
    public static BlockEntityType<TileEntityCircuitFabricator> TYPE;

    public static final int PROCESS_TIME_REQUIRED = 300;
    @NetworkedField(targetSide = EnvType.CLIENT)
    public int processTicks = 0;
    private ItemStack producingStack = ItemStack.EMPTY;
    private long ticks;

    public TileEntityCircuitFabricator()
    {
        super(TYPE);
        this.storage.setMaxExtract(ConfigManagerCore.hardMode.get() ? 40 : 20);
        this.inventory = NonNullList.withSize(7, ItemStack.EMPTY);
    }

    @Override
    public void tick()
    {
        super.tick();

        this.updateInput();

        if (!this.level.isClientSide)
        {
            boolean updateInv = false;

            if (this.hasEnoughEnergyToRun)
            {
                if (this.canCompress())
                {
                    ++this.processTicks;

                    if (this.processTicks >= this.getProcessTimeRequired())
                    {
                        this.level.playSound(null, this.getBlockPos(), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.3F, this.level.random.nextFloat() * 0.1F + 0.9F);
                        this.processTicks = 0;
                        this.compressItems();
                        updateInv = true;
                    }
                }
                else
                {
                    this.processTicks = 0;
                }
            }
            else
            {
                this.processTicks = 0;
            }

            if (updateInv)
            {
                this.setChanged();
            }
        }

        this.ticks++;
    }

    public int getProcessTimeRequired()
    {
        return TileEntityCircuitFabricator.PROCESS_TIME_REQUIRED * 2 / (1 + this.poweredByTierGC);
    }

    public void updateInput()
    {
        this.producingStack = CircuitFabricatorRecipes.getOutputForInput(this.getInventory().subList(1, 6));
    }

    private boolean canCompress()
    {
        if (this.producingStack.isEmpty())
        {
            return false;
        }
        if (this.getInventory().get(6).isEmpty())
        {
            return true;
        }
        if (!this.getInventory().get(6).isEmpty() && !this.getInventory().get(6).sameItem(this.producingStack))
        {
            return false;
        }
        int result = this.getInventory().get(6).isEmpty() ? 0 : this.getInventory().get(6).getCount() + this.producingStack.getCount();
        return result <= this.getMaxStackSize() && result <= this.producingStack.getMaxStackSize();
    }

    public void compressItems()
    {
        if (this.canCompress())
        {
            ItemStack resultItemStack = this.producingStack.copy();
            if (this.level.getDimension() instanceof IZeroGDimension)
            {
                if (resultItemStack.getItem() == GCItems.compressedWaferBasic)
                {
                    resultItemStack.setCount(5);
                }
                else if (resultItemStack.getItem() == GCItems.compressedWaferSolar)
                {
                    resultItemStack.setCount(15);
                }
                else
                {
                    resultItemStack.setCount(resultItemStack.getCount() * 2);
                }
            }

            if (this.getInventory().get(6).isEmpty())
            {
                this.getInventory().set(6, resultItemStack);
            }
            else if (this.getInventory().get(6).sameItem(resultItemStack))
            {
                if (this.getInventory().get(6).getCount() + resultItemStack.getCount() > 64)
                {
                    resultItemStack.setCount(this.getInventory().get(6).getCount() + resultItemStack.getCount() - 64);
                    GCCoreUtil.spawnItem(this.level, this.getBlockPos(), resultItemStack);
                    this.getInventory().get(6).setCount(64);
                }
                else
                {
                    this.getInventory().get(6).grow(resultItemStack.getCount());
                }
            }
        }

        for (int i = 1; i < 6; i++)
        {
            this.removeItem(i, 1);
        }
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.processTicks = nbt.getInt("smeltingTicks");
        this.readMachineSidesFromNBT(nbt);  //Needed by IMachineSides
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putInt("smeltingTicks", this.processTicks);
        this.addMachineSidesToNBT(nbt);  //Needed by IMachineSides
        return nbt;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return false;
//    }

    @Override
    public boolean canPlaceItem(int slotID, ItemStack itemStack)
    {
        if (slotID == 0)
        {
            return itemStack != null && ItemElectricBase.isElectricItem(itemStack.getItem());
        }

        if (slotID > 5)
        {
            return false;
        }

        ArrayList<ItemStack> list = CircuitFabricatorRecipes.slotValidItems.get(slotID - 1);

        for (ItemStack test : list)
        {
            if (test.sameItem(itemStack))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        if (side == Direction.DOWN)
        {
            return new int[]{6};
        }

        //Offer whichever silicon slot has less silicon
        boolean siliconFlag = !this.getInventory().get(2).isEmpty() && (this.getInventory().get(3).isEmpty() || this.getInventory().get(3).getCount() < this.getInventory().get(2).getCount());
        return siliconFlag ? new int[]{0, 1, 3, 4, 5} : new int[]{0, 1, 2, 4, 5};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slotID, ItemStack par2ItemStack, Direction par3)
    {
        return slotID < 6 && this.canPlaceItem(slotID, par2ItemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slotID, ItemStack par2ItemStack, Direction par3)
    {
        return slotID == 6;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.processTicks > 0;
    }

    @Override
    public Direction getFront()
    {
        return Direction.NORTH; // TODO Fix circuit fabricator direction
//        return BlockMachineBase.getFront(this.world.getBlockState(getPos()));
    }

    @Override
    public Direction getElectricInputDirection()
    {
        switch (this.getSide(MachineSide.ELECTRIC_IN))
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
        return new MachineSide[]{MachineSide.ELECTRIC_IN};
    }

    @Override
    public Face[] listDefaultFaces()
    {
        return new Face[]{Face.LEFT};
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
        return new ContainerCircuitFabricator(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.circuit_fabricator");
    }
}
