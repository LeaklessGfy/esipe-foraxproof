package fr.upem.foraxproof.core.exporter;

import java.util.Objects;

/**
 * It is a generic RuntimeException for exporters.
 */
public class UncheckedExporterException extends RuntimeException {
    /**
     * Constructs an instance of this class.
     *
     * @param   message
     *          the detail message, can be null
     * @param   cause
     *          the {@code Exception}
     *
     * @throws  NullPointerException
     *          if the cause is {@code null}
     */
    public UncheckedExporterException(String message, Exception cause) {
        super(message, Objects.requireNonNull(cause));
    }

    /**
     * Constructs an instance of this class.
     *
     * @param   cause
     *          the {@code Exception}
     *
     * @throws  NullPointerException
     *          if the cause is {@code null}
     */
    public UncheckedExporterException(Exception cause) {
        super(Objects.requireNonNull(cause));
    }

    @Override
    public Exception getCause() {
        return (Exception) super.getCause();
    }
}
