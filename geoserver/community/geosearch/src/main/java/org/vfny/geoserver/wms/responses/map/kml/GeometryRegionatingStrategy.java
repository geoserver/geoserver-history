package org.vfny.geoserver.wms.responses.map.kml;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.FeatureSource;
import java.util.Comparator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.opengis.filter.FilterFactory;
import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geoserver.ows.HttpErrorCodeException;
import org.vfny.geoserver.wms.WMSMapContext;
import org.geotools.map.MapLayer;


public class GeometryRegionatingStrategy extends CachedHierarchyRegionatingStrategy{
    Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.geosearch");
    public static int FEATURES_PER_TILE = 100;

    protected String findCacheTable(WMSMapContext con, MapLayer layer){
        return super.findCacheTable(con, layer) + "_geometry_regionator";
    }

    protected TileLevel createTileHierarchy(WMSMapContext con, MapLayer layer){
        LOGGER.info("About to build the geometry hierarchy");
        try{
            FeatureSource<SimpleFeatureType, SimpleFeature> source = 
                (FeatureSource<SimpleFeatureType, SimpleFeature>) layer.getFeatureSource();
            CoordinateReferenceSystem nativeCRS = source.getSchema().getDefaultGeometry().getCRS();
            ReferencedEnvelope fullBounds = TileLevel.getWorldBounds();
            fullBounds = fullBounds.transform(nativeCRS, true);
            TileLevel root = TileLevel.makeRootLevel(fullBounds, FEATURES_PER_TILE, new GeometryComparator());
            FilterFactory ff = (FilterFactory)CommonFactoryFinder.getFilterFactory(null);
            DefaultQuery query = new DefaultQuery(Query.ALL);
            FeatureCollection col = source.getFeatures(query);

            root.populate(col);

            return root;
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Error while trying to regionate by geometry.", e);
            throw new HttpErrorCodeException(500, "Error while trying to regionate by geometry.", e);
        }
    }

    private class GeometryComparator implements Comparator {
        public int compare(Object a, Object b){
            SimpleFeature fa = (SimpleFeature) a;
            SimpleFeature fb = (SimpleFeature) b;
            
            return 0;
        }
    }
}

