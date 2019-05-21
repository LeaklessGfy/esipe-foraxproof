package fr.upem.foraxproof.core.event.asm.method;

import fr.upem.foraxproof.core.analysis.Location;
import org.objectweb.asm.Label;

/**
 * VisitLineNumberEvent occur when visitor visit line number
 */
public class VisitLineNumberEvent {
    private final int line;
    private final Label start;
    private final Location location;

    /**
     * VisitLineNumberEvent constructor.
     * @param line the line number in the source file from which the class was compiled.
     * @param start start the first instruction corresponding to the line number.
     * @param location location the location to locate the class visited.
     */
    public VisitLineNumberEvent(int line, Label start, Location location) {
        this.line = line;
        this.start = start;
        this.location = location;
    }

    /**
     * Returns the line of the instruction in the source file.
     * @return the line of the instruction.
     */
    public int getLine() {
        return line;
    }

    /**
     * Returns the first instruction corresponding to the line number.
     * @return start
     */
    public Label getStart() {
        return start;
    }

    /**
     * Get the location of the class visited.
     * @return the location of the class visited.
     */
    public Location getLocation() {
        return location;
    }
}
