package org.vfny.geoserver.wms.responses.map.kml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import java.util.logging.Level;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapLayer;
import org.geotools.referencing.CRS;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.filter.spatial.BBOX;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class DataRegionatingStrategy extends CachedHierarchyRegionatingStrategy {

    AttributeStrategy strategy;

    @Override
    protected String getDatabaseName(WMSMapContext con, MapLayer layer)
            throws Exception {
        FeatureSource fs = layer.getFeatureSource();
        SimpleFeatureType type = (SimpleFeatureType) fs.getSchema();

        // find out which attribute we're going to use
        String attribute = (String) con.getRequest().getFormatOptions().get(
                "regionateAttr");
        if (attribute == null) {
            attribute = typeInfo.getRegionateAttribute();
        }
        if (attribute == null) {
            LOGGER
                    .log(Level.INFO,
                            "No attribute specified, falling back on geometry attribute");
            attribute = type.getGeometryDescriptor().getLocalName();
        }

        // Make sure the attribute is actually there
        AttributeType attributeType = type.getType(attribute);
        if (attributeType == null) {
            throw new WmsException("Could not find regionating attribute "
                    + attribute + " in layer " + typeInfo.getName());
        }

        // Find the best matching strategy given the type of the attribute
        Class binding = attributeType.getBinding();
        if (Geometry.class.isAssignableFrom(binding)
                && !Point.class.isAssignableFrom(binding))
            strategy = new GeometrySizeStrategy(attribute, fs);
        else if (Comparable.class.isAssignableFrom(binding))
            strategy = new ComparableStrategy(attribute, fs);
        else {
            LOGGER.log(Level.SEVERE, "Could not find a suitable strategy "
                    + "for the specified attribute, "
                    + "falling back on random one");
            strategy = new RandomStrategy(fs);
        }

        // make sure a special db for this layer and attribute will be created
        return super.getDatabaseName(con, layer) + "_" + attribute;

    }

    @Override
    protected FeatureIterator getSortedFeatures(ReferencedEnvelope envelope,
            Connection cacheConn) throws Exception {
        return strategy.getSortedFeatures(envelope, cacheConn);
    }

    /**
     * 
     * The delegate that does the actual work of grabbing out the geometries
     * TODO: avoid this hierarchy, create a set of explicit subclasses of
     * {@link CachedHierarchyRegionatingStrategy} instead
     */
    public interface AttributeStrategy {
        FeatureIterator getSortedFeatures(ReferencedEnvelope envelope,
                Connection cacheConn) throws Exception;
    }

    /**
     * Strategy that leverages the underlying datastore ability to make sorts.
     * Given that sorting is not guaranteed and that it may not be fast (e.g.,
     * lack of an index) it may be a good idea to use the same approach as
     * geometry, that is, store the index. In the case where the datastore has
     * sort (but eventually not fast) we could do this in one shot, in the case
     * where sorting is not there, we could create a column and copy the sorting
     * attribute, then make H2 do the sorting for us (that might be slow..)
     * 
     * @author Andrea Aime
     * 
     */
    public static class ComparableStrategy implements AttributeStrategy {

        private String attribute;

        private FeatureSource fs;

        public ComparableStrategy(String attribute, FeatureSource fs) {
            this.attribute = attribute;
            this.fs = fs;
        }

        public FeatureIterator getSortedFeatures(ReferencedEnvelope env,
                Connection cacheConn) throws Exception {
            // build the bbox filter
            GeometryDescriptor geom = fs.getSchema().getGeometryDescriptor();
            CoordinateReferenceSystem nativeCrs = geom
                    .getCoordinateReferenceSystem();
            FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
            if (!CRS.equalsIgnoreMetadata(WGS84, nativeCrs))
                env = env.transform(nativeCrs, true);
            BBOX filter = ff.bbox(geom.getLocalName(), env.getMinX(), env
                    .getMinY(), env.getMaxX(), env.getMaxY(), null);

            // build an optimized query (only the necessary attributes
            DefaultQuery q = new DefaultQuery();
            q.setFilter(filter);
            q.setPropertyNames(new String[] { geom.getLocalName(), attribute });
            // TODO: enable this when JTS learns how to compute centroids
            // without triggering the
            // generation of Coordinate[] out of the sequences...
            // q.setHints(new Hints(Hints.JTS_COORDINATE_SEQUENCE_FACTORY,
            // PackedCoordinateSequenceFactory.class));
            q.setSortBy(new SortBy[] { ff.sort(attribute,
                            SortOrder.DESCENDING) });

            // return the reader
            return fs.getFeatures(q).features();
        }
    }

    /**
     * Uses the geometry size as the sorting criteria for features
     * 
     * @author Andrea Aime
     */
    public static class GeometrySizeStrategy implements AttributeStrategy {

        static final SimpleFeatureType IDX_FEATURE_TYPE;

        static {
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            tb.crs(WGS84);
            tb.add("point", Point.class);
            tb.setName("FeatureCentroids");
            IDX_FEATURE_TYPE = tb.buildFeatureType();
        }

        private String attribute;

        private FeatureSource fs;

        public GeometrySizeStrategy(String attribute, FeatureSource fs) {
            this.attribute = attribute;
            this.fs = fs;
        }

        public FeatureIterator getSortedFeatures(ReferencedEnvelope envelope,
                Connection cacheConn) throws Exception {
            // first of all, let's check if the geometry index table is there
            Statement st = null;
            try {
                st = cacheConn.createStatement();
                try {
                    st.executeQuery("SELECT * FROM GEOMIDX LIMIT 1");
                } catch (SQLException e) {
                    buildIndex(cacheConn);
                }
            } finally {
                JDBCUtils.close(st);
            }

            return new IndexFeatureIterator(cacheConn, envelope);
        }

        private void buildIndex(Connection conn) throws Exception {
            Statement st = null;
            PreparedStatement ps = null;
            FeatureIterator fi = null;
            try {
                st = conn.createStatement();
                st.execute("CREATE TABLE GEOMIDX(" //
                        + "X NUMBER, " //
                        + "Y NUMBER, " //
                        + "FID VARCHAR(64), " //
                        + "SIZE NUMBER)");
                st.execute("CREATE INDEX GEOMIDX_COORDS ON GEOMIDX(X, Y)");
                st.execute("CREATE INDEX GEOMIDX_SIZE ON GEOMIDX(SIZE)");

                // prepare this statement so that the sql parser has to deal
                // with it just once
                ps = conn.prepareStatement("INSERT INTO "
                        + "GEOMIDX(X, Y, FID, SIZE) VALUES (?, ?, ?, ?)");

                // build an optimized query, loading only the necessary
                // attributes
                GeometryDescriptor geom = fs.getSchema()
                        .getGeometryDescriptor();
                CoordinateReferenceSystem nativeCrs = geom
                        .getCoordinateReferenceSystem();
                DefaultQuery q = new DefaultQuery();
                q.setPropertyNames(new String[] { geom.getLocalName() });

                // setup the eventual transform
                MathTransform tx = null;
                double[] coords = new double[2];
                if (!CRS.equalsIgnoreMetadata(nativeCrs, WGS84))
                    tx = CRS.findMathTransform(nativeCrs, WGS84);

                // read all the features and fill the index table
                // make it so the insertion is a single big transaction, should
                // be faster,
                // provided it does not kill H2...
                conn.setAutoCommit(false);
                fi = fs.getFeatures().features();
                while (fi.hasNext()) {
                    // grab the centroid and transform it in 4326 if necessary
                    SimpleFeature f = (SimpleFeature) fi.next();
                    Geometry g = (Geometry) f.getDefaultGeometry();
                    Point centroid = g.getCentroid();
                    coords[0] = centroid.getX();
                    coords[1] = centroid.getY();
                    if (tx != null)
                        tx.transform(coords, 0, coords, 0, 1);

                    // grab the size
                    double size;
                    if (g instanceof MultiPoint)
                        size = ((MultiPoint) g).getNumGeometries();
                    if (g instanceof Polygon || g instanceof MultiPolygon)
                        size = g.getArea();
                    else
                        size = g.getLength();

                    // insert
                    ps.setDouble(1, coords[0]);
                    ps.setDouble(2, coords[1]);
                    ps.setString(3, f.getID());
                    ps.setDouble(4, size);
                    ps.execute();
                }
                // todo: commit every 1000 features or so. No transaction is
                // slower, but too big transaction imposes a big overhead on the db
                conn.commit();
                
                // hum, shall we kick H2 so that it updates the statistics?
            } finally {
                conn.setAutoCommit(true);
                JDBCUtils.close(st);
                JDBCUtils.close(ps);
                if (fi != null)
                    fi.close();
            }
        }

        public static class IndexFeatureIterator implements FeatureIterator {
            SimpleFeatureBuilder builder;

            GeometryFactory gf;

            Statement st;

            ResultSet rs;

            boolean nextCalled;

            boolean next;

            public IndexFeatureIterator(Connection cacheConn,
                    ReferencedEnvelope envelope) throws Exception {
                // grab all of the geometries sitting inside the envelope

                try {
                    st = cacheConn.createStatement();
                    String sql = "SELECT X, Y, FID \n"
                            + "FROM GEOMIDX\n" // 
                            + "WHERE X >= " + envelope.getMinX() + "\n"
                            + "AND X <= " + envelope.getMaxX() + "\n"
                            + "AND Y >= " + envelope.getMinY() + "\n"
                            + "AND Y <= " + envelope.getMaxY() + "\n"
                            + "ORDER BY SIZE DESC";
                    rs = st.executeQuery(sql);
                    // make sure everything is properly closed in case of
                    // exception
                } catch (SQLException e) {
                    close();
                }

                // prepare the builders we'll use to create all of the features
                builder = new SimpleFeatureBuilder(IDX_FEATURE_TYPE);
                gf = new GeometryFactory();
            }

            public void close() {
                JDBCUtils.close(rs);
                JDBCUtils.close(st);
            }

            public boolean hasNext() {
                // the contract of the iterator does not say this will be
                // called just once, we have to guard against multiple calls
                if (!nextCalled)
                    try {
                        next = rs.next();
                        nextCalled = true;
                    } catch (SQLException e) {
                        close();
                        throw new RuntimeException(
                                "Error while accessing next db record", e);
                    }

                return next;
            }

            public Feature next() throws NoSuchElementException {
                if (!nextCalled)
                    hasNext();
                nextCalled = false;
                try {
                    double x = rs.getDouble(1);
                    double y = rs.getDouble(2);
                    builder.add(gf.createPoint(new Coordinate(x, y)));
                    return builder.buildFeature(rs.getString(3));
                } catch (SQLException e) {
                    close();
                    throw new RuntimeException(
                            "Problems reading the geometry index");
                }
            }

        }
    }

    /**
     * Just picks random features. Less trivial than it seems, since we have to
     * ensure to read at least n feaures out of the db and it would be nice if
     * they were really randomly distributed (randomly covering current tile's
     * area as opposed of being all crammed inside a corner). Probably needs a
     * full extraction of the data and building H2 index as well, possibly doing
     * a single full scan of the data (that makes it even less trivial...)
     */
    public static class RandomStrategy implements AttributeStrategy {
        private String attribute;

        private FeatureSource fs;

        public RandomStrategy(FeatureSource fs) {
            this.attribute = attribute;
            this.fs = fs;
        }

        public FeatureIterator getSortedFeatures(ReferencedEnvelope envelope,
                Connection cacheConn) throws Exception {
            throw new UnsupportedOperationException(
                    "Still have to code up this one sorry!!");
        }
    }
}