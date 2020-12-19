package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.tile.IDisableableMachine;
import team.galacticraft.galacticraft.common.api.transmission.NetworkType;
import team.galacticraft.galacticraft.common.api.transmission.tile.IConnector;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import team.galacticraft.galacticraft.common.api.world.IGalacticraftDimension;
import team.galacticraft.galacticraft.common.api.world.ISolarLevel;
import team.galacticraft.galacticraft.common.core.Annotations.NetworkedField;
import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GCBlocks;
import team.galacticraft.galacticraft.common.core.blocks.BlockMulti;
import team.galacticraft.galacticraft.common.core.blocks.BlockMulti.EnumBlockMultiType;
import team.galacticraft.galacticraft.common.core.blocks.BlockSolar;
import team.galacticraft.galacticraft.common.core.energy.item.ItemElectricBase;
import team.galacticraft.galacticraft.common.core.energy.tile.TileBaseUniversalElectricalSource;
import team.galacticraft.galacticraft.common.core.inventory.ContainerSolar;
import team.galacticraft.galacticraft.common.core.inventory.IInventoryDefaults;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
//import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

public abstract class TileEntitySolar extends TileBaseUniversalElectricalSource implements IMultiBlock, IDisableableMachine, IInventoryDefaults, WorldlyContainer, IConnector, MenuProvider
{
    public static class TileEntitySolarT1 extends TileEntitySolar
    {
    //    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.solarPanel)
        public static BlockEntityType<TileEntitySolarT1> TYPE;

        public TileEntitySolarT1()
        {
            super(TYPE);
            this.storage.setMaxExtract(TileEntitySolar.MAX_GENERATE_WATTS);
            this.storage.setMaxReceive(TileEntitySolar.MAX_GENERATE_WATTS);
            this.setTierGC(1);
//            this.initialised = true;
            this.inventory = NonNullList.withSize(1, ItemStack.EMPTY);
        }

        @Override
        public Component getDisplayName()
        {
            return new TranslatableComponent("container.solar_basic");
        }
    }

    public static class TileEntitySolarT2 extends TileEntitySolar
    {
    //    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.solarPanelAdvanced)
        public static BlockEntityType<TileEntitySolarT2> TYPE;

        public TileEntitySolarT2()
        {
            super(TYPE);
            this.storage.setMaxExtract(TileEntitySolar.MAX_GENERATE_WATTS);
            this.storage.setMaxReceive(TileEntitySolar.MAX_GENERATE_WATTS);
            this.storage.setCapacity(30000);
            this.setTierGC(2);
//            this.initialised = true;
            this.inventory = NonNullList.withSize(1, ItemStack.EMPTY);
        }

        @Override
        public Component getDisplayName()
        {
            return new TranslatableComponent("container.solar_advanced");
        }
    }

    @NetworkedField(targetSide = EnvType.CLIENT)
    public int solarStrength = 0;
    public float targetAngle;
    public float currentAngle;
    @NetworkedField(targetSide = EnvType.CLIENT)
    public boolean disabled = false;
    @NetworkedField(targetSide = EnvType.CLIENT)
    public int disableCooldown = 0;
    public static final int MAX_GENERATE_WATTS = 200;
    @NetworkedField(targetSide = EnvType.CLIENT)
    public int generateWatts = 0;

    //    protected boolean initialised = false;
    private boolean initialisedMulti = false;
    private AABB renderAABB;

    public TileEntitySolar(BlockEntityType<?> type)
    {
        super(type);
    }

