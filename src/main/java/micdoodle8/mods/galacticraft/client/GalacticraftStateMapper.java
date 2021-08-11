package micdoodle8.mods.galacticraft.client;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;

public class GalacticraftStateMapper extends StateMapperBase
{
    public static final GalacticraftStateMapper INSTANCE = new GalacticraftStateMapper();

    public static String getPropertyString(IBlockState state)
    {
        return INSTANCE.getPropertyString(state.getProperties());
    }

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state)
    {
        ResourceLocation loc = Block.REGISTRY.getNameForObject(state.getBlock());
        loc = new ResourceLocation(loc.getNamespace().replace("|", ""), loc.getPath());
        return new ModelResourceLocation(loc, getPropertyString(state));
    }
}
