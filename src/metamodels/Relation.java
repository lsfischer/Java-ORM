package metamodels;

/**
 * Created by Lucas on 20-Apr-17.
 */
public class Relation {
    private Class foreignClass;
    private Class regularClass; // Class that is not the foreign class
    private String relationshipType;

    public Relation(Class regularClass, Class foreignClass, String relationshipType){
        this.regularClass = regularClass;
        this.foreignClass = foreignClass;
        this.relationshipType = relationshipType;
    }

    public Class getRegularClass(){
        return this.regularClass;
    }
    public Class getForeignClass(){
        return this.foreignClass;
    }
    public String getRelationshipType(){
        return this.relationshipType;
    }
    public void setRegularClass(Class regularClass){
        this.regularClass = regularClass;
    }
    public void setForeignClass(Class foreignClass){
        this.foreignClass = foreignClass;
    }
    public void setRelationshipType(String relationshipType){
        this.relationshipType = relationshipType;
    }

}
