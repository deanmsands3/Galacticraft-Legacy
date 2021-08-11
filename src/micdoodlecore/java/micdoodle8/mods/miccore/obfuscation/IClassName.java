package micdoodle8.mods.miccore.obfuscation;

import org.objectweb.asm.Type;
import micdoodle8.mods.miccore.obfuscation.names.ArrayName;

/**
 * Created by Mysteryem on 2017-01-30.
 */
public interface IClassName {
    default ArrayName asArray() {
        return new ArrayName(this);
    }

    default Type asType() {
        return Type.getType(this.asDescriptor());
    }

    default String asDescriptor() {
        return this.toString();
    }
}
