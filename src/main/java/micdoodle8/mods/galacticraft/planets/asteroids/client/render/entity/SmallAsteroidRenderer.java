package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.SmallAsteroidEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.EmptyModelData;

public class SmallAsteroidRenderer extends EntityRenderer<SmallAsteroidEntity>
{
    public SmallAsteroidRenderer(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public void render(SmallAsteroidEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        RenderSystem.disableRescaleNormal();
        matrixStack.push();
        matrixStack.translate(0, 0.5F, 0);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(entity.rotationPitch));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(entity.rotationYaw));
        BlockState state = AsteroidBlocks.DARK_ASTEROID_ROCK.getDefaultState();
        dispatcher.renderBlock(state, matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
        matrixStack.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(SmallAsteroidEntity entity)
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}