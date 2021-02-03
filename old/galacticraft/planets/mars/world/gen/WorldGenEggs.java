package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import com.mojang.datafixers.Dynamic;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockBasicMars;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockSlimelingEgg;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.ChunkGeneratorSettings;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import java.util.Random;
import java.util.function.Function;

public class WorldGenEggs extends Feature<NoneFeatureConfiguration>
{
    public WorldGenEggs(Function<Dynamic<?>, ? extends NoneFeatureConfiguration> configFactoryIn)
    {
        super(configFactoryIn);
    }

    @Override
    public boolean place(LevelAccessor worldIn, ChunkGenerator<? extends ChunkGeneratorSettings> generator, Random rand, BlockPos pos, NoneFeatureConfiguration config)
    {
        int i1 = pos.getX() + rand.nextInt(8) - rand.nextInt(8);
        int j1 = pos.getY() + rand.nextInt(4) - rand.nextInt(4);
        int k1 = pos.getZ() + rand.nextInt(8) - rand.nextInt(8);
        BlockPos newPos = new BlockPos(i1, j1, k1);

        if (!worldIn.hasChunkAt(newPos.offset(1, 0, 1)))
        {
            return false;
        }

        if (worldIn.isEmptyBlock(newPos) && (j1 < 127 || !worldIn.getDimension().isHasCeiling()))
        {
            BlockState below = worldIn.getBlockState(newPos.below());
            if (below.getBlock() == MarsBlocks.MARS_FINE_REGOLITH)
            {
                Block block;
                switch (rand.nextInt(3))
                {
                case 0:
                    block = MarsBlocks.RED_SLIMELING_EGG;
                    break;
                case 1:
                    block = MarsBlocks.BLUE_SLIMELING_EGG;
                    break;
                default:
                case 2:
                    block = MarsBlocks.YELLOW_SLIMELING_EGG;
                    break;
                }
                worldIn.setBlock(newPos, block.defaultBlockState(), 2);
            }
        }

        return true;
    }
}
