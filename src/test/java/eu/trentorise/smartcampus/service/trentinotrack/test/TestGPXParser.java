package eu.trentorise.smartcampus.service.trentinotrack.test;

import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import eu.trentorise.smartcampus.service.trentinotrack.scripts.encoder.GPXParser;

public class TestGPXParser {

	public static void main(String[] args) throws XMLStreamException {
		InputStream is = TestGPXParser.class.getResourceAsStream("/valle_dell_adige.1312528075.GPX.txt");
		new GPXParser(is).parse();
	}
}
