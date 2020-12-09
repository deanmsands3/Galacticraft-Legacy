package team.galacticraft.galacticraft.common.core.util;

import net.minecraft.world.damagesource.DamageSource;

public class DamageSourceGC extends DamageSource
{
    public static final DamageSourceGC spaceshipCrash = (DamageSourceGC) new DamageSourceGC("spaceship_crash").bypassArmor().setExplosion();
    public static final DamageSourceGC oxygenSuffocation = (DamageSourceGC) new DamageSourceGC("oxygen_suffocation").bypassArmor().bypassMagic();
    public static final DamageSourceGC thermal = (DamageSourceGC) new DamageSourceGC("thermal").bypassArmor().bypassMagic();
    public static final DamageSourceGC acid = new DamageSourceGC("sulphuric_acid");
    public static final DamageSourceGC laserTurret = new DamageSourceGC("laser_turret");

    public DamageSourceGC(String damageType)
    {
        super(damageType);
    }
}
