package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import micdoodle8.mods.galacticraft.planets.mars.entities.TNTProjectileEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TNTMinecartRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TNTProjectileRenderer extends EntityRenderer<TNTProjectileEntity>
{
    public TNTProjectileRenderer(EntityRendererManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 0.5F;
    }

    @Override
    public void render(TNTProjectileEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        matrixStack.push();
        matrixStack.translate(0.0D, 0.5D, 0.0D);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-90.0F));
        matrixStack.translate(-0.5D, -0.5D, 0.5D);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(90.0F));
        TNTMinecartRenderer.renderTntFlash(Blocks.TNT.getDefaultState(), matrixStack, buffer, packedLight, entity.ticksExisted % 10 >= 5);
        matrixStack.pop();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getEntityTexture(TNTProjectileEntity entity)
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}