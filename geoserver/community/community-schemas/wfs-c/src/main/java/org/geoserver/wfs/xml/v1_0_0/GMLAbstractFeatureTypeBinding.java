/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_0_0;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.geotools.gml2.FeatureTypeCache;
import org.geotools.xml.BindingWalkerFactory;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;


public final class GMLAbstractFeatureTypeBinding extends org.geotools.gml2.bindings.GMLAbstractFeatureTypeBinding {
    GeometryFactory geometryFactory;
    Data catalog;

    public GMLAbstractFeatureTypeBinding(FeatureTypeCache featureTypeCache,
        BindingWalkerFactory bwFactory, GeometryFactory geometryFactory, Data catalog) {
        super(featureTypeCache, bwFactory);
        this.geometryFactory = geometryFactory;
        this.catalog = catalog;
    }

    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //pre process parsee tree to make sure types match up
        FeatureTypeInfo meta = catalog.getFeatureTypeInfo(instance.getName(),
                instance.getNamespace());

        if (meta != null) {
            FeatureType featureType = meta.getFeatureType();

            //go through each attribute, performing various hacks to make make sure things 
            // cocher
            for (int i = 0; i < featureType.getAttributeCount(); i++) {
                AttributeType attributeType = featureType.getAttributeType(i);
                String name = attributeType.getName();
                Class type = attributeType.getType();

                if ("boundedBy".equals(name)) {
                    Node boundedByNode = node.getChild("boundedBy");

                    //hack 1: if boundedBy is in the parse tree has a bounding box and the attribute 
                    // needs a polygon, convert
                    if (boundedByNode.getValue() instanceof Envelope) {
                        Envelope bounds = (Envelope) boundedByNode.getValue();

                        if (type.isAssignableFrom(Polygon.class)) {
                            Polygon polygon = polygon(bounds);
                            boundedByNode.setValue(polygon);
                        } else if (type.isAssignableFrom(MultiPolygon.class)) {
                            MultiPolygon multiPolygon = geometryFactory.createMultiPolygon(new Polygon[] {
                                        polygon(bounds)
                                    });
                            boundedByNode.setValue(multiPolygon);
                        }
                    }
                }
            }
        }

        return super.parse(instance, node, value);
    }

    Polygon polygon(Envelope bounds) {
        return geometryFactory.createPolygon(geometryFactory.createLinearRing(
                new Coordinate[] {
                    new Coordinate(bounds.getMinX(), bounds.getMinY()),
                    new Coordinate(bounds.getMinX(), bounds.getMaxY()),
                    new Coordinate(bounds.getMaxX(), bounds.getMaxY()),
                    new Coordinate(bounds.getMaxX(), bounds.getMinY()),
                    new Coordinate(bounds.getMinX(), bounds.getMinY())
                }), null);
    }
}
