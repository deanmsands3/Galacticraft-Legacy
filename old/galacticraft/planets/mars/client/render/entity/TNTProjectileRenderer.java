package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.planets.mars.entities.TNTProjectileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TNTProjectileRenderer extends EntityRenderer<TNTProjectileEntity>
{
    public TNTProjectileRenderer(EntityRenderDispatcher renderManager)
    {
        super(renderManager);
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(TNTProjectileEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight)
    {
        matrixStack.pushPose();
        matrixStack.translate(0.0D, 0.5D, 0.0D);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
        matrixStack.translate(-0.5D, -0.5D, 0.5D);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        TntMinecartRenderer.renderWhiteSolidBlock(Blocks.TNT.defaultBlockState(), matrixStack, buffer, packedLight, entity.tickCount % 10 >= 5);
        matrixStack.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getEntityTexture(TNTProjectileEntity entity)
    {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}