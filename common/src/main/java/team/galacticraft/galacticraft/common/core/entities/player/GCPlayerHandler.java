package team.galacticraft.galacticraft.common.core.entities.player;

import team.galacticraft.galacticraft.common.api.GalacticraftRegistry;
import team.galacticraft.galacticraft.common.api.event.oxygen.GCCoreOxygenSuffocationEvent;
import team.galacticraft.galacticraft.common.api.item.EnumExtendedInventorySlot;
import team.galacticraft.galacticraft.common.api.item.IArmorCorrosionResistant;
import team.galacticraft.galacticraft.common.api.item.IHoldableItem;
import team.galacticraft.galacticraft.common.api.item.IItemThermal;
import team.galacticraft.galacticraft.common.api.prefab.entity.EntityAutoRocket;
import team.galacticraft.galacticraft.common.api.recipe.ISchematicPage;
import team.galacticraft.galacticraft.common.api.recipe.SchematicRegistry;
import team.galacticraft.galacticraft.common.api.world.IGalacticraftDimension;
import team.galacticraft.galacticraft.core.Constants;
import team.galacticraft.galacticraft.core.GCBlocks;
import team.galacticraft.galacticraft.core.GCItems;
import team.galacticraft.galacticraft.core.GalacticraftCore;
import team.galacticraft.galacticraft.core.blocks.BlockUnlitTorch;
import team.galacticraft.galacticraft.core.dimension.SpaceRace;
import team.galacticraft.galacticraft.core.dimension.SpaceRaceManager;
import team.galacticraft.galacticraft.core.entities.*;
import team.galacticraft.galacticraft.core.items.ItemParaChute;
import team.galacticraft.galacticraft.core.network.PacketSimple;
import team.galacticraft.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import team.galacticraft.galacticraft.core.tile.TileEntityTelemetry;
import team.galacticraft.galacticraft.core.util.*;
import team.galacticraft.galacticraft.core.world.gen.dungeon.StructureDungeon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.server.permission.PermissionAPI;

import java.util.*;
import java.util.Map.Entry;

public class GCPlayerHandler
{
    private static final int OXYGENHEIGHTLIMIT = 450;
    public static final int GEAR_NOT_PRESENT = -1;

    private final List<Item> torchItemsSpace = new ArrayList<>(2);
    private final List<Item> torchItemsRegular = new ArrayList<>(2);
    private final List<ItemStack> itemChangesPre = new ArrayList<>(6);
    private final List<ItemStack> itemChangesPost = new ArrayList<>(6);
    private static final HashMap<UUID, Integer> deathTimes = new HashMap<>();

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.getPlayer() instanceof ServerPlayer)
        {
            this.onPlayerLogin((ServerPlayer) event.getPlayer());
        }
    }

//    @SubscribeEvent
//    public void onPlayerLogout(PlayerLoggedOutEvent event)
//    {
//        if (event.player instanceof EntityPlayerMP)
//        {
//            this.onPlayerLogout((EntityPlayerMP) event.player);
//        }
//    }
//
//    @SubscribeEvent
//    public void onPlayerRespawn(PlayerRespawnEvent event)
//    {
//        if (event.player instanceof EntityPlayerMP)
//        {
//            this.onPlayerRespawn((EntityPlayerMP) event.player);
//        }
//    }

    @SubscribeEvent
    public void onPlayerCloned(PlayerEvent.Clone event)
    {
        GCPlayerStats oldStats = GCPlayerStats.get(event.getOriginal());
        GCPlayerStats newStats = GCPlayerStats.get(event.getPlayer());
        newStats.copyFrom(oldStats, !event.isWasDeath() || event.getOriginal().world.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY));
        if (event.getOriginal() instanceof ServerPlayer && event.getPlayer() instanceof ServerPlayer)
        {
            TileEntityTelemetry.updateLinkedPlayer((ServerPlayer) event.getOriginal(), (ServerPlayer) event.getPlayer());
        }
        if (event.isWasDeath() && event.getOriginal() instanceof ServerPlayer)
        {
            UUID uu = event.getOriginal().getUuid();
            if (event.getOriginal().world.getDimension() instanceof IGalacticraftDimension)
            {
                Integer timeA = deathTimes.get(uu);
                int bb = ((ServerPlayer) event.getOriginal()).server.getTickCount();
                if (timeA != null && (bb - timeA) < 1500)
                {
                    String msg = EnumColor.YELLOW + I18n.get("commands.gchouston.help.1") + " " + EnumColor.WHITE + I18n.get("commands.gchouston.help.2");
                    event.getOriginal().sendMessage(new TextComponent(msg));
                }
                deathTimes.put(uu, bb);
            }
            else
            {
                deathTimes.remove(uu);
            }
        }
    }

    @SubscribeEvent
    public void onAttachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof ServerPlayer)
        {
            event.addCapability(GCCapabilities.GC_PLAYER_PROP, new CapabilityProviderStats((ServerPlayer) event.getObject()));
        }
        else if (event.getObject() instanceof Player && ((Player) event.getObject()).level.isClientSide)
        {
            this.onAttachCapabilityClient(event);
        }
    }

    @Environment(EnvType.CLIENT)
    private void onAttachCapabilityClient(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof LocalPlayer)
        {
            event.addCapability(GCCapabilities.GC_PLAYER_CLIENT_PROP, new CapabilityProviderStatsClient((LocalPlayer) event.getObject()));
        }
    }

//    @SubscribeEvent
//    public void onEntityConstructing(EntityEvent.EntityConstructing event)
//    {
//        if (event.getEntity() instanceof EntityPlayerMP && GCPlayerStats.get((EntityPlayerMP) event.getEntity()) == null)
//        {
//            GCPlayerStats.register((EntityPlayerMP) event.getEntity());
//        }
//
//        if (event.entity instanceof EntityPlayer && event.entity.world.isRemote)
//        {
//            this.onEntityConstructingClient(event);
//        }
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public void onEntityConstructingClient(EntityEvent.EntityConstructing event)
//    {
//        if (event.getEntity() instanceof EntityPlayerSP)
//        {
//            if (GCPlayerStatsClient.get((EntityPlayerSP) event.getEntity()) == null)
//            {
//                GCPlayerStatsClient.register((EntityPlayerSP) event.getEntity());
//            }
//
//            Minecraft.getInstance().gameSettings.sendSettingsToServer();
//        }
//    }

    private void onPlayerLogin(ServerPlayer player)
    {
//        GCPlayerStats oldData = this.playerStatsMap.remove(player.getPersistentID());
//        if (oldData != null)
//        {
//            oldData.saveNBTData(player.getEntityData());
//        }

        GCPlayerStats stats = GCPlayerStats.get(player);

        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_GET_CELESTIAL_BODY_LIST, GCCoreUtil.getDimensionType(player.level), new Object[]{}), player);
        int repeatCount = stats.getBuildFlags() >> 9;
        if (repeatCount < 3)
        {
            stats.setBuildFlags(stats.getBuildFlags() & 1536);
        }
//        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_STATS, GCCoreUtil.getDimensionID(player.world), stats.getMiscNetworkedStats() ), player);
//        ColorUtil.sendUpdatedColorsToPlayer(stats); TODO
    }

