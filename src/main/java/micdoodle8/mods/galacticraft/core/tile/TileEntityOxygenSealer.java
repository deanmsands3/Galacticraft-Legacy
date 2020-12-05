package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.api.tile.ITileClientUpdates;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GCBlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockOxygenSealer;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.EnergyStorageTile;
import micdoodle8.mods.galacticraft.core.fluid.OxygenPressureProtocol;
import micdoodle8.mods.galacticraft.core.fluid.ThreadFindSeal;
import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenSealer;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public class TileEntityOxygenSealer extends TileEntityOxygen implements ITileClientUpdates, MenuProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.oxygenSealer)
    public static BlockEntityType<TileEntityOxygenSealer> TYPE;

    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean sealed;
    public boolean lastSealed = false;

    public boolean lastDisabled = false;

    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean active;
    public ThreadFindSeal threadSeal;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int stopSealThreadCooldown;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int threadCooldownTotal;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean calculatingSealed;
    public static int countEntities = 0;
    private static int countTemp = 0;
    private static boolean sealerCheckedThisTick = false;
    public static ArrayList<TileEntityOxygenSealer> loadedTiles = new ArrayList<>();
    private static final int UNSEALED_OXYGENPERTICK = 12;
    public List<BlockVec3> leaksClient;


    public TileEntityOxygenSealer()
    {
        super(TYPE/*"container.oxygen_sealer"*/, 10000, UNSEALED_OXYGENPERTICK);
        this.noRedstoneControl = true;
        this.storage.setMaxExtract(5.0F);  //Half of a standard machine's power draw
        this.storage.setMaxReceive(25.0F);
        this.storage.setCapacity(EnergyStorageTile.STANDARD_CAPACITY * 2);  //Large capacity so it can keep working for a while even if chunk unloads affect its power supply
        inventory = NonNullList.withSize(3, ItemStack.EMPTY);
    }

    @Override
    public void onLoad()
    {
        if (!this.level.isClientSide)
        {
            if (!TileEntityOxygenSealer.loadedTiles.contains(this))
            {
                TileEntityOxygenSealer.loadedTiles.add(this);
            }
            this.stopSealThreadCooldown = 126 + countEntities;
        }
        else
        {
            this.clientOnLoad();
        }
    }

    @Override
    public void setRemoved()
    {
        if (!this.level.isClientSide)
        {
            TileEntityOxygenSealer.loadedTiles.remove(this);
        }
        super.setRemoved();
    }

    @Override
    public void onChunkUnloaded()
    {
        if (!this.level.isClientSide)
        {
            TileEntityOxygenSealer.loadedTiles.remove(this);
        }
        super.onChunkUnloaded();
    }

    public int getScaledThreadCooldown(int i)
    {
        if (this.active)
        {
            return Math.min(i, (int) Math.floor(this.stopSealThreadCooldown * i / (double) this.threadCooldownTotal));
        }
        return 0;
    }

    public int getFindSealChecks()
    {
        if (!this.active || this.getOxygenStored() < this.oxygenPerTick || !this.hasEnoughEnergyToRun)
        {
            return 0;
        }
        BlockPos posAbove = new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 1, this.getBlockPos().getZ());
        BlockState stateAbove = this.level.getBlockState(posAbove);
        if (!(stateAbove.getBlock().isAir(stateAbove, this.level, posAbove)) && !OxygenPressureProtocol.canBlockPassAir(this.level, stateAbove, this.getBlockPos().above(), Direction.UP))
        {
            // The vent is blocked
            return 0;
        }

        return 1250;
    }

    public boolean thermalControlEnabled()
    {
        ItemStack oxygenItemStack = this.getItem(2);
        return oxygenItemStack != null && oxygenItemStack.getItem() == GCItems.ambientThermalController && this.hasEnoughEnergyToRun && !this.disabled;
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
                int oxygenDraw = (int) Math.floor(Math.min(UNSEALED_OXYGENPERTICK * 2.5F, this.getMaxOxygenStored() - this.getOxygenStored()));
                this.setOxygenStored(getOxygenStored() + oxygenItem.discharge(oxygenItemStack, oxygenDraw));
                if (this.getOxygenStored() > this.getMaxOxygenStored())
                {
                    this.setOxygenStored(this.getOxygenStored());
                }
            }

            if (this.thermalControlEnabled())
            {
                if (this.storage.getMaxExtract() != 20.0F)
                {
                    this.storage.setMaxExtract(20.0F);
                }
            }
            else if (this.storage.getMaxExtract() != 5.0F)
            {
                this.storage.setMaxExtract(5.0F);
                this.storage.setMaxReceive(25.0F);
            }
        }

        this.oxygenPerTick = this.sealed ? 2 : UNSEALED_OXYGENPERTICK;
        super.tick();

        if (!this.level.isClientSide)
        {
            // Some code to count the number of Oxygen Sealers being updated,
            // tick by tick - needed for queueing
            TileEntityOxygenSealer.countTemp++;

            this.active = this.getOxygenStored() >= 1 && this.hasEnoughEnergyToRun && !this.disabled;
            if (this.disabled != this.lastDisabled)
            {
                this.lastDisabled = this.disabled;
                if (!this.disabled)
                {
                    this.stopSealThreadCooldown = this.threadCooldownTotal * 3 / 5;
                }
            }

            //TODO: if multithreaded, this codeblock should not run if the current threadSeal is flagged looping
            if (this.stopSealThreadCooldown > 0)
            {
                this.stopSealThreadCooldown--;
            }
            else if (!TileEntityOxygenSealer.sealerCheckedThisTick)
            {
                // This puts any Sealer which is updated to the back of the queue for updates
                this.threadCooldownTotal = this.stopSealThreadCooldown = 75 + TileEntityOxygenSealer.countEntities;
                if (this.active || this.sealed)
                {
                    TileEntityOxygenSealer.sealerCheckedThisTick = true;
                    OxygenPressureProtocol.updateSealerStatus(this);
                }
            }

            //TODO: if multithreaded, this.threadSeal needs to be atomic
            if (this.threadSeal != null)
            {
                if (this.threadSeal.looping.get())
                {
                    this.calculatingSealed = this.active;
                }
                else
                {
                    this.calculatingSealed = false;
                    this.sealed = this.threadSeal.sealedFinal.get();
                }
            }
            else
            {
                this.calculatingSealed = true;  //Give an initial 'Check pending' in GUI when first placed
            }

            this.lastSealed = this.sealed;
        }
    }

    public static void onServerTick()
    {
        TileEntityOxygenSealer.countEntities = TileEntityOxygenSealer.countTemp;
        TileEntityOxygenSealer.countTemp = 0;
        TileEntityOxygenSealer.sealerCheckedThisTick = false;
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
            case 2:
                return itemstack.getItem() == GCItems.ambientThermalController;
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
        if (slotID == 2)
        {
            return itemstack.getItem() == GCItems.ambientThermalController;
        }
        return false;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.getOxygenStored() > this.oxygenPerTick && !this.getDisabled(0);
    }

    @Override
    public Direction getFront()
    {
        BlockState state = this.level.getBlockState(getBlockPos());
        if (state.getBlock() instanceof BlockOxygenSealer)
        {
            return state.getValue(BlockOxygenSealer.FACING);
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
        return this.hasEnoughEnergyToRun && this.active;
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

    public static HashMap<BlockVec3, TileEntityOxygenSealer> getSealersAround(Level world, BlockPos pos, int rSquared)
    {
        HashMap<BlockVec3, TileEntityOxygenSealer> ret = new HashMap<BlockVec3, TileEntityOxygenSealer>();

        for (TileEntityOxygenSealer tile : new ArrayList<TileEntityOxygenSealer>(TileEntityOxygenSealer.loadedTiles))
        {
            if (tile != null && tile.getLevel() == world && tile.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < rSquared)
            {
                ret.put(new BlockVec3(tile.getBlockPos()), tile);
            }
        }

        return ret;
    }

    public static TileEntityOxygenSealer getNearestSealer(Level world, double x, double y, double z)
    {
        TileEntityOxygenSealer ret = null;
        double dist = 96 * 96D;

        for (Object tile : world.blockEntityList)
        {
            if (tile instanceof TileEntityOxygenSealer)
            {
                double testDist = ((TileEntityOxygenSealer) tile).distanceToSqr(x, y, z);
                if (testDist < dist)
                {
                    dist = testDist;
                    ret = (TileEntityOxygenSealer) tile;
                }
            }
        }

        return ret;
    }

    @Override
    public void sendUpdateToClient(ServerPlayer player)
    {
        if (this.sealed || this.threadSeal == null || this.threadSeal.leakTrace == null || this.threadSeal.leakTrace.isEmpty())
        {
            return;
        }
        Integer[] data = new Integer[this.threadSeal.leakTrace.size()];
        int index = 0;
        for (BlockVec3 vec : this.threadSeal.leakTrace)
        {
            int dx = vec.x - this.worldPosition.getX() + 128;
            int dz = vec.z - this.worldPosition.getZ() + 128;
            int dy = vec.y;
            int composite;
            if (dx < 0 || dx > 255 || dz < 0 || dz > 255 || dy < 0)
            {
                composite = -1;
            }
            else
            {
                composite = dz + ((dy + (dx << 8)) << 8);
            }
            data[index++] = composite;
        }
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_LEAK_DATA, GCCoreUtil.getDimensionType(player.level), new Object[]{this.getBlockPos(), data}), player);
    }

    @Override
    public void buildDataPacket(int[] data)
    {
        //unused
    }

    @Override
    public void updateClient(List<Object> data)
    {
        this.leaksClient = new ArrayList<>();
        if (data.size() > 1)
        {
            for (int i = 1; i < data.size(); i++)
            {
                int comp = (Integer) data.get(i);
                if (comp >= 0)
                {
                    int dx = (comp >> 16) - 128;
                    int dy = (comp >> 8) & 255;
                    int dz = (comp & 255) - 128;
                    this.leaksClient.add(new BlockVec3(this.worldPosition.getX() + dx, dy, this.worldPosition.getZ() + dz));
                }
            }
        }
    }

    public List<BlockVec3> getLeakTraceClient()
    {
        this.clientOnLoad();
        return this.leaksClient;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerOxygenSealer(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.oxygen_sealer");
    }
}
