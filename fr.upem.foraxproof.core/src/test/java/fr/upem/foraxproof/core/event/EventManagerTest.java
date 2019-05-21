package fr.upem.foraxproof.core.event;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventManagerTest {
    @Test
    void testSubscribe() {
        Registrable test = subscriber -> subscriber.subscribe(Object.class, (e, d) -> {});
        EventManager subscriber = new EventManager(Stream.of(test));
        assertEquals(true, subscriber.hasListener(Object.class));
    }
    @Test
    void testDispatch() {
        Object o = new Object();
        Registrable test = subscriber -> subscriber.subscribe(Object.class, (e, d) -> assertEquals(o, e));
        EventManager dispatcher = new EventManager(Stream.of(test));
        dispatcher.dispatch(o);
    }
}
