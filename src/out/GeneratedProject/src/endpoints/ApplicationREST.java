package endpoints;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utils.FreemarkerEngine;
import java.util.ArrayList;
import java.util.HashMap;
import static spark.Spark.*;
import person.Person;

public class ApplicationREST {

    public static void restEndPoints(FreemarkerEngine engine) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        get("/api/person/", (request, response) -> {
            ArrayList<Person> persons = Person.all();
            for(Person obj : persons){
            }
            String jsonStr = gson.toJson(persons);
            response.type("application/json");
            return jsonStr;
            });

        get("/api/person/:id/", (request, response) -> {
            String id = request.params(":id");
            Person person = Person.get(id);
            if (person == null) {
             response.status(404);
            } else {
                String jsonStr = gson.toJson(person);
                response.type("application/json");
                return jsonStr;
            }
            return null;
        });

        post("/api/person/", (request, response) -> {
            String json = request.body();
            if (json.isEmpty()) {
                response.status(400);
                return "";
            } else {
                Person fromJson = gson.fromJson(json, Person.class);
                fromJson.setId(0);
                fromJson.save();
                response.status(201);
                return gson.toJson(fromJson);
            }
        });

        put("/api/person/:id/", (request, response) -> {
            String jsonStr = request.body();
            if (jsonStr.isEmpty()) {
                response.status(400);
                return "";
            } else {
                Person fromJson = gson.fromJson(jsonStr, Person.class);
                String id = request.params(":id");
                Person objectToUpdate = Person.get(id);
                objectToUpdate = fromJson;
                objectToUpdate.save();
                response.type("application/json");
                return gson.toJson(objectToUpdate);
            }
        });

        delete("/api/person/:id/", (request, response) -> {
            String id = request.params(":id");
            Person toDelete = Person.get(id);
            if (toDelete == null) {
                response.status(404); //podiamos omitir porque ao fazermos return null ele ja nos da 404
                return null;
            } else {
                toDelete.delete();
                response.redirect("/api/person/");
                return null;
            }
        });

    }
}