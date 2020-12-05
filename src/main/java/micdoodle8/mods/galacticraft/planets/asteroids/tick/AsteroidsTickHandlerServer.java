package micdoodle8.mods.galacticraft.planets.asteroids.tick;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.ShortRangeTelepadHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.Dimension;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class AsteroidsTickHandlerServer
{
    public static ShortRangeTelepadHandler spaceRaceData = null;
    public static List<EntityAstroMiner> activeMiners = new ArrayList<>();
    public static AtomicBoolean loadingSavedChunks = new AtomicBoolean();
    private static Field droppedChunks = null;

    public static void restart()
    {
        spaceRaceData = null;
        activeMiners.clear();
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        MinecraftServer server = GCCoreUtil.getServer();
        //Prevent issues when clients switch to LAN servers
        if (server == null)
        {
            return;
        }

        if (event.phase == TickEvent.Phase.START)
        {
            TileEntityMinerBase.checkNewMinerBases();

            if (AsteroidsTickHandlerServer.spaceRaceData == null)
            {
                Level world = server.getLevel(DimensionType.OVERWORLD);
                AsteroidsTickHandlerServer.spaceRaceData = ((ServerLevel) world).getDataStorage().computeIfAbsent(() -> new ShortRangeTelepadHandler(ShortRangeTelepadHandler.saveDataID), ShortRangeTelepadHandler.saveDataID);

                if (AsteroidsTickHandlerServer.spaceRaceData == null)
                {
                    AsteroidsTickHandlerServer.spaceRaceData = new ShortRangeTelepadHandler(ShortRangeTelepadHandler.saveDataID);
                    ((ServerLevel) world).getDataStorage().set(AsteroidsTickHandlerServer.spaceRaceData);
                }
            }

            int index = -1;
            for (EntityAstroMiner miner : activeMiners)
            {
                index++;
                if (!miner.isAlive())
                {
//                    minerIt.remove();  Don't remove it, we want the index number to be static for the others
                    continue;
                }

                if (miner.playerMP != null)
                {
                    GCPlayerStats stats = GCPlayerStats.get(miner.playerMP);
                    if (stats != null)
                    {
                        List<BlockVec3> list = stats.getActiveAstroMinerChunks();
                        boolean inListAlready = false;
                        Iterator<BlockVec3> it = list.iterator();
                        while (it.hasNext())
                        {
                            BlockVec3 data = it.next();
                            if (data.sideDoneBits == index)  //SideDoneBits won't be saved to NBT, but during an active server session we can use it as a cross-reference to the index here - it's a 4th data int hidden inside a BlockVec3
                            {
                                if (!miner.isAlive())
                                {
                                    it.remove();  //Player stats should not save position of dead AstroMiner entity (probably broken by player deliberately breaking it)
                                }
                                else
                                {
                                    data.x = miner.xChunk;
                                    data.z = miner.zChunk;
                                }
                                inListAlready = true;
                                break;
                            }
                        }
                        if (!inListAlready)
                        {
                            BlockVec3 data = new BlockVec3(miner.xChunk, miner.dimension.getId(), miner.zChunk);
                            data.sideDoneBits = index;
                            list.add(data);
                        }
                    }
                }

            }
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            for (EntityAstroMiner miner : activeMiners)
            {
                if (miner.playerMP != null && miner.level == event.world && miner.isAlive())
                {
                    miner.serverTick = true;
                    miner.serverTickSave = miner.tickCount;
                }
            }
        }
        else if (event.phase == TickEvent.Phase.END)
        {
            for (EntityAstroMiner miner : activeMiners)
            {
                if (!miner.isAlive())
                {
//                    minerIt.remove();  Don't remove it, we want the index number to be static for the others
                    continue;
                }
                if (miner.playerMP != null && miner.level == event.world)
                {
                    if (miner.serverTick)
                    {
                        //Force an entity update tick, if it didn't happen already (mainly needed on Sponge servers - entities not super close to players seem to be not updated at all on Sponge even if the chunk is active, see issue #3307)
                        miner.tickCount = miner.serverTickSave + 1;
                        miner.tick();
                    }

                    try
                    {
                        if (droppedChunks == null)
                        {
                            Class clazz = ((ServerLevel) miner.level).getChunkSource().getClass();
                            droppedChunks = clazz.getDeclaredField(GCCoreUtil.isDeobfuscated() ? "droppedChunksSet" : "field_73248_b");
                            droppedChunks.setAccessible(true);
                        }
                        Set<Long> undrop = (Set<Long>) droppedChunks.get(((ServerLevel) miner.level).getChunkSource());
                        undrop.remove(ChunkPos.asLong(miner.xChunk, miner.zChunk));
                    }
                    catch (Exception ignore)
                    {
                    }
                }
            }
        }
    }

    public static void removeChunkData(GCPlayerStats stats, EntityAstroMiner entityToRemove)
    {
        int index = 0;
        for (EntityAstroMiner miner : activeMiners)
        {
            if (miner == entityToRemove)  //Found it in the list here
            {
                List<BlockVec3> list = stats.getActiveAstroMinerChunks();
                Iterator<BlockVec3> it = list.iterator();
                while (it.hasNext())
                {
                    BlockVec3 data = it.next();
                    if (data.sideDoneBits == index)  //Found it in the player's stats
                    {
                        it.remove();
                        return;
                    }
                }
                return;
            }
            index++;
        }
    }

    /**
     * How this works: every spawned or saved (in player stats) miner is added to the
     * activeMiners list here.
     * Once per server tick its position will be saved to player stats.
     * When the player quits, the saved miner positions will be saved with the player's stats NBT
     * When the player next loads, loadAstroChunkList will force load those chunks, therefore
     * reactivating AstroMiners if those chunks weren't already loaded.
     */
    public static void loadAstroChunkList(List<BlockVec3> activeChunks)
    {
        List<BlockVec3> copyList = new LinkedList<>(activeChunks);
        activeChunks.clear();
        if (!(AsteroidsTickHandlerServer.loadingSavedChunks.getAndSet(true)))
        {
            for (BlockVec3 data : copyList)
            {
                Dimension p = WorldUtil.getProviderForDimensionServer(DimensionType.getById(data.y));
                if (p != null && p.getWorld() != null)
                {
                    GCLog.debug("Loading chunk " + data.y + ": " + data.x + "," + data.z + " - should contain a miner!");
                    ServerLevel w = (ServerLevel) p.getWorld();
                    boolean previous = CompatibilityManager.forceLoadChunks(w);
                    w.getChunkSource().getChunk(data.x, data.z, true);
                    CompatibilityManager.forceLoadChunksEnd(w, previous);
                }
            }
            AsteroidsTickHandlerServer.loadingSavedChunks.set(false);
        }
    }

    public static int monitorMiner(EntityAstroMiner entityAstroMiner)
    {
        int result = activeMiners.size();
        activeMiners.add(entityAstroMiner);
        GCLog.debug("Monitoring miner " + result);
        return result;
    }

}
