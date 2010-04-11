package org.geoserver.rest.client;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.ws.rs.core.MediaType;

import org.geoserver.rest.client.datatypes.ConnectionParameters;
import org.geoserver.rest.client.datatypes.Coverage;
import org.geoserver.rest.client.datatypes.CoverageStore;
import org.geoserver.rest.client.datatypes.CoverageStoreType;
import org.geoserver.rest.client.datatypes.DataStore;
import org.geoserver.rest.client.datatypes.FeatureType;
import org.geoserver.rest.client.datatypes.LatLonBoundingBox;
import org.geoserver.rest.client.datatypes.Layer;
import org.geoserver.rest.client.datatypes.LayerGroup;
import org.geoserver.rest.client.datatypes.Namespace;
import org.geoserver.rest.client.datatypes.Style;
import org.geoserver.rest.client.datatypes.Styles;
import org.geoserver.rest.client.datatypes.Workspace;

/**
 * The core class of the Geoserver REST Configuration SDK that allows users to interact
 * with the administration/configuration Geoserver endpoint.
 * 
 * Users of this library should interact with the SDK through this class.
 * 
 * @author Ronak Patel
 */
public class GeoserverPoster {

	/**
	 * The {@link Coverage} manipulation client SDK object
	 */
	private final CoverageConfigClient coverageClient;

	/**
	 * The {@link CoverageStore} manipulation client SDK object
	 */
	private final CoverageStoreConfigClient coverageStoreClient;

	/**
	 * The {@link DataStore} manipulation client SDK object
	 */
	private final DataStoreConfigClient dataStoreClient;

	/**
	 * The {@link FeatureType} manipulation client SDK object
	 */
	private final FeatureTypeConfigClient featureTypeClient;

	/**
	 * The configured Geoserver URL
	 */
	private final URL geoserverUrl;

	/**
	 * The {@link LayerGroup} manipulation client SDK object
	 */
	private final LayerGroupConfigClient layerGroupClient;

	/**
	 * The {@link Workspace} manipulation client SDK object
	 */
	private final WorkspaceConfigClient workspaceClient;

	/**
	 * Creates a default SDK object, thoroughly configuring the SDK for full
	 * operation use
	 * 
	 * @param username
	 *            the Geoserver configuration admin username
	 * @param password
	 *            the Geoserver configuration admin password
	 * @param geoserverUrl
	 *            the Geoserver URL
	 */
	public GeoserverPoster(final String username, final String password, final URL geoserverUrl) {

		this.geoserverUrl = geoserverUrl;

		final GeoserverConfigClient geoserverClient = new GeoserverConfigClient(username, password, this.geoserverUrl);

		this.workspaceClient = new WorkspaceConfigClient(geoserverClient);
		this.coverageStoreClient = new CoverageStoreConfigClient(geoserverClient);
		this.coverageClient = new CoverageConfigClient(geoserverClient);
		this.dataStoreClient = new DataStoreConfigClient(geoserverClient);
		this.layerGroupClient = new LayerGroupConfigClient(geoserverClient);
		this.featureTypeClient = new FeatureTypeConfigClient(geoserverClient);
	}

	/**
	 * Adds a {@link Coverage} to a {@link LayerGroup} by creating the
	 * {@link Layer}s and possibly the {@link LayerGroup} on the server.
	 * 
	 * @param group
	 *            the group to add the {@link Layer}s to
	 * @param coverage
	 *            the {@link Coverage} to add to the group
	 * @throws IOException
	 *             if a communication error occurred while contacting the server
	 */
	public void addToLayerGroup(final String group, final Coverage coverage) throws IOException {

		//set the layer style
		final Style style = new Style();
		style.setName("raster");

		this.addToLayerGroup(group, coverage.getName(), style);
	}

	/**
	 * Adds a {@link FeatureType} to a {@link LayerGroup} by creating the
	 * {@link Layer}s and possibly the {@link LayerGroup} on the server.
	 * 
	 * @param group
	 *            the group to add the {@link Layer}s to
	 * @param feature
	 *            the {@link FeatureType} to add to the group
	 * @throws IOException
	 *             if a communication error occurred while contacting the server
	 */
	public void addToLayerGroup(final String group, final FeatureType feature) throws IOException {

		//set the layer style
		final Style style = new Style();
		style.setName("polygon");

		this.addToLayerGroup(group, feature.getName(), style);
	}

