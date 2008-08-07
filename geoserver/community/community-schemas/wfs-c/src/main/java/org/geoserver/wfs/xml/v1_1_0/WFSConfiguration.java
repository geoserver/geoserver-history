/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0;

import javax.xml.namespace.QName;

import net.opengis.wfs.WfsFactory;

import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.geoserver.ows.xml.v1_0.OWSConfiguration;
import org.geoserver.wfs.xml.FeatureTypeSchemaBuilder;
import org.geoserver.wfs.xml.WFSHandlerFactory;
import org.geoserver.wfs.xml.XSQNameBinding;
import org.geoserver.wfs.xml.filter.v1_1.FilterTypeBinding;
import org.geoserver.wfs.xml.filter.v1_1.PropertyNameTypeBinding;
import org.geoserver.wfs.xml.gml3.CircleTypeBinding;
import org.geoserver.wfs.xml.v1_1_0.overrides.ISOAbstractFeatureTypeBinding;
import org.geoserver.wfs.xml.v1_1_0.overrides.ISOAbstractGeometryTypeBinding;
import org.geoserver.wfs.xml.v1_1_0.overrides.ISOCodeTypeBinding;
import org.geoserver.wfs.xml.v1_1_0.overrides.ISOXSDateBinding;
import org.geoserver.wfs.xml.v1_1_0.overrides.ISOXSDateTimeBinding;
import org.geoserver.wfs.xml.v1_1_0.overrides.ISOCurvePropertyTypeBinding;
import org.geoserver.wfs.xml.v1_1_0.overrides.ISOEnvelopeTypeBinding;
import org.geoserver.wfs.xml.v1_1_0.overrides.ISOFeaturePropertyExtractor;
import org.geoserver.wfs.xml.v1_1_0.overrides.ISOFeaturePropertyTypeBinding;
import org.geoserver.wfs.xml.v1_1_0.overrides.ISOGeometryPropertyTypeBinding;
import org.geoserver.wfs.xml.v1_1_0.overrides.ISOMeasureTypeBinding;
import org.geoserver.wfs.xml.v1_1_0.overrides.ISOMultiPointTypeBinding;
import org.geoserver.wfs.xml.v1_1_0.overrides.ISOMultiSurfaceTypeBinding;
import org.geoserver.wfs.xml.v1_1_0.overrides.ISOPointTypeBinding;
import org.geoserver.wfs.xml.v1_1_0.overrides.ISOXSAnyTypeBinding;
import org.geoserver.wfs.xml.xs.DateBinding;
import org.geotools.filter.v1_1.OGC;
import org.geotools.filter.v1_1.RegfuncOGCConfiguration;
import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.gml3.bindings.GML;
import org.geotools.xml.BindingConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Schemas;
import org.geotools.xs.bindings.XS;
import org.picocontainer.MutablePicoContainer;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;


public class WFSConfiguration extends Configuration {
    /**
     * catalog
     */
    Data catalog;

    /**
     * Schema builder
     */
    FeatureTypeSchemaBuilder schemaBuilder;

    public WFSConfiguration(Data catalog, FeatureTypeSchemaBuilder schemaBuilder) {
        super();

        this.catalog = catalog;
        this.schemaBuilder = schemaBuilder;

        catalog.getGeoServer().addListener(new GeoServer.Listener() {
                public void changed() {
                    flush();
                }
            });

        addDependency(new RegfuncOGCConfiguration());
        addDependency(new GMLConfiguration());
        addDependency(new OWSConfiguration());
    }

    public void addDependency(Configuration dependency) {
        //override to make public
        super.addDependency(dependency);
    }

    public String getNamespaceURI() {
        return WFS.NAMESPACE;
    }

    public String getSchemaFileURL() {
        return getSchemaLocationResolver().resolveSchemaLocation(null, WFS.NAMESPACE, "wfs.xsd");
    }

    public BindingConfiguration getBindingConfiguration() {
        return new WFSBindingConfiguration();
    }

    public XSDSchemaLocationResolver getSchemaLocationResolver() {
        return new WFSSchemaLocationResolver();
    }

    protected XSDSchemaLocator createSchemaLocator() {
        return new WFSSchemaLocator(this, catalog, schemaBuilder);
    }

