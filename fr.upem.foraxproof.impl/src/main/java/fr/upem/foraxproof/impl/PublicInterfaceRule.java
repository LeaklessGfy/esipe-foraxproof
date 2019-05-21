package fr.upem.foraxproof.impl;

import fr.upem.foraxproof.core.ASMUtils;
import fr.upem.foraxproof.core.event.Registrable;
import fr.upem.foraxproof.core.event.asm.VisitEvent;
import fr.upem.foraxproof.core.visitor.CallbackVisitor;
import fr.upem.foraxproof.core.analysis.Record;
import fr.upem.foraxproof.core.event.EventDispatcher;
import fr.upem.foraxproof.core.event.EventSubscriber;
import fr.upem.foraxproof.core.event.app.AddAdditionalEvent;
import fr.upem.foraxproof.core.event.app.AddRecordEvent;
import fr.upem.foraxproof.core.event.asm.VisitMethodEvent;
import fr.upem.foraxproof.core.Rule;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

import java.util.Set;

/**
 * An PublicInterface rule is a rule that checks if public methods
 * returns an implementation of a collection instead of an interface.
 * If a public method returns an implementation, it adds a record error.
 */
@Rule("PublicInterface")
public class PublicInterfaceRule implements Registrable {
    private final Set<String> extendsList = Set.of(
            "java/util/Map",
            "java/util/List",
            "java/util/Set",
            "java/util/HashMap",
            "java/util/ArrayList",
            "java/util/LinkedList",
            "java/util/HashSet"
    );

    @Override
    public void register(EventSubscriber subscriber) {
        subscriber.subscribe(VisitMethodEvent.class, this::onVisitMethod);
    }

    private void onVisitMethod(VisitMethodEvent event, EventDispatcher dispatcher) {
        if (!ASMUtils.isPublic(event.getAccess())) {
            return;
        }
        runOnType(Type.getReturnType(event.getDesc()), dispatcher, event);
        for (Type type : Type.getArgumentTypes(event.getDesc())) {
            runOnType(type, dispatcher, event);
        }
    }

    private void runOnType(Type type, EventDispatcher dispatcher, VisitMethodEvent event) {
        if (type.getSort() != Type.OBJECT) {
            return;
        }
        String name = type.getInternalName();
        ClassVisitor visitor = buildVisitor(name, event, dispatcher, true);
        dispatcher.dispatch(new AddAdditionalEvent(name, visitor, ClassReader.SKIP_CODE));
    }

    private ClassVisitor buildVisitor(String init, VisitMethodEvent event, EventDispatcher dispatcher, boolean root) {
        return new CallbackVisitor.Builder().onVisit(e -> {
                    if (shouldReturn(e, root, dispatcher, event, init)) {
                        return;
                    }
                    makeVisit(e.getSuperName(), init, event, dispatcher);
                    for (String interfaze : e.getInterfaces()) {
                        makeVisit(interfaze, init, event, dispatcher);
                    }
                }).toVisitor();
    }

    private boolean shouldReturn(VisitEvent e, boolean root, EventDispatcher dispatcher, VisitMethodEvent event, String init) {
        if (ASMUtils.isInterface(e.getAccess()) && root) {
            return true;
        }
        if (extendsList.contains(e.getName()) || hasCollection(e.getInterfaces())) {
            addRecord(dispatcher, event, init);
            return true;
        }
        return false;
    }

    private static void addRecord(EventDispatcher dispatcher, VisitMethodEvent event, String name) {
        String msg = ASMUtils.getMethodToString(event.getName(), event.getDesc()) + " should not use " + name + " but directly or one interface that implements java/util/Collection";
        dispatcher.dispatch(new AddRecordEvent(Record.error(event.getLocation(), "PublicInterface", msg)));
    }

    private boolean hasCollection(String[] interfaces) {
        for (String interfaze : interfaces) {
            if (interfaze.equals("java/util/Collection")) {
                return true;
            }
        }
        return false;
    }

    private void makeVisit(String name, String init, VisitMethodEvent event, EventDispatcher dispatcher) {
        if (name != null) {
            ClassVisitor visitor = buildVisitor(init, event, dispatcher, false);
            dispatcher.dispatch(new AddAdditionalEvent(name, visitor, ClassReader.SKIP_CODE));
        }
    }

    @Override
    public String toString() {
        return "PublicInterface Rule";
    }
}
