package team.galacticraft.galacticraft.common.compat;

import me.shedaniel.architectury.networking.NetworkChannel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class GalacticraftCommon {
    public static final NetworkChannel NETWORK_CHANNEL = NetworkChannel.create(new ResourceLocation("galacticraft", "channel"));
    public static final List<Biome> BIOMES_LIST = new ArrayList<>(); //GalacticraftCore.biomesList replacement

    public static void registerPackets() {
        
    }
}
