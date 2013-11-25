package eu.trentorise.smartcampus.service.trentinotrack.scripts;

import it.sayservice.platform.core.bus.common.exception.DataFlowException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.WordUtils;

import com.google.protobuf.Message;

import eu.trentorise.smartcampus.service.trentinotrack.data.message.Trentinotrack.BikeTrack;
import eu.trentorise.smartcampus.service.trentinotrack.data.message.Trentinotrack.CampiglioDataPage;
import eu.trentorise.smartcampus.service.trentinotrack.data.message.Trentinotrack.OpenDataResourcePage;
import eu.trentorise.smartcampus.service.trentinotrack.data.message.Trentinotrack.OpenDataResourcePages;
import eu.trentorise.smartcampus.service.trentinotrack.data.message.Trentinotrack.ResourceDescr;
import eu.trentorise.smartcampus.service.trentinotrack.scripts.encoder.GPXParser;
import eu.trentorise.smartcampus.service.trentinotrack.scripts.encoder.Polyline;
import eu.trentorise.smartcampus.service.trentinotrack.scripts.encoder.RDFParser;


public class TrentinoTrackScript {	

	private static final int MIN_LENGTH = 5000;

	public List<Message> downloadZip(ResourceDescr res, CampiglioDataPage page) throws MalformedURLException, IOException, DataFlowException {
		ZipFile zf = downloadZip(res.getLink());
		List<Message> result = new ArrayList<Message>();
		
		Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zf.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			
			BufferedInputStream in = new BufferedInputStream(zf.getInputStream(entry));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

	    byte data[] = new byte[1024];
	    int count;
	    while((count = in.read(data,0,1024)) != -1)
	    {
	    		bos.write(data, 0, count);
	    }		
	    bos.close();
	    
	    BikeTrack bt = parseGpx(res, bos.toString());
	    BikeTrack.Builder builder = BikeTrack.newBuilder(bt);
	    builder.setLabel(page.getTitle() + " - " + bt.getLabel());
	    builder.setId(page.getTitle() + " - " + bt.getLabel());
			
			result.add(builder.build());
		}		
		return result;
	}	
	
	private ZipFile downloadZip(String address) throws MalformedURLException, IOException {
		String tmp = System.getProperty("java.io.tmpdir");
		File f = new File(tmp, "tmpzip.zip");
		
		FileOutputStream fos = new FileOutputStream(f);
		
		BufferedInputStream in = new BufferedInputStream(new URL(address).openStream());
	    byte data[] = new byte[1024];
	    int count;
	    while((count = in.read(data,0,1024)) != -1)
	    {
	        fos.write(data, 0, count);
	    }		
	    fos.close();
		
			ZipFile zf = new ZipFile(f);
			
			return zf;
	}
	
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

				if (p.getPolyline() == null || Double.parseDouble(p.getLength()) < MIN_LENGTH) {
					continue;
				}
				
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
	
	public BikeTrack parseGpx(ResourceDescr descr, String rdf) throws DataFlowException {
		if (!rdf.startsWith("<")) rdf = rdf.substring(rdf.indexOf('<'));
		GPXParser parser = new GPXParser(new ByteArrayInputStream(rdf.trim().getBytes(Charset.forName("UTF-8"))));
		try {
			List<Polyline> list = parser.parse();
			assert list.size() == 1;
			for (Polyline p : list) {
				BikeTrack.Builder bt = BikeTrack.newBuilder()
				.setAbout(descr.getExtPage())
				.setId(p.getId());
				bt.setLabel(cleanLabel(p.getLabel()));
				if (p.getLength() != null) bt.setLength(p.getLength());
				if (p.getPolyline() != null) bt.setPolyline(p.getPolyline());
				// suppose to have a single track for a file
				return bt.build();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new DataFlowException("Failed to parse rdf data");
	}
	
	private String cleanLabel(String label) {
		return WordUtils.capitalize(label.replace("_", " "));
	}

}
