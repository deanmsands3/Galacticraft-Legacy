package team.galacticraft.galacticraft.common.api.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;
import team.galacticraft.galacticraft.common.api.recipe.ISchematicPage;
import team.galacticraft.galacticraft.common.api.recipe.SchematicRegistry;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import team.galacticraft.galacticraft.common.api.vector.Vector3D;
import team.galacticraft.galacticraft.common.compat.PlatformSpecific;
import team.galacticraft.galacticraft.common.compat.cap.GCApiComponents;
import team.galacticraft.galacticraft.common.compat.cap.NbtSerializable;
import team.galacticraft.galacticraft.common.compat.item.ItemInventory;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Predicate;

public class GCPlayerStats implements NbtSerializable {
    public WeakReference<ServerPlayer> player;

    public ItemInventory extendedInventory = PlatformSpecific.createInventory(11, Lists.asList(stack -> true,
            new Predicate[]{stack -> true, stack -> true, stack -> true, stack -> true, stack -> true, stack -> true,
                    stack -> true, stack -> true, stack -> true, stack -> true})); //todo CORE ITEMS

    public int airRemaining;
    public int airRemaining2;

    public int thermalLevel;
    public boolean thermalLevelNormalising;

    public int damageCounter;

    // temporary data while player is in planet selection GUI
    public int spaceshipTier = 1;
    public NonNullList<ItemStack> stacks = NonNullList.withSize(2, ItemStack.EMPTY);
    public int fuelLevel;
    public Item rocketItem;
    public ItemStack launchpadStack;
    public int astroMinerCount = 0;
    private List<BlockVec3> activeAstroMinerChunks = new LinkedList<>();

    public boolean usingParachute;

    public ItemStack parachuteInSlot = ItemStack.EMPTY;
    public ItemStack lastParachuteInSlot = ItemStack.EMPTY;

    public ItemStack frequencyModuleInSlot = ItemStack.EMPTY;
    public ItemStack lastFrequencyModuleInSlot = ItemStack.EMPTY;

    public ItemStack maskInSlot = ItemStack.EMPTY;
    public ItemStack lastMaskInSlot = ItemStack.EMPTY;

    public ItemStack gearInSlot = ItemStack.EMPTY;
    public ItemStack lastGearInSlot = ItemStack.EMPTY;

    public ItemStack tankInSlot1 = ItemStack.EMPTY;
    public ItemStack lastTankInSlot1 = ItemStack.EMPTY;

    public ItemStack tankInSlot2 = ItemStack.EMPTY;
    public ItemStack lastTankInSlot2 = ItemStack.EMPTY;

    public ItemStack thermalHelmetInSlot = ItemStack.EMPTY;
    public ItemStack lastThermalHelmetInSlot = ItemStack.EMPTY;

    public ItemStack thermalChestplateInSlot = ItemStack.EMPTY;
    public ItemStack lastThermalChestplateInSlot = ItemStack.EMPTY;

    public ItemStack thermalLeggingsInSlot = ItemStack.EMPTY;
    public ItemStack lastThermalLeggingsInSlot = ItemStack.EMPTY;

    public ItemStack thermalBootsInSlot = ItemStack.EMPTY;
    public ItemStack lastThermalBootsInSlot = ItemStack.EMPTY;

    public ItemStack shieldControllerInSlot = ItemStack.EMPTY;
    public ItemStack lastShieldControllerInSlot = ItemStack.EMPTY;

    public int launchAttempts = 0;

    public int spaceRaceInviteTeamID;

    public boolean usingPlanetSelectionGui;
    public String savedPlanetList = "";
    public int openPlanetSelectionGuiCooldown;
    public boolean hasOpenedPlanetSelectionGui = false;

    public int chestSpawnCooldown;
    public Vector3D chestSpawnVector;

    public int teleportCooldown;

    public int chatCooldown;

    public double distanceSinceLastStep;
    public int lastStep;

    public double coordsTeleportedFromX;
    public double coordsTeleportedFromZ;

    public HashMap<DimensionType, DimensionType> spaceStationDimensionData = Maps.newHashMap();

    public boolean oxygenSetupValid;
    public boolean lastOxygenSetupValid;

    public boolean touchedGround;
    public boolean lastOnGround;
    public boolean inLander;
    public boolean justLanded;

    public List<ISchematicPage> unlockedSchematics = new LinkedList<>();
    public List<ISchematicPage> lastUnlockedSchematics = new LinkedList<>();

    public int cryogenicChamberCooldown;

    public boolean receivedSoundWarning;
    public boolean receivedBedWarning;
    public boolean openedSpaceRaceManager = false;
    public boolean sentFlags = false;
    public boolean newInOrbit = true;
    public boolean newAdventureSpawn;
    public int buildFlags = 0;

