package person;

import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Classe Person
 */
public class Person {

    private String name;
    private int age;
    private int id;
    private static SQLiteConn sqLiteConn = new SQLiteConn("src/person/person.db");

    /**
     * Construtor da classe Person
     * @param name Parametro para inicializaçao do Atributo name
     * @param age Parametro para inicializaçao do Atributo age
     */
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
     * Metodo que retorna o Atributo name
     * @return Valor do Atributo name
     */
    public String getName() {
        return name;
    }

    /**
     * Metodo que altera o valor do Atributo name
     * @param name Novo valor do Atributo
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Metodo que retorna o Atributo age
     * @return Valor do Atributo age
     */
    public int getAge() {
        return age;
    }

    /**
     * Metodo que altera o valor do Atributo age
     * @param age Novo valor do Atributo
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Metodo privado que inicializa o atributo sqLiteConn
     */
    private static void openSqLite(){
        sqLiteConn = new SQLiteConn("src/person/person.db");
    }


    /**
      * Metodo que retorna o atributo Id
      * @return Inteiro id
      */
    public int getId() {
        return this.id;
    }

    /**
     * Metodo que altera o atributo Id
     * @param id Novo Id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Metodo que guarda na base de dados o estado atual de todos os atributos da Classe
     */
    public void save(){
        openSqLite();
        if(this.id >= 1){
            String sql = String.format("UPDATE Person SET name = '%s',age = '%s' WHERE id = '%s'",this.name,this.age,this.id);
            sqLiteConn.executeUpdate(sql);
        }else{
            String sql = String.format("INSERT INTO Person (name,age) VALUES ('%s','%s')",this.name,this.age);
            int idPerson = sqLiteConn.executeUpdate(sql);
            setId(idPerson);
        }
        sqLiteConn.close();
    }


    /**
     * Metodo que elimina da base de dados o registo correspondente a esta Classe
     */
    public void delete(){
        if(this.id >= 1){
            String sql = "DELETE FROM Person WHERE id = "+this.id;
            openSqLite();
            sqLiteConn.executeUpdate(sql);
            sqLiteConn.close();
        }else{
            System.out.println("This object does not exist in the database");
        }
    }

    /**
     * Metodo que retorna uma ArrayList de Person correspondente a todos os Person atualmente na base de dados
     * @return ArrayList de Person
     */
    public static ArrayList<Person> all(){
        return where("id = id");
    }

    /**
     * Metodo que retorna um Person correspondente ao Id inserido
     * @param id Id do Person a retornar
     * @return Person
     */
    public static Person get(String id){
        Person person = null;
        if(!where("id = "+id).isEmpty()){
            person = where("id = "+id).get(0);
        }else{
            System.out.println("There is no Person with id: "+ id);
        }
        return person;
    }

    /**
     * Metodo que retorna uma ArrayList de Person com base na condicao recebida por parametro
     * @param condition String que especifica a condiçao a realizar na base de dados
     * @return ArrayList de Person
     */
    public static ArrayList<Person> where(String condition){
        ArrayList<Person> list = new ArrayList<>();
        openSqLite();
        ResultSet rs = sqLiteConn.executeQuery("SELECT * FROM Person WHERE " + condition);
        try{
            while(rs.next()){
                Person person = new Person(rs.getString("name"), rs.getInt("age"));

                int id = rs.getInt("id");
                person.setId(id);


                list.add(person);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        sqLiteConn.close();
        return list;
    }

    @Override
    public String toString(){
        return "ID: " + this.id + "\nname: " + this.name + "\nage: " + this.age ;
    }


}

