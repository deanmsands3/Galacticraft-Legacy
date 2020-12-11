package team.galacticraft.galacticraft.common.core.items;

import team.galacticraft.galacticraft.common.core.GCBlocks;
import team.galacticraft.galacticraft.common.core.blocks.BlockAdvancedTile;
import team.galacticraft.galacticraft.common.core.blocks.BlockTileGC;
import team.galacticraft.galacticraft.common.core.energy.EnergyDisplayHelper;
import team.galacticraft.galacticraft.common.core.energy.tile.TileBaseElectricBlock;
import team.galacticraft.galacticraft.common.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.List;

public class ItemBlockDesc extends BlockItem
{
    public ItemBlockDesc(Block blockIn, Properties builder)
    {
        super(blockIn, builder);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level world, Player player)
    {
        if (!world.isClientSide)
        {
            return;
        }

        //The player could be a FakePlayer made by another mod e.g. LogisticsPipes
        if (player instanceof LocalPlayer)
        {
            if (this.getBlock() == GCBlocks.fuelLoader)
            {
                ClientProxyCore.playerClientHandler.onBuild(4, (LocalPlayer) player);
            }
            else if (this.getBlock() == GCBlocks.fuelLoader)
            {
                ClientProxyCore.playerClientHandler.onBuild(6, (LocalPlayer) player);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        if (this.getBlock() instanceof IShiftDescription && ((IShiftDescription) this.getBlock()).showDescription(stack))
        {
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340))
            {
                List<String> descString = Minecraft.getInstance().font.split(((IShiftDescription) this.getBlock()).getShiftDescription(stack), 150);
                for (String string : descString)
                {
                    tooltip.add(new TextComponent(string));
                }
            }
            else
            {
                /*if (this.getBlock() instanceof BlockTileGC)
                {
                    TileEntity te = ((BlockTileGC) this.getBlock()).newBlockEntity(null, getBlock().getStateFromMeta(stack.getDamage() & 12));
                    if (te instanceof TileBaseElectricBlock)
                    {
                        float powerDrawn = ((TileBaseElectricBlock) te).storage.getMaxExtract();
                        if (powerDrawn > 0)
                        {
                            tooltip.add(TextFormatting.GREEN + I18n.getWithFormat("item_desc.powerdraw", EnergyDisplayHelper.getEnergyDisplayS(powerDrawn * 20)));
                        }
                    }
                }
                else if (this.getBlock() instanceof BlockAdvancedTile)
                {
                    TileEntity te = ((BlockAdvancedTile) this.getBlock()).newBlockEntity(worldIn, getBlock().getStateFromMeta(stack.getDamage() & 12));
                    if (te instanceof TileBaseElectricBlock)
                    {
                        float powerDrawn = ((TileBaseElectricBlock) te).storage.getMaxExtract();
                        if (powerDrawn > 0)
                        {
                            tooltip.add(TextFormatting.GREEN + I18n.getWithFormat("item_desc.powerdraw", EnergyDisplayHelper.getEnergyDisplayS(powerDrawn * 20)));
                        }
                    }
                }*/
                tooltip.add(new TranslatableComponent(WithFormat("item_desc.shift", Minecraft.getInstance().options.keyShift.getTranslatedKeyMessage())));
            }
        }
    }
}
