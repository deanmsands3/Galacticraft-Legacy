package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import micdoodle8.mods.galacticraft.core.entities.Tier1RocketEntity;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.opengl.GL11;

public class Overlay
{
    /**
     * Get the player's spaceship height off ground
     *
     * @param player thePlayer
     * @return position of player's spaceship
     */
    protected static int getPlayerPositionY(Player player)
    {
        if (player.getVehicle() != null && player.getVehicle() instanceof Tier1RocketEntity)
        {
            return (int) Math.floor(((Tier1RocketEntity) player.getVehicle()).getY());
        }

        return (int) Math.floor(player.getY());
    }

    /**
     * Draw a textured rectangle at the specified position
     *
     * @param par1 xpos
     * @param par2 ypos
     * @param par3 u
     * @param par4 v
     * @param par5 width
     * @param par6 height
     */
    protected static void blit(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        final float var7 = 0.00390625F;
        final float var8 = 0.00390625F;
        final Tesselator tess = Tesselator.getInstance();
        BufferBuilder worldRenderer = tess.getBuilder();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_TEX);
        worldRenderer.vertex(par1 + 0, par2 + par6, 0.0).uv((par3 + 0) * var7, (par4 + par6) * var8).endVertex();
        worldRenderer.vertex(par1 + par5, par2 + par6, 0.0).uv((par3 + par5) * var7, (par4 + par6) * var8).endVertex();
        worldRenderer.vertex(par1 + par5, par2 + 0, 0.0).uv((par3 + par5) * var7, (par4 + 0) * var8).endVertex();
        worldRenderer.vertex(par1 + 0, par2 + 0, 0.0).uv((par3 + 0) * var7, (par4 + 0) * var8).endVertex();
        tess.end();
    }

    /**
     * Draws a rectangle with middle at point specified
     *
     * @param var1 x
     * @param var3 y
     * @param var5 depth
     * @param var7 width
     * @param var9 height
     */
    protected static void drawCenteringRectangle(double var1, double var3, double var5, double var7, double var9)
    {
//        var7 *= 0.5D;
//        var9 *= 0.5D;
//        final Tessellator tess = Tessellator.getInstance();
//        BufferBuilder worldRenderer = tess.getBuffer();
//        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//        worldRenderer.pos(var1 - var7, var3 + var9, var5).tex(0.0D, 1.0D).endVertex();
//        worldRenderer.pos(var1 + var7, var3 + var9, var5).tex(1.0D, 1.0D).endVertex();
//        worldRenderer.pos(var1 + var7, var3 - var9, var5).tex(1.0D, 0.0D).endVertex();
//        worldRenderer.pos(var1 - var7, var3 - var9, var5).tex(0.0D, 0.0D).endVertex();
//        tess.draw(); TODO Drawing
    }
}
