package org.geoserver.web.data.datastore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.geoserver.catalog.Catalog;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.GeoServerHomePage;
import org.geoserver.web.data.DataPage;
import org.geoserver.web.data.datastore.panel.CheckBoxParamPanel;
import org.geoserver.web.data.datastore.panel.PasswordParamPanel;
import org.geoserver.web.data.datastore.panel.TextParamPanel;
import org.geoserver.web.data.tree.GeoServerDataTreePage;
import org.geotools.data.DataAccessFactory;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.postgis.PostgisDataStoreFactory;
import org.vfny.geoserver.util.DataStoreUtils;

/**
 * Provides a form to configure a geotools DataStore
 * 
 * @author Gabriel Roldan
 */
public class DataStoreConfiguration extends GeoServerBasePage {

    /**
     * Holds datastore parameters. Properties will be settled by the form input
     * fields.
     */
    private final Map<String, ?> parametersMap;

    public DataStoreConfiguration(final String dataStoreFactDisplayName) {
        parametersMap = new HashMap<String, Object>();

        final List<ParamInfo> paramsInfo = new ArrayList<ParamInfo>();
        {
            DataStoreFactorySpi dsFact = DataStoreUtils.aquireFactory(dataStoreFactDisplayName);
            if (dsFact == null) {
                throw new IllegalArgumentException("Can't locate a datastore factory named '"
                        + dataStoreFactDisplayName + "'");
            }
            Param[] dsParams = dsFact.getParametersInfo();
            for (Param p : dsParams) {
                paramsInfo.add(new ParamInfo(p));
            }
        }

        final Form paramsForm = new Form("dataStoreForm") {
            @Override
            public void onSubmit() {
                setResponsePage(GeoServerHomePage.class);
            }
        };

        add(paramsForm);

        final TextField textField = new TextField("dataStoreId");
        paramsForm.add(textField);

        ListView paramsList = new ListView("parameters", paramsInfo) {
            @Override
            protected void populateItem(ListItem item) {
                ParamInfo parameter = (ParamInfo) item.getModelObject();
                Component inputComponent = getInputComponent("parameterPanel", parametersMap,
                        parameter);
                if (parameter.getTitle() != null) {
                    inputComponent.add(new SimpleAttributeModifier("title", parameter.getTitle()));
                }
                item.add(inputComponent);
            }
        };
        // needed for form components not to loose state
        paramsList.setReuseItems(true);

        paramsForm.add(paramsList);

        paramsForm.add(new Button("cancel") {
            @Override
            public void onSubmit() {
                setResponsePage(DataPage.class);
            }
        });

        paramsForm.add(new Button("submit") {
            @Override
            public void onSubmit() {
                Catalog catalog = getCatalog();
                setResponsePage(GeoServerDataTreePage.class);
            }
        });
    }

    /**
     * Creates a form input component for the given datastore param based on its
     * type and metadata properties.
     * 
     * @param param
     * @return
     */
    private Panel getInputComponent(final String componentId, final Map<String, ?> paramsMap,
            final ParamInfo param) {
        final Class binding = param.getBinding();
        Panel parameterPanel;
        if (Boolean.class == binding) {
            parameterPanel = new CheckBoxParamPanel(componentId, paramsMap, param);
        } else if (String.class == binding && param.isPassword()) {
            parameterPanel = new PasswordParamPanel(componentId, paramsMap, param);
        } else {
            parameterPanel = new TextParamPanel(componentId, paramsMap, param);
        }
        return parameterPanel;
    }
}
