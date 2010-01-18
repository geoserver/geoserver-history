package org.geoserver.rest.client;

import java.io.IOException;
import java.rmi.RemoteException;

import org.geoserver.rest.client.datatypes.Coverage;
import org.geoserver.rest.client.datatypes.CoverageStore;
import org.geoserver.rest.client.datatypes.CoverageStores;
import org.geoserver.rest.client.datatypes.Workspace;
import org.geoserver.rest.client.jsonholder.CoverageStoreJSONHolder;
import org.geoserver.rest.client.jsonholder.CoverageStoresJSONHolder;

import com.sun.jersey.api.client.ClientResponse;

/**
 * The Geoserver Coverage Store Configuration client object that allows users to manipulate {@link CoverageStore}s on
 * the connected Geoserver instance. Coverage Stores are used to manage holdings of {@link Coverage}s.
 *
 * @author Ronak Patel
 */
public class CoverageStoreConfigClient extends ConfigClient {

	/**
	 * The URI for manipulating a single {@link CoverageStore}
	 */
	private static final String STORE_URI = "/rest/workspaces/{ws}/coveragestores/{cs}";

	/**
	 * The URI for manipulating a collection of {@link CoverageStore}s
	 */
	private static final String URI = "/rest/workspaces/{ws}/coveragestores";

	/**
	 * Creates a Geoserver interaction object
	 * 
	 * @param geoserverClient
	 *            the Geoserver Client Configuration
	 */
	public CoverageStoreConfigClient(final GeoserverConfigClient geoserverClient) {
		super(geoserverClient);
	}

	/**
	 * Creates a {@link CoverageStore} on the server using the specified
	 * {@link CoverageStore}. The server representation of the store will be
	 * identical to the local object's properties.
	 * 
	 * @param workspace
	 *            the {@link Workspace} to add the {@link CoverageStore} to
	 * @param store
	 *            the exact {@link CoverageStore} to modify
	 * @return the created {@link CoverageStore}
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *              if communications or file access problems occurred
	 */
	public CoverageStore createCoverageStore(final Workspace workspace, final CoverageStore store) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + (CoverageStoreConfigClient.URI);
		uri = uri.replace("{ws}", workspace.getName());
		
		store.setEnabled(true);
		store.setWorkspace(workspace);

		this.createEntity(uri, new CoverageStoreJSONHolder(store));

		return store;
	}

	/**
	 * Removes a {@link CoverageStore} from the server under the specified
	 * {@link Workspace}.
	 * 
	 * @param workspace
	 *            the {@link Workspace} that the {@link CoverageStore} is
	 *            located in
	 * @param store
	 *            the {@link CoverageStore} to be deleted
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems occurred
	 */
	public void deleteCoverageStore(final Workspace workspace, final CoverageStore store) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + (CoverageStoreConfigClient.STORE_URI);
		uri = uri.replace("{ws}", workspace.getName());
		uri = uri.replace("{cs}", store.getName());

		store.setWorkspace(workspace);
		
		this.deleteEntity(uri);
	}

	/**
	 * Retrieves a {@link CoverageStore} of the specified <code>name</code>
	 * 
	 * @param workspace
	 *            the {@link Workspace} that the {@link CoverageStore} is
	 *            located in
	 * @param name
	 *            the name of the {@link CoverageStore} that should be retrieved
	 * @return the retrieved {@link CoverageStore}
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems occurred
	 */
	public CoverageStore getCoverageStore(final Workspace workspace, final String name) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + (CoverageStoreConfigClient.STORE_URI);
		uri = uri.replace("{ws}", workspace.getName());
		uri = uri.replace("{cs}", name);

		final ClientResponse response = this.getEntity(uri);

		final CoverageStoreJSONHolder holder = response.getEntity(CoverageStoreJSONHolder.class);
		return holder.getCoverageStore();
	}

	/**
	 * Retrieves a collection of {@link CoverageStore}s held under the specified
	 * {@link Workspace}
	 * 
	 * @param workspace
	 *            the {@link Workspace} that the {@link CoverageStore} is
	 *            located in
	 * @return the retrieved list of {@link CoverageStore}s
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems occurred
	 */
	public CoverageStores getCoverageStores(final Workspace workspace) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + (CoverageStoreConfigClient.URI);
		uri = uri.replace("{ws}", workspace.getName());

		final ClientResponse response = this.getEntity(uri);
		final CoverageStoresJSONHolder holder = response.getEntity(CoverageStoresJSONHolder.class);

		return holder.getCoverageStores();
	}

	/**
	 * Updates the specified {@link CoverageStore} with its local properties.
	 * This essentially syncs the server with the local properties.
	 * 
	 * @param workspace
	 *            the {@link Workspace} that the {@link CoverageStore} is
	 *            located in
	 * @param store
	 *            the {@link CoverageStore} to update
	 * @return the updated {@link CoverageStore}
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems occurred
	 */
	public CoverageStore updateCoverageStore(final Workspace workspace, final CoverageStore store) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + (CoverageStoreConfigClient.STORE_URI);
		uri = uri.replace("{ws}", workspace.getName());
		uri = uri.replace("{cs}", store.getName());

		this.updateEntity(uri, new CoverageStoreJSONHolder(store));

		return store;
	}
}
