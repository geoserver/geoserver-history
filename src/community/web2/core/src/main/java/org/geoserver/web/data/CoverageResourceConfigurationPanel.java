package org.geoserver.web.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.web.data.store.panel.ColorPickerPanel;
import org.geoserver.web.data.store.panel.TextParamPanel;
import org.geoserver.web.util.MapModel;

/**
 * A configuration panel for CoverageInfo properties that related to WCS publication
 * @author Andrea Aime - OpenGeo
 *
 */
@SuppressWarnings("serial")
public class CoverageResourceConfigurationPanel extends ResourceConfigurationPanel {

    public CoverageResourceConfigurationPanel(String id, IModel model){
        super(id, model);

        final CoverageInfo coverage = (CoverageInfo) getResourceInfo();
        final IModel paramsModel = new PropertyModel(getModel(), "parameters");
        
        List<String> keys = new ArrayList<String>(coverage.getParameters().keySet());
        Collections.sort(keys);
        ListView paramsList = new ListView("parameters", keys) {
            
            @Override
            protected void populateItem(ListItem item) {
                Component inputComponent = getInputComponent("parameterPanel", paramsModel, item.getModelObjectAsString());
                item.add(inputComponent);
            }
        };
        // needed for form components not to loose state
        paramsList.setReuseItems(true);
        add(paramsList);
        
        if(keys.size() == 0)
            setVisible(false);
   }
    
    private Component getInputComponent(String id,
            IModel paramsModel, String keyName) {
        if(keyName.contains("Color"))
            return new ColorPickerPanel(id, new MapModel(paramsModel, keyName), keyName, false);
        else
            return new TextParamPanel(id, new MapModel(paramsModel, keyName), keyName, false);
    }
}
