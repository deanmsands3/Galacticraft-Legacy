package team.galacticraft.galacticraft.common.core.tile;

import com.mojang.authlib.GameProfile;
import team.galacticraft.galacticraft.common.api.entity.ITelemetry;
import team.galacticraft.galacticraft.common.api.prefab.entity.EntitySpaceshipBase;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3;
import team.galacticraft.galacticraft.common.api.vector.BlockVec3Dim;
import team.galacticraft.galacticraft.core.GCBlockNames;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.GalacticraftCore;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
import team.galacticraft.galacticraft.core.network.PacketSimple;
import team.galacticraft.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import team.galacticraft.galacticraft.core.util.GCLog;
import team.galacticraft.galacticraft.core.util.PlayerUtil;
import team.galacticraft.galacticraft.core.util.WorldUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.dimension.Dimension;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class TileEntityTelemetry extends BlockEntity implements TickableBlockEntity
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.telemetry)
    public static BlockEntityType<TileEntityTelemetry> TYPE;

    //    public Class<?> clientClass;
    public EntityType<?> clientType;
    public int[] clientData = {-1};
    public String clientName;
    public GameProfile clientGameProfile = null;

    public static HashSet<BlockVec3Dim> loadedList = new HashSet<BlockVec3Dim>();
    public Entity linkedEntity;
    private UUID toUpdate = null;
    private int pulseRate = 400;
    private int lastHurttime = 0;
    private int ticks = 0;

    public TileEntityTelemetry()
    {
        super(TYPE);
    }

    @Override
    public void onLoad()
    {
        if (this.level.isClientSide)
        {
            loadedList.add(new BlockVec3Dim(this));
        }
    }

    @Override
    public void setRemoved()
    {
        super.setRemoved();
        if (this.level.isClientSide)
        {
            loadedList.remove(new BlockVec3Dim(this));
        }
    }

    @Override
    public void tick()
    {
        if (!this.level.isClientSide && ++this.ticks % 2 == 0)
        {
            if (this.toUpdate != null)
            {
                this.addTrackedEntity(this.toUpdate);
                this.toUpdate = null;
            }

            String name = null;
            int[] data = {-1, -1, -1, -1, -1};
            String strUUID = "";

            if (linkedEntity != null)
            {
                //Help the Garbage Collector
                if (!linkedEntity.isAlive())
                {
                    linkedEntity = null;
                    name = "";
                    //TODO: track players after death and respawn? or not?
                }
                else
                {
                    if (linkedEntity instanceof ServerPlayer)
                    {
                        name = "$" + linkedEntity.getName();
                    }
                    else
                    {
                        ResourceLocation res = ForgeRegistries.ENTITIES.getKey(linkedEntity.getType());
//                        EntityEntry entityEntry = EntityRegistry.getEntry(linkedEntity.getClass());
//                        if (entityEntry != null && entityEntry.getRegistryName() != null)
//                        {
//                            name = entityEntry.getRegistryName().toString();
//                        }
                        name = res.toString();
                    }

                    if (name == null)
                    {
                        GCLog.info("Telemetry Unit: Error finding name for " + linkedEntity.getClass().getSimpleName());
                        name = "";
                    }

                    double xmotion = linkedEntity.getDeltaMovement().x;
                    double ymotion = linkedEntity instanceof LivingEntity ? linkedEntity.getDeltaMovement().y + 0.078D : linkedEntity.getDeltaMovement().y;
                    double zmotion = linkedEntity.getDeltaMovement().z;
                    data[2] = (int) (Mth.sqrt(xmotion * xmotion + ymotion * ymotion + zmotion * zmotion) * 2000D);

                    if (linkedEntity instanceof ITelemetry)
                    {
                        ((ITelemetry) linkedEntity).transmitData(data);
                    }
                    else if (linkedEntity instanceof LivingEntity)
                    {
                        LivingEntity eLiving = (LivingEntity) linkedEntity;
                        data[0] = eLiving.hurtTime;

                        //Calculate a "pulse rate" based on motion and taking damage
                        this.pulseRate--;
                        if (eLiving.hurtTime > this.lastHurttime)
                        {
                            this.pulseRate += 100;
                        }
                        this.lastHurttime = eLiving.hurtTime;
                        if (eLiving.getVehicle() != null)
                        {
                            data[2] /= 4;  //reduced pulse effect if riding a vehicle
                        }
                        else if (data[2] > 1)
                        {
                            this.pulseRate += 2;
                        }
                        this.pulseRate += Math.max(data[2] - pulseRate, 0) / 4;
                        if (this.pulseRate > 2000)
                        {
                            this.pulseRate = 2000;
                        }
                        if (this.pulseRate < 400)
                        {
                            this.pulseRate = 400;
                        }
                        data[2] = this.pulseRate / 10;

                        data[1] = (int) (eLiving.getHealth() * 100 / eLiving.getMaxHealth());
                        if (eLiving instanceof ServerPlayer)
                        {
                            data[3] = ((ServerPlayer) eLiving).getFoodData().getFoodLevel() * 5;
                            GCPlayerStats stats = GCPlayerStats.get(eLiving);
                            data[4] = stats.getAirRemaining() * 4096 + stats.getAirRemaining2();
                            UUID uuid = eLiving.getUUID();
                            if (uuid != null)
                            {
                                strUUID = uuid.toString();
                            }
                        }
                        else if (eLiving instanceof Horse)
                        {
//                            data[3] = ((EntityHorse) eLiving).getType().ordinal();
                            data[4] = ((Horse) eLiving).getVariant();
                        }
                        else if (eLiving instanceof Villager)
                        {
//                            data[3] = ((VillagerEntity) eLiving).getVillagerData().getProfession();
                            data[4] = ((Villager) eLiving).getAge();
                        }
                        else if (eLiving instanceof Wolf)
                        {
                            data[3] = ((Wolf) eLiving).getCollarColor().getId();
                            data[4] = ((Wolf) eLiving).isInterested() ? 1 : 0;
                        }
                        else if (eLiving instanceof Sheep)
                        {
                            data[3] = ((Sheep) eLiving).getColor().getId();
                            data[4] = ((Sheep) eLiving).isSheared() ? 1 : 0;
                        }
                        else if (eLiving instanceof Cat)
                        {
                            data[3] = ((Cat) eLiving).getCatType();
                        }
                        else if (eLiving instanceof Skeleton)
                        {
//                            data[3] = ((EntitySkeleton) eLiving).getSkeletonType().ordinal();
                        }
                        else if (eLiving instanceof Zombie)
                        {
//                            data[3] = ((EntityZombie) eLiving).isVillager() ? 1 : 0; TODO Fix for MC 1.10
                            data[4] = eLiving.isBaby() ? 1 : 0;
                        }
                    }
                }
            }
            else
            {
                name = "";
            }
            GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_UPDATE_TELEMETRY, this.level.getDimension().getType(), new Object[]{this.getBlockPos(), name, data[0], data[1], data[2], data[3], data[4], strUUID}), new TargetPoint(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), 320D, this.level.getDimension().getType()));
        }
    }

    @Environment(EnvType.CLIENT)
    public void receiveUpdate(List<Object> data, DimensionType dimID)
    {
        String name = (String) data.get(1);
        if (name.startsWith("$"))
        {
            //It's a player name
//            this.clientClass = ServerPlayerEntity.class;
            this.clientType = EntityType.PLAYER;
            String strName = name.substring(1);
            this.clientName = strName;
            this.clientGameProfile = PlayerUtil.getSkinForName(strName, (String) data.get(7), dimID);
        }
        else
        {
            EntityType<?> entityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(name));
            this.clientType = entityEntry;
        }
        this.clientData = new int[5];
        for (int i = 2; i < 7; i++)
        {
            this.clientData[i - 2] = (Integer) data.get(i);
        }
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        Long msb = nbt.getLong("entityUUIDMost");
        Long lsb = nbt.getLong("entityUUIDLeast");
        this.toUpdate = new UUID(msb, lsb);
    }


    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        if (this.linkedEntity != null && this.linkedEntity.isAlive())
        {
            nbt.putLong("entityUUIDMost", this.linkedEntity.getUUID().getMostSignificantBits());
            nbt.putLong("entityUUIDLeast", this.linkedEntity.getUUID().getLeastSignificantBits());
        }
        return nbt;
    }

    public void addTrackedEntity(UUID uuid)
    {
        this.pulseRate = 400;
        this.lastHurttime = 0;
        Stream<Entity> eList = ((ServerLevel) this.level).getEntities();
        Optional<Entity> match = eList.filter((e) -> e.getUUID().equals(uuid)).findFirst();
        if (match.isPresent())
        {
            this.linkedEntity = match.get();
            if (this.linkedEntity instanceof EntitySpaceshipBase)
            {
                ((EntitySpaceshipBase) this.linkedEntity).addTelemetry(this);
            }
        }
//        for (Entity e : eList)
//        {
//            if (e.getUniqueID().equals(uuid))
//            {
//                this.linkedEntity = e;
//                if (e instanceof EntitySpaceshipBase)
//                {
//                    ((EntitySpaceshipBase) e).addTelemetry(this);
//                }
//                return;
//            }
//        }
        //TODO Add some kind of watcher to add the entity when next loaded
        this.linkedEntity = null;
    }

    public void addTrackedEntity(Entity e)
    {
        this.pulseRate = 400;
        this.lastHurttime = 0;
        this.linkedEntity = e;
        if (e instanceof EntitySpaceshipBase)
        {
            ((EntitySpaceshipBase) e).addTelemetry(this);
        }
    }

    public void removeTrackedEntity()
    {
        this.pulseRate = 400;
        this.linkedEntity = null;
    }

    public static TileEntityTelemetry getNearest(BlockEntity te)
    {
        if (te == null)
        {
            return null;
        }
        BlockVec3 target = new BlockVec3(te);

        int distSq = 1025;
        BlockVec3Dim nearest = null;
        DimensionType dim = GCCoreUtil.getDimensionType(te.getLevel());
        for (BlockVec3Dim telemeter : loadedList)
        {
            if (telemeter.dim != dim)
            {
                continue;
            }
            int dist = telemeter.distanceSquared(target);
            if (dist < distSq)
            {
                distSq = dist;
                nearest = telemeter;
            }
        }

        if (nearest == null)
        {
            return null;
        }
        BlockEntity result = te.getLevel().getBlockEntity(new BlockPos(nearest.x, nearest.y, nearest.z));
        if (result instanceof TileEntityTelemetry)
        {
            return (TileEntityTelemetry) result;
        }
        return null;
    }

    /**
     * Call this when a player wears a frequency module to check
     * whether it has been linked with a Telemetry Unit.
     *
     * @param held   The frequency module
     * @param player
     */
    public static void frequencyModulePlayer(ItemStack held, ServerPlayer player, boolean remove)
    {
        if (held == null)
        {
            return;
        }
        CompoundTag fmData = held.getTag();
        if (fmData != null && fmData.contains("teDim"))
        {
            int dim = fmData.getInt("teDim");
            int x = fmData.getInt("teCoordX");
            int y = fmData.getInt("teCoordY");
            int z = fmData.getInt("teCoordZ");
            Dimension wp = WorldUtil.getProviderForDimensionServer(DimensionType.getById(dim));
            //TODO
            if (wp == null || wp.getWorld() == null)
            {
                GCLog.debug("Frequency module worn: world dimension is null.  This is a bug. " + dim);
            }
            else
            {
                BlockEntity te = wp.getWorld().getBlockEntity(new BlockPos(x, y, z));
                if (te instanceof TileEntityTelemetry)
                {
                    if (remove)
                    {
                        if (((TileEntityTelemetry) te).linkedEntity == player)
                        {
                            ((TileEntityTelemetry) te).removeTrackedEntity();
                        }
                    }
                    else
                    {
                        ((TileEntityTelemetry) te).addTrackedEntity(player.getUUID());
                    }
                }
            }
        }
    }

    public static void updateLinkedPlayer(ServerPlayer playerOld, ServerPlayer playerNew)
    {
        for (BlockVec3Dim telemeter : loadedList)
        {
            BlockEntity te = telemeter.getTileEntityNoLoad();
            if (te instanceof TileEntityTelemetry)
            {
                if (((TileEntityTelemetry) te).linkedEntity == playerOld)
                {
                    ((TileEntityTelemetry) te).linkedEntity = playerNew;
                }
            }
        }
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }
}
