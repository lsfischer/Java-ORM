package metamodels;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Class {
    private String name;
    private List<Attribute> attributes;
    private List<Relation> foreignKeys;
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

    public List<Relation> getForeignKeys() {
        return foreignKeys;
    }

    public void setForeignKeys(List<Relation> foreignKeys) {
        this.foreignKeys = foreignKeys;
    }

    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }

    /**
     * Adds a list of attributes to a class
     * @param attributes
     */
    public void addVariousAttributes(ArrayList<Attribute> attributes){
        this.attributes.addAll(attributes);
    }

    /**
     * Adds a list of relations to a class
     * @param relations
     */
    public void addVariousRelations(ArrayList<Relation> relations){
        this.foreignKeys.addAll(relations);
    }

    public void addForeignKey(Relation relation) {
        this.foreignKeys.add(relation);
    }

    public String getPkg(){
        return this.pkg;
    }
    public void setPkg(String pkg){
        this.pkg = pkg;
    }
}