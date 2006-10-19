package org.geoserver.wfs.http.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.ResultTypeType;

import org.geoserver.ows.Operation;
import org.geoserver.ows.ServiceException;
import org.geoserver.ows.http.OWSUtils;
import org.geoserver.ows.http.Response;
import org.geoserver.wfs.FeatureProducer;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSException;
import org.geotools.xs.bindings.XSDateTimeBinding;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class FeatureCollectionTypeResponse extends Response 
	implements ApplicationContextAware {

	/** 
	 * Application context for loading feature producers 
	 */
	ApplicationContext context;
	
	/**
	 * WFS configuration
	 */
	WFS wfs;
	
	public FeatureCollectionTypeResponse( WFS wfs ) {
		super( FeatureCollectionType.class );
		this.wfs = wfs;
	}

	public void setApplicationContext( ApplicationContext context ) throws BeansException {
		this.context = context;
	}
	
	public boolean canHandle( Operation operation ) {
		return "WFS".equalsIgnoreCase( operation.getService().getId() ) && ( 
			"GetFeature".equalsIgnoreCase( operation.getId() ) || 
			"GetFeatureWithLock".equalsIgnoreCase( operation.getId() )
		);
			
	}
	
	public String getMimeType( Operation operation ) throws ServiceException {
		if ( resultType( operation ) == ResultTypeType.HITS_LITERAL ) {
			//hits are always just xml
			return "text/xml";
		}
		
		//producer will determine the mime type
		String outputFormat = outputFormat( operation );
		return lookupProducer( outputFormat ).getMimeType();
	}
	
	public void write( Object value, OutputStream output, Operation operation )
			throws IOException, ServiceException {
		
		if ( resultType( operation ) == ResultTypeType.HITS_LITERAL ) {
			//just write out feature collection directly
			FeatureCollectionType result = (FeatureCollectionType) value;
			
			//we know it must be 1.1, 1.0 doesn't support "hits"
			WfsXmlWriter writer = new WfsXmlWriter.WFS1_1( wfs, output );
			writer.openTag( 
				"wfs", "FeatureCollection", new String[] { 
					"timeStamp", new XSDateTimeBinding().encode( result.getTimeStamp(), null ),  
					"numberOfFeatures", result.getNumberOfFeatures().toString() 
				}
			);
			writer.closeTag( "wfs", "FeatureCollection" );
			writer.close();
			
			return;
		}
		
		//look up the producer and go
		String outputFormat = outputFormat( operation );
		FeatureProducer producer = lookupProducer( outputFormat );
		producer.produce( outputFormat, (FeatureCollectionType) value, output );
		
	}

	protected ResultTypeType resultType ( Operation operation ) {
		GetFeatureType request = 
			(GetFeatureType) OWSUtils.parameter( operation.getParameters(), GetFeatureType.class );
		return request.getResultType();
	}
	
	protected String outputFormat( Operation operation ) throws WFSException {
		GetFeatureType request = 
			(GetFeatureType) OWSUtils.parameter( operation.getParameters(), GetFeatureType.class );
		
		return request.getOutputFormat();
	}
	
	 protected FeatureProducer lookupProducer( String outputFormat ) 
		throws WFSException {
	
		Collection producers = context.getBeansOfType( FeatureProducer.class ).values();
		List matches = new ArrayList();
		for ( Iterator p = producers.iterator(); p.hasNext(); ) {
			FeatureProducer producer = (FeatureProducer) p.next();
			
			if ( producer.getOutputFormats() != null && 
					producer.getOutputFormats().contains( outputFormat ) ) {
				matches.add( producer );
			}
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
