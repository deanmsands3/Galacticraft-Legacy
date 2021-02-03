package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.advancement.GCTriggers;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.SharedMonsterAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;
import java.util.Random;

public class EvolvedSkeletonBossEntity extends EntityBossBase implements IEntityBreathable, RangedAttackMob, IIgnoreShift
{
    protected long ticks = 0;
    private static final ItemStack defaultHeldItem = new ItemStack(Items.BOW, 1);

    public int throwTimer;
    public int postThrowDelay = 20;
    public Entity thrownEntity;
    public Entity targetEntity;

    public EvolvedSkeletonBossEntity(EntityType<? extends EvolvedSkeletonBossEntity> type, Level worldIn)
    {
        super(type, worldIn);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EntityAIArrowAttack(this, 1.0D, 25, 10.0F));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false, true));
    }

    public static EvolvedSkeletonBossEntity create(Level world)
    {
        return new EvolvedSkeletonBossEntity(GCEntities.EVOLVED_SKELETON_BOSS, world);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty)
    {
        super.populateDefaultEquipmentSlots(difficulty);
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
        this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.BOW));
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        double difficulty = 0;
        switch (this.level.getDifficulty())
        {
        case HARD:
            difficulty = 2D;
            break;
        case NORMAL:
            difficulty = 1D;
            break;
        }
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(150.0F * ConfigManagerCore.dungeonBossHealthMod.get());
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D + 0.075 * difficulty);
    }

    @Override
    protected void tickDeath()
    {
        super.tickDeath();

        if (!this.level.isClientSide)
        {
            if (this.deathTicks == 100)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(PacketSimple.EnumSimplePacket.C_PLAY_SOUND_BOSS_DEATH, GCCoreUtil.getDimensionType(this.level), new Object[]{1.5F}), new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 40.0D, GCCoreUtil.getDimensionType(this.level)));
            }
        }
    }

    @Override
    public boolean isInWater()
    {
        return false;
    }

    @Override
    public boolean updateInWaterState()
    {
        return false;
    }

    @Override
    public void positionRider(Entity passenger)
    {
        if (this.hasPassenger(passenger))
        {
            final double offsetX = Math.sin(-this.yHeadRot / Constants.RADIANS_TO_DEGREES_D);
            final double offsetZ = Math.cos(this.yHeadRot / Constants.RADIANS_TO_DEGREES_D);
            final double offsetY = 2 * Math.cos((this.throwTimer + this.postThrowDelay) * 0.05F);

            passenger.setPos(this.getX() + offsetX, this.getY() + this.getRideHeight() + passenger.getRidingHeight() + offsetY, this.getZ() + offsetZ);
        }
    }

    @Override
    public void knockback(Entity par1Entity, float par2, double par3, double par5)
    {
    }

    @Override
    public void playerTouch(Player par1EntityPlayer)
    {
        if (!this.isNoAi() && this.getPassengers().isEmpty() && this.postThrowDelay == 0 && this.throwTimer == 0 && par1EntityPlayer.equals(this.targetEntity) && this.deathTicks == 0)
        {
            if (!this.level.isClientSide)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_PLAY_SOUND_BOSS_LAUGH, GCCoreUtil.getDimensionType(this.level), new Object[]{}), new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 40.0D, GCCoreUtil.getDimensionType(this.level)));
                par1EntityPlayer.startRiding(this);
            }

            this.throwTimer = 40;
        }

        super.playerTouch(par1EntityPlayer);
    }

    @Override
    public boolean isPushable()
    {
        return false;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        this.playSound(GCSounds.bossOoh, this.getSoundVolume(), this.getVoicePitch() + 1.0F);
        return null;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return null;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public ItemStack getHeldItem()
//    {
//        return EntitySkeletonBoss.defaultHeldItem;
//    }


    @Override
    public MobType getMobType()
    {
        return MobType.UNDEAD;
    }

    @Override
    public void tick()
    {
        this.ticks++;

        for (int j2 = 0; j2 < level.players().size(); ++j2) //World#isAnyPlayerWithinRangeAt
        {
            Player entityplayer = level.players().get(j2);

            if (EntitySelector.NO_SPECTATORS.test(entityplayer) && entityplayer instanceof ServerPlayer)
            {
                double d0 = entityplayer.distanceToSqr(this.getX(), this.getY(), this.getZ());

                if (d0 < 20 * 20)
                {
                    GCTriggers.FIND_MOON_BOSS.trigger(((ServerPlayer) entityplayer));

                }
            }
        }

        if (!this.level.isClientSide && this.getHealth() <= 150.0F * ConfigManagerCore.dungeonBossHealthMod.get() / 2)
        {
            this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        }

        final Player player = this.level.getNearestPlayer(this.getX(), this.getY(), this.getZ(), 20.0, false);

        if (player != null && !player.equals(this.targetEntity))
        {
            if (this.distanceToSqr(player) < 400.0D)
            {
                this.getNavigation().createPath(player, 0);
                this.targetEntity = player;
            }
        }
        else
        {
            this.targetEntity = null;
        }

        if (this.throwTimer > 0)
        {
            this.throwTimer--;
        }

        if (this.postThrowDelay > 0)
        {
            this.postThrowDelay--;
        }

        if (!this.getPassengers().isEmpty() && this.throwTimer == 0)
        {
            this.postThrowDelay = 20;

            this.thrownEntity = this.getPassengers().get(0);

            if (!this.level.isClientSide)
            {
                this.ejectPassengers();
            }
        }

        if (this.thrownEntity != null && this.postThrowDelay == 18)
        {
            double d0 = this.getX() - this.thrownEntity.getX();
            double d1;

            for (d1 = this.getZ() - this.thrownEntity.getZ(); d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D)
            {
                d0 = (Math.random() - Math.random()) * 0.01D;
            }


            if (!this.level.isClientSide)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_PLAY_SOUND_BOW, GCCoreUtil.getDimensionType(this.level), new Object[]{}), new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 40.0, GCCoreUtil.getDimensionType(this.level)));
            }
            ((Player) this.thrownEntity).hurtDir = (float) Math.atan2(d1, d0) * Constants.RADIANS_TO_DEGREES - this.yRot;

            this.thrownEntity.hasImpulse = true;
            final float f = Mth.sqrt(d0 * d0 + d1 * d1);
            final float f1 = 2.4F;
            this.thrownEntity.setDeltaMovement(this.thrownEntity.getDeltaMovement().multiply(0.5, 0.5, 0.5));
            this.thrownEntity.setDeltaMovement(this.thrownEntity.getDeltaMovement().add(-d0 / f * f1, f1 / 5.0, -d1 / f * f1));

            if (this.thrownEntity.getDeltaMovement().y > 0.4000000059604645D)
            {
                this.thrownEntity.setDeltaMovement(this.getDeltaMovement().x, 0.4000000059604645D, this.getDeltaMovement().z);
            }
        }

        super.tick();
    }

    @Override
    public ItemEntity spawnAtLocation(ItemStack par1ItemStack, float par2)
    {
        final ItemEntity entityitem = new ItemEntity(this.level, this.getX(), this.getY() + par2, this.getZ(), par1ItemStack);
        entityitem.setDeltaMovement(entityitem.getDeltaMovement().x, -2.0, entityitem.getDeltaMovement().z);
        entityitem.setDefaultPickUpDelay();
        if (captureDrops() != null)
        {
            this.captureDrops().add(entityitem);
        }
        else
        {
            this.level.addFreshEntity(entityitem);
        }
        return entityitem;
    }

