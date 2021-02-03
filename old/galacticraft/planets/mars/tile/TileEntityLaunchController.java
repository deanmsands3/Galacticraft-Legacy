package micdoodle8.mods.galacticraft.planets.mars.tile;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.planets.ConfigManagerPlanets;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockLaunchController;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlockNames;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerLaunchController;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerTerraformer;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.server.*;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

// import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
// import micdoodle8.mods.galacticraft.core.util.GCLog;
// import java.util.HashMap;
// import java.util.Map;

public class TileEntityLaunchController extends TileBaseElectricBlockWithInventory implements WorldlyContainer, ILandingPadAttachable, MenuProvider
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsBlockNames.LAUNCH_CONTROLLER)
    public static BlockEntityType<TileEntityLaunchController> TYPE;

    public static final int WATTS_PER_TICK = 1;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean launchPadRemovalDisabled = true;
    //    private Ticket chunkLoadTicket;
//    private List<BlockPos> connectedPads = new ArrayList<BlockPos>();
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int frequency = -1;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int destFrequency = -1;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public UUID ownerUUID;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean frequencyValid;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean destFrequencyValid;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int launchDropdownSelection;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean launchSchedulingEnabled;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean controlEnabled;
    public boolean hideTargetDestination = true;
    public boolean requiresClientUpdate;
    public Object attachedDock = null;
    private boolean frequencyCheckNeeded = false;
    //    private static Map<Integer, Long> tickCounts = new HashMap<>();
//    private static Map<Integer, Integer> instanceCounts = new HashMap<>();
    private static final TicketType<TileEntityLaunchController> TICKET_TYPE = TicketType.create(Constants.MOD_ID_PLANETS + ":chunk_loader", Comparator.comparing(BlockEntity::getBlockPos));
    public static final int TICKET_DISTANCE = 2;

    @Nullable
    private Level prevWorld;
    @Nullable
    private BlockPos prevPos;

    private boolean hasRegistered;
    private boolean isFirstTick = true;

    private final Set<ChunkPos> chunkSet = new ObjectOpenHashSet<>();

    public TileEntityLaunchController()
    {
        super(TYPE);
        this.storage.setMaxExtract(6);
        this.noRedstoneControl = true;
        this.inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.level.isClientSide)
        {
            this.controlEnabled = this.launchSchedulingEnabled && this.hasEnoughEnergyToRun && !this.getDisabled(0);

            if (this.frequencyCheckNeeded)
            {
                this.checkDestFrequencyValid();
                this.frequencyCheckNeeded = false;
            }

            if (this.requiresClientUpdate)
            {
                // PacketDispatcher.sendPacketToAllPlayers(this.getPacket());
                // TODO
                this.requiresClientUpdate = false;
            }

            if (this.ticks % 40 == 0)
            {
                this.setFrequency(this.frequency);
                this.setDestinationFrequency(this.destFrequency);
            }

//            if (this.ticks % 20 == 0)
//            {
//                if (this.chunkLoadTicket != null)
//                {
//                    for (int i = 0; i < this.connectedPads.size(); i++)
//                    {
//                        BlockPos coords = this.connectedPads.get(i);
//                        Block block = this.world.getBlockState(coords).getBlock();
//
//                        if (block != GCBlocks.landingPadFull)
//                        {
//                            this.connectedPads.remove(i);
//                            ForgeChunkManager.unforceChunk(this.chunkLoadTicket, new ChunkPos(coords.getX() >> 4, coords.getZ() >> 4));
//                        }
//                    }
//                }
//            }
        }
        else
        {
            if (this.frequency == -1 && this.destFrequency == -1)
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_ADVANCED_GUI, GCCoreUtil.getDimensionType(this.level), new Object[]{5, this.getBlockPos(), 0}));
            }
        }

        Level world = this.getLevel();
        if (world != null && !world.isClientSide && world.getChunkSource() instanceof ServerChunkCache)
        {
            if (isFirstTick)
            {
                isFirstTick = false;
                if (!ConfigManagerPlanets.launchControllerChunkLoad.get())
                {
                    //If we just loaded but are not actually able to operate
                    // release any tickets we have assigned to us that we loaded with
                    releaseChunkTickets(world, this.getBlockPos());
                }
            }

            if (hasRegistered && prevWorld != null && (prevPos == null || prevWorld != world || prevPos != this.getBlockPos()))
            {
                releaseChunkTickets(prevWorld);
            }

            if (hasRegistered && !ConfigManagerPlanets.launchControllerChunkLoad.get())
            {
                releaseChunkTickets(world);
            }

            if (!hasRegistered)
            {
                registerChunkTickets(world);
            }
        }
    }

    public UUID getOwnerUUID()
    {
        return this.ownerUUID;
    }

    public void setOwnerUUID(UUID uuid)
    {
        this.ownerUUID = uuid;
    }

    @Override
    public void setRemoved()
    {
        super.setRemoved();

//        if (this.chunkLoadTicket != null)
//        {
//            ForgeChunkManager.releaseTicket(this.chunkLoadTicket);
//        }

        if (!level.isClientSide() && prevWorld != null)
        {
            releaseChunkTickets(prevWorld);
        }
    }

