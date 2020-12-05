package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.api.item.IItemThermal;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemThermalPadding extends Item implements IItemThermal, ISortable
{
//    public static String[] names = { "thermal_helm", "thermal_chestplate", "thermal_leggings", "thermal_boots", "thermal_helm0", "thermal_chestplate0", "thermal_leggings0", "thermal_boots0" };

    public ItemThermalPadding(Item.Properties properties)
    {
        super(properties);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//        this.setMaxStackSize(1);
//        this.setUnlocalizedName(assetName);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            for (int i = 0; i < ItemThermalPadding.names.length / 2; i++)
//            {
//                list.add(new ItemStack(this, 1, i));
//            }
//        }
//    }

//    @Override
//    public String getUnlocalizedName(ItemStack par1ItemStack)
//    {
//        if (names.length > par1ItemStack.getDamage())
//        {
//            return "item." + ItemThermalPadding.names[par1ItemStack.getDamage()];
//        }
//
//        return "unnamed";
//    }

//    @Override
//    public int getMetadata(int par1)
//    {
//        return par1;
//    }

    @Override
    public int getThermalStrength()
    {
        return 1;
    }

    @Override
    public boolean isValidForSlot(ItemStack stack, int armorSlot)
    {
        return stack.getDamageValue() == armorSlot;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.ARMOR;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand)
    {
        ItemStack itemStack = player.getItemInHand(hand);

        if (player instanceof ServerPlayer)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            ItemStack gear = stats.getExtendedInventory().getItem(6);
            ItemStack gear1 = stats.getExtendedInventory().getItem(7);
            ItemStack gear2 = stats.getExtendedInventory().getItem(8);
            ItemStack gear3 = stats.getExtendedInventory().getItem(9);

            if (itemStack.getDamageValue() == 0)
            {
                if (gear.isEmpty())
                {
                    stats.getExtendedInventory().setItem(6, itemStack.copy());
                    itemStack.setCount(0);
                }
            }
            else if (itemStack.getDamageValue() == 1)
            {
                if (gear1.isEmpty())
                {
                    stats.getExtendedInventory().setItem(7, itemStack.copy());
                    itemStack.setCount(0);
                }
            }
            else if (itemStack.getDamageValue() == 2)
            {
                if (gear2.isEmpty())
                {
                    stats.getExtendedInventory().setItem(8, itemStack.copy());
                    itemStack.setCount(0);
                }
            }
            else if (itemStack.getDamageValue() == 3)
            {
                if (gear3.isEmpty())
                {
                    stats.getExtendedInventory().setItem(9, itemStack.copy());
                    itemStack.setCount(0);
                }
            }
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, itemStack);
    }
}
