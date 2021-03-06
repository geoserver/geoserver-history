/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.store;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.validation.IValidator;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.ResourcePool;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.store.panel.CheckBoxParamPanel;
import org.geoserver.web.data.store.panel.NamespacePanel;
import org.geoserver.web.data.store.panel.TextParamPanel;
import org.geoserver.web.data.store.panel.WorkspacePanel;
import org.geotools.data.DataAccessFactory;
import org.geotools.util.logging.Logging;

/**
 * Abstract base class for adding/editing a {@link DataStoreInfo}, provides the UI components and a
 * template method {@link #onSaveDataStore(Form)} for the subclasses to perform the insertion or
 * update of the object.
 * 
 * @author Gabriel Roldan
 * @see DataAccessNewPage
 * @see DataAccessEditPage
 */
abstract class AbstractDataAccessPage extends GeoServerSecuredPage {

    protected static final Logger LOGGER = Logging.getLogger("org.geoserver.web.data.store");

    /**
     * Needed as an instance variable so if the DataAccess has a namespace parameter, it is
     * automatically updated to match the workspace's namespace as per GEOS-3149 until the
     * resource/publish split is finalized
     */
    protected WorkspacePanel workspacePanel;

    private NamespacePanel namespacePanel;

    public AbstractDataAccessPage() {

    }

    /**
     * 
     * @param storeInfo
     * @throws IllegalArgumentException
     */
    protected void initUI(final DataStoreInfo storeInfo) throws IllegalArgumentException {

        if (storeInfo.getWorkspace() == null) {
            throw new IllegalArgumentException("Workspace not provided");
        }

        final Catalog catalog = getCatalog();
        final ResourcePool resourcePool = catalog.getResourcePool();
        DataAccessFactory dsFactory;
        try {
            dsFactory = resourcePool.getDataStoreFactory(storeInfo);
        } catch (IOException e) {
            String msg = (String) new ResourceModel(
                    "AbstractDataAccessPage.cantGetDataStoreFactory").getObject();
            msg += ": " + e.getMessage();
            throw new IllegalArgumentException(msg);
        }
        if (dsFactory == null) {
            String msg = (String) new ResourceModel(
                    "AbstractDataAccessPage.cantGetDataStoreFactory").getObject();
            throw new IllegalArgumentException(msg);
        }

        final IModel model = new CompoundPropertyModel(storeInfo);

        final Form paramsForm = new Form("dataStoreForm", model);
        add(paramsForm);

        paramsForm.add(new Label("storeType", dsFactory.getDisplayName()));
        paramsForm.add(new Label("storeTypeDescription", dsFactory.getDescription()));

        {
            final IModel wsModel = new PropertyModel(model, "workspace");
            final IModel wsLabelModel = new ResourceModel("AbstractDataAccessPage.workspace");
            workspacePanel = new WorkspacePanel("workspacePanel", wsModel, wsLabelModel, true);
        }
        paramsForm.add(workspacePanel);

        final TextParamPanel dataStoreNamePanel;

        dataStoreNamePanel = new TextParamPanel("dataStoreNamePanel", new PropertyModel(model,
                "name"),
                new ResourceModel("AbstractDataAccessPage.dataSrcName", "Data Source Name"), true);
        paramsForm.add(dataStoreNamePanel);

        paramsForm.add(new TextParamPanel("dataStoreDescriptionPanel", new PropertyModel(model,
                "description"), new ResourceModel("description", "Description"), false,
                (IValidator[]) null));

        paramsForm.add(new CheckBoxParamPanel("dataStoreEnabledPanel", new PropertyModel(model,
                "enabled"), new ResourceModel("enabled", "Enabled")));

        final StoreEditPanel storeEditPanel;
        {
            /*
             * Here's where the extension point is applied in order to give extensions a chance to
             * provide custom behavior/components for the coverage form other than the default
             * single "url" input field
             */
            GeoServerApplication app = getGeoServerApplication();
            storeEditPanel = StoreExtensionPoints.getStoreEditPanel("parametersPanel", paramsForm,
                    storeInfo, app);
        }
        paramsForm.add(storeEditPanel);

        paramsForm.add(new FeedbackPanel("feedback"));

        // validate the selected workspace does not already contain a store with the same name
        final String dataStoreInfoId = storeInfo.getId();
        StoreNameValidator storeNameValidator = new StoreNameValidator(workspacePanel
                .getFormComponent(), dataStoreNamePanel.getFormComponent(), dataStoreInfoId);
        paramsForm.add(storeNameValidator);

        paramsForm.add(new BookmarkablePageLink("cancel", StorePage.class));

        paramsForm.add(new AjaxSubmitLink("save", paramsForm) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onError(AjaxRequestTarget target, Form form) {
                super.onError(target, form);
                target.addComponent(paramsForm);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                try {
                    DataStoreInfo dataStore = (DataStoreInfo) form.getModelObject();
                    onSaveDataStore(dataStore, target);
                } catch (IllegalArgumentException e) {
                    paramsForm.error(e.getMessage());
                    target.addComponent(paramsForm);
                }
            }
        });

