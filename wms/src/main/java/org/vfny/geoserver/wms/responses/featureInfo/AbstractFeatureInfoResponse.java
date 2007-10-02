/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.featureInfo;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.IllegalFilterException;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetFeatureInfoRequest;
import org.vfny.geoserver.wms.requests.GetMapRequest;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;


/**
 * Abstract class to do the common work of the FeatureInfoResponse subclasses.
 * Subclasses should just need to implement writeTo(), to write the actual
 * response, the executions are handled here, figuring out where on the map
 * the pixel is located.
 *
 * <p>
 * Would be nice to have some greater control over the pixels that are
 * selected. Ideally we would be able to detect things like the size of the
 * mark, so that users need not click on the exact center, or the exact pixel.
 * This is not a big deal for polygons, but is for lines and points.  One
 * half solution to make things a bit nicer would be a global parameter to set
 * a wider pixel range.
 * </p>
 *
 * @author James Macgill, PSU
 * @author Gabriel Roldan, Axios
 * @author Chris Holmes, TOPP
 * @author Brent Owens, TOPP
 */
public abstract class AbstractFeatureInfoResponse extends GetFeatureInfoDelegate {
    /** A logger for this class. */
    protected static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses.wms.featureinfo");

    /** The formats supported by this map delegate. */
    protected List supportedFormats = null;
    protected List results;
    protected List metas;

    /**
     * setted in execute() from the requested output format, it's holded just
     * to be sure that method has been called before getContentType() thus
     * supporting the workflow contract of the request processing
     */
    protected String format = null;

    /**
     * Creates a new GetMapDelegate object.
     */

    /**
     * Autogenerated proxy constructor.
     */
    public AbstractFeatureInfoResponse() {
        super();
    }

    /**
     * Returns the content encoding for the output data.
     *
     * <p>
     * Note that this reffers to an encoding applied to the response stream
     * (such as GZIP or DEFLATE), and not to the MIME response type, wich is
     * returned by <code>getContentType()</code>
     * </p>
     *
     * @return <code>null</code> since no special encoding is performed while
     *         wrtting to the output stream.
     */
    public String getContentEncoding() {
        return null;
    }

