package micdoodle8.mods.galacticraft.core.world.gen.placement;

import com.mojang.datafixers.Dynamic;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.SimpleFeatureDecorator;

public class SapphirePlacement extends SimpleFeatureDecorator<NoneDecoratorConfiguration>
{
   public SapphirePlacement(Function<Dynamic<?>, ? extends NoneDecoratorConfiguration> config) {
      super(config);
   }

   public Stream<BlockPos> getPositions(Random random, NoneDecoratorConfiguration config, BlockPos pos) {
      int i = 1 + random.nextInt(2);
      return IntStream.range(0, i).mapToObj((i1) -> {
         int j = random.nextInt(16) + pos.getX();
         int k = random.nextInt(16) + pos.getZ();
         int l = random.nextInt(28) + 4;
         return new BlockPos(j, l, k);
      });
   }
}
