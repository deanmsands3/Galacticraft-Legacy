package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.entity.ICargoEntity;
import team.galacticraft.galacticraft.common.api.entity.IDockable;
import team.galacticraft.galacticraft.common.api.entity.IFuelable;
import team.galacticraft.galacticraft.common.api.entity.ILandable;
import team.galacticraft.galacticraft.common.api.tile.IFuelDock;
import team.galacticraft.galacticraft.common.api.tile.ILandingPad;
import team.galacticraft.galacticraft.common.api.tile.ILandingPadAttachable;
import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GCBlocks;
import team.galacticraft.galacticraft.common.core.blocks.BlockMulti;
import team.galacticraft.galacticraft.common.core.blocks.BlockMulti.EnumBlockMultiType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
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
//import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TileEntityLandingPad extends TileEntityFake implements IMultiBlock, IFuelable, IFuelDock, ICargoEntity, ILandingPad
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.landingPadFull)
    public static BlockEntityType<TileEntityLandingPad> TYPE;

    public TileEntityLandingPad()
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
            final List<Entity> list = this.level.getEntitiesOfClass(Entity.class, new AABB(this.getBlockPos().getX() - 0.5D, this.getBlockPos().getY(), this.getBlockPos().getZ() - 0.5D, this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 1.0D, this.getBlockPos().getZ() + 0.5D));

            boolean docked = false;

            for (final Object o : list)
            {
                if (o instanceof IDockable && ((Entity) o).isAlive())
                {
                    final IDockable fuelable = (IDockable) o;

                    if (!fuelable.inFlight())
                    {
                        docked = true;

                        if (fuelable != this.dockedEntity && fuelable.isDockValid(this))
                        {
                            if (fuelable instanceof ILandable)
                            {
                                ((ILandable) fuelable).landEntity(this.getBlockPos());
                            }
                            else
                            {
                                fuelable.setPad(this);
                            }
                        }

                        break;
                    }
                }
            }

            if (!docked)
            {
                this.dockedEntity = null;
            }
        }
    }

//    @Override
//    public boolean canUpdate()
//    {
//        return true;
//    }

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
        ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, positions, placedPosition, this.getMultiType());
    }

    @Override
    public EnumBlockMultiType getMultiType()
    {
        return EnumBlockMultiType.ROCKET_PAD;
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

            if (stateAt.getBlock() == GCBlocks.fakeBlock && stateAt.getValue(BlockMulti.MULTI_TYPE) == EnumBlockMultiType.ROCKET_PAD)
            {
                if (this.level.isClientSide && this.level.random.nextDouble() < 0.1D)
                {
                    Minecraft.getInstance().particleEngine.destroy(pos, this.level.getBlockState(pos));
                }
                this.level.destroyBlock(pos, false);
            }
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
    public HashSet<ILandingPadAttachable> getConnectedTiles()
    {
        HashSet<ILandingPadAttachable> connectedTiles = new HashSet<ILandingPadAttachable>();

        for (int x = this.getBlockPos().getX() - 1; x < this.getBlockPos().getX() + 2; x++)
        {
            this.testConnectedTile(x, this.getBlockPos().getZ() - 2, connectedTiles);
            this.testConnectedTile(x, this.getBlockPos().getZ() + 2, connectedTiles);
        }

        for (int z = this.getBlockPos().getZ() - 1; z < this.getBlockPos().getZ() + 2; z++)
        {
            this.testConnectedTile(this.getBlockPos().getX() - 2, z, connectedTiles);
            this.testConnectedTile(this.getBlockPos().getX() + 2, z, connectedTiles);
        }

        return connectedTiles;
    }

    private void testConnectedTile(int x, int z, HashSet<ILandingPadAttachable> connectedTiles)
    {
        BlockPos testPos = new BlockPos(x, this.getBlockPos().getY(), z);
        if (!this.level.hasChunkAt(testPos))
        {
            return;
        }

        final BlockEntity tile = this.level.getBlockEntity(testPos);

        if (tile instanceof ILandingPadAttachable && ((ILandingPadAttachable) tile).canAttachToLandingPad(this.level, this.getBlockPos()))
        {
            connectedTiles.add((ILandingPadAttachable) tile);
//            if (GalacticraftCore.isPlanetsLoaded && tile instanceof TileEntityLaunchController)
//            {
//                ((TileEntityLaunchController) tile).setAttachedPad(this);
//            } TODO Planets
        }
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
    @Environment(EnvType.CLIENT)
    public AABB getRenderBoundingBox()
    {
        return new AABB(getBlockPos().getX() - 1, getBlockPos().getY(), getBlockPos().getZ() - 1, getBlockPos().getX() + 2, getBlockPos().getY() + 0.4D, getBlockPos().getZ() + 2);
    }

    @Override
    public boolean isBlockAttachable(LevelReader world, BlockPos pos)
    {
        BlockEntity tile = world.getBlockEntity(pos);

        if (tile != null && tile instanceof ILandingPadAttachable)
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
