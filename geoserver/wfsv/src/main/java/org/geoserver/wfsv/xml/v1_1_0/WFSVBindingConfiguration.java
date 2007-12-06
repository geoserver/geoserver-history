/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfsv.xml.v1_1_0;

import org.geoserver.wfs.xml.v1_1_0.WFSBindingConfiguration;
import org.picocontainer.MutablePicoContainer;


/**
 * Binding configuration for the http://www.opengis.net/wfsv schema.
 *
 */
public final class WFSVBindingConfiguration extends WFSBindingConfiguration {
    /**
     * @generated modifiable
     */
    public void configure(MutablePicoContainer container) {
        super.configure(container);

        //Types
        container.registerComponentImplementation(WFSV.DifferenceQueryType,
            DifferenceQueryTypeBinding.class);
        container.registerComponentImplementation(WFSV.GetDiffType,
            GetDiffTypeBinding.class);
        container.registerComponentImplementation(WFSV.GetLogType,
            GetLogTypeBinding.class);
        container.registerComponentImplementation(WFSV.RollbackType,
            RollbackTypeBinding.class);
        container.registerComponentImplementation(WFSV.VersionedDeleteElementType,
            VersionedDeleteElementTypeBinding.class);
        container.registerComponentImplementation(WFSV.VersionedUpdateElementType,
            VersionedUpdateElementTypeBinding.class);
    }
}
