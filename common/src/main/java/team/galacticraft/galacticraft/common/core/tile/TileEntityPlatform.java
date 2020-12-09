package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.world.IZeroGDimension;
import team.galacticraft.galacticraft.core.GCBlockNames;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.GCBlocks;
import team.galacticraft.galacticraft.core.blocks.BlockPlatform;
import team.galacticraft.galacticraft.core.blocks.BlockPlatform.EnumCorner;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStatsClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TileEntityPlatform extends BlockEntity implements TickableBlockEntity
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.platform)
    public static BlockEntityType<TileEntityPlatform> TYPE;

    private static final int MAXRANGE = 16;
    private EnumCorner corner = EnumCorner.NONE;
    private AABB detection = null;
    private boolean noCollide;
    private boolean moving;
    private boolean lightOn = false;
    private int colorState = 0;   //0 = green  1 = red
    private int colorTicks = 0;
    private AABB renderAABB;
    private int lightA;
    private int lightB;
    private int deltaY;
    private boolean firstTickCheck;

    public TileEntityPlatform()
    {
        super(TYPE);
    }

    public TileEntityPlatform(EnumCorner corner)
    {
        this();
        this.corner = corner;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.corner = EnumCorner.byId(nbt.getInt("co"));
        if (this.corner != EnumCorner.NONE)
        {
            this.firstTickCheck = true;
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putInt("co", this.corner.getId());
        return nbt;
    }

    @Override
    public void tick()
    {
        if (this.firstTickCheck && !this.level.isClientSide)
        {
            this.firstTickCheck = !this.checkIntact();
        }

        if (this.corner == EnumCorner.NONE && !this.level.isClientSide)
        {
            final List<TileEntityPlatform> adjacentPlatforms = new LinkedList<>();
            final int thisX = this.getBlockPos().getX();
            final int thisY = this.getBlockPos().getY();
            final int thisZ = this.getBlockPos().getZ();

            for (int x = -1; x < 1; x++)
            {
                for (int z = -1; z < 1; z++)
                {
                    BlockPos pos = new BlockPos(x + thisX, thisY, z + thisZ);
                    final BlockEntity tile = this.level.hasChunkAt(pos) ? this.level.getBlockEntity(pos) : null;

                    if (tile instanceof TileEntityPlatform && !tile.isRemoved() && ((TileEntityPlatform) tile).corner == EnumCorner.NONE)
                    {
                        final BlockEntity tileUp = this.level.getBlockEntity(pos.above());
                        final BlockEntity tileDown = this.level.getBlockEntity(pos.below());
                        if (!(tileUp instanceof TileEntityPlatform) && !(tileDown instanceof TileEntityPlatform))
                        {
                            adjacentPlatforms.add((TileEntityPlatform) tile);
                        }
                    }
                }
            }

            if (adjacentPlatforms.size() == 4)
            {
                int index = 1;
                for (final TileEntityPlatform tile : adjacentPlatforms)
                {
                    tile.setWhole(EnumCorner.byId(index));
                    index++;
                }
            }
        }
        else if (this.level.isClientSide)
        {
            this.updateClient();
        }
    }

    @Environment(EnvType.CLIENT)
    private void updateClient()
    {
        this.lightOn = false;
        if (this.colorTicks > 0)
        {
            if (--this.colorTicks == 0)
            {
                this.colorState = 0;
            }
        }

        BlockState b = this.level.getBlockState(this.getBlockPos());
        if (b.getBlock() == GCBlocks.platform && b.getValue(BlockPlatform.CORNER) == EnumCorner.NW)
        {
            //Scan area for player entities and light up
            if (this.detection == null)
            {
                this.detection = new AABB(this.getBlockPos().getX() + 0.9D, this.getBlockPos().getY() + 0.75D, this.getBlockPos().getZ() + 0.9D, this.getBlockPos().getX() + 1.1D, this.getBlockPos().getY() + 1.85D, this.getBlockPos().getZ() + 1.1D);
            }
            final List<Entity> list = this.level.getEntitiesOfClass(Player.class, detection);

            if (list.size() > 0)
            {
                // Light up the platform
                this.lightOn = true;

                // If this player is within the box
                LocalPlayer p = Minecraft.getInstance().player;
                GCPlayerStatsClient stats = GCPlayerStatsClient.get(p);
                if (list.contains(p) && !stats.getPlatformControlled() && p.getVehicle() == null)
                {
                    if (p.input.shiftKeyDown)
                    {
                        int canDescend = this.checkNextPlatform(-1);
                        if (canDescend == -1)
                        {
                            this.colorState = 1;
                            this.colorTicks = 16;
                        }
                        else if (canDescend > 0)
                        {
                            BlockEntity te = this.level.getBlockEntity(this.worldPosition.below(canDescend));
                            if (te instanceof TileEntityPlatform)
                            {
                                TileEntityPlatform tep = (TileEntityPlatform) te;
                                stats.startPlatformAscent(this, tep, this.worldPosition.getY() - canDescend + (this.level.getDimension() instanceof IZeroGDimension ? 0.97D : (double) BlockPlatform.HEIGHT));
                                this.startMove(tep);
                                tep.startMove(this);
                            }
                        }
                    }
                    else if (p.input.jumping)
                    {
                        int canAscend = this.checkNextPlatform(1);
                        if (canAscend == -1)
                        {
                            this.colorState = 1;
                            this.colorTicks = 16;
                        }
                        else if (canAscend > 0)
                        {
                            BlockEntity te = this.level.getBlockEntity(this.worldPosition.above(canAscend));
                            if (te instanceof TileEntityPlatform)
                            {
                                p.setDeltaMovement(p.getDeltaMovement().x, 0.0, p.getDeltaMovement().z);
                                TileEntityPlatform tep = (TileEntityPlatform) te;
                                stats.startPlatformAscent(tep, this, this.worldPosition.getY() + canAscend + BlockPlatform.HEIGHT + 0.01D);
                                this.startMove(tep);
                                tep.startMove(this);
                            }
                        }
                    }
                }
            }
        }
    }

    @Environment(EnvType.CLIENT)
    private void startMove(TileEntityPlatform te)
    {
        this.moving = true;
        this.lightA = this.getBlendedLight();
        this.lightB = te.getBlendedLight();
        this.deltaY = te.getBlockPos().getY() - this.getBlockPos().getY();
    }

    /**
     * @return 0 for no platform, range for good platform, -1 for blocked platform
     */
    private int checkNextPlatform(int dir)
    {
        final int thisX = this.getBlockPos().getX();
        final int thisY = this.getBlockPos().getY();
        final int thisZ = this.getBlockPos().getZ();
        int maxY = thisY + MAXRANGE * dir;
        if (maxY > 255)
        {
            maxY = 255;
        }
        if (maxY < 0)
        {
            maxY = 0;
        }

        for (int y = thisY + dir; y != maxY; y += dir)
        {
            int c1 = this.checkCorner(new BlockPos(thisX, y, thisZ), EnumCorner.NW);
            if (c1 >= 2)
            {
                return c1 - 3;
            }
            c1 += this.checkCorner(new BlockPos(thisX + 1, y, thisZ), EnumCorner.NE) * 4;
            if (c1 >= 8)
            {
                return c1 - 3;
            }
            c1 += this.checkCorner(new BlockPos(thisX, y, thisZ + 1), EnumCorner.SW) * 16;
            if (c1 >= 32)
            {
                return c1 - 3;
            }
            c1 += this.checkCorner(new BlockPos(thisX + 1, y, thisZ + 1), EnumCorner.SE) * 64;
            if (c1 >= 128)
            {
                return c1 - 3;
            }
            // Good platform on all four corners
            if (c1 == 0)
            {
                continue;
            }
            if (this.motionObstructed(thisY + 1, y - thisY))
            {
                return -1;
            }
            return (y - thisY) * dir;
        }
        return 0;
    }

    /**
     * @return 0 for air, 1 for good platform, 2 for blocked platform, 3 for block
     */
    private int checkCorner(BlockPos blockPos, EnumCorner corner)
    {
        BlockState b = this.level.getBlockState(blockPos);
        if (b.getBlock() instanceof AirBlock)
        {
            return 0;
        }
        if (b.getBlock() == GCBlocks.platform && b.getValue(BlockPlatform.CORNER) == corner)
        {
            return (this.level.getBlockState(blockPos.above(1)).isViewBlocking(level, blockPos) || this.level.getBlockState(blockPos.above(2)).isViewBlocking(level, blockPos)) ? 2 : 1;
        }
        if (b.isViewBlocking(level, blockPos) || b.getShape(level, blockPos) == Shapes.block())
        {
            return 3;
        }
        return 0;
    }

    private void setWhole(EnumCorner corner)
    {
        this.corner = corner;
        this.level.setBlockAndUpdate(this.getBlockPos(), GCBlocks.platform.defaultBlockState().setValue(BlockPlatform.CORNER, corner));
    }

    public void onDestroy(BlockEntity callingBlock)
    {
        if (this.corner != EnumCorner.NONE)
        {
            resetBlocks();
        }
        this.level.destroyBlock(this.worldPosition, true);
    }

    private void resetBlocks()
    {
        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(this.worldPosition, positions);

        for (BlockPos pos : positions)
        {
            if (this.level.hasChunkAt(pos) && this.level.getBlockState(pos).getBlock() == GCBlocks.platform)
            {
                final BlockEntity tile = this.level.getBlockEntity(pos);
                if (tile instanceof TileEntityPlatform)
                {
                    ((TileEntityPlatform) tile).setWhole(EnumCorner.NONE);
                }
            }
        }
    }

    public void getPositions(BlockPos blockPos, List<BlockPos> positions)
    {
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        switch (this.corner)
        {
        case NONE:
            break;
        case NW:
            positions.add(new BlockPos(x + 1, y, z));
            positions.add(new BlockPos(x, y, z + 1));
            positions.add(new BlockPos(x + 1, y, z + 1));
            break;
        case SW:
            positions.add(new BlockPos(x + 1, y, z));
            positions.add(new BlockPos(x, y, z - 1));
            positions.add(new BlockPos(x + 1, y, z - 1));
            break;
        case NE:
            positions.add(new BlockPos(x - 1, y, z));
            positions.add(new BlockPos(x, y, z + 1));
            positions.add(new BlockPos(x - 1, y, z + 1));
            break;
        case SE:
            positions.add(new BlockPos(x - 1, y, z));
            positions.add(new BlockPos(x, y, z - 1));
            positions.add(new BlockPos(x - 1, y, z - 1));
            break;
        }
    }

    private boolean checkIntact()
    {
        BlockState bs = this.level.getBlockState(this.worldPosition);
        if (bs.getBlock() != GCBlocks.platform || bs.getValue(BlockPlatform.CORNER) != this.corner)
        {
            this.resetBlocks();
            return false;
        }
        int x = this.worldPosition.getX();
        int y = this.worldPosition.getY();
        int z = this.worldPosition.getZ();
        int count = 0;
        switch (this.corner)
        {
        case NONE:
            count = 3;
            break;
        case NW:
            count += checkState(new BlockPos(x + 1, y, z), EnumCorner.NE);
            count += checkState(new BlockPos(x, y, z + 1), EnumCorner.SW);
            count += checkState(new BlockPos(x + 1, y, z + 1), EnumCorner.SE);
            break;
        case SW:
            count += checkState(new BlockPos(x + 1, y, z), EnumCorner.SE);
            count += checkState(new BlockPos(x, y, z - 1), EnumCorner.NW);
            count += checkState(new BlockPos(x + 1, y, z - 1), EnumCorner.NE);
            break;
        case NE:
            count += checkState(new BlockPos(x - 1, y, z), EnumCorner.NW);
            count += checkState(new BlockPos(x, y, z + 1), EnumCorner.SE);
            count += checkState(new BlockPos(x - 1, y, z + 1), EnumCorner.SW);
            break;
        case SE:
            count += checkState(new BlockPos(x - 1, y, z), EnumCorner.SW);
            count += checkState(new BlockPos(x, y, z - 1), EnumCorner.NE);
            count += checkState(new BlockPos(x - 1, y, z - 1), EnumCorner.NW);
            break;
        }

        if (count >= 3)
        {
            return count == 3;
        }

        this.resetBlocks();
        return true;
    }

    private int checkState(BlockPos blockPos, EnumCorner corner)
    {
        if (!this.level.hasChunkAt(blockPos))
        {
            return 4;
        }

        BlockState bs = this.level.getBlockState(blockPos);
        if (bs.getBlock() == GCBlocks.platform && bs.getValue(BlockPlatform.CORNER) == corner)
        {
            final BlockEntity tile = this.level.getBlockEntity(blockPos);
            if (tile instanceof TileEntityPlatform)
            {
                ((TileEntityPlatform) tile).corner = corner;
                ((TileEntityPlatform) tile).firstTickCheck = false;
                return 1;
            }
        }

        return 0;
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState)
//    {
//        return oldState.getBlock() != newState.getBlock();
//    }

    public boolean noCollide()
    {
        return this.noCollide;
    }

    public void markNoCollide(int y, boolean b)
    {
        BlockEntity te;
        final int x = this.getBlockPos().getX();
        final int z = this.getBlockPos().getZ();
        y += this.getBlockPos().getY();
        te = this.level.getBlockEntity(new BlockPos(x, y, z));
        if (te instanceof TileEntityPlatform)
        {
            ((TileEntityPlatform) te).noCollide = b;
        }
        te = this.level.getBlockEntity(new BlockPos(x + 1, y, z));
        if (te instanceof TileEntityPlatform)
        {
            ((TileEntityPlatform) te).noCollide = b;
        }
        te = this.level.getBlockEntity(new BlockPos(x, y, z + 1));
        if (te instanceof TileEntityPlatform)
        {
            ((TileEntityPlatform) te).noCollide = b;
        }
        te = this.level.getBlockEntity(new BlockPos(x + 1, y, z + 1));
        if (te instanceof TileEntityPlatform)
        {
            ((TileEntityPlatform) te).noCollide = b;
        }
    }

    public boolean lightEnabled()
    {
        return this.lightOn;
    }

    public int lightColor()
    {
        return this.colorState;
    }

    public boolean isMoving()
    {
        return this.moving;
    }

    public void stopMoving()
    {
        this.moving = false;
    }

    @Environment(EnvType.CLIENT)
    public float getYOffset(float partialTicks)
    {
        if (this.moving)
        {
            LocalPlayer p = Minecraft.getInstance().player;
            float playerY = (float) (p.yOld + (p.getY() - p.yOld) * (double) partialTicks);
            return (playerY - this.worldPosition.getY() - BlockPlatform.HEIGHT);
        }
        else
        {
            return 0.0F;
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public AABB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AABB(this.worldPosition.offset(-1, -18, -1), this.worldPosition.offset(1, 18, 1));
        }
        return this.renderAABB;
    }

    @Environment(EnvType.CLIENT)
    public int getBlendedLight()
    {
//        int j = 0, k = 0;
//        int light = this.world.getCombinedLight(this.getPos().up(), 0);
//        j += light % 65536;
//        k += light / 65536;
//        light = this.world.getCombinedLight(this.getPos().add(1, 1, 0), 0);
//        j += light % 65536;
//        k += light / 65536;
//        light = this.world.getCombinedLight(this.getPos().add(0, 1, 1), 0);
//        j += light % 65536;
//        k += light / 65536;
//        light = this.world.getCombinedLight(this.getPos().add(1, 1, 1), 0);
//        j += light % 65536;
//        k += light / 65536;
//        return j / 4 + k * 16384;
        return 0; // TODO Lighting
    }

    @Environment(EnvType.CLIENT)
    public float getMeanLightX(float yOffset)
    {
        float a = (float) (this.lightA % 65536);
        float b = (float) (this.lightB % 65536);
        float f = yOffset / deltaY;
        return (1 - f) * a + f * b;
    }

    @Environment(EnvType.CLIENT)
    public float getMeanLightZ(float yOffset)
    {
        float a = (float) (this.lightA / 65536);
        float b = (float) (this.lightB / 65536);
        float f = yOffset / deltaY;
        return (1 - f) * a + f * b;
    }

    @Environment(EnvType.CLIENT)
    public boolean motionObstructed(double y, double velocityY)
    {
        LocalPlayer p = Minecraft.getInstance().player;
        int x = this.worldPosition.getX() + 1;
        int z = this.worldPosition.getZ() + 1;
        double size = 9 / 16D;
        double height = p.getBbHeight() + velocityY;
        double depth = velocityY < 0D ? 0.179D : 0D;
        AABB bb = new AABB(x - size, y - depth, z - size, x + size, y + height, z + size);
        BlockPlatform.ignoreCollisionTests = true;
        boolean obstructed = this.level.getBlockCollisions(p, bb).count() > 0;
        BlockPlatform.ignoreCollisionTests = false;
        return obstructed;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public double getViewDistance()
    {
        return Constants.RENDERDISTANCE_MEDIUM;
    }
}
