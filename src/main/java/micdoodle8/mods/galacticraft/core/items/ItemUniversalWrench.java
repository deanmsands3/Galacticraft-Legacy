package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.blocks.BlockAdvanced;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ItemUniversalWrench extends Item implements ISortable
{
    public ItemUniversalWrench(Item.Properties properties)
    {
        super(properties);
//        this.setUnlocalizedName(assetName);
//        this.setMaxStackSize(1);
//        this.setMaxDamage(256);
        //this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
    }

//    @Annotations.RuntimeInterface(clazz = "buildcraft.api.tools.IToolWrench", modID = CompatibilityManager.modidBuildcraft)
//    public boolean canWrench(PlayerEntity player, Hand hand, ItemStack wrench, RayTraceResult rayTrace)
//    {
//        return true;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "buildcraft.api.tools.IToolWrench", modID = CompatibilityManager.modidBuildcraft)
//    public void wrenchUsed(PlayerEntity player, Hand hand, ItemStack wrench, RayTraceResult rayTrace)
//    {
//        ItemStack stack = player.inventory.getCurrentItem();
//
//        if (!stack.isEmpty())
//        {
//            stack.damageItem(1, player);
//
//            if (stack.getDamage() >= stack.getMaxDamage())
//            {
//                stack.shrink(1);
//            }
//
//            if (stack.getCount() <= 0)
//            {
//                player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
//            }
//        }
//    }

    public void wrenchUsed(Player entityPlayer, BlockPos pos)
    {

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

//    @Override
//    public boolean doesSneakBypassUse(ItemStack stack, IBlockReader world, BlockPos pos, PlayerEntity player)
//    {
//        return true;
//    }


    @Override
    public boolean doesSneakBypassUse(ItemStack stack, LevelReader world, BlockPos pos, Player player)
    {
        return true;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level world, Player player)
    {
        if (world.isClientSide && player instanceof LocalPlayer)
        {
            ClientProxyCore.playerClientHandler.onBuild(3, (LocalPlayer) player);
        }
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stackIn, UseOnContext context)
    {
        if (context.getLevel().isClientSide || context.getPlayer().isShiftKeyDown())
        {
            return InteractionResult.PASS;
        }

        BlockState state = context.getLevel().getBlockState(context.getClickedPos());

        if (state.getBlock() instanceof BlockAdvanced)
        {
            if (((BlockAdvanced) state.getBlock()).onUseWrench(context.getLevel(), context.getClickedPos(), context.getPlayer(), context.getHand(), context.getItemInHand(), new BlockHitResult(context.getClickLocation(), context.getClickedFace(), context.getClickedPos(), context.isInside())) == InteractionResult.SUCCESS)
            {
                return InteractionResult.SUCCESS;
            }
        }

        for (Property<?> entry : state.getProperties())
        {
            if (entry instanceof DirectionProperty && entry.getName().equals("facing") && state.getValue(entry) instanceof Direction)
            {
                DirectionProperty property = (DirectionProperty) entry;
                Collection<Direction> values = property.getPossibleValues();
                if (values.size() > 0)
                {
                    boolean done = false;
                    Direction currentFacing = state.getValue(property);

                    // Special case: horizontal facings should be rotated around the Y axis - this includes most of GC's own blocks
                    if (values.size() == 4 && !values.contains(Direction.UP) && !values.contains(Direction.DOWN))
                    {
                        Direction newFacing = currentFacing.getClockWise();
                        if (values.contains(newFacing))
                        {
                            context.getLevel().setBlockAndUpdate(context.getClickedPos(), state.setValue(property, newFacing));
                            done = true;
                        }
                    }
                    if (!done)
                    {
                        // General case: rotation will follow the order in FACING (may be a bit jumpy)
                        List<Direction> list = Arrays.asList(values.toArray(new Direction[0]));
                        int i = list.indexOf(currentFacing) + 1;
                        Direction newFacing = list.get(i >= list.size() ? 0 : i);
                        context.getLevel().setBlockAndUpdate(context.getClickedPos(), state.setValue(property, newFacing));
                    }

                    ItemStack stack = context.getPlayer().getItemInHand(context.getHand()).copy();
                    stack.hurtAndBreak(1, context.getPlayer(), (entity) ->
                    {
                    });
                    context.getPlayer().setItemInHand(context.getHand(), stack);

                    BlockEntity tile = context.getLevel().getBlockEntity(context.getClickedPos());
//                    if (tile instanceof TileBaseUniversalElectrical)
//                        ((TileBaseUniversalElectrical) tile).updateFacing(); TODO Ic2 support

                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.PASS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.TOOLS;
    }
}
