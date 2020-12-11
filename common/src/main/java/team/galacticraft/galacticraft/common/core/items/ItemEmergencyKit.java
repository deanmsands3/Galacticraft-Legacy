package team.galacticraft.galacticraft.common.core.items;

import team.galacticraft.galacticraft.common.core.GCItems;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
import team.galacticraft.galacticraft.common.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemEmergencyKit extends ItemDesc implements ISortable
{
    private static final int SIZE = 9;

    public ItemEmergencyKit(Properties properties)
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
    @Environment(EnvType.CLIENT)
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
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, itemStack);
    }

    public static ItemStack getContents(int slot)
    {
        switch (slot)
        {
        case 0:
            return new ItemStack(GCItems.oxMask);
        case 1:
            return new ItemStack(GCItems.oxygenGear);
        case 2:
            return new ItemStack(GCItems.oxTankLight);
        case 3:
            return new ItemStack(GCItems.oxTankLight);
        case 4:
            return new ItemStack(GCItems.steelPickaxe);
        case 5:
            return new ItemStack(GCItems.dehydratedPotato, 1);
        case 6:
            return PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.HEALING);
        case 7:
            return PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_NIGHT_VISION);
        case 8:
            return new ItemStack(GCItems.parachuteRed, 1);
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
        return I18n.get(this.getDescriptionId() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }
}