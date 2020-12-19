package team.galacticraft.galacticraft.common.core.tile;

import java.util.ArrayList;
import java.util.List;

import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GCBlocks;
import team.galacticraft.galacticraft.common.core.blocks.BlockAdvanced;
import team.galacticraft.galacticraft.common.core.blocks.BlockMulti;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
//import net.minecraftforge.registries.ObjectHolder;

public class TileEntityFake extends BlockEntity
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.fakeBlock)
    public static BlockEntityType<TileEntityFake> TYPE;
    //NOTE: No need for networking in 1.8+: see comment in initialiseMultiTiles()

    // The the position of the main block
    public BlockPos mainBlockPosition;

    public TileEntityFake()
    {
        this(TYPE);
    }

    public TileEntityFake(BlockEntityType<?> type)
    {
        super(type);
    }

    public TileEntityFake(BlockPos mainBlockPosition)
    {
        this(TYPE);
        this.mainBlockPosition = mainBlockPosition;
    }

    //    public void setMainBlock(BlockPos mainBlock)
//    {
//        this.mainBlockPosition = mainBlock;
//
//        if (!this.world.isRemote)
//        {
//            this.world.notifyBlockUpdate(this.getPos());
//        }
//    }

    public void onBlockRemoval()
    {
        if (this.mainBlockPosition != null)
        {
            BlockEntity tileEntity = this.level.getBlockEntity(this.mainBlockPosition);

            if (tileEntity instanceof IMultiBlock)
            {
                IMultiBlock mainBlock = (IMultiBlock) tileEntity;
                mainBlock.onDestroy(this);
            }
        }
    }

    public InteractionResult onBlockActivated(Level worldIn, BlockPos pos, Player player)
    {
        if (this.mainBlockPosition != null)
        {
            BlockEntity tileEntity = this.level.getBlockEntity(this.mainBlockPosition);

            if (tileEntity instanceof IMultiBlock)
            {
                return ((IMultiBlock) tileEntity).onActivated(player);
            }
        }

        return InteractionResult.PASS;
    }

    public InteractionResult onBlockWrenched(Level world, BlockPos pos, Player entityPlayer, InteractionHand hand, BlockHitResult hit)
    {
        if (this.mainBlockPosition != null)
        {
            BlockState state = this.level.getBlockState(this.mainBlockPosition);

            if (state.getBlock() instanceof BlockAdvanced)
            {
                return ((BlockAdvanced) state.getBlock()).use(state, world, this.mainBlockPosition, entityPlayer, hand, hit);
            }
        }

        return InteractionResult.PASS;
    }

    public BlockEntity getMainBlockTile()
    {
        if (this.mainBlockPosition != null)
        {
            return this.level.getBlockEntity(this.mainBlockPosition);
        }

        return null;
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        CompoundTag tag = nbt.getCompound("mainBlockPosition");
        this.mainBlockPosition = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);

        if (this.mainBlockPosition != null)
        {
            CompoundTag tag = new CompoundTag();
            tag.putInt("x", this.mainBlockPosition.getX());
            tag.putInt("y", this.mainBlockPosition.getY());
            tag.putInt("z", this.mainBlockPosition.getZ());
            nbt.put("mainBlockPosition", tag);
        }

        return nbt;
    }

    protected boolean initialiseMultiTiles(BlockPos pos, Level world)
    {
        IMultiBlock thisTile = (IMultiBlock) this;

        //Client can create its own fake blocks and tiles - no need for networking in 1.8+
        if (world.isClientSide)
        {
            thisTile.onCreate(world, pos);
        }

        List<BlockPos> positions = new ArrayList<>();
        thisTile.getPositions(pos, positions);
        boolean result = true;
        for (BlockPos vecToAdd : positions)
        {
            BlockEntity tile = world.getBlockEntity(vecToAdd);
            if (tile instanceof TileEntityFake)
            {
                ((TileEntityFake) tile).mainBlockPosition = pos;
            }
            else if (tile == null)
            {
                Block b = world.getBlockState(vecToAdd).getBlock();
                if (!(b instanceof BlockMulti))
                {
                    world.setBlock(vecToAdd, GCBlocks.fakeBlock.defaultBlockState().setValue(BlockMulti.MULTI_TYPE, thisTile.getMultiType()), 2);
                }
                world.setBlockEntity(vecToAdd, new TileEntityFake(pos));
            }
            else
            {
                result = false;
            }
        }
        if (result == false && !world.isClientSide)
        {
//            //Try again to create all the multiblocks - currently disabled because making new tiles here interferes with server->client tileEntity sync during worldgen (Abandoned Base)
//            thisTile.onCreate(world, pos);
        }

        return result;
    }
}
