package team.galacticraft.galacticraft.common.core.items;
//
//import team.galacticraft.galacticraft.common.core.blocks.BlockAluminumWire;
//import team.galacticraft.galacticraft.common.core.proxy.ClientProxyCore;
//import net.minecraft.block.Block;
//import net.minecraft.item.Rarity;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.fml.EnvType;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class ItemBlockAluminumWire extends ItemBlockDesc
//{
//    public ItemBlockAluminumWire(Block block)
//    {
//        super(block);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//    }
//
//    /*@Override
//    @OnlyIn(Dist.CLIENT)
//    public IIcon getIconFromDamage(int par1)
//    {
//        return this.getBlock().getIcon(0, par1);
//    }*/
//
//    @Override
//    public String getUnlocalizedName(ItemStack itemstack)
//    {
//        String name = BlockAluminumWire.EnumWireType.values()[itemstack.getDamage()].getName();
//        return this.getBlock().getUnlocalizedName() + "." + name;
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
//    public int getMetadata(int damage)
//    {
//        return damage;
//    }
//}
