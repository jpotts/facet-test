package com.metaversant.alfresco;

import java.util.*;

import org.alfresco.repo.dictionary.constraint.ListOfValuesConstraint;
import org.alfresco.repo.search.impl.solr.facet.handler.AbstractFacetLabelDisplayHandler;
import org.alfresco.repo.search.impl.solr.facet.handler.FacetLabel;
import org.alfresco.service.cmr.dictionary.Constraint;
import org.alfresco.service.cmr.dictionary.ConstraintDefinition;
import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.cmr.dictionary.PropertyDefinition;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.surf.util.ParameterCheck;

public class ConstraintDisplayHandler extends AbstractFacetLabelDisplayHandler {
    private static Log logger = LogFactory.getLog(ConstraintDisplayHandler.class);

    // Dependencies
    private DictionaryService dictionaryService;

    public ConstraintDisplayHandler(Set<String> supportedFieldFacets) {
        ParameterCheck.mandatory("supportedFieldFacets", supportedFieldFacets);
        this.supportedFieldFacets = Collections.unmodifiableSet(new HashSet<>(supportedFieldFacets));
    }

    @Override
    public FacetLabel getDisplayLabel(String value) {
        String title = null;
        logger.debug("Inside getDisplayLabel for: " + value);

        // iterate over the fields this handler supports
        // for each one, ask the dictionary for its constraints
        // if there is a list of values constraint, see if the data dictionary knows what the label is
        // for the value passed in to the method
        for (String prop : supportedFieldFacets) {
            PropertyDefinition propDef = dictionaryService.getProperty(QName.createQName(prop.substring(1)));
            List<ConstraintDefinition> constraints = propDef.getConstraints();
            for (ConstraintDefinition constraintDef : constraints) {
                Constraint constraint = constraintDef.getConstraint();
                if (constraint instanceof ListOfValuesConstraint) {
                    ListOfValuesConstraint lovConstraint = (ListOfValuesConstraint) constraint;
                    title = lovConstraint.getDisplayLabel(value, dictionaryService);
                    logger.debug("Label: " + title);
                }
            }
        }

        // if title is still null at this point, just use the value instead
        if (title == null) {
            title = value;
        }

        return new FacetLabel(value, title, -1);
    }

    public void setDictionaryService(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }
}
