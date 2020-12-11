package team.galacticraft.galacticraft.common.core.entities;

import io.netty.buffer.ByteBuf;
import team.galacticraft.galacticraft.common.api.vector.Vector3D;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.core.network.PacketEntityUpdate;
import team.galacticraft.galacticraft.common.core.network.PacketEntityUpdate.IEntityFullSync;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityAdvancedMotion extends InventoryEntity implements IControllableEntity, IEntityFullSync
{
    protected long ticks = 0;

    public float currentDamage;
    public int timeSinceHit;
    public int rockDirection;

    public double advancedPositionX;
    public double advancedPositionY;
    public double advancedPositionZ;
    public double advancedYaw;
    public double advancedPitch;
    public int posRotIncrements;

    protected boolean lastOnGround;

    public EntityAdvancedMotion(EntityType<?> type, Level world)
    {
        super(type, world);
        this.blocksBuilding = true;
        this.noCulling = true;
//        this.isImmuneToFire = true;
    }

    public EntityAdvancedMotion(EntityType<?> type, Level world, double var2, double var4, double var6)
    {
        this(type, world);
        this.setPos(var2, var4, var6);
    }

    @Override
    protected void defineSynchedData()
    {
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
        return this.getBbHeight() - 1.0D;
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
            final double offsetx = Math.cos(this.yRot / Constants.RADIANS_TO_DEGREES_D + 114.8) * -0.5D;
            final double offsetz = Math.sin(this.yRot / Constants.RADIANS_TO_DEGREES_D + 114.8) * -0.5D;
            passenger.setPos(this.getX() + offsetx, this.getY() + this.getRideHeight() + passenger.getRidingHeight(), this.getZ() + offsetz);
        }
    }

    @Override
    public void setPositionRotationAndMotion(double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ, boolean onGround)
    {
        if (this.level.isClientSide)
        {
            this.advancedPositionX = x;
            this.advancedPositionY = y;
            this.advancedPositionZ = z;
            this.advancedYaw = yaw;
            this.advancedPitch = pitch;
            this.setDeltaMovement(motX, motY, motZ);
            this.posRotIncrements = 5;
        }
        else
        {
            this.setPos(x, y, z);
            this.setRot(yaw, pitch);
            this.setDeltaMovement(motX, motY, motZ);
            if (onGround || this.forceGroundUpdate())
            {
                this.onGround = onGround;
            }
        }
    }

    protected boolean forceGroundUpdate()
    {
        return true;
    }

    @Override
    public void animateHurt()
    {
        this.rockDirection = -this.rockDirection;
        this.timeSinceHit = 10;
        this.currentDamage *= 5;
    }

    @Override
    public boolean hurt(DamageSource var1, float var2)
    {
        if (!this.isAlive() || var1.equals(DamageSource.CACTUS) || !this.allowDamageSource(var1))
        {
            return true;
        }
        else
        {
            Entity e = var1.getEntity();
            if (this.isInvulnerableTo(var1) || this.getY() > 300 || (e instanceof LivingEntity && !(e instanceof Player)))
            {
                return false;
            }
            else
            {
                this.rockDirection = -this.rockDirection;
                this.timeSinceHit = 10;
                this.currentDamage = this.currentDamage + var2 * 10;
                this.markHurt();

                if (e instanceof Player && ((Player) e).abilities.instabuild)
                {
                    this.currentDamage = 100;
                }

                if (this.currentDamage > 70)
                {
                    if (!this.getPassengers().isEmpty())
                    {
                        this.ejectPassengers();

                        return false;
                    }

                    if (!this.level.isClientSide)
                    {
                        this.dropItems();

                        this.remove();
                    }
                }

                return true;
            }
        }
    }

    public abstract List<ItemStack> getItemsDropped();

    public abstract boolean shouldMove();

    public abstract void spawnParticles();

    public abstract void tickInAir();

    public abstract void tickOnGround();

    public abstract void onGroundHit();

    public abstract Vector3D getMotionVec();

    /**
     * Can be called in the superclass init method
     * before the subclass fields have been initialised!
     * Therefore include null checks!!!
     */
    public abstract ArrayList<Object> getNetworkedData();

    /**
     * @return ticks between packets being sent to client
     */
    public abstract int getPacketTickSpacing();

    /**
     * @return players within this distance will recieve packets from this
     * entity
     */
    public abstract double getPacketSendDistance();

    public abstract void readNetworkedData(ByteBuf buffer);

    public abstract boolean allowDamageSource(DamageSource damageSource);

    public void dropItems()
    {
        if (this.getItemsDropped() == null)
        {
            return;
        }

        for (final ItemStack item : this.getItemsDropped())
        {
            if (item != null && !item.isEmpty())
            {
                this.spawnAtLocation(item, 0);
            }
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean b)
    {
        if (!this.getPassengers().isEmpty())
        {
            if (this.getPassengers().contains(Minecraft.getInstance().player))
            {
            }
            else
            {
                this.posRotIncrements = posRotationIncrements + 5;
                this.advancedPositionX = x;
                this.advancedPositionY = y;
                this.advancedPositionZ = z;
                this.advancedYaw = yaw;
                this.advancedPitch = pitch;
            }
        }
    }

    @Override
    public void move(MoverType typeIn, Vec3 pos)
    {
        if (this.shouldMove())
        {
            super.move(typeIn, pos);
        }
    }

    public abstract boolean canSetPositionClient();

    public abstract boolean shouldSendAdvancedMotionPacket();

    @Override
    public void tick()
    {
        this.ticks++;

        super.tick();

        if (this.canSetPositionClient() && this.level.isClientSide && (this.getPassengers().isEmpty() || !this.getPassengers().contains(Minecraft.getInstance().player)))
        {
            double x;
            double y;
            double var12;
            double z;
            if (this.posRotIncrements > 0)
            {
                x = this.getX() + (this.advancedPositionX - this.getX()) / this.posRotIncrements;
                y = this.getY() + (this.advancedPositionY - this.getY()) / this.posRotIncrements;
                z = this.getZ() + (this.advancedPositionZ - this.getZ()) / this.posRotIncrements;
                var12 = Mth.wrapDegrees(this.advancedYaw - this.yRot);
                this.yRot = (float) (this.yRot + var12 / this.posRotIncrements);
                this.xRot = (float) (this.xRot + (this.advancedPitch - this.xRot) / this.posRotIncrements);
                --this.posRotIncrements;
                this.setPos(x, y, z);
                this.setRot(this.yRot, this.xRot);
            }
            else
            {
//                x = this.getPosX() + this.motionX;
//                y = this.getPosY() + this.motionY;
//                z = this.getPosZ() + this.motionZ;
//                this.setPosition(x, y, z);
            }
        }

        if (this.timeSinceHit > 0)
        {
            this.timeSinceHit--;
        }

        if (this.currentDamage > 0)
        {
            this.currentDamage--;
        }

        if (this.level.isClientSide)
        {
            this.spawnParticles();
        }

        if (this.onGround)
        {
            this.tickOnGround();
        }
        else
        {
            this.tickInAir();
        }

        if (this.level.isClientSide)
        {
            Vector3D mot = this.getMotionVec();
            this.setDeltaMovement(mot.x, mot.y, mot.z);
        }
        //Necessary on both server and client to achieve a correct this.onGround setting
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.onGround && !this.lastOnGround)
        {
            this.onGroundHit();
        }

        if (shouldSendAdvancedMotionPacket())
        {
            if (this.level.isClientSide)
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketEntityUpdate(this));
            }

            if (!this.level.isClientSide && this.ticks % 5 == 0)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketEntityUpdate(this), new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 50.0, GCCoreUtil.getDimensionType(this.level)));
            }
        }

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        this.lastOnGround = this.onGround;
    }

    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        sendData.addAll(this.getNetworkedData());
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        this.readNetworkedData(buffer);
    }

//    @OnlyIn(Dist.CLIENT)
//    public void spawnParticles(Map<Vector3, Vector3> points)
//    {
//        for (final Entry<Vector3, Vector3> vec : points.entrySet())
//        {
//            final Vector3 posVec = vec.getKey();
//            final Vector3 motionVec = vec.getValue();
//
//            this.addParticle(this.getParticle(this.rand, posVec.x, posVec.y, posVec.z, motionVec.x, motionVec.y, motionVec.z));
//        }
//    }

//    @OnlyIn(Dist.CLIENT)
//    public void addParticle(Particle fx)
//    {
//        final Minecraft mc = Minecraft.getInstance();
//
//        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null)
//        {
//            if (fx != null)
//            {
//                mc.effectRenderer.addEffect(fx);
//            }
//        }
//    } TODO Particles
}