package team.galacticraft.galacticraft.common.core.entities;

import team.galacticraft.galacticraft.common.api.recipe.SchematicRegistry;
import team.galacticraft.galacticraft.core.network.PacketSimple;
import team.galacticraft.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityHangingSchematic extends HangingEntity
{
    public int schematic;
    private boolean sendToClient;
    private int tickCounter1;

    protected EntityHangingSchematic(EntityType<? extends EntityHangingSchematic> type, Level world)
    {
        super(type, world);
    }

    public EntityHangingSchematic(EntityType<? extends EntityHangingSchematic> type, Level world, BlockPos pos, Direction facing, int schematicType)
    {
        super(type, world);
        this.schematic = schematicType;
        this.setDirection(facing);
    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick()
    {
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        if (this.sendToClient)
        {
            this.sendToClient = false;
            this.sendToClient(this.level, this.pos);
        }

        if (this.tickCounter1++ == 10)
        {
            this.tickCounter1 = 0;

            if (!this.level.isClientSide && this.isAlive() && !this.survives())
            {
                this.remove();
                this.dropItem(null);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tagCompound)
    {
        tagCompound.putInt("schem", this.schematic);
        super.addAdditionalSaveData(tagCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag)
    {
        this.schematic = tag.getInt("schem");
        super.readAdditionalSaveData(tag);
        this.setSendToClient();
    }

    @Override
    public int getWidth()
    {
        return 32;
    }

    @Override
    public int getHeight()
    {
        return 32;
    }

    @Override
    public void dropItem(Entity brokenEntity)
    {
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS))
        {
            if (brokenEntity instanceof Player)
            {
                Player entityplayer = (Player) brokenEntity;

                if (entityplayer.abilities.instabuild)
                {
                    return;
                }
            }

            this.spawnAtLocation(SchematicRegistry.getSchematicItem(this.schematic), 0.0F);
        }
    }

    @Override
    public ItemStack getPickedResult(HitResult target)
    {
        return SchematicRegistry.getSchematicItem(this.schematic);
    }

    @Override
    public void moveTo(double x, double y, double z, float yaw, float pitch)
    {
        BlockPos blockpos = this.pos.offset(x - this.getX(), y - this.getY(), z - this.getZ());
        this.setPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
    }

    public void sendToClient(Level worldIn, BlockPos blockpos)
    {
        DimensionType dimID = GCCoreUtil.getDimensionType(worldIn);
        GCCoreUtil.sendToAllAround(new PacketSimple(EnumSimplePacket.C_SPAWN_HANGING_SCHEMATIC, dimID, new Object[]{blockpos, this.getId(), this.direction.ordinal(), this.schematic}), worldIn, dimID, blockpos, 150D);
    }

    public void setSendToClient()
    {
        this.sendToClient = true;
    }

    @Override
    public void playPlacementSound()
    {
        this.playSound(SoundEvents.PAINTING_PLACE, 1.0F, 1.0F);
    }
}
