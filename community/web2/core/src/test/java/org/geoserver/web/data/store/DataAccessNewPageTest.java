/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.store;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerWicketTestSupport;
import org.geotools.data.property.PropertyDataStoreFactory;

/**
 * Test suite for {@link DataStoreConfiguration}
 * 
 * @author Gabriel Roldan
 */
public class DataAccessNewPageTest extends GeoServerWicketTestSupport {

    public void testInitCreateNewDataStoreNullWorkspaceId() {
        final String workspaceId = null;
        final String dataStoreFactoryDisplayName = "_invalid_";
        try {
            new DataAccessNewPage(workspaceId, dataStoreFactoryDisplayName);
            fail("Expected NPE on null workspaceId");
        } catch (NullPointerException e) {
            assertEquals("workspaceId can't be null", e.getMessage());
        }
    }

    public void testInitCreateNewDataStoreInvalidWorkspaceId() {
        final String workspaceId = "nonExistentWorkspaceId";
        final String dataStoreFactoryDisplayName = "_invalid_";
        try {
            new DataAccessNewPage(workspaceId, dataStoreFactoryDisplayName);
            fail("Expected IAE on non existent workspaceId");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Workspace not found. Id:"));
        }
    }

    public void testInitCreateNewDataStoreInvalidDataStoreFactoryName() {
        final Catalog catalog = getGeoServerApplication().getCatalog();

        final String workspaceId = catalog.getWorkspaces().get(0).getId();
        final String dataStoreFactoryDisplayName = "_invalid_";
        try {
            new DataAccessNewPage(workspaceId, dataStoreFactoryDisplayName);
            fail("Expected IAE on invalid datastore factory name");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Can't locate a datastore factory"));
        }
    }

    /**
     * Make sure in case the DataStore has a "namespace" parameter, its value is initialized to the
     * NameSpaceInfo one that matches the workspace
     */
    public void testInitCreateNewDataStoreSetsNamespaceParam() {
        final Catalog catalog = getGeoServerApplication().getCatalog();

        final String workspaceId = catalog.getWorkspaces().get(0).getId();
        final String dataStoreFactoryDisplayName = new PropertyDataStoreFactory().getDisplayName();

        final AbstractDataAccessPage page = new DataAccessNewPage(workspaceId,
                dataStoreFactoryDisplayName);

        final String assignedNamespace = (String) page.parametersMap.get("namespace");
        final String expectedNamespace = catalog.getNamespaceByPrefix(
                catalog.getWorkspace(workspaceId).getName()).getURI();

        assertEquals(expectedNamespace, assignedNamespace);

    }

    public void testPageRendersOnLoad() {
        final Catalog catalog = getGeoServerApplication().getCatalog();

        final WorkspaceInfo workspace = catalog.getWorkspaces().get(0);
        final String workspaceId = workspace.getId();
        final PropertyDataStoreFactory dataStoreFactory = new PropertyDataStoreFactory();
        final String dataStoreFactoryDisplayName = dataStoreFactory.getDisplayName();

        final AbstractDataAccessPage page = new DataAccessNewPage(workspaceId,
                dataStoreFactoryDisplayName);

        login();
        tester.startPage(page);
        
        tester.assertLabel("storeType", dataStoreFactoryDisplayName);
        tester.assertLabel("storeTypeDescription", dataStoreFactory.getDescription());
        tester.assertLabel("workspaceName", workspace.getName());

        // TODO: add more components assertions
    }

}
