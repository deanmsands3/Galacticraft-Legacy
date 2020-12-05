package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntitySlimelingEgg;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import javax.annotation.Nullable;

public class BlockSlimelingEgg extends Block implements IShiftDescription, ISortable
{
    //    private IIcon[] icons;
    public static final BooleanProperty BROKEN = BooleanProperty.create("broken");
    protected static final VoxelShape AABB = Shapes.box(0.25, 0.0, 0.25, 0.75, 0.625, 0.75);

    public BlockSlimelingEgg(Properties builder)
    {
        super(builder);
        this.registerDefaultState(stateDefinition.any().setValue(BROKEN, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return AABB;
    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

//    @Override
//    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
//    {
//        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
//    }

//    @Override
//    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
//    {
//        Block block = par1World.getBlock(par2, par3 - 1, par4);
//        return block.isSideSolid(par1World, par2, par3, par4, ForgeDirection.UP);
//    }

    private InteractionResult beginHatch(Level world, BlockPos pos, Player player, int time)
    {
        BlockState state = world.getBlockState(pos);

        if (!state.getValue(BROKEN))
        {
            world.setBlock(pos, state.setValue(BROKEN, true), 2);

            BlockEntity tile = world.getBlockEntity(pos);

            if (tile instanceof TileEntitySlimelingEgg)
            {
                ((TileEntitySlimelingEgg) tile).timeToHatch = world.random.nextInt(30 + time) + time;
                ((TileEntitySlimelingEgg) tile).lastTouchedPlayerUUID = player.getUUID().toString();
            }

            world.setBlocksDirty(pos, this.defaultBlockState(), state); // Forces block render update. Better way to do this?

            return InteractionResult.SUCCESS;
        }
        else
        {
            world.setBlocksDirty(pos, this.defaultBlockState(), state); // Forces block render update. Better way to do this?
            return InteractionResult.PASS;
        }
    }

    @Override
    public boolean removedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid)
    {
        ItemStack currentStack = player.getMainHandItem();
        if (currentStack != ItemStack.EMPTY && currentStack.getItem() instanceof PickaxeItem)
        {
            return world.removeBlock(pos, false);
        }
        else if (player.abilities.instabuild)
        {
            return world.removeBlock(pos, false);
        }
        else
        {
            beginHatch(world, pos, player, 0);
            return false;
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit)
    {
        return beginHatch(worldIn, pos, playerIn, 20);
    }

    @Override
    public void playerDestroy(Level worldIn, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, @Nullable ItemStack currentStack)
    {
        if (!currentStack.isEmpty() && currentStack.getItem() instanceof PickaxeItem)
        {
            player.awardStat(Stats.BLOCK_MINED.get(this));
            player.causeFoodExhaustion(0.025F);
            popResource(worldIn, pos, getCloneItemStack(worldIn, pos, state));
//            this.dropBlockAsItem(worldIn, pos, state.getBlock().getStateFromMeta(state.getBlock().getMetaFromState(state) % 3), 0);
            if (currentStack.getItem() == MarsItems.deshPickaxe && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, currentStack) > 0)
            {
                ItemStack itemstack = new ItemStack(MarsItems.deshPickSlime, 1/*, currentStack.getDamage()*/);
                itemstack.setDamageValue(currentStack.getDamageValue());
                if (currentStack.getTag() != null)
                {
                    itemstack.setTag(currentStack.getTag().copy());
                }
                player.inventory.setItem(player.inventory.selected, itemstack);
            }
        }
    }

    /*@Override
    @OnlyIn(Dist.CLIENT)
    public IIcon getIcon(int LogicalSide, int metadata)
    {
        return this.icons[metadata % 6];
    }*/

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public Item getItemDropped(int meta, Random random, int par3)
//    {
//        return Item.getItemFromBlock(this);
//    }

//    @Override
//    public int damageDropped(BlockState state)
//    {
//        return getMetaFromState(state);
//    }

//    @Override
//    public int quantityDropped(int meta, int fortune, Random random)
//    {
//        return 1;
//    }

//    @Override
//    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        for (int var4 = 0; var4 < EnumEggColor.values().length; ++var4)
//        {
//            list.add(new ItemStack(this, 1, var4));
//        }
//    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return new TileEntitySlimelingEgg();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player)
    {
//        int metadata = state.getBlock().getMetaFromState(state);
//        EnumEggColor color = state.get(BlockSlimelingEgg.EGG_COLOR);
//
//        if (color)
//        {
//            return new ItemStack(Item.getItemFromBlock(this), 1, 0);
//        }
//        if (metadata == 4)
//        {
//            return new ItemStack(Item.getItemFromBlock(this), 1, 1);
//        }
//        if (metadata == 5)
//        {
//            return new ItemStack(Item.getItemFromBlock(this), 1, 2);
//        }
        return super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return GCCoreUtil.translate("block.galacticraftplanets.slimeling_egg.description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(EGG_COLOR, EnumEggColor.values()[meta % 3]).with(BROKEN, meta - 3 >= 0);
//    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(BROKEN);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.EGG;
    }
}
