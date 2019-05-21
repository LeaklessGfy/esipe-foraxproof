import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

public abstract class ExampleClass {
    protected final String protectedString = "I am protected";
    private final int id = 0;
    public String project;

    public ExampleClass(){
        project = "foraxproof";
    }

    public boolean isString(Object o) {
        if (!(o instanceof Integer)) {
            return false;
        }
        o.equals("toto");
        if (!(o instanceof Long)) {
            return false;
        }
        if (!(o instanceof String)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ExampleClass)) {
            return false;
        }
        ExampleClass other = (ExampleClass) o;
        return id == other.id && protectedString.equals(other.protectedString) && project.equals(other.project);
    }

    protected void nothing(){}

    public ArrayList<String> test() {
        return new ArrayList<String>();
    }

    public MyCol testPublic() {
        return new MyCol();
    }
}
