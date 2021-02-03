package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.SmallAsteroidEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;

public class SmallAsteroidRenderer extends EntityRenderer<SmallAsteroidEntity>
{
    public SmallAsteroidRenderer(EntityRenderDispatcher manager)
    {
        super(manager);
    }

    @Override
    public void render(SmallAsteroidEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        RenderSystem.disableRescaleNormal();
        matrixStack.pushPose();
        matrixStack.translate(0, 0.5F, 0);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(entity.xRot));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(entity.yRot));
        BlockState state = AsteroidBlocks.DARK_ASTEROID_ROCK.defaultBlockState();
        dispatcher.renderSingleBlock(state, matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
        matrixStack.popPose();
    }

    @Override
    public ResourceLocation getEntityTexture(SmallAsteroidEntity entity)
    {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}