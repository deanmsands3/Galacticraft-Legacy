package team.galacticraft.galacticraft.common.core.client.jei.refinery;
//
//import mezz.jei.api.ingredients.IIngredients;
//import mezz.jei.api.recipe.IRecipeWrapper;
//import net.minecraft.item.ItemStack;
//import javax.annotation.Nonnull;
//
//public class RefineryRecipeWrapper implements IRecipeWrapper
//{
//    @NotNull
//    private final ItemStack input;
//    @NotNull
//    private final ItemStack output;
//
//    public RefineryRecipeWrapper(@NotNull ItemStack input, @NotNull ItemStack output)
//    {
//        this.input = input;
//        this.output = output;
//    }
//
//    @Override
//    public void getIngredients(IIngredients ingredients)
//    {
//        ingredients.setInput(ItemStack.class, this.input);
//        ingredients.setOutput(ItemStack.class, this.output);
//    }
//}