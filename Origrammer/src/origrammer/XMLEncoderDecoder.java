package origrammer;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class XMLEncoderDecoder {

	public XMLEncoderDecoder() {

	}

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

}
