package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import javax.annotation.Nullable;

public class ItemCanisterLiquidNitrogen extends ItemCanisterGeneric implements ISortable
{
//    protected IIcon[] icons = new IIcon[7];

    public ItemCanisterLiquidNitrogen(Item.Properties builder)
    {
        super(builder);
//        this.setAllowedFluid("liquidnitrogen");
        //this.setTextureName(GalacticraftPlanets.TEXTURE_PREFIX + assetName);
    }

    /*@Override
    @OnlyIn(Dist.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        for (int i = 0; i < this.icons.length; i++)
        {
            this.icons[i] = iconRegister.registerIcon(this.getIconString() + "_" + i);
        }
    }*/

//    @Override
//    public String getUnlocalizedName(ItemStack itemStack)
//    {
//        if (itemStack.getMaxDamage() - itemStack.getDamage() == 0)
//        {
//            return "item.empty_gas_canister";
//        }
//
//        if (itemStack.getDamage() == 1)
//        {
//            return "item.canister.liquid_nitrogen.full";
//        }
//
//        return "item.canister.liquid_nitrogen.partial";
//    }

    /*@Override
    public IIcon getIconFromDamage(int par1)
    {
        final int damage = 6 * par1 / this.getMaxDamage();

        if (this.icons.length > damage)
        {
            return this.icons[this.icons.length - damage - 1];
        }

        return super.getIconFromDamage(damage);
    }*/

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack par1ItemStack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        if (par1ItemStack.getMaxDamage() - par1ItemStack.getDamageValue() > 0)
        {
            tooltip.add(new TextComponent(GCCoreUtil.translate("item.canister.liquid_nitrogen") + ": " + (par1ItemStack.getMaxDamage() - par1ItemStack.getDamageValue())));
        }
    }

    private Block canFreeze(Block b)
    {
        if (b == Blocks.WATER)
        {
            return Blocks.ICE;
        }
        if (b == Blocks.LAVA)
        {
            return Blocks.OBSIDIAN;
        }
        return null;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand hand)
    {
        ItemStack itemStack = playerIn.getItemInHand(hand);

        int damage = itemStack.getDamageValue() + 125;
        if (damage > itemStack.getMaxDamage())
        {
            return new InteractionResultHolder<>(InteractionResult.PASS, itemStack);
        }

        HitResult movingobjectposition = Item.getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.ANY);

        if (movingobjectposition.getType() == HitResult.Type.MISS)
        {
            return new InteractionResultHolder<>(InteractionResult.PASS, itemStack);
        }
        else
        {
            if (movingobjectposition.getType() == HitResult.Type.BLOCK)
            {
                BlockHitResult blockResult = (BlockHitResult) movingobjectposition;
                BlockPos pos = blockResult.getBlockPos();

                if (!worldIn.canMineBlockBody(playerIn, pos))
                {
                    return new InteractionResultHolder<>(InteractionResult.PASS, itemStack);
                }

                if (!playerIn.mayUseItemAt(pos, blockResult.getDirection(), itemStack))
                {
                    return new InteractionResultHolder<>(InteractionResult.PASS, itemStack);
                }

                //Material material = par2World.getBlock(i, j, k).getMaterial();
                BlockState state = worldIn.getBlockState(pos);
                Block b = state.getBlock();
//                int meta = b.getMetaFromState(state);

                Block result = this.canFreeze(b);
                if (result != null)
                {
                    this.setNewDamage(itemStack, damage);
                    worldIn.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.FLINTANDSTEEL_USE, SoundSource.NEUTRAL, 1.0F, Item.random.nextFloat() * 0.4F + 0.8F);
                    worldIn.setBlock(pos, result.defaultBlockState(), 3);
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
                }
            }

            return new InteractionResultHolder<>(InteractionResult.PASS, itemStack);
        }
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.CANISTER;
    }
}
