package fr.upem.foraxproof.core.event.asm;

import fr.upem.foraxproof.core.analysis.Location;
import fr.upem.foraxproof.core.analysis.JavaType;

/**
 * VisitAnnotationEvent occur when a visitor visit an annotation
 */
public class VisitAnnotationEvent {
    private final String desc;
    private final boolean visible;
    private final JavaType type;
    private final Location location;

    /**
     * VisitAnnotationEvent constructor.
     * @param desc desc of annotation
     * @param visible visible of annotation
     * @param type type of annotation
     * @param location location where annotation occur
     */
    public VisitAnnotationEvent(String desc, boolean visible, JavaType type, Location location) {
        this.desc = desc;
        this.visible = visible;
        this.type = type;
        this.location = location;
    }

    /**
     * Get desc
     * @return desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Get visible
     * @return visible
     */
    public boolean getVisible() {
        return visible;
    }

    /**
     * Get type
     * @return type
     */
    public JavaType getType() {
        return type;
    }

    /**
     * Get location
     * @return location
     */
    public Location getLocation() {
        return location;
    }
}
