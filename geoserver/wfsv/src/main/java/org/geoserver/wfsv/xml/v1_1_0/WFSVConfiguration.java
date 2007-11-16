/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfsv.xml.v1_1_0;

import net.opengis.wfsv.WfsvFactory;

import org.geoserver.wfs.xml.FeatureTypeSchemaBuilder;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.picocontainer.MutablePicoContainer;
import org.vfny.geoserver.global.Data;


/**
 * Parser configuration for the http://www.opengis.net/wfsv schema.
 *
 * @generated
 */
public class WFSVConfiguration extends WFSConfiguration {
    /**
     * Creates a new configuration.
     *
     * @generated
     */
    public WFSVConfiguration(Data catalog, FeatureTypeSchemaBuilder schemaBuilder, WFSV wfsv) {
        super(catalog, schemaBuilder, wfsv);
    }

    protected void registerBindings(MutablePicoContainer container) {
        super.registerBindings(container);
        
        //Types
        container.registerComponentImplementation(WFSV.DifferenceQueryType,
            DifferenceQueryTypeBinding.class);
        container.registerComponentImplementation(WFSV.GetDiffType, GetDiffTypeBinding.class);
        container.registerComponentImplementation(WFSV.GetLogType, GetLogTypeBinding.class);
        container.registerComponentImplementation(WFSV.RollbackType, RollbackTypeBinding.class);
        container.registerComponentImplementation(WFSV.VersionedDeleteElementType,
            VersionedDeleteElementTypeBinding.class);
        container.registerComponentImplementation(WFSV.VersionedUpdateElementType,
            VersionedUpdateElementTypeBinding.class);
    }
    
    public void configureContext(MutablePicoContainer context) {
        super.configureContext(context);
        //        context.registerComponentInstance(OwsFactory.eINSTANCE);
        //        context.registerComponentInstance(WfsFactory.eINSTANCE);
        context.registerComponentInstance(WfsvFactory.eINSTANCE);
    }
}
