package fr.upem.foraxproof.core.handler;

import fr.upem.foraxproof.core.analysis.JavaType;
import fr.upem.foraxproof.core.analysis.Location;
import fr.upem.foraxproof.core.analysis.Record;
import fr.upem.foraxproof.core.event.EventManager;
import fr.upem.foraxproof.core.event.app.AddRecordEvent;
import fr.upem.foraxproof.core.event.asm.VisitAnnotationEvent;
import fr.upem.foraxproof.core.event.asm.VisitEndEvent;
import fr.upem.foraxproof.core.exporter.Exporter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddRecordHandlerTest {
    @Test
    void testShouldNotExportDirectly() {
        Location location = fakeLocation();
        Record r = Record.error(location, "Test", "Test");
        int[] count = {0};
        AddRecordHandler handler = fakeHandler(count);
        EventManager manager = new EventManager(Stream.of(handler));
        manager.dispatch(new AddRecordEvent(r));
        assertEquals(0, count[0]);
    }

    @Test
    void testExporter() {
        Location location = fakeLocation();
        Record r = Record.error(location, "Test", "Test");
        AddRecordHandler handler = new AddRecordHandler(fakeExporter(r));
        EventManager manager = new EventManager(Stream.of(handler));
        manager.dispatch(new AddRecordEvent(r));
        manager.dispatch(new VisitEndEvent(location));
    }

    @Test
    void testForaxProof() {
        Location location = fakeLocation();
        int[] count = {0};
        AddRecordHandler handler = fakeHandler(count);
        EventManager manager = new EventManager(Stream.of(handler));
        manager.dispatch(new AddRecordEvent(Record.error(location, "Test", "Test")));
        manager.dispatch(new VisitAnnotationEvent("Lfr/upem/foraxproof/core/ForaxProof;", true, JavaType.CLASS, location));
        manager.dispatch(new VisitEndEvent(location));
        assertEquals(0, count[0]);
    }

    private Location fakeLocation() {
        return new Location.Builder()
                .setType(JavaType.CLASS)
                .setSource("Test")
                .toLocation();
    }

    private Exporter fakeExporter(Record r) {
        return new Exporter() {
            @Override
            public void insertRecord(Record record) {
                assertEquals(record, r);
            }
            @Override
            public void close() throws IOException {}
        };
    }

    private AddRecordHandler fakeHandler(int[] count) {
        return new AddRecordHandler(new Exporter() {
            @Override
            public void insertRecord(Record record) {
                count[0]++;
            }
            @Override
            public void close() throws IOException {}
        });
    }
}
