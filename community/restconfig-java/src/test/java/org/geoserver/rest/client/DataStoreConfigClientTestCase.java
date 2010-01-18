/**
 * 
 */
package org.geoserver.rest.client;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.geoserver.rest.client.datatypes.ConnectionParameters;
import org.geoserver.rest.client.datatypes.CoverageStore;
import org.geoserver.rest.client.datatypes.DataStore;
import org.geoserver.rest.client.datatypes.DataStores;
import org.geoserver.rest.client.datatypes.Namespace;
import org.geoserver.rest.client.datatypes.Url;
import org.geoserver.rest.client.datatypes.Workspace;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 *
 * @author Ronak Patel
 */
public class DataStoreConfigClientTestCase {

	private DataStoreConfigClient client;
	
	private Workspace workspace;
	
	public DataStoreConfigClientTestCase() throws MalformedURLException {
		
		this.workspace = new Workspace();
		this.workspace.setName("topp");
		
		this.client = new DataStoreConfigClient(new GeoserverConfigClient("admin", "geoserver", new URL("http://localhost:8080/geoserver")));
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.DataStoreConfigClient#createDataStore(org.geoserver.rest.client.datatypes.Workspace, org.geoserver.rest.client.datatypes.DataStore)}.
	 * @throws IOException 
	 */
	@Test
	public final void testCreateDataStore() throws IOException {
		
		File file = new File("test-data/usa.zip");

		final Namespace namespace = new Namespace();
		namespace.setString(this.workspace.getName());
		
		final Url url = new Url();
		url.setString("file:" + file.getAbsolutePath().replace('\\', '/'));
		
		final ConnectionParameters params = new ConnectionParameters();
		params.setNamespace(namespace);
		params.setUrl(url);
		
		final DataStore store = new DataStore();
		store.setName("newDataStore");
		store.setConnectionParameters(params);
		store.setEnabled(true);

		//create the store
		this.client.createDataStore(this.workspace, store);
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.DataStoreConfigClient#deleteDataStore(org.geoserver.rest.client.datatypes.Workspace, org.geoserver.rest.client.datatypes.DataStore)}.
	 * @throws IOException 
	 */
	@Test
	public final void testDeleteDataStore() throws IOException {
		
		final DataStore store = new DataStore();
		store.setName("newDataStore");
		
		this.client.deleteDataStore(this.workspace, store);
		
		try {
			this.client.getDataStore(this.workspace, store.getName());
			
			Assert.fail("The datastore: " + store.getName() + " was not deleted properly!");
		} catch (Exception e) {
			
		}
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.DataStoreConfigClient#getDataStore(org.geoserver.rest.client.datatypes.Workspace, java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public final void testGetDataStore() throws IOException {
		
		final DataStore store = this.client.getDataStore(this.workspace, "states_shapefile");
		
		Assert.assertEquals("states_shapefile", store.getName());
		Assert.assertTrue(store.isEnabled());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.DataStoreConfigClient#getDataStores(org.geoserver.rest.client.datatypes.Workspace, java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public final void testGetDataStores() throws IOException {
		
		final DataStores store = this.client.getDataStores(this.workspace);
		
		Assert.assertEquals(2, store.getDataStore().size());
		Assert.assertEquals("states_shapefile", store.getDataStore().get(0).getName());
		Assert.assertEquals("taz_shapes", store.getDataStore().get(1).getName());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.DataStoreConfigClient#updateDataStore(org.geoserver.rest.client.datatypes.Workspace, org.geoserver.rest.client.datatypes.DataStore)}.
	 * @throws IOException 
	 */
	@Test
	public final void testUpdateDataStore() throws IOException {
		
		final DataStore store = new DataStore();
		store.setName("states_shapefile");
		store.setEnabled(false);
		
		final DataStore updated = this.client.updateDataStore(this.workspace, store);
		
		Assert.assertFalse(updated.isEnabled());
		
		updated.setEnabled(true);
		
		this.client.updateDataStore(this.workspace, updated);
	}

}
