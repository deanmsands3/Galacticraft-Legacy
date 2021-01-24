package micdoodle8.mods.galacticraft.planets.mars.schematic;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiSchematicBuggy;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSchematicBuggy;
import micdoodle8.mods.galacticraft.planets.ConfigManagerPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.GuiSchematicCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerSchematicCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SchematicCargoRocket implements ISchematicPage
{
    @Override
    public int getPageID()
    {
        return ConfigManagerPlanets.idSchematicCargoRocket.get();
    }

//    @Override
//    public int getGuiID()
//    {
//        return GuiIdsPlanets.NASA_WORKBENCH_CARGO_ROCKET + Constants.MOD_ID_PLANETS.hashCode();
//    }

    @Override
    public ItemStack getRequiredItem()
    {
        return new ItemStack(MarsItems.CARGO_ROCKET_SCHEMATIC, 1);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public MenuScreens.ScreenConstructor<ContainerSchematicCargoRocket, GuiSchematicCargoRocket> getResultScreen(Player player, BlockPos pos)
    {
        return GuiSchematicCargoRocket::new;
    }

    @Override
    public SimpleMenuProvider getContainerProvider(Player player)
    {
        return new SimpleMenuProvider((w, p, pl) -> new ContainerSchematicCargoRocket(w, p), new TranslatableComponent("container.schematic_cargo_rocket"));
    }

    @Override
    public int compareTo(ISchematicPage o)
    {
        if (this.getPageID() > o.getPageID())
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }
}
