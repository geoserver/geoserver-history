package org.geoserver.geosearch;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import com.vividsolutions.jts.geom.Envelope;

import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import org.vfny.geoserver.config.CoverageConfig;


public class CoverageLayer implements Layer {
    private CoverageConfig myCoverage;

    public CoverageLayer(CoverageConfig cc){
        myCoverage = cc;
    }

    public GeneralEnvelope getBounds(){
        try{
            return makeGeneralEnvelope(
                    convertBBoxToLatLon(
                        makeEnvelope(
                            myCoverage.getEnvelope()),
                        myCoverage.getCrs()
                        ),
                    myCoverage.getCrs()
                    );
        } catch (Exception e){
            return myCoverage.getEnvelope();
        }
    }

    public String getTitle(){
        return myCoverage.getName();
    }

    public String getDescription(){
        return myCoverage.getDescription();
    }
    
    private GeneralEnvelope makeGeneralEnvelope(Envelope e, CoordinateReferenceSystem c) {
        GeneralEnvelope genv = new GeneralEnvelope(c);
        genv.setRange(1, e.getMinX(), e.getMaxX());
        genv.setRange(0, e.getMinY(), e.getMaxY());
        return genv;
    }

    private Envelope makeEnvelope(GeneralEnvelope genv){
        return new Envelope(genv.getMinimum(0), genv.getMaximum(0), genv.getMinimum(1), genv.getMaximum(1));
    }

    private Envelope convertBBoxToLatLon(Envelope bbox, CoordinateReferenceSystem nativeCRS) throws Exception {
        CoordinateReferenceSystem latLon = CRS.decode("EPSG:4326");

        Envelope env = null;
        if (!CRS.equalsIgnoreMetadata(latLon, nativeCRS)){
            MathTransform xform = CRS.findMathTransform(nativeCRS, latLon, true);
            env = JTS.transform(bbox, null, xform, 10); // convert databbox to native CRS
        } else {
            env = bbox;
        }

        return env;
    }

}
