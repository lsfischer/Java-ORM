package utils.transformations;

import metamodels.Attribute;
import metamodels.Class;
import metamodels.Model;
import metamodels.Relation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.naming.directory.AttributeInUseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Lucas on 17-Apr-17.
 */
public class Model2Model {

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

            Model model = new Model(modelName);

            model.addVariousClasses(getClasses(modelNode));

            for(Class c : model.getClasses()){
                for(Relation relation : getRelations(modelNode)){

                    //If the Class is in a relation, add that relation to the class
                    if(relation.getRegularClass().getName().equals(c.getName())
                            || relation.getForeignClass().getName().equals(c.getName())){
                        c.addRelation(relation);
                    }
                }
            }
            return model;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Class> getClasses(Node modelNode) {
        ArrayList<Class> classes = new ArrayList<>();
        NodeList nList = modelNode.getChildNodes();
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE && nNode.getNodeName() == "class") {
                Class clazz = new Class(nNode.getAttributes().getNamedItem("name").getNodeValue());
                clazz.setPkg(modelNode.getAttributes().getNamedItem("name").getNodeValue().toLowerCase());
                clazz.addVariousAttributes(getAttributes(nNode));
                //clazz.addVariousRelations(getRelations(nNode));
                classes.add(clazz);
            }
        }
        return classes;
    }

    public static ArrayList<Relation> getRelations(Node modelNode) {
        ArrayList<Relation> relations = new ArrayList<>();
        NodeList nList = modelNode.getChildNodes();
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE && nNode.getNodeName() == "foreignKey") {
                Class regularClass = new Class(nNode.getAttributes().getNamedItem("firstClass").getNodeValue());
                Class foreignClass = new Class(nNode.getAttributes().getNamedItem("secondClass").getNodeValue());
                Relation relation = new Relation(regularClass,foreignClass,nNode.getAttributes().getNamedItem("type").getNodeValue());
                relations.add(relation);
            }
        }
        return relations;
    }


    public static ArrayList<Attribute> getAttributes(Node classNode) {

        ArrayList<Attribute> attributes = new ArrayList<>();
        NodeList classChilds = classNode.getChildNodes();

        for (int j = 0; j < classChilds.getLength(); j++) {

            Node childNode = classChilds.item(j);

            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                if (childNode.getNodeName() == "attribute") {
                    String name = childNode.getAttributes().getNamedItem("name").getNodeValue();
                    String type = childNode.getAttributes().getNamedItem("type").getNodeValue();
                    Attribute attribute = new Attribute(name, type);
                    attributes.add(attribute);
                }
            }
        }
        return attributes;
    }
/*
    public static ArrayList<Relation> getRelations(Node classNode) {

        ArrayList<Relation> relations = new ArrayList<>();
        NodeList classChilds = classNode.getChildNodes();

        for (int j = 0; j < classChilds.getLength(); j++) {

            Node childNode = classChilds.item(j);

            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                if (childNode.getNodeName() == "foreignKey") {
                    Class foreignClass = new Class(childNode.getAttributes().getNamedItem("name").getNodeValue());
                    Relation relation = new Relation(foreignClass, childNode.getAttributes().getNamedItem("type").getNodeValue());
                    relations.add(relation);
                }
            }
        }
        return relations;
    }
    */
}
