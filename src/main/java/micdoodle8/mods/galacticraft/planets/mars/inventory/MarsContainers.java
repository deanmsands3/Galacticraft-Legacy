package micdoodle8.mods.galacticraft.planets.mars.inventory;

import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.core.inventory.ContainerRocketInventory;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.*;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.tile.*;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.IForgeRegistry;

import static micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks.*;

public class MarsContainers
{
    @SubscribeEvent
    public static void initContainers(RegistryEvent.Register<MenuType<?>> evt)
    {
        IForgeRegistry<MenuType<?>> r = evt.getRegistry();

        MenuType<ContainerElectrolyzer> electrolyzer = IForgeContainerType.create((windowId, inv, data) -> new ContainerElectrolyzer(windowId, inv, (TileEntityElectrolyzer) inv.player.world.getBlockEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        MenuType<ContainerGasLiquefier> gasLiquefier = IForgeContainerType.create((windowId, inv, data) -> new ContainerGasLiquefier(windowId, inv, (TileEntityGasLiquefier) inv.player.world.getBlockEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        MenuType<ContainerLaunchController> launchController = IForgeContainerType.create((windowId, inv, data) -> new ContainerLaunchController(windowId, inv, (TileEntityLaunchController) inv.player.world.getBlockEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        MenuType<ContainerLaunchController> launchControllerAdvanced = IForgeContainerType.create((windowId, inv, data) -> new ContainerLaunchController(windowId, inv, (TileEntityLaunchController) inv.player.world.getBlockEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        MenuType<ContainerMethaneSynthesizer> methaneSynthesizer = IForgeContainerType.create((windowId, inv, data) -> new ContainerMethaneSynthesizer(windowId, inv, (TileEntityMethaneSynthesizer) inv.player.world.getBlockEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        MenuType<ContainerSchematicTier2Rocket> schematicT2Rocket = IForgeContainerType.create((windowId, inv, data) -> new ContainerSchematicTier2Rocket(windowId, inv));
        MenuType<ContainerSchematicCargoRocket> schematicCargoRocket = IForgeContainerType.create((windowId, inv, data) -> new ContainerSchematicCargoRocket(windowId, inv));
        MenuType<ContainerSlimeling> slimeling = IForgeContainerType.create((windowId, inv, data) -> new ContainerSlimeling(windowId, inv, (EntitySlimeling) inv.player.world.getEntityById(data.readInt())));
        MenuType<ContainerTerraformer> terraformer = IForgeContainerType.create((windowId, inv, data) -> new ContainerTerraformer(windowId, inv, (TileEntityTerraformer) inv.player.world.getBlockEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));

        register(r, electrolyzer, MarsContainerNames.ELECTROLYZER);
        register(r, gasLiquefier, MarsContainerNames.GAS_LIQUEFIER);
        register(r, launchController, MarsContainerNames.LAUNCH_CONTROLLER);
        register(r, launchControllerAdvanced, MarsContainerNames.LAUNCH_CONTROLLER_ADVANCED);
        register(r, methaneSynthesizer, MarsContainerNames.METHANE_SYNTHESIZER);
        register(r, schematicT2Rocket, MarsContainerNames.SCHEMATIC_T2_ROCKET);
        register(r, schematicCargoRocket, MarsContainerNames.SCHEMATIC_CARGO_ROCKET);
        register(r, slimeling, MarsContainerNames.SLIMELING);
        register(r, terraformer, MarsContainerNames.TERRAFORMER);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
        {
            Screens.register(electrolyzer, GuiWaterElectrolyzer::new);
            Screens.register(gasLiquefier, GuiGasLiquefier::new);
            Screens.register(launchController, GuiLaunchController::new);
            Screens.register(launchControllerAdvanced, GuiLaunchController::new);
            Screens.register(methaneSynthesizer, GuiMethaneSynthesizer::new);
            Screens.register(schematicT2Rocket, GuiSchematicTier2Rocket::new);
            Screens.register(schematicCargoRocket, GuiSchematicCargoRocket::new);
            Screens.register(slimeling, GuiSlimelingInventory::new);
            Screens.register(terraformer, GuiTerraformer::new);
        });
    }
}
