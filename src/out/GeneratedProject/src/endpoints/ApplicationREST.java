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

        post("/api/author/", (request, response) -> {
            String json = request.body();
            if (json.isEmpty()) {
                response.status(400);
                return "";
            } else {
                Author fromJson = gson.fromJson(json, Author.class);
                fromJson.setId(0);
                fromJson.save();
                response.status(201);
                return gson.toJson(fromJson);
            }
        });

        put("/api/author/:id/", (request, response) -> {
            String jsonStr = request.body();
            if (jsonStr.isEmpty()) {
                response.status(400);
                return "";
            } else {
                Author fromJson = gson.fromJson(jsonStr, Author.class);
                String id = request.params(":id");
                Author objectToUpdate = Author.get(id);
                objectToUpdate = fromJson;
                objectToUpdate.save();
                response.type("application/json");
                return gson.toJson(objectToUpdate);
            }
        });

        delete("/api/author/:id/", (request, response) -> {
            String id = request.params(":id");
            Author toDelete = Author.get(id);
            if (toDelete == null) {
                response.status(404); //podiamos omitir porque ao fazermos return null ele ja nos da 404
                return null;
            } else {
                toDelete.delete();
                response.redirect("/api/author/");
                return null;
            }
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

        post("/api/book/", (request, response) -> {
            String json = request.body();
            if (json.isEmpty()) {
                response.status(400);
                return "";
            } else {
                Book fromJson = gson.fromJson(json, Book.class);
                fromJson.setId(0);
                fromJson.save();
                response.status(201);
                return gson.toJson(fromJson);
            }
        });

        put("/api/book/:id/", (request, response) -> {
            String jsonStr = request.body();
            if (jsonStr.isEmpty()) {
                response.status(400);
                return "";
            } else {
                Book fromJson = gson.fromJson(jsonStr, Book.class);
                String id = request.params(":id");
                Book objectToUpdate = Book.get(id);
                objectToUpdate = fromJson;
                objectToUpdate.save();
                response.type("application/json");
                return gson.toJson(objectToUpdate);
            }
        });

        delete("/api/book/:id/", (request, response) -> {
            String id = request.params(":id");
            Book toDelete = Book.get(id);
            if (toDelete == null) {
                response.status(404); //podiamos omitir porque ao fazermos return null ele ja nos da 404
                return null;
            } else {
                toDelete.delete();
                response.redirect("/api/book/");
                return null;
            }
        });

    }
}