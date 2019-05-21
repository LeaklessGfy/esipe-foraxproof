package fr.upem.foraxproof.core.handler;

import fr.upem.foraxproof.core.analysis.JavaType;
import fr.upem.foraxproof.core.analysis.Location;
import fr.upem.foraxproof.core.analysis.Record;
import fr.upem.foraxproof.core.event.EventManager;
import fr.upem.foraxproof.core.event.app.AddRecordEvent;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CounterHandlerTest {
    @Test
    void test() {
        CounterHandler counter = new CounterHandler();
        EventManager manager = new EventManager(Stream.of(counter));
        Location location = fakeLocation();
        manager.dispatch(new AddRecordEvent(Record.error(location, "Test", "Test")));
        manager.dispatch(new AddRecordEvent(Record.error(location, "Test", "Test")));
        manager.dispatch(new AddRecordEvent(Record.error(location, "Test", "Test")));
        assertEquals(3, counter.getCounter());
    }

    private Location fakeLocation() {
        return new Location.Builder()
                .setType(JavaType.CLASS)
                .setSource("Test")
                .toLocation();
    }
}
