package team.galacticraft.galacticraft.common.api.recipe;

import team.galacticraft.galacticraft.common.core.util.RecipeUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import java.util.*;

public class ShapelessOreRecipeGC implements IRecipeUpdatable, Recipe<CraftingContainer>
{
    protected ItemStack output = null;
    protected ArrayList<Object> input = new ArrayList<Object>();

    public ShapelessOreRecipeGC(Block result, Object... recipe)
    {
        this(new ItemStack(result), recipe);
    }

    public ShapelessOreRecipeGC(Item result, Object... recipe)
    {
        this(new ItemStack(result), recipe);
    }

    public ShapelessOreRecipeGC(ItemStack result, Object... recipe)
    {
        output = result.copy();
        for (Object in : recipe)
        {
            if (in instanceof ItemStack)
            {
                input.add(((ItemStack) in).copy());
            }
            else if (in instanceof Item)
            {
                input.add(new ItemStack((Item) in));
            }
            else if (in instanceof Block)
            {
                input.add(new ItemStack((Block) in));
            }
            else if (in instanceof String)
            {
                input.add(ItemTags.getAllTags().getTagOrEmpty(new ResourceLocation("forge", (String) in)));
            }
            else
            {
                StringBuilder ret = new StringBuilder("Invalid compressor shapeless ore recipe: ");
                for (Object tmp : recipe)
                {
                    ret.append(tmp).append(", ");
                }
                ret.append(output);
                throw new RuntimeException(ret.toString());
            }
        }
    }

    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return width * height >= input.size();
    }

    @Override
    public ItemStack getResultItem()
    {
        return output;
    }

    @Override
    public ItemStack getCraftingResult(CraftingContainer var1)
    {
        return output.copy();
    }

    @Override
    public boolean matches(CraftingContainer var1, Level world)
    {
        List<Object> required = new LinkedList<>(input);

        for (int x = 0; x < var1.getContainerSize(); x++)
        {
            ItemStack slot = var1.getItem(x);

            if (!slot.isEmpty())
            {
                boolean inRecipe = false;
                Iterator<Object> req = required.iterator();

                while (req.hasNext())
                {
                    boolean match = false;

                    Object next = req.next();

                    if (next instanceof ItemStack)
                    {
                        Collection<ResourceLocation> tags1 = ItemTags.getAllTags().getMatchingTags(((ItemStack) next).getItem());
                        Collection<ResourceLocation> tags2 = ItemTags.getAllTags().getMatchingTags(slot.getItem());
                        match = false;
                        for (ResourceLocation res : tags1)
                        {
                            if (tags2.contains(res))
                            {
                                match = true;
                                break;
                            }
                        }
//                        match = OreDictionary.itemMatches((ItemStack)next, slot, false);
                    }
                    else if (next instanceof Tag)
                    {
                        match = ((Tag<Item>) next).contains(slot.getItem());
//                        Iterator<ItemStack> itr = ((List<ItemStack>)next).iterator();
//                        while (itr.hasNext() && !match)
//                        {
//                            match = OreDictionary.itemMatches(itr.next(), slot, false);
//                        }
                    }

                    if (match)
                    {
                        inRecipe = true;
                        required.remove(next);
                        break;
                    }
                }

                if (!inRecipe)
                {
                    return false;
                }
            }
        }

        return required.isEmpty();
    }

    public boolean matches(List<ItemStack> var1)
    {
        List<Object> required = new LinkedList<>(input);

        for (ItemStack slot : var1)
        {
            if (slot != null)
            {
                boolean inRecipe = false;
                Iterator<Object> req = required.iterator();

                while (req.hasNext())
                {
                    boolean match = false;

                    Object next = req.next();

                    if (next instanceof ItemStack)
                    {
                        Collection<ResourceLocation> tags1 = ItemTags.getAllTags().getMatchingTags(((ItemStack) next).getItem());
                        Collection<ResourceLocation> tags2 = ItemTags.getAllTags().getMatchingTags(slot.getItem());
                        match = false;
                        for (ResourceLocation res : tags1)
                        {
                            if (tags2.contains(res))
                            {
                                match = true;
                                break;
                            }
                        }
//                        match = OreDictionary.itemMatches((ItemStack)next, slot, false);
                    }
                    else if (next instanceof Tag)
                    {
//                        Iterator<ItemStack> itr = ((List<ItemStack>)next).iterator();
//                        while (itr.hasNext() && !match)
//                        {
//                            match = OreDictionary.itemMatches(itr.next(), slot, false);
//                        }
                        Collection<ResourceLocation> tags = ItemTags.getAllTags().getMatchingTags(slot.getItem());
                        match = false;
                        for (ResourceLocation res : tags)
                        {
                            if (next == ItemTags.getAllTags().getTag(res))
                            {
                                match = true;
                                break;
                            }
                        }
                    }

                    if (match)
                    {
                        inRecipe = true;
                        required.remove(next);
                        break;
                    }
                }

                if (!inRecipe)
                {
                    return false;
                }
            }
        }

        return required.isEmpty();
    }

    public ArrayList<Object> getInput()
    {
        return this.input;
    }

//    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) //getRecipeLeftovers
//    {
//        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
//    }

    @Override
    public void replaceInput(ItemStack inputA, List<ItemStack> inputB)
    {
        for (int i = 0; i < this.input.size(); i++)
        {
            Object test = this.input.get(i);
            if (test instanceof ItemStack && ItemStack.isSame(inputA, (ItemStack) test) && RecipeUtil.areItemStackTagsEqual(inputA, (ItemStack) test))
            {
                this.input.set(i, inputB);
            }
            else if (test instanceof List<?> && itemListContains((List<?>) test, inputA))
            {
                this.input.set(i, inputB);
            }
        }
    }

    @Override
    public void replaceInput(ItemStack inputB)
    {
        for (int i = 0; i < this.input.size(); i++)
        {
            Object test = this.input.get(i);
            if (test instanceof List<?> && itemListContains((List<?>) test, inputB))
            {
                this.input.set(i, inputB);
            }
        }
    }

    private static boolean itemListContains(List<?> test, ItemStack stack)
    {
        for (Object b : test)
        {
            if (b instanceof ItemStack && ItemStack.isSame(stack, (ItemStack) b) && RecipeUtil.areItemStackTagsEqual(stack, (ItemStack) b))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public ResourceLocation getId()
    {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return null;
    }

    @Override
    public RecipeType<?> getType()
    {
        return null;
    }
}