package fr.upem.foraxproof.core.runner;

import fr.upem.foraxproof.core.analysis.Record;
import fr.upem.foraxproof.core.event.EventManager;
import fr.upem.foraxproof.core.event.Registrable;
import fr.upem.foraxproof.core.event.app.AddRecordEvent;
import fr.upem.foraxproof.core.event.asm.VisitEvent;
import fr.upem.foraxproof.core.handler.CounterHandler;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConcurrentRunnerTest {
    @Test
    void testShouldHaveOneRecord() throws IOException {
        CounterHandler handler = new CounterHandler();
        Registrable rule = fakeRegistrable();
        EventManager manager = new EventManager(Stream.of(handler, rule));
        ConcurrentRunner runner = new ConcurrentRunner(manager, 20);
        runner.run(List.of("../resources/ExampleClass.class"));
        assertEquals(1, handler.getCounter());
    }

    private Registrable fakeRegistrable() {
        return subscriber -> subscriber.subscribe(VisitEvent.class, (e, d) ->
                d.dispatch(new AddRecordEvent(Record.error(e.getLocation(), "Test", "Test")))
        );
    }
}
