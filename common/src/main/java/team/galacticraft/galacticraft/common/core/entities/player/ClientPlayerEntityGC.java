package team.galacticraft.galacticraft.common.core.entities.player;

import team.galacticraft.galacticraft.common.api.prefab.entity.EntitySpaceshipBase;
import team.galacticraft.galacticraft.common.api.entity.ICameraZoomEntity;
import team.galacticraft.galacticraft.common.api.event.ZeroGravityEvent;
import team.galacticraft.galacticraft.common.api.item.IHoldableItem;
import team.galacticraft.galacticraft.common.api.world.IZeroGDimension;
import team.galacticraft.galacticraft.core.client.EventHandlerClient;
import team.galacticraft.galacticraft.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Direction;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.StatsCounter;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.monster.SharedMonsterAttributes;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;
import java.util.List;

public class ClientPlayerEntityGC extends LocalPlayer
{
    private boolean lastIsFlying;
    private boolean sneakLast;
    private int lastLandingTicks;
    private boolean checkedCape = false;
    private ResourceLocation galacticraftCape = null;

    public ClientPlayerEntityGC(Minecraft mcIn, ClientLevel worldIn, ClientPacketListener netHandler, StatsCounter statFileWriter, ClientRecipeBook book)
    {
        super(mcIn, worldIn, netHandler, statFileWriter, book);
    }

//    @Override
//    public void wakeUpPlayer(boolean immediately, boolean updateWorldFlag, boolean setSpawn)
//    {
////        if (!ClientProxyCore.playerClientHandler.wakeUpPlayer(this, immediately, updateWorldFlag, setSpawn))
////        {
////            super.wakeUpPlayer(immediately, updateWorldFlag, setSpawn);
////        } TODO Cryo chamber
//    }

    @Override
    public void stopSleeping()
    {
        super.stopSleeping();
    }

    @Override
    public boolean isInWall()
    {
        return ClientProxyCore.playerClientHandler.isEntityInsideOpaqueBlock(this, super.isInWall());
    }

    @Override
    public boolean isEffectiveAi()
    {
        return true;
    }


