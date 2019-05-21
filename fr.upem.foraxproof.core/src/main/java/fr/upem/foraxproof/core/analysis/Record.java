package fr.upem.foraxproof.core.analysis;

import java.util.Objects;

/**
 * A Record is an Object that allow to report an information during an analysis.
 *
 * @author Vincent Rasquier
 * @author Alex Pliez
 *
 * @since RELEASE 1.0
 */
public final class Record {
    private final Level level;
    private final Location location;
    private final String rule;
    private final String msg;

    private Record(Level level, Location location, String rule, String msg) {
        this.level = Objects.requireNonNull(level, "Level shouldn't be null");
        this.location = Objects.requireNonNull(location, "Location shouldn't be null");
        this.rule = Objects.requireNonNull(rule, "Rule shouldn't be null");
        this.msg = Objects.requireNonNull(msg, "Msg shouldn't be null");
    }

    /**
     * Create a Record of level Error.
     * @param location location of the record
     * @param rule rule name that report the record
     * @param msg a brief description of the issue or information
     * @return a new Record
     */
    public static Record error(Location location, String rule, String msg) {
        return new Record(Level.ERROR, location, rule, msg);
    }

    /**
     * Create a Record of level Warning.
     * @param location location of the record
     * @param rule rule name that report the record
     * @param msg a brief description of the issue or information
     * @return a new Record
     */
    public static Record warning(Location location, String rule, String msg) {
        return new Record(Level.WARNING, location, rule, msg);
    }

    /**
     * Create a Record of level Info.
     * @param location location of the record
     * @param rule rule name that report the record
     * @param msg a brief description of the issue or information
     * @return a new Record
     */
    public static Record info(Location location, String rule, String msg) {
        return new Record(Level.INFO, location, rule, msg);
    }

    /**
     * Get the level of the record
     * @return the level of the record
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Get the location of the record
     * @return the location of the record
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Get the rule name of the record
     * @return the rule of the record
     */
    public String getRule() {
        return rule;
    }

    /**
     * Get the msg of the record
     * @return the msg of the record
     */
    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return location.getSource() +  ": [" + level.toString() + "] " + rule + " rule : " + msg;
    }
}