	/**
	 * Adds an arbitrary {@link Layer} to a {@link LayerGroup} by creating the
	 * {@link Layer}s and possibly the {@link LayerGroup} on the server.
	 * 
	 * @param group
	 *            the group to add the {@link Layer}s to
	 * @param layerName
	 *            the name of the {@link Layer} to add to the group
	 * @param style
	 *            the style to apply to the {@link Layer}
	 * @throws IOException
	 *             if a communication error occurred while contacting the server
	 */
	private void addToLayerGroup(final String group, final String layerName, final Style style) throws RemoteException, IOException {

		//create the layer representation
		final Layer layer = new Layer();
		layer.setName(layerName);

		//set the layer style
		final Styles styles = new Styles();
		styles.getStyle().add(style);
		layer.setStyles(styles);

		//now fetch and update the existing layer group
		LayerGroup layerGroup;

		try {
			layerGroup = this.getLayerGroup(group);

			System.out.println("We have layer group: " + layerGroup.getName());
			
			//check to see if this layer already exists inside of Geoserver
			boolean exists = false;

			for (final Layer existingLayer : layerGroup.getLayers().getLayer()) {

				if(existingLayer.getName().equals(layer.getName())) {
					exists = true;
				}
			}

			//if the layer does not already exist, then let's try to recreate it
			if(exists == false) {
				//the number of layers and styles must match
				layerGroup.getLayers().getLayer().add(layer);
				layerGroup.getStyles().getStyle().add(style);

				//update the layer group
				this.layerGroupClient.updateLayerGroup(layerGroup);
			}

		} catch (final Exception e) {

			//if the layer group didn't exist...we will create it from scratch
			layerGroup = new LayerGroup();
			layerGroup.setName(group);
			layerGroup.getLayers().getLayer().add(layer);

			layerGroup.setStyles(styles);

			this.layerGroupClient.createLayerGroup(layerGroup);
		}
	}

	/**
	 * Returns the {@link Coverage} manipulation client SDK object
	 * 
	 * @return the {@link Coverage} manipulation client SDK object
	 */
	public CoverageConfigClient getCoverageClient() {
		return this.coverageClient;
	}

	/**
	 * Returns and possibly creates a {@link CoverageStore} named
	 * <code>coverageStoreName</code> from the Geoserver server
	 * 
	 * @param workspace
	 *            an existing {@link Workspace}
	 * @param coverageStoreName
	 *            the name of the {@link CoverageStore} to retrieve or create if
	 *            it does not exist
	 * @return the retrieved {@link CoverageStore}
	 * @throws IOException
	 *             if Geoserver could not be contacted
	 */
	private CoverageStore getCoverageStore(final Workspace workspace, final String coverageStoreName, final CoverageStoreType type) throws IOException {

		try {
			return this.coverageStoreClient.getCoverageStore(workspace, coverageStoreName);
		} catch (final IOException e) {
			final CoverageStore createdStore = new CoverageStore();
			createdStore.setName(coverageStoreName);
			createdStore.setEnabled(true);
			createdStore.setWorkspace(workspace);
			createdStore.setType(type);

			//create the coverage store
			this.coverageStoreClient.createCoverageStore(workspace, createdStore);

			return createdStore;
		}
	}

	/**
	 * Returns the {@link CoverageStore} manipulation client SDK object
	 * 
	 * @return the {@link CoverageStore} manipulation client SDK object
	 */
	public CoverageStoreConfigClient getCoverageStoreClient() {
		return this.coverageStoreClient;
	}

