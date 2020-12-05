package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class BlockMinerBaseFull extends BlockTileGC
{
    public BlockMinerBaseFull(Properties builder)
    {
        super(builder);
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.INVISIBLE;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos)
    {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos)
    {
        return true;
    }

    @Override
    public boolean isSuffocating(BlockState state, BlockGetter worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isRedstoneConductor(BlockState state, BlockGetter worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isValidSpawn(BlockState state, BlockGetter worldIn, BlockPos pos, EntityType<?> type)
    {
        return false;
    }

//    @Override
//    public int damageDropped(BlockState state)
//    {
//        return 0;
//    }
//
//    @Override
//    public int quantityDropped(BlockState state, int fortune, Random random)
//    {
//        return 1;
//    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world)
    {
        return new TileEntityMinerBase();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

//    @Override
//    public BlockState getStateForPlacement(BlockItemUseContext context)
//    {
//        return this.getStateFromMeta(0);
//        //TODO
//        //return this.getMetadataFromAngle(world, x, y, z, LogicalSide);
//    }

//    @Override
//    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, Direction side)
//    {
//        //TODO
//        /*
//    	if (this.getMetadataFromAngle(world, x, y, z, LogicalSide) != -1)
//        {
//            return true;
//        }
//    	 */
//
//        return true;
//    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);

        if (tileEntity instanceof TileEntityMinerBase)
        {
            ((TileEntityMinerBase) tileEntity).onBlockRemoval();
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public InteractionResult onMachineActivated(Level worldIn, BlockPos pos, BlockState state, Player playerIn, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof TileEntityMinerBase)
        {
            return ((TileEntityMinerBase) tileEntity).onActivated(playerIn);
        }
        else
        {
            return InteractionResult.PASS;
        }
    }

//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune)
//    {
//        return Item.getItemFromBlock(AsteroidBlocks.blockMinerBase);
//    }
//
//    @Override
//    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, BlockState state, int fortune)
//    {
//        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
//        ret.add(new ItemStack(Item.getItemFromBlock(AsteroidBlocks.blockMinerBase), 8, 0));
//        return ret;
//    }

    @Override
    public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player)
    {
        return new ItemStack(Item.byBlock(AsteroidBlocks.blockMinerBase), 1);
    }

    @Override
    public InteractionResult onUseWrench(Level world, BlockPos pos, Player entityPlayer, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof TileEntityMinerBase)
        {
            ((TileEntityMinerBase) te).updateFacing();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public BlockRenderLayer getRenderLayer()
//    {
//        return BlockRenderLayer.CUTOUT;
//    }
}
