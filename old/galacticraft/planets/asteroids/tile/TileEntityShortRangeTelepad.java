package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti.EnumBlockMultiType;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import micdoodle8.mods.galacticraft.core.inventory.ContainerRefinery;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlockNames;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.BlockTelepadFake;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.ShortRangeTelepadHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.inventory.ContainerShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.network.PacketSimpleAsteroids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TileEntityShortRangeTelepad extends TileBaseElectricBlock implements IMultiBlock, IInventoryDefaults, WorldlyContainer, MenuProvider
{
    public enum EnumTelepadSearchResult
    {
        VALID,
        NOT_FOUND,
        TOO_FAR,
        WRONG_DIM,
        TARGET_DISABLED
    }

    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + AsteroidBlockNames.SHORT_RANGE_TELEPAD)
    public static BlockEntityType<TileEntityShortRangeTelepad> TYPE;

    public static final int MAX_TELEPORT_TIME = 150;
    public static final int TELEPORTER_RANGE = 256;
    public static final int ENERGY_USE_ON_TELEPORT = 2500;

    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int address = -1;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean addressValid = false;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int targetAddress = -1;
    public EnumTelepadSearchResult targetAddressResult = EnumTelepadSearchResult.NOT_FOUND;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int teleportTime = 0;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public String owner = "";
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean teleporting;
    private AABB renderAABB;

    public TileEntityShortRangeTelepad()
    {
        super(TYPE);
        this.storage.setMaxExtract(ConfigManagerCore.hardMode.get() ? 115 : 50);
        this.inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    public int canTeleportHere()
    {
        if (this.level.isClientSide)
        {
            return -1;
        }

        this.setAddress(this.address);
        this.setTargetAddress(this.targetAddress);

        if (!this.addressValid)
        {
            return 1;
        }

        if (this.getEnergyStoredGC() < ENERGY_USE_ON_TELEPORT)
        {
            return 2;
        }

        return 0; // GOOD
    }

    @Override
    public void tick()
    {
        if (this.ticks % 40 == 0 && !this.level.isClientSide)
        {
            this.setAddress(this.address);
            this.setTargetAddress(this.targetAddress);
        }

        if (!this.level.isClientSide)
        {
            if (!this.getDisabled(0) && this.targetAddressResult == EnumTelepadSearchResult.VALID && (this.ticks % 5 == 0 || teleporting))
            {
                List containedEntities = this.level.getEntitiesOfClass(LivingEntity.class, new AABB(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(),
                        this.getBlockPos().getX() + 1, this.getBlockPos().getY() + 2, this.getBlockPos().getZ() + 1));

                if (containedEntities.size() > 0 && this.getEnergyStoredGC() >= ENERGY_USE_ON_TELEPORT)
                {
                    ShortRangeTelepadHandler.TelepadEntry entry = ShortRangeTelepadHandler.getLocationFromAddress(this.targetAddress);

                    if (entry != null)
                    {
                        teleporting = true;
                    }
                }
                else
                {
                    teleporting = false;
                }
            }

            if (this.teleporting)
            {
                this.teleportTime++;

                if (teleportTime >= MAX_TELEPORT_TIME)
                {
                    ShortRangeTelepadHandler.TelepadEntry entry = ShortRangeTelepadHandler.getLocationFromAddress(this.targetAddress);

                    BlockVec3 finalPos = (entry == null) ? null : entry.position;

                    if (finalPos != null)
                    {
                        BlockEntity tileAt = finalPos.getTileEntity(this.level);
                        List<LivingEntity> containedEntities = this.level.getEntitiesOfClass(LivingEntity.class, new AABB(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(),
                                this.getBlockPos().getX() + 1, this.getBlockPos().getY() + 2, this.getBlockPos().getZ() + 1));

                        if (tileAt instanceof TileEntityShortRangeTelepad)
                        {
                            TileEntityShortRangeTelepad destTelepad = (TileEntityShortRangeTelepad) tileAt;
                            int teleportResult = destTelepad.canTeleportHere();
                            if (teleportResult == 0)
                            {
                                for (LivingEntity e : containedEntities)
                                {
                                    e.setPos(finalPos.x + 0.5F, finalPos.y + 0.08F, finalPos.z + 0.5F);
//                                    this.world.updateEntityWithOptionalForce(e, true); TODO Still necessary?
                                    if (e instanceof ServerPlayer)
                                    {
                                        ((ServerPlayer) e).connection.teleport(finalPos.x, finalPos.y, finalPos.z, e.yRot, e.xRot);
                                    }
                                    GalacticraftCore.packetPipeline.sendToDimension(new PacketSimpleAsteroids(PacketSimpleAsteroids.EnumSimplePacketAsteroids.C_TELEPAD_SEND, GCCoreUtil.getDimensionType(this.level), new Object[]{finalPos, e.getId()}), GCCoreUtil.getDimensionType(this.level));
                                }

                                if (containedEntities.size() > 0)
                                {
                                    this.storage.setEnergyStored(this.storage.getEnergyStoredGC() - ENERGY_USE_ON_TELEPORT);
                                    destTelepad.storage.setEnergyStored(this.storage.getEnergyStoredGC() - ENERGY_USE_ON_TELEPORT);
                                }
                            }
                            else
                            {
                                switch (teleportResult)
                                {
                                case -1:
                                    for (LivingEntity e : containedEntities)
                                    {
                                        if (e instanceof Player)
                                        {
                                            e.sendMessage(new TextComponent("Cannot Send client-LogicalSide")); // No need for translation, since this should never happen
                                        }
                                    }
                                    break;
                                case 1:
                                    for (LivingEntity e : containedEntities)
                                    {
                                        if (e instanceof Player)
                                        {
                                            e.sendMessage(new TextComponent("Target address invalid")); // No need for translation, since this should never happen
                                        }
                                    }
                                    break;
                                case 2:
                                    for (LivingEntity e : containedEntities)
                                    {
                                        if (e instanceof Player)
                                        {
                                            e.sendMessage(new TextComponent(GCCoreUtil.translate("gui.message.target_no_energy")));
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }

                    this.teleportTime = 0;
                    this.teleporting = false;
                }
            }
            else
            {
                this.teleportTime = Math.max(--this.teleportTime, 0);
            }
        }

        super.tick();
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);

        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.getInventory());

        if (GCCoreUtil.getEffectiveSide() == LogicalSide.SERVER)
        {
            this.setAddress(nbt.getInt("Address"));
        }
        this.targetAddress = nbt.getInt("TargetAddress");
        this.owner = nbt.getString("Owner");
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        ContainerHelper.saveAllItems(nbt, this.getInventory());

        nbt.putInt("TargetAddress", this.targetAddress);
        nbt.putInt("Address", this.address);
        nbt.putString("Owner", this.owner);
        return nbt;
    }

    @Override
    public void addExtraNetworkedData(List<Object> networkedList)
    {
        super.addExtraNetworkedData(networkedList);
        networkedList.add(targetAddressResult.ordinal());
    }

    @Override
    public void readExtraNetworkedData(ByteBuf dataStream)
    {
        super.readExtraNetworkedData(dataStream);
        targetAddressResult = EnumTelepadSearchResult.values()[dataStream.readInt()];
    }

    @Override
    public double getPacketRange()
    {
        return 24.0D;
    }

    @Override
    public InteractionResult onActivated(Player entityPlayer)
    {
        if (!level.isClientSide)
        {
            NetworkHooks.openGui((ServerPlayer) entityPlayer, this, buf -> buf.writeBlockPos(pos));
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onCreate(Level world, BlockPos placedPosition)
    {
        List<BlockPos> positions = new LinkedList<>();
        this.getPositions(placedPosition, positions);
        for (BlockPos vecToAdd : positions)
        {
            ((BlockTelepadFake) AsteroidBlocks.SHORT_RANGE_TELEPAD_DUMMY).makeFakeBlock(world, vecToAdd, placedPosition, AsteroidBlocks.SHORT_RANGE_TELEPAD_DUMMY.defaultBlockState().setValue(BlockTelepadFake.TOP, vecToAdd.getY() == placedPosition.getY() + 2));
        }
    }

    @Override
    public BlockMulti.EnumBlockMultiType getMultiType()
    {
        //Not actually used - maybe this shouldn't be an IMultiBlock at all?
        return EnumBlockMultiType.MINER_BASE;
    }

    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int buildHeight = this.level.getMaxBuildHeight() - 1;
        for (int y = 0; y < 3; y += 2)
        {
            if (placedPosition.getY() + y > buildHeight)
            {
                return;
            }
            for (int x = -1; x <= 1; x++)
            {
                for (int z = -1; z <= 1; z++)
                {
                    if (x == 0 && y == 0 && z == 0)
                    {
                        continue;
                    }
                    positions.add(placedPosition.offset(x, y, z));
                }
            }
        }
    }

    @Override
    public void onDestroy(BlockEntity callingBlock)
    {
        final BlockPos thisBlock = getBlockPos();
        List<BlockPos> positions = new LinkedList<>();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            BlockState stateAt = this.level.getBlockState(pos);

            if (stateAt.getBlock() == AsteroidBlocks.SHORT_RANGE_TELEPAD_DUMMY)
            {
                this.level.destroyBlock(pos, false);
            }
        }
        this.level.destroyBlock(thisBlock, true);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AABB(getBlockPos().getX() - 1, getBlockPos().getY(), getBlockPos().getZ() - 1, getBlockPos().getX() + 2, getBlockPos().getY() + 4, getBlockPos().getZ() + 2);
        }
        return this.renderAABB;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public double getViewDistance()
    {
        return Constants.RENDERDISTANCE_MEDIUM;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0};
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public int getMaxStackSize()
    {
        return 64;
    }

    @Override
    public boolean stillValid(Player par1EntityPlayer)
    {
        return this.level.getBlockEntity(getBlockPos()) == this && par1EntityPlayer.distanceToSqr(this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 0.5D, this.getBlockPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean canPlaceItem(int slotID, ItemStack itemStack)
    {
        return slotID == 0 && ItemElectricBase.isElectricItem(itemStack.getItem());
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        return index == 0;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction)
    {
        return this.canPlaceItem(index, itemStackIn);
    }

    @Override
    public EnumSet<Direction> getElectricalOutputDirections()
    {
        return EnumSet.noneOf(Direction.class);
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return !this.getDisabled(0);
    }

    @Override
    public Direction getElectricInputDirection()
    {
        return Direction.UP;
//        return Direction.byIndex((this.getBlockMetadata() & 3) + 2);
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getItem(0);
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown == 0)
        {
            switch (index)
            {
            case 0:
                this.disabled = disabled;
                this.disableCooldown = 10;
                if (level != null && !level.isClientSide)
                {
                    ShortRangeTelepadHandler.addShortRangeTelepad(this);
                }
                break;
            default:
                break;
            }
        }
    }

    @Override
    public boolean getDisabled(int index)
    {
        switch (index)
        {
        case 0:
            return this.disabled;
        default:
            break;
        }

        return true;
    }

    public void setAddress(int address)
    {
        if (this.level != null && address != this.address)
        {
            ShortRangeTelepadHandler.removeShortRangeTeleporter(this);
        }

        this.address = address;

        if (this.address >= 0)
        {
            ShortRangeTelepadHandler.TelepadEntry entry = ShortRangeTelepadHandler.getLocationFromAddress(this.address);
            this.addressValid = entry == null || (this.level != null && (entry.dimensionID == GCCoreUtil.getDimensionType(this.level) && entry.position.x == this.getBlockPos().getX() && entry.position.y == this.getBlockPos().getY() && entry.position.z == this.getBlockPos().getZ()));
        }
        else
        {
            this.addressValid = false;
        }

        if (this.level != null && !this.level.isClientSide)
        {
            ShortRangeTelepadHandler.addShortRangeTelepad(this);
        }
    }

    public boolean updateTarget()
    {
        if (this.targetAddress >= 0 && !this.level.isClientSide)
        {
            this.targetAddressResult = EnumTelepadSearchResult.NOT_FOUND;

            ShortRangeTelepadHandler.TelepadEntry addressResult = ShortRangeTelepadHandler.getLocationFromAddress(this.targetAddress);

            if (addressResult != null)
            {
                if (GCCoreUtil.getDimensionType(this.level) == addressResult.dimensionID)
                {
                    double distance = this.distanceToSqr(addressResult.position.x + 0.5F, addressResult.position.y + 0.5F, addressResult.position.z + 0.5F);

                    if (distance < Math.pow(TELEPORTER_RANGE, 2))
                    {
                        if (!addressResult.enabled)
                        {
                            this.targetAddressResult = EnumTelepadSearchResult.TARGET_DISABLED;
                            return false;
                        }

                        this.targetAddressResult = EnumTelepadSearchResult.VALID;
                        return true;
                    }
                    else
                    {
                        this.targetAddressResult = EnumTelepadSearchResult.TOO_FAR;
                        return false;
                    }
                }
                else
                {
                    this.targetAddressResult = EnumTelepadSearchResult.WRONG_DIM;
                    return false;
                }
            }
            else
            {
                this.targetAddressResult = EnumTelepadSearchResult.NOT_FOUND;
                return false;
            }
        }
        else
        {
            this.targetAddressResult = EnumTelepadSearchResult.NOT_FOUND;
            return false;
        }
    }

    public void setTargetAddress(int address)
    {
        this.targetAddress = address;
        this.updateTarget();
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public String getOwner()
    {
        return this.owner;
    }

    @OnlyIn(Dist.CLIENT)
    public String getReceivingStatus()
    {
        if (!this.addressValid)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.invalid_address");
        }

        if (this.getEnergyStoredGC() <= 0.0F)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.no_energy");
        }

        if (this.getEnergyStoredGC() <= ENERGY_USE_ON_TELEPORT)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.not_enough_energy");
        }

        if (this.getDisabled(0))
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.disabled");
        }

        return EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.message.receiving_active");
    }

    @OnlyIn(Dist.CLIENT)
    public String getSendingStatus()
    {
        if (!this.addressValid)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.invalid_target_address");
        }

        if (this.targetAddressResult == EnumTelepadSearchResult.TOO_FAR)
        {
            return EnumColor.RED + GCCoreUtil.translateWithFormat("gui.message.telepad_too_far", TELEPORTER_RANGE);
        }

        if (this.targetAddressResult == EnumTelepadSearchResult.WRONG_DIM)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.telepad_wrong_dim");
        }

        if (this.targetAddressResult == EnumTelepadSearchResult.NOT_FOUND)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.telepad_not_found");
        }

        if (this.targetAddressResult == EnumTelepadSearchResult.TARGET_DISABLED)
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.message.telepad_target_disabled");
        }

        if (this.getEnergyStoredGC() <= 0.0F)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.no_energy");
        }

        if (this.getEnergyStoredGC() <= ENERGY_USE_ON_TELEPORT)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.not_enough_energy");
        }

        if (this.getDisabled(0))
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.disabled");
        }

        return EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.message.sending_active");
    }

    @OnlyIn(Dist.CLIENT)
    public Vector3 getParticleColor(Random rand, boolean sending)
    {
        float teleportTimeScaled = Math.min(1.0F, this.teleportTime / (float) TileEntityShortRangeTelepad.MAX_TELEPORT_TIME);
        float f;
        f = rand.nextFloat() * 0.6F + 0.4F;

        if (sending && this.targetAddressResult != EnumTelepadSearchResult.VALID)
        {
            return new Vector3(f, f * 0.3F, f * 0.3F);
        }

        if (!sending && !this.addressValid)
        {
            return new Vector3(f, f * 0.3F, f * 0.3F);
        }

        if (this.getEnergyStoredGC() < ENERGY_USE_ON_TELEPORT)
        {
            return new Vector3(f, f * 0.6F, f * 0.3F);
        }

        float r = f * 0.3F;
        float g = f * (0.3F + (teleportTimeScaled * 0.7F));
        float b = f * (1.0F - (teleportTimeScaled * 0.7F));

        return new Vector3(r, g, b);
    }

    @Override
    public Direction getFront()
    {
        return Direction.NORTH;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        return new ContainerShortRangeTelepad(containerId, playerInv, this);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("container.short_range_telepad");
    }
}
