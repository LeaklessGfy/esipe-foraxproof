package fr.upem.foraxproof.impl;

import fr.upem.foraxproof.core.ASMUtils;
import fr.upem.foraxproof.core.analysis.Record;
import fr.upem.foraxproof.core.event.EventDispatcher;
import fr.upem.foraxproof.core.event.EventSubscriber;
import fr.upem.foraxproof.core.event.Registrable;
import fr.upem.foraxproof.core.event.app.AddRecordEvent;
import fr.upem.foraxproof.core.event.asm.VisitFieldEvent;
import fr.upem.foraxproof.core.Rule;
import fr.upem.foraxproof.core.event.asm.VisitMethodEvent;

/**
 * A Protected rule is a rule that checks if there is
 * no protected field in a class.
 * If it's not the case, it adds a Warning record.
 */
@Rule("Protected")
public class ProtectedRule implements Registrable {
    @Override
    public void register(EventSubscriber subscriber) {
        subscriber.subscribe(VisitFieldEvent.class, this::onVisitField);
        subscriber.subscribe(VisitMethodEvent.class, this::onVisitMethod);
    }

    private void onVisitField(VisitFieldEvent event, EventDispatcher dispatcher) {
        if (ASMUtils.isProtected(event.getAccess())) {
            String msg = event.getName() + " field shouldn't use protected visibility";
            dispatcher.dispatch(new AddRecordEvent(Record.warning(event.getLocation(), "Protected", msg)));
        }
    }
    private void onVisitMethod(VisitMethodEvent event, EventDispatcher dispatcher) {
        if (ASMUtils.isProtected(event.getAccess())) {
            String msg = ASMUtils.getMethodToString(event.getName(), event.getDesc()) + " method shouldn't use protected visibility";
            dispatcher.dispatch(new AddRecordEvent(Record.warning(event.getLocation(), "Protected", msg)));
        }
    }

    @Override
    public String toString() {
        return "Protected Rule";
    }
}
