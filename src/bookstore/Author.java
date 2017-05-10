package bookstore;

import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Author {

    private String first_name;
    private String last_name;
    private String email;
    private ArrayList<Book> books = new ArrayList<>();
    private int id;
    private static SQLiteConn sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");

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



    private static void openSqLite(){
        sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");
    }

    public ArrayList<Book> getBooks() {
        openSqLite();
        ResultSet resultSet = sqLiteConn.executeQuery("SELECT book_id FROM Book_Author WHERE author_id = " + id);
        try{
            while(resultSet.next()){
                String relationId = resultSet.getString("book_id");
                if(!relationId.equals("0")){
                    this.addBook(Book.get(relationId));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        sqLiteConn.close();
        return this.books;
    }
    public void addBook(Book book){
        this.books.add(book);
    }
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void save(){
        openSqLite();
        if(this.id >= 1){
            String sql = String.format("UPDATE Author SET first_name = '%s',last_name = '%s',email = '%s' WHERE id = '%s'",this.first_name,this.last_name,this.email,this.id);
            sqLiteConn.executeUpdate(sql);
        }else{
            String sql = String.format("INSERT INTO Author (first_name,last_name,email) VALUES ('%s','%s','%s')",this.first_name,this.last_name,this.email);
            int idPerson = sqLiteConn.executeUpdate(sql);
            setId(idPerson);
        }
        sqLiteConn.close();
    }


    public void delete(){
        if(this.id >= 1){
            String sql = "DELETE FROM Author WHERE id = "+this.id;
            openSqLite();
            sqLiteConn.executeUpdate(sql);
            sqLiteConn.close();
        }else{
            System.out.println("This object does not exist in the database");
        }
    }

    public static ArrayList<Author> all(){
        ArrayList<Author> list = new ArrayList<>();
        openSqLite();
        ResultSet rs = sqLiteConn.executeQuery("SELECT * FROM Author");
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
        sqLiteConn.close();
        return list;
    }
    //POR O GET A IR BUSCAR O WHERE com id = id, escusamos de ter dois metodos
    public static Author get(String id){
        Author author = null;
        openSqLite();
        ResultSet rs = sqLiteConn.executeQuery("SELECT * FROM Author WHERE id = " + id);
        try{
            rs.next();

            author = new Author();

            int idFromDB = rs.getInt("id");
            author.setId(idFromDB);

            String first_name = rs.getString("first_name");
            author.setFirst_name(first_name);

            String last_name = rs.getString("last_name");
            author.setLast_name(last_name);

            String email = rs.getString("email");
            author.setEmail(email);


        }catch(Exception e){
            e.printStackTrace();
        }
        sqLiteConn.close();
        return author;
    }

    public static ArrayList<Author> where(String condition){
        ArrayList<Author> list = new ArrayList<>();
        openSqLite();
        ResultSet rs = sqLiteConn.executeQuery("SELECT * FROM Author WHERE " + condition);
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
        sqLiteConn.close();
        return list;
    }

    @Override
    public String toString(){
        return "ID: " + this.id + "\nfirst_name: " + this.first_name + "\nlast_name: " + this.last_name + "\nemail: " + this.email ;
    }


}

