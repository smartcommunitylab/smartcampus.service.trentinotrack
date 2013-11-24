package eu.trentorise.smartcampus.service.trentinotrack.test;

import java.io.InputStream;
import java.util.List;

import eu.trentorise.smartcampus.service.trentinotrack.scripts.encoder.GPXParser;
import eu.trentorise.smartcampus.service.trentinotrack.scripts.encoder.Polyline;

public class TestGPXParser {

	public static void main(String[] args) throws Exception {
		InputStream is = TestGPXParser.class.getResourceAsStream("/fiemme_fassa.1312527747.GPX.txt");
		List<Polyline> list = new GPXParser(is).parse();
		System.err.println(list);
	}
}
