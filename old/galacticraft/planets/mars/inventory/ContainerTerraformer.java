package micdoodle8.mods.galacticraft.planets.mars.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.inventory.SlotSpecific;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTerraformer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ContainerTerraformer extends AbstractContainerMenu
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsContainerNames.TERRAFORMER)
    public static MenuType<ContainerTerraformer> TYPE;

    private final TileEntityTerraformer terraformer;
    private static LinkedList<ItemStack> saplingList = null;

    public ContainerTerraformer(int containerId, Inventory playerInv, TileEntityTerraformer terraformer)
    {
        super(TYPE, containerId);
        this.terraformer = terraformer;

        this.addSlot(new SlotSpecific(this.terraformer, 0, 25, 19, new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.BUCKET)));

        this.addSlot(new SlotSpecific(this.terraformer, 1, 25, 39, IItemElectric.class));

        int var6;
        int var7;

        for (var6 = 0; var6 < 3; ++var6)
        {
            List<ItemStack> stacks = new ArrayList<ItemStack>();

            if (var6 == 0)
            {
                stacks.add(new ItemStack(Items.BONE_MEAL, 1));
            }
            else if (var6 == 1)
            {
                if (ContainerTerraformer.saplingList == null)
                {
                    initSaplingList();
                }

                stacks.addAll(ContainerTerraformer.saplingList);
            }
            else if (var6 == 2)
            {
                stacks.add(new ItemStack(Items.WHEAT_SEEDS));
            }

            for (var7 = 0; var7 < 4; ++var7)
            {
                this.addSlot(new SlotSpecific(this.terraformer, var7 + var6 * 4 + 2, 25 + var7 * 18, 63 + var6 * 24, stacks.toArray(new ItemStack[stacks.size()])));
            }
        }

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlot(new Slot(playerInv, var7 + var6 * 9 + 9, 8 + var7 * 18, 155 + var6 * 18));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlot(new Slot(playerInv, var6, 8 + var6 * 18, 213));
        }

        this.terraformer.startOpen(playerInv.player);
    }

    @Override
    public void removed(Player entityplayer)
    {
        super.removed(entityplayer);
        this.terraformer.stopOpen(entityplayer);
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.terraformer.stillValid(par1EntityPlayer);
    }

    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot slot = this.slots.get(par1);
        final int b = this.slots.size();

        if (slot != null && slot.hasItem())
        {
            final ItemStack var4 = slot.getItem();
            var2 = var4.copy();

            if (par1 < b - 36)
            {
                if (!this.moveItemStackTo(var4, b - 36, b, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (EnergyUtil.isElectricItem(var4.getItem()))
                {
                    if (!this.moveItemStackTo(var4, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (var4.getItem() == Items.WATER_BUCKET)
                {
                    if (!this.moveItemStackTo(var4, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (var4.getItem() == Items.BONE_MEAL)
                {
                    if (!this.moveItemStackTo(var4, 2, 6, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (this.getSlot(6).mayPlace(var4))
                {
                    if (!this.moveItemStackTo(var4, 6, 10, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (var4.getItem() == Items.WHEAT_SEEDS)
                {
                    if (!this.moveItemStackTo(var4, 10, 14, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (par1 < b - 9)
                {
                    if (!this.moveItemStackTo(var4, b - 9, b, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.moveItemStackTo(var4, b - 36, b - 9, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (var4.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (var4.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(par1EntityPlayer, var4);
        }

        return var2;
    }

    public static boolean isOnSaplingList(ItemStack stack)
    {
        if (saplingList == null)
        {
            initSaplingList();
        }

        for (ItemStack sapling : saplingList)
        {
            if (sapling.sameItem(stack))
            {
                return true;
            }
        }

        return false;
    }

    private static void initSaplingList()
    {
        ContainerTerraformer.saplingList = new LinkedList<>();

        for (Item item : ItemTags.SAPLINGS.getValues())
        {
            ContainerTerraformer.saplingList.add(new ItemStack(item, 1));
        }

//        for (Block b : Block.REGISTRY)
//        {
//            if (b instanceof BushBlock)
//            {
//                try
//                {
//                    Item item = Item.getItemFromBlock(b);
//                    if (item != Items.AIR)
//                    {
//                        //item.getSubItems(item, null, subItemsList); - can't use because clientside only
//                        ContainerTerraformer.saplingList.add(new ItemStack(item, 1, 0));
//                        String basicName = item.getUnlocalizedName(new ItemStack(item, 1, 0));
//                        for (int i = 1; i < 16; i++)
//                        {
//                            ItemStack testStack = new ItemStack(item, 1, i);
//                            String testName = item.getUnlocalizedName(testStack);
//                            if (testName.equals("") || testName.equals(basicName))
//                            {
//                                break;
//                            }
//                            ContainerTerraformer.saplingList.add(testStack);
//                        }
//                    }
//                }
//                catch (Exception e)
//                {
//                }
//            }
//        }
    }
}