    public int incrementalDamage = 0;
    private float savedSpeed = 0F;  // used by titanium armor

    public String startDimension = "";
    public int glassColor1 = -1;
    public int glassColor2 = -1;
    public int glassColor3 = -1;

    //    private BlockState[] panelLightingBases = new BlockState[BlockPanelLighting.PANELTYPES_LENGTH]; TODO Panel Lighting
    private int panelLightingColor = 0xf0f0e0;

    public static GCPlayerStats get(ServerPlayer player) {
        return GCApiComponents.getPlayerStats().get(player).orElseThrow(new IllegalStateException("Failed to get GC stats!"));
    }

    public GCPlayerStats()
    {
    }

    public GCPlayerStats(WeakReference<ServerPlayer> player)
    {
        this.player = player;
    }

    public WeakReference<ServerPlayer> getPlayer()
    {
        return player;
    }

    public void setPlayer(WeakReference<ServerPlayer> player)
    {
        this.player = player;
    }

    public ItemInventory getExtendedInventory()
    {
        return extendedInventory;
    }

    public void setExtendedInventory(ItemInventory extendedInventory)
    {
        this.extendedInventory = extendedInventory;
    }

    public int getAirRemaining()
    {
        return airRemaining;
    }

    public void setAirRemaining(int airRemaining)
    {
        this.airRemaining = airRemaining;
    }

    public int getAirRemaining2()
    {
        return airRemaining2;
    }

    public void setAirRemaining2(int airRemaining2)
    {
        this.airRemaining2 = airRemaining2;
    }

    public int getThermalLevel()
    {
        return thermalLevel;
    }

    public void setThermalLevel(int thermalLevel)
    {
        this.thermalLevel = thermalLevel;
    }

    public boolean isThermalLevelNormalising()
    {
        return thermalLevelNormalising;
    }

    public void setThermalLevelNormalising(boolean thermalLevelNormalising)
    {
        this.thermalLevelNormalising = thermalLevelNormalising;
    }

    public int getDamageCounter()
    {
        return damageCounter;
    }

    public void setDamageCounter(int damageCounter)
    {
        this.damageCounter = damageCounter;
    }

    public int getSpaceshipTier()
    {
        return spaceshipTier;
    }

    public void setSpaceshipTier(int spaceshipTier)
    {
        this.spaceshipTier = spaceshipTier;
    }

    public NonNullList<ItemStack> getRocketStacks()
    {
        return stacks;
    }

    public void setRocketStacks(NonNullList<ItemStack> rocketStacks)
    {
        this.stacks = rocketStacks;
    }

    public int getFuelLevel()
    {
        return fuelLevel;
    }

    public void setFuelLevel(int fuelLevel)
    {
        this.fuelLevel = fuelLevel;
    }

    public Item getRocketItem()
    {
        return rocketItem;
    }

    public void setRocketItem(Item rocketItem)
    {
        this.rocketItem = rocketItem;
    }

    @Nullable
    public ItemStack getLaunchpadStack()
    {
        return launchpadStack;
    }

    public void setLaunchpadStack(ItemStack launchpadStack)
    {
        this.launchpadStack = launchpadStack;
    }

    public int getAstroMinerCount()
    {
        return astroMinerCount;
    }

    public void setAstroMinerCount(int astroMinerCount)
    {
        this.astroMinerCount = astroMinerCount;
    }

    public List<BlockVec3> getActiveAstroMinerChunks()
    {
        return this.activeAstroMinerChunks;
    }

    public boolean isUsingParachute()
    {
        return usingParachute;
    }

    public void setUsingParachute(boolean usingParachute)
    {
        this.usingParachute = usingParachute;
    }

    public ItemStack getParachuteInSlot()
    {
        return parachuteInSlot;
    }

    public void setParachuteInSlot(ItemStack parachuteInSlot)
    {
        this.parachuteInSlot = parachuteInSlot;
    }

    public ItemStack getLastParachuteInSlot()
    {
        return lastParachuteInSlot;
    }

    public void setLastParachuteInSlot(ItemStack lastParachuteInSlot)
    {
        this.lastParachuteInSlot = lastParachuteInSlot;
    }

    public ItemStack getFrequencyModuleInSlot()
    {
        return frequencyModuleInSlot;
    }

    public void setFrequencyModuleInSlot(ItemStack frequencyModuleInSlot)
    {
        this.frequencyModuleInSlot = frequencyModuleInSlot;
    }

    public ItemStack getLastFrequencyModuleInSlot()
    {
        return lastFrequencyModuleInSlot;
    }

    public void setLastFrequencyModuleInSlot(ItemStack lastFrequencyModuleInSlot)
    {
        this.lastFrequencyModuleInSlot = lastFrequencyModuleInSlot;
    }

