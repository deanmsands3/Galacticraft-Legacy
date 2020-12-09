package team.galacticraft.galacticraft.common.core.energy;

import team.galacticraft.galacticraft.core.util.GCCoreUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;
import java.util.List;

public class EnergyDisplayHelper
{
    public static void getEnergyDisplayTooltip(float energyVal, float maxEnergy, List<String> strList)
    {
        strList.add(ChatFormatting.GREEN + I18n.get("gui.message.energy") + ": " + getEnergyDisplayS(energyVal));
        strList.add(ChatFormatting.RED + I18n.get("gui.message.max_energy") + ": " + getEnergyDisplayS(maxEnergy));
    }

    public static String getEnergyDisplayS(float energyVal)
    {
        if (EnergyConfigHandler.displayEnergyUnitsIC2.get())
        {
            return getEnergyDisplayIC2(energyVal * EnergyConfigHandler.TO_IC2_RATIOdisp);
        }
        else if (EnergyConfigHandler.displayEnergyUnitsBC.get())
        {
            return getEnergyDisplayBC(energyVal * EnergyConfigHandler.TO_BC_RATIOdisp);
        }
        else if (EnergyConfigHandler.displayEnergyUnitsMek.get())
        {
            return getEnergyDisplayMek(energyVal * EnergyConfigHandler.TO_MEKANISM_RATIOdisp);
        }
        else if (EnergyConfigHandler.displayEnergyUnitsRF.get())
        {
            return getEnergyDisplayRF(energyVal * EnergyConfigHandler.TO_RF_RATIOdisp);
        }
        String val = String.valueOf(getEnergyDisplayI(energyVal));
        String newVal = "";

        for (int i = val.length() - 1; i >= 0; i--)
        {
            newVal += val.charAt(val.length() - 1 - i);
            if (i % 3 == 0 && i != 0)
            {
                newVal += ',';
            }
        }

        return newVal + " gJ";
    }

    public static String getEnergyDisplayIC2(float energyVal)
    {
        String val = String.valueOf(getEnergyDisplayI(energyVal));
        String newVal = "";

        for (int i = val.length() - 1; i >= 0; i--)
        {
            newVal += val.charAt(val.length() - 1 - i);
            if (i % 3 == 0 && i != 0)
            {
                newVal += ',';
            }
        }

        return newVal + " EU";
    }

    public static String getEnergyDisplayBC(float energyVal)
    {
        String val = String.valueOf(getEnergyDisplayI(energyVal));

        return val + " MJ";
    }

    public static String getEnergyDisplayMek(float energyVal)
    {
        if (energyVal < 1000)
        {
            String val = String.valueOf(getEnergyDisplayI(energyVal));
            return val + " J";
        }
        else if (energyVal < 1000000)
        {
            String val = getEnergyDisplay1DP(energyVal / 1000);
            return val + " kJ";
        }
        else
        {
            String val = getEnergyDisplay1DP(energyVal / 1000000);
            return val + " MJ";
        }
    }

    public static String getEnergyDisplayRF(float energyVal)
    {
        String val = String.valueOf(getEnergyDisplayI(energyVal));

        return val + " RF";
    }

    public static int getEnergyDisplayI(float energyVal)
    {
        return Mth.floor(energyVal);
    }

    public static String getEnergyDisplay1DP(float energyVal)
    {
        return "" + Mth.floor(energyVal) + "." + (Mth.floor(energyVal * 10) % 10) + (Mth.floor(energyVal * 100) % 10);
    }
}
