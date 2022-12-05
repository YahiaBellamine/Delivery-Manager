package com.pld.agile.utils.xml;

import com.pld.agile.model.Intersection;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

public class XMLSerialiser {
    private Element shapeRoot;
    private static Document document;
    private static XMLSerialiser instance = null;


    /**
     * Open an XML file and write an XML description of the plan in it
     * @param optimalTour the plan to serialise
     * @throws ParserConfigurationException
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerException
     * @throws ExceptionXML
     */
    public static void save(LinkedList<Intersection> optimalTour) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, ExceptionXML{
        File xml = XMLFileOpener.getInstance().open(false);
        StreamResult result = new StreamResult(xml);
        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element tour = document.createElement("tour");
        for(Intersection in:optimalTour){
            Element intersection=document.createElement("intersection");
            //Element intersection=tour.addElement("intersection");
            createAttribute(intersection,"id",Long.toString(in.getId()));
            createAttribute(intersection,"latitude",Double.toString(in.getLatitude()));
            createAttribute(intersection,"longitude",Double.toString(in.getLongitude()));
            tour.appendChild(intersection);
        }
        document.appendChild(tour);
        DOMSource source = new DOMSource(document);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
        xformer.transform(source, result);
    }

//    private Element createPlanElt(Plan plan) {
//        Element racine = document.createElement("plan");
//        createAttribute(racine,"height",Integer.toString(plan.getHeight()));
//        createAttribute(racine,"width",Integer.toString(plan.getWidth()));
//        Iterator<Shape> it = plan.getShapeIterator();
//        while (it.hasNext()){
//            it.next().display(this);
//            racine.appendChild(shapeRoot);
//        }
//        return racine;
//    }
//
    private static void createAttribute(Element root, String name, String value){
        Attr attribute = document.createAttribute(name);
        root.setAttributeNode(attribute);
        attribute.setValue(value);
    }
//
//    @Override
//    public void display(Circle c) {
//        shapeRoot = document.createElement("circle");
//        createAttribute(shapeRoot,"x",Integer.toString(c.getCenter().getX()));
//        createAttribute(shapeRoot,"y",Integer.toString(c.getCenter().getY()));
//        createAttribute(shapeRoot,"radius",Integer.toString(c.getRadius()));
//    }
//
//    @Override
//    public void display(Rectangle r) {
//        shapeRoot = document.createElement("rectangle");
//        createAttribute(shapeRoot,"x",Integer.toString(r.getCorner().getX()));
//        createAttribute(shapeRoot,"y",Integer.toString(r.getCorner().getY()));
//        createAttribute(shapeRoot,"width",Integer.toString(r.getWidth()));
//        createAttribute(shapeRoot,"height",Integer.toString(r.getHeight()));
//    }private Element shapeRoot;
//    private Document document;
//    private static XMLSerialiser instance = null;
//    private XMLSerialiser(){}
//    public static XMLSerialiser getInstance(){
//        if (instance == null)
//            instance = new XMLSerialiser();
//        return instance;
//    }


}
