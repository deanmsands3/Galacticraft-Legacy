package micdoodle8.mods.galacticraft.core.util;

import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import micdoodle8.mods.galacticraft.annotations.ForRemoval;
import micdoodle8.mods.galacticraft.annotations.ReplaceWith;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IAntiGrav;
import micdoodle8.mods.galacticraft.api.entity.IWorldTransferCallback;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.galaxies.Satellite;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.api.world.SpaceStationType;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.SpaceStationWorldData;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.entities.EntityCelestialFake;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.ItemParaChute;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTelemetry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.server.permission.PermissionAPI;
import org.apache.commons.lang3.ArrayUtils;

/**
 * This class will be removed and all methods
 * moved to a dedicated utility class. At this time all planned utility classes
 * will be internal and non-accessible.
 * <P>
 * Certain methods commonly used will be moved into the API package for use by addon developers.
 * When more information is available each method will be annotated accordingly with {@link ReplaceWith}
 * <p>
 *
 */
@ForRemoval(deadline = "4.1.0")
public class WorldUtil
{

    /**
     * Dimension IDs and providers (providers are -26 or -27 by default)
     */
    public static HashMap<Integer, Integer> registeredSpaceStations;

    /**
     * Dimension IDs and provider names
     */
    public static Map<Integer, String> dimNames = new TreeMap<>();
    public static Map<EntityPlayerMP, HashMap<String, Integer>> celestialMapCache = new MapMaker().weakKeys().makeMap();
    public static List<Integer> registeredPlanets;

    public static float getGravityFactor(Entity entity)
    {
        if (entity.world.provider instanceof IGalacticraftWorldProvider)
        {
            final IGalacticraftWorldProvider customProvider = (IGalacticraftWorldProvider) entity.world.provider;
            float returnValue = MathHelper.sqrt(0.08F / (0.08F - customProvider.getGravity()));
            if (returnValue > 2.5F)
            {
                returnValue = 2.5F;
            }
            if (returnValue < 0.75F)
            {
                returnValue = 0.75F;
            }
            return returnValue;
        }
        if (entity instanceof IAntiGrav)
        {
        }
        return 1F;
    }

    public static Vector3 getWorldColor(World world)
    {
        return new Vector3(1, 1, 1);
    }

    public static WorldProvider getProviderForNameServer(String par1String)
    {
        String nameToFind = par1String;
        if (par1String.contains("$"))
        {
            final String[] twoDimensions = par1String.split("\\$");
            nameToFind = twoDimensions[0];
        }
        if (nameToFind == null)
        {
            return null;
        }

        for (Map.Entry<Integer, String> element : WorldUtil.dimNames.entrySet())
        {
            if (nameToFind.equalsIgnoreCase(element.getValue()))
            {
                return WorldUtil.getProviderForDimensionServer(element.getKey());
            }
        }

        GalacticraftCore.logger.info("Failed to find matching world for '" + par1String + "'");
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static WorldProvider getProviderForNameClient(String par1String)
    {
        String nameToFind = par1String;
        if (par1String.contains("$"))
        {
            final String[] twoDimensions = par1String.split("\\$");
            nameToFind = twoDimensions[0];
        }
        if (nameToFind == null)
        {
            return null;
        }

        for (Map.Entry<Integer, String> element : WorldUtil.dimNames.entrySet())
        {
            if (nameToFind.equalsIgnoreCase(element.getValue()))
            {
                return WorldUtil.getProviderForDimensionClient(element.getKey());
            }
        }

        GalacticraftCore.logger.info("Failed to find matching world for '" + par1String + "'");
        return null;
    }

    public static void initialiseDimensionNames()
    {
        WorldProvider provider = WorldUtil.getProviderForDimensionServer(ConfigManagerCore.idDimensionOverworld);
        WorldUtil.dimNames.put(ConfigManagerCore.idDimensionOverworld, provider.getDimensionType().getName());
    }

    /**
     * This will *load* all the GC dimensions which the player has access to
     * (taking account of space station permissions). Loading the dimensions
     * through Forge activates any chunk loaders or forced chunks in that
     * dimension, if the dimension was not previously loaded. This may place
     * load on the server.
     *
     * @param tier - the rocket tier to test
     * @param playerBase - the player who will be riding the rocket (needed for
     *        space station permissions)
     * @return a List of integers which are the dimension IDs
     */
    public static List<Integer> getPossibleDimensionsForSpaceshipTier(int tier, EntityPlayerMP playerBase)
    {
        List<Integer> temp = new ArrayList<>();

        if (!ConfigManagerCore.disableRocketsToOverworld)
        {
            temp.add(ConfigManagerCore.idDimensionOverworld);
        }

        for (Integer element : WorldUtil.registeredPlanets)
        {
            if (element == ConfigManagerCore.idDimensionOverworld)
            {
                continue;
            }
            WorldProvider provider = WorldUtil.getProviderForDimensionServer(element);

            if (provider != null)
            {
                if (provider instanceof IGalacticraftWorldProvider)
                {
                    if (((IGalacticraftWorldProvider) provider).canSpaceshipTierPass(tier))
                    {
                        temp.add(element);
                    }
                } else
                {
                    temp.add(element);
                }
            }
        }

        for (Integer element : WorldUtil.registeredSpaceStations.keySet())
        {
            final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.world, element, null);

            if (!ConfigManagerCore.spaceStationsRequirePermission || data.getAllowedAll() || data.getAllowedPlayers().contains(PlayerUtil.getName(playerBase))
                || ArrayUtils.contains(playerBase.server.getPlayerList().getOppedPlayerNames(), playerBase.getName()))
            {
                // Satellites always reachable from their own homeworld or from
                // its other satellites
                if (playerBase != null)
                {
                    int currentWorld = playerBase.dimension;
                    // Player is on homeworld
                    if (currentWorld == data.getHomePlanet())
                    {
                        temp.add(element);
                        continue;
                    }
                    if (playerBase.world.provider instanceof IOrbitDimension)
                    {
                        // Player is currently on another space station around
                        // the same planet
                        final SpaceStationWorldData dataCurrent = SpaceStationWorldData.getStationData(playerBase.world, playerBase.dimension, null);
                        if (dataCurrent.getHomePlanet() == data.getHomePlanet())
                        {
                            temp.add(element);
                            continue;
                        }
                    }
                }

                // Testing dimension is a satellite, but with a different
                // homeworld - test its tier
                WorldProvider homeWorld = WorldUtil.getProviderForDimensionServer(data.getHomePlanet());

                if (homeWorld != null)
                {
                    if (homeWorld instanceof IGalacticraftWorldProvider)
                    {
                        if (((IGalacticraftWorldProvider) homeWorld).canSpaceshipTierPass(tier))
                        {
                            temp.add(element);
                        }
                    } else
                    {
                        temp.add(element);
                    }
                }
            }
        }

        return temp;
    }

