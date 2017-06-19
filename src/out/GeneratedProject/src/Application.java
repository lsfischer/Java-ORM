/*
 * Simple Spark web application
 *
 */

import bookstore.Author;
import bookstore.Book;
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

        // Set up Person endpoints
        get("/author/list", (request, response) -> {
            HashMap<Object,Object> n = new HashMap<>();
            n.put("objs",Author.all());
            return engine.render(n,"bookstore/author/list.html");
        });


        get("/author/get", (request, response) -> {
            HashMap<Object,Object> model = new HashMap<>();
            Author objct = Author.get(request.queryParams("id"));
            model.put("obj",objct);
            model.put("relation",objct.getBooks());

            return engine.render(model,"bookstore/author/get.html");
        });


        post("/author/update", (request, response) -> {
            Author obj = Author.get(request.queryParams("id"));

            obj.setFirst_name(request.queryParams("first_name"));
            obj.setLast_name(request.queryParams("last_name"));
            obj.setEmail(request.queryParams("email"));
            obj.save();

            response.redirect("/author/list");
            return null;
        });

        get("/author/delete", (request, response) -> {
            Author obj = Author.get(request.queryParams("id"));
            obj.delete();

            response.redirect("/author/list");
            return null;
        });

        get("/author/create", (request, response) -> {
        HashMap<Object,Object> n = new HashMap<>();
                n.put("foreignObjs",Book.all());
        return engine.render(n,"bookstore/author/create.html");
        });

        post("/author/create", (request, response) -> {
            Author obj = new Author();
            obj.setFirst_name(request.queryParams("first_name"));
            obj.setLast_name(request.queryParams("last_name"));
            obj.setEmail(request.queryParams("email"));
            obj.save();
            obj.set__name__("Author_"+obj.getId());
            obj.save();

            response.redirect("/author/list");
            return null;

        });

        // Set up endpoints
        get("/", (request, response) -> {
            return engine.render(null, "index.html");
        });

        // Set up Person endpoints
        get("/book/list", (request, response) -> {
            HashMap<Object,Object> n = new HashMap<>();
            n.put("objs",Book.all());
            return engine.render(n,"bookstore/book/list.html");
        });


        get("/book/get", (request, response) -> {
            HashMap<Object,Object> model = new HashMap<>();
            Book objct = Book.get(request.queryParams("id"));
            model.put("obj",objct);
            model.put("relation",objct.getAuthor());

            return engine.render(model,"bookstore/book/get.html");
        });


        post("/book/update", (request, response) -> {
            Book obj = Book.get(request.queryParams("id"));

            obj.setTitle(request.queryParams("title"));
            obj.setPubdate(request.queryParams("pubdate"));
            obj.setPrice(Double.parseDouble(request.queryParams("price")));
            obj.setQuantity(Integer.parseInt(request.queryParams("quantity")));
            obj.save();

            response.redirect("/book/list");
            return null;
        });

        get("/book/delete", (request, response) -> {
            Book obj = Book.get(request.queryParams("id"));
            obj.delete();

            response.redirect("/book/list");
            return null;
        });

        get("/book/create", (request, response) -> {
        HashMap<Object,Object> n = new HashMap<>();
                n.put("foreignObjs",Author.all());
        return engine.render(n,"bookstore/book/create.html");
        });

        post("/book/create", (request, response) -> {
            Book obj = new Book("", 0);
            obj.setTitle(request.queryParams("title"));
            obj.setPubdate(request.queryParams("pubdate"));
            obj.setPrice(Double.parseDouble(request.queryParams("price")));
            obj.setQuantity(Integer.parseInt(request.queryParams("quantity")));
            obj.save();
            obj.set__name__("Book_"+obj.getId());
            obj.save();

            response.redirect("/book/list");
            return null;

        });

    }


}