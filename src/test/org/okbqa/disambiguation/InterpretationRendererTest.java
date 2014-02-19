package org.okbqa.disambiguation;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.okbqa.disambiguation.bean.EntityBinding;
import org.okbqa.disambiguation.bean.TemplateInterpretation;
import org.okbqa.disambiguation.bean.TemplateInterpretations;
import org.okbqa.disambiguation.service.InterpretationRenderer;


public class InterpretationRendererTest {

	@Test
	public void renderTest() {
		TemplateInterpretations tis = new TemplateInterpretations();
		TemplateInterpretation ti = new TemplateInterpretation();
		
		ti.setScore(0.3);
		
		List<EntityBinding> ebs = ti.getEntityBindings();
		ebs.add(new EntityBinding("variable", "value", "type", 0.3));
		
		tis.getInterpretations().add(ti);
		tis.setScore(0.3);
		
		assertEquals("InterpretationRenderer#render: unexpected json output", 
			"{\"score\":0.3,\"ned\":[{\"score\":0.3,\"entities\":[{\"var\":\"variable\",\"value\":\"value\",\"score\":0.3,\"type\":\"type\"}]}]}"	
				, InterpretationRenderer.render(tis));
	}
}
