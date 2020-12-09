package team.galacticraft.galacticraft.common.core.client.jei.oxygencompressor;
//
//import team.galacticraft.galacticraft.common.api.GalacticraftRegistry;
//import team.galacticraft.galacticraft.common.api.item.EnumExtendedInventorySlot;
//import team.galacticraft.galacticraft.core.items.ItemOxygenTank;
//import net.minecraft.item.ItemStack;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class OxygenCompressorRecipeMaker
//{
//    public static List<OxygenCompressorRecipeWrapper> getRecipesList()
//    {
//        List<OxygenCompressorRecipeWrapper> recipes = new ArrayList<>();
//
//        for (ItemStack stack : GalacticraftRegistry.listAllGearForSlot(EnumExtendedInventorySlot.LEFT_TANK))
//        {
//            if (stack != null && stack.getItem() instanceof ItemOxygenTank)
//            {
//                recipes.add(new OxygenCompressorRecipeWrapper(stack.copy()));
//            }
//        }
//
//        return recipes;
//    }
//}
