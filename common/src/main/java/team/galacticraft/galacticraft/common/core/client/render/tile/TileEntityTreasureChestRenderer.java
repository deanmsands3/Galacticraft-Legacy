package team.galacticraft.galacticraft.common.core.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.tile.TileEntityTreasureChest;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@Environment(EnvType.CLIENT)
public class TileEntityTreasureChestRenderer extends BlockEntityRenderer<TileEntityTreasureChest>
{
    private final ResourceLocation texture;

    private final ModelPart singleLid;
    private final ModelPart singleBottom;
    private final ModelPart[] keyParts = new ModelPart[5];
    private final ModelPart latch;

    public TileEntityTreasureChestRenderer(BlockEntityRenderDispatcher rendererDispatcherIn, ResourceLocation texture)
    {
        super(rendererDispatcherIn);
        this.texture = texture;
        singleBottom = new ModelPart(64, 64, 0, 19);
        singleBottom.addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);
        singleLid = new ModelPart(64, 64, 0, 0);
        singleLid.addBox(1.0F, 0.0F, -14.0F, 14.0F, 5.0F, 14.0F, 0.0F);
        singleLid.y = 9.0F;
        singleLid.z = 15.0F;
        keyParts[4] = new ModelPart(64, 64, 50, 43);
        keyParts[4].addBox(7F, 2F, -0.5F, 3, 1, 1);
        keyParts[4].setPos(0F, 0F, 0F);
        keyParts[4].setTexSize(64, 64);
        keyParts[4].mirror = true;
        keyParts[3] = new ModelPart(64, 64, 39, 43);
        keyParts[3].addBox(6F, 1F, -0.5F, 4, 1, 1);
        keyParts[3].setPos(0F, 0F, 0F);
        keyParts[3].setTexSize(64, 64);
        keyParts[3].mirror = true;
        keyParts[2] = new ModelPart(64, 64, 14, 43);
        keyParts[2].addBox(-0.5F, 0F, -0.5F, 11, 1, 1);
        keyParts[2].setPos(0F, 0F, 0F);
        keyParts[2].setTexSize(64, 64);
        keyParts[2].mirror = true;
        keyParts[1] = new ModelPart(64, 64, 9, 43);
        keyParts[1].addBox(-1.5F, -0.5F, -0.5F, 1, 2, 1);
        keyParts[1].setPos(0F, 0F, 0F);
        keyParts[1].setTexSize(64, 64);
        keyParts[1].mirror = true;
        keyParts[0] = new ModelPart(64, 64, 0, 43);
        keyParts[0].addBox(-4.5F, -1F, -0.5F, 3, 3, 1);
        keyParts[0].setPos(0F, 0F, 0F);
        keyParts[0].setTexSize(64, 64);
        keyParts[0].mirror = true;
        latch = new ModelPart(64, 64, 0, 0).setTexSize(64, 64);
        latch.addBox(-2.0F, -0.05F, -15.1F, 4, 4, 1, 0.0F);
        latch.x = 8.0F;
        latch.y = 7.0F;
        latch.z = 15.0F;
    }

    @Override
    public void render(TileEntityTreasureChest chest, float partialTicks, PoseStack matStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
//        GL11.glPushMatrix();
        matStack.pushPose();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
////        GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
//        matStack.translate(0.0F, 1.0F, 1.0F);
//        matStack.scale(1.0F, -1.0F, -1.0F);
////        GL11.glScalef(1.0F, -1.0F, -1.0F);
//        matStack.translate(0.5F, 0.5F, 0.5F);
////        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        float f = chest.getBlockState().getValue(ChestBlock.FACING).toYRot();
////        GL11.glRotatef(f, 0.0F, 1.0F, 0.0F);
//        matStack.rotate(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), f, true));
////        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
//        matStack.translate(-0.5F, -0.5F, -0.5F);
        matStack.translate(0.5D, 0.5D, 0.5D);
        matStack.mulPose(Vector3f.YP.rotationDegrees(-f));
        matStack.translate(-0.5D, -0.5D, -0.5D);
        float f1 = ((LidBlockEntity) chest).getOpenNess(partialTicks);
        f1 = 1.0F - f1;
        f1 = 1.0F - f1 * f1 * f1;
        float lidRotation = (f1 * ((float) Math.PI / 2F));
        singleLid.xRot = lidRotation;

        RenderType renderType = RenderType.entitySolid(texture);
        VertexConsumer builder = bufferIn.getBuffer(renderType);

        if (!chest.locked)
        {
            for (final ModelPart nmtmr : keyParts)
            {
                nmtmr.x = 8.0F;
                nmtmr.y = 7.0F;
                nmtmr.z = -2.0F;
                nmtmr.yRot = 3 * Constants.HALF_PI;
                nmtmr.xRot = lidRotation;
                nmtmr.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }

        latch.x = 8.0F;
        latch.y = 7.0F;
        latch.z = 15.0F;
        latch.xRot = 0;
        latch.yRot = 0;
        latch.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);

        singleLid.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        singleBottom.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
//        singleLatch.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        GL11.glPopMatrix();
        matStack.popPose();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
