package metamodels;

/**
 * Lucas Fischer, Nº 140221004
 * Daniel Basilio, Nº 140221003
 */

import org.w3c.dom.Attr;

/**
 * Classe Metamodelo que especifica as caracteristicas de um atributo
 */
public class Attribute {
    private String name;
    private String type;
    private boolean required;

    /**
     * Construtor da classe metamodelo
     * @param name Nome do Atributo
     * @param type Tipo de dado do Atributo
     */
    public Attribute(String name, String type) {
        this.name = name;
        this.type = type;
        this.required = false;
    }

    /**
     * Metodo que retorna o nome do Atributo
     * @return Nome do Atributo
     */
    public String getName() {
        return name;
    }

    /**
     * Metodo que altera o nome do Atributo
     * @param name Novo nome do Atributo
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Metodo que retorna o tipo de dado do Atributo
     * @return Tipo do produto
     */
    public String getType() {
        return type;
    }

    /**
     * Metodo que altera o tipo de dado do Atributo
     * @param type Novo tipo de dado do Atributo
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Metodo que retorna o tipo booleano required
     * @return Valor logico required
     */
    public boolean getRequired(){
        return this.required;
    }

    /**
     * Metodo que altera o valor do atributo required para true;
     */
    public void setRequired(){
        this.required = true;
    }
}





