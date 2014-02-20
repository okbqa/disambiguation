package org.okbqa.disambiguation.model;

public class EntityBinding {
    private String variable;
    private String value;
    private String type;
    private double score;
    
    public EntityBinding(String variable, String value, String type, double score) {
    	this.variable = variable;
    	this.value = value;
    	this.type = type;
    	this.score = score;
    }
    
    public EntityBinding(String variable, Entity entity) {
    	this.variable = variable;
    	this.value = entity.getURI();
    	this.type = entity.getType();
    	this.score = entity.getScore();
    }
    
	public String getVariable() {
		return variable;
	}
	public void setVariable(String variable) {
		this.variable = variable;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	
}
