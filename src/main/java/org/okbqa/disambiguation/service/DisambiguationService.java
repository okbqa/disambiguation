package org.okbqa.disambiguation.service;

import java.util.List;

import org.okbqa.disambiguation.bean.Entity;
import org.okbqa.disambiguation.bean.PseudoSPARQLTemplate;

public interface DisambiguationService {
    public abstract List<Entity> disambiguate(PseudoSPARQLTemplate template);
}
