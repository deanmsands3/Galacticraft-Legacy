package micdoodle8.mods.galacticraft.planets.venus.entities;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.entities.EntityBossBase;
import micdoodle8.mods.galacticraft.core.entities.IBoss;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.mars.entities.CreeperBossEntity;
import micdoodle8.mods.galacticraft.planets.mars.entities.MarsEntities;
import micdoodle8.mods.galacticraft.planets.venus.items.VenusItems;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.SharedMonsterAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class SpiderQueenEntity extends EntityBossBase implements IEntityBreathable, IBoss, RangedAttackMob
{
    private static final EntityDataAccessor<Byte> BURROWED_COUNT = SynchedEntityData.defineId(SpiderQueenEntity.class, EntityDataSerializers.BYTE);
    public boolean shouldEvade;
    private final List<JuicerEntity> juicersSpawned = Lists.newArrayList();
    private List<UUID> spawnedPreload;

    private int rangedAttackTime;
    private final int minRangedAttackTime;
    private final int maxRangedAttackTime;

    public SpiderQueenEntity(EntityType<? extends SpiderQueenEntity> type, Level worldIn)
    {
        super(type, worldIn);
//        this.setSize(1.4F, 0.9F);
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.maxRangedAttackTime = 60;
        this.minRangedAttackTime = 20;
        this.noCulling = true;
    }

    public static SpiderQueenEntity create(Level world)
    {
        return new SpiderQueenEntity(VenusEntities.SPIDER_QUEEN, world);
    }

    @Override
    public boolean isPushable()
    {
        return false;
    }

    @Override
    public double getRideHeight()
    {
        return this.getBbHeight() * 0.5F;
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn)
    {
        return new GroundPathNavigation(this, worldIn);
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(BURROWED_COUNT, (byte) -1);
    }

    public byte getBurrowedCount()
    {
        return this.entityData.get(BURROWED_COUNT);
    }

    public void setBurrowedCount(byte count)
    {
        this.entityData.set(BURROWED_COUNT, count);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.level.isClientSide)
        {
            if (!this.shouldEvade && this.deathTicks <= 0)
            {
                LivingEntity attackTarget = this.getTarget();

                if (attackTarget != null)
                {
                    double dX = attackTarget.getX() - this.getX();
                    double dY = attackTarget.getBoundingBox().minY + (double) (attackTarget.getBbHeight() / 3.0F) - this.getY();
                    double dZ = attackTarget.getZ() - this.getZ();

                    float distance = 5.0F;
                    double d0 = this.distanceToSqr(attackTarget.getX(), attackTarget.getBoundingBox().minY, attackTarget.getZ());

                    this.getLookControl().setLookAt(attackTarget, 30.0F, 30.0F);

                    if (--this.rangedAttackTime == 0)
                    {
                        if (dX * dX + dY * dY + dZ * dZ > distance * distance)
                        {
                            float f = Mth.sqrt(d0) / distance;
                            this.performRangedAttack(attackTarget, 0.0F);
                            this.rangedAttackTime = Mth.floor(f * (float) (this.maxRangedAttackTime - this.minRangedAttackTime) + (float) this.minRangedAttackTime);
                        }
                    }
                    else if (this.rangedAttackTime < 0)
                    {
                        float f2 = Mth.sqrt(d0) / distance;
                        this.rangedAttackTime = Mth.floor(f2 * (float) (this.maxRangedAttackTime - this.minRangedAttackTime) + (float) this.minRangedAttackTime);
                    }
                }
            }
        }

        if (this.spawnedPreload != null)
        {
            for (UUID id : this.spawnedPreload)
            {
                Entity entity = null;
                Optional<Entity> first = ((ServerLevel) this.level).getEntities().filter((e) -> e.getUniqueID().equals(id)).findFirst();
                if (first.isPresent())
                {
                    entity = first.get();
                }
                if (entity instanceof JuicerEntity)
                {
                    this.juicersSpawned.add((JuicerEntity) entity);
                }
            }
            if (this.juicersSpawned.size() == this.spawnedPreload.size())
            {
                this.spawnedPreload.clear();
                this.spawnedPreload = null;
            }
        }

        if (!this.level.isClientSide && this.shouldEvade)
        {
            if (this.spawner != null)
            {
                AABB roomBounds = this.spawner.getRangeBounds();
                double tarX = (roomBounds.minX + roomBounds.maxX) / 2.0;
                double tarZ = (roomBounds.minZ + roomBounds.maxZ) / 2.0;
                double dX = tarX - this.getX();
                double dY = roomBounds.maxY - this.getY();
                double dZ = tarZ - this.getZ();

                double movespeed = 1.0 * this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
                this.setDeltaMovement(Math.min(Math.max(dX / 2.0F, -movespeed), movespeed), this.getDeltaMovement().y, Math.min(Math.max(dZ / 2.0F, -movespeed), movespeed));
                this.navigation.moveTo(tarX, this.getY(), tarZ, movespeed);

                if (Math.abs(dX) < 0.1 && Math.abs(dZ) < 0.1)
                {
                    this.setDeltaMovement(this.getDeltaMovement().x, Math.min(dY, 0.2), this.getDeltaMovement().z);

                    if (Math.abs(dY) - this.getBbHeight() < 1.1 && Math.abs(this.getY() - this.yOld) < 0.05)
                    {
                        if (this.getBurrowedCount() >= 0)
                        {
                            if (this.tickCount % 20 == 0)
                            {
                                if (this.juicersSpawned.size() < 6)
                                {
                                    JuicerEntity juicer = new JuicerEntity(VenusEntities.JUICER, this.level);
                                    double angle = Math.random() * 2 * Math.PI;
                                    double dist = 3.0F;
                                    juicer.setPos(this.getX() + dist * Math.sin(angle), this.getY() + 0.2F, this.getZ() + dist * Math.cos(angle));
                                    juicer.setHanging(true);
                                    this.level.addFreshEntity(juicer);
                                    this.juicersSpawned.add(juicer);
                                }
                            }

                            if (this.getBurrowedCount() < 20)
                            {
                                this.setBurrowedCount((byte) (this.getBurrowedCount() + 1));
                            }
                        }
                        else
                        {
                            this.setBurrowedCount((byte) 0);
                        }
                    }
                }
            }

            if (!this.juicersSpawned.isEmpty())
            {
                boolean allDead = true;
                for (JuicerEntity juicer : this.juicersSpawned)
                {
                    if (juicer.isAlive())
                    {
                        allDead = false;
                    }
                }
                if (allDead)
                {
                    this.juicersSpawned.clear();
                    this.shouldEvade = false;
                    this.setBurrowedCount((byte) -1);
                }
            }
        }
    }

    @Override
    public ItemStack getGuaranteedLoot(Random rand)
    {
        List<ItemStack> stackList = GalacticraftRegistry.getDungeonLoot(3);
        return stackList.get(rand.nextInt(stackList.size())).copy();
    }

    @Override
    public void knockback(Entity entityIn, float par2, double par3, double par4)
    {
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(150.0F * ConfigManagerCore.dungeonBossHealthMod.get());
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.250000001192092896D);
    }

    @Override
    protected float getVoicePitch()
    {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.1F + 0.4F;
    }

    @Override
    protected float getSoundVolume()
    {
        return 5.0F;
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
        this.playSound(SoundEvents.SPIDER_STEP, 0.5F, 0.5F);
    }

