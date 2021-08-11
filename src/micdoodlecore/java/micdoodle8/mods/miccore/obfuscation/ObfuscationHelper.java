package micdoodle8.mods.miccore.obfuscation;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import micdoodle8.mods.miccore.obfuscation.names.MethodName;
import micdoodle8.mods.miccore.obfuscation.names.PrimitiveName;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ObfuscationHelper {
    public static final IClassName BOOLEAN = new PrimitiveName("Z");
    public static final IClassName CHAR = new PrimitiveName("C");
    public static final IClassName DOUBLE = new PrimitiveName("D");
    public static final IClassName FLOAT = new PrimitiveName("F");
    public static final IDeobfAware INIT = new MethodName("<init>");
    public static final IClassName INT = new PrimitiveName("I");
    public static final boolean IS_DEV_ENVIRONMENT = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
    public static final IClassName LONG = new PrimitiveName("J");
    public static final IClassName SHORT = new PrimitiveName("S");
    public static final IClassName VOID = new PrimitiveName("V");
    public static final Map<String, String> deobfNameLookup = new HashMap<>();

    static {
        if (IS_DEV_ENVIRONMENT) {
            FMLDeobfuscatingRemapper instance = FMLDeobfuscatingRemapper.INSTANCE;
            try {
                Field rawFieldMapsField = FMLDeobfuscatingRemapper.class.getDeclaredField("rawFieldMaps");
                rawFieldMapsField.setAccessible(true);

                @SuppressWarnings("unchecked")
                Map<String, Map<String, String>> rawFieldMaps = (Map<String, Map<String, String>>)rawFieldMapsField.get(instance);

                Field rawMethodMapsField = FMLDeobfuscatingRemapper.class.getDeclaredField("rawMethodMaps");
                rawMethodMapsField.setAccessible(true);

                @SuppressWarnings("unchecked")
                Map<String, Map<String, String>> rawMethodMaps = (Map<String, Map<String, String>>)rawMethodMapsField.get(instance);

                for (Map<String, String> innerMap : rawFieldMaps.values()) {
                    for (Map.Entry<String, String> entry : innerMap.entrySet()) {
                        String key = entry.getKey();
                        String[] split = key.split(":");
                        if (split.length == 2) {
                            deobfNameLookup.put(split[0], entry.getValue());
                        }
                        else {
                            System.out.println("[UpAndDown] Unknown field mapping \"" + key + "\" -> \"" + entry.getValue() + "\"");
                        }
                    }
                }

                for (Map<String, String> innerMap : rawMethodMaps.values()) {
                    for (Map.Entry<String, String> entry : innerMap.entrySet()) {
                        String key = entry.getKey();
                        String[] split = key.split("\\(");
                        if (split.length == 2) {
                            deobfNameLookup.put(split[0], entry.getValue());
                        }
                        else {
                            System.out.println("[UpAndDown] Unknown method mapping \"" + key + "\" -> \"" + entry.getValue() + "\"");
                        }
                    }
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

//            System.out.println("Map build complete");
        }
    }


}
