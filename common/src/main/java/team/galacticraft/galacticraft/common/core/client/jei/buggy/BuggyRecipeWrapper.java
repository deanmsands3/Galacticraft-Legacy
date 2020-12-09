package team.galacticraft.galacticraft.common.core.client.jei.buggy;
//
//import com.google.common.collect.Lists;
//import mezz.jei.api.ingredients.IIngredients;
//import mezz.jei.api.recipe.IRecipeWrapper;
//import team.galacticraft.galacticraft.common.api.recipe.INasaWorkbenchRecipe;
//import net.minecraft.item.ItemStack;
//
//import javax.annotation.Nonnull;
//
//public class BuggyRecipeWrapper implements IRecipeWrapper
//{
//    @NotNull
//    private final INasaWorkbenchRecipe recipe;
//
//    public BuggyRecipeWrapper(@NotNull INasaWorkbenchRecipe recipe)
//    {
//        this.recipe = recipe;
//    }
//
//    @Override
//    public void getIngredients(IIngredients ingredients)
//    {
//        ingredients.setInputs(ItemStack.class, Lists.newArrayList(recipe.getRecipeInput().values()));
//        ingredients.setOutput(ItemStack.class, recipe.getRecipeOutput());
//    }
//}