	/**
	 * Retrieves the {@link DataStore} that exists under the specified
	 * {@link Workspace} named <code>dataStoreName</code>
	 * 
	 * @param workspace
	 *            the {@link Workspace} that the {@link DataStore} is to be
	 *            organized under
	 * @param dataStoreName
	 *            the name of the {@link DataStore} to retrieve
	 * @return the {@link DataStore} that exists under the specified
	 *         {@link Workspace} named <code>dataStoreName</code>
	 * @throws IOException
	 *             if Geoserver could not be contacted
	 */
	private DataStore getDataStore(final Workspace workspace, final String dataStoreName) throws IOException {

		try {
			return this.dataStoreClient.getDataStore(workspace, dataStoreName);
		} catch (final IOException e) {

			final DataStore createdStore = new DataStore();
			createdStore.setName(dataStoreName);
			createdStore.setEnabled(true);
			createdStore.setWorkspace(workspace);

			final ConnectionParameters parameters = new ConnectionParameters();

			final Namespace namespace = new Namespace();
			namespace.setString(dataStoreName);
			namespace.setNamespace("http://" + dataStoreName);
			parameters.setNamespace(namespace);

			createdStore.setConnectionParameters(parameters);

			//create the datastore
			return createdStore;
		}
	}

	/**
	 * Returns the {@link DataStore} client SDK object
	 * 
	 * @return the {@link DataStore} client SDK object
	 */
	public DataStoreConfigClient getDataStoreClient() {
		return this.dataStoreClient;
	}

	/**
	 * Returns the {@link FeatureType} client SDK object
	 * 
	 * @return the {@link FeatureType} client SDK object
	 */
	public FeatureTypeConfigClient getFeatureTypeClient() {
		return this.featureTypeClient;
	}

	/**
	 * Returns the configured Geoserver URL
	 * 
	 * @return the configured Geoserver URL
	 */
	public URL getGeoserverUrl() {
		return this.geoserverUrl;
	}

	/**
	 * Returns the configured WFS service address
	 * 
	 * @return the configured WFS service address
	 * @throws MalformedURLException
	 *             if the WFS service address could not be properly specified
	 */
	public URL getGeoserverWFSUrl() throws MalformedURLException {

		return new URL(this.geoserverUrl.toExternalForm() + "/wfs");
	}

	/**
	 * Returns a WFS URL for an existing {@link FeatureType} hosted at this
	 * Geoserver server
	 * 
	 * @param feature
	 *            an existing {@link FeatureType}
	 * @return a WFS URL for an existing {@link FeatureType} hosted at this
	 *         Geoserver server
	 * @throws MalformedURLException
	 *             if the WFS service address could not be properly specified
	 */
	public URL getGeoserverWFSUrl(final FeatureType feature) throws MalformedURLException {

		final LatLonBoundingBox boundingBox = feature.getLatLonBoundingBox();

		final StringBuilder urlBuilder = new StringBuilder(this.getGeoserverWMSUrl().toExternalForm());
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

		return new URL(urlBuilder.toString());
	}

	/**
	 * Returns the configured WMS service address
	 * 
	 * @return the configured WMS service address
	 * @throws MalformedURLException
	 *             if the WMS service address could not be properly specified
	 */
	public URL getGeoserverWMSUrl() throws MalformedURLException {

		return new URL(this.geoserverUrl.toExternalForm() + "/wms");
	}

	/**
	 * Returns a WMS URL for an existing {@link Coverage} hosted at this
	 * Geoserver server
	 * 
	 * @param coverage
	 *            an existing {@link Coverage}
	 * @return a WMS URL for an existing {@link Coverage} hosted at this
	 *         Geoserver server
	 * @throws MalformedURLException
	 *             if the created URL is malformed due to an invalid
	 *             {@link Coverage}
	 */
	public URL getGeoserverWMSUrl(final Coverage coverage) throws MalformedURLException {

		final LatLonBoundingBox boundingBox = coverage.getLatLonBoundingBox();

		final StringBuilder urlBuilder = new StringBuilder(this.getGeoserverWMSUrl().toExternalForm());
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

		return new URL(urlBuilder.toString());
	}

	/**
	 * Returns the {@link LayerGroup} whose name matches <code>name</code>
	 * 
	 * @param name
	 *            the name of a {@link LayerGroup} to be retrieved
	 * @return the {@link LayerGroup} whose name matches <code>name</code>
	 * @throws IOException
	 *             if Geoserver could not properly be contacted
	 */
	public LayerGroup getLayerGroup(final String name) throws IOException {

		if((name == null) || name.length() == 0) {
			throw new IllegalArgumentException("Unknown Layer Group: " + name);
		}

		return this.layerGroupClient.getLayerGroup(name);
	}

