/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002-2006, GeoTools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geoserver.wfs.xml.v1_1_0.overrides;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.gml3.bindings.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.feature.Attribute;
import org.opengis.geometry.DirectPosition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.namespace.QName;


/**
 * Binding object for the type http://www.opengis.net/gml:PointType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *   &lt;complexType name=&quot;PointType&quot;&gt;
 *       &lt;annotation&gt;
 *           &lt;documentation&gt;A Point is defined by a single coordinate tuple.&lt;/documentation&gt;
 *       &lt;/annotation&gt;
 *       &lt;complexContent&gt;
 *           &lt;extension base=&quot;gml:AbstractGeometricPrimitiveType&quot;&gt;
 *               &lt;sequence&gt;
 *                   &lt;choice&gt;
 *                       &lt;annotation&gt;
 *                           &lt;documentation&gt;GML supports two different ways to specify the direct poisiton of a point. 1. The &quot;pos&quot; element is of type
 *                                                           DirectPositionType.&lt;/documentation&gt;
 *                       &lt;/annotation&gt;
 *                       &lt;element ref=&quot;gml:pos&quot;/&gt;
 *                       &lt;element ref=&quot;gml:coordinates&quot;&gt;
 *                           &lt;annotation&gt;
 *                               &lt;documentation&gt;Deprecated with GML version 3.1.0 for coordinates with ordinate values that are numbers. Use &quot;pos&quot;
 *                                                                   instead. The &quot;coordinates&quot; element shall only be used for coordinates with ordinates that require a string
 *                                                                   representation, e.g. DMS representations.&lt;/documentation&gt;
 *                           &lt;/annotation&gt;
 *                       &lt;/element&gt;
 *                       &lt;element ref=&quot;gml:coord&quot;&gt;
 *                           &lt;annotation&gt;
 *                               &lt;documentation&gt;Deprecated with GML version 3.0. Use &quot;pos&quot; instead. The &quot;coord&quot; element is included for
 *                                                                   backwards compatibility with GML 2.&lt;/documentation&gt;
 *                           &lt;/annotation&gt;
 *                       &lt;/element&gt;
 *                   &lt;/choice&gt;
 *               &lt;/sequence&gt;
 *           &lt;/extension&gt;
 *       &lt;/complexContent&gt;
 *   &lt;/complexType&gt;
 * </code>
 *         </pre>
 *
 * </p>
 *
 * @generated
 */
public class ISOPointTypeBinding extends AbstractComplexBinding {
    GeometryFactory gFactory;

    public ISOPointTypeBinding(GeometryFactory gFactory) {
        this.gFactory = gFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.PointType;
    }

    public int getExecutionMode() {
        return BEFORE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Point.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        if (node.hasChild(DirectPosition.class)) {
            DirectPosition dp = (DirectPosition) node.getChildValue(DirectPosition.class);

            return gFactory.createPoint(new Coordinate(dp.getOrdinate(0), dp.getOrdinate(1)));
        }

        if (node.hasChild(Coordinate.class)) {
            return gFactory.createPoint((Coordinate) node.getChildValue(Coordinate.class));
        }

        if (node.hasChild(CoordinateSequence.class)) {
            return gFactory.createPoint((CoordinateSequence) node.getChildValue(
                    CoordinateSequence.class));
        }

        return null;
    }

    public Object getProperty(Object object, QName name) {
        if (GML.pos.equals(name)) {
            Point point;

            if (object instanceof Attribute) {
                Attribute att = (Attribute) object;
                point = (Point) att.get();
            } else {
                point = (Point) object;
            }

            if (point != null) {
                DirectPosition2D dp = new DirectPosition2D();
                dp.setOrdinate(0, point.getX());
                dp.setOrdinate(1, point.getY());

                return dp;
            }
        }

        return null;
    }

    public Element encode(Object object, Document document, Element value)
        throws Exception {
        return value;
    }
}
