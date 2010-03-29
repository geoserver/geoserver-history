/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.gss;

/**
 * The synchronisation service, represents the calls that can be made to a GSS Unit (remote node)
 * 
 * @author aaime
 */
public interface GeoServerSynchronizationService {

    /**
     * Grabs the last central revision known to this Unit
     * 
     * @param request
     * @return
     */
    public CentralRevisionsType getCentralRevision(GetCentralRevisionType request);
}
