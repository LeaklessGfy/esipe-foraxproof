package fr.upem.foraxproof.impl;

import fr.upem.foraxproof.core.ASMUtils;
import fr.upem.foraxproof.core.analysis.Location;
import fr.upem.foraxproof.core.analysis.Record;
import fr.upem.foraxproof.core.event.EventDispatcher;
import fr.upem.foraxproof.core.event.EventSubscriber;
import fr.upem.foraxproof.core.event.Registrable;
import fr.upem.foraxproof.core.event.app.AddRecordEvent;
import fr.upem.foraxproof.core.event.asm.method.VisitEndEvent;
import fr.upem.foraxproof.core.event.asm.method.VisitLineNumberEvent;
import fr.upem.foraxproof.core.Rule;

import java.util.HashMap;
import java.util.Optional;

/**
 * A Length rule is a rule that checks if the length
 * of a method is 8 lines or less.
 * If it's not the case, it adds an Error record.
 */
@Rule("Length")
public class LengthRule implements Registrable {
    private final ThreadLocal<HashMap<String, Integer[]>> lines = ThreadLocal.withInitial(HashMap::new);

    @Override
    public void register(EventSubscriber subscriber) {
        subscriber.subscribe(VisitLineNumberEvent.class, this::onVisitLineNumber);
        subscriber.subscribe(VisitEndEvent.class, this::onVisitMethodEndEvent);
    }

    private void onVisitLineNumber(VisitLineNumberEvent event, EventDispatcher dispatcher) {
        Location location = event.getLocation();
        Optional<Location.Method> optional = location.getMethod();
        if (!optional.isPresent()) {
            return;
        }
        populate(optional.get(), event.getLine());
    }

    private void onVisitMethodEndEvent(VisitEndEvent event, EventDispatcher dispatcher) {
        Location location = event.getLocation();
        Optional<Location.Method> optional = location.getMethod();
        if (!optional.isPresent()) {
            return;
        }
        finalCheck(optional.get(), location, dispatcher);
        lines.remove();
    }

    private void populate(Location.Method method, int line) {
        String key = method.getName() + method.getSignature() + method.getDesc();
        lines.get().compute(key, (k, tab) -> {
            if (tab == null) {
                return new Integer[]{line, line + 1};
            }
            tab[1] = line;
            return tab;
        });
    }

    private void finalCheck(Location.Method method, Location location, EventDispatcher dispatcher) {
        String key = method.getName() + method.getSignature() + method.getDesc();
        Integer[] range = lines.get().getOrDefault(key, new Integer[]{0, 1});
        int length = range[1] - range[0];
        if (length > 8) {
            String name = ASMUtils.getMethodToString(method.getName(), method.getDesc());
            String msg = "method " + name + " is too big (" + length + " lines)";
            dispatcher.dispatch(new AddRecordEvent(Record.error(location, "Length", msg)));
        }
    }

    @Override
    public String toString() {
        return "Length Rule";
    }
}
