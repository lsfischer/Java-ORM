package worker;

import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Classe Person
 */
public class Person {

    private String first_name;
    private String last_name;
    private Worker worker;
    private int id;
    private static SQLiteConn sqLiteConn = new SQLiteConn("src/worker/worker.db");

    /**
     * Construtor vazio da classe Person
     */
    public Person(){
    }

    /**
     * Metodo que retorna o Atributo first_name
     * @return Valor do Atributo first_name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * Metodo que altera o valor do Atributo first_name
     * @param first_name Novo valor do Atributo
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * Metodo que retorna o Atributo last_name
     * @return Valor do Atributo last_name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * Metodo que altera o valor do Atributo last_name
     * @param last_name Novo valor do Atributo
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * Metodo privado que inicializa o atributo sqLiteConn
     */
    private static void openSqLite(){
        sqLiteConn = new SQLiteConn("src/worker/worker.db");
    }

    /**
     * Metodo que retorna um Worker pertencente a este Objeto
     * @return Worker do Person
     */
    public Worker getWorker() {
        openSqLite();
        ResultSet resultSet = sqLiteConn.executeQuery("SELECT id FROM Worker WHERE person_id = "+id);
            try{
                resultSet.next();
                String relationId = Integer.toString(resultSet.getInt("id"));
                if(!relationId.equals("0")){
                    this.setWorker(Worker.get(relationId));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        sqLiteConn.close();
        return this.worker;
    }

    /**
     * Metodo que altera o valor do atributo worker
     * @param worker Worker a ser inserido
     */
    public void setWorker(Worker worker) {
        this.worker = worker;
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
            String sql = String.format("UPDATE Person SET first_name = '%s',last_name = '%s' WHERE id = '%s'",this.first_name,this.last_name,this.id);
            sqLiteConn.executeUpdate(sql);
        }else{
            String sql = String.format("INSERT INTO Person (first_name,last_name) VALUES ('%s','%s')",this.first_name,this.last_name);
            int idPerson = sqLiteConn.executeUpdate(sql);
            setId(idPerson);
        }
        sqLiteConn.close();
         saveRelation();
    }

     /**
      * Metodo privado que adiciona na tabela relacao, as relacoes entre este Objeto e os Objetos presentes na ArrayList de {rels.foreignClass.name}
      */
     private void saveRelation(){
        if(worker.getId() == 0){
            worker.save();
        }
        openSqLite();
        String sql = String.format("UPDATE Worker SET person_id = '%s' WHERE id = '%s'", this.id, worker.getId());
        sqLiteConn.executeUpdate(sql);
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
                Person person = new Person();

                int id = rs.getInt("id");
                person.setId(id);

                String first_name = rs.getString("first_name");
                person.setFirst_name(first_name);

                String last_name = rs.getString("last_name");
                person.setLast_name(last_name);


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
        return "ID: " + this.id + "\nfirst_name: " + this.first_name + "\nlast_name: " + this.last_name ;
    }


}

