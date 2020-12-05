package team.galacticraft.galacticraft.common.api.recipe;

import org.jetbrains.annotations.NotNull;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;

public interface INasaWorkbenchRecipe
{
    boolean matches(Container inventory);

    int getRecipeSize();

    @NotNull
    ItemStack getRecipeOutput();

    HashMap<Integer, ItemStack> getRecipeInput();
}
