package fr.upem.foraxproof.core;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * ASMUtils contains a brunch of static methods used to
 * provides "human easy readable" processing.
 */
public final class ASMUtils {
    /**
     * Returns true if access parameter contains a bit to
     * declare a ACC_PROTECTED.
     * @param access the int access to process.
     * @return true if access is protected.
     */
    public static boolean isProtected(int access) {
        return (access & Opcodes.ACC_PROTECTED) != 0;
    }

    /**
     * Returns true if access parameter contains a bit to
     * declare a ACC_PUBLIC.
     * @param access the int access to process.
     * @return true if access is public.
     */
    public static boolean isPublic(int access) {
        return (access & Opcodes.ACC_PUBLIC) != 0;
    }

    /**
     * Returns true if access parameter contains a bit to
     * declare a ACC_ABSTRACT.
     * @param access the int access to process.
     * @return true if access is abstract.
     */
    public static boolean isAbstract(int access) {
        return (access & Opcodes.ACC_ABSTRACT) != 0;
    }

    /**
     * Returns true if access parameter contains a bit to
     * declare a ACC_INTERFACE.
     * @param access the int access to process.
     * @return true if access declare an interface.
     */
    public static boolean isInterface(int access) {
        return (access & Opcodes.ACC_INTERFACE) != 0;
    }

    /**
     * Returns true if access parameter contains a bit to
     * declare a ACC_STATIC.
     * @param access the int access to process.
     * @return true if access is static.
     */
    public static boolean isStatic(int access){
        return (access & Opcodes.ACC_STATIC) != 0;
    }

    /**
     * Returns true if a method name provided as parameter refers to a constructor.
     * @param name the name of the method.
     * @return true if the name refers to a constructor.
     */
    public static boolean isConstructor(String name) {
        return "<init>".equals(name);
    }

    /**
     * Returns a String representing a method with ASM fields provided.
     * @param name the name of the method.
     * @param desc the descriptor of the method.
     * @return A String representing a method with human friendly syntax.
     */
    public static String getMethodToString(String name, String desc) {
        Type returnType = Type.getReturnType(desc);
        Type[] parametersTypes = Type.getArgumentTypes(desc);
        String parameters = Arrays.stream(parametersTypes)
                .map(Type::getClassName)
                .collect(Collectors.joining(", "));

        return returnType.getClassName() + " " + name + "(" + parameters + ");";
    }
}
