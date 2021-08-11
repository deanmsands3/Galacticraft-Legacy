package micdoodle8.mods.galacticraft.blocks;

import net.minecraft.block.material.Material;

public class BlockDoubleSlabGC extends BlockSlabGC
{
    public BlockDoubleSlabGC(String name, Material material)
    {
        super(material);
        this.setTranslationKey(name);
    }

    @Override
    public boolean isDouble()
    {
        return true;
    }
}