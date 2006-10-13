package org.geoserver.wfs.xml;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.wfs.xml.wfs.v1_0_0.WFS;
import org.geotools.catalog.GeoResource;
import org.geotools.xml.impl.AttributeHandler;
import org.geotools.xml.impl.DocumentHandler;
import org.geotools.xml.impl.ElementHandler;
import org.geotools.xml.impl.ElementHandlerImpl;
import org.geotools.xml.impl.Handler;
import org.geotools.xml.impl.HandlerFactory;
import org.geotools.xml.impl.ParserHandler;

/**
 * Special handler factory which creates handlers for elements which are 
 * defined as wfs feature types.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class WFSHandlerFactory implements HandlerFactory {

	static Logger logger = Logger.getLogger( "org.geoserver.wfs" );
	
	/**
	 * Catalog reference
	 */
	GeoServerCatalog catalog;
	/**
	 * 
	 */
	Class/*<FeatureTypeSchema>*/ ftSchemaClass;
	
	public WFSHandlerFactory( GeoServerCatalog catalog, Class ftSchemaClass ) {
		this.catalog = catalog;
		this.ftSchemaClass = ftSchemaClass;
	}
	
	public DocumentHandler createDocumentHandler( ParserHandler parser ) {
		return null;
	}

	public ElementHandler createElementHandler( QName name, Handler parent, ParserHandler parser ) {
		String namespaceURI = name.getNamespaceURI();
		if ( namespaceURI == null ) {
			//assume default
			namespaceURI = catalog.getNamespaceSupport().getURI( "" );
		}
		
		String prefix = catalog.getNamespaceSupport().getPrefix( namespaceURI ); 
		if ( prefix == null ) {
			//if namespace not declared, not for us
			return null;
		}
		
		try {
			List resources = catalog.resources( FeatureTypeInfo.class );
			for ( Iterator r = resources.iterator(); r.hasNext(); ) {
				GeoResource resource = (GeoResource) r.next();
				FeatureTypeInfo featureTypeInfo = 
					(FeatureTypeInfo) resource.resolve( FeatureTypeInfo.class, null );
				if ( prefix.equals( featureTypeInfo.namespacePrefix() ) ) {
					if ( name.getLocalPart().equals( featureTypeInfo.getTypeName() ) ) {
						//found it
						FeatureTypeSchema ftSchema = 
							(FeatureTypeSchema) resource.resolve( ftSchemaClass, null );
						XSDSchema schema = ftSchema.schema();
						for ( Iterator e = schema.getElementDeclarations().iterator(); e.hasNext(); ) {
							XSDElementDeclaration element = (XSDElementDeclaration) e.next();
							if ( name.getLocalPart().equals( element.getName() ) ) {
								return new ElementHandlerImpl( element, parent, parser );		
							}
						}
						
					}
				}
			}
		} 
		catch (IOException e) {
			logger.log( Level.WARNING, null, e );
		}
		
		return null;
	}

	public ElementHandler createElementHandler( XSDElementDeclaration e, Handler parent, ParserHandler parser ) {
		return null;
	}

	public AttributeHandler createAttributeHandler( XSDAttributeDeclaration a, Handler parent, ParserHandler parser  ) {
		return null;
	}

	
}