//    @Override
//    public void onTicketLoaded(Ticket ticket, boolean placed)
//    {
//        if (!this.world.isRemote && ConfigManagerPlanets.launchControllerChunkLoad.get())
//        {
//            if (ticket == null)
//            {
//                return;
//            }
//
//            if (this.chunkLoadTicket == null)
//            {
//                this.chunkLoadTicket = ticket;
//            }
//
//            CompoundNBT nbt = this.chunkLoadTicket.getModData();
//            nbt.putInt("ChunkLoaderTileX", this.getPos().getX());
//            nbt.putInt("ChunkLoaderTileY", this.getPos().getY());
//            nbt.putInt("ChunkLoaderTileZ", this.getPos().getZ());
//
//            for (int x = -2; x <= 2; x++)
//            {
//                for (int z = -2; z <= 2; z++)
//                {
//                    Block blockID = this.world.getBlockState(this.getPos().add(x, 0, z)).getBlock();
//
//                    if (blockID instanceof BlockPadFull)
//                    {
//                        if (this.getPos().getX() + x >> 4 != this.getPos().getX() >> 4 || this.getPos().getZ() + z >> 4 != this.getPos().getZ() >> 4)
//                        {
//                            this.connectedPads.add(new BlockPos(this.getPos().getX() + x, this.getPos().getY(), this.getPos().getZ() + z));
//
//                            if (placed)
//                            {
//                                ChunkLoadingCallback.forceChunk(this.chunkLoadTicket, this.world, this.getPos().getX() + x, this.getPos().getY(), this.getPos().getZ() + z, this.getOwnerName());
//                            }
//                            else
//                            {
//                                ChunkLoadingCallback.addToList(this.world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.getOwnerName());
//                            }
//                        }
//                    }
//                }
//            }
//
//            ChunkLoadingCallback.forceChunk(this.chunkLoadTicket, this.world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.getOwnerName());
//        }
//    }

//    @Override
//    public Ticket getTicket()
//    {
//        return this.chunkLoadTicket;
//    }

