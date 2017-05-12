package bookstore;

import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Classe Book
 */
public class Book {

    private String title;
    private String pubDate;
    private double price;
    private int quantity;
    private ArrayList<Author> author = new ArrayList<>();
    private int id;
    private static SQLiteConn sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");

    /**
     * Construtor da classe Book
     * @param pubDate Parametro para inicializaçao do Atributo pubDate
     * @param quantity Parametro para inicializaçao do Atributo quantity
     */
    public Book(String pubDate, int quantity) {
        this.pubDate = pubDate;
        this.quantity = quantity;
    }

    /**
     * Metodo que retorna o Atributo title
     * @return Valor do Atributo title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Metodo que altera o valor do Atributo title
     * @param title Novo valor do Atributo
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Metodo que retorna o Atributo pubDate
     * @return Valor do Atributo pubDate
     */
    public String getPubDate() {
        return pubDate;
    }

    /**
     * Metodo que altera o valor do Atributo pubDate
     * @param pubDate Novo valor do Atributo
     */
    public void setPubdate(String pubDate) {
        this.pubDate = pubDate;
    }

    /**
     * Metodo que retorna o Atributo price
     * @return Valor do Atributo price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Metodo que altera o valor do Atributo price
     * @param price Novo valor do Atributo
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Metodo que retorna o Atributo quantity
     * @return Valor do Atributo quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Metodo que altera o valor do Atributo quantity
     * @param quantity Novo valor do Atributo
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Metodo privado que inicializa o atributo sqLiteConn
     */
    private static void openSqLite(){
        sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");
    }

    /**
     * Metodo que retorna os Author pertencentes a este Objeto
     * @return
     */
    public ArrayList<Author> getAuthor() {
        openSqLite();
        String sql = "SELECT author_id FROM Book_Author WHERE book_id = " + id;
        ResultSet resultSet = sqLiteConn.executeQuery(sql);
        try{
            while(resultSet.next()){
                String relationId = Integer.toString(resultSet.getInt("author_id"));
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

    /**
     * Metodo que adiciona um Author a ArrayList de Author
     * @param author Author a ser adicionado
     */
    public void addAuthor(Author author) throws IllegalArgumentException {
        if(author.getId() == 0){
            throw new IllegalArgumentException("You need to save Author id: " + author.getId() + " in the database first");
        }else{
            this.author.add(author);
            author.addBook(this);
            //TODO Secalhar aqui arranjar maneira de chamar o update, caso o livro ja esteja na BD e queiramos adicionar mais um autor
        }
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
        if(!author.isEmpty()){
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
         }else{
            System.out.println("You need to add a author to this book");
        }
    }

     /**
      * Metodo privado que adiciona na tabela relacao, as relacoes entre este Objeto e os Objetos presentes na ArrayList de {rels.foreignClass.name}
      */
     private void saveRelation(){
        openSqLite();
         for(Author object : author){
            String sql = String.format("INSERT INTO Book_Author (book_id, author_id) VALUES ('%s', '%s')", this.id, object.getId());
            sqLiteConn.executeUpdate(sql);
         }
     }

    /**
     * Metodo que elimina da base de dados o registo correspondente a esta Classe
     */
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

    /**
     * Metodo que retorna uma ArrayList de Book correspondente a todos os Book atualmente na base de dados
     * @return ArrayList de Book
     */
    public static ArrayList<Book> all(){
        return where("id = id");
    }

    /**
     * Metodo que retorna um Book correspondente ao Id inserido
     * @param id Id do Book a retornar
     * @return Book
     */
    public static Book get(String id){
        Book book = null;
        if(!where("id = "+id).isEmpty()){
            book = where("id = "+id).get(0);
        }else{
            System.out.println("There is no Author with id: "+ id);
        }
        return book;
    }

    /**
     * Metodo que retorna uma ArrayList de Book com base na condicao recebida por parametro
     * @param condition String que especifica a condiçao a realizar na base de dados
     * @return ArrayList de Book
     */
    public static ArrayList<Book> where(String condition){
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

