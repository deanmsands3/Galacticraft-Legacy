package team.galacticraft.galacticraft.common.core.client.jei.circuitfabricator;
//
//import mezz.jei.api.ingredients.IIngredients;
//import mezz.jei.api.recipe.IRecipeWrapper;
//import net.minecraft.item.ItemStack;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.List;
//
//public class CircuitFabricatorRecipeWrapper implements IRecipeWrapper
//{
//    @NotNull
//    private final List<Object> input;
//    @NotNull
//    private final ItemStack output;
//
//    public CircuitFabricatorRecipeWrapper(@NotNull List<Object> objects, @NotNull ItemStack output)
//    {
//        this.input = objects;
//        this.output = output;
//    }
//
//    @Override
//    public void getIngredients(IIngredients ingredients)
//    {
//        ingredients.setInputs(ItemStack.class, this.input);
//        ingredients.setOutput(ItemStack.class, this.output);
//    }
//
//    public boolean equals(Object o)
//    {
//        if (o instanceof CircuitFabricatorRecipeWrapper)
//        {
//            CircuitFabricatorRecipeWrapper match = (CircuitFabricatorRecipeWrapper)o;
//            if (!ItemStack.areItemStacksEqual(match.output, this.output))
//                return false;
//            for (int i = 0; i < this.input.size(); i++)
//            {
//                Object a = this.input.get(i);
//                Object b = match.input.get(i);
//                if (a == null && b == null)
//                    continue;
//
//                if (a instanceof ItemStack)
//                {
//                    if (!(b instanceof ItemStack))
//                        return false;
//                    if (!ItemStack.areItemStacksEqual((ItemStack) a, (ItemStack) b))
//                        return false;
//                }
//                else if (a instanceof List<?>)
//                {
//                    if (!(b instanceof List<?>))
//                        return false;
//                    List aa = ((List)a);
//                    List bb = ((List)b);
//                    if (aa.size() != bb.size())
//                        return false;
//                    for (int j = 0; j < aa.size(); j++)
//                    {
//                        ItemStack c = (ItemStack) aa.get(j);
//                        ItemStack d = (ItemStack) bb.get(j);
//                        if (!ItemStack.areItemStacksEqual((ItemStack) c, (ItemStack) d))
//                            return false;
//                    }
//                }
//            }
//            return true;
//        }
//        return false;
//    }
//}
