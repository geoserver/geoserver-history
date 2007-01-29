/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import net.opengis.wfs.DescribeFeatureTypeType;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetCapabilitiesType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.GetFeatureWithLockType;
import net.opengis.wfs.LockFeatureResponseType;
import net.opengis.wfs.LockFeatureType;
import net.opengis.wfs.TransactionResponseType;
import net.opengis.wfs.TransactionType;
import org.geotools.xml.transform.TransformerBase;
import org.vfny.geoserver.global.FeatureTypeInfo;


/**
 * Web Feature Service.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public interface WebFeatureService {
    /**
     * WFS GetCapabilities operation.
     *
     * @param request The get capabilities request.
     *
     * @return A transformer instance capable of serializing a wfs capabilities
     * document.
     *
     * @throws WFSException Any service exceptions.
     */
    public TransformerBase getCapabilities(GetCapabilitiesType request)
        throws WFSException;

    /**
     * WFS DescribeFeatureType operation.
     *
     * @param request The describe feature type request.
     *
     * @return A set of feature type metadata objects.
     *
     * @throws WFSException Any service exceptions.
     */
    public FeatureTypeInfo[] describeFeatureType(DescribeFeatureTypeType request)
        throws WFSException;

    /**
     * WFS GetFeature operation.
     *
     * @param request The get feature request.
     *
     * @return A feature collection type instance.
     *
     * @throws WFSException Any service exceptions.
     */
    public FeatureCollectionType getFeature(GetFeatureType request)
        throws WFSException;

    /**
     * WFS GetFeatureWithLock operation.
     *
     * @param request The get feature with lock request.
     *
      * @return A feature collection type instance.
     *
     * @throws WFSException Any service exceptions.
     */
    public FeatureCollectionType getFeatureWithLock(GetFeatureWithLockType request)
        throws WFSException;

    /**
     * WFS LockFeatureType operation.
     *
     * @param request The lock feature request.
     *
     * @return A lock feture response type.
     *
     * @throws WFSException An service exceptions.
     */
    public LockFeatureResponseType lockFeature(LockFeatureType request)
        throws WFSException;

    /**
     * WFS transaction operation.
     *
     * @param request The transaction request.
     *
     * @return A transaction response instance.
     *
     * @throws WFSException Any service exceptions.
     */
    public TransactionResponseType transaction(TransactionType request)
        throws WFSException;
}
