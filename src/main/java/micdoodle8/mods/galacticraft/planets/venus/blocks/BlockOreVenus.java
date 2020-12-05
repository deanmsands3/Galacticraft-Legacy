package micdoodle8.mods.galacticraft.planets.venus.blocks;

import com.google.common.base.Predicate;
import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class BlockOreVenus extends Block implements IDetectableResource, IPlantableBlock, ISortable
{
    public BlockOreVenus(Properties builder)
    {
        super(builder);
    }

//    @Override
//    public int quantityDropped(BlockState state, int fortune, Random random)
//    {
//        EnumBlockBasicVenus type = ((EnumBlockBasicVenus) state.get(BASIC_TYPE_VENUS));
//        switch (type)
//        {
//        case ORE_SILICON:
//            if (fortune > 0)
//            {
//                int j = random.nextInt(fortune + 2) - 1;
//
//                if (j < 0)
//                {
//                    j = 0;
//                }
//
//                return this.quantityDropped(random) * (j + 1);
//            }
//        default:
//            return this.quantityDropped(random);
//        }
//    }


    @Override
    public int getExpDrop(BlockState state, LevelReader world, BlockPos pos, int fortune, int silktouch)
    {
        if (state.getBlock() == VenusBlocks.oreQuartz || state.getBlock() == VenusBlocks.oreSilicon)
        {
            return Mth.nextInt(world instanceof Level ? ((Level) world).random : new Random(), 2, 5);
        }
        return 0;
    }

    @Override
    public boolean isValueable(BlockState state)
    {
        return true;
    }

    @Override
    public int requiredLiquidBlocksNearby()
    {
        return 4;
    }

    @Override
    public boolean isPlantable(BlockState state)
    {
        return false;
    }

    @Override
    public EnumSortCategory getCategory()
    {
//        EnumBlockBasicVenus type = ((EnumBlockBasicVenus) getStateFromMeta(meta).getValue(BASIC_TYPE_VENUS));
//        switch (type)
//        {
//        case ORE_ALUMINUM:
//        case ORE_COPPER:
//        case ORE_GALENA:
//        case ORE_QUARTZ:
//        case ORE_SILICON:
//        case ORE_TIN:
//        case ORE_SOLAR_DUST:
            return EnumSortCategory.ORE;
//        case DUNGEON_BRICK_1:
//        case DUNGEON_BRICK_2:
//            return EnumSortCategory.BRICKS;
//        case LEAD_BLOCK:
//            return EnumSortCategory.INGOT_BLOCK;
//        default:
//            return EnumSortCategory.GENERAL;
//        }
    }
}
