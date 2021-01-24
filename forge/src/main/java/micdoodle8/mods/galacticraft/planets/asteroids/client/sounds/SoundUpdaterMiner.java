package micdoodle8.mods.galacticraft.planets.asteroids.client.sounds;

import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.AstroMinerEntity;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.world.entity.Entity;

/**
 * This file is part of the Galacticraft project
 *
 * @author micdoodle8, radfast
 */
public class SoundUpdaterMiner extends AbstractTickableSoundInstance
{
    private final LocalPlayer thePlayer;
    private final AstroMinerEntity theRocket;
    private boolean soundStopped;
    private float targetVolume;
    private float targetPitch;

    public SoundUpdaterMiner(LocalPlayer par1EntityPlayerSP, AstroMinerEntity par2Entity)
    {
        super(GCSounds.astroMiner, SoundCategory.AMBIENT);
        this.theRocket = par2Entity;
        this.thePlayer = par1EntityPlayerSP;
        this.volume = 0.00001F;  //If it's zero it won't start playing
        this.targetVolume = 0.6F;
        this.targetPitch = 1.0F;
        this.pitch = 1.0F;  //pitch
        this.looping = true;
        this.delay = 0;  //repeat delay
        this.updateSoundLocation(par2Entity);
    }

    /**
     * Updates the JList with a new model.
     */
    @Override
    public void tick()
    {
        if (this.theRocket.isAlive())
        {
            if (this.theRocket.AIstate == AstroMinerEntity.AISTATE_ATBASE || this.theRocket.AIstate == AstroMinerEntity.AISTATE_DOCKING)
            {
                this.targetVolume = 0.6F;
                this.targetPitch = 0.1F;
            }
            else
            {
                this.targetVolume = 1.0F;
                this.targetPitch = 1.0F;
            }
            if (this.volume < this.targetVolume)
            {
                this.volume += 0.1F;
                if (this.volume > this.targetVolume)
                {
                    this.volume = this.targetVolume;
                }
            }
            else if (this.volume > this.targetVolume)
            {
                this.volume -= 0.1F;
                if (this.volume < this.targetVolume)
                {
                    this.volume = this.targetVolume;
                }
            }
            if (this.pitch < this.targetPitch)
            {
                this.pitch += 0.05F;
                if (this.pitch > this.targetPitch)
                {
                    this.pitch = this.targetPitch;
                }
            }
            else if (this.pitch > this.targetPitch)
            {
                this.pitch -= 0.05F;
                if (this.pitch < this.targetPitch)
                {
                    this.pitch = this.targetPitch;
                }
            }
            this.updateSoundLocation(this.theRocket);
        }
        else
        {
            this.stopped = true;
        }
    }

    public void stopRocketSound()
    {
        this.stopped = true;
        this.soundStopped = true;
    }

    public void updateSoundLocation(Entity e)
    {
        this.x = (float) e.getX();
        this.y = (float) e.getY();
        this.z = (float) e.getZ();
    }
}
