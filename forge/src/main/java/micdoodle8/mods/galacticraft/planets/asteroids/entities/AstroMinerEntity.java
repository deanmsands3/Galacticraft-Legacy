package micdoodle8.mods.galacticraft.planets.asteroids.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.IAntiGrav;
import micdoodle8.mods.galacticraft.api.entity.IEntityNoisy;
import micdoodle8.mods.galacticraft.api.entity.ITelemetry;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.util.*;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.client.sounds.SoundUpdaterMiner;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.DimensionAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.tick.AsteroidsTickHandlerServer;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.minecraft.block.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.network.NetworkHooks;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class AstroMinerEntity extends Entity implements Container, IPacketReceiver, IEntityNoisy, IAntiGrav, ITelemetry
{
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(AstroMinerEntity.class, EntityDataSerializers.FLOAT);

    public static final int MINE_LENGTH = 24;
    public static final int MINE_LENGTH_AST = 12;
    private static final int MAXENERGY = 12000;
    private static final int RETURNENERGY = 1000;
    private static final int RETURNDROPS = 10;
    private static final int INV_SIZE = 227;
    private static final float cLENGTH = 2.6F;
    private static final float cWIDTH = 1.8F;
    private static final float cHEIGHT = 1.7F;
    private static final double SPEEDUP = 2.5D;

    public static final int AISTATE_OFFLINE = -1;
    public static final int AISTATE_STUCK = 0;
    public static final int AISTATE_ATBASE = 1;
    public static final int AISTATE_TRAVELLING = 2;
    public static final int AISTATE_MINING = 3;
    public static final int AISTATE_RETURNING = 4;
    public static final int AISTATE_DOCKING = 5;

    public static final int FAIL_BASEDESTROYED = 3;
    public static final int FAIL_OUTOFENERGY = 4;
    public static final int FAIL_RETURNPATHBLOCKED = 5;
    public static final int FAIL_ANOTHERWASLINKED = 8;

    private final boolean TEMPDEBUG = false;
    private final boolean TEMPFAST = false;

    public boolean serverTick = false;
    public int serverTickSave;
    public NonNullList<ItemStack> stacks;

    public int energyLevel;
    public int mineCount = 0;
    public float targetYaw;
    public float targetPitch;

    public int AIstate;
    public int timeInCurrentState = 0;
    public ServerPlayer playerMP = null;
    private UUID playerUUID;
    private BlockVec3 posTarget;
    private BlockVec3 posBase;
    private BlockVec3 waypointBase;
    private final LinkedList<BlockVec3> wayPoints = new LinkedList<>();
    private final LinkedList<BlockVec3> minePoints = new LinkedList<>();
    private BlockVec3 minePointCurrent = null;
    private Direction baseFacing;
    public Direction facing;
    private Direction facingAI;
    private Direction lastFacing;
    private static final BlockVec3[] headings = {
            new BlockVec3(0, -1, 0),
            new BlockVec3(0, 1, 0),
            new BlockVec3(0, 0, -1),
            new BlockVec3(0, 0, 1),
            new BlockVec3(-1, 0, 0),
            new BlockVec3(1, 0, 0)};
    private static final BlockVec3[] headings2 = {
            new BlockVec3(0, -3, 0),
            new BlockVec3(0, 2, 0),
            new BlockVec3(0, 0, -3),
            new BlockVec3(0, 0, 2),
            new BlockVec3(-3, 0, 0),
            new BlockVec3(2, 0, 0)};

    private final int baseSafeRadius = 32;
    private final double speedbase = TEMPFAST ? 0.16D : 0.022D;
    private double speed = speedbase;
    private final float rotSpeedBase = TEMPFAST ? 8F : 1.5F;
    private float rotSpeed = rotSpeedBase;
    private double speedup = SPEEDUP;
    private boolean noSpeedup = false;  //This stops the miner getting stuck at turning points
    public float shipDamage;
    public int currentDamage;
    public int timeSinceHit;
    private boolean flagLink = false;
    private boolean flagCheckPlayer = false;
    private boolean toAddToServer = AsteroidsTickHandlerServer.loadingSavedChunks.get();

    //To do:
    //   break the entity drops it as an item

    private int turnProgress;
    private double minecartX;
    private double minecartY;
    private double minecartZ;
    private double minecartYaw;
    private double minecartPitch;
    @OnlyIn(Dist.CLIENT)
    private double velocityX;
    @OnlyIn(Dist.CLIENT)
    private double velocityY;
    @OnlyIn(Dist.CLIENT)
    private double velocityZ;

    private int tryBlockLimit;
    private int inventoryDrops;
    public boolean stopForTurn;

    private static final ArrayList<Block> noMineList = new ArrayList<>();
    public static BlockState blockingBlock = Blocks.AIR.defaultBlockState();
    private int givenFailMessage = 0;
    private BlockVec3 mineLast = null;
    private int mineCountDown = 0;
    private int pathBlockedCount = 0;
    public LinkedList<BlockVec3> laserBlocks = new LinkedList<>();
    public LinkedList<Integer> laserTimes = new LinkedList<>();
    public float retraction = 1F;
    protected AbstractTickableSoundInstance soundUpdater;
    private boolean soundToStop = false;
    private boolean spawnedInCreative = false;
    private int serverIndex;

    static
    {
        //Avoid:
        // Overworld: avoid lava source blocks, mossy cobble, End Portal and Fortress blocks
        // railtrack, levers, redstone dust, GC walkways,
        //Anything with a tileEntity will also be avoided:
        // spawners, chests, oxygen pipes, hydrogen pipes, wires
        noMineList.add(Blocks.BEDROCK);
        noMineList.add(Blocks.MOSSY_COBBLESTONE);
        noMineList.add(Blocks.END_PORTAL);
        noMineList.add(Blocks.END_PORTAL_FRAME);
        noMineList.add(Blocks.NETHER_PORTAL);
        noMineList.add(Blocks.STONE_BRICKS);
        noMineList.add(Blocks.FARMLAND);
        noMineList.add(Blocks.RAIL);
        noMineList.add(Blocks.LEVER);
        noMineList.add(Blocks.REDSTONE_WIRE);
        noMineList.add(AsteroidBlocks.WALKWAY);
        //TODO:
        //Add configurable blacklist
    }

//    public EntityAstroMiner(World world, NonNullList<ItemStack> cargo, int energy)
//    {
//        this(world);
//        this.toAddToServer = true;
//        this.stacks = cargo;
//        this.energyLevel = energy;
//    }

    public AstroMinerEntity(EntityType<? extends AstroMinerEntity> type, Level worldIn)
    {
        super(type, worldIn);
        this.facing = Direction.NORTH;
        this.blocksBuilding = true;
        this.noCulling = true;
//        this.isImmuneToFire = true;
//        this.width = cLENGTH;
//        this.height = cWIDTH;
//        this.setSize(cLENGTH, cWIDTH);
//        this.myEntitySize = Entity.EnumEntitySize.SIZE_6;
//        this.dataManager.addObject(this.currentDamage, new Integer(0));
//        this.dataManager.addObject(this.timeSinceHit, new Integer(0));
        this.noPhysics = true;

        if (level != null && level.isClientSide)
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
        }
    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance)
    {
        double d0 = this.getBoundingBox().getSize();

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * 5.0;
        return distance < d0 * d0;
    }


    @Override
    protected void defineSynchedData()
    {
        this.entityData.define(DAMAGE, 0.0F);
    }

    @Override
    public int getContainerSize()
    {
        return this.stacks.size();
    }

    @Override
    public ItemStack getItem(int var1)
    {
        return this.stacks.get(var1);
    }

    @Override
    public ItemStack removeItem(int index, int count)
    {
        ItemStack itemstack = ContainerHelper.removeItem(this.stacks, index, count);

        if (!itemstack.isEmpty())
        {
            this.setChanged();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index)
    {
        ItemStack oldstack = ContainerHelper.takeItem(this.stacks, index);
        if (!oldstack.isEmpty())
        {
            this.setChanged();
        }
        return oldstack;
    }

    @Override
    public void setItem(int index, ItemStack stack)
    {
        this.stacks.set(index, stack);

        if (stack.getCount() > this.getMaxStackSize())
        {
            stack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.stacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

//    @Override
//    public String getName()
//    {
//        return GCCoreUtil.translate("entity.astro_miner");
//    }

    @Override
    public int getMaxStackSize()
    {
        return 64;
    }

    @Override
    public boolean stillValid(Player var1)
    {
        return this.isAlive() && var1.distanceToSqr(this) <= 64.0D;
    }

    @Override
    public void setChanged()
    {
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemstack)
    {
        return false;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    //We don't use these because we use forge containers
    @Override
    public void startOpen(Player player)
    {
    }

    //We don't use these because we use forge containers
    @Override
    public void stopOpen(Player player)
    {
    }

//    @Override
//    public int getField(int id)
//    {
//        return 0;
//    }
//
//    @Override
//    public void setField(int id, int value)
//    {
//    }

//    @Override
//    public int getFieldCount()
//    {
//        return 0;
//    }

    @Override
    public void clearContent()
    {

    }

    private boolean emptyInventory(TileEntityMinerBase minerBase)
    {
        boolean doneOne = false;
        for (int i = 0; i < this.stacks.size(); i++)
        {
            ItemStack stack = this.stacks.get(i);
            if (stack.isEmpty())
            {
                continue;
            }
            int sizeprev = stack.getCount();
            minerBase.addToInventory(stack);
            if (stack.isEmpty())
            {
                this.stacks.set(i, ItemStack.EMPTY);
                this.setChanged();
                return true;
            }
            else if (stack.getCount() < sizeprev)
            {
                this.stacks.set(i, stack);
                this.setChanged();
                //Something was transferred although some stacks remaining
                return true;
            }
        }
        //No stacks were transferred
        return false;
    }

    @Override
    public void tick()
    {
        this.serverTick = false;
        if (this.getY() < -64.0D)
        {
            this.remove();
            return;
        }

        if (this.getDamage() > 0.0F)
        {
            this.setDamage(this.getDamage() - 1.0F);
        }

        stopForTurn = !this.checkRotation();
        this.facing = this.getFacingFromRotation();
        this.setBoundingBoxForFacing();

        if (this.level.isClientSide)
        {
            //CLIENT CODE
            if (this.turnProgress == 0)
            {
                this.turnProgress++;
                if (this.AIstate < AISTATE_TRAVELLING)
                {
                    //It should be stationary, so this deals with the spooky movement (due to minor differences between server and client position)
                    this.setPosRaw(this.minecartX, this.minecartY, this.minecartZ);
                }
                else
                {
                    double diffX = this.minecartX - this.getX();
                    double diffY = this.minecartY - this.getY();
                    double diffZ = this.minecartZ - this.getZ();
                    if (Math.abs(diffX) > 1.0D || Math.abs(diffY) > 1.0D || Math.abs(diffZ) > 1.0D)
                    {
                        this.setPosRaw(this.minecartX, this.minecartY, this.minecartZ);
                    }
                    else
                    {
                        if (Math.abs(diffX) > Math.abs(this.getDeltaMovement().x))
                        {
                            this.setDeltaMovement(this.getDeltaMovement().x + diffX / 10.0, this.getDeltaMovement().y, this.getDeltaMovement().z);
                        }
                        if (Math.abs(diffY) > Math.abs(this.getDeltaMovement().y))
                        {
                            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y + diffY / 10.0, this.getDeltaMovement().z);
                        }
                        if (Math.abs(diffZ) > Math.abs(this.getDeltaMovement().z))
                        {
                            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z + diffZ / 10.0);
                        }
                    }
                }
            }
            this.setPosRaw(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
            setBoundingBox(getBoundingBox().move(this.getDeltaMovement()));
            this.setRot(this.yRot, this.xRot);
            if (this.AIstate == AISTATE_MINING && this.tickCount % 2 == 0)
            {
                this.prepareMoveClient(TEMPFAST ? 8 : 1, 2);
            }

            //Sound updates on client
            if (this.AIstate < AISTATE_ATBASE)
            {
                this.stopRocketSound();
            }
            return;
        }

        if (this.toAddToServer)
        {
            this.toAddToServer = false;
            this.serverIndex = AsteroidsTickHandlerServer.monitorMiner(this);
        }

        //SERVER CODE
        if (this.tickCount % 10 == 0 || this.flagLink)
        {
            this.flagLink = false;
            this.checkPlayer();
            if (posBase.blockExists(this.level))
            {
                BlockEntity tileEntity = posBase.getTileEntity(this.level);
                if (tileEntity instanceof TileEntityMinerBase && ((TileEntityMinerBase) tileEntity).isMaster && !tileEntity.isRemoved())
                {
                    //Create link with base on loading the EntityAstroMiner
                    UUID linker = ((TileEntityMinerBase) tileEntity).getLinkedMiner();
                    if (!this.getUUID().equals(linker))
                    {
                        if (linker == null)
                        {
                            ((TileEntityMinerBase) tileEntity).linkMiner(this);
                        }
                        else
                        {
                            this.freeze(FAIL_ANOTHERWASLINKED);
                            return;
                        }
                    }
                    else if (((TileEntityMinerBase) tileEntity).linkedMiner != this)
                    {
                        ((TileEntityMinerBase) tileEntity).linkMiner(this);
                    }
                }
                else
                {
                    if (this.playerMP != null && (this.givenFailMessage & (1 << FAIL_BASEDESTROYED)) == 0)
                    {
                        this.playerMP.sendMessage(new TextComponent(GCCoreUtil.translate("gui.message.astro_miner" + FAIL_BASEDESTROYED + ".fail")));
                        this.givenFailMessage += (1 << FAIL_BASEDESTROYED);
                        //Continue mining even though base was destroyed - maybe it will be replaced
                    }
                }
            }
        }
        else if (this.flagCheckPlayer)
        {
            this.checkPlayer();
        }

        if (this.playerMP == null)
        {
            //Go into dormant state if player is offline
            //but do not actually set the dormant state on the server, so can resume immediately if player comes online
            if (this.getDeltaMovement().x != 0 || this.getDeltaMovement().y != 0 || this.getDeltaMovement().z != 0)
            {
                this.setDeltaMovement(0.0, 0.0, 0.0);
                GalacticraftCore.packetPipeline.sendToDimension(new PacketDynamic(this), GCCoreUtil.getDimensionType(this.level));
            }
            return;
        }

        if (this.lastFacing != this.facingAI)
        {
            this.lastFacing = this.facingAI;
            this.prepareMove(12, 0);
            this.prepareMove(12, 1);
            this.prepareMove(12, 2);
        }

        this.xOld = this.getX();
        this.yOld = this.getY();
        this.zOld = this.getZ();
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        this.xRotO = this.xRot;
        this.yRotO = this.yRot;

        if (this.AIstate > AISTATE_ATBASE)
        {
            if (this.energyLevel <= 0)
            {
                this.freeze(FAIL_OUTOFENERGY);
            }
            else if (!(this.level.getDimension() instanceof DimensionAsteroids) && this.tickCount % 2 == 0)
            {
                this.energyLevel--;
            }
            //No energy consumption when moving in space in Asteroids dimension (this reduces the risk of the Astro Miner becoming stranded!)
        }

        switch (this.AIstate)
        {
        case AISTATE_STUCK:
            //TODO blinking distress light or something?
            //Attempt to re-start every 30 seconds or so
            if (this.tickCount % 600 == 0)
            {
                if ((this.givenFailMessage & 8) > 0)
                {
                    //The base was destroyed - see if it has been replaced?
                    this.atBase();
                }
                else
                {
                    //See if the return path has been unblocked, and give a small amount of backup energy to try to get home
                    this.AIstate = AISTATE_RETURNING;
                    if (this.energyLevel <= 0)
                    {
                        this.energyLevel = 20;
                    }
                }
            }
            break;
        case AISTATE_ATBASE:
            this.atBase();
            break;
        case AISTATE_TRAVELLING:
            if (!this.moveToTarget())
            {
                this.prepareMove(TEMPFAST ? 8 : 2, 2);
            }
            break;
        case AISTATE_MINING:
            if (!this.doMining() && this.tickCount % 2 == 0)
            {
                this.energyLevel--;
                this.prepareMove(TEMPFAST ? 8 : 1, 2);
            }
            break;
        case AISTATE_RETURNING:
            this.moveToBase();
            this.prepareMove(TEMPFAST ? 8 : 4, 1);
            break;
        case AISTATE_DOCKING:
            if (this.waypointBase != null)
            {
                this.speed = speedbase / 1.6;
                this.rotSpeed = rotSpeedBase / 1.6F;
                if (this.moveToPos(this.waypointBase, true))
                {
                    this.AIstate = AISTATE_ATBASE;
                    this.setDeltaMovement(0.0, 0.0, 0.0);
                    this.speed = speedbase;
                    this.rotSpeed = rotSpeedBase;
                }
            }
            else
            {
                GCLog.severe("AstroMiner missing base position: this is a bug.");
                this.AIstate = AISTATE_STUCK;
            }
            break;
        }

        GalacticraftCore.packetPipeline.sendToDimension(new PacketDynamic(this), GCCoreUtil.getDimensionType(this.level));

        this.setPosRaw(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
        setBoundingBox(getBoundingBox().move(this.getDeltaMovement()));

/*        if (this.dataManager.get(this.timeSinceHit) > 0)
        {
            this.dataManager.set(this.timeSinceHit, Integer.valueOf(this.dataManager.get(this.timeSinceHit) - 1));
        }

        if (this.dataManager.get(this.currentDamage) > 0)
        {
            this.dataManager.set(this.currentDamage, Integer.valueOf(this.dataManager.get(this.currentDamage) - 1));
        }       
*/
    }

    private void checkPlayer()
    {
        if (this.playerMP == null)
        {
            if (this.playerUUID != null)
            {
                this.playerMP = PlayerUtil.getPlayerByUUID(this.playerUUID);
            }
        }
        else
        {
            if (!PlayerUtil.isPlayerOnline(this.playerMP))
            {
                this.playerMP = null;
            }
        }
    }

    private void freeze(int i)
    {
        this.AIstate = AISTATE_STUCK;
        this.setDeltaMovement(0.0, 0.0, 0.0);
        if (this.playerMP != null && (this.givenFailMessage & (1 << i)) == 0)
        {
            this.playerMP.sendMessage(new TextComponent(GCCoreUtil.translate("gui.message.astro_miner" + i + ".fail")));
            this.givenFailMessage += (1 << i);
        }
    }

    //packet with AIstate, energy, rotationP + Y, mining data count
    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        this.AIstate = buffer.readInt();
        this.energyLevel = buffer.readInt();
        this.targetPitch = buffer.readFloat();
        this.targetYaw = buffer.readFloat();
        this.mineCount = buffer.readInt();
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        BlockPos pos = new BlockPos(x, y, z);
        if (this.level.hasChunkAt(pos))
        {
            BlockEntity tile = this.level.getBlockEntity(pos);
            if (tile instanceof TileEntityMinerBase)
            {
                ((TileEntityMinerBase) tile).linkedMiner = this;
                ((TileEntityMinerBase) tile).linkCountDown = 20;
            }
        }
    }

    @Override
    public void getNetworkedData(ArrayList<Object> list)
    {
        if (this.level.isClientSide)
        {
            return;
        }
        list.add(this.playerMP == null ? AISTATE_OFFLINE : this.AIstate);
        list.add(this.energyLevel);
        list.add(this.targetPitch);
        list.add(this.targetYaw);
        list.add(this.mineCount);
        list.add(this.posBase.x);
        list.add(this.posBase.y);
        list.add(this.posBase.z);
    }

    public void recall()
    {
        if (this.AIstate > AISTATE_ATBASE && this.AIstate < AISTATE_RETURNING)
        {
            AIstate = AISTATE_RETURNING;
            this.pathBlockedCount = 0;
        }
    }

    private Direction getFacingFromRotation()
    {
        if (this.xRot > 45F)
        {
            return Direction.UP;
        }
        if (this.xRot < -45F)
        {
            return Direction.DOWN;
        }
        float rY = this.yRot % 360F;
        //rotationYaw 5 90 4 270 2 180 3 0
        if (rY < 45F || rY > 315F)
        {
            return Direction.SOUTH;
        }
        if (rY < 135F)
        {
            return Direction.EAST;
        }
        if (rY < 225F)
        {
            return Direction.NORTH;
        }
        return Direction.WEST;
    }

    private void atBase()
    {
        BlockEntity tileEntity = posBase.getTileEntity(this.level);

        if (!(tileEntity instanceof TileEntityMinerBase) || tileEntity.isRemoved() || !((TileEntityMinerBase) tileEntity).isMaster)
        {
            this.freeze(FAIL_BASEDESTROYED);
            return;
        }

        TileEntityMinerBase minerBase = (TileEntityMinerBase) tileEntity;
        //If it's successfully reached its base, clear all fail messages except number 6, which is that all mining areas are finished (see below)
        this.givenFailMessage &= 64;
        this.wayPoints.clear();

        boolean somethingTransferred = true;
        if (this.tickCount % 5 == 0)
        {
            somethingTransferred = this.emptyInventory(minerBase);
        }
        this.inventoryDrops = 0;

        // Recharge
        if (minerBase.hasEnoughEnergyToRun && this.energyLevel < MAXENERGY)
        {
            this.energyLevel += 16;
            minerBase.storage.extractEnergyGC(minerBase.storage.getMaxExtract(), false);
        }

        // When fully charged, set off again
        if (this.energyLevel >= MAXENERGY && !somethingTransferred && this.hasHoldSpace())
        {
            this.energyLevel = MAXENERGY;
            if (this.findNextTarget(minerBase))
            {
                this.AIstate = AISTATE_TRAVELLING;
                this.wayPoints.add(this.waypointBase.clone());
                this.mineCount = 0;
            }
            else
            {
                if (this.playerMP != null && (this.givenFailMessage & 64) == 0)
                {
                    this.playerMP.sendMessage(new TextComponent(GCCoreUtil.translate("gui.message.astro_miner6.fail")));
                    this.givenFailMessage += 64;
                }
            }
        }
    }

    private boolean hasHoldSpace()
    {
        for (int i = 0; i < this.getContainerSize(); i++)
        {
            if (this.stacks.get(i).isEmpty())
            {
                return true;
            }
        }
        return false;
    }

    private boolean findNextTarget(TileEntityMinerBase minerBase)
    {
        //If mining has finished, or path has been blocked two or more times, try mining elsewhere
        if (!this.minePoints.isEmpty() && this.pathBlockedCount < 2)
        {
            this.posTarget = this.minePoints.getFirst().clone();
            GCLog.debug("Still mining at: " + posTarget.toString() + " Remaining shafts: " + this.minePoints.size());
            return true;
        }

        // Target is completely mined: change target
        this.posTarget = minerBase.findNextTarget();
        this.pathBlockedCount = 0;

        //No more mining targets, the whole area is mined
        if (this.posTarget == null)
        {
            return false;
        }

        GCLog.debug("Miner target: " + posTarget.toString());

        return true;
    }

    /**
     * @return True if reached a turning point
     */
    private boolean moveToTarget()
    {
        if (this.energyLevel < RETURNENERGY || this.inventoryDrops > RETURNDROPS)
        {
            AIstate = AISTATE_RETURNING;
            this.pathBlockedCount = 0;
            return true;
        }

        if (this.posTarget == null)
        {
            GCLog.severe("AstroMiner missing target: this is a bug.");
            AIstate = AISTATE_STUCK;
            return true;
        }

        if (this.moveToPos(this.posTarget, false))
        {
            AIstate = AISTATE_MINING;
            wayPoints.add(this.posTarget.clone());
            this.setMinePoints();
            return true;
        }

        return false;
    }

    private void moveToBase()
    {
        if (this.wayPoints.size() == 0)
        {
            //When it gets there: stop and reverse in!
            AIstate = AISTATE_DOCKING;
            if (this.waypointBase != null)
            {
                //Teleport back to base in case of any serious problem
                this.setPos(this.waypointBase.x, this.waypointBase.y, this.waypointBase.z);
                this.facingAI = this.baseFacing;
            }
            return;
        }

        if (this.moveToPos(this.wayPoints.getLast(), true))
        {
            this.wayPoints.removeLast();
        }
    }

    private void setMinePoints()
    {
        //Still some areas left to mine from last visit (maybe it was full or out of power?)
        if (this.minePoints.size() > 0)
        {
            return;
        }

        BlockVec3 inFront = new BlockVec3(Mth.floor(this.getX() + 0.5D), Mth.floor(this.getY() + 1.5D), Mth.floor(this.getZ() + 0.5D));
        int otherEnd = (this.level.getDimension() instanceof DimensionAsteroids) ? MINE_LENGTH_AST : MINE_LENGTH;
        if (this.baseFacing == Direction.NORTH || this.baseFacing == Direction.WEST)
        {
            otherEnd = -otherEnd;
        }
        switch (this.baseFacing)
        {
        case NORTH:
        case SOUTH:
            this.minePoints.add(inFront.clone().translate(0, 0, otherEnd));
            this.minePoints.add(inFront.clone().translate(4, 0, otherEnd));
            this.minePoints.add(inFront.clone().translate(4, 0, 0));
            this.minePoints.add(inFront.clone().translate(2, 3, 0));
            this.minePoints.add(inFront.clone().translate(2, 3, otherEnd));
            this.minePoints.add(inFront.clone().translate(-2, 3, otherEnd));
            this.minePoints.add(inFront.clone().translate(-2, 3, 0));
            this.minePoints.add(inFront.clone().translate(-4, 0, 0));
            this.minePoints.add(inFront.clone().translate(-4, 0, otherEnd));
            this.minePoints.add(inFront.clone().translate(-2, -3, otherEnd));
            this.minePoints.add(inFront.clone().translate(-2, -3, 0));
            this.minePoints.add(inFront.clone().translate(2, -3, 0));
            this.minePoints.add(inFront.clone().translate(2, -3, otherEnd));
            this.minePoints.add(inFront.clone().translate(0, 0, otherEnd));
            break;
        case WEST:
        case EAST:
            this.minePoints.add(inFront.clone().translate(otherEnd, 0, 0));
            this.minePoints.add(inFront.clone().translate(otherEnd, 0, 4));
            this.minePoints.add(inFront.clone().translate(0, 0, 4));
            this.minePoints.add(inFront.clone().translate(0, 3, 2));
            this.minePoints.add(inFront.clone().translate(otherEnd, 3, 2));
            this.minePoints.add(inFront.clone().translate(otherEnd, 3, -2));
            this.minePoints.add(inFront.clone().translate(0, 3, -2));
            this.minePoints.add(inFront.clone().translate(0, 0, -4));
            this.minePoints.add(inFront.clone().translate(otherEnd, 0, -4));
            this.minePoints.add(inFront.clone().translate(otherEnd, -3, -2));
            this.minePoints.add(inFront.clone().translate(0, -3, -2));
            this.minePoints.add(inFront.clone().translate(0, -3, 2));
            this.minePoints.add(inFront.clone().translate(otherEnd, -3, 2));
            this.minePoints.add(inFront.clone().translate(otherEnd, 0, 0));
            break;
        }
    }

    /**
     * @return True if reached a turning point
     */
    private boolean doMining()
    {
        if (this.energyLevel < RETURNENERGY || this.inventoryDrops > RETURNDROPS || this.minePoints.size() == 0)
        {
            if (this.minePoints.size() > 0 && this.minePointCurrent != null)
            {
                this.minePoints.addFirst(this.minePointCurrent);
            }
            AIstate = AISTATE_RETURNING;
            this.pathBlockedCount = 0;
            GCLog.debug("Miner going home: " + this.posBase.toString() + " " + this.minePoints.size() + " shafts still to be mined");
            return true;
        }

        if (this.moveToPos(this.minePoints.getFirst(), false))
        {
            this.minePointCurrent = this.minePoints.removeFirst();
            GCLog.debug("Miner mid mining: " + this.minePointCurrent.toString() + " " + this.minePoints.size() + " shafts still to be mined");
            return true;
        }
        return false;
    }

    private void tryBackIn()
    {
        if (this.waypointBase.distanceSquared(new BlockVec3(this)) <= 9.1D)
        {
            this.AIstate = AISTATE_DOCKING;
            switch (this.baseFacing)
            {
            case NORTH:
                this.targetYaw = 180;
                break;
            case SOUTH:
                this.targetYaw = 0;
                break;
            case WEST:
                this.targetYaw = 270;
                break;
            case EAST:
                this.targetYaw = 90;
                break;
            }
        }
        else
        {
            this.freeze(FAIL_RETURNPATHBLOCKED);
        }
    }


    /**
     * Mine out the area in front of the miner (dist blocks from miner centre)
     *
     * @param limit Maximum block count to be mined this tick
     * @param dist
     * @return True if the mining failed (meaning the miner's path is blocked)
     */
    private boolean prepareMove(int limit, int dist)
    {
        if (this.mineCountDown > 0)
        {
            this.mineCountDown--;
            return false;
        }
        BlockVec3 inFront = new BlockVec3(Mth.floor(this.getX() + 0.5D), Mth.floor(this.getY() + 1.5D), Mth.floor(this.getZ() + 0.5D));
        if (dist == 2)
        {
            inFront.translate(headings2[this.facingAI.get3DDataValue()]);
        }
        else
        {
            if ((this.facingAI.get3DDataValue() & 1) == Direction.DOWN.getIndex())
            {
                dist++;
            }
            if (dist > 0)
            {
                inFront.translate(headings[this.facingAI.get3DDataValue()].clone().scale(dist));
            }
        }

        if (!inFront.equals(this.mineLast) && this.AIstate != AISTATE_ATBASE)
        {
            this.mineCountDown = 3;
            this.mineLast = inFront;
            return false;
        }

        int x = inFront.x;
        int y = inFront.y;
        int z = inFront.z;

        //Test not trying to mine own dock!
        if (y == this.waypointBase.y && x == this.waypointBase.x - ((this.baseFacing == Direction.EAST) ? 1 : 0) && z == this.waypointBase.z - ((this.baseFacing == Direction.SOUTH) ? 1 : 0))
        {
            this.tryBackIn();
            return false;
        }
        boolean wayBarred = false;
        this.tryBlockLimit = limit;

        //Check not obstructed by something immovable e.g. bedrock
        //Mine out the 12 blocks in front of it in direction of travel when getting close
        //There are 12 blocks around ... and 12 in front.  One block per tick?
        //(That means can move at 5/6 block per second when mining, and 1.67 bps when traveling)
        BlockPos pos = new BlockPos(x, y, z);
        switch (Direction.from3DDataValue(this.facingAI.get3DDataValue() & 6))
        {
        case DOWN:
            if (tryMineBlock(pos))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(1, 0, 0)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(1, 0, -1)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(0, 0, -1)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(0, 0, -2)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(-1, 0, -2)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(-1, 0, -1)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(-2, 0, -1)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(-2, 0, 0)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(-1, 0, 0)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(-1, 0, 1)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(0, 0, 1)))
            {
                wayBarred = true;
            }
            break;
        case NORTH:
            if (tryMineBlock(pos.offset(0, -2, 0)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(-1, -2, 0)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(0, -1, 0)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(-1, -1, 0)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(1, -1, 0)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(-2, -1, 0)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(1, 0, 0)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(-2, 0, 0)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(-1, 0, 0)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(0, 1, 0)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(-1, 1, 0)))
            {
                wayBarred = true;
            }
            break;
        case WEST:
            if (tryMineBlock(pos.offset(0, -2, -1)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(0, -1, 0)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(0, -1, -1)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(0, -1, +1)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(0, -1, -2)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(0, 0, 1)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(0, 0, -2)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(0, 0, -1)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(0, -2, 0)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(0, 1, -1)))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos))
            {
                wayBarred = true;
            }
            if (tryMineBlock(pos.offset(0, 1, 0)))
            {
                wayBarred = true;
            }
            break;
        }

        //If it is obstructed, return to base, or stand still if that is impossible
        if (wayBarred)
        {
            if (this.playerMP != null && blockingBlock.getBlock() != Blocks.AIR)
            {
                if (blockingBlock.getBlock() == Blocks.STONE)
                {
                    this.playerMP.sendMessage(new TextComponent(GCCoreUtil.translate("gui.message.astro_miner1_a.fail") + " " + GCCoreUtil.translate("gui.message.astro_miner1_b.fail")));
                }
                else
                {
                    this.playerMP.sendMessage(new TextComponent(GCCoreUtil.translate("gui.message.astro_miner1_a.fail") + " " + GCCoreUtil.translate(AstroMinerEntity.blockingBlock.toString())));
                }
            }
            this.setDeltaMovement(0.0, 0.0, 0.0);
            this.tryBlockLimit = 0;
            if (this.AIstate == AISTATE_TRAVELLING)
            {
                this.AIstate = AISTATE_RETURNING;
            }
            else if (AIstate == AISTATE_MINING)
            {
                this.pathBlockedCount++;
                this.AIstate = AISTATE_RETURNING;
            }
            else if (this.AIstate == AISTATE_RETURNING)
            {
                this.tryBackIn();
            }
            else
            {
                this.freeze(FAIL_RETURNPATHBLOCKED);
            }
            blockingBlock = Blocks.AIR.defaultBlockState();
        }

        if (this.tryBlockLimit == limit && !this.noSpeedup)
        {
            this.setDeltaMovement(this.getDeltaMovement().multiply(this.speedup, this.speedup, this.speedup));
        }

        return wayBarred;
    }

    private boolean prepareMoveClient(int limit, int dist)
    {
        BlockVec3 inFront = new BlockVec3(Mth.floor(this.getX() + 0.5D), Mth.floor(this.getY() + 1.5D), Mth.floor(this.getZ() + 0.5D));
        if (dist == 2)
        {
            inFront.translate(headings2[this.facing.get3DDataValue()]);
        }
        else
        {
            if ((this.facing.get3DDataValue() & 1) == Direction.DOWN.getIndex())
            {
                dist++;
            }
            if (dist > 0)
            {
                inFront.translate(headings[this.facing.get3DDataValue()].clone().scale(dist));
            }
        }
        if (inFront.equals(this.mineLast))
        {
            return false;
        }

        int x = inFront.x;
        int y = inFront.y;
        int z = inFront.z;

        boolean wayBarred = false;
        this.tryBlockLimit = limit;

        //Check not obstructed by something immovable e.g. bedrock
        //Mine out the 12 blocks in front of it in direction of travel when getting close
        //There are 12 blocks around ... and 12 in front.  One block per tick?
        //(That means can move at 5/6 block per second when mining, and 1.67 bps when traveling)
        BlockPos pos = new BlockPos(x, y, z);
        switch (Direction.from3DDataValue(this.facing.get3DDataValue() & 6))
        {
        case DOWN:
            if (tryBlockClient(pos))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(1, 0, 0)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(1, 0, -1)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(0, 0, -1)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(0, 0, -2)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(-1, 0, -2)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(-1, 0, -1)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(-2, 0, -1)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(-2, 0, 0)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(-1, 0, 0)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(-1, 0, 1)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(0, 0, 1)))
            {
                wayBarred = true;
            }
            break;
        case NORTH:
            if (tryBlockClient(pos.offset(0, -2, 0)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(-1, -2, 0)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(0, -1, 0)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(-1, -1, 0)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(1, -1, 0)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(-2, -1, 0)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(1, 0, 0)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(-2, 0, 0)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(-1, 0, 0)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(0, 1, 0)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(-1, 1, 0)))
            {
                wayBarred = true;
            }
            break;
        case WEST:
            if (tryBlockClient(pos.offset(0, -2, -1)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(0, -1, 0)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(0, -1, -1)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(0, -1, +1)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(0, -1, -2)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(0, 0, 1)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(0, 0, -2)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(0, 0, -1)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(0, -2, 0)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(0, 1, -1)))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos))
            {
                wayBarred = true;
            }
            if (tryBlockClient(pos.offset(0, 1, 0)))
            {
                wayBarred = true;
            }
            break;
        }

        //If it is obstructed, return to base, or stand still if that is impossible
        if (wayBarred)
        {
            this.tryBlockLimit = 0;
        }

        if (this.tryBlockLimit == limit)
        {
            this.mineLast = inFront;
        }

        return wayBarred;
    }

    private boolean tryMineBlock(BlockPos pos)
    {
        //Check things to avoid in front of it (see static list for list) including base type things
        //Can move through liquids including flowing lava
        BlockState state = this.level.getBlockState(pos);
        Block b = state.getBlock();
        if (b.getMaterial(state) == Material.AIR)
        {
            return false;
        }
        if (noMineList.contains(b))
        {
            blockingBlock = state;
            return true;
        }
        if (b instanceof LiquidBlock)
        {
            if ((b == Blocks.LAVA || b == Blocks.LAVA) && state.getValue(LiquidBlock.LEVEL) == 0 && this.AIstate != AISTATE_RETURNING)
            {
                blockingBlock = Blocks.LAVA.defaultBlockState().setValue(LiquidBlock.LEVEL, 0);
                return true;
            }
            return false;
        }
        if (b instanceof IFluidBlock)
        {
            return false;
        }

        boolean gtFlag = false;
        if (b != GCBlocks.FALLEN_METEOR)
        {
            if (b instanceof IPlantable && b != Blocks.LARGE_FERN && b != Blocks.DEAD_BUSH && b != Blocks.TALL_GRASS && b != Blocks.LILY_PAD && !(b instanceof FlowerBlock) && b != Blocks.SUGAR_CANE)
            {
                blockingBlock = state;
                return true;
            }
//            int meta = b.getMetaFromState(state);
            if (b.getDestroySpeed(state, this.level, pos) < 0)
            {
                blockingBlock = state;
                return true;
            }
            if (b.isEntityBlock(state))
            {
                if (CompatibilityManager.isGTLoaded() && gregTechCheck(b))
                {
                    gtFlag = true;
                }
                else
                {
                    blockingBlock = state;
                    return true;
                }
            }
        }

        if (this.tryBlockLimit == 0)
        {
            return false;
        }
        int result = ForgeHooks.onBlockBreakEvent(this.level, this.playerMP.gameMode.getGameModeForPlayer(), this.playerMP, pos);
        if (result < 0)
        {
            blockingBlock = Blocks.STONE.defaultBlockState();
//            blockingBlock.block = Blocks.STONE;
//            blockingBlock.meta = 0;
            return true;
        }

        this.tryBlockLimit--;

        //Collect the mined block - unless it's a plant or leaves in which case just break it
        if (!((b instanceof IPlantable && !(b instanceof SugarCaneBlock)) || b instanceof LeavesBlock))
        {
            ItemStack drops = gtFlag ? getGTDrops((ServerLevel) this.level, pos, b) : getPickBlock(this.level, pos, b);
            if (drops != null && !this.addToInventory(drops))
            {
                //drop itemstack if AstroMiner can't hold it
                dropStack(pos, drops);
            }
        }

        this.level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        return false;
    }

    private void dropStack(BlockPos pos, ItemStack drops)
    {
        float f = 0.7F;
        double d0 = this.level.random.nextFloat() * f + (1.0F - f) * 0.5D;
        double d1 = this.level.random.nextFloat() * f + (1.0F - f) * 0.5D;
        double d2 = this.level.random.nextFloat() * f + (1.0F - f) * 0.5D;
        ItemEntity entityitem = new ItemEntity(this.level, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, drops);
        entityitem.setDefaultPickUpDelay();
        this.level.addFreshEntity(entityitem);
        this.inventoryDrops++;
    }

    private boolean gregTechCheck(Block b)
    {
        Class clazz = CompatibilityManager.classGTOre;
        return clazz != null && clazz.isInstance(b);
    }

    private ItemStack getGTDrops(ServerLevel w, BlockPos pos, Block b)
    {
        List<ItemStack> array = Block.getDrops(b.defaultBlockState(), w, pos, w.getBlockEntity(pos));
        if (array != null && !array.isEmpty())
        {
            return array.get(0);
        }
        return ItemStack.EMPTY;
    }

    private boolean tryBlockClient(BlockPos pos)
    {
        BlockVec3 bv = new BlockVec3(pos.getX(), pos.getY(), pos.getZ());
        if (this.laserBlocks.contains(bv))
        {
            return false;
        }

        //Add minable blocks to the laser fx list
        BlockState state = this.level.getBlockState(pos);
        Block b = state.getBlock();
        if (b.getMaterial(state) == Material.AIR)
        {
            return false;
        }
        if (noMineList.contains(b))
        {
            return true;
        }
        if (b instanceof LiquidBlock)
        {
            return false;
        }
        if (b instanceof IFluidBlock)
        {
            return false;
        }
        if (b instanceof IPlantable)
        {
            return true;
        }
        if (b.isEntityBlock(state) || b.getDestroySpeed(state, this.level, pos) < 0)
        {
            return true;
        }
        if (this.tryBlockLimit == 0)
        {
            return false;
        }

        this.tryBlockLimit--;

        this.laserBlocks.add(bv);
        this.laserTimes.add(this.tickCount);
        return false;
    }


    public void removeLaserBlocks(int removeCount)
    {
        for (int i = 0; i < removeCount; i++)
        {
            this.laserBlocks.removeFirst();
            this.laserTimes.removeFirst();
        }
    }

    private ItemStack getPickBlock(Level world, BlockPos pos, Block b)
    {
        if (b == GCBlocks.FALLEN_METEOR)
        {
            return new ItemStack(GCItems.RAW_METEORIC_IRON);
        }
//        if (b instanceof BlockSpaceGlass)
//        {
//            return b.getDrops(world, pos, b.getDefaultState(), 0).get(0);
//        } TODO Space glass

        int i = 0;
        Item item = Item.byBlock(b);

        if (item == null)
        {
            GCLog.info("AstroMiner was unable to mine anything from: " + b.getName().getColoredString());
            return null;
        }

//        if (item.getHasSubtypes())
//        {
//            i = b.getMetaFromState(world.getBlockState(pos));
//        }

        return new ItemStack(item, 1);
    }

    private boolean addToInventory(ItemStack itemstack)
    {
        boolean flag1 = false;
        int k = 0;
        int invSize = this.getContainerSize();

        ItemStack itemstack1;

        if (itemstack.isStackable())
        {
            while (!itemstack.isEmpty() && k < invSize)
            {
                itemstack1 = this.stacks.get(k);

                if (RecipeUtil.stacksMatch(itemstack, itemstack1))
                {
                    int l = itemstack1.getCount() + itemstack.getCount();

                    if (l <= itemstack.getMaxStackSize())
                    {
                        itemstack.setCount(0);
                        itemstack1.setCount(l);
                        flag1 = true;
                    }
                    else if (itemstack1.getCount() < itemstack.getMaxStackSize())
                    {
                        itemstack.shrink(itemstack.getMaxStackSize() - itemstack1.getCount());
                        itemstack1.setCount(itemstack.getMaxStackSize());
                        flag1 = true;
                    }
                }

                ++k;
            }
        }

        if (!itemstack.isEmpty())
        {
            k = 0;

            while (k < invSize)
            {
                itemstack1 = this.stacks.get(k);

                if (itemstack1.isEmpty())
                {
                    this.stacks.set(k, itemstack.copy());
                    itemstack.setCount(0);
                    flag1 = true;
                    break;
                }

                ++k;
            }
        }

        if (flag1)
        {
            this.setChanged();
            this.mineCount++;
        }
        return flag1;
    }


    /**
     * Logic to move the miner to a given position
     *
     * @param pos
     * @param reverse True if returning home (re-use same tunnels)
     * @return False while the miner is en route, True when the position is reached
     */
    private boolean moveToPos(BlockVec3 pos, boolean reverse)
    {
        this.noSpeedup = false;

        if (reverse != (this.baseFacing.get3DDataValue() < 4))
        {
            if (this.getZ() > pos.z + 0.0001D || this.getZ() < pos.z - 0.0001D)
            {
                this.moveToPosZ(pos.z, stopForTurn);
                if (TEMPDEBUG)
                {
                    GCLog.debug("At " + getX() + "," + getY() + "," + getZ() + "Moving Z to " + pos.toString() + (stopForTurn ? " : Stop for turn " + this.xRot + "," + this.yRot + " | " + this.targetPitch + "," + this.targetYaw : ""));
                }
            }
            else if (this.getY() > pos.y - 0.9999D || this.getY() < pos.y - 1.0001D)
            {
                this.moveToPosY(pos.y - 1, stopForTurn);
                if (TEMPDEBUG)
                {
                    GCLog.debug("At " + getX() + "," + getY() + "," + getZ() + "Moving Y to " + pos.toString() + (stopForTurn ? " : Stop for turn " + this.xRot + "," + this.yRot + " | " + this.targetPitch + "," + this.targetYaw : ""));
                }
            }
            else if (this.getX() > pos.x + 0.0001D || this.getX() < pos.x - 0.0001D)
            {
                this.moveToPosX(pos.x, stopForTurn);
                if (TEMPDEBUG)
                {
                    GCLog.debug("At " + getX() + "," + getY() + "," + getZ() + "Moving X to " + pos.toString() + (stopForTurn ? " : Stop for turn " + this.xRot + "," + this.yRot + " | " + this.targetPitch + "," + this.targetYaw : ""));
                }
            }
            else
            {
                return true;
            }
            //got there
        }
        else
        {
            if (this.getX() > pos.x + 0.0001D || this.getX() < pos.x - 0.0001D)
            {
                this.moveToPosX(pos.x, stopForTurn);
                if (TEMPDEBUG)
                {
                    GCLog.debug("At " + getX() + "," + getY() + "," + getZ() + "Moving X to " + pos.toString() + (stopForTurn ? " : Stop for turn " + this.xRot + "," + this.yRot + " | " + this.targetPitch + "," + this.targetYaw : ""));
                }
            }
            else if (this.getY() > pos.y - 0.9999D || this.getY() < pos.y - 1.0001D)
            {
                this.moveToPosY(pos.y - 1, stopForTurn);
                if (TEMPDEBUG)
                {
                    GCLog.debug("At " + getX() + "," + getY() + "," + getZ() + "Moving Y to " + pos.toString() + (stopForTurn ? " : Stop for turn " + this.xRot + "," + this.yRot + " | " + this.targetPitch + "," + this.targetYaw : ""));
                }
            }
            else if (this.getZ() > pos.z + 0.0001D || this.getZ() < pos.z - 0.0001D)
            {
                this.moveToPosZ(pos.z, stopForTurn);
                if (TEMPDEBUG)
                {
                    GCLog.debug("At " + getX() + "," + getY() + "," + getZ() + "Moving Z to " + pos.toString() + (stopForTurn ? " : Stop for turn " + this.xRot + "," + this.yRot + " | " + this.targetPitch + "," + this.targetYaw : ""));
                }
            }
            else
            {
                return true;
            }
            //got there
        }

        return false;
    }

    private void moveToPosX(int x, boolean stopForTurn)
    {
        this.targetPitch = 0;

        if (this.getX() > x)
        {
            if (this.AIstate != AISTATE_DOCKING)
            {
                this.targetYaw = 270;
            }
//            this.motionX = -this.speed;
            this.setDeltaMovement(-this.speed, this.getDeltaMovement().y, this.getDeltaMovement().z);
            //TODO some acceleration and deceleration
            if (this.getDeltaMovement().x * speedup <= x - this.getX())
            {
//                this.motionX = x - this.getPosX();
                this.setDeltaMovement(x - this.getX(), this.getDeltaMovement().y, this.getDeltaMovement().z);
                this.noSpeedup = true;
            }
            this.facingAI = Direction.WEST;
        }
        else
        {
            if (this.AIstate != AISTATE_DOCKING)
            {
                this.targetYaw = 90;
            }
//            this.motionX = this.speed;
            this.setDeltaMovement(this.speed, this.getDeltaMovement().y, this.getDeltaMovement().z);
            if (this.getDeltaMovement().x * speedup >= x - this.getX())
            {
//                this.motionX = x - this.getPosX();
                this.setDeltaMovement(x - this.getX(), this.getDeltaMovement().y, this.getDeltaMovement().z);
                this.noSpeedup = true;
            }
            this.facingAI = Direction.EAST;
        }

        if (stopForTurn)
        {
//            this.motionX = 0;
            this.setDeltaMovement(0.0, this.getDeltaMovement().y, this.getDeltaMovement().z);
        }

//        this.motionY = 0;
//        this.motionZ = 0;
        this.setDeltaMovement(this.getDeltaMovement().x, 0.0, 0.0);
    }

    private void moveToPosY(int y, boolean stopForTurn)
    {
        if (this.getY() > y)
        {
            this.targetPitch = -90;
//            this.motionY = -this.speed;
            this.setDeltaMovement(this.getDeltaMovement().x, -this.speed, this.getDeltaMovement().z);
            if (this.getDeltaMovement().y * speedup <= y - this.getY())
            {
//                this.motionY = y - this.getPosY();
                this.setDeltaMovement(this.getDeltaMovement().x, y - this.getY(), this.getDeltaMovement().z);
                this.noSpeedup = true;
            }
            this.facingAI = Direction.DOWN;
        }
        else
        {
            this.targetPitch = 90;
//            this.motionY = this.speed;
            this.setDeltaMovement(this.getDeltaMovement().x, this.speed, this.getDeltaMovement().z);
            if (this.getDeltaMovement().y * speedup >= y - this.getY())
            {
//                this.motionY = y - this.getPosY();
                this.setDeltaMovement(this.getDeltaMovement().x, y - this.getY(), this.getDeltaMovement().z);
                this.noSpeedup = true;
            }
            this.facingAI = Direction.UP;
        }

        if (stopForTurn)
        {
//            this.motionY = 0;
            this.setDeltaMovement(this.getDeltaMovement().x, 0.0, this.getDeltaMovement().z);
        }

//        this.motionX = 0;
//        this.motionZ = 0;
        this.setDeltaMovement(0.0, this.getDeltaMovement().y, 0.0);
    }

    private void moveToPosZ(int z, boolean stopForTurn)
    {
        this.targetPitch = 0;

        if (this.getZ() > z)
        {
            if (this.AIstate != AISTATE_DOCKING)
            {
                this.targetYaw = 180;
            }
//            this.motionZ = -this.speed;
            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y, -this.speed);
            //TODO some acceleration and deceleration
            if (this.getDeltaMovement().z * speedup <= z - this.getZ())
            {
//                this.motionZ = z - this.getPosZ();
                this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y, z - this.getZ());
                this.noSpeedup = true;
            }
            this.facingAI = Direction.NORTH;
        }
        else
        {
            if (this.AIstate != AISTATE_DOCKING)
            {
                this.targetYaw = 0;
            }
//            this.motionZ = this.speed;
            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y, this.speed);
            if (this.getDeltaMovement().z * speedup >= z - this.getZ())
            {
//                this.motionZ = z - this.getPosZ();
                this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y, z - this.getZ());
                this.noSpeedup = true;
            }
            this.facingAI = Direction.SOUTH;
        }

        if (stopForTurn)
        {
//            this.motionZ = 0;
            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y, 0.0);
        }

