package bookstore;

import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Author {

    private String first_name;
    private String last_name;
    private String email;
    private int id;
    static SQLiteConn sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");

    //Empty Constructor
    public Author(){
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void save(){
        if(this.id >= 1){
            String sql = String.format("UPDATE Author SET first_name = '%s',last_name = '%s',email = '%s' WHERE id = '%s'",this.first_name,this.last_name,this.email,this.id);
            sqLiteConn.executeUpdate(sql);
        }else{
            String sql = String.format("INSERT INTO Author (first_name,last_name,email) VALUES ('%s','%s','%s')",this.first_name,this.last_name,this.email);
            int idPerson = sqLiteConn.executeUpdate(sql);
            setId(idPerson);
        }
    }

    public void delete(){
        if(this.id >= 1){
            String sql = "DELETE FROM Author WHERE id = "+this.id;
            sqLiteConn.executeUpdate(sql);
        }else{
            System.out.println("This object does not exist in the database");
        }
    }

    public static ResultSet getResultSet(String condition){
        String sql;
        if(condition.isEmpty()){
            sql = "SELECT * FROM Author";
        }else{
            sql = "SELECT * FROM Author WHERE " + condition;
        }
        ResultSet rs = sqLiteConn.executeQuery(sql);
        return rs;
    }

    public static ArrayList all(){
        ArrayList<Author> list = new ArrayList<>();
        ResultSet rs = getResultSet("");
        try{
            while(rs.next()){
                Author author = new Author();

                int id = rs.getInt("id");
                author.setId(id);

                String first_name = rs.getString("first_name");
                author.setFirst_name(first_name);

                String last_name = rs.getString("last_name");
                author.setLast_name(last_name);

                String email = rs.getString("email");
                author.setEmail(email);

                list.add(author);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static Author get(String id){
        Author author = new Author();
        ResultSet rs = getResultSet(id);
        try{
            while(rs.next()){
                int idFromDB = rs.getInt("id");
                author.setId(idFromDB);

                String first_name = rs.getString("first_name");
                author.setFirst_name(first_name);

                String last_name = rs.getString("last_name");
                author.setLast_name(last_name);

                String email = rs.getString("email");
                author.setEmail(email);

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return author;
    }

    public static ArrayList where(String condition){
        ArrayList<Author> list = new ArrayList<>();
        ResultSet rs = getResultSet(condition);
        try{
            while(rs.next()){
                Author author = new Author();

                int id = rs.getInt("id");
                author.setId(id);

                String first_name = rs.getString("first_name");
                author.setFirst_name(first_name);

                String last_name = rs.getString("last_name");
                author.setLast_name(last_name);

                String email = rs.getString("email");
                author.setEmail(email);

                list.add(author);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String toString(){
        return "ID: " + this.id + "\nfirst_name: " + this.first_name + "\nlast_name: " + this.last_name + "\nemail: " + this.email ;
    }

     public ArrayList<Book> getBooks(){
        //TODO Fazer alguma coisa com isto
        ArrayList<Book> list = new ArrayList<>();
        //String sql = "SELECT * FROM Book Where id = "+this.bookID;
        return list;
     }
}

