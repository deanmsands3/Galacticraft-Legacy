package team.galacticraft.galacticraft.forge.mixin;

import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Chunk.class)
public interface ChunkMixin {
    @Accessor
    boolean isLoaded();
}
