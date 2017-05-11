package metamodels;

/**
 * Lucas Fischer, Nº 140221004
 * Daniel Basilio, Nº 140221003
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Metamodelo que especifica as caracteristicas de um Modelo
 */
public class Model {
    private String name;
    private List<Class> classes;

    /**
     * Construtor da classe metamodelo
     * @param name Nome do Modelo
     */
    public Model(String name) {
        this.name = name;
        this.classes = new ArrayList<>();
    }

    /**
     * Metodo que retorna o nome do Modelo
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Metodo que altera o nome do Modelo
     * @param name Novo nome do Modelo
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Metodo que retorna uma ArrayList de Classes pertencentes ao Modelo
     * @return ArrayList de Classes
     */
    public List<Class> getClasses() {
        return classes;
    }

    /**
     * Metodo que substitui a ArrayList de Classes do Modelo por outra recebida por parametro
     * @param classes ArrayList de Classes
     */
    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    /**
     * Metodo que adiciona uma Classe a ArrayList de Classes do Modelo
     * @param clazz Classe a ser adicionada a ArrayList
     */
    public void addClass(Class clazz) {
        this.classes.add(clazz);
    }

    /**
     * Metodo que adiciona as classes de uma lista recebida como parametro a lista de Classes
     * @param classes ArrayList de Classes  a ser adicionada a ArrayList
     */
    public void addVariousClasses(ArrayList<Class> classes){
        this.classes.addAll(classes);
    }
}