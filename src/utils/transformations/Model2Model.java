package utils.transformations;

import metamodels.Attribute;
import metamodels.Class;
import metamodels.Model;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Created by Lucas on 17-Apr-17.
 */
public class Model2Model {
    //Fazer isto melhor
    public static Model getModel(String filename) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(new File(filename));
            // Get model node
            Node modelNode = document.getDocumentElement();
            String modelName = modelNode.getAttributes()
                    .getNamedItem("name").getNodeValue();

            NodeList nList = modelNode.getChildNodes();
            Model model = new Model(modelName);
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Class clazz = new Class(nNode.getAttributes().getNamedItem("name").getNodeValue());
                    NodeList classChilds = nNode.getChildNodes();

                    for (int j = 0; j < classChilds.getLength(); j++) {
                        Node childNode = classChilds.item(j);

                        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                            if (childNode.getNodeName() == "attribute") {
                                String name = childNode.getAttributes().getNamedItem("name").getNodeValue();
                                String type = childNode.getAttributes().getNamedItem("type").getNodeValue();
                                Attribute attribute = new Attribute(name, type);
                                clazz.addAttribute(attribute);
                            }
                            if (childNode.getNodeName() == "foreignKey") {
                                Class c = new Class(childNode.getAttributes().getNamedItem("name").getNodeValue());
                                clazz.addForeignKey(c);
                            }
                        }
                    }
                    model.addClass(clazz);
                }
            }
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
