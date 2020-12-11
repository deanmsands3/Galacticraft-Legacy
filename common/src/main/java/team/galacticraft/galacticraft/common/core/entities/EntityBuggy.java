package team.galacticraft.galacticraft.common.core.entities;

import io.netty.buffer.ByteBuf;
import team.galacticraft.galacticraft.common.api.entity.IDockable;
import team.galacticraft.galacticraft.common.api.tile.IFuelDock;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GCItems;
import team.galacticraft.galacticraft.common.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.core.TransformerHooks;
import team.galacticraft.galacticraft.common.core.network.*;
import team.galacticraft.galacticraft.common.core.network.PacketEntityUpdate.IEntityFullSync;
import team.galacticraft.galacticraft.common.core.tick.KeyHandlerClient;
import team.galacticraft.galacticraft.common.core.tile.TileEntityBuggyFueler;
import team.galacticraft.galacticraft.common.core.util.FluidUtil;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.*;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import me.shedaniel.architectury.fluid.FluidStack;
import team.galacticraft.galacticraft.common.compat.fluid.ActionType;
import team.galacticraft.galacticraft.common.compat.fluid.FluidTank;
import team.galacticraft.galacticraft.common.compat.PlatformSpecific;
import net.minecraftforge.fml.network.PacketDistributor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityBuggy extends Entity implements Container, IPacketReceiver, IDockable, IControllableEntity, IEntityFullSync
{
    public enum BuggyType
    {
        NO_INVENTORY(0, new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/buggy_0.png")),
        INVENTORY_1(18, new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/buggy_1.png")),
        INVENTORY_2(36, new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/buggy_2.png")),
        INVENTORY_3(54, new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/buggy_3.png"));

        private final int invSize;
        private final ResourceLocation textureLoc;

        BuggyType(int invSize, ResourceLocation textureLoc)
        {
            this.invSize = invSize;
            this.textureLoc = textureLoc;
        }

        public int getInvSize()
        {
            return invSize;
        }

        public static BuggyType byId(int id)
        {
            return values()[id];
        }

        public ResourceLocation getTextureLoc()
        {
            return textureLoc;
        }
    }

    private static final EntityDataAccessor<Integer> CURRENT_DAMAGE = SynchedEntityData.defineId(EntityBuggy.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TIME_SINCE_HIT = SynchedEntityData.defineId(EntityBuggy.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ROCK_DIRECTION = SynchedEntityData.defineId(EntityBuggy.class, EntityDataSerializers.INT);
    public static final int tankCapacity = 1000;
    public FluidTank buggyFuelTank = new FluidTank(EntityBuggy.tankCapacity);
    protected long ticks = 0;
    public BuggyType buggyType;
    public int currentDamage;
    public int timeSinceHit;
    public int rockDirection;
    public double speed;
    public float wheelRotationZ;
    public float wheelRotationX;
    float maxSpeed = 0.5F;
    float accel = 0.2F;
    float turnFactor = 3.0F;
    public String texture;
    private NonNullList<ItemStack> stacks = NonNullList.withSize(60, ItemStack.EMPTY);
    public double boatX;
    public double boatY;
    public double boatZ;
    public double boatYaw;
    public double boatPitch;
    public int boatPosRotationIncrements;
    private IFuelDock landingPad;
    private int timeClimbing;
    private boolean shouldClimb;

    public EntityBuggy(EntityType<EntityBuggy> type, Level world)
    {
        super(type, world);
//        this.setSize(1.4F, 0.6F);
        this.speed = 0.0D;
        this.blocksBuilding = true;
        this.noCulling = true;
//        this.isImmuneToFire = true;

        if (world.isClientSide)
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
        }
    }

    public void setBuggyType(BuggyType type)
    {
        if (this.buggyType != null)
        {
            throw new RuntimeException("Cannot change buggy type");
        }
        this.buggyType = type;
        this.stacks = NonNullList.withSize(this.buggyType.getInvSize(), ItemStack.EMPTY);
    }

    public static Item getItemFromType(BuggyType buggyType)
    {
        switch (buggyType)
        {
        default:
        case NO_INVENTORY:
            return GCItems.buggy;
        case INVENTORY_1:
            return GCItems.buggyInventory1;
        case INVENTORY_2:
            return GCItems.buggyInventory2;
        case INVENTORY_3:
            return GCItems.buggyInventory3;
        }
    }

    public static BuggyType getTypeFromItem(Item item)
    {
        if (item == GCItems.buggy)
        {
            return BuggyType.NO_INVENTORY;
        }
        if (item == GCItems.buggyInventory1)
        {
            return BuggyType.INVENTORY_1;
        }
        if (item == GCItems.buggyInventory2)
        {
            return BuggyType.INVENTORY_2;
        }
        if (item == GCItems.buggyInventory3)
        {
            return BuggyType.INVENTORY_3;
        }
        return null;
    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    //    public EntityBuggy(World var1, double var2, double var4, double var6, int type)
//    {
//        this(var1);
//        this.setPosition(var2, var4, var6);
//        this.setBuggyType(type);
//        this.stacks = NonNullList.withSize(this.buggyType * 18, ItemStack.EMPTY);
//    }

    @Override
    protected void defineSynchedData()
    {
        this.entityData.define(CURRENT_DAMAGE, 0);
        this.entityData.define(TIME_SINCE_HIT, 0);
        this.entityData.define(ROCK_DIRECTION, 1);
    }

    public int getScaledFuelLevel(int i)
    {
        final double fuelLevel = this.buggyFuelTank.getFluid() == FluidStack.EMPTY ? 0 : this.buggyFuelTank.getFluid().getAmount();

        return (int) (fuelLevel * i / EntityBuggy.tankCapacity);
    }

//    public ModelBase getModel()
//    {
//        return null;
//    }

    @Override
    public ItemStack getPickedResult(HitResult target)
    {
        return new ItemStack(getItemFromType(getBuggyType()), 1);
    }

    public BuggyType getBuggyType()
    {
        return buggyType;
    }

    @Override
    protected boolean isMovementNoisy()
    {
        return false;
    }

    @Override
    public boolean isPushable()
    {
        return false;
    }

    @Override
    public double getRideHeight()
    {
        return this.getBbHeight() - 3.0D;
    }

    @Override
    public boolean isPickable()
    {
        return this.isAlive();
    }

    @Override
    public void positionRider(Entity passenger)
    {
        if (this.hasPassenger(passenger))
        {
            final double offsetX = Math.cos(this.yRot / Constants.RADIANS_TO_DEGREES_D + 114.8) * -0.5D;
            final double offsetZ = Math.sin(this.yRot / Constants.RADIANS_TO_DEGREES_D + 114.8) * -0.5D;
            passenger.setPos(this.getX() + offsetX, this.getY() + 0.4F + passenger.getRidingHeight(), this.getZ() + offsetZ);
        }
    }

    @Override
    public void setPositionRotationAndMotion(double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ, boolean onGround)
    {
        if (this.level.isClientSide)
        {
            this.boatX = x;
            this.boatY = y;
            this.boatZ = z;
            this.boatYaw = yaw;
            this.boatPitch = pitch;
            this.setDeltaMovement(motX, motY, motZ);
            this.boatPosRotationIncrements = 5;
        }
        else
        {
            this.setPos(x, y, z);
            this.setRot(yaw, pitch);
            this.setDeltaMovement(motX, motY, motZ);
        }
    }

    @Override
    public void animateHurt()
    {
        this.entityData.set(ROCK_DIRECTION, -this.entityData.get(ROCK_DIRECTION));
        this.entityData.set(TIME_SINCE_HIT, 10);
        this.entityData.set(CURRENT_DAMAGE, this.entityData.get(CURRENT_DAMAGE) * 5);
    }

    @Override
    public boolean hurt(DamageSource var1, float var2)
    {
        if (!this.isAlive() || var1.equals(DamageSource.CACTUS))
        {
            return true;
        }
        else
        {
            Entity e = var1.getEntity();
            boolean flag = e instanceof Player && ((Player) e).abilities.instabuild;

            if (this.isInvulnerableTo(var1) || (e instanceof LivingEntity && !(e instanceof Player)))
            {
                return false;
            }
            else
            {
                this.entityData.set(ROCK_DIRECTION, -this.entityData.get(ROCK_DIRECTION));
                this.entityData.set(TIME_SINCE_HIT, 10);
                this.entityData.set(CURRENT_DAMAGE, (int) (this.entityData.get(CURRENT_DAMAGE) + var2 * 10));
                this.markHurt();

                if (e instanceof Player && ((Player) e).abilities.instabuild)
                {
                    this.entityData.set(CURRENT_DAMAGE, 100);
                }

                if (flag || this.entityData.get(CURRENT_DAMAGE) > 2)
                {
                    if (!this.getPassengers().isEmpty())
                    {
                        this.ejectPassengers();
                    }

                    if (flag)
                    {
                        this.remove();
                    }
                    else
                    {
                        this.remove();
                        if (!this.level.isClientSide)
                        {
                            this.dropBuggyAsItem();
                        }
                    }
                    this.remove();
                }

                return true;
            }
        }
    }

    public void dropBuggyAsItem()
    {
        List<ItemStack> dropped = this.getItemsDropped();

        if (dropped == null)
        {
            return;
        }

        for (final ItemStack item : dropped)
        {
            ItemEntity entityItem = this.spawnAtLocation(item, 0);

            if (item.hasTag())
            {
                entityItem.getItem().setTag(item.getTag().copy());
            }
        }
    }

    public List<ItemStack> getItemsDropped()
    {
        final List<ItemStack> items = new ArrayList<ItemStack>();

        ItemStack buggy = new ItemStack(getItemFromType(getBuggyType()), 1);
        buggy.setTag(new CompoundTag());
        buggy.getTag().putInt("BuggyFuel", this.buggyFuelTank.getFluidAmount());
        items.add(buggy);

        for (ItemStack item : this.stacks)
        {
            if (!item.isEmpty())
            {
                items.add(item);
            }
        }

        return items;
    }


    @Override
    @Environment(EnvType.CLIENT)
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean b)
    {
        if (!this.getPassengers().isEmpty())
        {
            if (!this.getPassengers().contains(Minecraft.getInstance().player))
            {
                this.boatPosRotationIncrements = posRotationIncrements + 5;
                this.boatX = x;
                this.boatY = y;
                this.boatZ = z;
                this.boatYaw = yaw;
                this.boatPitch = pitch;
            }
        }
    }

    @Override
    public void tick()
    {
        this.ticks++;

        super.tick();

        if (this.level.isClientSide)
        {
            this.wheelRotationX += Math.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z) * 150.0F * (this.speed < 0 ? 1 : -1);
            this.wheelRotationX %= 360;
            this.wheelRotationZ = Math.max(-30.0F, Math.min(30.0F, this.wheelRotationZ * 0.9F));
        }

        if (this.level.isClientSide && !Minecraft.getInstance().player.equals(this.level.getNearestPlayer(this, -1)))
        {
            double x;
            double y;
            double var12;
            double z;
            if (this.boatPosRotationIncrements > 0)
            {
                x = this.getX() + (this.boatX - this.getX()) / this.boatPosRotationIncrements;
                y = this.getY() + (this.boatY - this.getY()) / this.boatPosRotationIncrements;
                z = this.getZ() + (this.boatZ - this.getZ()) / this.boatPosRotationIncrements;
                var12 = Mth.wrapDegrees(this.boatYaw - this.yRot);
                this.yRot = (float) (this.yRot + var12 / this.boatPosRotationIncrements);
                this.xRot = (float) (this.xRot + (this.boatPitch - this.xRot) / this.boatPosRotationIncrements);
                --this.boatPosRotationIncrements;
                this.setPos(x, y, z);
                this.setRot(this.yRot, this.xRot);
            }
            else
            {
                x = this.getX() + this.getDeltaMovement().x;
                y = this.getY() + this.getDeltaMovement().y;
                z = this.getZ() + this.getDeltaMovement().z;
                if (!this.getPassengers().isEmpty())
                {
                    this.setPos(x, y, z);
                }

                if (this.onGround)
                {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.5, 0.5, 0.5));
                }

                this.setDeltaMovement(this.getDeltaMovement().multiply(0.9900000095367432D, 0.949999988079071D, 0.9900000095367432D));
            }
            return;
        }

        if (this.entityData.get(TIME_SINCE_HIT) > 0)
        {
            this.entityData.set(TIME_SINCE_HIT, this.entityData.get(TIME_SINCE_HIT) - 1);
        }

        if (this.entityData.get(CURRENT_DAMAGE) > 0)
        {
            this.entityData.set(CURRENT_DAMAGE, this.entityData.get(CURRENT_DAMAGE) - 1);
        }

        if (!this.onGround)
        {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -TransformerHooks.getGravityForEntity(this) * 0.5D, 0.0));
        }

        if (this.wasInWater && this.speed > 0.2D)
        {
            this.level.playSound(null, (float) this.getX(), (float) this.getY(), (float) this.getZ(), SoundEvents.GENERIC_BURN, SoundSource.NEUTRAL, 0.5F, 2.6F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.8F);
        }

        this.speed *= 0.98D;

        if (this.speed > this.maxSpeed)
        {
            this.speed = this.maxSpeed;
        }

        if (this.horizontalCollision && this.shouldClimb)
        {
            this.speed *= 0.9;
            double motY = 0.15D * ((-Math.pow((this.timeClimbing) - 1, 2)) / 250.0F) + 0.15F;
            motY = Math.max(-0.15, motY);
            this.setDeltaMovement(this.getDeltaMovement().x, motY, this.getDeltaMovement().z);
            this.shouldClimb = false;
        }

        if ((this.getDeltaMovement().x == 0 || this.getDeltaMovement().z == 0) && !this.onGround)
        {
            this.timeClimbing++;
        }
        else
        {
            this.timeClimbing = 0;
        }

        if (this.level.isClientSide && this.buggyFuelTank.getFluid() != FluidStack.EMPTY && this.buggyFuelTank.getFluid().getAmount() > 0)
        {
            this.setDeltaMovement(-(this.speed * Math.cos((this.yRot - 90F) / Constants.RADIANS_TO_DEGREES_D)), getDeltaMovement().y, -(this.speed * Math.sin((this.yRot - 90F) / Constants.RADIANS_TO_DEGREES_D)));
        }

        if (this.level.isClientSide)
        {
            this.move(MoverType.SELF, this.getDeltaMovement());
        }

        if (!this.level.isClientSide && Math.abs(this.getDeltaMovement().x * this.getDeltaMovement().z) > 0.000001)
        {
            double d = this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z;

            if (d != 0 && this.ticks % (Mth.floor(2 / d) + 1) == 0)
            {
                this.removeFuel(1);
            }
        }

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        if (this.level.isClientSide)
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketEntityUpdate(this));
        }
        else if (this.ticks % 5 == 0)
        {
            GalacticraftCore.packetPipeline.sendToAllAround(new PacketEntityUpdate(this), new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 50.0D, GCCoreUtil.getDimensionType(this.level)));
            GalacticraftCore.packetPipeline.sendToAllAround(new PacketDynamic(this), new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 50.0D, GCCoreUtil.getDimensionType(this.level)));
        }
    }

    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        if (this.level.isClientSide)
        {
            return;
        }
        sendData.add(this.buggyType);
        sendData.add(this.buggyFuelTank);
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        this.buggyType = BuggyType.values()[buffer.readInt()];

        try
        {
            this.buggyFuelTank = NetworkUtil.readFluidTank(buffer);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt)
    {
        this.buggyType = BuggyType.values()[nbt.getInt("buggyType")];
        ContainerHelper.loadAllItems(nbt, this.stacks);

        if (nbt.contains("fuelTank"))
        {
            this.buggyFuelTank.readFromNBT(nbt.getCompound("fuelTank"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt)
    {
        if (level.isClientSide)
        {
            return;
        }
        nbt.putInt("buggyType", this.buggyType.ordinal());
        final ListTag var2 = new ListTag();

        if (this.buggyFuelTank.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("fuelTank", this.buggyFuelTank.writeToNBT(new CompoundTag()));
        }

        ContainerHelper.saveAllItems(nbt, stacks);
    }

    @Override
    public int getContainerSize()
    {
        return this.buggyType.getInvSize();
    }

    @Override
    public ItemStack getItem(int index)
    {
        return this.stacks.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count)
    {
        ItemStack itemstack = ContainerHelper.removeItem(this.stacks, index, count);

        if (!itemstack.isEmpty())
        {
            this.setChanged();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index)
    {
        return ContainerHelper.takeItem(this.stacks, index);
    }

    @Override
    public void setItem(int index, ItemStack stack)
    {
        this.stacks.set(index, stack);

        if (stack.getCount() > this.getMaxStackSize())
        {
            stack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }

    @Override
    public int getMaxStackSize()
    {
        return 64;
    }

    @Override
    public boolean stillValid(Player var1)
    {
        return this.isAlive() && var1.distanceToSqr(this) <= 64.0D;
    }

    @Override
    public void setChanged()
    {
    }

    //We don't use these because we use forge containers
    @Override
    public void startOpen(Player player)
    {
    }

    //We don't use these because we use forge containers
    @Override
    public void stopOpen(Player player)
    {
    }

    @Override
    public void clearContent()
    {

    }

    @Override
    public boolean interact(Player player, InteractionHand hand)
    {
        if (this.level.isClientSide)
        {
            if (this.getPassengers().isEmpty())
            {
                player.sendMessage(new TextComponent(KeyHandlerClient.leftKey.getTranslatedKeyMessage() + " / " + KeyHandlerClient.rightKey.getTranslatedKeyMessage() + "  - " + I18n.get("gui.buggy.turn")));
                player.sendMessage(new TextComponent(KeyHandlerClient.accelerateKey.getTranslatedKeyMessage() + "       - " + I18n.get("gui.buggy.accel")));
                player.sendMessage(new TextComponent(KeyHandlerClient.decelerateKey.getTranslatedKeyMessage() + "       - " + I18n.get("gui.buggy.decel")));
                player.sendMessage(new TextComponent(KeyHandlerClient.openFuelGui.getTranslatedKeyMessage() + "       - " + I18n.get("gui.buggy.inv")));
            }

            return true;
        }
        else
        {
            if (this.getPassengers().contains(player))
            {
                this.removePassenger(player);

                return true;
            }
            else
            {
                player.startRiding(this);
                return true;
            }
        }
    }

    @Override
    public boolean pressKey(int key)
    {
        if (this.level.isClientSide && (key == 6 || key == 8 || key == 9))
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_CONTROL_ENTITY, GCCoreUtil.getDimensionType(this.level), new Object[]{key}));
            return true;
        }

        switch (key)
        {
        case 0: // Accelerate
            this.speed += this.accel / 20D;
            this.shouldClimb = true;
            return true;
        case 1: // Deccelerate
            this.speed -= this.accel / 20D;
            this.shouldClimb = true;
            return true;
        case 2: // Left
            this.yRot -= 0.5F * this.turnFactor;
            this.wheelRotationZ = Math.max(-30.0F, Math.min(30.0F, this.wheelRotationZ + 0.5F));
            return true;
        case 3: // Right
            this.yRot += 0.5F * this.turnFactor;
            this.wheelRotationZ = Math.max(-30.0F, Math.min(30.0F, this.wheelRotationZ - 0.5F));
            return true;
        }

        return false;
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemstack)
    {
        return false;
    }

    @Override
    public int addFuel(FluidStack liquid, ActionType action)
    {
        if (this.landingPad != null)
        {
            return FluidUtil.fillWithGCFuel(this.buggyFuelTank, liquid, action);
        }

        return 0;
    }

    @Override
    public FluidStack removeFuel(int amount)
    {
        return this.buggyFuelTank.drain(amount, ActionType.EXECUTE);
    }

    @Override
    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
    {
        if (this.buggyType == BuggyType.NO_INVENTORY)
        {
            return EnumCargoLoadingState.NOINVENTORY;
        }

        int count = 0;

        for (count = 0; count < this.stacks.size(); count++)
        {
            ItemStack stackAt = this.stacks.get(count);

            if (stackAt.getItem() == stack.getItem() && stackAt.getCount() < stackAt.getMaxStackSize())
            {
                if (stackAt.getCount() + stack.getCount() <= stackAt.getMaxStackSize())
                {
                    if (doAdd)
                    {
                        stackAt.grow(stack.getCount());
                        this.setChanged();
                    }

                    return EnumCargoLoadingState.SUCCESS;
                }
                else
                {
                    //Part of the stack can fill this slot but there will be some left over
                    int origSize = stackAt.getCount();
                    int surplus = origSize + stack.getCount() - stackAt.getMaxStackSize();

                    if (doAdd)
                    {
                        stackAt.setCount(stackAt.getMaxStackSize());
                        this.setChanged();
                    }

                    stack.setCount(surplus);
                    if (this.addCargo(stack, doAdd) == EnumCargoLoadingState.SUCCESS)
                    {
                        return EnumCargoLoadingState.SUCCESS;
                    }

                    stackAt.setCount(origSize);
                    return EnumCargoLoadingState.FULL;
                }
            }
        }

        for (count = 0; count < this.stacks.size(); count++)
        {
            ItemStack stackAt = this.stacks.get(count);

            if (!stackAt.isEmpty())
            {
                if (doAdd)
                {
                    this.stacks.set(count, stack);
                    this.setChanged();
                }

                return EnumCargoLoadingState.SUCCESS;
            }
        }

        return EnumCargoLoadingState.FULL;
    }

    @Override
    public RemovalResult removeCargo(boolean doRemove)
    {
        for (int i = 0; i < this.stacks.size(); i++)
        {
            ItemStack stackAt = this.getItem(i);

            if (stackAt != null)
            {
                ItemStack resultStack = stackAt.copy();
                resultStack.setCount(1);

                if (doRemove)
                {
                    stackAt.shrink(1);
                    this.setChanged();
                }

                return new RemovalResult(EnumCargoLoadingState.SUCCESS, resultStack);
            }
        }

        return new RemovalResult(EnumCargoLoadingState.EMPTY, ItemStack.EMPTY);
    }

    @Override
    public void setPad(IFuelDock pad)
    {
        this.landingPad = pad;
    }

    @Override
    public IFuelDock getLandingPad()
    {
        return this.landingPad;
    }

    @Override
    public void onPadDestroyed()
    {

    }

    @Override
    public boolean isDockValid(IFuelDock dock)
    {
        return dock instanceof TileEntityBuggyFueler;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public UUID getOwnerUUID()
    {
        if (!this.getPassengers().isEmpty() && !(this.getPassengers().get(0) instanceof Player))
        {
            return null;
        }

        return !this.getPassengers().isEmpty() ? this.getPassengers().get(0).getUUID() : null;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.stacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean inFlight()
    {
        return false;
    }
}
