package fr.upem.foraxproof.core.handler;

import fr.upem.foraxproof.core.analysis.Location;
import fr.upem.foraxproof.core.analysis.Record;
import fr.upem.foraxproof.core.event.EventDispatcher;
import fr.upem.foraxproof.core.event.EventSubscriber;
import fr.upem.foraxproof.core.event.Registrable;
import fr.upem.foraxproof.core.event.app.AddRecordEvent;
import fr.upem.foraxproof.core.event.app.EndEvent;
import fr.upem.foraxproof.core.event.asm.VisitAnnotationEvent;
import fr.upem.foraxproof.core.event.asm.VisitEndEvent;
import fr.upem.foraxproof.core.exporter.Exporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * AddRecordHandler is a registrable that take care to add specific record to the appropriate exporter.
 */
public final class AddRecordHandler implements Registrable {
    private final Exporter exporter;
    private final ThreadLocal<ArrayList<Record>> records;
    private final ThreadLocal<HashSet<Location>> annotations;

    /**
     * Constructs a new AddRecordHandler with the specified exporter.
     * @param exporter the exporter to use to export records.
     */
    public AddRecordHandler(Exporter exporter) {
        this.exporter = exporter;
        this.records = ThreadLocal.withInitial(ArrayList::new);
        this.annotations = ThreadLocal.withInitial(HashSet::new);
    }

    @Override
    public void register(EventSubscriber subscriber) {
        subscriber.subscribe(VisitAnnotationEvent.class, this::onVisitAnnotation);
        subscriber.subscribe(AddRecordEvent.class, this::onAddRecord);
        subscriber.subscribe(VisitEndEvent.class, this::onVisitEnd);
        subscriber.subscribe(EndEvent.class, this::onEnd);
    }

    private void onVisitAnnotation(VisitAnnotationEvent event, EventDispatcher dispatcher) {
        if (event.getDesc().equals("Lfr/upem/foraxproof/core/ForaxProof;")) {
            annotations.get().add(event.getLocation());
        }
    }

    private void onAddRecord(AddRecordEvent event, EventDispatcher dispatcher) {
        records.get().add(event.getRecord());
    }

    private void onVisitEnd(VisitEndEvent event, EventDispatcher dispatcher) {
        synchronized (annotations) {
            HashSet<Location> locations = annotations.get();
            records.get().stream()
                    .filter(record -> !locations.contains(record.getLocation()))
                    .forEach(exporter::insertRecord);
            annotations.remove();
            records.remove();
        }
    }

    private void onEnd(EndEvent event, EventDispatcher dispatcher) {
        try {
            synchronized (annotations) {
                exporter.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Add Record Event Handler";
    }
}