    public ItemStack getMaskInSlot()
    {
        return maskInSlot;
    }

    public void setMaskInSlot(ItemStack maskInSlot)
    {
        this.maskInSlot = maskInSlot;
    }

    public ItemStack getLastMaskInSlot()
    {
        return lastMaskInSlot;
    }

    public void setLastMaskInSlot(ItemStack lastMaskInSlot)
    {
        this.lastMaskInSlot = lastMaskInSlot;
    }

    public ItemStack getGearInSlot()
    {
        return gearInSlot;
    }

    public void setGearInSlot(ItemStack gearInSlot)
    {
        this.gearInSlot = gearInSlot;
    }

    public ItemStack getLastGearInSlot()
    {
        return lastGearInSlot;
    }

    public void setLastGearInSlot(ItemStack lastGearInSlot)
    {
        this.lastGearInSlot = lastGearInSlot;
    }

    public ItemStack getTankInSlot1()
    {
        return tankInSlot1;
    }

    public void setTankInSlot1(ItemStack tankInSlot1)
    {
        this.tankInSlot1 = tankInSlot1;
    }

    public ItemStack getLastTankInSlot1()
    {
        return lastTankInSlot1;
    }

    public void setLastTankInSlot1(ItemStack lastTankInSlot1)
    {
        this.lastTankInSlot1 = lastTankInSlot1;
    }

    public ItemStack getTankInSlot2()
    {
        return tankInSlot2;
    }

    public void setTankInSlot2(ItemStack tankInSlot2)
    {
        this.tankInSlot2 = tankInSlot2;
    }

    public ItemStack getLastTankInSlot2()
    {
        return lastTankInSlot2;
    }

    public void setLastTankInSlot2(ItemStack lastTankInSlot2)
    {
        this.lastTankInSlot2 = lastTankInSlot2;
    }

    public ItemStack getThermalHelmetInSlot()
    {
        return thermalHelmetInSlot;
    }

    public void setThermalHelmetInSlot(ItemStack thermalHelmetInSlot)
    {
        this.thermalHelmetInSlot = thermalHelmetInSlot;
    }

    public ItemStack getLastThermalHelmetInSlot()
    {
        return lastThermalHelmetInSlot;
    }

    public void setLastThermalHelmetInSlot(ItemStack lastThermalHelmetInSlot)
    {
        this.lastThermalHelmetInSlot = lastThermalHelmetInSlot;
    }

    public ItemStack getThermalChestplateInSlot()
    {
        return thermalChestplateInSlot;
    }

    public void setThermalChestplateInSlot(ItemStack thermalChestplateInSlot)
    {
        this.thermalChestplateInSlot = thermalChestplateInSlot;
    }

    public ItemStack getLastThermalChestplateInSlot()
    {
        return lastThermalChestplateInSlot;
    }

    public void setLastThermalChestplateInSlot(ItemStack lastThermalChestplateInSlot)
    {
        this.lastThermalChestplateInSlot = lastThermalChestplateInSlot;
    }

    public ItemStack getThermalLeggingsInSlot()
    {
        return thermalLeggingsInSlot;
    }

    public void setThermalLeggingsInSlot(ItemStack thermalLeggingsInSlot)
    {
        this.thermalLeggingsInSlot = thermalLeggingsInSlot;
    }

    public ItemStack getLastThermalLeggingsInSlot()
    {
        return lastThermalLeggingsInSlot;
    }

    public void setLastThermalLeggingsInSlot(ItemStack lastThermalLeggingsInSlot)
    {
        this.lastThermalLeggingsInSlot = lastThermalLeggingsInSlot;
    }

    public ItemStack getThermalBootsInSlot()
    {
        return thermalBootsInSlot;
    }

    public void setThermalBootsInSlot(ItemStack thermalBootsInSlot)
    {
        this.thermalBootsInSlot = thermalBootsInSlot;
    }

    public ItemStack getLastThermalBootsInSlot()
    {
        return lastThermalBootsInSlot;
    }

    public void setLastThermalBootsInSlot(ItemStack lastThermalBootsInSlot)
    {
        this.lastThermalBootsInSlot = lastThermalBootsInSlot;
    }

    public ItemStack getShieldControllerInSlot()
    {
        return shieldControllerInSlot;
    }

    public void setShieldControllerInSlot(ItemStack shieldControllerInSlot)
    {
        this.shieldControllerInSlot = shieldControllerInSlot;
    }

    public ItemStack getLastShieldControllerInSlot()
    {
        return lastShieldControllerInSlot;
    }

    public void setLastShieldControllerInSlot(ItemStack lastShieldControllerInSlot)
    {
        this.lastShieldControllerInSlot = lastShieldControllerInSlot;
    }

    public int getLaunchAttempts()
    {
        return launchAttempts;
    }

