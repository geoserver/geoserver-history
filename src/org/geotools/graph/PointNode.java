/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geotools.graph;

import org.geotools.feature.Feature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;



/**
 * DOCUMENT ME!
 *
 * @author Justin Deoliveira
 * @version $Revision: 1.1 $
 */
public class PointNode extends Node {
    /** DOCUMENT ME!  */
    private double m_x;

    /** DOCUMENT ME!  */
    private double m_y;

    /**
     * Creates a new PointNode object.
     *
     * @param feature DOCUMENT ME!
     * @param edgeList DOCUMENT ME!
     * @param coord DOCUMENT ME!
     */
    public PointNode(Feature feature, EdgeList edgeList, Coordinate coord) {
        super(feature, edgeList);
        m_x = coord.x;
        m_y = coord.y;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double x() {
        return (m_x);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double y() {
        return (m_y);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Coordinate asCoordinate() {
        return (new Coordinate(x(), y()));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Geometry buildGeometry() {
        return (new GeometryFactory().createPoint(new Coordinate(m_x, m_y)));
    }
}
