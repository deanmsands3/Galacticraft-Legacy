package micdoodle8.mods.galacticraft.planets.mars.client.jei.tier2rocket;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeMaker;

import java.util.ArrayList;
import java.util.List;

public class Tier2RocketRecipeMaker
{
    public static List<Tier2RocketRecipeWrapper> getRecipesList()
    {
        List<Tier2RocketRecipeWrapper> recipes = new ArrayList<>();

        int chestCount = -1;
        for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getRocketT2Recipes())
        {
            int chests = Tier1RocketRecipeMaker.countChests(recipe); 
            if (chests == chestCount)
                continue;
            chestCount = chests;
            Tier2RocketRecipeWrapper wrapper = new Tier2RocketRecipeWrapper(recipe);
            recipes.add(wrapper);
        }

        return recipes;
    }
}
