package micdoodle8.mods.galacticraft.api.entity;


import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Implement into entities that make a sound all the time, like rockets
 */
public interface IEntityNoisy
{
    @OnlyIn(Dist.CLIENT)
    AbstractTickableSoundInstance getSoundUpdater();

    @OnlyIn(Dist.CLIENT)
    SoundInstance setSoundUpdater(LocalPlayer player);
}
