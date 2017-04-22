import bookstore.Author;
import bookstore.Book;
import com.sun.org.apache.xpath.internal.SourceTree;
import metamodels.Attribute;
import metamodels.Class;
import metamodels.Model;
import metamodels.Relation;
import person.Person;
import utils.sqlite.SQLiteConn;
import utils.transformations.Model2Model;
import utils.transformations.Model2Text;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        // Build model
        //Model model = getPersonModel();
        //buildModel(model);
        //buildModel(getBookStoreModel());

        // Test ORM
        //  testORM();
        // LAB 6
       Model model = Model2Model.getModel("src/models/bookstore.xml");
       buildModel(model);
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
        
        Relation newRelation1 = new Relation(author, "N2N"); //isto passa a deixar de ser necessario, porque
        book.addForeignKey(newRelation1);                                 //a informaçao dos autores do livro esta na tabela autores, basta
                                                                          //fazer uma query a tabela autores

       // Relation newRelation2 = new Relation(book, "1");
       // author.addForeignKey(newRelation2);

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
            /*
            try {
                File fout = new File("src/" + model.getName().toLowerCase() + "/" + c.getName() + ".java");
                FileOutputStream fos = new FileOutputStream(fout);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                bw.write(javaClasses);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
        }
    }

    public static void testORM() {
        /*
        Author a = Author.get(1);
        ArrayList<Book> bookList = a.getBooks();
        for (Book b : bookList) {
            System.out.println(b.getId() + "\t" + b.getTitle() + "\t" + b.getPrice() + "\t" + b.getPubDate() + "\t" + b.getQuantity());
        }

        Person alfredo = new Person();
        alfredo.setName("Alfredo");
        alfredo.setAge(22);
        //  alfredo.save();

        Person lucas = new Person();
        lucas.setName("Lucas");
        lucas.setAge(20);
        lucas.save();

        ArrayList<Person> persons = Person.all();
        for (Person p : persons) {
            System.out.println(p.getId() + "\t" + p.getName() + "\t" + p.getAge());
        }

        System.out.println("-------------- UPDATE NAME AND AGE OF Lucas-----------");
        lucas.setName("Updated Lucas");
        lucas.setAge(22);
        lucas.save();
        ArrayList<Person> persons2 = Person.all();
        for (Person p2 : persons2) {
            System.out.println(p2.getId() + "\t" + p2.getName() + "\t" + p2.getAge());
        }

        lucas.delete();
        System.out.println("-------------- DELETE Updated Lucas-----------");
        ArrayList<Person> persons3 = Person.all();
        for (Person p3 : persons3) {
            System.out.println(p3.getId() + "\t" + p3.getName() + "\t" + p3.getAge());
        }
        Person p = new Person();
        p.setName("asdasd");
        p.setAge(20);
        //Person not yet in DB, should return "This object does not exist in the database"
        p.delete();

        /*
        System.out.println("---------------------- Find By id --------------------");
        Person p = Person.get(38);
        System.out.println("Getting id number 38 : " + p.getId() + "\t" + p.getName() + "\t" + p.getAge());

        System.out.println("---------------------- Find By Name --------------------");
        ArrayList<Person> alfredos = Person.where("name = 'Alfredo'");
        for (Person p1 : alfredos) {
            System.out.println(p1.getId() + "\t" + p1.getName() + "\t" + p1.getAge());
        }

        System.out.println("---------------------- Find By Age --------------------");
        ArrayList<Person> ages = Person.where("age >= 21");
        for (Person p1 : ages) {
            System.out.println(p1.getId() + "\t" + p1.getName() + "\t" + p1.getAge());
        }
        */

    }
}