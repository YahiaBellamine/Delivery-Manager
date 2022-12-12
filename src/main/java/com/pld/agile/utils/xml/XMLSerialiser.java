package com.pld.agile.utils.xml;

import com.pld.agile.model.DeliveryRequest;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.Tour;
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
import java.util.List;

public class XMLSerialiser {
    private Element shapeRoot;
    private static Document document;
    private static XMLSerialiser instance = null;


    /**
     * Open an XML file and write an XML description of the plan in it
     * @param optimalTours the list of tours to serialise
     * @throws ParserConfigurationException
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerException
     * @throws ExceptionXML
     */
    public static void save(List<Tour> optimalTours) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, ExceptionXML{
        File xml = XMLFileOpener.getInstance().open(false);
        StreamResult result = new StreamResult(xml);
        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        int counter = 1;
        Element tours = document.createElement("tours");
        for(Tour optimalTour : optimalTours) {
            Element tour = document.createElement("tour");
            createAttribute(tour, "id_tour", Integer.toString(counter));
            for(DeliveryRequest de:optimalTour.getDeliveryRequests()){
                Element delivery=document.createElement("delivery_request");
                String tw="";
                tw+= Integer.toString(de.getTimeWindow().getStart());
                tw+=" : ";
                tw+=Integer.toString(de.getTimeWindow().getEnd());
                createAttribute(delivery,"time_window",tw);
                createAttribute(delivery,"id_intersection",Long.toString(de.getAddress().getId()));
                String time="";
                double decimal=de.getArrivalTime()%1;
                double h= de.getArrivalTime()-decimal;
                double min=decimal*60-decimal*60%1;
                double s=(decimal*60-min)*60;
                time=String.format("%02d",(int)h)+":"+String.format("%02d",(int)min)+":"+String.format("%02d",(int)s);
                createAttribute(delivery,"delivery_time",time);
                tour.appendChild(delivery);
            }

            for(Intersection in:optimalTour.getIntersections()){
                Element intersection=document.createElement("intersection");
                //Element intersection=tour.addElement("intersection");
                createAttribute(intersection,"id",Long.toString(in.getId()));
                createAttribute(intersection,"latitude",Double.toString(in.getLatitude()));
                createAttribute(intersection,"longitude",Double.toString(in.getLongitude()));
                tour.appendChild(intersection);
            }
            document.appendChild(tour);
            tours.appendChild(tour);
            ++counter;
        }

        document.appendChild(tours);

        DOMSource source = new DOMSource(document);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
        xformer.transform(source, result);
    }
    private static void createAttribute(Element root, String name, String value){
        Attr attribute = document.createAttribute(name);
        root.setAttributeNode(attribute);
        attribute.setValue(value);
    }

}
