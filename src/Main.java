
import bookstore.Author;
import bookstore.Book;
import javafx.scene.control.Cell;
import metamodels.Attribute;
import metamodels.Class;
import metamodels.Model;
import metamodels.Relation;
import sun.net.www.protocol.http.AuthenticationHeader;
import utils.sqlite.SQLiteConn;
import utils.transformations.Model2Model;
import utils.transformations.Model2Text;
import whitepages.Cellphone;
import whitepages.Person;
import worker.Worker;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        startProgram();
        //testORM();
    }

    public static String getModelChoice() {
        System.out.println("Which model do you want ? (Answer with the number of the desired model)");
        System.out.println("1 - BookStore");
        System.out.println("2 - Person");
        System.out.println("3 - Person(XMI model)");
        System.out.println("4- WhitePages");
        System.out.println("5- Worker");

        String modelChoice = sc.nextLine();
        while (!modelChoice.equals("1") && !modelChoice.equals("2") && !modelChoice.equals("3")
                && !modelChoice.equals("4") && !modelChoice.equals("5")) {
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
        String languageChoice = "";
        if (modelChoice.equals("3")) {
            Model model = Model2Model.getModel("src/models/person.xmi", false);
            buildModel(model);
        } else {
            languageChoice = getLanguageChoice();
        }


        if (modelChoice.equals("1") && languageChoice.equals("1")) {
            buildModel(getBookStoreModel());
        }
        if (modelChoice.equals("2") && languageChoice.equals("1")) {
            buildModel(getPersonModel());
        }
        if (modelChoice.equals("1") && languageChoice.equals("2")) {
            Model model = Model2Model.getModel("src/models/bookstore.xml", true);
            buildModel(model);
        }
        if (modelChoice.equals("2") && languageChoice.equals("2")) {
            Model model = Model2Model.getModel("src/models/person.xml", true);
            buildModel(model);
        }
        if (modelChoice.equals("4") && languageChoice.equals("1")) {
            buildModel(getWhitePagesModel());
        }
        if (modelChoice.equals("4") && languageChoice.equals("2")) {
            Model model = Model2Model.getModel("src/models/whitepages.xml", true);
            buildModel(model);
        }
        if (modelChoice.equals("5") && languageChoice.equals("1")) {
            buildModel(getWorkerModel());
        }
        if (modelChoice.equals("5") && languageChoice.equals("2")) {
            Model model = Model2Model.getModel("src/models/worker.xml", true);
            buildModel(model);
        }
    }

    public static Model getWhitePagesModel(){
        Model model = new Model("Whitepages");
        Class person = new Class("Person");
        person.addAttribute(new Attribute("first_name", "String"));
        person.addAttribute(new Attribute("last_name", "String"));
        person.setPkg(model.getName().toLowerCase());

        Class cellPhone = new Class("CellPhone");
        cellPhone.addAttribute(new Attribute("number", "int"));
        cellPhone.setPkg(model.getName().toLowerCase());

        Relation relation = new Relation(person, cellPhone, "12N", true, false);
        person.addRelation(relation);
        cellPhone.addRelation(relation);

        model.addClass(person);
        model.addClass(cellPhone);
        return model;
    }

    public static Model getWorkerModel(){
        Model model = new Model("Worker");
        Class person = new Class("Person");
        person.addAttribute(new Attribute("first_name", "String"));
        person.addAttribute(new Attribute("last_name", "String"));
        person.setPkg(model.getName().toLowerCase());

        Class worker = new Class("Worker");
        worker.addAttribute(new Attribute("job", "String"));
        worker.addAttribute(new Attribute("jobDescription", "String"));
        worker.addAttribute(new Attribute("salary", "int"));
        worker.setPkg(model.getName().toLowerCase());

        Relation relation = new Relation(person, worker, "121", true, false);
        person.addRelation(relation);
        worker.addRelation(relation);

        model.addClass(person);
        model.addClass(worker);
        return model;
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

        Relation relation = new Relation(book, author, "N2N", false, true);
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
        File f = new File("src/" + model.getName().toLowerCase());
        f.mkdirs();

        SQLiteConn sqLiteConn = new SQLiteConn("src/" + model.getName().toLowerCase() + "/" + model.getName().toLowerCase() + ".db");
        sqLiteConn.execute(sqlTables);

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

        //Bookstore N-N
        /*Author a1 = new Author();
        a1.setFirst_name("Lucas");
        a1.setLast_name("Fischer");
        a1.setEmail("lucas@gmail.com");

        Author a2 = new Author();
        a2.setFirst_name("Lucas");
        a2.setLast_name("Fischer");
        a2.setEmail("lucas@gmail.com");

        Book b1 = new Book("11/05/2017",20);
        b1.setPrice(20);
        b1.setTitle("Livro com 2 autores");
        b1.addAuthor(a1);
        b1.addAuthor(a2);
        b1.save();
        System.out.println(Book.get("1").getAuthor());
        System.out.println(Book.where("title = 'Livro com 2 autores'"));
        System.out.println(Author.get("1").getBooks());*/

        //WhitePages 1-N
        /*Person person1 = new Person("Lucas");
        person1.setLast_name("Fischer");
        Cellphone c1 = new Cellphone(912345678);
        Cellphone c2 = new Cellphone(123456789);
        person1.addCellphone(c1);
        person1.addCellphone(c2);
        person1.save(); //no need to save the cellphones, it does that automaticly
        System.out.println(Person.get("1").getCellphone());
        System.out.println(Cellphone.get("2").getPerson());*/

        //Worker 1-1
        /*worker.Person p1 = new worker.Person();
        p1.setFirst_name("Lucas");
        p1.setLast_name("Fischer");
        Worker w1 = new Worker();
        w1.setJob("Tirador de 20s");
        w1.setJobdescription("Pessoa que so tira 20s nos projetos de DBM");
        w1.setSalary(20.0);
        p1.setWorker(w1);
        p1.save();
        System.out.println(worker.Person.get("2").getWorker());
        System.out.println(Worker.get("1").getPerson());*/

        //Person
        /*person.Person p1 = new person.Person("Lucas",20);
        person.Person p2 = new person.Person("Daniel", 21);
        p1.save();
        p2.save();
        System.out.println(person.Person.all());
        System.out.println(person.Person.where("name = 'Lucas'"));
        System.out.println(person.Person.get("2"));*/

    }
}