package org.okbqa.disambiguation.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.okbqa.disambiguation.InterpretationRenderer;
import org.okbqa.disambiguation.PseudoSPARQLTemplateParser;
import org.okbqa.disambiguation.client.DBpediaSpotlightClient;
import org.okbqa.disambiguation.client.SpotlightException;
import org.okbqa.disambiguation.model.PseudoSPARQLTemplate;
import org.okbqa.disambiguation.model.TemplateInterpretations;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

/**
 *
 * @author bdares
 */
@Path("/disambiguate")
public class DisambiguationWebService {
	
	public static final int PORT_DISAMBIGUATION = 9345;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String getResults(@QueryParam("data") String data, @QueryParam("endpoints") String endpoints) {
    	System.out.println("got request");
    	
        if(data == null) return "{\"message\":\"invalid input\"}";
        
        PseudoSPARQLTemplate template = PseudoSPARQLTemplateParser.parse(data);
        
        System.out.println("got template");
        
        DBpediaSpotlightClient client = new DBpediaSpotlightClient();
        
        try {
	        TemplateInterpretations tis = client.extract(template);
	        
	        System.out.println("got interpretation");
	        
	        return InterpretationRenderer.render(tis);
        } catch (SpotlightException e) {
        	return "{\"message\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}";
        }
    }

    public static void main(String[] args) throws IOException {

        final String baseUri = "http://localhost:" + PORT_DISAMBIGUATION + "/";
        final Map<String, String> initParams =
                new HashMap<String, String>();

        initParams.put("com.sun.jersey.config.property.packages",
                "org.okbqa.disambiguation.rest");

        System.out.println("Starting grizzly...");
        SelectorThread threadSelector;
        threadSelector = GrizzlyWebContainerFactory.create(baseUri, initParams);
        System.out.println(String.format(
                "Jersey app started with WADL available at %sapplication.wadl\n "
                + "Try out %s\nHit enter to stop it...", baseUri, baseUri));
        System.in.read();
        threadSelector.stopEndpoint();
        System.exit(0);
    }
}