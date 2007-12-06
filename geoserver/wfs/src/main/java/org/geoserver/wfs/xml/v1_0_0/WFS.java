/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_0_0;

import javax.xml.namespace.QName;


/**
 * This interface contains the qualified names of all the types,elements, and
 * attributes in the http://www.opengis.net/wfs schema.
 *
 * @generated
 */
public interface WFS {
    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/wfs";

    /* Type Definitions */
    /** @generated */
    public static final QName ALLSOMETYPE = new QName("http://www.opengis.net/wfs",
            "AllSomeType");

    /** @generated */
    public static final QName DELETEELEMENTTYPE = new QName("http://www.opengis.net/wfs",
            "DeleteElementType");

    /** @generated */
    public static final QName DESCRIBEFEATURETYPETYPE = new QName("http://www.opengis.net/wfs",
            "DescribeFeatureTypeType");

    /** @generated */
    public static final QName EMPTYTYPE = new QName("http://www.opengis.net/wfs",
            "EmptyType");

    /** @generated */
    public static final QName FEATURECOLLECTIONTYPE = new QName("http://www.opengis.net/wfs",
            "FeatureCollectionType");

    /** @generated */
    public static final QName FEATURESLOCKEDTYPE = new QName("http://www.opengis.net/wfs",
            "FeaturesLockedType");

    /** @generated */
    public static final QName FEATURESNOTLOCKEDTYPE = new QName("http://www.opengis.net/wfs",
            "FeaturesNotLockedType");

    /** @generated */
    public static final QName GETCAPABILITIESTYPE = new QName("http://www.opengis.net/wfs",
            "GetCapabilitiesType");

    /** @generated */
    public static final QName GETFEATURETYPE = new QName("http://www.opengis.net/wfs",
            "GetFeatureType");

    /** @generated */
    public static final QName GETFEATUREWITHLOCKTYPE = new QName("http://www.opengis.net/wfs",
            "GetFeatureWithLockType");

    /** @generated */
    public static final QName INSERTELEMENTTYPE = new QName("http://www.opengis.net/wfs",
            "InsertElementType");

    /** @generated */
    public static final QName INSERTRESULTTYPE = new QName("http://www.opengis.net/wfs",
            "InsertResultType");

    /** @generated */
    public static final QName LOCKFEATURETYPE = new QName("http://www.opengis.net/wfs",
            "LockFeatureType");

    /** @generated */
    public static final QName LOCKTYPE = new QName("http://www.opengis.net/wfs",
            "LockType");

    /** @generated */
    public static final QName NATIVETYPE = new QName("http://www.opengis.net/wfs",
            "NativeType");

    /** @generated */
    public static final QName PROPERTYTYPE = new QName("http://www.opengis.net/wfs",
            "PropertyType");

    /** @generated */
    public static final QName QUERYTYPE = new QName("http://www.opengis.net/wfs",
            "QueryType");

    /** @generated */
    public static final QName STATUSTYPE = new QName("http://www.opengis.net/wfs",
            "StatusType");

    /** @generated */
    public static final QName TRANSACTIONRESULTTYPE = new QName("http://www.opengis.net/wfs",
            "TransactionResultType");

    /** @generated */
    public static final QName TRANSACTIONTYPE = new QName("http://www.opengis.net/wfs",
            "TransactionType");

    /** @generated */
    public static final QName UPDATEELEMENTTYPE = new QName("http://www.opengis.net/wfs",
            "UpdateElementType");

    /** @generated */
    public static final QName WFS_LOCKFEATURERESPONSETYPE = new QName("http://www.opengis.net/wfs",
            "WFS_LockFeatureResponseType");

    /** @generated */
    public static final QName WFS_TRANSACTIONRESPONSETYPE = new QName("http://www.opengis.net/wfs",
            "WFS_TransactionResponseType");

    /* Elements */
    /** @generated */
    public static final QName DELETE = new QName("http://www.opengis.net/wfs",
            "Delete");

    /** @generated */
    public static final QName DESCRIBEFEATURETYPE = new QName("http://www.opengis.net/wfs",
            "DescribeFeatureType");

    /** @generated */
    public static final QName FAILED = new QName("http://www.opengis.net/wfs",
            "FAILED");

    /** @generated */
    public static final QName FEATURECOLLECTION = new QName("http://www.opengis.net/wfs",
            "FeatureCollection");

    /** @generated */
    public static final QName GETCAPABILITIES = new QName("http://www.opengis.net/wfs",
            "GetCapabilities");

    /** @generated */
    public static final QName GETFEATURE = new QName("http://www.opengis.net/wfs",
            "GetFeature");

    /** @generated */
    public static final QName GETFEATUREWITHLOCK = new QName("http://www.opengis.net/wfs",
            "GetFeatureWithLock");

    /** @generated */
    public static final QName INSERT = new QName("http://www.opengis.net/wfs",
            "Insert");

    /** @generated */
    public static final QName LOCKFEATURE = new QName("http://www.opengis.net/wfs",
            "LockFeature");

    /** @generated */
    public static final QName LOCKID = new QName("http://www.opengis.net/wfs",
            "LockId");

    /** @generated */
    public static final QName NATIVE = new QName("http://www.opengis.net/wfs",
            "Native");

    /** @generated */
    public static final QName PARTIAL = new QName("http://www.opengis.net/wfs",
            "PARTIAL");

    /** @generated */
    public static final QName PROPERTY = new QName("http://www.opengis.net/wfs",
            "Property");

    /** @generated */
    public static final QName QUERY = new QName("http://www.opengis.net/wfs",
            "Query");

    /** @generated */
    public static final QName SUCCESS = new QName("http://www.opengis.net/wfs",
            "SUCCESS");

    /** @generated */
    public static final QName TRANSACTION = new QName("http://www.opengis.net/wfs",
            "Transaction");

    /** @generated */
    public static final QName UPDATE = new QName("http://www.opengis.net/wfs",
            "Update");

    /** @generated */
    public static final QName WFS_LOCKFEATURERESPONSE = new QName("http://www.opengis.net/wfs",
            "WFS_LockFeatureResponse");

    /** @generated */
    public static final QName WFS_TRANSACTIONRESPONSE = new QName("http://www.opengis.net/wfs",
            "WFS_TransactionResponse");

    /* Attributes */
}
