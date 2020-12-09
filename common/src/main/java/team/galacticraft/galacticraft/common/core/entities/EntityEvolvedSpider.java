package team.galacticraft.galacticraft.common.core.entities;

import team.galacticraft.galacticraft.common.api.entity.IEntityBreathable;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.GCItems;
import team.galacticraft.galacticraft.core.util.ConfigManagerCore;
import team.galacticraft.galacticraft.core.util.GCLog;
import team.galacticraft.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.SharedMonsterAttributes;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;

public class EntityEvolvedSpider extends Spider implements IEntityBreathable
{
    public EntityEvolvedSpider(EntityType<? extends EntityEvolvedSpider> type, Level world)
    {
        super(type, world);
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(22.0D);
        double difficulty = 0;
        switch (this.level.getDifficulty())
        {
        case HARD:
            difficulty = 2D;
            break;
        case NORMAL:
            difficulty = 1D;
            break;
        default:
            break;
        }
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3 + 0.05 * difficulty);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2D + difficulty);
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(LevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag)
    {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);

        // onInitialSpawn is called for EntitySpider, which has a chance of adding a vanilla skeleton, remove these
        for (Entity entity : getPassengers())
        {
            entity.stopRiding();
            if (!(entity instanceof Skeleton))
            {
                GCLog.severe("Removed unexpected passenger from spider: " + entity);
            }
            else
            {
                entity.remove();
            }
        }

        if (this.level.random.nextInt(100) == 0)
        {
            EntityEvolvedSkeleton entityskeleton = new EntityEvolvedSkeleton(GCEntities.EVOLVED_SKELETON, level);
            entityskeleton.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0F);
            entityskeleton.finalizeSpawn(worldIn, difficultyIn, reason, null, null);
            this.level.addFreshEntity(entityskeleton);
            entityskeleton.startRiding(this);
        }

        if (spawnDataIn == null)
        {
            spawnDataIn = new SpiderEffectsGroupData();

            if (this.level.getDifficulty() == Difficulty.HARD && this.level.random.nextFloat() < 0.1F * difficultyIn.getSpecialMultiplier())
            {
                ((SpiderEffectsGroupData) spawnDataIn).setRandomEffect(this.level.random);
            }
        }

        if (spawnDataIn instanceof SpiderEffectsGroupData)
        {
            MobEffect potion = ((SpiderEffectsGroupData) spawnDataIn).effect;

            if (potion != null)
            {
                this.addEffect(new MobEffectInstance(potion, Integer.MAX_VALUE));
            }
        }

        return spawnDataIn;
    }

//    protected void addRandomDrop()
//    {
//        switch (this.rand.nextInt(14))
//        {
//        case 0:
//        case 1:
//        case 2:
//            this.dropItem(GCItems.cheeseCurd, 1);
//            break;
//        case 3:
//        case 4:
//        case 5:
//            this.dropItem(Items.FERMENTED_SPIDER_EYE, 1);
//            break;
//        case 6:
//        case 7:
//            //Oxygen tank half empty or less
//            this.entityDropItem(new ItemStack(GCItems.oxTankMedium, 1, 901 + this.rand.nextInt(900)), 0.0F);
//            break;
//        case 8:
//            this.dropItem(GCItems.oxygenGear, 1);
//            break;
//        case 9:
//            this.dropItem(GCItems.oxygenConcentrator, 1);
//            break;
//        default:
//            if (ConfigManagerCore.challengeMobDropsAndSpawning.get())
//            {
//                this.dropItem(Items.NETHER_WART, 1);
//            }
//            break;
//        }
//    } TODO Loot

//    @Override
//    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
//    {
//        super.dropLoot(wasRecentlyHit, lootingModifier > 1 ? lootingModifier - 1 : 0, source);
//        if (wasRecentlyHit && this.rand.nextFloat() < 0.025F + (float)lootingModifier * 0.02F)
//        {
//            this.addRandomDrop();
//        }
//    }
}
