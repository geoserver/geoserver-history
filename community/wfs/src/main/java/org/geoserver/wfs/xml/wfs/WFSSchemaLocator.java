package org.geoserver.wfs.xml.wfs;

import java.io.IOException;

import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.geotools.filter.v1_0.OGCSchemaLocationResolver;
import org.geotools.gml2.bindings.GMLSchemaLocationResolver;
import org.geotools.xlink.bindings.XLINKSchemaLocationResolver;
import org.geotools.xml.Schemas;

public class WFSSchemaLocator implements XSDSchemaLocator {

	public XSDSchema locateSchema( 
		XSDSchema schema, String namespaceURI,  String rawSchemaLocationURI, String resolvedSchemaLocationURI
	) {
	
		if ( WFS.NAMESPACE.equals( namespaceURI ) ) {
			String location = getClass().getResource( "WFS-basic.xsd" ).toString();
			
			XSDSchemaLocationResolver[] locators = new XSDSchemaLocationResolver[] {
				new XLINKSchemaLocationResolver(), new OGCSchemaLocationResolver(), 
				new GMLSchemaLocationResolver(), new WFSSchemaLocationResolver()
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
