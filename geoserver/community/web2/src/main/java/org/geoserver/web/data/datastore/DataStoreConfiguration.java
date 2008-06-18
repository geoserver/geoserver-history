package org.geoserver.web.data.datastore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.data.datastore.panel.CheckBoxParamPanel;
import org.geoserver.web.data.datastore.panel.PasswordParamPanel;
import org.geoserver.web.data.datastore.panel.TextParamPanel;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.postgis.PostgisDataStoreFactory;

/**
 * Provides a form to configure a geotools DataStore
 * 
 * @author Gabriel Roldan
 */
public class DataStoreConfiguration extends GeoServerBasePage {

    public DataStoreConfiguration() {
        Param[] paramInfo = new PostgisDataStoreFactory().getParametersInfo();

        List<Param> parameterPanels = Arrays.asList(paramInfo);

        Form paramsForm = new Form("form");
        add(paramsForm);

        ListView paramsList = new ListView("parameters", parameterPanels) {
            @Override
            protected void populateItem(ListItem item) {
                Param parameter = (Param) item.getModelObject();
                Component inputComponent = getInputComponent("parameterPanel", parameter);
                item.add(inputComponent);
            }
        };
        // needed for form components not to loose state
        paramsList.setReuseItems(true);
        paramsForm.add(paramsList);
    }

    /**
     * Creates a form input component for the given datastore param based on its
     * type and metadata properties.
     * 
     * @param param
     * @return
     */
    private Panel getInputComponent(final String componentId, final Param param) {
        final Class binding = param.type;
        Panel parameterPanel;
        if (Boolean.class == binding) {
            parameterPanel = new CheckBoxParamPanel(componentId, param);
        } else if (String.class == binding && param.isPassword()) {
            parameterPanel = new PasswordParamPanel(componentId, param);
        } else {
            parameterPanel = new TextParamPanel(componentId, param);
        }
        return parameterPanel;
    }
}