    /**
     * Writes the image to the client.
     *
     * @param out The output stream to write to.
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public abstract void writeTo(OutputStream out) throws ServiceException, IOException;

    /**
     * The formats this delegate supports.
     *
     * @return The list of the supported formats
     */
    public List getSupportedFormats() {
        return supportedFormats;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gs app context
     *
     * @task TODO: implement
     */
    public void abort(GeoServer gs) {
    }

    /**
     * Gets the content type.  This is set by the request, should only be
     * called after execute.  GetMapResponse should handle this though.
     *
     * @param gs server configuration
     *
     * @return The mime type that this response will generate.
     *
     * @throws IllegalStateException if<code>execute()</code> has not been
     *         previously called
     */
    public String getContentType(GeoServer gs) {
        if (format == null) {
            throw new IllegalStateException(
                "Content type unknown since execute() has not been called yet");
        }

        // chain geoserver charset so that multibyte feature info responses
        // gets properly encoded, same as getCapabilities responses 
        return format + ";charset=" + gs.getCharSet().name();
    }

    /**
     * Performs the execute request using geotools rendering.
     *
     * @param requestedLayers The information on the types requested.
     * @param queries The results of the queries to generate maps with.
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @throws WmsException For any problems.
     */
    protected void execute(FeatureTypeInfo[] requestedLayers, Filter[] filters, int x, int y)
        throws WmsException {
        GetFeatureInfoRequest request = getRequest();
        this.format = request.getInfoFormat();

        GetMapRequest getMapReq = request.getGetMapRequest();
        CoordinateReferenceSystem requestedCRS = getMapReq.getCrs(); // optional, may be null

        int width = getMapReq.getWidth();
        int height = getMapReq.getHeight();
        Envelope bbox = getMapReq.getBbox();

        Coordinate upperLeft = pixelToWorld(x - 2, y - 2, bbox, width, height);
        Coordinate lowerRight = pixelToWorld(x + 2, y + 2, bbox, width, height);

        Coordinate[] coords = new Coordinate[5];
        coords[0] = upperLeft;
        coords[1] = new Coordinate(lowerRight.x, upperLeft.y);
        coords[2] = lowerRight;
        coords[3] = new Coordinate(upperLeft.x, lowerRight.y);
        coords[4] = coords[0];

        //DJB: 
        //TODO: this should probably put a max features restriction on the query
        //      unforunately, this queryies by filter not by query, so this is a slightly more difficult problem
        //      Its not a big deal not to do this because the writer will ensure that the correct # of items
        //      are actually printed out.
        //DJB: DONE - see "q", below
        // NOTE: you can ask for results from multiple layers at once.  So, if max features is 2 (after you add max features query to above) and you
        //       query 10 layers, you could still get 20 features being sent on.
        //       Thats why max features is handled at the query portion!
        GeometryFactory geomFac = new GeometryFactory();

        LinearRing boundary = geomFac.createLinearRing(coords); // this needs to be done with each FT so it can be reprojected

        Polygon pixelRect = geomFac.createPolygon(boundary, null);

        FilterFactory2 filterFac = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());

        Filter getFInfoFilter = null;

        int layerCount = requestedLayers.length;
        results = new ArrayList(layerCount);
        metas = new ArrayList(layerCount);

        try {
            for (int i = 0; i < layerCount; i++) {
                FeatureTypeInfo finfo = requestedLayers[i];

                CoordinateReferenceSystem dataCRS = finfo.getFeatureType().getDefaultGeometry()
                                                         .getCoordinateSystem();

                // reproject the bounding box
                if ((requestedCRS != null) && !CRS.equalsIgnoreMetadata(dataCRS, requestedCRS)) {
                    try {
                        MathTransform transform = CRS.findMathTransform(requestedCRS, dataCRS, true);
                        pixelRect = (Polygon) JTS.transform(pixelRect, transform); // reprojected
                    } catch (MismatchedDimensionException e) {
                        LOGGER.severe(e.getLocalizedMessage());
                    } catch (TransformException e) {
                        LOGGER.severe(e.getLocalizedMessage());
                    } catch (FactoryException e) {
                        LOGGER.severe(e.getLocalizedMessage());
                    }
                }

                try {
                    getFInfoFilter = filterFac.intersects(filterFac.property(finfo.getFeatureType().getDefaultGeometry()
                            .getLocalName()), filterFac.literal(pixelRect));
//                    getFInfoFilter = filterFac.createGeometryFilter(AbstractFilter.GEOMETRY_INTERSECTS);
//                    ((GeometryFilter) getFInfoFilter).addLeftGeometry(filterFac
//                        .createLiteralExpression(pixelRect));
//
//                    if (finfo.getFeatureType().getDefaultGeometry() != null) {
//                        ((GeometryFilter) getFInfoFilter).addRightGeometry(filterFac
//                            .createAttributeExpression());
//                    } else {
//                        LOGGER.warning("GetFeatureInfo for feature type with no default geometry.");
//                    }
//                    ((GeometryFilter) getFInfoFilter).addLeftGeometry(filterFac
//                        .createLiteralExpression(pixelRect));
                } catch (IllegalFilterException e) {
                    e.printStackTrace();
                    throw new WmsException(null, "Internal error : " + e.getMessage());
                }

                // include the eventual layer definition filter
                if (filters[i] != null) {
                    getFInfoFilter = filterFac.and(getFInfoFilter, filters[i]);
                }

                Query q = new DefaultQuery(finfo.getTypeName(), null, getFInfoFilter,
                        request.getFeatureCount(), Query.ALL_NAMES, null);
                FeatureCollection match = finfo.getFeatureSource().getFeatures(q);

                //this was crashing Gml2FeatureResponseDelegate due to not setting
                //the featureresults, thus not being able of querying the SRS
                //if (match.getCount() > 0) {
                results.add(match);
                metas.add(finfo);

                //}
            }
        } catch (IOException ioe) {
            throw new WmsException(null, "Internal error : " + ioe.getMessage());
        }
    }

    /**
     * Converts a coordinate expressed on the device space back to real world
     * coordinates.  Stolen from LiteRenderer but without the need of a
     * Graphics object
     *
     * @param x horizontal coordinate on device space
     * @param y vertical coordinate on device space
     * @param map The map extent
     * @param width image width
     * @param height image height
     *
     * @return The correspondent real world coordinate
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    private Coordinate pixelToWorld(int x, int y, Envelope map, int width, int height) {
        //set up the affine transform and calculate scale values
        AffineTransform at = worldToScreenTransform(map, width, height);

        Point2D result = null;

        try {
            result = at.inverseTransform(new java.awt.geom.Point2D.Double(x, y),
                    new java.awt.geom.Point2D.Double());
        } catch (NoninvertibleTransformException e) {
            throw new RuntimeException(e);
        }

        Coordinate c = new Coordinate(result.getX(), result.getY());

        return c;
    }

    /**
     * Sets up the affine transform.  Stolen from liteRenderer code.
     *
     * @param mapExtent the map extent
     * @param width the screen size
     * @param height DOCUMENT ME!
     *
     * @return a transform that maps from real world coordinates to the screen
     */
    private AffineTransform worldToScreenTransform(Envelope mapExtent, int width, int height) {
        double scaleX = (double) width / mapExtent.getWidth();
        double scaleY = (double) height / mapExtent.getHeight();

        double tx = -mapExtent.getMinX() * scaleX;
        double ty = (mapExtent.getMinY() * scaleY) + height;

        AffineTransform at = new AffineTransform(scaleX, 0.0d, 0.0d, -scaleY, tx, ty);

        return at;
    }
}
