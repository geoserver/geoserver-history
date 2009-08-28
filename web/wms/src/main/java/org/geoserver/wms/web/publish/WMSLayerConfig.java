/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.web.publish;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.web.publish.LayerConfigurationPanel;
import org.geoserver.web.wicket.LiveCollectionModel;

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
                    new PropertyModel(layerModel, "defaultStyle"), styles, new StyleChoiceRenderer());
        defaultStyle.setRequired(true);
        add(defaultStyle);

        // build a palette with no reordering allowed, since order doesn't affect anything
        IModel stylesModel = LiveCollectionModel.set(new PropertyModel(layerModel, "styles"));
        Palette extraStyles = new Palette("extraStyles", stylesModel, styles,
                new StyleNameRenderer(), 10, false) {
            /**
             * Override otherwise the header is not i18n'ized
             */
            @Override
            public Component newSelectedHeader(final String componentId) {
                return new Label(componentId, new ResourceModel("ExtraStylesPalette.selectedHeader"));
            }

            /**
             * Override otherwise the header is not i18n'ized
             */
            @Override
            public Component newAvailableHeader(final String componentId) {
                return new Label(componentId, new ResourceModel("ExtraStylesPalette.availableHeader"));
            }
        };
        add(extraStyles);
        add(new TextField("wmsPath", new PropertyModel(layerModel, "path")));
    }
}