	/**
	 * Returns the {@link LayerGroup} SDK Manipulation Object
	 * 
	 * @return the {@link LayerGroup} SDK Manipulation Object
	 */
	public LayerGroupConfigClient getLayerGroupClient() {
		return this.layerGroupClient;
	}

	/**
	 * Retrieves the {@link Workspace} on Geoserver matching the specified
	 * <code>workspaceName</code>. If no {@link Workspace} by this name exists,
	 * it is created on the server and returned.
	 * 
	 * @param workspaceName
	 *            the name of a workspace to create on the server
	 * @return the created/retrieved {@link Workspace}
	 * @throws IOException
	 *             if Geoserver could not properly be contacted
	 */
	public Workspace getWorkspace(final String workspaceName) throws IOException {

		if((workspaceName == null) || workspaceName.length() == 0) {
			throw new IllegalArgumentException("Unknown workspace: " + workspaceName);
		}

		Workspace workspace;

		try {
			workspace = this.workspaceClient.getWorkspace(workspaceName);
		} catch (final IOException e) {
			workspace = new Workspace();
			workspace.setName(workspaceName);

			this.workspaceClient.createWorkspace(workspace);
		}

		return workspace;
	}

	/**
	 * Returns the {@link Workspace} SDK Manipulation Object
	 * 
	 * @return the {@link Workspace} SDK Manipulation Object
	 */
	public WorkspaceConfigClient getWorkspaceClient() {
		return this.workspaceClient;
	}

	/**
	 * Posts a {@link Coverage} to Geoserver using the underlying SDK. This is
	 * the preferred method of uploading {@link Coverage} files.
	 * 
	 * @param workspaceName
	 *            the name of the workspace to contain the new {@link Coverage}.
	 *            The {@link Workspace} will be automatically created if it does
	 *            not already exist.
	 * @param coverage
	 *            the GeoTIFF or ESRI WorldImage file to upload
	 * @return a {@link Coverage} representation of the uploaded file
	 * @throws IOException if a communication error occurred while contacting the server
	 */
	public Coverage postCoverage(final String workspaceName, final File coverage) throws IOException {

		//get Workspace
		final Workspace createdWorkspace = this.getWorkspace(workspaceName);

		//the coverage store name cannot have a . in it...so remove it
		String storeName = coverage.getName();
		storeName = storeName.replace(".", "");

		final CoverageStoreType type;
		
		final String coverageName = coverage.getName();
		if(coverageName.endsWith(".tif") || coverageName.endsWith(".tiff")) {
			type = CoverageStoreType.GeoTIFF;
		} else {
			if(coverageName.endsWith(".png") || coverageName.endsWith("jpeg") || coverageName.endsWith("jpg")) {
				type = CoverageStoreType.WorldImage;
			} else {
				throw new IllegalArgumentException("Unknown File Type: " + coverage.getName());
			}
		}
		
		//get Coverage Store
		final CoverageStore store = this.getCoverageStore(createdWorkspace, storeName, type);

		return this.coverageClient.createCoverage(createdWorkspace, store, coverage);
	}

	/**
	 * Posts a {@link FeatureType} to Geoserver using the underlying SDK. This
	 * is the preferred method of uploading {@link FeatureType} files.
	 * 
	 * @param workspaceName
	 *            the name of the workspace to contain the new
	 *            {@link FeatureType}. The {@link Workspace} will be
	 *            automatically created if it does not already exist.
	 * @param feature
	 *            the ESRI Shapefile file to upload
	 * @return a {@link FeatureType} representation of the uploaded file
	 * @throws IOException
	 *             if a communication error occurred while contacting the server
	 */
	public FeatureType postFeature(final String workspaceName, final File feature) throws IOException {

		//get Workspace
		final Workspace workspace = this.getWorkspace(workspaceName);

		//the feature store name cannot have a . in it...so remove it
		String featureName = feature.getName();
		featureName = featureName.replace(".", "");

		//get Data Store
		final DataStore store = this.getDataStore(workspace, featureName);

		return this.featureTypeClient.createFeatureType(workspace, store, feature);
	}
}
