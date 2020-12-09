package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.GalacticraftRegistry;
import team.galacticraft.galacticraft.common.api.recipe.INasaWorkbenchRecipe;
import team.galacticraft.galacticraft.core.Annotations.NetworkedField;
import team.galacticraft.galacticraft.core.GCBlockNames;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.GCItems;
import team.galacticraft.galacticraft.core.blocks.BlockMachineBase;
import team.galacticraft.galacticraft.core.client.sounds.GCSounds;
import team.galacticraft.galacticraft.core.energy.item.ItemElectricBase;
import team.galacticraft.galacticraft.core.energy.tile.TileBaseElectricBlock;
import team.galacticraft.galacticraft.core.inventory.ContainerDeconstructor;
import team.galacticraft.galacticraft.core.inventory.IInventoryDefaults;
import team.galacticraft.galacticraft.core.util.ConfigManagerCore;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import team.galacticraft.galacticraft.core.util.GCLog;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TileEntityDeconstructor extends TileBaseElectricBlock implements IInventoryDefaults, WorldlyContainer, IMachineSides, MenuProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.deconstructor)
    public static BlockEntityType<TileEntityDeconstructor> TYPE;

    public static final float SALVAGE_CHANCE = 0.75F;
    public static final int PROCESS_TIME_REQUIRED_BASE = 250;
    public int processTimeRequired = PROCESS_TIME_REQUIRED_BASE;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int processTicks = 0;
    private final ItemStack producingStack = ItemStack.EMPTY;
    private long ticks;

    public static List<ItemStack> salvageable = new LinkedList<>();
    public static List<INasaWorkbenchRecipe> knownRecipes = new LinkedList<>();
    private int recursiveCount;

    static
    {
        initialiseItemList();
    }

    private static void initialiseItemList()
    {
//        if (GalacticraftCore.isPlanetsLoaded)
//        {
//            addSalvage(new ItemStack(AsteroidsItems.basicItem, 1, 5));  //T3 plate
//            addSalvage(new ItemStack(AsteroidsItems.basicItem, 1, 6));  //Compressed titanium
//            addSalvage(new ItemStack(AsteroidsItems.basicItem, 1, 0));  //Titanium ingot
//            addSalvage(new ItemStack(MarsItems.marsItemBasic, 1, 3));  //T2 plate
//            addSalvage(new ItemStack(MarsItems.marsItemBasic, 1, 5));  //Compressed desh
//            addSalvage(new ItemStack(MarsItems.marsItemBasic, 1, 2));  //Desh ingot
//        } TODO planets
        addSalvage(new ItemStack(GCItems.flagPole));
        addSalvage(new ItemStack(GCItems.heavyPlatingTier1));
        addSalvage(new ItemStack(GCItems.ingotMeteoricIron, 1));  //Meteoric iron ingot
        addSalvage(new ItemStack(GCItems.compressedSteel, 1));  //Compressed steel
        addSalvage(new ItemStack(GCItems.compressedAluminum, 1));  //Compressed meteoric iron
        addSalvage(new ItemStack(GCItems.compressedBronze, 1));  //Compressed bronze
        addSalvage(new ItemStack(GCItems.compressedCopper, 1));
        addSalvage(new ItemStack(GCItems.compressedIron, 1));
        addSalvage(new ItemStack(GCItems.compressedMeteoricIron, 1));
        addSalvage(new ItemStack(GCItems.compressedSteel, 1));  //Compressed iron
        addSalvage(new ItemStack(GCItems.compressedTin, 1));
        addSalvage(new ItemStack(GCItems.compressedWaferAdvanced, 1));
        addSalvage(new ItemStack(GCItems.compressedWaferBasic, 1));
        addSalvage(new ItemStack(GCItems.compressedWaferSolar, 1));
        addSalvage(new ItemStack(Items.IRON_INGOT));
        addSalvage(new ItemStack(Items.GOLD_INGOT));
        addSalvage(new ItemStack(Items.GOLD_NUGGET));
        addSalvage(new ItemStack(Items.DIAMOND));
    }

    public static void initialiseRecipeList()
    {
        knownRecipes.addAll(GalacticraftRegistry.getRocketT1Recipes());
        knownRecipes.addAll(GalacticraftRegistry.getBuggyBenchRecipes());
    }

    public static void initialiseRecipeListPlanets()
    {
        knownRecipes.addAll(GalacticraftRegistry.getRocketT2Recipes());
        knownRecipes.addAll(GalacticraftRegistry.getCargoRocketRecipes());
        knownRecipes.addAll(GalacticraftRegistry.getRocketT3Recipes());
        knownRecipes.addAll(GalacticraftRegistry.getAstroMinerRecipes());
    }

    public TileEntityDeconstructor()
    {
        super(TYPE);
        this.storage.setMaxExtract(ConfigManagerCore.hardMode.get() ? 90 : 75);
        this.setTierGC(2);
        this.inventory = NonNullList.withSize(11, ItemStack.EMPTY);
    }

    public static void addSalvage(ItemStack itemStack)
    {
        for (ItemStack inList : salvageable)
        {
            if (ItemStack.isSame(inList, itemStack))
            {
                return;
            }
        }
        salvageable.add(itemStack.copy());
    }

    public static boolean isSalvage(ItemStack stack)
    {
        for (ItemStack inList : salvageable)
        {
            if (ItemStack.isSame(inList, stack))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.level.isClientSide)
        {
            boolean updateInv = false;

            if (this.hasEnoughEnergyToRun)
            {
                if (this.canDeconstruct())
                {
                    ++this.processTicks;

                    if ((this.processTicks * 5) % this.processTimeRequired == 5)
                    {
                        this.level.playSound(null, this.getBlockPos(), GCSounds.deconstructor, SoundSource.BLOCKS, 0.25F, this.level.random.nextFloat() * 0.04F + 0.38F);
                    }

                    if (this.processTicks >= this.processTimeRequired)
                    {
                        this.processTicks = 0;
                        this.deconstruct();
                        updateInv = true;
                    }
                }
                else
                {
                    this.processTicks = 0;
                }
            }
            else
            {
                this.processTicks = 0;
            }

            if (updateInv)
            {
                this.setChanged();
            }
        }

        this.ticks++;
    }

    private boolean canDeconstruct()
    {
        return !this.getInventory().get(1).isEmpty();
    }

    public void deconstruct()
    {
        List<ItemStack> ingredients = new LinkedList<>();
        ItemStack input = this.getInventory().get(1).copy();
        input.setCount(1);
        ingredients.add(input);
        this.recursiveCount = 0;
        List<ItemStack> salvaged = this.getSalvageable(ingredients, null);
        salvaged = this.squashList(salvaged);
        salvaged = this.randomChanceList(salvaged);
        if (salvaged != null)
        {
            for (ItemStack output : salvaged)
            {
                this.addToOutputMatrix(output);
            }
        }
        this.removeItem(1, 1);
    }

    private List<ItemStack> getSalvageable(List<ItemStack> ingredients, ItemStack done)
    {
        if (ingredients == null || ingredients.isEmpty())
        {
            return null;
        }
        if (this.recursiveCount++ > 10)
        {
            return null;
        }

        List<ItemStack> ret = new LinkedList<>();
        for (ItemStack stack : ingredients)
        {
            if (isSalvage(stack))
            {
                ret.add(stack);
            }
            else
            {
                GCLog.debug("Trying to " + this.recursiveCount + " break down " + stack.toString());
                List<ItemStack> ingredients2 = this.getIngredients(stack);
                if (ingredients2 != null)
                {
                    if (done != null)
                    {
                        Iterator<ItemStack> it = ingredients2.iterator();
                        while (it.hasNext())
                        {
                            if (ItemStack.matches(it.next(), done))
                            {
                                it.remove();  //prevent recursive A->{B}  B->{A} type recipe chains
                            }
                        }
                    }
                    List<ItemStack> recursive = this.getSalvageable(ingredients2, stack);
                    if (recursive != null && !recursive.isEmpty())
                    {
                        ret.addAll(recursive);
                    }
                }
            }
        }
        this.recursiveCount--;
        return ret;
    }

    private List<ItemStack> squashList(List<ItemStack> ingredients)
    {
        if (ingredients == null || ingredients.isEmpty())
        {
            return null;
        }
        List<ItemStack> ret = new LinkedList<>();
        for (ItemStack stack : ingredients)
        {
            boolean matched = false;
            for (ItemStack stack1 : ret)
            {
                if (ItemStack.isSame(stack, stack1))
                {
                    matched = true;
                    stack1.grow(stack.getCount());
                    break;
                }
            }
            if (!matched)
            {
                ret.add(stack);
            }
        }
        return ret;
    }

    private List<ItemStack> randomChanceList(List<ItemStack> ingredients)
    {
        if (ingredients == null || ingredients.isEmpty())
        {
            return null;
        }
        List<ItemStack> ret = new LinkedList<>();
        for (ItemStack stack : ingredients)
        {
            int count = stack.getCount();
            int result = 0;
            for (int i = 0; i < count; i++)
            {
                if (this.level.random.nextFloat() < SALVAGE_CHANCE)
                {
                    result++;
                }
            }
            if (result > 0)
            {
                stack.setCount(result);
                ret.add(stack);
            }
        }
        return ret;
    }

    private List<ItemStack> getIngredients(ItemStack stack)
    {
        for (INasaWorkbenchRecipe recipe : knownRecipes)
        {
            ItemStack test = recipe.getRecipeOutput();
            if (ItemStack.isSame(test, stack) && test.getCount() == 1)
            {
                return toItemStackList(recipe.getRecipeInput().values());
            }
        }
//        for (IRecipe recipe : CraftingManager.REGISTRY)
//        {
//            ItemStack test = recipe.getRecipeOutput();
//            if (ItemStack.areItemsEqual(test, stack) && test.getCount() == 1)
//            {
//                if (recipe instanceof ShapedRecipe)
//                {
//                    return expandRecipeInputs(((ShapedRecipe) recipe).recipeItems);
//                }
//                else if (recipe instanceof ShapelessRecipe)
//                {
//                    return expandRecipeInputs(((ShapelessRecipe) recipe).recipeItems);
//                }
//                else if (recipe instanceof ShapedOreRecipe)
//                {
//                    return expandRecipeInputs(((ShapedOreRecipe) recipe).getIngredients());
//                }
//                else if (recipe instanceof ShapelessOreRecipe)
//                {
//                    return expandRecipeInputs(((ShapelessOreRecipe) recipe).getIngredients());
//                }
//            }
//        } TODO Deconstructor recipes
        return null;
    }

    private List<ItemStack> expandRecipeInputs(List<?> inputs)
    {
        List<ItemStack> ret = new LinkedList<>();
        for (Object input : inputs)
        {
            ItemStack toAdd = parseRecipeInput(input);
            if (toAdd != null && !toAdd.isEmpty())
            {
                ret.add(toAdd);
            }
        }
        return ret;
    }

    private ItemStack parseRecipeInput(Object input)
    {
        if (input instanceof Ingredient)
        {
            for (ItemStack obj : ((Ingredient) input).getItems())
            {
                ItemStack ret = parseRecipeInput(obj);
                if (ret != null)
                {
                    return ret;
                }
            }
        }
//        else if (input instanceof ItemStack)
//        {
//            ItemStack stack = (ItemStack) input;
//            if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE)
//            {
//                return new ItemStack(stack.getItem(), stack.getCount(), 0);
//            }
//            else
//            {
//                return stack.copy();
//            }
//        }
//        else if (input instanceof String)
//        {
//            List<ItemStack> stacks = OreDictionary.getOres((String) input);
//            if (stacks.isEmpty())
//            {
//                return null;
//            }
//            for (ItemStack stack : stacks)
//            {
//                if (isSalvage(stack))
//                {
//                    return stack.copy();
//                }
//            }
//            return stacks.get(0) == null ? null : stacks.get(0).copy();
//        } TODO Deconstructor recipes
        else if (input instanceof Iterable)
        {
            for (Object obj : (Iterable) input)
            {
                ItemStack ret = parseRecipeInput(obj);
                if (ret != null)
                {
                    return ret;
                }
            }
        }

        return null;
    }

    private List<ItemStack> toItemStackList(Collection<ItemStack> inputs)
    {
        List<ItemStack> ret = new LinkedList<>();
        for (ItemStack o : inputs)
        {
            if (o != null && !o.isEmpty())
            {
                ret.add(o.copy());
            }
        }
        return ret;
    }

    private void addToOutputMatrix(ItemStack stack)
    {
        for (int i = 2; i < 11; i++)
        {
            if (this.getInventory().get(i).isEmpty())
            {
                this.getInventory().set(i, stack);
                return;
            }
            if (!(ItemStack.isSame(stack, this.getInventory().get(i))))
            {
                continue;
            }
            int size = this.getInventory().get(i).getCount();
            if (size + stack.getCount() < this.getMaxStackSize())
            {
                this.getInventory().get(i).grow(stack.getCount());
                return;
            }
            this.getInventory().get(i).setCount(this.getMaxStackSize());
            stack.shrink(this.getMaxStackSize() - size);
        }
        GCCoreUtil.spawnItem(this.level, this.getBlockPos(), stack);
    }

    @Override
    public void load(CompoundTag par1NBTTagCompound)
    {
        super.load(par1NBTTagCompound);
        this.processTicks = par1NBTTagCompound.getInt("smeltingTicks");

        this.readMachineSidesFromNBT(par1NBTTagCompound);  //Needed by IMachineSides
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putInt("smeltingTicks", this.processTicks);

        this.addMachineSidesToNBT(nbt);  //Needed by IMachineSides
        return nbt;
    }

    @Override
    public boolean canPlaceItem(int slotID, ItemStack itemStack)
    {
        if (slotID == 0)
        {
            return itemStack != null && !itemStack.isEmpty() && ItemElectricBase.isElectricItem(itemStack.getItem());
        }

        return slotID == 1;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        if (side == Direction.DOWN)
        {
            return new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10};
        }

        return new int[]{1};
    }

    @Override
    public boolean canTakeItemThroughFace(int slotID, ItemStack par2ItemStack, Direction par3)
    {
        return slotID >= 2;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.processTicks > 0;
    }

    @Override
    public Direction getFront()
    {
        return BlockMachineBase.getFront(this.level.getBlockState(getBlockPos()));
    }

    @Override
    public Direction getElectricInputDirection()
    {
        switch (this.getSide(MachineSide.ELECTRIC_IN))
        {
        case RIGHT:
            return getFront().getCounterClockWise();
        case REAR:
            return getFront().getOpposite();
        case TOP:
            return Direction.UP;
        case BOTTOM:
            return Direction.DOWN;
        case LEFT:
        default:
            return getFront().getClockWise();
        }
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getItem(0);
    }

    //------------------
    //Added these methods and field to implement IMachineSides properly 
    //------------------
    @Override
    public MachineSide[] listConfigurableSides()
    {
        return new MachineSide[]{MachineSide.ELECTRIC_IN};
    }

    @Override
    public Face[] listDefaultFaces()
    {
        return new Face[]{Face.LEFT};
    }

    private MachineSidePack[] machineSides;

    @Override
    public synchronized MachineSidePack[] getAllMachineSides()
    {
        if (this.machineSides == null)
        {
            this.initialiseSides();
        }

        return this.machineSides;
    }

    @Override
    public void setupMachineSides(int length)
    {
        this.machineSides = new MachineSidePack[length];
    }

    @Override
    public void onLoad()
    {
        this.clientOnLoad();
    }

    @Override
    public IMachineSidesProperties getConfigurationType()
    {
        return IMachineSidesProperties.TWOFACES_HORIZ;
    }
    //------------------END OF IMachineSides implementation

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerDeconstructor(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.deconstructor");
    }
}
