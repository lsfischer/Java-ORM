package metamodels;

import java.util.ArrayList;
import java.util.List;

public class Class {
    private String name;
    private List<Attribute> attributes;
    private List<Class> foreignKeys;
    private String pkg;

    public Class(String name) {
        this.name = name;
        this.attributes = new ArrayList<>();
        this.foreignKeys = new ArrayList<>();
        this.pkg = name.toLowerCase();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<Class> getForeignKeys() {
        return foreignKeys;
    }

    public void setForeignKeys(List<Class> foreignKeys) {
        this.foreignKeys = foreignKeys;
    }

    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }

    public void addForeignKey(Class clazz) {
        this.foreignKeys.add(clazz);
    }

    public String getPkg(){
        return this.pkg;
    }
    public void setPkg(String pkg){
        this.pkg = pkg;
    }
}