//    private void onPlayerLogout(EntityPlayerMP player)
//    {
//    }
//
//    private void onPlayerRespawn(EntityPlayerMP player)
//    {
//        GCPlayerStats oldData = this.playerStatsMap.remove(player.getPersistentID());
//        GCPlayerStats stats = GCPlayerStats.get(player);
//
//        if (oldData != null)
//        {
//            stats.copyFrom(oldData, false);
//        }
//
//        stats.player = new WeakReference<EntityPlayerMP>(player);
//    }

    public static void checkGear(ServerPlayer player, GCPlayerStats stats, boolean forceSend)
    {
        stats.setMaskInSlot(stats.getExtendedInventory().getItem(0));
        stats.setGearInSlot(stats.getExtendedInventory().getItem(1));
        stats.setTankInSlot1(stats.getExtendedInventory().getItem(2));
        stats.setTankInSlot2(stats.getExtendedInventory().getItem(3));
        stats.setParachuteInSlot(stats.getExtendedInventory().getItem(4));
        stats.setFrequencyModuleInSlot(stats.getExtendedInventory().getItem(5));
        stats.setThermalHelmetInSlot(stats.getExtendedInventory().getItem(6));
        stats.setThermalChestplateInSlot(stats.getExtendedInventory().getItem(7));
        stats.setThermalLeggingsInSlot(stats.getExtendedInventory().getItem(8));
        stats.setThermalBootsInSlot(stats.getExtendedInventory().getItem(9));
        stats.setShieldControllerInSlot(stats.getExtendedInventory().getItem(10));
        //

        if (stats.getFrequencyModuleInSlot() != stats.getLastFrequencyModuleInSlot() || forceSend)
        {
            if (player.server != null)
            {
                if (stats.getFrequencyModuleInSlot().isEmpty())
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.FREQUENCY_MODULE);
                    TileEntityTelemetry.frequencyModulePlayer(stats.getLastFrequencyModuleInSlot(), player, true);
                }
                else if (stats.getLastFrequencyModuleInSlot().isEmpty())
                {
                    int gearID = GalacticraftRegistry.findMatchingGearID(stats.getFrequencyModuleInSlot(), EnumExtendedInventorySlot.FREQUENCY_MODULE);

                    if (gearID >= 0)
                    {
                        GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.FREQUENCY_MODULE, gearID);
                        TileEntityTelemetry.frequencyModulePlayer(stats.getFrequencyModuleInSlot(), player, false);
                    }
                }
            }

            stats.setLastFrequencyModuleInSlot(stats.getFrequencyModuleInSlot());
        }

        //

        if (stats.getMaskInSlot() != stats.getLastMaskInSlot() || forceSend)
        {
            if (stats.getMaskInSlot().isEmpty())
            {
                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.MASK);
            }
            else if (stats.getLastMaskInSlot().isEmpty() || forceSend)
            {
                int gearID = GalacticraftRegistry.findMatchingGearID(stats.getMaskInSlot(), EnumExtendedInventorySlot.MASK);

                if (gearID >= 0)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.MASK, gearID);
                }
            }

            stats.setLastMaskInSlot(stats.getMaskInSlot());
        }

        //

        if (stats.getGearInSlot() != stats.getLastGearInSlot() || forceSend)
        {
            if (stats.getGearInSlot().isEmpty())
            {
                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.GEAR);
            }
            else if (stats.getGearInSlot().getItem() == GCItems.oxygenGear && (stats.getLastGearInSlot().isEmpty() || forceSend))
            {
                int gearID = GalacticraftRegistry.findMatchingGearID(stats.getGearInSlot(), EnumExtendedInventorySlot.GEAR);

                if (gearID >= 0)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.GEAR, gearID);
                }
            }

            stats.setLastGearInSlot(stats.getGearInSlot());
        }

        //

        if (stats.getTankInSlot1() != stats.getLastTankInSlot1() || forceSend)
        {
            if (stats.getTankInSlot1().isEmpty())
            {
                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.LEFT_TANK);
                stats.setAirRemaining(0);
                GCPlayerHandler.sendAirRemainingPacket(player, stats);
            }
            else if (stats.getLastTankInSlot1().isEmpty() || forceSend)
            {
                int gearID = GalacticraftRegistry.findMatchingGearID(stats.getTankInSlot1(), EnumExtendedInventorySlot.LEFT_TANK);

                if (gearID >= 0)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.LEFT_TANK, gearID);
                }
                if (!stats.getMaskInSlot().isEmpty() && !stats.getGearInSlot().isEmpty())
                {
                    stats.setAirRemaining(stats.getTankInSlot1().getMaxDamage() - stats.getTankInSlot1().getDamageValue());
                    GCPlayerHandler.sendAirRemainingPacket(player, stats);
                }
            }
            //if the else is reached then both tankInSlot and lastTankInSlot are non-null
            else if (stats.getTankInSlot1().getItem() != stats.getLastTankInSlot1().getItem())
            {
                int gearID = GalacticraftRegistry.findMatchingGearID(stats.getTankInSlot1(), EnumExtendedInventorySlot.LEFT_TANK);

                if (gearID >= 0)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.LEFT_TANK, gearID);
                }
                if (!stats.getMaskInSlot().isEmpty() && !stats.getGearInSlot().isEmpty())
                {
                    stats.setAirRemaining(stats.getTankInSlot1().getMaxDamage() - stats.getTankInSlot1().getDamageValue());
                    GCPlayerHandler.sendAirRemainingPacket(player, stats);
                }
            }

            stats.setLastTankInSlot1(stats.getTankInSlot1());
        }

        //

        if (stats.getTankInSlot2() != stats.getLastTankInSlot2() || forceSend)
        {
            if (stats.getTankInSlot2().isEmpty())
            {
                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.RIGHT_TANK);
                stats.setAirRemaining2(0);
                GCPlayerHandler.sendAirRemainingPacket(player, stats);
            }
            else if (stats.getLastTankInSlot2().isEmpty() || forceSend)
            {
                int gearID = GalacticraftRegistry.findMatchingGearID(stats.getTankInSlot2(), EnumExtendedInventorySlot.RIGHT_TANK);

                if (gearID >= 0)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.RIGHT_TANK, gearID);
                }
                if (!stats.getMaskInSlot().isEmpty() && !stats.getGearInSlot().isEmpty())
                {
                    stats.setAirRemaining2(stats.getTankInSlot2().getMaxDamage() - stats.getTankInSlot2().getDamageValue());
                    GCPlayerHandler.sendAirRemainingPacket(player, stats);
                }
            }
            //if the else is reached then both tankInSlot and lastTankInSlot are non-null
            else if (stats.getTankInSlot2().getItem() != stats.getLastTankInSlot2().getItem())
            {
                int gearID = GalacticraftRegistry.findMatchingGearID(stats.getTankInSlot2(), EnumExtendedInventorySlot.RIGHT_TANK);

                if (gearID >= 0)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.RIGHT_TANK, gearID);
                }
                if (!stats.getMaskInSlot().isEmpty() && !stats.getGearInSlot().isEmpty())
                {
                    stats.setAirRemaining2(stats.getTankInSlot2().getMaxDamage() - stats.getTankInSlot2().getDamageValue());
                    GCPlayerHandler.sendAirRemainingPacket(player, stats);
                }
            }

            stats.setLastTankInSlot2(stats.getTankInSlot2());
        }

        //

        if (stats.getParachuteInSlot() != stats.getLastParachuteInSlot() || forceSend)
        {
            if (stats.isUsingParachute())
            {
                if (stats.getParachuteInSlot().isEmpty())
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.PARACHUTE);
                }
                else if (forceSend || stats.getLastParachuteInSlot().isEmpty() || stats.getParachuteInSlot().getDamageValue() != stats.getLastParachuteInSlot().getDamageValue())
                {
                    int gearID = GalacticraftRegistry.findMatchingGearID(stats.getParachuteInSlot(), EnumExtendedInventorySlot.PARACHUTE);

                    if (gearID >= 0)
                    {
                        GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.PARACHUTE, stats.getParachuteInSlot().getDamageValue());
                    }
                }
            }

            stats.setLastParachuteInSlot(stats.getParachuteInSlot());
        }

        //

        if (stats.getThermalHelmetInSlot() != stats.getLastThermalHelmetInSlot() || forceSend)
        {
            ThermalArmorEvent armorEvent = new ThermalArmorEvent(0, stats.getThermalHelmetInSlot());
            MinecraftForge.EVENT_BUS.post(armorEvent);

            if (armorEvent.armorResult != ThermalArmorEvent.ArmorAddResult.NOTHING)
            {
                if (stats.getThermalHelmetInSlot().isEmpty() || armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.REMOVE)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.THERMAL_HELMET);
                }
                else if (armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.ADD && (stats.getLastThermalHelmetInSlot().isEmpty() || forceSend))
                {
                    int gearID = GalacticraftRegistry.findMatchingGearID(stats.getThermalHelmetInSlot(), EnumExtendedInventorySlot.THERMAL_HELMET);

                    if (gearID >= 0)
                    {
                        GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.THERMAL_HELMET, gearID);
                    }
                }
            }

            stats.setLastThermalHelmetInSlot(stats.getThermalHelmetInSlot());
        }

        if (stats.getThermalChestplateInSlot() != stats.getLastThermalChestplateInSlot() || forceSend)
        {
            ThermalArmorEvent armorEvent = new ThermalArmorEvent(1, stats.getThermalChestplateInSlot());
            MinecraftForge.EVENT_BUS.post(armorEvent);

            if (armorEvent.armorResult != ThermalArmorEvent.ArmorAddResult.NOTHING)
            {
                if (stats.getThermalChestplateInSlot().isEmpty() || armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.REMOVE)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.THERMAL_CHESTPLATE);
                }
                else if (armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.ADD && (stats.getLastThermalChestplateInSlot().isEmpty() || forceSend))
                {
                    int gearID = GalacticraftRegistry.findMatchingGearID(stats.getThermalChestplateInSlot(), EnumExtendedInventorySlot.THERMAL_CHESTPLATE);

                    if (gearID >= 0)
                    {
                        GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.THERMAL_CHESTPLATE, gearID);
                    }
                }
            }

            stats.setLastThermalChestplateInSlot(stats.getThermalChestplateInSlot());
        }

        if (stats.getThermalLeggingsInSlot() != stats.getLastThermalLeggingsInSlot() || forceSend)
        {
            ThermalArmorEvent armorEvent = new ThermalArmorEvent(2, stats.getThermalLeggingsInSlot());
            MinecraftForge.EVENT_BUS.post(armorEvent);

            if (armorEvent.armorResult != ThermalArmorEvent.ArmorAddResult.NOTHING)
            {
                if (stats.getThermalLeggingsInSlot().isEmpty() || armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.REMOVE)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.THERMAL_LEGGINGS);
                }
                else if (armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.ADD && (stats.getLastThermalLeggingsInSlot().isEmpty() || forceSend))
                {
                    int gearID = GalacticraftRegistry.findMatchingGearID(stats.getThermalLeggingsInSlot(), EnumExtendedInventorySlot.THERMAL_LEGGINGS);

                    if (gearID >= 0)
                    {
                        GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.THERMAL_LEGGINGS, gearID);
                    }
                }
            }

            stats.setLastThermalLeggingsInSlot(stats.getThermalLeggingsInSlot());
        }

        if (stats.getThermalBootsInSlot() != stats.getLastThermalBootsInSlot() || forceSend)
        {
            ThermalArmorEvent armorEvent = new ThermalArmorEvent(3, stats.getThermalBootsInSlot());
            MinecraftForge.EVENT_BUS.post(armorEvent);

            if (armorEvent.armorResult != ThermalArmorEvent.ArmorAddResult.NOTHING)
            {
                if (stats.getThermalBootsInSlot().isEmpty() || armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.REMOVE)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.THERMAL_BOOTS);
                }
                else if (armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.ADD && (stats.getLastThermalBootsInSlot().isEmpty() || forceSend))
                {
                    int gearID = GalacticraftRegistry.findMatchingGearID(stats.getThermalBootsInSlot(), EnumExtendedInventorySlot.THERMAL_BOOTS);

                    if (gearID >= 0)
                    {
                        GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.THERMAL_BOOTS, gearID);
                    }
                }
            }

            stats.setLastThermalBootsInSlot(stats.getThermalBootsInSlot());
        }

