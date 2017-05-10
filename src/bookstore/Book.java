package bookstore;

import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Book {

    private String title;
    private String pubDate;
    private double price;
    private int quantity;
    private ArrayList<Author> author = new ArrayList<>();
    private int id;
    private static SQLiteConn sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");

    public Book(String pubDate, int quantity) {
        this.pubDate = pubDate;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getPubDate() {
        return pubDate;
    }

    public void setPubdate(String pubDate) {
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



    private static void openSqLite(){
        sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");
    }
    public ArrayList<Author> getAuthor() {
        openSqLite();
        String sql = "SELECT id FROM Author WHERE book_id = "+id;
        ResultSet resultSet = sqLiteConn.executeQuery(sql);
        try{
            while(resultSet.next()){
                String relationId = resultSet.getString("id");
                if(!relationId.equals("0")){
                    this.addAuthor(Author.get(relationId));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        sqLiteConn.close();
        return this.author;
    }

    public void addAuthor(Author author) throws IllegalArgumentException {
        if(author.getId() == 0){
            throw new IllegalArgumentException("You need to save Author id: " + author.getId() + " in the database first");
        }else{
            this.author.add(author);
            //TODO Secalhar aqui arranjar maneira de chamar o update, caso o livro ja esteja na BD e queiramos adicionar mais um autor
        }
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
            String sql = String.format("UPDATE Book SET title = '%s',pubDate = '%s',price = '%s',quantity = '%s' WHERE id = '%s'",this.title,this.pubDate,this.price,this.quantity,this.id);
            sqLiteConn.executeUpdate(sql);
        }else{
            String sql = String.format("INSERT INTO Book (title,pubDate,price,quantity) VALUES ('%s','%s','%s','%s')",this.title,this.pubDate,this.price,this.quantity);
            int idPerson = sqLiteConn.executeUpdate(sql);
            setId(idPerson);
        }
        sqLiteConn.close();
         saveRelation();
    }

     private void saveRelation(){
        getAuthor();
        openSqLite();
        for(Author object : author){
            String sql = String.format("UPDATE Author SET book_id = '%s' WHERE id = '%s'", this.id, object.getId());
            sqLiteConn.executeUpdate(sql);
        }
        sqLiteConn.close();
     }

    public void delete(){
        if(this.id >= 1){
            String sql = "DELETE FROM Book WHERE id = "+this.id;
            openSqLite();
            sqLiteConn.executeUpdate(sql);
            sqLiteConn.close();
        }else{
            System.out.println("This object does not exist in the database");
        }
    }

    public static ArrayList all(){
        ArrayList<Book> list = new ArrayList<>();
        openSqLite();
        ResultSet rs = sqLiteConn.executeQuery("SELECT * FROM Book");
        try{
            while(rs.next()){
                Book book = new Book(rs.getString("pubDate"), rs.getInt("quantity"));

                int id = rs.getInt("id");
                book.setId(id);

                String title = rs.getString("title");
                book.setTitle(title);

                double price = rs.getDouble("price");
                book.setPrice(price);


                list.add(book);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        sqLiteConn.close();
        return list;
    }
    //POR O GET A IR BUSCAR O WHERE com id = id, escusamos de ter dois metodos
    public static Book get(String id){
        Book book = null;
        openSqLite();
        ResultSet rs = sqLiteConn.executeQuery("SELECT * FROM Book WHERE id = " + id);
        try{
            rs.next();

            book = new Book(rs.getString("pubDate"), rs.getInt("quantity"));

            int idFromDB = rs.getInt("id");
            book.setId(idFromDB);

            String title = rs.getString("title");
            book.setTitle(title);

            double price = rs.getDouble("price");
            book.setPrice(price);


        }catch(Exception e){
            e.printStackTrace();
        }
        sqLiteConn.close();
        return book;
    }

    public static ArrayList where(String condition){
        ArrayList<Book> list = new ArrayList<>();
        openSqLite();
        ResultSet rs = sqLiteConn.executeQuery("SELECT * FROM Book WHERE " + condition);
        try{
            while(rs.next()){
                Book book = new Book(rs.getString("pubDate"), rs.getInt("quantity"));

                int id = rs.getInt("id");
                book.setId(id);

                String title = rs.getString("title");
                book.setTitle(title);

                double price = rs.getDouble("price");
                book.setPrice(price);


                list.add(book);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        sqLiteConn.close();
        return list;
    }

    @Override
    public String toString(){
        return "ID: " + this.id + "\ntitle: " + this.title + "\npubDate: " + this.pubDate + "\nprice: " + this.price + "\nquantity: " + this.quantity ;
    }


}

