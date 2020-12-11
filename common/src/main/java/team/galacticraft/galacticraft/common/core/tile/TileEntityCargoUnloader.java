package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.entity.ICargoEntity;
import team.galacticraft.galacticraft.common.api.entity.ICargoEntity.EnumCargoLoadingState;
import team.galacticraft.galacticraft.common.api.entity.ICargoEntity.RemovalResult;
import team.galacticraft.galacticraft.common.api.tile.ILandingPadAttachable;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import team.galacticraft.galacticraft.common.core.Annotations.NetworkedField;
import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.blocks.BlockCargoLoader;
import team.galacticraft.galacticraft.common.core.energy.item.ItemElectricBase;
import team.galacticraft.galacticraft.common.core.inventory.ContainerCargoBase.ContainerCargoUnloader;
import team.galacticraft.galacticraft.common.core.util.RecipeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityCargoUnloader extends TileEntityCargoBase implements ILandingPadAttachable, MenuProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.cargoUnloader)
    public static BlockEntityType<TileEntityCargoUnloader> TYPE;

    @NetworkedField(targetSide = EnvType.CLIENT)
    public boolean targetEmpty;
    @NetworkedField(targetSide = EnvType.CLIENT)
    public boolean targetNoInventory;
    @NetworkedField(targetSide = EnvType.CLIENT)
    public boolean noTarget;

    public ICargoEntity attachedFuelable;

    public TileEntityCargoUnloader()
    {
        super(TYPE);
        this.storage.setMaxExtract(45);
        this.inventory = NonNullList.withSize(15, ItemStack.EMPTY);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.level.isClientSide)
        {
            if (this.ticks % 100 == 0)
            {
                this.checkForCargoEntity();
            }

            if (this.attachedFuelable != null)
            {
                this.noTarget = false;
                RemovalResult result = this.attachedFuelable.removeCargo(false);

                if (!result.resultStack.isEmpty())
                {
                    this.targetEmpty = false;

                    EnumCargoLoadingState state = this.addCargo(result.resultStack, false);

                    this.targetEmpty = state == EnumCargoLoadingState.EMPTY;

                    if (this.ticks % (this.poweredByTierGC > 1 ? 9 : 15) == 0 && state == EnumCargoLoadingState.SUCCESS && !this.disabled && this.hasEnoughEnergyToRun)
                    {
                        this.addCargo(this.attachedFuelable.removeCargo(true).resultStack, true);
                    }
                }
                else
                {
                    this.targetNoInventory = result.resultState == EnumCargoLoadingState.NOINVENTORY;
                    this.noTarget = result.resultState == EnumCargoLoadingState.NOTARGET;
                    this.targetEmpty = true;
                }
            }
            else
            {
                this.noTarget = true;
            }
        }
    }

    public void checkForCargoEntity()
    {
        boolean foundFuelable = false;

        BlockVec3 thisVec = new BlockVec3(this);
        for (final Direction dir : Direction.values())
        {
            final BlockEntity pad = thisVec.getTileEntityOnSide(this.level, dir);

            if (pad != null && pad instanceof TileEntityFake)
            {
                final BlockEntity mainTile = ((TileEntityFake) pad).getMainBlockTile();

                if (mainTile instanceof ICargoEntity)
                {
                    this.attachedFuelable = (ICargoEntity) mainTile;
                    foundFuelable = true;
                    break;
                }
            }
            else if (pad != null && pad instanceof ICargoEntity)
            {
                this.attachedFuelable = (ICargoEntity) pad;
                foundFuelable = true;
                break;
            }
        }

        if (!foundFuelable)
        {
            this.attachedFuelable = null;
        }
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }


//    @Override
//    public boolean hasCustomName()
//    {
//        return false;
//    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return side != this.getElectricInputDirection() ? new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14} : new int[]{};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        if (side != this.getElectricInputDirection())
        {
            if (slotID == 0)
            {
                return ItemElectricBase.isElectricItemEmpty(itemstack);
            }
            else
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canPlaceItem(int slotID, ItemStack itemstack)
    {
        if (slotID == 0)
        {
            return ItemElectricBase.isElectricItem(itemstack.getItem());
        }
        else
        {
            return true;
        }
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return !this.getDisabled(0);
    }

    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
    {
        int count = 1;

        for (count = 1; count < this.getInventory().size(); count++)
        {
            ItemStack stackAt = this.getInventory().get(count);

            if (RecipeUtil.stacksMatch(stack, stackAt) && stackAt.getCount() < stackAt.getMaxStackSize())
            {
                if (stackAt.getCount() + stack.getCount() <= stackAt.getMaxStackSize())
                {
                    if (doAdd)
                    {
                        stackAt.grow(stack.getCount());
                        this.setChanged();
                    }

                    return EnumCargoLoadingState.SUCCESS;
                }
                else
                {
                    //Part of the stack can fill this slot but there will be some left over
                    int origSize = stackAt.getCount();
                    int surplus = origSize + stack.getCount() - stackAt.getMaxStackSize();

                    if (doAdd)
                    {
                        stackAt.setCount(stackAt.getMaxStackSize());
                        this.setChanged();
                    }

                    stack.setCount(surplus);
                    if (this.addCargo(stack, doAdd) == EnumCargoLoadingState.SUCCESS)
                    {
                        return EnumCargoLoadingState.SUCCESS;
                    }

                    stackAt.setCount(origSize);
                    return EnumCargoLoadingState.FULL;
                }
            }
        }

        int size = this.getInventory().size();
        for (count = 1; count < size; count++)
        {
            ItemStack stackAt = this.getInventory().get(count);

            if (stackAt.isEmpty())
            {
                if (doAdd)
                {
                    this.getInventory().set(count, stack);
                    this.setChanged();
                }

                return EnumCargoLoadingState.SUCCESS;
            }
        }

        return EnumCargoLoadingState.FULL;
    }

    @Override
    public boolean canAttachToLandingPad(LevelReader world, BlockPos pos)
    {
        return true;
    }

    @Override
    public Direction getFront()
    {
        BlockState state = this.level.getBlockState(getBlockPos());
        if (state.getBlock() instanceof BlockCargoLoader)
        {
            return (state.getValue(BlockCargoLoader.FACING));
        }
        return Direction.NORTH;
    }

    @Override
    public Direction getElectricInputDirection()
    {
        return getFront().getClockWise();
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerCargoUnloader(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.cargo_unloader");
    }
}
