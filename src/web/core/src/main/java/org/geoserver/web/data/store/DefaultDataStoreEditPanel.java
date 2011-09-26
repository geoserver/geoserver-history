/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.store;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.ResourcePool;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.data.resource.DataStorePanelInfo;
import org.geoserver.web.data.store.panel.CheckBoxParamPanel;
import org.geoserver.web.data.store.panel.DropDownChoiceParamPanel;
import org.geoserver.web.data.store.panel.LabelParamPanel;
import org.geoserver.web.data.store.panel.NamespacePanel;
import org.geoserver.web.data.store.panel.PasswordParamPanel;
import org.geoserver.web.data.store.panel.TextParamPanel;
import org.geoserver.web.util.MapModel;
import org.geoserver.web.wicket.FileExistsValidator;
import org.geotools.data.DataAccessFactory;
import org.geotools.data.Repository;
import org.geotools.data.DataAccessFactory.Param;

/**
 * A default {@link StoreEditPanel} contribution for the {@link DataStorePanelInfo} extension point
 * to work on any {@link DataStoreInfo}s.
 * <p>
 * This default store parameters panel contributes to the store edit form
 * {@link DataStorePanelInfo#getComponentClass() extension point} by providing a dynamically and
 * introspectively generated list of form input fields based on the {@link DataAccessFactory}
 * parameters for the given {@code DataStoreInfo}.
 * </p>
 * 
 * @author Gabriel Roldan
 * 
 * @see Param
 * @see ResourcePool#getDataStoreFactory(DataStoreInfo)
 * @see DataStorePanelInfo
 * @see AbstractDataAccessPage
 * @see TextParamPanel
 * @see CheckBoxParamPanel
 * @see LabelParamPanel
 * @see NamespacePanel
 */
@SuppressWarnings("serial")
public class DefaultDataStoreEditPanel extends StoreEditPanel {

    private static final long serialVersionUID = -1969433619372747193L;

    /**
     * Creates a new parameters panel with a list of input fields matching the {@link Param}s for
     * the factory related to the {@code DataStoreInfo} that's the model of the provided {@code
     * Form}.
     * 
     * @param componentId
     *            the id for this component instance
     * @param storeEditForm
     *            the form being build by the calling class, whose model is the
     *            {@link DataStoreInfo} being edited
     */
    public DefaultDataStoreEditPanel(final String componentId, final Form storeEditForm) {
        super(componentId, storeEditForm);

        final IModel model = storeEditForm.getModel();
        final DataStoreInfo info = (DataStoreInfo) model.getObject();
        final Catalog catalog = getCatalog();
        final ResourcePool resourcePool = catalog.getResourcePool();
        DataAccessFactory dsFactory;
        try {
            dsFactory = resourcePool.getDataStoreFactory(info);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final Map<String, ParamInfo> paramsMetadata = new LinkedHashMap<String, ParamInfo>();

        {
            final boolean isNew = null == info.getId();
            final Param[] dsParams = dsFactory.getParametersInfo();
            for (Param p : dsParams) {
                ParamInfo paramInfo = new ParamInfo(p);
                // hide the repository params, the resource pool will inject it transparently
                if(!Repository.class.equals(paramInfo.getBinding())) {
                    paramsMetadata.put(p.key, paramInfo);
                    if (isNew) {
                        // set default value
                        applyParamDefault(paramInfo, info);
                    }
                }
            }
        }

        final List<String> keys = new ArrayList<String>(paramsMetadata.keySet());
        final IModel paramsModel = new PropertyModel(model, "connectionParameters");

        ListView paramsList = new ListView("parameters", keys) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem item) {
                String paramName = item.getDefaultModelObjectAsString();
                ParamInfo paramMetadata = paramsMetadata.get(paramName);

                Component inputComponent;
                inputComponent = getInputComponent("parameterPanel", paramsModel, paramMetadata);

                String description = paramMetadata.getTitle();
                if (description != null) {
                    inputComponent.add(new SimpleAttributeModifier("title", description));
                }
                item.add(inputComponent);
            }
        };
        // needed for form components not to loose state
        paramsList.setReuseItems(true);

