package org.geoserver.rest.client;

import java.net.URL;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

/**
 * The core configuration of the connected Geoserver server instance. This class encapsulates the
 * parameters require to correctly
 *
 * @author Ronak Patel
 */
public class GeoserverConfigClient {

	/**
	 * The HTTP Basic Authentication Filter
	 */
	private final HTTPBasicAuthFilter authFilter;

	/**
	 * The JAX-RS Client API used to communicate with Geoserver
	 */
	private final Client client;

	/**
	 * The geoserver URL
	 */
	private final URL geoserverUrl;

	/**
	 * The password to communicate with the REST Configuration endpoint
	 */
	private final String password;

	/**
	 * The username to communicate with the REST Configuration endpoint
	 */
	private final String userName;

	/**
	 * Constructs a basic client to communicte with Geoserver.
	 * 
	 * @param username
	 *            the username to communicate with the REST Configuration
	 *            endpoint
	 * @param password
	 *            the password to communicate with the REST Configuration
	 *            endpoint
	 * @param geoserverUrl
	 *            the geoserver URL
	 */
	public GeoserverConfigClient(final String username, final String password, final URL geoserverUrl) {

		this.userName = username;
		this.password = password;
		this.geoserverUrl = geoserverUrl;

		this.authFilter = new HTTPBasicAuthFilter(this.userName, this.password);

		this.client = Client.create();
		this.client.addFilter(this.authFilter);
		this.client.setFollowRedirects(true);
	}

	/**
	 * Retrieves the HTTP Basic Authentication Filter
	 * 
	 * @return the HTTP Basic Authentication Filter
	 */
	public HTTPBasicAuthFilter getAuthFilter() {
		return this.authFilter;
	}

	/**
	 * Returns the JAX-RS Client API used to communicate with Geoserver
	 * 
	 * @return the JAX-RS Client API used to communicate with Geoserver
	 */
	public Client getClient() {
		return this.client;
	}

	/**
	 * Returns the geoserver URL
	 * 
	 * @return the geoserver URL
	 */
	public URL getGeoserverUrl() {
		return this.geoserverUrl;
	}

	/**
	 * Returns the password to communicate with the REST Configuration endpoint
	 * 
	 * @return the password to communicate with the REST Configuration endpoint
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Returns the username to communicate with the REST Configuration endpoint
	 * 
	 * @return the username to communicate with the REST Configuration endpoint
	 */
	public String getUserName() {
		return this.userName;
	}
}
