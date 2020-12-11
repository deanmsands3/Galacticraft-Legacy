package team.galacticraft.galacticraft.common.core.client.sounds;

import team.galacticraft.galacticraft.common.api.prefab.entity.EntityAutoRocket;
import team.galacticraft.galacticraft.common.api.prefab.entity.EntitySpaceshipBase.EnumLaunchPhase;
import team.galacticraft.galacticraft.common.Constants;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

/**
 * This file is part of the Galacticraft project
 *
 * @author micdoodle8
 */
public class SoundUpdaterRocket extends AbstractTickableSoundInstance
{
    private final LocalPlayer thePlayer;
    private final EntityAutoRocket theRocket;
    private boolean soundStopped;
    private boolean ignition = false;

    public SoundUpdaterRocket(LocalPlayer par1EntityPlayerSP, EntityAutoRocket par2Entity)
    {
        super(GCSounds.shuttle, SoundSource.NEUTRAL);
        this.theRocket = par2Entity;
        this.thePlayer = par1EntityPlayerSP;
        this.attenuation = Attenuation.NONE;
        this.volume = 0.00001F;  //If it's zero it won't start playing
        this.pitch = 0.0F;  //pitch
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
            if (this.theRocket.launchPhase == EnumLaunchPhase.IGNITED.ordinal())
            {
                if (!ignition)
                {
                    this.pitch = 0.0F;
                    ignition = true;
                }
                if (this.theRocket.timeUntilLaunch < this.theRocket.getPreLaunchWait())
                {
                    if (this.pitch < 1.0F)
                    {
                        this.pitch += 0.0025F;
                    }

                    if (this.pitch > 1.0F)
                    {
                        this.pitch = 1.0F;
                    }
                }
            }
            else
            {
                this.pitch = 1.0F;
            }

            if (this.theRocket.launchPhase >= EnumLaunchPhase.IGNITED.ordinal())
            {
                if (this.theRocket.getY() > 1000)
                {
                    this.volume = 0F;
                    if (this.theRocket.launchPhase != EnumLaunchPhase.LANDING.ordinal())
                    {
                        this.stopped = true;
                    }
                }
                else if (this.theRocket.getY() > Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT)
                {
                    this.volume = 1.0F - (float) ((this.theRocket.getY() - Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT) / (1000.0 - Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT));
                }
                else
                {
                    this.volume = 1.0F;
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
