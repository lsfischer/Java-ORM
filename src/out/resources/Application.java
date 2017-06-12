/*
* Simple Spark web application
*
*/
import person.Person;
import utils.FreemarkerEngine;

import java.util.HashMap;

import static spark.Spark.*;

public class Application {
    public static void main(String[] args) {
        // Configure Spark
        port(8000);
        staticFiles.externalLocation("src/resources");

        // Configure freemarker engine
        FreemarkerEngine engine = new FreemarkerEngine("src/resources/templates");

        // Set up endpoints
                get("/", (request, response) -> {
        return engine.render(null, "index.html");
        });

        get("/author/list", (request, response) -> {
        HashMap<String, Object> model = new HashMap<>();
        model.put("objs", Author.all());
        return engine.render(model,"author/list.html");
        });

        get("/author/get", (request, response) -> {
        HashMap<String, Object> model = new HashMap<>();
        Author author = Author.get(request.queryParams("id"));
        model.put("obj",author);
        return engine.render(model,"author/get.html");
        });

        post("/author/update", (request, response) -> {
        Author obj = Author.get(request.queryParams("id"));
        obj.setFirst_name(request.queryParams("first_name"));
        obj.setLast_name(request.queryParams("last_name"));
        obj.setEmail(request.queryParams("email"));
        obj.save();
        response.redirect("get?id="+obj.getId());
        return null;
        });

        get("/author/delete", (request, response) ->{
            Author author = Author.get(request.queryParams("id"));
            author.delete();
            response.redirect("list");
            return null;
        });

        get("/author/create", (request, response) ->{
            return engine.render(null, "author/create.html");
        });

        post("/author/create", (request, response) ->{
            Author author = new Author();
            author.setFirst_name(request.queryParams("first_name"));
            author.setLast_name(request.queryParams("last_name"));
            author.setEmail(request.queryParams("email"));
            response.redirect("list");
            return null;
        });

        get("/", (request, response) -> {
        return engine.render(null, "index.html");
        });

        get("/book/list", (request, response) -> {
        HashMap<String, Object> model = new HashMap<>();
        model.put("objs", Book.all());
        return engine.render(model,"book/list.html");
        });

        get("/book/get", (request, response) -> {
        HashMap<String, Object> model = new HashMap<>();
        Book book = Book.get(request.queryParams("id"));
        model.put("obj",book);
        return engine.render(model,"book/get.html");
        });

        post("/book/update", (request, response) -> {
        Book obj = Book.get(request.queryParams("id"));
        obj.setTitle(request.queryParams("title"));
        obj.setPubdate(request.queryParams("pubDate"));
        obj.setPrice(Double.parseDouble(request.queryParams("price")));
        obj.setQuantity(Integer.parseInt(request.queryParams("quantity")));
        obj.save();
        response.redirect("get?id="+obj.getId());
        return null;
        });

        get("/book/delete", (request, response) ->{
            Book book = Book.get(request.queryParams("id"));
            book.delete();
            response.redirect("list");
            return null;
        });

        get("/book/create", (request, response) ->{
            return engine.render(null, "book/create.html");
        });

        post("/book/create", (request, response) ->{
            Book book = new Book();
            book.setTitle(request.queryParams("title"));
            book.setPubdate(request.queryParams("pubDate"));
            book.setPrice(Double.parseDouble(request.queryParams("price")));
            book.setQuantity(Integer.parseInt(request.queryParams("quantity")));
            response.redirect("list");
            return null;
        });

    }
}