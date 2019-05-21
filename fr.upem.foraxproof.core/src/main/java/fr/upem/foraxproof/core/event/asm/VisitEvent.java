package fr.upem.foraxproof.core.event.asm;

import fr.upem.foraxproof.core.analysis.Location;

/**
 * VisitEvent occur when visitor visit class
 */
public class VisitEvent {
    private final int version;
    private final int access;
    private final String name;
    private final String signature;
    private final String superName;
    private final String[] interfaces;
    private final Location location;

    /**
     * VisitEvent constructor.
     * @param version the class version.
     * @param access the class access flags (see org.objectweb.asm.Opcodes to have more information).
     * @param name the internal name of the class.
     * @param signature the signature of the class. Can be null if the class is not generic, and does not extend or implement generic classes or interfaces.
     * @param superName the internal name of the superclass.
     * @param interfaces  the internal names of the class interfaces. Can be null.
     * @param location the location of the class.
     */
    public VisitEvent(int version, int access, String name, String signature, String superName, String[] interfaces, Location location) {
        this.version = version;
        this.access = access;
        this.name = name;
        this.signature = signature;
        this.superName = superName;
        this.interfaces = interfaces;
        this.location = location;
    }

    /**
     * Get the class version.
     * @return version
     */
    public int getVersion() {
        return version;
    }

    /**
     * Get the class access flags (see org.objectweb.asm.Opcodes to have more information).
     * @return access
     */
    public int getAccess() {
        return access;
    }

    /**
     * Get the internal name of the class.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the signature of the class. Can be null if the class is not generic, and does not extend or implement generic classes or interfaces.
     * @return signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Get the superName the internal name of the superclass.
     * @return superName
     */
    public String getSuperName() {
        return superName;
    }

    /**
     * Get the internal names of the class interfaces. Can be null.
     * @return interfaces
     */
    public String[] getInterfaces() {
        return interfaces;
    }

    /**
     * Get the location of the class.
     * @return location
     */
    public Location getLocation() {
        return location;
    }
}
