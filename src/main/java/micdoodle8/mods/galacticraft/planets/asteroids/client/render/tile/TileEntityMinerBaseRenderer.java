package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.util.List;

@Environment(EnvType.CLIENT)
public class TileEntityMinerBaseRenderer extends BlockEntityRenderer<TileEntityMinerBase>
{
    private BakedModel minerBaseModelBaked;
    private List<BlockVec3> offsets = Lists.newArrayList();

    public TileEntityMinerBaseRenderer(BlockEntityRenderDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
        GCModelCache.INSTANCE.reloadCallback(this::updateModels);
        offsets.add(new BlockVec3(0, 0, 0));
        offsets.add(new BlockVec3(1, 0, 0));
        offsets.add(new BlockVec3(0, 0, 1));
        offsets.add(new BlockVec3(1, 0, 1));
        offsets.add(new BlockVec3(0, 1, 0));
        offsets.add(new BlockVec3(1, 1, 0));
        offsets.add(new BlockVec3(0, 1, 1));
        offsets.add(new BlockVec3(1, 1, 1));
    }

    private void updateModels()
    {
        minerBaseModelBaked = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/minerbase0.obj"), ImmutableList.of("dock"));
    }

    private int getMinerBaseLight(BlockAndTintGetter lightReaderIn, BlockPos pos)
    {
        int totalSky = 0;
        int totalBlock = 0;

        for (BlockVec3 offset : offsets)
        {
            BlockPos offsetPos = pos.offset(offset.x, offset.y, offset.z);
            totalSky += lightReaderIn.getBrightness(LightLayer.SKY, offsetPos);
            int block = lightReaderIn.getBrightness(LightLayer.BLOCK, offsetPos);
            BlockState state = lightReaderIn.getBlockState(offsetPos);
            int k = state.getLightValue(lightReaderIn, offsetPos);
            if (block < k)
            {
                block = k;
            }
            totalBlock += block;
        }

        return (totalSky / offsets.size()) << 20 | (totalBlock / offsets.size()) << 4;
    }

    @Override
    public void render(TileEntityMinerBase tile, float partialTicks, PoseStack matStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        if (!tile.isMaster)
        {
            return;
        }

        int light = getMinerBaseLight(tile.getLevel(), tile.getBlockPos());

//        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        matStack.pushPose();

//        GL11.glTranslatef((float) x + 1F, (float) y + 1F, (float) z + 1F);
//        GL11.glScalef(0.05F, 0.05F, 0.05F);
        matStack.translate(1.0F, 1.0F, 1.0F);
        matStack.scale(0.05F, 0.05F, 0.05F);

        switch (tile.facing)
        {
        case SOUTH:
//            GL11.glRotatef(180F, 0, 1F, 0);
            matStack.mulPose(new Quaternion(0.0F, 180.0F, 0.0F, true));
            break;
        case WEST:
//            GL11.glRotatef(90F, 0, 1F, 0);
            matStack.mulPose(new Quaternion(0.0F, 90.0F, 0.0F, true));
            break;
        case NORTH:
            break;
        case EAST:
//            GL11.glRotatef(270F, 0, 1F, 0);
            matStack.mulPose(new Quaternion(0.0F, 270.0F, 0.0F, true));
            break;
        }

        ClientUtil.drawBakedModel(minerBaseModelBaked, bufferIn, matStack, light);

        matStack.popPose();
    }
}
