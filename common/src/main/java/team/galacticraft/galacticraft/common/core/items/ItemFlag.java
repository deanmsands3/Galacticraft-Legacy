package team.galacticraft.galacticraft.common.core.items;

import team.galacticraft.galacticraft.common.api.item.IHoldableItemCustom;
import team.galacticraft.galacticraft.common.api.vector.Vector3;
import team.galacticraft.galacticraft.common.core.entities.EntityFlag;
import team.galacticraft.galacticraft.common.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import team.galacticraft.galacticraft.common.core.util.PlayerUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.math.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ItemFlag extends Item implements IHoldableItemCustom, ISortable
{
    public int placeProgress;

    public ItemFlag(Properties properties)
    {
        super(properties);
//        this.setMaxDamage(0);
//        this.setMaxStackSize(1);
//        this.setUnlocalizedName(assetName);
        //this.setTextureName("arrow");
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entity, int timeLeft)
    {
        final int useTime = this.getUseDuration(stack) - timeLeft;

        boolean placed = false;

        if (!(entity instanceof Player))
        {
            return;
        }

        Player player = (Player) entity;

        final HitResult var12 = getPlayerPOVHitResult(worldIn, player, ClipContext.Fluid.ANY);

        float var7 = useTime / 20.0F;
        var7 = (var7 * var7 + var7 * 2.0F) / 3.0F;

        if (var7 > 1.0F)
        {
            var7 = 1.0F;
        }

        if (var7 == 1.0F && var12 != null && var12.getType() == HitResult.Type.BLOCK)
        {
            BlockHitResult blockResult = (BlockHitResult) var12;
            final BlockPos pos = blockResult.getBlockPos();

            if (!worldIn.isClientSide)
            {
                final EntityFlag flag = new EntityFlag(worldIn, pos.getX() + 0.5F, pos.getY() + 1.0F, pos.getZ() + 0.5F, (int) (entity.yRot - 90));

                if (worldIn.getEntitiesOfClass(EntityFlag.class, new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 3, pos.getZ() + 1)).isEmpty())
                {
                    worldIn.addFreshEntity(flag);
//                    flag.setType(stack.getDamage());
                    flag.setOwner(PlayerUtil.getName(player));
                    worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundType.METAL.getBreakSound(), SoundSource.BLOCKS, SoundType.METAL.getVolume(), SoundType.METAL.getPitch() + 2.0F);
                    placed = true;
                }
                else
                {
                    entity.sendMessage(new TranslatableComponent(("gui.flag.already_placed")));
                }
            }

            if (placed)
            {
                final int var2 = this.getInventorySlotContainItem(player, stack);

                if (var2 >= 0 && !player.abilities.instabuild)
                {
                    player.inventory.items.get(var2).shrink(1);
                }
            }
        }
    }

    private int getInventorySlotContainItem(Player player, ItemStack stack)
    {
        for (int var2 = 0; var2 < player.inventory.items.size(); ++var2)
        {
            if (!player.inventory.items.get(var2).isEmpty() && player.inventory.items.get(var2).sameItem(stack))
            {
                return var2;
            }
        }

        return -1;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack)
    {
        return UseAnim.NONE;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand hand)
    {
        playerIn.startUsingItem(hand);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(hand));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

//    @Override
//    public String getUnlocalizedName(ItemStack itemStack)
//    {
//        return "item.flag";
//    }

    /*@Override
    public IIcon getIconFromDamage(int damage)
    {
        return super.getIconFromDamage(damage);
    }*/

    @Override
    public boolean shouldHoldLeftHandUp(Player player)
    {
        return true;
    }

    @Override
    public boolean shouldHoldRightHandUp(Player player)
    {
        return true;
    }

    @Override
    public Vector3 getLeftHandRotation(Player player)
    {
        return new Vector3((float) Math.PI + 1.3F, 0.5F, (float) Math.PI / 5.0F);
    }

    @Override
    public Vector3 getRightHandRotation(Player player)
    {
        return new Vector3((float) Math.PI + 1.3F, -0.5F, (float) Math.PI / 5.0F);
    }

    @Override
    public boolean shouldCrouch(Player player)
    {
        return false;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }
}
