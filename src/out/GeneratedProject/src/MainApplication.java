import endpoints.*;
import static spark.Spark.*;
import utils.FreemarkerEngine;

public class MainApplication {

    public static void main(String[] args) {

        // Configure Spark
        port(8000);
        staticFiles.externalLocation("src/resources");

        // Configure freemarker engine
        FreemarkerEngine engine = new FreemarkerEngine("src/resources/templates");

        Application.htmlEndPoints(engine);
        ApplicationREST.restEndPoints(engine);
    }
}