package org.vfny.geoserver.wms.responses.map.kml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.DefaultQuery;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.vfny.geoserver.wms.WMSMapContext;

/**
 * Strategy for regionating based on algorithmic stuff related to the actual
 * data. This strategy is fairly ill-defined and should be considered highly
 * experimental.
 * 
 * @author David Winslow
 */
public class DataRegionatingStrategy implements RegionatingStrategy {

    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.geosearch");
    private static long MAX_LEVELS = 5;
    private static long FEATURES_PER_TILE = 5;
    private static String myAttributeName = "count";

    private Long myMin;
    private Long myMax;

    public DataRegionatingStrategy(String attname){
        myAttributeName = attname;
    }

    public void preProcess(WMSMapContext con, int index) {
        LOGGER.severe("Regionate attribute: " + myAttributeName);
        preProcessEven(con, index);
    }

    public void preProcessEven(WMSMapContext con, int index) {
        try {
            long zoomLevel = getZoomLevel(con, index);
            long featuresToSkip = findSkipAmount(zoomLevel);
            long featureCount = findLayerSize(zoomLevel);

            FilterFactory ff = (FilterFactory)CommonFactoryFinder.getFilterFactory(null);
            FeatureSource source = con.getLayer(index).getFeatureSource();

            DefaultQuery query =
                new DefaultQuery(Query.ALL);
            SortBy sortBy = ff.sort(myAttributeName, SortOrder.ASCENDING);
            query.setSortBy(new SortBy[]{sortBy});
            FeatureCollection col = source.getFeatures(Query.ALL); 
//            LOGGER.info("Collection contains " + col.size() + " features.");
//            LOGGER.info("Zoom level: " + zoomLevel);
//            LOGGER.info("Skipping params: " + featuresToSkip + " : " + featureCount);

            Iterator it = col.iterator();

            try{
                SimpleFeature f;
                for (int i = 0; i < featuresToSkip - 1; i++) f = (SimpleFeature)it.next();
                f = (SimpleFeature) it.next();

                if (f.getAttribute(myAttributeName) != null)
                    myMax = ((Number)f.getAttribute(myAttributeName)).longValue();

                for (int i = 0; it.hasNext() && i < featureCount; i++) f = (SimpleFeature)it.next();

                if (it.hasNext()) f = (SimpleFeature)it.next();

                if (f.getAttribute(myAttributeName) != null)
                    myMin = ((Number)f.getAttribute(myAttributeName)).longValue();
            } finally {
                col.close(it);
            }
        } catch (Exception e) {
            LOGGER.severe("Failure while trying to regionate by data: " + e);
            e.printStackTrace();
        }
    }

    private void preProcessBasic(WMSMapContext con, int index) {
        try {
            long zoomLevel = getZoomLevel(con, index);
            // LOGGER.info("I've decided this is at zoom level: " + zoomLevel);

            FeatureCollection col = con.getLayer(index).getFeatureSource().getFeatures(Query.ALL);

            Iterator it = col.iterator();
            myMin = null;
            myMax = null;

            try {
                while (it.hasNext()) {
                    SimpleFeature f = (SimpleFeature) it.next();

                    long l = ((Number) f.getAttribute(myAttributeName)).longValue();
                    if (myMin == null || myMin.longValue() > l)
                        myMin = new Long(l);
                    if (myMax == null || myMax.longValue() < l)
                        myMax = new Long(l);
                }
            } finally {
                col.close(it);
            }

            long range = myMax - myMin;
            myMin = myMin + (zoomLevel * range / MAX_LEVELS);
            myMax = myMin + (range / MAX_LEVELS);

        } catch (Exception e) {
            LOGGER.severe("Tried to regionate data, but failed due to " + e);
        }
    }

    /**
     * Find the number of features that are at lower zoomlevels than the one provided.  When
     * skipping around the sorted list of features to find the minimum value of the range 
     * included at this zoomlevel.
     */
    private long findSkipAmount(long zoomLevel){
        long skipAmount = 0;
        for (long i = 0; i < zoomLevel; i++){
            skipAmount += Math.pow(4, i) * FEATURES_PER_TILE;
        }
        return skipAmount;
    }

    private long findLayerSize(long zoomLevel){
        return (long)Math.pow(4,zoomLevel) * FEATURES_PER_TILE;
    }

    private long getZoomLevel(WMSMapContext context, int index)
        throws Exception {
            FeatureSource<SimpleFeatureType, SimpleFeature> source = 
                (FeatureSource<SimpleFeatureType, SimpleFeature>) context.getLayer(index).getFeatureSource();
            ReferencedEnvelope fullBounds = source.getBounds();
            ReferencedEnvelope requestBounds = context.getAreaOfInterest();
            boolean reprojectBBox = (fullBounds.getCoordinateReferenceSystem() != null)
                && !CRS.equalsIgnoreMetadata(
                        requestBounds.getCoordinateReferenceSystem(), 
                        fullBounds.getCoordinateReferenceSystem()
                        );
            if (reprojectBBox) {
                requestBounds = requestBounds.transform(fullBounds.getCoordinateReferenceSystem(), true);
            } else {
                LOGGER.info("Data-based regionating code couldn't reproject, please make sure your "
                + "datastore is configured properly (hint: for postgis add -s <srsid> to the "
                + "shp2psql command arguments)"
                );
            }
            return 1 - (int) (Math.log(requestBounds.getWidth() / fullBounds.getWidth()));
        }

    public boolean include(SimpleFeature feature) {
        try {
            Object att = feature.getAttribute(myAttributeName);
            long l = ((Number) att).longValue();

            return myMin.longValue() <= l && myMax.longValue() >= l;
        } catch (Exception e) {
            LOGGER.info("Encountered problem while trying to apply data regionating filter: " + e);
        }
        return false;
    }

}
