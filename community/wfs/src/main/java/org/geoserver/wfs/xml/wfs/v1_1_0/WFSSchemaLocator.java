package org.geoserver.wfs.xml.wfs.v1_1_0;

import java.io.IOException;

import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.geoserver.wfs.xml.wfs.v1_0_0.WFS;
import org.geoserver.wfs.xml.wfs.v1_0_0.WFSSchemaLocationResolver;
import org.geoserver.xml.ows.v1_0_0.OWSSchemaLocationResolver;

import org.geotools.filter.v1_1.OGCSchemaLocationResolver;
import org.geotools.gml3.bindings.GMLSchemaLocationResolver;
import org.geotools.gml3.bindings.smil.SMIL20SchemaLocationResolver;
import org.geotools.xlink.bindings.XLINKSchemaLocationResolver;
import org.geotools.xml.Schemas;
public class WFSSchemaLocator implements XSDSchemaLocator {

	public XSDSchema locateSchema( 
		XSDSchema schema, String namespaceURI,  String rawSchemaLocationURI, String resolvedSchemaLocationURI
	) {
	
		if ( WFS.NAMESPACE.equals( namespaceURI ) ) {
			String location = getClass().getResource( "wfs.xsd" ).toString();
			
			XSDSchemaLocationResolver[] locators = new XSDSchemaLocationResolver[] {
				new XLINKSchemaLocationResolver(), new OGCSchemaLocationResolver(), 
				new SMIL20SchemaLocationResolver(), new GMLSchemaLocationResolver(), 
				new OWSSchemaLocationResolver(), new WFSSchemaLocationResolver()
			};
			try {
				return Schemas.parse( location, null, locators );
			} 
			catch (IOException e) {
				//TODO:  log this
			}
		}
		
		return null;
	}

}
