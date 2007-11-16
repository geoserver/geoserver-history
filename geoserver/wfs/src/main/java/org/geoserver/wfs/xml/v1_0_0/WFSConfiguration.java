/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_0_0;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import net.opengis.ows.OwsFactory;
import net.opengis.wfs.WfsFactory;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.geoserver.wfs.xml.FeatureTypeSchemaBuilder;
import org.geoserver.wfs.xml.WFSHandlerFactory;
import org.geoserver.wfs.xml.gml3.AbstractGeometryTypeBinding;

import org.geotools.filter.v1_0.OGCBBOXTypeBinding;
import org.geotools.filter.v1_0.OGCConfiguration;
import org.geotools.filter.v1_1.OGC;
import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml2.GML;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.OptionalComponentParameter;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.SetterInjectionComponentAdapter;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;


/**
 * Parser configuration for wfs 1.0.
 *
 * @author Justin Deoliveira, The Open Planning Project
 * TODO: this class duplicates a lot of what is is in the 1.1 configuration, merge them
 */
public class WFSConfiguration extends Configuration {
    Data catalog;
    FeatureTypeSchemaBuilder schemaBuilder;

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
    }

    protected void registerBindings(MutablePicoContainer container) {
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
    
    public void configureContext(MutablePicoContainer context) {
        super.configureContext(context);

        context.registerComponentInstance(OwsFactory.eINSTANCE);
        context.registerComponentInstance(WfsFactory.eINSTANCE);
        context.registerComponentInstance(new WFSHandlerFactory(catalog, schemaBuilder));
        context.registerComponentInstance(catalog);
        
        //TODO: this code is copied from the 1.1 configuration, FACTOR IT OUT!!!
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

    protected void configureBindings(MutablePicoContainer bindings) {
        super.configureBindings(bindings);

        //override the GMLAbstractFeatureTypeBinding
        bindings.registerComponentImplementation(GML.AbstractFeatureType,
            GMLAbstractFeatureTypeBinding.class);
        
        //use setter injection for AbstractGeometryType bindign to allow an 
        // optional crs to be set in teh binding context for parsing, this crs
        // is set by the binding of a parent element.
        // note: it is important that this component adapter is non-caching so 
        // that the setter property gets updated properly every time
        bindings.registerComponent(
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
        bindings.registerComponent(new SetterInjectionComponentAdapter(OGC.BBOXType,
                OGCBBOXTypeBinding.class,
                new Parameter[] { new OptionalComponentParameter(CoordinateReferenceSystem.class) }));
        
    }
}
