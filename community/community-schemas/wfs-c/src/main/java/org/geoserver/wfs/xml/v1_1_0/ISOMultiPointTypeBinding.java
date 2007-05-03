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
package org.geoserver.wfs.xml.v1_1_0;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import org.geotools.gml3.bindings.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.feature.Attribute;
import java.util.ArrayList;
import javax.xml.namespace.QName;


/**
 * Binding object for the type http://www.opengis.net/gml:MultiPointType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType name=&quot;MultiPointType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;A MultiPoint is defined by one or more Points, referenced through pointMember elements.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base=&quot;gml:AbstractGeometricAggregateType&quot;&gt;
 *              &lt;sequence&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;The members of the geometric aggregate can be specified either using the &quot;standard&quot; property or the array property style. It is also valid to use both the &quot;standard&quot; and the array property style in the same collection.
 *  NOTE: Array properties cannot reference remote geometry elements.&lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *                  &lt;element maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot; ref=&quot;gml:pointMember&quot;/&gt;
 *                  &lt;element minOccurs=&quot;0&quot; ref=&quot;gml:pointMembers&quot;/&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 * </code>
 *         </pre>
 *
 * </p>
 *
 * @generated
 */
public class ISOMultiPointTypeBinding extends AbstractComplexBinding {
    GeometryFactory gFactory;

    public ISOMultiPointTypeBinding(GeometryFactory gFactory) {
        this.gFactory = gFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.MultiPointType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return MultiPoint.class;
    }

    public int getExecutionMode() {
        return BEFORE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        ArrayList points = new ArrayList();

        if (node.hasChild(Point.class)) {
            points.addAll(node.getChildValues(Point.class));
        }

        if (node.hasChild(Point[].class)) {
            Point[] p = (Point[]) node.getChildValue(Point[].class);

            for (int i = 0; i < p.length; i++)
                points.add(p[i]);
        }

        return gFactory.createMultiPoint((Point[]) points.toArray(new Point[points.size()]));
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if (GML.pointMember.equals(name)) {
            Attribute att = (Attribute) object;
            MultiPoint multiPoint = (MultiPoint) att.get();

            if (multiPoint != null) {
                Point[] members = new Point[multiPoint.getNumGeometries()];

                for (int i = 0; i < members.length; i++) {
                    members[i] = (Point) multiPoint.getGeometryN(i);
                }

                return members;
            }
        }

        return null;
    }
}
