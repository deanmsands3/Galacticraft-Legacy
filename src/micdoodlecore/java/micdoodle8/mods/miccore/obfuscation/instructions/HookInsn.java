package micdoodle8.mods.miccore.obfuscation.instructions;

import org.objectweb.asm.Opcodes;
import micdoodle8.mods.miccore.obfuscation.IDeobfAware;
import micdoodle8.mods.miccore.obfuscation.MethodDesc;
import micdoodle8.mods.miccore.obfuscation.names.ObjectName;

/**
 * Created by Mysteryem on 2017-01-30.
 */
public class HookInsn extends MethodInsn {
    private static final ObjectName Hooks = new ObjectName("uk/co/mysterymayhem/gravitymod/asm/Hooks");

    public HookInsn(IDeobfAware methodName, MethodDesc description) {
        super(Opcodes.INVOKESTATIC, Hooks, methodName, description);
    }

    public HookInsn(String methodName, MethodDesc description) {
        super(Opcodes.INVOKESTATIC, Hooks, methodName, description);
    }
}
