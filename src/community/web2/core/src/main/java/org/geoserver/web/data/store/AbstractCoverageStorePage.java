/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.store;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.store.panel.CheckBoxParamPanel;
import org.geoserver.web.data.store.panel.TextParamPanel;
import org.geoserver.web.data.store.panel.WorkspacePanel;
import org.geotools.coverage.grid.io.AbstractGridFormat;

/**
 * Supports coverage store configuration
 * 
 * @author Andrea Aime
 */
@SuppressWarnings("serial")
abstract class AbstractCoverageStorePage extends GeoServerSecuredPage {

    private Form paramsForm;

    void initUI(final CoverageStoreInfo store) {
        AbstractGridFormat format = store.getFormat();

        // build the form
        paramsForm = new Form("rasterStoreForm");
        add(paramsForm);

        // the format description labels
        paramsForm.add(new Label("storeType", format.getName()));
        paramsForm.add(new Label("storeTypeDescription", format.getDescription()));

        IModel model = new Model(store);
        setModel(model);

        // name
        PropertyModel nameModel = new PropertyModel(model, "name");
        final TextParamPanel namePanel = new TextParamPanel("namePanel", nameModel,
                new ResourceModel("dataSrcName", "Data Source Name"), true);

        paramsForm.add(namePanel);

        // description and enabled
        paramsForm.add(new TextParamPanel("descriptionPanel", new PropertyModel(model,
                "description"), new ResourceModel("dataSrcName", "Data Source Name"), false));
        paramsForm.add(new CheckBoxParamPanel("enabledPanel", new PropertyModel(model, "enabled"),
                new ResourceModel("enabled", "Enabled")));
        // a custom converter will turn this into a namespace url
        final WorkspacePanel workspacePanel = new WorkspacePanel("workspacePanel",
                new PropertyModel(model, "workspace"), new ResourceModel("workspace", "Workspace"),
                true);
        paramsForm.add(workspacePanel);

        // url
        paramsForm.add(new TextParamPanel("urlPanel", new PropertyModel(model, "URL"),
                new ResourceModel("url", "URL"), true));

        // cancel/submit buttons
        paramsForm.add(new BookmarkablePageLink("cancel", StorePage.class));
        paramsForm.add(saveLink());

        // feedback panel for error messages
        paramsForm.add(new FeedbackPanel("feedback"));

        StoreNameValidator storeNameValidator = new StoreNameValidator(workspacePanel
                .getFormComponent(), namePanel.getFormComponent(), store.getId());
        paramsForm.add(storeNameValidator);
    }

    private SubmitLink saveLink() {
        return new SubmitLink("save") {
            @Override
            public void onSubmit() {
                CoverageStoreInfo info = (CoverageStoreInfo) AbstractCoverageStorePage.this
                        .getModelObject();
                onSave(info);
            }
        };
    }

    protected abstract void onSave(CoverageStoreInfo info);

}
