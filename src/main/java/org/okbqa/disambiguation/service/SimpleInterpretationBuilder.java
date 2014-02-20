package org.okbqa.disambiguation.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.okbqa.disambiguation.bean.Entity;
import org.okbqa.disambiguation.bean.EntityBinding;
import org.okbqa.disambiguation.bean.EntitySlot;
import org.okbqa.disambiguation.bean.PseudoSPARQLTemplate;
import org.okbqa.disambiguation.bean.TemplateInterpretation;
import org.okbqa.disambiguation.bean.TemplateInterpretations;
import org.okbqa.disambiguation.client.DBpediaSpotlightClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SimpleInterpretationBuilder implements InterpretationBuilder {

	private static String escapeQuotes(String input) {
		return input.replace("\"", "&quot;");
	}
	
	public TemplateInterpretations interpret(PseudoSPARQLTemplate pst,
			List<Entity> entities) {
		TemplateInterpretations tis = new TemplateInterpretations();
		
		// obtain variables that are rdf:Resource and their verbalizations
		List<String> resourceVariables = new ArrayList<String>();
		for (EntitySlot es : pst.getSlots()) {
			if (es.getPredicate().equals("is") &&
					es.getObject().equals("rdf:resource"))
				resourceVariables.add(es.getSubject());
		}
		
		Map<String, String> varToVerbalization = new HashMap<String, String>();
		
		for (EntitySlot es : pst.getSlots()) {
			if (es.getPredicate().equals("verbalization") &&
					resourceVariables.contains(es.getSubject())) {
				varToVerbalization.put(es.getSubject(), es.getObject());
			}
		}

		// build a query for just the rdf:Resource variables
		String query = null;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			// root element
			Document doc = docBuilder.newDocument();
			Element annotation = doc.createElement("annotation");
			doc.appendChild(annotation);
			
			annotation.setAttribute("text", escapeQuotes(pst.getQuestion()));
			
			// surface forms
			for (String verbalization : varToVerbalization.values()) {
				Element surfaceForm = doc.createElement("surfaceForm");
				annotation.appendChild(surfaceForm);
				surfaceForm.setAttribute("name", verbalization);
				surfaceForm.setAttribute("offset", String.valueOf(pst.getQuestion().indexOf(verbalization)));
			}
			
			query = doc.toString();
		} catch (Exception e) {
			
		}
		
		// obtain matching Entity objects for above rdf:Resource variables
		DBpediaSpotlightClient spotlightClient = new DBpediaSpotlightClient();
		
		// place results in our bean objects
		TemplateInterpretation ti = new TemplateInterpretation();
		List<EntityBinding> bindings = ti.getEntityBindings();
		
		try {
			Map<String, Entity> surfaceToUri = spotlightClient.obtainMapping(query);
			
			for (String var : varToVerbalization.keySet()) {
				Entity e = surfaceToUri.get(varToVerbalization.get(var));
				
				EntityBinding eb = new EntityBinding(var, e.getURI(), e.getType(), 0.5);
				bindings.add(eb);
			}
			
		} catch (Exception e) {
			return new TemplateInterpretations();
		}
		
		tis.getInterpretations().add(ti);
		return tis;
	}
}
