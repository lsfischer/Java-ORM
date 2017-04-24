package bookstore;
import java.util.Date;

import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Book {

    private String title;
    private Date pubDate;
    private double price;
    private int quantity;
    private ArrayList<Author> author;
    private int id;
    SQLiteConn sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");

    //Empty Constructor
    public Book(){
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubdate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ArrayList<Author> getAuthor() {
        return author;
    }

    public void addAuthor(Author author) {
        this.author.add(author);
    }
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void save(){
        if(this.id >= 1){
            String sql = String.format("UPDATE Book SET title = '%s',pubDate = '%s',price = '%s',quantity = '%s' WHERE id = '%s'",this.title,this.pubDate,this.price,this.quantity,this.id);
            sqLiteConn.executeUpdate(sql);
        }else{
            String sql = String.format("INSERT INTO Book (title,pubDate,price,quantity) VALUES ('%s','%s','%s','%s')",this.title,this.pubDate,this.price,this.quantity);
            int idPerson = sqLiteConn.executeUpdate(sql);
            setId(idPerson);
        }
    }

    public void delete(){
        if(this.id >= 1){
            String sql = "DELETE FROM Book WHERE id = "+this.id;
            sqLiteConn.executeUpdate(sql);
        }else{
            System.out.println("This object does not exist in the database");
        }
    }

    public static ArrayList all(){
        ArrayList<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM Book";
        SQLiteConn sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");
        ResultSet rs = sqLiteConn.executeQuery(sql);
        try{
            while(rs.next()){
                Book book = new Book();

                int id = rs.getInt("id");
                book.setId(id);

                String title = rs.getString("title");
                book.setTitle(title);

                Date pubDate = rs.getDate("pubDate");
                book.setPubdate(pubDate);

                double price = rs.getDouble("price");
                book.setPrice(price);

                int quantity = rs.getInt("quantity");
                book.setQuantity(quantity);

                list.add(book);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static Book get(int id){
        Book book = new Book();
        String sql = "SELECT * FROM Book WHERE id = " + id;
        SQLiteConn sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");
        ResultSet rs = sqLiteConn.executeQuery(sql);
        try{
            while(rs.next()){
                int idFromDB = rs.getInt("id");
                book.setId(idFromDB);

                String title = rs.getString("title");
                book.setTitle(title);

                Date pubDate = rs.getDate("pubDate");
                book.setPubdate(pubDate);

                double price = rs.getDouble("price");
                book.setPrice(price);

                int quantity = rs.getInt("quantity");
                book.setQuantity(quantity);

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return book;
    }

    public static ArrayList where(String condition){
        ArrayList<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE " + condition;
        SQLiteConn sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");
        ResultSet rs = sqLiteConn.executeQuery(sql);
        try{
            while(rs.next()){
                Book book = new Book();

                int id = rs.getInt("id");
                book.setId(id);

                String title = rs.getString("title");
                book.setTitle(title);

                Date pubDate = rs.getDate("pubDate");
                book.setPubdate(pubDate);

                double price = rs.getDouble("price");
                book.setPrice(price);

                int quantity = rs.getInt("quantity");
                book.setQuantity(quantity);

                list.add(book);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

}

