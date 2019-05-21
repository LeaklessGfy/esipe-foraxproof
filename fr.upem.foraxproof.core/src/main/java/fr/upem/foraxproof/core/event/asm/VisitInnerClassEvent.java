package fr.upem.foraxproof.core.event.asm;

import fr.upem.foraxproof.core.analysis.Location;

/**
 * VisitInnerClassEvent occur when visitor visit inner class
 */
public class VisitInnerClassEvent {
    private final String name;
    private final String outer;
    private final String innerName;
    private final int access;
    private final Location location;

    /**
     * VisitInnerClassEvent constructor
     * @param name the internal name of an inner class.
     * @param outer the internal name of the class to which the inner class belongs.
     * @param innerName the simple name of the inner class inside its enclosing class. Can be null for anonymous inner classes.
     * @param access the access flags of the inner class (see org.objectweb.asm.Opcodes to have more information).
     * @param location the location of the inner class.
     */
    public VisitInnerClassEvent(String name, String outer, String innerName, int access, Location location) {
        this.name = name;
        this.outer = outer;
        this.innerName = innerName;
        this.access = access;
        this.location = location;
    }

    /**
     * Returns the name of the inner class.
     * @return the name of the inner class.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the name of the class to which the inner class belongs.
     * @return the name of the outer class.
     */
    public String getOuter() {
        return outer;
    }

    /**
     * Returns the simple name of the inner class inside its enclosing class.
     * @return the simple name of the inner class.
     */
    public String getInnerName() {
        return innerName;
    }

    /**
     * Returns the access flags of the inner class.
     * @return the access flags of the inner class.
     */
    public int getAccess() {
        return access;
    }

    /**
     * Returns the location of the inner class.
     * @return the location of the inner class.
     */
    public Location getLocation() {
        return location;
    }
}
