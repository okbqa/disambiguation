package org.okbqa.disambiguation.model;

import java.util.ArrayList;
import java.util.List;

public class TemplateInterpretations {
	private double score;
	private List<TemplateInterpretation> interpretations = new ArrayList<TemplateInterpretation>();
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public List<TemplateInterpretation> getInterpretations() {
		return interpretations;
	}
	public void setInterpretations(List<TemplateInterpretation> interpretations) {
		this.interpretations = interpretations;
	}
}
