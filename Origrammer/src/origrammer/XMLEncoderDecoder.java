package origrammer;

import java.awt.Color;
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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import origrammer.geometry.OriLine;



//https://howtodoinjava.com/java/serialization/xmlencoder-and-xmldecoder-example/
public class XMLEncoderDecoder {

	public XMLEncoderDecoder() {

	}


	
//	public boolean serializeToXML(Diagram d, String filePath) {
//		
//		DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder icBuilder;
//		
//		try {
//			icBuilder = icFactory.newDocumentBuilder();
//			Document doc = icBuilder.newDocument();
//			Element mainRootElement = doc.createElement("Diagram");
//			doc.appendChild(mainRootElement);
//			
////			int title = Origrammer.diagram.getTitle();
////			int creator = Origrammer.diagram.getCreator();
////			Color year = Origrammer.diagram.getYear();
////		
////			mainRootElement.setAttribute("title", Integer.toString(title));
////			mainRootElement.setAttribute("creator", Integer.toString(creator));
////			mainRootElement.setAttribute("year", Integer.toString(year));
//
//			
//			mainRootElement.appendChild(getPaper(doc));
//			
//			int stepNum = Origrammer.diagram.steps.size();
//
//			for (int i=0; i<stepNum; i++) {
//				mainRootElement.appendChild(getStep(doc, i));
//
//			}
//	
//			Transformer transformer = TransformerFactory.newInstance().newTransformer();
//			transformer.setOutputProperty(OutputKeys.INDENT,  "yes");
//			DOMSource source = new DOMSource(doc);
//			StreamResult console = new StreamResult(System.out);
//			transformer.transform(source,  console);
//			
//			System.out.println("\nXML DOM Created Successfully..");
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//
//		return true;
//	}
//	
//	/**
//	 * Creates Node with all data of paper
//	 * @param doc
//	 * @return
//	 */
//	private static Node getPaper(Document doc) {
//		Element paper = doc.createElement("Paper");
//		paper.setAttribute("recPaperWidth", Integer.toString(Origrammer.diagram.recPaperWidth));
//		paper.setAttribute("recPaperHeight", Integer.toString(Origrammer.diagram.recPaperHeight));
//		
//		Element paperColor = doc.createElement("paperColor");
//		
//		paperColor.setAttribute("r", Integer.toString(Origrammer.diagram.paperColor.getRed()));
//		paperColor.setAttribute("g", Integer.toString(Origrammer.diagram.paperColor.getGreen()));
//		paperColor.setAttribute("b", Integer.toString(Origrammer.diagram.paperColor.getBlue()));
//		paperColor.setAttribute("a", Integer.toString(Origrammer.diagram.paperColor.getAlpha()));
//		paper.appendChild(paperColor);
//		
////		Element startOrientation = doc.createElement("startOrientation");
////		startOrientation.setAttribute("whiteSideUp", Boolean.toString(Origrammer.diagram.));
////		paper.appendChild(startOrientation);
//		
//		return paper;
//	}
//	
//	
//	
//	private static Node getStep(Document doc, int stepNum) {
//		Element step = doc.createElement("Step");
//		
//		step.setAttribute("id", Integer.toString(stepNum));
//		step.setAttribute("stepDescription", Origrammer.diagram.steps.get(stepNum).stepDescription);
//		
//		int lineNum = Origrammer.diagram.steps.get(stepNum).lines.size();
//		
//		for (int i=0; i<lineNum; i++) {
//			OriLine oriLine = Origrammer.diagram.steps.get(stepNum).lines.get(i);
//			step.appendChild(getLine(doc, oriLine));
//		}
//		return step;
//		
//	}
//	
//	/**
//	 *  Creates Node with all data of oriLine
//	 * @param doc
//	 * @param oriLine
//	 * @return
//	 */
//	private static Node getLine(Document doc, OriLine oriLine) {
//		Element line = doc.createElement("Line");
//		
//		line.setAttribute("type", Integer.toString(oriLine.type));
//		
//		Element p0 = doc.createElement("p0");
//		p0.setAttribute("x", Double.toString(oriLine.p0.x));
//		p0.setAttribute("y", Double.toString(oriLine.p0.y));
//		line.appendChild(p0);
//		
//		Element p1 = doc.createElement("p1");
//		p1.setAttribute("x", Double.toString(oriLine.p1.x));
//		p1.setAttribute("y", Double.toString(oriLine.p1.y));
//		line.appendChild(p1);
//		
//		return line;
//	}
	
	
	
	public boolean serializeToXML(DiagramDataSet dataset, String filePath) {
		try {
			XMLEncoder encoder = new XMLEncoder(
							new BufferedOutputStream(
							new FileOutputStream(filePath)));
			
			encoder.setExceptionListener(new ExceptionListener() {
		        @Override
		        public void exceptionThrown(Exception e) {
		            System.out.println("got exception. e=" + e);
		            e.printStackTrace();
		        }
		    });
			
			encoder.writeObject(dataset);
			encoder.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public DiagramDataSet deserializeFromXML(String filePath) {
		DiagramDataSet decodedDiagram;
		
		try {
			XMLDecoder decoder = new XMLDecoder(
							new BufferedInputStream(
							new FileInputStream(filePath)));
			decodedDiagram = (DiagramDataSet) decoder.readObject();
			decoder.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return decodedDiagram;
	}
	
//	public Diagram deserializeFromXML() {
//		Diagram decodedDiagram = new Diagram();
//		String xml = crunchifyGetURLContents(url);
//		
//		
//		return decodedDiagram;
//	}


}
