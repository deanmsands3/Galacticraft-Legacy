package team.galacticraft.galacticraft.common.core.items;

import team.galacticraft.galacticraft.common.core.GCItems;
import team.galacticraft.galacticraft.common.core.client.gui.screen.GuiPreLaunchChecklist;
import team.galacticraft.galacticraft.common.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.common.core.util.EnumSortCategory;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import team.galacticraft.galacticraft.common.core.util.WorldUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;
import java.util.List;

public class ItemPreLaunchChecklist extends Item implements ISortable
{
    public ItemPreLaunchChecklist(Properties properties)
    {
        super(properties);
//        this.setUnlocalizedName(assetName);
//        this.setMaxStackSize(1);
        //this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    @Override
    @Environment(EnvType.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack item, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        if (!item.isEmpty() && this == GCItems.heavyPlatingTier1)
        {
            tooltip.add(new TranslatableComponent(("item.tier1.desc")));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand hand)
    {
        ItemStack itemStack = playerIn.getItemInHand(hand);

        if (worldIn.isClientSide)
        {
            Minecraft.getInstance().setScreen(new GuiPreLaunchChecklist(WorldUtil.getAllChecklistKeys(), itemStack.getTag().getCompound("checklistData")));
//            playerIn.openGui(GalacticraftCore.instance, GuiIdsCore.PRE_LAUNCH_CHECKLIST, playerIn.world, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
        }

        return new InteractionResultHolder<>(InteractionResult.PASS, itemStack);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }
}
