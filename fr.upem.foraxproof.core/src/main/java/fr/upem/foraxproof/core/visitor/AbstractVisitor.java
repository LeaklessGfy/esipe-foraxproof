package fr.upem.foraxproof.core.visitor;

import fr.upem.foraxproof.core.analysis.JavaType;
import fr.upem.foraxproof.core.analysis.Location;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM6;

abstract class AbstractVisitor extends ClassVisitor {
    private final Location.Builder builder;

    AbstractVisitor() {
        super(ASM6);
        this.builder = new Location.Builder();
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        builder.setType(JavaType.CLASS);
        builder.setSource(name);
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
        builder.setType(JavaType.CLASS);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        builder.setType(JavaType.CLASS);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        builder.setType(JavaType.CLASS);

        return null;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        builder.setType(JavaType.FIELD);
        builder.setField(new Location.Field(access, name, desc, signature));

        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        builder.setType(JavaType.METHOD);
        builder.setMethod(new Location.Method(access, name, desc, signature));

        return null;
    }

    @Override
    public void visitEnd() {
        builder.setType(JavaType.CLASS);
    }

    public void visitLineNumber(int line, Label start) {
        builder.setLine(line);
    }

    Location getLocation() {
        return builder.toLocation();
    }
}
