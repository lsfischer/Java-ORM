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
        <#list classes as class>
        get("/", (request, response) -> {
        return engine.render(null, "index.html");
        });

        get("/${class.name?lower_case}/list", (request, response) -> {
        HashMap<String, Object> model = new HashMap<>();
        model.put("objs", ${class.name}.all());
        return engine.render(model,"${class.name?lower_case}/list.html");
        });

        get("/${class.name?lower_case}/get", (request, response) -> {
        HashMap<String, Object> model = new HashMap<>();
        ${class.name} ${class.name?lower_case} = ${class.name}.get(request.queryParams("id"));
        model.put("obj",${class.name?lower_case});
        return engine.render(model,"${class.name?lower_case}/get.html");
        });

        post("/${class.name?lower_case}/update", (request, response) -> {
        ${class.name} obj = ${class.name}.get(request.queryParams("id"));
        <#list class.attributes as attr>
        <#if attr.type == "int">
        obj.set${attr.name?capitalize}(Integer.parseInt(request.queryParams("${attr.name}")));
        </#if>
        <#if attr.type == "double">
        obj.set${attr.name?capitalize}(Double.parseDouble(request.queryParams("${attr.name}")));
        </#if>
        <#if attr.type == "float">
        obj.set${attr.name?capitalize}(Float.parseFloat(request.queryParams("${attr.name}")));
        </#if>
        <#if attr.type != "float" && attr.type != "int" && attr.type != "double">
        obj.set${attr.name?capitalize}(request.queryParams("${attr.name}"));
        </#if>
        </#list>
        obj.save();
        response.redirect("get?id="+obj.getId());
        return null;
        });

        get("/${class.name?lower_case}/delete", (request, response) ->{
            ${class.name} ${class.name?lower_case} = ${class.name}.get(request.queryParams("id"));
            ${class.name?lower_case}.delete();
            response.redirect("list");
            return null;
        });

        get("/${class.name?lower_case}/create", (request, response) ->{
            return engine.render(null, "${class.name?lower_case}/create.html");
        });

        post("/${class.name?lower_case}/create", (request, response) ->{
            ${class.name} ${class.name?lower_case} = new ${class.name}();
            <#list class.attributes as attr>
            <#if attr.type == "int">
            ${class.name?lower_case}.set${attr.name?capitalize}(Integer.parseInt(request.queryParams("${attr.name}")));
            </#if>
            <#if attr.type == "double">
            ${class.name?lower_case}.set${attr.name?capitalize}(Double.parseDouble(request.queryParams("${attr.name}")));
            </#if>
            <#if attr.type == "float">
            ${class.name?lower_case}.set${attr.name?capitalize}(Float.parseFloat(request.queryParams("${attr.name}")));
            </#if>
            <#if attr.type != "float" && attr.type != "int" && attr.type != "double">
            ${class.name?lower_case}.set${attr.name?capitalize}(request.queryParams("${attr.name}"));
            </#if>
            </#list>
            response.redirect("list");
            return null;
        });

        </#list>
    }
}