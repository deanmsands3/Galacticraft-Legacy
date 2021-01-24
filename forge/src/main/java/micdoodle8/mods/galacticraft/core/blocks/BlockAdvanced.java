package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import java.lang.reflect.Method;

/**
 * An advanced block class that is to be extended for wrenching capabilities.
 */
public abstract class BlockAdvanced extends Block
{
    public BlockAdvanced(Properties builder)
    {
        super(builder);
//        this.setHardness(0.6f);
//        this.setResistance(2.5F);
        //A default blast resistance for GC machines and tiles, similar to a bookshelf
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit)
    {
        if (hand != Hand.MAIN_HAND)
        {
            return ActionResultType.PASS;
        }

        ItemStack heldItem = playerIn.getItemInHand(hand);

        if (this.useWrench(worldIn, pos, playerIn, hand, heldItem, hit) == ActionResultType.CONSUME)
        {
            return ActionResultType.CONSUME;
        }

        if (playerIn.isShiftKeyDown())
        {
            if (this.onSneakMachineActivated(worldIn, pos, playerIn, hand, heldItem, hit) == ActionResultType.CONSUME)
            {
                return ActionResultType.CONSUME;
            }
        }

        return this.onMachineActivated(worldIn, pos, state, playerIn, hand, heldItem, hit);
    }

    protected InteractionResult useWrench(Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        if (heldItem.getItem() == GCItems.STANDARD_WRENCH)
        {
            if (playerIn.isShiftKeyDown())
            {
                if (this.onSneakUseWrench(worldIn, pos, playerIn, hand, heldItem, hit) == ActionResultType.SUCCESS)
                {
                    playerIn.swing(hand);
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.PASS;
            }

            playerIn.swing(hand);
            return ActionResultType.PASS;
        }
        /*
         * Check if the player is holding a wrench or an electric item. If so,
         * call the wrench event.
         */
        if (this.isUsableWrench(playerIn, heldItem, pos))
        {
            this.damageWrench(playerIn, heldItem, pos);

            if (playerIn.isShiftKeyDown())
            {
                if (this.onSneakUseWrench(worldIn, pos, playerIn, hand, heldItem, hit) == ActionResultType.SUCCESS)
                {
                    playerIn.swing(hand);
                    return ActionResultType.SUCCESS;
                }
            }

            if (this.onUseWrench(worldIn, pos, playerIn, hand, heldItem, hit) == ActionResultType.SUCCESS)
            {
                playerIn.swing(hand);
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }

    /**
     * A function that denotes if an itemStack is a wrench that can be used.
     * Override this for more wrench compatibility. Compatible with Buildcraft
     * and IC2 wrench API via reflection.
     *
     * @return True if it is a wrench.
     */
    public boolean isUsableWrench(Player entityPlayer, ItemStack itemStack, BlockPos pos)
    {
        if (entityPlayer != null && itemStack != null)
        {
            Item item = itemStack.getItem();
            if (item == GCItems.STANDARD_WRENCH)
            {
                return false;
            }

            Class<? extends Item> wrenchClass = item.getClass();

            /**
             * Buildcraft
             */
            try
            {
                Method methodCanWrench = wrenchClass.getMethod("canWrench", Player.class, BlockPos.class);
                return (Boolean) methodCanWrench.invoke(item, entityPlayer, pos);
            }
            catch (NoClassDefFoundError e)
            {
            }
            catch (Exception e)
            {
            }

            if (CompatibilityManager.isIc2Loaded())
            {
                /**
                 * Industrialcraft
                 */
                if (wrenchClass == CompatibilityManager.classIC2wrench || wrenchClass == CompatibilityManager.classIC2wrenchElectric)
                {
                    return itemStack.getDamageValue() < itemStack.getMaxDamage();
                }
            }
        }

        return false;
    }

    /**
     * This function damages a wrench. Works with Buildcraft and Industrialcraft
     * wrenches.
     *
     * @return True if damage was successfull.
     */
    public boolean damageWrench(Player entityPlayer, ItemStack itemStack, BlockPos pos)
    {
        if (this.isUsableWrench(entityPlayer, itemStack, pos))
        {
            Class<? extends Item> wrenchClass = itemStack.getItem().getClass();

            /**
             * Buildcraft
             */
            try
            {
                Method methodWrenchUsed = wrenchClass.getMethod("wrenchUsed", Player.class, BlockPos.class);
                methodWrenchUsed.invoke(itemStack.getItem(), entityPlayer, pos);
                return true;
            }
            catch (Exception e)
            {
            }

            /**
             * Industrialcraft
             */
            try
            {
                if (wrenchClass == CompatibilityManager.classIC2wrench || wrenchClass == CompatibilityManager.classIC2wrenchElectric)
                {
                    Method methodWrenchDamage = wrenchClass.getMethod("damage", ItemStack.class, Integer.TYPE, Player.class);
                    methodWrenchDamage.invoke(itemStack.getItem(), itemStack, 1, entityPlayer);
                    return true;
                }
            }
            catch (Exception e)
            {
            }
        }

        return false;
    }

    /**
     * Called when the machine is right clicked by the player
     *
     * @return True if something happens
     */
    public InteractionResult onMachineActivated(Level worldIn, BlockPos pos, BlockState state, Player playerIn, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        return ActionResultType.PASS;
    }

    /**
     * Called when the machine is being wrenched by a player while sneaking.
     *
     * @return True if something happens
     */
    public InteractionResult onSneakMachineActivated(Level world, BlockPos pos, Player entityPlayer, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        return ActionResultType.PASS;
    }

    /**
     * Called when a player uses a wrench on the machine
     *
     * @return True if some happens
     */
    public InteractionResult onUseWrench(Level world, BlockPos pos, Player entityPlayer, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        return ActionResultType.PASS;
    }

    /**
     * Called when a player uses a wrench on the machine while sneaking. Only
     * works with the UE wrench.
     *
     * @return True if some happens
     */
    public InteractionResult onSneakUseWrench(Level world, BlockPos pos, Player entityPlayer, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        return this.onUseWrench(world, pos, entityPlayer, hand, heldItem, hit);
    }
}
