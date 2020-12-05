package micdoodle8.mods.galacticraft.planets.mars.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.planets.mars.util.MarsUtil;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;

public class EntityLandingBalloons extends EntityLanderBase implements IIgnoreShift, ICameraZoomEntity
{
    private int groundHitCount;
    private float rotationPitchSpeed;
    private float rotationYawSpeed;

    public EntityLandingBalloons(EntityType<? extends EntityLandingBalloons> type, Level worldIn)
    {
        super(type, worldIn);
//        this.setSize(2.0F, 2.0F);
        this.rotationPitchSpeed = this.random.nextFloat();
        this.rotationYawSpeed = this.random.nextFloat();
    }

    public static EntityLandingBalloons createEntityLandingBalloons(ServerPlayer player)
    {
        EntityLandingBalloons balloons = new EntityLandingBalloons(MarsEntities.LANDING_BALLOONS, player.level);

        GCPlayerStats stats = GCPlayerStats.get(player);
        balloons.stacks = NonNullList.withSize(stats.getRocketStacks().size() + 1, ItemStack.EMPTY);
        balloons.fuelTank.setFluid(new FluidStack(GCFluids.FUEL.getFluid(), stats.getFuelLevel()));

        for (int i = 0; i < stats.getRocketStacks().size(); i++)
        {
            if (!stats.getRocketStacks().get(i).isEmpty())
            {
                balloons.stacks.set(i, stats.getRocketStacks().get(i).copy());
            }
            else
            {
                balloons.stacks.get(i).setCount(0);
            }
        }

        balloons.absMoveTo(player.getX(), player.getY(), player.getZ(), 0, 0);

        player.startRiding(balloons, true);
//        this.setSize(2.0F, 2.0F);
        return balloons;
    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public double getRideHeight()
    {
        return super.getRideHeight() - 0.9;
    }

    @Override
    public float getRotateOffset()
    {
        //Signal no rotate
        return -20.0F;
    }

    @Override
    public void tick()
    {
        if (!this.getPassengers().isEmpty())
        {
            for (Entity entity : this.getPassengers())
            {
                entity.onGround = false;
            }
        }

        super.tick();

        if (!this.getPassengers().isEmpty())
        {
            for (Entity entity : this.getPassengers())
            {
                entity.onGround = false;
            }
        }

        if (!this.onGround)
        {
            this.xRot += this.rotationPitchSpeed;
            this.yRot += this.rotationYawSpeed;
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt)
    {
        super.readAdditionalSaveData(nbt);
        this.groundHitCount = nbt.getInt("GroundHitCount");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt)
    {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("GroundHitCount", this.groundHitCount);
    }

//    @Override
//    public String getName()
//    {
//        return GCCoreUtil.translate("container.mars_lander");
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

            this.ejectPassengers();

            return true;
        }
        else if (this.getPassengers().isEmpty() && this.groundHitCount >= 14 && player instanceof ServerPlayer)
        {
//            MarsUtil.openParachestInventory((ServerPlayerEntity) player, this);  TODO guis
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

//        switch (key)
//        {
//            case 0: // Accelerate
//            {
//                this.rotationPitchSpeed -= 0.5F * TURN_FACTOR;
//                return true;
//            }
//            case 1: // Deccelerate
//            {
//                this.rotationPitchSpeed += 0.5F * TURN_FACTOR;
//                return true;
//            }
//            case 2: // Left
//                this.rotationYawSpeed -= 0.5F * TURN_FACTOR;
//                return true;
//            case 3: // Right
//                this.rotationYawSpeed += 0.5F * TURN_FACTOR;
//                return true;
//        }

        return false;
    }

    @Override
    public boolean shouldMove()
    {
        if (this.ticks < 40 || !this.hasReceivedPacket)
        {
            return false;
        }

        return ((!this.getPassengers().isEmpty() && this.groundHitCount < 14) || !this.onGround);
    }

//    @Override
//    public boolean shouldSpawnParticles()
//    {
//        return false;
//    }
//
//    @Override
//    public Map<Vector3, Vector3> getParticleMap()
//    {
//        return null;
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public Particle getParticle(Random rand, double x, double y, double z, double motX, double motY, double motZ)
//    {
//        return null;
//    }

    @Override
    public void spawnParticles()
    {

    }

    @Override
    public void tickInAir()
    {
        if (this.level.isClientSide)
        {
            if (this.groundHitCount == 0)
            {
                this.setDeltaMovement(this.getDeltaMovement().x, -this.getY() / 50.0D, this.getDeltaMovement().z);
            }
            else if (this.groundHitCount < 14 || this.shouldMove())
            {
                this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y * 0.95 - 0.08, this.getDeltaMovement().z);
            }
            else
            {
                if (!this.shouldMove())
                {
                    this.setDeltaMovement(0.0, 0.0, 0.0);
                    this.rotationPitchSpeed = 0.0F;
                    this.rotationYawSpeed = 0.0F;
                }
            }
        }
    }

    @Override
    public void tickOnGround()
    {
    }

    @Override
    public void onGroundHit()
    {
    }

    @Override
    public Vector3D getMotionVec()
    {
        if (this.onGround)
        {
            if (this.groundHitCount < 14)
            {
                this.groundHitCount++;
                double mag = (1.0D / this.groundHitCount) * 4.0D;
                double mX = this.random.nextDouble() - 0.5;
                double mY = 1.0D;
                double mZ = this.random.nextDouble() - 0.5;
                mX *= mag / 3.0D;
                mY *= mag;
                mZ *= mag / 3.0D;
                return new Vector3D(mX, mY, mZ);
            }
        }

        if (this.ticks >= 40 && this.ticks < 45)
        {
//            this.motionY = this.getInitialMotionY();
            this.setDeltaMovement(this.getDeltaMovement().x, this.getInitialMotionY(), this.getDeltaMovement().z);
        }

        if (!this.shouldMove())
        {
            return new Vector3D(0, 0, 0);
        }

        return new Vector3D(this.getDeltaMovement().x, this.ticks < 40 ? 0 : this.getDeltaMovement().y, this.getDeltaMovement().z);
    }

    @Override
    public ArrayList<Object> getNetworkedData()
    {
        ArrayList<Object> objList = new ArrayList<Object>();
        objList.addAll(super.getNetworkedData());
        if ((this.level.isClientSide && this.hasReceivedPacket && this.groundHitCount <= 14) || (!this.level.isClientSide && this.groundHitCount == 14))
        {
            objList.add(this.groundHitCount);
        }
        return objList;
    }

    @Override
    public int getPacketTickSpacing()
    {
        return 5;
    }

    @Override
    public double getPacketSendDistance()
    {
        return 50.0D;
    }

    @Override
    public void readNetworkedData(ByteBuf buffer)
    {
        try
        {
            super.readNetworkedData(buffer);

            if (buffer.readableBytes() > 0)
            {
                this.groundHitCount = buffer.readInt();
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
        return this.groundHitCount > 0 && super.allowDamageSource(damageSource);
    }

    @Override
    public double getInitialMotionY()
    {
        return 0;
    }

    @Override
    public float getCameraZoom()
    {
        return 15.0F;
    }

    @Override
    public boolean defaultThirdPerson()
    {
        return true;
    }

    @Override
    public boolean shouldIgnoreShiftExit()
    {
        return this.groundHitCount < 14 || !this.onGround;
    }
}