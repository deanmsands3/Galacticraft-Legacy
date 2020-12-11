package team.galacticraft.galacticraft.common.core.items;

import team.galacticraft.galacticraft.common.api.item.IHoldableItem;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.entities.EntityBuggy;
import team.galacticraft.galacticraft.common.core.entities.GCEntities;
import team.galacticraft.galacticraft.common.core.fluid.GCFluids;
import team.galacticraft.galacticraft.common.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.util.math.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import me.shedaniel.architectury.fluid.FluidStack;

import org.jetbrains.annotations.Nullable;
import java.util.List;

public class ItemBuggy extends Item implements IHoldableItem, ISortable
{
    public ItemBuggy(Properties properties)
    {
        super(properties);
//        this.setUnlocalizedName(assetName);
        //this.setTextureName("arrow");
//        this.setMaxStackSize(1);
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    @Override
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            for (int i = 0; i < 4; i++)
//            {
//                list.add(new ItemStack(this, 1, i));
//            }
//        }
//    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand hand)
    {
        ItemStack itemstack = playerIn.getItemInHand(hand);
        final float var4 = 1.0F;
        final float var5 = playerIn.xRotO + (playerIn.xRot - playerIn.xRotO) * var4;
        final float var6 = playerIn.yRotO + (playerIn.yRot - playerIn.yRotO) * var4;
        final double var7 = playerIn.xo + (playerIn.getX() - playerIn.xo) * var4;
        final double var9 = playerIn.yo + (playerIn.getY() - playerIn.yo) * var4 + 1.62D - playerIn.getRidingHeight();
        final double var11 = playerIn.zo + (playerIn.getZ() - playerIn.zo) * var4;
        final Vec3 var13 = new Vec3(var7, var9, var11);
        final float var14 = Mth.cos(-var6 / Constants.RADIANS_TO_DEGREES - (float) Math.PI);
        final float var15 = Mth.sin(-var6 / Constants.RADIANS_TO_DEGREES - (float) Math.PI);
        final float var16 = -Mth.cos(-var5 / Constants.RADIANS_TO_DEGREES);
        final float var17 = Mth.sin(-var5 / Constants.RADIANS_TO_DEGREES);
        final float var18 = var15 * var16;
        final float var20 = var14 * var16;
        final double var21 = 5.0D;
        final Vec3 var23 = var13.add(var18 * var21, var17 * var21, var20 * var21);
        final HitResult var24 = worldIn.clip(new ClipContext(var13, var23, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, playerIn));

        if (var24.getType() == HitResult.Type.MISS)
        {
            return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
        }
        else
        {
            final Vec3 var25 = playerIn.getViewVector(var4);
            boolean var26 = false;
            final float var27 = 1.0F;
            final List<?> var28 = worldIn.getEntities(playerIn, playerIn.getBoundingBox().inflate(var25.x * var21, var25.y * var21, var25.z * var21).expandTowards(var27, var27, var27));

            for (int i = 0; i < var28.size(); ++i)
            {
                final Entity var30 = (Entity) var28.get(i);

                if (var30.isPickable())
                {
                    final float var31 = var30.getPickRadius();
                    final AABB var32 = var30.getBoundingBox().expandTowards(var31, var31, var31);

                    if (var32.contains(var13))
                    {
                        var26 = true;
                    }
                }
            }

            if (var26)
            {
                return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
            }
            else
            {
                if (var24.getType() == HitResult.Type.BLOCK)
                {
                    BlockHitResult blockResult = (BlockHitResult) var24;
                    int x = blockResult.getBlockPos().getX();
                    int y = blockResult.getBlockPos().getY();
                    int z = blockResult.getBlockPos().getZ();

                    if (worldIn.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.SNOW)
                    {
                        --y;
                    }

                    final EntityBuggy var35 = GCEntities.BUGGY.create(worldIn);
                    var35.setBuggyType(EntityBuggy.getTypeFromItem(itemstack.getItem()));
                    var35.setPos(x + 0.5F, y + 1.0F, z + 0.5F);

//                    if (!worldIn.getCollisionBoxes(var35, var35.getBoundingBox().expand(-0.1D, -0.1D, -0.1D)).isEmpty())
//                    {
//                        return new ActionResult<>(ActionResultType.PASS, itemstack);
//                    } TODO Needed?

                    if (itemstack.hasTag() && itemstack.getTag().contains("BuggyFuel"))
                    {
                        var35.buggyFuelTank.setFluid(new FluidStack(GCFluids.FUEL.getFluid(), itemstack.getTag().getInt("BuggyFuel")));
                    }

                    if (!worldIn.isClientSide)
                    {
                        worldIn.addFreshEntity(var35);
                    }

                    if (!playerIn.abilities.instabuild)
                    {
                        itemstack.shrink(1);
                    }
                }

                return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack item, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        EntityBuggy.BuggyType type = EntityBuggy.getTypeFromItem(item.getItem());
        if (type.getInvSize() != 0)
        {
            tooltip.add(new TranslatableComponent(("gui.buggy.storage_space") + ": " + type.getInvSize()));
        }

        if (item.hasTag() && item.getTag().contains("BuggyFuel"))
        {
            tooltip.add(new TranslatableComponent(("gui.message.fuel") + ": " + item.getTag().getInt("BuggyFuel") + " / " + EntityBuggy.tankCapacity));
        }
    }

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
    public boolean shouldCrouch(Player player)
    {
        return true;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }
}
