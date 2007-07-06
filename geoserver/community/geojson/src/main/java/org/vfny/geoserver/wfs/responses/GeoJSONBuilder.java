/*
 * Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wfs.responses;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import net.sf.json.JSONException;
import net.sf.json.util.JSONBuilder;
import java.io.Writer;


/**
 * This class extends the JSONBuilder to be able to write out geometric types, as per geojson.org.
 * The current version is out of date, needs to be updated to the latest version of the spec,
 * but for now am leaving as is since we just transferred over from another json lib.
 * <p>
 *
 * @author Chris Holmes, The Open Planning Projec
 * @version $Id$
 *
 */
public class GeoJSONBuilder extends JSONBuilder {
    public GeoJSONBuilder(Writer w) {
        super(w);
    }

    /**
     * Writes any geometry object.  This class figures out which geometry representation to write
     * and calls subclasses to actually write the object.
     * @param geometry The geoemtry be encoded
     * @return The JSONBuilder with the new geoemtry
     * @throws JSONException If anything goes wrong
     */
    public JSONBuilder writeGeom(Geometry geometry) throws JSONException {
        this.object();
        this.key("type");
        this.value(getGeometryName(geometry));

        int geometryType = getGeometryType(geometry);

        switch (geometryType) {
        case POINT:
        case LINESTRING:
            writeCoordinates(geometry.getCoordinates());

            break;

        case POLYGON:
            writePolygon((Polygon) geometry);

            break;

        case MULTIPOINT:
        case MULTILINESTRING:
        case MULTIPOLYGON:
        case MULTIGEOMETRY:
            writeMulti((GeometryCollection) geometry, getGeometryName(geometry));

            break;
        }

        return this.endObject();
    }

    /**
     * Write the coordinates of a geometry
     * @param coords The coordinates to write
     * @return this
     * @throws JSONException
     */
    public JSONBuilder writeCoordinates(Coordinate[] coords)
        throws JSONException {
        this.key("coordinates");
        this.array();

        for (int i = 0; i < coords.length; i++) {
            Coordinate coord = coords[i];
            this.value(coord.x);
            this.value(coord.y);
        }

        return this.endArray();
    }

    /**
     * Writes a polygon
     * @param geometry The polygon to write
     * @throws JSONException
     */
    private void writePolygon(Polygon geometry) throws JSONException {
        String lineRing = "LinearRing";

        this.key("exterior");
        this.object();
        this.key("type");
        this.value(lineRing);
        writeCoordinates(geometry.getCoordinates());

        this.endObject(); //end the linear ring

        for (int i = 0, ii = geometry.getNumInteriorRing(); i < ii; i++) {
            this.key("interior");
            this.object();
            this.key("type");
            this.value(lineRing);
            writeCoordinates(geometry.getCoordinates());
            this.endObject(); //end the linear ring
        }

        //this.endObject(); //end the 
    }

    private void writeMulti(GeometryCollection geometry, String member)
        throws JSONException {
        this.key("members");
        this.array();

        for (int i = 0, n = geometry.getNumGeometries(); i < n; i++) {
            //start(member);
            writeGeom(geometry.getGeometryN(i));

            //end(member);
        }

        this.endArray();
    }

    /** Internal representation of OGC SF Point */
    protected static final int POINT = 1;

    /** Internal representation of OGC SF LineString */
    protected static final int LINESTRING = 2;

    /** Internal representation of OGC SF Polygon */
    protected static final int POLYGON = 3;

    /** Internal representation of OGC SF MultiPoint */
    protected static final int MULTIPOINT = 4;

    /** Internal representation of OGC SF MultiLineString */
    protected static final int MULTILINESTRING = 5;

    /** Internal representation of OGC SF MultiPolygon */
    protected static final int MULTIPOLYGON = 6;

    /** Internal representation of OGC SF MultiGeometry */
    protected static final int MULTIGEOMETRY = 7;

    public static String getGeometryName(Geometry geometry) {
        Class geomClass = geometry.getClass();
        String returnValue = null;

        if (geomClass.equals(Point.class)) {
            returnValue = "Point";
        } else if (geomClass.equals(LineString.class)) {
            returnValue = "LineString";
        } else if (geomClass.equals(Polygon.class)) {
            returnValue = "Polygon";
        } else if (geomClass.equals(MultiPoint.class)) {
            returnValue = "MultiPoint";
        } else if (geomClass.equals(MultiLineString.class)) {
            returnValue = "MultiLineString";
        } else if (geomClass.equals(MultiPolygon.class)) {
            returnValue = "MultiPolygon";
        } else if (geomClass.equals(GeometryCollection.class)) {
            returnValue = "GeometryCollection";
        } else {
            //HACK!!! throw exception
            returnValue = null;
        }

        return returnValue;
    }

    /**
     * Gets the internal representation for the given Geometry
     *
     * @param geometry a Geometry
     *
     * @return int representation of Geometry
     */
    public static int getGeometryType(Geometry geometry) {
        //LOGGER.entering("GMLUtils", "getGeometryType", geometry);
        Class geomClass = geometry.getClass();
        int returnValue = -1;

        if (geomClass.equals(Point.class)) {
            //LOGGER.finest("found point");
            returnValue = POINT;
        } else if (geomClass.equals(LineString.class)) {
            //LOGGER.finest("found linestring");
            returnValue = LINESTRING;
        } else if (geomClass.equals(Polygon.class)) {
            //LOGGER.finest("found polygon");
            returnValue = POLYGON;
        } else if (geomClass.equals(MultiPoint.class)) {
            //LOGGER.finest("found multiPoint");
            returnValue = MULTIPOINT;
        } else if (geomClass.equals(MultiLineString.class)) {
            returnValue = MULTILINESTRING;
        } else if (geomClass.equals(MultiPolygon.class)) {
            returnValue = MULTIPOLYGON;
        } else if (geomClass.equals(GeometryCollection.class)) {
            returnValue = MULTIGEOMETRY;
        } else {
            returnValue = -1;

            //HACK!!! throw exception.
        }

        return returnValue;
    }
}
