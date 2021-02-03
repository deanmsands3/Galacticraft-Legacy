package micdoodle8.mods.galacticraft.core.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemStackTileEntityRendererGC extends BlockEntityWithoutLevelRenderer
{
    public static final ItemStackTileEntityRendererGC INSTANCE = new ItemStackTileEntityRendererGC();
    private TileEntityTreasureChest tier1TreasureChest;

    @Override
    public void renderByItem(ItemStack itemStack, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay)
    {
        Item item = itemStack.getItem();

        if (item instanceof BlockItem)
        {
            BlockEntity tileentity = null;
            Block block = ((BlockItem)item).getBlock();

            if (block == GCBlocks.TIER_1_TREASURE_CHEST)
            {
                tileentity = this.tier1TreasureChest;
            }

            if (tileentity != null)
            {
                BlockEntityRenderDispatcher.instance.renderItem(tileentity, matrixStack, buffer, combinedLight, combinedOverlay);
            }
        }
    }

    public void init()
    {
        this.tier1TreasureChest = new TileEntityTreasureChest();
    }
}