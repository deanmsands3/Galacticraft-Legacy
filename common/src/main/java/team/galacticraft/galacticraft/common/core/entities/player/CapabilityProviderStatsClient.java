package team.galacticraft.galacticraft.common.core.entities.player;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import team.galacticraft.galacticraft.common.api.util.LazyOptional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CapabilityProviderStatsClient implements ICapabilityProvider
{
    private final LocalPlayer owner;
    private final LazyOptional<GCPlayerStatsClient> holder = LazyOptional.create(StatsClientCapability::new);

    public CapabilityProviderStatsClient(LocalPlayer owner)
    {
        this.owner = owner;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull final Capability<T> cap, final @Nullable Direction side)
    {
        if (cap == GCCapabilities.GC_STATS_CLIENT_CAPABILITY)
        {
            return GCCapabilities.GC_STATS_CLIENT_CAPABILITY.orEmpty(cap, holder);
        }

        return LazyOptional.empty();
    }
}