//        this.motionY = 0;
//        this.motionX = 0;
        this.setDeltaMovement(0.0, 0.0, this.getDeltaMovement().z);
    }

    private boolean checkRotation()
    {
        boolean flag = true;
        //Handle the turns when it changes direction
        if (this.xRot > this.targetPitch + 0.001F || this.xRot < this.targetPitch - 0.001F)
        {
            if (this.xRot > this.targetPitch + 180)
            {
                this.xRot -= 360;
            }
            else if (this.xRot < this.targetPitch - 180)
            {
                this.xRot += 360;
            }

            if (this.xRot > this.targetPitch)
            {
                this.xRot -= this.rotSpeed;
                if (this.xRot < this.targetPitch)
                {
                    this.xRot = this.targetPitch;
                }
            }
            else
            {
                this.xRot += this.rotSpeed;
                if (this.xRot > this.targetPitch)
                {
                    this.xRot = this.targetPitch;
                }
            }
        }

        if (this.yRot > this.targetYaw + 0.001F || this.yRot < this.targetYaw - 0.001F)
        {
            if (this.yRot > this.targetYaw + 180)
            {
                this.yRot -= 360;
            }
            else if (this.yRot < this.targetYaw - 180)
            {
                this.yRot += 360;
            }

            if (this.yRot > this.targetYaw)
            {
                this.yRot -= this.rotSpeed;
                if (this.yRot < this.targetYaw)
                {
                    this.yRot = this.targetYaw;
                }
            }
            else
            {
                this.yRot += this.rotSpeed;
                if (this.yRot > this.targetYaw)
                {
                    this.yRot = this.targetYaw;
                }
            }
            flag = false;
        }

        return flag;
    }

    /**
     * x y z should be the mid-point of the 4 base blocks
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @param facing
     * @param base
     * @return
     */
    public static boolean spawnMinerAtBase(Level world, int x, int y, int z, Direction facing, BlockVec3 base, ServerPlayer player)
    {
        if (world.isClientSide)
        {
            return true;
        }
//        final EntityAstroMiner miner = new EntityAstroMiner(world, NonNullList.withSize(EntityAstroMiner.INV_SIZE, ItemStack.EMPTY), 0);
        AstroMinerEntity miner = new AstroMinerEntity(AsteroidEntities.ASTRO_MINER, world);
        miner.stacks = NonNullList.withSize(AstroMinerEntity.INV_SIZE, ItemStack.EMPTY);
        miner.energyLevel = 0;
        miner.setPlayer(player);
        if (player.abilities.instabuild)
        {
            miner.spawnedInCreative = true;
        }
        miner.waypointBase = new BlockVec3(x, y, z).modifyPositionFromSide(facing, 1);
        miner.setPos(miner.waypointBase.x, miner.waypointBase.y - 1, miner.waypointBase.z);
        miner.baseFacing = facing;
        miner.facingAI = facing;
        miner.lastFacing = facing;
//        miner.motionX = 0;
//        miner.motionY = 0;
//        miner.motionZ = 0;
        miner.setDeltaMovement(0.0, 0.0, 0.0);
        miner.targetPitch = 0;
        switch (facing)
        {
        case NORTH:
            miner.targetYaw = 180;
            break;
        case SOUTH:
            miner.targetYaw = 0;
            break;
        case WEST:
            miner.targetYaw = 270;
            break;
        case EAST:
            miner.targetYaw = 90;
            break;
        }
        miner.xRot = miner.targetPitch;
        miner.yRot = miner.targetYaw;
        miner.setBoundingBoxForFacing();
        miner.AIstate = AISTATE_ATBASE;
        miner.posBase = base;

        //Increase motion speed when moving in empty space between asteroids
        miner.speedup = (world.getDimension() instanceof DimensionAsteroids) ? SPEEDUP * 2.2D : SPEEDUP;

        //Clear blocks, and test to see if its movement area in front of the base is blocked
        if (miner.prepareMove(12, 0))
        {
//            miner.isDead = true;
//            miner.setDead();
            miner.remove();
            return false;
        }
        if (miner.prepareMove(12, 1))
        {
//            miner.isDead = true;
            miner.remove();
            return false;
        }
        if (miner.prepareMove(12, 2))
        {
//            miner.isDead = true;
            miner.remove();
            return false;
        }

        world.addFreshEntity(miner);
        miner.flagLink = true;
        return true;
    }

    public void setPlayer(ServerPlayer player)
    {
        this.playerMP = player;
        this.playerUUID = player.getUUID();
    }

    private EntityDimensions minerSize = super.getDimensions(Pose.STANDING);

    private void setBoundingBoxForFacing()
    {
        float xsize = cWIDTH;
        float ysize = cWIDTH;
        float zsize = cWIDTH;
        switch (this.facing)
        {
        case DOWN:
        case UP:
            ysize = cLENGTH;
            break;
        case NORTH:
        case SOUTH:
            ysize = cHEIGHT;
            zsize = cLENGTH;
            break;
        case WEST:
        case EAST:
            ysize = cHEIGHT;
            xsize = cLENGTH;
            break;
        }
        minerSize = EntityDimensions.scalable(Math.max(xsize, zsize), ysize);
//        this.width = Math.max(xsize, zsize);
//        this.height = ysize;
        this.setBoundingBox(new AABB(this.getX() - xsize / 2D, this.getY() + 1D - ysize / 2D, this.getZ() - zsize / 2D,
                this.getX() + xsize / 2D, this.getY() + 1D + ysize / 2D, this.getZ() + zsize / 2D));
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn)
    {
        return minerSize;
    }

    @Override
    public boolean hurt(DamageSource par1DamageSource, float par2)
    {
        if (!this.isAlive() || par1DamageSource.equals(DamageSource.CACTUS))
        {
            return true;
        }

        if (!this.level.isClientSide)
        {
            Entity e = par1DamageSource.getEntity();

            //If creative mode player, kill the entity (even if player owner is offline) and drop nothing
            if (e instanceof Player && ((Player) e).abilities.instabuild)
            {
                if (this.playerMP == null && !this.spawnedInCreative)
                {
                    e.sendMessage(new TextComponent("WARNING: that Astro Miner belonged to an offline player, cannot reset player's Astro Miner count."));
                }
                this.remove();
                return true;
            }

            //Invulnerable to mobs
            if (this.isInvulnerableTo() || (e instanceof LivingEntity && !(e instanceof Player)))
            {
                return false;
            }
            else
            {
                this.markHurt();
//                this.dataManager.set(this.timeSinceHit, Integer.valueOf(10));
//                this.dataManager.set(this.currentDamage, Integer.valueOf((int) (this.dataManager.get(this.currentDamage) + par2 * 10)));
                this.shipDamage += par2 * 10;

                if (e instanceof Player)
                {
                    this.shipDamage += par2 * 21;
//                    this.dataManager.set(this.currentDamage, 100);
                }

                if (this.shipDamage > 90)
                {
                    this.remove();
                    this.dropShipAsItem();
                    return true;
                }

                return true;
            }
        }
        else
        {
            return true;
        }
    }

    @Override
    public AABB getCollideAgainstBox(Entity par1Entity)
    {
        return null;  //AstroMiners aren't stopped by any other entity
    }

    @Override
    public AABB getCollideBox()
    {
        return this.getBoundingBox().deflate(0.1D);
    }

    @Override
    public boolean isPushable()
    {
        return false;
    }

    @Override
    public boolean isPickable()
    {
        return this.isAlive();
    }

    @Override
    public void animateHurt()
    {
//	    this.dataManager.set(this.timeSinceHit, Integer.valueOf(10));
//	    this.dataManager.set(this.currentDamage, Integer.valueOf(this.dataManager.get(this.currentDamage) * 5));
    }

    public float getDamage()
    {
        return this.entityData.get(DAMAGE);
    }

    public void setDamage(float p_70492_1_)
    {
        this.entityData.set(DAMAGE, Float.valueOf(p_70492_1_));
    }

    @Override
    public void moveTo(double x, double y, double z, float rotYaw, float rotPitch)
    {
        this.minecartX = x;
        this.minecartY = y;
        this.minecartZ = z;
        super.moveTo(x, y, z, rotYaw, rotPitch);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean b)
    {
        this.minecartX = x;
        this.minecartY = y;
        this.minecartZ = z;
        this.minecartYaw = y;
        this.minecartPitch = pitch;
        this.turnProgress = 0;
//        this.motionX = this.velocityX;
//        this.motionY = this.velocityY;
//        this.motionZ = this.velocityZ;
        this.setDeltaMovement(this.velocityX, this.velocityY, this.velocityZ);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void lerpMotion(double p_70016_1_, double p_70016_3_, double p_70016_5_)
    {
        this.velocityX = p_70016_1_;
        this.velocityY = p_70016_3_;
        this.velocityZ = p_70016_5_;
        this.setDeltaMovement(this.velocityX, this.velocityY, this.velocityZ);
        this.turnProgress = 0;
    }

//    @Override
//    protected void setSize(float p_70105_1_, float p_70105_2_)
//    {
//        this.setBoundingBoxForFacing();
//    }

    @Override
    public void setPos(double x, double y, double z)
    {
        this.setBoundingBox(this.getBoundingBox().move(x - this.getX(), y - this.getY(), z - this.getZ()));
        super.setPosRaw(x, y, z);
    }

    @Override
    public void remove()
    {
        if (!this.level.isClientSide && this.playerMP != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(this.playerMP);
            if (!this.spawnedInCreative)
            {
                int astroCount = stats.getAstroMinerCount();
                if (astroCount > 0)
                {
                    stats.setAstroMinerCount(stats.getAstroMinerCount() - 1);
                }
            }
            AsteroidsTickHandlerServer.removeChunkData(stats, this);
        }

        super.remove();
        if (posBase != null)
        {
            BlockEntity tileEntity = posBase.getTileEntity(this.level);
            if (tileEntity instanceof TileEntityMinerBase)
            {
                ((TileEntityMinerBase) tileEntity).unlinkMiner();
            }
        }

        if (this.soundUpdater != null)
        {
            this.soundUpdater.tick();
        }
    }

    public boolean isInvulnerableTo()
    {
        //Can't be damaged if its player is offline - it's in a fully dormant state
        return this.playerMP == null;
    }

    public List<ItemStack> getItemsDropped(List<ItemStack> droppedItems)
    {
        ItemStack rocket = new ItemStack(AsteroidsItems.ASTRO_MINER, 1);
        droppedItems.add(rocket);
        for (int i = 0; i < this.stacks.size(); i++)
        {
            if (!this.stacks.get(i).isEmpty())
            {
                droppedItems.add(this.stacks.get(i));
            }
            this.stacks.set(i, ItemStack.EMPTY);
        }
        return droppedItems;
    }

    public void dropShipAsItem()
    {
        if (this.level.isClientSide)
        {
            return;
        }

        for (final ItemStack item : this.getItemsDropped(new ArrayList<ItemStack>()))
        {
            ItemEntity entityItem = this.spawnAtLocation(item, 0);

            if (item.hasTag())
            {
                entityItem.getItem().setTag(item.getTag().copy());
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AbstractTickableSoundInstance getSoundUpdater()
    {
        return this.soundUpdater;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public SoundInstance setSoundUpdater(LocalPlayer player)
    {
        this.soundUpdater = new SoundUpdaterMiner(player, this);
        return this.soundUpdater;
    }

    public void stopRocketSound()
    {
        if (this.soundUpdater != null)
        {
            ((SoundUpdaterMiner) this.soundUpdater).stopRocketSound();
        }
        this.soundToStop = false;
    }

    @Override
    public void transmitData(int[] data)
    {
        data[0] = (int) (this.getX());
        data[1] = (int) (this.getY());
        data[2] = (int) (this.getZ());
        data[3] = this.energyLevel;
        data[4] = this.AIstate;
    }

    @Override
    public void receiveData(int[] data, String[] str)
    {
        str[0] = "";
        str[1] = "x: " + data[0];
        str[2] = "y: " + data[1];
        str[3] = "z: " + data[2];
        int energyPerCent = data[3] / 120;
        str[4] = GCCoreUtil.translate("gui.energy_storage.desc.1") + ": " + energyPerCent + "%";
        switch (data[4])
        {
        case AstroMinerEntity.AISTATE_STUCK:
            str[0] = GCCoreUtil.translate("gui.message.no_energy");
            break;
        case AstroMinerEntity.AISTATE_ATBASE:
            str[0] = GCCoreUtil.translate("gui.miner.docked");
            break;
        case AstroMinerEntity.AISTATE_TRAVELLING:
            str[0] = GCCoreUtil.translate("gui.miner.travelling");
            break;
        case AstroMinerEntity.AISTATE_MINING:
            str[0] = GCCoreUtil.translate("gui.miner.mining");
            break;
        case AstroMinerEntity.AISTATE_RETURNING:
            str[0] = GCCoreUtil.translate("gui.miner.returning");
            break;
        case AstroMinerEntity.AISTATE_DOCKING:
            str[0] = GCCoreUtil.translate("gui.miner.docking");
            break;
        case AstroMinerEntity.AISTATE_OFFLINE:
            str[0] = GCCoreUtil.translate("gui.miner.offline");
            break;
        }
    }

    @Override
    public void adjustDisplay(int[] data)
    {
        GL11.glScalef(0.9F, 0.9F, 0.9F);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt)
    {
        this.stacks = NonNullList.withSize(INV_SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.stacks);
        int itemCount = 0;
        for (ItemStack stack : this.stacks)
        {
            itemCount += stack.getCount();
        }
        this.mineCount = itemCount;

        if (nbt.contains("sindex"))
        {
            this.serverIndex = nbt.getInt("sindex");
        }
        else
        {
            this.serverIndex = -1;
        }

        if (nbt.contains("Energy"))
        {
            this.energyLevel = nbt.getInt("Energy");
        }
        if (nbt.contains("BaseX"))
        {
            this.posBase = new BlockVec3(nbt.getInt("BaseX"), nbt.getInt("BaseY"), nbt.getInt("BaseZ"));
            this.flagLink = true;
        }
        if (nbt.contains("TargetX"))
        {
            this.posTarget = new BlockVec3(nbt.getInt("TargetX"), nbt.getInt("TargetY"), nbt.getInt("TargetZ"));
        }
        if (nbt.contains("WBaseX"))
        {
            this.waypointBase = new BlockVec3(nbt.getInt("WBaseX"), nbt.getInt("WBaseY"), nbt.getInt("WBaseZ"));
        }
        if (nbt.contains("BaseFacing"))
        {
            this.baseFacing = Direction.from3DDataValue(nbt.getInt("BaseFacing"));
        }
        if (nbt.contains("AIState"))
        {
            this.AIstate = nbt.getInt("AIState");
        }
        if (nbt.contains("Facing"))
        {
            this.facingAI = Direction.from3DDataValue(nbt.getInt("Facing"));
            switch (this.facingAI)
            {
            case NORTH:
                this.targetYaw = 180;
                break;
            case SOUTH:
                this.targetYaw = 0;
                break;
            case WEST:
                this.targetYaw = 270;
                break;
            case EAST:
                this.targetYaw = 90;
                break;
            }
        }
        this.lastFacing = null;
        if (nbt.contains("WayPoints"))
        {
            this.wayPoints.clear();
            final ListTag wpList = nbt.getList("WayPoints", 10);
            for (int j = 0; j < wpList.size(); j++)
            {
                CompoundTag bvTag = wpList.getCompound(j);
                this.wayPoints.add(BlockVec3.read(bvTag));
            }
        }
        if (nbt.contains("MinePoints"))
        {
            this.minePoints.clear();
            final ListTag mpList = nbt.getList("MinePoints", 10);
            for (int j = 0; j < mpList.size(); j++)
            {
                CompoundTag bvTag = mpList.getCompound(j);
                this.minePoints.add(BlockVec3.read(bvTag));
            }
        }
        if (nbt.contains("MinePointCurrent"))
        {
            this.minePointCurrent = BlockVec3.read(nbt.getCompound("MinePointCurrent"));
        }
        else
        {
            this.minePointCurrent = null;
        }
        if (nbt.contains("playerUUIDMost", 4) && nbt.contains("playerUUIDLeast", 4))
        {
            this.playerUUID = new UUID(nbt.getLong("playerUUIDMost"), nbt.getLong("playerUUIDLeast"));
        }
        else
        {
            this.playerUUID = null;
        }
        if (nbt.contains("speedup"))
        {
            this.speedup = nbt.getDouble("speedup");
        }
        else
        {
            this.speedup = (WorldUtil.getProviderForDimensionServer(this.dimension) instanceof DimensionAsteroids) ? SPEEDUP * 1.6D : SPEEDUP;
        }

        this.pathBlockedCount = nbt.getInt("pathBlockedCount");
        this.spawnedInCreative = nbt.getBoolean("spawnedInCreative");
        this.flagCheckPlayer = true;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt)
    {
        if (level.isClientSide)
        {
            return;
        }
        final ListTag var2 = new ListTag();

        ContainerHelper.saveAllItems(nbt, this.stacks);

        nbt.put("Items", var2);
        nbt.putInt("sindex", this.serverIndex);
        nbt.putInt("Energy", this.energyLevel);
        if (this.posBase != null)
        {
            nbt.putInt("BaseX", this.posBase.x);
            nbt.putInt("BaseY", this.posBase.y);
            nbt.putInt("BaseZ", this.posBase.z);
        }
        if (this.posTarget != null)
        {
            nbt.putInt("TargetX", this.posTarget.x);
            nbt.putInt("TargetY", this.posTarget.y);
            nbt.putInt("TargetZ", this.posTarget.z);
        }
        if (this.waypointBase != null)
        {
            nbt.putInt("WBaseX", this.waypointBase.x);
            nbt.putInt("WBaseY", this.waypointBase.y);
            nbt.putInt("WBaseZ", this.waypointBase.z);
        }
        nbt.putInt("BaseFacing", this.baseFacing.get3DDataValue());
        nbt.putInt("AIState", this.AIstate);
        nbt.putInt("Facing", this.facingAI.get3DDataValue());
        if (this.wayPoints.size() > 0)
        {
            ListTag wpList = new ListTag();
            for (int j = 0; j < this.wayPoints.size(); j++)
            {
                wpList.add(this.wayPoints.get(j).write(new CompoundTag()));
            }
            nbt.put("WayPoints", wpList);
        }
        if (this.minePoints.size() > 0)
        {
            ListTag mpList = new ListTag();
            for (int j = 0; j < this.minePoints.size(); j++)
            {
                mpList.add(this.minePoints.get(j).write(new CompoundTag()));
            }
            nbt.put("MinePoints", mpList);
        }
        if (this.minePointCurrent != null)
        {
            nbt.put("MinePointCurrent", this.minePointCurrent.write(new CompoundTag()));
        }
        if (this.playerUUID != null)
        {
            nbt.putLong("playerUUIDMost", this.playerUUID.getMostSignificantBits());
            nbt.putLong("playerUUIDLeast", this.playerUUID.getLeastSignificantBits());
        }
        nbt.putDouble("speedup", this.speedup);
        nbt.putInt("pathBlockedCount", this.pathBlockedCount);
        nbt.putBoolean("spawnedInCreative", this.spawnedInCreative);
    }
}

