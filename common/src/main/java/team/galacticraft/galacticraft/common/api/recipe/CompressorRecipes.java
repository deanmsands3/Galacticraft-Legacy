package team.galacticraft.galacticraft.common.api.recipe;

import team.galacticraft.galacticraft.common.api.GalacticraftConfigAccess;
import team.galacticraft.galacticraft.common.core.GCItems;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CompressorRecipes
{
    private static final List<Recipe> recipes = new ArrayList<>();
    private static final List<Recipe> recipesAdventure = new ArrayList<>();
    private static boolean adventureOnly = false;
    private static Field adventureFlag;
    private static final boolean flagNotCached = true;
    public static boolean steelIngotsPresent = false;
    public static List<ItemStack> steelRecipeGC;

    public static ShapedRecipesGC addRecipe(ItemStack output, Object... inputList)
    {
        StringBuilder s = new StringBuilder();
        int i = 0;
        int j = 0;
        int k = 0;

        if (inputList[i] instanceof String[])
        {
            String[] astring = (String[]) inputList[i++];

            for (String s1 : astring)
            {
                ++k;
                j = s1.length();
                s.append(s1);
            }
        }
        else
        {
            while (inputList[i] instanceof String)
            {
                String s2 = (String) inputList[i++];
                ++k;
                j = s2.length();
                s.append(s2);
            }
        }

        HashMap<Character, ItemStack> hashmap = new HashMap<>(inputList.length / 2, 1.0F);

        for (; i < inputList.length; i += 2)
        {
            Character character = (Character) inputList[i];
            ItemStack itemstack1 = null;

            if (inputList[i + 1] instanceof Item)
            {
                itemstack1 = new ItemStack((Item) inputList[i + 1]);
            }
            else if (inputList[i + 1] instanceof Block)
            {
//                itemstack1 = new ItemStack((Block) inputList[i + 1], 1, 32767);
                itemstack1 = new ItemStack((Block) inputList[i + 1], 1);
            }
            else if (inputList[i + 1] instanceof ItemStack)
            {
                itemstack1 = (ItemStack) inputList[i + 1];
            }

            hashmap.put(character, itemstack1);
        }

        ItemStack[] aitemstack = new ItemStack[j * k];

        for (int i1 = 0; i1 < j * k; ++i1)
        {
            char c0 = s.charAt(i1);

            if (hashmap.containsKey(c0))
            {
                aitemstack[i1] = hashmap.get(c0).copy();
            }
            else
            {
                aitemstack[i1] = null;
            }
        }

        ShapedRecipesGC shapedRecipes = new ShapedRecipesGC(j, k, aitemstack, output);
        if (!adventureOnly)
        {
            CompressorRecipes.recipes.add(shapedRecipes);
        }
        CompressorRecipes.recipesAdventure.add(shapedRecipes);
        return shapedRecipes;
    }

    public static void addShapelessRecipe(ItemStack par1ItemStack, Object... par2ArrayOfObj)
    {
        ArrayList<Object> arraylist = new ArrayList<>();
        int i = par2ArrayOfObj.length;

        for (Object object1 : par2ArrayOfObj)
        {
            if (object1 instanceof ItemStack)
            {
                arraylist.add(((ItemStack) object1).copy());
            }
            else if (object1 instanceof Item)
            {
                arraylist.add(new ItemStack((Item) object1));
            }
            else if (object1 instanceof String)
            {
                arraylist.add(object1);
            }
            else
            {
                if (!(object1 instanceof Block))
                {
                    throw new RuntimeException("Invalid shapeless compressor recipe!");
                }

                arraylist.add(new ItemStack((Block) object1));
            }
        }

        Recipe toAdd = new ShapelessOreRecipeGC(par1ItemStack, arraylist.toArray());
        if (!adventureOnly)
        {
            CompressorRecipes.recipes.add(toAdd);
        }
        CompressorRecipes.recipesAdventure.add(toAdd);
    }

    public static ShapedRecipesGC addRecipeAdventure(ItemStack output, Object... inputList)
    {
        adventureOnly = true;
        ShapedRecipesGC returnValue = CompressorRecipes.addRecipe(output, inputList);
        adventureOnly = false;
        return returnValue;
    }

    public static void addShapelessAdventure(ItemStack par1ItemStack, Object... par2ArrayOfObj)
    {
        adventureOnly = true;
        CompressorRecipes.addShapelessRecipe(par1ItemStack, par2ArrayOfObj);
        adventureOnly = false;
    }

    public static ItemStack findMatchingRecipe(CraftingContainer inventory, Level par2World)
    {
        int i = 0;
        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack itemstack1 = ItemStack.EMPTY;
        int j;

        for (j = 0; j < inventory.getContainerSize(); ++j)
        {
            ItemStack itemstack2 = inventory.getItem(j);

            if (!itemstack2.isEmpty())
            {
                if (i == 0)
                {
                    itemstack = itemstack2;
                }

                if (i == 1)
                {
                    itemstack1 = itemstack2;
                }

                ++i;
            }
        }

        if (i == 2 && itemstack.getItem() == itemstack1.getItem() && itemstack.getCount() == 1 && itemstack1.getCount() == 1 && itemstack.getItem().isRepairable(itemstack))
        {
            int k = itemstack.getItem().getMaxDamage() - itemstack.getDamageValue();
            int l = itemstack.getItem().getMaxDamage() - itemstack1.getDamageValue();
            int i1 = k + l + itemstack.getItem().getMaxDamage() * 5 / 100;
            int j1 = itemstack.getItem().getMaxDamage() - i1;

            if (j1 < 0)
            {
                j1 = 0;
            }

            ItemStack stack = new ItemStack(itemstack.getItem(), 1);
            stack.setDamageValue(j1);
            return stack;
        }
        else
        {
            List<Recipe> theRecipes = CompressorRecipes.getRecipeList();

            for (j = 0; j < theRecipes.size(); ++j)
            {
                Recipe irecipe = theRecipes.get(j);

                if (irecipe instanceof ShapedRecipesGC && irecipe.matches(inventory, par2World))
                {
                    return irecipe.getResultItem().copy();
                }
                else if (irecipe instanceof ShapelessOreRecipeGC && irecipe.matches(inventory, par2World))
                {
                    return irecipe.getResultItem().copy();
                }
            }

            return ItemStack.EMPTY;
        }
    }

    public static List<Recipe> getRecipeListAll()
    {
        List<Recipe> result = new ArrayList<>(CompressorRecipes.recipesAdventure);
        List<Recipe> endList = getRecipeListHidden(true, true);
        result.removeIf(irecipe -> endList.contains(irecipe));
        Recipe ice = null;
        Item iceItem = new ItemStack(Blocks.ICE).getItem();
        for (Recipe test : result)
        {
            if (test.getResultItem().getItem() == iceItem)
            {
                ice = test;
                break;
            }
        }
        if (ice != null)
        {
            result.remove(ice);
            result.add(ice);
        }
        result.addAll(endList);
        return result;
    }

    public static List<Recipe> getRecipeListHidden(boolean hideSteel, boolean hideAdventure)
    {
        if (!hideAdventure)
        {
            return new ArrayList<Recipe>(0);
        }

        List<Recipe> result = new ArrayList<>(CompressorRecipes.recipesAdventure);
        result.removeIf(irecipe -> CompressorRecipes.recipes.contains(irecipe));
        if (steelIngotsPresent && hideSteel)
        {
            List<Recipe> resultSteelless = new ArrayList<>(result.size());
            for (Recipe recipe : result)
            {
                ItemStack output = recipe.getResultItem();
                if (output == null)
                {
                    continue;  //Intentional ItemStack null check
                }
                if (output.getItem() == GCItems.compressedSteel && recipe instanceof ShapelessOreRecipeGC)
                {
                    if (((ShapelessOreRecipeGC) recipe).matches(steelRecipeGC))
                    {
                        continue;
                    }
                }
                resultSteelless.add(recipe);
            }
            return resultSteelless;
        }
        return result;
    }

    public static List<Recipe> getRecipeList()
    {
        if (GalacticraftConfigAccess.getChallengeRecipes())
        {
            return CompressorRecipes.recipesAdventure;
        }

        // Filter out the GC steel recipe in Hard Mode
        if (steelIngotsPresent && GalacticraftConfigAccess.getHardMode())
        {
            List<Recipe> resultSteelless = new ArrayList<>(CompressorRecipes.recipes.size());
            for (Recipe recipe : CompressorRecipes.recipes)
            {
                ItemStack output = recipe.getResultItem();
                if (output == null)
                {
                    continue;  //Intentional ItemStack null check
                }
                if (output.getItem() == GCItems.compressedSteel && recipe instanceof ShapelessOreRecipeGC)
                {
                    if (((ShapelessOreRecipeGC) recipe).matches(steelRecipeGC))
                    {
                        continue;
                    }
                }
                resultSteelless.add(recipe);
            }
            return resultSteelless;
        }

        return CompressorRecipes.recipes;
    }

    public static List<Recipe> getRecipes(ItemStack match)
    {
        List<Recipe> result = new ArrayList(CompressorRecipes.getRecipeList());
        result.removeIf(irecipe -> !ItemStack.matches(match, irecipe.getResultItem()));
        return result;
    }

    /**
     * Caution: call this BEFORE the JEI plugin registers recipes - or else the removed recipe will still be shown in JEI.
     */
    public static void removeRecipe(ItemStack match)
    {
        CompressorRecipes.recipes.removeIf(irecipe -> ItemStack.matches(match, irecipe.getResultItem()));
        CompressorRecipes.recipesAdventure.removeIf(irecipe -> ItemStack.matches(match, irecipe.getResultItem()));
    }

    public static void replaceRecipeIngredient(ItemStack ingredient, List<ItemStack> replacement)
    {
        if (ingredient == null)
        {
            return;
        }

        for (Recipe recipe : CompressorRecipes.recipesAdventure)
        {
            if (recipe instanceof IRecipeUpdatable)
            {
                ((IRecipeUpdatable) recipe).replaceInput(ingredient, replacement);
            }
        }
    }

    public static void replaceRecipeIngredient(ItemStack ingredient)
    {
        if (ingredient == null)
        {
            return;
        }

        for (Recipe recipe : CompressorRecipes.recipesAdventure)
        {
            if (recipe instanceof IRecipeUpdatable)
            {
                ((IRecipeUpdatable) recipe).replaceInput(ingredient);
            }
        }
    }
}
