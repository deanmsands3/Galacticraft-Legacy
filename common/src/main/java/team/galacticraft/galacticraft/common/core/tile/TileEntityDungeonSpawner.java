package team.galacticraft.galacticraft.common.core.tile;

import team.galacticraft.galacticraft.common.api.vector.Vector3;
import team.galacticraft.galacticraft.common.core.GCBlockNames;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.core.entities.*;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ObjectHolder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TileEntityDungeonSpawner<E extends Entity> extends TileEntityAdvanced
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.bossSpawner)
    public static BlockEntityType<TileEntityDungeonSpawner<?>> TYPE;

    public Class<E> bossClass;
    public IBoss boss;
    public boolean spawned;
    public boolean isBossDefeated;
    public boolean playerInRange;
    public boolean lastPlayerInRange;
    private Vector3 roomCoords;
    private Vector3 roomSize;
    public long lastKillTime;
    private BlockPos chestPos;
    private AABB range15 = null;
    private AABB rangeBounds = null;
    private AABB rangeBoundsPlus3 = null;
    private AABB rangeBoundsPlus11 = null;

    public TileEntityDungeonSpawner()
    {
        super(TYPE);
    }

    public TileEntityDungeonSpawner(BlockEntityType<? extends TileEntityDungeonSpawner<E>> type, Class<E> bossClass)
    {
        super(type);
        this.bossClass = bossClass;
    }

    public TileEntityDungeonSpawner(Class<E> bossClass)
    {
        super(TYPE);
        this.bossClass = bossClass;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    public void tick()
    {
        super.tick();

        if (this.roomCoords == null)
        {
            return;
        }

        if (!this.level.isClientSide)
        {
            if (this.range15 == null)
            {
                final Vector3 thisVec = new Vector3(this);
                this.range15 = new AABB(thisVec.x - 15, thisVec.y - 15, thisVec.z - 15, thisVec.x + 15, thisVec.y + 15, thisVec.z + 15);
                this.rangeBounds = new AABB(this.roomCoords.intX(), this.roomCoords.intY(), this.roomCoords.intZ(), this.roomCoords.intX() + this.roomSize.intX(), this.roomCoords.intY() + this.roomSize.intY(), this.roomCoords.intZ() + this.roomSize.intZ());
                this.rangeBoundsPlus3 = this.rangeBounds.inflate(3, 3, 3);
            }

            if (this.lastKillTime > 0 && Util.getMillis() - lastKillTime > 900000) // 15 minutes
            {
                this.lastKillTime = 0;
                this.isBossDefeated = false;
                //After 15 minutes a new boss is able to be spawned 
            }

            final List<E> l = this.level.getEntitiesOfClass(bossClass, this.range15);

            for (final Entity e : l)
            {
                if (e.isAlive())
                {
                    this.boss = (IBoss) e;
                    this.spawned = true;
                    this.isBossDefeated = false;
                    this.boss.onBossSpawned(this);
                }
            }

            List<Monster> entitiesWithin = this.level.getEntitiesOfClass(Monster.class, this.rangeBoundsPlus3);

            for (Entity mob : entitiesWithin)
            {
                if (this.getDisabledCreatures().contains(mob.getClass()))
                {
                    mob.remove();
                }
            }

            List<Player> playersWithin = this.level.getEntitiesOfClass(Player.class, this.rangeBounds);

            this.playerInRange = !playersWithin.isEmpty();

            if (this.playerInRange)
            {
                if (!this.lastPlayerInRange && !this.spawned)
                {
                    //Try to create a boss entity
                    if (this.boss == null && !this.isBossDefeated)
                    {
                        try
                        {
                            Method m = this.bossClass.getDeclaredMethod("create", Level.class);
                            this.boss = (IBoss) m.invoke(null, this.level);
                            ((Entity) this.boss).setPos(this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 1.0, this.getBlockPos().getZ() + 0.5);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    //Now spawn the boss
                    if (this.boss != null)
                    {
                        if (this.boss instanceof Mob)
                        {
                            Mob bossLiving = (Mob) this.boss;
                            bossLiving.finalizeSpawn(level, level.getCurrentDifficultyAt(new BlockPos(bossLiving)), MobSpawnType.SPAWNER, null, null);
                            this.level.addFreshEntity(bossLiving);
                            this.playSpawnSound(bossLiving);
                            this.spawned = true;
                        }
                    }
                }
            }

            this.lastPlayerInRange = this.playerInRange;
        }
    }

    public void playSpawnSound(Entity entity)
    {

    }

    public List<Class<? extends Mob>> getDisabledCreatures()
    {
        List<Class<? extends Mob>> list = new ArrayList<Class<? extends Mob>>();
        list.add(EntityEvolvedSkeleton.class);
        list.add(EntityEvolvedCreeper.class);
        list.add(EntityEvolvedZombie.class);
        list.add(EntityEvolvedSpider.class);
        return list;
    }

    public void setRoom(Vector3 coords, Vector3 size)
    {
        this.roomCoords = coords;
        this.roomSize = size;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);

        this.playerInRange = this.lastPlayerInRange = nbt.getBoolean("playerInRange");
        this.isBossDefeated = nbt.getBoolean("defeated");

        try
        {
            this.bossClass = (Class<E>) Class.forName(nbt.getString("bossClass"));
        }
        catch (Exception e)
        {
            // This exception will be thrown when read is called from TileEntity.handleUpdateTag
            // but we only care if an exception is thrown on server EnvType read
            if (!this.level.isClientSide)
            {
                e.printStackTrace();
            }
        }

        this.roomCoords = new Vector3();
        this.roomCoords.x = nbt.getFloat("roomCoordsX");
        this.roomCoords.y = nbt.getFloat("roomCoordsY");
        this.roomCoords.z = nbt.getFloat("roomCoordsZ");
        this.roomSize = new Vector3();
        this.roomSize.x = nbt.getFloat("roomSizeX");
        this.roomSize.y = nbt.getFloat("roomSizeY");
        this.roomSize.z = nbt.getFloat("roomSizeZ");

        if (nbt.contains("lastKillTime"))
        {
            this.lastKillTime = nbt.getLong("lastKillTime");
        }
        else if (nbt.contains("lastKillTimeNew"))
        {
            long savedTime = nbt.getLong("lastKillTimeNew");
            this.lastKillTime = savedTime == 0 ? 0 : savedTime + Util.getMillis();
        }


        if (nbt.contains("chestPosNull") && !nbt.getBoolean("chestPosNull"))
        {
            this.chestPos = new BlockPos(nbt.getInt("chestX"), nbt.getInt("chestY"), nbt.getInt("chestZ"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);

        nbt.putBoolean("playerInRange", this.playerInRange);
        nbt.putBoolean("defeated", this.isBossDefeated);
        nbt.putString("bossClass", this.bossClass.getCanonicalName());

        if (this.roomCoords != null)
        {
            nbt.putDouble("roomCoordsX", this.roomCoords.x);
            nbt.putDouble("roomCoordsY", this.roomCoords.y);
            nbt.putDouble("roomCoordsZ", this.roomCoords.z);
            nbt.putDouble("roomSizeX", this.roomSize.x);
            nbt.putDouble("roomSizeY", this.roomSize.y);
            nbt.putDouble("roomSizeZ", this.roomSize.z);
        }

        nbt.putLong("lastKillTimeNew", this.lastKillTime == 0 ? 0 : this.lastKillTime - Util.getMillis());

        nbt.putBoolean("chestPosNull", this.chestPos == null);
        if (this.chestPos != null)
        {
            nbt.putInt("chestX", this.chestPos.getX());
            nbt.putInt("chestY", this.chestPos.getY());
            nbt.putInt("chestZ", this.chestPos.getZ());
        }
        return nbt;
    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }

    @Override
    public double getPacketRange()
    {
        return 0;
    }

    @Override
    public int getPacketCooldown()
    {
        return 0;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return false;
    }

    public BlockPos getChestPos()
    {
        return chestPos;
    }

    public void setChestPos(BlockPos chestPos)
    {
        this.chestPos = chestPos;
    }

    public AABB getRangeBounds()
    {
        if (this.rangeBounds == null)
        {
            this.rangeBounds = new AABB(this.roomCoords.intX(), this.roomCoords.intY(), this.roomCoords.intZ(), this.roomCoords.intX() + this.roomSize.intX(), this.roomCoords.intY() + this.roomSize.intY(), this.roomCoords.intZ() + this.roomSize.intZ());
        }

        return this.rangeBounds;
    }

    public AABB getRangeBoundsPlus11()
    {
        if (this.rangeBoundsPlus11 == null)
        {
            this.rangeBoundsPlus11 = this.getRangeBounds().inflate(11, 11, 11);
        }

        return this.rangeBoundsPlus11;
    }
}
