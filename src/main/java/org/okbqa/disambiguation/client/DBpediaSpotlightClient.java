package org.okbqa.disambiguation.client;

/**
 * Copyright 2011 Pablo Mendes, Max Jakob
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.okbqa.disambiguation.model.Entity;
import org.okbqa.disambiguation.model.EntityBinding;
import org.okbqa.disambiguation.model.EntitySlot;
import org.okbqa.disambiguation.model.PseudoSPARQLTemplate;
import org.okbqa.disambiguation.model.TemplateInterpretation;
import org.okbqa.disambiguation.model.TemplateInterpretations;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Simple web service-based annotation client for DBpedia Spotlight.
 *
 * @author pablomendes, Joachim Daiber, bdares
 */

public class DBpediaSpotlightClient {

    private final static String API_URL = "http://spotlight.dbpedia.org/";
	private static final double CONFIDENCE = 0.0;
	private static final int SUPPORT = 0;
	
	private String getResponse(String requestURI) throws SpotlightException{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet;
		try {
			httpGet = new HttpGet(requestURI);
			
			httpGet.addHeader("Accept", "application/json");
			CloseableHttpResponse response = httpclient.execute(httpGet);
			
			try {
				return response.getEntity().getContent().toString();
			} catch (Exception e) {
				throw new SpotlightException(e);
			} finally {
			    try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			throw new SpotlightException(e);
		}
	}

	public List<Entity> extract(String text) throws AnnotationException {
		try {
			String spotlightResponse = getResponse(API_URL + "rest/annotate/?" +
					"confidence=" + CONFIDENCE
					+ "&support=" + SUPPORT
					+ "&text=" + URLEncoder.encode(text, "utf-8"));
			
			assert spotlightResponse != null;

			JSONObject resultJSON = null;
			JSONArray entities = null;

			try {
				resultJSON = new JSONObject(spotlightResponse);
				entities = resultJSON.getJSONArray("Resources");
			} catch (JSONException e) {
				throw new AnnotationException(e);
			}

			List<Entity> resources = new LinkedList<Entity>();
			for(int i = 0; i < entities.length(); i++) {
				try {
					JSONObject entity = entities.getJSONObject(i);
					resources.add(Entity.fromJSON(entity));
				} catch (JSONException e) {
					throw new AnnotationException(e);
	            }
			}
			return resources;

		} catch (Exception e) {
			throw new AnnotationException(e);
		}
	}
	
	public TemplateInterpretations extract(PseudoSPARQLTemplate pst) throws SpotlightException{
		TemplateInterpretations ret = new TemplateInterpretations();
		TemplateInterpretation ti = new TemplateInterpretation();
		List<EntityBinding> bindings = ti.getEntityBindings();
		
		// obtain variables that are rdf:Resource and their verbalizations
		List<String> resourceVariables = new ArrayList<String>();
		for (EntitySlot es : pst.getSlots()) {
			if (es.getPredicate().equals("is") &&
					es.getObject().equals("rdf:Resource"))
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
			
			annotation.setAttribute("text", pst.getQuestion().replace("\"", "&quot;"));
			
			// surface forms
			for (String verbalization : varToVerbalization.values()) {
				Element surfaceForm = doc.createElement("surfaceForm");
				annotation.appendChild(surfaceForm);
				surfaceForm.setAttribute("name", verbalization);
				surfaceForm.setAttribute("offset", String.valueOf(pst.getQuestion().indexOf(verbalization)));
			}
			
			query = doc.toString();
		} catch (Exception e) {
			query = "";
		}
		
		try {
			String spotlightResponse = getResponse(API_URL + "rest/annotate/?" +
					"spotter=SpotXmlParser" +
					"&text=" + URLEncoder.encode(query, "utf-8"));
			
			JSONObject resultJSON = null;
			JSONArray entities = null;

			try {
				resultJSON = new JSONObject(spotlightResponse);
				ti.setScore(resultJSON.getDouble("@confidence"));
				entities = resultJSON.getJSONArray("Resources");
			} catch (JSONException e) {
				throw new AnnotationException(e);
			}
			
			for (int i = 0; i < entities.length(); i++) {
				JSONObject entityJSON = entities.getJSONObject(i);
				Entity entity = Entity.fromJSON(entityJSON);
				bindings.add(new EntityBinding(entityJSON.getString("@surfaceForm"), entity));
			}
			
		} catch (Exception e) {
			
		}
		return ret;
	}
}