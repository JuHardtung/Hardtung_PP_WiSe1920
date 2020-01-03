package origrammer;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;



//https://howtodoinjava.com/java/serialization/xmlencoder-and-xmldecoder-example/
public class XMLEncoderDecoder {

	public XMLEncoderDecoder() {
		
	}
	
	
	public boolean serializeToXML(DiagramDataSet dataset, String filePath) {
		try {
			XMLEncoder encoder = new XMLEncoder(
					new BufferedOutputStream(
							new FileOutputStream(filePath)));
			encoder.writeObject(dataset);
			encoder.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
    public DiagramDataSet deserializeFromXML() {
    	DiagramDataSet decodedDiagram;
    	try {
        XMLDecoder decoder = new XMLDecoder(
        					new BufferedInputStream(
        					new FileInputStream("settings.xml")));
        decodedDiagram = (DiagramDataSet) decoder.readObject();
        decoder.close();
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    		return null;
    	}
        return decodedDiagram;
    }
	
	
}
