package team.galacticraft.galacticraft.common.core.client.jei.oxygencompressor;
//
//import mezz.jei.api.ingredients.IIngredients;
//import mezz.jei.api.recipe.BlankRecipeWrapper;
//import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
//import net.minecraft.item.ItemStack;
//
//import org.jetbrains.annotations.NotNull;
//
//public class OxygenCompressorRecipeWrapper extends BlankRecipeWrapper implements ICraftingRecipeWrapper
//{
//    @NotNull
//    private final ItemStack output;
//
//    public OxygenCompressorRecipeWrapper(@NotNull ItemStack output)
//    {
//        this.output = output;
//    }
//
//    @Override
//    public void getIngredients(IIngredients ingredients)
//    {
//        ingredients.setOutput(ItemStack.class, this.output);
//    }
//}