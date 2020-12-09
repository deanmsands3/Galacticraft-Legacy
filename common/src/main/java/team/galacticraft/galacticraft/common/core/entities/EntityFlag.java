package team.galacticraft.galacticraft.common.core.entities;

import team.galacticraft.galacticraft.common.api.vector.Vector3;
import team.galacticraft.galacticraft.core.GCItems;
import team.galacticraft.galacticraft.core.util.ClientUtil;
import team.galacticraft.galacticraft.core.wrappers.FlagData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityFlag extends Entity
{
    private static final EntityDataAccessor<String> OWNER = SynchedEntityData.defineId(EntityFlag.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(EntityFlag.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> FACING_ANGLE = SynchedEntityData.defineId(EntityFlag.class, EntityDataSerializers.INT);
    public double xPosition;
    public double yPosition;
    public double zPosition;
    public boolean indestructable = false;
    public FlagData flagData;

    public EntityFlag(EntityType<EntityFlag> type, Level world)
    {
        super(type, world);
        this.noCulling = true;
    }

    public EntityFlag(Level world, double x, double y, double z, int dir)
    {
        this(GCEntities.FLAG, world);
        this.setFacingAngle(dir);
        this.setPos(x, y, z);
        this.xPosition = x;
        this.yPosition = y;
        this.zPosition = z;
    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean hurt(DamageSource par1DamageSource, float par2)
    {
        Entity e = par1DamageSource.getEntity();
        boolean flag = e instanceof Player && ((Player) e).abilities.instabuild;

        if (!this.level.isClientSide && this.isAlive() && !this.indestructable)
        {
            if (this.isInvulnerableTo(par1DamageSource))
            {
                return false;
            }
            else
            {
                this.markHurt();
                this.setDamage(this.getDamage() + par2 * 10);
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundType.METAL.getBreakSound(), SoundSource.BLOCKS, SoundType.METAL.getVolume(), SoundType.METAL.getPitch() + 1.0F);

                if (e instanceof Player && ((Player) e).abilities.instabuild)
                {
                    this.setDamage(100.0F);
                }

                if (flag || this.getDamage() > 40)
                {
                    if (!this.getPassengers().isEmpty())
                    {
                        this.ejectPassengers();
                    }

                    if (flag)
                    {
                        this.remove();
                    }
                    else
                    {
                        this.remove();
                        this.dropItemStack();
                    }
                }

                return true;
            }
        }
        else
        {
            return true;
        }
    }

    @Override
    public ItemStack getPickedResult(HitResult target)
    {
        return new ItemStack(GCItems.flag, 1);
    }

    public int getFlagWidth()
    {
        return 25;
    }

    public int getFlagHeight()
    {
        return 40;
    }

    @Override
    public boolean isPickable()
    {
        return true;
    }

    @Override
    protected boolean isMovementNoisy()
    {
        return false;
    }

    @Override
    public AABB getCollideAgainstBox(Entity par1Entity)
    {
        return par1Entity.getCollideBox();
    }

    @Override
    public boolean isPushable()
    {
        return false;
    }

    @Override
    protected void defineSynchedData()
    {
        this.entityData.define(OWNER, "");
        this.entityData.define(DAMAGE, 0.0F);
        this.entityData.define(FACING_ANGLE, -1);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt)
    {
        this.setOwner(nbt.getString("Owner"));
        this.indestructable = nbt.getBoolean("Indestructable");

        this.xPosition = nbt.getDouble("TileX");
        this.yPosition = nbt.getDouble("TileY");
        this.zPosition = nbt.getDouble("TileZ");
        this.setFacingAngle(nbt.getInt("AngleI"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt)
    {
        nbt.putString("Owner", String.valueOf(this.getOwner()));
        nbt.putBoolean("Indestructable", this.indestructable);
        nbt.putInt("AngleI", this.getFacingAngle());
        nbt.putDouble("TileX", this.xPosition);
        nbt.putDouble("TileY", this.yPosition);
        nbt.putDouble("TileZ", this.zPosition);
    }

    public void dropItemStack()
    {
        this.spawnAtLocation(new ItemStack(GCItems.flag, 1), 0.0F);
    }

    @Override
    public void tick()
    {
        super.tick();

        if ((this.tickCount - 1) % 20 == 0 && this.level.isClientSide)
        {
            this.flagData = ClientUtil.updateFlagData(this.getOwner(), Minecraft.getInstance().player.distanceTo(this) < 50.0D);
        }

        Vector3 vec = new Vector3((float) this.getX(), (float) this.getY(), (float) this.getZ());
        vec = vec.translate(new Vector3(0, -1, 0));
        final Block blockAt = vec.getBlock(this.level);

        if (blockAt != null)
        {
            BlockPos pos = new BlockPos(vec.intX(), vec.intY(), vec.intZ());
            if (blockAt instanceof FenceBlock)
            {

            }
            else if (blockAt.isAir(this.level.getBlockState(pos), this.level, pos))
            {
                this.setDeltaMovement(getDeltaMovement().add(0.0, -0.02F, 0.0F));
            }
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    @Override
    public boolean interact(Player player, InteractionHand hand)
    {
        if (!this.level.isClientSide)
        {
            this.setFacingAngle(this.getFacingAngle() + 3);
        }

        return true;
    }

    public void setOwner(String par1)
    {
        this.entityData.set(OWNER, String.valueOf(par1));
    }

    public String getOwner()
    {
        return this.entityData.get(OWNER);
    }

    public void setDamage(float par1)
    {
        this.entityData.set(DAMAGE, Float.valueOf(par1));
    }

    public float getDamage()
    {
        return this.entityData.get(DAMAGE);
    }

    public void setFacingAngle(int par1)
    {
        this.entityData.set(FACING_ANGLE, Integer.valueOf(par1));
    }

    public int getFacingAngle()
    {
        return this.entityData.get(FACING_ANGLE);
    }
}
