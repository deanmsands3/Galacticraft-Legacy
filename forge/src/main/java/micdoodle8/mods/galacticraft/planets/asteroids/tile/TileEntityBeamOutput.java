package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import micdoodle8.mods.galacticraft.api.power.ILaserNode;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAdvanced;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fml.LogicalSide;

import java.util.LinkedList;

public abstract class TileEntityBeamOutput extends TileEntityAdvanced implements ILaserNode
{
    public LinkedList<ILaserNode> nodeList = new LinkedList<ILaserNode>();
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public BlockPos targetVec = new BlockPos(-1, -1, -1);
    public float pitch;
    public float yaw;
    private BlockPos preLoadTarget = null;
    private BlockPos lastTargetVec = new BlockPos(-1, -1, -1);

    public TileEntityBeamOutput(BlockEntityType<? extends TileEntityBeamOutput> type)
    {
        super(type);
    }

    @Override
    public void tick()
    {
        if (this.preLoadTarget != null)
        {
            BlockEntity tileAtTarget = this.level.getBlockEntity(this.preLoadTarget);

            if (tileAtTarget != null && tileAtTarget instanceof ILaserNode)
            {
                this.setTarget((ILaserNode) tileAtTarget);
                this.preLoadTarget = null;
            }
        }

        super.tick();

        if (!this.targetVec.equals(this.lastTargetVec))
        {
            this.setChanged();
        }

        this.lastTargetVec = this.targetVec;

        if (this.level.isClientSide)
        {
            this.updateOrientation();
        }
        else if (this.targetVec.getX() == -1 && this.targetVec.getY() == -1 && this.targetVec.getZ() == -1)
        {
            this.initiateReflector();
        }

    }

