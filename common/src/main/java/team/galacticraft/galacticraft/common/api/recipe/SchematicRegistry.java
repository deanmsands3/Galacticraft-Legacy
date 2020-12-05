package team.galacticraft.galacticraft.common.api.recipe;

import team.galacticraft.galacticraft.common.api.recipe.SchematicEvent.FlipPage;
import team.galacticraft.galacticraft.common.api.recipe.SchematicEvent.Unlock;
import team.galacticraft.galacticraft.common.core.util.GCLog;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;

public class SchematicRegistry
{
    public static ArrayList<ISchematicPage> schematicRecipes = new ArrayList<ISchematicPage>();
    public static ArrayList<ItemStack> schematicItems = new ArrayList<>();
    public static ArrayList<ResourceLocation> textures = new ArrayList<>();

    /**
     * Register a new schematic page
     */
    public static void registerSchematicRecipe(ISchematicPage page)
    {
        if (!SchematicRegistry.schematicRecipes.contains(page))
        {
            SchematicRegistry.schematicRecipes.add(page);
        }
    }

    /**
     * Finds the recipe for the given itemstack
     *
     * @param stack the itemstack to be searched with
     * @return the recipe that requires the provided itemstack
     */
    public static ISchematicPage getMatchingRecipeForItemStack(ItemStack stack)
    {
        for (final ISchematicPage schematic : SchematicRegistry.schematicRecipes)
        {
            final ItemStack requiredItem = schematic.getRequiredItem();

            if (requiredItem != null && stack != null && requiredItem.sameItem(stack))
            {
                return schematic;
            }
        }

        return null;
    }

    /**
     * Finds the recipe for the given recipe ID
     *
     * @param id the ID to be searched with
     * @return the recipe that has and ID equal to the one provided
     */
    public static ISchematicPage getMatchingRecipeForID(int id)
    {
        for (final ISchematicPage schematic : SchematicRegistry.schematicRecipes)
        {
            if (schematic.getPageID() == id)
            {
                return schematic;
            }
        }

        return null;
    }

    /**
     * Called when a player unlocks a page. Used internally.
     *
     * @param player the player that unlocked the schematic
     * @param page   the schematic page to be unlocked
     */
    public static void addUnlockedPage(ServerPlayer player, ISchematicPage page)
    {
        // Used internally to add page to player's list of unlocked schematics.
        // No need to subscribe to this event
        if (page != null)
        {
            MinecraftForge.EVENT_BUS.post(new Unlock(player, page));
        }
    }

    /**
     * Called when a player unlocks a page. Used internally.
     *
     * @param player the player that unlocked the schematic
     * @param stack  the itemstack the player has provided
     * @return the schematic page that was unlocked
     */
    public static ISchematicPage unlockNewPage(ServerPlayer player, ItemStack stack)
    {
        if (stack != null)
        {
            final ISchematicPage schematic = SchematicRegistry.getMatchingRecipeForItemStack(stack);

            if (schematic != null)
            {
                SchematicRegistry.addUnlockedPage(player, schematic);

                return schematic;
            }
        }

        return null;
    }

    /**
     * Finds the correct schematic when player presses NEXT
     *
     * @param currentIndex the current index of unlocked schematics the player is viewing
     * @return the schematic page that will be shown when the player clicks NEXT
     */
    @Environment(EnvType.CLIENT)
    public static void flipToNextPage(Screen cs, int currentIndex)
    {
        Minecraft.getInstance().screen = null;

        // Used internally inside Galacticraft to flip to the next page. No need
        // to subscribe to this event.
        MinecraftForge.EVENT_BUS.post(new FlipPage(cs, null, currentIndex, 1));
    }

    /**
     * Finds the correct schematic when player presses BACK
     *
     * @param currentIndex the current index of unlocked schematics the player is viewing
     * @return the schematic page that will be shown when the player clicks BACK
     */
    @Environment(EnvType.CLIENT)
    public static void flipToPrevPage(Screen cs, int currentIndex)
    {
        Minecraft.getInstance().screen = null;

        // Used internally inside Galacticraft to flip to the last page. No need
        // to subscribe to this event.
        MinecraftForge.EVENT_BUS.post(new FlipPage(cs, null, currentIndex, -1));
    }

    public static void registerTexture(ResourceLocation loc)
    {
        textures.add(loc);
    }

    public static ResourceLocation getSchematicTexture(int index)
    {
        if (index < textures.size())
        {
            return textures.get(index);
        }

        GCLog.debug("couldn't find render texture for " + index);
        return textures.get(0);
    }

    public static int registerSchematicItem(ItemStack item)
    {
        int index = schematicItems.size();
        schematicItems.add(item);
        return index;
    }

    public static ItemStack getSchematicItem(int index)
    {
        if (index < schematicItems.size())
        {
            return schematicItems.get(index).copy();
        }

        GCLog.debug("couldn't find schematic item for " + index);
        return schematicItems.get(0).copy();
    }
}
