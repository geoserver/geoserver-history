package org.geoserver.wfs.xml.v1_1_0;

import java.util.Iterator;

import org.eclipse.xsd.XSDSchema;
import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.wfs.xml.FeatureTypeSchemaBuilder;
import org.geotools.xml.Configuration;
import org.geotools.xml.SchemaLocator;

/**
 * Schema locator which adds types defined in applications schemas to the wfs schema proper.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class WFSSchemaLocator extends SchemaLocator {

	/** catalog used to look up application schema types */
	GeoServerCatalog catalog;
	/** schema type builder */
	FeatureTypeSchemaBuilder schemaBuilder;
	
	public WFSSchemaLocator(Configuration configuration, GeoServerCatalog catalog, FeatureTypeSchemaBuilder schemaBuilder ) {
		super( configuration );
		this.catalog = catalog;
		this.schemaBuilder = schemaBuilder;
	}

	protected XSDSchema createSchema() throws Exception {
		XSDSchema wfsSchema = super.createSchema();
		
		//incorporate application schemas into the wfs schema
		for ( Iterator i = catalog.featureTypes().iterator(); i.hasNext(); ) {
			FeatureTypeInfo meta = (FeatureTypeInfo) i.next();
			
			//build the schema for the types in the single namespace
			XSDSchema schema = 
				schemaBuilder.build( new FeatureTypeInfo[ ] { meta } );
			
			//declare the namespace
			String prefix = meta.namespacePrefix();
			String namespaceURI = catalog.getNamespaceSupport().getURI( prefix );
			wfsSchema.getQNamePrefixToNamespaceMap().put( prefix, namespaceURI );
			
			//add the types + elements to the wfs schema
			for ( Iterator t = schema.getTypeDefinitions().iterator(); t.hasNext(); ) {
				wfsSchema.getTypeDefinitions().add( t.next() );
			}
			for ( Iterator e = schema.getElementDeclarations().iterator(); e.hasNext(); ) {
				wfsSchema.getElementDeclarations().add( e.next() );
			}
		}
		
		return wfsSchema;
	}
}
