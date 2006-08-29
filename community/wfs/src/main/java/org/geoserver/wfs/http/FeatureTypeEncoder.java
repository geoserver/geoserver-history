package org.geoserver.wfs.http;

import java.io.IOException;
import java.io.OutputStream;

import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.ows.Operation;
import org.geoserver.ows.ServiceException;
import org.geoserver.ows.http.Response;

/**
 * Encodes feature type info in a particular format.
 * <p>
 * This class is used by {@link DescribeFeatureTypeResponse} to support 
 * particular output format type.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public abstract class FeatureTypeEncoder extends Response {

	private String outputFormat;
	
	public FeatureTypeEncoder( String mimeType, String outputFormat ) {
		super( mimeType, FeatureTypeInfo[].class );
		this.outputFormat = outputFormat;
	}
	
	public final String getOutputFormat() {
		return outputFormat;
	}
	
	public final boolean canHandle(Operation operation) {
		if ( !"DescribeFeatureType".equalsIgnoreCase( operation.getId() ) )
			return false;
		
		if ( !"WFS".equalsIgnoreCase( operation.getService().getId() ) ) 
			return false;
		
		Object outputFormat;
		try {
			outputFormat = operation.get( "outputFormat" );
		} 
		catch (Exception e) {
			return false;
		}
		
		if ( outputFormat == null || !( outputFormat instanceof String ) )
			return false;
		
		return this.outputFormat.equalsIgnoreCase( (String) outputFormat );
	}
	
	public final void write(Object value, OutputStream output, Operation operation) 
		throws IOException, ServiceException {
		
		encode( (FeatureTypeInfo[]) value, output );
	}
	
	public abstract void encode( FeatureTypeInfo[] featureTypeInfos, OutputStream output ) 
		throws IOException;
}
