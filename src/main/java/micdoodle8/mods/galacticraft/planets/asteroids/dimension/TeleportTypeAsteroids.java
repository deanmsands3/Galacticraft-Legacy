package micdoodle8.mods.galacticraft.planets.asteroids.dimension;

import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.BlockAsteroidRock;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityEntryPod;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkSource;
import java.util.Random;

public class TeleportTypeAsteroids implements ITeleportType
{
    @Override
    public boolean useParachute()
    {
        return false;
    }

    @Override
    public Vector3D getPlayerSpawnLocation(ServerLevel world, ServerPlayer player)
    {
        if (player != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            int x = Mth.floor(stats.getCoordsTeleportedFromX());
            int z = Mth.floor(stats.getCoordsTeleportedFromZ());
            int limit = ConfigManagerCore.otherPlanetWorldBorders.get() - 2;
            if (limit > 20)
            {
                if (x > limit)
                {
                    z *= limit / x;
                    x = limit;
                }
                else if (x < -limit)
                {
                    z *= -limit / x;
                    x = -limit;
                }
                if (z > limit)
                {
                    x *= limit / z;
                    z = limit;
                }
                else if (z < -limit)
                {
                    x *= -limit / z;
                    z = -limit;
                }
            }

            int attemptCount = 0;

            //Small pre-generate with a chunk loading radius of 3, to make sure some asteroids get generated
            //(if the world is already generated here, this will be very quick)
            this.preGenChunks(world, x >> 4, z >> 4);

            do
            {
                BlockVec3 bv3 = null;
                if (world.getDimension() instanceof DimensionAsteroids)
                {
                    bv3 = ((DimensionAsteroids) world.getDimension()).getClosestAsteroidXZ(x, 0, z, true);
                }

                if (bv3 != null)
                {
                    //Check whether the returned asteroid is too far from the desired entry location in which case, give up
                    if (bv3.distanceSquared(new BlockVec3(x, 128, z)) > 25600)
                    {
                        break;
                    }

                    if (ConfigManagerCore.enableDebug.get())
                    {
                        GCLog.info("Testing asteroid at x" + (bv3.x) + " y" + (bv3.y) + " z" + bv3.z);
                    }
                    this.loadChunksAround(bv3.x, bv3.z, 2, world.getChunkSource());
                    this.loadChunksAround(bv3.x, bv3.z, -3, world.getChunkSource());

                    if (goodAsteroidEntry(world, bv3.x, bv3.y, bv3.z))
                    {
                        return new Vector3D(bv3.x, 310, bv3.z);
                    }
                    if (goodAsteroidEntry(world, bv3.x + 2, bv3.y, bv3.z + 2))
                    {
                        return new Vector3D(bv3.x + 2, 310, bv3.z + 2);
                    }
                    if (goodAsteroidEntry(world, bv3.x + 2, bv3.y, bv3.z - 2))
                    {
                        return new Vector3D(bv3.x + 2, 310, bv3.z - 2);
                    }
                    if (goodAsteroidEntry(world, bv3.x - 2, bv3.y, bv3.z - 2))
                    {
                        return new Vector3D(bv3.x - 2, 310, bv3.z - 2);
                    }
                    if (goodAsteroidEntry(world, bv3.x - 2, bv3.y, bv3.z + 2))
                    {
                        return new Vector3D(bv3.x - 2, 310, bv3.z + 2);
                    }

                    //Failed to find an asteroid even though there should be one there
                    if (ConfigManagerCore.enableDebug.get())
                    {
                        GCLog.info("Removing drilled out asteroid at x" + (bv3.x) + " z" + (bv3.z));
                    }
                    ((DimensionAsteroids) world.getDimension()).removeAsteroid(bv3.x, bv3.y, bv3.z);
                }

                attemptCount++;
            }
            while (attemptCount < 5);

            GCLog.info("Failed to find good large asteroid landing spot! Falling back to making a small one.");
            this.makeSmallLandingSpot(world, x, z);
            return new Vector3D(x, 310, z);
        }

        GCLog.severe("Null player when teleporting to Asteroids!");
        return new Vector3D(0, 310, 0);
    }