//        if ((stats.getShieldControllerInSlot() != stats.getLastShieldControllerInSlot() || forceSend) && GalacticraftCore.isPlanetsLoaded)
//        {
//            if (stats.getShieldControllerInSlot().isEmpty())
//            {
//                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.SHIELD_CONTROLLER);
//            }
//            else if (stats.getShieldControllerInSlot().getItem() == VenusItems.basicItem && (stats.getLastShieldControllerInSlot().isEmpty() || forceSend))
//            {
//                int gearID = GalacticraftRegistry.findMatchingGearID(stats.getShieldControllerInSlot(), EnumExtendedInventorySlot.SHIELD_CONTROLLER);
//
//                if (gearID >= 0)
//                {
//                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.SHIELD_CONTROLLER, gearID);
//                }
//            }
//
//            stats.setLastShieldControllerInSlot(stats.getShieldControllerInSlot());
//        } TODO Shield Controller
    }

    protected void checkThermalStatus(ServerPlayer player, GCPlayerStats playerStats)
    {
        if (player.level.getDimension() instanceof IGalacticraftDimension && !player.abilities.instabuild && !CompatibilityManager.isAndroid(player))
        {
            final ItemStack thermalPaddingHelm = playerStats.getExtendedInventory().getItem(6);
            final ItemStack thermalPaddingChestplate = playerStats.getExtendedInventory().getItem(7);
            final ItemStack thermalPaddingLeggings = playerStats.getExtendedInventory().getItem(8);
            final ItemStack thermalPaddingBoots = playerStats.getExtendedInventory().getItem(9);
            float lowestThermalStrength = 0.0F;
            if (!thermalPaddingHelm.isEmpty() && !thermalPaddingChestplate.isEmpty() && !thermalPaddingLeggings.isEmpty() && !thermalPaddingBoots.isEmpty())
            {
                if (thermalPaddingHelm.getItem() instanceof IItemThermal)
                {
                    lowestThermalStrength += ((IItemThermal) thermalPaddingHelm.getItem()).getThermalStrength();
                }
                if (thermalPaddingChestplate.getItem() instanceof IItemThermal)
                {
                    lowestThermalStrength += ((IItemThermal) thermalPaddingChestplate.getItem()).getThermalStrength();
                }
                if (thermalPaddingLeggings.getItem() instanceof IItemThermal)
                {
                    lowestThermalStrength += ((IItemThermal) thermalPaddingLeggings.getItem()).getThermalStrength();
                }
                if (thermalPaddingBoots.getItem() instanceof IItemThermal)
                {
                    lowestThermalStrength += ((IItemThermal) thermalPaddingBoots.getItem()).getThermalStrength();
                }
                lowestThermalStrength /= 4.0F;
                lowestThermalStrength = Math.abs(lowestThermalStrength);  //It shouldn't be negative, but just in case!
            }

            IGalacticraftDimension dimension = (IGalacticraftDimension) player.level.getDimension();
            float thermalLevelMod = dimension.getThermalLevelModifier();
            float absThermalLevelMod = Math.abs(thermalLevelMod);

            if (absThermalLevelMod > 0D)
            {
                int thermalLevelCooldownBase = Math.abs(Mth.floor(200 / thermalLevelMod));
                int normaliseCooldown = Mth.floor(150 / lowestThermalStrength);
                int thermalLevelTickCooldown = thermalLevelCooldownBase;
                if (thermalLevelTickCooldown < 1)
                {
                    thermalLevelTickCooldown = 1;   //Prevent divide by zero errors
                }

                if (!thermalPaddingHelm.isEmpty() && !thermalPaddingChestplate.isEmpty() && !thermalPaddingLeggings.isEmpty() && !thermalPaddingBoots.isEmpty())
                {
                    //If the thermal strength exceeds the dimension's thermal level mod, it can't improve the normalise
                    //This factor of 1.5F is chosen so that a combination of Tier 1 and Tier 2 thermal isn't enough to normalise on Venus (three Tier 2, one Tier 1 stays roughly constant)
                    float relativeFactor = Math.max(1.0F, absThermalLevelMod / lowestThermalStrength) / 1.5F;
                    normaliseCooldown = Mth.floor(normaliseCooldown / absThermalLevelMod * relativeFactor);
                    if (normaliseCooldown < 1)
                    {
                        normaliseCooldown = 1;   //Prevent divide by zero errors
                    }
                    // Player is wearing all required thermal padding items
                    if ((player.tickCount - 1) % normaliseCooldown == 0)
                    {
                        this.normaliseThermalLevel(player, playerStats, 1);
                    }
                    thermalLevelMod /= Math.max(1.0F, lowestThermalStrength / 2.0F);
                    absThermalLevelMod = Math.abs(thermalLevelMod);
                }

                if (OxygenUtil.isAABBInBreathableAirBlock(player, true))
                {
                    playerStats.setThermalLevelNormalising(true);
                    this.normaliseThermalLevel(player, playerStats, 1);
                    // If player is in ambient thermal area, slowly reset to normal
                    return;
                }

                // For each piece of thermal equipment being used, slow down the the harmful thermal change slightly
                if (!thermalPaddingHelm.isEmpty())
                {
                    thermalLevelTickCooldown += thermalLevelCooldownBase;
                }
                if (!thermalPaddingChestplate.isEmpty())
                {
                    thermalLevelTickCooldown += thermalLevelCooldownBase;
                }
                if (!thermalPaddingLeggings.isEmpty())
                {
                    thermalLevelTickCooldown += thermalLevelCooldownBase;
                }
                if (!thermalPaddingBoots.isEmpty())
                {
                    thermalLevelTickCooldown += thermalLevelCooldownBase;
                }

                // Instead of increasing/decreasing the thermal level by a large amount every ~200 ticks, increase/decrease
                //      by a small amount each time (still the same average increase/decrease)
                int thermalLevelTickCooldownSingle = Mth.floor(thermalLevelTickCooldown / absThermalLevelMod);
                if (thermalLevelTickCooldownSingle < 1)
                {
                    thermalLevelTickCooldownSingle = 1;   //Prevent divide by zero errors
                }

                if ((player.tickCount - 1) % thermalLevelTickCooldownSingle == 0)
                {
                    int last = playerStats.getThermalLevel();
                    playerStats.setThermalLevel(Math.min(Math.max(last + (thermalLevelMod < 0 ? -1 : 1), -22), 22));

                    if (playerStats.getThermalLevel() != last)
                    {
                        this.sendThermalLevelPacket(player, playerStats);
                    }
                }

                // If the normalisation is outpacing the freeze/overheat
                playerStats.setThermalLevelNormalising(thermalLevelTickCooldownSingle > normaliseCooldown &&
                        !thermalPaddingHelm.isEmpty() &&
                        !thermalPaddingChestplate.isEmpty() &&
                        !thermalPaddingLeggings.isEmpty() &&
                        !thermalPaddingBoots.isEmpty());

                if (!playerStats.isThermalLevelNormalising())
                {
                    if ((player.tickCount - 1) % thermalLevelTickCooldown == 0)
                    {
                        if (Math.abs(playerStats.getThermalLevel()) >= 22)
                        {
                            player.hurt(DamageSourceGC.thermal, 1.5F);
                        }
                    }

                    if (playerStats.getThermalLevel() < -15)
                    {
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 2, true, true));
                    }

                    if (playerStats.getThermalLevel() > 15)
                    {
                        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 5, 2, true, true));
                    }
                }
            }
            else
            //Normalise thermal level if on Space Station or non-modifier planet
            {
                playerStats.setThermalLevelNormalising(true);
                this.normaliseThermalLevel(player, playerStats, 2);
            }
        }
        else
        //Normalise thermal level if on Overworld or any non-GC dimension
        {
            playerStats.setThermalLevelNormalising(true);
            this.normaliseThermalLevel(player, playerStats, 3);
        }
    }

    public void normaliseThermalLevel(ServerPlayer player, GCPlayerStats stats, int increment)
    {
        final int last = stats.getThermalLevel();

        if (stats.getThermalLevel() < 0)
        {
            stats.setThermalLevel(stats.getThermalLevel() + Math.min(increment, -stats.getThermalLevel()));
        }
        else if (stats.getThermalLevel() > 0)
        {
            stats.setThermalLevel(stats.getThermalLevel() - Math.min(increment, stats.getThermalLevel()));
        }

        if (stats.getThermalLevel() != last)
        {
            this.sendThermalLevelPacket(player, stats);
        }
    }

    protected void checkShield(ServerPlayer playerMP, GCPlayerStats playerStats)
    {
        if (playerMP.tickCount % 20 == 0 && playerMP.level.getDimension() instanceof IGalacticraftDimension)
        {
            if (((IGalacticraftDimension) playerMP.level.getDimension()).shouldCorrodeArmor())
            {
                ItemStack shieldController = playerStats.getExtendedInventory().getItem(10);
                boolean valid = false;

                if (!shieldController.isEmpty())
                {
                    int gearID = GalacticraftRegistry.findMatchingGearID(shieldController, EnumExtendedInventorySlot.SHIELD_CONTROLLER);

                    if (gearID != -1)
                    {
                        valid = true;
                    }
                }

                if (!valid)
                {
                    for (ItemStack armor : playerMP.getArmorSlots())
                    {
                        if (!armor.isEmpty() && armor.getItem() instanceof ArmorItem && !(armor.getItem() instanceof IArmorCorrosionResistant))
                        {
                            armor.hurtAndBreak(1, playerMP, (entity) ->
                            {
                            });
                        }
                    }
                }
            }
        }
    }

    protected void checkOxygen(ServerPlayer player, GCPlayerStats stats)
    {
        if ((player.dimension == DimensionType.OVERWORLD || player.level.getDimension() instanceof IGalacticraftDimension) && (!(player.dimension == DimensionType.OVERWORLD || ((IGalacticraftDimension) player.level.getDimension()).hasBreathableAtmosphere()) || player.getY() > GCPlayerHandler.OXYGENHEIGHTLIMIT) && !player.abilities.instabuild && !(player.getVehicle() instanceof EntityLanderBase) && !(player.getVehicle() instanceof EntityAutoRocket) && !(player.getVehicle() instanceof EntityCelestialFake) && !CompatibilityManager.isAndroid(player))
        {
            final ItemStack tankInSlot = stats.getExtendedInventory().getItem(2);
            final ItemStack tankInSlot2 = stats.getExtendedInventory().getItem(3);

            final int drainSpacing = OxygenUtil.getDrainSpacing(tankInSlot, tankInSlot2);

            if (tankInSlot.isEmpty())
            {
                stats.setAirRemaining(0);
            }
            else
            {
                stats.setAirRemaining(tankInSlot.getMaxDamage() - tankInSlot.getDamageValue());
            }

            if (tankInSlot2.isEmpty())
            {
                stats.setAirRemaining2(0);
            }
            else
            {
                stats.setAirRemaining2(tankInSlot2.getMaxDamage() - tankInSlot2.getDamageValue());
            }

            if (drainSpacing > 0)
            {
                if ((player.tickCount - 1) % drainSpacing == 0 && !OxygenUtil.isAABBInBreathableAirBlock(player) && !stats.isUsingPlanetSelectionGui())
                {
                    int toTake = 1;
                    //Take 1 oxygen from Tank 1
                    if (stats.getAirRemaining() > 0)
                    {
                        tankInSlot.hurtAndBreak(1, player, (entity) ->
                        {
                        });
                        stats.setAirRemaining(stats.getAirRemaining() - 1);
                        toTake = 0;
                    }

                    //Alternatively, take 1 oxygen from Tank 2
                    if (toTake > 0 && stats.getAirRemaining2() > 0)
                    {
                        tankInSlot2.hurtAndBreak(1, player, (entity) ->
                        {
                        });
                        stats.setAirRemaining2(stats.getAirRemaining2() - 1);
                        toTake = 0;
                    }
                }
            }
            else
            {
                if ((player.tickCount - 1) % 60 == 0)
                {
                    if (OxygenUtil.isAABBInBreathableAirBlock(player))
                    {
                        if (stats.getAirRemaining() < 90 && !tankInSlot.isEmpty())
                        {
                            stats.setAirRemaining(Math.min(stats.getAirRemaining() + 1, tankInSlot.getMaxDamage() - tankInSlot.getDamageValue()));
                        }

                        if (stats.getAirRemaining2() < 90 && !tankInSlot2.isEmpty())
                        {
                            stats.setAirRemaining2(Math.min(stats.getAirRemaining2() + 1, tankInSlot2.getMaxDamage() - tankInSlot2.getDamageValue()));
                        }
                    }
                    else
                    {
                        if (stats.getAirRemaining() > 0)
                        {
                            stats.setAirRemaining(stats.getAirRemaining() - 1);
                        }

                        if (stats.getAirRemaining2() > 0)
                        {
                            stats.setAirRemaining2(stats.getAirRemaining2() - 1);
                        }
                    }
                }
            }

            final boolean airEmpty = stats.getAirRemaining() <= 0 && stats.getAirRemaining2() <= 0;

            if (player.onLadder())
            {
                stats.setOxygenSetupValid(stats.isLastOxygenSetupValid());
            }
            else
            {
                stats.setOxygenSetupValid(!((!OxygenUtil.hasValidOxygenSetup(player) || airEmpty) && !OxygenUtil.isAABBInBreathableAirBlock(player)));
            }

            if (!player.level.isClientSide && player.isAlive())
            {
                if (!stats.isOxygenSetupValid())
                {
                    GCCoreOxygenSuffocationEvent suffocationEvent = new GCCoreOxygenSuffocationEvent.Pre(player);
                    MinecraftForge.EVENT_BUS.post(suffocationEvent);

                    if (!suffocationEvent.isCanceled())
                    {
                        if (stats.getDamageCounter() == 0)
                        {
                            stats.setDamageCounter(ConfigManagerCore.suffocationCooldown.get());

                            player.hurt(DamageSourceGC.oxygenSuffocation, ConfigManagerCore.suffocationDamage.get() * (2 + stats.getIncrementalDamage()) / 2);
                            if (ConfigManagerCore.hardMode.get())
                            {
                                stats.setIncrementalDamage(stats.getIncrementalDamage() + 1);
                            }

                            GCCoreOxygenSuffocationEvent suffocationEventPost = new GCCoreOxygenSuffocationEvent.Post(player);
                            MinecraftForge.EVENT_BUS.post(suffocationEventPost);
                        }
                    }
                    else
                    {
                        stats.setOxygenSetupValid(true);
                    }
                }
                else
                {
                    stats.setIncrementalDamage(0);
                }
            }
        }
        else if ((player.tickCount - 1) % 20 == 0 && !player.abilities.instabuild && stats.getAirRemaining() < 90)
        {
            stats.setAirRemaining(stats.getAirRemaining() + 1);
            stats.setAirRemaining2(stats.getAirRemaining2() + 1);
        }
        else if (player.abilities.instabuild)
        {
            stats.setAirRemaining(90);
            stats.setAirRemaining2(90);
        }
        else
        {
            stats.setOxygenSetupValid(true);
        }
    }

    protected void throwMeteors(ServerPlayer player)
    {
        Level world = player.level;
        if (world.getDimension() instanceof IGalacticraftDimension && !world.isClientSide)
        {
            if (((IGalacticraftDimension) world.getDimension()).getMeteorFrequency() > 0 && ConfigManagerCore.meteorSpawnMod.get() > 0.0)
            {
                final int f = (int) (((IGalacticraftDimension) world.getDimension()).getMeteorFrequency() * 750D * (1.0 / ConfigManagerCore.meteorSpawnMod.get()));

                if (world.random.nextInt(f) == 0)
                {
                    final Player closestPlayer = world.getNearestPlayer(player, 100);

                    if (closestPlayer == null || closestPlayer.getId() <= player.getId())
                    {
                        int r = world.getServer().getPlayerList().getViewDistance();
                        int x, z;
                        double motX, motZ;
                        x = world.random.nextInt(20) + 160;
                        z = world.random.nextInt(20) - 10;
                        motX = world.random.nextDouble() * 2 - 2.5D;
                        motZ = world.random.nextDouble() * 5 - 2.5D;
                        int px = Mth.floor(player.getX());
                        if ((x + px >> 4) - (px >> 4) >= r)
                        {
                            x = ((px >> 4) + r << 4) - 1 - px;
                        }

                        EntityMeteor meteor = GCEntities.METEOR.create(world);
                        meteor.absMoveTo(player.getX() + x, 355D, player.getZ() + z, meteor.yRot, meteor.xRot);
                        meteor.setDeltaMovement(motX, 0, motZ);

                        world.addFreshEntity(meteor);
                    }
                }

                if (world.random.nextInt(f * 3) == 0)
                {
                    final Player closestPlayer = world.getNearestPlayer(player, 100);

                    if (closestPlayer == null || closestPlayer.getId() <= player.getId())
                    {
                        int r = world.getServer().getPlayerList().getViewDistance();
                        int x, z;
                        double motX, motZ;
                        x = world.random.nextInt(20) + 160;
                        z = world.random.nextInt(20) - 10;
                        motX = world.random.nextDouble() * 2 - 2.5D;
                        motZ = world.random.nextDouble() * 5 - 2.5D;
                        int px = Mth.floor(player.getX());
                        if ((x + px >> 4) - (px >> 4) >= r)
                        {
                            x = ((px >> 4) + r << 4) - 1 - px;
                        }

                        EntityMeteor meteor = GCEntities.METEOR_HUGE.create(world);
                        meteor.absMoveTo(player.getX() + x, 355D, player.getZ() + z, meteor.yRot, meteor.xRot);
                        meteor.setDeltaMovement(motX, 0, motZ);

                        world.addFreshEntity(meteor);
                    }
                }
            }
        }
    }

    private void checkItemAndSwitch(ItemStack currentItem, ServerPlayer player, List<Item> sourceList, List<Item> replaceList, boolean offhand)
    {
        if (!currentItem.isEmpty() && sourceList.contains(currentItem.getItem()))
        {
            //Get space torch for this overworld torch
            int torchItem = 0;
            for (Item i : sourceList)
            {
                if (i == currentItem.getItem())
                {
                    ItemStack stack = new ItemStack(replaceList.get(torchItem), currentItem.getCount());
                    if (offhand)
                    {
                        player.inventory.offhand.set(0, stack);
                    }
                    else
                    {
                        player.inventory.items.set(player.inventory.selected, stack);
                    }
                    break;
                }
                torchItem++;
            }
        }
    }

    protected void checkCurrentItem(ServerPlayer player)
    {
        ItemStack theCurrentItem = player.inventory.getSelected();
        ItemStack offHandItem = player.getOffhandItem();

        int postChangeItem = 0;
        for (ItemStack i : itemChangesPre)
        {
            if (!theCurrentItem.isEmpty() && i.getItem() == theCurrentItem.getItem() && i.getDamageValue() == theCurrentItem.getDamageValue())
            {
                ItemStack postChange = itemChangesPost.get(postChangeItem).copy();
                postChange.setCount(theCurrentItem.getCount());
                player.inventory.items.set(player.inventory.selected, postChange);
                break;
            }
            if (!offHandItem.isEmpty() && i.getItem() == offHandItem.getItem() && i.getDamageValue() == offHandItem.getDamageValue())
            {
                ItemStack postChange = itemChangesPost.get(postChangeItem).copy();
                postChange.setCount(offHandItem.getCount());
                player.inventory.offhand.set(0, postChange);
                break;
            }
            postChangeItem++;
        }
        if (OxygenUtil.noAtmosphericCombustion(player.level.getDimension()))
        {
            checkItemAndSwitch(theCurrentItem, player, torchItemsRegular, torchItemsSpace, false);
            checkItemAndSwitch(offHandItem, player, torchItemsRegular, torchItemsSpace, true);
        }
        else
        {
            checkItemAndSwitch(theCurrentItem, player, torchItemsSpace, torchItemsRegular, false);
            checkItemAndSwitch(offHandItem, player, torchItemsSpace, torchItemsRegular, true);
        }

        // If the player is holding a two-handed item in both hands, switch the off-hand item out, or drop if necessary
        if (!theCurrentItem.isEmpty() && !offHandItem.isEmpty())
        {
            if (theCurrentItem.getItem() instanceof IHoldableItem && offHandItem.getItem() instanceof IHoldableItem)
            {
                int emptyStack = player.inventory.getFreeSlot();

                if (emptyStack >= 0)
                {
                    ItemStack copyOffHandItem = offHandItem.copy();
                    copyOffHandItem.setPopTime(5);
                    player.inventory.items.set(emptyStack, copyOffHandItem);
                }
                else
                {
                    player.drop(offHandItem, false);
                }

                player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            }
        }
    }

    public void registerTorchTypes()
    {
        for (Entry<Block, Block> type : BlockUnlitTorch.registeredTorches.entrySet())
        {
            //Space Torch registration must be unique; there may be multiple mappings for vanillaTorch
            torchItemsSpace.add(Item.byBlock(type.getKey()));
            torchItemsRegular.add(Item.byBlock(type.getValue()));
        }
    }

    public void registerItemChanges()
    {
        for (Entry<Block, Block> type : GCBlocks.itemChanges.entrySet())
        {
            itemChangesPre.add(new ItemStack(type.getKey()));
            itemChangesPost.add(new ItemStack(type.getValue()));
        }
        for (Entry<ItemStack, ItemStack> type : GCItems.itemChanges.entrySet())
        {
            itemChangesPre.add(type.getKey());
            itemChangesPost.add(type.getValue());
        }
    }

    public static void setUsingParachute(ServerPlayer player, GCPlayerStats playerStats, boolean tf)
    {
        playerStats.setUsingParachute(tf);

        if (tf)
        {
            int subtype = GCPlayerHandler.GEAR_NOT_PRESENT;

            if (!playerStats.getParachuteInSlot().isEmpty())
            {
                subtype = playerStats.getParachuteInSlot().getDamageValue();
            }

            GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.PARACHUTE, subtype);
        }
        else
        {
            GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.PARACHUTE);
        }
    }

    protected static void updateFeet(ServerPlayer player, double motionX, double motionZ)
    {
        double motionSqrd = motionX * motionX + motionZ * motionZ;
        if (motionSqrd > 0.001D && !player.abilities.flying)
        {
            int iPosX = Mth.floor(player.getX());
            int iPosY = Mth.floor(player.getY() - 0.05);
            int iPosZ = Mth.floor(player.getZ());

            // If the block below is the moon block
            BlockState state = player.level.getBlockState(new BlockPos(iPosX, iPosY, iPosZ));
            if (state.getBlock() == GCBlocks.moonTurf)
            {
                // And is the correct metadata (moon turf)
//                if (state.get(BlockBasicMoon.BASIC_TYPE_MOON) == BlockBasicMoon.EnumBlockBasicMoon.MOON_TURF) TODO Footprints
//                {
//                    GCPlayerStats stats = GCPlayerStats.get(player);
//                    // If it has been long enough since the last step
//                    if (stats.getDistanceSinceLastStep() > 0.35D)
//                    {
//                        Vector3 pos = new Vector3(player);
//                        // Set the footprint position to the block below and add random number to stop z-fighting
//                        pos.y = MathHelper.floor(player.getPosY() - 1D) + player.world.rand.nextFloat() / 100.0F;
//
//                        // Adjust footprint to left or right depending on step count
//                        switch (stats.getLastStep())
//                        {
//                        case 0:
//                            float a = (-player.rotationYaw + 90F) / Constants.RADIANS_TO_DEGREES;
//                            pos.translate(new Vector3(MathHelper.sin(a) * 0.25F, 0, MathHelper.cos(a) * 0.25F));
//                            break;
//                        case 1:
//                            a = (-player.rotationYaw - 90F) / Constants.RADIANS_TO_DEGREES;
//                            pos.translate(new Vector3(MathHelper.sin(a) * 0.25, 0, MathHelper.cos(a) * 0.25));
//                            break;
//                        }
//
//                        float rotation = player.rotationYaw - 180;
//                        pos = WorldUtil.getFootprintPosition(player.world, rotation, pos, new BlockVec3(player));
//
//                        long chunkKey = ChunkPos.asLong(pos.intX() >> 4, pos.intZ() >> 4);
//                        TickHandlerServer.addFootprint(chunkKey, new Footprint(GCCoreUtil.getDimensionID(player.world), pos, rotation, player.getName(), -1), GCCoreUtil.getDimensionID(player.world));
//
//                        // Increment and cap step counter at 1
//                        stats.setLastStep((stats.getLastStep() + 1) % 2);
//                        stats.setDistanceSinceLastStep(0);
//                    }
//                    else
//                    {
//                        stats.setDistanceSinceLastStep(stats.getDistanceSinceLastStep() + motionSqrd);
//                    }
//                }
            }
        }
    }

    protected void updateSchematics(ServerPlayer player, GCPlayerStats stats)
    {
        SchematicRegistry.addUnlockedPage(player, SchematicRegistry.getMatchingRecipeForID(0));
        SchematicRegistry.addUnlockedPage(player, SchematicRegistry.getMatchingRecipeForID(Integer.MAX_VALUE));

        Collections.sort(stats.getUnlockedSchematics());

        if (player.connection != null && (stats.getUnlockedSchematics().size() != stats.getLastUnlockedSchematics().size() || (player.tickCount - 1) % 100 == 0))
        {
            Integer[] iArray = new Integer[stats.getUnlockedSchematics().size()];

            for (int i = 0; i < iArray.length; i++)
            {
                ISchematicPage page = stats.getUnlockedSchematics().get(i);
                iArray[i] = page == null ? -2 : page.getPageID();
            }

            List<Object> objList = new ArrayList<Object>();
            objList.add(iArray);

            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SCHEMATIC_LIST, GCCoreUtil.getDimensionType(player.level), objList), player);
        }
    }

    public static class ThermalArmorEvent extends Event
    {
        public ArmorAddResult armorResult = ArmorAddResult.NOTHING;
        public final int armorIndex;
        public final ItemStack armorStack;

        public ThermalArmorEvent(int armorIndex, ItemStack armorStack)
        {
            this.armorIndex = armorIndex;
            this.armorStack = armorStack;
        }

        public void setArmorAddResult(ArmorAddResult result)
        {
            this.armorResult = result;
        }

        public enum ArmorAddResult
        {
            ADD,
            REMOVE,
            NOTHING
        }
    }


    protected void sendPlanetList(ServerPlayer player, GCPlayerStats stats)
    {
        HashMap<String, DimensionType> map;
        if (player.tickCount % 50 == 0)
        //Check for genuine update - e.g. maybe some other player created a space station or changed permissions
        //CAUTION: possible server load due to dimension loading, if any planets or moons were (contrary to GC default) set to hotload
        {
            map = WorldUtil.getArrayOfPossibleDimensions(stats.getSpaceshipTier(), player);
        }
        else
        {
            map = WorldUtil.getArrayOfPossibleDimensionsAgain(stats.getSpaceshipTier(), player);
        }

        String temp = "";
        int count = 0;

        for (Entry<String, DimensionType> entry : map.entrySet())
        {
            temp = temp.concat(entry.getKey() + (count < map.entrySet().size() - 1 ? "?" : ""));
            count++;
        }

        if (!temp.equals(stats.getSavedPlanetList()) || (player.tickCount % 100 == 0))
        {
            boolean canCreateStations = PermissionAPI.hasPermission(player, Constants.PERMISSION_CREATE_STATION);
            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_DIMENSION_LIST, GCCoreUtil.getDimensionType(player.level), new Object[]{PlayerUtil.getName(player), temp, canCreateStations}), player);
            stats.setSavedPlanetList(temp);
            //GCLog.debug("Sending to " + PlayerUtil.getName(player) + ": " + temp);
        }
    }

    protected static void sendAirRemainingPacket(ServerPlayer player, GCPlayerStats stats)
    {
        final float f1 = stats.getTankInSlot1().isEmpty() ? 0.0F : stats.getTankInSlot1().getMaxDamage() / 90.0F;
        final float f2 = stats.getTankInSlot2().isEmpty() ? 0.0F : stats.getTankInSlot2().getMaxDamage() / 90.0F;
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_AIR_REMAINING, GCCoreUtil.getDimensionType(player.level), new Object[]{Mth.floor(stats.getAirRemaining() / f1), Mth.floor(stats.getAirRemaining2() / f2), PlayerUtil.getName(player)}), player);
    }

    protected void sendThermalLevelPacket(ServerPlayer player, GCPlayerStats stats)
    {
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_THERMAL_LEVEL, GCCoreUtil.getDimensionType(player.level), new Object[]{stats.getThermalLevel(), stats.isThermalLevelNormalising()}), player);
    }

    protected void sendDungeonDirectionPacket(ServerPlayer player, GCPlayerStats stats)
    {
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_DUNGEON_DIRECTION, GCCoreUtil.getDimensionType(player.level), new Object[]{StructureDungeon.directionToNearestDungeon((ServerLevel) player.level, player.getX() + player.getDeltaMovement().x, player.getZ() + player.getDeltaMovement().z)}), player);
    }

    public static void sendGearUpdatePacket(ServerPlayer player, EnumModelPacketType packetType, EnumExtendedInventorySlot gearType)
    {
        GCPlayerHandler.sendGearUpdatePacket(player, packetType, gearType, GCPlayerHandler.GEAR_NOT_PRESENT);
    }

    public static void sendGearUpdatePacket(ServerPlayer player, EnumModelPacketType packetType, EnumExtendedInventorySlot gearType, int gearID)
    {
        MinecraftServer theServer = player.server;
        if (theServer != null && PlayerUtil.getPlayerForUsernameVanilla(theServer, PlayerUtil.getName(player)) != null)
        {
            GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_UPDATE_GEAR_SLOT, GCCoreUtil.getDimensionType(player.level), new Object[]{player.getUUID(), packetType.ordinal(), gearType.ordinal(), gearID}), new PacketDistributor.TargetPoint(player.getX(), player.getY(), player.getZ(), 50.0, GCCoreUtil.getDimensionType(player.level)));
        }
    }

    public enum EnumModelPacketType
    {
        ADD,
        REMOVE
    }

    public void onPlayerUpdate(ServerPlayer player)
    {
        int tick = player.tickCount - 1;

        //This will speed things up a little
        GCPlayerStats stats = GCPlayerStats.get(player);

//        if ((ConfigManagerCore.challengeSpawnHandling.get()) && stats.getUnlockedSchematics().size() == 0)
//        {
//            if (stats.getStartDimension().length() > 0)
//            {
//                stats.setStartDimension("");
//            }
//            else
//            {
//                //PlayerAPI is installed
//                ServerWorld worldOld = (ServerWorld) player.world;
//                try
//                {
//                    worldOld.getPlayerChunkMap().removePlayer(player);
//                }
//                catch (Exception e)
//                {
//                }
//                worldOld.playerEntities.remove(player);
//                worldOld.updateAllPlayersSleepingFlag();
//                worldOld.loadedEntityList.remove(player);
//                worldOld.onEntityRemoved(player);
//                worldOld.getEntityTracker().untrack(player);
//                if (player.addedToChunk && worldOld.getChunkProvider().chunkExists(player.chunkCoordX, player.chunkCoordZ))
//                {
//                    Chunk chunkOld = worldOld.getChunkFromChunkCoords(player.chunkCoordX, player.chunkCoordZ);
//                    chunkOld.removeEntity(player);
//                    chunkOld.setModified(true);
//                }
//
//                ServerWorld worldNew = WorldUtil.getStartWorld(worldOld);
//                DimensionType dimID = GCCoreUtil.getDimensionID(worldNew);
//                player.dimension = dimID;
//                GCLog.debug("DEBUG: Sending respawn packet to player for dim " + dimID);
//                player.connection.sendPacket(new SRespawnPacket(dimID, player.world.getDifficulty(), player.world.getWorldInfo().getGenerator(), player.interactionManager.getGameType()));
//
//                if (worldNew.dimension instanceof DimensionSpaceStation)
//                {
//                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, GCCoreUtil.getDimensionID(player.world), new Object[] {}), player);
//                }
//                worldNew.addEntity(player);
//                player.setWorld(worldNew);
//                player.server.getPlayerList().preparePlayer(player, (ServerWorld) worldOld);
//            }
//
//            //This is a mini version of the code at WorldUtil.teleportEntity
//            player.interactionManager.setWorld((ServerWorld) player.world);
//            final ITeleportType type = GalacticraftRegistry.getTeleportTypeForDimension(player.world.getDimension().getClass());
//            Vector3 spawnPos = type.getPlayerSpawnLocation((ServerWorld) player.world, player);
//            ChunkPos pair = player.world.getChunkFromChunkCoords(spawnPos.intX() >> 4, spawnPos.intZ() >> 4).getPos();
//            GCLog.debug("Loading first chunk in new dimension.");
//            ((ServerWorld) player.world).getChunkProvider().loadChunk(pair.x, pair.z);
//            player.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, player.rotationYaw, player.rotationPitch);
//            type.setupAdventureSpawn(player);
//            type.onSpaceDimensionChanged(player.world, player, false);
//            player.setSpawnPoint(new BlockPos(spawnPos.intX(), spawnPos.intY(), spawnPos.intZ()), true, GCCoreUtil.getDimensionID(player.world));
//            stats.setNewAdventureSpawn(true);
//        } TODO Challenge spawning
        final boolean isInGCDimension = player.level.getDimension() instanceof IGalacticraftDimension;

        if (tick >= 25)
        {
            if (ConfigManagerCore.enableSpaceRaceManagerPopup.get() && !stats.hasOpenedSpaceRaceManager())
            {
                SpaceRace race = SpaceRaceManager.getSpaceRaceFromPlayer(PlayerUtil.getName(player));

                if (race == null || race.teamName.equals(SpaceRace.DEFAULT_NAME))
                {
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_SPACE_RACE_GUI, GCCoreUtil.getDimensionType(player.level), new Object[]{}), player);
                }

                stats.setOpenedSpaceRaceManager(true);
            }
            if (!stats.hasSentFlags())
            {
//                GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_STATS, GCCoreUtil.getDimensionID(player.world), stats.getMiscNetworkedStats() ), player); TODO Misc stats
                stats.setSentFlags(true);
            }
        }

        if (stats.getCryogenicChamberCooldown() > 0)
        {
            stats.setCryogenicChamberCooldown(stats.getCryogenicChamberCooldown() - 1);
        }

        if (!player.onGround && stats.isLastOnGround())
        {
            stats.setTouchedGround(true);
        }

        if (stats.getTeleportCooldown() > 0)
        {
            stats.setTeleportCooldown(stats.getTeleportCooldown() - 1);
        }

        if (stats.getChatCooldown() > 0)
        {
            stats.setChatCooldown(stats.getChatCooldown() - 1);
        }

        if (stats.getOpenPlanetSelectionGuiCooldown() > 0)
        {
            stats.setOpenPlanetSelectionGuiCooldown(stats.getOpenPlanetSelectionGuiCooldown() - 1);

            if (stats.getOpenPlanetSelectionGuiCooldown() == 1 && !stats.hasOpenedPlanetSelectionGui())
            {
                WorldUtil.toCelestialSelection(player, stats, stats.getSpaceshipTier());
                stats.setHasOpenedPlanetSelectionGui(true);
            }
        }

        if (stats.isUsingParachute())
        {
            if (!stats.getLastParachuteInSlot().isEmpty())
            {
                player.fallDistance = 0.0F;
            }
            if (player.onGround)
            {
                GCPlayerHandler.setUsingParachute(player, stats, false);
            }
        }

        this.checkCurrentItem(player);

        if (stats.isUsingPlanetSelectionGui())
        {
            //This sends the planets list again periodically (forcing the Celestial Selection screen to open) in case of server/client lag
            //#PACKETSPAM
            this.sendPlanetList(player, stats);
        }

		/*		if (isInGCDimension || player.usingPlanetSelectionGui)
                {
					player.connection.ticksForFloatKick = 0;
				}
		*/
        if (stats.getDamageCounter() > 0)
        {
            stats.setDamageCounter(stats.getDamageCounter() - 1);
        }

        if (isInGCDimension)
        {
            if (tick % 10 == 0)
            {
                boolean doneDungeon = false;
                ItemStack current = player.inventory.getSelected();
                if (current != ItemStack.EMPTY && current.getItem() == GCItems.dungeonFinder)
                {
                    this.sendDungeonDirectionPacket(player, stats);
                    doneDungeon = true;
                }
                if (tick % 30 == 0)
                {
                    GCPlayerHandler.sendAirRemainingPacket(player, stats);
                    this.sendThermalLevelPacket(player, stats);

                    if (!doneDungeon)
                    {
                        for (ItemStack stack : player.inventory.items)
                        {
                            if (stack != null && stack.getItem() == GCItems.dungeonFinder)
                            {
                                this.sendDungeonDirectionPacket(player, stats);
                                break;
                            }
                        }
                    }
                }
            }

            if (player.getVehicle() instanceof EntityLanderBase)
            {
                stats.setInLander(true);
                stats.setJustLanded(false);
            }
            else
            {
                if (stats.isInLander())
                {
                    stats.setJustLanded(true);
                }
                stats.setInLander(false);
            }

            if (player.onGround && stats.hasJustLanded())
            {
                stats.setJustLanded(false);

                //Set spawn point here if just descended from a lander for the first time
                if (player.getBedLocation(GCCoreUtil.getDimensionType(player.level)) == null || stats.isNewAdventureSpawn())
                {
                    int i = 30000000;
                    int j = Math.min(i, Math.max(-i, Mth.floor(player.getX() + 0.5D)));
                    int k = Math.min(256, Math.max(0, Mth.floor(player.getY() + 1.5D)));
                    int l = Math.min(i, Math.max(-i, Mth.floor(player.getZ() + 0.5D)));
                    BlockPos coords = new BlockPos(j, k, l);
                    DimensionType type = GCCoreUtil.getDimensionType(player.level);
                    player.setSpawnPoint(coords, player.isRespawnForced(type), true, type);
                    stats.setNewAdventureSpawn(false);
                }

                GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, GCCoreUtil.getDimensionType(player.level), new Object[]{}), player);
            }

