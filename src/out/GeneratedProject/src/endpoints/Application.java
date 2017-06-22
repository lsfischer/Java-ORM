package endpoints;
/*
 * Simple Spark web application
 *
 */

import person.Person;
import utils.FreemarkerEngine;
import java.util.HashMap;

import static spark.Spark.*;

public class Application {

    public static void htmlEndPoints(FreemarkerEngine engine) {

        // Set up endpoints
        get("/", (request, response) -> {
            return engine.render(null, "index.html");
        });

        // Set up Person endpoints
        get("/person/list", (request, response) -> {
            HashMap<Object,Object> model = new HashMap<>();
            model.put("objs",Person.all());
            return engine.render(model,"person/person/list.html");
        });


        get("/person/get", (request, response) -> {
            HashMap<Object,Object> model = new HashMap<>();
            Person objct = Person.get(request.queryParams("id"));
            model.put("obj",objct);

            return engine.render(model,"person/person/get.html");
        });


        post("/person/update", (request, response) -> {
            Person obj = Person.get(request.queryParams("id"));

            obj.setName(request.queryParams("name"));
            obj.setAge(Integer.parseInt(request.queryParams("age")));
            obj.save();

            response.redirect("/person/list");
            return null;
        });

        get("/person/delete", (request, response) -> {
            Person obj = Person.get(request.queryParams("id"));
            obj.delete();
            response.redirect("/person/list");
            return null;
        });

        get("/person/create", (request, response) -> {
            HashMap<Object,Object> model = new HashMap<>();
            return engine.render(model,"person/person/create.html");
        });

        post("/person/create", (request, response) -> {

            Person obj = new Person("", 0);
            obj.setName(request.queryParams("name"));
            obj.setAge(Integer.parseInt(request.queryParams("age")));
            obj.save();

            response.redirect("/person/list");
            return null;

        });

    }


}
