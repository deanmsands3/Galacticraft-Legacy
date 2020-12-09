package team.galacticraft.galacticraft.common.core.items;
//
//import java.util.List;
//
//import javax.annotation.Nullable;
//
//import team.galacticraft.galacticraft.core.proxy.ClientProxyCore;
//import team.galacticraft.galacticraft.core.util.EnumColor;
//import team.galacticraft.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.block.Block;
//import net.minecraft.client.util.ITooltipFlag;
//import net.minecraft.item.BlockItem;
//import net.minecraft.item.Rarity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class ItemBlockCreativeGC extends BlockItem
//{
//    public ItemBlockCreativeGC(Block block)
//    {
//        super(block);
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public Rarity getRarity(ItemStack par1ItemStack)
//    {
//        return ClientProxyCore.galacticraftItem;
//    }
//
//    @Override
//    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
//    {
//        tooltip.add(EnumColor.RED + I18n.get("gui.creative_only.desc"));
//    }
//}
