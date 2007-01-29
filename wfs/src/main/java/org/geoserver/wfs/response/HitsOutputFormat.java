package org.geoserver.wfs.response;

import java.io.IOException;
import java.io.OutputStream;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.ResultTypeType;
import net.opengis.wfs.WFSFactory;

import org.geoserver.ows.util.OwsUtils;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.geotools.xml.Encoder;
import org.xml.sax.SAXException;

/**
 * WFS output format for a GetFeature operation in which the resultType is "hits".
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class HitsOutputFormat extends WFSGetFeatureOutputFormat {

	/**
	 * WFS configuration
	 */
	WFS wfs;
	
	/**
	 * Xml configuration
	 */
	WFSConfiguration configuration;
	
	public HitsOutputFormat( WFS wfs, WFSConfiguration configuration ) {
		super( null ); //ignore output format
		
		this.wfs = wfs;
		this.configuration = configuration;
	}

	/**
	 * Checks that the resultType is of type "hits".
	 */
	protected boolean canHandleInternal(Operation operation) {
		GetFeatureType request = 
			(GetFeatureType) OwsUtils.parameter( operation.getParameters(), GetFeatureType.class );
		
		return request.getResultType() == ResultTypeType.HITS_LITERAL;
	}
	
	protected void write(FeatureCollectionType featureCollection,
			OutputStream output, Operation getFeature) throws IOException,
			ServiceException {
		
		//create a new feautre collcetion type with just the numbers
		FeatureCollectionType hits = WFSFactory.eINSTANCE.createFeatureCollectionType();
		hits.setNumberOfFeatures( featureCollection.getNumberOfFeatures() );
		hits.setTimeStamp( featureCollection.getTimeStamp() );
		
		Encoder encoder = new Encoder( configuration, configuration.schema() );
		encoder.setSchemaLocation(
			org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE, 
			ResponseUtils.appendPath( wfs.getSchemaBaseURL(), "wfs/1.1.0/wfs.xsd" )
		);
		try {
			encoder.encode( hits, org.geoserver.wfs.xml.v1_1_0.WFS.FEATURECOLLECTION, output );
		} 
		catch (SAXException e) {
			throw (IOException) new IOException( "Encoding error ").initCause( e );
		}
		
		return;

	}

}
