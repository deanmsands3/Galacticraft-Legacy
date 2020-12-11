package team.galacticraft.galacticraft.common.core.world.gen;

import team.galacticraft.galacticraft.common.api.world.BiomeGC;
import team.galacticraft.galacticraft.common.core.GCBlocks;
import team.galacticraft.galacticraft.common.core.world.gen.dungeon.DungeonConfiguration;
import team.galacticraft.galacticraft.common.core.world.gen.dungeon.RoomBoss;
import team.galacticraft.galacticraft.common.core.world.gen.dungeon.RoomTreasure;
import net.minecraft.world.level.levelgen.GenerationStep;
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
            return state.getBlock() == GCBlocks.moonStone;
        }
    });

    public BiomeMoon(BiomeBuilder biomeBuilder, boolean isAdaptive)
    {
        super(biomeBuilder, isAdaptive);
        this.addStructureStart(GCFeatures.MOON_DUNGEON.configured(new DungeonConfiguration(GCBlocks.moonDungeonBrick.defaultBlockState(), 25, 8, 16, 5, 6, RoomBoss.class, RoomTreasure.class)));
        this.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, GCFeatures.MOON_DUNGEON.configured(new DungeonConfiguration(GCBlocks.moonDungeonBrick.defaultBlockState(), 25, 8, 16, 5, 6, RoomBoss.class, RoomTreasure.class)).decorated(FeatureDecorator.NOPE.configured(DecoratorConfiguration.NONE)));
    }

    protected void addDefaultFeatures()
    {
        this.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreConfiguration(FILLER_TYPE_MOON, GCBlocks.oreCopperMoon.defaultBlockState(), 4)).decorated(FeatureDecorator.COUNT_RANGE.configured(new CountRangeDecoratorConfiguration(26, 0, 0, 60))));
        this.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreConfiguration(FILLER_TYPE_MOON, GCBlocks.oreTinMoon.defaultBlockState(), 4)).decorated(FeatureDecorator.COUNT_RANGE.configured(new CountRangeDecoratorConfiguration(23, 0, 0, 60))));
        this.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreConfiguration(FILLER_TYPE_MOON, GCBlocks.oreCheeseMoon.defaultBlockState(), 3)).decorated(FeatureDecorator.COUNT_RANGE.configured(new CountRangeDecoratorConfiguration(14, 0, 0, 85))));
        this.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreConfiguration(FILLER_TYPE_MOON, GCBlocks.moonDirt.defaultBlockState(), 32)).decorated(FeatureDecorator.COUNT_RANGE.configured(new CountRangeDecoratorConfiguration(20, 0, 0, 200))));
        this.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, GCFeatures.SAPPHIRE_ORE.configured(new ReplaceBlockConfiguration(GCBlocks.moonStone.defaultBlockState(), GCBlocks.oreSapphire.defaultBlockState())).decorated(GCFeatures.SAPPHIRE_ORE_PLACEMENT.configured(DecoratorConfiguration.NONE)));
    }
}
