package fr.upem.foraxproof.core.analysis;

import java.util.Objects;
import java.util.Optional;

/**
 * A Location is an immutable object that allows to locate precisely
 * the location of a detection associated with a Record.
 * A location provides different levels of precision.
 * You can specify location in this way :
 * - Only a class
 * - A class and a field
 * - A class and a method
 * - A class, a method and a field.
 *
 * @author Vincent Rasquier
 * @author Alex Pliez
 *
 * @since RELEASE 1.0
 */
public final class Location {
    /**
     * A Location.Field contains all information needed to
     * locate and distinguish a field with another.
     */
    public static final class Field {
        private final int access;
        private final String name;
        private final String desc;
        private final String signature;

        /**
         * Constructs a new Field.
         * @param access the field access flags (see org.objectweb.asm.Opcodes to have more information).
         * @param name the field name.
         * @param desc the field descriptor (see org.objectweb.asm.Type to have more information).
         * @param signature the field signature. Can be null if the field does not use generic types.
         */
        public Field(int access, String name, String desc, String signature) {
            this.access = access;
            this.name = name;
            this.desc = desc;
            this.signature = signature;
        }

        /**
         * Returns the access of this field.
         * @return the access of this field.
         */
        public int getAccess() {
            return access;
        }

        /**
         * Returns the name of this field.
         * @return the name of this field.
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the descriptor of this field.
         * @return the descriptor of this field.
         */
        public String getDesc() {
            return desc;
        }

        /**
         * Returns the signature of this field.
         * @return the signature of this field or null if the field does not use generic types.
         */
        public String getSignature() {
            return signature;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Field field = (Field) o;
            return access == field.access && (name != null ? name.equals(field.name) : field.name == null) && (desc != null ? desc.equals(field.desc) : field.desc == null) && (signature != null ? signature.equals(field.signature) : field.signature == null);

        }

        @Override
        public int hashCode() {
            int result = access;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (desc != null ? desc.hashCode() : 0);
            result = 31 * result + (signature != null ? signature.hashCode() : 0);
            return result;
        }
    }

    /**
     * A Location.Method contains all information needed to
     * locate and distinguish a method with another.
     */
    public static final class Method {
        private final int access;
        private final String name;
        private final String desc;
        private final String signature;

        /**
         * Constructs a new Method.
         * @param access the method access flags (see org.objectweb.asm.Opcodes to have more information).
         * @param name the method name.
         * @param desc the method descriptor (see org.objectweb.asm.Type to have more information).
         * @param signature the method signature. Can be null if the method parameters, return type and exceptions do not use generic types.
         */
        public Method(int access, String name, String desc, String signature) {
            this.access = access;
            this.name = name;
            this.desc = desc;
            this.signature = signature;
        }

        /**
         * Returns the access of this method.
         * @return the access of this method.
         */
        public int getAccess() {
            return access;
        }

        /**
         * Returns the name of this method.
         * @return the name of this method.
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the descriptor of this method.
         * @return the descriptor of this method.
         */
        public String getDesc() {
            return desc;
        }

        /**
         * Returns the signature of this method.
         * @return the signature of this method or null if the method parameters, return type and exceptions do not use generic types.
         */
        public String getSignature() {
            return signature;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Method method = (Method) o;
            return access == method.access && (name != null ? name.equals(method.name) : method.name == null) && (desc != null ? desc.equals(method.desc) : method.desc == null) && (signature != null ? signature.equals(method.signature) : method.signature == null);

        }

        @Override
        public int hashCode() {
            int result = access;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (desc != null ? desc.hashCode() : 0);
            result = 31 * result + (signature != null ? signature.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Method{" +
                    "access=" + access +
                    ", name='" + name + '\'' +
                    ", desc='" + desc + '\'' +
                    ", signature='" + signature + '\'' +
                    '}';
        }
    }

    private final JavaType type;
    private final String source;
    private final Field field;
    private final Method method;
    private final int line;

    private Location(Location.Builder builder) {
        this.type = Objects.requireNonNull(builder.type, "Type shouldn't be null");
        this.source = Objects.requireNonNull(builder.source, "Source shouldn't be null");
        this.field = builder.field;
        this.method = builder.method;
        this.line = builder.line;
    }

    /**
     * Returns the location type.
     * @return Returns the location type.
     */
    public JavaType getType() {
        return type;
    }

    /**
     * Returns the name of the source file.
     * @return the name of the source file.
     */
    public String getSource() {
        return source;
    }

    /**
     * Returns the Field location.
     * @return an optional Field location.
     */
    public Optional<Field> getField() {
        return Optional.ofNullable(field);
    }

    /**
     * Returns the Method location.
     * @return an optional Method location.
     */
    public Optional<Method> getMethod() {
        return Optional.ofNullable(method);
    }

    /**
     * Returns the line of the location in the source code.
     * @return the line of the location in the source code.
     */
    public int getLine() {
        return line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return type == location.type && equalsType(location) && source.equals(location.source);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + source.hashCode();
        return hashCodeType(result);
    }

    @Override
    public String toString() {
        return "Location{" +
                "type=" + type +
                ", source='" + source + '\'' +
                ", field=" + field +
                ", method=" + method +
                ", line=" + line +
                '}';
    }

    private boolean equalsType(Location location) {
        switch (type) {
            case FIELD:
                return field.equals(location.field);
            case METHOD:
                return method.equals(location.method);
            case INSTRUCTION:
                return line == location.line;
            default:
                return true;
        }
    }

    private int hashCodeType(int result) {
        switch (type) {
            case FIELD:
                return 31 * result + field.hashCode();
            case METHOD:
                return 31 * result + method.hashCode();
            case INSTRUCTION:
                return (31 * result + line) * 31 + method.hashCode();
            default:
                return result;
        }
    }

    /**
     * A Builder is a mutable object used to build
     * an immutable Location.
     */
    public static final class Builder {
        private JavaType type;
        private String source;
        private Field field;
        private Method method;
        private int line;

        /**
         * Set the type of the location.
         * @param type the type of the location.
         * @return a reference to this object.
         */
        public Builder setType(JavaType type){
            this.type = type;
            return this;
        }

        /**
         * Set the source file name of the location.
         * @param source the source file.
         * @return a reference to this object.
         */
        public Builder setSource(String source) {
            this.source = source;
            return this;
        }

        /**
         * Set the field of the location.
         * @param field the field containing precisions about location.
         * @return a reference to this object.
         */
        public Builder setField(Field field) {
            this.field = field;
            return this;
        }

        /**
         * Set the method of the location.
         * @param method the method containing precisions about location.
         * @return a reference to this object.
         */
        public Builder setMethod(Method method) {
            this.method = method;
            return this;
        }

        /**
         * Set the line of the location.
         * @param line the line number.
         * @return a reference to this object.
         */
        public Builder setLine(int line) {
            this.line = line;
            return this;
        }

        /**
         * Create immutable Location containing the current content of the Builder.
         * @return an immutable Location.
         */
        public Location toLocation() {
            return new Location(this);
        }
    }
}
