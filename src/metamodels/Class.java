package metamodels;

/**
 * Lucas Fischer, Nº 140221004
 * Daniel Basilio, Nº 140221003
 */

import org.w3c.dom.Attr;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Metamodelo que especifica as caracteristicas de uma classe
 */
public class Class {
    private String name;
    private List<Attribute> attributes;
    private List<Relation> relations;
    private String pkg;

    /**
     * Construtor da classe metamodelo
     * @param name Nome da Classe
     */
    public Class(String name) {
        this.name = name;
        this.attributes = new ArrayList<>();
        this.relations = new ArrayList<>();
        this.pkg = name.toLowerCase();
    }

    /**
     * Metodo que retorna o nome da classe
     * @return Nome da classe
     */
    public String getName() {
        return name;
    }

    /**
     * Metodo que altera o nome da classe
     * @param name Novo nome da Classe
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Metodo que retorna uma ArrayList de Atributos
     * @return  ArrayList de Atributos
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Metodo que substitui a ArrayList de Atributos por uma enviada por parametro
     * @param attributes ArrayList de Atributos
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * Metodo que retorna a ArrayList de Relaçoes da Classe
     * @return ArrayList de Relaçoes
     */
    public List<Relation> getRelations() {
        return relations;
    }

    /**
     * Metodo que substitui a ArrayList de Relaçoes por uma enviada por parametro
     * @param relations ArrayList de Relaçoes
     */
    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    /**
     * Metodo que adiciona um novo Atributo a Arraylist de Atributos
     * @param attribute Atributo a ser adicionado a ArrayList de Atributos
     */
    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }

    /**
     * Metodo que adiciona os elementos de uma lista recebida como parametro a lista de Atributos
     * @param attributes ArrayList de Atributos a ser adicionada a ArrayList
     */
    public void addVariousAttributes(ArrayList<Attribute> attributes) {
        this.attributes.addAll(attributes);
    }

    /**
     * Metodo que adiciona as relaçoes de uma lista recebida como parametro a lista de Relaçoes
     * @param relations ArrayList de Relations  a ser adicionada a ArrayList
     */
    public void addVariousRelations(ArrayList<Relation> relations) {
        this.relations.addAll(relations);
    }

    /**
     * Metodo que adiciona uma Relaçao a ArrayList de Relaçoes
     * @param relation Relation a ser adicionada a ArrayList de Relacoes
     */
    public void addRelation(Relation relation) {
        this.relations.add(relation);
    }

    /**
     * Metodo que retorna o nome do package da Classe
     * @return String do package
     */
    public String getPkg() {
        return this.pkg;
    }

    /**
     * Metodo que altera o nome do package da Classe
     * @param pkg
     */
    public void setPkg(String pkg) {
        this.pkg = pkg;
    }
}