    @Override
    public void aiStep()
    {
        ClientProxyCore.playerClientHandler.onTickPre(this);
        try
        {
            if (this.level.getDimension() instanceof IZeroGDimension)
            {

                //  from: ClientPlayerEntity
                ++this.sprintTime;
                if (this.sprintTriggerTime > 0) {
                    --this.sprintTriggerTime;
                }

                this.oPortalTime = this.portalTime;
                if (this.isInsidePortal) {
                    if (this.minecraft.screen != null && !this.minecraft.screen.isPauseScreen()) {
                        if (this.minecraft.screen instanceof AbstractContainerScreen) {
                            this.closeContainer();
                        }

                        this.minecraft.setScreen((Screen)null);
                    }

                    if (this.portalTime == 0.0F) {
                        this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PORTAL_TRIGGER, this.random.nextFloat() * 0.4F + 0.8F));
                    }

                    this.portalTime += 0.0125F;
                    if (this.portalTime >= 1.0F) {
                        this.portalTime = 1.0F;
                    }

                    this.isInsidePortal = false;
                } else if (this.hasEffect(MobEffects.CONFUSION) && this.getEffect(MobEffects.CONFUSION).getDuration() > 60) {
                    this.portalTime += 0.006666667F;
                    if (this.portalTime > 1.0F) {
                        this.portalTime = 1.0F;
                    }
                } else {
                    if (this.portalTime > 0.0F) {
                        this.portalTime -= 0.05F;
                    }

                    if (this.portalTime < 0.0F) {
                        this.portalTime = 0.0F;
                    }
                }

                this.processDimensionDelay();

                boolean flag = this.input.jumping;
                boolean flag1 = this.input.shiftKeyDown;
                boolean flag2 = this.movingForward();
                this.input.tick(this.isMovingSlowly());
                net.minecraftforge.client.ForgeHooksClient.onInputUpdate(this, this.input);
                this.minecraft.getTutorial().onInput(this.input);
                if (this.isUsingItem() && !this.isPassenger()) {
                    this.input.leftImpulse *= 0.2F;
                    this.input.forwardImpulse *= 0.2F;
                    this.sprintTriggerTime = 0;
                }

                //CUSTOM-------------------
                GCPlayerStatsClient stats = GCPlayerStatsClient.get(this);
                if (stats.getLandingTicks() > 0)
                {
                    this.input.leftImpulse *= 0.5F;
                    this.input.forwardImpulse *= 0.5F;
                }
                //-----------END CUSTOM

                // Omit auto-jump in zero-g
                boolean flag3 = false;

                net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent event = new net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent(this);
                if (!this.noPhysics && !net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {
                    this.checkInBlock(this.getX() - (double)this.getBbWidth() * 0.35D, event.getMinY(), this.getZ() + (double)this.getBbWidth() * 0.35D);
                    this.checkInBlock(this.getX() - (double)this.getBbWidth() * 0.35D, event.getMinY(), this.getZ() - (double)this.getBbWidth() * 0.35D);
                    this.checkInBlock(this.getX() + (double)this.getBbWidth() * 0.35D, event.getMinY(), this.getZ() - (double)this.getBbWidth() * 0.35D);
                    this.checkInBlock(this.getX() + (double)this.getBbWidth() * 0.35D, event.getMinY(), this.getZ() + (double)this.getBbWidth() * 0.35D);
                }

                boolean flag4 = (float)this.getFoodData().getFoodLevel() > 6.0F || this.abilities.mayfly;
                if ((this.onGround || this.isUnderWater()) && !flag1 && !flag2 && this.movingForward() && !this.isSprinting() && flag4 && !this.isUsingItem() && !this.hasEffect(MobEffects.BLINDNESS)) {
                    if (this.sprintTriggerTime <= 0 && !this.minecraft.options.keySprint.isDown()) {
                        this.sprintTriggerTime = 7;
                    } else {
                        this.setSprinting(true);
                    }
                }

                if (!this.isSprinting() && (!this.isInWater() || this.isUnderWater()) && this.movingForward() && flag4 && !this.isUsingItem() && !this.hasEffect(MobEffects.BLINDNESS) && this.minecraft.options.keySprint.isDown()) {
                    this.setSprinting(true);
                }

                if (this.isSprinting()) {
                    boolean flag5 = !this.input.hasForwardImpulse() || !flag4;
                    boolean flag6 = flag5 || this.horizontalCollision || this.isInWater() && !this.isUnderWater();
                    if (this.isSwimming()) {
                        if (!this.onGround && !this.input.shiftKeyDown && flag5 || !this.isInWater()) {
                            this.setSprinting(false);
                        }
                    } else if (flag6) {
                        this.setSprinting(false);
                    }
                }

                boolean flag7 = false;
                if (this.abilities.mayfly) {
                    if (this.minecraft.gameMode.isAlwaysFlying()) {
                        if (!this.abilities.flying) {
                            this.abilities.flying = true;
                            flag7 = true;
                            this.updateSwimAmount();
                        }
                    } else if (!flag && this.input.jumping && !flag3) {
                        if (this.jumpTriggerTime == 0) {
                            this.jumpTriggerTime = 7;
                        } else if (!this.isSwimming()) {
                            this.abilities.flying = !this.abilities.flying;
                            flag7 = true;
                            this.updateSwimAmount();
                            this.jumpTriggerTime = 0;
                        }
                    }
                }

                if (this.input.jumping && !flag7 && !flag && !this.abilities.flying && !this.isPassenger() && !this.onLadder()) {
                    ItemStack itemstack = this.getItemBySlot(EquipmentSlot.CHEST);
                    if (itemstack.getItem() == Items.ELYTRA && ElytraItem.isFlyEnabled(itemstack) && this.tryToStartFallFlying()) {
                        this.connection.send(new ServerboundPlayerCommandPacket(this, ServerboundPlayerCommandPacket.Action.START_FALL_FLYING));
                    }
                }

                // Omit elytra update in zero-g
                if (this.isInWater() && this.input.shiftKeyDown) {
                    this.goDownInWater();
                }

                if (this.isUnderLiquid(FluidTags.WATER)) {
                    int i = this.isSpectator() ? 10 : 1;
                    this.waterVisionTime = Mth.clamp(this.waterVisionTime + i, 0, 600);
                } else if (this.waterVisionTime > 0) {
                    this.isUnderLiquid(FluidTags.WATER);
                    this.waterVisionTime = Mth.clamp(this.waterVisionTime - 10, 0, 600);
                }

                if (this.abilities.flying && this.isControlledCamera()) {
                    int j = 0;
                    if (this.input.shiftKeyDown) {
                        --j;
                    }

                    if (this.input.jumping) {
                        ++j;
                    }

                    if (j != 0) {
                        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, (double)((float)j * this.abilities.getFlyingSpeed() * 3.0F), 0.0D));
                    }
                }

