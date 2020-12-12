package team.galacticraft.galacticraft.fabric;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import team.galacticraft.galacticraft.common.core.GCBlocks;
import team.galacticraft.galacticraft.common.core.GCItems;
import team.galacticraft.galacticraft.fabric.compat.registry.RegistryWrapperFabric;

public class GalacticraftFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        GCBlocks.registerBlocks(new RegistryWrapperFabric<>(Registry.BLOCK));
        GCBlocks.registerItemBlocks(new RegistryWrapperFabric<>(Registry.ITEM));
        GCBlocks.initTileEntities(new RegistryWrapperFabric<>(Registry.BLOCK_ENTITY_TYPE));
        GCItems.registerItems(new RegistryWrapperFabric<>(Registry.ITEM));
    }
}
