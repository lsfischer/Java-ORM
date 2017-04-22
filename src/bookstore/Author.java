package bookstore;

import utils.sqlite.SQLiteConn;

import java.sql.ResultSet;
import java.util.ArrayList;


public class Author {
    private String first_name;
    private String last_name;
    private String email;
    private int id;
    SQLiteConn sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");

    // Empty constructor
    public Author() {
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


    public void save() {
        if (this.id >= 1) {
            String sql = String.format("UPDATE Author SET first_name = '%s',last_name = '%s',email = '%s' WHERE id = '%s'", this.first_name, this.last_name, this.email, this.id);
            sqLiteConn.executeUpdate(sql);
        } else {
            String sql = String.format("INSERT INTO Author (first_name,last_name,email) VALUES ('%s','%s','%s')", this.first_name, this.last_name, this.email);
            int idPerson = sqLiteConn.executeUpdate(sql);
            setId(idPerson);
        }
    }

    public void delete() {
        if (this.id >= 1) {
            String sql = "DELETE FROM Author WHERE id = " + this.id;
            sqLiteConn.executeUpdate(sql);
        } else {
            System.out.println("This object does not exist in the database");
        }
    }

    //TODO isto está mal da maneira que temos, como um autor só pode escrever um livro (12N) é só fazer Select * From Book where ID = " + this.bookID;
    //TODO temos é que arranjar maneira de por o bookID na classe Author
    public ArrayList getBooks() {
        ArrayList<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE author_id = " + this.id;
        SQLiteConn sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");
        ResultSet rs = sqLiteConn.executeQuery(sql);
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String pubDate = rs.getString("pubDate");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");

                Book book = new Book();
                book.setId(id);
                book.setTitle(title);
                book.setPubdate(pubDate);
                book.setPrice(price);
                book.setQuantity(quantity);
                book.setAuthor(this);
                list.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList all() {
        ArrayList<Author> list = new ArrayList<>();
        String sql = "SELECT * FROM Author";
        SQLiteConn sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");
        ResultSet rs = sqLiteConn.executeQuery(sql);
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String email = rs.getString("email");
                Author author = new Author();
                author.setId(id);
                author.setFirst_name(first_name);
                author.setLast_name(last_name);
                author.setEmail(email);
                list.add(author);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Author get(int id) {
        Author author = new Author();
        String sql = "SELECT * FROM Author WHERE id = " + id;
        SQLiteConn sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");
        ResultSet rs = sqLiteConn.executeQuery(sql);
        try {
            while (rs.next()) {
                int idFromDB = rs.getInt("id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String email = rs.getString("email");
                author.setId(idFromDB);
                author.setFirst_name(first_name);
                author.setLast_name(last_name);
                author.setEmail(email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return author;
    }

    public static ArrayList where(String condition) {
        ArrayList<Author> list = new ArrayList<>();
        String sql = "SELECT * FROM Author WHERE " + condition;
        SQLiteConn sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");
        ResultSet rs = sqLiteConn.executeQuery(sql);
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String email = rs.getString("email");
                Author author = new Author();
                author.setId(id);
                author.setFirst_name(first_name);
                author.setLast_name(last_name);
                author.setEmail(email);
                list.add(author);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}