    private boolean goodAsteroidEntry(Level world, int x, int yorig, int z)
    {
        for (int k = 208; k > 48; k--)
        {
            if (!world.isEmptyBlock(new BlockPos(x, k, z)))
            {
                if (Math.abs(k - yorig) > 20)
                {
                    continue;
                }
                //Clear the downward path of small asteroids and any other asteroid rock
                for (int y = k + 2; y < 256; y++)
                {
                    if (world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockAsteroidRock)
                    {
                        world.removeBlock(new BlockPos(x, y, z), false);
                    }
                    if (world.getBlockState(new BlockPos(x - 1, y, z)).getBlock() instanceof BlockAsteroidRock)
                    {
                        world.removeBlock(new BlockPos(x - 1, y, z), false);
                    }
                    if (world.getBlockState(new BlockPos(x, y, z - 1)).getBlock() instanceof BlockAsteroidRock)
                    {
                        world.removeBlock(new BlockPos(x, y, z - 1), false);
                    }
                    if (world.getBlockState(new BlockPos(x - 1, y, z - 1)).getBlock() instanceof BlockAsteroidRock)
                    {
                        world.removeBlock(new BlockPos(x - 1, y, z - 1), false);
                    }
                }
                if (ConfigManagerCore.enableDebug.get())
                {
                    GCLog.info("Found asteroid at x" + (x) + " z" + (z));
                }
                return true;
            }
        }
        return false;
    }

    private void makeSmallLandingSpot(Level world, int x, int z)
    {
        this.loadChunksAround(x, z, -1, world.getChunkSource());

        for (int k = 255; k > 48; k--)
        {
            if (!world.isEmptyBlock(new BlockPos(x, k, z)))
            {
                this.makePlatform(world, x, k - 1, z);
                return;
            }
            if (!world.isEmptyBlock(new BlockPos(x - 1, k, z)))
            {
                this.makePlatform(world, x - 1, k - 1, z);
                return;
            }
            if (!world.isEmptyBlock(new BlockPos(x - 1, k, z - 1)))
            {
                this.makePlatform(world, x - 1, k - 1, z - 1);
                return;
            }
            if (!world.isEmptyBlock(new BlockPos(x, k, z - 1)))
            {
                this.makePlatform(world, x, k - 1, z - 1);
                return;
            }
        }

        this.makePlatform(world, x, 48 + world.random.nextInt(128), z);
        return;
    }

    private void loadChunksAround(int x, int z, int i, ChunkSource cp)
    {
        cp.getChunk(x >> 4, z >> 4, true);
        if ((x + i) >> 4 != x >> 4)
        {
            cp.getChunk((x + i) >> 4, z >> 4, true);
            if ((z + i) >> 4 != z >> 4)
            {
                cp.getChunk(x >> 4, (z + i) >> 4, true);
                cp.getChunk((x + i) >> 4, (z + i) >> 4, true);
            }
        }
        else if ((z + i) >> 4 != z >> 4)
        {
            cp.getChunk(x >> 4, (z + i) >> 4, true);
        }
    }

    private void makePlatform(Level world, int x, int y, int z)
    {
        for (int xx = -3; xx < 3; xx++)
        {
            for (int zz = -3; zz < 3; zz++)
            {
                if (xx == -3 && (zz == -3 || zz == 2))
                {
                    continue;
                }
                if (xx == 2 && (zz == -3 || zz == 2))
                {
                    continue;
                }
                doBlock(world, x + xx, y, z + zz);
            }
        }
        for (int xx = -2; xx < 2; xx++)
        {
            for (int zz = -2; zz < 2; zz++)
            {
                doBlock(world, x + xx, y - 1, z + zz);
            }
        }
        doBlock(world, x - 1, y - 2, z - 1);
        doBlock(world, x - 1, y - 2, z);
        doBlock(world, x, y - 2, z);
        doBlock(world, x, y - 2, z - 1);
    }

    private void doBlock(Level world, int x, int y, int z)
    {
        if (world.isEmptyBlock(new BlockPos(x, y, z)))
        {
            Block block = world.random.nextInt(3) == 0 ? AsteroidBlocks.rock0 : AsteroidBlocks.rock1;
            world.setBlock(new BlockPos(x, y, z), block.defaultBlockState(), 2);
        }
    }

    @Override
    public Vector3D getEntitySpawnLocation(ServerLevel world, Entity entity)
    {
        return new Vector3D(entity.getX(), ConfigManagerCore.disableLander.get() ? 250.0 : 900.0, entity.getZ());
    }

    @Override
    public Vector3D getParaChestSpawnLocation(ServerLevel world, ServerPlayer player, Random rand)
    {
        return null;
    }

