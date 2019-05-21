package fr.upem.foraxproof.impl;

import fr.upem.foraxproof.core.event.EventSubscriber;
import fr.upem.foraxproof.core.event.Registrable;
import fr.upem.foraxproof.core.event.app.AddRecordEvent;

public class TestHandler implements Registrable {
    private int errors;

    @Override
    public void register(EventSubscriber subscriber) {
        subscriber.subscribe(AddRecordEvent.class, (e, d) -> errors++);
    }

    int getErrors() {
        return errors;
    }
}