    public void setLaunchAttempts(int launchAttempts)
    {
        this.launchAttempts = launchAttempts;
    }

    public int getSpaceRaceInviteTeamID()
    {
        return spaceRaceInviteTeamID;
    }

    public void setSpaceRaceInviteTeamID(int spaceRaceInviteTeamID)
    {
        this.spaceRaceInviteTeamID = spaceRaceInviteTeamID;
    }

    public boolean isUsingPlanetSelectionGui()
    {
        return usingPlanetSelectionGui;
    }

    public void setUsingPlanetSelectionGui(boolean usingPlanetSelectionGui)
    {
        this.usingPlanetSelectionGui = usingPlanetSelectionGui;
    }

    public String getSavedPlanetList()
    {
        return savedPlanetList;
    }

    public void setSavedPlanetList(String savedPlanetList)
    {
        this.savedPlanetList = savedPlanetList;
    }

    public int getOpenPlanetSelectionGuiCooldown()
    {
        return openPlanetSelectionGuiCooldown;
    }

    public void setOpenPlanetSelectionGuiCooldown(int openPlanetSelectionGuiCooldown)
    {
        this.openPlanetSelectionGuiCooldown = openPlanetSelectionGuiCooldown;
    }

    public boolean hasOpenedPlanetSelectionGui()
    {
        return hasOpenedPlanetSelectionGui;
    }

    public void setHasOpenedPlanetSelectionGui(boolean hasOpenedPlanetSelectionGui)
    {
        this.hasOpenedPlanetSelectionGui = hasOpenedPlanetSelectionGui;
    }

    public int getChestSpawnCooldown()
    {
        return chestSpawnCooldown;
    }

    public void setChestSpawnCooldown(int chestSpawnCooldown)
    {
        this.chestSpawnCooldown = chestSpawnCooldown;
    }

    public Vector3D getChestSpawnVector()
    {
        return chestSpawnVector;
    }

    public void setChestSpawnVector(Vector3D chestSpawnVector)
    {
        this.chestSpawnVector = chestSpawnVector;
    }

    public int getTeleportCooldown()
    {
        return teleportCooldown;
    }

    public void setTeleportCooldown(int teleportCooldown)
    {
        this.teleportCooldown = teleportCooldown;
    }

    public int getChatCooldown()
    {
        return chatCooldown;
    }

    public void setChatCooldown(int chatCooldown)
    {
        this.chatCooldown = chatCooldown;
    }

    public double getDistanceSinceLastStep()
    {
        return distanceSinceLastStep;
    }

    public void setDistanceSinceLastStep(double distanceSinceLastStep)
    {
        this.distanceSinceLastStep = distanceSinceLastStep;
    }

    public int getLastStep()
    {
        return lastStep;
    }

    public void setLastStep(int lastStep)
    {
        this.lastStep = lastStep;
    }

    public double getCoordsTeleportedFromX()
    {
        return coordsTeleportedFromX;
    }

    public void setCoordsTeleportedFromX(double coordsTeleportedFromX)
    {
        this.coordsTeleportedFromX = coordsTeleportedFromX;
    }

    public double getCoordsTeleportedFromZ()
    {
        return coordsTeleportedFromZ;
    }

    public void setCoordsTeleportedFromZ(double coordsTeleportedFromZ)
    {
        this.coordsTeleportedFromZ = coordsTeleportedFromZ;
    }

    public HashMap<DimensionType, DimensionType> getSpaceStationDimensionData()
    {
        return spaceStationDimensionData;
    }

    public void setSpaceStationDimensionData(HashMap<DimensionType, DimensionType> spaceStationDimensionData)
    {
        this.spaceStationDimensionData = spaceStationDimensionData;
    }

    public boolean isOxygenSetupValid()
    {
        return oxygenSetupValid;
    }

    public void setOxygenSetupValid(boolean oxygenSetupValid)
    {
        this.oxygenSetupValid = oxygenSetupValid;
    }

    public boolean isLastOxygenSetupValid()
    {
        return lastOxygenSetupValid;
    }

    public void setLastOxygenSetupValid(boolean lastOxygenSetupValid)
    {
        this.lastOxygenSetupValid = lastOxygenSetupValid;
    }

    public boolean isTouchedGround()
    {
        return touchedGround;
    }

    public void setTouchedGround(boolean touchedGround)
    {
        this.touchedGround = touchedGround;
    }

    public boolean isLastOnGround()
    {
        return lastOnGround;
    }

    public void setLastOnGround(boolean lastOnGround)
    {
        this.lastOnGround = lastOnGround;
    }

    public boolean isInLander()
    {
        return inLander;
    }

    public void setInLander(boolean inLander)
    {
        this.inLander = inLander;
    }

