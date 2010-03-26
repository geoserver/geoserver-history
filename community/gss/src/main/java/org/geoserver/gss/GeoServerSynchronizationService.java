/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.gss;

public interface GeoServerSynchronizationService {

    public CentralRevisionsType getCentralRevision(GetCentralRevisionType request);
}
