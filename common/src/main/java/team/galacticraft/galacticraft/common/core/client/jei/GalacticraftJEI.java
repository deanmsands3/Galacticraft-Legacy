package team.galacticraft.galacticraft.common.core.client.jei;
//
//import mezz.jei.api.BlankModPlugin;
//import mezz.jei.api.IGuiHelper;
//import mezz.jei.api.IJeiRuntime;
//import mezz.jei.api.IModRegistry;
//import mezz.jei.api.IRecipeRegistry;
//import mezz.jei.api.JEIPlugin;
//import mezz.jei.api.recipe.IRecipeCategory;
//import mezz.jei.api.recipe.IRecipeCategoryRegistration;
//import mezz.jei.api.recipe.IRecipeWrapper;
//import mezz.jei.api.recipe.IRecipeWrapperFactory;
//import mezz.jei.api.recipe.IStackHelper;
//import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
//import team.galacticraft.galacticraft.common.api.recipe.CompressorRecipes;
//import team.galacticraft.galacticraft.common.api.recipe.INasaWorkbenchRecipe;
//import team.galacticraft.galacticraft.common.api.recipe.ShapedRecipesGC;
//import team.galacticraft.galacticraft.common.api.recipe.ShapelessOreRecipeGC;
//import team.galacticraft.galacticraft.common.core.GCBlocks;
//import team.galacticraft.galacticraft.common.core.GCItems;
//import team.galacticraft.galacticraft.common.core.client.jei.buggy.BuggyRecipeCategory;
//import team.galacticraft.galacticraft.common.core.client.jei.buggy.BuggyRecipeMaker;
//import team.galacticraft.galacticraft.common.core.client.jei.buggy.BuggyRecipeWrapper;
//import team.galacticraft.galacticraft.common.core.client.jei.circuitfabricator.CircuitFabricatorRecipeCategory;
//import team.galacticraft.galacticraft.common.core.client.jei.circuitfabricator.CircuitFabricatorRecipeMaker;
//import team.galacticraft.galacticraft.common.core.client.jei.circuitfabricator.CircuitFabricatorRecipeWrapper;
//import team.galacticraft.galacticraft.common.core.client.jei.ingotcompressor.IngotCompressorRecipeCategory;
//import team.galacticraft.galacticraft.common.core.client.jei.ingotcompressor.IngotCompressorShapedRecipeWrapper;
//import team.galacticraft.galacticraft.common.core.client.jei.ingotcompressor.IngotCompressorShapelessRecipeWrapper;
//import team.galacticraft.galacticraft.common.core.client.jei.oxygencompressor.OxygenCompressorRecipeCategory;
//import team.galacticraft.galacticraft.common.core.client.jei.oxygencompressor.OxygenCompressorRecipeMaker;
//import team.galacticraft.galacticraft.common.core.client.jei.oxygencompressor.OxygenCompressorRecipeWrapper;
//import team.galacticraft.galacticraft.common.core.client.jei.refinery.RefineryRecipeCategory;
//import team.galacticraft.galacticraft.common.core.client.jei.refinery.RefineryRecipeMaker;
//import team.galacticraft.galacticraft.common.core.client.jei.refinery.RefineryRecipeWrapper;
//import team.galacticraft.galacticraft.common.core.client.jei.tier1rocket.Tier1RocketRecipeCategory;
//import team.galacticraft.galacticraft.common.core.client.jei.tier1rocket.Tier1RocketRecipeMaker;
//import team.galacticraft.galacticraft.common.core.client.jei.tier1rocket.Tier1RocketRecipeWrapper;
//import team.galacticraft.galacticraft.common.core.recipe.ShapedRecipeNBT;
//import team.galacticraft.galacticraft.common.core.util.CompatibilityManager;
//import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.crafting.IRecipe;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import org.jetbrains.annotations.NotNull;
//
//@JEIPlugin
//public class GalacticraftJEI extends BlankModPlugin
//{
//    private static IModRegistry registryCached = null;
//    private static IRecipeRegistry recipesCached = null;
//
//    private static boolean hiddenSteel = false;
//    private static boolean hiddenAdventure = false;
//    public static List<IRecipeWrapper> hidden = new LinkedList<>();
//    private static IRecipeCategory ingotCompressorCategory;
//
//    @Override
//    public void register(@NotNull IModRegistry registry)
//    {
//        registryCached = registry;
//        IStackHelper stackHelper = registry.getJeiHelpers().getStackHelper();
//
//        registry.handleRecipes(INasaWorkbenchRecipe.class, Tier1RocketRecipeWrapper::new, RecipeCategories.ROCKET_T1_ID);
//        registry.handleRecipes(INasaWorkbenchRecipe.class, BuggyRecipeWrapper::new, RecipeCategories.BUGGY_ID);
//        registry.handleRecipes(CircuitFabricatorRecipeWrapper.class, recipe -> recipe, RecipeCategories.CIRCUIT_FABRICATOR_ID);
//        registry.handleRecipes(ShapedRecipesGC.class, IngotCompressorShapedRecipeWrapper::new, RecipeCategories.INGOT_COMPRESSOR_ID);
//        registry.handleRecipes(ShapelessOreRecipeGC.class, new IRecipeWrapperFactory<ShapelessOreRecipeGC>() {
//        	@Override public IRecipeWrapper getRecipeWrapper(ShapelessOreRecipeGC recipe) { return new IngotCompressorShapelessRecipeWrapper(stackHelper, recipe); }
//        		}, RecipeCategories.INGOT_COMPRESSOR_ID);
//        registry.handleRecipes(RefineryRecipeWrapper.class, recipe -> recipe, RecipeCategories.REFINERY_ID);
//        registry.handleRecipes(OxygenCompressorRecipeWrapper.class, recipe -> recipe, RecipeCategories.OXYGEN_COMPRESSOR_ID);
//        registry.handleRecipes(ShapedRecipeNBT.class, NBTSensitiveShapedRecipeWrapper::new, VanillaRecipeCategoryUid.CRAFTING);
//
//        registry.addRecipes(Tier1RocketRecipeMaker.getRecipesList(), RecipeCategories.ROCKET_T1_ID);
//        registry.addRecipes(BuggyRecipeMaker.getRecipesList(), RecipeCategories.BUGGY_ID);
//        registry.addRecipes(CircuitFabricatorRecipeMaker.getRecipesList(), RecipeCategories.CIRCUIT_FABRICATOR_ID);
//        registry.addRecipes(CompressorRecipes.getRecipeListAll(), RecipeCategories.INGOT_COMPRESSOR_ID);
//        registry.addRecipes(OxygenCompressorRecipeMaker.getRecipesList(), RecipeCategories.OXYGEN_COMPRESSOR_ID);
//        registry.addRecipes(RefineryRecipeMaker.getRecipesList(), RecipeCategories.REFINERY_ID);
//
//        registry.addRecipeCatalyst(new ItemStack(GCBlocks.nasaWorkbench), RecipeCategories.ROCKET_T1_ID, RecipeCategories.BUGGY_ID);
//        registry.addRecipeCatalyst(new ItemStack(GCBlocks.machineBase2, 1, 4), RecipeCategories.CIRCUIT_FABRICATOR_ID);
//        registry.addRecipeCatalyst(new ItemStack(GCBlocks.machineBase, 1, 12), RecipeCategories.INGOT_COMPRESSOR_ID);
//        registry.addRecipeCatalyst(new ItemStack(GCBlocks.machineBase2, 1, 0), RecipeCategories.INGOT_COMPRESSOR_ID);
//        registry.addRecipeCatalyst(new ItemStack(GCBlocks.oxygenCompressor), RecipeCategories.OXYGEN_COMPRESSOR_ID);
//        registry.addRecipeCatalyst(new ItemStack(GCBlocks.refinery), RecipeCategories.REFINERY_ID);
//        registry.addRecipeCatalyst(new ItemStack(GCBlocks.crafting), VanillaRecipeCategoryUid.CRAFTING);
//        registry.getRecipeTransferRegistry().addRecipeTransferHandler(new MagneticCraftingTransferInfo());
//
//        this.addInformationPages(registry);
//        GCItems.hideItemsJEI(registry.getJeiHelpers().getIngredientBlacklist());
//    }
//
//    @Override
//    public void registerCategories(IRecipeCategoryRegistration registry)
//    {
//        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
//        ingotCompressorCategory = new IngotCompressorRecipeCategory(guiHelper);
//        registry.addRecipeCategories(new Tier1RocketRecipeCategory(guiHelper),
//                new BuggyRecipeCategory(guiHelper),
//                new CircuitFabricatorRecipeCategory(guiHelper),
//                ingotCompressorCategory,
//                new OxygenCompressorRecipeCategory(guiHelper),
//                new RefineryRecipeCategory(guiHelper));
//    }
//
//    private void addInformationPages(IModRegistry registry)
//    {
//        registry.addIngredientInfo(new ItemStack(GCBlocks.oxygenPipe), ItemStack.class, I18n.get("jei.fluid_pipe.info"));
//        registry.addIngredientInfo(new ItemStack(GCBlocks.fuelLoader), ItemStack.class, I18n.get("jei.fuel_loader.info"));
//        registry.addIngredientInfo(new ItemStack(GCBlocks.oxygenCollector), ItemStack.class, I18n.get("jei.oxygen_collector.info"));
//        registry.addIngredientInfo(new ItemStack(GCBlocks.oxygenDistributor), ItemStack.class, I18n.get("jei.oxygen_distributor.info"));
//        registry.addIngredientInfo(new ItemStack(GCBlocks.oxygenSealer), ItemStack.class, I18n.get("jei.oxygen_sealer.info"));
//        if (CompatibilityManager.isAppEngLoaded())
//        {
//            registry.addIngredientInfo(new ItemStack(GCBlocks.machineBase2), ItemStack.class, new String [] { I18n.get("jei.electric_compressor.info"), I18n.get("jei.electric_compressor.appeng.info") });
//        }
//        else
//        {
//            registry.addIngredientInfo(new ItemStack(GCBlocks.machineBase2), ItemStack.class, I18n.get("jei.electric_compressor.info"));
//        }
//        registry.addIngredientInfo(new ItemStack(GCBlocks.crafting), ItemStack.class, I18n.get("jei.magnetic_crafting.info"));
//        registry.addIngredientInfo(new ItemStack(GCBlocks.brightLamp), ItemStack.class, I18n.get("jei.arc_lamp.info"));
//        registry.addIngredientInfo(new ItemStack(GCItems.wrench), ItemStack.class, I18n.get("jei.wrench.info"));
//    }
//
//    @Override
//    public void onRuntimeAvailable(IJeiRuntime rt)
//    {
//        recipesCached = rt.getRecipeRegistry();
//    }
//
//    public static void updateHidden(boolean hideSteel, boolean hideAdventure)
//    {
//        boolean changeHidden = false;
//        if (hideSteel != hiddenSteel)
//        {
//            hiddenSteel = hideSteel;
//            changeHidden = true;
//        }
//        if (hideAdventure != hiddenAdventure)
//        {
//            hiddenAdventure = hideAdventure;
//            changeHidden = true;
//        }
//        if (changeHidden && recipesCached != null)
//        {
//            unhide();
//            List<IRecipe> toHide = CompressorRecipes.getRecipeListHidden(hideSteel, hideAdventure);
//            hidden.clear();
//            List<IRecipeWrapper> allRW = recipesCached.getRecipeWrappers(ingotCompressorCategory);
//            for (IRecipe recipe : toHide)
//            {
//                hidden.add(recipesCached.getRecipeWrapper(recipe, RecipeCategories.INGOT_COMPRESSOR_ID));
//            }
//            hide();
//        }
//    }
//
//    private static void hide()
//    {
//        for (IRecipeWrapper wrapper : hidden)
//        {
//            recipesCached.hideRecipe(wrapper);
//        }
//    }
//
//    private static void unhide()
//    {
//        for (IRecipeWrapper wrapper : hidden)
//        {
//            recipesCached.unhideRecipe(wrapper);
//        }
//    }
//}
