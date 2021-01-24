package micdoodle8.mods.galacticraft.planets.mars.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@OnlyIn(Dist.CLIENT)
public class TileEntityTreasureChestRenderer<T extends TileEntityTreasureChest & LidBlockEntity> extends ChestRenderer<T>
{
//    private static final ResourceLocation treasureChestTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/model/treasure.png");

//    private final ModelTreasureChest chestModel = new ModelTreasureChest();
    public ModelPart[] keyParts = new ModelPart[6];

    public TileEntityTreasureChestRenderer(BlockEntityRenderDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
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
        this.keyParts[5] = new ModelPart(64, 64, 0, 0).setTexSize(64, 64);
        this.keyParts[5].addBox(-2.0F, -2.05F, -15.1F, 4, 4, 1, 0.0F);
        this.keyParts[5].x = 8.0F;
        this.keyParts[5].y = 7.0F;
        this.keyParts[5].z = 15.0F;
    }

    @Override
    public void render(T chest, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        Level world = chest.getLevel();
        boolean flag = world != null;
        BlockState blockstate = flag ? chest.getBlockState() : Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        ChestType chesttype = blockstate.hasProperty(ChestBlock.TYPE) ? blockstate.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
        Block block = blockstate.getBlock();

        matrixStackIn.pushPose();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.scale(1.0F, -1.0F, -1.0F);
        matrixStackIn.translate(0.5F, 0.5F, 0.5F);
        float f = chest.getBlockState().getValue(ChestBlock.FACING).toYRot();
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f));
        matrixStackIn.translate(-0.5F, -0.5F, -0.5F);
        Material material = this.getMaterial(chest, chesttype);
        VertexConsumer ivertexbuilder = material.buffer(bufferIn, RenderType::entityCutout);
        for (ModelPart renderer : keyParts)
        {
            renderer.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn);
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        matrixStackIn.popPose();
        super.render(chest, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}