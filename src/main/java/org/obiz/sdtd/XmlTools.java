package org.obiz.sdtd;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

public class XmlTools {

    public static Stream<Node> findNodes(String xpath, Node xmlDocument) {
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodeList = null;
        try {
            nodeList = (NodeList) xPath.compile(xpath).evaluate(xmlDocument, XPathConstants.NODESET);
            ArrayList<Node> result = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                result.add(nodeList.item(i));
            }
            return result.stream();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return new ArrayList<Node>().stream();
    }

    public static Stream<Node> findNodes(String xpath, Path fileName) {
        try {
            if(Files.exists(fileName)) {
                Document xmlDocument = buildDom(fileName);
                return findNodes(xpath, xmlDocument);
            } else {
                System.err.println("Not found! " + fileName);
            }
        } catch (Exception e) {
            System.err.println("Error at file: " + fileName.toString());
            e.printStackTrace();
        }
        return new ArrayList<Node>().stream();
    }

    public static Document buildDom(Path fileName) throws IOException, ParserConfigurationException, SAXException {
        InputStream fileIS = Files.newInputStream(fileName);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document xmlDocument = builder.parse(fileIS);
        return xmlDocument;
    }

    public static String nodeToString(Node node) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(node), new StreamResult(writer));
            return writer.toString();
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    static String getAttr(Node node, String attrName) {
        return node.getAttributes().getNamedItem(attrName).getNodeValue();
    }

    static String getAttrOfFirstChild(Node node, String xpath, String attrName) {
        Optional<Node> first = findNodes(xpath, node).findFirst();
        if(first.isPresent()) {
            return getAttr(first.get(), attrName);
        } else {
            return "-";
        }
    }

    static Optional<Node> getFirstChildNode(Node node, String xpath) {
        return findNodes(xpath, node).findFirst();
    }
}
