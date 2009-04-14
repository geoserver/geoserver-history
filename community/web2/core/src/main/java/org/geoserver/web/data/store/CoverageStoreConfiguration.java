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
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidator;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.layer.NewLayerPage;
import org.geoserver.web.data.resource.StoreNameValidator;
import org.geoserver.web.data.store.panel.CheckBoxParamPanel;
import org.geoserver.web.data.store.panel.LabelParamPanel;
import org.geoserver.web.data.store.panel.TextParamPanel;
import org.geoserver.web.data.store.panel.WorkspacePanel;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.opengis.coverage.grid.Format;

/**
 * Supports coverage store configuration
 * 
 * @author Andrea Aime
 */
@SuppressWarnings("serial")
public class CoverageStoreConfiguration extends GeoServerSecuredPage {
    Form paramsForm;

    private Panel namePanel;

    /**
     * 
     * @param workspaceId
     *            the {@link WorkspaceInfo#getId() id} of the workspace to
     *            attach the new coverage to
     * @param coverageFactoryName
     *            the {@link Format#getName() name} of the format to create a
     *            new raster coverage for
     */
    public CoverageStoreConfiguration(final String workspaceId,
            final String coverageFactoryName) {
        Catalog catalog = getCatalog();
        final WorkspaceInfo workspace = catalog.getWorkspace(workspaceId);
        CoverageStoreInfo store = catalog.getFactory().createCoverageStore();
        store.setWorkspace(workspace);
        store.setType(coverageFactoryName);
        store.setEnabled(true);

        init(store);
    }
    
    /**
     * 
     * @param storeId
     *            the store id 
     */
    public CoverageStoreConfiguration(final String storeId) {
        Catalog catalog = getCatalog();
        CoverageStoreInfo store =  catalog.getCoverageStore(storeId);
        if(store == null)
            throw new IllegalArgumentException("Cannot find coverage store " + storeId);

        init(store);
    }

    void init(CoverageStoreInfo store) {
        AbstractGridFormat format = store.getFormat();
        
        // the format description labels
        add(new Label("storeType", format.getName()));
        add(new Label("storeTypeDescription", format.getDescription()));
        
        // build the form
        paramsForm = new Form("rasterStoreForm");
        add(paramsForm);

        IModel model = new Model(store);
        setModel(model);

        // name
        PropertyModel nameModel = new PropertyModel(model, "name");
        if (store.getId() == null) {
            // a new store, the name is editable
            IValidator dsIdValidator = new StoreNameValidator(
                    DataStoreInfo.class);
            namePanel = new TextParamPanel("namePanel", nameModel,
                    "Data Source Name", true, dsIdValidator);

        } else {
            namePanel = new LabelParamPanel("namePanel", nameModel,
                    "Data Source Name");
        }
        paramsForm.add(namePanel);

        // description and enabled
        paramsForm.add(new TextParamPanel("descriptionPanel",
                new PropertyModel(model, "description"), "Description", false,
                null));
        paramsForm.add(new CheckBoxParamPanel("enabledPanel",
                new PropertyModel(model, "enabled"), "Enabled"));
        // a custom converter will turn this into a namespace url
        paramsForm.add(new WorkspacePanel("workspacePanel",
                new PropertyModel(model, "workspace"), "Workspace", true));

        // url
        paramsForm.add(new TextParamPanel("urlPanel", new PropertyModel(model,
                "URL"), "URL", true, null));

        // cancel/submit buttons
        paramsForm.add(new BookmarkablePageLink("cancel", StorePage.class));
        paramsForm.add(saveLink());

        // feedback panel for error messages
        paramsForm.add(new FeedbackPanel("feedback"));
    }

    private SubmitLink saveLink() {
        return new SubmitLink("save") {
            @Override
            public void onSubmit() {
                CoverageStoreInfo info = (CoverageStoreInfo) CoverageStoreConfiguration.this.getModelObject();
                getCatalog().save(info);
                setResponsePage(new NewLayerPage(info.getId()));
            }
        };
    }

    
}
