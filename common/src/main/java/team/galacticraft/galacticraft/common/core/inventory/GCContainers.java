package team.galacticraft.galacticraft.common.core.inventory;

import team.galacticraft.galacticraft.common.api.prefab.entity.EntityTieredRocket;
import team.galacticraft.galacticraft.common.core.client.gui.container.*;
import team.galacticraft.galacticraft.common.core.entities.EntityBuggy;
import team.galacticraft.galacticraft.common.core.inventory.ContainerCargoBase.ContainerCargoLoader;
import team.galacticraft.galacticraft.common.core.inventory.ContainerCargoBase.ContainerCargoUnloader;
import team.galacticraft.galacticraft.common.core.proxy.ClientProxyCore;
import team.galacticraft.galacticraft.common.core.tile.*;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.IForgeRegistry;

import static team.galacticraft.galacticraft.common.core.GCBlocks.register;

public class GCContainers
{
    @SubscribeEvent
    public static void initContainers(RegistryEvent.Register<MenuType<?>> evt)
    {
        IForgeRegistry<MenuType<?>> r = evt.getRegistry();

        MenuType<ContainerBuggy> buggy = IForgeContainerType.create((windowId, inv, data) -> new ContainerBuggy(windowId, inv, EntityBuggy.BuggyType.byId(data.readInt())));
        MenuType<ContainerCargoLoader> cargoLoader = IForgeContainerType.create((windowId, inv, data) -> new ContainerCargoLoader(windowId, inv, (TileEntityCargoBase) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerCargoUnloader> cargoUnloader = IForgeContainerType.create((windowId, inv, data) -> new ContainerCargoUnloader(windowId, inv, (TileEntityCargoBase) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerCircuitFabricator> circuitFabricator = IForgeContainerType.create((windowId, inv, data) -> new ContainerCircuitFabricator(windowId, inv, (TileEntityCircuitFabricator) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerCoalGenerator> coalGenerator = IForgeContainerType.create((windowId, inv, data) -> new ContainerCoalGenerator(windowId, inv, (TileEntityCoalGenerator) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerCrafting> crafting = IForgeContainerType.create((windowId, inv, data) -> new ContainerCrafting(windowId, inv, (TileEntityCrafting) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerDeconstructor> deconstructor = IForgeContainerType.create((windowId, inv, data) -> new ContainerDeconstructor(windowId, inv, (TileEntityDeconstructor) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerElectricFurnace> electricFurnace = IForgeContainerType.create((windowId, inv, data) -> new ContainerElectricFurnace(windowId, inv, (TileEntityElectricFurnace) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerElectricIngotCompressor> electricIngotCompressor = IForgeContainerType.create((windowId, inv, data) -> new ContainerElectricIngotCompressor(windowId, inv, (TileEntityElectricIngotCompressor) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerEnergyStorageModule> energyStorageModule = IForgeContainerType.create((windowId, inv, data) -> new ContainerEnergyStorageModule(windowId, inv, (TileEntityEnergyStorageModule) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerExtendedInventory> extendedInventory = IForgeContainerType.create((windowId, inv, data) ->
        {
            return new ContainerExtendedInventory(windowId, inv, ClientProxyCore.dummyInventory);
        });
        MenuType<ContainerFuelLoader> fuelLoader = IForgeContainerType.create((windowId, inv, data) -> new ContainerFuelLoader(windowId, inv, (TileEntityFuelLoader) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerIngotCompressor> ingotCompressor = IForgeContainerType.create((windowId, inv, data) -> new ContainerIngotCompressor(windowId, inv, (TileEntityIngotCompressor) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerOxygenCollector> oxygenCollector = IForgeContainerType.create((windowId, inv, data) -> new ContainerOxygenCollector(windowId, inv, (TileEntityOxygenCollector) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerOxygenCompressor> oxygenCompressor = IForgeContainerType.create((windowId, inv, data) -> new ContainerOxygenCompressor(windowId, inv, (TileEntityOxygenCompressor) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerOxygenDecompressor> oxygenDecompressor = IForgeContainerType.create((windowId, inv, data) -> new ContainerOxygenDecompressor(windowId, inv, (TileEntityOxygenDecompressor) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerOxygenDistributor> oxygenDistributor = IForgeContainerType.create((windowId, inv, data) -> new ContainerOxygenDistributor(windowId, inv, (TileEntityOxygenDistributor) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerOxygenSealer> oxygenSealer = IForgeContainerType.create((windowId, inv, data) -> new ContainerOxygenSealer(windowId, inv, (TileEntityOxygenSealer) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerOxygenStorageModule> oxygenStorageModule = IForgeContainerType.create((windowId, inv, data) -> new ContainerOxygenStorageModule(windowId, inv, (TileEntityOxygenStorageModule) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerPainter> painter = IForgeContainerType.create((windowId, inv, data) -> new ContainerPainter(windowId, inv, (TileEntityPainter) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerParaChest> parachest = IForgeContainerType.create((windowId, inv, data) -> new ContainerParaChest(windowId, inv, (TileEntityParaChest) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerRefinery> refinery = IForgeContainerType.create((windowId, inv, data) -> new ContainerRefinery(windowId, inv, (TileEntityRefinery) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ContainerRocketInventory> rocketInventory = IForgeContainerType.create((windowId, inv, data) -> new ContainerRocketInventory(windowId, inv, (EntityTieredRocket) inv.player.getVehicle()));
        MenuType<ContainerSchematic> schematic = IForgeContainerType.create((windowId, inv, data) -> new ContainerSchematic(windowId, inv));
        MenuType<ContainerSchematicTier1Rocket> schematicT1Rocket = IForgeContainerType.create((windowId, inv, data) -> new ContainerSchematicTier1Rocket(windowId, inv));
        MenuType<ContainerSchematicBuggy> schematicBuggy = IForgeContainerType.create((windowId, inv, data) -> new ContainerSchematicBuggy(windowId, inv));
        MenuType<ContainerSolar> solar = IForgeContainerType.create((windowId, inv, data) -> new ContainerSolar(windowId, inv, (TileEntitySolar) inv.player.world.getBlockEntity(data.readBlockPos())));
        MenuType<ChestMenu> treasureT1 = IForgeContainerType.create((windowId, inv, data) -> ((TileEntityTreasureChest) inv.player.world.getBlockEntity(data.readBlockPos())).createMenu(windowId, inv, inv.player));

        register(r, buggy, GCContainerNames.BUGGY);
        register(r, cargoLoader, GCContainerNames.CARGO_LOADER);
        register(r, cargoUnloader, GCContainerNames.CARGO_UNLOADER);
        register(r, circuitFabricator, GCContainerNames.CIRCUIT_FABRICATOR);
        register(r, coalGenerator, GCContainerNames.COAL_GENERATOR);
        register(r, crafting, GCContainerNames.CRAFTING);
        register(r, deconstructor, GCContainerNames.DECONSTRUCTOR);
        register(r, electricFurnace, GCContainerNames.ELECTRIC_FURNACE);
        register(r, electricIngotCompressor, GCContainerNames.ELECTRIC_INGOT_COMPRESSOR);
        register(r, energyStorageModule, GCContainerNames.ENERGY_STORAGE_MODULE);
        register(r, extendedInventory, GCContainerNames.EXTENDED_INVENTORY);
        register(r, fuelLoader, GCContainerNames.FUEL_LOADER);
        register(r, ingotCompressor, GCContainerNames.INGOT_COMPRESSOR);
        register(r, oxygenCollector, GCContainerNames.OXYGEN_COLLECTOR);
        register(r, oxygenCompressor, GCContainerNames.OXYGEN_COMPRESSOR);
        register(r, oxygenDecompressor, GCContainerNames.OXYGEN_DECOMPRESSOR);
        register(r, oxygenDistributor, GCContainerNames.OXYGEN_DISTRIBUTOR);
        register(r, oxygenSealer, GCContainerNames.OXYGEN_SEALER);
        register(r, oxygenStorageModule, GCContainerNames.OXYGEN_STORAGE_MODULE);
        register(r, painter, GCContainerNames.PAINTER);
        register(r, parachest, GCContainerNames.PARACHEST);
        register(r, refinery, GCContainerNames.REFINERY);
        register(r, rocketInventory, GCContainerNames.ROCKET_INVENTORY);
        register(r, schematic, GCContainerNames.SCHEMATIC);
        register(r, schematicT1Rocket, GCContainerNames.SCHEMATIC_T1_ROCKET);
        register(r, schematicBuggy, GCContainerNames.SCHEMATIC_BUGGY);
        register(r, solar, GCContainerNames.SOLAR);
        register(r, treasureT1, GCContainerNames.TREASURE_CHEST_T1);

        PlatformSpecific.executeSided(EnvType.CLIENT, () -> () ->
        {
            Screens.register(buggy, GuiBuggy::new);
            Screens.register(cargoLoader, GuiCargoLoader::new);
            Screens.register(cargoUnloader, GuiCargoUnloader::new);
            Screens.register(circuitFabricator, GuiCircuitFabricator::new);
            Screens.register(coalGenerator, GuiCoalGenerator::new);
            Screens.register(crafting, GuiCrafting::new);
            Screens.register(deconstructor, GuiDeconstructor::new);
            Screens.register(electricFurnace, GuiElectricFurnace::new);
            Screens.register(electricIngotCompressor, GuiElectricIngotCompressor::new);
            Screens.register(energyStorageModule, GuiEnergyStorageModule::new);
            Screens.register(extendedInventory, GuiExtendedInventory::new);
            Screens.register(fuelLoader, GuiFuelLoader::new);
            Screens.register(ingotCompressor, GuiIngotCompressor::new);
            Screens.register(oxygenCollector, GuiOxygenCollector::new);
            Screens.register(oxygenCompressor, GuiOxygenCompressor::new);
            Screens.register(oxygenDecompressor, GuiOxygenDecompressor::new);
            Screens.register(oxygenDistributor, GuiOxygenDistributor::new);
            Screens.register(oxygenSealer, GuiOxygenSealer::new);
            Screens.register(oxygenStorageModule, GuiOxygenStorageModule::new);
            Screens.register(painter, GuiPainter::new);
            Screens.register(parachest, GuiParaChest::new);
            Screens.register(refinery, GuiRefinery::new);
            Screens.register(rocketInventory, GuiRocketInventory::new);
            Screens.register(schematic, GuiSchematicInput::new);
            Screens.register(schematicT1Rocket, GuiSchematicTier1Rocket::new);
            Screens.register(schematicBuggy, GuiSchematicBuggy::new);
            Screens.register(solar, GuiSolar::new);
            Screens.register(treasureT1, GenericContainerScreen::new);
        });
    }
}
