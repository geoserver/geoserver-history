package org.geoserver.wfs.xml;

import java.util.Iterator;
import java.util.List;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.wfs.WFSTestSupport;
import org.geotools.catalog.GeoResource;

public class SchemaAdapterFactoryTest extends WFSTestSupport {

	protected void setUpInternal() throws Exception {
		super.setUpInternal();
		
		SchemaAdapterFactory factory = new SchemaAdapterFactory( wfs, catalog, loader);
		context.getBeanFactory().registerSingleton( "schemaAdapterFactory", factory );
	}
	
	public void testAdapt() throws Exception {
		
		List resources = catalog.resources( FeatureTypeSchema.class );
		assertFalse( resources.isEmpty() );
		
		for ( Iterator r = resources.iterator(); r.hasNext(); ) {
			GeoResource resource = (GeoResource) r.next();
			FeatureTypeSchema ftSchema = 
				(FeatureTypeSchema) resource.resolve( FeatureTypeSchema.class, null );
			XSDSchema schema = (XSDSchema) ftSchema.schema();
			assertNotNull( schema );
			
			assertEquals( 1, schema.getTypeDefinitions().size() );
			assertEquals( 1, schema.getElementDeclarations().size() );
			
			FeatureTypeInfo featureTypeInfo = 
				(FeatureTypeInfo) resource.resolve( FeatureTypeInfo.class, null );
		
			XSDTypeDefinition type = (XSDTypeDefinition) schema.getTypeDefinitions().get( 0 );
			assertEquals( featureTypeInfo.getTypeName() + "Type", type.getName() );

			XSDElementDeclaration element = 
				(XSDElementDeclaration) schema.getElementDeclarations().get( 0 );
			assertEquals( featureTypeInfo.getTypeName(), element.getName() );
			
			assertEquals( element.getType(), type );
		}
	}
	
	
}