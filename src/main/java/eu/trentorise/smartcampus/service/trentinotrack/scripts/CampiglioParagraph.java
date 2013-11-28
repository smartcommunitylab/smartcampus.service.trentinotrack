package eu.trentorise.smartcampus.service.trentinotrack.scripts;

import java.util.ArrayList;
import java.util.List;

public class CampiglioParagraph {

	private String title;
	private List<String> text;

	public CampiglioParagraph() {
		text = new ArrayList<String>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getText() {
		return text;
	}

	public void setText(List<String> text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return title; // + "=>" + text;
	}

}
