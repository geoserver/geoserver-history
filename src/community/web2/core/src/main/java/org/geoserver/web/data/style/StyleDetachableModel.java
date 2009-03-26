package org.geoserver.web.data.style;

import org.apache.wicket.model.LoadableDetachableModel;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.web.GeoServerApplication;

public class StyleDetachableModel extends LoadableDetachableModel {

    String id;
    
    public StyleDetachableModel(StyleInfo style) {
        this.id = style.getId();
    }
    
    @Override
    protected Object load() {
        return GeoServerApplication.get().getCatalog().getStyle( id );
    }

}