        add(paramsList);

    }

    /**
     * Creates a form input component for the given datastore param based on its type and metadata
     * properties.
     * 
     * @param paramMetadata
     * @return
     */
    private Panel getInputComponent(final String componentId, final IModel paramsModel,
            final ParamInfo paramMetadata) {

        final String paramName = paramMetadata.getName();
        final String paramLabel = paramMetadata.getName();
        final boolean required = paramMetadata.isRequired();
        final Class<?> binding = paramMetadata.getBinding();
        final List<Serializable> options = paramMetadata.getOptions();

        Panel parameterPanel;
        if("dbtype".equals(paramName) || "filetype".equals(paramName)) {
            // skip the two well known discriminators
            IModel model = new MapModel(paramsModel, paramName);
            TextParamPanel tp = new TextParamPanel(componentId,
                    model, new ResourceModel(paramLabel, paramLabel), required);
            tp.setVisible(false);
            parameterPanel = tp;
        } else  if ("namespace".equals(paramName)) {
            IModel namespaceModel = new NamespaceParamModel(paramsModel, paramName);
            IModel paramLabelModel = new ResourceModel(paramLabel, paramLabel);
            parameterPanel = new NamespacePanel(componentId, namespaceModel, paramLabelModel, true);
        } else if (options != null && options.size() > 0){
            
            IModel<Serializable> valueModel = new MapModel(paramsModel, paramName);
            IModel<String> labelModel = new ResourceModel(paramLabel, paramLabel);
            parameterPanel = new DropDownChoiceParamPanel(componentId, valueModel, labelModel, options,
                    required);
            
        } else if (Boolean.class == binding) {
            // TODO Add prefix for better i18n?
            parameterPanel = new CheckBoxParamPanel(componentId, new MapModel(paramsModel,
                    paramName), new ResourceModel(paramLabel, paramLabel));

        } else if (String.class == binding && paramMetadata.isPassword()) {
            parameterPanel = new PasswordParamPanel(componentId, new MapModel(paramsModel,
                    paramName), new ResourceModel(paramLabel, paramLabel), required);
        } else {
            IModel model;
            if("url".equalsIgnoreCase(paramName)) {
                model = new URLModel(paramsModel, paramName);
            } else {
                model = new MapModel(paramsModel, paramName);
            }
            
            TextParamPanel tp = new TextParamPanel(componentId,
                    model, new ResourceModel(paramLabel, paramLabel), required);
            // if it can be a reference to the local filesystem make sure it's valid
            if (paramName.equalsIgnoreCase("url")) {
                tp.getFormComponent().add(new FileExistsValidator());
            }
            // make sure the proper value is returned, but don't set it for strings otherwise
            // we incur in a wicket bug (the empty string is not converter back to a null)
            // GR: it doesn't work for File neither.
            // AA: better not mess with files, the converters turn data dir relative to
            // absolute and bye bye data dir portability
            if (binding != null && !String.class.equals(binding) && !File.class.equals(binding)
                    && !URL.class.equals(binding) && !binding.isArray()) {
                tp.getFormComponent().setType(binding);
            }
            parameterPanel = tp;
        }
        return parameterPanel;
    }

    

    /**
     * Model to wrap and unwrap a {@link NamespaceInfo} to and from a String for the Datastore's
     * "namespace" parameter
     * 
     */
    private final class NamespaceParamModel extends MapModel {
        private NamespaceParamModel(IModel model, String expression) {
            super(model, expression);
        }

        @Override
        public Object getObject() {
            String nsUri = (String) super.getObject();
            NamespaceInfo namespaceInfo = getCatalog().getNamespaceByURI(nsUri);
            return namespaceInfo;
        }

        @Override
        public void setObject(Object object) {
            NamespaceInfo namespaceInfo = (NamespaceInfo) object;
            String nsUri = namespaceInfo.getURI();
            super.setObject(nsUri);
        }
    }

    /**
     * Makes sure the file path for shapefiles do start with file:// otherwise
     * stuff like /home/user/file.shp won't be recognized as valid...
     * @author aaime
     *
     */
    private final class URLModel extends MapModel {
        private URLModel(IModel model, String expression) {
            super(model, expression);
        }

        @Override
        public void setObject(Object object) {
            String file = (String) object;
            if(!file.startsWith("file://") && !file.startsWith("file:") &&
                    !file.startsWith("http://"))
                file = "file://" + file;
            super.setObject(file);
        }
    }

}
