package person;
import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.util.ArrayList;


public class Person {
    private String name;
    private int age;
    private int id;
    SQLiteConn sqLiteConn = new SQLiteConn("src/person/person.db");
    // Empty constructor
    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void save(){
        if(this.id >= 1){
            String sql = String.format("UPDATE Person SET name = '%s',age = '%s' WHERE id = '%s'",this.name,this.age,this.id);
            sqLiteConn.executeUpdate(sql);
        }else{
            String sql = String.format("INSERT INTO Person (name,age) VALUES ('%s','%s')",this.name,this.age);
            int idPerson = sqLiteConn.executeUpdate(sql);
            setId(idPerson);
        }
    }

    public void delete(){
        if(this.id >= 1){
            String sql = "DELETE FROM Person WHERE id = "+this.id;
            sqLiteConn.executeUpdate(sql);
        }else{
            System.out.println("This object does not exist in the database");
        }
    }

    public static ArrayList all(){
        ArrayList<Person> list = new ArrayList<>();
        String sql = "SELECT * FROM Person";
        SQLiteConn sqLiteConn = new SQLiteConn("src/person/person.db");
        ResultSet rs = sqLiteConn.executeQuery(sql);
        try{
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                Person person = new Person();
                person.setId(id);
                person.setName(name);
                person.setAge(age);
                list.add(person);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static Person get(int id){
        Person person = new Person();
            String sql = "SELECT * FROM Person WHERE id = "+ id;
            SQLiteConn sqLiteConn = new SQLiteConn("src/person/person.db");
            ResultSet rs = sqLiteConn.executeQuery(sql);
            try{
                while(rs.next()){
                    int idFromDB = rs.getInt("id");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    person.setId(idFromDB);
                    person.setName(name);
                    person.setAge(age);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return person;
    }

    public static ArrayList where(String condition){
        ArrayList<Person> list = new ArrayList<>();
            String sql = "SELECT * FROM Person WHERE "+ condition;
            SQLiteConn sqLiteConn = new SQLiteConn("src/person/person.db");
            ResultSet rs = sqLiteConn.executeQuery(sql);
            try{
                while(rs.next()){
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    Person person = new Person();
                    person.setId(id);
                    person.setName(name);
                    person.setAge(age);
                    list.add(person);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return list;
    }
}