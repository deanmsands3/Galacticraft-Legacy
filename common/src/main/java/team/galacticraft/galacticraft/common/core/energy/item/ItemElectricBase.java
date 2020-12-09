package team.galacticraft.galacticraft.common.core.energy.item;

import team.galacticraft.galacticraft.common.api.item.IItemElectricBase;
import team.galacticraft.galacticraft.core.energy.EnergyConfigHandler;
import team.galacticraft.galacticraft.core.energy.EnergyDisplayHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemElectricBase extends Item implements IItemElectricBase
{
    private static Object itemManagerIC2;
    public float transferMax;
    //    private DefaultArtifactVersion mcVersion = null;
    public static final int DAMAGE_RANGE = 100;

    public ItemElectricBase(Properties builder)
    {
        super(builder);
//        this.setMaxStackSize(1);
//        this.setMaxDamage(DAMAGE_RANGE);
//        this.setNoRepair();
        this.setMaxTransfer();

//        this.mcVersion = new DefaultArtifactVersion((String) FMLInjectionData.data()[4]);

//        if (EnergyConfigHandler.isIndustrialCraft2Loaded())
//        {
//            itemManagerIC2 = new ElectricItemManagerIC2();
//        } TODO IC2 support
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

    protected void setMaxTransfer()
    {
        this.transferMax = 200;
    }

    @Override
    public float getMaxTransferGC(ItemStack itemStack)
    {
        return this.transferMax;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        String color = "";
        float joules = this.getElectricityStored(stack);

        if (joules <= this.getMaxElectricityStored(stack) / 3)
        {
            color = "\u00a74";
        }
        else if (joules > this.getMaxElectricityStored(stack) * 2 / 3)
        {
            color = "\u00a72";
        }
        else
        {
            color = "\u00a76";
        }

        tooltip.add(new TextComponent(color + EnergyDisplayHelper.getEnergyDisplayS(joules) + "/" + EnergyDisplayHelper.getEnergyDisplayS(this.getMaxElectricityStored(stack))));
    }

    /**
     * Makes sure the item is uncharged when it is crafted and not charged.
     * Change this if you do not want this to happen!
     */
    @Override
    public void onCraftedBy(ItemStack itemStack, Level par2World, Player par3EntityPlayer)
    {
        this.setElectricity(itemStack, 0);
    }

    @Override
    public float recharge(ItemStack itemStack, float energy, boolean doReceive)
    {
        float rejectedElectricity = Math.max(this.getElectricityStored(itemStack) + energy - this.getMaxElectricityStored(itemStack), 0);
        float energyToReceive = energy - rejectedElectricity;
        if (energyToReceive > this.transferMax)
        {
            rejectedElectricity += energyToReceive - this.transferMax;
            energyToReceive = this.transferMax;
        }

        if (doReceive)
        {
            this.setElectricity(itemStack, this.getElectricityStored(itemStack) + energyToReceive);
        }

        return energyToReceive;
    }

    @Override
    public float discharge(ItemStack itemStack, float energy, boolean doTransfer)
    {
        float thisEnergy = this.getElectricityStored(itemStack);
        float energyToTransfer = Math.min(Math.min(thisEnergy, energy), this.transferMax);

        if (doTransfer)
        {
            this.setElectricity(itemStack, thisEnergy - energyToTransfer);
        }

        return energyToTransfer;
    }

    @Override
    public int getTierGC(ItemStack itemStack)
    {
        return 1;
    }

    @Override
    public void setElectricity(ItemStack itemStack, float joules)
    {
        // Saves the frequency in the ItemStack
        if (itemStack.getTag() == null)
        {
            itemStack.setTag(new CompoundTag());
        }

        float electricityStored = Math.max(Math.min(joules, this.getMaxElectricityStored(itemStack)), 0);
        if (joules > 0F || itemStack.getTag().contains("electricity"))
        {
            itemStack.getTag().putFloat("electricity", electricityStored);
        }

        /** Sets the damage as a percentage to render the bar properly. */
        itemStack.setDamageValue(DAMAGE_RANGE - (int) (electricityStored / this.getMaxElectricityStored(itemStack) * DAMAGE_RANGE));
    }

    @Override
    public float getTransfer(ItemStack itemStack)
    {
        return Math.min(this.transferMax, this.getMaxElectricityStored(itemStack) - this.getElectricityStored(itemStack));
    }

    /**
     * Gets the energy stored in the item. Energy is stored using item NBT
     */
    @Override
    public float getElectricityStored(ItemStack itemStack)
    {
        if (itemStack.getTag() == null)
        {
            itemStack.setTag(new CompoundTag());
        }
        float energyStored = 0f;
        if (itemStack.getTag().contains("electricity"))
        {
            Tag obj = itemStack.getTag().get("electricity");
            if (obj instanceof DoubleTag)
            {
                energyStored = ((DoubleTag) obj).getAsFloat();
            }
            else if (obj instanceof FloatTag)
            {
                energyStored = ((FloatTag) obj).getAsFloat();
            }
        }
        else //First time check item - maybe from addInformation() in a JEI recipe display?
        {
            if (itemStack.getDamageValue() == DAMAGE_RANGE)
            {
                return 0F;
            }

            energyStored = this.getMaxElectricityStored(itemStack) * (DAMAGE_RANGE - itemStack.getDamageValue()) / DAMAGE_RANGE;
            itemStack.getTag().putFloat("electricity", energyStored);
        }

        /** Sets the damage as a percentage to render the bar properly. */
        itemStack.setDamageValue(DAMAGE_RANGE - (int) (energyStored / this.getMaxElectricityStored(itemStack) * DAMAGE_RANGE));
        return energyStored;
    }

//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            list.add(ElectricItemHelper.getUncharged(new ItemStack(this)));
//            list.add(ElectricItemHelper.getWithCharge(new ItemStack(this), this.getMaxElectricityStored(new ItemStack(this))));
//        }
//    } TODO

    public static boolean isElectricItem(Item item)
    {
        return item instanceof IItemElectricBase;

//        if (EnergyConfigHandler.isIndustrialCraft2Loaded())
//        {
//            if (item instanceof ic2.api.item.ISpecialElectricItem)
//            {
//                return true;
//            }
//        } TODO
    }

    public static boolean isElectricItemEmpty(ItemStack itemstack)
    {
        if (itemstack.isEmpty())
        {
            return false;
        }
        Item item = itemstack.getItem();

        if (item instanceof IItemElectricBase)
        {
            return ((IItemElectricBase) item).getElectricityStored(itemstack) <= 0;
        }

//        if (EnergyConfigHandler.isIndustrialCraft2Loaded())
//        {
//            if (item instanceof ic2.api.item.IElectricItem)
//            {
//                return !((ic2.api.item.IElectricItem) item).canProvideEnergy(itemstack);
//            }
//        } TODO

        return false;
    }

    public static boolean isElectricItemCharged(ItemStack itemstack)
    {
        if (itemstack == null)
        {
            return false;
        }
        Item item = itemstack.getItem();

        if (item instanceof IItemElectricBase)
        {
            return ((IItemElectricBase) item).getElectricityStored(itemstack) > 0;
        }

//        if (EnergyConfigHandler.isIndustrialCraft2Loaded())
//        {
//            if (item instanceof ic2.api.item.IElectricItem)
//            {
//                return ((ic2.api.item.IElectricItem) item).canProvideEnergy(itemstack);
//            }
//        } TODO

        return false;
    }

    //For RF compatibility

//    @RuntimeInterface(clazz = "cofh.redstoneflux.api.IEnergyContainerItem", modID = "")
//    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate)
//    {
//        return (int) (this.recharge(container, maxReceive * EnergyConfigHandler.RF_RATIO, !simulate) / EnergyConfigHandler.RF_RATIO);
//    }
//
//    @RuntimeInterface(clazz = "cofh.redstoneflux.api.IEnergyContainerItem", modID = "")
//    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate)
//    {
//        return (int) (this.discharge(container, maxExtract / EnergyConfigHandler.TO_RF_RATIO, !simulate) * EnergyConfigHandler.TO_RF_RATIO);
//    }
//
//    @RuntimeInterface(clazz = "cofh.redstoneflux.api.IEnergyContainerItem", modID = "")
//    public int getEnergyStored(ItemStack container)
//    {
//        return (int) (this.getElectricityStored(container) * EnergyConfigHandler.TO_RF_RATIO);
//    }
//
//    @RuntimeInterface(clazz = "cofh.redstoneflux.api.IEnergyContainerItem", modID = "")
//    public int getMaxEnergyStored(ItemStack container)
//    {
//        return (int) (this.getMaxElectricityStored(container) * EnergyConfigHandler.TO_RF_RATIO);
//    }
//
//    // The following seven methods are for Mekanism compatibility
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IEnergizedItem", modID = CompatibilityManager.modidMekanism)
//    public double getEnergy(ItemStack itemStack)
//    {
//        return this.getElectricityStored(itemStack) * EnergyConfigHandler.TO_MEKANISM_RATIO;
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IEnergizedItem", modID = CompatibilityManager.modidMekanism)
//    public void setEnergy(ItemStack itemStack, double amount)
//    {
//        this.setElectricity(itemStack, (float) amount * EnergyConfigHandler.MEKANISM_RATIO);
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IEnergizedItem", modID = CompatibilityManager.modidMekanism)
//    public double getMaxEnergy(ItemStack itemStack)
//    {
//        return this.getMaxElectricityStored(itemStack) * EnergyConfigHandler.TO_MEKANISM_RATIO;
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IEnergizedItem", modID = CompatibilityManager.modidMekanism)
//    public double getMaxTransfer(ItemStack itemStack)
//    {
//        return this.transferMax * EnergyConfigHandler.TO_MEKANISM_RATIO;
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IEnergizedItem", modID = CompatibilityManager.modidMekanism)
//    public boolean canReceive(ItemStack itemStack)
//    {
//        return (itemStack != null && !(itemStack.getItem() instanceof ItemBatteryInfinite));
//    }
//
//    public boolean canSend(ItemStack itemStack)
//    {
//        return true;
//    }
//
//    // All the following methods are for IC2 compatibility
//
//    @RuntimeInterface(clazz = "ic2.api.item.ISpecialElectricItem", modID = CompatibilityManager.modidIC2)
//    public IElectricItemManager getManager(ItemStack itemstack)
//    {
//        return (IElectricItemManager) ItemElectricBase.itemManagerIC2;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.item.IElectricItem", modID = CompatibilityManager.modidIC2)
//    public boolean canProvideEnergy(ItemStack itemStack)
//    {
//        return true;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.item.IElectricItem", modID = CompatibilityManager.modidIC2)
//    public int getTier(ItemStack itemStack)
//    {
//        return 1;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.item.IElectricItem", modID = CompatibilityManager.modidIC2)
//    public double getMaxCharge(ItemStack itemStack)
//    {
//        return this.getMaxElectricityStored(itemStack) / EnergyConfigHandler.IC2_RATIO;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.item.IElectricItem", modID = CompatibilityManager.modidIC2)
//    public double getTransferLimit(ItemStack itemStack)
//    {
//        return this.transferMax * EnergyConfigHandler.TO_IC2_RATIO;
//    } TODO Bat compat
}
