package micdoodle8.mods.galacticraft.planets.venus.entities;

import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.entities.IScaleableFuelLevel;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.planets.mars.entities.TNTProjectileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityEntryPodVenus extends EntityLanderBase implements IScaleableFuelLevel, ICameraZoomEntity, IIgnoreShift
{
    private Integer groundPosY = null;

    public EntityEntryPodVenus(EntityType<? extends EntityEntryPodVenus> type, Level worldIn)
    {
        super(type, worldIn);
//        this.setSize(1.5F, 3.0F);
    }

    public static EntityEntryPodVenus createEntityEntryPodVenus(ServerPlayer player)
    {
        EntityEntryPodVenus pod = new EntityEntryPodVenus(VenusEntities.VENUS_ENTRY_POD, player.level);

        GCPlayerStats stats = GCPlayerStats.get(player);
        pod.stacks = NonNullList.withSize(stats.getRocketStacks().size() + 1, ItemStack.EMPTY);
        pod.fuelTank.setFluid(new FluidStack(GCFluids.FUEL.getFluid(), stats.getFuelLevel()));

        for (int i = 0; i < stats.getRocketStacks().size(); i++)
        {
            if (!stats.getRocketStacks().get(i).isEmpty())
            {
                pod.stacks.set(i, stats.getRocketStacks().get(i).copy());
            }
            else
            {
                pod.stacks.get(i).setCount(0);
            }
        }

        pod.absMoveTo(player.getX(), player.getY(), player.getZ(), 0, 0);

        player.startRiding(pod, true);
        return pod;
    }

    @Override
    public double getInitialMotionY()
    {
        return -2.5F;
    }

    @Override
    public double getRideHeight()
    {
        return this.getBbHeight() - 2.0D;
    }

    @Override
    public float getRotateOffset()
    {
        //flag no rotate
        return -20F;
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
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tickOnGround()
    {

    }

    @Override
    public void tickInAir()
    {
        super.tickInAir();

        if (this.level.isClientSide)
        {
            if (!this.onGround)
            {
//                this.motionY -= 0.002D;
                this.setDeltaMovement(getDeltaMovement().x, this.getDeltaMovement().y - 0.002, this.getDeltaMovement().z);

                if (this.getDeltaMovement().y < -0.7F)
                {
//                    this.motionY *= 0.994F;
                    this.setDeltaMovement(getDeltaMovement().x, this.getDeltaMovement().y * 0.994F, this.getDeltaMovement().z);
                }

                if (this.getY() <= 242.0F)
                {
                    if (groundPosY == null)
                    {
                        this.groundPosY = this.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos(this.getX(), this.getY(), this.getZ())).getY();
                    }

                    if (this.getY() - this.groundPosY > 5.0F)
                    {
                        this.setDeltaMovement(getDeltaMovement().x, this.getDeltaMovement().y * 0.995F, this.getDeltaMovement().z);
                    }
                    else
                    {
                        this.setDeltaMovement(getDeltaMovement().x, this.getDeltaMovement().y * 0.9995F, this.getDeltaMovement().z);
                    }
                }
            }
        }
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
            return new Vector3D(0, 0, 0);
        }

        if (this.ticks >= 40 && this.ticks < 45)
        {
            this.setDeltaMovement(this.getDeltaMovement().x, this.getInitialMotionY(), this.getDeltaMovement().z);
        }

        if (!this.shouldMove())
        {
            return new Vector3D(0, 0, 0);
        }

        return new Vector3D(this.getDeltaMovement());
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
    public boolean pressKey(int key)
    {
        return false;
    }

//    @Override
//    public String getName()
//    {
//        return GCCoreUtil.translate("container.entry_pod");
//    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

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
//            GCCoreUtil.openParachestInv((ServerPlayerEntity) player, this); TODO guis
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
    public boolean shouldIgnoreShiftExit()
    {
        return !this.onGround;
    }

    public Integer getGroundPosY()
    {
        return groundPosY;
    }
}
