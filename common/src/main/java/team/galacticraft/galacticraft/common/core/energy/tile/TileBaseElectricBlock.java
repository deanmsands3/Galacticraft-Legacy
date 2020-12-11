package team.galacticraft.galacticraft.common.core.energy.tile;

import team.galacticraft.galacticraft.common.api.tile.IDisableableMachine;
import team.galacticraft.galacticraft.common.api.transmission.NetworkType;
import team.galacticraft.galacticraft.common.api.transmission.tile.IConnector;
import team.galacticraft.galacticraft.common.core.Annotations.NetworkedField;
import team.galacticraft.galacticraft.common.core.util.EnumColor;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import team.galacticraft.galacticraft.common.core.util.RedstoneUtil;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.EnumSet;

public abstract class TileBaseElectricBlock extends TileBaseUniversalElectrical implements IDisableableMachine, IConnector
{
    //	public int energyPerTick = 200;
    //	private final float ueMaxEnergy;

    @NetworkedField(targetSide = EnvType.CLIENT)
    public boolean disabled = false;
    @NetworkedField(targetSide = EnvType.CLIENT)
    public int disableCooldown = 0;
    @NetworkedField(targetSide = EnvType.CLIENT)
    public boolean hasEnoughEnergyToRun = false;
    public boolean noRedstoneControl = false;

    public TileBaseElectricBlock(BlockEntityType<?> type)
    {
        super(type);
    }

    public boolean shouldPullEnergy()
    {
        return this.shouldUseEnergy() || this.getEnergyStoredGC(null) < this.getMaxEnergyStoredGC();
    }

    public abstract boolean shouldUseEnergy();

    public abstract Direction getElectricInputDirection();

    public abstract ItemStack getBatteryInSlot();

    //	public TileBaseElectricBlock()
    //	{
    //		this.storage.setMaxReceive(ueWattsPerTick);
    //		this.storage.setMaxExtract(0);
    //		this.storage.setCapacity(maxEnergy);
    ////		this.ueMaxEnergy = maxEnergy;
    ////		this.ueWattsPerTick = ueWattsPerTick;
    //
    //		/*
    //		 * if (PowerFramework.currentFramework != null) { this.bcPowerProvider =
    //		 * new GCCoreLinkedPowerProvider(this);
    //		 * this.bcPowerProvider.configure(20, 1, 10, 10, 1000); }
    //		 */
    //	}

    //	@Override
    //	public float getMaxEnergyStored()
    //	{
    //		return this.ueMaxEnergy;
    //	}

    public int getScaledElecticalLevel(int i)
    {
        return (int) Math.floor(this.getEnergyStoredGC(null) * i / this.getMaxEnergyStoredGC(null));
        //- this.ueWattsPerTick;
    }

    //	@Override
    //	public float getRequest(EnumFacing direction)
    //	{
    //		if (this.shouldPullEnergy())
    //		{
    //			return this.ueWattsPerTick * 2;
    //		}
    //		else
    //		{
    //			return 0;
    //		}
    //	}
    //
    //	@Override
    //	public float getProvide(EnumFacing direction)
    //	{
    //		return 0;
    //	}

    @Override
    public void tick()
    {
        if (!this.level.isClientSide)
        {
            if (this.shouldPullEnergy() && this.getEnergyStoredGC(null) < this.getMaxEnergyStoredGC(null) && this.getBatteryInSlot() != null && this.getElectricInputDirection() != null)
            {
                this.discharge(this.getBatteryInSlot());
            }

            if (this.getEnergyStoredGC(null) > this.storage.getMaxExtract() && (this.noRedstoneControl || !RedstoneUtil.isBlockReceivingRedstone(this.level, this.getBlockPos())))
            {
                this.hasEnoughEnergyToRun = true;
                if (this.shouldUseEnergy())
                {
                    this.storage.extractEnergyGC(this.storage.getMaxExtract(), false);
                }
                else
                {
                    this.slowDischarge();
                }
            }
            else
            {
                this.hasEnoughEnergyToRun = false;
                this.slowDischarge();
            }
        }

        super.tick();

        if (!this.level.isClientSide)
        {
            if (this.disableCooldown > 0)
            {
                this.disableCooldown--;
            }
        }
    }

