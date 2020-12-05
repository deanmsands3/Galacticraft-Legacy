package micdoodle8.mods.galacticraft.core.client;

import com.mojang.blaze3d.platform.NativeImage;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class DynamicTextureProper extends DynamicTexture
{
    private boolean updateFlag = false;
    private final int width;    //We could transform these in the base class to protected
    private final int height;    //but whatever.

    public DynamicTextureProper(NativeImage img)
    {
        this(img.getWidth(), img.getHeight(), false);
        this.update(img);
    }

    public DynamicTextureProper(int width, int height, boolean clearIn)
    {
        super(width, height, clearIn);
        this.width = width;
        this.height = height;
    }

    public void update(NativeImage img)
    {
        this.getPixels().upload(0, 0, 0, false);
        this.updateFlag = true;
    }

    @Override
    public int getId()
    {
        if (this.updateFlag)
        {
            this.updateFlag = false;
            this.upload();
        }
        return super.getId();
    }
}
