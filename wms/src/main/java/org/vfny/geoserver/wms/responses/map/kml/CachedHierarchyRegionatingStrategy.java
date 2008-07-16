package org.vfny.geoserver.wms.responses.map.kml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.ows.HttpErrorCodeException;
import org.geotools.data.FeatureSource;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapLayer;
import org.geotools.referencing.CRS;
import org.geotools.util.CanonicalSet;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * Base class for regionating strategies. Common functionality provided:
 * <ul>
 * <li>tiling based on the TMS tiling recommendation</li>
 * <li>caching the assignment of a feature in a specific tile in an H2 database
 * stored in the data directory</li>
 * <li>
 * 
 * @author Andrea Aime - OpenGeo
 * @author David Winslow - OpenGeo
 * @author Arne Kepp - OpenGeo
 */
public abstract class CachedHierarchyRegionatingStrategy implements
        RegionatingStrategy {
    static Logger LOGGER = Logging.getLogger("org.geoserver.geosearch");

    static final CoordinateReferenceSystem WGS84;

    static final ReferencedEnvelope WORLD_BOUNDS;

    static final double MAX_TILE_WIDTH;

    static final double MAX_ERROR = 0.02;

    static final Set<String> NO_FIDS = Collections.emptySet();

    /**
     * This structure is used to make sure that multiple threads end up using
     * the same table name object, so that we can use it as a synchonization
     * token
     */
    static CanonicalSet<String> canonicalizer = CanonicalSet
            .newInstance(String.class);

    static {
        try {
            // common geographic info
            WGS84 = CRS.decode("EPSG:4326");
            WORLD_BOUNDS = new ReferencedEnvelope(new Envelope(180.0, -180.0,
                    90.0, -90.0), WGS84);
            MAX_TILE_WIDTH = WORLD_BOUNDS.getWidth() / 2.0;

            // make sure, once and for all, that H2 is around
            Class.forName("org.h2.Driver");
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not initialize the class constants", e);
        }
    }

    /**
     * The features contained in the current tile
     */
    protected Set<String> featuresInTile = Collections.emptySet();

    /**
     * The original area occupied by the data
     */
    protected ReferencedEnvelope dataEnvelope;

    /**
     * Reference to the layer being regionated
     */
    protected FeatureTypeInfo typeInfo;

    protected int featuresPerTile;

    protected String tableName;

    public boolean include(SimpleFeature feature) {
        return featuresInTile.contains(feature.getID());
    }

    public void preProcess(WMSMapContext context, MapLayer layer) {
        try {
            // grab information needed to reach the db and get a hold to a db
            // connection
            Data catalog = context.getRequest().getWMS().getData();
            FeatureSource featureSource = layer.getFeatureSource();
            typeInfo = catalog.getFeatureTypeInfo(featureSource.getName());
            String dataDir = catalog.getDataDirectory().getCanonicalPath();
            tableName = getDatabaseName(context, layer);

            // grab the features per tile, use a default if user did not
            // provide a decent value. The default should fill up the
            // tile when it shows up.
            featuresPerTile = typeInfo.getRegionateFeatureLimit();
            if (featuresPerTile <= 1)
                featuresPerTile = 64;

            // sanity check, the layer is not geometryless
            if (typeInfo.isGeometryless())
                throw new WmsException(typeInfo.getName()
                        + " is geometryless, cannot generate KML!");

            // make sure the request is within the data bounds, allowing for a
            // small error
            ReferencedEnvelope requestedEnvelope = context.getAreaOfInterest()
                    .transform(WGS84, true);
            LOGGER.log(Level.FINE, "Requested tile: {0}", requestedEnvelope);
            dataEnvelope = new ReferencedEnvelope(typeInfo
                    .getLatLongBoundingBox(), WGS84);

            // decide which tile we need to load/compute, and make sure
            // it's a valid tile request, that is, that is does fit with
            // the general tiling scheme (minus an eventual small error)
            Tile tile = new Tile(requestedEnvelope);
            ReferencedEnvelope tileEnvelope = tile.getEnvelope();
            if (!envelopeMatch(tileEnvelope, requestedEnvelope))
                throw new WmsException(
                        "Invalid bounding box request, it does not fit "
                                + "the nearest regionating tile. Requested area: "
                                + requestedEnvelope + ", " + "nearest tile: "
                                + tileEnvelope);

            // oki doki, let's compute the fids in the requested tile
            featuresInTile = getFeaturesForTile(dataDir, tile);
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE,
                    "Error occurred while pre-processing regionated features",
                    t);
            // shouldn't we rethrow the exception and fail with a OGC service
            // exception or a 505?
        }

        // This okay, just means the tile is empty
        if (featuresInTile.size() == 0) {
            throw new HttpErrorCodeException(204);
        }
    }

    /**
     * Returns true if the two envelope roughly match, that is, they are about
     * the same size and about the same location. The max difference allowed is
     * {@link #MAX_ERROR}, evaluated as a percentage of the width and height of
     * the envelope.
     * The method assumes both envelopes are in the same CRS
     * 
     * @param tileEnvelope
     * @param expectedEnvelope 
     * @return
     */
    private boolean envelopeMatch(ReferencedEnvelope tileEnvelope,
            ReferencedEnvelope expectedEnvelope) {
        double widthRatio = Math.abs(1.0 - tileEnvelope.getWidth()
                / expectedEnvelope.getWidth());
        double heightRatio = Math.abs(1.0 - tileEnvelope.getHeight()
                / expectedEnvelope.getHeight());
        double xRatio = Math.abs((tileEnvelope.getMinX() - expectedEnvelope
                .getMinX())
                / tileEnvelope.getWidth());
        double yRatio = Math.abs((tileEnvelope.getMinY() - expectedEnvelope
                .getMinY())
                / tileEnvelope.getHeight());
        return widthRatio < MAX_ERROR && heightRatio < MAX_ERROR
                && xRatio < MAX_ERROR && yRatio < MAX_ERROR;
    }

    /**
     * Open/creates the db and then reads/computes the tile features
     * 
     * @param dataDir
     * @param tile
     * @return
     * @throws Exception
     */
    private Set<String> getFeaturesForTile(String dataDir, Tile tile)
            throws Exception {
        Connection conn = null;
        Statement st = null;

        // build the synchonization token
        canonicalizer.add(tableName);
        tableName = canonicalizer.get(tableName);

        try {
            // make sure no two thread in parallel can build the same db
            synchronized (tableName) {
                // get a hold to the database that contains the cache (this will
                // eventually create the db)
                conn = DriverManager.getConnection("jdbc:h2:file:" + dataDir
                        + "/geosearch/h2cache_" + tableName, "geoserver",
                        "geopass");

                // try to create the table, if it's already there this will fail
                st = conn.createStatement();
                st.execute("CREATE TABLE IF NOT EXISTS TILECACHE( " //
                        + "x BIGINT, " //
                        + "y BIGINT, " //
                        + "z INT, " //
                        + "fid varchar (64))");
                st.execute("CREATE INDEX IF NOT EXISTS IDX_TILECACHE ON TILECACHE(x, y, z)");
            }

            return readFeaturesForTile(tile, conn);
        } finally {
            JDBCUtils.close(st);
            JDBCUtils.close(conn, null, null);
        }
    }

    /**
     * Reads/computes the tile feature set
     * 
     * @param tile
     *            the Tile whose features we must find
     * @param conn
     *            the H2 connection
     * @return
     * @throws Exception
     */
    protected Set<String> readFeaturesForTile(Tile tile, Connection conn)
            throws Exception {
        // grab the fids and decide whether we have to compute them
        Set<String> fids = readCachedTileFids(tile, conn);
        if (fids != null) {
            return fids;
        } else {
            // build the synchronization token
            String tileKey = tableName + tile.x + "-" + tile.y + "-" + tile.z;
            canonicalizer.add(tileKey);
            tileKey = canonicalizer.get(tileKey);

            synchronized (tileKey) {
                // might have been built while we were waiting
                fids = readCachedTileFids(tile, conn);
                if (fids != null)
                    return fids;

                // still missing, we need to compute them
                fids = computeFids(tile, conn);
                storeFids(tile, fids, conn);

                // optimization, if we did not manage to fill up this tile,
                // the ones below it will be empty -> mark them as such right
                // away
                if (fids.size() < featuresPerTile)
                    for (Tile child : tile.getChildren())
                        storeFids(child, NO_FIDS, conn);
            }
        }
        return fids;
    }

    /**
     * Store the fids inside
     * 
     * @param t
     * @param fids
     * @param conn
     * @throws SQLException
     */
    private void storeFids(Tile t, Set<String> fids, Connection conn)
            throws SQLException {
        PreparedStatement ps = null;
        try {
            // we are going to execute this one many times, 
            // let's prepare it so that the db engine does 
            // not have to parse it at every call
            String stmt = "INSERT INTO TILECACHE VALUES (" + t.x + ", " + t.y
                    + ", " + t.z + ", ?)";
            ps = conn.prepareStatement(stmt);

            if (fids.size() == 0) {
                // we just have to mark the tile as empty
                ps.setString(1, null);
                ps.execute();
            } else {
                // store all the fids
                conn.setAutoCommit(false);
                for (String fid : fids) {
                    ps.setString(1, fid);
                    ps.execute();
                }
                conn.commit();
            }
        } finally {
            conn.setAutoCommit(true);
            JDBCUtils.close(ps);
        }
    }

    /**
     * Computes the fids that will be stored in the specified tile
     * 
     * @param tileCoords
     * @param st
     * @return
     * @throws SQLException
     */
    private Set<String> computeFids(Tile tile, Connection conn)
            throws Exception {
        Set<String> parentFids = getUpwardFids(tile.getParent(), conn);
        Set<String> currFids = new HashSet<String>();
        FeatureIterator fi = null;
        try {
            // grab the features
            ReferencedEnvelope tileEnvelope = tile.getEnvelope();
            fi = getSortedFeatures(tileEnvelope, conn);

            // if the crs is not wgs84, we'll need to transform the point
            MathTransform tx = null;
            double[] coords = new double[2];

            // scan counting how many fids we've collected
            boolean first = true;
            while (fi.hasNext() && currFids.size() < featuresPerTile) {
                // grab the feature, skip it if it's already in a parent element
                SimpleFeature f = (SimpleFeature) fi.next();
                if (parentFids.contains(f.getID()))
                    continue;

                // check the need for a transformation
                if (first) {
                    first = false;
                    CoordinateReferenceSystem nativeCRS = f.getType()
                            .getCoordinateReferenceSystem();
                    typeInfo.getFeatureType().getCoordinateReferenceSystem();
                    if (nativeCRS != null
                            && !CRS.equalsIgnoreMetadata(nativeCRS, WGS84)) {
                        tx = CRS.findMathTransform(nativeCRS, WGS84);
                    }
                }

                // see if the features is to be included in this tile
                Point p = ((Geometry) f.getDefaultGeometry()).getCentroid();
                coords[0] = p.getX();
                coords[1] = p.getY();
                if (tx != null)
                    tx.transform(coords, 0, coords, 0, 1);
                if (tileEnvelope.contains(coords[0], coords[1]))
                    currFids.add(f.getID());
            }
        } finally {
            if (fi != null)
                fi.close();
        }
        return currFids;
    }

    /**
     * Returns all the features in the specified envelope, sorted according to
     * the priority used for regionating. The features returned do not have to
     * be the feature type ones, it's sufficient that they have the same FID and
     * a geometry whose centroid is the same as the original feature one.
     * 
     * @param envelope
     * @param indexConnection
     *            a connection to the feature id cache db
     * @return
     * @throws Exception
     */
    protected abstract FeatureIterator getSortedFeatures(
            ReferencedEnvelope envelope, Connection indexConnection)
            throws Exception;

    /**
     * Returns a set of all the fids in the specified tile and in the parents of
     * it, recursing up to the root tile
     * 
     * @param tile
     * @param st
     * @return
     * @throws SQLException
     */
    private Set<String> getUpwardFids(Tile tile, Connection conn)
            throws Exception {
        // recursion stop condition
        if (tile == null)
            return Collections.EMPTY_SET;

        // return the curren tile fids, and recurse up to the parent
        Set<String> fids = new HashSet();
        fids.addAll(readFeaturesForTile(tile, conn));
        fids.addAll(getUpwardFids(tile.getParent(), conn));
        return fids;
    }

    /**
     * Here we have three cases
     * <ul>
     * <li>the tile was already computed, and it resulted to be empty. We leave
     * a "x,y,z,null" marker to know if that happened, and in this case the
     * returned set will be empty</li> <li>the tile was already computed, and we
     * have data, the returned sest will be non empty</li> <li>the tile is new,
     * the db contains nothing, in this case we return "null"</li>
     * <ul>
     * 
     * @param tileCoords
     * @param conn
     * @throws SQLException
     */
    protected Set<String> readCachedTileFids(Tile tile, Connection conn)
            throws SQLException {
        Set<String> fids = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT fid FROM TILECACHE where x = "
                    + tile.x + " AND y = " + tile.y + " and z = " + tile.z);
            // decide whether we have to collect the fids or just to
            // return that the tile was empty
            if (rs.next()) {
                String fid = rs.getString(1);
                if (fid == null) {
                    return Collections.emptySet();
                } else {
                    fids = new HashSet<String>();
                    fids.add(fid);
                }
            }
            // fill the set with the collected fids
            while (rs.next()) {
                fids.add(rs.getString(1));
            }
        } finally {
            JDBCUtils.close(rs);
            JDBCUtils.close(st);
        }

        return fids;
    }

    /**
     * Returns the name to be used for the database. Should be unique for this
     * specific regionated layer.
     * 
     * @param con
     * @param layer
     * @return
     */
    protected String getDatabaseName(WMSMapContext con, MapLayer layer)
            throws Exception {
        MapLayerInfo[] config = con.getRequest().getLayers();
        for (int i = 0; i < config.length; i++) {
            MapLayerInfo theLayer = config[i];
            if (theLayer.getName().equals(layer.getTitle())) {
                return theLayer.getDirName();
            }
        }
        throw new RuntimeException("Weren't able to find the layer "
                + "inside the map context, this is most disturbing...");
    }

    /**
     * A regionating tile identified by its coordinates
     * 
     * @author Andrea Aime
     */
    protected class Tile {
        long x;

        long y;

        long z;

        ReferencedEnvelope envelope;

        /**
         * Creates a new tile with the given coordinates
         * 
         * @param x
         * @param y
         * @param z
         */
        public Tile(long x, long y, long z) {
            this.x = x;
            this.y = y;
            this.z = z;
            envelope = envelope(x, y, z);
        }

        private ReferencedEnvelope envelope(long x, long y, long z) {
            double tileSize = MAX_TILE_WIDTH / Math.pow(2, z);
            double xMin = x * tileSize + WORLD_BOUNDS.getMinX();
            double yMin = y * tileSize + WORLD_BOUNDS.getMinY();
            return new ReferencedEnvelope(xMin, xMin + tileSize, yMin, yMin
                    + tileSize, WGS84);
        }

        /**
         * Builds the best matching tile for the specified envelope
         */
        public Tile(ReferencedEnvelope wgs84Envelope) {
            z = Math.round(Math.log(MAX_TILE_WIDTH / wgs84Envelope.getWidth())
                    / Math.log(2));
            x = Math.round(((wgs84Envelope.getMinimum(0) - WORLD_BOUNDS
                    .getMinimum(0)) / MAX_TILE_WIDTH)
                    * Math.pow(2, z));
            y = Math.round(((wgs84Envelope.getMinimum(1) - WORLD_BOUNDS
                    .getMinimum(1)) / MAX_TILE_WIDTH)
                    * Math.pow(2, z));
            envelope = envelope(x, y, z);
        }

        /**
         * Returns the parent of this tile, or null if this tile is (one of) the
         * root of the current dataset
         * 
         * @return
         */
        public Tile getParent() {
            // if we got to one of the root tiles for this data set, just stop
            if (z == 0 || envelope.contains((BoundingBox) dataEnvelope))
                return null;
            else
                return new Tile((long) Math.floor(x / 2.0), (long) Math
                        .floor(y / 2.0), z - 1);
        }

        /**
         * Returns the four direct children of this tile
         * 
         * @return
         */
        public Tile[] getChildren() {
            Tile[] result = new Tile[4];
            result[0] = new Tile(x * 2, y * 2, z + 1);
            result[1] = new Tile(x * 2 + 1, y * 2, z + 1);
            result[2] = new Tile(x * 2, y * 2 + 1, z + 1);
            result[3] = new Tile(x * 2 + 1, y * 2 + 1, z + 1);
            return result;
        }

        /**
         * Returns the WGS84 envelope of this tile
         * 
         * @return
         */
        public ReferencedEnvelope getEnvelope() {
            return envelope;
        }

        @Override
        public String toString() {
            return "Tile X: " + x + ", Y: " + y + ", Z: " + z + " (" + envelope
                    + ")";
        }
    }

}
