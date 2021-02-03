package micdoodle8.mods.galacticraft.planets.mars.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.entities.EvolvedCreeperEntity;
import micdoodle8.mods.galacticraft.core.entities.EvolvedSkeletonEntity;
import micdoodle8.mods.galacticraft.core.entities.EvolvedSpiderEntity;
import micdoodle8.mods.galacticraft.core.entities.EvolvedZombieEntity;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.SharedMonsterAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.fml.network.NetworkHooks;

public class SludgelingEntity extends Monster implements IEntityBreathable
{
    public SludgelingEntity(EntityType<? extends SludgelingEntity> type, Level worldIn)
    {
        super(type, worldIn);
//        this.setSize(0.3F, 0.2F);
        this.goalSelector.availableGoals.clear();
        this.targetSelector.availableGoals.clear();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.25F, true));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 0, false, true, null));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, EvolvedZombieEntity.class, 0, false, true, null));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, EvolvedSkeletonEntity.class, 0, false, true, null));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, EvolvedSpiderEntity.class, 0, false, true, null));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, EvolvedCreeperEntity.class, 0, false, true, null));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, SlimelingEntity.class, 200, false, true, null));
    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
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
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(7.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.0F);
    }

    @Override
    protected boolean isMovementNoisy()
    {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.SILVERFISH_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.SILVERFISH_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.SILVERFISH_DEATH;
    }

//    public PlayerEntity getClosestEntityToAttack(double par1, double par3, double par5, double par7)
//    {
//        double var9 = -1.0D;
//        PlayerEntity var11 = null;
//
//        for (int var12 = 0; var12 < this.world.loadedEntityList.size(); ++var12)
//        {
//            PlayerEntity var13 = (PlayerEntity) this.world.loadedEntityList.get(var12);
//            double var14 = var13.getDistanceSq(par1, par3, par5);
//
//            if ((par7 < 0.0D || var14 < par7 * par7) && (var9 == -1.0D || var14 < var9))
//            {
//                var9 = var14;
//                var11 = var13;
//            }
//        }
//
//        return var11;
//    }

//    @Override
//    protected void playStepSound(BlockPos pos, Block block)
//    {
//        this.playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.15F, 1.0F);
//    }
//
//    @Override
//    protected Item getDropItem()
//    {
//        return Item.getItemFromBlock(Blocks.AIR);
//    }

    @Override
    public void tick()
    {
        this.yBodyRot = this.yRot;
        super.tick();
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


    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn)
    {
        return super.checkSpawnRules(worldIn, spawnReasonIn);
    }

    @Override
    public MobType getMobType()
    {
        return MobType.ARTHROPOD;
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }
}