    public static CelestialBody getReachableCelestialBodiesForDimensionID(int id)
    {
        List<CelestialBody> celestialBodyList = Lists.newArrayList();
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredSatellites().values());

        for (CelestialBody cBody : celestialBodyList)
        {
            if (cBody.isReachable())
            {
                if (cBody.getDimensionID() == id)
                {
                    return cBody;
                }
            }
        }

        return null;
    }

    public static CelestialBody getReachableCelestialBodiesForName(String name)
    {
        List<CelestialBody> celestialBodyList = Lists.newArrayList();
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredSatellites().values());

        for (CelestialBody cBody : celestialBodyList)
        {
            if (cBody.isReachable())
            {
                if (cBody.getName().equals(name))
                {
                    return cBody;
                }
            }
        }

        return null;
    }

    /**
     * CAUTION: this loads the dimension if it is not already loaded. This can
     * cause server load if used too frequently or with a list of multiple
     * dimensions.
     *
     * @param id
     * @return
     */
    public static World getWorldForDimensionServer(int id)
    {
        MinecraftServer theServer = GCCoreUtil.getServer();
        if (theServer == null)
        {
            GalacticraftCore.logger.debug("Called WorldUtil server side method but FML returned no server - is this a bug?");
            return null;
        }
        return theServer.getWorld(id);
    }

    /**
     * CAUTION: this loads the dimension if it is not already loaded. This can
     * cause server load if used too frequently or with a list of multiple
     * dimensions.
     *
     * @param id
     * @return
     */
    public static WorldProvider getProviderForDimensionServer(int id)
    {
        World ws = getWorldForDimensionServer(id);
        if (ws != null)
        {
            return ws.provider;
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static WorldProvider getProviderForDimensionClient(int id)
    {
        World ws = ClientProxyCore.mc.world;
        if (ws != null && GCCoreUtil.getDimensionID(ws) == id)
        {
            return ws.provider;
        }
        return DimensionManager.createProviderFor(id);
    }

    /**
     * This will *load* all the GC dimensions which the player has access to
     * (taking account of space station permissions). Loading the dimensions
     * through Forge activates any chunk loaders or forced chunks in that
     * dimension, if the dimension was not previously loaded. This may place
     * load on the server.
     *
     * @param tier - the rocket tier to test
     * @param playerBase - the player who will be riding the rocket (needed for
     *        checking space station permissions)
     * @return a Map of the names of the dimension vs. the dimension IDs
     */
    public static HashMap<String, Integer> getArrayOfPossibleDimensions(int tier, EntityPlayerMP playerBase)
    {
        List<Integer> ids = WorldUtil.getPossibleDimensionsForSpaceshipTier(tier, playerBase);
        final HashMap<String, Integer> map = new HashMap<>(ids.size(), 1F);

        for (Integer id : ids)
        {
            CelestialBody celestialBody = getReachableCelestialBodiesForDimensionID(id);

            // It's a space station
            if (id > 0 && celestialBody == null)
            {
                celestialBody = GalacticraftCore.satelliteSpaceStation;
                // This no longer checks whether a WorldProvider can be created,
                // for performance reasons (that causes the dimension to load
                // unnecessarily at map building stage)
                if (playerBase != null)
                {
                    final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.world, id, null);
                    map.put(celestialBody.getName() + "$" + data.getOwner() + "$" + data.getSpaceStationName() + "$" + id + "$" + data.getHomePlanet(), id);
                }
            } else if (celestialBody == GalacticraftCore.planetOverworld)
            {
                map.put(celestialBody.getName(), id);
            } else
            {
                WorldProvider provider = WorldUtil.getProviderForDimensionServer(id);
                if (celestialBody != null && provider != null)
                {
                    if (provider instanceof IGalacticraftWorldProvider && !(provider instanceof IOrbitDimension) || GCCoreUtil.getDimensionID(provider) == 0)
                    {
                        map.put(celestialBody.getName(), GCCoreUtil.getDimensionID(provider));
                    }
                }
            }
        }

        ArrayList<CelestialBody> cBodyList = new ArrayList<>();
        cBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
        cBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());

        for (CelestialBody body : cBodyList)
        {
            if (!body.isReachable())
            {
                map.put(body.getTranslatedName() + "*", body.getDimensionID());
            }
        }

        WorldUtil.celestialMapCache.put(playerBase, map);
        return map;
    }

    /**
     * Get the cached version of getArrayOfPossibleDimensions() to reduce server
     * load + unwanted dimension loading The cache will be updated every time
     * the 'proper' version of getArrayOfPossibleDimensions is called.
     *
     * @param tier - the rocket tier to test
     * @param playerBase - the player who will be riding the rocket (needed for
     *        checking space station permissions)
     * @return a Map of the names of the dimension vs. the dimension IDs
     */
    public static HashMap<String, Integer> getArrayOfPossibleDimensionsAgain(int tier, EntityPlayerMP playerBase)
    {
        HashMap<String, Integer> map = WorldUtil.celestialMapCache.get(playerBase);
        if (map != null)
        {
            return map;
        }
        return getArrayOfPossibleDimensions(tier, playerBase);
    }

    public static void unregisterSpaceStations()
    {
        if (WorldUtil.registeredSpaceStations != null)
        {
            for (Integer registeredID : WorldUtil.registeredSpaceStations.keySet())
            {
                DimensionManager.unregisterDimension(registeredID);
            }

            WorldUtil.registeredSpaceStations = null;
        }
    }

    public static void registerSpaceStations(MinecraftServer theServer, File spaceStationList)
    {
        //        WorldUtil.registeredSpaceStations = WorldUtil.getExistingSpaceStationList(spaceStationList);
        WorldUtil.registeredSpaceStations = Maps.newHashMap();
        if (theServer == null || !spaceStationList.exists() && !spaceStationList.isDirectory())
        {
            return;
        }

        final File[] var2 = spaceStationList.listFiles();

        if (var2 != null)
        {
            for (File var5 : var2)
            {
                if (var5.getName().startsWith("spacestation_") && var5.getName().endsWith(".dat"))
                {
                    try
                    {
                        // Note: this is kind of a hacky way of doing this,
                        // loading the NBT from each space station file
                        // during dimension registration, to find out what each
                        // space station's provider IDs are.

                        String name = var5.getName();
                        SpaceStationWorldData worldDataTemp = new SpaceStationWorldData(name);
                        name = name.substring(13, name.length() - 4);
                        int registeredID = Integer.parseInt(name);

                        FileInputStream fileinputstream = new FileInputStream(var5);
                        NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);
                        fileinputstream.close();
                        worldDataTemp.readFromNBT(nbttagcompound.getCompoundTag("data"));

                        // Search for id in server-defined statically loaded
                        // dimensions
                        int index = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, registeredID);

                        int providerID = index >= 0 ? worldDataTemp.getDimensionIdStatic() : worldDataTemp.getDimensionIdDynamic();
                        boolean registrationOK = false;
                        if (!DimensionManager.isDimensionRegistered(registeredID))
                        {
                            DimensionManager.registerDimension(registeredID, WorldUtil.getDimensionTypeById(providerID));
                            registrationOK = true;
                        } else if (GalacticraftRegistry.isDimensionTypeIDRegistered(providerID))
                        {
                            registrationOK = DimensionManager.getProviderType(registeredID).getId() == providerID;
                            if (!registrationOK)
                            {
                                try
                                {
                                    Class sponge = Class.forName("org.spongepowered.common.world.WorldManager");
                                    Field dtDI = sponge.getDeclaredField("dimensionTypeByDimensionId");
                                    dtDI.setAccessible(true);
                                    Int2ReferenceMap<DimensionType> result = (Int2ReferenceMap<DimensionType>) dtDI.get(null);
                                    if (result != null)
                                    {
                                        result.put(registeredID, WorldUtil.getDimensionTypeById(providerID));
                                        GalacticraftCore.logger.info("Re-registered dimension type " + providerID);
                                    }
                                    registrationOK = true;
                                } catch (ClassNotFoundException ignore)
                                {
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (registrationOK)
                        {
                            WorldUtil.registeredSpaceStations.put(registeredID, providerID);
                            if (index >= 0)
                            {
                                theServer.getWorld(registeredID);
                            }
                            WorldUtil.dimNames.put(registeredID, "Space Station " + registeredID);
                        } else
                        {
                            GalacticraftCore.logger.error("Dimension already registered to another mod: unable to register space station dimension " + registeredID);
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        //        for (Integer registeredID : WorldUtil.registeredSpaceStations)
        //        {
        //            int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, registeredID);
        //
        //            if (!DimensionManager.isDimensionRegistered(registeredID))
        //            {
        //	            if (id >= 0)
        //	            {
        //	                DimensionManager.registerDimension(registeredID, ConfigManagerCore.idDimensionOverworldOrbitStatic);
        //	                theServer.getWorld(registeredID);
        //                }
        //	            else
        //	            {
        //	                DimensionManager.registerDimension(registeredID, ConfigManagerCore.idDimensionOverworldOrbit);
        //	            }
        //            }
        //            else
        //            {
        //                GCLog.severe("Dimension already registered to another mod: unable to register space station dimension " + registeredID);
        //            }
        //        }
    }

    /**
     * Call this on FMLServerStartingEvent to register a planet which has a
     * dimension ID. Now returns a boolean to indicate whether registration was
     * successful. <p> NOTE: Planets and Moons dimensions should normally be
     * initialised at server init If you do not do this, you must find your own
     * way to register the dimension in DimensionManager and you must find your
     * own way to include the cached provider name in WorldUtil.dimNames <p>
     * IMPORTANT: GalacticraftRegistry.registerDimension() must always be called
     * in parallel with this meaning the CelestialBodies are iterated in the
     * same order when registered there and here.
     *
     * The defaultID should be 0, and the id should be both a dimension ID and a
     * DimensionType id.
     */
    public static boolean registerPlanet(int id, boolean initialiseDimensionAtServerInit, int defaultID)
    {
        if (WorldUtil.registeredPlanets == null)
        {
            WorldUtil.registeredPlanets = new ArrayList<>();
        }

        if (initialiseDimensionAtServerInit)
        {
            if (!DimensionManager.isDimensionRegistered(id))
            {
                DimensionManager.registerDimension(id, WorldUtil.getDimensionTypeById(id));
                GalacticraftCore.logger.info("Registered Dimension: " + id);
                WorldUtil.registeredPlanets.add(id);
            } else if (DimensionManager.getProviderType(id).getId() == id && GalacticraftRegistry.isDimensionTypeIDRegistered(id))
            {
                GalacticraftCore.logger.info("Re-registered dimension: " + id);
                WorldUtil.registeredPlanets.add(id);
            } else
            {
                GalacticraftCore.logger.error("Dimension already registered: unable to register planet dimension " + id);
                // Add 0 to the list to preserve the correct order of the
                // other planets (e.g. if server/client initialise with
                // different dimension IDs in configs, the order becomes
                // important for figuring out what is going on)
                WorldUtil.registeredPlanets.add(defaultID);
                return false;
            }
            DimensionType dt = WorldUtil.getDimensionTypeById(id);
            WorldProvider wp = dt.createDimension();
            WorldUtil.dimNames.put(id, WorldUtil.getDimensionName(wp));
            return true;
        }

        // Not to be initialised - still add to the registered planets list (for
        // hotloading later?)
        WorldUtil.registeredPlanets.add(id);
        return true;
    }

    public static void unregisterPlanets()
    {
        if (WorldUtil.registeredPlanets != null)
        {
            for (Integer var1 : WorldUtil.registeredPlanets)
            {
                DimensionManager.unregisterDimension(var1);
                GalacticraftCore.logger.info("Unregistered Dimension: " + var1);
            }

            WorldUtil.registeredPlanets = null;
        }
        WorldUtil.dimNames.clear();
    }

    public static void registerPlanetClient(Integer dimID, int providerIndex)
    {
        int typeID = GalacticraftRegistry.getDimensionTypeID(providerIndex);

        if (typeID == 0)
        {
            GalacticraftCore.logger.error("Server dimension " + dimID + " has no match on client due to earlier registration problem.");
        } else if (dimID == 0)
        {
            GalacticraftCore.logger.error("Client dimension " + providerIndex + " has no match on server - probably a server dimension ID conflict problem.");
        } else if (!WorldUtil.registeredPlanets.contains(dimID))
        {
            WorldUtil.registeredPlanets.add(dimID);
            DimensionManager.registerDimension(dimID, WorldUtil.getDimensionTypeById(typeID));
        } else
        {
            GalacticraftCore.logger.error("Dimension already registered to another mod: unable to register planet dimension " + dimID);
        }
    }

    public static SpaceStationWorldData bindSpaceStationToNewDimension(World world, EntityPlayerMP player, int homePlanetID)
    {
        int dynamicProviderID = -1;
        int staticProviderID = -1;
        for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
        {
            if (satellite.getParentPlanet().getDimensionID() == homePlanetID)
            {
                dynamicProviderID = satellite.getDimensionID();
                staticProviderID = satellite.getDimensionIdStatic();
            }
        }
        if (dynamicProviderID == -1 || staticProviderID == -1)
        {
            throw new RuntimeException("Space station being bound on bad provider IDs!");
        }
        int newID = DimensionManager.getNextFreeDimId();
        SpaceStationWorldData data = WorldUtil.createSpaceStation(world, newID, homePlanetID, dynamicProviderID, staticProviderID, player);
        dimNames.put(newID, "Space Station " + newID);
        GCPlayerStats stats = GCPlayerStats.get(player);
        stats.getSpaceStationDimensionData().put(homePlanetID, newID);
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, GCCoreUtil.getDimensionID(player.world), new Object[]
            {WorldUtil.spaceStationDataToString(stats.getSpaceStationDimensionData())}), player);
        return data;
    }

    public static SpaceStationWorldData createSpaceStation(World world, int dimID, int homePlanetID, int dynamicProviderID, int staticProviderID, EntityPlayerMP player)
    {
        if (!DimensionManager.isDimensionRegistered(dimID))
        {
            if (ConfigManagerCore.keepLoadedNewSpaceStations)
            {
                ConfigManagerCore.setLoaded(dimID);
            }

            int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, dimID);

            if (id >= 0)
            {
                DimensionManager.registerDimension(dimID, WorldUtil.getDimensionTypeById(staticProviderID));
                WorldUtil.registeredSpaceStations.put(dimID, staticProviderID);
            } else
            {
                DimensionManager.registerDimension(dimID, WorldUtil.getDimensionTypeById(dynamicProviderID));
                WorldUtil.registeredSpaceStations.put(dimID, dynamicProviderID);
            }
        } else
        {
            GalacticraftCore.logger.error("Dimension already registered to another mod: unable to register space station dimension " + dimID);
        }

        for (WorldServer server : world.getMinecraftServer().worlds)
        {
            GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_LIST, GCCoreUtil.getDimensionID(server), WorldUtil.getSpaceStationList()),
                GCCoreUtil.getDimensionID(server));
        }
        return SpaceStationWorldData.getStationData(world, dimID, homePlanetID, dynamicProviderID, staticProviderID, player);
    }

    public static Entity transferEntityToDimension(Entity entity, int dimensionID, WorldServer world)
    {
        return WorldUtil.transferEntityToDimension(entity, dimensionID, world, true, null);
    }

    /**
     * It is not necessary to use entity.setDead() following calling this
     * method. If the entity left the old world it was in, it will now
     * automatically be removed from that old world before the next update tick.
     * (See WorldUtil.removeEntityFromWorld())
     */
    public static Entity transferEntityToDimension(Entity entity, int dimensionID, WorldServer world, boolean transferInv, EntityAutoRocket ridingRocket)
    {
        if (!world.isRemote)
        {
            MinecraftServer mcServer = world.getMinecraftServer();

            if (mcServer != null)
            {
                final WorldServer var6 = mcServer.getWorld(dimensionID);

                if (var6 == null)
                {
                    GalacticraftCore.logger.error("Cannot Transfer Entity to Dimension: Could not get World for Dimension " + dimensionID);
                    return null;
                }

                final ITeleportType type = GalacticraftRegistry.getTeleportTypeForDimension(var6.provider.getClass());

                if (type != null)
                {
                    return WorldUtil.teleportEntity(var6, entity, dimensionID, type, transferInv, ridingRocket);
                }
            }
        }

        return null;
    }

    private static Entity teleportEntity(World worldNew, Entity entity, int dimID, ITeleportType type, boolean transferInv, EntityAutoRocket ridingRocket)
    {
        Entity otherRiddenEntity = null;
        if (entity.getRidingEntity() != null)
        {
            if (entity.getRidingEntity() instanceof EntitySpaceshipBase)
            {
                entity.startRiding(entity.getRidingEntity());
            } else if (entity.getRidingEntity() instanceof EntityCelestialFake)
            {
                Entity e = entity.getRidingEntity();
                e.removePassengers();
                e.setDead();
            } else
            {
                otherRiddenEntity = entity.getRidingEntity();
                entity.dismountRidingEntity();
            }
        }

        boolean dimChange = entity.world != worldNew;
        // Make sure the entity is added to the correct chunk in the OLD world
        // so that it will be properly removed later if it needs to be unloaded
        // from that world
        entity.world.updateEntityWithOptionalForce(entity, false);
        EntityPlayerMP player = null;
        Vector3 spawnPos = null;
        int oldDimID = GCCoreUtil.getDimensionID(entity.world);

        if (ridingRocket != null)
        {
            NBTTagCompound nbt = new NBTTagCompound();
            ridingRocket.isDead = false;
            ridingRocket.removePassengers();
            ridingRocket.writeToNBTOptional(nbt);

            ((WorldServer) ridingRocket.world).getEntityTracker().untrack(ridingRocket);
            removeEntityFromWorld(ridingRocket.world, ridingRocket, true);

            ridingRocket = (EntityAutoRocket) EntityList.createEntityFromNBT(nbt, worldNew);

            if (ridingRocket != null)
            {
                ridingRocket.setWaitForPlayer(true);

                if (ridingRocket instanceof IWorldTransferCallback)
                {
                    ((IWorldTransferCallback) ridingRocket).onWorldTransferred(worldNew);
                }
            }
        }

        if (dimChange)
        {
            if (entity instanceof EntityPlayerMP)
            {
                player = (EntityPlayerMP) entity;
                World worldOld = player.world;

                GCPlayerStats stats = GCPlayerStats.get(player);
                stats.setUsingPlanetSelectionGui(false);

                player.dimension = dimID;
                if (ConfigManagerCore.enableDebug)
                {
                    GalacticraftCore.logger.info("DEBUG: Sending respawn packet to player for dim " + dimID);
                }
                player.connection.sendPacket(new SPacketRespawn(dimID, worldNew.getDifficulty(), worldNew.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));
                player.server.getPlayerList().updatePermissionLevel(player);
                if (worldNew.provider instanceof WorldProviderSpaceStation)
                {
                    if (WorldUtil.registeredSpaceStations.containsKey(dimID))
                        // TODO This has never been effective before due to the
                        // earlier bug - what does it actually do?
                    {
                        NBTTagCompound var2 = new NBTTagCompound();
                        SpaceStationWorldData.getStationData(worldNew, dimID, player).writeToNBT(var2);
                        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_DATA, GCCoreUtil.getDimensionID(player.world), new Object[]
                            {dimID, var2}), player);
                    }
                }

                removeEntityFromWorld(worldOld, player, true);

                if (ridingRocket != null)
                {
                    spawnPos = new Vector3(ridingRocket);
                } else
                {
                    spawnPos = type.getPlayerSpawnLocation((WorldServer) worldNew, player);
                }
                forceMoveEntityToPos(entity, (WorldServer) worldNew, spawnPos, true);

                GalacticraftCore.logger.info("Server attempting to transfer player " + PlayerUtil.getName(player) + " to dimension " + GCCoreUtil.getDimensionID(worldNew));
                if (worldNew.provider instanceof WorldProviderSpaceStation)
                {
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, GCCoreUtil.getDimensionID(worldNew), new Object[]
                        {}), player);
                }
                player.capabilities.isFlying = false;

                player.server.getPlayerList().preparePlayer(player, (WorldServer) worldOld);
                player.interactionManager.setWorld((WorldServer) worldNew);
                player.connection.sendPacket(new SPacketPlayerAbilities(player.capabilities));
                player.server.getPlayerList().updateTimeAndWeatherForPlayer(player, (WorldServer) worldNew);
                player.server.getPlayerList().syncPlayerInventory(player);

                for (Object o : player.getActivePotionEffects())
                {
                    PotionEffect var10 = (PotionEffect) o;
                    player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), var10));
                }

                player.connection.sendPacket(new SPacketSetExperience(player.experience, player.experienceTotal, player.experienceLevel));
            } else
                // Non-player entity transfer i.e. it's an EntityCargoRocket or an
                // empty rocket
            {
                ArrayList<TileEntityTelemetry> tList = null;
                if (entity instanceof EntitySpaceshipBase)
                {
                    tList = ((EntitySpaceshipBase) entity).getTelemetry();
                }
                WorldUtil.removeEntityFromWorld(entity.world, entity, true);

                NBTTagCompound nbt = new NBTTagCompound();
                entity.isDead = false;
                entity.writeToNBTOptional(nbt);
                entity = EntityList.createEntityFromNBT(nbt, worldNew);

                if (entity == null)
                {
                    return null;
                }

                if (entity instanceof IWorldTransferCallback)
                {
                    ((IWorldTransferCallback) entity).onWorldTransferred(worldNew);
                }

                forceMoveEntityToPos(entity, (WorldServer) worldNew, new Vector3(entity), true);

                if (tList != null && tList.size() > 0)
                {
                    for (TileEntityTelemetry t : tList)
                    {
                        t.addTrackedEntity(entity);
                    }
                }
            }
        } else // Same dimension player transfer
            if (entity instanceof EntityPlayerMP)
            {
                player = (EntityPlayerMP) entity;
                player.closeScreen();
                GCPlayerStats stats = GCPlayerStats.get(player);
                stats.setUsingPlanetSelectionGui(false);

                if (ridingRocket != null)
                {
                    spawnPos = new Vector3(ridingRocket);
                } else
                {
                    spawnPos = type.getPlayerSpawnLocation((WorldServer) entity.world, (EntityPlayerMP) entity);
                }
                forceMoveEntityToPos(entity, (WorldServer) worldNew, spawnPos, false);

                GalacticraftCore.logger.info("Server attempting to transfer player " + PlayerUtil.getName(player) + " within same dimension " + GCCoreUtil.getDimensionID(worldNew));
                if (worldNew.provider instanceof WorldProviderSpaceStation)
                {
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, GCCoreUtil.getDimensionID(worldNew), new Object[]
                        {}), player);
                }
                player.capabilities.isFlying = false;
            }

        // Update PlayerStatsGC
        if (player != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            if (ridingRocket == null && type.useParachute() && stats.getExtendedInventory().getStackInSlot(4) != null
                && stats.getExtendedInventory().getStackInSlot(4).getItem() instanceof ItemParaChute)
            {
                GCPlayerHandler.setUsingParachute(player, stats, true);
            } else
            {
                GCPlayerHandler.setUsingParachute(player, stats, false);
            }

            if (stats.getRocketStacks() != null && !stats.getRocketStacks().isEmpty())
            {
                for (int stack = 0; stack < stats.getRocketStacks().size(); stack++)
                {
                    if (transferInv)
                    {
                        if (stats.getRocketStacks().get(stack).isEmpty())
                        {
                            if (stack == stats.getRocketStacks().size() - 1)
                            {
                                if (stats.getRocketItem() != null)
                                {
                                    stats.getRocketStacks().set(stack, new ItemStack(stats.getRocketItem(), 1, stats.getRocketType()));
                                }
                            } else if (stack == stats.getRocketStacks().size() - 2)
                            {
                                ItemStack launchpad = stats.getLaunchpadStack();
                                stats.getRocketStacks().set(stack, launchpad == null ? ItemStack.EMPTY : launchpad);
                                stats.setLaunchpadStack(null);
                            }
                        }
                    } else
                    {
                        stats.getRocketStacks().set(stack, ItemStack.EMPTY);
                    }
                }
            }

            if (transferInv && stats.getChestSpawnCooldown() == 0)
            {
                stats.setChestSpawnVector(type.getParaChestSpawnLocation((WorldServer) entity.world, player, new Random()));
                stats.setChestSpawnCooldown(200);
            }
        }

        if (ridingRocket != null)
        {
            boolean previous = CompatibilityManager.forceLoadChunks((WorldServer) worldNew);
            ridingRocket.forceSpawn = true;
            worldNew.spawnEntity(ridingRocket);
            ridingRocket.setWorld(worldNew);
            worldNew.updateEntityWithOptionalForce(ridingRocket, true);
            CompatibilityManager.forceLoadChunksEnd((WorldServer) worldNew, previous);
            entity.startRiding(ridingRocket);
            GalacticraftCore.logger.debug("Entering rocket at : " + entity.posX + "," + entity.posZ + " rocket at: " + ridingRocket.posX + "," + ridingRocket.posZ);
        } else if (otherRiddenEntity != null)
        {
            if (dimChange)
            {
                World worldOld = otherRiddenEntity.world;
                NBTTagCompound nbt = new NBTTagCompound();
                otherRiddenEntity.writeToNBTOptional(nbt);
                removeEntityFromWorld(worldOld, otherRiddenEntity, true);
                otherRiddenEntity = EntityList.createEntityFromNBT(nbt, worldNew);
                worldNew.spawnEntity(otherRiddenEntity);
                otherRiddenEntity.setWorld(worldNew);
            }
            otherRiddenEntity.setPositionAndRotation(entity.posX, entity.posY - 10, entity.posZ, otherRiddenEntity.rotationYaw, otherRiddenEntity.rotationPitch);
            worldNew.updateEntityWithOptionalForce(otherRiddenEntity, true);
        }

        if (entity instanceof EntityPlayerMP)
        {
            if (dimChange)
                FMLCommonHandler.instance().firePlayerChangedDimensionEvent((EntityPlayerMP) entity, oldDimID, dimID);

            // Spawn in a lander if appropriate
            type.onSpaceDimensionChanged(worldNew, (EntityPlayerMP) entity, ridingRocket != null);
        }

        return entity;
    }

    public static Entity teleportEntitySimple(World worldNew, int dimID, EntityPlayerMP player, Vector3 spawnPos)
    {
        if (player.getRidingEntity() != null)
        {
            player.getRidingEntity().setDead();
            player.dismountRidingEntity();
        }

        World worldOld = player.world;
        int oldDimID = GCCoreUtil.getDimensionID(worldOld);
        boolean dimChange = worldOld != worldNew;
        // Make sure the entity is added to the correct chunk in the OLD world
        // so that it will be properly removed later if it needs to be unloaded
        // from that world
        worldOld.updateEntityWithOptionalForce(player, false);

        if (dimChange)
        {
            player.dimension = dimID;
            if (ConfigManagerCore.enableDebug)
            {
                GalacticraftCore.logger.info("DEBUG: Sending respawn packet to player for dim " + dimID);
            }
            player.connection.sendPacket(new SPacketRespawn(dimID, player.world.getDifficulty(), player.world.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));
            if (worldNew.provider instanceof WorldProviderSpaceStation)
            {
                if (WorldUtil.registeredSpaceStations.containsKey(dimID))
                    // TODO This has never been effective before due to the earlier
                    // bug - what does it actually do?
                {
                    NBTTagCompound var2 = new NBTTagCompound();
                    SpaceStationWorldData.getStationData(worldNew, dimID, player).writeToNBT(var2);
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_DATA, GCCoreUtil.getDimensionID(player.world), new Object[]
                        {dimID, var2}), player);
                }
            }
            removeEntityFromWorld(worldOld, player, true);
            forceMoveEntityToPos(player, (WorldServer) worldNew, spawnPos, true);
            GalacticraftCore.logger.info("Server attempting to transfer player " + PlayerUtil.getName(player) + " to dimension " + GCCoreUtil.getDimensionID(worldNew));

            player.server.getPlayerList().preparePlayer(player, (WorldServer) worldOld);
            player.interactionManager.setWorld((WorldServer) worldNew);
            player.server.getPlayerList().updateTimeAndWeatherForPlayer(player, (WorldServer) worldNew);
            player.server.getPlayerList().syncPlayerInventory(player);

            for (Object o : player.getActivePotionEffects())
            {
                PotionEffect var10 = (PotionEffect) o;
                player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), var10));
            }

            player.connection.sendPacket(new SPacketSetExperience(player.experience, player.experienceTotal, player.experienceLevel));
            FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDimID, dimID);
        } else
        {
            forceMoveEntityToPos(player, (WorldServer) worldNew, spawnPos, false);
            GalacticraftCore.logger.info("Server attempting to transfer player " + PlayerUtil.getName(player) + " within same dimension " + GCCoreUtil.getDimensionID(worldNew));
        }
        player.capabilities.isFlying = false;
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, GCCoreUtil.getDimensionID(player.world), new Object[]
            {}), player);

        // Update PlayerStatsGC
        GCPlayerStats stats = GCPlayerStats.get(player);
        GCPlayerHandler.setUsingParachute(player, stats, false);

        return player;
    }

    /**
     * This correctly positions an entity at spawnPos in worldNew loading and
     * adding it to the chunk as required.
     *
     * @param entity
     * @param worldNew
     * @param spawnPos
     * @param spawnRequired - set this to true if the entity changed dimension
     *        so that a spawn in the new world is needed
     */
    public static void forceMoveEntityToPos(Entity entity, WorldServer worldNew, Vector3 spawnPos, boolean spawnRequired)
    {
        boolean previous = CompatibilityManager.forceLoadChunks(worldNew);
        ChunkPos pair = worldNew.getChunk(spawnPos.intX() >> 4, spawnPos.intZ() >> 4).getPos();
        GalacticraftCore.logger.debug("Loading first chunk in new dimension at " + pair.x + "," + pair.z);
        worldNew.getChunkProvider().loadChunk(pair.x, pair.z);
        entity.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
        WorldServer fromWorld = ((WorldServer) entity.world);
        if (spawnRequired)
        {
            ((WorldServer) entity.world).getEntityTracker().untrack(entity);
            entity.forceSpawn = true;
            worldNew.spawnEntity(entity);
            entity.setWorld(worldNew);
        }
        worldNew.updateEntityWithOptionalForce(entity, true);
        if (entity instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP) entity).connection.setPlayerLocation(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
        }
        CompatibilityManager.forceLoadChunksEnd(worldNew, previous);
    }

    public static WorldServer getStartWorld(WorldServer unchanged)
    {
        if (ConfigManagerCore.challengeSpawnHandling)
        {
            WorldProvider wp = WorldUtil.getProviderForNameServer("planet.asteroids");
            WorldServer worldNew = (wp == null) ? null : (WorldServer) wp.world;
            if (worldNew != null)
            {
                return worldNew;
            }
        }
        return unchanged;
    }

    @SideOnly(Side.CLIENT)
    public static EntityPlayer forceRespawnClient(int dimID, int par2, String par3, int par4)
    {
        SPacketRespawn fakePacket = new SPacketRespawn(dimID, EnumDifficulty.byId(par2), WorldType.byName(par3), WorldSettings.getGameTypeById(par4));
        FMLClientHandler.instance().getClient().player.connection.handleRespawn(fakePacket);
        return FMLClientHandler.instance().getClientPlayerEntity();
    }

    /**
     * This is similar to World.removeEntityDangerously() but without the risk
     * of concurrent modification error
     */
    private static void removeEntityFromWorld(World var0, Entity var1, boolean directlyRemove)
    {
        if (var1 instanceof EntityPlayer)
        {
            final EntityPlayer var2 = (EntityPlayer) var1;
            var2.closeScreen();
            var0.playerEntities.remove(var2);
            var0.updateAllPlayersSleepingFlag();
        }

        int i = var1.chunkCoordX;
        int j = var1.chunkCoordZ;

        if (var1.addedToChunk && var0.isBlockLoaded(new BlockPos(i << 4, 63, j << 4), true))
        {
            var0.getChunk(i, j).removeEntity(var1);
        }

        if (directlyRemove)
        {
            List<Entity> l = new ArrayList<>();
            l.add(var1);
            var0.unloadEntities(l);
            // This will automatically remove the entity from the world and the
            // chunk prior to the world's next update entities tick
            // It is important NOT to directly modify World.loadedEntityList
            // here, as the World will be currently iterating through that list
            // when updating each entity (see the line
            // "this.loadedEntityList.remove(i--);" in World.updateEntities()
        }

        var1.isDead = false;
    }

    public static SpaceStationRecipe getSpaceStationRecipe(int planetID)
    {
        for (SpaceStationType type : GalacticraftRegistry.getSpaceStationData())
        {
            if (type.getWorldToOrbitID() == planetID)
            {
                return type.getRecipeForSpaceStation();
            }
        }

        return null;
    }

    /**
     * This must return planets in the same order their provider IDs were
     * registered in GalacticraftRegistry by GalacticraftCore.
     */
    public static List<Object> getPlanetList()
    {
        List<Object> objList = new ArrayList<>();
        objList.add(getPlanetListInts());
        return objList;
    }

    public static Integer[] getPlanetListInts()
    {
        Integer[] iArray = new Integer[WorldUtil.registeredPlanets.size()];

        for (int i = 0; i < iArray.length; i++)
        {
            iArray[i] = WorldUtil.registeredPlanets.get(i);
        }

        return iArray;
    }

    /**
     * What's important here is that Galacticraft and the server both register
     * the same reachable Galacticraft planets (and their provider types) in the
     * same order. See WorldUtil.registerPlanet().
     *
     * Even if there are dimension conflicts or other problems, the planets must
     * be registered in the same order on both client and server. This should
     * happen automatically if Galacticraft versions match, and if planets
     * modules match (including Galacticraft-Planets and any other sub-mods).
     *
     * It is NOT a good idea for sub-mods to make the registration order of
     * planets variable or dependent on configs.
     */
    public static void decodePlanetsListClient(List<Object> data)
    {
        try
        {
            if (ConfigManagerCore.enableDebug)
            {
                GalacticraftCore.logger.info("GC connecting to server: received planets dimension ID list.");
            }
            if (WorldUtil.registeredPlanets != null)
            {
                for (Integer registeredID : WorldUtil.registeredPlanets)
                {
                    if (DimensionManager.isDimensionRegistered(registeredID))
                    {
                        DimensionManager.unregisterDimension(registeredID);
                    }
                }
            }
            WorldUtil.registeredPlanets = new ArrayList<>();

            String ids = "";
            if (data.size() > 0)
            {
                // Start the provider index at offset 2 to skip the two
                // Overworld Orbit dimensions
                // (this will be iterating through
                // GalacticraftRegistry.worldProviderIDs)
                int providerIndex = GalaxyRegistry.getRegisteredSatellites().size() * 2;
                if (data.get(0) instanceof Integer)
                {
                    for (Object o : data)
                    {
                        WorldUtil.registerPlanetClient((Integer) o, providerIndex);
                        providerIndex++;
                        ids += ((Integer) o).toString() + " ";
                    }
                } else if (data.get(0) instanceof Integer[])
                {
                    for (Object o : (Integer[]) data.get(0))
                    {
                        WorldUtil.registerPlanetClient((Integer) o, providerIndex);
                        providerIndex++;
                        ids += ((Integer) o).toString() + " ";
                    }
                }
            }
            if (ConfigManagerCore.enableDebug)
            {
                GalacticraftCore.logger.debug("GC clientside planet dimensions registered: " + ids);
                WorldProvider dimMoon = WorldUtil.getProviderForNameClient("moon.moon");
                if (dimMoon != null)
                {
                    GalacticraftCore.logger.debug("Crosscheck: Moon is " + GCCoreUtil.getDimensionID(dimMoon));
                }
                WorldProvider dimMars = WorldUtil.getProviderForNameClient("planet.mars");
                if (dimMars != null)
                {
                    GalacticraftCore.logger.debug("Crosscheck: Mars is " + GCCoreUtil.getDimensionID(dimMars));
                }
                WorldProvider dimAst = WorldUtil.getProviderForNameClient("planet.asteroids");
                if (dimAst != null)
                {
                    GalacticraftCore.logger.debug("Crosscheck: Asteroids is " + GCCoreUtil.getDimensionID(dimAst));
                }
            }
        } catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    public static List<Object> getSpaceStationList()
    {
        List<Object> objList = new ArrayList<>();
        objList.add(getSpaceStationListInts());
        return objList;
    }

    public static Integer[] getSpaceStationListInts()
    {
        Integer[] iArray = new Integer[WorldUtil.registeredSpaceStations.size() * 2];

        int i = 0;
        for (Map.Entry<Integer, Integer> e : WorldUtil.registeredSpaceStations.entrySet())
        {
            iArray[i] = e.getKey();
            iArray[i + 1] = e.getValue();
            i += 2;
        }

        //        for (int i = 0; i < iArray.length; i++)
        //        {
        //            iArray[i] = WorldUtil.registeredSpaceStations.get(i);
        //        }

        return iArray;
    }

    public static void decodeSpaceStationListClient(List<Object> data)
    {
        try
        {
            if (WorldUtil.registeredSpaceStations != null)
            {
                for (Integer registeredID : WorldUtil.registeredSpaceStations.keySet())
                {
                    DimensionManager.unregisterDimension(registeredID);
                }
            }
            WorldUtil.registeredSpaceStations = Maps.newHashMap();

            if (data.size() > 0)
            {
                if (data.get(0) instanceof Integer)
                {
                    for (int i = 0; i < data.size(); i += 2)
                    {
                        registerSSdim((Integer) data.get(i), (Integer) data.get(i + 1));
                    }
                    //                    for (Object dimID : data)
                    //                    {
                    //                        registerSSdim((Integer) dimID);
                    //                    }
                } else if (data.get(0) instanceof Integer[])
                {
                    Integer[] array = ((Integer[]) data.get(0));
                    for (int i = 0; i < array.length; i += 2)
                    {
                        registerSSdim(array[i], array[i + 1]);
                    }
                    //                    for (Object dimID : (Integer[]) data.get(0))
                    //                    {
                    //                        registerSSdim((Integer) dimID);
                    //                    }
                }
            }
        } catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void registerSSdim(Integer dimID, Integer providerKey)
    {
        if (!WorldUtil.registeredSpaceStations.containsKey(dimID))
        {
            if (!DimensionManager.isDimensionRegistered(dimID))
            {
                WorldUtil.registeredSpaceStations.put(dimID, providerKey);
                DimensionManager.registerDimension(dimID, WorldUtil.getDimensionTypeById(providerKey));
            } else
            {
                GalacticraftCore.logger.error("Dimension already registered on client: unable to register space station dimension " + dimID);
            }
        }
    }

    public static void toCelestialSelection(EntityPlayerMP player, GCPlayerStats stats, int tier)
    {
        player.dismountRidingEntity();
        stats.setSpaceshipTier(tier);

        HashMap<String, Integer> map = WorldUtil.getArrayOfPossibleDimensions(tier, player);
        String dimensionList = "";
        int count = 0;
        for (Entry<String, Integer> entry : map.entrySet())
        {
            dimensionList = dimensionList.concat(entry.getKey() + (count < map.entrySet().size() - 1 ? "?" : ""));
            count++;
        }

        boolean canCreateStations = PermissionAPI.hasPermission(player, Constants.PERMISSION_CREATE_STATION);
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_DIMENSION_LIST, GCCoreUtil.getDimensionID(player.world), new Object[]
            {PlayerUtil.getName(player), dimensionList, canCreateStations}), player);
        stats.setUsingPlanetSelectionGui(true);
        stats.setSavedPlanetList(dimensionList);
        Entity fakeEntity = new EntityCelestialFake(player.world, player.posX, player.posY, player.posZ);
        player.world.spawnEntity(fakeEntity);
        player.startRiding(fakeEntity);
    }

    public static Vector3 getFootprintPosition(World world, float rotation, Vector3 startPosition, BlockVec3 playerCenter)
    {
        Vector3 position = startPosition.clone();
        float footprintScale = 0.375F;

        int mainPosX = position.intX();
        int mainPosY = position.intY();
        int mainPosZ = position.intZ();
        BlockPos posMain = new BlockPos(mainPosX, mainPosY, mainPosZ);

        // If the footprint is hovering over air...
        if (world.getBlockState(posMain).getBlock().isAir(world.getBlockState(posMain), world, posMain))
        {
            position.x += (playerCenter.x - mainPosX);
            position.z += (playerCenter.z - mainPosZ);

            BlockPos pos1 = new BlockPos(position.intX(), position.intY(), position.intZ());
            // If the footprint is still over air....
            Block b2 = world.getBlockState(pos1).getBlock();
            if (b2 != null && b2.isAir(world.getBlockState(pos1), world, pos1))
            {
                for (EnumFacing direction : EnumFacing.VALUES)
                {
                    BlockPos offsetPos = posMain.offset(direction);
                    if (direction != EnumFacing.DOWN && direction != EnumFacing.UP)
                    {
                        if (!world.getBlockState(offsetPos).getBlock().isAir(world.getBlockState(offsetPos), world, offsetPos))
                        {
                            position.x += direction.getXOffset();
                            position.z += direction.getZOffset();
                            break;
                        }
                    }
                }
            }
        }

        mainPosX = position.intX();
        mainPosZ = position.intZ();

        double x0 = (Math.sin((45 - rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale) + position.x;
        double x1 = (Math.sin((135 - rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale) + position.x;
        double x2 = (Math.sin((225 - rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale) + position.x;
        double x3 = (Math.sin((315 - rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale) + position.x;
        double z0 = (Math.cos((45 - rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale) + position.z;
        double z1 = (Math.cos((135 - rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale) + position.z;
        double z2 = (Math.cos((225 - rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale) + position.z;
        double z3 = (Math.cos((315 - rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale) + position.z;

        double xMin = Math.min(Math.min(x0, x1), Math.min(x2, x3));
        double xMax = Math.max(Math.max(x0, x1), Math.max(x2, x3));
        double zMin = Math.min(Math.min(z0, z1), Math.min(z2, z3));
        double zMax = Math.max(Math.max(z0, z1), Math.max(z2, z3));

        if (xMin < mainPosX)
        {
            position.x += mainPosX - xMin;
        }

        if (xMax > mainPosX + 1)
        {
            position.x -= xMax - (mainPosX + 1);
        }

        if (zMin < mainPosZ)
        {
            position.z += mainPosZ - zMin;
        }

        if (zMax > mainPosZ + 1)
        {
            position.z -= zMax - (mainPosZ + 1);
        }

        return position;
    }

    public static String spaceStationDataToString(HashMap<Integer, Integer> data)
    {
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<Integer, Integer>> it = data.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<Integer, Integer> e = it.next();
            builder.append(e.getKey());
            builder.append("$");
            builder.append(e.getValue());
            if (it.hasNext())
            {
                builder.append("?");
            }
        }
        return builder.toString();
    }

    public static HashMap<Integer, Integer> stringToSpaceStationData(String input)
    {
        HashMap<Integer, Integer> data = Maps.newHashMap();
        if (!input.isEmpty())
        {
            String[] str0 = input.split("\\?");
            for (String element : str0)
            {
                String[] str1 = element.split("\\$");
                data.put(Integer.parseInt(str1[0]), Integer.parseInt(str1[1]));
            }
        }
        return data;
    }

    public static String getDimensionName(WorldProvider wp)
    {
        if (wp instanceof IGalacticraftWorldProvider)
        {
            CelestialBody cb = ((IGalacticraftWorldProvider) wp).getCelestialBody();
            if (cb != null && !(cb instanceof Satellite))
            {
                return cb.getTranslationKey();
            }
        }

        if (GCCoreUtil.getDimensionID(wp) == ConfigManagerCore.idDimensionOverworld)
        {
            return "overworld";
        }

        return wp.getDimensionType().getName();
    }

    private static void insertChecklistEntries(CelestialBody body, List<CelestialBody> bodiesDone, List<List<String>> checklistValues)
    {
        if (body.isReachable())
        {
            int insertPos = 0;
            for (CelestialBody prevBody : bodiesDone)
            {
                if (body.getTierRequirement() >= prevBody.getTierRequirement())
                {
                    insertPos++;
                }
            }
            List<String> checklist = Lists.newArrayList();
            checklist.add(body.getTranslationKey());
            checklist.addAll(body.getChecklistKeys());
            checklistValues.add(insertPos, checklist);
            bodiesDone.add(body);
        }
    }

    public static List<List<String>> getAllChecklistKeys()
    {
        List<List<String>> checklistValues = Lists.newArrayList();
        List<CelestialBody> bodiesDone = Lists.newArrayList();

        for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
        {
            insertChecklistEntries(planet, bodiesDone, checklistValues);
        }

        for (Moon moon : GalaxyRegistry.getRegisteredMoons().values())
        {
            insertChecklistEntries(moon, bodiesDone, checklistValues);
        }

        for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
        {
            insertChecklistEntries(satellite, bodiesDone, checklistValues);
        }

        return checklistValues;
    }

    public static DimensionType getDimensionTypeById(int id)
    {
        for (DimensionType dimensiontype : DimensionType.values())
        {
            if (dimensiontype.getId() == id)
            {
                return dimensiontype;
            }
        }

        GalacticraftCore.logger.error("There was a problem getting WorldProvider type " + id);
        GalacticraftCore.logger.error("(possibly this is a conflict, check Galacticraft config.)");
        return null;
    }

    public static void markAdjacentPadForUpdate(World worldIn, BlockPos pos)
    {
        BlockPos offsetPos;
        for (int dX = -2; dX <= 2; dX++)
        {
            for (int dZ = -2; dZ <= 2; dZ++)
            {
                offsetPos = pos.add(dX, 0, dZ);
                final IBlockState blockState = worldIn.getBlockState(offsetPos);

                if (blockState.getBlock() == GCBlocks.landingPadFull)
                {
                    worldIn.notifyBlockUpdate(offsetPos, blockState, blockState, 3);
                }
            }
        }
    }

    public static void setNextMorning(WorldServer world)
    {
        if (world.provider instanceof WorldProviderSpace)
        {
            long current = ((WorldProviderSpace) world.provider).preTickTime;
            long dayLength = ((WorldProviderSpace) world.provider).getDayLength();
            if (dayLength <= 0)
                return;
            world.setWorldTime(current - current % dayLength + dayLength);
        } else
        {
            long newTime = world.getWorldTime();
            for (WorldServer worldServer : GCCoreUtil.getWorldServerList(world))
            {
                if (worldServer == world)
                    continue;
                if (worldServer.provider instanceof WorldProviderSpace)
                {
                    ((WorldProviderSpace) worldServer.provider).adjustTime(newTime);
                }
            }
        }
    }
}
