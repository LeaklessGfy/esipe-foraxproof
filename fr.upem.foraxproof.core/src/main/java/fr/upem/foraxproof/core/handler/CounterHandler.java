package fr.upem.foraxproof.core.handler;

import fr.upem.foraxproof.core.event.EventDispatcher;
import fr.upem.foraxproof.core.event.EventSubscriber;
import fr.upem.foraxproof.core.event.Registrable;
import fr.upem.foraxproof.core.event.app.AddRecordEvent;
import fr.upem.foraxproof.core.event.app.EndEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CounterHandler is a registrable that take care to give the number of records.
 */
public class CounterHandler implements Registrable {
    private final AtomicInteger counter = new AtomicInteger();
    private final ConcurrentHashMap<String, Integer> m = new ConcurrentHashMap<>();

    @Override
    public void register(EventSubscriber subscriber) {
        subscriber.subscribe(AddRecordEvent.class, this::onAddRecord);
        subscriber.subscribe(EndEvent.class, this::onEnd);
    }

    private void onAddRecord(AddRecordEvent event, EventDispatcher dispatcher) {
        counter.incrementAndGet();
        m.compute(event.getRecord().getRule(), (k, v) -> v == null ? 1 : v + 1);
    }

    private void onEnd(EndEvent event, EventDispatcher dispatcher) {
        System.out.println("Number of records : " + counter.get());
        m.entrySet().forEach(System.out::println);
    }

    /**
     * Returns the current counter that corresponds to the number of records.
     * @return the current counter.
     */
    public int getCounter() {
        return counter.get();
    }

    @Override
    public String toString() {
        return "Counter Handler";
    }
}
