package org.okbqa.disambiguation.service;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.okbqa.disambiguation.bean.EntitySlot;
import org.okbqa.disambiguation.bean.PseudoSPARQLTemplate;

public class PseudoSPARQLTemplateParser {
	public static Collection<PseudoSPARQLTemplate> parse(String jsonString) {
		JSONArray templatesJSON = new JSONArray(jsonString);
		
		Collection<PseudoSPARQLTemplate> ret = new ArrayList<PseudoSPARQLTemplate>();
		
		for (int i = 0; i < templatesJSON.length(); i++) {
			JSONObject templateJSON = templatesJSON.getJSONObject(i);
			PseudoSPARQLTemplate pst = new PseudoSPARQLTemplate();
			pst.setPseudoQuery(templateJSON.getString("pseudoQuery"));
			pst.setQuestion(templateJSON.getString("question"));
			pst.setScore(templateJSON.getDouble("score"));
			
			JSONArray slotsJSON = templateJSON.getJSONArray("slots");
			for (int j = 0; j < slotsJSON.length(); j++) {
				JSONObject slotJSON = slotsJSON.getJSONObject(j);
				
				String predicate = slotJSON.getString("p");
				String subject = slotJSON.getString("s");
				String object = slotJSON.getString("o");
				EntitySlot slot = new EntitySlot(subject, predicate, object);
				pst.getSlots().add(slot);
			}
			
			ret.add(pst);
		}
		return ret;
	}
}