        // save the namespace panel as an instance variable. Needed as per GEOS-3149
        makeNamespaceSyncUpWithWorkspace();
    }

    /**
     * Call back method called when the save button is hit. Subclasses shall override in order to
     * perform the action over the catalog, whether it is adding a new {@link DataStoreInfo} or
     * saving the edits to an existing one
     * 
     * @param info
     *            the object to save
     * @param requestTarget
     * @throws IllegalArgumentException
     *             with an appropriate message for the user if the operation failed
     */
    protected abstract void onSaveDataStore(final DataStoreInfo info,
            AjaxRequestTarget requestTarget) throws IllegalArgumentException;

    /**
     * Make the {@link #namespacePanel} model to synch up with the workspace whenever the
     * {@link #workspacePanel} option changes.
     * <p>
     * This is so to maintain namespaces in synch with workspace while the resource/publish split is
     * not finalized, as per GEOS-3149.
     * </p>
     * <p>
     * Removing this method and the call to it on
     * {@link #getInputComponent(String, IModel, ParamInfo)} is all that's needed to let the
     * namespace be selectable independently of the workspace once the resource/publish split is
     * done.
     * </p>
     */
    private void makeNamespaceSyncUpWithWorkspace() {

        // do not allow the namespace choice to be manually changed
        final DropDownChoice wsDropDown = (DropDownChoice) workspacePanel.getFormComponent();
        // add an ajax onchange behaviour that keeps ws and ns in synch
        wsDropDown.add(new OnChangeAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                // see if the namespace param is tied to a NamespacePanel and save it
                if (namespacePanel == null) {
                    Component paramsPanel = AbstractDataAccessPage.this
                            .get("dataStoreForm:parametersPanel");
                    namespacePanel = findNamespacePanel((MarkupContainer) paramsPanel);

                    if (namespacePanel == null) {
                        // nothing to do
                        return;
                    }
                }

                WorkspaceInfo ws = (WorkspaceInfo) wsDropDown.getModelObject();
                String prefix = ws.getName();
                NamespaceInfo namespaceInfo = getCatalog().getNamespaceByPrefix(prefix);
                namespacePanel.setDefaultModelObject(namespaceInfo);
                target.addComponent(namespacePanel.getFormComponent());
            }
        });
    }

    @SuppressWarnings("unchecked")
    private NamespacePanel findNamespacePanel(MarkupContainer c) {
        Component child;
        for (Iterator<? extends Component> it = ((MarkupContainer) c).iterator(); it.hasNext();) {
            child = it.next();
            if (child instanceof NamespacePanel) {
                return (NamespacePanel) child;
            } else if (child instanceof MarkupContainer) {
                NamespacePanel panel = findNamespacePanel((MarkupContainer) child);
                if (panel != null) {
                    return panel;
                }
            }
        }
        return null;
    }

    protected void clone(final DataStoreInfo source, DataStoreInfo target) {
        target.setDescription(source.getDescription());
        target.setEnabled(source.isEnabled());
        target.setName(source.getName());
        target.setWorkspace(source.getWorkspace());
        target.setType(source.getType());

        target.getConnectionParameters().clear();
        target.getConnectionParameters().putAll(source.getConnectionParameters());
    }
}