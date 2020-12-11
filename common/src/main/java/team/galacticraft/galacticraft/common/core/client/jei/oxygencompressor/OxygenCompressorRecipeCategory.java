package team.galacticraft.galacticraft.common.core.client.jei.oxygencompressor;
//
//import mezz.jei.api.IGuiHelper;
//import mezz.jei.api.gui.*;
//import mezz.jei.api.ingredients.IIngredients;
//import mezz.jei.api.recipe.BlankRecipeCategory;
//import mezz.jei.api.recipe.IRecipeWrapper;
//import team.galacticraft.galacticraft.common.Constants;
//import team.galacticraft.galacticraft.common.core.GalacticraftCore;
//import team.galacticraft.galacticraft.common.core.client.jei.RecipeCategories;
//import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
//import net.minecraft.client.Minecraft;
//import net.minecraft.util.ResourceLocation;
//
//import org.jetbrains.annotations.NotNull;
//
//public class OxygenCompressorRecipeCategory extends BlankRecipeCategory
//{
//    private static final ResourceLocation compressorTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/compressor.png");
//
//    @NotNull
//    private final IDrawable background;
//    @NotNull
//    private final String localizedName;
//
//    private IDrawableAnimated oxygenBar;
//    private IDrawable oxygenLabel;
//
//    public OxygenCompressorRecipeCategory(IGuiHelper guiHelper)
//    {
//        this.background = guiHelper.createDrawable(compressorTexture, 3, 4, 168, 80);
//        this.localizedName = I18n.get("tile.oxygen_compressor.0");
//
//        IDrawableStatic progressBarDrawableO2 = guiHelper.createDrawable(compressorTexture, 197, 7, 54, 7);
//        this.oxygenBar = guiHelper.createAnimatedDrawable(progressBarDrawableO2, 70, IDrawableAnimated.StartDirection.RIGHT, true);
//        this.oxygenLabel = guiHelper.createDrawable(compressorTexture, 187, 0, 10, 10);
//    }
//
//    @NotNull
//    @Override
//    public String getUid()
//    {
//        return RecipeCategories.OXYGEN_COMPRESSOR_ID;
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
//        return this.background;
//    }
//
//    @Override
//    public void drawExtras(@NotNull Minecraft minecraft)
//    {
//        this.oxygenLabel.draw(minecraft, 97, 15);
//        this.oxygenBar.draw(minecraft, 110, 16);
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients)
//    {
//        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();
//
//        itemstacks.init(0, false, 129, 61);
//
//        if (recipeWrapper instanceof OxygenCompressorRecipeWrapper)
//        {
//            itemstacks.set(ingredients);
//        }
//    }
//
//    @Override
//    public String getModName()
//    {
//        return GalacticraftCore.NAME;
//    }
//}