//    @Override
//    public BlockPos getCoords()
//    {
//        return new BlockPos(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
//    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);

        this.ownerUUID = UUID.fromString(nbt.getString("OwnerName"));
        this.launchDropdownSelection = nbt.getInt("LaunchSelection");
        this.frequency = nbt.getInt("ControllerFrequency");
        this.destFrequency = nbt.getInt("TargetFrequency");
        this.frequencyCheckNeeded = true;
        this.launchPadRemovalDisabled = nbt.getBoolean("LaunchPadRemovalDisabled");
        this.launchSchedulingEnabled = nbt.getBoolean("LaunchPadSchedulingEnabled");
        this.hideTargetDestination = nbt.getBoolean("HideTargetDestination");
        this.requiresClientUpdate = GCCoreUtil.getEffectiveSide() == LogicalSide.SERVER;
        chunkSet.clear();
        ListTag list = nbt.getList("LoaderChunkSet", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
        for (Tag element : list)
        {
            chunkSet.add(new ChunkPos(((LongTag) element).getAsLong()));
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);

        if (this.ownerUUID != null)
        {
            nbt.putString("OwnerName", this.ownerUUID.toString());
        }

        nbt.putInt("LaunchSelection", this.launchDropdownSelection);
        nbt.putInt("ControllerFrequency", this.frequency);
        nbt.putInt("TargetFrequency", this.destFrequency);
        nbt.putBoolean("LaunchPadRemovalDisabled", this.launchPadRemovalDisabled);
        nbt.putBoolean("LaunchPadSchedulingEnabled", this.launchSchedulingEnabled);
        nbt.putBoolean("HideTargetDestination", this.hideTargetDestination);
        ListTag list = new ListTag();
        for (ChunkPos pos : chunkSet)
        {
            list.add(LongTag.valueOf(pos.toLong()));
        }
        nbt.put("LoaderChunkSet", list);
        return nbt;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public boolean canPlaceItem(int slotID, ItemStack itemStack)
    {
        return slotID == 0 && ItemElectricBase.isElectricItem(itemStack.getItem());
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slotID, ItemStack par2ItemStack, Direction par3)
    {
        return this.canPlaceItem(slotID, par2ItemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slotID, ItemStack par2ItemStack, Direction par3)
    {
        return slotID == 0;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return !this.getDisabled(0);
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown == 0)
        {
            switch (index)
            {
            case 0:
                this.disabled = disabled;
                this.disableCooldown = 10;
                break;
            case 1:
                this.launchSchedulingEnabled = disabled;
                break;
            case 2:
                this.hideTargetDestination = disabled;
                this.disableCooldown = 10;
                break;
            }
        }
    }

    @Override
    public boolean getDisabled(int index)
    {
        switch (index)
        {
        case 0:
            return this.disabled;
        case 1:
            return this.launchSchedulingEnabled;
        case 2:
            return this.hideTargetDestination;
        }

        return true;
    }

    @Override
    public boolean canAttachToLandingPad(LevelReader world, BlockPos pos)
    {
        BlockEntity tile = world.getBlockEntity(pos);

        return tile instanceof TileEntityLandingPad;
    }

    public void setFrequency(int frequency)
    {
        this.frequency = frequency;

        if (this.frequency >= 0)
        {
            this.frequencyValid = true;
            Iterable<ServerLevel> servers = GCCoreUtil.getWorldServerList(this.level);

            worldLoop:
            for (ServerLevel world : servers)
            {
                for (BlockEntity tile2 : new ArrayList<BlockEntity>(world.blockEntityList))
                {
                    if (this != tile2)
                    {
                        tile2 = world.getBlockEntity(tile2.getBlockPos());
                        if (tile2 == null)
                        {
                            continue;
                        }

                        if (tile2 instanceof TileEntityLaunchController)
                        {
                            TileEntityLaunchController launchController2 = (TileEntityLaunchController) tile2;

                            if (launchController2.frequency == this.frequency)
                            {
                                GCLog.debug("Launch Controller frequency conflict at " + tile2.getBlockPos() + " on dim: " + GCCoreUtil.getDimensionType(tile2));
                                this.frequencyValid = false;
                                break worldLoop;
                            }
                        }
                    }
                }
            }
        }
        else
        {
            this.frequencyValid = false;
        }
    }

    public void setDestinationFrequency(int frequency)
    {
        if (frequency != this.destFrequency)
        {
            this.destFrequency = frequency;
            this.checkDestFrequencyValid();
            this.updateRocketOnDockSettings();
        }
    }

    public void checkDestFrequencyValid()
    {
        if (!this.level.isClientSide)
        {
            this.destFrequencyValid = false;
            if (this.destFrequency >= 0)
            {
                Iterable<ServerLevel> servers = GCCoreUtil.getWorldServerList(this.level);
                for (ServerLevel world : servers)
                {
                    for (BlockEntity tile2 : new ArrayList<BlockEntity>(world.blockEntityList))
                    {
                        if (this != tile2)
                        {
                            tile2 = world.getBlockEntity(tile2.getBlockPos());
                            if (tile2 == null)
                            {
                                continue;
                            }

                            if (tile2 instanceof TileEntityLaunchController)
                            {
                                TileEntityLaunchController launchController2 = (TileEntityLaunchController) tile2;

                                if (launchController2.frequency == this.destFrequency)
                                {
                                    this.destFrequencyValid = true;
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean validFrequency()
    {
        this.checkDestFrequencyValid();
        return !this.getDisabled(0) && this.hasEnoughEnergyToRun && this.frequencyValid && this.destFrequencyValid;
    }

    public void setLaunchDropdownSelection(int newvalue)
    {
        if (newvalue != this.launchDropdownSelection)
        {
            this.launchDropdownSelection = newvalue;
            this.checkDestFrequencyValid();
            this.updateRocketOnDockSettings();
        }
    }

    public void setLaunchSchedulingEnabled(boolean newvalue)
    {
        if (newvalue != this.launchSchedulingEnabled)
        {
            this.launchSchedulingEnabled = newvalue;
            this.checkDestFrequencyValid();
            this.updateRocketOnDockSettings();
        }
    }

    public void updateRocketOnDockSettings()
    {
        if (this.attachedDock instanceof TileEntityLandingPad)
        {
            TileEntityLandingPad pad = ((TileEntityLandingPad) this.attachedDock);
            IDockable rocket = pad.getDockedEntity();
            if (rocket instanceof EntityAutoRocket)
            {
                ((EntityAutoRocket) rocket).updateControllerSettings(pad);
            }
        }
    }

    public void setAttachedPad(IFuelDock pad)
    {
        this.attachedDock = pad;
    }

    @Override
    public Direction getFront()
    {
        BlockState state = this.level.getBlockState(getBlockPos());
        return state.getValue(BlockLaunchController.FACING);
    }

    @Override
    public Direction getElectricInputDirection()
    {
        return getFront().getClockWise();
    }

    private void releaseChunkTickets(@Nonnull Level world)
    {
        releaseChunkTickets(world, prevPos);
    }

    private Set<ChunkPos> getChunkSet()
    {
        int chunkXMin = (worldPosition.getX() - 2) >> 4;
        int chunkXMax = (worldPosition.getX() + 2) >> 4;
        int chunkZMin = (worldPosition.getX() - 2) >> 4;
        int chunkZMax = (worldPosition.getZ() + 2) >> 4;
        Set<ChunkPos> set = new ObjectOpenHashSet<>();
        for (int chunkX = chunkXMin; chunkX <= chunkXMax; chunkX++)
        {
            for (int chunkZ = chunkZMin; chunkZ <= chunkZMax; chunkZ++)
            {
                set.add(new ChunkPos(chunkX, chunkZ));
            }
        }
        return set;
    }

    private void releaseChunkTickets(@Nonnull Level world, @Nullable BlockPos pos)
    {
        ServerChunkCache chunkProvider = (ServerChunkCache) world.getChunkSource();
        Iterator<ChunkPos> chunkIt = chunkSet.iterator();
//        ChunkManager manager = ((ServerWorld) world).getChunkProvider().chunkManager;
        while (chunkIt.hasNext())
        {
            ChunkPos chunkPos = chunkIt.next();
//            if (pos != null) {
//                manager.deregisterChunk(chunkPos, pos);
//            }
            chunkProvider.removeRegionTicket(TICKET_TYPE, chunkPos, TICKET_DISTANCE, this);
            chunkIt.remove();
        }
        this.hasRegistered = false;
        this.prevWorld = null;
    }

    private void registerChunkTickets(@Nonnull Level world)
    {
        ServerChunkCache chunkProvider = (ServerChunkCache) world.getChunkSource();
//        ChunkManager manager = ChunkManager.getInstance((ServerWorld) world);

        prevPos = this.getBlockPos();
        prevWorld = world;

        for (ChunkPos chunkPos : this.getChunkSet())
        {
            chunkProvider.addRegionTicket(TICKET_TYPE, chunkPos, TICKET_DISTANCE, this);
//            manager.registerChunk(chunkPos, prevPos);
            chunkSet.add(chunkPos);
        }

        hasRegistered = true;
    }

    /**
     * Release and re-register tickets, call when chunkset changes
     */
    public void refreshChunkTickets()
    {
        if (prevWorld != null)
        {
            releaseChunkTickets(prevWorld);
        }
        if (!level.isClientSide())
        {
            registerChunkTickets(Objects.requireNonNull(level.getLevel()));
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerLaunchController(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.launch_controller");
    }
}
