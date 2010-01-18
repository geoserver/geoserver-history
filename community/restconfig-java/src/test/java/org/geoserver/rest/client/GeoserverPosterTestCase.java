/**
 * 
 */
package org.geoserver.rest.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import org.geoserver.rest.client.datatypes.Coverage;
import org.geoserver.rest.client.datatypes.CoverageStore;
import org.geoserver.rest.client.datatypes.DataStore;
import org.geoserver.rest.client.datatypes.FeatureType;
import org.geoserver.rest.client.datatypes.LatLonBoundingBox;
import org.geoserver.rest.client.datatypes.LayerGroup;
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
public class GeoserverPosterTestCase {

	private final Workspace coverageWorkspace;
	
	private final Workspace featureWorkspace;
	
	private final CoverageStore coverageStore;
	
	private final DataStore dataStore;
	
	private final GeoserverPoster client;
	
	public GeoserverPosterTestCase() throws MalformedURLException {
	
		this.coverageWorkspace = new Workspace();
		this.coverageWorkspace.setName("nurc");
		
		this.featureWorkspace = new Workspace();
		this.featureWorkspace.setName("topp");
		
		this.dataStore = new DataStore();
		this.dataStore.setName("states_shapefile");
		
		this.coverageStore = new CoverageStore();
		this.coverageStore.setName("img_sample2");
		
		this.client = new GeoserverPoster("admin", "geoserver", new URL("http://localhost:8080/geoserver"));
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
	 * Test method for {@link org.geoserver.rest.client.GeoserverPoster#addToLayerGroup(java.lang.String, org.geoserver.rest.client.datatypes.Coverage)}.
	 * @throws IOException 
	 */
	@Test
	public final void testAddToLayerGroupStringCoverage() throws IOException {
		
		//fetch a coverage
		final Coverage coverage = this.client.getCoverageClient().getCoverage(this.coverageWorkspace, this.coverageStore, "Pk50095"); 
		
		Assert.assertNotNull(coverage);
		
		//add the coverage to a new layer group
		this.client.addToLayerGroup("test-group", coverage);
		
		//verify the addition occurred
		final LayerGroup retrievedGroup = this.client.getLayerGroup("test-group");
		
		Assert.assertEquals("test-group", retrievedGroup.getName());
		Assert.assertEquals(1, retrievedGroup.getLayers().getLayer().size());
		Assert.assertEquals(coverage.getName(), retrievedGroup.getLayers().getLayer().get(0).getName());
		
		//delete the layer group
		this.client.getLayerGroupClient().deleteLayerGroup(retrievedGroup);
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.GeoserverPoster#addToLayerGroup(java.lang.String, org.geoserver.rest.client.datatypes.FeatureType)}.
	 * @throws IOException 
	 */
	@Test
	public final void testAddToLayerGroupStringFeatureType() throws IOException {
		
		//fetch a coverage
		final FeatureType feature = this.client.getFeatureTypeClient().getFeatureType(this.featureWorkspace, this.dataStore, "states"); 
		
		Assert.assertNotNull(feature);
		
		//add the coverage to a new layer group
		this.client.addToLayerGroup("test-group", feature);
		
		//verify the addition occurred
		final LayerGroup retrievedGroup = this.client.getLayerGroup("test-group");
		
		Assert.assertEquals("test-group", retrievedGroup.getName());
		Assert.assertEquals(1, retrievedGroup.getLayers().getLayer().size());
		Assert.assertEquals(feature.getName(), retrievedGroup.getLayers().getLayer().get(0).getName());
		
		//delete the layer group
		this.client.getLayerGroupClient().deleteLayerGroup(retrievedGroup);
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.GeoserverPoster#getCoverageClient()}.
	 */
	@Test
	public final void testGetCoverageClient() {
		
		Assert.assertNotNull(this.client.getCoverageClient());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.GeoserverPoster#getCoverageStoreClient()}.
	 */
	@Test
	public final void testGetCoverageStoreClient() {
		
		Assert.assertNotNull(this.client.getCoverageStoreClient());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.GeoserverPoster#getDataStoreClient()}.
	 */
	@Test
	public final void testGetDataStoreClient() {
		
		Assert.assertNotNull(this.client.getDataStoreClient());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.GeoserverPoster#getFeatureTypeClient()}.
	 */
	@Test
	public final void testGetFeatureTypeClient() {
		
		Assert.assertNotNull(this.client.getFeatureTypeClient());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.GeoserverPoster#getGeoserverUrl()}.
	 * @throws MalformedURLException 
	 */
	@Test
	public final void testGetGeoserverUrl() throws MalformedURLException {
		
		Assert.assertEquals(new URL("http://localhost:8080/geoserver"), this.client.getGeoserverUrl());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.GeoserverPoster#getGeoserverWFSUrl()}.
	 * @throws MalformedURLException 
	 */
	@Test
	public final void testGetGeoserverWFSUrl() throws MalformedURLException {
		
		Assert.assertEquals(new URL("http://localhost:8080/geoserver/wfs"), this.client.getGeoserverWFSUrl());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.GeoserverPoster#getGeoserverWFSUrl(org.geoserver.rest.client.datatypes.FeatureType)}.
	 * @throws IOException 
	 */
	@Test
	public final void testGetGeoserverWFSUrlFeatureType() throws IOException {
		
		//fetch a coverage
		final FeatureType feature = this.client.getFeatureTypeClient().getFeatureType(this.featureWorkspace, this.dataStore, "states"); 
		
		final LatLonBoundingBox boundingBox = feature.getLatLonBoundingBox();

		final StringBuilder urlBuilder = new StringBuilder(this.client.getGeoserverUrl().toExternalForm());
		urlBuilder.append("?REQUEST=GetFeature&VERSION=1.1.0&LAYERS=");
		urlBuilder.append(feature.getName());
		urlBuilder.append("&SRS=EPSG:4326&BBOX=");
		urlBuilder.append(boundingBox.getMinx());
		urlBuilder.append(",");
		urlBuilder.append(boundingBox.getMiny());
		urlBuilder.append(",");
		urlBuilder.append(boundingBox.getMaxx());
		urlBuilder.append(",");
		urlBuilder.append(boundingBox.getMaxy());
		urlBuilder.append("&WIDTH=");
		urlBuilder.append(800);
		urlBuilder.append("&HEIGHT=");
		urlBuilder.append(600);
		urlBuilder.append("*TRANSPARENT=TRUE&FORMAT=PNG");

		Assert.assertEquals(urlBuilder, this.client.getGeoserverWFSUrl(feature));
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.GeoserverPoster#getGeoserverWMSUrl()}.
	 * @throws MalformedURLException 
	 */
	@Test
	public final void testGetGeoserverWMSUrl() throws MalformedURLException {
		
		Assert.assertEquals(new URL("http://localhost:8080/geoserver/wms"), this.client.getGeoserverWMSUrl());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.GeoserverPoster#getGeoserverWMSUrl(org.geoserver.rest.client.datatypes.Coverage)}.
	 * @throws IOException 
	 */
	@Test
	public final void testGetGeoserverWMSUrlCoverage() throws IOException {

		//fetch a coverage
		final Coverage coverage = this.client.getCoverageClient().getCoverage(this.coverageWorkspace, this.coverageStore, ""); 
		
		final LatLonBoundingBox boundingBox = coverage.getLatLonBoundingBox();

		final StringBuilder urlBuilder = new StringBuilder(this.client.getGeoserverUrl().toExternalForm());
		urlBuilder.append("?REQUEST=GetMap&VERSION=1.1.1&LAYERS=");
		urlBuilder.append(coverage.getName());
		urlBuilder.append("&SRS=EPSG:4326&BBOX=");
		urlBuilder.append(boundingBox.getMinx());
		urlBuilder.append(",");
		urlBuilder.append(boundingBox.getMiny());
		urlBuilder.append(",");
		urlBuilder.append(boundingBox.getMaxx());
		urlBuilder.append(",");
		urlBuilder.append(boundingBox.getMaxy());
		urlBuilder.append("&WIDTH=");
		urlBuilder.append(800);
		urlBuilder.append("&HEIGHT=");
		urlBuilder.append(600);
		urlBuilder.append("*TRANSPARENT=TRUE&FORMAT=PNG");

		Assert.assertEquals(urlBuilder, this.client.getGeoserverWMSUrl(coverage));
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.GeoserverPoster#getLayerGroup(java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public final void testGetLayerGroup() throws IOException {

		//verify the addition occurred
		final LayerGroup retrievedGroup = this.client.getLayerGroup("tasmania");
		
		Assert.assertEquals("tasmania", retrievedGroup.getName());
		Assert.assertEquals(4, retrievedGroup.getLayers().getLayer().size());
		Assert.assertEquals("tasmania_state_boundaries", retrievedGroup.getLayers().getLayer().get(0).getName());
		Assert.assertEquals("tasmania_water_bodies", retrievedGroup.getLayers().getLayer().get(1).getName());
		Assert.assertEquals("tasmania_roads", retrievedGroup.getLayers().getLayer().get(2).getName());
		Assert.assertEquals("tasmania_cities", retrievedGroup.getLayers().getLayer().get(3).getName());
		
		Assert.assertEquals(143.835, retrievedGroup.getBounds().getMinx());
		Assert.assertEquals(-43.648, retrievedGroup.getBounds().getMiny());
		Assert.assertEquals(148.479, retrievedGroup.getBounds().getMaxx());
		Assert.assertEquals(-39.574, retrievedGroup.getBounds().getMaxy());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.GeoserverPoster#getLayerGroupClient()}.
	 */
	@Test
	public final void testGetLayerGroupClient() {
		
		Assert.assertNotNull(this.client.getLayerGroupClient());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.GeoserverPoster#getWorkspace(java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public final void testGetWorkspace() throws IOException {
		
		final Workspace fetchedWorkspace = this.client.getWorkspace(this.featureWorkspace.getName());
		
		Assert.assertEquals(this.featureWorkspace.getName(), fetchedWorkspace.getName());
		Assert.assertEquals(this.featureWorkspace.getHref(), fetchedWorkspace.getHref());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.GeoserverPoster#getWorkspaceClient()}.
	 */
	@Test
	public final void testGetWorkspaceClient() {
		
		Assert.assertNotNull(this.client.getWorkspaceClient());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.GeoserverPoster#postCoverage(java.lang.String, java.io.File)}.
	 */
	@Test
	public final void testPostCoverage() {
		
		
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.GeoserverPoster#postFeature(java.lang.String, java.io.File)}.
	 */
	@Test
	public final void testPostFeature() {
		
		
	}

}
