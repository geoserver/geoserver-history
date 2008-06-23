/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.web.publish;

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

public class WMSLayerConfig extends LayerConfigurationPanel {

    private List<String> stylesToRemove;
    private List<String> stylesToAdd;
    private List<String> extraStyles;
    private List<String> availableStyles;
    private String wmsPath;

    public WMSLayerConfig(String id, IModel model){
        super(id, model);
        stylesToRemove  = new ArrayList<String>();
        stylesToAdd     = new ArrayList<String>();
        extraStyles     = new ArrayList<String>();
        for (StyleInfo info : getLayerInfo().getStyles()) extraStyles.add(info.getId());
        availableStyles = listStyles();
        availableStyles.removeAll(extraStyles);
        availableStyles.remove(getLayerInfo().getDefaultStyle());

        add(new DropDownChoice("defaultStyle", new Model(getLayerInfo().getDefaultStyle().getId()), new PropertyModel(this, "availableStyles")));

        add(new ListMultipleChoice("extraStyles",
                    new PropertyModel(this, "stylesToRemove"),
                    new PropertyModel(this, "extraStyles")
                    )
           );
        add(new ListMultipleChoice("availableStyles",
                    new PropertyModel(this, "stylesToAdd"),
                    new PropertyModel(this, "availableStyles")
                    )
           );
        add(new Button("addStyles"){
            public void onSubmit(){
                for (String style : stylesToAdd)
                    getLayerInfo().getStyles().add(getLayerInfo().getResource().getCatalog().getStyle(style));
            }
        });
        add(new Button("removeStyles"){
            public void onSubmit(){
                for (String style : stylesToAdd)
                    getLayerInfo().getStyles().remove(getLayerInfo().getResource().getCatalog().getStyle(style));
            }
        });
        add(new TextField("wmsPath", new PropertyModel(this, "wmsPath")));
    }

    private List<String> listStyles(){
        List<String> styles = new ArrayList<String>();

        for (StyleInfo info : getLayerInfo().getResource().getCatalog().getStyles()){
            styles.add(info.getName());
        }

        return styles;
    }
}
