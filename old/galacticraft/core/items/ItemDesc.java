package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.List;

import javax.annotation.Nullable;

public abstract class ItemDesc extends Item implements IShiftDescription
{
    public ItemDesc(Properties properties)
    {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        if (this.showDescription(stack))
        {
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340))
            {
                List<String> descString = Minecraft.getInstance().font.split(this.getShiftDescription(stack), 150);
                for (String string : descString)
                {
                    tooltip.add(new TextComponent(string));
                }
            }
            else
            {
                tooltip.add(new TextComponent(GCCoreUtil.translateWithFormat("item_desc.shift", Minecraft.getInstance().options.keyShift.getTranslatedKeyMessage())));
            }
        }
    }
}
