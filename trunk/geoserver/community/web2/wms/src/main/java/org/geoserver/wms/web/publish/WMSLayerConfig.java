/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.web.publish;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.web.publish.LayerConfigurationPanel;

@SuppressWarnings("serial")
public class WMSLayerConfig extends LayerConfigurationPanel {

    @SuppressWarnings("unused")
    private List<String> extraStyles;
    private List<String> availableStyles;

    @SuppressWarnings("unused")
    private String wmsPath;

    public WMSLayerConfig(String id, IModel model){
        super(id, model);
        extraStyles     = new ArrayList<String>();
        for (StyleInfo info : getLayerInfo().getStyles()) extraStyles.add(info.getId());
        availableStyles = listStyles();
        availableStyles.removeAll(extraStyles);
        availableStyles.remove(getLayerInfo().getDefaultStyle());

        StyleInfo defaultStyle = getLayerInfo().getDefaultStyle();

        add(new DropDownChoice("defaultStyle", 
                    new Model(defaultStyle == null ? "No style selected" : defaultStyle.getId()),
                    new PropertyModel(this, "availableStyles"))
           );

        add(new Palette("extraStyles",
                    new PropertyModel(this, "extraStyles"),
                    new PropertyModel(this, "availableStyles"),
                    new ChoiceRenderer(),
                    10,
                    false // no reordering allowed since order doesn't affect anything
                    )
           );

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
