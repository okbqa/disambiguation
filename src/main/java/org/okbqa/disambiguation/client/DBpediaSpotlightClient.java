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
import org.okbqa.disambiguation.bean.Entity;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple web service-based annotation client for DBpedia Spotlight.
 *
 * @author pablomendes, Joachim Daiber
 */

public class DBpediaSpotlightClient {

    private final static String API_URL = "http://spotlight.dbpedia.org/";
	private static final double CONFIDENCE = 0.0;
	private static final int SUPPORT = 0;

	public List<Entity> extract(String text) throws AnnotationException {
		String spotlightResponse;
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet;
		try {
			httpGet = new HttpGet(API_URL + "rest/annotate/?" +
					"confidence=" + CONFIDENCE
					+ "&support=" + SUPPORT
					+ "&text=" + URLEncoder.encode(text, "utf-8"));
			
			httpGet.addHeader("Accept", "application/json");
			CloseableHttpResponse response = httpclient.execute(httpGet);
			
			try {
				spotlightResponse = response.getEntity().getContent().toString();
			} catch (Exception e) {
				throw new AnnotationException(e);
			} finally {
			    try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			throw new AnnotationException(e);
		}

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
				String entityURI = entity.getString("@URI");
				int entityStart = new Integer(entity.getString("@offset"));
				int entityEnd = entityStart + entity.getString("@surfaceForm").length();
				
				// TODO: entities may have many types
				String entityType = entity.getString("@types").split(",")[0];
				
				resources.add(new Entity(entityURI, entityStart, entityEnd, entityType));
			} catch (JSONException e) {
				throw new AnnotationException(e);
            }
		}
		return resources;
	}
}