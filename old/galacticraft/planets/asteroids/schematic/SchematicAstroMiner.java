package micdoodle8.mods.galacticraft.planets.asteroids.schematic;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiSchematicBuggy;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSchematicBuggy;
import micdoodle8.mods.galacticraft.planets.ConfigManagerPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.client.gui.GuiSchematicAstroMinerDock;
import micdoodle8.mods.galacticraft.planets.asteroids.inventory.ContainerSchematicAstroMiner;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SchematicAstroMiner implements ISchematicPage
{
    @Override
    public int getPageID()
    {
        return ConfigManagerPlanets.idSchematicRocketT3.get() + 1;
    }

//    @Override
//    public int getGuiID()
//    {
//        return GuiIdsPlanets.NASA_WORKBENCH_ASTRO_MINER + Constants.MOD_ID_PLANETS.hashCode();
//    }

    @Override
    public ItemStack getRequiredItem()
    {
        return new ItemStack(MarsItems.ASTRO_MINER_SCHEMATIC);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public MenuScreens.ScreenConstructor<ContainerSchematicAstroMiner, GuiSchematicAstroMinerDock> getResultScreen(Player player, BlockPos pos)
    {
        return GuiSchematicAstroMinerDock::new;
    }

    @Override
    public SimpleMenuProvider getContainerProvider(Player player)
    {
        return new SimpleMenuProvider((w, p, pl) -> new ContainerSchematicAstroMiner(w, p), new TranslatableComponent("container.schematic_astro_miner"));
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
