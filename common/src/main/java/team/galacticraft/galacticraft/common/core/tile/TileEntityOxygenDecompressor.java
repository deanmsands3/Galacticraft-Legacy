package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.blocks.BlockOxygenCompressor;
import team.galacticraft.galacticraft.common.core.energy.item.ItemElectricBase;
import team.galacticraft.galacticraft.common.core.inventory.ContainerOxygenDecompressor;
import team.galacticraft.galacticraft.common.core.inventory.IInventoryDefaults;
import team.galacticraft.galacticraft.common.core.items.ItemOxygenTank;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;
import net.minecraftforge.registries.ObjectHolder;

import java.util.EnumSet;

public class TileEntityOxygenDecompressor extends TileEntityOxygen implements IInventoryDefaults, WorldlyContainer, MenuProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.oxygenDecompressor)
    public static BlockEntityType<TileEntityOxygenDecompressor> TYPE;

    public static final int OUTPUT_PER_TICK = 100;
    private boolean usingEnergy = false;

    public TileEntityOxygenDecompressor()
    {
        super(TYPE, 1200, 0);
        inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.level.isClientSide)
        {
            this.usingEnergy = false;
            ItemStack tank1 = this.getInventory().get(0);

            if (!tank1.isEmpty() && this.hasEnoughEnergyToRun && this.getOxygenStored() < this.getMaxOxygenStored())
            {
                if (tank1.getItem() instanceof ItemOxygenTank && tank1.getDamageValue() < tank1.getMaxDamage())
                {
                    tank1.setDamageValue(tank1.getDamageValue() + 1); // TODO Test: Shouldn't damage only be done if receieve was successful?
                    this.receiveOxygen(1, ActionType.EXECUTE);
                    this.usingEnergy = true;
                }
//                else if (tank1.getItem() instanceof ItemCanisterOxygenInfinite)
//                {
//                    this.receiveOxygen(1, ActionType.EXECUTE);
//                    this.usingEnergy = true;
//                } TODO Oxygen canisters
            }

            this.produceOxygen();
        }
    }

    @Override
    public int getMaxStackSize()
    {
        return 1;
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0, 1};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        if (this.canPlaceItem(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return itemstack.getDamageValue() < itemstack.getMaxDamage();
            case 1:
                return ItemElectricBase.isElectricItemCharged(itemstack);
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        if (this.canPlaceItem(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return itemstack.getDamageValue() == itemstack.getMaxDamage();
            case 1:
                return ItemElectricBase.isElectricItemEmpty(itemstack);
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canPlaceItem(int slotID, ItemStack itemstack)
    {
        switch (slotID)
        {
        case 0:
            return itemstack.getItem() instanceof ItemOxygenTank;
        case 1:
            return ItemElectricBase.isElectricItem(itemstack.getItem());
        }

        return false;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.usingEnergy;
    }

    @Override
    public Direction getFront()
    {
        BlockState state = this.level.getBlockState(getBlockPos());
        if (state.getBlock() instanceof BlockOxygenCompressor)
        {
            return state.getValue(BlockOxygenCompressor.FACING).getClockWise();
        }
        return Direction.NORTH;
    }

    @Override
    public Direction getElectricInputDirection()
    {
        return getFront();
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getItem(1);
    }

    public Direction getOxygenOutputDirection()
    {
        return this.getElectricInputDirection().getOpposite();
    }

    @Override
    public EnumSet<Direction> getOxygenInputDirections()
    {
        return EnumSet.noneOf(Direction.class);
    }

    @Override
    public EnumSet<Direction> getOxygenOutputDirections()
    {
        return EnumSet.of(this.getElectricInputDirection().getOpposite());
    }

    @Override
    public boolean shouldPullOxygen()
    {
        return false;
    }

    @Override
    public boolean shouldUseOxygen()
    {
        return false;
    }

    @Override
    public int getOxygenProvide(Direction direction)
    {
        return this.getOxygenOutputDirections().contains(direction) ? Math.min(TileEntityOxygenDecompressor.OUTPUT_PER_TICK, this.getOxygenStored()) : 0;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerOxygenDecompressor(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.oxygen_decompressor");
    }
}
