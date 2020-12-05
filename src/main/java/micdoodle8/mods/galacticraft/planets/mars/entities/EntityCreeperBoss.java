package micdoodle8.mods.galacticraft.planets.mars.entities;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.entities.EntityAIArrowAttack;
import micdoodle8.mods.galacticraft.core.entities.EntityBossBase;
import micdoodle8.mods.galacticraft.core.entities.EntitySkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.ConfigManagerPlanets;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.SharedMonsterAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class EntityCreeperBoss extends EntityBossBase implements IEntityBreathable, RangedAttackMob
{
    protected long ticks = 0;
    public int headsRemaining = 3;
    private Entity targetEntity;

    public EntityCreeperBoss(EntityType<? extends EntityCreeperBoss> type, Level worldIn)
    {
        super(type, worldIn);
//        this.setSize(2.0F, 7.0F);
//        this.isImmuneToFire = true;
    }

    public static EntityCreeperBoss create(Level world)
    {
        return new EntityCreeperBoss(MarsEntities.CREEPER_BOSS, world);
    }

    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EntityAIArrowAttack(this, 1.0D, 25, 20.0F));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage)
    {
        if (damageSource.getMsgId().equals("fireball"))
        {
            if (this.isInvulnerableTo(damageSource))
            {
                return false;
            }
            else if (super.hurt(damageSource, damage))
            {
                Entity entity = damageSource.getEntity();

                if (this.getPassengers().contains(entity) && this.getVehicle() != entity)
                {
                    if (entity != this && entity instanceof LivingEntity)
                    {
                        this.setTarget((LivingEntity) entity);
                    }

                    return true;
                }
                else
                {
                    return true;
                }
            }
            else
            {
                return false;
            }
        }

        return false;
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0F * ConfigManagerCore.dungeonBossHealthMod.get());
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.05F);
    }

    @Override
    public void knockback(Entity par1Entity, float par2, double par3, double par5)
    {
    }

    @Override
    public boolean isPushable()
    {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        this.playSound(GCSounds.bossOuch, this.getSoundVolume(), this.getVoicePitch() - 0.15F);
        return null;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return null;
    }

    //    @Override
//    protected String getHurtSound()
//    {
//        this.playSound(Constants.TEXTURE_PREFIX + "entity.ouch", this.getSoundVolume(), this.getSoundPitch() - 0.15F);
//        return null;
//    }
//
//    @Override
//    protected String getDeathSound()
//    {
//        return null;
//    }

    @Override
    protected void tickDeath()
    {
        super.tickDeath();

        if (!this.level.isClientSide)
        {
            if (this.deathTicks == 1)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_PLAY_SOUND_BOSS_DEATH, GCCoreUtil.getDimensionType(this.level), new Object[]{getVoicePitch() - 0.1F}), new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 40.0D, GCCoreUtil.getDimensionType(this.level)));
            }
        }
    }

    @Override
    public void aiStep()
    {
        this.ticks++;

        if (this.getHealth() <= 0)
        {
            this.headsRemaining = 0;
        }
        else if (this.getHealth() <= this.getMaxHealth() / 3.0)
        {
            this.headsRemaining = 1;
        }
        else if (this.getHealth() <= 2 * (this.getMaxHealth() / 3.0))
        {
            this.headsRemaining = 2;
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

        super.aiStep();
    }

//    @Override
//    protected Item getDropItem()
//    {
//        return Items.ARROW;
//    }
//
//    @Override
//    public ItemEntity entityDropItem(ItemStack par1ItemStack, float par2)
//    {
//        final ItemEntity entityitem = new ItemEntity(this.world, this.getPosX(), this.getPosY() + par2, this.getPosZ(), par1ItemStack);
//        entityitem.motionY = -2.0D;
//        entityitem.setDefaultPickupDelay();
//        if (this.captureDrops)
//        {
//            this.capturedDrops.add(entityitem);
//        }
//        else
//        {
//            this.world.addEntity(entityitem);
//        }
//        return entityitem;
//    }
//
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
    public ItemStack getGuaranteedLoot(Random rand)
    {
        List<ItemStack> stackList = new LinkedList<>();
        stackList.addAll(GalacticraftRegistry.getDungeonLoot(2));
        boolean hasT3Rocket = false;
        boolean hasAstroMiner = false;
        // Check if player seems to have Tier 3 rocket or Astro Miner already - in that case we don't want more
        // (we don't really want him giving powerful schematics to his friends who are still on Overworld) 
        final Player player = this.level.getNearestPlayer(this.getX(), this.getY(), this.getZ(), 20.0, false);
        if (player != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            if (stats != null)
            {
                for (ISchematicPage page : stats.getUnlockedSchematics())
                {
                    if (page.getPageID() == ConfigManagerPlanets.idSchematicRocketT3.get())
                    {
                        hasT3Rocket = true;
                    }
                    else if (page.getPageID() == ConfigManagerPlanets.idSchematicRocketT3.get() + 1)
                    {
                        hasAstroMiner = true;
                    }
                }
            }
        }
        // The following code assumes the list start is hard coded to: Cargo Rocket, T3 Rocket, Astro Miner in that order
        // (see MarsModule.init())
        //
        // Remove schematics which he already has
        if (hasT3Rocket && hasAstroMiner)
        {
            // (but do not remove both, otherwise the list is too short)
            if (stackList.size() == 3)
            {
                stackList.remove(1 + rand.nextInt(2));
            }
            else
            {
                stackList.remove(2);
                stackList.remove(1);
            }
        }
        else if (hasT3Rocket)
        {
            stackList.remove(1);
        }
        else if (hasAstroMiner)
        {
            stackList.remove(2);
        }
        // If he does not yet have the T3 rocket, limit the list size to 2 so 50% chance of getting it
        // otherwise return the full list (note: addons could have added more schematics to the list)
        int range = (!hasT3Rocket) ? 2 : stackList.size();
        return stackList.get(rand.nextInt(range)).copy();
    }

    @Override
    public void performRangedAttack(LivingEntity entitylivingbase, float f)
    {
        this.level.levelEvent(null, 1024, new BlockPos(this), 0);
        double d3 = this.getX();
        double d4 = this.getY() + 5.5D;
        double d5 = this.getZ();
        double d6 = entitylivingbase.getX() - d3;
        double d7 = entitylivingbase.getY() + entitylivingbase.getEyeHeight() * 0.5D - d4;
        double d8 = entitylivingbase.getZ() - d5;
        EntityProjectileTNT projectileTNT = EntityProjectileTNT.createEntityProjectileTNT(this.level, this, d6 * 0.5D, d7 * 0.5D, d8 * 0.5D);
        projectileTNT.setPosRaw(d3, d4, d5);
        this.level.addFreshEntity(projectileTNT);
    }

    @Override
    public int getChestTier()
    {
        return 2;
    }

    @Override
    public void dropKey()
    {
        this.spawnAtLocation(new ItemStack(MarsItems.key, 1), 0.5F);
    }

    @Override
    public BossEvent.BossBarColor getHealthBarColor()
    {
        return BossEvent.BossBarColor.YELLOW;
    }

//    @Override
//    public void setSwingingArms(boolean swingingArms) {}

    @Override
    public void kill()
    {
        this.setHealth(0.0F);
    }
}
