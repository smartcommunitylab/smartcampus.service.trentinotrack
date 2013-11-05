package eu.trentorise.smartcampus.service.trentinotrack.scripts;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.google.protobuf.Message;

import eu.trentorise.smartcampus.service.trentinotrack.data.message.Trentinotrack.BikeTrack;
import eu.trentorise.smartcampus.service.trentinotrack.data.message.Trentinotrack.OpenDataResourcePage;
import eu.trentorise.smartcampus.service.trentinotrack.data.message.Trentinotrack.OpenDataResourcePages;
import eu.trentorise.smartcampus.service.trentinotrack.data.message.Trentinotrack.ResourceDescr;
import eu.trentorise.smartcampus.service.trentinotrack.scripts.encoder.GPXParser;
import eu.trentorise.smartcampus.service.trentinotrack.scripts.encoder.Polyline;
import eu.trentorise.smartcampus.service.trentinotrack.scripts.encoder.RDFParser;


public class TrentinoTrackScript {	

	public String getResourceRDFURL(OpenDataResourcePages pages) {
		for (OpenDataResourcePage page : pages.getPagesList()) {
			if ("rdf".equals(page.getFormat())) return page.getLink();
		}
		return null;
	}

	public OpenDataResourcePages getResourceGPXURLs(OpenDataResourcePages pages) {
		OpenDataResourcePages.Builder res = OpenDataResourcePages.newBuilder();
		for (OpenDataResourcePage page : pages.getPagesList()) {
			if ("gpx".equals(page.getFormat())) res.addPages(page);
		}
		return res.build();
	}

	public List<Message> parseRdf(String rdf) {
		RDFParser parser = new RDFParser(new ByteArrayInputStream(rdf.getBytes()), 32, "N");
		try {
			List<Polyline> list = parser.parse();
			List<Message> res = new ArrayList<Message>();
			for (Polyline p : list) {
				BikeTrack.Builder bt = BikeTrack.newBuilder()
				.setAbout(p.getAbout())
				.setId(p.getId());
				if (p.getLabel() != null) bt.setLabel(p.getLabel());
				if (p.getLength() != null) bt.setLength(p.getLength());
				if (p.getPolyline() != null) bt.setPolyline(p.getPolyline());
				res.add(bt.build());
			}
			return res;
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
	
	public BikeTrack parseGpx(ResourceDescr descr, String rdf) {
		if (!rdf.startsWith("<")) rdf = rdf.substring(rdf.indexOf('<'));
		GPXParser parser = new GPXParser(new ByteArrayInputStream(rdf.trim().getBytes()));
		try {
			List<Polyline> list = parser.parse();
			assert list.size() == 1;
			for (Polyline p : list) {
				BikeTrack.Builder bt = BikeTrack.newBuilder()
				.setAbout(descr.getExtPage())
				.setId(p.getId());
				bt.setLabel(p.getLabel());
				if (p.getLength() != null) bt.setLength(p.getLength());
				if (p.getPolyline() != null) bt.setPolyline(p.getPolyline());
				// suppose to have a single track for a file
				return bt.build();
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return null;
	}

}