                //Omit horse jumping - no horse jumping in space!

                // -from: PlayerEntity

                //Omit fly toggle timer

                if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)) {
                    if (this.getHealth() < this.getMaxHealth() && this.tickCount % 20 == 0) {
                        this.heal(1.0F);
                    }

                    if (this.foodData.needsFood() && this.tickCount % 10 == 0) {
                        this.foodData.setFoodLevel(this.foodData.getFoodLevel() + 1);
                    }
                }

                this.inventory.tick();
                this.oBob = this.bob;

                //  from: LivingEntity
                if (this.noJumpDelay > 0) {
                    --this.noJumpDelay;
                }

                if (this.isControlledByLocalInstance()) {
                    this.lerpSteps = 0;
                    this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
                }

                if (this.lerpSteps > 0) {
                    double d0 = this.getX() + (this.lerpX - this.getX()) / (double)this.lerpSteps;
                    double d2 = this.getY() + (this.lerpY - this.getY()) / (double)this.lerpSteps;
                    double d4 = this.getZ() + (this.lerpZ - this.getZ()) / (double)this.lerpSteps;
                    double d6 = Mth.wrapDegrees(this.lerpYRot - (double)this.yRot);
                    this.yRot = (float)((double)this.yRot + d6 / (double)this.lerpSteps);
                    this.xRot = (float)((double)this.xRot + (this.lerpXRot - (double)this.xRot) / (double)this.lerpSteps);
                    --this.lerpSteps;
                    this.setPos(d0, d2, d4);
                    this.setRot(this.yRot, this.xRot);
                } else if (!this.isEffectiveAi()) {
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
                }

                if (this.lerpHeadSteps > 0) {
                    this.yHeadRot = (float)((double)this.yHeadRot + Mth.wrapDegrees(this.lyHeadRot - (double)this.yHeadRot) / (double)this.lerpHeadSteps);
                    --this.lerpHeadSteps;
                }

                Vec3 vec3d = this.getDeltaMovement();
                double d1 = vec3d.x;
                double d3 = vec3d.y;
                double d5 = vec3d.z;
                if (Math.abs(vec3d.x) < 0.003D) {
                    d1 = 0.0D;
                }

                if (Math.abs(vec3d.y) < 0.003D) {
                    d3 = 0.0D;
                }

                if (Math.abs(vec3d.z) < 0.003D) {
                    d5 = 0.0D;
                }

                this.setDeltaMovement(d1, d3, d5);
                this.level.getProfiler().push("ai");
                if (this.isImmobile()) {
                    this.jumping = false;
                    this.xxa = 0.0F;
                    this.zza = 0.0F;
                } else if (this.isEffectiveAi()) {
                    this.level.getProfiler().push("newAi");
                    this.serverAiStep();
                    this.level.getProfiler().pop();
                }

                this.level.getProfiler().pop();
                this.level.getProfiler().push("jump");
                if (this.jumping) {
                    if (!(this.waterHeight > 0.0D) || this.onGround && !(this.waterHeight > 0.4D)) {
                        if (this.isInLava()) {
                            this.jumpInLiquid(FluidTags.LAVA);
                        } else if ((this.onGround || this.waterHeight > 0.0D && this.waterHeight <= 0.4D) && this.noJumpDelay == 0) {
                            this.jumpFromGround();
                            this.noJumpDelay = 10;
                        }
                    } else {
                        this.jumpInLiquid(FluidTags.WATER);
                    }
                } else {
                    this.noJumpDelay = 0;
                }

                this.level.getProfiler().pop();
                this.level.getProfiler().push("travel");
                this.xxa *= 0.98F;
                this.zza *= 0.98F;

                // CUSTOM--------------
                AABB aABB = this.getBoundingBox();
                if ((aABB.minY % 1D) == 0.5D)
                {
                    this.setBoundingBox(aABB.move(0D, 0.00001D, 0D));
                }
                //-----------END CUSTOM

                // Omit elytra in zero-g

                AABB axisalignedbb = this.getBoundingBox();
                this.travel(new Vec3((double)this.xxa, (double)this.yya, (double)this.zza));
                this.level.getProfiler().pop();
                this.level.getProfiler().push("push");
                if (this.autoSpinAttackTicks > 0) {
                    --this.autoSpinAttackTicks;
                    this.checkAutoSpinAttack(axisalignedbb, this.getBoundingBox());
                }

                this.pushEntities();
                this.level.getProfiler().pop();

                AttributeInstance iattributeinstance = this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
                if (!this.level.isClientSide) {
                    iattributeinstance.setBaseValue((double)this.abilities.getWalkingSpeed());
                }

