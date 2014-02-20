package org.okbqa.disambiguation.bean;

import org.json.JSONObject;

public class Entity {
	private String URI;
	
	// TODO: entities may have many types
	private String type;
	private int start;
	private int end;
	private double score;
    
    public Entity (String URI, int start, int end, String type, double score) {
    	this.setURI(URI);
    	this.setStart(start);
    	this.setEnd(end);
    	this.setType(type);
    	this.setScore(score);
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
	
	public static Entity fromJSON(JSONObject json) {
		String entityURI = json.getString("@URI");
		String surface = json.getString("@surfaceForm");
		int entityStart = new Integer(json.getString("@offset"));
		int entityEnd = entityStart + surface.length();
		String entityType = json.getString("@types").split(",")[0];
		double score = json.getDouble("@similarityScore");
		return new Entity(entityURI, entityStart, entityEnd, entityType, score);
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
}
