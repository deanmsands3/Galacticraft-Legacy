package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;
import java.util.Random;

public abstract class EntityBossBase extends Monster implements IBoss
{
    protected TileEntityDungeonSpawner<?> spawner;
    public int deathTicks = 0;

    public int entitiesWithin;
    public int entitiesWithinLast;

    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), getHealthBarColor(), BossInfo.Overlay.PROGRESS);

    public EntityBossBase(EntityType<? extends EntityBossBase> type, Level worldIn)
    {
        super(type, worldIn);
    }

    public abstract int getChestTier();

    public abstract ItemStack getGuaranteedLoot(Random rand);

    public abstract void dropKey();

    public abstract BossEvent.BossBarColor getHealthBarColor();

    @Override
    protected void customServerAiStep()
    {
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        super.customServerAiStep();
    }

    @Override
    protected void tickDeath()
    {
        ++this.deathTicks;

        if (this.deathTicks >= 180 && this.deathTicks <= 200)
        {
            final float x = (this.random.nextFloat() - 0.5F) * this.getBbWidth();
            final float y = (this.random.nextFloat() - 0.5F) * (this.getBbHeight() / 2.0F);
            final float z = (this.random.nextFloat() - 0.5F) * this.getBbWidth();
//            this.world.addParticle(EnumParticleTypes.EXPLOSION_HUGE, this.getPosX() + x, this.getPosY() + 2.0D + y, this.getPosZ() + z, 0.0D, 0.0D, 0.0D); TODO Particles
        }

        int i;
        int j;

        if (!this.level.isClientSide)
        {
            if (this.deathTicks >= 180 && this.deathTicks % 5 == 0)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(PacketSimple.EnumSimplePacket.C_PLAY_SOUND_EXPLODE, GCCoreUtil.getDimensionType(this.level), new Object[]{}), new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 40.0D, GCCoreUtil.getDimensionType(this.level)));
            }

            if (this.deathTicks > 150 && this.deathTicks % 5 == 0)
            {
                i = 30;

                while (i > 0)
                {
                    j = ExperienceOrb.getExperienceValue(i);
                    i -= j;
                    this.level.addFreshEntity(new ExperienceOrb(this.level, this.getX(), this.getY(), this.getZ(), j));
                }
            }
        }

        if (this.deathTicks == 200 && !this.level.isClientSide)
        {
            i = 20;

            while (i > 0)
            {
                j = ExperienceOrb.getExperienceValue(i);
                i -= j;
                this.level.addFreshEntity(new ExperienceOrb(this.level, this.getX(), this.getY(), this.getZ(), j));
            }

            TileEntityTreasureChest chest = null;

            if (this.spawner != null && this.spawner.getChestPos() != null)
            {
                BlockEntity chestTest = this.level.getBlockEntity(this.spawner.getChestPos());

                if (chestTest != null && chestTest instanceof TileEntityTreasureChest)
                {
                    chest = (TileEntityTreasureChest) chestTest;
                }
            }

            if (chest == null)
            {
                // Fallback to finding closest chest
                chest = TileEntityTreasureChest.findClosest(this, this.getChestTier());
            }

            if (chest != null)
            {
                double dist = this.distanceToSqr(chest.getBlockPos().getX() + 0.5, chest.getBlockPos().getY() + 0.5, chest.getBlockPos().getZ() + 0.5);
                if (dist < 1000 * 1000)
                {
                    if (!chest.locked)
                    {
                        chest.locked = true;
                    }

                    for (int k = 0; k < chest.getContainerSize(); k++)
                    {
                        chest.setItem(k, ItemStack.EMPTY);
                    }

                    chest.fillWithLoot(null);

//                    ChestGenHooks info = ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST);
//
//                    // Generate twice, since it's an extra special chest
//                    WeightedRandomChestContent.generateChestContents(this.rand, info.getItems(this.rand), chest, info.getCount(this.rand));
//                    WeightedRandomChestContent.generateChestContents(this.rand, info.getItems(this.rand), chest, info.getCount(this.rand));

                    ItemStack schematic = this.getGuaranteedLoot(this.random);
                    int slot = this.random.nextInt(chest.getContainerSize());
                    chest.setItem(slot, schematic);
                }
            }

            this.dropKey();

            super.remove();

            if (this.spawner != null)
            {
                //Note: spawner.isBossDefeated is true, so it's properly dead
                this.spawner.isBossDefeated = true;
                this.spawner.boss = null;
                this.spawner.spawned = false;

                if (!this.level.isClientSide)
                {
                    this.spawner.lastKillTime = ((ServerLevel) this.level).getServer().getNextTickTime();
                }
            }
        }
    }

    @Override
    public void tick()
    {
        if (this.level.isClientSide)
        {
            this.setHealth(this.getHealth());
        }

        if (this.spawner != null)
        {
            List<Player> playersWithin = this.level.getEntitiesOfClass(Player.class, this.spawner.getRangeBounds());

            this.entitiesWithin = playersWithin.size();

            if (this.entitiesWithin == 0 && this.entitiesWithinLast != 0)
            {
                List<Player> entitiesWithin2 = this.level.getEntitiesOfClass(Player.class, this.spawner.getRangeBoundsPlus11());

                for (Player p : entitiesWithin2)
                {
                    p.sendMessage(new TextComponent(GCCoreUtil.translate("gui.skeleton_boss.message")));
                }

                this.remove();
                //Note: spawner.isBossDefeated is false, so the boss will respawn if any player comes back inside the room

                return;
            }

            this.entitiesWithinLast = this.entitiesWithin;
        }

        super.tick();
    }

    @Override
    public void remove(boolean keepData)
    {
        if (this.spawner != null)
        {
            this.spawner.isBossDefeated = false;
            this.spawner.boss = null;
            this.spawner.spawned = false;
        }

        super.remove(keepData);
    }

    @Override
    public void onBossSpawned(TileEntityDungeonSpawner spawner)
    {
        this.spawner = spawner;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player)
    {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player)
    {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }
}
