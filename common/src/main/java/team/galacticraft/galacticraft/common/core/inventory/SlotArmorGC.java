package team.galacticraft.galacticraft.common.core.inventory;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.world.inventory.InventoryMenu.*;

public class SlotArmorGC extends Slot
{
    private static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[]{EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET};
    private static final EquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    final int armorType;
    final Player thePlayer;

    public SlotArmorGC(Player thePlayer, Container par2IInventory, int par3, int par4, int par5, int par6)
    {
        super(par2IInventory, par3, par4, par5);
        this.thePlayer = thePlayer;
        this.armorType = par6;
    }

    @Override
    public int getMaxStackSize()
    {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return stack.canEquip(VALID_EQUIPMENT_SLOTS[this.armorType], thePlayer);
//        final Item item = par1ItemStack.isEmpty() ? null : par1ItemStack.getItem();
//        return item != null && item.isValidArmor(par1ItemStack, VALID_EQUIPMENT_SLOTS[this.armorType], this.thePlayer);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, ARMOR_SLOT_TEXTURES[armorType]);
    }
}
