package micdoodle8.mods.galacticraft.api.prefab.entity;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.IEntityNoisy;
import micdoodle8.mods.galacticraft.api.entity.ILandable;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockPadFull;
import micdoodle8.mods.galacticraft.core.client.sounds.SoundUpdaterRocket;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.core.network.NetworkUtil;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Do not include this prefab class in your released mod download.
 */
public abstract class EntityAutoRocket extends EntitySpaceshipBase implements ILandable, Container, IEntityNoisy
{
    public int destinationFrequency = -1;
    public BlockPos targetVec;
    public DimensionType targetDimension;
    protected NonNullList<ItemStack> stacks;
    private IFuelDock landingPad;
    public EnumAutoLaunch autoLaunchSetting;
    public int autoLaunchCountdown;
    private BlockVec3 activeLaunchController;

    public String statusMessage;
    public String statusColour;
    public int statusMessageCooldown;
    public int lastStatusMessageCooldown;
    public boolean statusValid;
    protected double lastMotionY;
    protected double lastLastMotionY;
    private boolean waitForPlayer;
    protected AbstractTickableSoundInstance rocketSoundUpdater;
    private boolean rocketSoundToStop = false;
    private static Class<?> controllerClass = null;

    static
    {
        try
        {
            controllerClass = Class.forName("micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController");
        }
        catch (ClassNotFoundException e)
        {
            GCLog.info("Galacticraft-Planets' LaunchController not present, rockets will not be launch controlled.");
        }
    }

    public EntityAutoRocket(EntityType<? extends EntityAutoRocket> type, Level worldIn)
    {
        super(type, worldIn);

        if (level != null && level.isClientSide)
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
        }

        this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    }

