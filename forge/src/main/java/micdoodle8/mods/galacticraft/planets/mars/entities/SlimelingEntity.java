package micdoodle8.mods.galacticraft.planets.mars.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.planets.mars.MarsModuleClient;
import micdoodle8.mods.galacticraft.planets.mars.inventory.InventorySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.SitGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.SharedMonsterAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.EnumSet;
import java.util.UUID;

public class SlimelingEntity extends TamableAnimal implements IEntityBreathable
{
    public InventorySlimeling slimelingInventory = new InventorySlimeling(this);

    private static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(SlimelingEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> COLOR_RED = SynchedEntityData.defineId(SlimelingEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> COLOR_GREEN = SynchedEntityData.defineId(SlimelingEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> COLOR_BLUE = SynchedEntityData.defineId(SlimelingEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> AGE = SynchedEntityData.defineId(SlimelingEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> NAME = SynchedEntityData.defineId(SlimelingEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> FAV_FOOD_ID = SynchedEntityData.defineId(SlimelingEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> ATTACK_DAMAGE = SynchedEntityData.defineId(SlimelingEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> KILLS = SynchedEntityData.defineId(SlimelingEntity.class, EntityDataSerializers.INT);

    public float colorRed;
    public float colorGreen;
    public float colorBlue;
    public long ticksAlive;
    public int age = 0;
    public final int MAX_AGE = 100000;
    public String slimelingName = GCCoreUtil.translate("gui.message.unnamed");
    public int favFoodID = 1;
    public float attackDamage = 0.05F;
    public int kills;

    public SlimelingEntity(EntityType<? extends SlimelingEntity> type, Level worldIn)
    {
        super(type, worldIn);
//        this.setSize(0.45F, 0.7F);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.sitGoal = new EntityAISitGC(this);
        this.goalSelector.addGoal(2, this.sitGoal);
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(6, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NonTameRandomTargetGoal(this, SludgelingEntity.class, false, p_apply_1_ -> p_apply_1_ instanceof SludgelingEntity));
        this.setTame(false);

        switch (this.random.nextInt(3))
        {
        case 0:
            this.colorRed = 1.0F;
            break;
        case 1:
            this.colorBlue = 1.0F;
            break;
        case 2:
            this.colorRed = 1.0F;
            this.colorGreen = 1.0F;
            break;
        }

        this.setRandomFavFood();
    }

    public static SlimelingEntity createEntitySlimeling(Level worldIn, float r, float g, float b)
    {
        SlimelingEntity slimeling = new SlimelingEntity(MarsEntities.SLIMELING, worldIn);
        slimeling.colorRed = r;
        slimeling.colorGreen = g;
        slimeling.colorBlue = b;
        return slimeling;
    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public LivingEntity getOwner()
    {
        LivingEntity owner = super.getOwner();
        if (owner == null)
        {
            UUID ownerId = getOwnerUUID();
            if (ownerId != null)
            {
                return this.level.getPlayerByUUID(ownerId);
            }
        }
        return owner;
    }

    @Override
    public boolean isOwnedBy(LivingEntity entityLivingBase)
    {
        return entityLivingBase == this.getOwner();
    }

    @Override
    public boolean canBreatheUnderwater()
    {
        return true;
    }

    public float getSlimelingSize()
    {
        return this.getScale() * 2.0F;
    }

//    @Override
//    public void setScaleForAge(boolean par1)
//    {
//        this.setScale(this.getSlimelingSize());
//    }

    @Override
    public boolean isBaby()
    {
        return this.getAge() / (float) this.MAX_AGE < 0.33F;
    }

    private void setRandomFavFood()
    {
        switch (this.random.nextInt(10))
        {
        case 0:
            this.favFoodID = Item.getId(Items.GOLD_INGOT);
            break;
        case 1:
            this.favFoodID = Item.getId(Items.FLINT_AND_STEEL);
            break;
        case 2:
            this.favFoodID = Item.getId(Items.BAKED_POTATO);
            break;
        case 3:
            this.favFoodID = Item.getId(Items.STONE_SWORD);
            break;
        case 4:
            this.favFoodID = Item.getId(Items.GUNPOWDER);
            break;
        case 5:
            this.favFoodID = Item.getId(Items.WOODEN_HOE);
            break;
        case 6:
            this.favFoodID = Item.getId(Items.EMERALD);
            break;
        case 7:
            this.favFoodID = Item.getId(Items.TROPICAL_FISH);
            break;
        case 8:
            this.favFoodID = Item.getId(Items.REPEATER);
            break;
        case 9:
            this.favFoodID = Item.getId(Items.OAK_BOAT);
            break;
        }
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getMaxHealthSlimeling());
    }

//    @Override
//    public boolean isAIEnabled()
//    {
//        return true;
//    }


    @Override
    protected void customServerAiStep()
    {
        this.entityData.set(HEALTH, this.getHealth());
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(HEALTH, this.getHealth());
        this.entityData.define(COLOR_RED, this.colorRed);
        this.entityData.define(COLOR_GREEN, this.colorGreen);
        this.entityData.define(COLOR_BLUE, this.colorBlue);
        this.entityData.define(AGE, this.age);
        this.entityData.define(NAME, "");
        this.entityData.define(FAV_FOOD_ID, this.favFoodID);
        this.entityData.define(ATTACK_DAMAGE, this.attackDamage);
        this.entityData.define(KILLS, this.kills);
        this.setSlimelingName(GCCoreUtil.translate("gui.message.unnamed"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt)
    {
        super.addAdditionalSaveData(nbt);
        nbt.put("SlimelingInventory", this.slimelingInventory.writeToNBT(new ListTag()));
        nbt.putFloat("SlimeRed", this.colorRed);
        nbt.putFloat("SlimeGreen", this.colorGreen);
        nbt.putFloat("SlimeBlue", this.colorBlue);
        nbt.putInt("SlimelingAge", this.age);
        nbt.putString("SlimelingName", this.slimelingName);
        nbt.putInt("FavFoodID", this.favFoodID);
        nbt.putFloat("SlimelingDamage", this.attackDamage);
        nbt.putInt("SlimelingKills", this.kills);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt)
    {
        super.readAdditionalSaveData(nbt);
        this.slimelingInventory.readFromNBT(nbt.getList("SlimelingInventory", 10));
        this.colorRed = nbt.getFloat("SlimeRed");
        this.colorGreen = nbt.getFloat("SlimeGreen");
        this.colorBlue = nbt.getFloat("SlimeBlue");
        this.age = nbt.getInt("SlimelingAge");
        this.slimelingName = nbt.getString("SlimelingName");
        this.favFoodID = nbt.getInt("FavFoodID");
        this.attackDamage = nbt.getFloat("SlimelingDamage");
        this.kills = nbt.getInt("SlimelingKills");
        this.setColorRed(this.colorRed);
        this.setColorGreen(this.colorGreen);
        this.setColorBlue(this.colorBlue);
        this.setAge(this.age);
        this.setSlimelingName(this.slimelingName);
        this.setKillCount(this.kills);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        this.playSound(SoundEvents.SLIME_BLOCK_STEP, this.getSoundVolume(), 1.1F);
        return null;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        this.playSound(GCSounds.slimeDeath, this.getSoundVolume(), 0.8F);
        return null;
    }

//    @Override
//    protected Item getDropItem()
//    {
//        return Items.SLIME_BALL;
//    }

    @Override
    public void aiStep()
    {
        super.aiStep();

        if (!this.level.isClientSide)
        {
            if (this.ticksAlive <= 0)
            {
                this.setColorRed(this.colorRed);
                this.setColorGreen(this.colorGreen);
                this.setColorBlue(this.colorBlue);
            }

            this.ticksAlive++;

            if (this.ticksAlive % 2 == 0)
            {
                if (this.age < this.MAX_AGE)
                {
                    this.age++;
                }

                this.setAge(Math.min(this.age, this.MAX_AGE));
            }

            this.setFavoriteFood(this.favFoodID);
            this.setAttackDamage(this.attackDamage);
            this.setKillCount(this.kills);
//            this.setCargoSlot(this.slimelingInventory.getStackInSlot(1));
        }

        if (!this.level.isClientSide)
        {
            this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getMaxHealthSlimeling());

            if (this.getOwnerUUID() != null)
            {
                Entity owner = this.getOwner();

                if (owner != null)
                {
                    this.setOwnerUUID(owner.getUUID());
                }
            }
        }
    }

    private double getMaxHealthSlimeling()
    {
        if (this.isTame())
        {
            return 20.001D + 30.0 * ((double) this.age / (double) this.MAX_AGE);
        }
        else
        {
            return 8.0D;
        }
    }

    @Override
    public float getEyeHeight(Pose pose)
    {
        return this.getBbHeight() * 0.8F;
    }

    @Override
    public boolean hurt(DamageSource par1DamageSource, float par2)
    {
        if (this.isInvulnerableTo(par1DamageSource))
        {
            return false;
        }
        else
        {
            Entity entity = par1DamageSource.getEntity();
            this.setSittingAI(false);

            if (entity != null && !(entity instanceof Player))
            {
                par2 = (par2 + 1.0F) / 2.0F;
            }

            return super.hurt(par1DamageSource, par2);
        }
    }

    @Override
    public boolean doHurtTarget(Entity par1Entity)
    {
        return par1Entity.hurt(new EntityDamageSource("slimeling", this), this.getDamage());
    }

    public float getDamage()
    {
        int i = this.isTame() ? 5 : 2;
        return i * this.getAttackDamage();
    }

    @Override
    public void setTame(boolean par1)
    {
        super.setTame(par1);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getMaxHealthSlimeling());
    }

    @Override
    public boolean mobInteract(Player player, InteractionHand hand)
    {
        ItemStack itemstack = player.inventory.getSelected();

        if (this.isTame())
        {
            if (!itemstack.isEmpty())
            {
                if (itemstack.getItem() == this.getFavoriteFood())
                {
                    if (this.isOwnedBy(player))
                    {
                        itemstack.shrink(1);

                        if (itemstack.isEmpty())
                        {
                            player.inventory.setItem(player.inventory.selected, ItemStack.EMPTY);
                        }

                        if (this.level.isClientSide)
                        {
                            MarsModuleClient.openSlimelingGui(this, 1);
                        }

                        if (this.random.nextInt(3) == 0)
                        {
                            this.setRandomFavFood();
                        }
                    }
                    else
                    {
                        if (player instanceof ServerPlayer)
                        {
                            GCPlayerStats stats = GCPlayerStats.get(player);
                            if (stats.getChatCooldown() == 0)
                            {
                                player.sendMessage(new TextComponent(GCCoreUtil.translate("gui.slimeling.chat.wrong_player")));
                                stats.setChatCooldown(100);
                            }
                        }
                    }
                }
                else
                {
                    if (this.level.isClientSide)
                    {
                        MarsModuleClient.openSlimelingGui(this, 0);
                    }
                }
            }
            else
            {
                if (this.level.isClientSide)
                {
                    MarsModuleClient.openSlimelingGui(this, 0);
                }
            }

            return true;
        }
        else if (!itemstack.isEmpty() && itemstack.getItem() == Items.SLIME_BALL)
        {
            if (!player.abilities.instabuild)
            {
                itemstack.shrink(1);
            }

            if (itemstack.isEmpty())
            {
                player.inventory.setItem(player.inventory.selected, ItemStack.EMPTY);
            }

            if (!this.level.isClientSide)
            {
                if (this.random.nextInt(3) == 0)
                {
                    this.setTame(true);
                    this.getNavigation().stop();
                    this.setTarget(null);
                    this.setSittingAI(true);
                    this.setHealth(20.0F);
                    this.setOwnerUUID(player.getUUID());
                    this.spawnTamingParticles(true);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                }
                else
                {
                    this.spawnTamingParticles(false);
                    this.level.broadcastEntityEvent(this, (byte) 6);
                }
            }

            return true;
        }

        return super.mobInteract(player, hand);
    }

    public void setSittingAI(boolean sitting)
    {
        this.sitGoal.wantToSit(sitting);
    }

    @Override
    public boolean isFood(ItemStack par1ItemStack)
    {
        return false;
    }

    public SlimelingEntity spawnBabyAnimal(AgableMob par1EntityAgeable)
    {
        if (par1EntityAgeable instanceof SlimelingEntity)
        {
            SlimelingEntity otherSlimeling = (SlimelingEntity) par1EntityAgeable;

            Vector3 colorParentA = new Vector3(this.getColorRed(), this.getColorGreen(), this.getColorBlue());
            Vector3 colorParentB = new Vector3(otherSlimeling.getColorRed(), otherSlimeling.getColorGreen(), otherSlimeling.getColorBlue());
            Vector3 newColor = ColorUtil.addColorsRealistically(colorParentA, colorParentB);
            newColor.x = Math.max(Math.min(newColor.x, 1.0F), 0);
            newColor.y = Math.max(Math.min(newColor.y, 1.0F), 0);
            newColor.z = Math.max(Math.min(newColor.z, 1.0F), 0);
            SlimelingEntity newSlimeling = SlimelingEntity.createEntitySlimeling(this.level, newColor.x, newColor.y, newColor.z);

            UUID id = this.getOwnerUUID();

            if (id != null)
            {
                newSlimeling.setOwnerUUID(id);
                newSlimeling.setTame(true);
            }

            return newSlimeling;
        }

        return null;
    }

    @Override
    public boolean canMate(Animal par1EntityAnimal)
    {
        if (par1EntityAnimal == this)
        {
            return false;
        }
        else if (!this.isTame())
        {
            return false;
        }
        else if (!(par1EntityAnimal instanceof SlimelingEntity))
        {
            return false;
        }
        else
        {
            SlimelingEntity slimeling = (SlimelingEntity) par1EntityAnimal;
            return slimeling.isTame() && !slimeling.isSitting() && this.isInLove() && slimeling.isInLove();
        }
    }

    @Override
    public boolean wantsToAttack(LivingEntity toAttack, LivingEntity owner)
    {
        if (!(toAttack instanceof Creeper) && !(toAttack instanceof Ghast))
        {
            if (toAttack instanceof SlimelingEntity)
            {
                SlimelingEntity slimeling = (SlimelingEntity) toAttack;

                if (slimeling.isTame() && slimeling.getOwner() == owner)
                {
                    return false;
                }
            }

            return !(toAttack instanceof Player && owner instanceof Player && !((Player) owner).canHarmPlayer((Player) toAttack)) && (!(toAttack instanceof Horse) || !((Horse) toAttack).isTamed());
        }
        else
        {
            return false;
        }
    }

    @Override
    public AgableMob getBreedOffspring(AgableMob par1EntityAgeable)
    {
        return this.spawnBabyAnimal(par1EntityAgeable);
    }

    public float getColorRed()
    {
        return this.entityData.get(COLOR_RED);
    }

    public void setColorRed(float color)
    {
        this.entityData.set(COLOR_RED, color);
    }

    public float getColorGreen()
    {
        return this.entityData.get(COLOR_GREEN);
    }

    public void setColorGreen(float color)
    {
        this.entityData.set(COLOR_GREEN, color);
    }

    public float getColorBlue()
    {
        return this.entityData.get(COLOR_BLUE);
    }

    public void setColorBlue(float color)
    {
        this.entityData.set(COLOR_BLUE, color);
    }

    public int getAge()
    {
        return this.entityData.get(AGE);
    }

    public void setAge(int age)
    {
        this.entityData.set(AGE, age);
    }

    public String getSlimelingName()
    {
        return this.entityData.get(NAME);
    }

    public void setSlimelingName(String name)
    {
        this.entityData.set(NAME, name);
    }

    @Override
    public Component getName()
    {
        return new TextComponent(this.getSlimelingName());
    }

    public Item getFavoriteFood()
    {
        return Item.byId(this.entityData.get(FAV_FOOD_ID));
    }

    public void setFavoriteFood(int foodID)
    {
        this.entityData.set(FAV_FOOD_ID, foodID);
    }

    public float getAttackDamage()
    {
        return this.entityData.get(ATTACK_DAMAGE);
    }

    public void setAttackDamage(float damage)
    {
        this.entityData.set(ATTACK_DAMAGE, damage);
    }

    public int getKillCount()
    {
        return this.entityData.get(KILLS);
    }

    public void setKillCount(int damage)
    {
        this.entityData.set(KILLS, damage);
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    public float getScale()
    {
        return this.getAge() / (float) this.MAX_AGE * 0.5F + 0.5F;
    }

    public SitGoal getAiSit()
    {
        return this.sitGoal;
    }

    @Override
    public void die(DamageSource p_70645_1_)
    {
        super.die(p_70645_1_);

        if (!this.level.isClientSide)
        {
            ItemStack bag = this.slimelingInventory.getItem(1);
            if (bag != null && bag.getItem() == MarsItems.SLIMELING_INVENTORY_BAG)
            {
                this.slimelingInventory.removeItem(1, 64);
                this.spawnAtLocation(bag, 0.5F);
            }
        }
    }

    public static class EntityAISitGC extends SitGoal
    {
        private final TamableAnimal theEntity;
        private boolean isSitting;

        public EntityAISitGC(TamableAnimal theEntity)
        {
            super(theEntity);
            this.theEntity = theEntity;
//            this.setMutexBits(5);
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        @Override
        public boolean canUse()
        {
            if (!this.theEntity.isTame())
            {
                return false;
            }
            else if (this.theEntity.isInWater())
            {
                return false;
            }
            else
            {
                Entity e = this.theEntity.getOwner();
                if (e instanceof LivingEntity)
                {
                    LivingEntity living = (LivingEntity) e;
                    return living == null || ((!(this.theEntity.distanceToSqr(living) < 144.0D) || living.getLastHurtByMob() == null) && this.isSitting);
                }
                return false;
            }
        }

        @Override
        public void start()
        {
            this.theEntity.getNavigation().stop();
            this.theEntity.setSitting(true);
        }

        @Override
        public void stop()
        {
            this.theEntity.setSitting(false);
        }

        @Override
        public void wantToSit(boolean isSitting)
        {
            this.isSitting = isSitting;
        }
    }

    @Override
    protected void jumpFromGround()
    {
        this.setDeltaMovement(this.getDeltaMovement().x, 0.48D / WorldUtil.getGravityFactor(this), this.getDeltaMovement().z);
        if (this.getDeltaMovement().y < 0.28D)
        {
//            this.motionY = 0.28D;
            this.setDeltaMovement(this.getDeltaMovement().x, 0.28D, this.getDeltaMovement().z);
        }

        if (this.hasEffect(MobEffects.JUMP))
        {
//            this.motionY += (this.getActivePotionEffect(Effects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y + (this.getEffect(MobEffects.JUMP).getAmplifier() + 1) * 0.1F, this.getDeltaMovement().z);
        }

        if (this.isSprinting())
        {
            float f = this.yRot / Constants.RADIANS_TO_DEGREES;
//            this.motionX -= MathHelper.sin(f) * 0.2F;
//            this.motionZ += MathHelper.cos(f) * 0.2F;
            this.setDeltaMovement(this.getDeltaMovement().x - Mth.sin(f) * 0.2F, this.getDeltaMovement().y, this.getDeltaMovement().z + Mth.cos(f) * 0.2F);
        }

        this.hasImpulse = true;
        ForgeHooks.onLivingJump(this);
    }
}