    public boolean hasJustLanded()
    {
        return justLanded;
    }

    public void setJustLanded(boolean justLanded)
    {
        this.justLanded = justLanded;
    }

    public List<ISchematicPage> getUnlockedSchematics()
    {
        return unlockedSchematics;
    }

    public void setUnlockedSchematics(List<ISchematicPage> unlockedSchematics)
    {
        this.unlockedSchematics = unlockedSchematics;
    }

    public List<ISchematicPage> getLastUnlockedSchematics()
    {
        return lastUnlockedSchematics;
    }

    public void setLastUnlockedSchematics(List<ISchematicPage> lastUnlockedSchematics)
    {
        this.lastUnlockedSchematics = lastUnlockedSchematics;
    }

    public int getCryogenicChamberCooldown()
    {
        return cryogenicChamberCooldown;
    }

    public void setCryogenicChamberCooldown(int cryogenicChamberCooldown)
    {
        this.cryogenicChamberCooldown = cryogenicChamberCooldown;
    }

    public boolean hasReceivedSoundWarning()
    {
        return receivedSoundWarning;
    }

    public void setReceivedSoundWarning(boolean receivedSoundWarning)
    {
        this.receivedSoundWarning = receivedSoundWarning;
    }

    public boolean hasReceivedBedWarning()
    {
        return receivedBedWarning;
    }

    public void setReceivedBedWarning(boolean receivedBedWarning)
    {
        this.receivedBedWarning = receivedBedWarning;
    }

    public boolean hasOpenedSpaceRaceManager()
    {
        return openedSpaceRaceManager;
    }

    public void setOpenedSpaceRaceManager(boolean openedSpaceRaceManager)
    {
        this.openedSpaceRaceManager = openedSpaceRaceManager;
    }

    public boolean hasSentFlags()
    {
        return sentFlags;
    }

    public void setSentFlags(boolean sentFlags)
    {
        this.sentFlags = sentFlags;
    }

    public boolean isNewInOrbit()
    {
        return newInOrbit;
    }

    public void setNewInOrbit(boolean newInOrbit)
    {
        this.newInOrbit = newInOrbit;
    }

    public boolean isNewAdventureSpawn()
    {
        return newAdventureSpawn;
    }

    public void setNewAdventureSpawn(boolean newAdventureSpawn)
    {
        this.newAdventureSpawn = newAdventureSpawn;
    }

    public int getBuildFlags()
    {
        return buildFlags;
    }

    public void setBuildFlags(int buildFlags)
    {
        this.buildFlags = buildFlags;
    }

    public int getIncrementalDamage()
    {
        return incrementalDamage;
    }

    public void setIncrementalDamage(int incrementalDamage)
    {
        this.incrementalDamage = incrementalDamage;
    }

    public String getStartDimension()
    {
        return startDimension;
    }

    public void setStartDimension(String startDimension)
    {
        this.startDimension = startDimension;
    }

