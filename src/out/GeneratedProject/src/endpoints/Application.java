package endpoints;
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

    public static void htmlEndPoints(FreemarkerEngine engine) {

        // Set up endpoints
        get("/", (request, response) -> {
            return engine.render(null, "index.html");
        });

        // Set up Author endpoints
        get("/author/list", (request, response) -> {
            HashMap<Object,Object> model = new HashMap<>();
            model.put("objs",Author.all());
            return engine.render(model,"bookstore/author/list.html");
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
            Book book = Book.get(request.queryParams("foreignList"));
            obj.addBook(book);
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
            HashMap<Object,Object> model = new HashMap<>();
	    model.put("foreignObjs",Book.all());
            return engine.render(model,"bookstore/author/create.html");
        });

        post("/author/create", (request, response) -> {

            Author obj = new Author();
            if(request.queryParams("book_id") != null){
                String books[] = request.queryMap("book_id").values();
                for(int i = 0; i < books.length;i++){
                        Book book = Book.get(books[i]);
                        obj.addBook(book);
                }
            }
            obj.setFirst_name(request.queryParams("first_name"));
            obj.setLast_name(request.queryParams("last_name"));
            obj.setEmail(request.queryParams("email"));
            obj.save();

            response.redirect("/author/list");
            return null;

        });


        // Set up Book endpoints
        get("/book/list", (request, response) -> {
            HashMap<Object,Object> model = new HashMap<>();
            model.put("objs",Book.all());
            return engine.render(model,"bookstore/book/list.html");
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
            Author author = Author.get(request.queryParams("foreignList"));
            obj.addAuthor(author);
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
            HashMap<Object,Object> model = new HashMap<>();
	    model.put("foreignObjs",Author.all());
            return engine.render(model,"bookstore/book/create.html");
        });

        post("/book/create", (request, response) -> {

            Book obj = new Book("", 0);
            if(request.queryParams("author_id") != null){
                String authors[] = request.queryMap("author_id").values();
                for(int i = 0; i < authors.length;i++){
                    Author author = Author.get(authors[i]);
                    obj.addAuthor(author);
                }
            }
            obj.setTitle(request.queryParams("title"));
            obj.setPubdate(request.queryParams("pubdate"));
            obj.setPrice(Double.parseDouble(request.queryParams("price")));
            obj.setQuantity(Integer.parseInt(request.queryParams("quantity")));
            obj.save();

            response.redirect("/book/list");
            return null;

        });

    }


}