//                this.jumpMovementFactor = 0.02F;
//                if (this.isSprinting()) {
//                    this.jumpMovementFactor = (float)((double)this.jumpMovementFactor + 0.005999999865889549D);
//                }

                this.setSpeed((float)iattributeinstance.getValue());
                float f;
                if (this.onGround && !(this.getHealth() <= 0.0F) && !this.isSwimming()) {
                    f = Math.min(0.1F, Mth.sqrt(getHorizontalDistanceSqr(this.getDeltaMovement())));
                } else {
                    f = 0.0F;
                }

                this.bob += (f - this.bob) * 0.4F;
                if (this.getHealth() > 0.0F && !this.isSpectator()) {
                    AABB axisalignedbb1;
                    if (this.isPassenger() && !this.getVehicle().removed) {
                        axisalignedbb1 = this.getBoundingBox().minmax(this.getVehicle().getBoundingBox()).inflate(1.0D, 0.0D, 1.0D);
                    } else {
                        axisalignedbb1 = this.getBoundingBox().inflate(1.0D, 0.5D, 1.0D);
                    }

                    List<Entity> list = this.level.getEntities(this, axisalignedbb1);

                    for(int i = 0; i < list.size(); ++i) {
                        Entity entity = list.get(i);
                        if (!entity.removed) {
                            entity.playerTouch(this);
                        }
                    }
                }

                this.playShoulderEntityAmbientSound(this.getShoulderEntityLeft());
                this.playShoulderEntityAmbientSound(this.getShoulderEntityRight());
                if (!this.level.isClientSide && (this.fallDistance > 0.5F || this.isInWater()) || this.abilities.flying || this.isSleeping()) {
                    this.removeEntitiesOnShoulder();
                }

                //  from: ClientPlayerEntity
                //(modified CUSTOM)
                if (this.lastIsFlying != this.abilities.flying)
                {
                    this.lastIsFlying = this.abilities.flying;
                    this.updateSwimAmount();
                }
            }
            else
            {
                super.aiStep();
            }
        }
        catch (RuntimeException e)
        {
            LogManager.getLogger().error("A mod has crashed while Minecraft was doing a normal player tick update.  See details below.  GCEntityClientPlayerMP is in this because that is the player class name when Galacticraft is installed.  This is =*NOT*= a bug in Galacticraft, please report it to the mod indicated by the first lines of the crash report.");
            throw (e);
        }
        ClientProxyCore.playerClientHandler.onTickPost(this);
    }

    @Override
    public boolean isFallFlying() {
        return !(this.level.getDimension() instanceof IZeroGDimension) && super.isFallFlying();
    }

    private boolean movingForward() {
        double d0 = 0.8D;
        return this.isUnderWater() ? this.input.hasForwardImpulse() : (double)this.input.forwardImpulse >= 0.8D;
    }

    private void playShoulderEntityAmbientSound(@Nullable CompoundTag p_192028_1_) {
        if (p_192028_1_ != null && !p_192028_1_.contains("Silent") || !p_192028_1_.getBoolean("Silent")) {
            String s = p_192028_1_.getString("id");
            EntityType.byString(s).filter((p_213830_0_) -> {
                return p_213830_0_ == EntityType.PARROT;
            }).ifPresent((p_213834_1_) -> {
                Parrot.playAmbientSound(this.level, this);
            });
        }

    }

    @Override
    public void move(MoverType type, Vec3 pos)
    {
        super.move(type, pos);
        ClientProxyCore.playerClientHandler.move(this, type, pos);
    }

