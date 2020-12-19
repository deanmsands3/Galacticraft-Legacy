package team.galacticraft.galacticraft.common.core.tile;

import io.netty.buffer.ByteBuf;
import team.galacticraft.galacticraft.common.api.block.IOxygenReliantBlock;
import team.galacticraft.galacticraft.common.api.item.IItemOxygenSupply;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3Dim;
import team.galacticraft.galacticraft.common.api.vector.Vector3;
import team.galacticraft.galacticraft.common.core.Annotations.NetworkedField;
import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.blocks.BlockOxygenDistributor;
import team.galacticraft.galacticraft.common.core.energy.item.ItemElectricBase;
import team.galacticraft.galacticraft.common.core.entities.IBubbleProviderColored;
import team.galacticraft.galacticraft.common.core.inventory.ContainerOxygenDistributor;
import team.galacticraft.galacticraft.common.core.network.NetworkUtil;
import team.galacticraft.galacticraft.common.core.util.FluidUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
//import net.minecraftforge.registries.ObjectHolder;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

public class TileEntityOxygenDistributor extends TileEntityOxygen implements IBubbleProviderColored, MenuProvider
{
//    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.oxygenDistributor)
    public static BlockEntityType<TileEntityOxygenDistributor> TYPE;

    public boolean active;
    public boolean lastActive;

    public static HashSet<BlockVec3Dim> loadedTiles = new HashSet<>();
    public float bubbleSize;
    @NetworkedField(targetSide = EnvType.CLIENT)
    public boolean shouldRenderBubble = true;

    public TileEntityOxygenDistributor()
    {
        super(TYPE, 6000, 8);
//        this.oxygenBubble = null;
        this.inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    }

    @Override
    public void onLoad()
    {
        if (!this.level.isClientSide)
        {
            TileEntityOxygenDistributor.loadedTiles.add(new BlockVec3Dim(this));
        }
    }

    @Override
    public void onChunkUnloaded()
    {
        if (!this.level.isClientSide)
        {
            TileEntityOxygenDistributor.loadedTiles.remove(new BlockVec3Dim(this));
        }
        super.onChunkUnloaded();
    }

    @Override
    public void setRemoved()
    {
        if (!this.level.isClientSide/* && this.oxygenBubble != null*/)
        {
            int bubbleR = Mth.ceil(bubbleSize);
            int bubbleR2 = (int) (bubbleSize * bubbleSize);
            final int xMin = this.getBlockPos().getX() - bubbleR;
            final int xMax = this.getBlockPos().getX() + bubbleR;
            final int yMin = this.getBlockPos().getY() - bubbleR;
            final int yMax = this.getBlockPos().getY() + bubbleR;
            final int zMin = this.getBlockPos().getZ() - bubbleR;
            final int zMax = this.getBlockPos().getZ() + bubbleR;
            for (int x = xMin; x < xMax; x++)
            {
                for (int z = zMin; z < zMax; z++)
                {
                    //Doing y as the inner loop allows BlockVec3 to cache the chunks more efficiently
                    for (int y = yMin; y < yMax; y++)
                    {
                        BlockState state = new BlockVec3(x, y, z).getBlockState(this.level);

                        if (state != null && state.getBlock() instanceof IOxygenReliantBlock && this.getDistanceFromServer(x, y, z) <= bubbleR2)
                        {
                            this.level.getBlockTicks().scheduleTick(new BlockPos(x, y, z), state.getBlock(), 0);
                        }
                    }
                }
            }
//        	this.oxygenBubble.remove();
            TileEntityOxygenDistributor.loadedTiles.remove(new BlockVec3Dim(this));
        }

        super.setRemoved();
    }

    @Override
    public double getPacketRange()
    {
        return 64.0F;
    }

