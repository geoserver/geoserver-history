package org.geoserver.rest.client;

import java.io.IOException;
import java.rmi.RemoteException;

import org.geoserver.rest.client.datatypes.Layer;
import org.geoserver.rest.client.datatypes.Layers;
import org.geoserver.rest.client.jsonholder.LayerJSONHolder;
import org.geoserver.rest.client.jsonholder.LayersJSONHolder;

import com.sun.jersey.api.client.ClientResponse;

/**
 * The Geoserver Layer Configuration client object that allows users to manipulate {@link Layer}s on
 * the connected Geoserver instance. 
 *
 * @author Ronak Patel
 */
public class LayerConfigClient extends ConfigClient {

	/**
	 * The URI for manipulating a single {@link Layer}
	 */
	private static final String LAYER_URI = "/rest/layers/{l}";
	
	/**
	 * The URI for manipulating a collection of {@link Layer}s
	 */
	private static final String URI = "/rest/layers";

	/**
	 * Creates a Geoserver interaction object
	 * 
	 * @param geoserverClient
	 *            the Geoserver Client Configuration
	 */
	public LayerConfigClient(final GeoserverConfigClient geoserverClient) {
		super(geoserverClient);
	}

	/**
	 * Retrieves a collection of {@link Layer}s that are currently held on the
	 * server
	 * 
	 * @return the collection of {@link Layer}s that are currently held on the
	 *         server
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems occurred
	 */
	public Layers getLayers() throws RemoteException, IOException {

		final String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + LayerConfigClient.URI;

		final ClientResponse response = this.getEntity(uri);

		final LayersJSONHolder holder = response.getEntity(LayersJSONHolder.class);

		return holder.getLayers();
	}

	/**
	 * Retrieves a single {@link Layer} named <code>name</code> from the server
	 * 
	 * @param name
	 *            the name of the {@link Layer} of interest
	 * @return the retrieved {@link Layer}
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if the stream could not be read properly
	 */
	public Layer getLayer(final String name) throws RemoteException, IOException {
		
		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + LayerConfigClient.LAYER_URI;
		uri = uri.replace("{l}", name);
		
		final ClientResponse response = this.getEntity(uri);
		
		final LayerJSONHolder holder = response.getEntity(LayerJSONHolder.class);
		
		return holder.getLayer();
	}
	
	/**
	 * Updates the specified {@link Layer} with its local properties. This
	 * essentially syncs the server with the local properties.
	 * 
	 * @param layer
	 *            the {@link Layer} to update
	 * @return the updated {@link Layer}
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems occurred
	 */
	public Layer updateLayer(final Layer layer) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + LayerConfigClient.LAYER_URI;
		uri = uri.replace("{l}", layer.getName());

		this.updateEntity(uri, new LayerJSONHolder(layer));

		return this.getLayer(layer.getName());
	}
}
