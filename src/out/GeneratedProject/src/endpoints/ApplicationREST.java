package endpoints;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utils.FreemarkerEngine;
import java.util.ArrayList;
import java.util.HashMap;
import static spark.Spark.*;
import bookstore.Author;
import bookstore.Book;

public class ApplicationREST {

    public static void restEndPoints(FreemarkerEngine engine) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        get("/api/author/", (request, response) -> {
            ArrayList<Author> authors = Author.all();
            for(Author obj : authors){
                obj.getBooks();
            }
            String jsonStr = gson.toJson(authors);
            response.type("application/json");
            return jsonStr;
            });

        get("/api/author/:id/", (request, response) -> {
            String id = request.params(":id");
            Author author = Author.get(id);
            author.getBooks();
            if (author == null) {
             response.status(404);
            } else {
                String jsonStr = gson.toJson(author);
                response.type("application/json");
                return jsonStr;
            }
            return null;
        });

        get("/api/book/", (request, response) -> {
            ArrayList<Book> books = Book.all();
            for(Book obj : books){
            }
            String jsonStr = gson.toJson(books);
            response.type("application/json");
            return jsonStr;
            });

        get("/api/book/:id/", (request, response) -> {
            String id = request.params(":id");
            Book book = Book.get(id);
            if (book == null) {
             response.status(404);
            } else {
                String jsonStr = gson.toJson(book);
                response.type("application/json");
                return jsonStr;
            }
            return null;
        });


    }
}