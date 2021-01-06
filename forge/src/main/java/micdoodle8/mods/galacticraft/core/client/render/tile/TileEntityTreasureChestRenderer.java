package micdoodle8.mods.galacticraft.core.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockTier1TreasureChest;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileEntityTreasureChestRenderer extends TileEntityRenderer<TileEntityTreasureChest>
{
    private final ResourceLocation texture;
    private final Block block;

    private final ModelRenderer singleLid;
    private final ModelRenderer singleBottom;
    private final ModelRenderer[] keyParts = new ModelRenderer[5];
    private final ModelRenderer latch;

    public TileEntityTreasureChestRenderer(TileEntityRendererDispatcher rendererDispatcher, ResourceLocation texture, Block block)
    {
        super(rendererDispatcher);
        this.texture = texture;
        this.block = block;
        this.singleBottom = new ModelRenderer(64, 64, 0, 19);
        this.singleBottom.addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);
        this.singleLid = new ModelRenderer(64, 64, 0, 0);
        this.singleLid.addBox(1.0F, 0.0F, -14.0F, 14.0F, 5.0F, 14.0F, 0.0F);
        this.singleLid.rotationPointY = 9.0F;
        this.singleLid.rotationPointZ = 15.0F;
        this.keyParts[4] = new ModelRenderer(64, 64, 50, 43);
        this.keyParts[4].addBox(7F, 2F, -0.5F, 3, 1, 1);
        this.keyParts[4].setRotationPoint(0F, 0F, 0F);
        this.keyParts[4].setTextureSize(64, 64);
        this.keyParts[4].mirror = true;
        this.keyParts[3] = new ModelRenderer(64, 64, 39, 43);
        this.keyParts[3].addBox(6F, 1F, -0.5F, 4, 1, 1);
        this.keyParts[3].setRotationPoint(0F, 0F, 0F);
        this.keyParts[3].setTextureSize(64, 64);
        this.keyParts[3].mirror = true;
        this.keyParts[2] = new ModelRenderer(64, 64, 14, 43);
        this.keyParts[2].addBox(-0.5F, 0F, -0.5F, 11, 1, 1);
        this.keyParts[2].setRotationPoint(0F, 0F, 0F);
        this.keyParts[2].setTextureSize(64, 64);
        this.keyParts[2].mirror = true;
        this.keyParts[1] = new ModelRenderer(64, 64, 9, 43);
        this.keyParts[1].addBox(-1.5F, -0.5F, -0.5F, 1, 2, 1);
        this.keyParts[1].setRotationPoint(0F, 0F, 0F);
        this.keyParts[1].setTextureSize(64, 64);
        this.keyParts[1].mirror = true;
        this.keyParts[0] = new ModelRenderer(64, 64, 0, 43);
        this.keyParts[0].addBox(-4.5F, -1F, -0.5F, 3, 3, 1);
        this.keyParts[0].setRotationPoint(0F, 0F, 0F);
        this.keyParts[0].setTextureSize(64, 64);
        this.keyParts[0].mirror = true;
        this.latch = new ModelRenderer(64, 64, 0, 0).setTextureSize(64, 64);
        this.latch.addBox(-2.0F, -0.05F, -15.1F, 4, 4, 1, 0.0F);
        this.latch.rotationPointX = 8.0F;
        this.latch.rotationPointY = 7.0F;
        this.latch.rotationPointZ = 15.0F;
    }

    @Override
    public void render(TileEntityTreasureChest tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
    {
        World world = tile.getWorld();
        boolean flag = world != null;
        BlockState blockstate = flag ? tile.getBlockState() : this.block.getDefaultState().with(BlockTier1TreasureChest.FACING, Direction.NORTH);
        float f = blockstate.get(BlockTier1TreasureChest.FACING).getHorizontalAngle();
        matrixStack.push();
        matrixStack.translate(0.5D, 0.5D, 0.5D);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-f));
        matrixStack.translate(-0.5D, -0.5D, -0.5D);
        float f1 = tile.getLidAngle(partialTicks);
        f1 = 1.0F - f1;
        f1 = 1.0F - f1 * f1 * f1;
        float lidRotation = f1 * ((float) Math.PI / 2F);
        this.singleLid.rotateAngleX = lidRotation;

        RenderType renderType = RenderType.getEntitySolid(this.texture);
        IVertexBuilder builder = buffer.getBuffer(renderType);

        if (!tile.locked)
        {
            for (ModelRenderer nmtmr : this.keyParts)
            {
                nmtmr.rotationPointX = 8.0F;
                nmtmr.rotationPointY = 7.0F;
                nmtmr.rotationPointZ = -2.0F;
                nmtmr.rotateAngleY = 3 * Constants.halfPI;
                nmtmr.rotateAngleX = lidRotation;
                nmtmr.render(matrixStack, builder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }

        this.latch.rotationPointX = 8.0F;
        this.latch.rotationPointY = 7.0F;
        this.latch.rotationPointZ = 15.0F;
        this.latch.rotateAngleX = 0;
        this.latch.rotateAngleY = 0;
        this.latch.render(matrixStack, builder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        this.singleLid.render(matrixStack, builder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        this.singleBottom.render(matrixStack, builder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStack.pop();
    }
}