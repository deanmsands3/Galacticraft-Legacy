package team.galacticraft.galacticraft.common.core.inventory;

import team.galacticraft.galacticraft.common.api.entity.IRocketType.EnumRocketType;
import team.galacticraft.galacticraft.common.api.prefab.entity.EntityAutoRocket;
import team.galacticraft.galacticraft.common.api.prefab.entity.EntityTieredRocket;
import team.galacticraft.galacticraft.core.Constants;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerRocketInventory extends AbstractContainerMenu
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.ROCKET_INVENTORY)
    public static MenuType<ContainerRocketInventory> TYPE;

    private final Inventory playerInv;
    private final EntityAutoRocket rocket;

    public ContainerRocketInventory(int containerId, Inventory playerInv, EntityAutoRocket rocket)
    {
        super(TYPE, containerId);
        this.playerInv = playerInv;
        this.rocket = rocket;
        rocket.startOpen(playerInv.player);
        this.addSlotsWithInventory(rocket.getContainerSize());
    }

    public Inventory getPlayerInv()
    {
        return playerInv;
    }

    public EntityAutoRocket getRocket()
    {
        return rocket;
    }

    private void addSlotsWithInventory(int slotCount)
    {
        int y;
        int x;
        int ySize = 145 + (slotCount - 2) * 2;
        int lastRow = slotCount / 9;

        for (y = 0; y < lastRow; ++y)
        {
            for (x = 0; x < 9; ++x)
            {
                this.addSlot(new Slot(this.rocket, x + y * 9, 8 + x * 18, 50 + y * 18));
            }
        }

        for (y = 0; y < 3; ++y)
        {
            for (x = 0; x < 9; ++x)
            {
                this.addSlot(new Slot(this.playerInv, x + y * 9 + 9, 8 + x * 18, ySize - 82 + y * 18));
            }
        }

        for (y = 0; y < 9; ++y)
        {
            this.addSlot(new Slot(this.playerInv, y, 8 + y * 18, ySize - 24));
        }
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.rocket.stillValid(par1EntityPlayer);
    }

    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par2)
    {
        ItemStack var3 = ItemStack.EMPTY;
        final Slot var4 = this.slots.get(par2);
        final int b = this.slots.size() - 36;

        if (var4 != null && var4.hasItem())
        {
            final ItemStack var5 = var4.getItem();
            var3 = var5.copy();

            if (par2 < b)
            {
                if (!this.moveItemStackTo(var5, b, b + 36, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(var5, 0, b, false))
            {
                return ItemStack.EMPTY;
            }

            if (var5.getCount() == 0)
            {
                var4.set(ItemStack.EMPTY);
            }
            else
            {
                var4.setChanged();
            }
        }

        return var3;
    }

    @Override
    public void removed(Player par1EntityPlayer)
    {
        super.removed(par1EntityPlayer);
        this.playerInv.stopOpen(par1EntityPlayer);
    }
}
