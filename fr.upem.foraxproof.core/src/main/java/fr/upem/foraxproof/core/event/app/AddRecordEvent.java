package fr.upem.foraxproof.core.event.app;

import fr.upem.foraxproof.core.analysis.Record;

import java.util.Objects;

/**
 * AddRecordEvent ask to add a record inside the exporter
 */
public class AddRecordEvent {
    private final Record record;

    /**
     * AddRecordEvent constructor.
     * @param record record to add
     */
    public AddRecordEvent(Record record) {
        this.record = Objects.requireNonNull(record);
    }

    /**
     * Get the record
     * @return record
     */
    public Record getRecord() {
        return record;
    }
}
