package org.geoserver.wfs.v1_1_0;

import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.geoserver.wfs.xml.wfs.v1_1_0.WFSSchemaLocationResolver;
import org.geoserver.xml.ows.v1_0_0.OWSSchemaLocationResolver;
import org.geotools.xml.transform.TransformerBase.SchemaLocationSupport;

/**
 * Class which provides support for uri to schema locations for the 
 * wfs schema.
 * <p>
 * Falls back on {@link org.geoserver.xml.ows.v1_0_0.OWSSchemaLocationResolver}
 * and {@link org.geoserver.wfs.xml.wfs.v1_1_0.WFSSchemaLocationResolver}.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class WFSSchemaLocationSupport extends SchemaLocationSupport {

	public WFSSchemaLocationSupport() {
		OWSSchemaLocationResolver ows = new OWSSchemaLocationResolver();
		map( ows, "http://www.opengis.net/ows", "ows19115subset.xsd" );
		map( ows, "http://www.opengis.net/ows", "owsAll.xsd" );
		map( ows, "http://www.opengis.net/ows", "owsCommon.xsd" );
		map( ows, "http://www.opengis.net/ows", "owsDataIdentification.xsd" );
		map( ows, "http://www.opengis.net/ows", "owsExceptionReport.xsd");
		map( ows, "http://www.opengis.net/ows", "owsGetCapabilities.xsd");
		map( ows, "http://www.opengis.net/ows", "owsOperationsMetadata.xsd");
		map( ows, "http://www.opengis.net/ows", "owsServiceIdentification.xsd");
		map( ows, "http://www.opengis.net/ows", "owsServiceProvider.xsd");
		
		WFSSchemaLocationResolver wfs = new WFSSchemaLocationResolver();
		map( wfs, "http://www.opengis.net/wfs", "wfs.xsd" );
	}
	
	void map( XSDSchemaLocationResolver resolver, String uri, String schema ) {
		
	}
}
