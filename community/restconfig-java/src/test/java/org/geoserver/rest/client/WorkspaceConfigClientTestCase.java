/**
 * 
 */
package org.geoserver.rest.client;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;

import junit.framework.Assert;

import org.geoserver.rest.client.datatypes.CoverageStores;
import org.geoserver.rest.client.datatypes.DataStores;
import org.geoserver.rest.client.datatypes.Workspace;
import org.geoserver.rest.client.datatypes.Workspaces;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 *
 * @author Ronak Patel
 */
public class WorkspaceConfigClientTestCase {

	private final WorkspaceConfigClient client;

	public WorkspaceConfigClientTestCase() throws IOException {

		this.client = new WorkspaceConfigClient(new GeoserverConfigClient("admin", "geoserver", new URL("http://localhost:8080/geoserver")));
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
	 * Test method for {@link org.geoserver.rest.client.WorkspaceConfigClient#createWorkspace(org.geoserver.rest.client.datatypes.Workspace)}.
	 * @throws IOException
	 */
	@Test
	public final void testCreateWorkspace() throws IOException {

		final Workspace original = new Workspace();
		original.setName("test-workspace");

		final Workspace result = this.client.createWorkspace(original);

		Assert.assertNotNull(result);
		Assert.assertEquals("test-workspace", result.getName());
		Assert.assertNull(result.getHref());
		Assert.assertNull(result.getCoverageStores());
		Assert.assertNull(result.getDataStores());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.WorkspaceConfigClient#deleteWorkspace(org.geoserver.rest.client.datatypes.Workspace)}.
	 * @throws IOException
	 */
	@Test
	public final void testDeleteWorkspace() throws IOException {

		//first create a test workspace
		final Workspace original = new Workspace();
		original.setName("test-workspace");

		final Workspace result = this.client.createWorkspace(original);

		Assert.assertNotNull(result);

		//delete the created workspace
		this.client.deleteWorkspace(result);

		//try to retrieve the workspace again...an exception is to be expected
		try {
			this.client.getWorkspace(original.getName());

			Assert.fail("Workspace " + original.getName() + " was not successfully deleted.");
		} catch (final Exception e) {

		}
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.WorkspaceConfigClient#getWorkspace(java.lang.String)}.
	 * @throws IOException
	 */
	@Test
	public final void testGetWorkspace() throws IOException {

		final Workspace result = this.client.getWorkspace("sf");

		Assert.assertNotNull(result);
		Assert.assertEquals("sf", result.getName());
		Assert.assertEquals("http://localhost:8080/geoserver/rest/workspaces/sf/coveragestores.json", result.getCoverageStores());
		Assert.assertEquals("http://localhost:8080/geoserver/rest/workspaces/sf/datastores.json", result.getDataStores());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.WorkspaceConfigClient#getWorkspaces()}.
	 * @throws IOException
	 */
	@Test
	public final void testGetWorkspaces() throws IOException {

		final Workspaces workspaces = this.client.getWorkspaces();

		Assert.assertNotNull(workspaces);
		Assert.assertEquals(7, workspaces.getWorkspace().size());

		Assert.assertEquals("it.geosolutions", workspaces.getWorkspace().get(0).getName());
		Assert.assertEquals("http://localhost:8080/geoserver/rest/workspaces/it.geosolutions.json", workspaces.getWorkspace().get(0).getHref());
		
		Assert.assertEquals("cite", workspaces.getWorkspace().get(1).getName());
		Assert.assertEquals("http://localhost:8080/geoserver/rest/workspaces/cite.json", workspaces.getWorkspace().get(1).getHref());
		
		Assert.assertEquals("tiger", workspaces.getWorkspace().get(2).getName());
		Assert.assertEquals("http://localhost:8080/geoserver/rest/workspaces/tiger.json", workspaces.getWorkspace().get(2).getHref());
		
		Assert.assertEquals("sde", workspaces.getWorkspace().get(3).getName());
		Assert.assertEquals("http://localhost:8080/geoserver/rest/workspaces/sde.json", workspaces.getWorkspace().get(3).getHref());
		
		Assert.assertEquals("topp", workspaces.getWorkspace().get(4).getName());
		Assert.assertEquals("http://localhost:8080/geoserver/rest/workspaces/topp.json", workspaces.getWorkspace().get(4).getHref());
		
		Assert.assertEquals("sf", workspaces.getWorkspace().get(5).getName());
		Assert.assertEquals("http://localhost:8080/geoserver/rest/workspaces/sf.json", workspaces.getWorkspace().get(5).getHref());
		
		Assert.assertEquals("nurc", workspaces.getWorkspace().get(6).getName());
		Assert.assertEquals("http://localhost:8080/geoserver/rest/workspaces/nurc.json", workspaces.getWorkspace().get(6).getHref());
	}
}
