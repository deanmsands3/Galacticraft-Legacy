package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlockNames;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class TileEntityTelepadFake extends TileBaseElectricBlock
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + AsteroidBlockNames.fakeTelepad)
    public static BlockEntityType<TileEntityTelepadFake> TYPE;

    // The the position of the main block
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public BlockPos mainBlockPosition;
    private WeakReference<TileEntityShortRangeTelepad> mainTelepad = null;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    private boolean canConnect = false;

    public TileEntityTelepadFake()
    {
        super(TYPE);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }

    public void setMainBlock(BlockPos mainBlock)
    {
        this.setMainBlockInternal(mainBlock);

        if (!this.level.isClientSide)
        {
            BlockState state = this.level.getBlockState(this.getBlockPos());
            this.level.sendBlockUpdated(this.getBlockPos(), state, state, 3);
        }
    }

    private void setMainBlockInternal(BlockPos mainBlock)
    {
        this.mainBlockPosition = mainBlock;
        this.updateConnectable();
    }

    public void onBlockRemoval()
    {
        TileEntityShortRangeTelepad telepad = this.getBaseTelepad();

        if (telepad != null)
        {
            telepad.onDestroy(this);
        }
    }

    public InteractionResult onActivated(Player par5EntityPlayer)
    {
        TileEntityShortRangeTelepad telepad = this.getBaseTelepad();
        if (telepad == null)
        {
            return InteractionResult.FAIL;
        }
        return telepad.onActivated(par5EntityPlayer);
    }

    @Override
    public void tick()
    {
        super.tick();

        TileEntityShortRangeTelepad telepad = this.getBaseTelepad();

        if (telepad != null)
        {
            this.storage.setCapacity(telepad.storage.getCapacityGC());
            this.storage.setMaxExtract(telepad.storage.getMaxExtract());
            this.storage.setMaxReceive(telepad.storage.getMaxReceive());
            this.extractEnergyGC(null, telepad.receiveEnergyGC(null, this.getEnergyStoredGC(), false), false);
        }
    }

    private TileEntityShortRangeTelepad getBaseTelepad()
    {
        if (this.mainBlockPosition == null)
        {
            return null;
        }

        if (mainTelepad == null)
        {
            BlockEntity tileEntity = this.level.getBlockEntity(this.mainBlockPosition);

            if (tileEntity != null)
            {
                if (tileEntity instanceof TileEntityShortRangeTelepad)
                {
                    mainTelepad = new WeakReference<TileEntityShortRangeTelepad>(((TileEntityShortRangeTelepad) tileEntity));
                }
            }
        }

        if (mainTelepad == null)
        {
            this.level.removeBlock(this.mainBlockPosition, false);
        }
        else
        {
            TileEntityShortRangeTelepad telepad = this.mainTelepad.get();

            if (telepad != null)
            {
                return telepad;
            }
            else
            {
                this.level.removeBlockEntity(this.getBlockPos());
            }
        }

        return null;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        CompoundTag tagCompound = nbt.getCompound("mainBlockPosition");
        this.setMainBlockInternal(new BlockPos(tagCompound.getInt("x"), tagCompound.getInt("y"), tagCompound.getInt("z")));
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);

        if (this.mainBlockPosition != null)
        {
            CompoundTag tagCompound = new CompoundTag();
            tagCompound.putInt("x", this.mainBlockPosition.getX());
            tagCompound.putInt("y", this.mainBlockPosition.getY());
            tagCompound.putInt("z", this.mainBlockPosition.getZ());
            nbt.put("mainBlockPosition", tagCompound);
        }

        return nbt;
    }

    @Override
    public double getPacketRange()
    {
        return 30.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 50;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return true;
    }

    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        if (this.mainBlockPosition == null)
        {
            if (this.level.isClientSide || !this.resetMainBlockPosition())
            {
                return;
            }
        }
        super.getNetworkedData(sendData);
    }

    private boolean resetMainBlockPosition()
    {
        for (int x = -1; x <= 1; x++)
        {
            for (int z = -1; z <= 1; z++)
            {
                for (int y = -2; y < 1; y += 2)
                {
                    final BlockPos vecToCheck = this.getBlockPos().offset(x, y, z);
                    if (this.level.getBlockEntity(vecToCheck) instanceof TileEntityShortRangeTelepad)
                    {
                        this.setMainBlock(vecToCheck);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return false;
    }

    @Override
    public Direction getElectricInputDirection()
    {
        if (!this.canConnect)
        {
            return null;
        }

        return Direction.UP;
    }

    @Override
    public Direction getFront()
    {
        return Direction.NORTH;
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return ItemStack.EMPTY;
    }

    private void updateConnectable()
    {
        if (this.mainBlockPosition != null)
        {
            if (this.getBlockPos().getX() == mainBlockPosition.getX() && this.getBlockPos().getZ() == mainBlockPosition.getZ())
            {
                if (this.getBlockPos().getY() > mainBlockPosition.getY())
                {
                    // If the block has the same x- and y- coordinates, but is above the base block, this is the
                    //      connectable tile
                    this.canConnect = true;
                    return;
                }
            }
        }

        this.canConnect = false;
    }
}
