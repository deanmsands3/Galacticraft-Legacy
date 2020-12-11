package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.world.EnumAtmosphericGas;
import team.galacticraft.galacticraft.common.api.world.IGalacticraftDimension;
import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.blocks.BlockOxygenCollector;
import team.galacticraft.galacticraft.common.core.energy.item.ItemElectricBase;
import team.galacticraft.galacticraft.common.core.Annotations.NetworkedField;
import team.galacticraft.galacticraft.common.core.fluid.GCFluids;
import team.galacticraft.galacticraft.common.core.inventory.ContainerOxygenCollector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import me.shedaniel.architectury.fluid.FluidStack;
import net.minecraftforge.registries.ObjectHolder;

import java.util.EnumSet;

public class TileEntityOxygenCollector extends TileEntityOxygen implements MenuProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.oxygenCollector)
    public static BlockEntityType<TileEntityOxygenCollector> TYPE;

    public boolean active;
    public static final int OUTPUT_PER_TICK = 100;
    public static float OXYGEN_PER_PLANT = 0.75F;
    @NetworkedField(targetSide = EnvType.CLIENT)
    public float lastOxygenCollected;
    private boolean noAtmosphericOxygen = true;
    private boolean isInitialised = false;
    private boolean producedLastTick = false;

    public TileEntityOxygenCollector()
    {
        super(TYPE, 6000, 0);
        this.noRedstoneControl = true;
        inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    public int getCappedScaledOxygenLevel(int scale)
    {
        return (int) Math.max(Math.min(Math.floor((double) this.getOxygenStored() / (double) this.getMaxOxygenStored() * scale), scale), 0);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.level.isClientSide)
        {
            producedLastTick = this.getOxygenStored() < this.getMaxOxygenStored();

            this.produceOxygen();

            // if (this.getEnergyStored() > 0)
            // {
            // int gasToSend = Math.min(this.storedOxygen,
            // GCCoreTileEntityOxygenCollector.OUTPUT_PER_TICK);
            // GasStack toSend = new GasStack(GalacticraftCore.gasOxygen,
            // gasToSend);
            // this.storedOxygen -= GasTransmission.emitGasToNetwork(toSend,
            // this, this.getOxygenOutputDirection());
            //
            // Vector3 thisVec = new Vector3(this);
            // TileEntity tileEntity =
            // thisVec.modifyPositionFromSide(this.getOxygenOutputDirection()).getTileEntity(this.world);
            //
            // if (tileEntity instanceof IGasAcceptor)
            // {
            // if (((IGasAcceptor)
            // tileEntity).canReceiveGas(this.getOxygenOutputDirection().getOpposite(),
            // GalacticraftCore.gasOxygen))
            // {
            // double sendingGas = 0;
            //
            // if (this.storedOxygen >=
            // GCCoreTileEntityOxygenCollector.OUTPUT_PER_TICK)
            // {
            // sendingGas = GCCoreTileEntityOxygenCollector.OUTPUT_PER_TICK;
            // }
            // else
            // {
            // sendingGas = this.storedOxygen;
            // }
            //
            // this.storedOxygen -= sendingGas - ((IGasAcceptor)
            // tileEntity).receiveGas(new GasStack(GalacticraftCore.gasOxygen,
            // (int) Math.floor(sendingGas)));
            // }
            // }
            // }

            //Approximately once every 40 ticks, search out oxygen producing blocks
            if (this.level.random.nextInt(10) == 0)
            {
                if (this.hasEnoughEnergyToRun)
                {
                    // The later calculations are more efficient if power is a float, so
                    // there are fewer casts
                    float nearbyLeaves = 0;

                    if (!this.isInitialised)
                    {
                        this.noAtmosphericOxygen = (this.level.getDimension() instanceof IGalacticraftDimension && !((IGalacticraftDimension) this.level.getDimension()).isGasPresent(EnumAtmosphericGas.OXYGEN));
                        this.isInitialised = true;
                    }

                    if (this.noAtmosphericOxygen)
                    {
                        // Pre-test to see if close to the map edges, so code
                        // doesn't have to continually test for map edges inside the
                        // loop
                        if (this.getBlockPos().getX() > -29999995 && this.getBlockPos().getY() < 2999995 && this.getBlockPos().getZ() > -29999995 && this.getBlockPos().getZ() < 29999995)
                        {
                            // Test the y coordinates, so code doesn't have to keep
                            // testing that either
                            int miny = this.getBlockPos().getY() - 5;
                            int maxy = this.getBlockPos().getY() + 5;
                            if (miny < 0)
                            {
                                miny = 0;
                            }
                            if (maxy >= this.level.getMaxBuildHeight())
                            {
                                maxy = this.level.getMaxBuildHeight() - 1;
                            }

                            // Loop the x and the z first, so the y loop will be at
                            // fixed (x,z) coordinates meaning fixed chunk
                            // coordinates
                            for (int x = this.getBlockPos().getX() - 5; x <= this.getBlockPos().getX() + 5; x++)
                            {
                                int chunkx = x >> 4;
                                int intrachunkx = x & 15;
                                // Preload the first chunk for the z loop - there
                                // can be a maximum of 2 chunks in the z loop
                                int chunkz = this.getBlockPos().getZ() - 5 >> 4;
                                LevelChunk chunk = this.level.getChunk(chunkx, chunkz);
                                for (int z = this.getBlockPos().getZ() - 5; z <= this.getBlockPos().getZ() + 5; z++)
                                {
                                    if (z >> 4 != chunkz)
                                    {
                                        // moved across z chunk boundary into a new
                                        // chunk, so load the new chunk
                                        chunkz = z >> 4;
                                        chunk = this.level.getChunk(chunkx, chunkz);
                                    }
                                    for (int y = miny; y <= maxy; y++)
                                    {
                                        // chunk.getBlockID is like world.getBlock
                                        // but faster - needs to be given
                                        // intra-chunk coordinates though
                                        final BlockState state = chunk.getBlockState(new BlockPos(intrachunkx, y, z & 15));
                                        // Test for the two most common blocks (air
                                        // and breatheable air) without looking up
                                        // in the blocksList
                                        if (!(state.getBlock() instanceof AirBlock))
                                        {
                                            BlockPos pos = new BlockPos(x, y, z);
                                            if (state.is(BlockTags.LEAVES) || state.getBlock() instanceof IPlantable && ((IPlantable) state.getBlock()).getPlantType(this.level, pos) == PlantType.Crop)
                                            {
                                                nearbyLeaves += OXYGEN_PER_PLANT;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        nearbyLeaves = 9.3F * 10F;
                    }

                    nearbyLeaves = (float) Math.floor(nearbyLeaves);

                    this.lastOxygenCollected = nearbyLeaves / 10F;

                    this.tank.setFluid(new FluidStack(GCFluids.OXYGEN.getFluid(), (int) Math.max(Math.min(this.getOxygenStored() + nearbyLeaves, this.getMaxOxygenStored()), 0)));
                }
                else
                {
                    this.lastOxygenCollected = 0;
                }
            }
        }
    }

    // ISidedInventory Implementation:

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
    public boolean shouldUseEnergy()
    {
        return this.getOxygenStored() > 0F && producedLastTick;
    }

    @Override
    public Direction getFront()
    {
        BlockState state = this.level.getBlockState(getBlockPos());
        if (state.getBlock() instanceof BlockOxygenCollector)
        {
            return state.getValue(BlockOxygenCollector.FACING);
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
    public boolean shouldPullOxygen()
    {
        return false;
    }

//    @Override
//    public boolean canReceiveGas(EnumFacing EnvType, Gas type)
//    {
//    	return false;
//    }
//
//    @Override
//    public int receiveGas(EnumFacing EnvType, GasStack stack, boolean doTransfer)
//    {
//    	return 0;
//    }
//
//    @Override
//    public int receiveGas(EnumFacing EnvType, GasStack stack)
//    {
//    	return 0;
//    }

    @Override
    public boolean shouldUseOxygen()
    {
        return false;
    }

    @Override
    public EnumSet<Direction> getOxygenInputDirections()
    {
        return EnumSet.noneOf(Direction.class);
    }

    @Override
    public EnumSet<Direction> getOxygenOutputDirections()
    {
        return EnumSet.of(this.getElectricInputDirection().getOpposite());
    }

    @Override
    public int getOxygenProvide(Direction direction)
    {
        return this.getOxygenOutputDirections().contains(direction) ? Math.min(TileEntityOxygenStorageModule.OUTPUT_PER_TICK, this.getOxygenStored()) : 0;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerOxygenCollector(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.oxygen_collector");
    }
}
