package org.okbqa.disambiguation.service;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.okbqa.disambiguation.bean.Entity;
import org.okbqa.disambiguation.bean.PseudoSPARQLTemplate;
import org.okbqa.disambiguation.bean.TemplateInterpretations;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

/**
 *
 * @author bdares
 */
@Path("/disambiguate")
public class DisambiguationWebService {
	
	public static final int PORT_DISAMBIGUATION = 9345;

    @GET
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String getResults(@QueryParam("data") String data, @QueryParam("endpoints") String endpoints) {
        if(data == null) return "{}";
        JSONArray endpointList = null;
        if (endpoints != null) {
            endpointList = new JSONArray(endpoints);
        }
        
        Collection<PseudoSPARQLTemplate> templates = PseudoSPARQLTemplateParser.parse(data);
        
        // TODO: better way of selecting a template
        //       perhaps this service should only receive one template as input
        PseudoSPARQLTemplate first = templates.iterator().next();
        
        DisambiguationService ds = new DBpediaSpotlightDisambiguator();
        List<Entity> entities = ds.disambiguate(first);
        
        InterpretationBuilder ib = new SimpleInterpretationBuilder();
        TemplateInterpretations tis = ib.interpret(first, entities);
        
        return InterpretationRenderer.render(tis);
    }

    public static void main(String[] args) throws IOException {

        final String baseUri = "http://localhost:" + PORT_DISAMBIGUATION + "/";
        final Map<String, String> initParams =
                new HashMap<String, String>();

        initParams.put("com.sun.jersey.config.property.packages",
                "org.openqa.openqasimpleanswergeneration.service");

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