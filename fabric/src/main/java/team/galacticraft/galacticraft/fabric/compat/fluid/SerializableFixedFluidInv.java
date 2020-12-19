package team.galacticraft.galacticraft.fabric.compat.fluid;

import alexiil.mc.lib.attributes.fluid.FixedFluidInv;
import alexiil.mc.lib.attributes.fluid.FluidVolumeUtil;
import alexiil.mc.lib.attributes.fluid.impl.DelegatingFixedFluidInv;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.misc.Saveable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.jetbrains.annotations.NotNull;
import team.galacticraft.galacticraft.fabric.compat.util.Serializable;

public class SerializableFixedFluidInv extends DelegatingFixedFluidInv implements Serializable
{
    private SerializableFixedFluidInv(FixedFluidInv inv)
    {
        super(inv);
    }

    public static <P extends FixedFluidInv, T extends FixedFluidInv & Saveable> T create(@NotNull P parent)
    {
        if (parent instanceof Saveable) return (T) parent;
        return (T) new SerializableFixedFluidInv(parent);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        ListTag tanksTag = new ListTag();
        for (FluidVolume volume : fluidIterable())
        {
            tanksTag.add(volume.toTag());
        }
        tag.put("tanks", tanksTag);
        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag)
    {
        ListTag tanksTag = tag.getList("tanks", new CompoundTag().getId());
        for (int i = 0; i < tanksTag.size() && i < getTankCount(); i++)
        {
            forceSetInvFluid(i, FluidVolume.fromTag(tanksTag.getCompound(i)));
        }
        for (int i = tanksTag.size(); i < getTankCount(); i++)
        {
            forceSetInvFluid(i, FluidVolumeUtil.EMPTY);
        }
    }
}
