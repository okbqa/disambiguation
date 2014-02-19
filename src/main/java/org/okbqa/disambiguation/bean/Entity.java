package org.okbqa.disambiguation.bean;

public class Entity {
	private String URI;
	
	// TODO: entities may have many types
	private String type;
	private int start;
	private int end;
    
    public Entity (String URI, int start, int end, String type) {
    	this.setURI(URI);
    	this.setStart(start);
    	this.setEnd(end);
    	this.setType(type);
    }

	public void setType(String type) {
		this.type = type;
	}

	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getType() {
		return this.type;
	}
}
