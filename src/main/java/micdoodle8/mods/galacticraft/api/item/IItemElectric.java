package micdoodle8.mods.galacticraft.api.item;

import net.minecraft.item.ItemStack;

public interface IItemElectric
{

    /**
     * Adds energy to an item. Returns the quantity of energy that was accepted.
     * This should always return 0 if the item cannot be externally charged.
     *
     * @param itemStack ItemStack to be charged.
     * @param energy Maximum amount of energy to be sent into the item.
     * @param doRecharge If false, the charge will only be simulated.
     * @return Amount of energy that was accepted by the item.
     */
    float recharge(ItemStack itemStack, float energy, boolean doRecharge);

    /**
     * Removes energy from an item. Returns the quantity of energy that was
     * removed. This should always return 0 if the item cannot be externally
     * discharged.
     *
     * @param itemStack ItemStack to be discharged.
     * @param energy Maximum amount of energy to be removed from the item.
     * @param doDischarge If false, the discharge will only be simulated.
     * @return Amount of energy that was removed from the item.
     */
    float discharge(ItemStack itemStack, float energy, boolean doDischarge);

    /**
     * Get the amount of energy currently stored in the item.
     */
    float getElectricityStored(ItemStack theItem);

    /**
     * Get the max amount of energy that can be stored in the item.
     */
    float getMaxElectricityStored(ItemStack theItem);

    /**
     * Sets the amount of energy in the ItemStack.
     *
     * @param itemStack - the ItemStack.
     * @param joules - Amount of electrical energy.
     */
    void setElectricity(ItemStack itemStack, float joules);

    /**
     * @return the energy request this ItemStack demands.
     */
    float getTransfer(ItemStack itemStack);

    /**
     * @return The energy tier this item runs on (must be 1 or 2)
     */
    int getTierGC(ItemStack itemStack);
}
