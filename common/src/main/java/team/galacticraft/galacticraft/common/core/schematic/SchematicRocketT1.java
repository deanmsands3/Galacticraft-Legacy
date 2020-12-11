package team.galacticraft.galacticraft.common.core.schematic;

import team.galacticraft.galacticraft.common.api.recipe.SchematicPage;
import team.galacticraft.galacticraft.common.core.client.gui.container.GuiSchematicTier1Rocket;
import team.galacticraft.galacticraft.common.core.inventory.ContainerSchematicTier1Rocket;
import team.galacticraft.galacticraft.common.core.util.ConfigManagerCore;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SchematicRocketT1 extends SchematicPage
{
    @Override
    public int getPageID()
    {
        return ConfigManagerCore.idSchematicRocketT1.get();
    }

//    @Override
//    public int getGuiID()
//    {
//        return GuiIdsCore.NASA_WORKBENCH_ROCKET;
//    }

    @Override
    public ItemStack getRequiredItem()
    {
        return null;  //This null is OK, it's used only as a flag by the calling code in SchematicRegistry
    }

    @Environment(EnvType.CLIENT)
    @Override
    public MenuScreens.ScreenConstructor<ContainerSchematicTier1Rocket, GuiSchematicTier1Rocket> getResultScreen(Player player, BlockPos pos)
    {
        return GuiSchematicTier1Rocket::new;
    }

    @Override
    public SimpleMenuProvider getContainerProvider(Player player)
    {
        return new SimpleMenuProvider((w, p, pl) -> new ContainerSchematicTier1Rocket(w, p), new TranslatableComponent("container.schematic_add"));
    }
}
