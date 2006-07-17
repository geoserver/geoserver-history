package org.vfny.geoserver.wfs.requests;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.filter.AbstractFilter;
import org.geotools.filter.FidFilter;
import org.geotools.filter.GeometryFilter;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.expression.LiteralExpression;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.WfsXmlRequestReader;
import org.vfny.geoserver.wfs.WfsException;
import org.vfny.geoserver.wfs.servlets.WFService;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

public abstract class WfsKvpRequestReader extends KvpRequestReader {

	/** Class logger */
    private static Logger LOGGER = Logger.getLogger(
    "org.vfny.geoserver.requests.readers");
    
   
	/**
	 * Creates a new kvp reader for a WFS request.
	 * 
	 * @param kvpPairs The raw key value pairs.
	 * @param service The servlet handling the request.
	 */
	public WfsKvpRequestReader(Map kvpPairs, WFService service) {
		super(kvpPairs,service);
	}

	/**
     * Reads in three strings, representing some sort of feature constraints,
     * and translates them into filters.  If no filters exist, it returns an
     * empty list.
     *
     * @param fid A group of feature IDs, as a String.
     * @param filter A group of filters, as a String.
     * @param bbox A group of boxes, as a String.
     *
     * @return A list filters.
     *
     */
    protected static List readFilters(String fid, String filter, String bbox) 
    	throws WfsException {
        List unparsed = new ArrayList();
        List filters = new ArrayList();
        ListIterator i;

        // handles feature id(es) case
        if ((fid != null) && (filter == null) && (bbox == null)) {
            LOGGER.finest("reading fid filter: " + fid);
            unparsed = readNested(fid);
            i = unparsed.listIterator();

            while (i.hasNext()) {
                List ids = (List) i.next();
                ListIterator innerIterator = ids.listIterator();

                while (innerIterator.hasNext()) {
                    FidFilter fidFilter = factory.createFidFilter();
                    fidFilter.addFid((String) innerIterator.next());
                    filters.add(fidFilter);
                    LOGGER.finest("added fid filter: " + fidFilter);
                }
            }

            return filters;

            // handles filter(s) case
        } else if ((filter != null) && (fid == null) && (bbox == null)) {
            LOGGER.finest("reading filter: " + filter);
            unparsed = readFlat(filter, OUTER_DELIMETER);
            i = unparsed.listIterator();

            while (i.hasNext()) {
                Reader filterReader = new StringReader((String) i.next());
                filters.add(WfsXmlRequestReader.readFilter(filterReader));
            }

            return filters;

            // handles bounding box(s) case
        } else if ((bbox != null) && (fid == null) && (filter == null)) {
            LOGGER.finest("bbox filter: " + bbox);

            double[] rawCoords = new double[4];
            unparsed = readFlat(bbox, INNER_DELIMETER);
            i = unparsed.listIterator();

            // check to make sure that the bounding box has 4 coordinates
            if (unparsed.size() != 4) {
                throw new IllegalArgumentException("Requested bounding box contains wrong"
                    + "number of coordinates (should have " + "4): "
                    + unparsed.size());

                // if it does, store them in an array of doubles
            } else {
                int j = 0;

                while (i.hasNext()) {
                    try {
                        rawCoords[j] = Double.parseDouble((String) i.next());
                        j++;
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Bounding box coordinate " + j
                            + " is not parsable:" + unparsed.get(j));
                    }
                }
            }

            // turn the array of doubles into an appropriate geometry filter
            // TODO 2:
            //  hack alert: because we do not yet know the schema, we have
            //  used the '@' symbol for the attribute expression, to be
            //  replaced later by the appropriate attribute.  I would argue
            //  that this is a failure in the specification because there
            //  should always be explicit designation of geometry attibutes
            //  within the filter.  The BBOX element is ambiguous, since
            //  features may contain multiple geometries.  For now, we will
            //  parse it and keep a record of a 'primary geometry' in the
            //  server.
            try {
                GeometryFilter finalFilter = factory.createGeometryFilter(AbstractFilter.GEOMETRY_INTERSECTS);

                //leave as null and postgisDatSource will use default geom.
                //AttributeExpression leftExpression =
                //    factory.createAttributeExpression(null);
                //leftExpression.setAttributePath("@");
                // Creates coordinates for the linear ring
                Coordinate[] coords = new Coordinate[5];
                coords[0] = new Coordinate(rawCoords[0], rawCoords[1]);
                coords[1] = new Coordinate(rawCoords[0], rawCoords[3]);
                coords[2] = new Coordinate(rawCoords[2], rawCoords[3]);
                coords[3] = new Coordinate(rawCoords[2], rawCoords[1]);
                coords[4] = new Coordinate(rawCoords[0], rawCoords[1]);

                LinearRing outerShell = new LinearRing(coords,
                        new PrecisionModel(), 0);
                Geometry polygon = new Polygon(outerShell,
                        new PrecisionModel(), 0);
                LiteralExpression rightExpression = factory
                    .createLiteralExpression(polygon);

                //finalFilter.addLeftGeometry(leftExpression);
                finalFilter.addRightGeometry(rightExpression);
                filters.add(finalFilter);

                return filters;
                
            } catch (IllegalFilterException e) {
                new WfsException("Filter creation problem: " + filter)
                	.initCause(e);
            }

            // handles unconstrained case
        } else if ((bbox == null) && (fid == null) && (filter == null)) {
            return new ArrayList();

            // handles error when more than one filter specified
        } else {
            throw new WfsException("GetFeature KVP request contained "
                + "conflicting filters.  Filter: " + filter + ", fid: " + fid
                + ", bbox:" + bbox);
        }
        
        return null;
    }
}
