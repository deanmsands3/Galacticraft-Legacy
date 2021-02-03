package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.GrappleEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.item.*;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemGrappleHook extends BowItem implements ISortable
{
    private static final NonNullList<ItemStack> stringEntries = null;

    public ItemGrappleHook(Item.Properties properties)
    {
        super(properties);
//        this.setUnlocalizedName(assetName);
//        this.setMaxStackSize(1);
        //this.setTextureName("arrow");
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

////    @OnlyIn(Dist.CLIENT)
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
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entity, int timeLeft)
    {
        if (!(entity instanceof Player))
        {
            return;
        }

        Player player = (Player) entity;

        boolean canShoot = player.abilities.instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
        ItemStack string = ItemStack.EMPTY;

//        if (stringEntries == null) stringEntries = OreDictionary.getOres("string");

        for (ItemStack itemstack : player.inventory.items)
        {
//            if (!canShoot && OreDictionary.containsMatch(false, stringEntries, itemstack))
//            {
//                string = itemstack;
//                canShoot = true;
//            } TODO Oredict
        }

        if (canShoot)
        {
            ItemStack pickupString = string == ItemStack.EMPTY ? ItemStack.EMPTY : new ItemStack(string.getItem(), 1);
            pickupString.setTag(string.getTag());
            GrappleEntity grapple = GrappleEntity.createEntityGrapple(worldIn, player, 2.0F, pickupString);

            worldIn.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (Item.random.nextFloat() * 0.4F + 1.2F) + 0.5F);

            if (!worldIn.isClientSide)
            {
                worldIn.addFreshEntity(grapple);
            }

            stack.hurtAndBreak(1, player, (e) ->
            {
            });
            grapple.canBePickedUp = player.abilities.instabuild ? 2 : 1;

            if (!player.abilities.instabuild)
            {
                string.shrink(1);

                if (string.isEmpty())
                {
                    player.inventory.removeItem(string);
                }
            }
        }
        else if (worldIn.isClientSide)
        {
            player.sendMessage(new TextComponent(GCCoreUtil.translate("gui.message.grapple.fail")));
        }
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack)
    {
        return UseAction.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand hand)
    {
        playerIn.startUsingItem(hand);
        return new InteractionResultHolder<>(ActionResultType.SUCCESS, playerIn.getItemInHand(hand));
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }
}
