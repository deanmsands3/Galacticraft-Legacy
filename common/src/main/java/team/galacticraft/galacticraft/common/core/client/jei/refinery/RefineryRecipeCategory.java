package team.galacticraft.galacticraft.common.core.client.jei.refinery;
//
//import mezz.jei.api.IGuiHelper;
//import mezz.jei.api.gui.*;
//import mezz.jei.api.ingredients.IIngredients;
//import mezz.jei.api.recipe.IRecipeCategory;
//import mezz.jei.api.recipe.IRecipeWrapper;
//import team.galacticraft.galacticraft.core.Constants;
//import team.galacticraft.galacticraft.core.GalacticraftCore;
//import team.galacticraft.galacticraft.core.client.jei.RecipeCategories;
//import team.galacticraft.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.client.Minecraft;
//import net.minecraft.util.ResourceLocation;
//
//import javax.annotation.Nonnull;
//
//public class RefineryRecipeCategory implements IRecipeCategory
//{
//    private static final ResourceLocation refineryGuiTex = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/refinery_recipe.png");
//
//    @NotNull
//    private final IDrawable background;
//    @NotNull
//    private final String localizedName;
//    @NotNull
//    private final IDrawableAnimated oilBar;
//    @NotNull
//    private final IDrawableAnimated fuelBar;
//
//    public RefineryRecipeCategory(IGuiHelper guiHelper)
//    {
//        this.background = guiHelper.createDrawable(refineryGuiTex, 3, 4, 168, 64);
//        this.localizedName = I18n.get("tile.refinery");
//
//        IDrawableStatic progressBarDrawableOil = guiHelper.createDrawable(refineryGuiTex, 176, 0, 16, 38);
//        this.oilBar = guiHelper.createAnimatedDrawable(progressBarDrawableOil, 70, IDrawableAnimated.StartDirection.TOP, true);
//        IDrawableStatic progressBarDrawableFuel = guiHelper.createDrawable(refineryGuiTex, 192, 0, 16, 38);
//        this.fuelBar = guiHelper.createAnimatedDrawable(progressBarDrawableFuel, 70, IDrawableAnimated.StartDirection.BOTTOM, false);
//    }
//
//    @NotNull
//    @Override
//    public String getUid()
//    {
//        return RecipeCategories.REFINERY_ID;
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
//        this.oilBar.draw(minecraft, 40, 24);
//        this.fuelBar.draw(minecraft, 114, 24);
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients)
//    {
//        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();
//
//        itemstacks.init(0, true, 39, 2);
//        itemstacks.init(1, false, 113, 2);
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
