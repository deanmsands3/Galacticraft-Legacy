package micdoodle8.mods.galacticraft.core.tile;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.GCBlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.IScaleableFuelLevel;
import micdoodle8.mods.galacticraft.core.inventory.ContainerParaChest;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.network.PacketDynamicInventory;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Iterator;
import java.util.List;

public class TileEntityParaChest extends TileEntityAdvanced implements IInventorySettable, IScaleableFuelLevel
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.parachest)
    public static BlockEntityType<TileEntityParaChest> TYPE;

    private final int tankCapacity = 5000;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public FluidTank fuelTank = new FluidTank(this.tankCapacity);

    public boolean adjacentChestChecked = false;
    public float lidAngle;
    public float prevLidAngle;
    public int numUsingPlayers;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public DyeColor color = DyeColor.RED;

    public TileEntityParaChest()
    {
        super(TYPE);
        this.color = DyeColor.RED;
        inventory = NonNullList.withSize(3, ItemStack.EMPTY);
    }

    @Override
    public void onLoad()
    {
        if (this.level.isClientSide)
        {
            //Request size + contents information from server
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamicInventory(this));
        }
    }

    @Override
    public int getScaledFuelLevel(int i)
    {
        final double fuelLevel = this.fuelTank.getFluid() == FluidStack.EMPTY ? 0 : this.fuelTank.getFluid().getAmount();

        return (int) (fuelLevel * i / this.tankCapacity);
    }

    @Override
    public void setSizeInventory(int size)
    {
        this.inventory = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    protected boolean handleInventory()
    {
        // Custom size loading, so handle in this class
        return false;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);

        int size = nbt.getInt("chestContentLength");
        if ((size - 3) % 18 != 0)
        {
            size += 18 - ((size - 3) % 18);
        }
        this.inventory = NonNullList.withSize(size, ItemStack.EMPTY);

        ContainerHelper.loadAllItems(nbt, this.getInventory());

        if (nbt.contains("fuelTank"))
        {
            this.fuelTank.readFromNBT(nbt.getCompound("fuelTank"));
        }

        if (nbt.contains("color"))
        {
            this.color = DyeColor.values()[nbt.getInt("color")];
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);

        nbt.putInt("chestContentLength", this.getInventory().size());
        ContainerHelper.saveAllItems(nbt, this.getInventory());

        if (this.fuelTank.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("fuelTank", this.fuelTank.writeToNBT(new CompoundTag()));
        }

        nbt.putInt("color", this.color.ordinal());
        return nbt;
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        return this.save(new CompoundTag());
    }

    @Override
    public void clearCache()
    {
        super.clearCache();
        this.adjacentChestChecked = false;
    }

    @Override
    public void tick()
    {
        super.tick();
        float f;

        if (!this.level.isClientSide && this.numUsingPlayers != 0 && (this.ticks + this.getBlockPos().getX() + this.getBlockPos().getY() + this.getBlockPos().getZ()) % 200 == 0)
        {
            this.numUsingPlayers = 0;
            f = 5.0F;
            List<?> list = this.level.getEntitiesOfClass(Player.class, new AABB(this.getBlockPos().getX() - f, this.getBlockPos().getY() - f, this.getBlockPos().getZ() - f, this.getBlockPos().getX() + 1 + f, this.getBlockPos().getY() + 1 + f, this.getBlockPos().getZ() + 1 + f));
            Iterator<?> iterator = list.iterator();

            while (iterator.hasNext())
            {
                Player entityplayer = (Player) iterator.next();

                if (entityplayer.containerMenu instanceof ContainerParaChest)
                {
                    ++this.numUsingPlayers;
                }
            }
        }

        this.prevLidAngle = this.lidAngle;
        f = 0.1F;
        double d0;

        if (this.numUsingPlayers > 0 && this.lidAngle == 0.0F)
        {
            double d1 = this.getBlockPos().getX() + 0.5D;
            d0 = this.getBlockPos().getZ() + 0.5D;

            this.level.playSound(null, d1, this.getBlockPos().getY() + 0.5D, d0, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numUsingPlayers == 0 && this.lidAngle > 0.0F || this.numUsingPlayers > 0 && this.lidAngle < 1.0F)
        {
            float f1 = this.lidAngle;

            if (this.numUsingPlayers > 0)
            {
                this.lidAngle += f;
            }
            else
            {
                this.lidAngle -= f;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }

            float f2 = 0.5F;

            if (this.lidAngle < f2 && f1 >= f2)
            {
                d0 = this.getBlockPos().getX() + 0.5D;
                double d2 = this.getBlockPos().getZ() + 0.5D;

                this.level.playSound(null, d0, this.getBlockPos().getY() + 0.5D, d2, SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }

        if (!this.level.isClientSide)
        {
            this.checkFluidTankTransfer(this.getInventory().size() - 1, this.fuelTank);
        }
    }

    private void checkFluidTankTransfer(int slot, FluidTank tank)
    {
        FluidUtil.tryFillContainerFuel(tank, this.getInventory(), slot);
    }

    @Override
    public boolean triggerEvent(int par1, int par2)
    {
        if (par1 == 1)
        {
            this.numUsingPlayers = par2;
            return true;
        }
        else
        {
            return super.triggerEvent(par1, par2);
        }
    }

    @Override
    public void startOpen(Player player)
    {
        if (this.numUsingPlayers < 0)
        {
            this.numUsingPlayers = 0;
        }

        ++this.numUsingPlayers;
        this.level.blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), 1, this.numUsingPlayers);
        this.level.updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
        this.level.updateNeighborsAt(this.getBlockPos().below(), this.getBlockState().getBlock());
    }

    @Override
    public void stopOpen(Player player)
    {
        --this.numUsingPlayers;
        this.level.blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), 1, this.numUsingPlayers);
        this.level.updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
        this.level.updateNeighborsAt(this.getBlockPos().below(), this.getBlockState().getBlock());
    }

    @Override
    public boolean canPlaceItem(int par1, ItemStack par2ItemStack)
    {
        return true;
    }

    @Override
    public void setRemoved()
    {
        super.setRemoved();
        this.clearCache();
    }

    @Override
    public double getPacketRange()
    {
        return 12.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 3;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return true;
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        DyeColor color = this.color;

        super.decodePacketdata(buffer);

//        if (this.world.isRemote && color != this.color)
//        {
//            this.world.markBlockRangeForRenderUpdate(getPos(), getPos()); TODO Necessary?
//        }
    }
}
