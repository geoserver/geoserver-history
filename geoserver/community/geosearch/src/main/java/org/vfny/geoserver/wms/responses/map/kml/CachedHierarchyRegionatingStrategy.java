package org.vfny.geoserver.wms.responses.map.kml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapLayer;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.wms.WMSMapContext;
import org.geoserver.ows.HttpErrorCodeException;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Abstract class to provide 'automagic' caching of a TileLevel hierarchy and use it for a 
 * regionating strategy.
 * 
 * @author David Winslow
 */
public abstract class CachedHierarchyRegionatingStrategy implements RegionatingStrategy {

    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.geosearch");

    private Set myAcceptableFeatures;
    private int myFeaturesPerTile;

    public final void preProcess(WMSMapContext con, MapLayer layer) {
        myFeaturesPerTile = 100;
        
        FeatureTypeInfo fti = con.getRequest().getWMS().getData().getFeatureTypeInfo(layer.getFeatureSource().getName());
        myFeaturesPerTile = fti.getRegionateFeatureLimit();

        try{
            myAcceptableFeatures = getRangesFromDB(con, layer);
        } catch (Exception e){
            LOGGER.log(Level.INFO, "No cached tile hierarchy found; constructing quad tree from data.", e);
            populateDB(con, layer);
            try{
                myAcceptableFeatures = getRangesFromDB(con, layer);
            } catch (Exception e2){
                throw new HttpErrorCodeException(500, "Unexpected error while determining tile contents.", e2);
            }
        }

        if (myAcceptableFeatures.size() == 0){
            throw new HttpErrorCodeException(204); 
        }
    }

    private void populateDB(WMSMapContext con, MapLayer layer){
        try{
            Class.forName("org.h2.Driver");
            String dataDir = con.getRequest().getWMS().getData().getDataDirectory().getCanonicalPath();
            Connection connection = 
                DriverManager.getConnection(
                    "jdbc:h2:file:" + dataDir + "/h2database/regionate","geoserver", "geopass"
                    );

            String tableName = findCacheTable(con, layer);

            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS " + tableName);
            statement.execute("CREATE TABLE " + tableName + " ( x integer, y integer, z integer, fid varchar (50))");
            statement.execute("CREATE INDEX ON " + tableName + " (x, y, z)");

            ReferencedEnvelope worldBounds = getWorldBounds();
            ReferencedEnvelope western = new ReferencedEnvelope(
                    worldBounds.getMinimum(0),
                    worldBounds.getCenter(0),
                    worldBounds.getMinimum(1),
                    worldBounds.getMaximum(1),
                    worldBounds.getCoordinateReferenceSystem()
                    );
            ReferencedEnvelope eastern = new ReferencedEnvelope(
                    worldBounds.getCenter(0),
                    worldBounds.getMaximum(0),
                    worldBounds.getMinimum(1),
                    worldBounds.getMaximum(1),
                    worldBounds.getCoordinateReferenceSystem()
                    );


            buildDB(statement, tableName, layer.getFeatureSource(), western, new TreeSet<String>());
            buildDB(statement, tableName, layer.getFeatureSource(), western, new TreeSet<String>());

            statement.close();
            connection.close();
        } catch (Exception e){
            LOGGER.log(Level.WARNING, "Unable to store range information in database.", e);
        }
    }

    private Set getRangesFromDB(WMSMapContext con, MapLayer layer) throws Exception{
        Class.forName("org.h2.Driver");
        String dataDir = con.getRequest().getWMS().getData().getDataDirectory().getCanonicalPath();
        Connection connection = 
        	DriverManager.getConnection(
        			"jdbc:h2:file:" + dataDir + "/h2database/regionate", "geoserver", "geopass"
        			);
        String tableName = findCacheTable(con, layer);

        long[] coords = getTileCoords(con.getAreaOfInterest(), getWorldBounds());

        Statement statement = connection.createStatement();
        String sql = "SELECT fid FROM " + tableName + " WHERE x = " + coords[0] + " AND y = " + coords[1] + " AND z = " + coords[2];
        statement.execute( sql );

        ResultSet results = statement.getResultSet();
        Set returnable = new TreeSet();
        while (results.next()){
            returnable.add(results.getString(1));
        }

        return returnable;
    }

    protected String findCacheTable(WMSMapContext con, MapLayer layer){
        try{
            FeatureSource source = layer.getFeatureSource();
            MapLayerInfo[] config = con.getRequest().getLayers();
            for (int i = 0; i < config.length; i++){
                MapLayerInfo theLayer = config[i];
                if (theLayer.getName().equals(layer.getTitle())){
                    return theLayer.getDirName();
                } 
            }
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Exception while finding the location for the cachefile!", e);
        }
        return null;
    }

    public final boolean include(SimpleFeature feature) {
        try {
            return myAcceptableFeatures.contains(feature.getID());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Encountered problem while trying to apply data regionating filter: ", e);
        }
        return false;
    }

