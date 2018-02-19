package micdoodle8.mods.galacticraft.planets.mars.client.jei.cargorocket;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeMaker;

import java.util.ArrayList;
import java.util.List;

public class CargoRocketRecipeMaker
{
    public static List<CargoRocketRecipeWrapper> getRecipesList()
    {
        List<CargoRocketRecipeWrapper> recipes = new ArrayList<>();

        int chestCount = -1;
        for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getCargoRocketRecipes())
        {
            int chests = Tier1RocketRecipeMaker.countChests(recipe); 
            if (chests == chestCount)
                continue;
            chestCount = chests;
            CargoRocketRecipeWrapper wrapper = new CargoRocketRecipeWrapper(recipe);
            recipes.add(wrapper);
        }

        return recipes;
    }
}
