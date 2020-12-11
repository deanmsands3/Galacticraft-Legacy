package team.galacticraft.galacticraft.common.core.client.sounds;

import team.galacticraft.galacticraft.common.api.world.IGalacticraftDimension;
import team.galacticraft.galacticraft.common.core.proxy.ClientProxyCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.util.Mth;

public class MusicTickerGC extends MusicManager
{
    public MusicTickerGC(Minecraft client)
    {
        super(client);
    }

    @Override
    public void tick()
    {
        Music musictype = this.minecraft.getSituationalMusic();
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.dimension instanceof IGalacticraftDimension)
        {
            musictype = ClientProxyCore.MUSIC_TYPE_MARS;
        }

        if (this.currentMusic != null)
        {
            if (!musictype.getEvent().getLocation().equals(this.currentMusic.getLocation()))
            {
                this.minecraft.getSoundManager().stop(this.currentMusic);
                this.nextSongDelay = Mth.nextInt(this.random, 0, musictype.getMinDelay() / 2);
            }

            if (!this.minecraft.getSoundManager().isActive(this.currentMusic))
            {
                this.currentMusic = null;
                this.nextSongDelay = Math.min(Mth.nextInt(this.random, musictype.getMinDelay(), musictype.getMaxDelay()), this.nextSongDelay);
            }
        }

        this.nextSongDelay = Math.min(this.nextSongDelay, musictype.getMaxDelay());

        if (this.currentMusic == null && this.nextSongDelay-- <= 0)
        {
            this.startPlaying(musictype);
        }
    }
}