    public void slowDischarge()
    {
        if (this.ticks % 10 == 0)
        {
            this.storage.extractEnergyGC(5F, false);
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);

        nbt.putBoolean("isDisabled", this.getDisabled(0));
        return nbt;
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        return this.save(new CompoundTag());
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);

        this.setDisabled(0, nbt.getBoolean("isDisabled"));
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown == 0)
        {
            this.disabled = disabled;
            this.disableCooldown = 10;
        }
    }

    public abstract Direction getFront();

    @Override
    public boolean getDisabled(int index)
    {
        return this.disabled;
    }

//    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = CompatibilityManager.modidIC2)
//    public Direction getFacing(World world, BlockPos pos)
//    {
//        return this.getFront();
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = CompatibilityManager.modidIC2)
//    public boolean setFacing(World world, BlockPos pos, Direction newDirection, PlayerEntity player)
//    {
//        return false;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = CompatibilityManager.modidIC2)
//    public boolean wrenchCanRemove(World world, BlockPos pos, PlayerEntity player)
//    {
//        return false;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = CompatibilityManager.modidIC2)
//    public List<ItemStack> getWrenchDrops(World world, BlockPos pos, BlockState state, TileEntity te, PlayerEntity player, int fortune)
//    {
//        List<ItemStack> drops = Lists.newArrayList();
//        drops.add(this.getBlockType().getCloneItemStack(state, null, this.world, this.getPos(), player));
//        return drops;
//    } TODO IC Support

    @Override
    public EnumSet<Direction> getElectricalInputDirections()
    {
        if (this.getElectricInputDirection() == null)
        {
            return EnumSet.noneOf(Direction.class);
        }

        return EnumSet.of(this.getElectricInputDirection());
    }

    @Override
    public boolean stillValid(Player entityplayer)
    {
        return this.getLevel().getBlockEntity(this.getBlockPos()) == this && entityplayer.distanceToSqr(this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 0.5D, this.getBlockPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        if (direction == null || type != NetworkType.POWER)
        {
            return false;
        }

        return direction == this.getElectricInputDirection();
    }

    public String getGUIstatus()
    {
        if (!this.noRedstoneControl && RedstoneUtil.isBlockReceivingRedstone(this.level, this.getBlockPos()))
        {
            return EnumColor.DARK_RED + I18n.get("gui.status.off");
        }

        if (this.getEnergyStoredGC() == 0)
        {
            return EnumColor.DARK_RED + I18n.get("gui.status.missingpower");
        }

        if (this.getDisabled(0))
        {
            return EnumColor.ORANGE + I18n.get("gui.status.ready");
        }

        if (this.getEnergyStoredGC() < this.storage.getMaxExtract())
        {
            return EnumColor.ORANGE + I18n.get("gui.status.missingpower");
        }

        return EnumColor.DARK_GREEN + I18n.get("gui.status.active");
    }

    /**
     * @param missingInput = dynamically: null if all inputs are present, or a string if an input (e.g. oxygen, fuel) is missing
     * @param activeString = the specific 'Running' / 'Processing' etc string for this machine
     * @return
     */
    public String getGUIstatus(String missingInput, String activeString, boolean shorten)
    {
        if (!this.noRedstoneControl && RedstoneUtil.isBlockReceivingRedstone(this.level, this.getBlockPos()))
        {
            return EnumColor.DARK_RED + I18n.get("gui.status.off");
        }

        if (this.getEnergyStoredGC() == 0)
        {
            return EnumColor.DARK_RED + I18n.get(shorten ? "gui.status.missingpower.short" : "gui.status.missingpower");
        }

        if (missingInput != null)
        {
            return missingInput;
        }

        if (this.getDisabled(0))
        {
            return EnumColor.ORANGE + I18n.get("gui.status.ready");
        }

        if (this.getEnergyStoredGC() < this.storage.getMaxExtract())
        {
            return EnumColor.ORANGE + I18n.get(shorten ? "gui.status.missingpower.short" : "gui.status.missingpower");
        }

        if (activeString != null)
        {
            return activeString;
        }

        return EnumColor.RED + I18n.get("gui.status.unknown");
    }
}
