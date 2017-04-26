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
    static SQLiteConn sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");

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
        if(author.getId() == 0){
            System.out.println("You need to save this object in the DataBase first");
        }else{
            this.author.add(author);
        }
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
         saveRelation();
    }

     public void saveRelation(){
         for(Author object : author){
            String sql = String.format("INSERT INTO Book_Author (book_id, author_id) VALUES ('%s', '%s')", this.id, object.getId());
            sqLiteConn.executeUpdate(sql);
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

    public static ResultSet getResultSet(String condition){
        String sql;
        if(condition.isEmpty()){
            sql = "SELECT * FROM Book";
        }else{
            sql = "SELECT * FROM Book WHERE " + condition;
        }
        ResultSet rs = sqLiteConn.executeQuery(sql);
        return rs;
    }

    public static ArrayList all(){
        ArrayList<Book> list = new ArrayList<>();
        ResultSet rs = getResultSet("");
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

    public static Book get(String id){
        Book book = new Book();
        ResultSet rs = getResultSet(id);
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
        ResultSet rs = getResultSet(condition);
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

    @Override
    public String toString(){
        return "ID: " + this.id + "\ntitle: " + this.title + "\npubDate: " + this.pubDate + "\nprice: " + this.price + "\nquantity: " + this.quantity ;
    }

}

