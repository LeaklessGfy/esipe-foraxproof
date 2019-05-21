package fr.upem.foraxproof.core.event.asm;

import fr.upem.foraxproof.core.analysis.Location;

/**
 * VisitOuterClassEvent occur when visitor visit outer class
 */
public class VisitOuterClassEvent {
    private final String owner;
    private final String name;
    private final String desc;
    private final Location location;

    /**
     * VisitOuterClassEvent constructor.
     * @param owner the name of the enclosing class.
     * @param name the name of the method that contains the class.
     * @param desc the descriptors of the method that contains the class.
     * @param location the location of the class.
     */
    public VisitOuterClassEvent(String owner, String name, String desc, Location location) {
        this.owner = owner;
        this.name = name;
        this.desc = desc;
        this.location = location;
    }

    /**
     * Get the name of the enclosing class.
     * @return the name of the enclosing class.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Get the name of the method that contains the class.
     * @return the name of the method that contains the class. Can be null if the class is not enclosed in a method of its enclosing class.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the descriptors of the method that contains the class.
     * @return the descriptors of the method that contains the class. Can be null if the class is not enclosed in a method of its enclosing class.
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Get the location of the class.
     * @return the location of the class.
     */
    public Location getLocation() {
        return location;
    }
}
