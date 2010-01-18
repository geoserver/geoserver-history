package org.geoserver.rest.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.ws.rs.core.MediaType;

import org.geoserver.rest.client.datatypes.Coverage;
import org.geoserver.rest.client.datatypes.CoverageStore;
import org.geoserver.rest.client.datatypes.Coverages;
import org.geoserver.rest.client.datatypes.Workspace;
import org.geoserver.rest.client.jsonholder.CoverageJSONHolder;
import org.geoserver.rest.client.jsonholder.CoveragesJSONHolder;

import com.sun.jersey.api.client.ClientResponse;

/**
 * The Geoserver Coverage Configuration client object that allows users to manipulate {@link Coverage}s on
 * the connected Geoserver instance.
 *
 * @author Ronak Patel
 */
public class CoverageConfigClient extends ConfigClient {

	/**
	 * The URI for manipulating a single coverage 
	 */
	private static final String COVERAGE_URI = "/rest/workspaces/{ws}/coveragestores/{cs}/coverages/{c}";

	/**
	 * The URI for the coverage file upload REST endpoint
	 */
	private static final String UPLOAD_URI = "/rest/workspaces/{ws}/coveragestores/{cs}/file.{extension}";

	/**
	 * The URI for manipulating a collection of coverages
	 */
	private static final String URI = "/rest/workspaces/{ws}/coveragestores/{cs}/coverages";

	/**
	 * Creates a Geoserver interaction object
	 * 
	 * @param geoserverClient
	 *            the Geoserver Client Configuration
	 */
	public CoverageConfigClient(final GeoserverConfigClient geoserverClient) {
		super(geoserverClient);
	}

	/**
	 * Creates a {@link Coverage} on the server by uploading the specified
	 * {@link Coverage} {@link File} to the server. The file should be a GeoTiff
	 * or ESRI WorldImage file. No other formats are supported at this time.
	 * 
	 * @param workspace
	 *            the {@link Workspace} to add the {@link Coverage} to
	 * @param coverageStore
	 *            the exact {@link CoverageStore} to modify
	 * @param coverage
	 *            the {@link Coverage} file to upload to the server
	 * @return the created {@link Coverage}
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	public Coverage createCoverage(final Workspace workspace, final CoverageStore coverageStore, final File coverage) throws IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + CoverageConfigClient.UPLOAD_URI;
		uri = uri.replace("{ws}", workspace.getName());
		uri = uri.replace("{cs}", coverageStore.getName());

		if(coverage.exists() == false) {
			throw new FileNotFoundException("File " + coverage.getAbsolutePath() + " doesn't exist!!");
		}

		MediaType contentType = MediaType.valueOf("image/*");
		final String coverageName = coverage.getName();

		if(coverageName.endsWith(".tif") || coverageName.endsWith(".tiff")) {
			contentType = MediaType.valueOf("image/tiff");
			uri = uri.replace("{extension}", "geotiff");
		} else {
			if(coverageName.endsWith(".png") || coverageName.endsWith("jpeg") || coverageName.endsWith("jpg")) {
				contentType = MediaType.valueOf("image/png");
				uri = uri.replace("{extension}", "worldimage");
			} else {
				throw new IllegalArgumentException("Unknown File Type: " + coverage.getName());
			}
		}

		uri += "?coverageName=" + coverageName.substring(0, coverageName.lastIndexOf('.'));

		this.createEntity(uri, coverage, contentType);
		
		return this.getCoverage(workspace, coverageStore, coverage.getName());
	}

	/**
	 * Deletes the specified {@link Coverage} from the specified
	 * {@link CoverageStore} from the specified {@link Workspace}.
	 * 
	 * @param workspace
	 *            the workspace to modify
	 * @param coverageStore
	 *            the coverage store to modify
	 * @param coverage
	 *            the exact {@link Coverage} that should be removed
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	public void deleteCoverage(final Workspace workspace, final CoverageStore coverageStore, final Coverage coverage) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + CoverageConfigClient.COVERAGE_URI;
		uri = uri.replace("{ws}", workspace.getName());
		uri = uri.replace("{cs}", coverageStore.getName());
		uri = uri.replace("{c}", coverage.getName());

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
	public Coverage getCoverage(final Workspace workspace, final CoverageStore coverageStore, final String name) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + CoverageConfigClient.COVERAGE_URI;
		uri = uri.replace("{ws}", workspace.getName());
		uri = uri.replace("{cs}", coverageStore.getName());
		uri = uri.replace("{c}", name);

		final ClientResponse response = this.getEntity(uri);

		final CoverageJSONHolder coverageHolder = response.getEntity(CoverageJSONHolder.class);

		return coverageHolder.getCoverage();
	}

	/**
	 * Retrieves the collection of {@link Coverage}s from the server located
	 * under the specified {@link Workspace} and {@link CoverageStore}.
	 * 
	 * @param workspace
	 *            the {@link Workspace} that the {@link CoverageStore} is
	 *            located in
	 * @param coverageStore
	 *            the {@link CoverageStore} in question
	 * @return the list of {@link Coverage}s
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	public Coverages getCoverages(final Workspace workspace, final CoverageStore coverageStore) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + CoverageConfigClient.URI;
		uri = uri.replace("{ws}", workspace.getName());
		uri = uri.replace("{cs}", coverageStore.getName());

		final ClientResponse response = this.getEntity(uri);

		final CoveragesJSONHolder coveragesHolder = response.getEntity(CoveragesJSONHolder.class);

		return coveragesHolder.getCoverages();
	}

	/**
	 * Updates the properties of the specified {@link Coverage} at the server
	 * 
	 * @param workspace
	 *            the {@link Workspace} that the {@link CoverageStore} is
	 *            located in
	 * @param coverageStore
	 *            the {@link CoverageStore} that has the specified
	 *            {@link Coverage}
	 * @param coverage
	 *            the {@link Coverage} to update
	 * @return the updated {@link Coverage}
	 * @throws RemoteException
	 *             if there was a problem reaching the remote server
	 * @throws IOException
	 *             if communications or file access problems prevented the
	 *             upload from occurring
	 */
	public Coverage updateCoverage(final Workspace workspace, final CoverageStore coverageStore, final Coverage coverage) throws RemoteException, IOException {

		String uri = this.geoserverClient.getGeoserverUrl().toExternalForm() + CoverageConfigClient.COVERAGE_URI;
		uri = uri.replace("{ws}", workspace.getName());
		uri = uri.replace("{cs}", coverageStore.getName());
		uri = uri.replace("{c}", coverage.getName());

		this.updateEntity(uri, new CoverageJSONHolder(coverage));

		return coverage;
	}
}
