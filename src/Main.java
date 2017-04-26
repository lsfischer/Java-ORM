
import bookstore.Author;
import bookstore.Book;
import com.sun.org.apache.xpath.internal.SourceTree;
import metamodels.Attribute;
import metamodels.Class;
import metamodels.Model;
import metamodels.Relation;
import org.sqlite.SQLiteException;
import person.Person;
import utils.sqlite.SQLiteConn;
import utils.transformations.Model2Model;
import utils.transformations.Model2Text;

import java.io.*;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;


public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        startProgram();
        //testORM();
        //TODO Por a posibilidade de o utilizador criar o seu proprio modelo
    }

    public static String getModelChoice() {
        System.out.println("Which model do you want ? (Answer with the number of the desired model)");
        System.out.println("1 - BookStore");
        System.out.println("2 - Person");

        String modelChoice = sc.nextLine();
        while (!modelChoice.equals("1") && !modelChoice.equals("2")) {
            System.out.println("Invalid Choice");
            modelChoice = sc.nextLine();
        }
        return modelChoice;
    }

    public static String getLanguageChoice() {
        System.out.println("Do you want it in Java or XML ? (Answer with the desired number)");
        System.out.println("1 - Java");
        System.out.println("2 - XML");
        String languageChoice = sc.nextLine();
        while (!languageChoice.equals("1") && !languageChoice.equals("2")) {
            System.out.println("Invalid Choice");
            languageChoice = sc.nextLine();
        }
        return languageChoice;
    }

    public static void startProgram() {

        String modelChoice = getModelChoice();
        String languageChoice = getLanguageChoice();

        if (modelChoice.equals("1") && languageChoice.equals("1")) {
            buildModel(getBookStoreModel());
        }
        if (modelChoice.equals("2") && languageChoice.equals("1")) {
            buildModel(getPersonModel());
        }
        if (modelChoice.equals("1") && languageChoice.equals("2")) {
            Model model = Model2Model.getModel("src/models/bookstore.xml");
            buildModel(model);
        }
        if (modelChoice.equals("2") && languageChoice.equals("2")) {
            Model model = Model2Model.getModel("src/models/person.xml");
            buildModel(model);
        }
    }

    public static Model getPersonModel() {
        Model model = new Model("Person");

        Class person = new Class("Person");
        person.addAttribute(new Attribute("name", "String"));
        person.addAttribute(new Attribute("age", "int"));

        model.addClass(person);
        return model;
    }

    public static Model getBookStoreModel() {
        Model model = new Model("BookStore");

        Class author = new Class("Author");
        author.addAttribute(new Attribute("first_name", "String"));
        author.addAttribute(new Attribute("last_name", "String"));
        author.addAttribute(new Attribute("email", "String"));
        author.setPkg(model.getName().toLowerCase());

        Class book = new Class("Book");
        book.addAttribute(new Attribute("title", "String"));
        book.addAttribute(new Attribute("pubDate", "String"));
        book.addAttribute(new Attribute("price", "double"));
        book.addAttribute(new Attribute("quantity", "int"));
        book.setPkg(model.getName().toLowerCase());

        //Da nova maneira agora é assim
        Relation relation = new Relation(book, author, "12N");
        book.addRelation(relation);
        author.addRelation(relation);

        model.addClass(author);
        model.addClass(book);
        return model;
    }

    public static void buildModel(Model model) {

        // Generate SQL tables
        Model2Text model2Text = new Model2Text("src/templates");
        String sqlTables = model2Text.render(model, "sqlite3_create.ftl");
        System.out.println(sqlTables);
        //File f = new File("src/"+model.getName().toLowerCase());
        //f.mkdirs();

        SQLiteConn sqLiteConn = new SQLiteConn("src/" + model.getName().toLowerCase() + "/" + model.getName().toLowerCase() + ".db");
        //sqLiteConn.execute(sqlTables);

        // Generate Java classes
        for (Class c : model.getClasses()) {
            String javaClasses = model2Text.render(c, "java_class.ftl");
            System.out.println(javaClasses);

            try {
                File fout = new File("src/" + model.getName().toLowerCase() + "/" + c.getName() + ".java");
                FileOutputStream fos = new FileOutputStream(fout);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                bw.write(javaClasses);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void testORM() {
        //TODO Por o TestORM a funcionar com base no modelo que o utilizador escolher no startProgram()
        Book book1 = new Book();
        book1.setTitle("Title1");
        book1.setPrice(10);
        book1.setQuantity(100);
        book1.setPubdate("10/01/2001");
        Author a = Author.get("1");
        System.out.println(a);
        book1.addAuthor(a);
        //book
        /*
        //Creates a new Author and saves it in the data base
        Author newAuthor = new Author();
        newAuthor.setFirst_name("Lucas");
        newAuthor.setLast_name("Fischer");
        newAuthor.setEmail("lucasfischerpt@gmail.com");
        newAuthor.save();
        System.out.println("-----------------------------------");
        ArrayList<Author> authors = Author.all();
        for(Author a : authors){
            System.out.println(a);
        }
        System.out.println("-----------------------------------");
        */
    }
}