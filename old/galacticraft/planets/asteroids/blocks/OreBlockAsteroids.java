package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.List;

public class OreBlockAsteroids extends Block implements IDetectableResource, ISortable
{
    public OreBlockAsteroids(Properties properties)
    {
        super(properties);
    }

    @Override
    public int getExpDrop(BlockState state, LevelReader world, BlockPos pos, int fortune, int silktouch)
    {
        if (this == AsteroidBlocks.ILMENITE_ORE)
        {
            Mth.nextInt(RANDOM, 2, 3);
        }

        return super.getExpDrop(state, world, pos, fortune, silktouch);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
    {
//        if (this == AsteroidBlocks.oreIlmenite)
//        {
//            ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
//
//            int count = quantityDropped(state, fortune, ((World) world).rand);
//            for (int i = 0; i < count; i++)
//            {
//                ret.add(new ItemStack(AsteroidsItems.basicItem, 1, 3));
//            }
//            count = quantityDropped(state, fortune, ((World) world).rand);
//            for (int i = 0; i < count; i++)
//            {
//                ret.add(new ItemStack(AsteroidsItems.basicItem, 1, 4));
//            }
//            return ret;
//        } TODO Loot

        return super.getDrops(state, builder);
    }

    @Override
    public boolean isValueable(BlockState metadata)
    {
        return true;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.ORE;
    }
}
