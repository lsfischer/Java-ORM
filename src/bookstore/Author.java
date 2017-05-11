package bookstore;

import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Classe Author
 */
public class Author {

    private String first_name;
    private String last_name;
    private String email;
    private ArrayList<Book> books = new ArrayList<>();
    private int id;
    private static SQLiteConn sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");

    /**
     * Construtor vazio da classe Author
     */
    public Author(){
    }

    /**
     * Metodo que retorna o Atributo first_name
     * @return Valor do Atributo first_name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * Metodo que altera o nome do Atributo first_name
     * @param first_name Novo valor do Atributo
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * Metodo que retorna o Atributo last_name
     * @return Valor do Atributo last_name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * Metodo que altera o nome do Atributo last_name
     * @param last_name Novo valor do Atributo
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * Metodo que retorna o Atributo email
     * @return Valor do Atributo email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Metodo que altera o nome do Atributo email
     * @param email Novo valor do Atributo
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Metodo privado que inicializa o atributo sqLiteConn
     */
    private static void openSqLite(){
        sqLiteConn = new SQLiteConn("src/bookstore/bookstore.db");
    }

    /**
     * Metodo que retorna uma ArrayList de Book pertencentes ao Author
     * @return ArrayList de Book
     */
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

    /**
     * Metodo que adiciona um Book a ArrayList de Book
     * @param book Book a ser adicionado
     */
    public void addBook(Book book){
        this.books.add(book);
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
            String sql = String.format("UPDATE Author SET first_name = '%s',last_name = '%s',email = '%s' WHERE id = '%s'",this.first_name,this.last_name,this.email,this.id);
            sqLiteConn.executeUpdate(sql);
        }else{
            String sql = String.format("INSERT INTO Author (first_name,last_name,email) VALUES ('%s','%s','%s')",this.first_name,this.last_name,this.email);
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
            String sql = "DELETE FROM Author WHERE id = "+this.id;
            openSqLite();
            sqLiteConn.executeUpdate(sql);
            sqLiteConn.close();
        }else{
            System.out.println("This object does not exist in the database");
        }
    }

    /**
     * Metodo que retorna uma ArrayList de Author correspondente a todos os Author atualmente na base de dados
     * @return ArrayList de Author
     */
    public static ArrayList<Author> all(){
        return where("id = id");
    }

    /**
     * Metodo que retorna um Author correspondente ao Id inserido
     * @param id Id do Author a retornar
     * @return Author
     */
    public static Author get(String id){
        Author author = null;
        if(!where("id = "+id).isEmpty()){
            author = where("id = "+id).get(0);
        }else{
            System.out.println("There is no Author with id: "+ id);
        }
        return author;
    }

    /**
     * Metodo que retorna uma ArrayList de Author com base na condicao recebida por parametro
     * @param condition String que especifica a condiçao a realizar na base de dados
     * @return ArrayList de Author
     */
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

