package micdoodle8.mods.galacticraft.core.dimension.chunk;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.ChunkGeneratorSettings;

public class MoonGenSettings extends ChunkGeneratorSettings
{
    public MoonGenSettings()
    {
        this.defaultBlock = GCBlocks.MOON_ROCK.defaultBlockState();
        this.defaultFluid = Blocks.AIR.defaultBlockState();
    }

    public int getBiomeSize()
    {
        return 4;
    }

    public int getRiverSize()
    {
        return 4;
    }

    public int getBiomeId()
    {
        return -1;
    }

    @Override
    public int getBedrockFloorPosition()
    {
        return 0;
    }

    public int getHomeTreeDistance()
    {
        return 20;
    }

    public int getHomeTreeSeparation()
    {
        return 4;
    }
}