    public void configureContext(MutablePicoContainer context) {
        super.configureContext(context);

        context.registerComponentInstance(WfsFactory.eINSTANCE);
        context.registerComponentInstance(new WFSHandlerFactory(catalog, schemaBuilder));
        context.registerComponentInstance(catalog);
        //context.registerComponentImplementation(PropertyTypePropertyExtractor.class);
        context.registerComponentImplementation(ISOFeaturePropertyExtractor.class);
        
        //seed the cache with entries from the catalog
        FeatureTypeCache featureTypeCache = (FeatureTypeCache) context
            .getComponentInstanceOfType(FeatureTypeCache.class);

        /*
         * GR:commented out this block of code as setting up the featuretype
         * cache is only useful for gml parsing
        try {
            Collection featureTypes = catalog.getFeatureTypeInfos().values();

            for (Iterator f = featureTypes.iterator(); f.hasNext();) {
                FeatureTypeInfo meta = (FeatureTypeInfo) f.next();
                FeatureType featureType = meta.getFeatureType();

                featureTypeCache.put(featureType);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        */
    }

    protected void configureBindings(MutablePicoContainer container) {
        //register our custom bindings
       // container.registerComponentImplementation(XS.DATE, DateBinding.class);
        container.registerComponentImplementation(OGC.Filter, FilterTypeBinding.class);
        container.registerComponentImplementation(OGC.PropertyNameType,
            PropertyNameTypeBinding.class);
        container.registerComponentImplementation(GML.CircleType, CircleTypeBinding.class);
        
//        container.registerComponentImplementation(GML.AbstractGeometryType,
//                            AbstractGeometryTypeBinding.class);
                
        // remove bindings for MultiPolygon and MultiLineString
        // TODO: make this cite configurable
        Schemas.unregisterComponent(container, GML.MultiPolygonType);

        Schemas.unregisterComponent(container, XS.STRING);

        // register the overriding bindings needed to
        // encode from ISO Features
        registerBindingOverrides(container);


        //use setter injection for AbstractGeometryType bindign to allow an 
        // optional crs to be set in teh binding context for parsing, this crs
        // is set by the binding of a parent element.
        // note: it is important that this component adapter is non-caching so 
        // that the setter property gets updated properly every time
//        container.registerComponent(new SetterInjectionComponentAdapter(GML.AbstractGeometryType,
//                AbstractGeometryTypeBinding.class,
//                new Parameter[] { new OptionalComponentParameter(CoordinateReferenceSystem.class) }));

        // override XSQName binding
        container.registerComponentImplementation(XS.QNAME, XSQNameBinding.class);
    }
    
    private void registerBindingOverrides(MutablePicoContainer container) {
        registerOverride(container, XS.ANYTYPE, ISOXSAnyTypeBinding.class);
        registerOverride(container, GML.CodeType, ISOCodeTypeBinding.class);
        registerOverride(container, GML.MeasureType, ISOMeasureTypeBinding.class);
        registerOverride(container, GML.EnvelopeType, ISOEnvelopeTypeBinding.class);
        /*   
        registerOverride(container, XS.DATE, ISOXSDateBinding.class);
        registerOverride(container, XS.DATETIME, ISOXSDateTimeBinding.class);
      
        registerOverride(container, XS.COMPLEXTYPE, ISOXSComplexTypeBinding.class);
         */
        registerOverride(container, GML.FeaturePropertyType, ISOFeaturePropertyTypeBinding.class);
        registerOverride(container, GML.AbstractFeatureType, ISOAbstractFeatureTypeBinding.class);

        registerOverride(container, GML.AbstractGeometryType, ISOAbstractGeometryTypeBinding.class);
        registerOverride(container, GML.GeometryPropertyType, ISOGeometryPropertyTypeBinding.class);

        registerOverride(container, GML.PointType, ISOPointTypeBinding.class);
        registerOverride(container, GML.MultiPointType, ISOMultiPointTypeBinding.class);
        //registerOverride(container, GML.PointPropertyType, ISOPointPropertyTypeBinding.class);
        registerOverride(container, GML.MultiSurfaceType, ISOMultiSurfaceTypeBinding.class);

        registerOverride(container, GML.CurvePropertyType, ISOCurvePropertyTypeBinding.class);
    }

    /**
     * Deregisters the existing binding for the provided <code>name</code>
     * from the <code>container</code> and registers a new binding for it.
     *
     * @param container
     * @param name
     * @param newBinding
     */
    private void registerOverride(MutablePicoContainer container, QName name, Class newBinding) {
        Schemas.unregisterComponent(container, name);
        container.registerComponentImplementation(name, newBinding);
    }

}
