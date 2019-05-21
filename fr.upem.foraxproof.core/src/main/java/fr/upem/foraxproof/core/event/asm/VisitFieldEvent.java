package fr.upem.foraxproof.core.event.asm;

import fr.upem.foraxproof.core.analysis.Location;

/**
 * VisitFieldEvent occur when visitor visit field
 */
public class VisitFieldEvent {
    private final int access;
    private final String name;
    private final String desc;
    private final String signature;
    private final Object value;
    private final Location location;

    /**
     * VisitFieldEvent constructor.
     * @param access the field access flags (see org.objectweb.asm.Opcodes to have more information).
     * @param name the field name.
     * @param desc the field descriptor (see org.objectweb.asm.Type to have more information).
     * @param signature the field signature. Can be null if the field does not use generic types.
     * @param value the field initial value.
     * @param location the location of the field.
     */
    public VisitFieldEvent(int access, String name, String desc, String signature, Object value, Location location) {
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.value = value;
        this.location = location;
    }

    /**
     * Returns the access of this field.
     * @return the access of this field.
     */
    public int getAccess() {
        return access;
    }

    /**
     * Returns the name of this field.
     * @return the name of this field.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the descriptor of this field.
     * @return the descriptor of this field.
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Returns the signature of this field.
     * @return the signature of this field or null if the field does not use generic types.
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Returns the value of this field.
     * @return the value of this field.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Returns the location of this field.
     * @return the location of this field.
     */
    public Location getLocation() {
        return location;
    }
}
