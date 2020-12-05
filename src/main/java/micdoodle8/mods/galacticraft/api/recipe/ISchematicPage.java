package micdoodle8.mods.galacticraft.api.recipe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Schematic page to be added to NASA Workbench
 */
public interface ISchematicPage extends Comparable<ISchematicPage>
{
    /**
     * Get the page ID. Make it configurable since it has to be unique between
     * other Galacticraft addons. Determines order of schematics.
     */
    int getPageID();

    /**
     * The item required to unlock this schematic. The item class must implement
     * ISchematicItem, since it goes in the NASA Workbench unlock slot.
     */
    ItemStack getRequiredItem();

    /**
     * The resulting client-LogicalSide GUI for this page
     *
     * @param player The player opening this GUI
     * @param pos    Coordinates of the NASA Workbench
     * @return the GUI to be opened with this schematic
     */
    @Environment(EnvType.CLIENT)
    <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> MenuScreens.ScreenConstructor<M, U> getResultScreen(Player player, BlockPos pos);

    /**
     * The resulting container for this page
     *
     * @param player The player opening this GUI
     * @return the container to be opened with this schematic
     */
    SimpleMenuProvider getContainerProvider(Player player);
}
