package micdoodle8.mods.galacticraft.planets.venus.client.render.entity;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.planets.venus.entities.SpiderQueenWebEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;

@OnlyIn(Dist.CLIENT)
public class SpiderQueenWebRenderer extends EntityRenderer<SpiderQueenWebEntity>
{
    public SpiderQueenWebRenderer(EntityRenderDispatcher renderManager)
    {
        super(renderManager);
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(SpiderQueenWebEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        Lighting.turnOff();
        RenderSystem.disableRescaleNormal();
        matrixStack.pushPose();
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees((entity.tickCount + partialTicks) * 50.0F));
        matrixStack.translate(-0.5F, -1F, 0.5F);
        BlockState state = Blocks.COBWEB.defaultBlockState();
        dispatcher.renderSingleBlock(state, matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
        matrixStack.popPose();
        Lighting.turnBackOn();
    }

    @Override
    public ResourceLocation getEntityTexture(SpiderQueenWebEntity entity)
    {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}