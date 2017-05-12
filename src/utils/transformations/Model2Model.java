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
    /**
     * Metodo que permite retornar a tag <model></model>
     * @param filename nome do ficheiro xml
     * @param fromXML valor que indica se o ficheiro é em formato xml ou xmi
     * @return
     */
    public static Model getModel(String filename, boolean fromXML) {
        try {
            Document document;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            if (fromXML) {
                dbf.setValidating(true);
                document = db.parse(new File(filename));
            } else {
                document = db.parse(getModelFromXMI(filename));
            }

            // Get model node
            Node modelNode = document.getDocumentElement();
            String modelName = modelNode.getAttributes()
                    .getNamedItem("name").getNodeValue();

            Model model = new Model(modelName);

            model.addVariousClasses(getClasses(modelNode, fromXML));

            for (Class c : model.getClasses()) {
                for (Relation relation : getRelations(modelNode,fromXML)) {

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

    /**
     * Metodo que permite retornar todas as tags <class></class>
     * @param modelNode Nó xml "model"
     * @param fromXML valor que indica se o ficheiro é em formato xml ou xmi
     * @return
     */
    public static ArrayList<Class> getClasses(Node modelNode, boolean fromXML) {

        ArrayList<Class> classes = new ArrayList<>();
        NodeList nList = modelNode.getChildNodes();
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE && nNode.getNodeName() == "class") {
                Class clazz = new Class(nNode.getAttributes().getNamedItem("name").getNodeValue());
                clazz.setPkg(modelNode.getAttributes().getNamedItem("name").getNodeValue().toLowerCase());
                clazz.addVariousAttributes(getAttributes(nNode, fromXML));
                classes.add(clazz);
            }
        }
        return classes;
    }

    /**
     *  Metodo que permite retornar todas as tags <foreignKey></foreignKey>
     * @param modelNode Nó xml "model"
     * @param fromXML valor que indica se o ficheiro é em formato xml ou xmi
     * @return
     */
    public static ArrayList<Relation> getRelations(Node modelNode,boolean fromXML) {
        ArrayList<Relation> relations = new ArrayList<>();
        NodeList nList = modelNode.getChildNodes();
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE && nNode.getNodeName() == "foreignKey") {
                Class regularClass = new Class(nNode.getAttributes().getNamedItem("firstClass").getNodeValue());
                Class foreignClass = new Class(nNode.getAttributes().getNamedItem("secondClass").getNodeValue());
                String firstClassRequiredString = "true";
                String secondClassRequiredString = "false"; //Both classes cannont be required at the same time
                if(fromXML){
                    firstClassRequiredString = nNode.getAttributes().getNamedItem("firstClassRequired").getNodeValue();
                    secondClassRequiredString = nNode.getAttributes().getNamedItem("secondClassRequired").getNodeValue();
                }
                boolean firstClassRequired = Boolean.parseBoolean(firstClassRequiredString);
                boolean secondClassRequired = Boolean.parseBoolean(secondClassRequiredString);
                Relation relation = new Relation(regularClass, foreignClass, nNode.getAttributes().getNamedItem("type").getNodeValue(),firstClassRequired,secondClassRequired);
                relations.add(relation);
            }
        }
        return relations;
    }

    /**
     * Metodo que permite retornar todas as tags <attribute></attribute>
     * @param classNode Nó xml "class"
     * @param fromXML valor que indica se o ficheiro é em formato xml ou xmi
     * @return
     */
    public static ArrayList<Attribute> getAttributes(Node classNode, boolean fromXML) {

        ArrayList<Attribute> attributes = new ArrayList<>();
        NodeList classChilds = classNode.getChildNodes();

        for (int j = 0; j < classChilds.getLength(); j++) {

            Node childNode = classChilds.item(j);

            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                if (childNode.getNodeName() == "attribute") {
                    String name = childNode.getAttributes().getNamedItem("name").getNodeValue();
                    String type = childNode.getAttributes().getNamedItem("type").getNodeValue();
                    String required = "false";
                    if (fromXML) {
                        required = childNode.getAttributes().getNamedItem("required").getNodeValue();
                    }
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

    /**
     * Metodo que permite retornar um modelo em xmi
     * @param filename path do ficheiro xmi
     * @return
     */
    public static String getModelFromXMI(String filename) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
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

            getClassesXMI(modelNode, rootElem, doc);

            //Write to a xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            String returnedPath = "src/models/" + modelName + "FromXMI.xml";
            StreamResult result = new StreamResult(returnedPath);
            transformer.transform(source, result);

            return returnedPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metodo que permite retornar todas classes de um ficheiro xmi
     * @param modelNode Nó xmi "model"
     * @param modelElement Elemento model
     * @param doc Documento xmi
     */
    public static void getClassesXMI(Node modelNode, Element modelElement, Document doc) {
        NodeList nList = modelNode.getChildNodes();
        for (int i = 0; i < nList.getLength(); i++) {
            Node classNode = nList.item(i);
            if (classNode.getNodeType() == Node.ELEMENT_NODE && classNode.getNodeName() == "packagedElement") {
                Element classElem = doc.createElement("class");//Creates a <class> tag
                classElem.setAttribute("name", classNode.getAttributes().getNamedItem("name").getNodeValue());
                getAttributesXMI(classNode, classElem, doc);
                modelElement.appendChild(classElem);
            }
        }
    }

    /**
     *  Metodo que permite retornar todos os atributos das classes de um ficheiro xmi
     * @param classNode Nó xmi "class"
     * @param classElem Elemento class
     * @param doc Documento xmi
     */
    public static void getAttributesXMI(Node classNode, Element classElem, Document doc) {
        NodeList classAttributes = classNode.getChildNodes();
        for (int i = 0; i < classAttributes.getLength(); i++) {
            Node attributeNode = classAttributes.item(i);
            if (attributeNode.getNodeType() == Node.ELEMENT_NODE && attributeNode.getNodeName() == "ownedAttribute") {
                Element attributeElem = doc.createElement("attribute");
                attributeElem.setAttribute("name", attributeNode.getAttributes().getNamedItem("name").getNodeValue());
                getAttributeTypeXMI(attributeNode, attributeElem, doc);
                classElem.appendChild(attributeElem);
            }
        }
    }

    /**
     *  Metodo que permite retornar os tipos dos atributos das classes de um ficheiro xmi
     * @param attributeNode Nó xmi "attribute"
     * @param attributeElem Elemento attribute
     * @param doc Documento xmi
     */
    public static void getAttributeTypeXMI(Node attributeNode, Element attributeElem, Document doc) {
        NodeList attributeType = attributeNode.getChildNodes();
        for (int i = 0; i < attributeType.getLength(); i++) {
            Node typeNode = attributeType.item(i);
            if (typeNode.getNodeType() == Node.ELEMENT_NODE && typeNode.getNodeName() == "type") {
                String type = typeNode.getAttributes().getNamedItem("href").getNodeValue();
                String[] splited = type.split("#");
                if (splited[1].equals("Integer"))
                    splited[1] = "int";
                attributeElem.setAttribute("type", splited[1]);
            }
        }
    }
}
