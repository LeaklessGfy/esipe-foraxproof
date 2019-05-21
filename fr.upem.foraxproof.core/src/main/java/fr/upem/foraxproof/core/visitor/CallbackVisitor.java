package fr.upem.foraxproof.core.visitor;

import fr.upem.foraxproof.core.ForaxProof;
import fr.upem.foraxproof.core.event.CallbackManager;
import fr.upem.foraxproof.core.event.asm.VisitEndEvent;
import fr.upem.foraxproof.core.event.asm.VisitEvent;
import fr.upem.foraxproof.core.event.asm.VisitFieldEvent;
import fr.upem.foraxproof.core.event.asm.VisitInnerClassEvent;
import fr.upem.foraxproof.core.event.asm.VisitMethodEvent;
import fr.upem.foraxproof.core.event.asm.VisitOuterClassEvent;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.Objects;
import java.util.function.Consumer;

@ForaxProof("ClassVisitor utility")
public final class CallbackVisitor extends AbstractVisitor {
    private final CallbackManager manager;

    private CallbackVisitor(CallbackManager manager) {
        super();
        this.manager = Objects.requireNonNull(manager, "Manager shouldn't be null");
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);

        if (manager.hasCallback(VisitEvent.class)) {
            manager.call(new VisitEvent(version, access, name, signature, superName, interfaces, getLocation()));
        }
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
        super.visitOuterClass(owner, name, desc);

        if (manager.hasCallback(VisitOuterClassEvent.class)) {
            manager.call(new VisitOuterClassEvent(owner, name, desc, getLocation()));
        }
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        super.visitInnerClass(name, outerName, innerName, access);

        if (manager.hasCallback(VisitInnerClassEvent.class)) {
            manager.call(new VisitInnerClassEvent(name, outerName, innerName, access, getLocation()));
        }
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        super.visitField(access, name, desc, signature, value);

        if (manager.hasCallback(VisitFieldEvent.class)) {
            manager.call(new VisitFieldEvent(access, name, desc, signature, value, getLocation()));
        }

        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        super.visitMethod(access, name, desc, signature, exceptions);

        if (manager.hasCallback(VisitMethodEvent.class)) {
            manager.call(new VisitMethodEvent(access, name, desc, signature, exceptions, getLocation()));
        }

        return null;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();

        if (manager.hasCallback(VisitEndEvent.class)) {
            manager.call(new VisitEndEvent(getLocation()));
        }
    }

    public static class Builder {
        private final CallbackManager manager = new CallbackManager();

        public Builder onVisit(Consumer<VisitEvent> callback) {
            manager.on(VisitEvent.class, callback);
            return this;
        }

        public Builder onVisitField(Consumer<VisitFieldEvent> callback) {
            manager.on(VisitFieldEvent.class, callback);
            return this;
        }

        public Builder onVisitMethod(Consumer<VisitMethodEvent> callback) {
            manager.on(VisitMethodEvent.class, callback);
            return this;
        }

        public Builder onVisitInnerClass(Consumer<VisitInnerClassEvent> callback) {
            manager.on(VisitInnerClassEvent.class, callback);
            return this;
        }

        public ClassVisitor toVisitor() {
            return new CallbackVisitor(manager);
        }
    }
}
