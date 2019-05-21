package fr.upem.foraxproof.core.exporter;

import fr.upem.foraxproof.core.analysis.Record;

import java.io.Closeable;

/**
 * Exporter describe how an exporter of record should work.
 */
public interface Exporter extends Closeable {
    /**
     * Add a record into the exporter structure
     * @param record record to insert
     */
    void insertRecord(Record record);
}