    @Override
    public void setRemoved()
    {
        super.setRemoved();
        this.invalidateReflector();
    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    public void onLoad()
    {
    }

    @Override
    public void onChunkUnloaded()
    {
        this.invalidateReflector();
    }

    public void invalidateReflector()
    {
        for (ILaserNode node : this.nodeList)
        {
            node.removeNode(this);
        }

        this.nodeList.clear();
    }

    public void initiateReflector()
    {
        this.nodeList.clear();

        int chunkXMin = this.getBlockPos().getX() - 15 >> 4;
        int chunkZMin = this.getBlockPos().getZ() - 15 >> 4;
        int chunkXMax = this.getBlockPos().getX() + 15 >> 4;
        int chunkZMax = this.getBlockPos().getZ() + 15 >> 4;
        LevelChunk chunk;

        for (int cX = chunkXMin; cX <= chunkXMax; cX++)
        {
            for (int cZ = chunkZMin; cZ <= chunkZMax; cZ++)
            {
                chunk = this.level.getChunkSource().getChunk(cX, cZ, false);
                if (chunk != null)
                {
                    for (Object obj : chunk.getBlockEntities().values())
                    {
                        if (obj != this && obj instanceof ILaserNode)
                        {
                            BlockVec3 deltaPos = new BlockVec3(this).subtract(new BlockVec3(((ILaserNode) obj).getTile()));

                            if (deltaPos.x < 16 && deltaPos.y < 16 && deltaPos.z < 16)
                            {
                                ILaserNode laserNode = (ILaserNode) obj;

                                if (this.canConnectTo(laserNode) && laserNode.canConnectTo(this))
                                {
                                    this.addNode(laserNode);
                                    laserNode.addNode(this);
                                }
                            }
                        }
                    }
                }
            }
        }

        this.setTarget(this.nodeList.peekFirst());
    }

    @Override
    public void addNode(ILaserNode node)
    {
        int index = -1;

        for (int i = 0; i < this.nodeList.size(); i++)
        {
            if (new BlockVec3(this.nodeList.get(i).getTile()).equals(new BlockVec3(node.getTile())))
            {
                index = i;
                break;
            }
        }

        if (index != -1)
        {
            this.nodeList.set(index, node);
            return;
        }

        if (this.nodeList.isEmpty())
        {
            this.nodeList.add(node);
        }
        else
        {
            int nodeCompare = this.nodeList.get(0).compareTo(node, new BlockVec3(this));

            if (nodeCompare <= 0)
            {
                this.nodeList.addFirst(node);
                return;
            }

            nodeCompare = this.nodeList.get(this.nodeList.size() - 1).compareTo(node, new BlockVec3(this));

            if (nodeCompare >= 0)
            {
                this.nodeList.addLast(node);
                return;
            }

            index = 1;

            while (index < this.nodeList.size())
            {
                index++;
            }

            this.nodeList.add(index, node);
        }
    }

    @Override
    public void removeNode(ILaserNode node)
    {
        int index = -1;

        for (int i = 0; i < this.nodeList.size(); i++)
        {
            if (new BlockVec3(this.nodeList.get(i).getTile()).equals(new BlockVec3(node.getTile())))
            {
                index = i;
                break;
            }
        }

        if (new BlockVec3(node.getTile()).equals(new BlockVec3(this.targetVec)))
        {
            if (index == 0)
            {
                if (this.nodeList.size() > 1)
                {
                    this.setTarget(this.nodeList.get(index + 1));
                }
                else
                {
                    this.setTarget(null);
                }
            }
            else if (index > 0)
            {
                this.setTarget(this.nodeList.get(index - 1));
            }
            else
            {
                this.setTarget(null);
            }
        }

        if (index != -1)
        {
            this.nodeList.remove(index);
        }
    }

    public void updateOrientation()
    {
        if (this.getTarget() != null)
        {
            Vector3 direction = Vector3.subtract(this.getOutputPoint(false), this.getTarget().getInputPoint()).normalize();
            this.pitch = -Vector3.getAngle(new Vector3(-direction.x, -direction.y, -direction.z), new Vector3(0, 1, 0)) * Constants.RADIANS_TO_DEGREES + 90;
            this.yaw = (float) -(Math.atan2(direction.z, direction.x) * Constants.RADIANS_TO_DEGREES) + 90;
        }
    }

    @Override
    public BlockEntity getTile()
    {
        return this;
    }

    @Override
    public int compareTo(ILaserNode otherNode, BlockVec3 origin)
    {
        int thisDistance = new BlockVec3(this).subtract(origin).getMagnitudeSquared();
        int otherDistance = new BlockVec3(otherNode.getTile()).subtract(origin).getMagnitudeSquared();

        if (thisDistance < otherDistance)
        {
            return 1;
        }
        else if (thisDistance > otherDistance)
        {
            return -1;
        }

        return 0;
    }

    public InteractionResult onMachineActivated(Level worldIn, BlockPos pos, BlockState state, Player playerIn, InteractionHand hand, ItemStack heldItem, BlockHitResult hit)
    {
        if (this.nodeList.size() > 1)
        {
            int index = -1;

            if (this.getTarget() != null)
            {
                for (int i = 0; i < this.nodeList.size(); i++)
                {
                    if (new BlockVec3(this.nodeList.get(i).getTile()).equals(new BlockVec3(this.getTarget().getTile())))
                    {
                        index = i;
                        break;
                    }
                }
            }

            if (index == -1)
            {
                // This shouldn't happen, but just in case...
                this.initiateReflector();
            }
            else
            {
                index++;
                index %= this.nodeList.size();
                this.setTarget(this.nodeList.get(index));
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public ILaserNode getTarget()
    {
        if (this.targetVec.getX() != -1 || this.targetVec.getY() != -1 || this.targetVec.getZ() != -1)
        {
            BlockEntity tileAtTarget = this.level.getBlockEntity(this.targetVec);

            if (tileAtTarget != null && tileAtTarget instanceof ILaserNode)
            {
                return (ILaserNode) tileAtTarget;
            }

            return null;
        }

        return null;
    }

    public void setTarget(ILaserNode target)
    {
        if (target != null)
        {
            this.targetVec = target.getTile().getBlockPos();
        }
        else
        {
            this.targetVec = new BlockPos(-1, -1, -1);
        }
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);

        if (nbt.getBoolean("HasTarget"))
        {
            this.preLoadTarget = new BlockPos(nbt.getInt("TargetX"), nbt.getInt("TargetY"), nbt.getInt("TargetZ"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);

        nbt.putBoolean("HasTarget", this.getTarget() != null);

        if (this.getTarget() != null)
        {
            nbt.putInt("TargetX", this.getTarget().getTile().getBlockPos().getX());
            nbt.putInt("TargetY", this.getTarget().getTile().getBlockPos().getY());
            nbt.putInt("TargetZ", this.getTarget().getTile().getBlockPos().getZ());
        }

        return nbt;
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        return this.save(new CompoundTag());
    }
}
