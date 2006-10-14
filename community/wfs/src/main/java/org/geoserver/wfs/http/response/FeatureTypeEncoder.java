package org.geoserver.wfs.http.response;

import java.io.IOException;
import java.io.OutputStream;

import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.ows.Operation;
import org.geoserver.ows.ServiceException;
import org.geoserver.ows.http.Response;

/**
 * Encodes feature type info in a particular format.
 * <p>
 * This class is used by {@link FeatureTypeInfoResponse} to support 
 * particular output format type.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public abstract class FeatureTypeEncoder {

	/**
	 * The mime type
	 */
	private String mimeType;
	/**
	 * The output format
	 */
	private String outputFormat;
	
	public FeatureTypeEncoder( String mimeType, String outputFormat ) {
		this.mimeType = mimeType;
		this.outputFormat = outputFormat;
	}
	
	public String getMimeType() {
		return mimeType;
	}
	
	public final String getOutputFormat() {
		return outputFormat;
	}
	
	public abstract void encode( FeatureTypeInfo[] featureTypeInfos, OutputStream output ) 
		throws IOException;
}
