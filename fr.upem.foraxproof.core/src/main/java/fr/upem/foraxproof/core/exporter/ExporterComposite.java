package fr.upem.foraxproof.core.exporter;

import fr.upem.foraxproof.core.analysis.Record;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

/**
 * An ExporterComposite is like a multi-exporter. It allows to export
 * datas with multiple exporters at the same time.
 */
public final class ExporterComposite implements Exporter {
    private final Collection<Exporter> exporters;

    /**
     * Constructs a new ExporterComposite with a collection of exporters.
     * @param exporters A collection containing exporters to use.
     */
    public ExporterComposite(Collection<Exporter> exporters) {
        this.exporters = Objects.requireNonNull(exporters);
    }

    @Override
    public void insertRecord(Record record) {
        for (Exporter exporter : exporters) {
            exporter.insertRecord(record);
        }
    }

    @Override
    public void close() throws IOException {
        for (Exporter exporter : exporters) {
            exporter.close();
        }
    }
}
