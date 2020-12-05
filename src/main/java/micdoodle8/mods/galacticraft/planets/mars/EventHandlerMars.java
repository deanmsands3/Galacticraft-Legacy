package micdoodle8.mods.galacticraft.planets.mars;

import micdoodle8.mods.galacticraft.api.event.wgen.GCCoreEventPopulate;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC.OrientCameraEvent;
import micdoodle8.mods.galacticraft.core.event.EventLandingPadRemoval;
import micdoodle8.mods.galacticraft.core.event.EventWakePlayer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFake;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockCryoChamber;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.dimension.DimensionMars;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityCryogenicChamber;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.WorldGenEggs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class EventHandlerMars
{
    private Feature<NoneFeatureConfiguration> eggGenerator;

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event)
    {
        if (event.getSource().name.equals("slimeling") && event.getSource() instanceof EntityDamageSource)
        {
            EntityDamageSource source = (EntityDamageSource) event.getSource();

            if (source.getEntity() instanceof EntitySlimeling && !source.getEntity().level.isClientSide)
            {
                ((EntitySlimeling) source.getEntity()).kills++;
            }
        }
    }

    @SubscribeEvent
    public void onLivingAttacked(LivingAttackEvent event)
    {
        if (!event.getEntity().isInvulnerableTo(event.getSource()) && !event.getEntity().world.isClient && event.getEntityLiving().getHealth() <= 0.0F && !(event.getSource().isFire() && event.getEntityLiving().hasStatusEffect(MobEffects.FIRE_RESISTANCE)))
        {
            Entity entity = event.getSource().getAttacker();

            if (entity instanceof EntitySlimeling)
            {
                EntitySlimeling entitywolf = (EntitySlimeling) entity;

                if (entitywolf.isTame())
                {
//                    event.entityLiving.recentlyHit = 100;
//                    event.entityLiving.attackingPlayer = null; TODO
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerWakeUp(EventWakePlayer event)
    {
        Player player = event.getPlayer();
        BlockPos c = player.getBedLocation(player.dimension);
        BlockState state = player.getCommandSenderWorld().getBlockState(c);
        Block blockID = state.getBlock();

        if (blockID == MarsBlocks.cryoChamber)
        {
            if (!event.immediately && event.updateWorld && event.setSpawn)
            {
                event.result = Player.BedSleepingProblem.NOT_POSSIBLE_HERE;
            }
            else if (!event.immediately && !event.updateWorld && event.setSpawn)
            {
                if (!player.level.isClientSide)
                {
                    player.heal(5.0F);
                    GCPlayerStats.get(player).setCryogenicChamberCooldown(6000);

                    ServerLevel ws = (ServerLevel) player.level;
                    ws.updateSleepingPlayerList();
//                    if (ws.areAllPlayersAsleep() && ws.getGameRules().getBoolean("doDaylightCycle"))
//                    {
//                        WorldUtil.setNextMorning(ws);
//                    } TODO Needed in 1.14+?
                }
            }
        }
    }

//    @OnlyIn(Dist.CLIENT)
//    @SubscribeEvent
//    public void onPlayerRotate(RenderPlayerGC.RotatePlayerEvent event)
//    {
//        BlockPos blockPos = event.getEntityPlayer().getBedLocation(event.getPlayer().dimension);
//        if (blockPos != null)
//        {
//            BlockState state = event.getEntityPlayer().world.getBlockState(blockPos);
//            if (state.getBlock() == GCBlocks.fakeBlock && state.get(BlockMulti.MULTI_TYPE) == BlockMulti.EnumBlockMultiType.CRYO_CHAMBER)
//            {
//                TileEntity tile = event.getEntityPlayer().world.getTileEntity(blockPos);
//                if (tile instanceof TileEntityFake)
//                {
//                    state = event.getEntityPlayer().world.getBlockState(((TileEntityFake) tile).mainBlockPosition);
//                }
//            }
//
//            if (state.getBlock() == MarsBlocks.cryoChamber)
//            {
//                event.shouldRotate = true;
//                event.vanillaOverride = true;
//            }
//        }
//    } TODO Player rotation

    @SubscribeEvent
    public void onPlanetDecorated(GCCoreEventPopulate.Post event)
    {
        if (this.eggGenerator == null)
        {
            this.eggGenerator = new WorldGenEggs(NoneFeatureConfiguration::deserialize);
        }

        if (event.world.getDimension() instanceof DimensionMars)
        {
            int eggsPerChunk = 2;
            BlockPos blockpos;

            for (int eggCount = 0; eggCount < eggsPerChunk; ++eggCount)
            {
                blockpos = event.pos.offset(event.rand.nextInt(16) + 8, event.rand.nextInt(104) + 24, event.rand.nextInt(16) + 8);
                this.eggGenerator.place(event.world, ((ServerChunkCache) event.world.getChunkSource()).getGenerator(), event.rand, blockpos, new NoneFeatureConfiguration());
            }
        }
    }

    @Environment(EnvType.CLIENT)
    @SubscribeEvent
    public void orientCamera(OrientCameraEvent event)
    {
        Player entity = Minecraft.getInstance().player;

        if (entity != null)
        {
            int x = Mth.floor(entity.getX());
            int y = Mth.floor(entity.getY());
            int z = Mth.floor(entity.getZ());
            BlockEntity tile = Minecraft.getInstance().level.getBlockEntity(new BlockPos(x, y - 1, z));

            if (tile instanceof TileEntityFake)
            {
                tile = ((TileEntityFake) tile).getMainBlockTile();
            }

            if (tile instanceof TileEntityCryogenicChamber)
            {
                GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);

                switch (tile.getBlockState().getValue(BlockCryoChamber.FACING))
                {
                case SOUTH:
                    GL11.glTranslatef(-0.4F, -0.5F, 4.1F);
                    break;
                case WEST:
                    GL11.glTranslatef(0, -0.5F, 4.1F);
                    break;
                case NORTH:
                    GL11.glTranslatef(0, -0.5F, 4.1F);
                    break;
                case EAST:
                    GL11.glTranslatef(0.0F, -0.5F, 4.1F);
                    break;
                }

                GL11.glRotatef(-180, 0.0F, 1.0F, 0.0F);

                GL11.glRotatef(Minecraft.getInstance().player.getSleepTimer() - 50, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.0F, 0.3F, 0.0F);
            }
        }
    }

    @SubscribeEvent
    public void onLandingPadRemoved(EventLandingPadRemoval event)
    {
        BlockEntity tile = event.world.getBlockEntity(event.pos);

        if (tile instanceof IFuelDock)
        {
            IFuelDock dock = (IFuelDock) tile;

            for (ILandingPadAttachable connectedTile : dock.getConnectedTiles())
            {
                if (connectedTile instanceof TileEntityLaunchController)
                {
                    final TileEntityLaunchController launchController = (TileEntityLaunchController) connectedTile;
                    if (launchController.getEnergyStoredGC() > 0.0F && launchController.launchPadRemovalDisabled && !launchController.getDisabled(0))
                    {
                        event.allow = false;
                        return;
                    }
                    break;
                }
            }
        }
    }
}
