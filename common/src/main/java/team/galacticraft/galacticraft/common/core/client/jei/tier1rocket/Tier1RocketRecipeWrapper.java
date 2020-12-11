package team.galacticraft.galacticraft.common.core.client.jei.tier1rocket;
//
//import org.jetbrains.annotations.NotNull;
//
//import com.google.common.collect.Lists;
//import mezz.jei.api.ingredients.IIngredients;
//import mezz.jei.api.recipe.IRecipeWrapper;
//import team.galacticraft.galacticraft.common.api.recipe.INasaWorkbenchRecipe;
//import net.minecraft.item.ItemStack;
//
//public class Tier1RocketRecipeWrapper implements IRecipeWrapper
//{
//    @NotNull
//    private final INasaWorkbenchRecipe recipe;
//
//    public Tier1RocketRecipeWrapper(@NotNull INasaWorkbenchRecipe recipe)
//    {
//        this.recipe = recipe;
//    }
//
//    @Override
//    public void getIngredients(IIngredients ingredients)
//    {
//        ingredients.setInputs(ItemStack.class, Lists.newArrayList(recipe.getRecipeInput().values()));
//        ingredients.setOutput(ItemStack.class, this.recipe.getRecipeOutput());
//    }
//}