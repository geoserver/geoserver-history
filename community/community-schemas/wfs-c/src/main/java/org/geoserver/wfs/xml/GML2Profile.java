/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml;

import org.geotools.feature.Name;
import org.geotools.gml2.GMLSchema;
import org.geotools.gml2.bindings.GML;
import java.util.HashSet;
import java.util.Set;


public class GML2Profile extends TypeMappingProfile {
    static Set profile = new HashSet();

    static {
        //profile.add(new Name(GML.NAMESPACE, GML.POINTTYPE.getLocalPart()));
        profile.add(new Name(GML.NAMESPACE, GML.PointPropertyType.getLocalPart()));
        //profile.add(new Name(GML.NAMESPACE, GML.MULTIPOINTTYPE.getLocalPart()));
        profile.add(new Name(GML.NAMESPACE, GML.MultiPointPropertyType.getLocalPart()));

        //profile.add(new Name(GML.NAMESPACE, GML.LINESTRINGTYPE.getLocalPart()));
        profile.add(new Name(GML.NAMESPACE, GML.LineStringPropertyType.getLocalPart()));
        //profile.add(new Name(GML.NAMESPACE, GML.MULTILINESTRINGTYPE.getLocalPart()));
        profile.add(new Name(GML.NAMESPACE, GML.MultiLineStringPropertyType.getLocalPart()));

        //profile.add(new Name(GML.NAMESPACE, GML.POLYGONTYPE.getLocalPart()));
        profile.add(new Name(GML.NAMESPACE, GML.PolygonPropertyType.getLocalPart()));
        //profile.add(new Name(GML.NAMESPACE, GML.MULTIPOLYGONTYPE.getLocalPart()));
        profile.add(new Name(GML.NAMESPACE, GML.MultiPolygonPropertyType.getLocalPart()));

        profile.add(new Name(GML.NAMESPACE, GML.GeometryPropertyType.getLocalPart()));
    }

    public GML2Profile() {
        super(new GMLSchema(), profile);
    }
}
