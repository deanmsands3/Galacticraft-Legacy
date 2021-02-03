package micdoodle8.mods.galacticraft.core.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockTier1TreasureChest;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileEntityTreasureChestRenderer extends BlockEntityRenderer<TileEntityTreasureChest>
{
    private final ResourceLocation texture;
    private final Block block;

    private final ModelPart singleLid;
    private final ModelPart singleBottom;
    private final ModelPart[] keyParts = new ModelPart[5];
    private final ModelPart latch;

    public TileEntityTreasureChestRenderer(BlockEntityRenderDispatcher rendererDispatcher, ResourceLocation texture, Block block)
    {
        super(rendererDispatcher);
        this.texture = texture;
        this.block = block;
        this.singleBottom = new ModelPart(64, 64, 0, 19);
        this.singleBottom.addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);
        this.singleLid = new ModelPart(64, 64, 0, 0);
        this.singleLid.addBox(1.0F, 0.0F, -14.0F, 14.0F, 5.0F, 14.0F, 0.0F);
        this.singleLid.y = 9.0F;
        this.singleLid.z = 15.0F;
        this.keyParts[4] = new ModelPart(64, 64, 50, 43);
        this.keyParts[4].addBox(7F, 2F, -0.5F, 3, 1, 1);
        this.keyParts[4].setPos(0F, 0F, 0F);
        this.keyParts[4].setTexSize(64, 64);
        this.keyParts[4].mirror = true;
        this.keyParts[3] = new ModelPart(64, 64, 39, 43);
        this.keyParts[3].addBox(6F, 1F, -0.5F, 4, 1, 1);
        this.keyParts[3].setPos(0F, 0F, 0F);
        this.keyParts[3].setTexSize(64, 64);
        this.keyParts[3].mirror = true;
        this.keyParts[2] = new ModelPart(64, 64, 14, 43);
        this.keyParts[2].addBox(-0.5F, 0F, -0.5F, 11, 1, 1);
        this.keyParts[2].setPos(0F, 0F, 0F);
        this.keyParts[2].setTexSize(64, 64);
        this.keyParts[2].mirror = true;
        this.keyParts[1] = new ModelPart(64, 64, 9, 43);
        this.keyParts[1].addBox(-1.5F, -0.5F, -0.5F, 1, 2, 1);
        this.keyParts[1].setPos(0F, 0F, 0F);
        this.keyParts[1].setTexSize(64, 64);
        this.keyParts[1].mirror = true;
        this.keyParts[0] = new ModelPart(64, 64, 0, 43);
        this.keyParts[0].addBox(-4.5F, -1F, -0.5F, 3, 3, 1);
        this.keyParts[0].setPos(0F, 0F, 0F);
        this.keyParts[0].setTexSize(64, 64);
        this.keyParts[0].mirror = true;
        this.latch = new ModelPart(64, 64, 0, 0).setTexSize(64, 64);
        this.latch.addBox(-2.0F, -0.05F, -15.1F, 4, 4, 1, 0.0F);
        this.latch.x = 8.0F;
        this.latch.y = 7.0F;
        this.latch.z = 15.0F;
    }

    @Override
    public void render(TileEntityTreasureChest tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay)
    {
        Level world = tile.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? tile.getBlockState() : this.block.defaultBlockState().setValue(BlockTier1TreasureChest.FACING, Direction.NORTH);
        float f = blockstate.getValue(BlockTier1TreasureChest.FACING).toYRot();
        matrixStack.pushPose();
        matrixStack.translate(0.5D, 0.5D, 0.5D);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-f));
        matrixStack.translate(-0.5D, -0.5D, -0.5D);
        float f1 = tile.getOpenNess(partialTicks);
        f1 = 1.0F - f1;
        f1 = 1.0F - f1 * f1 * f1;
        float lidRotation = f1 * ((float) Math.PI / 2F);
        this.singleLid.xRot = lidRotation;

        RenderType renderType = RenderType.entitySolid(this.texture);
        VertexConsumer builder = buffer.getBuffer(renderType);

        if (!tile.locked)
        {
            for (ModelPart nmtmr : this.keyParts)
            {
                nmtmr.x = 8.0F;
                nmtmr.y = 7.0F;
                nmtmr.z = -2.0F;
                nmtmr.yRot = 3 * Constants.halfPI;
                nmtmr.xRot = lidRotation;
                nmtmr.render(matrixStack, builder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }

        this.latch.x = 8.0F;
        this.latch.y = 7.0F;
        this.latch.z = 15.0F;
        this.latch.xRot = 0;
        this.latch.yRot = 0;
        this.latch.render(matrixStack, builder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        this.singleLid.render(matrixStack, builder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        this.singleBottom.render(matrixStack, builder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStack.popPose();
    }
}