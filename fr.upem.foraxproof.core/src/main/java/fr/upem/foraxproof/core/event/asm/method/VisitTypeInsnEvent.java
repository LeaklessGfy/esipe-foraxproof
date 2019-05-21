package fr.upem.foraxproof.core.event.asm.method;

import fr.upem.foraxproof.core.analysis.Location;

/**
 * VisitTypeInsnEvent occurs when visitor visit a type insn in method
 */
public class VisitTypeInsnEvent {
    private final int opcode;
    private final String type;
    private final Location location;

    /**
     * VisitTypeInsnEvent constructor.
     * @param opcode the opcode of the type instruction to be visited. This opcode is either NEW, ANEWARRAY, CHECKCAST or INSTANCEOF.
     * @param type the operand of the instruction to be visited.
     * @param location the location of the instruction to be visited.
     */
    public VisitTypeInsnEvent(int opcode, String type, Location location) {
        this.opcode = opcode;
        this.type = type;
        this.location = location;
    }

    /**
     * Returns the opcode of the type instruction to be visited. This opcode is either NEW, ANEWARRAY, CHECKCAST or INSTANCEOF.
     * @return opcode
     */
    public int getOpcode() {
        return opcode;
    }

    /**
     * Get the operand of the instruction to be visited.
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Get the location of the instruction to be visited.
     * @return location
     */
    public Location getLocation() {
        return location;
    }
}
