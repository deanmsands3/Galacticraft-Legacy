package team.galacticraft.galacticraft.common.core.items;

import org.jetbrains.annotations.Nullable;

import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStatsClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemPropertyFunction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class ItemDungeonFinder extends ItemBase
{
   public ItemDungeonFinder(Properties builder) {
      super(builder);
      this.addProperty(new ResourceLocation(Constants.MOD_ID_CORE, "angle"), new ItemPropertyFunction() {
         @Environment(EnvType.CLIENT)
         private double rotation;
         @Environment(EnvType.CLIENT)
         private double rota;
         @Environment(EnvType.CLIENT)
         private long lastUpdateTick;

         @Environment(EnvType.CLIENT)
         public float call(ItemStack stack, @Nullable Level world, @Nullable LivingEntity livingEntity) {
            if (livingEntity == null && !stack.isFramed()) {
               return 0.0F;
            } else {
               boolean flag = livingEntity != null;
               Entity entity = flag ? livingEntity : stack.getFrame();
               if (world == null) {
                  world = entity.level;
               }

               double target;
               if (livingEntity instanceof Player) {
                  double d1 = flag ? (double)entity.yRot : this.getFrameRotation((ItemFrame)entity);
                  d1 = Mth.positiveModulo(d1 / 360.0D, 1.0D);
//                  double d2 = this.getSpawnToAngle(world, entity) / (double)((float)Math.PI * 2F);
                  double direction = Mth.positiveModulo(GCPlayerStatsClient.get(livingEntity).getDungeonDirection() / 360.0D, 1.0);
                  target = 0.5D - (d1 - 0.25D - direction);
               } else {
                  target = Math.random();
               }

               if (flag) {
                  target = this.wobble(world, target);
               }

               return Mth.positiveModulo((float)target, 1.0F);
            }
         }

         @Environment(EnvType.CLIENT)
         private double wobble(Level worldIn, double angle) {
            if (worldIn.getGameTime() != this.lastUpdateTick) {
               this.lastUpdateTick = worldIn.getGameTime();
               double d0 = angle - this.rotation;
               d0 = Mth.positiveModulo(d0 + 0.5D, 1.0D) - 0.5D;
               this.rota += d0 * 0.1D;
               this.rota *= 0.8D;
               this.rotation = Mth.positiveModulo(this.rotation + this.rota, 1.0D);
            }

            return this.rotation;
         }

         @Environment(EnvType.CLIENT)
         private double getFrameRotation(ItemFrame p_185094_1_) {
            return Mth.wrapDegrees(180 + p_185094_1_.getDirection().get2DDataValue() * 90);
         }

         @Environment(EnvType.CLIENT)
         private double getSpawnToAngle(LevelAccessor world, Entity entity) {
            BlockPos blockpos = world.getSharedSpawnPos();
            return Math.atan2((double)blockpos.getZ() - entity.getZ(), (double)blockpos.getX() - entity.getX());
         }
      });
   }
}