//            if (player.world.getDimension() instanceof DimensionSpaceStation || player.world.getDimension() instanceof IZeroGDimension || GalacticraftCore.isPlanetsLoaded && player.world.getDimension() instanceof WorldProviderAsteroids)
//            {
//            	this.preventFlyingKicks(player);
//                if (player.world.getDimension() instanceof DimensionSpaceStation && stats.isNewInOrbit())
//                {
//                    ((DimensionSpaceStation) player.world.getDimension()).getSpinManager().sendPackets(player);
//                    stats.setNewInOrbit(false);
//                }
//            }
//            else
//            {
//                stats.setNewInOrbit(true);
//            } TODO Space station
        }
        else
        {
            stats.setNewInOrbit(true);
        }

        checkGear(player, stats, false);

        if (stats.getChestSpawnCooldown() > 0)
        {
            stats.setChestSpawnCooldown(stats.getChestSpawnCooldown() - 1);

            if (stats.getChestSpawnCooldown() == 180)
            {
                if (stats.getChestSpawnVector() != null)
                {
                    EntityParachest chest = new EntityParachest(player.level, stats.getRocketStacks(), stats.getFuelLevel());

                    chest.setPos(stats.getChestSpawnVector().x, stats.getChestSpawnVector().y, stats.getChestSpawnVector().z);
                    chest.color = stats.getParachuteInSlot().isEmpty() ? DyeColor.WHITE : ((ItemParaChute) stats.getParachuteInSlot().getItem()).getColor();

                    if (!player.level.isClientSide)
                    {
                        player.level.addFreshEntity(chest);
                    }
                }
            }
        }

        //

        if (stats.getLaunchAttempts() > 0 && player.getVehicle() == null)
        {
            stats.setLaunchAttempts(0);
        }

        this.checkThermalStatus(player, stats);
        this.checkOxygen(player, stats);
        this.checkShield(player, stats);

        if (isInGCDimension && (stats.isOxygenSetupValid() != stats.isLastOxygenSetupValid() || tick % 100 == 0))
        {
            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_OXYGEN_VALIDITY, player.level.getDimension().getType(), new Object[]{stats.isOxygenSetupValid()}), player);
        }

        this.throwMeteors(player);

        this.updateSchematics(player, stats);

        if (tick % 250 == 0 && stats.getFrequencyModuleInSlot().isEmpty() && !stats.hasReceivedSoundWarning() && isInGCDimension && player.onGround && tick > 0 && ((IGalacticraftDimension) player.level.getDimension()).getSoundVolReductionAmount() > 1.0F)
        {
            String[] string2 = I18n.get("gui.frequencymodule.warning1").split(" ");
            StringBuilder sb = new StringBuilder();
            for (String aString2 : string2)
            {
                sb.append(" ").append(EnumColor.YELLOW).append(aString2);
            }
            player.sendMessage(new TextComponent(EnumColor.YELLOW + I18n.get("gui.frequencymodule.warning0") + " " + EnumColor.AQUA + GCItems.frequencyModule.getName(new ItemStack(GCItems.frequencyModule, 1)) + sb.toString()));
            stats.setReceivedSoundWarning(true);
        }

        // Player moves and sprints 18% faster with full set of Titanium Armor
