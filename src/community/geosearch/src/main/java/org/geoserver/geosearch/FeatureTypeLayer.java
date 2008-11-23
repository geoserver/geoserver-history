package org.geoserver.geosearch;

import org.vfny.geoserver.config.FeatureTypeConfig;
import org.geotools.geometry.GeneralEnvelope;
import com.vividsolutions.jts.geom.Envelope;

public class FeatureTypeLayer implements Layer {
    private FeatureTypeConfig myFeatureType;

    public FeatureTypeLayer(FeatureTypeConfig ftc){
        myFeatureType = ftc;
    }

    public GeneralEnvelope getBounds(){
        Envelope e = myFeatureType.getLatLongBBox();
        GeneralEnvelope genv = new GeneralEnvelope(2);

        genv.setRange(0, e.getMinY(), e.getMaxY());
        genv.setRange(1, e.getMinX(), e.getMaxX());

        return genv;
    }

    public String getTitle(){
        return myFeatureType.getTitle();
    }

    public String getDescription(){
        return myFeatureType.getAbstract();
    }
}

