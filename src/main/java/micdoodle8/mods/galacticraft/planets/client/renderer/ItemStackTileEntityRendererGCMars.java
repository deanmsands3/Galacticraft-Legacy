package micdoodle8.mods.galacticraft.planets.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ItemStackTileEntityRendererGCMars extends ItemStackTileEntityRenderer
{
    private final TileEntityTreasureChest tier2TreasureChest = new TileEntityTreasureChest();

    @Override
    public void render(ItemStack itemStack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
    {
        Item item = itemStack.getItem();

        if (item instanceof BlockItem)
        {
            TileEntity tileentity = null;
            Block block = ((BlockItem)item).getBlock();

            if (block == MarsBlocks.TIER_2_TREASURE_CHEST)
            {
                tileentity = this.tier2TreasureChest;
            }

            if (tileentity != null)
            {
                TileEntityRendererDispatcher.instance.renderItem(tileentity, matrixStack, buffer, combinedLight, combinedOverlay);
            }
        }
    }
}