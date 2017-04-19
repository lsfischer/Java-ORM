package metamodels;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private String name;
    private List<Class> classes;

    public Model(String name) {
        this.name = name;
        this.classes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    public void addClass(Class clazz) {
        this.classes.add(clazz);
    }
}