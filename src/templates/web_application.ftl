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

        // Set up endpoints
        get("/", (request, response) -> {
            return engine.render(null, "index.html");
        });

        <#list classes as class>


        // Set up ${class.name} endpoints
        get("/${class.name?lower_case}/list", (request, response) -> {
            HashMap<Object,Object> n = new HashMap<>();
            n.put("objs",${class.name}.all());
            return engine.render(n,"${class.name?lower_case}/list.html");
        });


        get("/${class.name?lower_case}/get", (request, response) -> {
            HashMap<Object,Object> n = new HashMap<>();
            ${class.name} objct = ${class.name}.get(Integer.parseInt(request.queryParams("id")));
            n.put("obj",objct);
            return engine.render(n,"${class.name?lower_case}/get.html");
        });


        post("/${class.name?lower_case}/update", (request, response) -> {
            ${class.name} ${class.name?lower_case} = ${name}.get(Integer.parseInt(request.queryParams("id")));

            <#list class.attributes as attr>
                <#if attr.type == "int">
            ${class.name?lower_case}.set${attr.name?capitalize}(Integer.parseInt(request.queryParams("${attr.name?lower_case}")));
                </#if>
                <#if attr.type == "double">
            ${class.name?lower_case}.set${attr.name?capitalize}(Double.parseDouble(request.queryParams("${attr.name?lower_case}")));
                </#if>
                <#if attr.type == "float">
            ${class.name?lower_case}.set${attr.name?capitalize}(Float.parseFloat(request.queryParams("${attr.name?lower_case}")));
                </#if>
                <#if attr.type != "float" && attr.type != "int" && attr.type != "double">
            ${class.name?lower_case}.set${attr.name?capitalize}(request.queryParams("${attr.name?lower_case}"));
                </#if>
            </#list>
            ${class.name?lower_case}.save();

            response.redirect("/${class.name?lower_case}/list");
            return null;
        });

        get("/${class.name?lower_case}/delete", (request, response) -> {
            ${class.name} ${class.name?lower_case} = ${class.name}.get(Integer.parseInt(request.queryParams("id")));
            ${class.name?lower_case}.delete();

            response.redirect("/${class.name?lower_case}/list");
            return null;
        });

        get("/${class.name?lower_case}/create", (request, response) -> {

            return engine.render(null,"${class.name?lower_case}/create.html");
        });

        post("/${class.name?lower_case}/create", (request, response) -> {
            ${class.name} ${class.name?lower_case} = ${class.name}.get(Integer.parseInt(request.queryParams("id")));

            <#list class.attributes as attr>
                <#if attr.type == "int">
            ${class.name?lower_case}.set${attr.name?capitalize}(Integer.parseInt(request.queryParams("${attr.name?lower_case}")));
                </#if>
                <#if attr.type == "double">
            ${class.name?lower_case}.set${attr.name?capitalize}(Double.parseDouble(request.queryParams("${attr.name?lower_case}")));
                </#if>
                <#if attr.type == "float">
            ${class.name?lower_case}.set${attr.name?capitalize}(Float.parseFloat(request.queryParams("${attr.name?lower_case}")));
                </#if>
                <#if attr.type != "float" && attr.type != "int" && attr.type != "double">
            ${class.name?lower_case}.set${attr.name?capitalize}(request.queryParams("${attr.name?lower_case}"));
                </#if>
            </#list>

            ${class.name?lower_case}.save();

            response.redirect("/${class.name?lower_case}/list");
            return null;
        });

        </#list>
    }


}