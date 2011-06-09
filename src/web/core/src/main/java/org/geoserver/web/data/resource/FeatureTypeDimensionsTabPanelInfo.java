/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.resource;

import java.util.Date;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.catalog.DimensionInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.impl.DimensionInfoImpl;
import org.geoserver.web.util.MetadataMapModel;

/**
 * An editor for time/elevation over vector data
 */
@SuppressWarnings("serial")
public class FeatureTypeDimensionsTabPanelInfo extends Panel {

    @SuppressWarnings("unchecked")
    public FeatureTypeDimensionsTabPanelInfo(String id, IModel model) {
        super(id, model);

        final LayerInfo layer = (LayerInfo) model.getObject();
        final ResourceInfo resource = layer.getResource();

        PropertyModel metadata = new PropertyModel(model, "resource.metadata");

        // time
        IModel time = new MetadataMapModel(metadata, FeatureTypeInfo.TIME, DimensionInfo.class);
        if (time.getObject() == null) {
            time.setObject(new DimensionInfoImpl());
        }
        add(new DimensionEditor("time", time, resource, Date.class));

        // elevation
        IModel elevation = new MetadataMapModel(metadata, FeatureTypeInfo.ELEVATION,
                DimensionInfo.class);
        if (elevation.getObject() == null) {
            elevation.setObject(new DimensionInfoImpl());
        }
        add(new DimensionEditor("elevation", elevation, resource, Number.class));
    }

}
