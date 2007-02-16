/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wfs.requests;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.geotools.feature.GeometryAttributeType;
import org.geotools.filter.AbstractFilter;
import org.geotools.filter.BBoxExpression;
import org.geotools.filter.FidFilter;
import org.geotools.filter.GeometryFilter;
import org.geotools.filter.LiteralExpression;
import org.geotools.geometry.jts.JTS;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.wfs.WfsException;
import org.vfny.geoserver.wfs.servlets.WFService;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Logger;


public abstract class WfsKvpRequestReader extends KvpRequestReader {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger("org.vfny.geoserver.requests.readers");

    /**
         * Creates a new kvp reader for a WFS request.
         *
         * @param kvpPairs The raw key value pairs.
         * @param service The servlet handling the request.
         */
    public WfsKvpRequestReader(Map kvpPairs, WFService service) {
        super(kvpPairs, service);
    }

    /**
     * Reads in three strings, representing some sort of feature
     * constraints, and translates them into filters.  If no filters exist, it
     * returns an empty list.
     *
     * @param typeNames DOCUMENT ME!
     * @param fid A group of feature IDs, as a String.
     * @param ogcFilter A group of filters, as a String.
     * @param cqlFilter DOCUMENT ME!
     * @param bbox A group of boxes, as a String.
     *
     * @return A list filters.
     *
     * @throws WfsException DOCUMENT ME!
     */
    protected List readFilters(List typeNames, String fid, String ogcFilter, String cqlFilter,
        String bbox) throws WfsException {
        // handles feature id(es) case
        if ((fid != null) && (ogcFilter == null) && ((bbox == null) & (cqlFilter == null))) {
            return readFidFilters(fid);

            // handles filter(s) case
        } else if ((ogcFilter != null) && (fid == null) && ((bbox == null) & (cqlFilter == null))) {
            return readOGCFilter(ogcFilter);

            // handles cql filter(s) case
        } else if ((cqlFilter != null) && (fid == null) && ((ogcFilter == null) & (bbox == null))) {
            return readCQLFilter(cqlFilter);

            // handles bounding box(s) case
        } else if ((bbox != null) && (fid == null) && ((ogcFilter == null) & (cqlFilter == null))) {
            return parseBBoxFilter(typeNames, ogcFilter, bbox);

            // handles unconstrained case
        } else if ((bbox == null) && (fid == null) && ((ogcFilter == null) & (cqlFilter == null))) {
            return new ArrayList();

            // handles error when more than one filter specified
        } else {
            throw new WfsException("GetFeature KVP request contained "
                + "conflicting filters.  Filter: " + ogcFilter + ", fid: " + fid + ", bbox:" + bbox);
        }
    }

    protected List parseBBoxFilter(List typeNames, String filter, String bbox)
        throws WfsException {
        List filters = new ArrayList();
        Envelope envelope;
        LOGGER.finest("bbox filter: " + bbox);

        try {
            envelope = parseBbox(bbox);

            BBoxExpression bboxExpression = factory.createBBoxExpression(envelope);

            // create a filter for each feature type, querying against the default geometry
            for (Iterator it = typeNames.iterator(); it.hasNext();) {
                String typeName = (String) it.next();
                GeometryFilter finalFilter = factory.createGeometryFilter(AbstractFilter.GEOMETRY_INTERSECTS);

                if (service.getCatalog() != null) {
                    FeatureTypeInfo info = service.getCatalog().getFeatureTypeInfo(typeName);
                    GeometryAttributeType geomAtt = info.getFeatureType().getDefaultGeometry();
                    finalFilter.addLeftGeometry(factory.createAttributeExpression(geomAtt.getName()));
                }

                finalFilter.addRightGeometry(bboxExpression);
                filters.add(finalFilter);
            }

            return filters;
        } catch (ServiceException e) {
            throw new WfsException(e);
        } catch (Exception e) {
            throw new WfsException("Filter creation problem: " + filter, e);
        }
    }

    protected List readOGCFilter(String filter) throws WfsException {
        // remap exception to the proper type for this service
        try {
            return super.readOGCFilter(filter);
        } catch (ServiceException e) {
            throw new WfsException(e);
        }
    }

    protected List readCQLFilter(String filter) throws WfsException {
        // remap exception to the proper type for this service
        try {
            return super.readCQLFilter(filter);
        } catch (ServiceException e) {
            throw new WfsException(e);
        }
    }
}
