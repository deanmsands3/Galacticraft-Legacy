package micdoodle8.mods.galacticraft.planets.venus.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.entities.ai.EntityMoveHelperCeiling;
import micdoodle8.mods.galacticraft.planets.venus.entities.ai.PathNavigateCeiling;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.math.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.SharedMonsterAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class JuicerEntity extends Monster implements IEntityBreathable
{
    private static final EntityDataAccessor<Boolean> IS_FALLING = SynchedEntityData.defineId(JuicerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_HANGING = SynchedEntityData.defineId(JuicerEntity.class, EntityDataSerializers.BOOLEAN);
    private BlockPos jumpTarget;
    private int timeSinceLastJump = 0;

    public JuicerEntity(EntityType<? extends JuicerEntity> type, Level worldIn)
    {
        super(type, worldIn);
        this.moveControl = new EntityMoveHelperCeiling(this);
//        this.setSize(0.95F, 0.6F);
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.timeSinceLastJump = this.random.nextInt(200) + 50;
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(IS_FALLING, false);
        this.entityData.define(IS_HANGING, false);
    }

    public boolean isHanging()
    {
        return this.entityData.get(IS_FALLING);
    }

    public void setHanging(boolean hanging)
    {
        this.entityData.set(IS_FALLING, hanging);
    }

    public boolean isFalling()
    {
        return this.entityData.get(IS_FALLING);
    }

    public void setFalling(boolean falling)
    {
        this.entityData.set(IS_FALLING, falling);
    }

    @Override
    public boolean canBreatheUnderwater()
    {
        return true;
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.SPIDER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn)
    {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    @Override
    protected boolean isMovementNoisy()
    {
        return false;
    }

    @Override
    protected float getVoicePitch()
    {
        return super.getVoicePitch() + 0.4F;
    }

//    @Override
//    protected Item getDropItem()
//    {
//        return Item.getItemFromBlock(Blocks.AIR);
//    }

    @Override
    public void aiStep()
    {
        if (this.isHanging())
        {
            this.onGround = true;
        }

        super.aiStep();

        if (!this.level.isClientSide)
        {
            if (this.jumpTarget == null)
            {
                if (this.timeSinceLastJump <= 0)
                {
                    BlockPos posAbove = new BlockPos(this.getX(), this.getY() + (this.isHanging() ? 1.0 : -0.5), this.getZ());
                    BlockState blockAbove = this.level.getBlockState(posAbove);

                    if (blockAbove.getBlock() == VenusBlocks.ORANGE_VENUS_DUNGEON_BRICKS || blockAbove.getBlock() == VenusBlocks.RED_VENUS_DUNGEON_BRICKS)
                    {
                        HitResult hit = this.level.clip(new ClipContext(new Vec3(this.getX(), this.getY(), this.getZ()), new Vec3(this.getX(), this.getY() + (this.isHanging() ? -10 : 10), this.getZ()), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));

                        if (hit.getType() == RayTraceResult.Type.BLOCK)
                        {
                            BlockHitResult blockResult = (BlockHitResult) hit;
                            BlockState blockBelow = this.level.getBlockState(blockResult.getBlockPos());
                            if (blockBelow.getBlock() == VenusBlocks.RED_VENUS_DUNGEON_BRICKS || blockBelow.getBlock() == VenusBlocks.ORANGE_VENUS_DUNGEON_BRICKS)
                            {
                                if (this.isHanging())
                                {
                                    this.jumpTarget = blockResult.getBlockPos();
                                    this.setFalling(this.jumpTarget != null);
                                }
                                else
                                {
                                    this.jumpTarget = blockResult.getBlockPos().relative(Direction.DOWN);
                                    this.setFalling(this.jumpTarget != null);
                                }
                            }
                        }
                    }
                }
                else
                {
                    this.timeSinceLastJump--;
                }
            }
        }

        if (this.isHanging())
        {
            this.setDeltaMovement(this.getDeltaMovement().x, 0.0, this.getDeltaMovement().z);
        }
    }

    @Override
    public void move(MoverType typeIn, Vec3 pos)
    {
        super.move(typeIn, pos);
        if (this.isHanging())
        {
            this.onGround = true;
        }
    }

    @Override
    public void tick()
    {
        super.tick();

        if (this.level.isClientSide)
        {
            this.animationSpeedOld = this.animationSpeed;
            double d1 = this.getX() - this.xo;
            double d0 = this.getZ() - this.zo;
            float f2 = Mth.sqrt(d1 * d1 + d0 * d0) * 4.0F;

            if (f2 > 1.0F)
            {
                f2 = 1.0F;
            }

            this.animationSpeed += (f2 - this.animationSpeed) * 0.4F;
            this.animationPosition += this.animationSpeed;
        }
        else
        {
            if (this.jumpTarget != null)
            {
                double diffX = this.jumpTarget.getX() - this.getX() + 0.5;
                double diffY = this.jumpTarget.getY() - this.getY() + 0.6;
                double diffZ = this.jumpTarget.getZ() - this.getZ() + 0.5;
                double motY = diffY > 0 ? Math.min(diffY / 2.0F, 0.2123F) : Math.max(diffY / 2.0F, -0.2123F);
                double motX = diffX > 0 ? Math.min(diffX / 2.0F, 0.2123F) : Math.max(diffX / 2.0F, -0.2123F);
                double motZ = diffZ > 0 ? Math.min(diffZ / 2.0F, 0.2123F) : Math.max(diffZ / 2.0F, -0.2123F);
                this.setDeltaMovement(motX, motY, motZ);
                if (diffY > 0.0F && Math.abs(this.jumpTarget.getY() - (this.getY() + this.getDeltaMovement().y)) < 0.4F)
                {
                    this.setPos(this.jumpTarget.getX() + 0.5, this.jumpTarget.getY() + 0.2, this.jumpTarget.getZ() + 0.5);
                    this.jumpTarget = null;
                    this.setFalling(false);
                    this.timeSinceLastJump = this.random.nextInt(180) + 60;
                    this.setHanging(true);
                }
                else if (diffY < 0.0F && Math.abs(this.jumpTarget.getY() - (this.getY() + this.getDeltaMovement().y)) < 0.8F)
                {
                    this.setPos(this.jumpTarget.getX() + 0.5, this.jumpTarget.getY() + 1.0, this.jumpTarget.getZ() + 0.5);
                    this.jumpTarget = null;
                    this.setFalling(false);
                    this.timeSinceLastJump = this.random.nextInt(180) + 60;
                    this.setHanging(false);
                }
                else
                {
                    this.setHanging(false);
                }
            }
        }
    }

//    @Override
//    protected boolean isValidLightLevel()
//    {
//        return true;
//    }
//
//    @Override
//    public boolean getCanSpawnHere()
//    {
//        if (super.getCanSpawnHere())
//        {
//            PlayerEntity var1 = this.world.getClosestPlayerToEntity(this, 5.0D);
//            return var1 == null;
//        }
//        else
//        {
//            return false;
//        }
//    }

//    @Override
//    public EnumCreatureAttribute getCreatureAttribute()
//    {
//        return EnumCreatureAttribute.ARTHROPOD;
//    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn)
    {
        return new PathNavigateCeiling(this, worldIn);
    }

//    @Override
//    public void setInWeb()
//    {
//    }


    @Override
    public void addAdditionalSaveData(CompoundTag nbt)
    {
        super.addAdditionalSaveData(nbt);

        nbt.putInt("timeSinceLastJump", this.timeSinceLastJump);
        nbt.putBoolean("jumpTargetNull", this.jumpTarget == null);
        if (this.jumpTarget != null)
        {
            nbt.putInt("jumpTargetX", this.jumpTarget.getX());
            nbt.putInt("jumpTargetY", this.jumpTarget.getY());
            nbt.putInt("jumpTargetZ", this.jumpTarget.getZ());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt)
    {
        super.readAdditionalSaveData(nbt);

        this.timeSinceLastJump = nbt.getInt("timeSinceLastJump");
        if (nbt.getBoolean("jumpTargetNull"))
        {
            this.jumpTarget = null;
        }
        else
        {
            this.jumpTarget = new BlockPos(nbt.getInt("jumpTargetX"), nbt.getInt("jumpTargetY"), nbt.getInt("jumpTargetZ"));
        }

        this.setFalling(this.jumpTarget != null);
    }
}
