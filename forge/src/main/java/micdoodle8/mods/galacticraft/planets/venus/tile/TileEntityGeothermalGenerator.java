package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Annotations;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectricalSource;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySolar;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.PlanetFluids;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerElectrolyzer;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlockNames;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.VenusModule;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockGeothermalGenerator;
import micdoodle8.mods.galacticraft.planets.venus.client.fx.VenusParticles;
import micdoodle8.mods.galacticraft.planets.venus.inventory.ContainerGeothermal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import java.util.EnumSet;

public class TileEntityGeothermalGenerator extends TileBaseUniversalElectricalSource implements IInventoryDefaults, WorldlyContainer, IConnector, IDisableableMachine, MenuProvider
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + VenusBlockNames.GEOTHERMAL_GENERATOR)
    public static BlockEntityType<TileEntityGeothermalGenerator> TYPE;

    public static final int MAX_GENERATE_GJ_PER_TICK = 200;
    public static final int MIN_GENERATE_GJ_PER_TICK = 30;

    private boolean validSpout;

    @Annotations.NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean disabled = false;
    @Annotations.NetworkedField(targetSide = LogicalSide.CLIENT)
    public int disableCooldown = 0;

    @Annotations.NetworkedField(targetSide = LogicalSide.CLIENT)
    public int generateWatts = 0;

    public TileEntityGeothermalGenerator()
    {
        super(TYPE);
        this.storage.setMaxExtract(TileEntitySolar.MAX_GENERATE_WATTS);
        this.storage.setMaxReceive(TileEntitySolar.MAX_GENERATE_WATTS);
        this.inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    public void tick()
    {
        if (!this.level.isClientSide)
        {
            this.receiveEnergyGC(null, this.generateWatts, false);
        }

        super.tick();

        if (this.ticks % 20 == 0)
        {
            BlockPos below = this.getBlockPos().below();
            BlockState stateBelow = this.level.getBlockState(below);

            boolean lastValidSpout = this.validSpout;
            this.validSpout = false;
            if (stateBelow.getBlock() == VenusBlocks.VAPOR_SPOUT)
            {
                BlockPos pos1 = below.below();
                for (; this.getBlockPos().getY() - pos1.getY() < 20; pos1 = pos1.below())
                {
                    BlockState state = this.level.getBlockState(pos1);
                    if (state.getBlock() == PlanetFluids.SULPHURIC_ACID.getBlock())
                    {
                        this.validSpout = true;
                        break;
                    }
                    else if (!state.getBlock().isAir(this.level.getBlockState(pos1), this.level, pos1))
                    {
                        // Not valid
                        break;
                    }
                }
            }

            if (this.level.isClientSide && this.validSpout != lastValidSpout)
            {
                // tick active texture
                BlockState state = this.level.getBlockState(this.getBlockPos());
                this.level.sendBlockUpdated(this.getBlockPos(), state, state, 3);
            }
        }

        if (!this.level.isClientSide)
        {
            this.recharge(this.getInventory().get(0));

            if (this.disableCooldown > 0)
            {
                this.disableCooldown--;
            }

            this.generateWatts = Math.min(Math.max(this.getGenerate(), 0), TileEntityGeothermalGenerator.MAX_GENERATE_GJ_PER_TICK);
        }
        else
        {
            if (this.generateWatts > 0 && this.ticks % ((int) ((float) MAX_GENERATE_GJ_PER_TICK / (this.generateWatts + 1)) * 5 + 1) == 0)
            {
                double posX = worldPosition.getX() + 0.5;
                double posY = worldPosition.getY() + 1.0;
                double posZ = worldPosition.getZ() + 0.5;
                level.addParticle(VenusParticles.ACID_EXHAUST, posX - 0.25, posY, posZ - 0.25, 0.0, 0.025, 0.0);
                level.addParticle(VenusParticles.ACID_EXHAUST, posX - 0.25, posY, posZ, 0.0, 0.025, 0.0);
                level.addParticle(VenusParticles.ACID_EXHAUST, posX - 0.25, posY, posZ + 0.25, 0.0, 0.025, 0.0);
                level.addParticle(VenusParticles.ACID_EXHAUST, posX, posY, posZ - 0.25, 0.0, 0.025, 0.0);
                level.addParticle(VenusParticles.ACID_EXHAUST, posX, posY, posZ, 0.0, 0.025, 0.0);
                level.addParticle(VenusParticles.ACID_EXHAUST, posX, posY, posZ + 0.25, 0.0, 0.025, 0.0);
                level.addParticle(VenusParticles.ACID_EXHAUST, posX + 0.25, posY, posZ - 0.25, 0.0, 0.025, 0.0);
                level.addParticle(VenusParticles.ACID_EXHAUST, posX + 0.25, posY, posZ, 0.0, 0.025, 0.0);
                level.addParticle(VenusParticles.ACID_EXHAUST, posX + 0.25, posY, posZ + 0.25, 0.0, 0.025, 0.0);
            }
        }

        this.produce();
    }

    private int getGenerate()
    {
        if (this.getDisabled(0))
        {
            return 0;
        }

        if (!this.validSpout)
        {
            return 0;
        }

        int diff = TileEntityGeothermalGenerator.MAX_GENERATE_GJ_PER_TICK - TileEntityGeothermalGenerator.MIN_GENERATE_GJ_PER_TICK;
        return (int) Math.floor((Math.sin(this.ticks / 50.0F) * 0.5F + 0.5F) * diff + TileEntityGeothermalGenerator.MIN_GENERATE_GJ_PER_TICK);
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        if (direction == null || type != NetworkType.POWER)
        {
            return false;
        }

        return direction == this.getElectricOutputDirection();
    }

    @Override
    public EnumSet<Direction> getElectricalInputDirections()
    {
        return EnumSet.noneOf(Direction.class);
    }

    public Direction getFront()
    {
        BlockState state = this.level.getBlockState(getBlockPos());
        if (state.getBlock() instanceof BlockGeothermalGenerator)
        {
            return state.getValue(BlockGeothermalGenerator.FACING);
        }
        return Direction.NORTH;
    }

    @Override
    public EnumSet<Direction> getElectricalOutputDirections()
    {
        return EnumSet.of(getFront().getClockWise());
    }

    @Override
    public Direction getElectricOutputDirection()
    {
        return getFront().getClockWise();
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
            if (this.disabled != disabled && this.level.isClientSide)
            {
                // tick active texture
                BlockState state = this.level.getBlockState(this.getBlockPos());
                this.level.sendBlockUpdated(this.getBlockPos(), state, state, 3);
            }

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

    public boolean hasValidSpout()
    {
        return validSpout;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerGeothermal(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.geothermal_generator");
    }
}