//        if (GalacticraftCore.isPlanetsLoaded && tick % 40 == 1 && player.inventory != null)
//        {
//            int titaniumCount = 0;
//            for (ItemStack armorPiece : player.getArmorInventoryList())
//            {
//                if (armorPiece != null && armorPiece.getItem() instanceof ItemArmorAsteroids)
//                {
//                    titaniumCount++;
//                }
//            }
//            if (stats.getSavedSpeed() == 0F)
//            {
//                if (titaniumCount == 4)
//                {
//                    float speed = player.abilities.getWalkSpeed();
//                    if (speed < 0.118F)
//                    {
//                        try {
//                            Field f = player.abilities.getClass().getDeclaredField(GCCoreUtil.isDeobfuscated() ? "walkSpeed" : "field_75097_g");
//                            f.setAccessible(true);
//                            f.set(player.abilities, 0.118F);
//                            stats.setSavedSpeed(speed);
//                        } catch (Exception e) { e.printStackTrace(); }
//                    }
//                }
//            }
//            else if (titaniumCount < 4)
//            {
//                try {
//                    Field f = player.abilities.getClass().getDeclaredField(GCCoreUtil.isDeobfuscated() ? "walkSpeed" : "field_75097_g");
//                    f.setAccessible(true);
//                    f.set(player.abilities, stats.getSavedSpeed());
//                    stats.setSavedSpeed(0F);
//                } catch (Exception e) { e.printStackTrace(); }
//            }
//        } TODO Planets

        stats.setLastOxygenSetupValid(stats.isOxygenSetupValid());
        stats.setLastUnlockedSchematics(stats.getUnlockedSchematics());
        stats.setLastOnGround(player.onGround);
    }

    public void preventFlyingKicks(ServerPlayer player)
    {
        player.fallDistance = 0.0F;
        player.connection.aboveGroundTickCount = 0;
    }
}