    public CompoundTag saveNBTData(CompoundTag nbt)
    {
        nbt.put("ExtendedInventoryGC", this.extendedInventory.toTag(new CompoundTag()));
        nbt.putInt("playerAirRemaining", this.airRemaining);
        nbt.putInt("damageCounter", this.damageCounter);
        nbt.putBoolean("OxygenSetupValid", this.oxygenSetupValid);
        nbt.putBoolean("usingParachute2", this.usingParachute);
        nbt.putBoolean("usingPlanetSelectionGui", this.usingPlanetSelectionGui);
        nbt.putInt("teleportCooldown", this.teleportCooldown);
        nbt.putDouble("coordsTeleportedFromX", this.coordsTeleportedFromX);
        nbt.putDouble("coordsTeleportedFromZ", this.coordsTeleportedFromZ);
        nbt.putString("startDimension", this.startDimension);
//        nbt.putString("spaceStationDimensionInfo", WorldUtil.spaceStationDataToString(this.spaceStationDimensionData)); //todo worldutil
        nbt.putInt("thermalLevel", this.thermalLevel);

        Collections.sort(this.unlockedSchematics);

        ListTag tagList = new ListTag();

        for (ISchematicPage page : this.unlockedSchematics)
        {
            if (page != null)
            {
                final CompoundTag nbttagcompound = new CompoundTag();
                nbttagcompound.putInt("UnlockedPage", page.getPageID());
                tagList.add(nbttagcompound);
            }
        }

        nbt.put("Schematics", tagList);

        nbt.putInt("rocketStacksLength", this.stacks.size());
        nbt.putInt("SpaceshipTier", this.spaceshipTier);
        nbt.putInt("FuelLevel", this.fuelLevel);
        if (this.rocketItem != null)
        {
            ItemStack returnRocket = new ItemStack(this.rocketItem, 1);
            nbt.put("ReturnRocket", returnRocket.save(new CompoundTag()));
        }

        ListTag nbttaglist = new ListTag();

        for (int i = 0; i < this.stacks.size(); ++i)
        {
            ItemStack itemstack = this.stacks.get(i);

            if (!itemstack.isEmpty())
            {
                CompoundTag nbttagcompound = new CompoundTag();
                nbttagcompound.putByte("Slot", (byte) i);
                itemstack.save(nbttagcompound);
                nbttaglist.add(nbttagcompound);
            }
        }

        if (!nbttaglist.isEmpty())
        {
            nbt.put("RocketItems", nbttaglist);
        }

        final CompoundTag var4 = new CompoundTag();
        if (this.launchpadStack != null)
        {
            nbt.put("LaunchpadStack", this.launchpadStack.save(var4));
        }
        else
        {
            nbt.put("LaunchpadStack", var4);
        }

        nbt.putInt("CryogenicChamberCooldown", this.cryogenicChamberCooldown);
        nbt.putBoolean("ReceivedSoundWarning", this.receivedSoundWarning);
        nbt.putBoolean("ReceivedBedWarning", this.receivedBedWarning);
        nbt.putInt("BuildFlags", this.buildFlags);
        nbt.putBoolean("ShownSpaceRace", this.openedSpaceRaceManager);
        nbt.putInt("AstroMinerCount", this.astroMinerCount);
        ListTag astroList = new ListTag();
        for (BlockVec3 data : this.activeAstroMinerChunks)
        {
            if (data != null)
            {
                astroList.add(data.write(new CompoundTag()));
            }
        }
        nbt.put("AstroData", astroList);

        nbt.putInt("GlassColor1", this.glassColor1);
        nbt.putInt("GlassColor2", this.glassColor2);
        nbt.putInt("GlassColor3", this.glassColor3);

//        ListNBT panelList = new ListNBT();
//        for (int i = 0; i < BlockPanelLighting.PANELTYPES_LENGTH; ++i)
//        {
//            final CompoundNBT stateNBT = new CompoundNBT();
//            BlockState bs = this.panelLightingBases[i];
//            if (bs != null)
//            {
//                TileEntityPanelLight.writeBlockState(stateNBT, bs);
//            }
//            panelList.add(stateNBT);
//        } TODO Panel Lighting
//        nbt.put("PanLi", panelList);

        nbt.putInt("PanCo", this.panelLightingColor);
        return nbt;
    }