    @Override
    public void tick()
    {
//        if (!this.initialised)
//        {
//            int metadata = this.getBlockMetadata();
//            if (metadata >= BlockSolar.ADVANCED_METADATA)
//            {
//                this.storage.setCapacity(30000);
//                this.setTierGC(2);
//            }
//            this.initialised = true;
//        }

        if (!this.initialisedMulti)
        {
            this.initialisedMulti = this.initialiseMultiTiles(this.getBlockPos(), this.level);
        }

        if (!this.level.isClientSide)
        {
            this.receiveEnergyGC(null, this.generateWatts, false);
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

                if (this.level.isDay() && (this.level.getDimension() instanceof IGalacticraftDimension || !this.level.isRaining() && !this.level.isThundering()))
                {
                    double distance = 100.0D;
                    double sinA = -Math.sin((this.currentAngle - 77.5D) / Constants.RADIANS_TO_DEGREES_D);
                    double cosA = Math.abs(Math.cos((this.currentAngle - 77.5D) / Constants.RADIANS_TO_DEGREES_D));

                    for (int x = -1; x <= 1; x++)
                    {
                        for (int z = -1; z <= 1; z++)
                        {
                            if (this.tierGC == 1)
                            {
                                if (this.level.canSeeSkyFromBelowWater(this.getBlockPos().offset(x, 2, z)))
                                {
                                    boolean valid = true;

                                    for (int y = this.getBlockPos().getY() + 3; y < 256; y++)
                                    {
                                        BlockPos atPos = new BlockPos(this.getBlockPos().getX() + x, y, this.getBlockPos().getZ() + z);
                                        BlockState state = this.level.getBlockState(atPos);

                                        if (state.getBlock().isSolidRender(state, level, atPos))
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
                            else
                            {
                                boolean valid = true;

                                BlockVec3 blockVec = new BlockVec3(this).translate(x, 3, z);
                                for (double d = 0.0D; d < distance; d++)
                                {
                                    BlockVec3 blockAt = blockVec.clone().translate((int) (d * sinA), (int) (d * cosA), 0);
                                    BlockState state = blockAt.getBlockState(this.level);

                                    if (state == null)
                                    {
                                        break;
                                    }

                                    if (state.getBlock().isSolidRender(state, level, blockAt.toBlockPos()))
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
            }
        }

        float angle = this.level.getTimeOfDay(1.0F) - 0.7845194F < 0 ? 1.0F - 0.7845194F : -0.7845194F;
        float celestialAngle = (this.level.getTimeOfDay(1.0F) + angle) * 360.0F;
//        if (!(this.world.getDimension() instanceof DimensionSpaceStation)) celestialAngle += 12.5F; TODO Space stations
//        if (GalacticraftCore.isPlanetsLoaded && this.world.getDimension() instanceof WorldProviderVenus) celestialAngle = 180F - celestialAngle; TODO planets
        celestialAngle %= 360;
        boolean isDaytime = this.level.isDay() && (celestialAngle < 180.5F || celestialAngle > 359.5F)/* || this.world.getDimension() instanceof DimensionSpaceStation  TODO Space stations*/;

        if (this.tierGC == 1)
        {
            if (!isDaytime || this.level.isRaining() || this.level.isThundering())
            {
                this.targetAngle = 77.5F + 180.0F;
            }
            else
            {
                this.targetAngle = 77.5F;
            }
        }
        else
        {
            if (!isDaytime || this.level.isRaining() || this.level.isThundering())
            {
                this.targetAngle = 77.5F + 180F;
            }
            else if (celestialAngle > 27.5F && celestialAngle < 152.5F)
            {
                float difference = this.targetAngle - celestialAngle + 12.5F;

                this.targetAngle -= difference / 20.0F;
            }
            else if (celestialAngle <= 27.5F || celestialAngle > 270F)
            {
                this.targetAngle = 15F;
            }
            else if (celestialAngle >= 152.5F)
            {
                this.targetAngle = 140F;
            }
        }

        float difference = this.targetAngle - this.currentAngle;

        this.currentAngle += difference / 20.0F;

        if (!this.level.isClientSide)
        {
            int generated = this.getGenerate();
            if (generated > 0)
            {
                this.generateWatts = Math.min(Math.max(generated, 0), TileEntitySolar.MAX_GENERATE_WATTS);
            }
            else
            {
                this.generateWatts = 0;
            }
        }

        this.produce();
    }

    protected boolean initialiseMultiTiles(BlockPos pos, Level world)
    {
        //Client can create its own fake blocks and tiles - no need for networking in 1.8+
        if (world.isClientSide)
        {
            this.onCreate(world, pos);
        }

        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(pos, positions);
        boolean result = true;
        for (BlockPos vecToAdd : positions)
        {
            BlockEntity tile = world.getBlockEntity(vecToAdd);
            if (tile instanceof TileEntityFake)
            {
                ((TileEntityFake) tile).mainBlockPosition = pos;
            }
            else
            {
                result = false;
            }
        }
        return result;
    }

    public int getGenerate()
    {
        if (this.getDisabled(0))
        {
            return 0;
        }

        float angle = this.level.getTimeOfDay(1.0F) - 0.784690560F < 0 ? 1.0F - 0.784690560F : -0.784690560F;
        float celestialAngle = (this.level.getTimeOfDay(1.0F) + angle) * 360.0F;
//        if (!(this.world.getDimension() instanceof DimensionSpaceStation)) celestialAngle += 12.5F; TODO Space stations

//        if (GalacticraftCore.isPlanetsLoaded && this.world.getDimension() instanceof WorldProviderVenus) celestialAngle = 180F - celestialAngle; TODO Planets
        celestialAngle %= 360F;

        float difference = (180.0F - Math.abs((this.currentAngle + 12.5F) % 180F - celestialAngle)) / 180.0F;

        return Mth.floor(0.01F * difference * difference * (this.solarStrength * (Math.abs(difference) * 500.0F)) * this.getSolarBoost());
    }

    public float getSolarBoost()
    {
        float result = (float) (this.level.getDimension() instanceof ISolarLevel ? ((ISolarLevel) this.level.getDimension()).getSolarEnergyMultiplier() : 1.0F);
//        if (GalacticraftCore.isPlanetsLoaded && this.world.getDimension() instanceof WorldProviderVenus)
//        {
//            if (this.pos.getY() > 90)
//            {
//                result += (this.pos.getY() - 90) / 1000F;   //Small improvement on Venus at higher altitudes
//            }
//        } TODO Planets
        return result;
    }

    @Override
    public InteractionResult onActivated(Player entityPlayer)
    {
        return InteractionResult.PASS; // TODO
//        return this.getBlockType().onBlockActivated(this.world, this.getPos(), this.world.getBlockState(getPos()), entityPlayer, EnumFacing.DOWN, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
    }

//    @Override
//    public boolean canUpdate()
//    {
//        return true;
//    }

    @Override
    public void onCreate(Level world, BlockPos placedPosition)
    {
        List<BlockPos> positions = new LinkedList<>();
        this.getPositions(placedPosition, positions);
        if (positions.size() > 0)
        {
            ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, positions.get(0), placedPosition, EnumBlockMultiType.SOLAR_PANEL_0);
            positions.remove(0);
        }
        ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, positions, placedPosition, this.getMultiType());
    }

    @Override
    public EnumBlockMultiType getMultiType()
    {
        return (this.getTierGC() == 1) ? EnumBlockMultiType.SOLAR_PANEL_1 : EnumBlockMultiType.SOLAR_PANEL_0;
    }

    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int buildHeight = this.level.getMaxBuildHeight() - 1;
        int y = placedPosition.getY() + 1;
        if (y > buildHeight)
        {
            return;
        }
        positions.add(new BlockPos(placedPosition.getX(), y, placedPosition.getZ()));

        y++;
        if (y > buildHeight)
        {
            return;
        }
        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                positions.add(new BlockPos(placedPosition.getX() + x, y, placedPosition.getZ() + z));
            }
        }
    }

    @Override
    public void onDestroy(BlockEntity callingBlock)
    {
        final BlockPos thisBlock = getBlockPos();
        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            BlockState stateAt = this.level.getBlockState(pos);

            if (stateAt.getBlock() == GCBlocks.fakeBlock)
            {
                EnumBlockMultiType type = stateAt.getValue(BlockMulti.MULTI_TYPE);
                if ((type == EnumBlockMultiType.SOLAR_PANEL_0 || type == EnumBlockMultiType.SOLAR_PANEL_1))
                {
                    if (this.level.isClientSide && this.level.random.nextDouble() < 0.1D)
                    {
                        Minecraft.getInstance().particleEngine.destroy(pos, GCBlocks.solarPanel.defaultBlockState());
                    }

                    this.level.removeBlock(pos, false);
                }
            }
        }

        this.level.destroyBlock(getBlockPos(), true);
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.storage.setCapacity(nbt.getFloat("maxEnergy"));
        this.currentAngle = nbt.getFloat("currentAngle");
        this.targetAngle = nbt.getFloat("targetAngle");
        this.setDisabled(0, nbt.getBoolean("disabled"));
        this.disableCooldown = nbt.getInt("disabledCooldown");

//        this.initialised = false;
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putFloat("maxEnergy", this.getMaxEnergyStoredGC());
        nbt.putFloat("currentAngle", this.currentAngle);
        nbt.putFloat("targetAngle", this.targetAngle);
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
        if (state.getBlock() instanceof BlockSolar)
        {
            return state.getValue(BlockSolar.FACING);
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

    @Override
    @Environment(EnvType.CLIENT)
    public AABB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AABB(getBlockPos().getX() - 1, getBlockPos().getY(), getBlockPos().getZ() - 1, getBlockPos().getX() + 2, getBlockPos().getY() + 4, getBlockPos().getZ() + 2);
        }
        return this.renderAABB;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public double getViewDistance()
    {
        return Constants.RENDERDISTANCE_LONG;
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
        if (direction == null || type != NetworkType.POWER)
        {
            return false;
        }

        return direction == this.getElectricOutputDirection();
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerSolar(containerId, playerInv, this);
    }
}
