package micdoodle8.mods.galacticraft.core.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ItemLiquidCanisterModel implements BakedModel
{
    private final BakedModel iBakedModel;

    public ItemLiquidCanisterModel(BakedModel i_modelToWrap)
    {
        this.iBakedModel = i_modelToWrap;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand)
    {
        return iBakedModel.getQuads(state, side, rand);
    }

    @Override
    public ItemOverrides getOverrides()
    {
        return BakedLiquidCanisterOverrideHandler.INSTANCE;
    }

    @Override
    public boolean useAmbientOcclusion()
    {
        return iBakedModel.useAmbientOcclusion();
    }

    @Override
    public boolean isAmbientOcclusion(BlockState state)
    {
        return useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d()
    {
        return iBakedModel.isGui3d();
    }

    @Override
    public boolean isCustomRenderer()
    {
        return iBakedModel.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon()
    {
        return iBakedModel.getParticleIcon();
    }

    @Override
    public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data)
    {
        return iBakedModel.getParticleIcon();
    }

    @Override
    public boolean func_230044_c_()
    {
        return iBakedModel.func_230044_c_();
    }

    @Override
    public ItemTransforms getTransforms()
    {
        return iBakedModel.getTransforms();
    }

    private static final class BakedLiquidCanisterOverrideHandler extends ItemOverrides
    {
        public static final BakedLiquidCanisterOverrideHandler INSTANCE = new BakedLiquidCanisterOverrideHandler();

        private BakedLiquidCanisterOverrideHandler()
        {
            super();
        }

        @Override
        public BakedModel resolve(BakedModel originalModel, ItemStack stack, Level world, LivingEntity entity)
        {
            if (stack.getTag() == null)
            {
                ItemStack copy = stack.copy();
                copy.setTag(new CompoundTag());
                copy.getTag().putBoolean("Unbreakable", true);
                return Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(copy);
            }
            return originalModel;
        }
    }
}
