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

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.gml3.bindings.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.feature.Attribute;
import java.util.List;
import javax.xml.namespace.QName;


/**
 * Binding object for the type http://www.opengis.net/gml:MultiSurfaceType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType name=&quot;MultiSurfaceType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;A MultiSurface is defined by one or more Surfaces, referenced through surfaceMember elements.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base=&quot;gml:AbstractGeometricAggregateType&quot;&gt;
 *              &lt;sequence&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;The members of the geometric aggregate can be specified either using the &quot;standard&quot; property or the array property style. It is also valid to use both the &quot;standard&quot; and the array property style in the same collection.
 *  NOTE: Array properties cannot reference remote geometry elements.&lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *                  &lt;element maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot; ref=&quot;gml:surfaceMember&quot;/&gt;
 *                  &lt;element minOccurs=&quot;0&quot; ref=&quot;gml:surfaceMembers&quot;/&gt;
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
public class ISOMultiSurfaceTypeBinding extends AbstractComplexBinding {
    GeometryFactory gf;

    public ISOMultiSurfaceTypeBinding(GeometryFactory gf) {
        this.gf = gf;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.MultiSurfaceType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return MultiPolygon.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        // &lt;element maxOccurs="unbounded" minOccurs="0"
        // ref="gml:surfaceMember"/&gt;
        List surfaces = node.getChildValues(Polygon.class);

        return gf.createMultiPolygon((Polygon[]) surfaces.toArray(new Polygon[surfaces.size()]));
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if (GML.surfaceMember.equals(name)) {
            Attribute att = (Attribute) object;
            MultiPolygon multiSurface = (MultiPolygon) att.get();

            if (multiSurface != null) {
                Polygon[] members = new Polygon[multiSurface.getNumGeometries()];

                for (int i = 0; i < members.length; i++) {
                    members[i] = (Polygon) multiSurface.getGeometryN(i);
                }

                return members;
            }
        }

        return null;
    }
}
