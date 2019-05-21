package fr.upem.foraxproof.impl;

import fr.upem.foraxproof.core.ASMUtils;
import fr.upem.foraxproof.core.analysis.Location;
import fr.upem.foraxproof.core.analysis.Record;
import fr.upem.foraxproof.core.event.EventDispatcher;
import fr.upem.foraxproof.core.event.EventSubscriber;
import fr.upem.foraxproof.core.event.Registrable;
import fr.upem.foraxproof.core.event.app.AddRecordEvent;
import fr.upem.foraxproof.core.Rule;
import fr.upem.foraxproof.core.event.asm.method.VisitTypeInsnEvent;

import java.util.Optional;

import static org.objectweb.asm.Opcodes.INSTANCEOF;

/**
 * An InstanceOf rule is a rule that checks if the instanceof
 * instruction is only used in equals(Object) method.
 * If it's not the case, it adds an Error record.
 */
@Rule("Instanceof")
public class InstanceOfRule implements Registrable {
    @Override
    public void register(EventSubscriber subscriber) {
        subscriber.subscribe(VisitTypeInsnEvent.class, this::onVisitTypeInsn);
    }

    private void onVisitTypeInsn(VisitTypeInsnEvent event, EventDispatcher dispatcher) {
        Location location = event.getLocation();
        Optional<Location.Method> optional = location.getMethod();
        if (!optional.isPresent()) {
            return;
        }
        check(event, optional.get(), location, dispatcher);
    }

    private void check(VisitTypeInsnEvent event, Location.Method method, Location location, EventDispatcher dispatcher) {
        if (event.getOpcode() == INSTANCEOF && !isObjectEqualsMethod(method.getName(), method.getDesc())) {
            String msg = "instanceof instruction at line " + location.getLine() + " in "+ ASMUtils.getMethodToString(method.getName(), method.getDesc());
            dispatcher.dispatch(new AddRecordEvent(Record.error(location, "Instanceof", msg)));
        }
    }

    private static boolean isObjectEqualsMethod(String name, String desc) {
        return "equals".equals(name) && "(Ljava/lang/Object;)Z".equals(desc);
    }

    @Override
    public String toString() {
        return "InstanceOf Rule";
    }
}
