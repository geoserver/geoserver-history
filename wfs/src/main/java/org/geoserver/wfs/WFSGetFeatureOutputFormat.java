package org.geoserver.wfs;

import java.io.IOException;
import java.io.OutputStream;

import net.opengis.wfs.FeatureCollectionType;

import org.geoserver.ows.Response;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;

/**
 * Base class for a response to a WFS GetFeature operation.
 * <p>
 * The result of a GetFeature operation is an instance of 
 * {@link FeatureCollectionType}. Subclasses are responsible for serializing 
 * an instance of this type in {@link #write(FeatureCollectionType, OutputStream, Operation)}.
 * </p>
 * <p>
 * Subclasses also need declare the mime-type in which the format is encoded.
 * </p>
 * 
 * @author Gabriel Rold?n, Axios Engineering
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public abstract class WFSGetFeatureOutputFormat extends Response {

	/**
	 * Constructor which sets the outputFormat.
	 * 
	 * @param outputFormat The well-known name of the format, not <code>null</code>
	 */
	public WFSGetFeatureOutputFormat( String outputFormat ) {
		super( FeatureCollectionType.class, outputFormat );
	}
	
	/**
	 * Ensures that the operation being executed is a GetFeature operation.
	 * <p>
	 * This method may be extended to add additional checks, it should not be 
	 * overriden.
	 * </p>
	 */
	public boolean canHandle(Operation operation) {
		if ( "GetFeature".equalsIgnoreCase( operation.getId() ) ) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Calls through to {@link #write(FeatureCollectionType, OutputStream, Operation)}.
	 */
	public final void write(Object value, OutputStream output, Operation operation)
			throws IOException, ServiceException {
	
		write( (FeatureCollectionType) value, output, operation );

	}
	
	/**
	 * Serializes the feature collection in the format declared.
	 * 
	 * @param featureCollection The feature collection.
	 * @param output The output stream to serialize to.
	 * @param getFeature The GetFeature operation descriptor.
	 */
	protected abstract void write( FeatureCollectionType featureCollection, OutputStream output, Operation getFeature )
		throws IOException, ServiceException;

}
