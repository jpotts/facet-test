package com.metaversant.alfresco;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.alfresco.repo.search.impl.solr.facet.handler.AbstractFacetLabelDisplayHandler;
import org.alfresco.repo.search.impl.solr.facet.handler.FacetLabel;
import org.springframework.extensions.surf.util.ParameterCheck;

public class ConstraintDisplayHandler extends AbstractFacetLabelDisplayHandler {
    private final Map<String, String> labels;

    public ConstraintDisplayHandler(Set<String> supportedFieldFacets) {
        this(supportedFieldFacets, Collections.<String, String> emptyMap());
    }

    public ConstraintDisplayHandler(Set<String> supportedFieldFacets, Map<String, String> labels) {
        ParameterCheck.mandatory("supportedFieldFacets", supportedFieldFacets);

        this.supportedFieldFacets = Collections.unmodifiableSet(new HashSet<>(supportedFieldFacets));
        this.labels = labels == null ? Collections.<String, String> emptyMap() : labels;
    }

    @Override
    public FacetLabel getDisplayLabel(String value) {
        String title = null;

        if (labels.containsKey(value)) {
            title = labels.get(value);
        } else {
            title = value;
        }

        return new FacetLabel(value, title, -1);
    }
}
