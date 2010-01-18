package org.geoserver.rest.client;

import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;

import org.geoserver.rest.client.datatypes.DataStore;
import org.geoserver.rest.client.datatypes.Workspace;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FeatureTypeConfigClientTestCase {

	private Workspace workspace;
	
	private DataStore dataStore;
	
	private FeatureTypeConfigClient client;
	
	public FeatureTypeConfigClientTestCase() throws MalformedURLException {
		
		this.workspace = new Workspace();
		this.workspace.setName("sf");
		
		this.dataStore = new DataStore();
		
		this.client = new FeatureTypeConfigClient(new GeoserverConfigClient("admin", "geoserver", new URL("http://localhost/geoserver")));
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testCreateFeatureTypeWorkspaceDataStoreFeatureType() {
		
	}

	@Test
	public final void testCreateFeatureTypeWorkspaceDataStoreFile() {
		
	}

	@Test
	public final void testDeleteFeatureType() {
		
	}

	@Test
	public final void testGetFeatureType() {
		
	}

	@Test
	public final void testGetFeatureTypes() {
		
	}

	@Test
	public final void testUpdateFeatureTypes() {
		
	}

}
