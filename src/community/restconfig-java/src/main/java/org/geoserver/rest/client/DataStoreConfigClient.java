package org.geoserver.rest.client;

import java.io.IOException;
import java.rmi.RemoteException;

import org.geoserver.rest.client.datatypes.DataStore;
import org.geoserver.rest.client.datatypes.DataStores;
import org.geoserver.rest.client.datatypes.Workspace;
import org.geoserver.rest.client.jsonholder.DataStoreJSONHolder;
import org.geoserver.rest.client.jsonholder.DataStoresJSONHolder;

import com.sun.jersey.api.client.ClientResponse;

/**
 * The Geoserver Data Store Configuration client object that allows users to manipulate {@link DataStore}s on the connected
 * Geoserver instance. Data Stores are used to manage holdings of {@link DataStore}s.
 *
 * @author Ronak Patel
 */
public class DataStoreConfigClient extends ConfigClient {

	/**
	 * The URI for manipulating a single {@link DataStore}
	 */
	private static final String STORE_URI = "/rest/workspaces/{ws}/datastores/{store}";

	/**
	 * The URI for manipulating a collection of {@link DataStore}s
	 */
	private static final String URI = "/rest/workspaces/{ws}/datastores";

	/**
	 * Creates a Geoserver interaction object
	 * 
	 * @param geoserverClient
	 *            the Geoserver Client Configuration
	 */
	public DataStoreConfigClient(final GeoserverConfigClient geoserverClient) {
		super(geoserverClient);
	}

	/**
	 * Creates a {@link DataStore} on the server using the specified
	 * {@link DataStore}. The server representation of the store will be
	 * identical to the local object's properties.
	 * 
	 * @param workspace
	 *            the {@link Workspace} to add the {@link DataStore} to
	 * @param store
	 *            the exact {@link DataStore} to modify
	 * @return the created {@link DataStore}
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	public DataStore createDataStore(final Workspace workspace, final DataStore store) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + DataStoreConfigClient.URI;
		uri = uri.replace("{ws}", workspace.getName());
		uri += "?configure=all";

		this.createEntity(uri, new DataStoreJSONHolder(store));

		return store;
	}

	/**
	 * Removes a {@link DataStore} from the server under the specified
	 * {@link Workspace}.
	 * 
	 * @param workspace
	 *            the {@link Workspace} that the {@link DataStore} is located in
	 * @param store
	 *            the {@link DataStore} to be deleted
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	public void deleteDataStore(final Workspace workspace, final DataStore store) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + DataStoreConfigClient.STORE_URI;
		uri = uri.replace("{ws}", workspace.getName());
		uri = uri.replace("{store}", store.getName());

		this.deleteEntity(uri);
	}

	/**
	 * Retrieves a {@link DataStore} of the specified <code>name</code>
	 * 
	 * @param workspace
	 *            the {@link Workspace} that the {@link DataStore} is located in
	 * @param name
	 *            the name of the {@link DataStore} that should be retrieved
	 * @return the retrieved {@link DataStore}
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	public DataStore getDataStore(final Workspace workspace, final String name) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + DataStoreConfigClient.STORE_URI;
		uri = uri.replace("{ws}", workspace.getName());
		uri = uri.replace("{store}", name);

		final ClientResponse response = this.getEntity(uri);

		final DataStoreJSONHolder holder = response.getEntity(DataStoreJSONHolder.class);

		return holder.getDataStore();
	}

	/**
	 * Retrieves a collection of {@link DataStore}s held under the specified
	 * {@link Workspace}
	 * 
	 * @param workspace
	 *            the {@link Workspace} that the {@link DataStore} is
	 *            located in
	 * @return the retrieved list of {@link DataStore}s
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	public DataStores getDataStores(final Workspace workspace) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + DataStoreConfigClient.URI;
		uri = uri.replace("{ws}", workspace.getName());

		final ClientResponse response = this.getEntity(uri);

		final DataStoresJSONHolder holder = response.getEntity(DataStoresJSONHolder.class);

		return holder.getDataStores();
	}

	/**
	 * Updates the specified {@link DataStore} with its local properties.
	 * This essentially syncs the server with the local properties.
	 * 
	 * @param workspace
	 *            the {@link Workspace} that the {@link DataStore} is
	 *            located in
	 * @param store
	 *            the {@link DataStore} to update
	 * @return the updated {@link DataStore}
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	public DataStore updateDataStore(final Workspace workspace, final DataStore store) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + DataStoreConfigClient.STORE_URI;
		uri = uri.replace("{ws}", workspace.getName());
		uri = uri.replace("{store}", store.getName());

		this.updateEntity(uri, new DataStoreJSONHolder(store));

		return store;
	}
}
