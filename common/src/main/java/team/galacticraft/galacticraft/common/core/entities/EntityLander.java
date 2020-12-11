package team.galacticraft.galacticraft.common.core.entities;

import team.galacticraft.galacticraft.common.api.entity.ICameraZoomEntity;
import team.galacticraft.galacticraft.common.api.entity.IIgnoreShift;
import team.galacticraft.galacticraft.common.api.vector.Vector3;
import team.galacticraft.galacticraft.common.api.vector.Vector3D;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.core.client.GCParticles;
import team.galacticraft.galacticraft.common.core.client.fx.EntityParticleData;
import team.galacticraft.galacticraft.common.core.network.PacketSimple;
import team.galacticraft.galacticraft.common.core.network.PacketSimple.EnumSimplePacket;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import team.galacticraft.galacticraft.common.compat.PlatformSpecific;

import java.util.HashMap;
import java.util.Map;

public class EntityLander extends EntityLanderBase implements IIgnoreShift, ICameraZoomEntity
{
    private double lastMotionY;

    public EntityLander(EntityType<EntityLander> type, Level world)
    {
        super(type, world);
    }

    public EntityLander(ServerPlayer player)
    {
        super(GCEntities.LANDER, player);
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
    public double getRideHeight()
    {
        return 2.25;
    }

    @Override
    public float getRotateOffset()
    {
        return +0.0F;
    }

    @Override
    public void tick()
    {
        super.tick();

        this.lastMotionY = this.getDeltaMovement().y;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt)
    {
        super.readAdditionalSaveData(nbt);

        this.lastMotionY = this.getDeltaMovement().y;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt)
    {
        super.addAdditionalSaveData(nbt);
    }

//    @Override
//    public String getName()
//    {
//        return I18n.get("container.lander");
//    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public boolean interact(Player player, InteractionHand hand)
    {
        if (this.level.isClientSide)
        {
            if (!this.onGround)
            {
                return false;
            }

            if (!this.getPassengers().isEmpty())
            {
                this.ejectPassengers();
            }

            return true;
        }

        if (this.getPassengers().isEmpty() && player instanceof ServerPlayer)
        {
//            GCCoreUtil.openParachestInv((ServerPlayerEntity) player, this);
            return true;
        }
        else if (player instanceof ServerPlayer)
        {
            if (!this.onGround)
            {
                return false;
            }

            this.ejectPassengers();
            return true;
        }
        else
        {
            return true;
        }
    }

    @Override
    public boolean pressKey(int key)
    {
        if (this.onGround)
        {
            return false;
        }

        float turnFactor = 2.0F;
        float angle = 45;

        switch (key)
        {
        case 0:
            this.xRot = Math.min(Math.max(this.xRot - 0.5F * turnFactor, -angle), angle);
            return true;
        case 1:
            this.xRot = Math.min(Math.max(this.xRot + 0.5F * turnFactor, -angle), angle);
            return true;
        case 2:
            this.yRot -= 0.5F * turnFactor;
            return true;
        case 3:
            this.yRot += 0.5F * turnFactor;
            return true;
        case 4:
            this.setDeltaMovement(getDeltaMovement().x, Math.min(this.getDeltaMovement().y + 0.03F, this.getY() < 90 ? -0.15 : -1.0), getDeltaMovement().z);
            return true;
        case 5:
            this.setDeltaMovement(getDeltaMovement().x, Math.min(this.getDeltaMovement().y - 0.022F, -1.0), getDeltaMovement().z);
            return true;
        }

        return false;
    }

    @Override
    public void spawnParticles()
    {
        if (this.ticks > 40 && this.xRot != 0.0000001F)
        {
            double sinPitch = Math.sin(this.xRot / Constants.RADIANS_TO_DEGREES_D);
            final double x1 = 4 * Math.cos(this.yRot / Constants.RADIANS_TO_DEGREES_D) * sinPitch;
            final double z1 = 4 * Math.sin(this.yRot / Constants.RADIANS_TO_DEGREES_D) * sinPitch;
            final double y1 = -4 * Math.abs(Math.cos(this.xRot / Constants.RADIANS_TO_DEGREES_D));

            final Map<Vector3, Vector3> particleMap = new HashMap<Vector3, Vector3>();
            particleMap.put(new Vector3(), new Vector3((float) x1, (float) (y1 + this.getDeltaMovement().y / 2), (float) z1));
            LivingEntity passenger = this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof LivingEntity) ? null : (LivingEntity) this.getPassengers().get(0);
            this.level.addParticle(new EntityParticleData(GCParticles.LANDER_FLAME, passenger != null ? passenger.getUUID() : getUUID()),
                    this.getX(), this.getY() + 1D + this.getDeltaMovement().y / 2, this.getZ(),
                    x1, y1 + this.getDeltaMovement().y / 2, z1);
        }
    }

