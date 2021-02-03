package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.DimensionSpace;
import micdoodle8.mods.galacticraft.core.dimension.chunk.OrbitGenSettings;
import micdoodle8.mods.galacticraft.core.world.gen.BiomeOrbit;
import micdoodle8.mods.galacticraft.core.world.gen.ChunkGeneratorOrbit;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeSourceType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/***
 * Properties of a WorldProviderSpaceStation
 *     1.  Spinnable with Spin Thrusters (if you don't want spin, create your own SpinManager subclass which does nothing)
 *         (note: your SkyProvider needs to rotate according to setSpinDeltaPerTick()
 *     2.  Oregen from other mods is inhibited in this dimension
 *     3.  AstroMiner placement is inhibited in this dimension
 *     4.  The player on arrival into this dimension (after rocket flight) will be in 1st person view
 *
 */
public abstract class DimensionSpaceStation extends DimensionSpace
{
//    private SpinManager spinManager = new SpinManager(this);

    public DimensionSpaceStation(Level worldIn, DimensionType typeIn, float lightMod)
    {
        super(worldIn, typeIn, lightMod);
//        this.getSpinManager().registerServerSide();
    }

//    public SpinManager getSpinManager()
//    {
//        return spinManager;
//    }

    @Override
    public ChunkGenerator<?> createRandomLevelGenerator()
    {
        OrbitGenSettings settings = new OrbitGenSettings();
        return new ChunkGeneratorOrbit(this.level, BiomeSourceType.FIXED.create(BiomeSourceType.FIXED.createSettings(level.getLevelData()).setBiome(BiomeOrbit.space)), settings);
    }

    @Override
    public void updateWeather(Runnable defaultLogic)
    {
        super.updateWeather(defaultLogic);
//        spinManager.updateSpin();
    }

    @OnlyIn(Dist.CLIENT)
    public abstract void setSpinDeltaPerTick(float angle);

    @OnlyIn(Dist.CLIENT)
    public abstract float getSkyRotation();

    @OnlyIn(Dist.CLIENT)
    public abstract void createSkyProvider();
}
