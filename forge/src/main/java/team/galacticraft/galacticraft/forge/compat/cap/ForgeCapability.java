package team.galacticraft.galacticraft.forge.compat.cap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import team.galacticraft.galacticraft.common.api.util.LazyOptional;
import team.galacticraft.galacticraft.common.compat.component.ComponentWrapper;

public class ForgeCapability<T> implements ComponentWrapper<T>
{
    private final Capability<T> capability;

    public ForgeCapability(Capability<T> capability)
    {
        this.capability = capability;
    }

    @Override
    public void fromTag(CompoundNBT tag, T instance)
    {
        capability.getStorage().readNBT(capability, instance, null, tag);
    }

    @Override
    public CompoundNBT toTag(CompoundNBT tag, T instance)
    {
        return (CompoundNBT) capability.getStorage().writeNBT(capability, instance, null);
    }

    @Override
    public LazyOptional<T> get(Object provider)
    {
        assert provider instanceof CapabilityProvider;
        return ((CapabilityProvider<?>) provider).getCapability(capability, null).isPresent() ? LazyOptional.create(() -> ((CapabilityProvider<?>) provider).getCapability(capability, null).get()) : LazyOptional.empty();
    }
}
