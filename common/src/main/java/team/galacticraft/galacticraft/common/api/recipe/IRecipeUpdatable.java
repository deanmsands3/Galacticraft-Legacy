package team.galacticraft.galacticraft.common.api.recipe;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public interface IRecipeUpdatable
{
    /**
     * Replace all inputs which match ItemStack inputA
     * with a List<ItemStack> (probably representing OreDict output).
     */
    void replaceInput(ItemStack ingredient, List<ItemStack> replacement);

    /**
     * Replace all inputs which are lists containing ItemStack ingredient
     * with simple ItemStack of ingredient.
     *
     * @param ingredient
     */
    void replaceInput(ItemStack ingredient);
}
