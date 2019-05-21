package fr.upem.foraxproof.core.handler;

import fr.upem.foraxproof.core.event.EventSubscriber;
import fr.upem.foraxproof.core.event.Registrable;
import fr.upem.foraxproof.core.event.app.EndEvent;

/**
 * TimeHandler is a registrable that take care to give a simple estimation of the time that take the analysis.
 */
public final class TimeHandler implements Registrable {
    private final long start = System.currentTimeMillis();

    @Override
    public void register(EventSubscriber subscriber) {
        subscriber.subscribe(EndEvent.class, (event, dispatcher) -> System.out.println("Estimate duration : " + (System.currentTimeMillis() - start) + "ms"));
    }

    @Override
    public String toString() {
        return "Time Handler";
    }
}
