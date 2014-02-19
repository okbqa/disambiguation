package org.okbqa.disambiguation.service;

import java.util.List;

import org.okbqa.disambiguation.bean.Entity;
import org.okbqa.disambiguation.bean.EntityBinding;
import org.okbqa.disambiguation.bean.PseudoSPARQLTemplate;
import org.okbqa.disambiguation.bean.TemplateInterpretation;

public class SimpleInterpretationBuilder implements InterpretationBuilder {

	public TemplateInterpretation interpret(PseudoSPARQLTemplate pst,
			List<Entity> entities) {
		TemplateInterpretation ti = new TemplateInterpretation();
		
		for (Entity e : entities) {
			String value = e.getURI();
			String var = "";
			String type = e.getType();
			double score = 1.0;
			
			ti.getEntityBindings().add(new EntityBinding(var, value, type, score));
		}
		
		ti.setScore(1.0);
		
		return ti;
	}

}
