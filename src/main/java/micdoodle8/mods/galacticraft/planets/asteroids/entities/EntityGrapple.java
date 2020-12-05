package micdoodle8.mods.galacticraft.planets.asteroids.entities;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.network.PacketSimpleAsteroids;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.math.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EntityGrapple extends Entity implements Projectile
{
    private static final EntityDataAccessor<Integer> PULLING_ENTITY_ID = SynchedEntityData.defineId(EntityGrapple.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_PULLING = SynchedEntityData.defineId(EntityGrapple.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ItemStack> STRING_ITEM_STACK = SynchedEntityData.defineId(EntityGrapple.class, EntityDataSerializers.ITEM_STACK);
    private BlockPos hitVec;
    @Nullable
    private BlockState inBlockState;
    private boolean inGround;
    public int canBePickedUp;
    public int arrowShake;
    public Player shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    public float rotationRoll;
    public float prevRotationRoll;
    public boolean pullingPlayer;

    public EntityGrapple(EntityType<? extends EntityGrapple> type, Level worldIn)
    {
        super(type, worldIn);
        this.noCulling = false;
//        this.yOffset = -1.5F;
//        this.setSize(0.75F, 0.75F);
    }

    public static EntityGrapple createEntityGrapple(Level world, Player shootingEntity, float par3, ItemStack stringStack)
    {
        EntityGrapple grapple = new EntityGrapple(AsteroidEntities.GRAPPLE.get(), world);
        grapple.shootingEntity = shootingEntity;
//        grapple.setSize(0.75F, 0.75F);

        if (shootingEntity != null)
        {
            grapple.canBePickedUp = 1;
            grapple.moveTo(shootingEntity.getX(), shootingEntity.getY() + shootingEntity.getEyeHeight(), shootingEntity.getZ(), shootingEntity.yRot, shootingEntity.xRot);
        }

        float motX = -Mth.sin(grapple.yRot / Constants.RADIANS_TO_DEGREES) * Mth.cos(grapple.xRot / Constants.RADIANS_TO_DEGREES);
        float motY = -Mth.sin(grapple.xRot / Constants.RADIANS_TO_DEGREES);
        float motZ = Mth.cos(grapple.yRot / Constants.RADIANS_TO_DEGREES) * Mth.cos(grapple.xRot / Constants.RADIANS_TO_DEGREES);
        grapple.setDeltaMovement(motX, motY, motZ);
        grapple.setPosRaw(grapple.getX() + grapple.getDeltaMovement().x, grapple.getY() + grapple.getDeltaMovement().y, grapple.getZ() + grapple.getDeltaMovement().z);
//        grapple.yOffset = -1.5F;
        grapple.setPos(grapple.getX(), grapple.getY(), grapple.getZ());
        grapple.shoot(grapple.getDeltaMovement().x, grapple.getDeltaMovement().y, grapple.getDeltaMovement().z, par3 * 1.5F, 1.0F);
        grapple.updateStringStack(stringStack);
        return grapple;
    }


    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance)
    {
        double d0 = this.getBoundingBox().getSize();

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * 10.0;
        return distance < d0 * d0;
    }

    @Override
    public double getRidingHeight()
    {
        return -1.5F;
    }

    @Override
    protected void defineSynchedData()
    {
        this.entityData.define(PULLING_ENTITY_ID, 0);
        this.entityData.define(IS_PULLING, false);
        this.entityData.define(STRING_ITEM_STACK, new ItemStack(Items.STRING));
    }

    @Override
    public void shoot(double mX, double mY, double mZ, float mult, float rand)
    {
        float f2 = Mth.sqrt(mX * mX + mY * mY + mZ * mZ);
        mX /= f2;
        mY /= f2;
        mZ /= f2;
        mX += this.random.nextGaussian() * (this.random.nextBoolean() ? -1 : 1) * 0.007499999832361937D * rand;
        mY += this.random.nextGaussian() * (this.random.nextBoolean() ? -1 : 1) * 0.007499999832361937D * rand;
        mZ += this.random.nextGaussian() * (this.random.nextBoolean() ? -1 : 1) * 0.007499999832361937D * rand;
        mX *= mult;
        mY *= mult;
        mZ *= mult;
        this.setDeltaMovement(mX, mY, mZ);
        float f3 = Mth.sqrt(mX * mX + mZ * mZ);
        this.yRotO = this.yRot = (float) Math.atan2(mX, mZ) * Constants.RADIANS_TO_DEGREES;
        this.xRotO = this.xRot = (float) Math.atan2(mY, f3) * Constants.RADIANS_TO_DEGREES;
        this.ticksInGround = 0;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean b)
    {
        this.setPos(x, y, z);
        this.setRot(yaw, pitch);
    }

    @Override
    public void setPos(double x, double y, double z)
    {
        super.setPos(x, y, z);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void lerpMotion(double x, double y, double z)
    {
//        this.motionX = x;
//        this.getMotion().y = y;
//        this.getMotion().z = z;
        this.setDeltaMovement(x, y, z);

        if (this.xRotO == 0.0F && this.yRotO == 0.0F)
        {
            float f = Mth.sqrt(x * x + z * z);
            this.yRotO = this.yRot = (float) Math.atan2(x, z) * Constants.RADIANS_TO_DEGREES;
            this.xRotO = this.xRot = (float) Math.atan2(y, f) * Constants.RADIANS_TO_DEGREES;
            this.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
            this.ticksInGround = 0;
        }
    }

    @Override
    public void tick()
    {
        super.tick();

        this.prevRotationRoll = this.rotationRoll;

        if (!this.level.isClientSide)
        {
            this.updateShootingEntity();

            if (this.getPullingEntity())
            {
                Player shootingEntity = this.getShootingEntity();
                if (shootingEntity != null)
                {
                    double deltaPosition = this.distanceToSqr(shootingEntity);

                    Vector3D mot = new Vector3D(shootingEntity.getDeltaMovement().x, shootingEntity.getDeltaMovement().y, shootingEntity.getDeltaMovement().z);

                    if (mot.getMagnitudeSquared() < 0.01 && this.pullingPlayer)
                    {
                        if (deltaPosition < 10)
                        {
                            this.playerTouch(shootingEntity);
                        }
                        this.updatePullingEntity(false);
                        this.remove();
                    }

                    this.pullingPlayer = true;
                }
            }
        }
        else
        {
            if (this.getPullingEntity())
            {
                Player shootingEntity = this.getShootingEntity();
                if (shootingEntity != null)
                {
                    shootingEntity.lerpMotion((this.getX() - shootingEntity.getX()) / 12.0F, (this.getY() - shootingEntity.getY()) / 12.0F, (this.getZ() - shootingEntity.getZ()) / 12.0F);
                    if (shootingEntity.level.isClientSide && shootingEntity.level.getDimension() instanceof IZeroGDimension)
                    {
                        GCPlayerStatsClient stats = GCPlayerStatsClient.get(shootingEntity);
                        if (stats != null)
                        {
//                            stats.getFreefallHandler().updateFreefall(shootingEntity); TODO Freefall
                        }
                    }
                }
            }
        }

        if (this.xRotO == 0.0F && this.yRotO == 0.0F)
        {
            float f = Mth.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z);
            this.yRotO = this.yRot = (float) Math.atan2(this.getDeltaMovement().x, this.getDeltaMovement().z) * Constants.RADIANS_TO_DEGREES;
            this.xRotO = this.xRot = (float) Math.atan2(this.getDeltaMovement().y, f) * Constants.RADIANS_TO_DEGREES;
        }

        if (this.hitVec != null)
        {
            BlockState state = this.level.getBlockState(this.hitVec);

            if (state.getMaterial() != Material.AIR)
            {
                VoxelShape neighbour = state.getShape(this.level, this.hitVec);

                if (neighbour != Shapes.empty() && neighbour.bounds().contains(new Vec3(this.getX(), this.getY(), this.getZ())))
                {
                    this.inGround = true;
                }
            }
        }

        if (this.arrowShake > 0)
        {
            --this.arrowShake;
        }

        if (this.inGround)
        {
            if (this.hitVec != null)
            {
                BlockState state = this.level.getBlockState(this.hitVec);

                if (state == inBlockState)
                {
                    if (this.shootingEntity != null)
                    {
                        this.shootingEntity.setDeltaMovement((this.getX() - this.shootingEntity.getX()) / 16.0F, (this.getY() - this.shootingEntity.getY()) / 16.0F, (this.getZ() - this.shootingEntity.getZ()) / 16.0F);
                        if (this.shootingEntity instanceof ServerPlayer)
                        {
                            GalacticraftCore.handler.preventFlyingKicks((ServerPlayer) this.shootingEntity);
                        }
                    }

                    if (!this.level.isClientSide && this.ticksInGround < 5)
                    {
                        this.updatePullingEntity(true);
                    }

                    ++this.ticksInGround;

                    if (this.ticksInGround == 1200)
                    {
                        this.remove();
                    }
                }
                else
                {
                    this.inGround = false;
                    this.setDeltaMovement(this.getDeltaMovement().multiply(this.random.nextFloat() * 0.2F, this.random.nextFloat() * 0.2F, this.random.nextFloat() * 0.2F));
                    this.ticksInGround = 0;
                    this.ticksInAir = 0;
                }
            }
        }
        else
        {
            this.rotationRoll += 5;
            ++this.ticksInAir;

            if (!this.level.isClientSide)
            {
                this.updatePullingEntity(false);
            }

            if (this.shootingEntity != null && this.distanceToSqr(this.shootingEntity) >= 40 * 40)
            {
                this.remove();
            }

            Vec3 vec31 = new Vec3(this.getX(), this.getY(), this.getZ());
            Vec3 vec3 = new Vec3(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
            HitResult castResult = this.level.clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            vec31 = new Vec3(this.getX(), this.getY(), this.getZ());
            vec3 = new Vec3(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);

            if (castResult.getType() != HitResult.Type.MISS)
            {
                vec3 = new Vec3(castResult.getLocation().x, castResult.getLocation().y, castResult.getLocation().z);
            }

            Entity entity = null;
            List<Entity> list = this.level.getEntities(this, this.getBoundingBox().expandTowards(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z).inflate(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            int i;
            final double border = 0.3D;

            for (i = 0; i < list.size(); ++i)
            {
                Entity entity1 = list.get(i);

                if (entity1.isPickable() && (entity1 != this.shootingEntity || this.ticksInAir >= 5))
                {
                    AABB axisalignedbb1 = entity1.getBoundingBox().inflate(border, border, border);
                    Optional<Vec3> rayResult = axisalignedbb1.clip(vec3, vec31);

                    if (rayResult.isPresent())
                    {
                        double d1 = vec3.distanceTo(rayResult.get());

                        if (d1 < d0 || d0 == 0.0D)
                        {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null)
            {
                castResult = new EntityHitResult(entity);
            }

            if (castResult.getType() == HitResult.Type.ENTITY && ((EntityHitResult) castResult).getEntity() instanceof Player)
            {
                Player entityplayer = (Player) ((EntityHitResult) castResult).getEntity();

                if (entityplayer.abilities.invulnerable || this.shootingEntity instanceof Player && !this.shootingEntity.canHarmPlayer(entityplayer))
                {
                    castResult = null;
                }
            }

            float motion;

            if (castResult != null)
            {
                if (castResult.getType() == HitResult.Type.ENTITY)
                {
                    BlockHitResult blockResult = (BlockHitResult) castResult;
                    this.hitVec = blockResult.getBlockPos();
                    BlockState state = this.level.getBlockState(blockResult.getBlockPos());
                    this.inBlockState = state;
                    float motionX = (float) (castResult.getLocation().x - this.getX());
                    float motionY = (float) (castResult.getLocation().y - this.getY());
                    float motionZ = (float) (castResult.getLocation().z - this.getZ());
                    this.setDeltaMovement(motionX, motionY, motionZ);
                    motion = Mth.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
                    this.setPosRaw(this.getX() - motionX / motion * 0.05000000074505806D,
                            this.getY() - motionY / motion * 0.05000000074505806D,
                            this.getZ() - motionZ / motion * 0.05000000074505806D);
                    this.inGround = true;

                    if (!this.inBlockState.isAir(this.level, blockResult.getBlockPos()))
                    {
                        this.inBlockState.entityInside(this.level, blockResult.getBlockPos(), this);
                    }
                }
            }

            this.setPosRaw(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
            motion = Mth.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z);
            this.yRot = (float) Math.atan2(this.getDeltaMovement().x, this.getDeltaMovement().z) * Constants.RADIANS_TO_DEGREES;
            this.xRot = (float) Math.atan2(this.getDeltaMovement().y, motion) * Constants.RADIANS_TO_DEGREES;

            while (this.xRot - this.xRotO < -180.0F)
            {
                this.xRotO -= 360.0F;
            }

            while (this.xRot - this.xRotO >= 180.0F)
            {
                this.xRotO += 360.0F;
            }

            while (this.yRot - this.yRotO < -180.0F)
            {
                this.yRotO -= 360.0F;
            }

            while (this.yRot - this.yRotO >= 180.0F)
            {
                this.yRotO += 360.0F;
            }

            this.xRot = this.xRotO + (this.xRot - this.xRotO) * 0.2F;
            this.yRot = this.yRotO + (this.yRot - this.yRotO) * 0.2F;

            if (this.isInWater())
            {
                float f4 = 0.25F;
                for (int l = 0; l < 4; ++l)
                {
                    this.level.addParticle(ParticleTypes.BUBBLE, this.getX() - this.getDeltaMovement().x * f4, this.getY() - this.getDeltaMovement().y * f4, this.getZ() - this.getDeltaMovement().z * f4, this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
                }

            }

            if (this.isInWaterOrRain())
            {
                this.clearFire();
            }

            this.setPos(this.getX(), this.getY(), this.getZ());
            this.checkInsideBlocks();
        }

        if (!this.level.isClientSide && (this.ticksInGround - 1) % 10 == 0)
        {
            GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimpleAsteroids(PacketSimpleAsteroids.EnumSimplePacketAsteroids.C_UPDATE_GRAPPLE_POS, GCCoreUtil.getDimensionType(this.level), new Object[]{this.getId(), new Vector3(this)}), new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 150, GCCoreUtil.getDimensionType(this.level)));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound)
    {
        if (this.hitVec != null)
        {
            compound.putShort("xTile", (short) this.hitVec.getX());
            compound.putShort("yTile", (short) this.hitVec.getY());
            compound.putShort("zTile", (short) this.hitVec.getZ());
        }
        compound.putShort("life", (short) this.ticksInGround);
        if (this.inBlockState != null)
        {
            compound.put("inBlockState", NbtUtils.writeBlockState(this.inBlockState));
        }
        compound.putByte("shake", (byte) this.arrowShake);
        compound.putByte("inGround", (byte) (this.inGround ? 1 : 0));
        compound.putByte("pickup", (byte) this.canBePickedUp);
        compound.putString("stringStack", Objects.requireNonNull(getStringItemStack().getItem().getRegistryName()).toString());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound)
    {
        if (compound.contains("xTile"))
        {
            this.hitVec = new BlockPos(compound.getShort("xTile"), compound.getShort("yTile"), compound.getShort("zTile"));
        }

        this.ticksInGround = compound.getShort("life");
        if (compound.contains("inBlockState", 10))
        {
            this.inBlockState = NbtUtils.readBlockState(compound.getCompound("inBlockState"));
        }
        this.arrowShake = compound.getByte("shake") & 255;
        this.inGround = compound.getByte("inGround") == 1;

        if (compound.contains("pickup", 99))
        {
            this.canBePickedUp = compound.getByte("pickup");
        }
        else if (compound.contains("player", 99))
        {
            this.canBePickedUp = compound.getBoolean("player") ? 1 : 0;
        }

        if (compound.contains("stringStack"))
        {
            this.updateStringStack(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(compound.getString("stringStack"))))));
        }
        else
        {
            this.updateStringStack(new ItemStack(Items.STRING));
        }
    }

    @Override
    public void playerTouch(Player par1EntityPlayer)
    {
        if (!this.level.isClientSide && this.inGround && this.arrowShake <= 0)
        {
            boolean flag = this.canBePickedUp == 1 || this.canBePickedUp == 2 && par1EntityPlayer.abilities.instabuild;

            if (this.canBePickedUp == 1 && getStringItemStack() != ItemStack.EMPTY && !par1EntityPlayer.inventory.add(getStringItemStack()))
            {
                flag = false;
            }

            if (flag)
            {
                this.playSound(SoundEvents.ITEM_PICKUP, 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                par1EntityPlayer.take(this, 1);
                this.remove();
            }
        }
    }

    @Override
    protected boolean isMovementNoisy()
    {
        return false;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public float getShadowSize()
//    {
//        return 0.0F;
//    }


    @Override
    public boolean isAttackable()
    {
        return false;
    }

    private void updateShootingEntity()
    {
        if (this.shootingEntity != null)
        {
            this.entityData.set(PULLING_ENTITY_ID, this.shootingEntity.getId());
        }
    }

    public Player getShootingEntity()
    {
        Entity entity = this.level.getEntity(this.entityData.get(PULLING_ENTITY_ID));

        if (entity instanceof Player)
        {
            return (Player) entity;
        }

        return null;
    }

    public void updatePullingEntity(boolean pulling)
    {
        this.entityData.set(IS_PULLING, pulling);
    }

    public void updateStringStack(ItemStack stringStack)
    {
        this.entityData.set(STRING_ITEM_STACK, stringStack);
    }

    public boolean getPullingEntity()
    {
        return this.entityData.get(IS_PULLING);
    }

    public ItemStack getStringItemStack()
    {
        return this.entityData.get(STRING_ITEM_STACK);
    }
}