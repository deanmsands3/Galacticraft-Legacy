package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.core.world.gen.BiomeMoonFlat;
import micdoodle8.mods.galacticraft.core.world.gen.BiomeMoonHills;
import micdoodle8.mods.galacticraft.core.world.gen.BiomeMoonSuperFlat;
import micdoodle8.mods.galacticraft.planets.venus.VenusModule;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer0;

public enum GenLayerMoonBiomes implements AreaTransformer0
{
    INSTANCE;

    private static final Biome[] biomes = new Biome[] { BiomeMoonHills.moonBiomeHills, BiomeMoonFlat.moonBiomeFlat, BiomeMoonSuperFlat.moonBiomeSuperFlat };

    @Override
    public int applyPixel(Context noise, int x, int y)
    {
        return Registry.BIOME.getId(biomes[noise.nextRandom(biomes.length)]);
    }
}
