package micdoodle8.mods.galacticraft.core.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.tile.TileEntityParaChest;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Collections;

public class EntityParachest extends Entity implements IPacketReceiver
{
    public NonNullList<ItemStack> cargo;
    public int fuelLevel;
    private boolean placedChest;
    public DyeColor color = DyeColor.WHITE;

    public EntityParachest(Level world, NonNullList<ItemStack> cargo, int fuelLevel)
    {
        super(GCEntities.PARA_CHEST, world);
        this.cargo = NonNullList.withSize(cargo.size(), ItemStack.EMPTY);
        Collections.copy(this.cargo, cargo);
        this.placedChest = false;
        this.fuelLevel = fuelLevel;
    }

    public EntityParachest(EntityType<EntityParachest> type, Level world)
    {
        super(type, world);
    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData()
    {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt)
    {
        int size = 56;
        if (nbt.contains("CargoLength"))
        {
            size = nbt.getInt("CargoLength");
        }
        this.cargo = NonNullList.withSize(size, ItemStack.EMPTY);

        ContainerHelper.loadAllItems(nbt, this.cargo);

        this.placedChest = nbt.getBoolean("placedChest");
        this.fuelLevel = nbt.getInt("FuelLevel");

        if (nbt.contains("color"))
        {
            this.color = DyeColor.values()[nbt.getInt("color")];
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt)
    {
        if (level.isClientSide)
        {
            return;
        }
        nbt.putInt("CargoLength", this.cargo.size());
        ContainerHelper.saveAllItems(nbt, this.cargo);

        nbt.putBoolean("placedChest", this.placedChest);
        nbt.putInt("FuelLevel", this.fuelLevel);
        nbt.putInt("color", this.color.ordinal());
    }

    @Override
    public void tick()
    {
        if (!this.placedChest)
        {
            if (this.onGround && !this.level.isClientSide)
            {
                for (int i = 0; i < 100; i++)
                {
                    final int x = Mth.floor(this.getX());
                    final int y = Mth.floor(this.getY());
                    final int z = Mth.floor(this.getZ());

                    if (tryPlaceAtPos(new BlockPos(x, y + i, z)))
                    {
                        return;
                    }
                }

                for (int size = 1; size < 5; ++size)
                {
                    for (int xOff = -size; xOff <= size; xOff++)
                    {
                        for (int yOff = -size; yOff <= size; yOff++)
                        {
                            for (int zOff = -size; zOff <= size; zOff++)
                            {
                                final int x = Mth.floor(this.getX()) + xOff;
                                final int y = Mth.floor(this.getY()) + yOff;
                                final int z = Mth.floor(this.getZ()) + zOff;

                                if (tryPlaceAtPos(new BlockPos(x, y, z)))
                                {
                                    return;
                                }
                            }
                        }
                    }
                }

                if (this.cargo != null)
                {
                    for (final ItemStack stack : this.cargo)
                    {
                        final ItemEntity e = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), stack);
                        this.level.addFreshEntity(e);
                    }
                }

                this.placedChest = true;
                this.remove();
            }
            else
            {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.35, 0.0));
            }

            this.setDeltaMovement(0.0, this.getDeltaMovement().y, 0.0);
            this.move(MoverType.SELF, this.getDeltaMovement());
        }

        if (!this.level.isClientSide && this.tickCount % 5 == 0)
        {
            GalacticraftCore.packetPipeline.sendToAllAround(new PacketDynamic(this), new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 64.0, GCCoreUtil.getDimensionType(this.level)));
        }
    }

    private boolean tryPlaceAtPos(BlockPos pos)
    {
        BlockState state = this.level.getBlockState(pos);
        Block block = state.getBlock();

        if (block.getMaterial(state).isReplaceable())
        {
            if (this.placeChest(pos))
            {
                this.placedChest = true;
                this.remove();
                return true;
            }
        }
        return false;
    }

    private boolean placeChest(BlockPos pos)
    {
        if (this.level.setBlock(pos, GCBlocks.parachest.defaultBlockState(), 3))
        {
            if (this.cargo != null)
            {
                final BlockEntity te = this.level.getBlockEntity(pos);

                if (te instanceof TileEntityParaChest)
                {
                    final TileEntityParaChest chest = (TileEntityParaChest) te;

                    chest.inventory = NonNullList.withSize(this.cargo.size() + 1, ItemStack.EMPTY);
                    chest.color = this.color;

                    Collections.copy(chest.getInventory(), this.cargo);

                    chest.fuelTank.fill(new FluidStack(GCFluids.FUEL.getFluid(), this.fuelLevel), IFluidHandler.FluidAction.EXECUTE);
                }
                else
                {
                    for (ItemStack stack : this.cargo)
                    {
                        final ItemEntity e = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), stack);
                        this.level.addFreshEntity(e);
                    }
                }
            }
            return true;
        }

        return false;
    }

    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        if (!this.level.isClientSide)
        {
            sendData.add(this.color.getId());
        }
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        if (this.level.isClientSide)
        {
            this.color = DyeColor.byId(buffer.readInt());
        }
    }
}
