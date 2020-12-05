package micdoodle8.mods.galacticraft.api.recipe;

import javax.annotation.Nonnull;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import java.util.HashMap;

public interface INasaWorkbenchRecipe
{
    boolean matches(Container inventory);

    int getRecipeSize();

    @Nonnull
    ItemStack getRecipeOutput();

    HashMap<Integer, ItemStack> getRecipeInput();
}
