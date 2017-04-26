package person;

import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Person {

    private String name;
    private int age;
    private int id;
    static SQLiteConn sqLiteConn = new SQLiteConn("src/person/person.db");

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    //Empty Constructor
    public Person(){
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

    public static ResultSet getResultSet(String condition){
        String sql;
        if(condition.isEmpty()){
            sql = "SELECT * FROM Person";
        }else{
            sql = "SELECT * FROM Person WHERE " + condition;
        }
        ResultSet rs = sqLiteConn.executeQuery(sql);
        return rs;
    }

    public static ArrayList all(){
        ArrayList<Person> list = new ArrayList<>();
        ResultSet rs = getResultSet("");
        try{
            while(rs.next()){
                Person person = new Person();

                int id = rs.getInt("id");
                person.setId(id);

                String name = rs.getString("name");
                person.setName(name);

                int age = rs.getInt("age");
                person.setAge(age);

                list.add(person);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static Person get(String id){
        Person person = new Person();
        ResultSet rs = getResultSet(id);
        try{
            while(rs.next()){
                int idFromDB = rs.getInt("id");
                person.setId(idFromDB);

                String name = rs.getString("name");
                person.setName(name);

                int age = rs.getInt("age");
                person.setAge(age);

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return person;
    }

    public static ArrayList where(String condition){
        ArrayList<Person> list = new ArrayList<>();
        ResultSet rs = getResultSet(condition);
        try{
            while(rs.next()){
                Person person = new Person();

                int id = rs.getInt("id");
                person.setId(id);

                String name = rs.getString("name");
                person.setName(name);

                int age = rs.getInt("age");
                person.setAge(age);

                list.add(person);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String toString(){
        return "ID: " + this.id + "\nname: " + this.name + "\nage: " + this.age ;
    }

}

