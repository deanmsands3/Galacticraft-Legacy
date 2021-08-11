package micdoodle8.mods.miccore;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;
import org.objectweb.asm.util.TraceMethodVisitor;

import micdoodle8.mods.miccore.obfuscation.IClassName;
import micdoodle8.mods.miccore.obfuscation.names.DeobfAwareString;
import micdoodle8.mods.miccore.patching.ClassPatcher;
import micdoodle8.mods.miccore.patching.PatchFailedException;
import net.minecraft.launchwrapper.IClassTransformer;

public class Transformer implements IClassTransformer {

    private static final HashMap<String, ClassPatcher> classNameToMethodMap = new HashMap<>();
    public static final Logger logger = LogManager.getLogger("MicdoodleCore", StringFormatterMessageFactory.INSTANCE);
    
    static {
    	
    }
    
    private static void addClassPatch(ClassPatcher patcher) {
        ClassPatcher oldVal = classNameToMethodMap.put(patcher.getClassName(), patcher);
        if (oldVal != null) {
            die("Duplicate class patch for " + oldVal.getClassName());
        }
    }
    
    /**
     * Used to ensure that patches are applied successfully.
     * If not, Minecraft will be made to crash.
     *
     * @param shouldLog           If we should log a success, or force a crash
     * @param dieMessage          Reason why we are forcing Minecraft to crash
     * @param formattableLogMsg   Formattable string a la String::format
     * @param objectsForFormatter Objects to be passed for formatting in log message
     */
    public static void logOrDie(boolean shouldLog, String dieMessage, String formattableLogMsg, Object... objectsForFormatter) {
        if (shouldLog) {
            log(formattableLogMsg, objectsForFormatter);
        }
        else {
            die(dieMessage);
        }
    }

    /**
     * Default logging method.
     *
     * @param string  Formattable string a la String::format
     * @param objects Objects to be passed for formatting in log message
     */
    public static void log(String string, Object... objects) {
        logger.info(string, objects);
    }

    /**
     * Default 'crash Minecraft' method.
     *
     * @param string Reason why we are forcing Minecraft to crash
     */
    public static void die(String string) throws PatchFailedException {
        throw new PatchFailedException("[UpAndDown] " + string);
    }

    public static void die(String string, Throwable cause) throws PatchFailedException {
        throw new PatchFailedException("[UpAndDown] " + string, cause);
    }

    /**
     * Debug helper method to print a class, from bytes, to "standard" output.
     * Useful to compare a class's bytecode, before and after patching if the bytecode is not what we expect, or if the
     * patched bytecode is invalid.
     *
     * @param bytes The bytes that make up the class
     */
    public static void printClassToStdOut(byte[] bytes) {
        printClassToStream(bytes, System.out);
    }

    /**
     * Internal method to print a class (from bytes), to an OutputStream.
     *
     * @param bytes        bytes that make up a class
     * @param outputStream stream to output the class's bytecode to
     */
    public static void printClassToStream(byte[] bytes, OutputStream outputStream) {
        ClassNode classNode2 = new ClassNode();
        ClassReader classReader2 = new ClassReader(bytes);
        PrintWriter writer = new PrintWriter(outputStream);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(classNode2, writer);
        classReader2.accept(traceClassVisitor, 0);
        writer.flush();
    }

    public static void printClassToFMLLogger(byte[] bytes) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        printClassToStream(bytes, byteArrayOutputStream);
        logger.info("\n%s", byteArrayOutputStream);
    }

    public static void printMethodToStdOut(MethodNode methodNode) {
        printMethodToStream(methodNode, System.out);
    }

    public static void printMethodToStream(MethodNode methodNode, OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);
        Textifier textifier = new Textifier();
        TraceMethodVisitor traceMethodVisitor = new TraceMethodVisitor(textifier);
        methodNode.accept(traceMethodVisitor);
        textifier.print(writer);
        writer.flush();
    }

    public static void printMethodToFMLLogger(MethodNode methodNode) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        printMethodToStream(methodNode, byteArrayOutputStream);
        logger.info("\n%s", byteArrayOutputStream);
    }

    public static void logPatchStarting(DeobfAwareString deobfAwareString) {
        logPatchStarting(deobfAwareString.getDeobf());
    }

    public static void logPatchStarting(Object object) {
        log("Patching %s", object);
    }

    /**
     * Use to add a local variable to a method.
     *
     * @param methodNode Method to add the localvariable to
     * @param iClassName The variable's class
     * @return The index of the new local variable
     */
    public static int addLocalVar(MethodNode methodNode, IClassName iClassName) {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(methodNode.access, methodNode.desc, methodNode);
        return localVariablesSorter.newLocal(iClassName.asType());
    }

    public static void logPatchComplete(DeobfAwareString deobfAwareString) {
        logPatchComplete(deobfAwareString.getDeobf());
    }

    public static void logPatchComplete(Object object) {
        log("Patched  %s", object);
    }

    public static void dieIfFalse(boolean shouldContinue, ClassNode classNode) {
        dieIfFalse(shouldContinue, "Failed to find the methods to patch in " + classNode.name +
                ". The Minecraft version you are using likely does not match what the mod requires.");
    }

    /**
     * Option to used to compound multiple tests together
     *
     * @param shouldContinue boolean to check, if false, Minecraft is forced to crash with the passed dieMessage
     * @param dieMessage     The message to use as the cause when making Minecraft crash
     * @return
     */
    public static void dieIfFalse(boolean shouldContinue, String dieMessage) {
        if (!shouldContinue) {
            die(dieMessage);
        }
    }

    private static void warn(String string, Object... objects) {
        logger.warn(string, objects);
    }

    /**
     * Core transformer method. Recieves classes by name and their bytes and processes them if necessary.
     *
     * @param className            class name prior to deobfuscation (I think)
     * @param transformedClassName runtime deobfuscated class name
     * @param bytes                the bytes that make up the class sent to the transformer
     * @return the bytes of the now processed class
     */
    @Override
    public byte[] transform(String className, String transformedClassName, byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        Function<byte[], byte[]> function = classNameToMethodMap.get(transformedClassName);

        if (function == null) {
            return bytes;
        }
        else {
            // Remove from the map to remove reference to the patcher so garbage collector can hopefully pick it up
            classNameToMethodMap.remove(transformedClassName);
            log("Patching class %s", transformedClassName);
            byte[] toReturn = function.apply(bytes);
            log("Patched class  %s", transformedClassName);
            return toReturn;
        }
    }
}
