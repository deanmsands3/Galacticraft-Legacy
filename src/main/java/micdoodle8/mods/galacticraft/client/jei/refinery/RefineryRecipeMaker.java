package micdoodle8.mods.galacticraft.client.jei.refinery;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.init.GCItems;

public class RefineryRecipeMaker
{
    public static List<RefineryRecipeWrapper> getRecipesList()
    {
        List<RefineryRecipeWrapper> recipes = new ArrayList<>();

        recipes.add(new RefineryRecipeWrapper(new ItemStack(GCItems.oilCanister, 1, 1), new ItemStack(GCItems.fuelCanister, 1, 1)));
        recipes.add(new RefineryRecipeWrapper(new ItemStack(GCItems.bucketOil), new ItemStack(GCItems.fuelCanister, 1, 1)));
        recipes.add(new RefineryRecipeWrapper(new ItemStack(GCItems.oilCanister, 1, 1), new ItemStack(GCItems.bucketFuel)));
        recipes.add(new RefineryRecipeWrapper(new ItemStack(GCItems.bucketOil), new ItemStack(GCItems.bucketFuel)));

        return recipes;
    }
}
