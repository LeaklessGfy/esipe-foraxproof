package fr.upem.foraxproof.core.event.app;

import org.objectweb.asm.ClassVisitor;

import java.util.Objects;

/**
 * AddAdditionalEvent asks for an additional analysis
 */
public class AddAdditionalEvent {
    private final String clazz;
    private final ClassVisitor visitor;
    private final int flags;

    /**
     * AddAdditionalEvent constructor.
     * @param clazz classname to analysis
     * @param visitor dedicate visitor to use for this additional analysis
     * @param flags flags to put for this analysis
     */
    public AddAdditionalEvent(String clazz, ClassVisitor visitor, int flags) {
        this.clazz = Objects.requireNonNull(clazz, "Clazz shouldn't be null");
        this.visitor = Objects.requireNonNull(visitor, "Visitor shouldn't be null");
        this.flags = flags;
    }

    /**
     * Get classname
     * @return classname
     */
    public String getClazz() {
        return clazz;
    }

    /**
     * Get visitor
     * @return visitor
     */
    public ClassVisitor getVisitor() {
        return visitor;
    }

    /**
     * Get flags
     * @return flags
     */
    public int getFlags() {
        return flags;
    }
}
