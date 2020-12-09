package team.galacticraft.galacticraft.common.core.entities;

import team.galacticraft.galacticraft.common.api.entity.IEntityBreathable;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.GCBlocks;
import team.galacticraft.galacticraft.core.GCItems;
import team.galacticraft.galacticraft.core.util.ConfigManagerCore;
import team.galacticraft.galacticraft.core.util.WorldUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.SharedMonsterAttributes;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

public class EntityEvolvedSkeleton extends Skeleton implements IEntityBreathable, ITumblable
{
    private static final EntityDataAccessor<Float> SPIN_PITCH = SynchedEntityData.defineId(EntityEvolvedSkeleton.class, EntityDataSerializers.FLOAT);
    private float tumbling = 0F;
    private float tumbleAngle = 0F;

    public EntityEvolvedSkeleton(EntityType<? extends EntityEvolvedSkeleton> type, Level worldIn)
    {
        super(type, worldIn);
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35F);
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

//    protected void addRandomDrop()
//    {
//        int r = this.rand.nextInt(12);
//        switch (r)
//        {
//        case 0:
//        case 1:
//        case 2:
//        case 3:
//        case 4:
//        case 5:
//            this.entityDropItem(new ItemStack(GCBlocks.oxygenPipe), 0.0F);
//            break;
//        case 6:
//            //Oxygen tank half empty or less
//            this.entityDropItem(new ItemStack(GCItems.oxTankMedium, 1, 901 + this.rand.nextInt(900)), 0.0F);
//            break;
//        case 7:
//        case 8:
//            this.dropItem(GCItems.canister, 1);
//            break;
//        default:
//            if (ConfigManagerCore.challengeMobDropsAndSpawning.get()) this.dropItem(Items.PUMPKIN_SEEDS, 1);
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
//        Item item = Items.BONE;
//
//        int j = this.rand.nextInt(3);
//
//        if (item != null)
//        {
//            if (lootingModifier > 0)
//            {
//                j += this.rand.nextInt(lootingModifier + 1);
//            }
//
//            for (int k = 1; k < j; ++k)
//            {
//                this.dropItem(item, 1);
//            }
//        }
//
//        j = this.rand.nextInt(3 + lootingModifier);
//        if (j > 1)
//            this.dropItem(Items.BONE, 1);
//
//        //Drop lapis as semi-rare drop if player hit and if dropping bones
//        if (wasRecentlyHit && (ConfigManagerCore.challengeMobDropsAndSpawning.get()) && j > 1 && this.rand.nextInt(12) <= lootingModifier)
//            this.entityDropItem(new ItemStack(Items.DYE, 1, 4), 0.0F);
//
//        if (wasRecentlyHit && this.rand.nextFloat() < 0.025F + (float)lootingModifier * 0.02F)
//        {
//            this.addRandomDrop();
//        }
//    } TODO Loot

    @Override
    public void setTumbling(float value)
    {
        if (value != 0F)
        {
            if (this.tumbling == 0F)
            {
                this.tumbling = (this.level.random.nextFloat() + 0.5F) * value;
            }
        }
        else
        {
            this.tumbling = 0F;
        }
    }

    @Override
    public void tick()
    {
        super.tick();
        if (this.isAlive())
        {
            if (this.tumbling != 0F)
            {
                if (this.onGround)
                {
                    this.tumbling = 0F;
                }
            }

            if (!this.level.isClientSide)
            {
                this.setSpinPitch(this.tumbling);
            }
            else
            {
                this.tumbling = this.getSpinPitch();
                this.tumbleAngle -= this.tumbling;
                if (this.tumbling == 0F && this.tumbleAngle != 0F)
                {
                    this.tumbleAngle *= 0.8F;
                    if (Math.abs(this.tumbleAngle) < 1F)
                    {
                        this.tumbleAngle = 0F;
                    }
                }
            }
        }
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.getEntityData().define(SPIN_PITCH, 0.0F);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt)
    {
        super.readAdditionalSaveData(nbt);
        this.tumbling = nbt.getFloat("tumbling");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt)
    {
        super.addAdditionalSaveData(nbt);
        nbt.putFloat("tumbling", this.tumbling);
    }

    public float getSpinPitch()
    {
        return this.getEntityData().get(SPIN_PITCH);
    }

    public void setSpinPitch(float pitch)
    {
        this.getEntityData().set(SPIN_PITCH, pitch);
    }

    @Override
    public float getTumbleAngle(float partial)
    {
        float angle = this.tumbleAngle - partial * this.tumbling;
        if (angle > 360F)
        {
            this.tumbleAngle -= 360F;
            angle -= 360F;
        }
        if (angle < 0F)
        {
            this.tumbleAngle += 360F;
            angle += 360F;
        }
        return angle;
    }

    @Override
    public float getTumbleAxisX()
    {
        double velocity2 = this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z;
        if (velocity2 == 0D)
        {
            return 1F;
        }
        return (float) (this.getDeltaMovement().z / Mth.sqrt(velocity2));
    }

    @Override
    public float getTumbleAxisZ()
    {
        double velocity2 = this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z;
        if (velocity2 == 0D)
        {
            return 0F;
        }
        return (float) (this.getDeltaMovement().x / Mth.sqrt(velocity2));
    }
}
