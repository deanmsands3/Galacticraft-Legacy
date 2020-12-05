package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.api.item.ISensorGlassesArmor;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemSensorGlasses extends ArmorItem implements ISortable, ISensorGlassesArmor
{
    public ItemSensorGlasses(Item.Properties builder)
    {
        super(EnumArmorGC.ARMOR_SENSOR_GLASSES, EquipmentSlot.HEAD, builder);
//        this.setUnlocalizedName(assetName);
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
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type)
    {
        return Constants.TEXTURE_PREFIX + "textures/model/armor/sensor_1.png";
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public void renderHelmetOverlay(ItemStack stack, Player player, int width, int height, float partialTicks)
    {
//        OverlaySensorGlasses.renderSensorGlassesMain(stack, player, partialTicks);
//        OverlaySensorGlasses.renderSensorGlassesValueableBlocks(stack, player, partialTicks); TODO Overlays
    }

    //    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void renderHelmetOverlay(ItemStack stack, PlayerEntity player, ScaledResolution resolution, float partialTicks)
//    {
//        OverlaySensorGlasses.renderSensorGlassesMain(stack, player, resolution, partialTicks);
//        OverlaySensorGlasses.renderSensorGlassesValueableBlocks(stack, player, resolution, partialTicks);
//    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GEAR;
    }
}
