package org.geoserver.wfs.http.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.ResultTypeType;
import net.opengis.wfs.WFSFactory;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.http.util.ResponseUtils;
import org.geoserver.ows.Operation;
import org.geoserver.ows.ServiceException;
import org.geoserver.ows.http.OWSUtils;
import org.geoserver.ows.http.Response;
import org.geoserver.wfs.FeatureProducer;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSException;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.geotools.xml.Encoder;
import org.geotools.xs.bindings.XSDateTimeBinding;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.xml.sax.SAXException;

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

	/**
	 * the catalog
	 */
	GeoServerCatalog catalog;
	
	public FeatureCollectionTypeResponse( WFS wfs, GeoServerCatalog catalog ) {
		super( FeatureCollectionType.class );
		this.wfs = wfs;
		this.catalog = catalog;
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
		
		FeatureCollectionType result = (FeatureCollectionType) value;
		
		//set the time, not sure when exactly we should set this to be accurate but 
		// try coming up with a test for that ;)
		result.setTimeStamp( Calendar.getInstance() );
		
		if ( resultType( operation ) == ResultTypeType.HITS_LITERAL ) {
			//just write out feature collection directly
		
			//create a new feautre collcetion type with just the numbers
			FeatureCollectionType hits = WFSFactory.eINSTANCE.createFeatureCollectionType();
			hits.setNumberOfFeatures( result.getNumberOfFeatures() );
			hits.setTimeStamp( result.getTimeStamp() );
			
			WFSConfiguration configuration = new WFSConfiguration( catalog );
			Encoder encoder = new Encoder( configuration, configuration.schema() );
			encoder.setSchemaLocation(
				org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE, 
				ResponseUtils.appendPath( wfs.getSchemaBaseURL(), "wfs/1.1.0/wfs.xsd" )
			);
			try {
				encoder.write( hits, org.geoserver.wfs.xml.v1_1_0.WFS.FEATURECOLLECTION, output );
			} 
			catch (SAXException e) {
				throw (IOException) new IOException( "Encoding error ").initCause( e );
			}
			
			return;
		}
		
		//look up the producer and go
		String outputFormat = outputFormat( operation );
		FeatureProducer producer = lookupProducer( outputFormat );
		producer.produce( outputFormat, result, output );
		
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
