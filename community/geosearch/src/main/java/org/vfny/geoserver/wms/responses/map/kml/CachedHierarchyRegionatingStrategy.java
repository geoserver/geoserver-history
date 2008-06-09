package org.vfny.geoserver.wms.responses.map.kml;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.ows.HttpErrorCodeException;
import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.jts.JTS;
import org.geotools.map.MapLayer;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.operation.MathTransform;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.wms.WMSMapContext;

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
    
    private static ReferencedEnvelope worldBounds;

    private static String WORLD_SRS = "EPSG:4326";

    private int myFeaturesPerTile;


    public final void preProcess(WMSMapContext con, MapLayer layer) {
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
        Connection connection;
        Statement statement;
        try{
            Class.forName("org.h2.Driver");
            String dataDir = con.getRequest().getWMS().getData().getDataDirectory().getCanonicalPath();
            connection = 
                DriverManager.getConnection(
                    "jdbc:h2:file:" + dataDir + "/h2database/regionate", "geoserver", "geopass"
                    );
            statement = connection.createStatement();
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Couldn't connect to embedded h2 database.", e);
            return;
        }

        try{
            String tableName = findCacheTable(con, layer);

            statement.execute("DROP TABLE IF EXISTS " + tableName);
            statement.execute("CREATE TABLE " + tableName + " ( x integer, y integer, z integer, fid varchar (50))");
            statement.execute("CREATE INDEX ON " + tableName + " (x, y, z)");
            
            CoordinateReferenceSystem epsg4326 = null;
            try { 
            	epsg4326 = CRS.decode("EPSG:4326"); 
            } catch (Exception e){ 
            	LOGGER.log(Level.SEVERE, "Failure to find EPSG:4326!!", e);
            }
            
            
            // GeoWebCache will try to start with the best tile that covers all the data, 
            // so we need to start at the same point
            ReferencedEnvelope layerBounds = layer.getBounds().transform(epsg4326,false);
            
            // ... but if it crosses the centra meridien we need to get two world tiles anyway
	        if(layerBounds.getMinX() < 0.0 && layerBounds.getMaxX() > 0.0) {
	            ReferencedEnvelope worldBounds = getWorldBounds();
	            // Western
	        	Envelope tmp = new Envelope(0.0, -180.0, 90.0, -90.0);
            	buildDB(statement, tableName, layer.getFeatureSource(),
            			new ReferencedEnvelope(tmp, epsg4326), 
            			new TreeSet<String>());
            	// Eastern
            	tmp = new Envelope(180.0, 0.0, 90.0, -90.0);
            	buildDB(statement, tableName, layer.getFeatureSource(),
            			new ReferencedEnvelope(tmp, epsg4326), 
            			new TreeSet<String>());
	        } else {
	        	// Figure out what the closest tile would be, then use that
            	buildDB(statement, tableName, layer.getFeatureSource(), 
            			expandToTile(layerBounds), 
            			new TreeSet<String>());
	        }

            statement.close();
            connection.close();
        } catch (Exception e){
            LOGGER.log(Level.WARNING, "Unable to store range information in database.", e);
        } finally {
            try{
                statement.close(); 
            } catch (SQLException sqle) {
                LOGGER.log(Level.SEVERE, "Error while closing connection to h2 database.", sqle);
            }
            try{
                connection.close();
            } catch (SQLException sqle) {
                LOGGER.log(Level.SEVERE, "Error while closing connection to h2 database.", sqle);
            }
        }
    }

    private Set<String> getRangesFromDB(WMSMapContext con, MapLayer layer) throws Exception{
        Connection connection;
        Statement statement;
        Set<String> returnable = new TreeSet<String>();
        try{
            Class.forName("org.h2.Driver");
            String dataDir = con.getRequest().getWMS().getData().getDataDirectory().getCanonicalPath();
            connection = 
                DriverManager.getConnection(
                        "jdbc:h2:file:" + dataDir + "/h2database/regionate", "geoserver", "geopass"
                        );
            statement = connection.createStatement();
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Couldn't connect to embedded h2 database.", e);
            throw e;
        }

        try{
            String tableName = findCacheTable(con, layer);

            long[] coords = getTileCoords(con.getAreaOfInterest(), getWorldBounds());

            String sql = "SELECT fid FROM " + tableName + " WHERE x = " + coords[0] + " AND y = " + coords[1] + " AND z = " + coords[2];
            statement.execute( sql );

            ResultSet results = statement.getResultSet();
            while (results.next()){
                returnable.add(results.getString(1));
            }
        } finally {
            try {
                statement.close();
            } catch (SQLException sqle){
                LOGGER.log(Level.SEVERE, "Error while closing connection to h2 database.", sqle);
            }
            try {
                connection.close();
            } catch (SQLException sqle) {
                LOGGER.log(Level.SEVERE, "Error while closing connection to h2 database.", sqle);
            }
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
                if (!parents.contains(f.getID()) &&
                        containsCentroid(bbox, (Geometry)f.getDefaultGeometry(), source.getBounds().getCoordinateReferenceSystem())
                   ) {
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

    private boolean containsCentroid(ReferencedEnvelope bbox, Geometry geom, CoordinateReferenceSystem nativeCRS){
        Envelope e = convertBBoxFromLatLon(geom.getEnvelopeInternal(), nativeCRS);
        double centerX = (e.getMaxX() + e.getMinX()) / 2;
        double centerY = (e.getMaxY() + e.getMinY()) / 2;

        return bbox.contains(centerX, centerY) 
            && centerX != bbox.getMinX() 
            && centerY != bbox.getMinY();
    }
    
    private Envelope convertBBoxFromLatLon(Envelope bbox, CoordinateReferenceSystem nativeCRS) {
        CoordinateReferenceSystem latLon = getWorldBounds().getCoordinateReferenceSystem();;

        Envelope env = bbox;
        if (!CRS.equalsIgnoreMetadata(latLon, nativeCRS)){
            try{
            MathTransform xform = CRS.findMathTransform(nativeCRS, latLon, true);
            env = JTS.transform(bbox, null, xform, 10); // convert databbox to native CRS
            } catch (Exception e) {
                // default to provided envelope
                LOGGER.log(Level.WARNING, "Couldn't transform bbox!!", e);
            }
        } 

        return env;
    }

    private FeatureCollection getFeatures(FeatureSource source, ReferencedEnvelope bbox) throws IOException{
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        
        Filter filter = factory.bbox(
                factory.property(source.getSchema().getDefaultGeometry().getName()), 
                bbox.getMinimum(0),
                bbox.getMinimum(1), 
                bbox.getMaximum(0), 
                bbox.getMaximum(1),
                WORLD_SRS
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

    /**
     * @param bbox
     * @return the given bbox split into four equal tiles, as bboxes
     */
    private static List<ReferencedEnvelope> quadSplit(ReferencedEnvelope bbox){
        List<ReferencedEnvelope> results = new ArrayList<ReferencedEnvelope>();
        
        //top left
        results.add(new ReferencedEnvelope(
                bbox.getCenter(0),
                bbox.getMinimum(0),
                bbox.getMaximum(1),
                bbox.getCenter(1),
                bbox.getCoordinateReferenceSystem()
                )
        );
        //top right
        results.add(new ReferencedEnvelope(
                bbox.getMaximum(0),
                bbox.getCenter(0),
                bbox.getMaximum(1),
                bbox.getCenter(1),
                bbox.getCoordinateReferenceSystem()
                )
        );

        // bottom left
        results.add(new ReferencedEnvelope(
                bbox.getCenter(0),
                bbox.getMinimum(0),
                bbox.getCenter(1),
                bbox.getMinimum(1),
                bbox.getCoordinateReferenceSystem()
                )
        );

        // bottom right
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

    // To avoid Caused by: org.geotools.referencing.operation.projection.ProjectionException: 
    // Latitude 90°00.0'S is too close to a pole.
    // we use an envelope
    public static ReferencedEnvelope getWorldBounds(){
    	if(worldBounds == null) {
    		try{
        		Envelope tmp = new Envelope(180.0, -180.0, 90.0, -90.0);
   
    			worldBounds = new ReferencedEnvelope(tmp, CRS.decode(WORLD_SRS));
    		} catch (Exception e){
    			LOGGER.log(Level.SEVERE, "Failure to find EPSG:4326!!", e);
    		}
    	}
        return worldBounds;
    }
    
    /**
     * Finds the BBOX of one tile that best encompasses the given origBounds 
     * 
     * @param origBounds, cannot cross the central meridie
     * @return
     */
    protected static ReferencedEnvelope expandToTile(ReferencedEnvelope origBounds) {
    	if(origBounds.getMaxX() > 0.0 && origBounds.getMinX() < 0.0 ) {
    		return null; // We warned you...
    	}

    	ReferencedEnvelope outerBounds;
    	Envelope tmp;
    	
    	// Find starting point (western or eastern hemisphere)
    	if(origBounds.getMaxX() > 0.0) {
    		tmp = new Envelope(180.0, 0.0, 90.0, -90.0);
    	} else {
    		tmp = new Envelope(0.0, -180.0, 90.0, -90.0);
    	}
    	outerBounds = new ReferencedEnvelope(tmp, getWorldBounds().getCoordinateReferenceSystem());
    	
    	// Make a copy
    	origBounds = new ReferencedEnvelope(origBounds);
    	// Tighten up a little
    	origBounds.expandBy(-1 * origBounds.getWidth()*0.01);
    	
    	// Now keep zooming in until no new tile covers the original bounds
    	boolean zoomIn = true;
    	
    	while(zoomIn) {
    		zoomIn = false;
    		Iterator<ReferencedEnvelope> iter = quadSplit(outerBounds).iterator();
    		while(iter.hasNext()) {
    			ReferencedEnvelope env = iter.next();
    			
    			if(env.covers((Envelope) origBounds)) {
    				outerBounds = env;
    				zoomIn = true;
    			}
    		}
    	}
    		
    	return outerBounds;
    }

    /**
     * Finds the grid location of the given requestBBox, in relation to worldBounds
     * 
     * @param requestBBox
     * @param worldBounds
     * @return {x,y,z}
     */
    public static long[] getTileCoords(ReferencedEnvelope requestBBox, ReferencedEnvelope worldBounds) {
        try{
            requestBBox = requestBBox.transform(worldBounds.getCoordinateReferenceSystem(), true);
        } catch (Exception e){
            LOGGER.log(Level.WARNING, "Couldn't reproject while acquiring tile coordinates", e);
        }
        
        // EPSG4326 is special, in that at at zoomlevel 0 it has two tiles
        // -> introduce notion of maxTileWidth, which is 180ish
        
        double maxTileWidth = worldBounds.getWidth() / 2.0;

        long z = Math.round(Math.log( maxTileWidth/ requestBBox.getWidth())/Math.log(2));
        long x = Math.round(((requestBBox.getMinimum(0) - worldBounds.getMinimum(0)) / maxTileWidth) * Math.pow(2, z));
        long y = Math.round(((worldBounds.getMaximum(1) - requestBBox.getMaximum(1)) / maxTileWidth) * Math.pow(2, z));

        return new long[]{x,y,z};
    }

}
