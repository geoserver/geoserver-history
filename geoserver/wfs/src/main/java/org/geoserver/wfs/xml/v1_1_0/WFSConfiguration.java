/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0;

import net.opengis.wfs.WfsFactory;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.geoserver.ows.xml.v1_0.OWSConfiguration;
import org.geoserver.wfs.xml.FeatureTypeSchemaBuilder;
import org.geoserver.wfs.xml.PropertyTypePropertyExtractor;
import org.geoserver.wfs.xml.WFSHandlerFactory;
import org.geoserver.wfs.xml.XSQNameBinding;
import org.geoserver.wfs.xml.filter.v1_1.FilterTypeBinding;
import org.geoserver.wfs.xml.filter.v1_1.PropertyNameTypeBinding;
import org.geoserver.wfs.xml.gml3.AbstractGeometryTypeBinding;
import org.geoserver.wfs.xml.gml3.CircleTypeBinding;
import org.geoserver.wfs.xml.xs.DateBinding;

import org.geotools.filter.v1_0.OGCBBOXTypeBinding;
import org.geotools.filter.v1_1.OGC;
import org.geotools.filter.v1_1.OGCConfiguration;
import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.gml3.GML;
import org.geotools.xml.Configuration;
import org.geotools.xml.OptionalComponentParameter;
import org.geotools.xs.XS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.SetterInjectionComponentAdapter;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;


public class WFSConfiguration extends Configuration {
    /**
     * catalog
     */
    protected Data catalog;

    /**
     * Schema builder
     */
    protected FeatureTypeSchemaBuilder schemaBuilder;

    public WFSConfiguration(Data catalog, FeatureTypeSchemaBuilder schemaBuilder, final WFS wfs) {
        super( wfs );

        this.catalog = catalog;
        this.schemaBuilder = schemaBuilder;
        
        catalog.getGeoServer().addListener(
          new GeoServer.Listener() {

            public void changed() {
                wfs.flush();
            }
          }
        );
        
        addDependency(new OGCConfiguration());
        addDependency(new GMLConfiguration());
        addDependency(new OWSConfiguration());
    }
    
    protected void registerBindings(MutablePicoContainer container) {
        //Types
        container.registerComponentImplementation(WFS.ACTIONTYPE, ActionTypeBinding.class);
        container.registerComponentImplementation(WFS.ALLSOMETYPE, AllSomeTypeBinding.class);
        container.registerComponentImplementation(WFS.BASE_TYPENAMELISTTYPE,
            Base_TypeNameListTypeBinding.class);
        container.registerComponentImplementation(WFS.BASEREQUESTTYPE, BaseRequestTypeBinding.class);
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
        container.registerComponentImplementation(WFS.FEATURETYPETYPE, FeatureTypeTypeBinding.class);
        container.registerComponentImplementation(WFS.GETCAPABILITIESTYPE,
            GetCapabilitiesTypeBinding.class);
        container.registerComponentImplementation(WFS.GETFEATURETYPE, GetFeatureTypeBinding.class);
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
        container.registerComponentImplementation(WFS.LOCKFEATURETYPE, LockFeatureTypeBinding.class);
        container.registerComponentImplementation(WFS.LOCKTYPE, LockTypeBinding.class);
        container.registerComponentImplementation(WFS.METADATAURLTYPE, MetadataURLTypeBinding.class);
        container.registerComponentImplementation(WFS.NATIVETYPE, NativeTypeBinding.class);
        container.registerComponentImplementation(WFS.OPERATIONSTYPE, OperationsTypeBinding.class);
        container.registerComponentImplementation(WFS.OPERATIONTYPE, OperationTypeBinding.class);
        container.registerComponentImplementation(WFS.OUTPUTFORMATLISTTYPE,
            OutputFormatListTypeBinding.class);
        container.registerComponentImplementation(WFS.PROPERTYTYPE, PropertyTypeBinding.class);
        container.registerComponentImplementation(WFS.QUERYTYPE, QueryTypeBinding.class);
        container.registerComponentImplementation(WFS.RESULTTYPETYPE, ResultTypeTypeBinding.class);
        container.registerComponentImplementation(WFS.TRANSACTIONRESPONSETYPE,
            TransactionResponseTypeBinding.class);
        container.registerComponentImplementation(WFS.TRANSACTIONRESULTSTYPE,
            TransactionResultsTypeBinding.class);
        container.registerComponentImplementation(WFS.TRANSACTIONSUMMARYTYPE,
            TransactionSummaryTypeBinding.class);
        container.registerComponentImplementation(WFS.TRANSACTIONTYPE, TransactionTypeBinding.class);
        container.registerComponentImplementation(WFS.TYPENAMELISTTYPE,
            TypeNameListTypeBinding.class);
        container.registerComponentImplementation(WFS.UPDATEELEMENTTYPE,
            UpdateElementTypeBinding.class);
        container.registerComponentImplementation(WFS.WFS_CAPABILITIESTYPE,
            WFS_CapabilitiesTypeBinding.class);
    }

    public Data getCatalog() {
        return catalog;
    }
    
    public void addDependency(Configuration dependency) {
        //override to make public
        super.addDependency(dependency);
    }

    protected void configureContext(MutablePicoContainer context) {
        super.configureContext(context);

        context.registerComponentInstance(WfsFactory.eINSTANCE);
        context.registerComponentInstance(new WFSHandlerFactory(catalog, schemaBuilder));
        context.registerComponentInstance(catalog);
        context.registerComponentImplementation(PropertyTypePropertyExtractor.class);

        //seed the cache with entries from the catalog
        FeatureTypeCache featureTypeCache = (FeatureTypeCache) context
            .getComponentInstanceOfType(FeatureTypeCache.class);

        try {
            Collection featureTypes = catalog.getFeatureTypeInfos().values();

            for (Iterator f = featureTypes.iterator(); f.hasNext();) {
                FeatureTypeInfo meta = (FeatureTypeInfo) f.next();
                SimpleFeatureType featureType = meta.getFeatureType();

                featureTypeCache.put(featureType);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void configureBindings(MutablePicoContainer container) {
        //register our custom bindings
        container.registerComponentImplementation(XS.DATE, DateBinding.class);
        container.registerComponentImplementation(OGC.Filter, FilterTypeBinding.class);
        container.registerComponentImplementation(OGC.PropertyNameType,
            PropertyNameTypeBinding.class);
        container.registerComponentImplementation(GML.CircleType, CircleTypeBinding.class);
        
        //use setter injection for AbstractGeometryType bindign to allow an 
        // optional crs to be set in teh binding context for parsing, this crs
        // is set by the binding of a parent element.
        // note: it is important that this component adapter is non-caching so 
        // that the setter property gets updated properly every time
        container.registerComponent(
            new SetterInjectionComponentAdapter( 
                GML.AbstractGeometryType, AbstractGeometryTypeBinding.class, 
                new Parameter[]{ new OptionalComponentParameter(CoordinateReferenceSystem.class)} 
            )
        );
        
        // use setter injection for OGCBBoxTypeBinding to allow an 
        // optional crs to be set in teh binding context for parsing, this crs
        // is set by the binding of a parent element.
        // note: it is important that this component adapter is non-caching so 
        // that the setter property gets updated properly every time
        container.registerComponent(new SetterInjectionComponentAdapter(OGC.BBOXType,
                OGCBBOXTypeBinding.class,
                new Parameter[] { new OptionalComponentParameter(CoordinateReferenceSystem.class) }));
        
        // override XSQName binding
        container.registerComponentImplementation(XS.QNAME, XSQNameBinding.class);
    }
}
