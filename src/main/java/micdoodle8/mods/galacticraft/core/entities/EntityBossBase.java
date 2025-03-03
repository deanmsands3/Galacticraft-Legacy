package micdoodle8.mods.galacticraft.core.entities;

import java.util.List;
import java.util.Random;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public abstract class EntityBossBase extends EntityMob implements IBoss
{

    protected TileEntityDungeonSpawner<?> spawner;
    public int deathTicks = 0;

    public int entitiesWithin;
    public int entitiesWithinLast;

    private final BossInfoServer bossInfo = (BossInfoServer) (new BossInfoServer(this.getDisplayName(), getHealthBarColor(), BossInfo.Overlay.PROGRESS));

    public EntityBossBase(World world)
    {
        super(world);
    }

    public abstract int getChestTier();

    public abstract ItemStack getGuaranteedLoot(Random rand);

    public abstract void dropKey();

    public abstract BossInfo.Color getHealthBarColor();

    @Override
    protected void updateAITasks()
    {
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        super.updateAITasks();
    }

    @Override
    protected void onDeathUpdate()
    {
        ++this.deathTicks;

        if (this.deathTicks >= 180 && this.deathTicks <= 200)
        {
            final float x = (this.rand.nextFloat() - 0.5F) * this.width;
            final float y = (this.rand.nextFloat() - 0.5F) * (this.height / 2.0F);
            final float z = (this.rand.nextFloat() - 0.5F) * this.width;
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + x, this.posY + 2.0D + y, this.posZ + z, 0.0D, 0.0D, 0.0D);
        }

        int i;
        int j;

        if (!this.world.isRemote)
        {
            if (this.deathTicks >= 180 && this.deathTicks % 5 == 0)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(PacketSimple.EnumSimplePacket.C_PLAY_SOUND_EXPLODE, GCCoreUtil.getDimensionID(this.world), new Object[]
                {}), new NetworkRegistry.TargetPoint(GCCoreUtil.getDimensionID(this.world), this.posX, this.posY, this.posZ, 40.0D));
            }

            if (this.deathTicks > 150 && this.deathTicks % 5 == 0)
            {
                i = 30;

                while (i > 0)
                {
                    j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
                }
            }
        }

        if (this.deathTicks == 200 && !this.world.isRemote)
        {
            i = 20;

            while (i > 0)
            {
                j = EntityXPOrb.getXPSplit(i);
                i -= j;
                this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
            }

            TileEntityTreasureChest chest = null;

            if (this.spawner != null && this.spawner.getChestPos() != null)
            {
                TileEntity chestTest = this.world.getTileEntity(this.spawner.getChestPos());

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
                double dist = this.getDistanceSq(chest.getPos().getX() + 0.5, chest.getPos().getY() + 0.5, chest.getPos().getZ() + 0.5);
                if (dist < 1000 * 1000)
                {
                    if (!chest.locked)
                    {
                        chest.locked = true;
                    }

                    for (int k = 0; k < chest.getSizeInventory(); k++)
                    {
                        chest.setInventorySlotContents(k, ItemStack.EMPTY);
                    }

                    chest.fillWithLoot(null);

//                    ChestGenHooks info = ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST);
//
//                    // Generate twice, since it's an extra special chest
//                    WeightedRandomChestContent.generateChestContents(this.rand, info.getItems(this.rand), chest, info.getCount(this.rand));
//                    WeightedRandomChestContent.generateChestContents(this.rand, info.getItems(this.rand), chest, info.getCount(this.rand));

                    ItemStack schematic = this.getGuaranteedLoot(this.rand);
                    int slot = this.rand.nextInt(chest.getSizeInventory());
                    chest.setInventorySlotContents(slot, schematic);
                }
            }

            this.dropKey();

            super.setDead();

            if (this.spawner != null)
            {
                // Note: spawner.isBossDefeated is true, so it's properly dead
                this.spawner.isBossDefeated = true;
                this.spawner.boss = null;
                this.spawner.spawned = false;

                if (!this.world.isRemote)
                {
                    this.spawner.lastKillTime = MinecraftServer.getCurrentTimeMillis();
                }
            }
        }
    }

    @Override
    public void onLivingUpdate()
    {
        if (this.world.isRemote)
        {
            this.setHealth(this.getHealth());
        }

        if (this.spawner != null)
        {
            List<EntityPlayer> playersWithin = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.spawner.getRangeBounds());

            this.entitiesWithin = playersWithin.size();

            if (this.entitiesWithin == 0 && this.entitiesWithinLast != 0)
            {
                List<EntityPlayer> entitiesWithin2 = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.spawner.getRangeBoundsPlus11());

                for (EntityPlayer p : entitiesWithin2)
                {
                    p.sendMessage(new TextComponentString(GCCoreUtil.translate("gui.skeleton_boss.message")));
                }

                this.setDead();
                // Note: spawner.isBossDefeated is false, so the boss will
                // respawn if any player comes back inside the room

                return;
            }

            this.entitiesWithinLast = this.entitiesWithin;
        }

        super.onLivingUpdate();
    }

    @Override
    public void setDead()
    {
        if (this.spawner != null)
        {
            this.spawner.isBossDefeated = false;
            this.spawner.boss = null;
            this.spawner.spawned = false;
        }

        super.setDead();
    }

    @Override
    public void onBossSpawned(TileEntityDungeonSpawner spawner)
    {
        this.spawner = spawner;
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player)
    {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player)
    {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }
}
