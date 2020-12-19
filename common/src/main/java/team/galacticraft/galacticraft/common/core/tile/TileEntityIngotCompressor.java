package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.recipe.CompressorRecipes;
import team.galacticraft.galacticraft.common.api.recipe.ShapedRecipesGC;
import team.galacticraft.galacticraft.common.core.Annotations.NetworkedField;
import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.inventory.ContainerIngotCompressor;
import team.galacticraft.galacticraft.common.core.inventory.IInventoryDefaults;
import team.galacticraft.galacticraft.common.core.inventory.PersistantInventoryCrafting;
import team.galacticraft.galacticraft.common.core.util.ConfigManagerCore;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.ForgeHooks;
//import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TileEntityIngotCompressor extends TileEntityAdvanced implements IInventoryDefaults, WorldlyContainer, MenuProvider
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.ingotCompressor)
    public static BlockEntityType<TileEntityIngotCompressor> TYPE;

    public static final int PROCESS_TIME_REQUIRED = 200;
    @NetworkedField(targetSide = EnvType.CLIENT)
    public int processTicks = 0;
    @NetworkedField(targetSide = EnvType.CLIENT)
    public int furnaceBurnTime = 0;
    @NetworkedField(targetSide = EnvType.CLIENT)
    public int currentItemBurnTime = 0;
    private long ticks;

    private ItemStack producingStack = ItemStack.EMPTY;
    public PersistantInventoryCrafting compressingCraftMatrix = new PersistantInventoryCrafting();
    public final Set<Player> playersUsing = new HashSet<Player>();
    private static final Random random = new Random();

    public TileEntityIngotCompressor()
    {
        super(TYPE);
        inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.level.isClientSide)
        {
            boolean updateInv = false;
            boolean flag = this.furnaceBurnTime > 0;

            if (this.furnaceBurnTime > 0)
            {
                --this.furnaceBurnTime;
            }

            if (this.furnaceBurnTime == 0 && this.canSmelt())
            {
                ItemStack fuel = this.getInventory().get(0);
                this.currentItemBurnTime = this.furnaceBurnTime = ForgeHooks.getBurnTime(fuel);

                if (this.furnaceBurnTime > 0)
                {
                    updateInv = true;

                    if (!fuel.isEmpty())
                    {
                        fuel.shrink(1);

                        if (fuel.getCount() == 0)
                        {
                            this.getInventory().set(0, fuel.getItem().getContainerItem(fuel));
                        }
                    }
                }
            }

            if (this.furnaceBurnTime > 0 && this.canSmelt())
            {
                ++this.processTicks;

                if (this.processTicks % 40 == 0 && this.processTicks > TileEntityIngotCompressor.PROCESS_TIME_REQUIRED / 2)
                {
                    this.level.playSound(null, this.getBlockPos(), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.3F, this.level.random.nextFloat() * 0.1F + 0.9F);
                }

                if (this.processTicks == TileEntityIngotCompressor.PROCESS_TIME_REQUIRED)
                {
                    this.processTicks = 0;
                    this.smeltItem();
                    updateInv = true;
                }
            }
            else
            {
                this.processTicks = 0;
            }

            if (flag != this.furnaceBurnTime > 0)
            {
                updateInv = true;
            }

            if (updateInv)
            {
                this.setChanged();
            }
        }

        this.ticks++;
    }

    public void updateInput()
    {
        this.producingStack = CompressorRecipes.findMatchingRecipe(this.compressingCraftMatrix, this.level);
    }

    private boolean canSmelt()
    {
        ItemStack itemstack = this.producingStack;
        if (itemstack.isEmpty())
        {
            return false;
        }
        if (this.getInventory().get(1).isEmpty())
        {
            return true;
        }
        if (!this.getInventory().get(1).sameItem(itemstack))
        {
            return false;
        }
        int result = this.getInventory().get(1).getCount() + itemstack.getCount();
        return result <= this.getMaxStackSize() && result <= itemstack.getMaxStackSize();
    }

    public static boolean isItemCompressorInput(ItemStack stack)
    {
        for (Recipe recipe : CompressorRecipes.getRecipeList())
        {
            if (recipe instanceof ShapedRecipesGC)
            {
                for (ItemStack itemstack1 : ((ShapedRecipesGC) recipe).recipeItems)
                {
                    if (stack.getItem() == itemstack1.getItem() && (itemstack1.getDamageValue() == 32767 || stack.getDamageValue() == itemstack1.getDamageValue()))
                    {
                        return true;
                    }
                }
            }
//            else if (recipe instanceof ShapelessOreRecipeGC)
//            {
//                ArrayList<Object> required = new ArrayList<Object>(((ShapelessOreRecipeGC) recipe).getInput());
//
//                Iterator<Object> req = required.iterator();
//
//                int match = 0;
//
//                while (req.hasNext())
//                {
//                    Object next = req.next();
//
//                    if (next instanceof ItemStack)
//                    {
//                        if (OreDictionary.itemMatches((ItemStack) next, stack, false))
//                        {
//                            match++;
//                        }
//                    }
//                    else if (next instanceof List)
//                    {
//                        for (ItemStack itemStack : ((List<ItemStack>) next))
//                        {
//                            if (OreDictionary.itemMatches(itemStack, stack, false))
//                            {
//                                match++;
//                                break;
//                            }
//                        }
//                    }
//                }
//
//                if (match == 0)
//                {
//                    continue;
//                }
//
//                if (match == 1)
//                {
//                    return true;
//                }
//
//                return random.nextInt(match) == 0;
//            } TODO OreDict recipes
        }

        return false;
    }

    private void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack resultItemStack = this.producingStack;
            if (ConfigManagerCore.quickMode.get())
            {
                if (resultItemStack.getItem().getDescriptionId(resultItemStack).contains("compressed"))
                {
                    resultItemStack.grow(resultItemStack.getCount());
                }
            }

            if (this.getInventory().get(1).isEmpty())
            {
                this.getInventory().set(1, resultItemStack.copy());
            }
            else if (this.getInventory().get(1).sameItem(resultItemStack))
            {
                if (this.getInventory().get(1).getCount() + resultItemStack.getCount() > 64)
                {
                    resultItemStack.grow(this.getInventory().get(1).getCount() - 64);
                    GCCoreUtil.spawnItem(this.level, this.getBlockPos(), resultItemStack);
                    this.getInventory().get(1).setCount(64);
                }
                else
                {
                    this.getInventory().get(1).grow(resultItemStack.getCount());
                }
            }

            for (int i = 0; i < this.compressingCraftMatrix.getContainerSize(); i++)
            {
                if (!this.compressingCraftMatrix.getItem(i).isEmpty() && this.compressingCraftMatrix.getItem(i).getItem() == Items.WATER_BUCKET)
                {
                    this.compressingCraftMatrix.setInventorySlotContentsNoUpdate(i, new ItemStack(Items.BUCKET));
                }
                else
                {
                    this.compressingCraftMatrix.removeItem(i, 1);
                }
            }
            this.updateInput();
        }
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.processTicks = nbt.getInt("smeltingTicks");
        ListTag var2 = nbt.getList("Items", 10);

        this.inventory = NonNullList.withSize(this.getContainerSize() - this.compressingCraftMatrix.getContainerSize(), ItemStack.EMPTY);

        ListTag nbttaglist = nbt.getList("Items", 10);

        for (int i = 0; i < nbttaglist.size(); ++i)
        {
            CompoundTag nbttagcompound = nbttaglist.getCompound(i);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < this.getInventory().size())
            {
                this.getInventory().set(j, ItemStack.of(nbttagcompound));
            }
            else if (j < this.getInventory().size() + this.compressingCraftMatrix.getContainerSize())
            {
                this.compressingCraftMatrix.setItem(j - this.getInventory().size(), ItemStack.of(nbttagcompound));
            }
        }

        this.updateInput();
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putInt("smeltingTicks", this.processTicks);
        ListTag var2 = new ListTag();
        int i;

        for (i = 0; i < this.getInventory().size(); ++i)
        {
            if (!this.getInventory().get(i).isEmpty())
            {
                CompoundTag tagCompound = new CompoundTag();
                tagCompound.putByte("Slot", (byte) i);
                this.getInventory().get(i).save(tagCompound);
                var2.add(tagCompound);
            }
        }

        for (i = 0; i < this.compressingCraftMatrix.getContainerSize(); ++i)
        {
            if (!this.compressingCraftMatrix.getItem(i).isEmpty())
            {
                CompoundTag tagCompound = new CompoundTag();
                tagCompound.putByte("Slot", (byte) (i + this.getInventory().size()));
                this.compressingCraftMatrix.getItem(i).save(tagCompound);
                var2.add(tagCompound);
            }
        }

        nbt.put("Items", var2);
        return nbt;
    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }

    @Override
    public int getContainerSize()
    {
        return super.getContainerSize() + this.compressingCraftMatrix.getContainerSize();
    }

    @Override
    public ItemStack getItem(int par1)
    {
        if (par1 >= this.getInventory().size())
        {
            return this.compressingCraftMatrix.getItem(par1 - this.getInventory().size());
        }

        return this.getInventory().get(par1);
    }

    @Override
    public ItemStack removeItem(int par1, int par2)
    {
        if (par1 >= this.getInventory().size())
        {
            ItemStack result = this.compressingCraftMatrix.removeItem(par1 - this.getInventory().size(), par2);
            if (!result.isEmpty())
            {
                this.updateInput();
            }
            this.setChanged();
            return result;
        }

        if (!this.getInventory().get(par1).isEmpty())
        {
            ItemStack var3;

            if (this.getInventory().get(par1).getCount() <= par2)
            {
                var3 = this.getInventory().get(par1);
                this.getInventory().set(par1, ItemStack.EMPTY);
                this.setChanged();
                return var3;
            }
            else
            {
                var3 = this.getInventory().get(par1).split(par2);

                if (this.getInventory().get(par1).isEmpty())
                {
                    this.getInventory().set(par1, ItemStack.EMPTY);
                }

                this.setChanged();
                return var3;
            }
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack removeItemNoUpdate(int par1)
    {
        if (par1 >= this.getInventory().size())
        {
            this.setChanged();
            return this.compressingCraftMatrix.removeItemNoUpdate(par1 - this.getInventory().size());
        }

        if (!this.getInventory().get(par1).isEmpty())
        {
            ItemStack var2 = this.getInventory().get(par1);
            this.getInventory().set(par1, ItemStack.EMPTY);
            this.setChanged();
            return var2;
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int par1, ItemStack stack)
    {
        if (par1 >= this.getInventory().size())
        {
            this.compressingCraftMatrix.setItem(par1 - this.getInventory().size(), stack);
            this.updateInput();
        }
        else
        {
            this.getInventory().set(par1, stack);

            if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize())
            {
                stack.setCount(this.getMaxStackSize());
            }
        }
        this.setChanged();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.getInventory())
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public int getMaxStackSize()
    {
        return 64;
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.level.getBlockEntity(this.getBlockPos()) == this && par1EntityPlayer.distanceToSqr(this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 0.5D, this.getBlockPos().getZ() + 0.5D) <= 64.0D;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public boolean canPlaceItem(int slotID, ItemStack itemStack)
    {
        if (slotID == 0)
        {
            return ForgeHooks.getBurnTime(itemStack) > 0;
        }
        else if (slotID >= 2)
        {
            if (!this.producingStack.isEmpty())
            {
                ItemStack stackInSlot = this.getItem(slotID);
                return !stackInSlot.isEmpty() && stackInSlot.sameItem(itemStack);
            }
            return TileEntityIngotCompressor.isItemCompressorInput(itemStack);
        }

        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        if (side == Direction.DOWN)
        {
            return new int[]{1};
        }
        int[] slots = new int[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayList<Integer> removeSlots = new ArrayList<>();

        for (int i = 2; i < 11; i++)
        {
            if (removeSlots.contains(i))
            {
                continue;
            }
            ItemStack stack1 = this.getItem(i);
            if (stack1.isEmpty())
            {
                continue;
            }

            for (int j = i + 1; j < 11; j++)
            {
                if (removeSlots.contains(j))
                {
                    continue;
                }
                ItemStack stack2 = this.getItem(j);
                if (stack2.isEmpty())
                {
                    continue;
                }

                if (stack1.sameItem(stack2))
                {
                    if (stack2.getCount() >= stack1.getCount())
                    {
                        removeSlots.add(j);
                    }
                    else
                    {
                        removeSlots.add(i);
                    }
                    break;
                }
            }
        }

        if (removeSlots.size() > 0)
        {
            int[] returnSlots = new int[slots.length - removeSlots.size()];
            int j = 0;
            for (int i = 0; i < slots.length; i++)
            {
                if (i > 0 && removeSlots.contains(slots[i]))
                {
                    continue;
                }
                returnSlots[j] = slots[i];
                j++;
            }

            return returnSlots;
        }

        return slots;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slotID, ItemStack par2ItemStack, Direction par3)
    {
        return this.canPlaceItem(slotID, par2ItemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slotID, ItemStack par2ItemStack, Direction par3)
    {
        return slotID == 1;
    }

    @Override
    public double getPacketRange()
    {
        return 12.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 3;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return true;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerIngotCompressor(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.compressor");
    }
}
