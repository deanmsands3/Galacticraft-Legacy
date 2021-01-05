package micdoodle8.mods.galacticraft.core.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ItemStackTileEntityRendererGC extends ItemStackTileEntityRenderer
{
    public static final ItemStackTileEntityRendererGC INSTANCE = new ItemStackTileEntityRendererGC();
    private TileEntityTreasureChest tier1TreasureChest;

    @Override
    public void render(ItemStack itemStack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
    {
        Item item = itemStack.getItem();

        if (item instanceof BlockItem)
        {
            TileEntity tileentity = null;
            Block block = ((BlockItem)item).getBlock();

            if (block == GCBlocks.TIER_1_TREASURE_CHEST)
            {
                tileentity = this.tier1TreasureChest;
            }

            if (tileentity != null)
            {
                TileEntityRendererDispatcher.instance.renderItem(tileentity, matrixStack, buffer, combinedLight, combinedOverlay);
            }
        }
    }

    public void init()
    {
        this.tier1TreasureChest = new TileEntityTreasureChest();
    }
}