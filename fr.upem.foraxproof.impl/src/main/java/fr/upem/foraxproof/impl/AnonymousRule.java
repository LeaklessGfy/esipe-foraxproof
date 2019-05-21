package fr.upem.foraxproof.impl;

import fr.upem.foraxproof.core.ASMUtils;
import fr.upem.foraxproof.core.event.Registrable;
import fr.upem.foraxproof.core.visitor.CallbackVisitor;
import fr.upem.foraxproof.core.analysis.Record;
import fr.upem.foraxproof.core.event.EventDispatcher;
import fr.upem.foraxproof.core.event.EventSubscriber;
import fr.upem.foraxproof.core.event.app.AddAdditionalEvent;
import fr.upem.foraxproof.core.event.app.AddRecordEvent;
import fr.upem.foraxproof.core.event.asm.VisitEndEvent;
import fr.upem.foraxproof.core.event.asm.VisitEvent;
import fr.upem.foraxproof.core.event.asm.VisitMethodEvent;
import fr.upem.foraxproof.core.Rule;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.util.HashSet;

/**
 * An AnonymousRule is a rule that checks if a class that implements
 * only super methods is Anonymous or an inner class.
 * If it's not the case, it adds a record Error.
 */
@Rule("Anonymous")
public class AnonymousRule implements Registrable {
    private final ThreadLocal<HashSet<String>> superMethods = ThreadLocal.withInitial(HashSet::new);
    private final ThreadLocal<Boolean> err = ThreadLocal.withInitial(() -> true);
    private final ThreadLocal<Integer> nb = ThreadLocal.withInitial(() -> 0);

    @Override
    public void register(EventSubscriber subscriber) {
        subscriber.subscribe(VisitEvent.class, this::onVisit);
        subscriber.subscribe(VisitMethodEvent.class, this::onVisitMethod);
        subscriber.subscribe(VisitEndEvent.class, this::onVisitEnd);
    }

    private void onVisit(VisitEvent event, EventDispatcher dispatcher) {
        if (!ASMUtils.isStatic(event.getAccess()) && !ASMUtils.isPublic(event.getAccess())) {
            err.set(false);
            return;
        }
        parseMethods(event.getSuperName(), dispatcher);
        for(String interfaze : event.getInterfaces()){
            parseMethods(interfaze, dispatcher);
        }
    }

    private void onVisitMethod(VisitMethodEvent event, EventDispatcher dispatcher) {
        if (!err.get()) {
            return;
        }
        if (!ASMUtils.isStatic(event.getAccess()) && !ASMUtils.isConstructor(event.getName())) {
            check(event);
        }
    }

    private void onVisitEnd(VisitEndEvent event, EventDispatcher dispatcher) {
        if (!err.get() || nb.get() == 0) {
            clean();
            return;
        }
        String msg = event.getLocation().getSource() + " class needs to be anonymous or inner because implements only super methods.";
        dispatcher.dispatch(new AddRecordEvent(Record.error(event.getLocation(), "Anonymous", msg)));
        clean();
    }

    private void clean() {
        err.remove();
        nb.remove();
        superMethods.remove();
    }

    private void check(VisitMethodEvent event) {
        nb.set(1);
        if (!superMethods.get().contains(event.getName() + event.getDesc())) {
            err.set(false);
        }
    }

    private void parseMethods(String className, EventDispatcher dispatcher) {
        if (className == null) {
            return;
        }
        ClassVisitor visitor = buildVisitor(className);
        dispatcher.dispatch(new AddAdditionalEvent(className, visitor, ClassReader.SKIP_FRAMES));
    }

    private ClassVisitor buildVisitor(String className) {
        return new CallbackVisitor.Builder().onVisitMethod(this::onParentVisitMethod)
                .onVisitInnerClass(e -> {
                    if (e.getName().equals(className)) {
                        err.set(false);
                    }
                }).toVisitor();
    }

    private void onParentVisitMethod(VisitMethodEvent e) {
        int access = e.getAccess();
        if ((ASMUtils.isPublic(access) || ASMUtils.isProtected(access)) && !ASMUtils.isStatic(access)) {
            superMethods.get().add(e.getName() + e.getDesc());
        }
    }

    @Override
    public String toString() {
        return "Anonymous Rule";
    }
}
