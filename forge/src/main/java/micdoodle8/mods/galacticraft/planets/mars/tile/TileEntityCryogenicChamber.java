package micdoodle8.mods.galacticraft.planets.mars.tile;

import com.mojang.datafixers.util.Either;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti.EnumBlockMultiType;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFake;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlockNames;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

import java.util.LinkedList;
import java.util.List;

public class TileEntityCryogenicChamber extends TileEntityFake implements IMultiBlock
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsBlockNames.CRYOGENIC_CHAMBER)
    public static BlockEntityType<TileEntityCryogenicChamber> TYPE;

    public boolean isOccupied;
    private boolean initialised;

    public TileEntityCryogenicChamber()
    {
        super(TYPE);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox()
    {
        return new AABB(getBlockPos().getX() - 1, getBlockPos().getY(), getBlockPos().getZ() - 1, getBlockPos().getX() + 2, getBlockPos().getY() + 3, getBlockPos().getZ() + 2);
    }

    @Override
    public InteractionResult onActivated(Player entityPlayer)
    {
        if (this.level.isClientSide)
        {
            return ActionResultType.PASS;
        }

        Either<Player.BedSleepingProblem, Unit> enumstatus = this.sleepInBedAt(entityPlayer, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ());

        enumstatus.ifLeft((result) ->
        {
            ((ServerPlayer) entityPlayer).connection.teleport(entityPlayer.getX(), entityPlayer.getY(), entityPlayer.getZ(), entityPlayer.yRot, entityPlayer.xRot);
            GalacticraftCore.packetPipeline.sendTo(new PacketSimpleMars(EnumSimplePacketMars.C_BEGIN_CRYOGENIC_SLEEP, GCCoreUtil.getDimensionType(entityPlayer.level), new Object[]{this.getBlockPos()}), (ServerPlayer) entityPlayer);
        });

        enumstatus.ifRight((result) ->
        {
            GCPlayerStats stats = GCPlayerStats.get(entityPlayer);
            entityPlayer.sendMessage(new TextComponent(GCCoreUtil.translateWithFormat("gui.cryogenic.chat.cant_use", stats.getCryogenicChamberCooldown() / 20)));
        });

        return enumstatus.left().isPresent() ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    public Either<Player.BedSleepingProblem, Unit> sleepInBedAt(Player entityPlayer, int par1, int par2, int par3)
    {
        if (!this.level.isClientSide)
        {
            if (entityPlayer.isSleeping() || !entityPlayer.isAlive())
            {
                return Either.left(PlayerEntity.SleepResult.OTHER_PROBLEM);
            }

            if (this.level.getBiome(new BlockPos(par1, par2, par3)) == Biomes.NETHER)
            {
                return Either.left(PlayerEntity.SleepResult.NOT_POSSIBLE_HERE);
            }

            GCPlayerStats stats = GCPlayerStats.get(entityPlayer);
            if (stats.getCryogenicChamberCooldown() > 0)
            {
                return Either.left(PlayerEntity.SleepResult.NOT_POSSIBLE_NOW);
            }
        }

        if (entityPlayer.getVehicle() != null)
        {
            entityPlayer.stopRiding();
        }

        entityPlayer.setPos(this.getBlockPos().getX() + 0.5F, this.getBlockPos().getY() + 1.9F, this.getBlockPos().getZ() + 0.5F);

        entityPlayer.startSleeping(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()));
//        entityPlayer.sleeping = true;
        entityPlayer.sleepCounter = 0;
//        entityPlayer.bedLocation = new BlockPos(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
        entityPlayer.setDeltaMovement(0.0, 0.0, 0.0);

        if (!this.level.isClientSide)
        {
            ((ServerLevel) this.level).updateSleepingPlayerList();
        }

        return Either.right(Unit.INSTANCE);
    }

//    @Override
//    public boolean canUpdate()
//    {
//        return true;
//    }

    @Override
    public void tick()
    {
        if (!this.initialised)
        {
            this.initialised = this.initialiseMultiTiles(this.getBlockPos(), this.level);
        }
    }

    @Override
    public void onCreate(Level world, BlockPos placedPosition)
    {
        this.mainBlockPosition = placedPosition;
        this.setChanged();

        List<BlockPos> positions = new LinkedList<>();
        this.getPositions(placedPosition, positions);
        ((BlockMulti) GCBlocks.MULTI_BLOCK).makeFakeBlock(world, positions, placedPosition, this.getMultiType());
    }

    @Override
    public BlockMulti.EnumBlockMultiType getMultiType()
    {
        return EnumBlockMultiType.CRYO_CHAMBER;
    }

    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int buildHeight = this.level.getMaxBuildHeight() - 1;

        for (int y = 1; y < 3; y++)
        {
            if (placedPosition.getY() + y > buildHeight)
            {
                return;
            }
            positions.add(new BlockPos(placedPosition.getX(), placedPosition.getY() + y, placedPosition.getZ()));
        }
    }

    @Override
    public void onDestroy(BlockEntity callingBlock)
    {
        final BlockPos thisBlock = getBlockPos();
        List<BlockPos> positions = new LinkedList<>();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            BlockState stateAt = this.level.getBlockState(pos);

            if (stateAt.getBlock() == GCBlocks.MULTI_BLOCK && stateAt.getValue(BlockMulti.MULTI_TYPE) == EnumBlockMultiType.CRYO_CHAMBER)
            {
                if (this.level.isClientSide && this.level.random.nextDouble() < 0.1D)
                {
                    Minecraft.getInstance().particleEngine.destroy(pos, this.level.getBlockState(pos));
                }
                this.level.destroyBlock(pos, false);
            }
        }
        this.level.destroyBlock(thisBlock, true);
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.isOccupied = nbt.getBoolean("IsChamberOccupied");
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putBoolean("IsChamberOccupied", this.isOccupied);
        return nbt;
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        return this.save(new CompoundTag());
    }
}
