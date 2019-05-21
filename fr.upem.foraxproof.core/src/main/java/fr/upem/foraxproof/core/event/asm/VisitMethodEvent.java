package fr.upem.foraxproof.core.event.asm;

import fr.upem.foraxproof.core.analysis.Location;

/**
 * VisitMethodEvent occur when visitor visit a method
 */
public class VisitMethodEvent {
    private final int access;
    private final String name;
    private final String desc;
    private final String signature;
    private final String[] exceptions;
    private final Location location;

    /**
     * VisitMethodEvent constructor.
     * @param access the method access flags (see org.objectweb.asm.Opcodes to have more information).
     * @param name the method name.
     * @param desc the method descriptor (see org.objectweb.asm.Type to have more information).
     * @param signature the method signature. Can be null if the method parameters, return type and exceptions do not use generic types.
     * @param exceptions the exceptions threw by the method.
     * @param location the method location.
     */
    public VisitMethodEvent(int access, String name, String desc, String signature, String[] exceptions, Location location) {
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.exceptions = exceptions;
        this.location = location;
    }

    /**
     * Returns the access of this method.
     * @return the access of this method.
     */
    public int getAccess() {
        return access;
    }

    /**
     * Returns the name of this method.
     * @return the name of this method.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the descriptor of this method.
     * @return the descriptor of this method.
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Returns the signature of this method.
     * @return the signature of this method or null if the method parameters, return type and exceptions do not use generic types.
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Returns the exceptions threw by the method.
     * @return the exceptions threw by the method. Can be null.
     */
    public String[] getExceptions() {
        return exceptions;
    }

    /**
     * Returns the method location.
     * @return the method location.
     */
    public Location getLocation() {
        return location;
    }
}
