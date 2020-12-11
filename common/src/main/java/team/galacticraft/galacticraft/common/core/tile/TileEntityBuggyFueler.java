package team.galacticraft.galacticraft.common.core.tile;

import com.google.common.base.Predicate;
import team.galacticraft.galacticraft.common.api.entity.ICargoEntity;
import team.galacticraft.galacticraft.common.api.entity.IDockable;
import team.galacticraft.galacticraft.common.api.entity.IFuelable;
import team.galacticraft.galacticraft.common.api.tile.IFuelDock;
import team.galacticraft.galacticraft.common.api.tile.ILandingPadAttachable;
import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.blocks.BlockMulti;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import me.shedaniel.architectury.fluid.FluidStack;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TileEntityBuggyFueler extends TileEntityFake implements IMultiBlock, IFuelable, IFuelDock, ICargoEntity
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.buggyPadFull)
    public static BlockEntityType<TileEntityBuggyFueler> TYPE;

    public TileEntityBuggyFueler()
    {
        super(TYPE);
    }

    private IDockable dockedEntity;
    private boolean initialised;

    @Override
    public void tick()
    {
        if (!this.initialised)
        {
            if (!this.level.isClientSide)
            {
                this.onCreate(this.level, this.getBlockPos());
            }
            this.initialiseMultiTiles(this.getBlockPos(), this.level);
            this.initialised = true;
        }

        if (!this.level.isClientSide)
        {
            final List<Entity> list = this.level.getEntitiesOfClass(Entity.class, new AABB(this.getBlockPos().getX() - 1.5D, this.getBlockPos().getY() - 2.0, this.getBlockPos().getZ() - 1.5D,
                    this.getBlockPos().getX() + 1.5D, this.getBlockPos().getY() + 4.0, this.getBlockPos().getZ() + 1.5D), (Predicate<Entity>) input -> input instanceof IFuelable);

            boolean changed = false;

            for (final Object o : list)
            {
                if (o != null && o instanceof IDockable && !this.level.isClientSide)
                {
                    final IDockable fuelable = (IDockable) o;

                    if (fuelable.isDockValid(this))
                    {
                        this.dockedEntity = fuelable;

                        this.dockedEntity.setPad(this);

                        changed = true;
                    }
                }
            }

            if (!changed)
            {
                if (this.dockedEntity != null)
                {
                    this.dockedEntity.setPad(null);
                }

                this.dockedEntity = null;
            }
        }
    }

    @Override
    public InteractionResult onActivated(Player entityPlayer)
    {
        return InteractionResult.PASS;
    }

    @Override
    public void onCreate(Level world, BlockPos placedPosition)
    {
        this.mainBlockPosition = placedPosition;
        this.setChanged();

        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(placedPosition, positions);
//        ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, positions, placedPosition, this.getMultiType());
    }

    @Override
    public BlockMulti.EnumBlockMultiType getMultiType()
    {
        return BlockMulti.EnumBlockMultiType.BUGGY_FUEL_PAD;
    }

    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int y = placedPosition.getY();
        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                if (x == 0 && z == 0)
                {
                    continue;
                }
                positions.add(new BlockPos(placedPosition.getX() + x, y, placedPosition.getZ() + z));
            }
        }
    }

    @Override
    public void onDestroy(BlockEntity callingBlock)
    {
        final BlockPos thisBlock = getBlockPos();
        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            BlockState stateAt = this.level.getBlockState(pos);

//            if (stateAt.getBlock() == GCBlocks.fakeBlock && (EnumBlockMultiType) stateAt.getValue(BlockMulti.MULTI_TYPE) == EnumBlockMultiType.BUGGY_FUEL_PAD)
//            {
//                if (this.world.isRemote && this.world.rand.nextDouble() < 0.1D)
//                {
//                    Minecraft.getInstance().particles.addBlockDestroyEffects(pos, this.world.getBlockState(pos));
//                }
//                this.world.destroyBlock(pos, false);
//            }
        }
        this.level.destroyBlock(thisBlock, true);

        if (this.dockedEntity != null)
        {
            this.dockedEntity.onPadDestroyed();
            this.dockedEntity = null;
        }
    }

    @Override
    public int addFuel(FluidStack liquid, ActionType action)
    {
        if (this.dockedEntity != null)
        {
            return this.dockedEntity.addFuel(liquid, action);
        }

        return 0;
    }

    @Override
    public FluidStack removeFuel(int amount)
    {
        if (this.dockedEntity != null)
        {
            return this.dockedEntity.removeFuel(amount);
        }

        return null;
    }

    @Override
    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
    {
        if (this.dockedEntity != null)
        {
            return this.dockedEntity.addCargo(stack, doAdd);
        }

        return EnumCargoLoadingState.NOTARGET;
    }

    @Override
    public RemovalResult removeCargo(boolean doRemove)
    {
        if (this.dockedEntity != null)
        {
            return this.dockedEntity.removeCargo(doRemove);
        }

        return new RemovalResult(EnumCargoLoadingState.NOTARGET, ItemStack.EMPTY);
    }

    @Override
    public HashSet<ILandingPadAttachable> getConnectedTiles()
    {
        HashSet<ILandingPadAttachable> connectedTiles = new HashSet<ILandingPadAttachable>();

        for (int x = -2; x < 3; x++)
        {
            for (int z = -2; z < 3; z++)
            {
                if (x == -2 || x == 2 || z == -2 || z == 2)
                {
                    if (Math.abs(x) != Math.abs(z))
                    {
                        final BlockEntity tile = this.level.getBlockEntity(new BlockPos(this.getBlockPos().getX() + x, this.getBlockPos().getY(), this.getBlockPos().getZ() + z));

                        if (tile instanceof ILandingPadAttachable && ((ILandingPadAttachable) tile).canAttachToLandingPad(this.level, this.getBlockPos()))
                        {
                            connectedTiles.add((ILandingPadAttachable) tile);
                        }
                    }
                }
            }
        }

        return connectedTiles;
    }

    @Override
    public boolean isBlockAttachable(LevelReader world, BlockPos pos)
    {
        BlockEntity tile = world.getBlockEntity(pos);

        if (tile instanceof ILandingPadAttachable)
        {
            return ((ILandingPadAttachable) tile).canAttachToLandingPad(world, this.getBlockPos());
        }

        return false;
    }

    @Override
    public IDockable getDockedEntity()
    {
        return this.dockedEntity;
    }

    @Override
    public void dockEntity(IDockable entity)
    {
        this.dockedEntity = entity;
    }
}
