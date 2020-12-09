package team.galacticraft.galacticraft.common.core.client.jei.ingotcompressor;
//
//import java.util.List;
//
//import mezz.jei.api.IGuiHelper;
//import mezz.jei.api.gui.*;
//import mezz.jei.api.ingredients.IIngredients;
//import mezz.jei.api.recipe.IRecipeCategory;
//import mezz.jei.api.recipe.IRecipeWrapper;
//import team.galacticraft.galacticraft.core.Constants;
//import team.galacticraft.galacticraft.core.GalacticraftCore;
//import team.galacticraft.galacticraft.core.client.jei.GalacticraftJEI;
//import team.galacticraft.galacticraft.core.client.jei.RecipeCategories;
//import team.galacticraft.galacticraft.core.util.ConfigManagerCore;
//import team.galacticraft.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.client.Minecraft;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ResourceLocation;
//
//import javax.annotation.Nonnull;
//
//public class IngotCompressorRecipeCategory implements IRecipeCategory
//{
//    private static final ResourceLocation compressorTex = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/ingot_compressor.png");
//    private static final ResourceLocation compressorTexBlank = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/ingot_compressor_blank.png");
//
//    @NotNull
//    private final IDrawable background;
//    @NotNull
//    private final IDrawable backgroundBlank;
//    @NotNull
//    private final String localizedName;
//    @NotNull
//    private final IDrawableAnimated progressBar;
//
//    private boolean drawNothing = false;
//
//    public IngotCompressorRecipeCategory(IGuiHelper guiHelper)
//    {
//        this.background = guiHelper.createDrawable(compressorTex, 18, 17, 137, 78);
//        this.backgroundBlank = guiHelper.createDrawable(compressorTexBlank, 18, 17, 137, 78);
//        this.localizedName = I18n.get("tile.machine.3");
//
//        IDrawableStatic progressBarDrawable = guiHelper.createDrawable(compressorTex, 176, 13, 52, 17);
//        this.progressBar = guiHelper.createAnimatedDrawable(progressBarDrawable, 70, IDrawableAnimated.StartDirection.LEFT, false);
//    }
//
//    @NotNull
//    @Override
//    public String getUid()
//    {
//        return RecipeCategories.INGOT_COMPRESSOR_ID;
//    }
//
//    @NotNull
//    @Override
//    public String getTitle()
//    {
//        return this.localizedName;
//    }
//
//    @NotNull
//    @Override
//    public IDrawable getBackground()
//    {
//        if (this.drawNothing)
//        {
//            return this.backgroundBlank;
//        }
//        return this.background;
//    }
//
//    @Override
//    public void drawExtras(@NotNull Minecraft minecraft)
//    {
//        if (!this.drawNothing) this.progressBar.draw(minecraft, 59, 19);
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients)
//    {
//        this.drawNothing = GalacticraftJEI.hidden.contains(recipeWrapper);
//        if (this.drawNothing) return;
//
//        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();
//
//        for (int j = 0; j < 9; j++)
//        {
//            itemstacks.init(j, true, j % 3 * 18, j / 3 * 18);
//        }
//
//        itemstacks.init(9, false, 119, 20);
//
//        if (ConfigManagerCore.quickMode.get())
//        {
//            List<ItemStack> output = ingredients.getOutputs(ItemStack.class).get(0);
//            ItemStack stackOutput = output.get(0);
//            if (stackOutput.getItem().getUnlocalizedName(stackOutput).contains("compressed"))
//            {
//                ItemStack stackDoubled = stackOutput.copy();
//                stackDoubled.setCount(stackOutput.getCount() * 2);
//                output.set(0, stackDoubled);
//            }
//        }
//
//        itemstacks.set(ingredients);
//    }
//
//    @Override
//    public String getModName()
//    {
//        return GalacticraftCore.NAME;
//    }
//}
