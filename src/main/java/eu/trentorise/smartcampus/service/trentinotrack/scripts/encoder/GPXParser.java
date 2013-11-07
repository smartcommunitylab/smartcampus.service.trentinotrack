package eu.trentorise.smartcampus.service.trentinotrack.scripts.encoder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class GPXParser {

	private InputStream is = null;

	public GPXParser(InputStream is) {
		super();
		this.is = is;
	}

	public List<Polyline> parse() throws Exception {
		List<Polyline> res = new ArrayList<Polyline>();
		
		XMLInputFactory f = XMLInputFactory.newInstance();
		XMLStreamReader r = f.createXMLStreamReader(is);
		Polyline pl = null;
		while (r.hasNext()) {
			if (r.isStartElement()) {
				QName n = r.getName();
				if (n.getLocalPart().equals("trk")) {
					pl = new Polyline();
					pl.setCoords(new ArrayList<double[]>());
					res.add(pl);
				}
				if (pl != null) {
					if (n.getLocalPart().equals("name")) {
						pl.setLabel(r.getElementText());
						pl.setId(pl.getLabel());
					}
					if (n.getLocalPart().equals("trkpt")) {
						String lat = r.getAttributeValue(0);
						String lon = r.getAttributeValue(1);
						pl.getCoords().add(new double[]{Double.parseDouble(lat), Double.parseDouble(lon)});
					}
				}
			}
			r.next();	
		}
		
		for (Polyline p : res) {
			p.setPolyline(convertPolyline(p.getCoords()));
		}
		
		return res;
	}

	private String convertPolyline(List<double[]> coords) {
		return encodeTrack(coords);
	}

	private String encodeTrack(List<double[]> coords) {
		return new PolylineEncoder().dpEncode(coords);
	}
	
}
