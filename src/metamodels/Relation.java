package metamodels;

/**
 * Created by Lucas on 20-Apr-17.
 */
public class Relation {
    private Class foreignClass;
    private String relationshipType;

    public Relation(Class foreignClass, String relationshipType){
        this.foreignClass = foreignClass;
        this.relationshipType = relationshipType;
    }

    public Class getForeignClass(){
        return this.foreignClass;
    }
    public String getRelationshipType(){
        return this.relationshipType;
    }
    public void setForeignClass(Class foreignClass){
        this.foreignClass = foreignClass;
    }
    public void setRelationshipType(String relationshipType){
        this.relationshipType = relationshipType;
    }


}
