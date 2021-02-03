package micdoodle8.mods.galacticraft.core.wrappers;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.Random;

abstract public class ModelTransformWrapper implements BakedModel
{
    private final BakedModel parent;

    public ModelTransformWrapper(BakedModel parent)
    {
        this.parent = parent;
    }

    @Override
    public boolean useAmbientOcclusion()
    {
        return parent.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d()
    {
        return parent.isGui3d();
    }

    @Override
    public boolean isCustomRenderer()
    {
        return parent.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon()
    {
        return parent.getParticleIcon();
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemTransforms getTransforms()
    {
        return parent.getTransforms();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand)
    {
        return parent.getQuads(state, side, rand);
    }

    @Override
    public ItemOverrides getOverrides()
    {
        return parent.getOverrides();
    }

    @Override
    public BakedModel handlePerspective(ItemTransforms.TransformType cameraTransformType, PoseStack mat)
    {
        if (!getTransformForPerspective(cameraTransformType, mat))
        {
            return net.minecraftforge.client.ForgeHooksClient.handlePerspective(getBakedModel(), cameraTransformType, mat);
        }

        return this;
    }

    @Override
    public boolean func_230044_c_()
    {
        return true;
    }

    abstract protected boolean getTransformForPerspective(ItemTransforms.TransformType cameraTransformType, PoseStack mat);
}
