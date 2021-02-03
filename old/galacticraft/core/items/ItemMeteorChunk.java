package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.entities.MeteorChunkEntity;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import java.util.List;

public class ItemMeteorChunk extends Item implements ISortable
{
    public static final int METEOR_BURN_TIME = 45 * 20;

    public ItemMeteorChunk(Item.Properties properties)
    {
        super(properties);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//        this.maxStackSize = 16;
//        this.setCreativeTab(ItemGroup.MATERIALS);
//        this.setUnlocalizedName(assetName);
        //this.setTextureName("arrow");
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }


    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (entityIn instanceof Player && stack.getItem() == GCItems.HOT_METEOR_CHUNK && !world.isClientSide)
        {
            if (stack.hasTag())
            {
                float meteorBurnTime = stack.getTag().getFloat("MeteorBurnTimeF");

                if (meteorBurnTime >= 0.5F)
                {
                    meteorBurnTime -= 0.5F;
                    stack.getTag().putFloat("MeteorBurnTimeF", meteorBurnTime);
                }
                else
                {
                    ((Player) entityIn).inventory.setItem(itemSlot, new ItemStack(GCItems.METEOR_CHUNK, stack.getCount()));
//                    stack.setItemDamage(0);
                    stack.setTag(null);
                }
            }
            else
            {
                stack.getOrCreateTag().putFloat("MeteorBurnTimeF", ItemMeteorChunk.METEOR_BURN_TIME);
            }
        }
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level world, Player entityPlayer)
    {
        super.onCraftedBy(stack, world, entityPlayer);

        if (stack.getItem() == GCItems.HOT_METEOR_CHUNK)
        {
            stack.getOrCreateTag().putFloat("MeteorBurnTimeF", ItemMeteorChunk.METEOR_BURN_TIME);
        }
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            list.add(new ItemStack(this, 1, 0));
//            list.add(new ItemStack(this, 1, 1));
//        }
//    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        if (stack.getItem() == GCItems.HOT_METEOR_CHUNK)
        {
            float burnTime = 0.0F;

            if (stack.hasTag())
            {
                float meteorBurnTime = stack.getTag().getFloat("MeteorBurnTimeF");
                burnTime = Math.round(meteorBurnTime / 10.0F) / 2.0F;
            }
            else
            {
                burnTime = 45.0F;
            }

            tooltip.add(new TextComponent(GCCoreUtil.translate("item.hot_description") + " " + burnTime + GCCoreUtil.translate("gui.seconds")));
        }
    }

    @Override
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

//    @Override
//    public String getUnlocalizedName(ItemStack itemStack)
//    {
//        return "item." + ItemMeteorChunk.names[itemStack.getDamage()];
//    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
    {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!player.abilities.instabuild)
        {
            itemStack.shrink(1);
        }

        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 0.0001F / (Item.random.nextFloat() * 0.1F));

        if (!world.isClientSide)
        {
            MeteorChunkEntity meteor = new MeteorChunkEntity(world, player, 1.0F);

            if (itemStack.getItem() == GCItems.HOT_METEOR_CHUNK)
            {
                meteor.setSecondsOnFire(20);
                meteor.isHot = true;
            }

            meteor.canBePickedUp = player.abilities.instabuild ? 2 : 1;
            world.addFreshEntity(meteor);
        }

        player.swing(hand);

        return new InteractionResultHolder<>(ActionResultType.SUCCESS, itemStack);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }
}