//    @Override
//    protected void dropFewItems(boolean b, int i)
//    {
//        if (this.rand.nextInt(200) - i >= 5)
//        {
//            return;
//        }
//
//        if (i > 0)
//        {
//            final ItemStack var2 = new ItemStack(Items.BOW);
//            EnchantmentHelper.addRandomEnchantment(this.rand, var2, 5, false);
//            this.entityDropItem(var2, 0.0F);
//        }
//        else
//        {
//            this.dropItem(Items.BOW, 1);
//        }
//    } TODO Item drops

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float f)
    {
        if (!this.getPassengers().isEmpty())
        {
            return;
        }

        Arrow arrow = new Arrow(this.level, this);
        double d0 = target.getX() - this.getX();
        double d1 = target.getBoundingBox().minY + (double) (target.getBbHeight() / 3.0F) - arrow.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Mth.sqrt(d0 * d0 + d2 * d2);
        arrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float) (14 - this.level.getDifficulty().getId() * 4));

        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(arrow);
    }

    @Override
    public boolean shouldIgnoreShiftExit()
    {
        return true;
    }

    @Override
    public ItemStack getGuaranteedLoot(Random rand)
    {
        List<ItemStack> stackList = GalacticraftRegistry.getDungeonLoot(1);
        return stackList.get(rand.nextInt(stackList.size())).copy();
    }

    @Override
    public int getChestTier()
    {
        return 1;
    }

    @Override
    public void dropKey()
    {
        this.spawnAtLocation(new ItemStack(GCItems.TIER_1_DUNGEON_KEY, 1), 0.5F);
    }

    @Override
    public BossEvent.BossBarColor getHealthBarColor()
    {
        return BossInfo.Color.GREEN;
    }

//    @Override
//    public void setSwingingArms(boolean swingingArms)
//    {
//        // TODO Auto-generated method stub
//        //Unused in this Galacticraft entity
//    }
}
