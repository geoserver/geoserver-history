/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfsv.xml.v1_1_0;

import net.opengis.wfsv.WfsvFactory;

import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.geotools.xml.Configuration;
import org.picocontainer.MutablePicoContainer;


/**
 * Parser configuration for the http://www.opengis.net/wfsv schema.
 *
 * @generated
 */
public class WFSVConfiguration extends Configuration {
    /**
     * Creates a new configuration.
     *
     * @generated
     */
    public WFSVConfiguration(WFSConfiguration wfsConfiguration, WFSV wfsv) {
        super(wfsv);
        
        addDependency(wfsConfiguration);
    }

    protected void registerBindings(MutablePicoContainer container) {
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
