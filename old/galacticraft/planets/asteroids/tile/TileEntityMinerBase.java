package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti.EnumBlockMultiType;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.tile.IMachineSides;
import micdoodle8.mods.galacticraft.core.tile.IMachineSidesProperties;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.util.*;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlockNames;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.DimensionAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.AstroMinerEntity;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.Map.Entry;

public class TileEntityMinerBase extends TileBaseElectricBlockWithInventory implements WorldlyContainer, IMultiBlock, IMachineSides
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + AsteroidBlockNames.FULL_ASTRO_MINER_BASE)
    public static BlockEntityType<TileEntityMinerBase> TYPE;

    public static final int HOLDSIZE = 72;
    private final int[] slotArray;
    public boolean isMaster = false;
    public Direction facing = Direction.NORTH;
    private BlockPos mainBlockPosition;
    private final LinkedList<BlockVec3> targetPoints = new LinkedList<>();
    private WeakReference<TileEntityMinerBase> masterTile = null;
    public boolean updateClientFlag;
    public boolean findTargetPointsFlag;
    public int linkCountDown = 0;
    public static Map<DimensionType, List<BlockPos>> newMinerBases = new HashMap<>();
    private AABB renderAABB;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int linkedMinerDataAIState;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int linkedMinerDataDX;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int linkedMinerDataDY;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int linkedMinerDataDZ;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int linkedMinerDataCount;

    public void setMainBlockPosition(BlockPos mainBlockPosition)
    {
        this.mainBlockPosition = mainBlockPosition;
    }

    /**
     * The number of players currently using this chest
     */
    public int numUsingPlayers;

    /**
     * Server sync counter (once per 20 ticks)
     */
    private int ticksSinceSync;

    private final boolean spawnedMiner = false;

    public AstroMinerEntity linkedMiner = null;
    public UUID linkedMinerID = null;
    private boolean initialised;

    public TileEntityMinerBase()
    {
        super(TYPE);
        this.storage.setMaxExtract(ConfigManagerCore.hardMode.get() ? 20 : 12);
        this.slotArray = new int[HOLDSIZE];
        for (int i = 0; i < HOLDSIZE; i++)
        {
            this.slotArray[i] = i + 1;
        }
        this.inventory = NonNullList.withSize(HOLDSIZE + 1, ItemStack.EMPTY);
    }

    public static void checkNewMinerBases()
    {
        Iterator<Entry<DimensionType, List<BlockPos>>> entries = newMinerBases.entrySet().iterator();
        while (entries.hasNext())
        {
            Entry<DimensionType, List<BlockPos>> entry = entries.next();
            if (entry.getValue().isEmpty())
            {
                continue;
            }

            Level w = WorldUtil.getWorldForDimensionServer(entry.getKey());
            if (w == null)
            {
                GCLog.severe("Astro Miner Base placement: Unable to find server world for dim " + entry.getKey());
                entries.remove();
                continue;
            }

            for (BlockPos posMain : entry.getValue())
            {
                BlockPos master = new BlockPos(posMain);
                for (int x = 0; x < 2; x++)
                {
                    for (int y = 0; y < 2; y++)
                    {
                        for (int z = 0; z < 2; z++)
                        {
                            BlockPos pos = posMain.offset(x, y, z);
                            w.setBlock(pos, AsteroidBlocks.FULL_ASTRO_MINER_BASE.defaultBlockState(), 2);
                            final BlockEntity tile = w.getBlockEntity(pos);

                            if (tile instanceof TileEntityMinerBase)
                            {
                                ((TileEntityMinerBase) tile).setMainBlockPos(master);
                                ((TileEntityMinerBase) tile).updateClientFlag = true;
                            }
                        }
                    }
                }
            }

            entry.getValue().clear();
        }
    }

    public static void addNewMinerBase(DimensionType dimID, BlockPos blockPos)
    {
        if (newMinerBases.containsKey(dimID))
        {
            newMinerBases.get(dimID).add(blockPos);
        }
        else
        {
            List<BlockPos> blockPositions = Lists.newArrayList();
            newMinerBases.put(dimID, blockPositions);
            blockPositions.add(blockPos);
        }
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.initialised)
        {
            this.initialised = true;
            if (!this.level.isClientSide && !this.isMaster)
            {
                if (this.getMaster() == null)
                {
                    this.level.setBlock(this.getBlockPos(), AsteroidBlocks.ASTRO_MINER_BASE.defaultBlockState(), 2);
                }
            }
        }

        if (this.updateClientFlag)
        {
            assert (!this.level.isClientSide);  //Just checking: updateClientFlag should not be capable of being set on clients
            this.updateAllInDimension();
            this.updateClientFlag = false;
        }

        if (this.findTargetPointsFlag)
        {
            if (this.isMaster && this.linkedMiner != null)
            {
                this.findTargetPoints();
            }
            this.findTargetPointsFlag = false;
        }

        //TODO: Find linkedminer by UUID and tick it if not chunkloaded?

        if (!this.isMaster)
        {
            TileEntityMinerBase master = this.getMaster();

            if (master != null)
            {
                float energyLimit = master.storage.getCapacityGC() - master.storage.getEnergyStoredGC();
                if (energyLimit < 0F)
                {
                    energyLimit = 0F;
                }
                this.storage.setCapacity(energyLimit);
                this.storage.setMaxExtract(energyLimit);
                this.storage.setMaxReceive(energyLimit);
                float hasEnergy = this.getEnergyStoredGC();
                if (hasEnergy > 0F)
                {
                    this.extractEnergyGC(null, master.receiveEnergyGC(null, hasEnergy, false), false);
                }
            }
        }

        //Used for refreshing client with linked miner position data
        if (this.linkCountDown > 0)
        {
            this.linkCountDown--;
        }

        if (this.isMaster && !this.level.isClientSide)
        {
            this.updateGUIstate();
            //System.out.println("Miner base state " + this.linkedMinerDataAIState);
        }
    }

    public void updateGUIstate()
    {
        if (this.linkedMinerID == null)
        {
            this.linkedMinerDataAIState = -3;
            return;
        }
        AstroMinerEntity miner = this.linkedMiner;
        if (miner == null || !miner.isAlive())
        {
            this.linkedMinerDataAIState = -3;
            return;
        }
        if (this.linkCountDown > 0)
        {
            this.linkedMinerDataAIState = -2;
            return;
        }

        this.linkedMinerDataAIState = miner.AIstate;
        this.linkedMinerDataDX = (Mth.floor(this.linkedMiner.getX()) - this.getBlockPos().getX() - 1);
        this.linkedMinerDataDY = (Mth.floor(this.linkedMiner.getY()) - this.getBlockPos().getY() - 1);
        this.linkedMinerDataDZ = (Mth.floor(this.linkedMiner.getZ()) - this.getBlockPos().getZ() - 1);
        this.linkedMinerDataCount = miner.mineCount;
    }

    //TODO - currently unused, the master position replaces this?
    protected void initialiseMultiTiles(BlockPos pos, Level world)
    {
        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(pos, positions);
        for (BlockPos vecToAdd : positions)
        {
            BlockEntity tile = world.getBlockEntity(vecToAdd);
            if (tile instanceof TileEntityMinerBase)
            {
                ((TileEntityMinerBase) tile).mainBlockPosition = pos;
            }
        }
    }

    public boolean spawnMiner(ServerPlayer player)
    {
        if (this.isMaster)
        {
            if (this.linkedMiner != null)
            {
                if (!this.linkedMiner.isAlive())
                {
                    this.unlinkMiner();
                }
            }
            if (this.linkedMinerID == null)
            {
//                System.err.println("" + this.facing);
                if (AstroMinerEntity.spawnMinerAtBase(this.level, this.getBlockPos().getX() + 1, this.getBlockPos().getY() + 1, this.getBlockPos().getZ() + 1, this.facing, new BlockVec3(this), player))
                {
                    this.findTargetPoints();
                    return true;
                }
            }
            return false;
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            return master.spawnMiner(player);
        }
        return false;
    }

    public TileEntityMinerBase getMaster()
    {
        if (this.mainBlockPosition == null)
        {
            return null;
        }

        if (masterTile == null)
        {
            BlockEntity tileEntity = this.level.getBlockEntity(this.mainBlockPosition);

            if (tileEntity instanceof TileEntityMinerBase)
            {
                masterTile = new WeakReference<TileEntityMinerBase>(((TileEntityMinerBase) tileEntity));
            }

            if (masterTile == null)
            {
                return null;
            }
        }

        TileEntityMinerBase master = this.masterTile.get();

        if (master != null && master.isMaster)
        {
            return master;
        }

        return null;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.facing = Direction.from2DDataValue(nbt.getInt("facing"));
        if (GCCoreUtil.getEffectiveSide() == LogicalSide.SERVER)
        {
            this.isMaster = nbt.getBoolean("isMaster");
            if (this.isMaster)
            {
                this.updateClientFlag = true;
            }
            else
            {
                CompoundTag tagCompound = nbt.getCompound("masterpos");
                if (tagCompound.getAllKeys().isEmpty())
                {
                    this.setMainBlockPosition(null);
                }
                else
                {
                    this.setMainBlockPosition(new BlockPos(tagCompound.getInt("x"), tagCompound.getInt("y"), tagCompound.getInt("z")));
                    this.updateClientFlag = true;
                }
            }
            if (nbt.contains("LinkedUUIDMost", 4) && nbt.contains("LinkedUUIDLeast", 4))
            {
                this.linkedMinerID = new UUID(nbt.getLong("LinkedUUIDMost"), nbt.getLong("LinkedUUIDLeast"));
            }
            else
            {
                this.linkedMinerID = null;
            }
            if (nbt.contains("TargetPoints"))
            {
                this.targetPoints.clear();
                final ListTag mpList = nbt.getList("TargetPoints", 10);
                for (int j = 0; j < mpList.size(); j++)
                {
                    CompoundTag bvTag = mpList.getCompound(j);
                    this.targetPoints.add(BlockVec3.read(bvTag));
                }
            }
            else
            {
                this.findTargetPointsFlag = this.isMaster;
            }
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putBoolean("isMaster", this.isMaster);
        if (!this.isMaster && this.mainBlockPosition != null)
        {
            CompoundTag masterTag = new CompoundTag();
            masterTag.putInt("x", this.mainBlockPosition.getX());
            masterTag.putInt("y", this.mainBlockPosition.getY());
            masterTag.putInt("z", this.mainBlockPosition.getZ());
            nbt.put("masterpos", masterTag);
        }
        nbt.putInt("facing", this.facing.get2DDataValue());
        if (this.isMaster && this.linkedMinerID != null)
        {
            nbt.putLong("LinkedUUIDMost", this.linkedMinerID.getMostSignificantBits());
            nbt.putLong("LinkedUUIDLeast", this.linkedMinerID.getLeastSignificantBits());
        }
        ListTag mpList = new ListTag();
        for (int j = 0; j < this.targetPoints.size(); j++)
        {
            mpList.add(this.targetPoints.get(j).write(new CompoundTag()));
        }
        nbt.put("TargetPoints", mpList);
        return nbt;
    }

    @Override
    public int getMaxStackSize()
    {
        return 64;
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.level.getBlockEntity(this.getBlockPos()) == this && par1EntityPlayer.distanceToSqr(this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 0.5D, this.getBlockPos().getZ() + 0.5D) <= 64.0D;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    public boolean addToInventory(ItemStack itemstack)
    {
        //TODO - add test for is container open and if so use Container.mergeItemStack

        boolean flag1 = false;
        int k = 1;
        int invSize = this.getContainerSize();

        ItemStack existingStack;

        if (itemstack.isStackable())
        {
            while (!itemstack.isEmpty() && k < invSize)
            {
                existingStack = this.getInventory().get(k);

                if (RecipeUtil.stacksMatch(itemstack, existingStack))
                {
                    int combined = existingStack.getCount() + itemstack.getCount();

                    if (combined <= itemstack.getMaxStackSize())
                    {
                        itemstack.setCount(0);
                        existingStack.setCount(combined);
                        flag1 = true;
                    }
                    else if (existingStack.getCount() < itemstack.getMaxStackSize())
                    {
                        itemstack.shrink(itemstack.getMaxStackSize() - existingStack.getCount());
                        existingStack.setCount(itemstack.getMaxStackSize());
                        flag1 = true;
                    }
                }

                ++k;
            }
        }

        if (!itemstack.isEmpty())
        {
            k = 1;

            while (k < invSize)
            {
                existingStack = this.getInventory().get(k);

                if (existingStack.isEmpty())
                {
                    this.getInventory().set(k, itemstack.copy());
                    itemstack.setCount(0);
                    flag1 = true;
                    break;
                }

                ++k;
            }
        }

        this.setChanged();
        return flag1;
    }

    @Override
    public void onLoad()
    {
        this.clientOnLoad();
    }

    /**
     * invalidates a tile entity
     */
    @Override
    public void setRemoved()
    {
        super.setRemoved();
        this.clearCache();
    }

    @Override
    public double getPacketRange()
    {
        return 20.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 3;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return true;
    }

    @Override
    public NonNullList<ItemStack> getInventory()
    {
        if (this.isMaster)
        {
            return super.getInventory();
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            return master.getInventory();
        }

        return super.getInventory();
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return false;
    }

    public void setMainBlockPos(BlockPos master)
    {
        this.masterTile = null;
        if (this.getBlockPos().equals(master))
        {
            this.isMaster = true;
            this.setMainBlockPosition(null);
            return;
        }
        this.isMaster = false;
        this.setMainBlockPosition(master);
        this.setChanged();
    }

    public void onBlockRemoval()
    {
        if (this.isMaster)
        {
            this.setRemoved();
            this.onDestroy(this);
            return;
        }

        TileEntityMinerBase master = this.getMaster();

        if (master != null && !master.isRemoved())
        {
            this.level.destroyBlock(master.getBlockPos(), false);
        }
    }

    @Override
    public InteractionResult onActivated(Player entityPlayer)
    {
        if (this.isMaster)
        {
            ItemStack holding = entityPlayer.getUseItem();
            return holding == ItemStack.EMPTY || holding.getItem() != AsteroidsItems.ASTRO_MINER ? ActionResultType.SUCCESS : ActionResultType.PASS;

//            entityPlayer.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_ASTEROIDS, this.world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()); TODO guis
        }
        else
        {
            TileEntityMinerBase master = this.getMaster();
            if (master == null)
            {
                return ActionResultType.FAIL;
            }
            return master.onActivated(entityPlayer);
        }
    }

    @Override
    public void onCreate(Level world, BlockPos placedPosition)
    {
    }

    @Override
    public BlockMulti.EnumBlockMultiType getMultiType()
    {
        return EnumBlockMultiType.MINER_BASE;
    }

    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        for (int y = 0; y < 2; y++)
        {
            if (placedPosition.getY() + y >= this.level.getMaxBuildHeight())
            {
                break;
            }
            for (int x = 0; x < 2; x++)
            {
                for (int z = 0; z < 2; z++)
                {
                    if (x + y + z == 0)
                    {
                        continue;
                    }
                    positions.add(new BlockPos(placedPosition.getX() + x, placedPosition.getY() + y, placedPosition.getZ() + z));
                }
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

            if (stateAt.getBlock() == AsteroidBlocks.FULL_ASTRO_MINER_BASE) //GCBlocks.fakeBlock && (EnumBlockMultiType) stateAt.getValue(BlockMulti.MULTI_TYPE) == EnumBlockMultiType.MINER_BASE)
            {
                if (this.level.isClientSide && this.level.random.nextDouble() < 0.1D)
                {
                    Minecraft.getInstance().particleEngine.destroy(pos, this.level.getBlockState(pos));
                }
                this.level.destroyBlock(pos, false);
            }
        }
        this.level.destroyBlock(thisBlock, true);
    }

    //TODO
    //maybe 2 electrical inputs are needed?
    //chest goes above (could be 2 chests or other mods storage)

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AABB(worldPosition, worldPosition.offset(2, 2, 2));
        }
        return this.renderAABB;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public double getViewDistance()
    {
        return Constants.RENDERDISTANCE_LONG;
    }

    @Override
    public void updateFacing()
    {
        if (this.isMaster && this.linkedMinerID == null)
        {
            // Re-orient the block
            switch (this.facing)
            {
            case SOUTH:
                this.facing = Direction.WEST;
                break;
            case EAST:
                this.facing = Direction.SOUTH;
                break;
            case WEST:
                this.facing = Direction.NORTH;
                break;
            case NORTH:
                this.facing = Direction.EAST;
                break;
            }

            super.updateFacing();
        }
        else
        {
            TileEntityMinerBase master = this.getMaster();
            if (master != null)
            {
                master.updateFacing();
            }
        }

        if (!this.level.isClientSide)
        {
            this.updateAllInDimension();
        }

        for (Direction facing : Direction.values())
        {
            BlockPos offset = this.getBlockPos().relative(facing);
            BlockEntity tileOffset = this.level.getBlockEntity(offset);
            if (tileOffset != null && !(tileOffset instanceof TileEntityMinerBase))
            {
                BlockState state = this.level.getBlockState(offset);
                state.getBlock().neighborChanged(state, this.level, offset, this.level.getBlockState(this.getBlockPos()).getBlock(), this.getBlockPos(), false);
                level.setBlocksDirty(offset, state.getBlock().defaultBlockState(), state); // Forces block render update. Better way to do this?
            }
        }

        this.setChanged();
    }

    @Override
    public void buildDataPacket(int[] data)
    {
        int x, y, z;
        if (this.mainBlockPosition != null)
        {
            x = this.mainBlockPosition.getX();
            y = this.mainBlockPosition.getY();
            z = this.mainBlockPosition.getZ();
        }
        else
        {
            x = this.getBlockPos().getX();
            y = this.getBlockPos().getY();
            z = this.getBlockPos().getZ();
        }
        int link = (this.linkedMinerID != null) ? 8 : 0;
        data[0] = link + this.facing.ordinal();
        data[1] = x;
        data[2] = y;
        data[3] = z;
    }

    @Override
    public Direction getElectricInputDirection()
    {
        if (this.isMaster)
        {
            return this.facing.getOpposite();
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            return master.facing.getOpposite();
        }
        return null;
    }

    public void linkMiner(AstroMinerEntity entityAstroMiner)
    {
        this.linkedMiner = entityAstroMiner;
        this.linkedMinerID = this.linkedMiner.getUUID();
        this.updateClientFlag = true;
        this.setChanged();
    }

    public void unlinkMiner()
    {
        this.linkedMiner = null;
        this.linkedMinerID = null;
        this.updateClientFlag = true;
        this.setChanged();
    }

    public UUID getLinkedMiner()
    {
        if (this.isMaster)
        {
            return this.linkedMinerID;
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            return master.linkedMinerID;
        }
        return null;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        if (this.isMaster)
        {
            return side != this.facing ? slotArray : new int[]{};
        }

        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            return master.getSlotsForFace(side);
        }

        return new int[]{};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        if (this.isMaster)
        {
            if (side != this.facing)
            {
                return slotID > 0 || ItemElectricBase.isElectricItemEmpty(itemstack);
            }

            return false;
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            return master.canTakeItemThroughFace(slotID, itemstack, side);
        }

        return false;
    }

    @Override
    public boolean canPlaceItem(int slotID, ItemStack itemstack)
    {
        if (this.isMaster)
        {
            return slotID > 0 || ItemElectricBase.isElectricItem(itemstack.getItem());
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            return master.canPlaceItem(slotID, itemstack);
        }

        return false;
    }

    @Override
    public ItemStack getItem(int par1)
    {
        if (this.isMaster)
        {
            return super.getItem(par1);
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            return master.getItem(par1);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int par1, int par2)
    {
        if (this.isMaster)
        {
            return super.removeItem(par1, par2);
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            return master.removeItem(par1, par2);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int par1)
    {
        if (this.isMaster)
        {
            return super.removeItemNoUpdate(par1);
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            return master.removeItemNoUpdate(par1);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int par1, ItemStack par2ItemStack)
    {
        if (this.isMaster)
        {
            super.setItem(par1, par2ItemStack);
            return;
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            master.setItem(par1, par2ItemStack);
        }

        return;
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        if (this.isMaster)
        {
            return this.getItem(0);
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            master.getBatteryInSlot();
        }

        return null;
    }

    @Override
    public int getContainerSize()
    {
        return HOLDSIZE + 1;
    }

    public BlockVec3 findNextTarget()
    {
        if (!this.targetPoints.isEmpty())
        {
            BlockVec3 pos = this.targetPoints.removeFirst();
            this.setChanged();
            if (pos != null)
            {
                return pos.clone();
            }
        }

        //No more mining targets, the whole area is mined
        return null;
    }

    private void findTargetPoints()
    {
        this.targetPoints.clear();
        BlockVec3 posnTarget = new BlockVec3(this);

        if (this.level.getDimension() instanceof DimensionAsteroids)
        {
            ArrayList<BlockVec3> roids = ((DimensionAsteroids) this.level.getDimension()).getClosestAsteroidsXZ(posnTarget.x, posnTarget.y, posnTarget.z, this.facing.get3DDataValue(), 100);
            if (roids != null && roids.size() > 0)
            {
                this.targetPoints.addAll(roids);
                return;
            }
        }

        posnTarget.modifyPositionFromSide(this.facing, this.level.random.nextInt(16) + 32);
        int miny = Math.min(this.getBlockPos().getY() * 2 - 90, this.getBlockPos().getY() - 22);
        if (miny < 5)
        {
            miny = 5;
        }
        posnTarget.y = miny + 5 + this.level.random.nextInt(4);

        this.targetPoints.add(posnTarget);

        Direction lateral = Direction.NORTH;
        Direction inLine = this.facing;
        if (inLine.getAxis() == Axis.Z)
        {
            lateral = Direction.WEST;
        }

        this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, 13));
        this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, -13));
        if (posnTarget.y > 17)
        {
            this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, 7).modifyPositionFromSide(Direction.DOWN, 11));
            this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, -7).modifyPositionFromSide(Direction.DOWN, 11));
        }
        else
        {
            this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, 26));
            this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, -26));
        }
        this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, 7).modifyPositionFromSide(Direction.UP, 11));
        this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, -7).modifyPositionFromSide(Direction.UP, 11));
        if (posnTarget.y < this.getBlockPos().getY() - 38)
        {
            this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, 13).modifyPositionFromSide(Direction.UP, 22));
            this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(Direction.UP, 22));
            this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, -13).modifyPositionFromSide(Direction.UP, 22));
        }

        int s = this.targetPoints.size();
        for (int i = 0; i < s; i++)
        {
            this.targetPoints.add(this.targetPoints.get(i).clone().modifyPositionFromSide(inLine, AstroMinerEntity.MINE_LENGTH + 6));
        }

        this.setChanged();
        return;
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        //Used to recall miner
        TileEntityMinerBase master;
        if (!this.isMaster)
        {
            master = this.getMaster();
            if (master == null)
            {
                return;
            }
        }
        else
        {
            master = this;
        }
        if (master.linkedMiner != null)
        {
            master.linkedMiner.recall();
        }
    }

    @Override
    public Direction getFront()
    {
        return this.facing;
    }

    /**
     * This would normally be used by IMachineSides
     * but here it's overridden to get at the same facing packet for the MinerBase's own purposes
     * (IMachineSides won't be using it because as implemented
     * here, extending TileEntityElectricBlock, sides are not configurable)
     */

    @Override
    public void updateClient(List<Object> data)
    {
        int data1 = (Integer) data.get(1);
        this.facing = Direction.from3DDataValue(data1 & 7);
        this.setMainBlockPos(new BlockPos((Integer) data.get(2), (Integer) data.get(3), (Integer) data.get(4)));
        if (data1 > 7)
        {
            this.linkedMinerID = UUID.randomUUID();
        }
        else
        {
            this.linkedMinerID = null;
        }
    }

    @Override
    public IMachineSidesProperties getConfigurationType()
    {
        return IMachineSidesProperties.NOT_CONFIGURABLE;
    }

    @Override
    public MachineSide[] listConfigurableSides()
    {
        return null;
    }

    @Override
    public Face[] listDefaultFaces()
    {
        return null;
    }

    @Override
    public synchronized MachineSidePack[] getAllMachineSides()
    {
        return null;
    }

    @Override
    public void setupMachineSides(int length)
    {
    }
}