//    public EntityAutoRocket(World world, double getPosX(), double getPosY(), double getPosZ())
//    {
//        this(world);
////        this.setSize(0.98F, 2F);
//        this.setPosition(getPosX(), getPosY(), getPosZ());
//        this.prevPosX = getPosX();
//        this.prevPosY = getPosY();
//        this.prevPosZ = getPosZ();
//    }

    //Used for Cargo Rockets, client is asking the server what is the status
    public boolean checkLaunchValidity()
    {
        this.statusMessageCooldown = 40;

        if (this.hasValidFuel())
        {
            if (this.launchPhase == EnumLaunchPhase.UNIGNITED.ordinal() && !this.level.isClientSide)
            {
                if (!this.setFrequency())
                {
                    this.destinationFrequency = -1;
                    this.statusMessage = Language.getInstance().getOrDefault("gui.message.frequency") + "#" + Language.getInstance().getOrDefault("gui.message.not_set");
                    this.statusColour = "\u00a7c";
                    return false;
                }
                else
                {
                    this.statusMessage = Language.getInstance().getOrDefault("gui.message.success");
                    this.statusColour = "\u00a7a";
                    return true;
                }
            }
        }
        else
        {
            this.destinationFrequency = -1;
            this.statusMessage = Language.getInstance().getOrDefault("gui.message.not_enough") + "#" + Language.getInstance().getOrDefault("gui.message.fuel");
            this.statusColour = "\u00a7c";
            return false;
        }

        this.destinationFrequency = -1;
        return false;
    }

    public boolean setFrequency()
    {
        if (!GalacticraftCore.isPlanetsLoaded || controllerClass == null)
        {
            return false;
        }

        if (this.activeLaunchController != null)
        {
            BlockEntity launchController = this.activeLaunchController.getTileEntity(this.level);
            if (controllerClass.isInstance(launchController))
            {
                try
                {
                    Boolean b = (Boolean) controllerClass.getMethod("validFrequency").invoke(launchController);

                    if (b != null && b)
                    {
                        int controllerFrequency = controllerClass.getField("destFrequency").getInt(launchController);
                        boolean foundPad = this.setTarget(false, controllerFrequency);

                        if (foundPad)
                        {
                            this.destinationFrequency = controllerFrequency;
                            GCLog.debug("Rocket under launch control: going to target frequency " + controllerFrequency);
                            return true;
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        this.destinationFrequency = -1;
        return false;
    }

    protected boolean setTarget(boolean doSet, int destFreq)
    {
        Iterable<ServerLevel> worlds = GCCoreUtil.getWorldServerList(this.level);
        if (!GalacticraftCore.isPlanetsLoaded || controllerClass == null)
        {
            return false;
        }

        for (ServerLevel world : worlds)
        {
            try
            {
                for (BlockEntity tile : new ArrayList<BlockEntity>(world.blockEntityList))
                {
                    if (!controllerClass.isInstance(tile))
                    {
                        continue;
                    }

                    tile = world.getBlockEntity(tile.getBlockPos());
                    if (!controllerClass.isInstance(tile))
                    {
                        continue;
                    }

                    int controllerFrequency = controllerClass.getField("frequency").getInt(tile);

                    if (destFreq == controllerFrequency)
                    {
                        boolean targetSet = false;

                        blockLoop:
                        for (int x = -2; x <= 2; x++)
                        {
                            for (int z = -2; z <= 2; z++)
                            {
                                BlockPos pos = new BlockPos(tile.getBlockPos().offset(x, 0, z));
                                Block block = world.getBlockState(pos).getBlock();

                                if (block instanceof BlockPadFull)
                                {
                                    if (doSet)
                                    {
                                        this.targetVec = pos;
                                    }

                                    targetSet = true;
                                    break blockLoop;
                                }
                            }
                        }

                        if (doSet)
                        {
                            this.targetDimension = tile.getLevel().dimensionType();
                        }

                        if (!targetSet)
                        {
                            if (doSet)
                            {
                                this.targetVec = null;
                            }

                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public int getScaledFuelLevel(int scale)
    {
        if (this.getFuelTankCapacity() <= 0)
        {
            return 0;
        }

        return this.fuelTank.getFluidAmount() * scale / this.getFuelTankCapacity() / ConfigManagerCore.rocketFuelFactor.get();
    }

    @Override
    public void tick()
    {
        //Workaround for a weird bug (?) in vanilla 1.8.9 where - if client view distance is shorter
        //than the server's chunk loading distance - chunks will unload on the client, but the
        //entity will still be in the WorldClient.loadedEntityList.  This results in an entity which
        //is in the world, not dead and still updating on both server and client, but not in any chunk's
        //chunk.entityLists.  Also, it won't be reloaded into any chunk's entityLists when the chunk comes
        //back into viewing range - not sure why, maybe because it's already in the World.loadedEntityList?
        //Because it's now not in any chunk's entityLists, it cannot be iterated for rendering by RenderGlobal,
        //so it's gone invisible!

        //Tracing shows that Chunk.onChunkUnload() is called on the chunk clientside when the chunk goes
        //out of the view distance.  However, even after Chunk.onChunkUnload() - which should remove
        //this entity from the WorldClient.loadedEntityList - this entity stays in the world loadedEntityList.
        //That's why an onUpdate() tick is active for it, still!
        //Weird, huh?
        if (this.level.isClientSide && this.inChunk && !CompatibilityManager.isCubicChunksLoaded)
        {
            LevelChunk chunk = this.level.getChunk(this.xChunk, this.zChunk);
            int cx = Mth.floor(this.getX()) >> 4;
            int cz = Mth.floor(this.getZ()) >> 4;
            if (chunk.loaded && this.xChunk == cx && this.zChunk == cz)
            {
                boolean thisfound = false;
                ClassInstanceMultiMap<Entity> mapEntities = chunk.getEntitySections()[this.yChunk];
                for (Entity ent : mapEntities)
                {
                    if (ent == this)
                    {
                        thisfound = true;
                        break;
                    }
                }
                if (!thisfound)
                {
                    chunk.addEntity(this);
                }
            }
        }

        if (this.launchPhase == EnumLaunchPhase.LANDING.ordinal() && this.hasValidFuel())
        {
            if (this.targetVec != null)
            {
                double yDiff = this.getY() - this.getOnPadYOffset() - this.targetVec.getY();
                this.setDeltaMovement(this.getDeltaMovement().x, Math.max(-2.0, (yDiff - 0.04) / -55.0), this.getDeltaMovement().z);

                //Some lateral motion in case not exactly on target (happens if rocket was moving laterally during launch)
                double diff = this.getX() - this.targetVec.getX() - 0.5D;
                double motX, motZ;
                if (diff > 0D)
                {
                    motX = Math.max(-0.1, diff / -100.0D);
                }
                else if (diff < 0D)
                {
                    motX = Math.min(0.1, diff / -100.0D);
                }
                else
                {
                    motX = 0D;
                }
                diff = this.getZ() - this.targetVec.getZ() - 0.5D;
                if (diff > 0D)
                {
                    motZ = Math.max(-0.1, diff / -100.0D);
                }
                else if (diff < 0D)
                {
                    motZ = Math.min(0.1, diff / -100.0D);
                }
                else
                {
                    motZ = 0D;
                }
                if (motZ != 0D || motX != 0D)
                {
                    double angleYaw = Math.atan(motZ / motX);
                    double signed = motX < 0 ? 50D : -50D;
                    double anglePitch = Math.atan(Math.sqrt(motZ * motZ + motX * motX) / signed) * 100D;
                    this.yRot = (float) angleYaw * Constants.RADIANS_TO_DEGREES;
                    this.xRot = (float) anglePitch * Constants.RADIANS_TO_DEGREES;
                }
                else
                {
                    this.xRot = 0F;
                }

                if (yDiff > 1D && yDiff < 4D)
                {
                    for (Object o : this.level.getEntities(this, this.getBoundingBox().move(0D, -3D, 0D), EntitySpaceshipBase.ROCKET_SELECTOR))
                    {
                        if (o instanceof EntitySpaceshipBase)
                        {
                            ((EntitySpaceshipBase) o).dropShipAsItem();
                            ((EntitySpaceshipBase) o).remove();
                        }
                    }
                }
                if (yDiff < 0.4)
                {
                    int yMin = Mth.floor(this.getBoundingBox().minY - this.getOnPadYOffset() - 0.45D) - 2;
                    int yMax = Mth.floor(this.getBoundingBox().maxY) + 1;
                    int zMin = Mth.floor(this.getZ()) - 1;
                    int zMax = Mth.floor(this.getZ()) + 1;
                    for (int x = Mth.floor(this.getX()) - 1; x <= Mth.floor(this.getX()) + 1; x++)
                    {
                        for (int z = zMin; z <= zMax; z++)
                        {
                            //Doing y as the inner loop may help with cacheing of chunks
                            for (int y = yMin; y <= yMax; y++)
                            {
                                if (this.level.getBlockEntity(new BlockPos(x, y, z)) instanceof IFuelDock)
                                {
                                    //Land the rocket on the pad found
                                    this.xRot = 0F;
                                    this.failRocket();
                                }
                            }
                        }
                    }
                }
            }
        }

        super.tick();

        if (!this.level.isClientSide)
        {
            if (this.statusMessageCooldown > 0)
            {
                this.statusMessageCooldown--;
            }

            if (this.statusMessageCooldown == 0 && this.lastStatusMessageCooldown > 0 && this.statusValid)
            {
                this.autoLaunch();
            }

            if (this.autoLaunchCountdown > 0 && (!(this instanceof EntityTieredRocket) || !this.getPassengers().isEmpty()))
            {
                if (--this.autoLaunchCountdown <= 0)
                {
                    this.autoLaunch();
                }
            }

            if (this.autoLaunchSetting == EnumAutoLaunch.ROCKET_IS_FUELED && this.fuelTank.getFluidAmount() == this.fuelTank.getCapacity() && (!(this instanceof EntityTieredRocket) || !this.getPassengers().isEmpty()))
            {
                this.autoLaunch();
            }

            if (this.autoLaunchSetting == EnumAutoLaunch.INSTANT)
            {
                if (this.autoLaunchCountdown == 0 && (!(this instanceof EntityTieredRocket) || !this.getPassengers().isEmpty()))
                {
                    this.autoLaunch();
                }
            }

            if (this.autoLaunchSetting == EnumAutoLaunch.REDSTONE_SIGNAL)
            {
                if (this.ticks % 11 == 0 && this.activeLaunchController != null)
                {
                    if (RedstoneUtil.isBlockReceivingRedstone(this.level, this.activeLaunchController.toBlockPos()))
                    {
                        this.autoLaunch();
                    }
                }
            }

            if (this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal())
            {
                this.setPad(null);
            }
            else
            {
                if (this.launchPhase == EnumLaunchPhase.UNIGNITED.ordinal() && this.landingPad != null && this.ticks % 17 == 0)
                {
                    this.updateControllerSettings(this.landingPad);
                }
            }

            this.lastStatusMessageCooldown = this.statusMessageCooldown;
        }

        if (this.launchPhase >= EnumLaunchPhase.IGNITED.ordinal())
        {
            if (this.rocketSoundUpdater != null)
            {
                this.rocketSoundUpdater.tick();
                this.rocketSoundToStop = true;
            }
        }
        else
        {
            if (this.rocketSoundToStop)
            {
                this.stopRocketSound();
                if (this.rocketSoundUpdater != null)
                {
                    Minecraft.getInstance().getSoundManager().stop(rocketSoundUpdater);
                }
                this.rocketSoundUpdater = null;
            }
        }
    }

    @Override
    protected boolean shouldMoveClientSide()
    {
        return false;
    }

    //Server LogicalSide only - this is a Launch Controlled ignition attempt
    private void autoLaunch()
    {
        if (this.autoLaunchSetting != null)
        {
            if (this.activeLaunchController != null)
            {
                BlockEntity tile = this.activeLaunchController.getTileEntity(this.level);

                if (controllerClass.isInstance(tile))
                {
                    Boolean autoLaunchEnabled = null;
                    try
                    {
                        autoLaunchEnabled = controllerClass.getField("controlEnabled").getBoolean(tile);
                    }
                    catch (Exception e)
                    {
                    }

                    if (autoLaunchEnabled != null && autoLaunchEnabled)
                    {
                        if (this.fuelTank.getFluidAmount() > this.fuelTank.getCapacity() * 2 / 5)
                        {
                            this.ignite();
                        }
                        else
                        {
                            this.failMessageInsufficientFuel();
                        }
                    }
                    else
                    {
                        this.failMessageLaunchController();
                    }
                }
            }
            this.autoLaunchSetting = null;

            return;
        }
        else
        {
            this.ignite();
        }
    }

    public boolean igniteWithResult()
    {
        if (this.setFrequency())
        {
            super.ignite();
            this.activeLaunchController = null;
            return true;
        }
        else
        {
            if (this.isPlayerRocket())
            {
                super.ignite();
                this.activeLaunchController = null;
                return true;
            }

            this.activeLaunchController = null;
            return false;
        }
    }

    @Override
    public void ignite()
    {
        this.igniteWithResult();
    }

    public abstract boolean isPlayerRocket();

    @Override
    public void landEntity(BlockPos pos)
    {
        BlockEntity tile = this.level.getBlockEntity(pos);

        if (tile instanceof IFuelDock)
        {
            IFuelDock dock = (IFuelDock) tile;

            if (this.isDockValid(dock))
            {
                if (!this.level.isClientSide)
                {
                    //Drop any existing rocket on the landing pad
                    if (dock.getDockedEntity() instanceof EntitySpaceshipBase && dock.getDockedEntity() != this)
                    {
                        ((EntitySpaceshipBase) dock.getDockedEntity()).dropShipAsItem();
                        ((EntitySpaceshipBase) dock.getDockedEntity()).remove();
                    }

                    this.setPad(dock);
                }

                this.onRocketLand(pos);
            }
        }
    }

    public void updateControllerSettings(IFuelDock dock)
    {
        HashSet<ILandingPadAttachable> connectedTiles = dock.getConnectedTiles();

        try
        {
            for (ILandingPadAttachable updatedTile : connectedTiles)
            {
                if (controllerClass.isInstance(updatedTile))
                {
                    //This includes a check for whether it has enough energy to run (if it doesn't, then a launch would not go to the destination frequency and the rocket would be lost!)
                    Boolean autoLaunchEnabled = controllerClass.getField("controlEnabled").getBoolean(updatedTile);

                    this.activeLaunchController = new BlockVec3((BlockEntity) updatedTile);

                    if (autoLaunchEnabled)
                    {
                        this.autoLaunchSetting = EnumAutoLaunch.values()[controllerClass.getField("launchDropdownSelection").getInt(updatedTile)];

                        switch (this.autoLaunchSetting)
                        {
                        case INSTANT:
                            //Small countdown to give player a moment to exit the Launch Controller GUI
                            if (this.autoLaunchCountdown <= 0 || this.autoLaunchCountdown > 12)
                            {
                                this.autoLaunchCountdown = 12;
                            }
                            break;
                        //The other settings set time to count down AFTER player mounts rocket but BEFORE engine ignition
                        //TODO: if autoLaunchCountdown > 0 add some smoke (but not flame) particle effects or other pre-flight test feedback so the player knows something is happening
                        case TIME_10_SECONDS:
                            if (this.autoLaunchCountdown <= 0 || this.autoLaunchCountdown > 200)
                            {
                                this.autoLaunchCountdown = 200;
                            }
                            break;
                        case TIME_30_SECONDS:
                            if (this.autoLaunchCountdown <= 0 || this.autoLaunchCountdown > 600)
                            {
                                this.autoLaunchCountdown = 600;
                            }
                            break;
                        case TIME_1_MINUTE:
                            if (this.autoLaunchCountdown <= 0 || this.autoLaunchCountdown > 1200)
                            {
                                this.autoLaunchCountdown = 1200;
                            }
                            break;
                        default:
                            break;
                        }
                    }
                    else
                    {
                        //This LaunchController is out of power, disabled, invalid target or set not to launch
                        //No auto launch - but maybe another connectedTile will have some launch settings?
                        this.autoLaunchSetting = null;
                        this.autoLaunchCountdown = 0;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void onRocketLand(BlockPos pos)
    {
        this.absMoveTo(pos.getX() + 0.5, pos.getY() + 0.4D + this.getOnPadYOffset(), pos.getZ() + 0.5, this.yRot, 0.0F);
        this.stopRocketSound();
        if (this.rocketSoundUpdater != null)
        {
            Minecraft.getInstance().getSoundManager().stop(rocketSoundUpdater);
        }
        this.rocketSoundUpdater = null;  //Allow sound to be restarted again if it relaunches
    }

    public void stopRocketSound()
    {
        if (this.rocketSoundUpdater != null)
        {
            ((SoundUpdaterRocket) this.rocketSoundUpdater).stopRocketSound();
        }
        this.rocketSoundToStop = false;
    }

    @Override
    public void remove(boolean keepData)
    {
        super.remove(keepData);

        if (this.rocketSoundUpdater != null)
        {
            this.rocketSoundUpdater.tick();
        }
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        if (!this.level.isClientSide)
        {
            double clientPosY = buffer.readDouble();
            if (clientPosY != Double.NaN && this.hasValidFuel())
            {
                if (this.launchPhase == EnumLaunchPhase.LAUNCHED.ordinal())
                {
                    if (clientPosY > this.getY())
                    {
                        this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y + (clientPosY - this.getY()) / 40D, this.getDeltaMovement().z);
                    }
                }
                else if (this.launchPhase == EnumLaunchPhase.LANDING.ordinal())
                {
                    if (clientPosY < this.getY())
                    {
                        this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y + (clientPosY - this.getY()) / 40D, this.getDeltaMovement().z);
                    }
                }
            }
            return;
        }
        int launchPhaseLast = this.launchPhase;
        super.decodePacketdata(buffer);
        this.fuelTank.setFluid(new FluidStack(GCFluids.FUEL.getFluid(), buffer.readInt()));
        boolean landingNew = buffer.readBoolean();
        if (landingNew && launchPhaseLast != EnumLaunchPhase.LANDING.ordinal())
        {
            if (this.rocketSoundUpdater != null)
            {
                Minecraft.getInstance().getSoundManager().stop(rocketSoundUpdater);
            }
            this.rocketSoundUpdater = null;  //Flag TickHandlerClient to restart it
        }
        this.destinationFrequency = buffer.readInt();

        if (buffer.readBoolean())
        {
            this.targetVec = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
        }

        double motX = buffer.readDouble() / 8000.0D;
        double motY = buffer.readDouble() / 8000.0D;
        double motZ = buffer.readDouble() / 8000.0D;
        double lastMotY = buffer.readDouble() / 8000.0D;
        double lastLastMotY = buffer.readDouble() / 8000.0D;

        if (!this.hasValidFuel())
        {
            this.setDeltaMovement(motX, motY, motZ);
            this.lastMotionY = lastMotY;
            this.lastLastMotionY = lastLastMotY;
        }

        if (this.stacks == null)
        {
            this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        }

        this.setWaitForPlayer(buffer.readBoolean());

        this.statusMessage = NetworkUtil.readUTF8String(buffer);
        this.statusMessage = this.statusMessage.equals("") ? null : this.statusMessage;
        this.statusMessageCooldown = buffer.readInt();
        this.lastStatusMessageCooldown = buffer.readInt();
        this.statusValid = buffer.readBoolean();

        //Update client with correct rider if needed
        if (this.level.isClientSide)
        {
            int shouldBeMountedId = buffer.readInt();
            if (this.getPassengers().isEmpty())
            {
                if (shouldBeMountedId > -1)
                {
                    Entity e = Minecraft.getInstance().level.getEntity(shouldBeMountedId);
                    if (e != null)
                    {
                        if (e.level != this.level)
                        {
                            if (e instanceof Player)
                            {
                                e = WorldUtil.forceRespawnClient(this.level.dimensionType(), ((ServerPlayer) e).gameMode.getGameModeForPlayer());
                                e.startRiding(this);
                            }
                        }
                        else
                        {
                            e.startRiding(this);
                        }
                    }
                }
            }
            else if (this.getPassengers().get(0).getId() != shouldBeMountedId)
            {
                if (shouldBeMountedId == -1)
                {
                    this.ejectPassengers();
                }
                else
                {
                    Entity e = Minecraft.getInstance().level.getEntity(shouldBeMountedId);
                    if (e != null)
                    {
                        if (e.level != this.level)
                        {
                            if (e instanceof Player)
                            {
                                e = WorldUtil.forceRespawnClient(this.level.dimensionType(), ((ServerPlayer) e).gameMode.getGameModeForPlayer());
                                e.startRiding(this);
                            }
                        }
                        else
                        {
                            e.startRiding(this);
                        }
                    }
                }
            }
        }
        this.statusColour = NetworkUtil.readUTF8String(buffer);
        if (this.statusColour.equals(""))
        {
            this.statusColour = null;
        }
    }

    @Override
    public void getNetworkedData(ArrayList<Object> list)
    {
        if (this.level.isClientSide)
        {
            if (this.getPassengers().contains(Minecraft.getInstance().player) && this.hasValidFuel())
            {
                list.add(this.getY());
            }
            else
            {
                list.add(Double.NaN);
            }
        }
        super.getNetworkedData(list);

        list.add(this.fuelTank.getFluidAmount());
        list.add(this.launchPhase == EnumLaunchPhase.LANDING.ordinal());
        list.add(this.destinationFrequency);
        list.add(this.targetVec != null);

        if (this.targetVec != null)
        {
            list.add(this.targetVec.getX());
            list.add(this.targetVec.getY());
            list.add(this.targetVec.getZ());
        }

        list.add(this.getDeltaMovement().x * 8000.0D);
        list.add(this.getDeltaMovement().y * 8000.0D);
        list.add(this.getDeltaMovement().z * 8000.0D);
        list.add(this.lastMotionY * 8000.0D);
        list.add(this.lastLastMotionY * 8000.0D);

        list.add(this.getWaitForPlayer());

        list.add(this.statusMessage != null ? this.statusMessage : "");
        list.add(this.statusMessageCooldown);
        list.add(this.lastStatusMessageCooldown);
        list.add(this.statusValid);

        if (!this.level.isClientSide)
        {
            list.add(this.getPassengers().isEmpty() ? -1 : this.getPassengers().get(0).getId());
        }
        list.add(this.statusColour != null ? this.statusColour : "");
    }

    @Override
    protected void failRocket()
    {
        this.stopRocketSound();

        if (this.shouldCancelExplosion())
        {
            //TODO: why looking around when already know the target?
            //TODO: it would be good to land on an alternative neighbouring pad if there is already a rocket on the target pad
            for (int i = -3; i <= 3; i++)
            {
                BlockPos pos = new BlockPos((int) Math.floor(this.getX()), (int) Math.floor(this.getY() + i), (int) Math.floor(this.getZ()));
                if (this.launchPhase == EnumLaunchPhase.LANDING.ordinal() && this.targetVec != null && this.level.getBlockEntity(pos) instanceof IFuelDock && this.getY() - this.targetVec.getY() < 5)
                {
                    for (int x = Mth.floor(this.getX()) - 1; x <= Mth.floor(this.getX()) + 1; x++)
                    {
                        for (int y = Mth.floor(this.getY() - 3.0D); y <= Mth.floor(this.getY()) + 1; y++)
                        {
                            for (int z = Mth.floor(this.getZ()) - 1; z <= Mth.floor(this.getZ()) + 1; z++)
                            {
                                BlockPos pos1 = new BlockPos(x, y, z);
                                BlockEntity tile = this.level.getBlockEntity(pos1);

                                if (tile instanceof IFuelDock)
                                {
                                    this.landEntity(pos1);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal())
        {
            super.failRocket();
        }
    }

    protected boolean shouldCancelExplosion()
    {
        return this.hasValidFuel();
    }

    @Override
    public boolean hasValidFuel()
    {
        return this.fuelTank.getFluidAmount() > 0;
    }

    public void cancelLaunch()
    {
        this.setLaunchPhase(EnumLaunchPhase.UNIGNITED);
        this.timeUntilLaunch = 0;
        if (!this.level.isClientSide && !this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof ServerPlayer)
        {
            this.getPassengers().get(0).sendMessage(new TextComponent(GCCoreUtil.translate("gui.rocket.warning.nogyroscope")), this.uuid);
        }
    }

    public void failMessageLaunchController()
    {
        if (!this.level.isClientSide && !this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof ServerPlayer)
        {
            this.getPassengers().get(0).sendMessage(new TextComponent(GCCoreUtil.translate("gui.rocket.warning.launchcontroller")), this.uuid);
        }
    }

    public void failMessageInsufficientFuel()
    {
        if (!this.level.isClientSide && !this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof ServerPlayer)
        {
            this.getPassengers().get(0).sendMessage(new TextComponent(GCCoreUtil.translate("gui.rocket.warning.fuelinsufficient")), this.uuid);
        }
    }

    @Override
    public void onLaunch()
    {
        if (!(this.level.dimensionType() == GalacticraftCore.planetOverworld.getDimensionType() || this.level.dimension() instanceof IGalacticraftDimension))
        {
            if (ConfigManagerCore.disableRocketLaunchAllNonGC.get())
            {
                this.cancelLaunch();
                return;
            }

            //No rocket flight in the Nether, the End etc
            for (int i = ConfigManagerCore.disableRocketLaunchDimensions.get().size() - 1; i >= 0; i--)
            {
                if (ConfigManagerCore.disableRocketLaunchDimensions.get().get(i).equals(this.level.dimension().getRegistryName().toString()))
                {
                    this.cancelLaunch();
                    return;
                }
            }

        }

        super.onLaunch();

        if (!this.level.isClientSide)
        {
            GCPlayerStats stats = null;

            if (!this.getPassengers().isEmpty())
            {
                for (Entity player : this.getPassengers())
                {
                    if (player instanceof ServerPlayer)
                    {
                        stats = GCPlayerStats.get(player);
                        stats.setLaunchpadStack(null);

                        if (!(this.level.dimensionType() instanceof IOrbitDimension))
                        {
                            stats.setCoordsTeleportedFromX(player.getX());
                            stats.setCoordsTeleportedFromZ(player.getZ());
                        }
                    }
                }

                Entity playerMain = this.getPassengers().get(0);
                if (playerMain instanceof ServerPlayer)
                {
                    stats = GCPlayerStats.get(playerMain);
                }
            }

            int amountRemoved = 0;

            PADSEARCH:
            for (int x = Mth.floor(this.getX()) - 1; x <= Mth.floor(this.getX()) + 1; x++)
            {
                for (int y = Mth.floor(this.getY()) - 3; y <= Mth.floor(this.getY()) + 1; y++)
                {
                    for (int z = Mth.floor(this.getZ()) - 1; z <= Mth.floor(this.getZ()) + 1; z++)
                    {
                        BlockPos pos = new BlockPos(x, y, z);
                        final Block block = this.level.getBlockState(pos).getBlock();

//                        if (block != null && block instanceof BlockLandingPadFull)
//                        {
//                            if (amountRemoved < 9)
//                            {
//                                EventLandingPadRemoval event = new EventLandingPadRemoval(this.world, pos);
//                                MinecraftForge.EVENT_BUS.post(event);
//
//                                if (event.allow)
//                                {
//                                    this.world.removeBlock(pos, false);
//                                    amountRemoved = 9;
//                                }
//                                break PADSEARCH;
//                            }
//                        } TODO Full landing pads
                    }
                }
            }

            //Set the player's launchpad item for return on landing - or null if launchpads not removed
            if (stats != null && amountRemoved == 9)
            {
                stats.setLaunchpadStack(new ItemStack(GCBlocks.ROCKET_LAUNCH_PAD, 9));
            }

            this.playSound(SoundEvents.ITEM_PICKUP, 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt)
    {
        super.addAdditionalSaveData(nbt);

        nbt.put("fuelTank", this.fuelTank.writeToNBT(new CompoundTag()));

        if (this.getContainerSize() > 0)
        {
            ContainerHelper.saveAllItems(nbt, this.stacks);
        }

        nbt.putBoolean("TargetValid", this.targetVec != null);

        if (this.targetVec != null)
        {
            nbt.putDouble("targetTileX", this.targetVec.getX());
            nbt.putDouble("targetTileY", this.targetVec.getY());
            nbt.putDouble("targetTileZ", this.targetVec.getZ());
        }

        nbt.putBoolean("WaitingForPlayer", this.getWaitForPlayer());
        nbt.putBoolean("Landing", this.launchPhase == EnumLaunchPhase.LANDING.ordinal());
        nbt.putInt("AutoLaunchSetting", this.autoLaunchSetting != null ? this.autoLaunchSetting.getIndex() : -1);
        nbt.putInt("TimeUntilAutoLaunch", this.autoLaunchCountdown);
        nbt.putInt("DestinationFrequency", this.destinationFrequency);
        if (this.activeLaunchController != null)
        {
            this.activeLaunchController.write(nbt, "ALCat");
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt)
    {
        super.readAdditionalSaveData(nbt);

        if (nbt.contains("fuelTank"))
        {
            this.fuelTank.readFromNBT(nbt.getCompound("fuelTank"));
        }

        if (this.getContainerSize() > 0)
        {
            this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            ContainerHelper.loadAllItems(nbt, this.stacks);
        }

        if (nbt.getBoolean("TargetValid") && nbt.contains("targetTileX"))
        {
            this.targetVec = new BlockPos(Mth.floor(nbt.getDouble("targetTileX")), Mth.floor(nbt.getDouble("targetTileY")), Mth.floor(nbt.getDouble("targetTileZ")));
        }

        this.setWaitForPlayer(nbt.getBoolean("WaitingForPlayer"));
        int autoLaunchValue = nbt.getInt("AutoLaunchSetting");
        this.autoLaunchSetting = autoLaunchValue == -1 ? null : EnumAutoLaunch.values()[autoLaunchValue];
        this.autoLaunchCountdown = nbt.getInt("TimeUntilAutoLaunch");
        this.destinationFrequency = nbt.getInt("DestinationFrequency");
        this.activeLaunchController = BlockVec3.read(nbt, "ALCat");
    }

    @Override
    public int addFuel(FluidStack liquid, IFluidHandler.FluidAction action)
    {
        return FluidUtil.fillWithGCFuel(this.fuelTank, liquid, action);
    }

    @Override
    public FluidStack removeFuel(int amount)
    {
        return this.fuelTank.drain(amount * ConfigManagerCore.rocketFuelFactor.get(), IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public void setPad(IFuelDock pad)
    {
        //Called either when a rocket lands or when one is placed
        //Can also be called with null param when rocket leaves a pad
        this.landingPad = pad;
        if (pad != null)
        {
            pad.dockEntity(this);
            //NOTE: setPad() is called also when a world or chunk is loaded - if the rocket is Ignited (from NBT save data) do not change those settings
            if (!(this.launchPhase == EnumLaunchPhase.IGNITED.ordinal()))
            {
                this.setLaunchPhase(EnumLaunchPhase.UNIGNITED);
                this.targetVec = null;
                if (GalacticraftCore.isPlanetsLoaded)
                {
                    this.updateControllerSettings(pad);
                }
            }
        }
    }

    @Override
    public IFuelDock getLandingPad()
    {
        return this.landingPad;
    }

    @Override
    public int getMaxFuel()
    {
        return this.fuelTank.getCapacity();
    }

    @Override
    public boolean isDockValid(IFuelDock dock)
    {
        return (dock instanceof TileEntityLandingPad);
    }

    @Override
    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
    {
        if (this.getContainerSize() <= 3)
        {
            if (this.autoLaunchSetting == EnumAutoLaunch.CARGO_IS_FULL)
            {
                this.autoLaunch();
            }

            return EnumCargoLoadingState.NOINVENTORY;
        }

        int count = 0;

        for (count = 0; count < this.stacks.size() - 2; count++)
        {
            ItemStack stackAt = this.stacks.get(count);

            if (!stackAt.isEmpty() && stackAt.getItem() == stack.getItem() && stackAt.getDamageValue() == stack.getDamageValue() && stackAt.getCount() < stackAt.getMaxStackSize())
            {
                if (stackAt.getCount() + stack.getCount() <= stackAt.getMaxStackSize())
                {
                    if (doAdd)
                    {
                        stackAt.grow(stack.getCount());
                        this.setChanged();
                    }

                    return EnumCargoLoadingState.SUCCESS;
                }
                else
                {
                    //Part of the stack can fill this slot but there will be some left over
                    int origSize = stackAt.getCount();
                    int surplus = origSize + stack.getCount() - stackAt.getMaxStackSize();

                    if (doAdd)
                    {
                        stackAt.setCount(stackAt.getMaxStackSize());
                        this.setChanged();
                    }

                    stack.setCount(surplus);
                    if (this.addCargo(stack, doAdd) == EnumCargoLoadingState.SUCCESS)
                    {
                        return EnumCargoLoadingState.SUCCESS;
                    }

                    stackAt.setCount(origSize);
                    if (this.autoLaunchSetting == EnumAutoLaunch.CARGO_IS_FULL)
                    {
                        this.autoLaunch();
                    }
                    return EnumCargoLoadingState.FULL;
                }
            }
        }

        for (count = 0; count < this.stacks.size() - 2; count++)
        {
            ItemStack stackAt = this.stacks.get(count);

            if (stackAt.isEmpty())
            {
                if (doAdd)
                {
                    this.stacks.set(count, stack);
                    this.setChanged();
                }

                return EnumCargoLoadingState.SUCCESS;
            }
        }

        if (this.autoLaunchSetting == EnumAutoLaunch.CARGO_IS_FULL)
        {
            this.autoLaunch();
        }

        return EnumCargoLoadingState.FULL;
    }

    @Override
    public RemovalResult removeCargo(boolean doRemove)
    {
        for (int i = 0; i < this.stacks.size() - 2; i++)
        {
            ItemStack stackAt = this.stacks.get(i);

            if (!stackAt.isEmpty())
            {
                ItemStack resultStack = stackAt.copy();
                resultStack.setCount(1);

                if (doRemove)
                {
                    stackAt.shrink(1);
                }

                if (doRemove)
                {
                    this.setChanged();
                }
                return new RemovalResult(EnumCargoLoadingState.SUCCESS, resultStack);
            }
        }

        if (this.autoLaunchSetting == EnumAutoLaunch.CARGO_IS_UNLOADED)
        {
            this.autoLaunch();
        }

        return new RemovalResult(EnumCargoLoadingState.EMPTY, ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItem(int index)
    {
        if (this.stacks == null)
        {
            return ItemStack.EMPTY;
        }

        return this.stacks.get(index);
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
        return ContainerHelper.takeItem(this.stacks, index);
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
    public int getMaxStackSize()
    {
        return 64;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return false;
//    }

    @Override
    public boolean stillValid(Player entityplayer)
    {
        return this.isAlive() && entityplayer.distanceToSqr(this) <= 64.0D;
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemstack)
    {
        return false;
    }

    @Override
    public void setChanged()
    {
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

    @Override
    public void clearContent()
    {

    }

    @Override
    public void onPadDestroyed()
    {
        if (this.isAlive() && this.launchPhase < EnumLaunchPhase.LAUNCHED.ordinal())
        {
            this.dropShipAsItem();
            this.remove();
        }
    }

    @Override
    public List<ItemStack> getItemsDropped(List<ItemStack> droppedItemList)
    {
        if (this.stacks != null)
        {
            for (final ItemStack item : this.stacks)
            {
                if (item != null && !item.isEmpty())
                {
                    droppedItemList.add(item);
                }
            }
        }

        return droppedItemList;
    }

    public boolean getWaitForPlayer()
    {
        return this.waitForPlayer;
    }

    public void setWaitForPlayer(boolean waitForPlayer)
    {
        this.waitForPlayer = waitForPlayer;
    }

    public enum EnumAutoLaunch
    {
        CARGO_IS_UNLOADED(0, "cargo_unloaded"),
        CARGO_IS_FULL(1, "cargo_full"),
        ROCKET_IS_FUELED(2, "fully_fueled"),
        INSTANT(3, "instant"),
        TIME_10_SECONDS(4, "ten_sec"),
        TIME_30_SECONDS(5, "thirty_sec"),
        TIME_1_MINUTE(6, "one_min"),
        REDSTONE_SIGNAL(7, "redstone_sig");

        private final int index;
        private final String title;

        EnumAutoLaunch(int index, String title)
        {
            this.index = index;
            this.title = title;
        }

        public int getIndex()
        {
            return this.index;
        }

        public String getTitle()
        {
            return GCCoreUtil.translate("gui.message." + this.title + "");
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AbstractTickableSoundInstance getSoundUpdater()
    {
        return this.rocketSoundUpdater;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public SoundInstance setSoundUpdater(LocalPlayer player)
    {
        this.rocketSoundUpdater = new SoundUpdaterRocket(player, this);
        return this.rocketSoundUpdater;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRender(double x, double y, double z)
    {
        double d0 = this.getX() - x;
        double d1 = this.getY() - y;
        double d2 = this.getZ() - z;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        return d3 < Constants.RENDERDISTANCE_LONG;
    }

    @Override
    public boolean inFlight()
    {
        return this.getLaunched();
    }
}