    @Override
    public void addExtraNetworkedData(List<Object> networkedList)
    {
        if (!this.level.isClientSide && !this.isRemoved())
        {
//            networkedList.add(this.oxygenBubble != null);
//            if (this.oxygenBubble != null)
//            {
//                networkedList.add(this.oxygenBubble.getEntityId());
//            }
            if (this.level.getServer().isDedicatedServer())
            {
                networkedList.add(loadedTiles.size());
                //TODO: Limit this to ones in the same dimension as this tile?
                for (BlockVec3Dim distributor : loadedTiles)
                {
                    if (distributor == null)
                    {
                        networkedList.add(-1);
                        networkedList.add(-1);
                        networkedList.add(-1);
                        networkedList.add("null");
                    }
                    else
                    {
                        networkedList.add(distributor.x);
                        networkedList.add(distributor.y);
                        networkedList.add(distributor.z);
                        networkedList.add(distributor.dim.getRegistryName().toString());
                    }
                }
            }
            else
            //Signal integrated server, do not clear loadedTiles
            {
                networkedList.add(-1);
            }
            networkedList.add(this.bubbleSize);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public AABB getRenderBoundingBox()
    {
        return new AABB(this.getBlockPos().getX() - this.bubbleSize, this.getBlockPos().getY() - this.bubbleSize, this.getBlockPos().getZ() - this.bubbleSize, this.getBlockPos().getX() + this.bubbleSize, this.getBlockPos().getY() + this.bubbleSize, this.getBlockPos().getZ() + this.bubbleSize);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public double getViewDistance()
    {
        return Constants.RENDERDISTANCE_LONG;
    }

    @Override
    public void readExtraNetworkedData(ByteBuf dataStream)
    {
        if (this.level.isClientSide)
        {
//            if (dataStream.readBoolean())
//            {
//                this.oxygenBubble = (EntityBubble) worldObj.getEntityByID(dataStream.readInt());
//            }
            int size = dataStream.readInt();
            if (size >= 0)
            {
                loadedTiles.clear();
                for (int i = 0; i < size; ++i)
                {
                    int i1 = dataStream.readInt();
                    int i2 = dataStream.readInt();
                    int i3 = dataStream.readInt();
                    String str = NetworkUtil.readUTF8String(dataStream);
                    if (str == null || str == "null")
                    {
                        continue;
                    }
                    DimensionType i4 = DimensionType.getByName(new ResourceLocation(str));
                    if (i1 == -1 && i2 == -1 && i3 == -1 && i4 == null)
                    {
                        continue;
                    }
                    loadedTiles.add(new BlockVec3Dim(i1, i2, i3, i4));
                }
            }
            this.bubbleSize = dataStream.readFloat();
        }
    }

    public int getDistanceFromServer(int par1, int par3, int par5)
    {
        final int d3 = this.getBlockPos().getX() - par1;
        final int d4 = this.getBlockPos().getY() - par3;
        final int d5 = this.getBlockPos().getZ() - par5;
        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    @Override
    public void tick()
    {
        if (!this.level.isClientSide)
        {
            ItemStack oxygenItemStack = this.getItem(1);
            if (oxygenItemStack != null && oxygenItemStack.getItem() instanceof IItemOxygenSupply)
            {
                IItemOxygenSupply oxygenItem = (IItemOxygenSupply) oxygenItemStack.getItem();
                int oxygenDraw = (int) Math.floor(Math.min(this.oxygenPerTick * 2.5F, this.getMaxOxygenStored() - this.getOxygenStored()));
                this.setOxygenStored(getOxygenStored() + oxygenItem.discharge(oxygenItemStack, oxygenDraw));
                if (this.getOxygenStored() > this.getMaxOxygenStored())
                {
                    this.setOxygenStored(this.getOxygenStored());
                }
            }
        }

        super.tick();

        if (!this.level.isClientSide)
        {
            if (this.getEnergyStoredGC() > 0.0F && this.hasEnoughEnergyToRun && this.getOxygenStored() > this.oxygenPerTick)
            {
                this.bubbleSize += 0.01F;
            }
            else
            {
                this.bubbleSize -= 0.1F;
            }

            this.bubbleSize = Math.min(Math.max(this.bubbleSize, 0.0F), 10.0F);
        }

//        if (!hasValidBubble && !this.world.isRemote && (this.oxygenBubble == null || this.ticks < 25))
//        {
//            //Check it's a world without a breathable atmosphere
//        	if (this.oxygenBubble == null && this.world.getDimension() instanceof IGalacticraftWorldProvider && !((IGalacticraftWorldProvider)this.world.getDimension()).hasBreathableAtmosphere())
//            {
//                this.oxygenBubble = new EntityBubble(this.world, new Vector3(this), this);
//                this.hasValidBubble = true;
//                this.world.addEntity(this.oxygenBubble);
//            }
//        }

        if (!this.level.isClientSide/* && this.oxygenBubble != null*/)
        {
            this.active = bubbleSize >= 1 && this.hasEnoughEnergyToRun && this.getOxygenStored() > this.oxygenPerTick;

            if (this.ticks % (this.active ? 20 : 4) == 0)
            {
                double size = bubbleSize;
                int bubbleR = Mth.floor(size) + 4;
                int bubbleR2 = (int) (size * size);
                for (int x = this.getBlockPos().getX() - bubbleR; x <= this.getBlockPos().getX() + bubbleR; x++)
                {
                    for (int y = this.getBlockPos().getY() - bubbleR; y <= this.getBlockPos().getY() + bubbleR; y++)
                    {
                        for (int z = this.getBlockPos().getZ() - bubbleR; z <= this.getBlockPos().getZ() + bubbleR; z++)
                        {
                            BlockPos pos = new BlockPos(x, y, z);
                            BlockState state = this.level.getBlockState(pos);
                            Block block = state.getBlock();

                            if (block instanceof IOxygenReliantBlock)
                            {
                                if (this.getDistanceFromServer(x, y, z) <= bubbleR2)
                                {
                                    ((IOxygenReliantBlock) block).onOxygenAdded(this.level, pos, state);
                                }
                                else
                                {
                                    //Do not necessarily extinguish it - it might be inside another oxygen system
                                    this.level.getBlockTicks().scheduleTick(pos, block, 0);
                                }
                            }
                        }
                    }
                }
            }
        }

        this.lastActive = this.active;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);

        if (nbt.contains("bubbleVisible"))
        {
            this.setBubbleVisible(nbt.getBoolean("bubbleVisible"));
        }

        if (nbt.contains("bubbleSize"))
        {
            this.bubbleSize = nbt.getFloat("bubbleSize");
        }
//        this.hasValidBubble = nbt.getBoolean("hasValidBubble");
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);

        nbt.putBoolean("bubbleVisible", this.shouldRenderBubble);
        nbt.putFloat("bubbleSize", this.bubbleSize);
//        nbt.putBoolean("hasValidBubble", this.hasValidBubble);
        return nbt;
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0, 1};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        if (this.canPlaceItem(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return ItemElectricBase.isElectricItemCharged(itemstack);
            case 1:
                return itemstack.getDamageValue() < itemstack.getItem().getMaxDamage();
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        switch (slotID)
        {
        case 0:
            return ItemElectricBase.isElectricItemEmpty(itemstack);
        case 1:
            return FluidUtil.isEmptyContainer(itemstack);
        default:
            return false;
        }
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public boolean canPlaceItem(int slotID, ItemStack itemstack)
    {
        if (itemstack.isEmpty())
        {
            return false;
        }
        if (slotID == 0)
        {
            return ItemElectricBase.isElectricItem(itemstack.getItem());
        }
        if (slotID == 1)
        {
            return itemstack.getItem() instanceof IItemOxygenSupply;
        }
        return false;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.getOxygenStored() > this.oxygenPerTick;
    }

    @Override
    public Direction getFront()
    {
        BlockState state = this.level.getBlockState(getBlockPos());
        if (state.getBlock() instanceof BlockOxygenDistributor)
        {
            return state.getValue(BlockOxygenDistributor.FACING);
        }
        return Direction.NORTH;
    }

    @Override
    public Direction getElectricInputDirection()
    {
        return getFront().getClockWise();
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getItem(0);
    }

    @Override
    public boolean shouldUseOxygen()
    {
        return this.hasEnoughEnergyToRun;
    }

    @Override
    public EnumSet<Direction> getOxygenInputDirections()
    {
        return EnumSet.of(this.getElectricInputDirection().getOpposite());
    }

    @Override
    public EnumSet<Direction> getOxygenOutputDirections()
    {
        return EnumSet.noneOf(Direction.class);
    }

//    @Override
//    public IBubble getBubble()
//    {
//        return this.oxygenBubble;
//    }

    public boolean inBubble(double pX, double pY, double pZ)
    {
        double r = bubbleSize;
        r *= r;
        double d3 = this.getBlockPos().getX() + 0.5D - pX;
        d3 *= d3;
        if (d3 > r)
        {
            return false;
        }
        double d4 = this.getBlockPos().getZ() + 0.5D - pZ;
        d4 *= d4;
        if (d3 + d4 > r)
        {
            return false;
        }
        double d5 = this.getBlockPos().getY() + 0.5D - pY;
        return d3 + d4 + d5 * d5 < r;
    }

    @Override
    public void setBubbleVisible(boolean shouldRender)
    {
        this.shouldRenderBubble = shouldRender;
//        this.oxygenBubble.setShouldRender(shouldRender);
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
        return new Vector3(0.125F, 0.125F, 0.5F);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerOxygenDistributor(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.oxygen_distributor");
    }
}
