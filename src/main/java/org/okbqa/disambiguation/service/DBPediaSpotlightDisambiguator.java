package org.okbqa.disambiguation.service;

import java.util.List;

import org.okbqa.disambiguation.bean.Entity;
import org.okbqa.disambiguation.bean.PseudoSPARQLTemplate;
import org.okbqa.disambiguation.client.AnnotationException;
import org.okbqa.disambiguation.client.DBPediaSpotlightClient;

public class DBPediaSpotlightDisambiguator implements DisambiguationService {

	public List<Entity> disambiguate(PseudoSPARQLTemplate template) {
		DBPediaSpotlightClient client = new DBPediaSpotlightClient();
		
		try {
			return client.extract(template.getQuestion());
		} catch (AnnotationException e) {
			// TODO
		}
		return null;
	}

}
