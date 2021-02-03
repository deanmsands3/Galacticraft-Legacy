package micdoodle8.mods.galacticraft.core.world.gen;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class CraterConfig implements FeatureConfiguration {
   public final boolean largeCraters;

   public CraterConfig(boolean largeCraters) {
      this.largeCraters = largeCraters;
   }

   public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
      return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("large_craters"), ops.createBoolean(this.largeCraters))));
   }

   public static <T> CraterConfig deserialize(Dynamic<T> dynamic) {
      boolean flag = dynamic.get("large_craters").asBoolean(false);
      return new CraterConfig(flag);
   }
}
