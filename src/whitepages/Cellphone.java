package whitepages;

import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Classe Cellphone
 */
public class Cellphone {

    private int number;
    private int id;
    private static SQLiteConn sqLiteConn = new SQLiteConn("src/whitepages/whitepages.db");

    /**
     * Construtor da classe Cellphone
     * @param number Parametro para inicializaçao do Atributo number
     */
    public Cellphone(int number) {
        this.number = number;
    }

    /**
     * Metodo que retorna o Atributo number
     * @return Valor do Atributo number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Metodo que altera o valor do Atributo number
     * @param number Novo valor do Atributo
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Metodo privado que inicializa o atributo sqLiteConn
     */
    private static void openSqLite(){
        sqLiteConn = new SQLiteConn("src/whitepages/whitepages.db");
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
            String sql = String.format("UPDATE Cellphone SET number = '%s' WHERE id = '%s'",this.number,this.id);
            sqLiteConn.executeUpdate(sql);
        }else{
            String sql = String.format("INSERT INTO Cellphone (number) VALUES ('%s')",this.number);
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
            String sql = "DELETE FROM Cellphone WHERE id = "+this.id;
            openSqLite();
            sqLiteConn.executeUpdate(sql);
            sqLiteConn.close();
        }else{
            System.out.println("This object does not exist in the database");
        }
    }

    /**
     * Metodo que retorna uma ArrayList de Cellphone correspondente a todos os Cellphone atualmente na base de dados
     * @return ArrayList de Cellphone
     */
    public static ArrayList<Cellphone> all(){
        return where("id = id");
    }

    /**
     * Metodo que retorna um Cellphone correspondente ao Id inserido
     * @param id Id do Cellphone a retornar
     * @return Cellphone
     */
    public static Cellphone get(String id){
        Cellphone cellphone = null;
        if(!where("id = "+id).isEmpty()){
            cellphone = where("id = "+id).get(0);
        }else{
            System.out.println("There is no Author with id: "+ id);
        }
        return cellphone;
    }

    /**
     * Metodo que retorna uma ArrayList de Cellphone com base na condicao recebida por parametro
     * @param condition String que especifica a condiçao a realizar na base de dados
     * @return ArrayList de Cellphone
     */
    public static ArrayList<Cellphone> where(String condition){
        ArrayList<Cellphone> list = new ArrayList<>();
        openSqLite();
        ResultSet rs = sqLiteConn.executeQuery("SELECT * FROM Cellphone WHERE " + condition);
        try{
            while(rs.next()){
                Cellphone cellphone = new Cellphone(rs.getInt("number"));

                int id = rs.getInt("id");
                cellphone.setId(id);


                list.add(cellphone);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        sqLiteConn.close();
        return list;
    }

    @Override
    public String toString(){
        return "ID: " + this.id + "\nnumber: " + this.number ;
    }

     /**
      * Metodo que retorna o Person pertencente a este Objeto
      * @return Person
      */
     public Person getPerson(){
        Person person = null;
        openSqLite();
        ResultSet rs = sqLiteConn.executeQuery("SELECT person_id FROM Cellphone WHERE id = " + this.id);
        try{
            rs.next();
            person = Person.get(Integer.toString(rs.getInt("person_id")));
        }catch(Exception e){
            e.printStackTrace();
        }
        sqLiteConn.close();
        return person;
     }

}

