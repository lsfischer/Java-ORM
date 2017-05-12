package metamodels;

/**
 * Lucas Fischer, Nº 140221004
 * Daniel Basilio, Nº 140221003
 */

import com.sun.javaws.exceptions.InvalidArgumentException;

/**
 * Classe Metamodelo que especifica as caracteristicas de uma Relacao
 */
public class Relation {
    private Class foreignClass;
    private Class regularClass;
    private String relationshipType;
    private boolean firstClassRequired;
    private boolean secondClassRequired;

    /**
     * Constructor da classe metamodelo
     *
     * @param regularClass     Propria classe que contem este metamodelo (Classe que nao e a foreign class)
     * @param foreignClass     Classe que e a foreign key
     * @param relationshipType Tipo de relacao (1-1/1-n/n-n)
     * @param firstClassRequired valor que indica se a primeira classe é obrigatória na relação
     * @param secondClassRequired valor que indica se a segunda classe é obrigatória na relação
     * @throws IllegalArgumentException Exception para garantir que não existe relação com duas classes obrigatórias ao mesmo tempo que causaria conflitos
     */
    public Relation(Class regularClass, Class foreignClass, String relationshipType, boolean firstClassRequired, boolean secondClassRequired) throws IllegalArgumentException {
        this.regularClass = regularClass;
        this.foreignClass = foreignClass;
        this.relationshipType = relationshipType;
        if (firstClassRequired && secondClassRequired) {
            throw new IllegalArgumentException("Both Classes cannont be required at the same time !");
        }
        this.firstClassRequired = firstClassRequired;
        this.secondClassRequired = secondClassRequired;
    }

    /**
     * Metodo que retorna a Regular Class da Relacao
     *
     * @return Regular Class da Relacao
     */
    public Class getRegularClass() {
        return this.regularClass;
    }

    /**
     * Metodo que retorna a Foreign Class da Relacao
     *
     * @return Foreign Class da Relacao
     */
    public Class getForeignClass() {
        return this.foreignClass;
    }

    /**
     * Metodo que retorna o valor da obrigatoriedade da classe de origem da relação
     *
     * @return boolean com o valor da obrigatoriedade da classe de origem da relação
     */
    public boolean getFirstClassRequired() {
        return this.firstClassRequired;
    }

    /**
     * Metodo que retorna o valor da obrigatoriedade da classe de destino da relação
     *
     * @return boolean com o valor da obrigatoriedade da classe de destino da relação
     */
    public boolean getSecondClassRequired() {
        return this.secondClassRequired;
    }

    /**
     * Metodo que retorna o tipo de Relacao
     *
     * @return String com o tipo de Relacao
     */
    public String getRelationshipType() {
        return this.relationshipType;
    }

    /**
     * Metodo que altera a Regular Class da Relacao
     *
     * @param regularClass Nova Regular Class
     */
    public void setRegularClass(Class regularClass) {
        this.regularClass = regularClass;
    }

    /**
     * Metodo que altera a Foreign Class da Relacao
     *
     * @param foreignClass Nova Foreign Class
     */
    public void setForeignClass(Class foreignClass) {
        this.foreignClass = foreignClass;
    }

    /**
     * Metodo que altera o tipo de Relacao
     *
     * @param relationshipType Novo tipo de Relacao
     */
    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    /**
     * Metodo que altera a obrigatoriedade da classe de origem da relação
     *
     * @param firstClassRequired Novo valor para o atributo firstClassRequired
     */
    public void setFirstClassRequired(boolean firstClassRequired) {
        this.firstClassRequired = firstClassRequired;
    }

    /**
     * Metodo que altera a obrigatoriedade da classe de origem da relação
     *
     * @param secondClassRequired Novo valor para o atributo firstClassRequired
     */
    public void setSecondClassRequired(boolean secondClassRequired) {
        this.secondClassRequired = secondClassRequired;
    }

}
