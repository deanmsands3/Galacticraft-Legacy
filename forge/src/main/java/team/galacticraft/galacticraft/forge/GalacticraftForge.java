package team.galacticraft.galacticraft.forge;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.galacticraft.galacticraft.forge.compat.registry.RegistryWrapperForge;

@Mod(Constants.MOD_ID_CORE)
@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE)
public class GalacticraftForge {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> evt) {
        GCBlocks.registerBlocks(new RegistryWrapperForge<>(evt.getRegistry()));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> evt)
    {
        GCBlocks.registerBlockItems(new RegistryWrapperForge<>(evt.getRegistry()));
        GCItems.registerItems(new RegistryWrapperForge<>(evt.getRegistry()));
    }
}
