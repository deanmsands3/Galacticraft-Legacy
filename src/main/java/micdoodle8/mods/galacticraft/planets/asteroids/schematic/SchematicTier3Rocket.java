package micdoodle8.mods.galacticraft.planets.asteroids.schematic;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiSchematicBuggy;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSchematicBuggy;
import micdoodle8.mods.galacticraft.planets.ConfigManagerPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.client.gui.GuiSchematicTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.inventory.ContainerSchematicTier3Rocket;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SchematicTier3Rocket implements ISchematicPage
{
    @Override
    public int getPageID()
    {
        return ConfigManagerPlanets.idSchematicRocketT3.get();
    }

//    @Override
//    public int getGuiID()
//    {
//        return GuiIdsPlanets.NASA_WORKBENCH_TIER_3_ROCKET + Constants.MOD_ID_PLANETS.hashCode();
//    }

    @Override
    public ItemStack getRequiredItem()
    {
        return new ItemStack(MarsItems.schematicRocketT3);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public MenuScreens.ScreenConstructor<ContainerSchematicTier3Rocket, GuiSchematicTier3Rocket> getResultScreen(Player player, BlockPos pos)
    {
        return GuiSchematicTier3Rocket::new;
    }

    @Override
    public SimpleMenuProvider getContainerProvider(Player player)
    {
        return new SimpleMenuProvider((w, p, pl) -> new ContainerSchematicTier3Rocket(w, p), new TranslatableComponent("container.schematic_tier3_rocket"));
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
