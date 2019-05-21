package fr.upem.foraxproof.core.exporter;

import fr.upem.foraxproof.core.analysis.Record;

import java.io.IOException;

/**
 * A PrintExporter is an Exporter that prints all records on
 * the standard output.
 */
public final class PrintExporter implements Exporter {
    @Override
    public void insertRecord(Record record) {
        System.out.println(record);
    }

    @Override
    public void close() throws IOException {}
}
