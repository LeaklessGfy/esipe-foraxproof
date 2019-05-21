package fr.upem.foraxproof.core.visitor;

import fr.upem.foraxproof.core.ForaxProof;
import fr.upem.foraxproof.core.analysis.JavaType;
import fr.upem.foraxproof.core.event.EventDispatcher;
import fr.upem.foraxproof.core.event.asm.VisitAnnotationEvent;
import fr.upem.foraxproof.core.event.asm.VisitEndEvent;
import fr.upem.foraxproof.core.event.asm.VisitEvent;
import fr.upem.foraxproof.core.event.asm.VisitFieldEvent;
import fr.upem.foraxproof.core.event.asm.VisitInnerClassEvent;
import fr.upem.foraxproof.core.event.asm.method.VisitLineNumberEvent;
import fr.upem.foraxproof.core.event.asm.VisitMethodEvent;
import fr.upem.foraxproof.core.event.asm.VisitOuterClassEvent;
import fr.upem.foraxproof.core.event.asm.method.VisitTypeInsnEvent;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.Objects;

import static org.objectweb.asm.Opcodes.ASM6;

/**
 * EventVisitor is the main visitor use to dispatch event.
 */
@ForaxProof("ClassVisitor utility")
public final class EventVisitor extends AbstractVisitor {
    private final EventDispatcher dispatcher;

    /**
     * EventVisitor constructor.
     * @param dispatcher the dispatcher of events
     */
    public EventVisitor(EventDispatcher dispatcher) {
        super();
        this.dispatcher = Objects.requireNonNull(dispatcher);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);

        if (dispatcher.hasListener(VisitEvent.class)) {
            dispatcher.dispatch(new VisitEvent(version, access, name, signature, superName, interfaces, getLocation()));
        }
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
        super.visitOuterClass(owner, name, desc);

        if (dispatcher.hasListener(VisitOuterClassEvent.class)) {
            dispatcher.dispatch(new VisitOuterClassEvent(owner, name, desc, getLocation()));
        }
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        super.visitInnerClass(name, outerName, innerName, access);

        if (dispatcher.hasListener(VisitInnerClassEvent.class)) {
            dispatcher.dispatch(new VisitInnerClassEvent(name, outerName, innerName, access, getLocation()));
        }
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        super.visitAnnotation(desc, visible);

        if (dispatcher.hasListener(VisitAnnotationEvent.class)) {
            dispatcher.dispatch(new VisitAnnotationEvent(desc, visible, JavaType.CLASS, getLocation()));
        }

        return null;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        super.visitField(access, name, desc, signature, value);

        if (dispatcher.hasListener(VisitFieldEvent.class)) {
            dispatcher.dispatch(new VisitFieldEvent(access, name, desc, signature, value, getLocation()));
        }

        return new FieldVisitor(ASM6) {
            @Override
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                if (dispatcher.hasListener(VisitAnnotationEvent.class)) {
                    dispatcher.dispatch(new VisitAnnotationEvent(desc, visible, JavaType.FIELD, getLocation()));
                }

                return null;
            }
        };
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        super.visitMethod(access, name, desc, signature, exceptions);

        if (dispatcher.hasListener(VisitMethodEvent.class)) {
            dispatcher.dispatch(new VisitMethodEvent(access, name, desc, signature, exceptions, getLocation()));
        }

        return new MethodVisitor(ASM6) {
            @Override
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                if (dispatcher.hasListener(VisitAnnotationEvent.class)) {
                    dispatcher.dispatch(new VisitAnnotationEvent(desc, visible, JavaType.METHOD, getLocation()));
                }

                return null;
            }

            @Override
            public void visitTypeInsn(int opcode, String type) {
                if (dispatcher.hasListener(VisitTypeInsnEvent.class)) {
                    dispatcher.dispatch(new VisitTypeInsnEvent(opcode, type, getLocation()));
                }
            }

            @Override
            public void visitLineNumber(int line, Label start) {
                EventVisitor.super.visitLineNumber(line, start);

                if (dispatcher.hasListener(VisitLineNumberEvent.class)) {
                    dispatcher.dispatch(new VisitLineNumberEvent(line, start, getLocation()));
                }
            }

            @Override
            public void visitEnd() {
                if (dispatcher.hasListener(fr.upem.foraxproof.core.event.asm.method.VisitEndEvent.class)) {
                    dispatcher.dispatch(new fr.upem.foraxproof.core.event.asm.method.VisitEndEvent(getLocation()));
                }
            }
        };
    }

    @Override
    public void visitEnd() {
        super.visitEnd();

        if (dispatcher.hasListener(VisitEndEvent.class)) {
            dispatcher.dispatch(new VisitEndEvent(getLocation()));
        }
    }
}
