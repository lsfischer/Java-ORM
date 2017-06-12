import metamodels.Attribute;
import metamodels.Class;
import metamodels.Model;
import metamodels.Relation;
import utils.sqlite.SQLiteConn;
import utils.transformations.Model2Model;
import utils.transformations.Model2Text;

import java.io.*;
import java.util.Scanner;


public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        //startProgram();
        //testORM();
        buildServerApp(Model2Model.getModel("src/models/bookstore.xml", true));
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

    public static Model getWhitePagesModel() {
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

    public static Model getWorkerModel() {
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
    }

    public static void buildModelToServer(Model model) {
        Model2Text model2Text = new Model2Text("src/templates");
        String sqlTables = model2Text.render(model, "sqlite3_create.ftl");
        System.out.println(sqlTables);
        File f = new File("src/out/" + model.getName().toLowerCase());
        f.mkdirs();

        SQLiteConn sqLiteConn = new SQLiteConn("src/out/" + model.getName().toLowerCase() + "/" + model.getName().toLowerCase() + ".db");
        sqLiteConn.execute(sqlTables);

        for (Class c : model.getClasses()) {
            String javaClasses = model2Text.render(c, "java_class.ftl");
            System.out.println(javaClasses);
            try {
                File fout = new File("src/out/" + model.getName().toLowerCase() + "/" + c.getName() + ".java");
                FileOutputStream fos = new FileOutputStream(fout);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                bw.write(javaClasses);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void buildServerApp(Model model) {
        buildModelToServer(model);
        buildWebIndex(model);
        buildWebList(model);
        buildWebGet(model);
        buildWebCreate(model);
        buildApplication(model);
    }

    private static void createFile(String parsedFile, Model model, String path) {
        try {
            File fout = new File(path);
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(parsedFile);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Web_index
    public static void buildWebIndex(Model model) {
        Model2Text model2Text = new Model2Text("src/templates");
        String javaClasses = model2Text.render(model, "web_index.ftl");
        System.out.println(javaClasses);
        File f = new File("src/out/resources/templates/");
        f.mkdirs();
        createFile(javaClasses, model, "src/out/resources/templates/index.html");
    }

    //Web_list
    public static void buildWebList(Model model) {
        Model2Text model2Text = new Model2Text("src/templates");
        for (Class c : model.getClasses()) {
            File f = new File("src/out/resources/templates/" + model.getName().toLowerCase() + "/" + c.getName().toLowerCase());
            f.mkdirs();
            String javaClasses = model2Text.render(c, "web_list.ftl");
            System.out.println(javaClasses);
            createFile(javaClasses, model, "src/out/resources/templates/" + model.getName().toLowerCase() + "/" + c.getName().toLowerCase() + "/list.html");
        }
    }


    //Web_get
    public static void buildWebGet(Model model) {
        Model2Text model2Text = new Model2Text("src/templates");

        for (Class c : model.getClasses()) {
            String javaClasses = model2Text.render(c, "web_get.ftl");
            System.out.println(javaClasses);
            createFile(javaClasses, model, "src/out/resources/templates/" + model.getName().toLowerCase() + "/" + c.getName().toLowerCase() + "/get.html");
        }
    }

    //Web_create
    public static void buildWebCreate(Model model) {
        Model2Text model2Text = new Model2Text("src/templates");
        for (Class c : model.getClasses()) {
            String javaClasses = model2Text.render(c, "web_create.ftl");
            System.out.println(javaClasses);
            createFile(javaClasses, model, "src/out/resources/templates/" + model.getName().toLowerCase() + "/" + c.getName().toLowerCase() + "/create.html");
        }
    }

    public static void buildApplication(Model model) {
        Model2Text model2Text = new Model2Text("src/templates");
        String applicationJava = model2Text.render(model, "web_Application.ftl");
        System.out.println(applicationJava);
        createFile(applicationJava, model, "src/out/Application.java");
    }
}