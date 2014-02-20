package org.okbqa.disambiguation;

import org.json.JSONArray;
import org.json.JSONObject;
import org.okbqa.disambiguation.model.EntitySlot;
import org.okbqa.disambiguation.model.PseudoSPARQLTemplate;

public class PseudoSPARQLTemplateParser {
	public static PseudoSPARQLTemplate parse(String jsonString) {
		JSONObject templateJSON = new JSONObject(jsonString);
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
		
		return pst;
	}
}
