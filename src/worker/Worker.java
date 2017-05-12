package worker;

import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Classe Worker
 */
public class Worker {

    private String job;
    private String jobDescription;
    private double salary;
    private int id;
    private static SQLiteConn sqLiteConn = new SQLiteConn("src/worker/worker.db");

    /**
     * Construtor vazio da classe Worker
     */
    public Worker(){
    }

    /**
     * Metodo que retorna o Atributo job
     * @return Valor do Atributo job
     */
    public String getJob() {
        return job;
    }

    /**
     * Metodo que altera o valor do Atributo job
     * @param job Novo valor do Atributo
     */
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * Metodo que retorna o Atributo jobDescription
     * @return Valor do Atributo jobDescription
     */
    public String getJobDescription() {
        return jobDescription;
    }

    /**
     * Metodo que altera o valor do Atributo jobDescription
     * @param jobDescription Novo valor do Atributo
     */
    public void setJobdescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    /**
     * Metodo que retorna o Atributo salary
     * @return Valor do Atributo salary
     */
    public double getSalary() {
        return salary;
    }

    /**
     * Metodo que altera o valor do Atributo salary
     * @param salary Novo valor do Atributo
     */
    public void setSalary(double salary) {
        this.salary = salary;
    }

    /**
     * Metodo privado que inicializa o atributo sqLiteConn
     */
    private static void openSqLite(){
        sqLiteConn = new SQLiteConn("src/worker/worker.db");
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
            String sql = String.format("UPDATE Worker SET job = '%s',jobDescription = '%s',salary = '%s' WHERE id = '%s'",this.job,this.jobDescription,this.salary,this.id);
            sqLiteConn.executeUpdate(sql);
        }else{
            String sql = String.format("INSERT INTO Worker (job,jobDescription,salary) VALUES ('%s','%s','%s')",this.job,this.jobDescription,this.salary);
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
            String sql = "DELETE FROM Worker WHERE id = "+this.id;
            openSqLite();
            sqLiteConn.executeUpdate(sql);
            sqLiteConn.close();
        }else{
            System.out.println("This object does not exist in the database");
        }
    }

    /**
     * Metodo que retorna uma ArrayList de Worker correspondente a todos os Worker atualmente na base de dados
     * @return ArrayList de Worker
     */
    public static ArrayList<Worker> all(){
        return where("id = id");
    }

    /**
     * Metodo que retorna um Worker correspondente ao Id inserido
     * @param id Id do Worker a retornar
     * @return Worker
     */
    public static Worker get(String id){
        Worker worker = null;
        if(!where("id = "+id).isEmpty()){
            worker = where("id = "+id).get(0);
        }else{
            System.out.println("There is no Worker with id: "+ id);
        }
        return worker;
    }

    /**
     * Metodo que retorna uma ArrayList de Worker com base na condicao recebida por parametro
     * @param condition String que especifica a condiçao a realizar na base de dados
     * @return ArrayList de Worker
     */
    public static ArrayList<Worker> where(String condition){
        ArrayList<Worker> list = new ArrayList<>();
        openSqLite();
        ResultSet rs = sqLiteConn.executeQuery("SELECT * FROM Worker WHERE " + condition);
        try{
            while(rs.next()){
                Worker worker = new Worker();

                int id = rs.getInt("id");
                worker.setId(id);

                String job = rs.getString("job");
                worker.setJob(job);

                String jobDescription = rs.getString("jobDescription");
                worker.setJobdescription(jobDescription);

                double salary = rs.getDouble("salary");
                worker.setSalary(salary);


                list.add(worker);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        sqLiteConn.close();
        return list;
    }

    @Override
    public String toString(){
        return "ID: " + this.id + "\njob: " + this.job + "\njobDescription: " + this.jobDescription + "\nsalary: " + this.salary ;
    }

     /**
      * Metodo que retorna o Person pertencente a este Objeto
      * @return Person
      */
     public Person getPerson(){
        Person person = null;
        openSqLite();
        ResultSet rs = sqLiteConn.executeQuery("SELECT person_id FROM Worker WHERE id = " + this.id);
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

