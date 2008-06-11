/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_0_0;

import org.geotools.xml.BindingConfiguration;
import org.picocontainer.MutablePicoContainer;


/**
 * Binding configuration for the http://www.opengis.net/wfs schema.
 *
 * @generated
 */
public final class WFSBindingConfiguration implements BindingConfiguration {
    /**
     * @generated modifiable
     */
    public void configure(MutablePicoContainer container) {
        //Types
        container.registerComponentImplementation(WFS.ALLSOMETYPE, AllSomeTypeBinding.class);
        container.registerComponentImplementation(WFS.DELETEELEMENTTYPE,
            DeleteElementTypeBinding.class);
        container.registerComponentImplementation(WFS.DESCRIBEFEATURETYPETYPE,
            DescribeFeatureTypeTypeBinding.class);
        container.registerComponentImplementation(WFS.EMPTYTYPE, EmptyTypeBinding.class);
        container.registerComponentImplementation(WFS.FEATURECOLLECTIONTYPE,
            FeatureCollectionTypeBinding.class);
        container.registerComponentImplementation(WFS.FEATURESLOCKEDTYPE,
            FeaturesLockedTypeBinding.class);
        container.registerComponentImplementation(WFS.FEATURESNOTLOCKEDTYPE,
            FeaturesNotLockedTypeBinding.class);
        container.registerComponentImplementation(WFS.GETCAPABILITIESTYPE,
            GetCapabilitiesTypeBinding.class);
        container.registerComponentImplementation(WFS.GETFEATURETYPE, GetFeatureTypeBinding.class);
        container.registerComponentImplementation(WFS.GETFEATUREWITHLOCKTYPE,
            GetFeatureWithLockTypeBinding.class);
        container.registerComponentImplementation(WFS.INSERTELEMENTTYPE,
            InsertElementTypeBinding.class);
        container.registerComponentImplementation(WFS.INSERTRESULTTYPE,
            InsertResultTypeBinding.class);
        container.registerComponentImplementation(WFS.LOCKFEATURETYPE, LockFeatureTypeBinding.class);
        container.registerComponentImplementation(WFS.LOCKTYPE, LockTypeBinding.class);
        container.registerComponentImplementation(WFS.NATIVETYPE, NativeTypeBinding.class);
        container.registerComponentImplementation(WFS.PROPERTYTYPE, PropertyTypeBinding.class);
        container.registerComponentImplementation(WFS.QUERYTYPE, QueryTypeBinding.class);
        container.registerComponentImplementation(WFS.STATUSTYPE, StatusTypeBinding.class);
        container.registerComponentImplementation(WFS.TRANSACTIONRESULTTYPE,
            TransactionResultTypeBinding.class);
        container.registerComponentImplementation(WFS.TRANSACTIONTYPE, TransactionTypeBinding.class);
        container.registerComponentImplementation(WFS.UPDATEELEMENTTYPE,
            UpdateElementTypeBinding.class);
        container.registerComponentImplementation(WFS.WFS_LOCKFEATURERESPONSETYPE,
            WFS_LockFeatureResponseTypeBinding.class);
        container.registerComponentImplementation(WFS.WFS_TRANSACTIONRESPONSETYPE,
            WFS_TransactionResponseTypeBinding.class);
    }
}
