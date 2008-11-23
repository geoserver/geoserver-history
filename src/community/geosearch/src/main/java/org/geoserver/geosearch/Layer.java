package org.geoserver.geosearch;

import org.geotools.geometry.GeneralEnvelope;

public interface Layer {
    public GeneralEnvelope getBounds();
    public String getTitle();
    public String getDescription();
}
