package metamodels;

/**
 * Lucas Fischer, Nº 140221004
 * Daniel Basilio, Nº 140221003
 */

/**
 * Classe Metamodelo que especifica as caracteristicas de uma Relacao
 */
public class Relation {
    private Class foreignClass;
    private Class regularClass;
    private String relationshipType;

    /**
     * Constructor da classe metamodelo
     * @param regularClass Propria classe que contem este metamodelo (Classe que nao e a foreign class)
     * @param foreignClass Classe que e a foreign key
     * @param relationshipType Tipo de relacao (1-1/1-n/n-n)
     */
    public Relation(Class regularClass, Class foreignClass, String relationshipType){
        this.regularClass = regularClass;
        this.foreignClass = foreignClass;
        this.relationshipType = relationshipType;
    }

    /**
     * Metodo que retorna a Regular Class da Relacao
     * @return Regular Class da Relacao
     */
    public Class getRegularClass(){
        return this.regularClass;
    }

    /**
     * Metodo que retorna a Foreign Class da Relacao
     * @return Foreign Class da Relacao
     */
    public Class getForeignClass(){
        return this.foreignClass;
    }

    /**
     * Metodo que retorna o tipo de Relacao
     * @return String com o tipo de Relacao
     */
    public String getRelationshipType(){
        return this.relationshipType;
    }

    /**
     * Metodo que altera a Regular Class da Relacao
     * @param regularClass Nova Regular Class
     */
    public void setRegularClass(Class regularClass){
        this.regularClass = regularClass;
    }

    /**
     * Metodo que altera a Foreign Class da Relacao
     * @param foreignClass Nova Foreign Class
     */
    public void setForeignClass(Class foreignClass){
        this.foreignClass = foreignClass;
    }

    /**
     * Metodo que altera o tipo de Relacao
     * @param relationshipType Novo tipo de Relacao
     */
    public void setRelationshipType(String relationshipType){
        this.relationshipType = relationshipType;
    }

}
