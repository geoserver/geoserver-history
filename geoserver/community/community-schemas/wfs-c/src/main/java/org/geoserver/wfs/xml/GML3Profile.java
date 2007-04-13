/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml;

import org.geotools.feature.Name;
import org.geotools.gml3.GMLSchema;
import org.geotools.gml3.bindings.GML;
import java.util.HashSet;
import java.util.Set;


public class GML3Profile extends TypeMappingProfile {
    static Set profile = new HashSet();

    static {
        //basic
        profile.add(new Name(GML.NAMESPACE, GML.MeasureType.getLocalPart()));

        //geomtetries
        //profile.add( new Name(  GML.NAMESPACE, GML.PointType.getLocalPart() ) );
        profile.add(new Name(GML.NAMESPACE, GML.PointPropertyType.getLocalPart()));
        //profile.add( new Name(  GML.NAMESPACE, GML.MultiPointType.getLocalPart() ) );
        profile.add(new Name(GML.NAMESPACE, GML.MultiPointPropertyType.getLocalPart()));

        //profile.add( new Name(  GML.NAMESPACE, GML.LineStringType.getLocalPart() ) );
        profile.add(new Name(GML.NAMESPACE, GML.LineStringPropertyType.getLocalPart()));
        //profile.add( new Name(  GML.NAMESPACE, GML.MultiLineStringType.getLocalPart() ) );
        profile.add(new Name(GML.NAMESPACE, GML.MultiLineStringPropertyType.getLocalPart()));

        //profile.add( new Name(  GML.NAMESPACE, GML.CurveType.getLocalPart() ) );
        profile.add(new Name(GML.NAMESPACE, GML.CurvePropertyType.getLocalPart()));
        //profile.add( new Name(  GML.NAMESPACE, GML.MultiCurveType.getLocalPart() ) );
        profile.add(new Name(GML.NAMESPACE, GML.MultiCurvePropertyType.getLocalPart()));

        //profile.add( new Name(  GML.NAMESPACE, GML.PolygonType.getLocalPart() ) );
        profile.add(new Name(GML.NAMESPACE, GML.PolygonPropertyType.getLocalPart()));
        profile.add(new Name(GML.NAMESPACE, GML.SurfacePropertyType.getLocalPart()));

        //profile.add( new Name(  GML.NAMESPACE, GML.MultiPolygonType.getLocalPart() ) );
        profile.add(new Name(GML.NAMESPACE, GML.MultiPolygonPropertyType.getLocalPart()));
        //profile.add( new Name(  GML.NAMESPACE, GML.MultiSurfaceType.getLocalPart() ) );
        profile.add(new Name(GML.NAMESPACE, GML.MultiSurfacePropertyType.getLocalPart()));

        //profile.add( new Name(  GML.NAMESPACE, GML.AbstractGeometryType.getLocalPart() ) );
        profile.add(new Name(GML.NAMESPACE, GML.GeometryPropertyType.getLocalPart()));
    }

    public GML3Profile() {
        super(new GMLSchema(), profile);
    }
}
