package micdoodle8.mods.galacticraft.planets.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTreasureChestMars;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityTreasureChestVenus;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemStackTileEntityRendererPlanets extends BlockEntityWithoutLevelRenderer
{
    public static final ItemStackTileEntityRendererPlanets INSTANCE = new ItemStackTileEntityRendererPlanets();
    private TileEntityTreasureChestMars tier2TreasureChest;
    private TileEntityTreasureChestVenus tier3TreasureChest;

    @Override
    public void renderByItem(ItemStack itemStack, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay)
    {
        Item item = itemStack.getItem();

        if (item instanceof BlockItem)
        {
            BlockEntity tileentity = null;
            Block block = ((BlockItem)item).getBlock();

            if (block == MarsBlocks.TIER_2_TREASURE_CHEST)
            {
                tileentity = this.tier2TreasureChest;
            }
            else if (block == VenusBlocks.TIER_3_TREASURE_CHEST)
            {
                tileentity = this.tier3TreasureChest;
            }

            if (tileentity != null)
            {
                BlockEntityRenderDispatcher.instance.renderItem(tileentity, matrixStack, buffer, combinedLight, combinedOverlay);
            }
        }
    }

    public void init()
    {
        this.tier2TreasureChest = new TileEntityTreasureChestMars();
        this.tier3TreasureChest = new TileEntityTreasureChestVenus();
    }
}