package endpoints;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utils.FreemarkerEngine;
import java.util.ArrayList;
import java.util.HashMap;
import static spark.Spark.*;
<#list classes as class>
import ${name?lower_case}.${class.name};
</#list>

public class ApplicationREST {

    public static void restEndPoints(FreemarkerEngine engine) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        <#list classes as class>
        get("/api/${class.name?lower_case}/", (request, response) -> {
            ArrayList<${class.name}> ${class.name?lower_case}s = ${class.name}.all();
            for(${class.name} obj : ${class.name?lower_case}s){
            <#list class.relations as rels>
            <#if class.name == rels.foreignClass.name>
                obj.get${rels.regularClass.name}s();
            </#if>
            </#list>
            }
            String jsonStr = gson.toJson(${class.name?lower_case}s);
            response.type("application/json");
            return jsonStr;
            });

        get("/api/${class.name?lower_case}/:id/", (request, response) -> {
            String id = request.params(":id");
            ${class.name} ${class.name?lower_case} = ${class.name}.get(id);
            <#list class.relations as rels>
            <#if class.name == rels.foreignClass.name>
            ${class.name?lower_case}.get${rels.regularClass.name}s();
            </#if>
            </#list>
            if (${class.name?lower_case} == null) {
             response.status(404);
            } else {
                String jsonStr = gson.toJson(${class.name?lower_case});
                response.type("application/json");
                return jsonStr;
            }
            return null;
        });

        </#list>

    }
}