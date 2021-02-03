package micdoodle8.mods.galacticraft.core.tile;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.*;
import micdoodle8.mods.galacticraft.core.items.ItemEmergencyKit;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TileEntityEmergencyBox extends BlockEntity implements TickableBlockEntity, IPacketReceiver
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.EMERGENCY_POST)
    public static BlockEntityType<TileEntityEmergencyBox> TYPE;

    private static final float SPEED = 4F;
    public float angleA = 0F;
    public float angleB = 0F;
    public float angleC = 0F;
    public float angleD = 0F;
    public float lastAngleA = 0F;
    public float lastAngleB = 0F;
    public float lastAngleC = 0F;
    public float lastAngleD = 0F;

    private boolean openN = false;
    private boolean openW = false;
    private boolean openS = false;
    private boolean openE = false;
    private int cooldown = 0;

    private final HashSet<BlockVec3> airToRestore = new HashSet<>();
    private boolean activated = false;
    private Vec3 vec3Centre;
    private Vec3 thisVec3;
    private AABB mobsAABB;

    public TileEntityEmergencyBox()
    {
        super(TYPE);
    }

    @Override
    public void tick()
    {
        if (!this.activated)
        {
            this.activated = true;
            this.setLightBlocks();
            this.thisVec3 = new Vec3(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ());
            this.vec3Centre = new Vec3(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D);
            this.mobsAABB = new AABB(this.worldPosition.getX() - 14, this.worldPosition.getY() - 7, this.worldPosition.getZ() - 14, this.worldPosition.getX() + 14, this.worldPosition.getY() + 7, this.worldPosition.getZ() + 14);
        }

        if (this.level.isClientSide)
        {
            if (this.openN && this.angleA < 90F)
            {
                this.lastAngleA = this.angleA;
                this.angleA += SPEED;
                if (this.angleA > 90F)
                {
                    this.angleA = 90F;
                }
            }
            if (this.openW && this.angleB < 90F)
            {
                this.lastAngleB = this.angleB;
                this.angleB += SPEED;
                if (this.angleB > 90F)
                {
                    this.angleB = 90F;
                }
            }
            if (this.openS && this.angleC < 90F)
            {
                this.lastAngleC = this.angleC;
                this.angleC += SPEED;
                if (this.angleC > 90F)
                {
                    this.angleC = 90F;
                }
            }
            if (this.openE && this.angleD < 90F)
            {
                this.lastAngleD = this.angleD;
                this.angleD += SPEED;
                if (this.angleD > 90F)
                {
                    this.angleD = 90F;
                }
            }

            if (!this.openN && this.angleA > 0F)
            {
                this.lastAngleA = this.angleA;
                this.angleA -= SPEED;
                if (this.angleA < 0F)
                {
                    this.angleA = 0F;
                }
            }
            if (!this.openW && this.angleB > 0F)
            {
                this.lastAngleB = this.angleB;
                this.angleB -= SPEED;
                if (this.angleB < 0F)
                {
                    this.angleB = 0F;
                }
            }
            if (!this.openS && this.angleC > 0F)
            {
                this.lastAngleC = this.angleC;
                this.angleC -= SPEED;
                if (this.angleC < 0F)
                {
                    this.angleC = 0F;
                }
            }
            if (!this.openE && this.angleD > 0F)
            {
                this.lastAngleD = this.angleD;
                this.angleD -= SPEED;
                if (this.angleD < 0F)
                {
                    this.angleD = 0F;
                }
            }
        }
        else
        {
            if (this.cooldown > 0)
            {
                this.cooldown--;
            }

            boolean updateRequired = false;
            if (this.openN)
            {
                boolean clash = false;
                BlockPos testPos = this.worldPosition.north(1);
                BlockState bs = this.level.getBlockState(testPos);
                if (!(bs.getBlock() instanceof AirBlock))
                {
                    VoxelShape neighbour = bs.getShape(this.level, testPos);
                    if (neighbour == Shapes.block())
                    {
                        clash = true;
                    }
                    VoxelShape check = Shapes.box(0.125D, 0.125D, 11 / 16D, 0.875D, 0.875D, 1D);
                    if (neighbour != Shapes.empty())
                    {
                        clash = check.bounds().intersects(neighbour.bounds());
                    }
                }
                if (clash)
                {
                    this.openN = false;
                    updateRequired = true;
                }
            }
            if (this.openS)
            {
                boolean clash = false;
                BlockPos testPos = this.worldPosition.south(1);
                BlockState bs = this.level.getBlockState(testPos);
                if (!(bs.getBlock() instanceof AirBlock))
                {
                    VoxelShape neighbour = bs.getShape(this.level, testPos);
                    if (neighbour == Shapes.block())
                    {
                        clash = true;
                    }
                    VoxelShape check = Shapes.box(0.125D, 0.125D, 0D, 0.875D, 0.875D, 5 / 16D);
                    if (neighbour != Shapes.empty())
                    {
                        clash = check.bounds().intersects(neighbour.bounds());
                    }
                }
                if (clash)
                {
                    this.openS = false;
                    updateRequired = true;
                }
            }
            if (this.openW)
            {
                boolean clash = false;
                BlockPos testPos = this.worldPosition.west(1);
                BlockState bs = this.level.getBlockState(testPos);
                if (!(bs.getBlock() instanceof AirBlock))
                {
                    VoxelShape neighbour = bs.getShape(this.level, testPos);
                    if (neighbour == Shapes.block())
                    {
                        clash = true;
                    }
                    VoxelShape check = Shapes.box(11 / 16D, 0.125D, 0.125D, 1D, 0.875D, 0.875D);
                    if (neighbour != Shapes.empty())
                    {
                        clash = check.bounds().intersects(neighbour.bounds());
                    }
                }
                if (clash)
                {
                    this.openW = false;
                    updateRequired = true;
                }
            }
            if (this.openE)
            {
                boolean clash = false;
                BlockPos testPos = this.worldPosition.east(1);
                BlockState bs = this.level.getBlockState(testPos);
                if (!(bs.getBlock() instanceof AirBlock))
                {
                    VoxelShape neighbour = bs.getShape(this.level, testPos);
                    if (neighbour == Shapes.block())
                    {
                        clash = true;
                    }
                    VoxelShape check = Shapes.box(0D, 0.125D, 0.125D, 5 / 16D, 0.875D, 0.875D);
                    if (neighbour != Shapes.empty())
                    {
                        clash = check.bounds().intersects(neighbour.bounds());
                    }
                }
                if (clash)
                {
                    this.openE = false;
                    updateRequired = true;
                }
            }

            if (updateRequired)
            {
                this.updateClients();
            }

            if (this.level.random.nextInt(15) == 0)
            {
                this.scareMobs();
            }
        }
    }

    private void scareMobs()
    {
        List<Entity> moblist = this.level.getEntities(null, mobsAABB, (entity) -> entity instanceof Enemy);
        if (!moblist.isEmpty())
        {
            for (Entity entry : moblist)
            {
                if (!(entry instanceof PathfinderMob && entry instanceof IEntityBreathable))
                {
                    continue;
                }
                PathfinderMob mob = (PathfinderMob) entry;
                PathNavigation nav = mob.getNavigation();
                if (nav == null)
                {
                    continue;
                }

                Vec3 vecNewTarget = RandomPos.getPosAvoid(mob, 12, 5, vec3Centre);
                if (vecNewTarget == null)
                {
                    vecNewTarget = RandomPos.getPosAvoid(mob, 14, 7, vec3Centre);
                    if (vecNewTarget == null)
                    {
                        continue;
                    }
                }
                double distanceNew = vecNewTarget.distanceToSqr(thisVec3);
                double distanceCurrent = thisVec3.distanceToSqr(new Vec3(mob.getX(), mob.getY(), mob.getZ()));
                if (distanceNew > distanceCurrent)
                {
                    Vec3 vecOldTarget = null;
                    if (nav.getPath() != null && !nav.getPath().isDone())
                    {
                        vecOldTarget = nav.getPath().currentPos(mob);
                    }
                    if (vecOldTarget == null || distanceCurrent > vecOldTarget.distanceToSqr(thisVec3))
                    {
                        nav.moveTo(vecNewTarget.x, vecNewTarget.y, vecNewTarget.z, 1.1D);
                    }
                }
            }
        }
    }

    public float getAngleA(float f)
    {
        float result = this.angleA + (this.angleA - this.lastAngleA) * f;
        if (result > 90F)
        {
            result = 90F;
        }
        if (result < 0F)
        {
            result = 0F;
        }
        return result;
    }

    public float getAngleB(float f)
    {
        float result = this.angleB + (this.angleB - this.lastAngleB) * f;
        if (result > 90F)
        {
            result = 90F;
        }
        if (result < 0F)
        {
            result = 0F;
        }
        return result;
    }

    public float getAngleC(float f)
    {
        float result = this.angleC + (this.angleC - this.lastAngleC) * f;
        if (result > 90F)
        {
            result = 90F;
        }
        if (result < 0F)
        {
            result = 0F;
        }
        return result;
    }

    public float getAngleD(float f)
    {
        float result = this.angleD + (this.angleD - this.lastAngleD) * f;
        if (result > 90F)
        {
            result = 90F;
        }
        if (result < 0F)
        {
            result = 0F;
        }
        return result;
    }

    public void click(Player player, Direction side, boolean kitted)
    {
        switch (side)
        {
        case NORTH:
            if (this.openN && this.cooldown == 0)
            {
                if (kitted)
                {
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(GCItems.SPACE_EMERGENCY_KIT), 0);
                    this.level.setBlock(this.worldPosition, GCBlocks.EMERGENCY_POST.defaultBlockState(), 3);
                    break;
                }
                else
                {
                    ItemStack stack = player.inventory.getSelected();
                    if (!stack.isEmpty() && stack.getItem() instanceof ItemEmergencyKit)
                    {
                        player.inventory.setItem(player.inventory.selected, ItemStack.EMPTY);
                        this.level.setBlock(this.worldPosition, GCBlocks.EMERGENCY_POST_KIT.defaultBlockState(), 3);
                        this.openW = false;
                        this.openS = false;
                        this.openE = false;
                    }
                }
            }
            this.cooldown = 12;
            this.openN = !this.openN;
            this.updateClients();
            break;
        case WEST:
            if (this.openW && this.cooldown == 0)
            {
                if (kitted)
                {
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(GCItems.SPACE_EMERGENCY_KIT), 0);
                    this.level.setBlock(this.worldPosition, GCBlocks.EMERGENCY_POST.defaultBlockState(), 3);
                    break;
                }
                else
                {
                    ItemStack stack = player.inventory.getSelected();
                    if (!stack.isEmpty() && stack.getItem() instanceof ItemEmergencyKit)
                    {
                        player.inventory.setItem(player.inventory.selected, ItemStack.EMPTY);
                        this.level.setBlock(this.worldPosition, GCBlocks.EMERGENCY_POST_KIT.defaultBlockState(), 3);
                        this.openN = false;
                        this.openS = false;
                        this.openE = false;
                    }
                }
            }
            this.cooldown = 12;
            this.openW = !this.openW;
            this.updateClients();
            break;
        case SOUTH:
            if (this.openS && this.cooldown == 0)
            {
                if (kitted)
                {
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(GCItems.SPACE_EMERGENCY_KIT), 0);
                    this.level.setBlock(this.worldPosition, GCBlocks.EMERGENCY_POST.defaultBlockState(), 3);
                    break;
                }
                else
                {
                    ItemStack stack = player.inventory.getSelected();
                    if (!stack.isEmpty() && stack.getItem() instanceof ItemEmergencyKit)
                    {
                        player.inventory.setItem(player.inventory.selected, ItemStack.EMPTY);
                        this.level.setBlock(this.worldPosition, GCBlocks.EMERGENCY_POST_KIT.defaultBlockState(), 3);
                        this.openN = false;
                        this.openW = false;
                        this.openE = false;
                    }
                }
            }
            this.cooldown = 12;
            this.openS = !this.openS;
            this.updateClients();
            break;
        case EAST:
            if (this.openE && this.cooldown == 0)
            {
                if (kitted)
                {
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(GCItems.SPACE_EMERGENCY_KIT), 0);
                    this.level.setBlock(this.worldPosition, GCBlocks.EMERGENCY_POST.defaultBlockState(), 3);
                    break;
                }
                else
                {
                    ItemStack stack = player.inventory.getSelected();
                    if (!stack.isEmpty() && stack.getItem() instanceof ItemEmergencyKit)
                    {
                        player.inventory.setItem(player.inventory.selected, ItemStack.EMPTY);
                        this.level.setBlock(this.worldPosition, GCBlocks.EMERGENCY_POST_KIT.defaultBlockState(), 3);
                        this.openN = false;
                        this.openW = false;
                        this.openS = false;
                    }
                }
            }
            this.cooldown = 12;
            this.openE = !this.openE;
            this.updateClients();
            break;
        default:
        }
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        int data = nbt.getInt("open");
        this.openN = (data & 1) == 1;
        this.openW = (data & 2) == 2;
        this.openS = (data & 4) == 4;
        this.openE = (data & 8) == 8;
        if (GCCoreUtil.getEffectiveSide() == LogicalSide.SERVER)
        {
            this.airToRestore.clear();
            ListTag airBlocks = nbt.getList("air", 10);
            if (airBlocks.size() > 0)
            {
                for (int j = airBlocks.size() - 1; j >= 0; j--)
                {
                    CompoundTag tag1 = airBlocks.getCompound(j);
                    if (tag1 != null)
                    {
                        this.airToRestore.add(BlockVec3.read(tag1));
                    }
                }
            }
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        int data = (this.openN ? 1 : 0) + (this.openW ? 2 : 0) + (this.openS ? 4 : 0) + (this.openE ? 8 : 0);
        nbt.putInt("open", data);

        ListTag airBlocks = new ListTag();
        for (BlockVec3 vec : this.airToRestore)
        {
            CompoundTag tag = new CompoundTag();
            vec.write(tag);
            airBlocks.add(tag);
        }
        nbt.put("air", airBlocks);
        return nbt;
    }

    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        if (this.level.isClientSide)
        {
            return;
        }

        int data = (this.openN ? 1 : 0) + (this.openW ? 2 : 0) + (this.openS ? 4 : 0) + (this.openE ? 8 : 0);
        sendData.add((byte) data);
        for (BlockVec3 vec : this.airToRestore)
        {
            sendData.add(vec);
        }
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        if (this.level.isClientSide)
        {
            try
            {
                int data = buffer.readByte();
                this.openN = (data & 1) == 1;
                this.openW = (data & 2) == 2;
                this.openS = (data & 4) == 4;
                this.openE = (data & 8) == 8;
                while (buffer.readableBytes() >= 12)
                {
                    int x = buffer.readInt();
                    int y = buffer.readInt();
                    int z = buffer.readInt();
                    this.airToRestore.add(new BlockVec3(x, y, z));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoad()
    {
        if (this.level.isClientSide)
        {
            //Request any networked information from server on first client tick
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
        }
    }

    private void updateClients()
    {
        GalacticraftCore.packetPipeline.sendToAllAround(new PacketDynamic(this), new PacketDistributor.TargetPoint(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), 128, GCCoreUtil.getDimensionType(this.level)));
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState)
//    {
//        return oldState.getBlock() != newState.getBlock();
//    }

    private void brightenAir(BlockPos blockpos, BlockState newState)
    {
        LevelChunk chunk = this.level.getChunkAt(blockpos);
        BlockState oldState = chunk.setBlockState(blockpos, newState, false);
        if (this.level.isClientSide && oldState != null)
        {
            this.level.markAndNotifyBlock(blockpos, chunk, oldState, newState, 2);
        }
        //No block tick on server - not necessary for changing air to air (also must not trigger a sealer edge check!)
        this.airToRestore.add(new BlockVec3(blockpos));
//        this.world.checkLightFor(LightType.BLOCK, blockpos); TODO Lighting
        this.setChanged();
    }

    private void setDarkerAir(BlockVec3 vec)
    {
        BlockPos blockpos = vec.toBlockPos();
        Block b = this.level.getBlockState(blockpos).getBlock();
        BlockState newState;
        if (b == GCBlocks.BRIGHT_AIR)
        {
            newState = Blocks.AIR.defaultBlockState();
        }
        else if (b == GCBlocks.BRIGHT_BREATHEABLE_AIR)
        {
            newState = GCBlocks.BREATHEABLE_AIR.defaultBlockState();
        }
        else
        {
            return;
        }

        LevelChunk chunk = this.level.getChunkAt(blockpos);
        BlockState oldState = chunk.setBlockState(blockpos, newState, false);
        if (this.level.isClientSide && oldState != null)
        {
            this.level.markAndNotifyBlock(blockpos, chunk, oldState, newState, 2);
        }
//        this.world.checkLightFor(LightType.BLOCK, blockpos);  TODO Lighting
    }

    private void revertAir()
    {
        if (this.airToRestore.isEmpty())
        {
            return;
        }

        int index = 0;
        for (BlockVec3 vec : this.airToRestore)
        {
            this.setDarkerAir(vec);
        }
        this.airToRestore.clear();
        this.setChanged();
    }

    @Override
    public void setRemoved()
    {
        this.revertAir();
        super.setRemoved();
    }

    private void setLightBlocks()
    {
        this.setLightBlock(new BlockPos(this.worldPosition.getX() - 6, this.worldPosition.getY(), this.worldPosition.getZ()));
        this.setLightBlock(new BlockPos(this.worldPosition.getX() + 6, this.worldPosition.getY(), this.worldPosition.getZ()));
        this.setLightBlock(new BlockPos(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ() - 6));
        this.setLightBlock(new BlockPos(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ() + 6));
        this.setLightBlock(new BlockPos(this.worldPosition.getX() - 5, this.worldPosition.getY(), this.worldPosition.getZ() - 5));
        this.setLightBlock(new BlockPos(this.worldPosition.getX() - 5, this.worldPosition.getY(), this.worldPosition.getZ() + 5));
        this.setLightBlock(new BlockPos(this.worldPosition.getX() + 5, this.worldPosition.getY(), this.worldPosition.getZ() + 5));
        this.setLightBlock(new BlockPos(this.worldPosition.getX() + 5, this.worldPosition.getY(), this.worldPosition.getZ() - 5));
    }

    private boolean setLightBlock(BlockPos blockPos)
    {
        BlockState bs = this.level.getBlockState(blockPos);
        if (bs.getBlock() == Blocks.AIR)
        {
            this.brightenAir(blockPos, GCBlocks.BRIGHT_AIR.defaultBlockState());
            return true;
        }
        else if (bs.getBlock() == GCBlocks.BREATHEABLE_AIR)
        {
            this.brightenAir(blockPos, GCBlocks.BRIGHT_BREATHEABLE_AIR.defaultBlockState());
            return true;
        }

        blockPos = blockPos.above(1);
        bs = this.level.getBlockState(blockPos);
        if (bs.getBlock() == Blocks.AIR)
        {
            this.brightenAir(blockPos, GCBlocks.BRIGHT_AIR.defaultBlockState());
            return true;
        }
        else if (bs.getBlock() == GCBlocks.BREATHEABLE_AIR)
        {
            this.brightenAir(blockPos, GCBlocks.BRIGHT_BREATHEABLE_AIR.defaultBlockState());
            return true;
        }

        return false;
    }
}
