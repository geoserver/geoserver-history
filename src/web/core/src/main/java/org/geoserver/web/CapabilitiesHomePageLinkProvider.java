package org.geoserver.web;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

public interface CapabilitiesHomePageLinkProvider {
    public Component getCapabilitiesComponent(final String id,
            final IModel<GeoServerApplication> app);
}