    @Override
    public void tickInAir()
    {
        super.tickInAir();

        if (this.level.isClientSide)
        {
            if (!this.onGround)
            {
                this.setDeltaMovement(getDeltaMovement().add(0.0, -0.008D, 0.0));
            }

            double motY = -1 * Math.sin(this.xRot / Constants.RADIANS_TO_DEGREES_D);
            double motX = Math.cos(this.yRot / Constants.RADIANS_TO_DEGREES_D) * motY;
            double motZ = Math.sin(this.yRot / Constants.RADIANS_TO_DEGREES_D) * motY;
            this.setDeltaMovement(motX / 2.0, getDeltaMovement().y, motZ / 2.0);
        }
    }

    @Override
    public void tickOnGround()
    {
        //Signal switch off flames
        this.xRot = 0.0000001F;
    }

    @Override
    public void onGroundHit()
    {
        if (!this.level.isClientSide)
        {
            if (Math.abs(this.lastMotionY) > 2.0D)
            {
                for (Entity entity : this.getPassengers())
                {
                    entity.stopRiding();
                    if (entity instanceof ServerPlayer)
                    {
                        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, GCCoreUtil.getDimensionType(this.level), new Object[]{}), (ServerPlayer) entity);
                    }
                    entity.setDeltaMovement(0.0, 0.0, 0.0);
                    entity.setPos(entity.getX(), this.getY() + this.getRideHeight(), entity.getZ());
                    if (this.level instanceof ServerLevel)
                    {
                        ((ServerLevel) this.level).updateChunkPos(entity);
                    }
                }
                this.level.explode(this, this.getX(), this.getY(), this.getZ(), 12, Explosion.BlockInteraction.BREAK);

                this.remove();
            }
        }
    }

    @Override
    public Vector3D getMotionVec()
    {
        if (this.onGround)
        {
            return new Vector3D(0, 0, 0);
        }

        if (this.ticks >= 40 && this.ticks < 45)
        {
            this.setDeltaMovement(this.getDeltaMovement().x, this.getInitialMotionY(), this.getDeltaMovement().z);
        }

        return new Vector3D((float) this.getDeltaMovement().x, (float) (this.ticks < 40 ? 0 : this.getDeltaMovement().y), (float) this.getDeltaMovement().z);
    }

    @Override
    public float getCameraZoom()
    {
        return 15;
    }

    @Override
    public boolean defaultThirdPerson()
    {
        return true;
    }

    @Override
    public boolean shouldIgnoreShiftExit()
    {
        return !this.onGround;
    }

    @Override
    public double getInitialMotionY()
    {
        return -2.5D;
    }

    @Override
    protected boolean isMovementNoisy()
    {
        return false;
    }

    @Override
    public AABB getCollideBox()
    {
        return null;
    }

    @Override
    public AABB getCollideAgainstBox(Entity par1Entity)
    {
        return null;
    }

    @Override
    public boolean isPushable()
    {
        return false;
    }

    @Override
    public boolean isPickable()
    {
        return this.isAlive();
    }
}