package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.item.IItemOxygenSupply;
import team.galacticraft.galacticraft.core.GCBlockNames;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.blocks.BlockOxygenCompressor;
import team.galacticraft.galacticraft.core.energy.item.ItemElectricBase;
import team.galacticraft.galacticraft.core.inventory.ContainerOxygenCompressor;
import team.galacticraft.galacticraft.core.items.ItemOxygenTank;
import team.galacticraft.galacticraft.core.util.FluidUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ObjectHolder;

import java.util.EnumSet;

public class TileEntityOxygenCompressor extends TileEntityOxygen implements MenuProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.oxygenCompressor)
    public static BlockEntityType<TileEntityOxygenCompressor> TYPE;

    public static final int TANK_TRANSFER_SPEED = 2;
    private boolean usingEnergy = false;

    public TileEntityOxygenCompressor()
    {
        super(TYPE, 1200, 16);
        this.storage.setMaxExtract(15);
        inventory = NonNullList.withSize(3, ItemStack.EMPTY);
    }

    @Override
    public void tick()
    {
        if (!this.level.isClientSide)
        {
            ItemStack oxygenItemStack = this.getItem(2);
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

        if (!this.level.isClientSide)
        {
            this.usingEnergy = false;
            if (this.getOxygenStored() > 0 && this.hasEnoughEnergyToRun)
            {
                ItemStack tank0 = this.getInventory().get(0);

                if (!tank0.isEmpty())
                {
                    if (tank0.getItem() instanceof ItemOxygenTank && tank0.getDamageValue() > 0)
                    {
                        tank0.setDamageValue(tank0.getDamageValue() - TileEntityOxygenCompressor.TANK_TRANSFER_SPEED);
                        this.setOxygenStored(this.getOxygenStored() - TileEntityOxygenCompressor.TANK_TRANSFER_SPEED);
                        this.usingEnergy = true;
                    }
                }
            }
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
        return new int[]{0, 1, 2};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        if (this.canPlaceItem(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return itemstack.getDamageValue() > 1;
            case 1:
                return ItemElectricBase.isElectricItemCharged(itemstack);
            case 2:
                return itemstack.getDamageValue() < itemstack.getItem().getMaxDamage();
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        switch (slotID)
        {
        case 0:
            return itemstack.getItem() instanceof ItemOxygenTank && itemstack.getDamageValue() == 0;
        case 1:
            return ItemElectricBase.isElectricItemEmpty(itemstack);
        case 2:
            return FluidUtil.isEmptyContainer(itemstack);
        default:
            return false;
        }
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
        case 2:
            return itemstack.getItem() instanceof IItemOxygenSupply;
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

    @Override
    public boolean shouldUseOxygen()
    {
        return false;
    }

    @Override
    public EnumSet<Direction> getOxygenInputDirections()
    {
        return EnumSet.of(this.getElectricInputDirection().getOpposite());
    }

    @Override
    public EnumSet<Direction> getOxygenOutputDirections()
    {
        return EnumSet.noneOf(Direction.class);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerOxygenCompressor(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.oxygen_compressor");
    }
}
