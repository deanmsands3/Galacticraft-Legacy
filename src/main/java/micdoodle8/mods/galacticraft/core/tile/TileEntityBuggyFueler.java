package micdoodle8.mods.galacticraft.core.tile;

import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import micdoodle8.mods.galacticraft.api.entity.ICargoEntity;
import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.entity.IFuelable;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti.EnumBlockMultiType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;

public class TileEntityBuggyFueler extends TileEntityMulti implements IMultiBlock, IFuelable, IFuelDock, ICargoEntity
{

    public TileEntityBuggyFueler()
    {
        super(null);
    }

    private IDockable dockedEntity;
    private boolean initialised;

    @Override
    public void update()
    {
        if (!this.initialised)
        {
            if (!this.world.isRemote)
                this.onCreate(this.world, this.getPos());
            this.initialiseMultiTiles(this.getPos(), this.world);
            this.initialised = true;
        }

        if (!this.world.isRemote)
        {
            final List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.getPos().getX() - 1.5D, this.getPos().getY() - 2.0, this.getPos().getZ() - 1.5D,
                this.getPos().getX() + 1.5D, this.getPos().getY() + 4.0, this.getPos().getZ() + 1.5D), (Predicate<Entity>) input -> input instanceof IFuelable);

            boolean changed = false;

            for (final Object o : list)
            {
                if (o != null && o instanceof IDockable && !this.world.isRemote)
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
    public boolean onActivated(EntityPlayer entityPlayer)
    {
        return false;
    }

    @Override
    public void onCreate(World world, BlockPos placedPosition)
    {
        this.mainBlockPosition = placedPosition;
        this.markDirty();

        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(placedPosition, positions);
        ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, positions, placedPosition, this.getMultiType());
    }

    @Override
    public BlockMulti.EnumBlockMultiType getMultiType()
    {
        return EnumBlockMultiType.BUGGY_FUEL_PAD;
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
                    continue;
                positions.add(new BlockPos(placedPosition.getX() + x, y, placedPosition.getZ() + z));
            }
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        final BlockPos thisBlock = getPos();
        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            IBlockState stateAt = this.world.getBlockState(pos);

            if (stateAt.getBlock() == GCBlocks.fakeBlock && (EnumBlockMultiType) stateAt.getValue(BlockMulti.MULTI_TYPE) == EnumBlockMultiType.BUGGY_FUEL_PAD)
            {
                if (this.world.isRemote && this.world.rand.nextDouble() < 0.1D)
                {
                    FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(pos, this.world.getBlockState(pos));
                }
                this.world.destroyBlock(pos, false);
            }
        }
        this.world.destroyBlock(thisBlock, true);

        if (this.dockedEntity != null)
        {
            this.dockedEntity.onPadDestroyed();
            this.dockedEntity = null;
        }
    }

    @Override
    public int addFuel(FluidStack liquid, boolean doFill)
    {
        if (this.dockedEntity != null)
        {
            return this.dockedEntity.addFuel(liquid, doFill);
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
                        final TileEntity tile = this.world.getTileEntity(new BlockPos(this.getPos().getX() + x, this.getPos().getY(), this.getPos().getZ() + z));

                        if (tile != null && tile instanceof ILandingPadAttachable && ((ILandingPadAttachable) tile).canAttachToLandingPad(this.world, this.getPos()))
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
    public boolean isBlockAttachable(IBlockAccess world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile != null && tile instanceof ILandingPadAttachable)
        {
            return ((ILandingPadAttachable) tile).canAttachToLandingPad(world, this.getPos());
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
