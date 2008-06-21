package org.geoserver.wfs.web.publish;

import java.util.List;
import java.util.ArrayList;

import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.web.publish.LayerConfigurationPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ComponentPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;

public class WFSLayerConfig extends LayerConfigurationPanel {

    public WFSLayerConfig(String id, IModel model){
        super(id, model);

        add(new TextField("maxFeatures", new PropertyModel(getLayerInfo().getResource(), "maxFeatures")));
    }

    private List<String> listStyles(){
        List<String> styles = new ArrayList<String>();

        for (StyleInfo info : getLayerInfo().getResource().getCatalog().getStyles()){
            styles.add(info.getName());
        }

        return styles;
    }
}
