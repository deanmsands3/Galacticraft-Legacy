package micdoodle8.mods.galacticraft.core.schematic;

import micdoodle8.mods.galacticraft.api.recipe.SchematicPage;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiSchematicBuggy;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSchematicBuggy;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SchematicMoonBuggy extends SchematicPage
{
    @Override
    public int getPageID()
    {
        return ConfigManagerCore.idSchematicMoonBuggy.get();
    }

//    @Override
//    public int getGuiID()
//    {
//        return GuiIdsCore.NASA_WORKBENCH_BUGGY;
//    }

    @Override
    public ItemStack getRequiredItem()
    {
        return new ItemStack(GCItems.MOON_BUGGY_SCHEMATIC, 1);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public MenuScreens.ScreenConstructor<ContainerSchematicBuggy, GuiSchematicBuggy> getResultScreen(Player player, BlockPos pos)
    {
        return GuiSchematicBuggy::new;
    }

    @Override
    public SimpleMenuProvider getContainerProvider(Player player)
    {
        return new SimpleMenuProvider((w, p, pl) -> new ContainerSchematicBuggy(w, p), new TranslatableComponent("container.buggy"));
    }
}
