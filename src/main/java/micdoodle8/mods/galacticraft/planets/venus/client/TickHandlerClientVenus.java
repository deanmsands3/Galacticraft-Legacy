package micdoodle8.mods.galacticraft.planets.venus.client;

import com.google.common.collect.Maps;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.ConfigManagerPlanets;
import micdoodle8.mods.galacticraft.planets.venus.dimension.DimensionVenus;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Iterator;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class TickHandlerClientVenus
{
    private final Map<BlockPos, Integer> lightning = Maps.newHashMap();

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event)
    {
        final Minecraft minecraft = Minecraft.getInstance();
        final LocalPlayer player = minecraft.player;
        final LocalPlayer playerBaseClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);

//        if (event.phase == TickEvent.Phase.END)
//        {
//        }
    }

    @SubscribeEvent
    public void renderLightning(ClientProxyCore.EventSpecialRender event)
    {
        final Minecraft minecraft = Minecraft.getInstance();
        final LocalPlayer player = minecraft.player;
        if (player != null && !ConfigManagerPlanets.disableAmbientLightning.get())
        {
            Iterator<Map.Entry<BlockPos, Integer>> it = lightning.entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry<BlockPos, Integer> entry = it.next();
                long seed = entry.getValue() / 10 + entry.getKey().getX() + entry.getKey().getZ();
                FakeLightningBoltRenderer.renderBolt(seed, entry.getKey().getX() - ClientProxyCore.playerPosX, entry.getKey().getY() - ClientProxyCore.playerPosY, entry.getKey().getZ() - ClientProxyCore.playerPosZ);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        final Minecraft minecraft = Minecraft.getInstance();

        final ClientLevel world = minecraft.level;

        if (world != null)
        {
            if (world.getDimension() instanceof DimensionVenus)
            {
//                if (world.getDimension().getSkyRenderer() == null)
//                {
//                    world.getDimension().setSkyRenderer(new SkyProviderVenus((IGalacticraftDimension) world.getDimension()));
//                } TODO Sky renderers

                if (world.getDimension().getCloudRenderer() == null)
                {
                    world.getDimension().setCloudRenderer(new CloudRenderer());
                }

//                if (world.getDimension().getWeatherRenderer() == null)
//                {
//                    world.getDimension().setWeatherRenderer(new WeatherRendererVenus());
//                } TODO Weather renderers
            }
        }
    }

    @Environment(EnvType.CLIENT)
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        final Minecraft minecraft = Minecraft.getInstance();
        final LocalPlayer player = minecraft.player;

        if (player == event.player)
        {
            if (!ConfigManagerPlanets.disableAmbientLightning.get())
            {
                Iterator<Map.Entry<BlockPos, Integer>> it = lightning.entrySet().iterator();
                while (it.hasNext())
                {
                    Map.Entry<BlockPos, Integer> entry = it.next();
                    int val = entry.getValue();
                    if (val - 1 <= 0)
                    {
                        it.remove();
                    }
                    else
                    {
                        entry.setValue(val - 1);
                    }
                }

                if (player.getRandom().nextInt(300 + (int) (800F * minecraft.level.rainLevel)) == 0 && minecraft.level.getDimension() instanceof DimensionVenus)
                {
                    double freq = player.getRandom().nextDouble() * Math.PI * 2.0F;
                    double dist = 180.0F;
                    double dX = dist * Math.cos(freq);
                    double dZ = dist * Math.sin(freq);
                    double posX = player.getX() + dX;
                    double posY = 70;
                    double posZ = player.getZ() + dZ;
                    minecraft.level.playSound(player, posX, posY, posZ, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 500.0F + player.getRandom().nextFloat() * 500F, 1.0F + player.getRandom().nextFloat() * 0.2F);
                    lightning.put(new BlockPos(posX, posY, posZ), 20);
                }
            }
        }
    }
}
