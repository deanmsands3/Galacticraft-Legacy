package micdoodle8.mods.galacticraft.core.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;

//TODO Removed if MinecraftForge#7356 got merged
@Mixin(MusicManager.class)
public class MixinMusicTicker
{
    @Redirect(method = "tick()V", at = @At(value = "INVOKE", target = "net/minecraft/client/Minecraft.getAmbientMusicType()Lnet/minecraft/client/audio/MusicTicker$MusicType;"))
    private MusicManager.Music getAmbientMusicType(Minecraft mc)
    {
        /*if (mc.world != null && mc.world.dimension instanceof IGalacticraftDimension)TODO Disable this until forge add support for MusicTicker.MusicType
        {
            return ClientProxyCore.MUSIC_TYPE_MARS;
        }*/
        return mc.getSituationalMusic();
    }
}