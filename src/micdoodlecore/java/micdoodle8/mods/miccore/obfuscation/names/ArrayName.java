package micdoodle8.mods.miccore.obfuscation.names;

import micdoodle8.mods.miccore.obfuscation.IClassName;

/**
 * Created by Mysteryem on 2017-01-30.
 */
public class ArrayName extends DeobfAwareString implements IClassName {

    public ArrayName(IClassName componentClass) {
        super(componentClass.toString());
    }

    @Override
    public String asDescriptor() {
        return '[' + this.toString();
    }
}
