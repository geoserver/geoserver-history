package org.geoserver.web.data.datastore;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.geoserver.web.GeoServerBasePage;
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

        Form paramsForm = new Form("form");
        add(paramsForm);

        RepeatingView rv = new RepeatingView("parameter");
        paramsForm.add(rv);

        for (int i = 0; i < paramInfo.length; i++) {
            Param param = paramInfo[i];
            String paramName = param.key;
            String paramValue = param.sample == null ? "" : String.valueOf(param.sample);

            WebMarkupContainer parent = new WebMarkupContainer(rv.newChildId());
            rv.add(parent);

            parent.add(new Label("paramName", paramName));

            Component inputComponent = getInputComponent("paramValue", param);
            parent.add(inputComponent);
        }

    }

    /**
     * Creates a form input component for the given datastore param based on its
     * type and metadata properties.
     * 
     * @param param
     * @return
     */
    private FormComponent getInputComponent(final String componentId, final Param param) {
        String paramValue = String.valueOf(param.sample);
        return new TextField(componentId);
        /*
        FormComponent component;

        final Class binding = param.type;
        if (Boolean.class == binding) {
            component = new CheckBox(componentId);
            component.add(new SimpleAttributeModifier("type", "checkbox"));
            
        } else if (String.class == binding) {
            if (param.isPassword()) {
                component = new PasswordTextField(componentId);
            } else {
                component = new TextField(componentId);
            }
            component.add(new SimpleAttributeModifier("type", "text"));
        } else {
            component = new TextField(componentId);
            component.add(new SimpleAttributeModifier("type", "text"));
        }

        return component;
        */
    }
}
