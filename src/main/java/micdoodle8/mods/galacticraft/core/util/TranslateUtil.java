package micdoodle8.mods.galacticraft.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialType;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.text.translation.LanguageMap;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class TranslateUtil
{

    private static TranslateUtil instance = getInstance();

    private Map<String, String> celestialKeys = new HashMap<>();

    private TranslateUtil()
    {
        try
        {
            Optional<LanguageMap> langMapInstance = ASMUtil.getStaticObject(LanguageMap.class, "instance");
            populateMap(ASMUtil.getPrivateValue(LanguageMap.class, langMapInstance.get(), "languageList"));
        } catch (Exception e)
        {
            GalacticraftCore.logger.error(e, "An error occured when trying to retreive LanguageMap");
        }
    }

    private void populateMap(Map<String, String> map)
    {
        Collection<String> names = CelestialType.getAllNames();
        for (String type : names)
        {
            map.entrySet().stream().filter(e -> e.getKey().startsWith(type)).collect(Collectors.toSet()).forEach(e ->
            {
                celestialKeys.put(e.getKey().toLowerCase(), e.getValue());
            });
        }
    }

    public static void generateMaps()
    {
        if (instance != null)
        {
            return;
        }
        instance = new TranslateUtil();
    }

    public static TranslateUtil getInstance()
    {
        return instance;
    }

    public String translate(String translationKey)
    {
        for (Entry<String, String> s : celestialKeys.entrySet())
        {
            if (s.getKey().startsWith(translationKey))
            {
                return s.getValue();
            }
        }

        return I18n.translateToLocal(translationKey);

    }
}
