/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfsv.xml.v1_1_0;

import javax.xml.namespace.QName;

import org.geoserver.wfs.xml.FeatureTypeSchemaBuilder;
import org.geoserver.wfs.xml.v1_1_0.WFS;


/**
 * This interface contains the qualified names of all the types,elements, and
 * attributes in the http://www.opengis.net/wfsv schema.
 *
 * @generated
 */
public class WFSV extends WFS {
    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/wfsv";

    /* Type Definitions */
    /** @generated */
    public static final QName DifferenceQueryType = new QName("http://www.opengis.net/wfsv",
            "DifferenceQueryType");

    /** @generated */
    public static final QName GetDiffType = new QName("http://www.opengis.net/wfsv", "GetDiffType");

    /** @generated */
    public static final QName GetLogType = new QName("http://www.opengis.net/wfsv", "GetLogType");

    /** @generated */
    public static final QName RollbackType = new QName("http://www.opengis.net/wfsv", "RollbackType");

    /** @generated */
    public static final QName VersionedDeleteElementType = new QName("http://www.opengis.net/wfsv",
            "VersionedDeleteElementType");

    /** @generated */
    public static final QName VersionedUpdateElementType = new QName("http://www.opengis.net/wfsv",
            "VersionedUpdateElementType");

    /* Elements */
    /** @generated */
    public static final QName DifferenceQuery = new QName("http://www.opengis.net/wfsv",
            "DifferenceQuery");

    /** @generated */
    public static final QName GetDiff = new QName("http://www.opengis.net/wfsv", "GetDiff");

    /** @generated */
    public static final QName GetLog = new QName("http://www.opengis.net/wfsv", "GetLog");

    /** @generated */
    public static final QName Rollback = new QName("http://www.opengis.net/wfsv", "Rollback");

    /** @generated */
    public static final QName VersionedDelete = new QName("http://www.opengis.net/wfsv",
            "VersionedDelete");

    /** @generated */
    public static final QName VersionedUpdate = new QName("http://www.opengis.net/wfsv",
            "VersionedUpdate");

    /* Attributes */
    
    public WFSV(FeatureTypeSchemaBuilder schemaBuilder) {
        super(schemaBuilder);
    }
    
    public String getNamespaceURI() {
        return NAMESPACE;
    }
    
    public String getSchemaLocation() {
        return getClass().getResource("wfsv.xsd").toString();
    }

}
