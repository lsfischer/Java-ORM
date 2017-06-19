/*
 * Freemarker template engine
 *
 */

package utils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerEngine {

    private Configuration cfg;

    public FreemarkerEngine(String pathname) {
        try {
            cfg = new Configuration(Configuration.VERSION_2_3_25); //mudar para version 2_3_26
            cfg.setDirectoryForTemplateLoading(new File(pathname));
            cfg.setDefaultEncoding("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Renders a model using a template.
     *
     * @param model the model
     * @param name  the template name
     * @return      String
     */
    public String render(Object model, String name) {
        try {
            Template template = cfg.getTemplate(name);
            Writer out = new StringWriter();
            template.process(model, out);
            return out.toString();
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
