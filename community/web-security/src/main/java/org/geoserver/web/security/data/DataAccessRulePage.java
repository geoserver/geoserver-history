/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.security.data;

import java.util.logging.Level;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.geoserver.security.DataAccessRule;
import org.geoserver.security.DataAccessRuleDAO;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.wicket.ConfirmationAjaxLink;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.ParamResourceModel;
import org.geoserver.web.wicket.SimpleAjaxLink;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;

/**
 * A page listing data access rules, allowing for removal, addition and linking to an edit page
 */
@SuppressWarnings("serial")
public class DataAccessRulePage extends GeoServerSecuredPage {

    private GeoServerTablePanel<DataAccessRule> rules;

    public DataAccessRulePage() {
        DataAccessRuleProvider provider = new DataAccessRuleProvider();
        rules = new GeoServerTablePanel<DataAccessRule>("table", provider) {

            @Override
            protected Component getComponentForProperty(String id, IModel itemModel,
                    Property<DataAccessRule> property) {
                if (property == DataAccessRuleProvider.RULEKEY) {
                    return editRuleLink(id, itemModel, property);
                } if (property == DataAccessRuleProvider.ROLES) {
                    return new Label(id,  property.getModel(itemModel));
                } else if (property == DataAccessRuleProvider.REMOVE) {
                    return removeRuleLink(id, itemModel);
                } else {
                    throw new RuntimeException("Uknown property " + property);
                }

            }
        };
        rules.setOutputMarkupId(true);
        add(rules);
        add(addRuleLink());
    }
    
    ConfirmationAjaxLink removeRuleLink(String id, IModel itemModel) {
        DataAccessRule rule = ((DataAccessRule) itemModel.getObject());
        IModel confirmRemoveModel = new ParamResourceModel("confirmRemoveRule", this, rule.getKey());
        return new ConfirmationAjaxLink(id, itemModel, new ParamResourceModel("removeRule", this, rule.getKey()), confirmRemoveModel) {

            @Override
            protected void onClick(AjaxRequestTarget target) {
                try {
                    DataAccessRule rule = ((DataAccessRule) getModelObject());
                    DataAccessRuleDAO dao = DataAccessRuleDAO.get();
                    dao.removeRule(rule);
                    dao.storeRules();
                } catch(Exception e) {
                    LOGGER.log(Level.SEVERE, "Error occurred while removing a rule and saving out the result", e);
                    error(new ParamResourceModel("saveError", this, e.getMessage()));
                    target.addComponent(feedbackPanel);
                }
                target.addComponent(rules);
            }
            
        };
    }

    AjaxLink addRuleLink() {
        return new AjaxLink("addRule", new Model()) {

            @Override
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(new NewDataAccessRulePage());
            }

        };
    }

    Component editRuleLink(String id, IModel itemModel, Property<DataAccessRule> property) {
        return new SimpleAjaxLink(id, itemModel, property.getModel(itemModel)) {

            @Override
            protected void onClick(AjaxRequestTarget target) {
                setResponsePage(new EditDataAccessRulePage((DataAccessRule) getModelObject()));
            }

        };
    }

}
