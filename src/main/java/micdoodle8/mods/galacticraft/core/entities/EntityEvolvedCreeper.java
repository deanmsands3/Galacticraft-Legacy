package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.SharedMonsterAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import java.util.UUID;

public class EntityEvolvedCreeper extends Creeper implements IEntityBreathable
{
    //    private float sizeXBase = -1.0F;
//    private float sizeYBase;
    private static final EntityDataAccessor<Boolean> IS_CHILD = SynchedEntityData.defineId(EntityEvolvedCreeper.class, EntityDataSerializers.BOOLEAN);
    private static final UUID babySpeedBoostUUID = UUID.fromString("ef67a435-32a4-4efd-b218-e7431438b109");
    private static final AttributeModifier babySpeedBoostModifier = new AttributeModifier(babySpeedBoostUUID, "Baby speed boost evolved creeper", 0.5D, AttributeModifier.Operation.MULTIPLY_BASE);

    public EntityEvolvedCreeper(EntityType<? extends EntityEvolvedCreeper> type, Level worldIn)
    {
        super(type, worldIn);
        this.goalSelector.availableGoals.clear();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SwellGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(IS_CHILD, false);
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25.0D);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt)
    {
        super.addAdditionalSaveData(nbt);

        if (this.isBaby())
        {
            nbt.putBoolean("IsBaby", true);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt)
    {
        super.readAdditionalSaveData(nbt);

        if (nbt.getBoolean("IsBaby"))
        {
            this.setChild(true);
        }
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

//    public void setChildSize(boolean isChild)
//    {
//        this.setCreeperScale(isChild ? 0.5F : 1.0F);
//    }

//    @Override
//    protected final void setSize(float sizeX, float sizeY)
//    {
//        boolean flag = this.sizeXBase > 0.0F && this.sizeYBase > 0.0F;
//        this.sizeXBase = sizeX;
//        this.sizeYBase = sizeY;
//
//        if (!flag)
//        {
//            this.setCreeperScale(1.0F);
//        }
//    }

//    protected final void setCreeperScale(float scale)
//    {
//        super.setSize(this.sizeXBase * scale, this.sizeYBase * scale);
    //FMLLog.info("" + this.sizeYBase + " " + scale);
//    }

    @Override
    public boolean isBaby()
    {
        return this.entityData.get(IS_CHILD);
    }

    @Override
    protected int getExperienceReward(Player p_70693_1_)
    {
        if (this.isBaby())
        {
            this.xpReward = (this.xpReward * 5) / 2;
        }

        return super.getExperienceReward(p_70693_1_);
    }

    public void setChild(boolean isChild)
    {
        this.entityData.set(IS_CHILD, isChild);

        if (this.level != null && !this.level.isClientSide)
        {
            AttributeInstance iattributeinstance = this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            iattributeinstance.removeModifier(babySpeedBoostModifier);

            if (isChild)
            {
                iattributeinstance.addModifier(babySpeedBoostModifier);
            }
        }

//        this.setChildSize(isChild);
    }

//    @Override
//    protected void jump()
//    {
//        this.motionY = 0.45D / WorldUtil.getGravityFactor(this);
//        if (this.motionY < 0.22D)
//        {
//            this.motionY = 0.22D;
//        }
//
//        if (this.isPotionActive(Effects.JUMP_BOOST))
//        {
//            this.motionY += (this.getActivePotionEffect(Effects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
//        }
//
//        if (this.isSprinting())
//        {
//            float f = this.rotationYaw / Constants.RADIANS_TO_DEGREES;
//            this.motionX -= MathHelper.sin(f) * 0.2F;
//            this.motionZ += MathHelper.cos(f) * 0.2F;
//        }
//
//        this.isAirBorne = true;
//        ForgeHooks.onLivingJump(this);
//    }

//    @Override
//    protected Item getDropItem()
//    {
//        if (this.isBurning())
//        {
//            return Items.BLAZE_ROD;
//        }
//        return Items.REDSTONE;
//    } TODO Loot

//    protected void addRandomDrop()
//    {
//        switch (this.rand.nextInt(12))
//        {
//        case 0:
//        case 1:
//        case 2:
//        case 3:
//            this.entityDropItem(new ItemStack(Blocks.SAND), 0.0F);
//            break;
//        case 4:
//        case 5:
//            //Oxygen tank half empty or less
//            this.entityDropItem(new ItemStack(GCItems.oxTankMedium, 1, 901 + this.rand.nextInt(900)), 0.0F);
//            break;
//        case 6:
//            this.dropItem(GCItems.oxygenGear, 1);
//            break;
//        case 7:
//        case 8:
//            this.entityDropItem(new ItemStack(Blocks.ICE), 0.0F);
//            break;
//        default:
//            if (ConfigManagerCore.challengeMobDropsAndSpawning.get()) this.dropItem(Items.REEDS, 1);
//            break;
//        }
//    }
//
//    @Override
//    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
//    {
//        // No loot table
//        this.dropFewItems(wasRecentlyHit, lootingModifier);
//        this.dropEquipment(wasRecentlyHit, lootingModifier);
//    }
//
//    @Override
//    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
//    {
//        super.dropFewItems(wasRecentlyHit, lootingModifier);
//
//        if (wasRecentlyHit && this.rand.nextFloat() < 0.025F + (float)lootingModifier * 0.02F)
//        {
//            this.addRandomDrop();
//        }
//    }
}
