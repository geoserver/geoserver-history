package org.vfny.geoserver.wms.responses.map.kml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
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

    private List myRanges;
    private Long myMin;
    private Long myMax;
    private long myZoomLevel;

    /**
     * Create a data regionating strategy that uses the specified attribute.
     * @param attname the name of the attribute to use.  The attribute must be numeric.
     */
    public DataRegionatingStrategy(String attname){
        myAttributeName = attname;
    }

    public void preProcess(WMSMapContext con, int index) {
        myZoomLevel = getZoomLevel(con, index);
        myRanges = getRangesFromCache(con, index);
        if (myRanges == null){
            myRanges = preProcessBasic(con, index);
            addRangesToCache(con, index, myRanges);
        }
        setRange(myRanges);
    }

    /**
     * Check the cache and find the cached range values for the current combination of (layer, zoomlevel, attribute). 
     * If the values are not cached, return null.
     *
     * @param con the WMSMapContext for the current request
     * @param index the numeric index of the layer currently being processed
     */
    private List getRangesFromCache(WMSMapContext con, int index){
        return null;
    }

    /**
     * Cache the range values for the current combination of (layer, zoomlevel, attribute)
     */
    private void addRangesToCache(WMSMapContext con, int index, List ranges){
    }

    private void setRange(List ranges){
        myMin = (myZoomLevel <= 1) ? null : new Long(((Number)ranges.get((int)(myZoomLevel - 1))).longValue());
        myMax = (myZoomLevel >= ranges.size()) 
            ? new Long(((Number)ranges.get((int)(ranges.size() - 1))).longValue()) 
            : new Long(((Number)ranges.get((int)(myZoomLevel))).longValue());
    }

    private boolean inRange(Number n){
        if ((myMin != null) && (myMin.longValue() > n.longValue())) return false;
        if ((myMax != null) && (myMax.longValue() < n.longValue())) return false;
        return true;
    }

    public void preProcessEven(WMSMapContext con, int index) {
        List ranges = new ArrayList();
        try {
            FilterFactory ff = (FilterFactory)CommonFactoryFinder.getFilterFactory(null);
            FeatureSource source = con.getLayer(index).getFeatureSource();

            DefaultQuery query =
                new DefaultQuery(Query.ALL);
            SortBy sortBy = ff.sort(myAttributeName, SortOrder.ASCENDING);
            query.setSortBy(new SortBy[]{sortBy});
            FeatureCollection col = source.getFeatures(Query.ALL); 

            Iterator it = col.iterator();

            try{
                SimpleFeature f = null;
                for (int i = 0; i < MAX_LEVELS; i++){
                    int j = 0;
                    do {
                        f = (SimpleFeature)it.next();
                    } while (j < getFeatureCountAtZoomLevel(i));
                    ranges.add(f.getAttribute(myAttributeName));
                }
            } finally {
                col.close(it);
            }
        } catch (Exception e) {
            LOGGER.severe("Failure while trying to regionate by data: " + e);
            e.printStackTrace();
        }
    }

    private List preProcessBasic(WMSMapContext con, int index) {
        List ranges = new ArrayList();
        try {
            long zoomLevel = getZoomLevel(con, index);
            // LOGGER.info("I've decided this is at zoom level: " + zoomLevel);

            FeatureCollection col = con.getLayer(index).getFeatureSource().getFeatures(Query.ALL);

            Iterator it = col.iterator();
            Long min = null;
            Long max = null;

            try {
                while (it.hasNext()) {
                    SimpleFeature f = (SimpleFeature) it.next();

                    long l = ((Number) f.getAttribute(myAttributeName)).longValue();
                    if (min == null || min.longValue() > l)
                        min = new Long(l);
                    if (max == null || max.longValue() < l)
                        max = new Long(l);
                }
            } finally {
                col.close(it);
            }

            long range = max - min;

            for (long i = 1; i < MAX_LEVELS; i++){
                ranges.add(min + (i * range / MAX_LEVELS));
            }
        } catch (Exception e) {
            LOGGER.severe("Tried to regionate data, but failed due to " + e);
        }

        return ranges;
    }

    /**
     * Find the number of features that are at lower zoomlevels than the one provided.  When
     * skipping around the sorted list of features to find the minimum value of the range 
     * included at this zoomlevel.
     */
    private long findSkipAmount(long zoomLevel){
        long skipAmount = 0;
        for (long i = 0; i < zoomLevel; i++){
            skipAmount += getFeatureCountAtZoomLevel(i);
        }
        return skipAmount;
    }

    private long getFeatureCountAtZoomLevel(long zoomLevel){
        return (long)(Math.pow(4, zoomLevel) * FEATURES_PER_TILE);
    }

    private long findLayerSize(long zoomLevel){
        return (long)Math.pow(4,zoomLevel) * FEATURES_PER_TILE;
    }

    private long getZoomLevel(WMSMapContext context, int index){
        try{
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
            return Math.max(1, 1 - (int) (Math.log(requestBounds.getWidth() / fullBounds.getWidth())));
        } catch (Exception e){
            LOGGER.severe("Zoom Level calculation failed, error was: " + e);
            return 1;
        }
    }

    public boolean include(SimpleFeature feature) {
        try {
            Object att = feature.getAttribute(myAttributeName);
            Number num = (Number)att;

            // return myMin.longValue() <= l && myMax.longValue() >= l;
            return inRange(num);
        } catch (Exception e) {
            LOGGER.info("Encountered problem while trying to apply data regionating filter: " + e);
        }
        return false;
    }

}
