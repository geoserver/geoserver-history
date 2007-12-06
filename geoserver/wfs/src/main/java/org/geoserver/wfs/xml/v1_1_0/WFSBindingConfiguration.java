/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0;

import org.geotools.xml.BindingConfiguration;
import org.picocontainer.MutablePicoContainer;


/**
 * Binding configuration for the http://www.opengis.net/wfs schema.
 *
 * @generated
 */
public class WFSBindingConfiguration implements BindingConfiguration {
    /**
     * @generated modifiable
     */
    public void configure(MutablePicoContainer container) {
        //Types
        container.registerComponentImplementation(WFS.ACTIONTYPE,
            ActionTypeBinding.class);
        container.registerComponentImplementation(WFS.ALLSOMETYPE,
            AllSomeTypeBinding.class);
        container.registerComponentImplementation(WFS.BASE_TYPENAMELISTTYPE,
            Base_TypeNameListTypeBinding.class);
        container.registerComponentImplementation(WFS.BASEREQUESTTYPE,
            BaseRequestTypeBinding.class);
        container.registerComponentImplementation(WFS.DELETEELEMENTTYPE,
            DeleteElementTypeBinding.class);
        container.registerComponentImplementation(WFS.DESCRIBEFEATURETYPETYPE,
            DescribeFeatureTypeTypeBinding.class);
        container.registerComponentImplementation(WFS.FEATURECOLLECTIONTYPE,
            FeatureCollectionTypeBinding.class);
        container.registerComponentImplementation(WFS.FEATURESLOCKEDTYPE,
            FeaturesLockedTypeBinding.class);
        container.registerComponentImplementation(WFS.FEATURESNOTLOCKEDTYPE,
            FeaturesNotLockedTypeBinding.class);
        container.registerComponentImplementation(WFS.FEATURETYPELISTTYPE,
            FeatureTypeListTypeBinding.class);
        container.registerComponentImplementation(WFS.FEATURETYPETYPE,
            FeatureTypeTypeBinding.class);
        container.registerComponentImplementation(WFS.GETCAPABILITIESTYPE,
            GetCapabilitiesTypeBinding.class);
        container.registerComponentImplementation(WFS.GETFEATURETYPE,
            GetFeatureTypeBinding.class);
        container.registerComponentImplementation(WFS.GETFEATUREWITHLOCKTYPE,
            GetFeatureWithLockTypeBinding.class);
        container.registerComponentImplementation(WFS.GETGMLOBJECTTYPE,
            GetGmlObjectTypeBinding.class);
        container.registerComponentImplementation(WFS.GMLOBJECTTYPELISTTYPE,
            GMLObjectTypeListTypeBinding.class);
        container.registerComponentImplementation(WFS.GMLOBJECTTYPETYPE,
            GMLObjectTypeTypeBinding.class);
        container.registerComponentImplementation(WFS.IDENTIFIERGENERATIONOPTIONTYPE,
            IdentifierGenerationOptionTypeBinding.class);
        container.registerComponentImplementation(WFS.INSERTEDFEATURETYPE,
            InsertedFeatureTypeBinding.class);
        container.registerComponentImplementation(WFS.INSERTELEMENTTYPE,
            InsertElementTypeBinding.class);
        container.registerComponentImplementation(WFS.INSERTRESULTSTYPE,
            InsertResultTypeBinding.class);
        container.registerComponentImplementation(WFS.LOCKFEATURERESPONSETYPE,
            LockFeatureResponseTypeBinding.class);
        container.registerComponentImplementation(WFS.LOCKFEATURETYPE,
            LockFeatureTypeBinding.class);
        container.registerComponentImplementation(WFS.LOCKTYPE,
            LockTypeBinding.class);
        container.registerComponentImplementation(WFS.METADATAURLTYPE,
            MetadataURLTypeBinding.class);
        container.registerComponentImplementation(WFS.NATIVETYPE,
            NativeTypeBinding.class);
        container.registerComponentImplementation(WFS.OPERATIONSTYPE,
            OperationsTypeBinding.class);
        container.registerComponentImplementation(WFS.OPERATIONTYPE,
            OperationTypeBinding.class);
        container.registerComponentImplementation(WFS.OUTPUTFORMATLISTTYPE,
            OutputFormatListTypeBinding.class);
        container.registerComponentImplementation(WFS.PROPERTYTYPE,
            PropertyTypeBinding.class);
        container.registerComponentImplementation(WFS.QUERYTYPE,
            QueryTypeBinding.class);
        container.registerComponentImplementation(WFS.RESULTTYPETYPE,
            ResultTypeTypeBinding.class);
        container.registerComponentImplementation(WFS.TRANSACTIONRESPONSETYPE,
            TransactionResponseTypeBinding.class);
        container.registerComponentImplementation(WFS.TRANSACTIONRESULTSTYPE,
            TransactionResultsTypeBinding.class);
        container.registerComponentImplementation(WFS.TRANSACTIONSUMMARYTYPE,
            TransactionSummaryTypeBinding.class);
        container.registerComponentImplementation(WFS.TRANSACTIONTYPE,
            TransactionTypeBinding.class);
        container.registerComponentImplementation(WFS.TYPENAMELISTTYPE,
            TypeNameListTypeBinding.class);
        container.registerComponentImplementation(WFS.UPDATEELEMENTTYPE,
            UpdateElementTypeBinding.class);
        container.registerComponentImplementation(WFS.WFS_CAPABILITIESTYPE,
            WFS_CapabilitiesTypeBinding.class);
    }
}