    public void loadNBTData(CompoundTag nbt)
    {
        try
        {
            this.airRemaining = nbt.getInt("playerAirRemaining");
            this.damageCounter = nbt.getInt("damageCounter");
            this.oxygenSetupValid = this.lastOxygenSetupValid = nbt.getBoolean("OxygenSetupValid");
            this.thermalLevel = nbt.getInt("thermalLevel");

            // Backwards compatibility
//            ListTag nbttaglist = nbt.getList("Inventory", 10);
//            this.extendedInventory.readFromNBTOld(nbttaglist);

            if (nbt.contains("ExtendedInventoryGC"))
            {
                this.extendedInventory.fromTag(nbt.getCompound("ExtendedInventoryGC"));
            }

            // Added for GCInv command - if tried to load an offline player's
            // inventory, load it now
            // (if there was no offline load, then the dontload flag in doLoad()
            // will make sure nothing happens)
            ServerPlayer p = this.player.get();
//            if (p != null)
//            {
//                ItemStack[] saveinv = CommandGCInv.getSaveData(PlayerUtil.getName(p).toLowerCase());
//                if (saveinv != null)
//                {
//                    CommandGCInv.doLoad(p);
//                }
//            } TODO Commands

            if (nbt.contains("SpaceshipTier"))
            {
                this.spaceshipTier = nbt.getInt("SpaceshipTier");
            }

            //New keys in version 3.0.5.220
            if (nbt.contains("FuelLevel"))
            {
                this.fuelLevel = nbt.getInt("FuelLevel");
            }
            if (nbt.contains("ReturnRocket"))
            {
                ItemStack returnRocket = ItemStack.of(nbt.getCompound("ReturnRocket"));
                this.rocketItem = returnRocket.getItem();
            }

            this.usingParachute = nbt.getBoolean("usingParachute2");
            this.usingPlanetSelectionGui = nbt.getBoolean("usingPlanetSelectionGui");
            this.teleportCooldown = nbt.getInt("teleportCooldown");
            this.coordsTeleportedFromX = nbt.getDouble("coordsTeleportedFromX");
            this.coordsTeleportedFromZ = nbt.getDouble("coordsTeleportedFromZ");
            this.startDimension = nbt.contains("startDimension") ? nbt.getString("startDimension") : "";
            if (nbt.contains("spaceStationDimensionID"))
            {
                // If loading from an old save file, the home space station is always the overworld, so use 0 as home planet
//                this.spaceStationDimensionData = WorldUtil.stringToSpaceStationData("0$" + nbt.getInt("spaceStationDimensionID"));
            }
            else //TODO _M
            {
//                this.spaceStationDimensionData = WorldUtil.stringToSpaceStationData(nbt.getString("spaceStationDimensionInfo"));
            }

            if (nbt.getBoolean("usingPlanetSelectionGui"))
            {
                this.openPlanetSelectionGuiCooldown = 20;
            }

            if (nbt.contains("RocketItems") && nbt.contains("rocketStacksLength"))
            {
                int length = nbt.getInt("rocketStacksLength");

                this.stacks = NonNullList.withSize(length, ItemStack.EMPTY);

                ListTag nbttaglist1 = nbt.getList("RocketItems", 10);

                for (int i = 0; i < nbttaglist1.size(); ++i)
                {
                    CompoundTag nbttagcompound = nbttaglist1.getCompound(i);
                    int j = nbttagcompound.getByte("Slot") & 255;

                    if (j >= 0 && j < this.stacks.size())
                    {
                        this.stacks.set(j, ItemStack.of(nbttagcompound));
                    }
                }
            }

            this.unlockedSchematics = new ArrayList<ISchematicPage>();

            if (p != null)
            {
                for (int i = 0; i < nbt.getList("Schematics", 10).size(); ++i)
                {
                    final CompoundTag nbttagcompound = nbt.getList("Schematics", 10).getCompound(i);

                    final int j = nbttagcompound.getInt("UnlockedPage");

                    SchematicRegistry.addUnlockedPage(p, SchematicRegistry.getMatchingRecipeForID(j));
                }
            }

            Collections.sort(this.unlockedSchematics);

            this.cryogenicChamberCooldown = nbt.getInt("CryogenicChamberCooldown");

            if (nbt.contains("ReceivedSoundWarning"))
            {
                this.receivedSoundWarning = nbt.getBoolean("ReceivedSoundWarning");
            }
            if (nbt.contains("ReceivedBedWarning"))
            {
                this.receivedBedWarning = nbt.getBoolean("ReceivedBedWarning");
            }

            if (nbt.contains("LaunchpadStack"))
            {
                this.launchpadStack = ItemStack.of(nbt.getCompound("LaunchpadStack"));
            }
//            else
//            {
//                // for backwards compatibility with saves which don't have this tag - players can't lose launchpads
//                this.launchpadStack = new ItemStack(GCBlocks.landingPad, 9);
//            }

            if (nbt.contains("BuildFlags"))
            {
                this.buildFlags = nbt.getInt("BuildFlags");
            }

            if (nbt.contains("ShownSpaceRace"))
            {
                this.openedSpaceRaceManager = nbt.getBoolean("ShownSpaceRace");
            }

            if (nbt.contains("AstroMinerCount"))
            {
                this.astroMinerCount = nbt.getInt("AstroMinerCount");
            }
            if (nbt.contains("AstroData"))
            {
                this.activeAstroMinerChunks.clear();
                ListTag astroList = nbt.getList("AstroData", 10);
                for (int i = 0; i < astroList.size(); ++i)
                {
                    final CompoundTag nbttagcompound = astroList.getCompound(i);
                    BlockVec3 data = BlockVec3.read(nbttagcompound);
                    this.activeAstroMinerChunks.add(data);
                }
//                if (GalacticraftCore.isPlanetsLoaded)
//                {
//                    AsteroidsTickHandlerServer.loadAstroChunkList(this.activeAstroMinerChunks);
//                } TODO Planets
            }

            if (nbt.contains("GlassColor1"))
            {
                this.glassColor1 = nbt.getInt("GlassColor1");
                this.glassColor2 = nbt.getInt("GlassColor2");
                this.glassColor3 = nbt.getInt("GlassColor3");
            }

//            if (nbt.contains("PanLi"))
//            {
//                final ListNBT panels = nbt.getList("PanLi", 10);
//                for (int i = 0; i < panels.size(); ++i)
//                {
//                    if (i == BlockPanelLighting.PANELTYPES_LENGTH) break;
//                    final CompoundNBT stateNBT = panels.getCompound(i);
//                    BlockState bs = TileEntityPanelLight.readBlockState(stateNBT);
//                    this.panelLightingBases[i] = (bs.getBlock() == Blocks.AIR) ? null : bs;
//                }
//            } TODO Panel Lighting

            if (nbt.contains("PanCo"))
            {
                this.panelLightingColor = nbt.getInt("PanCo");
            }


            PlatformSpecific.getLogger().debug("Loading GC player data for " + player.get().getName() + " : " + this.buildFlags);

            this.sentFlags = false;
        }
        catch (Exception e)
        {
            PlatformSpecific.getLogger().fatal("Found error in saved Galacticraft player data for " + player.get().getName() + " - this should fix itself next relog.");
            e.printStackTrace();
        }

        PlatformSpecific.getLogger().debug("Finished loading GC player data for " + player.get().getName() + " : " + this.buildFlags);
    }

