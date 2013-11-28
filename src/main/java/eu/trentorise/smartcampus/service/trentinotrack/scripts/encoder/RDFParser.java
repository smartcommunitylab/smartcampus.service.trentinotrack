package eu.trentorise.smartcampus.service.trentinotrack.scripts.encoder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class RDFParser {

	private InputStream is = null;
	private int zone = 32;
	private String hemisphere = "N";

	public RDFParser(InputStream is, int zone, String hemisphere) {
		super();
		this.is = is;
		this.zone = zone;
		this.hemisphere = hemisphere;
	}

	public List<Polyline> parse() throws XMLStreamException {
		List<Polyline> res = new ArrayList<Polyline>();
		
		XMLInputFactory f = new com.ctc.wstx.stax.WstxInputFactory();
		XMLStreamReader r = f.createXMLStreamReader(is);
		Map<String, Polyline> map = new HashMap<String, Polyline>();
		Polyline pl = null;
		while (r.hasNext()) {
			if (r.isStartElement()) {
				QName n = r.getName();
				if (n.getLocalPart().equals("Description")) {
					String about = r.getAttributeValue(0);
					String id = about.substring(about.lastIndexOf('/')+1);
					try {
						id = ""+Integer.parseInt(id);
					} catch (NumberFormatException e) {
						id = id.substring(id.lastIndexOf('_')+1);
					}
					pl = map.get(id);
					if (pl == null) {
						pl = new Polyline();
						pl.setAbout(about);
						pl.setId(id);
						map.put(id, pl);
					}
				}
				if (n.getLocalPart().equals("polyline")) {
					pl.setPolyline(convertPolyline(r.getElementText()));
				}
				if (n.getLocalPart().equals("label")) {
					String txt = r.getElementText();
					if (pl.getLabel() == null || txt.length() > pl.getLabel().length()) {
						pl.setLabel(txt);
					}
				}
				if (n.getLocalPart().equals("length")) {
					pl.setLength(r.getElementText());
				}
			}
			r.next();	
		}
		res.addAll(map.values());
		return res;
	}

	private String convertPolyline(String elementText) {
		if (elementText == null) return null;
		String[] latlngpairs = elementText.split(" ");
		List<double[]> coords = new ArrayList<double[]>();
		for (String llpair : latlngpairs) {
			String[] ll = llpair.split(",");
			double[] umtCoords = new double[]{Double.parseDouble(ll[0]),Double.parseDouble(ll[1])};
			double[] llCoords = new CoordinateConversion().utm2LatLon(zone, hemisphere, umtCoords[0], umtCoords[1]);
			coords.add(llCoords);
		}

		return encodeTrack(coords);
	}

	private String encodeTrack(List<double[]> coords) {
		return PolylineEncoder.encode(coords);
	}
	
}
