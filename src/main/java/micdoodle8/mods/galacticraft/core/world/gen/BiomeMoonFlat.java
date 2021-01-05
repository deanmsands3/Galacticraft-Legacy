package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.api.world.BiomeGC;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class BiomeMoonFlat extends BiomeMoon
{
    public static final BiomeMoonFlat moonBiomeFlat = new BiomeMoonFlat();

    BiomeMoonFlat()
    {
        super((new Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, new SurfaceBuilderConfig(GCBlocks.MOON_TURF.getDefaultState(), GCBlocks.MOON_DIRT.getDefaultState(), GCBlocks.MOON_DIRT.getDefaultState())).precipitation(RainType.NONE).category(Category.NONE).depth(0.9F).scale(0.0F).temperature(0.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent(null), true);
        addDefaultFeatures();
    }

    @Override
    public float getSpawningChance()
    {
        return 0.1F;
    }
}
