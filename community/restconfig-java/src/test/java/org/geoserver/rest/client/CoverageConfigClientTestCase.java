/**
 * 
 */
package org.geoserver.rest.client;

import java.net.MalformedURLException;
import java.net.URL;

import org.geoserver.rest.client.datatypes.CoverageStore;
import org.geoserver.rest.client.datatypes.Workspace;
import org.junit.Test;

/**
 *
 *
 * @author Ronak Patel
 */
public class CoverageConfigClientTestCase {

	private final CoverageConfigClient client;
	
	private final Workspace workspace;
	
	private final CoverageStore store;
	
	public CoverageConfigClientTestCase() throws MalformedURLException {
		
		this.client = new CoverageConfigClient(new GeoserverConfigClient("admin", "geoserver", new URL("http://localhost:8080/geoserver")));
		
		this.workspace = new Workspace();
		this.workspace.setName("nurc");
		
		this.store = new CoverageStore();
		this.store.setName("arcGridSample");
	}
	
	/**
	 * Test method for {@link org.geoserver.rest.client.CoverageConfigClient#createCoverage(org.geoserver.rest.client.datatypes.Workspace, org.geoserver.rest.client.datatypes.CoverageStore, java.io.File)}.
	 */
	@Test
	public final void testCreateCoverage() {

		
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.CoverageConfigClient#deleteCoverage(org.geoserver.rest.client.datatypes.Workspace, org.geoserver.rest.client.datatypes.CoverageStore, org.geoserver.rest.client.datatypes.Coverage)}.
	 */
	@Test
	public final void testDeleteCoverage() {

	}

	/**
	 * Test method for {@link org.geoserver.rest.client.CoverageConfigClient#getCoverage(org.geoserver.rest.client.datatypes.Workspace, org.geoserver.rest.client.datatypes.CoverageStore, java.lang.String)}.
	 */
	@Test
	public final void testGetCoverage() {

	}

	/**
	 * Test method for {@link org.geoserver.rest.client.CoverageConfigClient#getCoverages(org.geoserver.rest.client.datatypes.Workspace, org.geoserver.rest.client.datatypes.CoverageStore)}.
	 */
	@Test
	public final void testGetCoverages() {

	}

	/**
	 * Test method for {@link org.geoserver.rest.client.CoverageConfigClient#updateCoverage(org.geoserver.rest.client.datatypes.Workspace, org.geoserver.rest.client.datatypes.CoverageStore, org.geoserver.rest.client.datatypes.Coverage)}.
	 */
	@Test
	public final void testUpdateCoverage() {

	}

}
