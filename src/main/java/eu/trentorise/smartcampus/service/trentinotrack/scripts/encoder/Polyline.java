package eu.trentorise.smartcampus.service.trentinotrack.scripts.encoder;

import java.util.List;

public class Polyline {

	private String label;
	private String polyline;
	private String length;
	private String about;
	private String id;
	
	private List<double[]> coords;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getPolyline() {
		return polyline;
	}
	public void setPolyline(String polyline) {
		this.polyline = polyline;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public List<double[]> getCoords() {
		return coords;
	}
	public void setCoords(List<double[]> coords) {
		this.coords = coords;
	}
	@Override
	public String toString() {
		return "Polyline " + id + " [label=" + label + ", polyline=" + polyline
				+ ", length=" + length + ", about=" + about + "]\n";
	}
	
	
}
