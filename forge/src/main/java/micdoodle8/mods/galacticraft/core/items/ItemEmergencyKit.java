package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemEmergencyKit extends ItemDesc implements ISortable
{
    private static final int SIZE = 9;

    public ItemEmergencyKit(Item.Properties properties)
    {
        super(properties);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(false);
//        this.setMaxStackSize(1);
//        this.setUnlocalizedName(assetName);
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GEAR;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand)
    {
        ItemStack itemStack = player.getItemInHand(hand);
        if (player instanceof ServerPlayer)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);

            for (int i = 0; i < SIZE; i++)
            {
                ItemStack newGear = getContents(i);
                if (newGear.getItem() instanceof IClickableItem)
                {
                    newGear = ((IClickableItem) newGear.getItem()).onItemRightClick(newGear, worldIn, player);
                }
                if (newGear.getCount() >= 1)
                {
                    ItemHandlerHelper.giveItemToPlayer(player, newGear, 0);
                }
            }

            itemStack.setCount(0);
            return new InteractionResultHolder<>(ActionResultType.SUCCESS, itemStack);
        }
        return new InteractionResultHolder<>(ActionResultType.PASS, itemStack);
    }

    public static ItemStack getContents(int slot)
    {
        switch (slot)
        {
        case 0:
            return new ItemStack(GCItems.OXYGEN_MASK);
        case 1:
            return new ItemStack(GCItems.OXYGEN_GEAR);
        case 2:
            return new ItemStack(GCItems.LIGHT_OXYGEN_TANK);
        case 3:
            return new ItemStack(GCItems.LIGHT_OXYGEN_TANK);
        case 4:
            return new ItemStack(GCItems.HEAVY_DUTY_PICKAXE);
        case 5:
            return new ItemStack(GCItems.DEHYDRATED_POTATOES, 1);
        case 6:
            return PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.HEALING);
        case 7:
            return PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_NIGHT_VISION);
        case 8:
            return new ItemStack(GCItems.RED_PARACHUTE, 1);
        default:
            return null;
        }
    }

    public static Object[] getRecipe()
    {
        Object[] result = new Object[]{"EAB", "CID", "FGH", 'A', null, 'B', null, 'C', null, 'D', null, 'E', null, 'F', null, 'G', null, 'H', null, 'I', null};
        for (int i = 0; i < SIZE; i++)
        {
            result[i * 2 + 4] = getContents(i);
        }
        return result;
    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return GCCoreUtil.translate(this.getDescriptionId() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }
}