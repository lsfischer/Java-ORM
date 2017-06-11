/*
 * Simple Spark web application
 *
 */

<#list classes as class>
import ${class.name?lower_case}.${class.name};
</#list>
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

        <#list classes as class>
        // Set up endpoints
        get("/", (request, response) -> {
            return engine.render(null, "index.html");
        });

        // Set up Person endpoints
        get("/${class.name?lower_case}/list", (request, response) -> {
            HashMap<Object,Object> n = new HashMap<>();
            n.put("objs",${class.name}.all());
            return engine.render(n,"${class.name?lower_case}/list.html");
        });


        get("/person/get", (request, response) -> {
            HashMap<Object,Object> n = new HashMap<>();
            ${class.name} objct = ${class.name}.get(Integer.parseInt(request.queryParams("id")));
            n.put("obj",objct);
            return engine.render(n,"${class.name?lower_case}/get.html");
        });


        post("/person/update", (request, response) -> {
            ${class.name} obj = ${name}.get(Integer.parseInt(request.queryParams("id")));

            <#list class.attributes as attr>
                <#if attr.type == "int">
                    obj.set${attr.name?capitalize}(Integer.parseInt(request.queryParams("${attr.name?lower_case}")));
                </#if>
                <#if attr.type == "double">
                    obj.set${attr.name?capitalize}(Double.parseDouble(request.queryParams("${attr.name?lower_case}")));
                </#if>
                <#if attr.type == "float">
                    obj.set${attr.name?capitalize}(Float.parseFloat(request.queryParams("${attr.name?lower_case}")));
                </#if>
                <#if attr.type != "float" && attr.type != "int" && attr.type != "double">
                    obj.set${attr.name?capitalize}(request.queryParams("${attr.name?lower_case}"));
                </#if>

            </#list>
            obj.save();

            response.redirect("/${class.name?lower_case}/list");
            return null;
        });

        get("/person/delete", (request, response) -> {
            Person pessoa = Person.get(Integer.parseInt(request.queryParams("id")));
            pessoa.delete();

            response.redirect("/person/list");
            return null;
        });

        get("/person/create", (request, response) -> {

            return engine.render(null,"person/create.html");
        });

        post("/person/create", (request, response) -> {
            Person pessoa = new Person();
            pessoa.setName(request.queryParams("name"));
            pessoa.setAge(Integer.parseInt(request.queryParams("age")));
            pessoa.save();

            response.redirect("/person/list");
            return null;
        });

        </#list>
    }


}