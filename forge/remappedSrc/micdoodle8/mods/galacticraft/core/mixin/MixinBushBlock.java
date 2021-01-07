package micdoodle8.mods.galacticraft.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.DimensionAsteroids;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

@Mixin(BushBlock.class)
public abstract class MixinBushBlock
{
    @Inject(method = "isValidPosition(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/IWorldReader;Lnet/minecraft/util/math/BlockPos;)Z", cancellable = true, at = @At("HEAD"))
    private void isValidPosition(BlockState state, IWorldReader world, BlockPos pos, CallbackInfoReturnable info)
    {
        BlockPos blockpos = pos.down();

        // hopefully it is a good condition for nether wart in asteroids dimension
        if (state.getBlock() == Blocks.NETHER_WART && world.getDimension() instanceof DimensionAsteroids)
        {
            info.setReturnValue(world.getBlockState(blockpos).getBlock() == GCBlocks.MOON_ROCK || world.getBlockState(blockpos).canSustainPlant(world, blockpos, Direction.UP, (BushBlock)(Object)this));
        }
    }
}