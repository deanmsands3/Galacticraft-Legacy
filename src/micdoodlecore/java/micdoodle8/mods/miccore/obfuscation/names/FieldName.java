package micdoodle8.mods.miccore.obfuscation.names;

/**
 * Created by Mysteryem on 2017-01-30.
 */
public class FieldName extends DeobfAwareString {
    public FieldName(String deobf, String obf) {
        super(deobf, obf);
    }

    public FieldName(String name) {
        super(name);
    }
}
