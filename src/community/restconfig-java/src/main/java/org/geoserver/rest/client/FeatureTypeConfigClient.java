package org.geoserver.rest.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.ws.rs.core.MediaType;

import org.geoserver.rest.client.datatypes.Coverage;
import org.geoserver.rest.client.datatypes.CoverageStore;
import org.geoserver.rest.client.datatypes.DataStore;
import org.geoserver.rest.client.datatypes.FeatureType;
import org.geoserver.rest.client.datatypes.FeatureTypes;
import org.geoserver.rest.client.datatypes.Workspace;
import org.geoserver.rest.client.jsonholder.FeatureTypeJSONHolder;
import org.geoserver.rest.client.jsonholder.FeatureTypesJSONHolder;

import com.sun.jersey.api.client.ClientResponse;

/**
 * The Geoserver Feature Configuration client object that allows users to manipulate {@link FeatureType}s on the connected
 * Geoserver instance. Data Stores are used to manage holdings of {@link FeatureType}s.
 *
 * @author Ronak Patel
 */
public class FeatureTypeConfigClient extends ConfigClient {

	/**
	 * The URI for manipulating a single {@link FeatureType}
	 */
	private static final String FEATURE_URI = "/rest/workspaces/{ws}/datastores/{ds}/featuretypes/{ft}";

	/**
	 * The URI for the feature file upload REST endpoint
	 */
	private static final String UPLOAD_URI = "/rest/workspaces/{ws}/datastores/{ds}/file.{extension}";

	/**
	 * The URI for manipulating a collection of {@link FeatureType}s
	 */
	private static final String URI = "/rest/workspaces/{ws}/datastores/{ds}/featuretypes";

	/**
	 * Creates a Geoserver interaction object
	 * 
	 * @param geoserverClient
	 *            the Geoserver Client Configuration
	 */
	public FeatureTypeConfigClient(final GeoserverConfigClient geoserverClient) {
		super(geoserverClient);
	}

