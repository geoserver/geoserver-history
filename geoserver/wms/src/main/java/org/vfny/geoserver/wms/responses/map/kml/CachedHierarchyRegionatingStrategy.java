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
import java.util.concurrent.locks.ReentrantLock;
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
import org.opengis.referencing.operation.TransformException;
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

    private Set myAcceptableFeatures = null;
    
    private static ReferencedEnvelope worldBounds;

    private static String WORLD_SRS = "EPSG:4326";

    private int myFeaturesPerTile;

    private String myCacheTable;

    private static final ReentrantLock lock = new ReentrantLock();

    /**
     * When the number of features within the bbox currently being processed is below this threshold,
     * go ahead and build the rest of the tile hierarchy in memory.  (This is a performance thing; 
     * sweeping over the database for each tile can really bog things down.)
     */
    private static int DB_SWEEP_CUTOFF = 50000; 

    public static long featureCounter = 0;

    public final void preProcess(WMSMapContext con, MapLayer layer) {
        FeatureTypeInfo fti = con.getRequest().getWMS().getData().getFeatureTypeInfo(layer.getFeatureSource().getName());
        myFeaturesPerTile = fti.getRegionateFeatureLimit();

        try{
            myAcceptableFeatures = getRangesFromDB(con, layer);            
        } catch (Exception e){
            LOGGER.log(Level.INFO, "No cached tile hierarchy found; constructing quad tree from data: " + e.getMessage());
            
            lock.lock();
            try {
                // Try again, may have been populated while we waited
                try{
                    myAcceptableFeatures = getRangesFromDB(con, layer);
                } catch (Exception e2) {
                    // Okay, this is a new one, lets populate it
                    populateDB(con, layer);
                    
                    // It *really* should work now
                    try{
                        myAcceptableFeatures = getRangesFromDB(con, layer);
                    } catch (Exception e3){
                        throw new HttpErrorCodeException(500, "Unexpected error while determining tile contents.", e3);
                    }
                }
            } finally {
              lock.unlock();
            }
        }
        
        // This okay, just means the tile is empty
        if (myAcceptableFeatures.size() == 0){
            throw new HttpErrorCodeException(204); 
        }


    }

    private void populateDB(WMSMapContext con, MapLayer layer) {
        Connection connection;
        Statement statement;
        
        String tableName = null;
        try {
            tableName = getCacheTable(con, layer);
            
            Class.forName("org.h2.Driver");
            String dataDir = con.getRequest().getWMS().getData()
                    .getDataDirectory().getCanonicalPath();
            connection = DriverManager.getConnection("jdbc:h2:file:" + dataDir
                    + "/h2database/regionate_"+tableName, "geoserver", "geopass");
            statement = connection.createStatement();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Couldn't connect to embedded h2 database.", e);
            return;
        }

        

        try {
            statement.execute("DROP TABLE IF EXISTS " + tableName);
            statement.execute("CREATE TABLE " + tableName
                    + " ( x BIGINT, y BIGINT, z INT, fid varchar (64))");
            statement.execute("CREATE INDEX ON " + tableName + " (x, y, z)");

            CoordinateReferenceSystem epsg4326 = null;
            try {
                epsg4326 = CRS.decode("EPSG:4326");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failure to find EPSG:4326!!", e);
            }

            /**
             * GeoWebCache will try to start with the best tile that covers all
             * the data, so we need to start at the same point
             **/
            

            ReferencedEnvelope layerBounds = null;
            
            if(layer.getBounds().crs().equals(epsg4326)) {
                layerBounds = layer.getBounds();
            } else {
                LOGGER.log(Level.SEVERE, "Transforming " + layer.getBounds().toString() + " to EPSG:4326.");
                try {
                    layerBounds = layer.getBounds().transform(epsg4326, false);
                } catch (NullPointerException npe) {
                    LOGGER.log(Level.SEVERE, "Caught NPE, presumably from transform, reverting to world bounds.");
                } catch (TransformException te) { 
                    LOGGER.log(Level.SEVERE, "Encountered transform exception, reverting to world bounds: "
                            + te.getMessage());
                }
                
                if(layerBounds == null) {
                    layerBounds = getWorldBounds();
                }
            }
            // ... but if it crosses the central meridien we need to get two
            // world tiles anyway
            if (layerBounds.getMinX() < 0.0 && layerBounds.getMaxX() > 0.0) {
                LOGGER.log(Level.SEVERE,
                        "Regionating strategy for layer "+ layer.getTitle()
                      + " will use world bounds. Starting to build database now.");
                this.featureCounter = 0; // global

                // Western
                ReferencedEnvelope tmp = new ReferencedEnvelope(new Envelope(
                        0.0, -180.0, 90.0, -90.0), epsg4326);

                buildDB(statement, tableName, layer.getFeatureSource(), tmp,
                        new TreeSet<String>());
                // Eastern
                tmp = new ReferencedEnvelope(new Envelope(180.0, 0.0, 90.0,
                        -90.0), epsg4326);

                buildDB(statement, tableName, layer.getFeatureSource(), tmp,
                        new TreeSet<String>());
            } else {
                // Figure out what the closest tile would be, then use that
                ReferencedEnvelope outerBounds = expandToTile(layerBounds);
                long[] coords = getTileCoords(outerBounds, getWorldBounds());
                LOGGER.log(Level.SEVERE, "Regionating strategy for layer "
                        + layer.getTitle() + " will start at "
                        + outerBounds.toString() + " , " + coords[0] + ","
                        + coords[1] + "," + coords[2]
                        + ".  Starting to build database now.");
                this.featureCounter = 0; // global

                buildDB(statement, tableName, layer.getFeatureSource(),
                        outerBounds, new TreeSet<String>());
            }
            statement.close();
            connection.close();

        } catch (Exception e) {
            LOGGER.log(Level.WARNING,
                    "Unable to store range information in database.", e);
        } finally {
            try {
                statement.close();
            } catch (SQLException sqle) {
                LOGGER.log(Level.SEVERE,
                        "Error while closing connection to h2 database.", sqle);
            }
            try {
                connection.close();
            } catch (SQLException sqle) {
                LOGGER.log(Level.SEVERE,
                        "Error while closing connection to h2 database.", sqle);
            }
        }

        // Check how much we actually stored 
        //(yeah, that reopens everything, but that's the point):
        long dbRows = getRowsInDB(con, tableName);
        LOGGER.log(Level.SEVERE, "Table " + tableName + " contains "
                + Long.toString(dbRows) + " rows after populateDB()");
    }

    private Set<String> getRangesFromDB(WMSMapContext con, MapLayer layer) throws Exception{
        Connection connection;
        Statement statement;
        Set<String> returnable = new TreeSet<String>();
        String tableName = null;
        
        try{
            tableName = getCacheTable(con, layer);
            
            Class.forName("org.h2.Driver");
            String dataDir = con.getRequest().getWMS().getData().getDataDirectory().getCanonicalPath();
            connection = 
                DriverManager.getConnection(
                        "jdbc:h2:file:" + dataDir + "/h2database/regionate_"+tableName, "geoserver", "geopass"
                        );
            statement = connection.createStatement();
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Couldn't connect to embedded h2 database.", e);
            throw e;
        }

        long[] coords = null;
        
        try{
            coords = getTileCoords(con.getAreaOfInterest(), getWorldBounds());
            
            //LOGGER.log(Level.SEVERE, "Received request for "+layer.getTitle() + " " +
            //		con.getAreaOfInterest().toString() + " " + coords[0] + ","
            //		+ coords[1] + "," + coords[2]);
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

        //LOGGER.log(Level.SEVERE, "Returning "+returnable.size()
        //		+ " features from " + tableName + " for " 
        //		+ coords[0] + ","+ coords[1] + "," + coords[2]);
        
        return returnable;
    }

    private final String getCacheTable(WMSMapContext con, MapLayer layer){
        if (myCacheTable == null){
            myCacheTable = findCacheTable(con, layer);
        }
        return myCacheTable;
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
    	FeatureCollection col = getFeatures(source, bbox);
        ReferencedEnvelope reprojectedBBox;
        try{
            reprojectedBBox = bbox.transform(source.getBounds().getCoordinateReferenceSystem(), true);
        } catch (Exception e){
            reprojectedBBox = bbox;
            LOGGER.log(Level.WARNING, "Couldn't transform bbox to native CRS; using lat/lon bbox instead.", e);
        }

        if (col.size() > DB_SWEEP_CUTOFF){
            PriorityQueue<SimpleFeature> pq = new PriorityQueue<SimpleFeature>(myFeaturesPerTile, getComparator());
            Iterator<SimpleFeature> it = col.iterator();
            try{
                while (it.hasNext()){
                    SimpleFeature f = it.next();
                    if (!parents.contains(f.getID()) &&
                            containsCentroid(reprojectedBBox, (Geometry)f.getDefaultGeometry())
                       ) {
                        pq.add(f);
                        featureCounter++;
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
        } else {
        	// We're  trying to be clever here, not loop over the entire dataset for each tile
            TileLevel root = new TileLevel(reprojectedBBox, myFeaturesPerTile, getComparator());
            root.populateExcluding(col, parents);
            ReferencedEnvelope world = getWorldBounds();
            try{
            	// This will *always* fail with the current version of GT,
            	// because world -> ReferencedEnvelope[-180.0 : 180.0, -90.0 : 90.0] -> TransformException
                //world.transform(source.getBounds().getCoordinateReferenceSystem(), true);
            	
            	// Instead try
        		Envelope tmp = new Envelope(179.99, -179.99, 89.995, -89.995);
        		world = new ReferencedEnvelope(tmp, getWorldBounds().getCoordinateReferenceSystem());
        		world.transform(source.getBounds().getCoordinateReferenceSystem(), true);
        		
            } catch (Exception e){
                // ignore, just use untransformed value
                LOGGER.log(Level.WARNING, "Couldn't convert world bounds to native coords", e);
            }
            root.writeTo(st, tablename, world);
        }
        
        LOGGER.log(Level.SEVERE, "builDB of " + tablename + " completed after " 
                + Long.toString(this.featureCounter) + " features. (Number may not be accurate.)");
    }

    public static boolean containsCentroid(ReferencedEnvelope bbox, Geometry geom){
        Envelope e = geom.getEnvelopeInternal();
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

        return  source.getFeatures(filter);
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
    protected static List<ReferencedEnvelope> quadSplit(ReferencedEnvelope bbox){
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
    	
    	// Tighten up a little, we're expanding in the next step
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
        long y = Math.round(((requestBBox.getMinimum(1) - worldBounds.getMinimum(1)) / maxTileWidth) * Math.pow(2, z));

        return new long[]{x,y,z};
    }
    
    private long getRowsInDB(WMSMapContext con, String tableName) {
    	long result = -1;
    	
        Connection connection;
        Statement statement;
        
        try{
            Class.forName("org.h2.Driver");
            String dataDir = con.getRequest().getWMS().getData().getDataDirectory().getCanonicalPath();
            connection = 
                DriverManager.getConnection(
                    "jdbc:h2:file:" + dataDir + "/h2database/regionate_"+tableName, "geoserver", "geopass"
                    );
            statement = connection.createStatement();
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Couldn't connect to embedded h2 database.", e);
            return result;
        }
        
        try {
        	statement.execute("SELECT COUNT(x) FROM " + tableName);
        	ResultSet results = statement.getResultSet();
        	results.first();
        	result = results.getInt(1);
        	statement.close();
        	connection.close();
        
        } catch (SQLException sqle) {
        	LOGGER.log(Level.SEVERE, sqle.getMessage());
        }
        return result;
    }

}
