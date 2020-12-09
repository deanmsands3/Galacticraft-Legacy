package team.galacticraft.galacticraft.common.core.client.obj;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class ContentsModelConfiguration implements IModelConfiguration {

    @Nullable
    @Override
    public UnbakedModel getOwnerModel() {
        return null;
    }

    @NotNull
    @Override
    public String getModelName() {
        return "transmitter_contents";
    }

    @Override
    public boolean isTexturePresent(@NotNull String name) {
        return false;
    }

    @NotNull
    @Override
    public Material resolveTexture(@NotNull String name) {
        return ModelLoaderRegistry.blockMaterial(name);
    }

    @Override
    public boolean isShadedInGui() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean useSmoothLighting() {
        return false;
    }

    @NotNull
    @Override
    @Deprecated
    public ItemTransforms getCameraTransforms() {
        return ItemTransforms.NO_TRANSFORMS;
    }

    @NotNull
    @Override
    public ModelState getCombinedTransform() {
        return BlockModelRotation.X0_Y0;
    }
}