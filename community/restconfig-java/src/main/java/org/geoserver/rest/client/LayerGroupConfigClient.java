package org.geoserver.rest.client;

import java.io.IOException;
import java.rmi.RemoteException;

import org.geoserver.rest.client.datatypes.Coverage;
import org.geoserver.rest.client.datatypes.CoverageStore;
import org.geoserver.rest.client.datatypes.Layer;
import org.geoserver.rest.client.datatypes.LayerGroup;
import org.geoserver.rest.client.datatypes.LayerGroups;
import org.geoserver.rest.client.datatypes.Workspace;
import org.geoserver.rest.client.jsonholder.LayerGroupJSONHolder;
import org.geoserver.rest.client.jsonholder.LayerGroupsJSONHolder;

import com.sun.jersey.api.client.ClientResponse;

/**
 * The Geoserver Coverage Store Configuration client object that allows users to manipulate {@link Layer}s on
 * the connected Geoserver instance. Layer Groups are used to manage holdings of {@link Layer}s.
 *
 * @author Ronak Patel
 */
public class LayerGroupConfigClient extends ConfigClient {

	/**
	 * The URI for manipulating a single {@link LayerGroup}
	 */
	private static final String GROUP_URI = "/rest/layergroups/{lg}";

	/**
	 * The URI for manipulating a collection of {@link LayerGroup}s
	 */
	private static final String URI = "/rest/layergroups";

	/**
	 * Creates a Geoserver interaction object
	 * 
	 * @param geoserverClient
	 *            the Geoserver Client Configuration
	 */
	public LayerGroupConfigClient(final GeoserverConfigClient geoserverClient) {
		super(geoserverClient);
	}

	/**
	 * Creates a {@link LayerGroup} on the server using the specified
	 * {@link LayerGroup}. The server representation of the group will be
	 * identicial to the local object's properties.
	 * 
	 * @param group
	 *            the {@link LayerGroup} to create
	 * @return the created {@link LayerGroup}
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems occurred
	 */
	public LayerGroup createLayerGroup(final LayerGroup group) throws RemoteException, IOException {

		final String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + LayerGroupConfigClient.URI;

		this.createEntity(uri, new LayerGroupJSONHolder(group));

		return group;
	}

	/**
	 * Removes a {@link LayerGroup} from the server under the specified
	 * {@link Workspace}
	 * 
	 * @param group
	 *            the {@link LayerGroup} to delete
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems occurred
	 */
	public void deleteLayerGroup(final LayerGroup group) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + LayerGroupConfigClient.GROUP_URI;
		uri = uri.replace("{lg}", group.getName());

		this.deleteEntity(uri);
	}

	/**
	 * Retrieves a {@link LayerGroup} of the specified <code>name</code>
	 * 
	 * @param name
	 *            the name of the {@link LayerGroup} to retrieve
	 * @return the retrieved {@link LayerGroup}
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems occurred
	 */
	public LayerGroup getLayerGroup(final String name) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + LayerGroupConfigClient.GROUP_URI;
		uri = uri.replace("{lg}", name);

		final ClientResponse response = this.getEntity(uri);

		final LayerGroupJSONHolder holder = response.getEntity(LayerGroupJSONHolder.class);

		final LayerGroup group = holder.getLayerGroup();
		group.setBounds(holder.getBounds());
		
		return group;
	}

	/**
	 * Retrieves a collection of {@link LayerGroup}s from the server
	 * 
	 * @return a collection of {@link LayerGroup}s from the server
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems occurred
	 */
	public LayerGroups getLayerGroups() throws RemoteException, IOException {

		final String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + LayerGroupConfigClient.URI;

		final ClientResponse response = this.getEntity(uri);

		final LayerGroupsJSONHolder holder = response.getEntity(LayerGroupsJSONHolder.class);

		return holder.getLayerGroups();
	}

	/**
	 * Updates the specified {@link LayerGroup} with its local properties. This
	 * essentially syncs the server with the local properties.
	 * 
	 * @param group
	 *            the {@link LayerGroup} to update
	 * @return the updated {@link LayerGroup}
	 * 
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems occurred
	 */
	public LayerGroup updateLayerGroup(final LayerGroup group) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + LayerGroupConfigClient.GROUP_URI;
		uri = uri.replace("{lg}", group.getName());

		//scrub all extraneous information
		//group..setBounds(null);
		
		this.updateEntity(uri, new LayerGroupJSONHolder(group));

		return group;
	}
}
