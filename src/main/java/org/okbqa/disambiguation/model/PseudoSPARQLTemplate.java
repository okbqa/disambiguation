package org.okbqa.disambiguation.model;

import java.util.List;

public class PseudoSPARQLTemplate {
	private String question;
	private String pseudoQuery;
	private List<EntitySlot> slots;
	private double score;
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getPseudoQuery() {
		return pseudoQuery;
	}
	public void setPseudoQuery(String pseudoQuery) {
		this.pseudoQuery = pseudoQuery;
	}
	public List<EntitySlot> getSlots() {
		return slots;
	}
	public void setSlots(List<EntitySlot> slots) {
		this.slots = slots;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
}
