package micdoodle8.mods.galacticraft.planets.tags;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class GCPlanetsTags
{
    public static final Tag<Block> DESH_ORES = new BlockTags.Wrapper(new ResourceLocation("forge", "ores/desh"));
    public static final Tag<Block> ILMENITE_ORES = new BlockTags.Wrapper(new ResourceLocation("forge", "ores/ilmenite"));
    public static final Tag<Block> LEAD_STORAGE_BLOCKS = new BlockTags.Wrapper(new ResourceLocation("forge", "storage_blocks/lead"));
    public static final Tag<Block> DESH_STORAGE_BLOCKS = new BlockTags.Wrapper(new ResourceLocation("forge", "storage_blocks/desh"));

    public static final Tag<Item> LEAD_INGOTS = new ItemTags.Wrapper(new ResourceLocation("forge", "ingots/lead"));
    public static final Tag<Item> DESH_INGOTS = new ItemTags.Wrapper(new ResourceLocation("forge", "ingots/desh"));

    public static final Tag<Item> DESH_ORES_ITEM = new ItemTags.Wrapper(new ResourceLocation("forge", "ores/desh"));
    public static final Tag<Item> ILMENITE_ORES_ITEM = new ItemTags.Wrapper(new ResourceLocation("forge", "ores/ilmenite"));
}