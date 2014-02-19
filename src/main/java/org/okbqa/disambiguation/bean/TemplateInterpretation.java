package org.okbqa.disambiguation.bean;

import java.util.ArrayList;
import java.util.List;

public class TemplateInterpretation {
	private double score;
	private List<EntityBinding> entityBindings = new ArrayList<EntityBinding>();
	
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public List<EntityBinding> getEntityBindings() {
		return entityBindings;
	}
	public void setEntityBindings(List<EntityBinding> entityBindings) {
		this.entityBindings = entityBindings;
	}
	
}
