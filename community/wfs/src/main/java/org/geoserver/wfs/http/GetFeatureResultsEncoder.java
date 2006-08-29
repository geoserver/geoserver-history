package org.geoserver.wfs.http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.geoserver.ows.Operation;
import org.geoserver.ows.ServiceException;
import org.geoserver.ows.http.Response;
import org.geoserver.wfs.FeatureProducer;
import org.geoserver.wfs.GetFeature;
import org.geoserver.wfs.GetFeatureResults;
import org.geoserver.wfs.WFSException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class GetFeatureResultsEncoder extends Response 
	implements ApplicationContextAware {

	/** 
	 * Application context for loading feature producers 
	 */
	ApplicationContext context;
	/**
	 * The output format being used
	 */
	String outputFormat;
	
	public GetFeatureResultsEncoder( String mimeType, String outputFormat ) {
		super( mimeType, GetFeatureResults.class );
		this.outputFormat = outputFormat;
	}

	public void setApplicationContext( ApplicationContext context ) throws BeansException {
		this.context = context;
	}
	
	public boolean canHandle( Operation operation ) {
		if ( operation.getOperation() instanceof GetFeature ) {
			GetFeature getFeature = (GetFeature) operation.getOperation();
			return outputFormat.equals( getFeature.getOutputFormat() );
		}
		
		return false;
	}
	
	public void write( Object value, OutputStream output, Operation operation )
			throws IOException, ServiceException {
		
		FeatureProducer producer = lookupProducer( outputFormat );
		producer.produce( outputFormat, (GetFeatureResults) value, output );
		
	}

	 protected FeatureProducer lookupProducer( String outputFormat ) 
		throws WFSException {
	
		Collection producers = context.getBeansOfType( FeatureProducer.class ).values();
		List matches = new ArrayList();
		for ( Iterator p = producers.iterator(); p.hasNext(); ) {
			FeatureProducer producer = (FeatureProducer) p.next();
			
			if ( producer.canProduce( outputFormat ) ) 
				matches.add( producer );
		}
		
		if ( matches.isEmpty() ) {
			String msg = "output format: " + outputFormat + " not supported by geoserver";
			throw new WFSException( msg, "InvalidFormat" );
		}
		
		if ( matches.size() > 1 ) {
			String msg = "multiple producers found for output format: " + outputFormat;
			throw new WFSException( msg );
		}
		
		return (FeatureProducer) matches.get( 0 );
	 }
}
