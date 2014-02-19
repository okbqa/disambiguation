package org.okbqa.disambiguation.service;

import java.util.List;

import org.okbqa.disambiguation.bean.Entity;
import org.okbqa.disambiguation.bean.PseudoSPARQLTemplate;
import org.okbqa.disambiguation.client.AnnotationException;
import org.okbqa.disambiguation.client.DBpediaSpotlightClient;

public class DBpediaSpotlightDisambiguator implements DisambiguationService {

	public List<Entity> disambiguate(PseudoSPARQLTemplate template) {
		DBpediaSpotlightClient client = new DBpediaSpotlightClient();
		
		try {
			return client.extract(template.getQuestion());
		} catch (AnnotationException e) {
			// TODO
		}
		return null;
	}

}
