package utils.transformations;

import metamodels.Attribute;
import metamodels.Class;
import metamodels.Model;
import metamodels.Relation;
import org.w3c.dom.*;

import javax.naming.directory.AttributeInUseException;
import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Lucas on 17-Apr-17.
 */
public class Model2Model {

    public static Document getDocument(boolean needed, String filename) {
        if (needed) {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(true);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(new File(filename));
                return document;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(true);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(getModelFromXMI(filename));
                return document;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public static Model getModel(String filename, boolean fromXML) {
        try {
            Document document;
            if(fromXML){
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(true);
                DocumentBuilder db = dbf.newDocumentBuilder();
                 document = db.parse(new File(filename));
            }else{
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                 document = db.parse(getModelFromXMI(filename));
            }



            // Get model node
            Node modelNode = document.getDocumentElement();
            String modelName = modelNode.getAttributes()
                    .getNamedItem("name").getNodeValue();

            Model model = new Model(modelName);

            model.addVariousClasses(getClasses(modelNode));

            for (Class c : model.getClasses()) {
                for (Relation relation : getRelations(modelNode)) {

                    //If the Class is in a relation, add that relation to the class
                    if (relation.getRegularClass().getName().equals(c.getName())
                            || relation.getForeignClass().getName().equals(c.getName())) {
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
                Relation relation = new Relation(regularClass, foreignClass, nNode.getAttributes().getNamedItem("type").getNodeValue());
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
                    String required = "false";
                    //TODO VERIFICAR SE VEM DO XML OU DO XMI
                    /*
                    if(childNode.getAttributes().getNamedItem("required").getNodeValue() != null){
                         required = childNode.getAttributes().getNamedItem("required").getNodeValue();
                    }
*/
                    Attribute attribute = new Attribute(name, type);
                    if (Boolean.parseBoolean(required)) {
                        attribute.setRequired();
                    }
                    attributes.add(attribute);
                }
            }
        }
        return attributes;
    }

    public static String getModelFromXMI(String filename) {
        //TODO MELHORAR ISTO, DIVIDIR EM SUBMETODOS
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //dbf.setValidating(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(new File(filename));

            // Get model node
            Node modelNode = document.getDocumentElement();
            String modelName = modelNode.getAttributes()
                    .getNamedItem("name").getNodeValue();

            Document doc = db.newDocument();
            Element rootElem = doc.createElement("model");
            rootElem.setAttribute("name", modelName);
            doc.appendChild(rootElem);

            NodeList nList = modelNode.getChildNodes();
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE && nNode.getNodeName() == "packagedElement") {

                    Element classElem = doc.createElement("class");//Creates a <class> tag

                    classElem.setAttribute("name", nNode.getAttributes().getNamedItem("name").getNodeValue());//Sets a name attribute to <class> tag
                    //checks <class> tag sub-tags
                    NodeList classAttributes = nNode.getChildNodes();
                    for (int j = 0; j < classAttributes.getLength(); j++) {
                        Node node = classAttributes.item(j);
                        if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName() == "ownedAttribute") {
                            Element attributeElem = doc.createElement("attribute");
                            attributeElem.setAttribute("name", node.getAttributes().getNamedItem("name").getNodeValue());

                            NodeList attributeType = node.getChildNodes();
                            for (int k = 0; k < attributeType.getLength(); k++) {
                                Node typeNode = attributeType.item(k);
                                if (typeNode.getNodeType() == Node.ELEMENT_NODE && typeNode.getNodeName() == "type") {
                                    String type = typeNode.getAttributes().getNamedItem("href").getNodeValue();
                                    String[] splited = type.split("#");
                                    if (splited[1].equals("Integer"))
                                        splited[1] = "int";
                                    attributeElem.setAttribute("type", splited[1]);
                                }
                            }
                            classElem.appendChild(attributeElem);
                        }
                    }
                    rootElem.appendChild(classElem);
                }
            }

            //Write to a xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            //TODO MUDAR O NOME DO FICHEIRO
            StreamResult result = new StreamResult("src/models/person2.xml");
            transformer.transform(source, result);

            //TODO MUDAR ISTO PARA SER GENERICO
            return "src/models/person2.xml";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
