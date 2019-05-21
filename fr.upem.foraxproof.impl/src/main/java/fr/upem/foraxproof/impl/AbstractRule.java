package fr.upem.foraxproof.impl;

import fr.upem.foraxproof.core.ASMUtils;
import fr.upem.foraxproof.core.analysis.Record;
import fr.upem.foraxproof.core.event.EventDispatcher;
import fr.upem.foraxproof.core.event.EventSubscriber;
import fr.upem.foraxproof.core.event.Registrable;
import fr.upem.foraxproof.core.event.app.AddRecordEvent;
import fr.upem.foraxproof.core.Rule;
import fr.upem.foraxproof.core.event.asm.VisitEvent;

/**
 * An AbstractRule is a rule that checks if an Abstract Class is
 * public and return a warning record if it's the case.
 */
@Rule("Abstract")
public class AbstractRule implements Registrable {
    @Override
    public void register(EventSubscriber subscriber) {
        subscriber.subscribe(VisitEvent.class, this::onVisit);
    }

    private void onVisit(VisitEvent event, EventDispatcher dispatcher) {
        int access = event.getAccess();

        if (ASMUtils.isAbstract(access) && ASMUtils.isPublic(access) && !ASMUtils.isInterface(access)) {
            String msg = event.getName() + " class shouldn't be public";
            dispatcher.dispatch(new AddRecordEvent(Record.warning(event.getLocation(), "Abstract", msg)));
        }
    }

    @Override
    public String toString() {
        return "Abstract Rule";
    }
}
