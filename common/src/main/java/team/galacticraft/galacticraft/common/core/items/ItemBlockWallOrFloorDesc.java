package team.galacticraft.galacticraft.common.core.items;

import team.galacticraft.galacticraft.core.GCBlocks;
import team.galacticraft.galacticraft.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import javax.annotation.Nullable;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.List;

public class ItemBlockWallOrFloorDesc extends StandingAndWallBlockItem
{
    public ItemBlockWallOrFloorDesc(Block floorBlock, Block wallBlockIn, Properties builder)
    {
        super(floorBlock, wallBlockIn, builder);
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
                    TileEntity te = ((BlockTileGC) this.getBlock()).createTileEntity(null, getBlock().getStateFromMeta(stack.getDamage() & 12));
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
                    TileEntity te = ((BlockAdvancedTile) this.getBlock()).createTileEntity(worldIn, getBlock().getStateFromMeta(stack.getDamage() & 12));
                    if (te instanceof TileBaseElectricBlock)
                    {
                        float powerDrawn = ((TileBaseElectricBlock) te).storage.getMaxExtract();
                        if (powerDrawn > 0)
                        {
                            tooltip.add(TextFormatting.GREEN + I18n.getWithFormat("item_desc.powerdraw", EnergyDisplayHelper.getEnergyDisplayS(powerDrawn * 20)));
                        }
                    }
                }*/
                tooltip.add(new TextComponent(I18n.getWithFormat("item_desc.shift", Minecraft.getInstance().options.keyShift.getTranslatedKeyMessage())));
            }
        }
    }
}
