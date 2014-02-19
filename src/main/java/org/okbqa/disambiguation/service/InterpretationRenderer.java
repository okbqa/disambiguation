package org.okbqa.disambiguation.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.okbqa.disambiguation.bean.EntityBinding;
import org.okbqa.disambiguation.bean.TemplateInterpretation;
import org.okbqa.disambiguation.bean.TemplateInterpretations;

public class InterpretationRenderer {
	
	public static String render(TemplateInterpretations ti) {
		return render(ti, false);
	}
	
	public static String render(TemplateInterpretations ti, boolean prettyPrint) {
		int indent = prettyPrint ? 4 : 0;
		
		JSONObject out = new JSONObject();
		out.put("score", ti.getScore());
		JSONArray ned = new JSONArray();
		for (TemplateInterpretation t : ti.getInterpretations()) {
			JSONObject tiJSON = new JSONObject();
			tiJSON.put("score", t.getScore());
			
			JSONArray bindingsJSON = new JSONArray();
			for (EntityBinding eb : t.getEntityBindings()) {
				JSONObject bindingJSON = new JSONObject();
				
				bindingJSON.put("score", eb.getScore());
				bindingJSON.put("type", eb.getType());
				bindingJSON.put("value", eb.getValue());
				bindingJSON.put("var", eb.getVariable());
				
				bindingsJSON.put(bindingJSON);
			}
			
			tiJSON.put("entities", bindingsJSON);
			
			ned.put(tiJSON);
		}
		out.put("ned", ned);
		
		return out.toString(indent);
	}
}