//    @Override
//    public void onUpdate()
//    {
//        ClientProxyCore.playerClientHandler.onUpdate(this);
//        super.onUpdate();
//    }

    @Override
    public boolean isShiftKeyDown()
    {
        if (this.level.getDimension() instanceof IZeroGDimension)
        {
            ZeroGravityEvent zeroGEvent = new ZeroGravityEvent.SneakOverride(this);
            MinecraftForge.EVENT_BUS.post(zeroGEvent);
            if (zeroGEvent.isCanceled())
            {
                return super.isShiftKeyDown();
            }

            GCPlayerStatsClient stats = GCPlayerStatsClient.get(this);
            if (stats.getLandingTicks() > 0)
            {
                if (this.lastLandingTicks == 0)
                {
                    this.lastLandingTicks = stats.getLandingTicks();
                }

                this.sneakLast = stats.getLandingTicks() < this.lastLandingTicks;
                return sneakLast;
            }
            else
            {
                this.lastLandingTicks = 0;
            }
//            if (stats.getFreefallHandler().pjumpticks > 0)
//            {
//                this.sneakLast = true;
//                return true;
//            } TODO Freefall
            if (EventHandlerClient.sneakRenderOverride)
            {
                if (this.input != null && this.input.shiftKeyDown != this.sneakLast)
                {
                    return false;
                }
                //                if (stats.freefallHandler.testFreefall(this)) return false;
//                if (stats.isInFreefall() || stats.getFreefallHandler().onWall)
//                {
//                    this.sneakLast = false;
//                    return false;
//                } TODO Freefall
            }
            this.sneakLast = this.input != null && this.input.shiftKeyDown;
        }
        else
        {
            this.sneakLast = false;
            if (EventHandlerClient.sneakRenderOverride && this.onGround && this.inventory.getSelected() != null && this.inventory.getSelected().getItem() instanceof IHoldableItem && !(this.getVehicle() instanceof ICameraZoomEntity))
            {
                IHoldableItem holdableItem = (IHoldableItem) this.inventory.getSelected().getItem();

                if (holdableItem.shouldCrouch(this))
                {
                    return true;
                }
            }
        }
        return super.isShiftKeyDown();
    }

//    @Override
//    public float getEyeHeight(Pose p_213307_1_)
//    {
//        float f = eyeHeight;
//
//        if (this.isPlayerSleeping())
//        {
//            return 0.2F;
//        }
//
//        float ySize = 0.0F;
//        if (this.world.getDimension() instanceof IZeroGDimension)
//        {
//            GCPlayerStatsClient stats = GCPlayerStatsClient.get(this);
//            if (stats.getLandingTicks() > 0)
//            {
//                ySize = stats.getLandingYOffset()[stats.getLandingTicks()];
//                if (this.movementInput.sneak && ySize < 0.08F)
//                {
//                    ySize = 0.08F;
//                }
//            }
////            else if (stats.getFreefallHandler().pjumpticks > 0)
////            {
////                ySize = 0.01F * stats.getFreefallHandler().pjumpticks;
////            }
////            else if (this.isSneaking() && !stats.isInFreefall() && !stats.getFreefallHandler().onWall)
////            {
////                ySize = 0.08F;
////            } TODO Freefall
//        }
//        else if (this.isSneaking() && this.movementInput != null && this.movementInput.sneak)
//        {
//            ySize = 0.08F;
//        }
//
//        return f - ySize;
//    }

    @Nullable
    @Override
    public Direction getBedOrientation()
    {
        return ClientProxyCore.playerClientHandler.getBedDirection(this, super.getBedOrientation());
    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void setVelocity(double xx, double yy, double zz)
//    {
//    	if (this.world.getDimension() instanceof WorldProviderOrbit)
//    	{
//    		((WorldProviderOrbit)this.world.getDimension()).setVelocityClient(this, xx, yy, zz);
//    	}
//    	super.setVelocity(xx, yy, zz);
//    }
//

    /*@Override
    public void setInPortal()
    {
    	if (!(this.world.getDimension() instanceof IGalacticraftWorldProvider))
    	{
    		super.setInPortal();
    	}
    } TODO Fix disable of portal */

    @Override
    public ResourceLocation getCloakTextureLocation()
    {
        if (this.getVehicle() instanceof EntitySpaceshipBase)
        {
            // Don't draw any cape if riding a rocket (the cape renders outside the rocket model!)
            return null;
        }

        ResourceLocation vanillaCape = super.getCloakTextureLocation();

        if (!this.checkedCape)
        {
            PlayerInfo networkplayerinfo = this.getPlayerInfo();
            this.galacticraftCape = ClientProxyCore.capeMap.get(networkplayerinfo.getProfile().getId().toString().replace("-", ""));
            this.checkedCape = true;
        }

        if ((ConfigManagerCore.overrideCapes.get() || vanillaCape == null) && galacticraftCape != null)
        {
            return galacticraftCape;
        }

        return vanillaCape;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public int getBrightnessForRender()
//    {
//        double height = this.posY + (double) this.getEyeHeight();
//        if (height > 255D)
//        {
//            height = 255D;
//        }
//        BlockPos blockpos = new BlockPos(this.posX, height, this.posZ);
//        return this.world.isBlockLoaded(blockpos) ? this.world.getCombinedLight(blockpos, 0) : 0;
//    }
}
