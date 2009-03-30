/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.web.publish;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.web.publish.LayerConfigurationPanel;

/**
 * Configures {@link LayerInfo} WMS specific attributes 
 */
@SuppressWarnings("serial")
public class WMSLayerConfig extends LayerConfigurationPanel {


    public WMSLayerConfig(String id, IModel layerModel){
        super(id, layerModel);

        // default style chooser. A default style is required
        StylesModel styles = new StylesModel();
        DropDownChoice defaultStyle = new DropDownChoice("defaultStyle", 
                    new PropertyModel(layerModel, "defaultStyle"), styles);
        defaultStyle.setRequired(true);
        add(defaultStyle);

        // build a palette with no reordering allowed, since order doesn't affect anything
        add(new Palette("extraStyles",
                new LayerStylesModel(layerModel), styles,
                    new StyleNameRenderer(), 10, false)
           );

        add(new TextField("wmsPath", new PropertyModel(layerModel, "path")));
    }

    /**
     * Wicket cannot deal with collection property that does not have a setter, so we
     * do the magic inside there (and sort the style names in the process)
     * @author Andrea Aime - OpenGeo
     *
     */
    class LayerStylesModel implements IModel {
        IModel layerModel;
        
        public LayerStylesModel(IModel layerModel) {
            this.layerModel = layerModel;
        }

        public Object getObject() {
            List<StyleInfo> styles = new ArrayList(((LayerInfo) layerModel.getObject()).getStyles());
            Collections.sort(styles, new StyleNameComparator());
            return styles;
        }

        public void setObject(Object object) {
            Set<StyleInfo> styles = ((LayerInfo) layerModel.getObject()).getStyles();
            styles.clear();
            styles.addAll((Collection<StyleInfo>) object);
        }

        public void detach() {
            layerModel.detach();
        }
        
    }
}
