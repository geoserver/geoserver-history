package org.geoserver.wfs.http.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

import net.opengis.wfs.DescribeFeatureTypeType;

import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.ows.Operation;
import org.geoserver.ows.ServiceException;
import org.geoserver.ows.http.OWSUtils;
import org.geoserver.ows.http.Response;
import org.geoserver.wfs.WFSException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class FeatureTypeInfoResponse extends Response 
	implements ApplicationContextAware {

	ApplicationContext context;
	
	public FeatureTypeInfoResponse( ) {
		super( FeatureTypeInfo[].class );
	}

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
	
	public boolean canHandle(Operation operation) {
		if ( !"DescribeFeatureType".equalsIgnoreCase( operation.getId() ) )
			return false;
		
		if ( !"WFS".equalsIgnoreCase( operation.getService().getId() ) ) 
			return false;
		
		return true;
	}
	
	public String getMimeType(Operation operation) throws ServiceException {
		FeatureTypeEncoder encoder = featureTypeEncoder( operation );
		return encoder.getMimeType();
	}
	
	public void write(Object value, OutputStream output, Operation operation)
			throws IOException, ServiceException {
		
		featureTypeEncoder( operation ).encode( (FeatureTypeInfo[]) value, output );
	}
	
	private FeatureTypeEncoder featureTypeEncoder( Operation operation ) throws ServiceException {
		
		//get the output format
		DescribeFeatureTypeType request = (DescribeFeatureTypeType) OWSUtils.parameter( 
			operation.getParameters(), DescribeFeatureTypeType.class 
		);
		String outputFormat = request.getOutputFormat();
		
		Collection featureTypeEncoders = context.getBeansOfType( FeatureTypeEncoder.class ).values();
		for ( Iterator itr = featureTypeEncoders.iterator(); itr.hasNext(); ) {
			FeatureTypeEncoder featureTypeEncoder = (FeatureTypeEncoder) itr.next();
			if ( outputFormat.equals( featureTypeEncoder.getOutputFormat() ) )  {
				return featureTypeEncoder;
			}
		}
		
		String msg = "Could not find encoder for format: " + outputFormat;
		throw new WFSException( msg, "InvalidFormat" );
	}

}