    public void copyFrom(GCPlayerStats oldData, boolean keepInv)
    {
        if (keepInv)
        {
            this.extendedInventory.fromTag(oldData.getExtendedInventory().toTag(new CompoundTag()));
        }

        this.spaceStationDimensionData = oldData.getSpaceStationDimensionData();
        this.unlockedSchematics = oldData.getUnlockedSchematics();
        this.receivedSoundWarning = oldData.hasReceivedSoundWarning();
        this.receivedBedWarning = oldData.hasReceivedBedWarning();
        this.openedSpaceRaceManager = oldData.hasOpenedSpaceRaceManager();
        this.spaceRaceInviteTeamID = oldData.getSpaceRaceInviteTeamID();
        this.buildFlags = oldData.getBuildFlags();
        this.glassColor1 = oldData.getGlassColor1();
        this.glassColor2 = oldData.getGlassColor2();
        this.glassColor3 = oldData.getGlassColor3();
//        this.panelLightingBases = oldData.getPanelLightingBases(); TODO Panel lighting
        this.panelLightingColor = oldData.getPanelLightingColor();
        this.astroMinerCount = oldData.getAstroMinerCount();
        this.activeAstroMinerChunks = oldData.getActiveAstroMinerChunks();
        this.sentFlags = false;
    }

    public void setGlassColors(int color1, int color2, int color3)
    {
        boolean changes = false;
        if (this.glassColor1 != color1)
        {
            changes = true;
            this.glassColor1 = color1;
        }
        if (this.glassColor2 != color2)
        {
            changes = true;
            this.glassColor2 = color2;
        }
        if (this.glassColor3 != color3)
        {
            changes = true;
            this.glassColor3 = color3;
        }
        if (changes)
        {
//            ColorUtil.sendUpdatedColorsToPlayer(this); //todo colorutil
        }
    }

    public int getGlassColor1()
    {
        return glassColor1;
    }

    public int getGlassColor2()
    {
        return glassColor2;
    }

    public int getGlassColor3()
    {
        return glassColor3;
    }

//    @Override
//    public BlockState[] getPanelLightingBases()
//    {
//        return panelLightingBases;
//    } TODO Panel Lighting

    public int getPanelLightingColor()
    {
        return panelLightingColor;
    }

    public void setPanelLightingColor(int color)
    {
        panelLightingColor = color;
    }

//    @Override
//    public Object[] getMiscNetworkedStats()
//    {
//        int length = 2 + BlockPanelLighting.PANELTYPES_LENGTH * 2;
//        Object[] result = new Object[length];
//        result[0] = this.getBuildFlags();
//        BlockPanelLighting.getNetworkedData(result, this.panelLightingBases);
//        result[length - 1] = this.panelLightingColor;
//        return result;
//    } TODO Panel Lighting

    public void setSavedSpeed(float value)
    {
        this.savedSpeed = value;
    }

    public float getSavedSpeed()
    {
        return this.savedSpeed;
    }

    @Override
    public void fromTag(CompoundTag var1) {
loadNBTData(var1);
    }

    @Override
    public CompoundTag toTag(CompoundTag var1) {
        return saveNBTData(var1);
    }

//    private EnumExtendedInventorySlot getTypeFromSlot() //todo CORE ITEMS (see ext inv)
//    {
//        switch (this.getSlotIndex())
//        {
//            case 0:
//                return EnumExtendedInventorySlot.MASK;
//            case 1:
//                return EnumExtendedInventorySlot.GEAR;
//            case 2:
//                return EnumExtendedInventorySlot.LEFT_TANK;
//            case 3:
//                return EnumExtendedInventorySlot.RIGHT_TANK;
//            case 4:
//                return EnumExtendedInventorySlot.PARACHUTE;
//            case 5:
//                return EnumExtendedInventorySlot.FREQUENCY_MODULE;
//            case 6:
//                return EnumExtendedInventorySlot.THERMAL_HELMET;
//            case 7:
//                return EnumExtendedInventorySlot.THERMAL_CHESTPLATE;
//            case 8:
//                return EnumExtendedInventorySlot.THERMAL_LEGGINGS;
//            case 9:
//                return EnumExtendedInventorySlot.THERMAL_BOOTS;
//            case 10:
//                return EnumExtendedInventorySlot.SHIELD_CONTROLLER;
//        }
//
//        return null;
//    }
}
