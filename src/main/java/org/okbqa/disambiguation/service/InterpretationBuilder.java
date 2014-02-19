package org.okbqa.disambiguation.service;

import java.util.List;

import org.okbqa.disambiguation.bean.Entity;
import org.okbqa.disambiguation.bean.PseudoSPARQLTemplate;
import org.okbqa.disambiguation.bean.TemplateInterpretations;

public interface InterpretationBuilder {
	public abstract TemplateInterpretations interpret(PseudoSPARQLTemplate pst, List<Entity> entities);
}
