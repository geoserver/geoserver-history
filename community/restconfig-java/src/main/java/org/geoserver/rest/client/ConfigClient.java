package org.geoserver.rest.client;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * The base Geoserver configuration client object that encapsulates the basic interaction 
 * with a remote Geoserver instance through the Geoserver REST API.
 * 
 * Derived classes are to define the specific interactions with a component of the REST API.
 *
 * The full specification of the Geoserver API is available at: 
 * {@link http://docs.geoserver.org/2.0.x/en/user/extensions/rest/index.html}
 * 
 * @author Ronak Patel
 */
public abstract class ConfigClient {

	/**
	 * The Geoserver Client Configuration
	 */
	protected final GeoserverConfigClient geoserverClient;

	/**
	 * Creates a Geoserver interaction object
	 * 
	 * @param geoserverClient
	 *            the Geoserver Client Configuration
	 */
	public ConfigClient(final GeoserverConfigClient geoserverClient) {
		this.geoserverClient = geoserverClient;
	}

	/**
	 * Creates a REST resource on the server by PUTing a binary file to
	 * Geoserver. Geoserver's API
	 * 
	 * @param uri
	 *            the RESTful URI to interact with
	 * @param entity
	 *            the {@link File} to upload to the server
	 * @param type
	 *            the {@link MediaType} of the file
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	protected void createEntity(final String uri, final File entity, final MediaType type) throws RemoteException, IOException {

		final WebResource uriResource = this.geoserverClient.getClient().resource(uri);

		final ClientResponse response = uriResource.entity(entity, type).put(ClientResponse.class);

		this.verifyReponseStatus(response);
	}

	/**
	 * Creates a REST resource on the server by POSTing the specified entity to the server.
	 * 
	 * @param uri
	 *            the RESTful URI to interact with
	 * @param entity
	 *            the entity to upload to the server
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	protected void createEntity(final String uri, final Object entity) throws RemoteException, IOException {

		final WebResource uriResource = this.geoserverClient.getClient().resource(uri);

		final ClientResponse response = uriResource.entity(entity, MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class);

		this.verifyReponseStatus(response);
	}

	/**
	 * Deletes the resource on the server at the specified url.
	 * 
	 * @param uri
	 *            the RESTful URI to interact with
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	protected void deleteEntity(final String uri) throws RemoteException, IOException {
		final WebResource uriResource = this.geoserverClient.getClient().resource(uri);

		final ClientResponse response = uriResource.delete(ClientResponse.class);

		this.verifyReponseStatus(response);
	}

	/**
	 * Retrieves an entity on the server from the specified {@link URL}
	 * 
	 * @param uri
	 *            the RESTful URI to interact with
	 * @return the full response that was received from the server
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	protected ClientResponse getEntity(final String uri) throws RemoteException, IOException {
		final WebResource uriResource = this.geoserverClient.getClient().resource(uri);

		final ClientResponse response = uriResource.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);

		this.verifyReponseStatus(response);
		return response;
	}

	/**
	 * Updates a RESTful entity on the server by PUTing the attached
	 * {@link Object}.
	 * 
	 * @param uri
	 *            the RESTful URI to interact with
	 * @param entity
	 *            the entity to update on the server
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	protected void updateEntity(final String uri, final Object entity) throws RemoteException, IOException {

		final WebResource uriResource = this.geoserverClient.getClient().resource(uri);

		final ClientResponse response = uriResource.entity(entity, MediaType.APPLICATION_JSON_TYPE).put(ClientResponse.class);

		this.verifyReponseStatus(response);
	}

	/**
	 * Verifies the status of a response to a request by verifying the value of
	 * the HTTP Status.
	 * 
	 * Any HTTP Status that is in the 3xx, 4xx, or 5xx will raise an exception.
	 * All others will pass through this method.
	 * 
	 * @param response
	 *            the response generated from a request
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	protected void verifyReponseStatus(final ClientResponse response) throws RemoteException, IOException {
		final int statusCode = response.getStatus();

		if(statusCode > 299) {
			throw new RemoteException("Status: " + statusCode + " Reason: " + response.getResponseStatus());
		}
	}
}
