package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.api.world.BiomeGC;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.RoomBoss;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.RoomTreasure;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.CountRangeDecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

public class BiomeMoon extends BiomeGC
{
    public OreConfiguration.Predicates FILLER_TYPE_MOON = OreConfiguration.Predicates.create("MOON_STONE", "moon_stone", (state) -> {
        if (state == null) {
            return false;
        } else {
            return state.getBlock() == GCBlocks.MOON_ROCK;
        }
    });

    public BiomeMoon(BiomeBuilder biomeBuilder, boolean isAdaptive)
    {
        super(biomeBuilder, isAdaptive);
        this.addStructureStart(GCFeatures.MOON_DUNGEON.configured(new DungeonConfiguration(GCBlocks.MOON_DUNGEON_BRICKS.defaultBlockState(), 25, 8, 16, 5, 6, RoomBoss.class, RoomTreasure.class)));
        this.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, GCFeatures.MOON_DUNGEON.configured(new DungeonConfiguration(GCBlocks.MOON_DUNGEON_BRICKS.defaultBlockState(), 25, 8, 16, 5, 6, RoomBoss.class, RoomTreasure.class)).decorated(FeatureDecorator.NOPE.configured(DecoratorConfiguration.NONE)));
    }

    protected void addDefaultFeatures()
    {
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreConfiguration(FILLER_TYPE_MOON, GCBlocks.MOON_COPPER_ORE.defaultBlockState(), 4)).decorated(FeatureDecorator.COUNT_RANGE.configured(new CountRangeDecoratorConfiguration(26, 0, 0, 60))));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreConfiguration(FILLER_TYPE_MOON, GCBlocks.MOON_TIN_ORE.defaultBlockState(), 4)).decorated(FeatureDecorator.COUNT_RANGE.configured(new CountRangeDecoratorConfiguration(23, 0, 0, 60))));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreConfiguration(FILLER_TYPE_MOON, GCBlocks.CHEESE_ORE.defaultBlockState(), 3)).decorated(FeatureDecorator.COUNT_RANGE.configured(new CountRangeDecoratorConfiguration(14, 0, 0, 85))));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreConfiguration(FILLER_TYPE_MOON, GCBlocks.MOON_DIRT.defaultBlockState(), 32)).decorated(FeatureDecorator.COUNT_RANGE.configured(new CountRangeDecoratorConfiguration(20, 0, 0, 200))));
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, GCFeatures.SAPPHIRE_ORE.configured(new ReplaceBlockConfiguration(GCBlocks.MOON_ROCK.defaultBlockState(), GCBlocks.SAPPHIRE_ORE.defaultBlockState())).decorated(GCFeatures.SAPPHIRE_ORE_PLACEMENT.configured(DecoratorConfiguration.NONE)));
    }
}
