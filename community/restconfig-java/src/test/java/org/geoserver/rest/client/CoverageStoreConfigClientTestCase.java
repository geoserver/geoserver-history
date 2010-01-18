/**
 * 
 */
package org.geoserver.rest.client;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

import org.geoserver.rest.client.datatypes.CoverageStore;
import org.geoserver.rest.client.datatypes.CoverageStoreType;
import org.geoserver.rest.client.datatypes.CoverageStores;
import org.geoserver.rest.client.datatypes.Workspace;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 *
 * @author Ronak Patel
 */
public class CoverageStoreConfigClientTestCase {

	private final CoverageStoreConfigClient client;
	
	private final Workspace workspace;
	
	public CoverageStoreConfigClientTestCase() throws MalformedURLException {
		
		this.client = new CoverageStoreConfigClient(new GeoserverConfigClient("admin", "geoserver", new URL("http://localhost:8080/geoserver")));
		
		this.workspace = new Workspace();
		this.workspace.setName("sf");
	}
	
	/**
	 * Test method for {@link org.geoserver.rest.client.CoverageStoreConfigClient#createCoverageStore(org.geoserver.rest.client.datatypes.Workspace, org.geoserver.rest.client.datatypes.CoverageStore)}.
	 * @throws IOException 
	 * @throws RemoteException 
	 */
	@Test
	public final void testCreateCoverageStore() throws RemoteException, IOException {

		File file = new File("test-data/usa.png");

		final CoverageStore store = new CoverageStore();
		store.setName("newCoverageStore");
		store.setType(CoverageStoreType.WorldImage);
		store.setUrl("file://" + file.getAbsolutePath().replace('\\', '/'));

		//create the store
		final CoverageStore created = this.client.createCoverageStore(this.workspace, store);
		
		//remove the store
		this.client.deleteCoverageStore(this.workspace, created);
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.CoverageStoreConfigClient#deleteCoverageStore(org.geoserver.rest.client.datatypes.Workspace, org.geoserver.rest.client.datatypes.CoverageStore)}.
	 */
	@Test
	public final void testDeleteCoverageStore() throws IOException {
		
		File file = new File("test-data/usa.png");

		final CoverageStore store = new CoverageStore();
		store.setName("newCoverageStore");
		store.setType(CoverageStoreType.WorldImage);
		store.setUrl("file://" + file.getAbsolutePath().replace('\\', '/'));

		//create the store
		final CoverageStore created = this.client.createCoverageStore(this.workspace, store);
		
		//remove the store
		this.client.deleteCoverageStore(this.workspace, created);
		
		try {
			this.client.getCoverageStore(this.workspace, created.getName());
			
			Assert.fail("The coverage store " + created.getName() + " was not successfully deleted.");
		} catch (Exception e) {
			
		}
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.CoverageStoreConfigClient#getCoverageStore(org.geoserver.rest.client.datatypes.Workspace, java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public final void testGetCoverageStore() throws IOException {
		
		final CoverageStore store = this.client.getCoverageStore(this.workspace, "sfdem");
		
		Assert.assertEquals("sfdem", store.getName());
		Assert.assertNull(store.getHref());
		Assert.assertEquals("file:data/sf/sfdem.tif", store.getUrl());
		Assert.assertEquals("sf", store.getWorkspace().getName());
		Assert.assertEquals(CoverageStoreType.GeoTIFF, store.getType());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.CoverageStoreConfigClient#getCoverageStores(org.geoserver.rest.client.datatypes.Workspace)}.
	 * @throws IOException 
	 */
	@Test
	public final void testGetCoverageStores() throws IOException {
		
		final CoverageStores stores = this.client.getCoverageStores(this.workspace);
		
		final List<CoverageStore> storeList = stores.getCoverageStore();
		
		Assert.assertEquals(1, storeList.size());
		Assert.assertEquals("sfdem", storeList.get(0).getName());
		Assert.assertNotNull(storeList.get(0).getHref());
		Assert.assertNull(storeList.get(0).getUrl());
		Assert.assertNull(storeList.get(0).getWorkspace());
		Assert.assertNull(storeList.get(0).getType());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.CoverageStoreConfigClient#updateCoverageStore(org.geoserver.rest.client.datatypes.Workspace, org.geoserver.rest.client.datatypes.CoverageStore)}.
	 * @throws IOException 
	 */
	@Test
	public final void testUpdateCoverageStore() throws IOException {
		
		final CoverageStore store = this.client.getCoverageStore(this.workspace, "sfdem");
		store.setEnabled(false);
		
		final CoverageStore updated = this.client.updateCoverageStore(this.workspace, store);
		
		//verify that the update was made
		Assert.assertFalse(updated.isEnabled());
		
		updated.setEnabled(true);
		
		this.client.updateCoverageStore(this.workspace, updated);
	}
}
