package fr.upem.foraxproof.core.analysis;

/**
 * A JavaType represents the type of a record. It is used
 * by a Location to give information about the granularity
 * of a record.
 *
 * @author Vincent Rasquier
 * @author Alex Pliez
 *
 * @since RELEASE 1.0
 */
public enum JavaType {
    CLASS, FIELD, METHOD, INSTRUCTION
}
