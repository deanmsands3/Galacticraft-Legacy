package team.galacticraft.galacticraft.common.compat.fluid;

import me.shedaniel.architectury.utils.Fraction;

public final class FractionUtil
{
    private FractionUtil() {}

    public static Fraction max(Fraction a, Fraction b) {
        return a.isGreaterThan(b) ? a : b;
    }

    public static Fraction min(Fraction a, Fraction b) {
        return a.isLessThan(b) ? a : b;
    }
}
