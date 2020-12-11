package team.galacticraft.galacticraft.common.core.client.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import team.galacticraft.galacticraft.common.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.lwjgl.opengl.GL11;

public class RenderPlanet
{
    private static final TextureManager textureManager = Minecraft.getInstance().textureManager;

    private static final ResourceLocation textureEuropa = new ResourceLocation(Constants.MOD_ID_CORE, "textures/misc/planets/europa.png");
    private static final ResourceLocation textureGanymede = new ResourceLocation(Constants.MOD_ID_CORE, "textures/misc/planets/ganymede.png");
    private static final ResourceLocation textureIo = new ResourceLocation(Constants.MOD_ID_CORE, "textures/misc/planets/io.png");
    private static final ResourceLocation textureSaturn = new ResourceLocation(Constants.MOD_ID_CORE, "textures/misc/planets/saturn.png");
    private static final ResourceLocation textureJupiterInner = new ResourceLocation(Constants.MOD_ID_CORE, "textures/misc/planets/jupiter_inner.png");
    private static final ResourceLocation textureJupiterUpper = new ResourceLocation(Constants.MOD_ID_CORE, "textures/misc/planets/jupiter_upper.png");

    public static void renderPlanet(int textureId, float scale, float ticks, float relSize)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        float size = relSize / 70 * scale;
        ticks = ((float) System.nanoTime()) / 50000000F;
        RenderPlanet.drawTexturedRectUV(-size / 2, -size / 2, size, size, ticks);
    }

    public static void renderPlanet(ResourceLocation texture, float scale, float ticks, float relSize)
    {
        RenderPlanet.textureManager.bind(texture);
        float size = relSize / 70 * scale;
        ticks = ((float) System.nanoTime()) / 50000000F;
        RenderPlanet.drawTexturedRectUV(-size / 2, -size / 2, size, size, ticks);
    }

    public static void renderID(int id, float scale, float ticks)
    {
        ResourceLocation texture;
        switch (id)
        {
        case 0:
            texture = textureEuropa;
            break;
        case 1:
            texture = textureGanymede;
            break;
        case 2:
            texture = textureIo;
            break;
        case 3:
            texture = textureJupiterInner;
            break;
        case 4:
            texture = textureSaturn;
            break;
        default:
            texture = textureGanymede;
        }
        if (id == 3)  //Jupiter
        {
            float relSize = 48F;
            float size = relSize / 70 * scale;
            RenderPlanet.textureManager.bind(texture);
            RenderPlanet.drawTexturedRectUV(-size / 2, -size / 2, size, size, ticks);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef(0, 0, -0.001F);
            RenderPlanet.textureManager.bind(textureJupiterUpper);
            size *= 1.001F;
            RenderPlanet.drawTexturedRectUV(-size / 2, -size / 2, size, size, ticks * 0.85F);
        }
        else
        {
            RenderPlanet.renderPlanet(texture, scale, ticks, 8F);
        }
    }

    public static void drawTexturedRectUV(float x, float y, float width, float height, float ticks)
    {
        for (int ysect = 0; ysect < 6; ysect++)
        {
//    		drawTexturedRectUVSixth(x, y, width, height, (ticks / 600F) % 1F, ysect / 6F);
            // - 80F * MathHelper.sin(angle)
            float factor = 1F + Mth.cos((7.5F + 10F * ysect) / 62F);
            drawTexturedRectUVSixth(x, y, width, height, (ticks / 1100F) % 1F - (1F - factor) * 0.15F, ysect / 6F, 0.16F * factor);
        }
    }

    public static void drawTexturedRectUVSixth(float x, float y, float width, float height, float prog, float y0, float span)
    {
        y0 /= 2;
        if (prog < 0F)
        {
            prog += 1.0F;
        }
        prog = 1.0F - prog;
        float y1 = y0 + 1 / 12F;
        float y2 = 1F - y1;
        float y3 = 1F - y0;
        float yaa = y + height * y0;
        float yab = y + height * y1;
        float yba = y + height * y2;
        float ybb = y + height * y3;
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuilder();
        if (prog <= 1F - span)
        {
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_TEX);
            worldRenderer.vertex(x, yab, 0F).uv(prog, y1).endVertex();
            worldRenderer.vertex(x + width, yab, 0F).uv(prog + span, y1).endVertex();
            worldRenderer.vertex(x + width, yaa, 0F).uv(prog + span, y0).endVertex();
            worldRenderer.vertex(x, yaa, 0F).uv(prog, y0).endVertex();
            tessellator.end();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_TEX);
            worldRenderer.vertex(x, ybb, 0F).uv(prog, y3).endVertex();
            worldRenderer.vertex(x + width, ybb, 0F).uv(prog + span, y3).endVertex();
            worldRenderer.vertex(x + width, yba, 0F).uv(prog + span, y2).endVertex();
            worldRenderer.vertex(x, yba, 0F).uv(prog, y2).endVertex();
            tessellator.end();
        }
        else
        {
            double xp = x + width * (1F - prog) / span;
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_TEX);
            worldRenderer.vertex(x, yab, 0F).uv(prog, y1).endVertex();
            worldRenderer.vertex(xp, yab, 0F).uv(1.0F, y1).endVertex();
            worldRenderer.vertex(xp, yaa, 0F).uv(1.0F, y0).endVertex();
            worldRenderer.vertex(x, yaa, 0F).uv(prog, y0).endVertex();
            tessellator.end();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_TEX);
            worldRenderer.vertex(x, ybb, 0F).uv(prog, y3).endVertex();
            worldRenderer.vertex(xp, ybb, 0F).uv(1.0F, y3).endVertex();
            worldRenderer.vertex(xp, yba, 0F).uv(1.0F, y2).endVertex();
            worldRenderer.vertex(x, yba, 0F).uv(prog, y2).endVertex();
            tessellator.end();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_TEX);
            worldRenderer.vertex(xp, yab, 0F).uv(0F, y1).endVertex();
            worldRenderer.vertex(x + width, yab, 0F).uv(prog - 1F + span, y1).endVertex();
            worldRenderer.vertex(x + width, yaa, 0F).uv(prog - 1F + span, y0).endVertex();
            worldRenderer.vertex(xp, yaa, 0F).uv(0F, y0).endVertex();
            tessellator.end();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_TEX);
            worldRenderer.vertex(xp, ybb, 0F).uv(0F, y3).endVertex();
            worldRenderer.vertex(x + width, ybb, 0F).uv(prog - 1F + span, y3).endVertex();
            worldRenderer.vertex(x + width, yba, 0F).uv(prog - 1F + span, y2).endVertex();
            worldRenderer.vertex(xp, yba, 0F).uv(0F, y2).endVertex();
            tessellator.end();
        }
    }
}
