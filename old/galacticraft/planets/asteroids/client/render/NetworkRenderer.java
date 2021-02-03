package micdoodle8.mods.galacticraft.planets.asteroids.client.render;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamOutput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.lwjgl.opengl.GL11;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import java.util.ArrayList;
import java.util.List;

public class NetworkRenderer
{
    public static void renderNetworks(Level world, float partialTicks)
    {
        List<TileEntityBeamOutput> nodes = new ArrayList<TileEntityBeamOutput>();

        for (Object o : new ArrayList<BlockEntity>(world.blockEntityList))
        {
            if (o instanceof TileEntityBeamOutput)
            {
                nodes.add((TileEntityBeamOutput) o);
            }
        }

        if (nodes.isEmpty())
        {
            return;
        }

        Tesselator tess = Tesselator.getInstance();
        LocalPlayer player = Minecraft.getInstance().player;
        double interpPosX = player.xOld + (player.getX() - player.xOld) * partialTicks;
        double interpPosY = player.yOld + (player.getY() - player.yOld) * partialTicks;
        double interpPosZ = player.zOld + (player.getZ() - player.zOld) * partialTicks;

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        for (TileEntityBeamOutput tileEntity : nodes)
        {
            if (tileEntity.getTarget() == null)
            {
                continue;
            }

            GL11.glPushMatrix();

            Vector3 outputPoint = tileEntity.getOutputPoint(true);
            Vector3 targetInputPoint = tileEntity.getTarget().getInputPoint();

            Vector3 direction = Vector3.subtract(outputPoint, targetInputPoint);
            float directionLength = direction.getMagnitude();

            float posX = (float) (tileEntity.getBlockPos().getX() - interpPosX);
            float posY = (float) (tileEntity.getBlockPos().getY() - interpPosY);
            float posZ = (float) (tileEntity.getBlockPos().getZ() - interpPosZ);
            GL11.glTranslatef(posX, posY, posZ);

            GL11.glTranslatef(outputPoint.floatX() - tileEntity.getBlockPos().getX(), outputPoint.floatY() - tileEntity.getBlockPos().getY(), outputPoint.floatZ() - tileEntity.getBlockPos().getZ());
            GL11.glRotatef(tileEntity.yaw + 180, 0, 1, 0);
            GL11.glRotatef(-tileEntity.pitch, 1, 0, 0);
            GL11.glRotatef(tileEntity.ticks * 10, 0, 0, 1);

            tess.getBuilder().begin(GL11.GL_LINES, DefaultVertexFormat.POSITION_COLOR);

            for (Direction dir : Direction.values())
            {
                tess.getBuilder().vertex(dir.getStepX() / 40.0F, dir.getStepY() / 40.0F, dir.getStepZ() / 40.0F).color(tileEntity.getColor().floatX(), tileEntity.getColor().floatY(), tileEntity.getColor().floatZ(), 1.0F).endVertex();
                tess.getBuilder().vertex(dir.getStepX() / 40.0F, dir.getStepY() / 40.0F, directionLength + dir.getStepZ() / 40.0F).color(tileEntity.getColor().floatX(), tileEntity.getColor().floatY(), tileEntity.getColor().floatZ(), 1.0F).endVertex();
            }

            tess.end();

            GL11.glPopMatrix();
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glColor4f(1, 1, 1, 1);
    }
}
