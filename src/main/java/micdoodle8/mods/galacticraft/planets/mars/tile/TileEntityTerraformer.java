package micdoodle8.mods.galacticraft.planets.mars.tile;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.entities.IBubbleProviderColored;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSolar;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.IFluidHandlerWrapper;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockTerraformer;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlockNames;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerTerraformer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DirectionalPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityTerraformer extends TileBaseElectricBlockWithInventory implements WorldlyContainer, IDisableableMachine, IBubbleProviderColored, IFluidHandlerWrapper, MenuProvider
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsBlockNames.terraformer)
    public static BlockEntityType<TileEntityTerraformer> TYPE;

    private final int tankCapacity = 2000;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public FluidTank waterTank = new FluidTank(this.tankCapacity);
    public boolean active;
    public boolean lastActive;
    public static final int WATTS_PER_TICK = 1;
    private final ArrayList<BlockPos> terraformableBlocksList = new ArrayList<BlockPos>();
    private final ArrayList<BlockPos> grassBlockList = new ArrayList<BlockPos>();
    private final ArrayList<BlockPos> grownTreesList = new ArrayList<BlockPos>();
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int terraformableBlocksListSize = 0; // used for server->client ease
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int grassBlocksListSize = 0; // used for server->client ease
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean treesDisabled;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean grassDisabled;
    public final double MAX_SIZE = 15.0D;
    private int[] useCount = new int[2];
    private int saplingIndex = 6;
    public float bubbleSize;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean shouldRenderBubble = true;

    public TileEntityTerraformer()
    {
        super(TYPE);
        this.storage.setMaxExtract(ConfigManagerCore.hardMode.get() ? 60 : 30);
        this.inventory = NonNullList.withSize(14, ItemStack.EMPTY);
    }

    public int getScaledWaterLevel(int i)
    {
        final double fuelLevel = this.waterTank.getFluid() == FluidStack.EMPTY ? 0 : this.waterTank.getFluid().getAmount();

        return (int) (fuelLevel * i / this.tankCapacity);
    }

    @Override
    public void setRemoved()
    {
        super.setRemoved();
    }

    public double getDistanceFromServer(double par1, double par3, double par5)
    {
        final double d3 = this.getBlockPos().getX() + 0.5D - par1;
        final double d4 = this.getBlockPos().getY() + 0.5D - par3;
        final double d5 = this.getBlockPos().getZ() + 0.5D - par5;
        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    @Override
    public void tick()
    {
        super.tick();

//        if (this.terraformBubble == null)
//        {
//            if (!this.world.isRemote)
//            {
//                this.terraformBubble = new EntityTerraformBubble(this.world, new Vector3(this), this);
//                this.world.addEntity(this.terraformBubble);
//            }
//        }

        if (!this.level.isClientSide)
        {
            final FluidStack liquid = FluidUtil.getFluidContained(this.getInventory().get(0));
            if (FluidUtil.isFluidStrict(liquid, Fluids.WATER.getRegistryName().getPath()))
            {
                FluidUtil.loadFromContainer(waterTank, Fluids.WATER, this.getInventory(), 0, liquid.getAmount());
            }

            this.active = this.bubbleSize == this.MAX_SIZE && this.hasEnoughEnergyToRun && !this.getFirstBonemealStack().isEmpty() && this.waterTank.getFluid() != FluidStack.EMPTY && this.waterTank.getFluid().getAmount() > 0;
        }

        if (!this.level.isClientSide && (this.active != this.lastActive || this.ticks % 60 == 0))
        {
            this.terraformableBlocksList.clear();
            this.grassBlockList.clear();

            if (this.active)
            {
                int bubbleSize = (int) Math.ceil(this.bubbleSize);
                double bubbleSizeSq = this.bubbleSize;
                bubbleSizeSq *= bubbleSizeSq;
                boolean doGrass = !this.grassDisabled && !this.getFirstSeedStack().isEmpty();
                boolean doTrees = !this.treesDisabled && !this.getFirstSaplingStack().isEmpty();
                for (int x = this.getBlockPos().getX() - bubbleSize; x < this.getBlockPos().getX() + bubbleSize; x++)
                {
                    for (int y = this.getBlockPos().getY() - bubbleSize; y < this.getBlockPos().getY() + bubbleSize; y++)
                    {
                        for (int z = this.getBlockPos().getZ() - bubbleSize; z < this.getBlockPos().getZ() + bubbleSize; z++)
                        {
                            BlockPos pos = new BlockPos(x, y, z);
                            Block blockID = this.level.getBlockState(pos).getBlock();
                            if (blockID == null)
                            {
                                continue;
                            }

                            if (!(blockID.isAir(this.level.getBlockState(pos), this.level, pos)) && this.getDistanceFromServer(x, y, z) < bubbleSizeSq)
                            {
                                if (doGrass && blockID instanceof ITerraformableBlock && ((ITerraformableBlock) blockID).isTerraformable(this.level, pos))
                                {
                                    this.terraformableBlocksList.add(new BlockPos(x, y, z));
                                }
                                else if (doTrees)
                                {
                                    Block blockIDAbove = this.level.getBlockState(pos.above()).getBlock();
                                    if (blockID == Blocks.GRASS && blockIDAbove.isAir(this.level.getBlockState(pos.above()), this.level, pos.above()))
                                    {
                                        this.grassBlockList.add(new BlockPos(x, y, z));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!this.level.isClientSide && this.terraformableBlocksList.size() > 0 && this.ticks % 15 == 0)
        {
            ArrayList<BlockPos> terraformableBlocks2 = new ArrayList<BlockPos>(this.terraformableBlocksList);

            int randomIndex = this.level.random.nextInt(this.terraformableBlocksList.size());
            BlockPos vec = terraformableBlocks2.get(randomIndex);

            if (this.level.getBlockState(vec).getBlock() instanceof ITerraformableBlock)
            {
                Block id;

                switch (this.level.random.nextInt(40))
                {
                case 0:
                    boolean water = true;
                    for (Direction dir : Direction.values())
                    {
                        if (dir.getAxis().isHorizontal() && this.level.getBlockState(vec.relative(dir)).getShape(level, vec.relative(dir)) != Shapes.block())
                        {
                            water = false;
                            break;
                        }
                    }
                    if (water)
                    {
                        id = Blocks.WATER;
                    }
                    else
                    {
                        id = Blocks.GRASS;
                    }
                    break;
                default:
                    id = Blocks.GRASS;
                    break;
                }

                this.level.setBlockAndUpdate(vec, id.defaultBlockState());

                if (id == Blocks.GRASS)
                {
                    this.useCount[0]++;
                    this.waterTank.drain(1, IFluidHandler.FluidAction.EXECUTE);
                    this.checkUsage(1);
                }
                else if (id == Blocks.WATER)
                {
                    this.checkUsage(2);
                }
            }

            this.terraformableBlocksList.remove(randomIndex);
        }

        if (!this.level.isClientSide && !this.treesDisabled && this.grassBlockList.size() > 0 && this.ticks % 50 == 0)
        {
            int randomIndex = this.level.random.nextInt(this.grassBlockList.size());
            BlockPos vecGrass = grassBlockList.get(randomIndex);

            if (this.level.getBlockState(vecGrass).getBlock() == Blocks.GRASS)
            {
                BlockPos vecSapling = vecGrass.offset(0, 1, 0);
                ItemStack sapling = this.getFirstSaplingStack();
                boolean flag = false;

                //Attempt to prevent placement too close to other trees
                for (BlockPos testVec : this.grownTreesList)
                {
                    if (testVec.distSqr(vecSapling) < 9)
                    {
                        flag = true;
                        break;
                    }
                }

                if (!flag && sapling != null)
                {
                    Item item = sapling.getItem();
                    if (item instanceof BlockItem)
                    {
                        if (((BlockItem) item).place(new DirectionalPlaceContext(level, vecSapling, Direction.DOWN, sapling, Direction.UP)) == InteractionResult.SUCCESS)
                        {
                            Block b = level.getBlockState(vecSapling).getBlock();
                            if (b instanceof SaplingBlock)
                            {
                                if (this.level.getRawBrightness(vecSapling, 0) >= 9)
                                {
                                    ((SaplingBlock) b).performBonemeal((ServerLevel) this.level, this.level.random, vecSapling, this.level.getBlockState(vecSapling));
                                    this.grownTreesList.add(new BlockPos(vecSapling.getX(), vecSapling.getY(), vecSapling.getZ()));
                                }
                            }
                            else if (b instanceof BushBlock)
                            {
                                if (this.level.getRawBrightness(worldPosition, 0) >= 5)
                                //Hammer the tick tick a few times to try to get it to grow - it won't always
                                {
                                    for (int j = 0; j < 12; j++)
                                    {
                                        if (this.level.getBlockState(vecSapling).getBlock() == b)
                                        {
                                            b.tick(this.level.getBlockState(vecSapling), (ServerLevel) this.level, vecSapling, this.level.random);
                                        }
                                        else
                                        {
                                            this.grownTreesList.add(new BlockPos(vecSapling.getX(), vecSapling.getY(), vecSapling.getZ()));
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    this.useCount[1]++;
                    this.waterTank.drain(50, IFluidHandler.FluidAction.EXECUTE);
                    this.checkUsage(0);
                }
            }

            this.grassBlockList.remove(randomIndex);
        }

        if (!this.level.isClientSide)
        {
            this.terraformableBlocksListSize = this.terraformableBlocksList.size();
            this.grassBlocksListSize = this.grassBlockList.size();
        }

        if (this.hasEnoughEnergyToRun && (!this.grassDisabled || !this.treesDisabled))
        {
            this.bubbleSize = (float) Math.min(Math.max(0, this.bubbleSize + 0.1F), this.MAX_SIZE);
        }
        else
        {
            this.bubbleSize = (float) Math.min(Math.max(0, this.bubbleSize - 0.1F), this.MAX_SIZE);
        }

        this.lastActive = this.active;
    }

    @Override
    public void addExtraNetworkedData(List<Object> networkedList)
    {
        if (!this.level.isClientSide)
        {
            networkedList.add(this.bubbleSize);
        }
    }

    @Override
    public void readExtraNetworkedData(ByteBuf dataStream)
    {
        if (this.level.isClientSide)
        {
            this.bubbleSize = dataStream.readFloat();
        }
    }

    private void checkUsage(int type)
    {
        ItemStack stack = null;

        if ((this.useCount[0] + this.useCount[1]) % 4 == 0)
        {
            stack = this.getFirstBonemealStack();

            if (!stack.isEmpty())
            {
                stack.shrink(1);
            }
        }

        switch (type)
        {
        case 0:
            stack = this.getInventory().get(this.saplingIndex);

            if (!stack.isEmpty())
            {
                stack.shrink(1);
            }
            break;
        case 1:
            if (this.useCount[0] % 4 == 0)
            {
                stack = this.getFirstSeedStack();

                if (!stack.isEmpty())
                {
                    stack.shrink(1);
                }
            }
            break;
        case 2:
            this.waterTank.drain(50, IFluidHandler.FluidAction.EXECUTE);
            break;
        }
    }

    private int getSelectiveStack(int start, int end)
    {
        for (int i = start; i < end; i++)
        {
            ItemStack stack = this.getInventory().get(i);

            if (!stack.isEmpty())
            {
                return i;
            }
        }

        return -1;
    }

    private int getRandomStack(int start, int end)
    {
        int stackcount = 0;
        for (int i = start; i < end; i++)
        {
            if (!this.getInventory().get(i).isEmpty())
            {
                stackcount++;
            }
        }

        if (stackcount == 0)
        {
            return -1;
        }

        int random = this.level.random.nextInt(stackcount);
        for (int i = start; i < end; i++)
        {
            if (!this.getInventory().get(i).isEmpty())
            {
                if (random == 0)
                {
                    return i;
                }
                random--;
            }
        }

        return -1;
    }

    public ItemStack getFirstBonemealStack()
    {
        int index = this.getSelectiveStack(2, 6);

        if (index != -1)
        {
            return this.getInventory().get(index);
        }

        return ItemStack.EMPTY;
    }

    public ItemStack getFirstSaplingStack()
    {
        int index = this.getRandomStack(6, 10);

        if (index != -1)
        {
            this.saplingIndex = index;
            return this.getInventory().get(index);
        }

        return ItemStack.EMPTY;
    }

    public ItemStack getFirstSeedStack()
    {
        int index = this.getSelectiveStack(10, 14);

        if (index != -1)
        {
            return this.getInventory().get(index);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);

        this.bubbleSize = nbt.getFloat("BubbleSize");
        this.useCount = nbt.getIntArray("UseCountArray");

        if (this.useCount.length == 0)
        {
            this.useCount = new int[2];
        }

        if (nbt.contains("waterTank"))
        {
            this.waterTank.readFromNBT(nbt.getCompound("waterTank"));
        }

        if (nbt.contains("bubbleVisible"))
        {
            this.setBubbleVisible(nbt.getBoolean("bubbleVisible"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putFloat("BubbleSize", this.bubbleSize);
        nbt.putIntArray("UseCountArray", this.useCount);

        if (this.waterTank.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("waterTank", this.waterTank.writeToNBT(new CompoundTag()));
        }

        nbt.putBoolean("bubbleVisible", this.shouldRenderBubble);
        return nbt;
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        return this.canPlaceItem(slotID, itemstack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        if (slotID == 0)
        {
            return FluidUtil.isEmptyContainer(itemstack);
        }
        if (slotID == 1)
        {
            return ItemElectricBase.isElectricItemEmpty(itemstack);
        }

        return false;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public boolean canPlaceItem(int slotID, ItemStack itemstack)
    {
        switch (slotID)
        {
        case 0:
            LazyOptional<FluidStack> holder = net.minecraftforge.fluids.FluidUtil.getFluidContained(itemstack);
            return holder.isPresent() && holder.orElse(null).getFluid() == Fluids.WATER;
        case 1:
            return ItemElectricBase.isElectricItem(itemstack.getItem());
        case 2:
        case 3:
        case 4:
        case 5:
            return itemstack.getItem() == Items.BONE_MEAL;
        case 6:
        case 7:
        case 8:
        case 9:
            return ContainerTerraformer.isOnSaplingList(itemstack);
        case 10:
        case 11:
        case 12:
        case 13:
            return itemstack.getItem() == Items.WHEAT_SEEDS;
        }
        return false;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return !this.grassDisabled || !this.treesDisabled;
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getItem(1);
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown <= 0)
        {
            switch (index)
            {
            case 0:
                this.treesDisabled = !this.treesDisabled;
                break;
            case 1:
                this.grassDisabled = !this.grassDisabled;
                break;
            }

            this.disableCooldown = 10;
        }
    }

    @Override
    public boolean getDisabled(int index)
    {
        switch (index)
        {
        case 0:
            return this.treesDisabled;
        case 1:
            return this.grassDisabled;
        }

        return false;
    }

//    @Override
//    public IBubble getBubble()
//    {
//        return this.terraformBubble;
//    }

    @Override
    public void setBubbleVisible(boolean shouldRender)
    {
        this.shouldRenderBubble = shouldRender;
    }

    @Override
    public double getPacketRange()
    {
        return 64.0F;
    }

    //Pipe handling
    @Override
    public boolean canDrain(Direction from, Fluid fluid)
    {
        return false;
    }

    @Override
    public FluidStack drain(Direction from, FluidStack resource, IFluidHandler.FluidAction action)
    {
        return null;
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, IFluidHandler.FluidAction action)
    {
        return null;
    }

    @Override
    public boolean canFill(Direction from, Fluid fluid)
    {
        return (fluid == null || "water".equals(fluid.getRegistryName().getPath())) && from != this.getElectricInputDirection();
    }

    @Override
    public int fill(Direction from, FluidStack resource, IFluidHandler.FluidAction action)
    {
        int used = 0;

        if (resource != null && this.canFill(from, resource.getFluid()))
        {
            used = this.waterTank.fill(resource, action);
        }

        return used;
    }

//    @Override
//    public FluidTankInfo[] getTankInfo(Direction from)
//    {
//        return new FluidTankInfo[] { new FluidTankInfo(this.waterTank) };
//    }


    @Override
    public int getTanks()
    {
        return 1;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        return tank == 0 ? this.waterTank.getFluid() : FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank)
    {
        return tank == 0 ? this.waterTank.getCapacity() : 0;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
    {
        return tank == 0 && this.waterTank.isFluidValid(stack);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public AABB getRenderBoundingBox()
    {
        return new AABB(this.getBlockPos().getX() - this.bubbleSize, this.getBlockPos().getY() - this.bubbleSize, this.getBlockPos().getZ() - this.bubbleSize, this.getBlockPos().getX() + this.bubbleSize, this.getBlockPos().getY() + this.bubbleSize, this.getBlockPos().getZ() + this.bubbleSize);
    }

    @Override
    public float getBubbleSize()
    {
        return this.bubbleSize;
    }

    @Override
    public boolean getBubbleVisible()
    {
        return this.shouldRenderBubble;
    }

    @Override
    public Vector3 getColor()
    {
        return new Vector3(0.125F, 0.5F, 0.125F);
    }

    @Override
    public Direction getFront()
    {
        return this.level.getBlockState(getBlockPos()).getValue(BlockTerraformer.FACING);
    }

    @Override
    public Direction getElectricInputDirection()
    {
        return getFront().getClockWise();
    }

//    @Override
//    public boolean hasCapability(Capability<?> capability, Direction facing)
//    {
//        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
//    }

    private LazyOptional<IFluidHandler> holder = null;

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            if (holder == null)
            {
                holder = LazyOptional.of(new NonNullSupplier<IFluidHandler>()
                {
                    @Nonnull
                    @Override
                    public IFluidHandler get()
                    {
                        return new FluidHandlerWrapper(TileEntityTerraformer.this, facing);
                    }
                });
            }
            return holder.cast();
        }
//        if (EnergyUtil.checkMekGasHandler(capability))
//        {
//            return (T) this;
//        }  TODO Mek support
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        if (direction == null)
        {
            return false;
        }
        if (type == NetworkType.POWER)
        {
            return direction == this.getElectricInputDirection();
        }
        if (type == NetworkType.FLUID)
        {
            return direction != this.getElectricInputDirection();
        }
        return false;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerTerraformer(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.tile_terraformer");
    }
}