	/**
	 * Creates a {@link FeatureType} on the server with the specified contents.
	 * 
	 * @param workspace
	 *            the {@link Workspace} to add the {@link FeatureType} to
	 * @param dataStore
	 *            the exact {@link DataStore} to modify
	 * @param feature
	 *            the {@link FeatureType} file to upload to the server
	 * @return the created {@link FeatureType}
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	public FeatureType createFeatureType(final Workspace workspace, final DataStore dataStore, final FeatureType feature) throws IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + FeatureTypeConfigClient.URI;
		uri = uri.replace("{ws}", workspace.getName());
		uri = uri.replace("{ds}", dataStore.getName());

		this.createEntity(uri, feature);

		return this.getFeatureType(workspace, dataStore, feature.getName());
	}

	/**
	 * Creates a {@link FeatureType} on the server by uploading the specified
	 * {@link FeatureType} {@link File} to the server. The file should be a ESRI Shapefile.
	 * No other formats are supported at this time.
	 * 
	 * @param workspace
	 *            the {@link Workspace} to add the {@link FeatureType} to
	 * @param dataStore
	 *            the exact {@link DataStore} to modify
	 * @param feature
	 *            the {@link FeatureType} file to upload to the server
	 * @return the created {@link FeatureType}
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	public FeatureType createFeatureType(final Workspace workspace, final DataStore dataStore, final File feature) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + FeatureTypeConfigClient.UPLOAD_URI;
		uri = uri.replace("{ws}", workspace.getName());
		uri = uri.replace("{ds}", dataStore.getName());

		if(feature.exists() == false) {
			throw new FileNotFoundException("File " + feature.getAbsolutePath() + " doesn't exist!!");
		}

		MediaType contentType = MediaType.valueOf("application/zip");
		final String featureName = feature.getName();

		if(featureName.endsWith(".zip")) {
			contentType = MediaType.valueOf("application/zip");
			uri = uri.replace("{extension}", "shp");
		} else {
			if(featureName.endsWith(".gml")) {
				contentType = MediaType.APPLICATION_OCTET_STREAM_TYPE;
				uri = uri.replace("{extension}", "gml");
			} else {
				throw new IllegalArgumentException("Unknown File Type: " + feature.getName());
			}
		}

		this.createEntity(uri, feature, contentType);

		return this.getFeatureType(workspace, dataStore, feature.getName());
	}

	/**
	 * Deletes the specified {@link FeatureType} from the specified
	 * {@link CoverageStore} from the specified {@link Workspace}.
	 * 
	 * @param workspace
	 *            the workspace to modify
	 * @param dataStore
	 *            the coverage store to modify
	 * @param feature
	 *            the exact {@link FeatureType} that should be removed
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	public void deleteFeatureType(final Workspace workspace, final DataStore dataStore, final FeatureType feature) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + FeatureTypeConfigClient.FEATURE_URI;
		uri = uri.replace("{ws}", workspace.getName());
		uri = uri.replace("{ds}", dataStore.getName());
		uri = uri.replace("{ft}", feature.getName());

		this.deleteEntity(uri);
	}

	/**
	 * Retrieves the {@link Coverage} with the specified <code>name</code> from
	 * the server.
	 * 
	 * @param workspace
	 *            the {@link Workspace} that the {@link CoverageStore} is
	 *            located in
	 * @param coverageStore
	 *            the {@link CoverageStore} that has the specified
	 *            {@link Coverage}
	 * @param name
	 *            the name of the {@link Coverage} that should be retrieved
	 * @return the retrieved {@link Coverage}
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	public FeatureType getFeatureType(final Workspace workspace, final DataStore dataStore, final String name) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + FeatureTypeConfigClient.URI;
		uri = uri.replace("{ws}", workspace.getName());
		uri = uri.replace("{ds}", dataStore.getName());
		uri = uri.replace("{ft}", name);

		uri += "?list=all";

		final ClientResponse response = this.getEntity(uri);

		final FeatureTypesJSONHolder featureHolder = response.getEntity(FeatureTypesJSONHolder.class);

		return featureHolder.getFeatureTypes().getFeatureType().get(0);
	}

	/**
	 * Retrieves the collection of {@link FeatureType}s from the server located
	 * under the specified {@link Workspace} and {@link DataStore}.
	 * 
	 * @param workspace
	 *            the {@link Workspace} that the {@link DataStore} is
	 *            located in
	 * @param dataStore
	 *            the {@link DataStore} in question
	 * @return the list of {@link FeatureType}s
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	public FeatureTypes getFeatureTypes(final Workspace workspace, final DataStore dataStore) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + FeatureTypeConfigClient.URI;
		uri = uri.replace("{ws}", workspace.getName());
		uri = uri.replace("{ds}", dataStore.getName());

		final ClientResponse response = this.getEntity(uri);

		final FeatureTypesJSONHolder featuresHolder = response.getEntity(FeatureTypesJSONHolder.class);

		return featuresHolder.getFeatureTypes();
	}

	/**
	 * Updates the properties of the specified {@link FeatureType} at the server
	 * 
	 * @param workspace
	 *            the {@link Workspace} that the {@link DataStore} is
	 *            located in
	 * @param dataStore
	 *            the {@link DataStore} that has the specified
	 *            {@link Coverage}
	 * @param coverage
	 *            the {@link FeatureType} to update
	 * @return the updated {@link FeatureType}
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	public FeatureType updateFeatureTypes(final Workspace workspace, final DataStore dataStore, final FeatureType feature) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + FeatureTypeConfigClient.FEATURE_URI;
		uri = uri.replace("{ws}", workspace.getName());
		uri = uri.replace("{ds}", dataStore.getName());
		uri = uri.replace("{ft}", feature.getName());

		this.updateEntity(uri, new FeatureTypeJSONHolder(feature));

		return feature;
	}
}
