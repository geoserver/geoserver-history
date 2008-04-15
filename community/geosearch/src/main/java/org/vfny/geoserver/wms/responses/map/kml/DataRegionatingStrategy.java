package org.vfny.geoserver.wms.responses.map.kml;

import java.util.Iterator;
import java.util.logging.Logger;

import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.vfny.geoserver.wms.WMSMapContext;

/**
 * Strategy for regionating based on algorithmic stuff related to the actual data.
 * This strategy is fairly ill-defined and should be considered highly experimental. 
 * 
 * @author David Winslow
 */
public class DataRegionatingStrategy implements RegionatingStrategy{

    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.geosearch");
    private static long MAX_LEVELS = 5;

    private Long myMin; 
    private Long myMax;
    private int myZoomLevel;

    public void preProcess(WMSMapContext con, int index){
        try {
            FeatureSource<SimpleFeatureType, SimpleFeature> source = (FeatureSource<SimpleFeatureType, SimpleFeature>) con
                .getLayer(index).getFeatureSource();
            ReferencedEnvelope fullBounds = source.getBounds();
            ReferencedEnvelope requestBounds = con.getAreaOfInterest();
            try {
                boolean reprojectBBox = 
                    (fullBounds.getCoordinateReferenceSystem() != null)
                    && !CRS.equalsIgnoreMetadata(
                            requestBounds.getCoordinateReferenceSystem(),
                            fullBounds.getCoordinateReferenceSystem()
                            );
                if (reprojectBBox) {
                    requestBounds = requestBounds.transform(fullBounds
                            .getCoordinateReferenceSystem(), true);
                }
            } catch (Exception e) {
            }

            myZoomLevel = 1 - (int)(Math.log(requestBounds.getWidth() / fullBounds.getWidth()));
            LOGGER.info("I've decided this is at zoom level: " + myZoomLevel);

            FeatureCollection col = con.getLayer(index).getFeatureSource()
                .getFeatures(Query.ALL);
            // dummied out... for now
            Iterator it = col.iterator();
            myMin = null;
            myMax = null;

            try {
                while (it.hasNext()) {
                    SimpleFeature f = (SimpleFeature) it.next();

                    long l = ((Long) f.getAttribute("cat")).longValue();
                    if (myMin == null || myMin.longValue() > l)
                        myMin = new Long(l);
                    if (myMax == null || myMax.longValue() < l)
                        myMax = new Long(l);
                }
            } finally {
                col.close(it);
            }

            long range = myMax - myMin;
            myMin = myMin + (myZoomLevel * range / MAX_LEVELS);
            myMax = myMin + (range / MAX_LEVELS);

            LOGGER.info("Regionating data: max: " + myMax + ", min: " + myMin);
        } catch (Exception e) {
            LOGGER.severe("Tried to regionate data, but failed due to " + e);
        }
    }

    public boolean include(SimpleFeature feature){
        try{
            Object att = feature.getAttribute("cat");
            long l = ((Long) att).longValue();

            return myMin <= l && myMax >= l;
        } catch (Exception e){
            LOGGER.info(e.getMessage());
        }
        return false;
    }

}
