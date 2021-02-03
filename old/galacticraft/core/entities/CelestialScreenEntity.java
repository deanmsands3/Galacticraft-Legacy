package micdoodle8.mods.galacticraft.core.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.*;

public class CelestialScreenEntity extends EntityAdvancedMotion implements IIgnoreShift
{
    private boolean lastShouldMove;
    private UUID persistantRiderUUID;
    private Boolean shouldMoveClient;
    private Boolean shouldMoveServer;
    private boolean hasReceivedPacket;
    private ArrayList prevData;
    private boolean networkDataChanged;

    public CelestialScreenEntity(EntityType<CelestialScreenEntity> type, Level world)
    {
        super(type, world);
    }

    public CelestialScreenEntity(Level var1, double var2, double var4, double var6)
    {
        this(GCEntities.CELESTIAL_SCREEN, var1);
        this.setPos(var2, var4, var6);
    }

    public CelestialScreenEntity(ServerPlayer player)
    {
        this(player.level, player.getX(), player.getY(), player.getZ());

        this.absMoveTo(player.getX(), player.getY(), player.getZ(), 0, 0);

        player.startRiding(this, true);
    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean shouldSendAdvancedMotionPacket()
    {
        return this.shouldMoveClient != null && this.shouldMoveServer != null;
    }

    @Override
    public boolean canSetPositionClient()
    {
        return this.shouldSendAdvancedMotionPacket();
    }

    @Override
    public void tick()
    {
        if (this.getPassengers().isEmpty())
        {
            this.remove();
            return;
        }
        super.tick();

        if (this.ticks < 40 && this.getY() > 150)
        {
            if (this.getPassengers().isEmpty())
            {
                final Player player = this.level.getNearestPlayer(this, 5);

                if (player != null && player.getVehicle() == null)
                {
                    player.startRiding(this, true);
                }
            }
        }

        AABB box = this.getBoundingBox().inflate(0.2D, 0.4D, 0.2D);

        final List<Entity> var15 = this.level.getEntities(this, box);

        if (var15 != null && !var15.isEmpty())
        {
            for (Entity entity : var15)
            {
                if (!this.getPassengers().contains(entity))
                {
                    this.pushEntityAway(entity);
                }
            }
        }
    }

    private void pushEntityAway(Entity entityToPush)
    {
        if (!this.getPassengers().contains(entityToPush) && this.getVehicle() != entityToPush)
        {
            double d0 = this.getX() - entityToPush.getX();
            double d1 = this.getZ() - entityToPush.getZ();
            double d2 = Mth.absMax(d0, d1);

            if (d2 >= 0.009999999776482582D)
            {
                d2 = Mth.sqrt(d2);
                d0 /= d2;
                d1 /= d2;
                double d3 = 1.0D / d2;

                if (d3 > 1.0D)
                {
                    d3 = 1.0D;
                }

                d0 *= d3;
                d1 *= d3;
                d0 *= 0.05000000074505806D;
                d1 *= 0.05000000074505806D;
                d0 *= 1.0F - entityToPush.pushthrough;
                d1 *= 1.0F - entityToPush.pushthrough;
                entityToPush.push(-d0, 0.0D, -d1);
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt)
    {
        if (nbt.contains("RiderUUID_LSB"))
        {
            this.persistantRiderUUID = new UUID(nbt.getLong("RiderUUID_LSB"), nbt.getLong("RiderUUID_MSB"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt)
    {
        final ListTag nbttaglist = new ListTag();

        UUID id = this.getOwnerUUID();

        if (id != null)
        {
            nbt.putLong("RiderUUID_LSB", id.getLeastSignificantBits());
            nbt.putLong("RiderUUID_MSB", id.getMostSignificantBits());
        }
    }

    @Override
    public boolean shouldMove()
    {
        return false;
    }

    @Override
    public void tickInAir()
    {
        if (this.level.isClientSide)
        {
            this.setDeltaMovement(0.0, 0.0, 0.0);

            this.lastShouldMove = false;
        }
    }

    @Override
    public ArrayList<Object> getNetworkedData()
    {
        final ArrayList<Object> objList = new ArrayList<Object>();

        if (this.level.isClientSide)
        {
            this.shouldMoveClient = this.shouldMove();
            objList.add(this.shouldMoveClient);
        }
        else
        {
            this.shouldMoveServer = this.shouldMove();
            objList.add(this.shouldMoveServer);
            //Server send rider information for client to check
            objList.add(this.getPassengers().isEmpty() ? -1 : this.getPassengers().get(0).getId());
        }

        this.networkDataChanged = !objList.equals(this.prevData);
        this.prevData = objList;
        return objList;
    }

    @Override
    public boolean networkedDataChanged()
    {
        return this.networkDataChanged;
    }

    @Override
    public boolean canRiderInteract()
    {
        return true;
    }

    @Override
    public int getPacketTickSpacing()
    {
        return 2;
    }

    @Override
    public double getPacketSendDistance()
    {
        return 500.0D;
    }

    @Override
    public void readNetworkedData(ByteBuf buffer)
    {
        try
        {
            if (this.level.isClientSide)
            {
                this.hasReceivedPacket = true;
                this.shouldMoveServer = buffer.readBoolean();

                //Check has correct rider on client
                int shouldBeMountedId = buffer.readInt();
                if (this.getPassengers().isEmpty())
                {
                    if (shouldBeMountedId > -1)
                    {
                        Entity e = Minecraft.getInstance().level.getEntity(shouldBeMountedId);
                        if (e != null)
                        {
                            e.startRiding(this, true);
                        }
                    }
                }
                else if (this.getPassengers().get(0).getId() != shouldBeMountedId)
                {
                    if (shouldBeMountedId == -1)
                    {
                        this.ejectPassengers();
                    }
                    else
                    {
                        Entity e = Minecraft.getInstance().level.getEntity(shouldBeMountedId);
                        if (e != null)
                        {
                            e.startRiding(this, true);
                        }
                    }
                }
            }
            else
            {
                this.shouldMoveClient = buffer.readBoolean();
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean allowDamageSource(DamageSource damageSource)
    {
        return !damageSource.isExplosion();
    }

    @Override
    public List<ItemStack> getItemsDropped()
    {
        return null;
    }

    @Override
    public boolean canPlaceItem(int var1, ItemStack var2)
    {
        return false;
    }

    @Override
    public double getPacketRange()
    {
        return 50.0D;
    }

    @Override
    public UUID getOwnerUUID()
    {
        if (!this.getPassengers().isEmpty() && !(this.getPassengers().get(0) instanceof Player))
        {
            return null;
        }

        UUID id;

        if (!this.getPassengers().isEmpty())
        {
            id = this.getPassengers().get(0).getUUID();

            this.persistantRiderUUID = id;
        }
        else
        {
            id = this.persistantRiderUUID;
        }

        return id;
    }

    @Override
    public boolean pressKey(int key)
    {
        return false;
    }

    @Override
    public int getContainerSize()
    {
        return 0;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return false;
//    }

    @Override
    public void spawnParticles()
    {
    }

    @Override
    public void tickOnGround()
    {
        this.tickInAir();
    }

    @Override
    public void onGroundHit()
    {

    }

    @Override
    public Vector3D getMotionVec()
    {
        return new Vector3D(0, 0, 0);
    }

    @Override
    public boolean shouldIgnoreShiftExit()
    {
        return true;
    }
}