package micdoodle8.mods.galacticraft.items;

import micdoodle8.mods.galacticraft.blocks.BlockMachine;
import micdoodle8.mods.galacticraft.blocks.BlockMachine2;
import micdoodle8.mods.galacticraft.blocks.BlockMachineBase;
import micdoodle8.mods.galacticraft.init.GCBlocks;
import micdoodle8.mods.galacticraft.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockMachine extends ItemBlockDesc
{
    public ItemBlockMachine(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public String getTranslationKey(ItemStack itemstack)
    {
        int index = 0;
        int typenum = itemstack.getItemDamage() & 12;

        if (this.getBlock() instanceof BlockMachineBase)
        {
            return ((BlockMachineBase) this.getBlock()).getTranslationKey(typenum);
        }
        return "tile.machine.0";
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            return;
        }

        int typenum = stack.getItemDamage() & 12;

        //The player could be a FakePlayer made by another mod e.g. LogisticsPipes
        if (player instanceof EntityPlayerSP)
        {
            if (this.getBlock() == GCBlocks.machineBase && typenum == BlockMachine.EnumMachineType.COMPRESSOR.getMetadata())
            {
                ClientProxyCore.playerClientHandler.onBuild(1, (EntityPlayerSP) player);
            }
            else if (this.getBlock() == GCBlocks.machineBase2 && typenum == BlockMachine2.EnumMachineExtendedType.CIRCUIT_FABRICATOR.getMetadata())
            {
                ClientProxyCore.playerClientHandler.onBuild(2, (EntityPlayerSP) player);
            }
        }
    }

    @Override
    public String getTranslationKey()
    {
        return this.getBlock().getTranslationKey() + ".0";
    }
}
