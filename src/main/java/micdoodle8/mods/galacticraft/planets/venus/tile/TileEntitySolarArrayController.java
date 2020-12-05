package micdoodle8.mods.galacticraft.planets.venus.tile;

import com.google.common.collect.Sets;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkProvider;
import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.dimension.DimensionSpaceStation;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectricalSource;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockSolarArrayController;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlockNames;
import micdoodle8.mods.galacticraft.planets.venus.dimension.DimensionVenus;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.planets.venus.inventory.ContainerGeothermal;
import micdoodle8.mods.galacticraft.planets.venus.inventory.ContainerSolarArrayController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import java.util.EnumSet;
import java.util.Set;

public class TileEntitySolarArrayController extends TileBaseUniversalElectricalSource implements IDisableableMachine, IInventoryDefaults, WorldlyContainer, IConnector, MenuProvider
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + VenusBlockNames.solarArrayController)
    public static BlockEntityType<TileEntitySolarArrayController> TYPE;

    private int solarStrength = 0;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean disabled = false;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int disableCooldown = 0;
    public static final int MAX_GENERATE_WATTS = 1000;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int generateWatts = 0;
    private final Set<ITransmitter> solarArray = Sets.newHashSet();
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int connectedInfo = 0;

    private boolean initialised = false;

    public TileEntitySolarArrayController()
    {
        super(TYPE);
        this.storage.setMaxExtract(TileEntitySolarArrayController.MAX_GENERATE_WATTS);
        this.storage.setMaxReceive(TileEntitySolarArrayController.MAX_GENERATE_WATTS);
        this.storage.setCapacity(50000);
        this.initialised = true;
        this.inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    public void tick()
    {
        if (!this.initialised)
        {
            this.storage.setCapacity(50000);
            this.initialised = true;
        }

        if (!this.level.isClientSide)
        {
            this.receiveEnergyGC(null, this.generateWatts, false);

            EnumSet<Direction> outputDirections = EnumSet.noneOf(Direction.class);
            for (Direction dir : Direction.values())
            {
                if (dir.getAxis().isHorizontal() && !this.getElectricalOutputDirections().contains(dir))
                {
                    outputDirections.add(dir);
                }
            }
//            outputDirections.addAll(Arrays.asList(Direction.HORIZONTALS));
//            outputDirections.removeAll(this.getElectricalOutputDirections());

            BlockVec3 thisVec = new BlockVec3(this);
            solarArray.clear();
            for (Direction direction : outputDirections)
            {
                BlockEntity tileAdj = thisVec.getTileEntityOnSide(this.level, direction);

                if (tileAdj != null)
                {
                    if (tileAdj instanceof INetworkProvider)
                    {
                        if (tileAdj instanceof ITransmitter)
                        {
                            if (((ITransmitter) tileAdj).canConnect(direction.getOpposite(), NetworkType.SOLAR_MODULE))
                            {
                                if (((INetworkProvider) tileAdj).getNetwork() instanceof SolarModuleNetwork)
                                {
                                    solarArray.addAll(((SolarModuleNetwork) ((INetworkProvider) tileAdj).getNetwork()).getTransmitters());
                                }
                            }
                        }
                        else
                        {
                            if (((INetworkProvider) tileAdj).getNetwork() instanceof SolarModuleNetwork)
                            {
                                solarArray.addAll(((SolarModuleNetwork) ((INetworkProvider) tileAdj).getNetwork()).getTransmitters());
                            }
                        }
                    }
                }
            }
        }

        super.tick();

        if (!this.level.isClientSide)
        {
            this.recharge(this.getInventory().get(0));

            if (this.disableCooldown > 0)
            {
                this.disableCooldown--;
            }

            if (!this.getDisabled(0) && this.ticks % 20 == 0)
            {
                this.solarStrength = 0;
                int arraySizeWithinRange = 0;
                BlockPos offset;
                if (this.level.isDay() && (this.level.getDimension() instanceof IGalacticraftDimension || !this.level.isRaining() && !this.level.isThundering()))
                {
                    for (ITransmitter transmitter : solarArray)
                    {
                        BlockEntity tile = (BlockEntity) transmitter;
                        Vec3i diff = tile.getBlockPos().subtract(this.getBlockPos());
                        if (Math.abs(diff.getX()) <= 16 && diff.getY() == 0 && Math.abs(diff.getZ()) <= 16)
                        {
                            arraySizeWithinRange++;
                            if (this.level.canSeeSkyFromBelowWater(tile.getBlockPos()))
                            {
                                boolean valid = true;

                                for (int y = this.getBlockPos().getY() + 1; y < 256; y++)
                                {
                                    offset = this.getBlockPos().offset(0, y, 0);
                                    BlockState state = this.level.getBlockState(offset);

                                    if (state.isSolidRender(level, offset))
                                    {
                                        valid = false;
                                        break;
                                    }
                                }

                                if (valid)
                                {
                                    this.solarStrength++;
                                }
                            }
                        }
                    }
                }

                connectedInfo = solarStrength << 16 | arraySizeWithinRange;
            }
        }

        float angle = this.level.getTimeOfDay(1.0F) - 0.7845194F < 0 ? 1.0F - 0.7845194F : -0.7845194F;
        float celestialAngle = (this.level.getTimeOfDay(1.0F) + angle) * 360.0F;
        if (!(this.level.getDimension() instanceof DimensionSpaceStation))
        {
            celestialAngle += 12.5F;
        }
        if (this.level.getDimension() instanceof DimensionVenus)
        {
            celestialAngle = 180F - celestialAngle;
        }
        celestialAngle %= 360;
        boolean isDaytime = this.level.isDay() && (celestialAngle < 180.5F || celestialAngle > 359.5F) || this.level.getDimension() instanceof DimensionSpaceStation;

        if (!this.level.isClientSide)
        {
            int generated = this.getGenerate();
            if (generated > 0)
            {
                this.generateWatts = Math.min(Math.max(generated, 0), TileEntitySolarArrayController.MAX_GENERATE_WATTS);
            }
            else
            {
                this.generateWatts = 0;
            }
        }

        this.produce();
    }

    public int getGenerate()
    {
        if (this.getDisabled(0))
        {
            return 0;
        }

        return (int) Math.floor(solarStrength * 6.3F * this.getSolarBoost());
    }

    public float getSolarBoost()
    {
        float result = (float) (this.level.getDimension() instanceof ISolarLevel ? ((ISolarLevel) this.level.getDimension()).getSolarEnergyMultiplier() : 1.0F);
        if (this.level.getDimension() instanceof DimensionSpaceStation)
        {
            // 10% boost for new solar on space stations
            result *= 1.1F;
        }
        if (this.level.getDimension() instanceof DimensionVenus)
        {
            if (this.worldPosition.getY() > 90)
            {
                result += (this.worldPosition.getY() - 90) / 1000F;   //Small improvement on Venus at higher altitudes
            }
        }
        return result;
    }

    @Override
    public double getPacketRange()
    {
        return 20.0D;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.storage.setCapacity(nbt.getFloat("maxEnergy"));
        this.setDisabled(0, nbt.getBoolean("disabled"));
        this.disableCooldown = nbt.getInt("disabledCooldown");

        this.initialised = false;
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putFloat("maxEnergy", this.getMaxEnergyStoredGC());
        nbt.putInt("disabledCooldown", this.disableCooldown);
        nbt.putBoolean("disabled", this.getDisabled(0));

        return nbt;
    }

	/*@Override
    public float getRequest(EnumFacing direction)
	{
		return 0;
	}
	*/

    @Override
    public EnumSet<Direction> getElectricalInputDirections()
    {
        return EnumSet.noneOf(Direction.class);
    }

    public Direction getFront()
    {
        BlockState state = this.level.getBlockState(getBlockPos());
        if (state.getBlock() instanceof BlockSolarArrayController)
        {
            return state.getValue(BlockSolarArrayController.FACING);
        }
        return Direction.NORTH;
    }

    @Override
    public EnumSet<Direction> getElectricalOutputDirections()
    {
        return EnumSet.of(getFront());
    }

    @Override
    public Direction getElectricOutputDirection()
    {
        return getFront();
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown == 0)
        {
            this.disabled = disabled;
            this.disableCooldown = 20;
        }
    }

    @Override
    public boolean getDisabled(int index)
    {
        return this.disabled;
    }

    public int getScaledElecticalLevel(int i)
    {
        return (int) Math.floor(this.getEnergyStoredGC() * i / this.getMaxEnergyStoredGC());
    }

    @Override
    public int getMaxStackSize()
    {
        return 1;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0};
    }

    @Override
    public boolean canTakeItemThroughFace(int slotID, ItemStack itemstack, Direction side)
    {
        return slotID == 0;
    }

    @Override
    public boolean canPlaceItem(int slotID, ItemStack itemstack)
    {
        return slotID == 0 && ItemElectricBase.isElectricItem(itemstack.getItem());
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        return type == NetworkType.POWER && direction == this.getElectricOutputDirection() ||
                type == NetworkType.SOLAR_MODULE && direction != this.getElectricOutputDirection() && direction.getAxis() != Direction.Axis.Y;
    }

    public int getPossibleArraySize()
    {
        return connectedInfo & 0xFFFF;
    }

    public int getActualArraySize()
    {
        return (connectedInfo >> 16) & 0xFFFF;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerSolarArrayController(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.solar_array_controller");
    }
}
