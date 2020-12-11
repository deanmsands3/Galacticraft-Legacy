package team.galacticraft.galacticraft.common.core.proxy;

import team.galacticraft.galacticraft.common.api.GalacticraftRegistry;
import team.galacticraft.galacticraft.common.api.item.EnumExtendedInventorySlot;
import team.galacticraft.galacticraft.common.api.vector.Vector3;
import team.galacticraft.galacticraft.common.core.entities.player.GCPlayerHandler;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
import team.galacticraft.galacticraft.common.core.entities.player.IPlayerServer;
import team.galacticraft.galacticraft.common.core.entities.player.PlayerServer;
import team.galacticraft.galacticraft.common.core.fluid.FluidNetwork;
import team.galacticraft.galacticraft.common.core.tick.TickHandlerServer;
import team.galacticraft.galacticraft.common.core.util.GCCoreUtil;
import team.galacticraft.galacticraft.common.core.util.WorldUtil;
import team.galacticraft.galacticraft.common.core.wrappers.PartialCanister;
import team.galacticraft.galacticraft.common.core.wrappers.PlayerGearData;
import net.minecraft.network.PacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.Fluid;

public class CommonProxyCore
{
    public IPlayerServer player = new PlayerServer();

    public void registerVariants()
    {

    }

    public void registerFluidTexture(Fluid fluid, ResourceLocation submergedTexture)
    {
    }

    public Level getClientWorld()
    {
        return null;
    }

//    public void addParticle(String particleID, Vector3 position, Vector3 motion, Object[] otherInfo)
//    {
//    }

    public Level getWorldForID(DimensionType dimensionID)
    {
        return WorldUtil.getWorldForDimensionServer(dimensionID);
    }

    public Player getPlayerFromNetHandler(PacketListener handler)
    {
        if (handler instanceof ServerGamePacketListenerImpl)
        {
            return ((ServerGamePacketListenerImpl) handler).player;
        }
        else
        {
            return null;
        }
    }

    public void postRegisterItem(Item item)
    {
    }

    public void unregisterNetwork(FluidNetwork fluidNetwork)
    {
        if (GCCoreUtil.getEffectiveSide().isServer())
        {
            TickHandlerServer.removeFluidNetwork(fluidNetwork);
        }
    }

    public void registerNetwork(FluidNetwork fluidNetwork)
    {
        if (GCCoreUtil.getEffectiveSide().isServer())
        {
            TickHandlerServer.addFluidNetwork(fluidNetwork);
        }
    }

    public boolean isPaused()
    {
        return false;
    }

    public PlayerGearData getGearData(Player player)
    {
        GCPlayerStats stats = GCPlayerStats.get(player);

        int mask = stats.getMaskInSlot() == null ? GCPlayerHandler.GEAR_NOT_PRESENT : GalacticraftRegistry.findMatchingGearID(stats.getMaskInSlot(), EnumExtendedInventorySlot.MASK);
        int gear = stats.getGearInSlot() == null ? GCPlayerHandler.GEAR_NOT_PRESENT : GalacticraftRegistry.findMatchingGearID(stats.getGearInSlot(), EnumExtendedInventorySlot.GEAR);
        int leftTank = stats.getTankInSlot1() == null ? GCPlayerHandler.GEAR_NOT_PRESENT : GalacticraftRegistry.findMatchingGearID(stats.getTankInSlot1(), EnumExtendedInventorySlot.LEFT_TANK);
        int rightTank = stats.getTankInSlot2() == null ? GCPlayerHandler.GEAR_NOT_PRESENT : GalacticraftRegistry.findMatchingGearID(stats.getTankInSlot2(), EnumExtendedInventorySlot.RIGHT_TANK);
        int frequencyModule = stats.getFrequencyModuleInSlot() == null ? GCPlayerHandler.GEAR_NOT_PRESENT : GalacticraftRegistry.findMatchingGearID(stats.getFrequencyModuleInSlot(), EnumExtendedInventorySlot.FREQUENCY_MODULE);
        int[] thermalPadding = new int[4];
        thermalPadding[0] = stats.getThermalHelmetInSlot() == null ? GCPlayerHandler.GEAR_NOT_PRESENT : GalacticraftRegistry.findMatchingGearID(stats.getThermalHelmetInSlot(), EnumExtendedInventorySlot.THERMAL_HELMET);
        thermalPadding[1] = stats.getThermalChestplateInSlot() == null ? GCPlayerHandler.GEAR_NOT_PRESENT : GalacticraftRegistry.findMatchingGearID(stats.getThermalChestplateInSlot(), EnumExtendedInventorySlot.THERMAL_CHESTPLATE);
        thermalPadding[2] = stats.getThermalLeggingsInSlot() == null ? GCPlayerHandler.GEAR_NOT_PRESENT : GalacticraftRegistry.findMatchingGearID(stats.getThermalLeggingsInSlot(), EnumExtendedInventorySlot.THERMAL_LEGGINGS);
        thermalPadding[3] = stats.getThermalBootsInSlot() == null ? GCPlayerHandler.GEAR_NOT_PRESENT : GalacticraftRegistry.findMatchingGearID(stats.getThermalBootsInSlot(), EnumExtendedInventorySlot.THERMAL_BOOTS);
        //TODO: Parachute
        return new PlayerGearData(player, mask, gear, leftTank, rightTank, frequencyModule, thermalPadding);
    }
}
