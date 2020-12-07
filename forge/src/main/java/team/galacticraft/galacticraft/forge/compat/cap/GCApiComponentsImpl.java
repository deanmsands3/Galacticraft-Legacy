package team.galacticraft.galacticraft.forge.compat.cap;

import com.mojang.datafixers.types.templates.Tag;
import me.shedaniel.architectury.ExpectPlatform;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import team.galacticraft.galacticraft.common.Constants;
import team.galacticraft.galacticraft.common.api.entity.GCPlayerStats;
import team.galacticraft.galacticraft.common.compat.cap.ComponentWrapper;

public class GCApiComponentsImpl {
    @CapabilityInject(GCPlayerStats.class)
    public static Capability<GCPlayerStats> GC_STATS_CAPABILITY = null;

    public static ComponentWrapper<GCPlayerStats> getPlayerStats() {
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(GCPlayerStats.class, new Capability.IStorage<GCPlayerStats>()
        {
            @Override
            public Tag writeNBT(Capability<GCPlayerStats> capability, GCPlayerStats instance, Direction side)
            {
                CompoundNBT nbt = new CompoundNBT();
                instance.saveNBTData(nbt);
                return nbt;
            }

            @Override
            public void readNBT(Capability<GCPlayerStats> capability, GCPlayerStats instance, Direction side, Tag nbt)
            {
                instance.loadNBTData((CompoundNBT) nbt);
            }
        }, StatsCapability::new);

    }
}