    private void preGenChunks(Level w, int cx, int cz)
    {
        this.preGenChunk(w, cx, cz);
        for (int r = 1; r < 3; r++)
        {
            int xmin = cx - r;
            int xmax = cx + r;
            int zmin = cz - r;
            int zmax = cz + r;
            for (int i = -r; i < r; i++)
            {
                this.preGenChunk(w, xmin, cz + i);
                this.preGenChunk(w, xmax, cz - i);
                this.preGenChunk(w, cx - i, zmin);
                this.preGenChunk(w, cx + i, zmax);
            }
        }
    }

    private void preGenChunk(Level w, int chunkX, int chunkZ)
    {
        w.getChunk(chunkX, chunkZ);
    }

    @Override
    public void onSpaceDimensionChanged(Level newWorld, ServerPlayer player, boolean ridingAutoRocket)
    {
        if (!ridingAutoRocket && player != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);

            if (stats.getTeleportCooldown() <= 0)
            {
                if (player.abilities.flying)
                {
                    player.abilities.flying = false;
                }

                if (!newWorld.isClientSide)
                {
                    EntityEntryPod entryPod = EntityEntryPod.createEntityEntryPod(player);

                    boolean previous = CompatibilityManager.forceLoadChunks((ServerLevel) newWorld);
                    entryPod.forcedLoading = true;
                    newWorld.addFreshEntity(entryPod);
                    CompatibilityManager.forceLoadChunksEnd((ServerLevel) newWorld, previous);
                }

                stats.setTeleportCooldown(10);
            }
        }
    }

    @Override
    public void setupAdventureSpawn(ServerPlayer player)
    {
        GCPlayerStats stats = GCPlayerStats.get(player);
        SchematicRegistry.unlockNewPage(player, new ItemStack(GCItems.schematicRocketT2, 1)); //Knows how to build T2 rocket
        SchematicRegistry.unlockNewPage(player, new ItemStack(MarsItems.schematicRocketT3, 1)); //Knows how to build T3 rocket
        SchematicRegistry.unlockNewPage(player, new ItemStack(MarsItems.schematicAstroMiner, 1)); //Knows how to build Astro Miner
        NonNullList<ItemStack> rocketStacks = NonNullList.create();
        stats.setFuelLevel(1000);
        rocketStacks.add(new ItemStack(GCItems.oxMask));
        rocketStacks.add(new ItemStack(GCItems.oxygenGear));
        rocketStacks.add(new ItemStack(GCItems.oxTankMedium));
        rocketStacks.add(new ItemStack(GCItems.oxTankHeavy));
        rocketStacks.add(new ItemStack(GCItems.oxTankHeavy));
        rocketStacks.add(new ItemStack(AsteroidsItems.canisterLOX));
        rocketStacks.add(new ItemStack(AsteroidsItems.canisterLOX));
        rocketStacks.add(new ItemStack(AsteroidsItems.canisterLOX));
        rocketStacks.add(new ItemStack(AsteroidsItems.thermalCloth, 32));
        rocketStacks.add(new ItemStack(Blocks.GLASS_PANE, 16));
        rocketStacks.add(new ItemStack(Blocks.OAK_PLANKS, 32));
        rocketStacks.add(new ItemStack(MarsItems.ingotDesh, 16)); //Desh ingot
        rocketStacks.add(new ItemStack(GCItems.compressedWaferBasic, 8)); //Basic Wafer
        rocketStacks.add(new ItemStack(GCItems.solarModule1, 2)); //Solar Panels
        rocketStacks.add(new ItemStack(GCItems.dehydratedApple, 16));  //Canned food
        rocketStacks.add(new ItemStack(Items.EGG, 12));

        SpawnEggItem egg = SpawnEggItem.byId(EntityType.COW);
        ItemStack spawnEgg = new ItemStack(egg, 2);
        rocketStacks.add(spawnEgg);
        rocketStacks.add(PotionUtils.setPotion(new ItemStack(Items.POTION, 4), Potions.LONG_NIGHT_VISION)); //Night Vision Potion
        rocketStacks.add(new ItemStack(MarsBlocks.cryoChamber, 1)); //Cryogenic Chamber
        rocketStacks.add(new ItemStack(MarsItems.rocketTierTwoCargo2, 1));
        stats.setRocketStacks(rocketStacks);
    }
}
