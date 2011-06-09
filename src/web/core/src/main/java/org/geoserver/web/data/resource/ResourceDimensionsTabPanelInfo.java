/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.resource;

import org.apache.wicket.model.IModel;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerInfo;

/**
 * Plugs into the layer page a time/elevation selector for vector data
 * 
 * @author Alessio
 */
@SuppressWarnings("serial")
public class ResourceDimensionsTabPanelInfo extends LayerEditTabPanel {

    @SuppressWarnings("unchecked")
    public ResourceDimensionsTabPanelInfo(String id, IModel model) {
        super(id, model);

        final LayerInfo resourceInfo = (LayerInfo) model.getObject();

        if (resourceInfo.getResource() instanceof FeatureTypeInfo) {
            FeatureTypeDimensionsTabPanelInfo ftInfoDimensionPanel = new FeatureTypeDimensionsTabPanelInfo(
                    "resourceInfoDimensionPanel", model);
            ftInfoDimensionPanel.setOutputMarkupId(true);
            add(ftInfoDimensionPanel);
        } 

    }
}
