package fr.upem.foraxproof.core.analysis;

/**
 * A Level represents the severity level of a Record. Permits
 * to know if actions needs to be performed immediately or soon.
 *
 * @author Vincent Rasquier
 * @author Alex Pliez
 *
 * @since RELEASE 1.0
 */
public enum Level {
    /**
     * Actions must be taken to correct the record.
     */
    ERROR,
    /**
     * Warning, you need to check the record to correct it or be sure
     * that it's normal in your case.
     */
    WARNING,
    /**
     * Some informational records. For example, if your rule
     * detects some deprecated methods in a next version of Java.
     */
    INFO
}
