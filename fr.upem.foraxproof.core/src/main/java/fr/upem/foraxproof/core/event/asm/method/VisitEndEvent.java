package fr.upem.foraxproof.core.event.asm.method;

import fr.upem.foraxproof.core.analysis.Location;

/**
 * VisitEndEvent occur when visitor visit end of a class
 */
public class VisitEndEvent {
    private final Location location;

    /**
     * VisitEndEvent constructor.
     * @param location location of event
     */
    public VisitEndEvent(Location location) {
        this.location = location;
    }

    /**
     * Get the location of the event
     * @return location of the event
     */
    public Location getLocation() {
        return location;
    }
}
