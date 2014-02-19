package org.okbqa.disambiguation.service;

import java.util.List;

import org.okbqa.disambiguation.bean.Entity;
import org.okbqa.disambiguation.bean.EntityBinding;
import org.okbqa.disambiguation.bean.PseudoSPARQLTemplate;
import org.okbqa.disambiguation.bean.TemplateInterpretation;
import org.okbqa.disambiguation.bean.TemplateInterpretations;

public class SimpleInterpretationBuilder implements InterpretationBuilder {

	/**
	 * Produces every possible set of bindings.  Not recommended for real use.
	 */
	
	public TemplateInterpretations interpret(PseudoSPARQLTemplate pst,
			List<Entity> entities) {
		TemplateInterpretations tis = new TemplateInterpretations();
		
		int permutationCount = 1;
		for (int i = 2; i <= entities.size(); i++) permutationCount *= i;
		
		for (int i = 0; i < permutationCount; i++) {
			TemplateInterpretation ti = new TemplateInterpretation();
			
			for (Entity e : entities) {
				String value = e.getURI();
				String var = ""; // TODO: ith-permutation's jth-index variable
				String type = e.getType();
				double score = 1.0;
				
				ti.getEntityBindings().add(new EntityBinding(var, value, type, score));
			}
			
			ti.setScore(1.0);
			
			tis.getInterpretations().add(ti);
		}
		
		return tis;
	}
}