    public abstract Comparator<SimpleFeature> getComparator();

    public final void buildDB(Statement st, String tablename, FeatureSource source, ReferencedEnvelope bbox, Set<String> parents) throws IOException{
        PriorityQueue<SimpleFeature> pq = new PriorityQueue<SimpleFeature>(myFeaturesPerTile, getComparator());
        FeatureCollection col = getFeatures(source, bbox);
        Iterator<SimpleFeature> it = col.iterator();
        try{
            while (it.hasNext()){
                SimpleFeature f = it.next();
                if (!parents.contains(f.getID())){
                    pq.add(f);
                    if (pq.size() > myFeaturesPerTile) pq.poll();
                }
            } 
        } finally {
            col.close(it);
        }

        if (pq.size() > 0){
            for (SimpleFeature feature : pq) writeToDB(st, tablename, bbox, feature);
            for (SimpleFeature feature : pq) parents.add(feature.getID());
            for (ReferencedEnvelope box : quadSplit(bbox)) buildDB(st, tablename, source, box, parents);
            for (SimpleFeature feature : pq) parents.remove(feature.getID());
        }
    }

    private FeatureCollection getFeatures(FeatureSource source, ReferencedEnvelope bbox) throws IOException{
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        String crsName = "EPSG:4326";
        try{
            crsName = CRS.lookupIdentifier(bbox.getCoordinateReferenceSystem(), false);
        } catch (Exception e){
            LOGGER.log(Level.WARNING, "Couldn't find id for crs: " + bbox.getCoordinateReferenceSystem().getName(), e);
        }
        Filter filter = factory.bbox(
                factory.property(source.getSchema().getDefaultGeometry().getName()), 
                bbox.getMinimum(0),
                bbox.getMinimum(1), 
                bbox.getMaximum(0), 
                bbox.getMaximum(1),
                crsName
                );

        // LOGGER.info("Filtering by: " + filter);
        return source.getFeatures(filter);
    }

    private void writeToDB(Statement st, String tablename, ReferencedEnvelope bbox, SimpleFeature f){
        long[] coords = getTileCoords(bbox, getWorldBounds());
        try{
            String SQL = "INSERT INTO " + tablename + " VALUES ( " + coords[0] + ", " + coords[1] + ", " + coords[2] + ", \'" + f.getID() + "\' )"; 
            st.execute(SQL);
        } catch (SQLException sqle){
            LOGGER.log(Level.SEVERE, "Problem while storing regionated hierarchy in database: ", sqle);
        }
    }

    private List<ReferencedEnvelope> quadSplit(ReferencedEnvelope bbox){
        List<ReferencedEnvelope> results = new ArrayList<ReferencedEnvelope>();
        results.add(new ReferencedEnvelope(
                bbox.getCenter(0),
                bbox.getMinimum(0),
                bbox.getMaximum(1),
                bbox.getCenter(1),
                bbox.getCoordinateReferenceSystem()
                )
        );

        results.add(new ReferencedEnvelope(
                bbox.getMaximum(0),
                bbox.getCenter(0),
                bbox.getMaximum(1),
                bbox.getCenter(1),
                bbox.getCoordinateReferenceSystem()
                )
        );

        results.add(new ReferencedEnvelope(
                bbox.getCenter(0),
                bbox.getMinimum(0),
                bbox.getCenter(1),
                bbox.getMinimum(1),
                bbox.getCoordinateReferenceSystem()
                )
        );

        results.add(new ReferencedEnvelope(
                bbox.getMaximum(0),
                bbox.getCenter(0),
                bbox.getCenter(1),
                bbox.getMinimum(1),
                bbox.getCoordinateReferenceSystem()
                )
        );

        return results;
    }

    public static ReferencedEnvelope getWorldBounds(){
        try{
            // To avoid Caused by: org.geotools.referencing.operation.projection.ProjectionException: 
            // Latitude 90°00.0'S is too close to a pole.
            return new ReferencedEnvelope(-179.9, 179.9, -89.95, 89.95, CRS.decode("EPSG:4326"));
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Failure to find EPSG:4326!!", e);
        }

        return null;
    }

    public static long[] getTileCoords(ReferencedEnvelope requestBBox, ReferencedEnvelope worldBounds){
        try{
            requestBBox = requestBBox.transform(worldBounds.getCoordinateReferenceSystem(), true);
        } catch (Exception e){
            LOGGER.log(Level.WARNING, "Couldn't reproject while acquiring tile coordinates", e);
        }

        long z = Math.round(Math.log(worldBounds.getWidth() / requestBBox.getWidth())/Math.log(2));
        long x = Math.round(((requestBBox.getMinimum(0) - worldBounds.getMinimum(0)) / worldBounds.getLength(0)) * Math.pow(2, z));
        long y = Math.round(((worldBounds.getMaximum(1) - requestBBox.getMaximum(1)) / worldBounds.getLength(1)) * Math.pow(2, z-1));

        return new long[]{x,y,z};
    }

}