//    @Override
//    protected Item getDropItem()
//    {
//        return Items.STRING;
//    }
//
//    @Override
//    protected void dropFewItems(boolean par1, int par2)
//    {
//        super.dropFewItems(par1, par2);
//
//        if (par1 && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + par2) > 0))
//        {
//            this.dropItem(Items.SPIDER_EYE, 1);
//        }
//    }

//    @Override
//    public EnumCreatureAttribute getCreatureAttribute()
//    {
//        return EnumCreatureAttribute.ARTHROPOD;
//    }

    @Override
    public boolean canBeAffected(MobEffectInstance potioneffectIn)
    {
        return potioneffectIn.getEffect() != MobEffects.POISON && super.canBeAffected(potioneffectIn);
    }

    @Override
    public float getEyeHeight(Pose p_213307_1_)
    {
        return 0.65F;
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source)
    {
        if (this.getBurrowedCount() >= 0)
        {
            return true;
        }

        return super.isInvulnerableTo(source);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount)
    {
        float healthLast = this.getHealth();
        super.actuallyHurt(damageSrc, damageAmount);
        float health = this.getHealth();

        float thirdHealth = this.getMaxHealth() / 3.0F;

        if (health < thirdHealth && healthLast >= thirdHealth)
        {
            shouldEvade = true;
        }
        else if (health < 2 * thirdHealth && healthLast >= 2 * thirdHealth)
        {
            shouldEvade = true;
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float damage)
    {
        SpiderQueenWebEntity entityarrow = SpiderQueenWebEntity.createEntityWebShot(this.level, this, target, 0.8F, (float) (14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(entityarrow);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt)
    {
        super.addAdditionalSaveData(nbt);

        nbt.putBoolean("should_evade", this.shouldEvade);

        ListTag list = new ListTag();
        for (JuicerEntity juicer : this.juicersSpawned)
        {
            list.add(LongTag.valueOf(juicer.getUUID().getMostSignificantBits()));
            list.add(LongTag.valueOf(juicer.getUUID().getLeastSignificantBits()));
        }
        nbt.put("spawned_children", list);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt)
    {
        super.readAdditionalSaveData(nbt);

        this.shouldEvade = nbt.getBoolean("should_evade");

        if (nbt.contains("spawned_children"))
        {
            this.spawnedPreload = Lists.newArrayList();
            ListTag list = nbt.getList("spawned_children", 4);
            for (int i = 0; i < list.size(); i += 2)
            {
                LongTag tagMost = (LongTag) list.get(i);
                LongTag tagLeast = (LongTag) list.get(i + 1);
                this.spawnedPreload.add(new UUID(tagMost.getAsLong(), tagLeast.getAsLong()));
            }
        }
    }

    @Override
    public int getChestTier()
    {
        return 3;
    }

    @Override
    public ItemEntity spawnAtLocation(ItemStack par1ItemStack, float par2)
    {
        final ItemEntity entityitem = new ItemEntity(this.level, this.getX(), this.getY() + par2, this.getZ(), par1ItemStack);
        entityitem.setDeltaMovement(entityitem.getDeltaMovement().x, -2.0, entityitem.getDeltaMovement().z);
        entityitem.setDefaultPickUpDelay();
        if (this.captureDrops() != null)
        {
            this.captureDrops().add(entityitem);
        }
        else
        {
            this.level.addFreshEntity(entityitem);
        }
        return entityitem;
    }

    @Override
    public void dropKey()
    {
        this.spawnAtLocation(new ItemStack(VenusItems.TIER_3_DUNGEON_KEY, 1), 0.5F);
    }

    @Override
    public BossEvent.BossBarColor getHealthBarColor()
    {
        return BossInfo.Color.PURPLE;
    }

//    @Override
//    public void setSwingingArms(boolean swingingArms)
//    {
//    }
}
