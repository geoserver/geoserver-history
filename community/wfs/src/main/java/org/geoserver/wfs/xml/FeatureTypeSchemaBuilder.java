package org.geoserver.wfs.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDCompositor;
import org.eclipse.xsd.XSDDerivationMethod;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDConstants;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.http.util.ResponseUtils;
import org.geoserver.wfs.WFS;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.opengis.feature.type.Name;

/**
 * Builds a {@link org.eclipse.xsd.XSDSchema} from {@link org.geoserver.data.feature.FeatureTypeInfo}
 * instances. 
 * <p>
 * 
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public abstract class FeatureTypeSchemaBuilder {

	/** wfs configuration */
	WFS wfs;
	/** the catalog */
	GeoServerCatalog catalog;
	
	/**
	 * profiles used for type mapping.
	 */
	protected List profiles;
	
	/**
	 * gml schema stuff 
	 */
	protected XSDSchemaLocator gmlSchemaLocator;
	protected String gmlNamespace;
	protected String gmlSchemaLocation;
	
	protected FeatureTypeSchemaBuilder( WFS wfs, GeoServerCatalog catalog ) {
		this.wfs = wfs;
		this.catalog = catalog;
		
		profiles = new ArrayList();
		profiles.add( new XSProfile() );
	}
	
	public XSDSchema build( FeatureTypeInfo[] featureTypeInfos ) throws IOException {
		
		XSDFactory factory = XSDFactory.eINSTANCE;
		XSDSchema schema = factory.createXSDSchema();
		schema.setSchemaForSchemaQNamePrefix( "xsd" );
		schema.getQNamePrefixToNamespaceMap().put( "xsd", XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001 );
		
		//group the feature types by namespace
		HashMap ns2featureTypeInfos = new HashMap();
		for ( int i = 0; i < featureTypeInfos.length; i++ ) {
			String prefix = featureTypeInfos[i].namespacePrefix();
			List l = (List) ns2featureTypeInfos.get( prefix );
			if ( l == null ) {
				l = new ArrayList();
			}
			l.add( featureTypeInfos[i] );
			
			ns2featureTypeInfos.put( prefix, l );
		}
		
		if ( ns2featureTypeInfos.entrySet().size() == 1 ) {
			//import gml schema
			XSDImport imprt = factory.createXSDImport();
			imprt.setNamespace( gmlNamespace );
			imprt.setSchemaLocation( 
				ResponseUtils.appendPath( wfs.getSchemaBaseURL(), gmlSchemaLocation )
			);
			XSDSchema gmlSchema = gmlSchemaLocator.locateSchema( null, gmlNamespace, null, null );
			imprt.setResolvedSchema( gmlSchema );
			
			schema.getContents().add( imprt );
			
			String targetPrefix = (String) ns2featureTypeInfos.keySet().iterator().next();
			String targetNamespace = catalog.getNamespaceSupport().getURI( targetPrefix );
			
			schema.setTargetNamespace( targetNamespace );
			//schema.getQNamePrefixToNamespaceMap().put( null, targetNamespace );
			schema.getQNamePrefixToNamespaceMap().put( targetPrefix, targetNamespace );
			schema.getQNamePrefixToNamespaceMap().put( "gml", gmlNamespace );
			
			//all types in same namespace, write out the schema
			for ( int i = 0; i < featureTypeInfos.length; i++ ) {
				complexType( featureTypeInfos[i].featureType(), schema, factory );
				element( featureTypeInfos[i], schema, factory );
			}
		}
		else {
			//different namespaces, write out import statements
			for ( Iterator i = ns2featureTypeInfos.entrySet().iterator(); i.hasNext(); ) {
				Map.Entry entry = (Map.Entry) i.next();
				String prefix = (String) entry.getKey();
				List types = (List) entry.getValue();
				
				StringBuffer queryString = 
					new StringBuffer( "request=DescribeFeatureType&version=1.1.0" );
				queryString.append( "&typeName=" );
				for ( Iterator t = types.iterator(); t.hasNext(); ) {
					FeatureTypeInfo type = (FeatureTypeInfo) t.next();
					queryString.append( type.name() );
					
					if ( t.hasNext() )
						queryString.append( "," );
				}
				
				String schemaLocation = ResponseUtils.appendQueryString( 
					wfs.getOnlineResource().toString(), queryString.toString() 
				);
				String namespace = catalog.getNamespaceSupport().getURI( prefix );
				
				XSDImport imprt = factory.createXSDImport();
				imprt.setNamespace( namespace );
				imprt.setSchemaLocation( schemaLocation );
				
				schema.getContents().add( imprt );
			}
		}
		
		return schema;
	}
	
	void element( FeatureTypeInfo meta, XSDSchema schema, XSDFactory factory ) {
		XSDElementDeclaration element = factory.createXSDElementDeclaration();
		element.setName( meta.getTypeName() );
		
		element.setSubstitutionGroupAffiliation( schema.resolveElementDeclaration( gmlNamespace, "_Feature" ) );
		String namespaceURI = catalog.getNamespaceSupport().getURI( meta.namespacePrefix() );
		element.setTypeDefinition( 
			schema.resolveTypeDefinition( namespaceURI, meta.getTypeName() + "_Type" )	
		);
		 
		schema.getContents().add( element );
		schema.updateElement();
	}
	
	void complexType( FeatureType featureType, XSDSchema schema, XSDFactory factory ) {
		
		 XSDComplexTypeDefinition complexType = factory.createXSDComplexTypeDefinition();
		 complexType.setName( featureType.getTypeName() + "_Type" );
		  
		 complexType.setDerivationMethod(XSDDerivationMethod.EXTENSION_LITERAL);
		 complexType.setBaseTypeDefinition(
			 schema.resolveComplexTypeDefinition( gmlNamespace, "AbstractFeatureType" ) 
		 );
		 
		 XSDModelGroup group = factory.createXSDModelGroup();
		 group.setCompositor( XSDCompositor.SEQUENCE_LITERAL );
		 
		 AttributeType[] attributes = featureType.getAttributeTypes();
		
		 for ( int i = 0; i < attributes.length; i++ ) {
			 AttributeType attribute = attributes[i];
			 XSDElementDeclaration element = factory.createXSDElementDeclaration();
			 element.setName( attribute.getName() );
			 element.setNillable( attribute.isNillable() );
			 
			 Class binding = attribute.getType();
			 Name typeName = findTypeName( binding );
			 
			 XSDTypeDefinition type = schema.resolveTypeDefinition( 
				 typeName.getNamespaceURI(), typeName.getLocalPart() 
			 );
			 element.setTypeDefinition( type );
			 
			 XSDParticle particle = factory.createXSDParticle();
			 particle.setMinOccurs( attribute.getMinOccurs() );
			 particle.setMaxOccurs( attribute.getMaxOccurs() );
			 particle.setContent( element );
			 group.getContents().add( particle );
		 }
		 
		 XSDParticle particle = factory.createXSDParticle();
		 particle.setContent( group );
		 
		 complexType.setContent( particle );
		 
		 schema.getContents().add(complexType);
		 schema.updateElement();
		 
	}

	Name findTypeName( Class binding ) {
		for ( Iterator p = profiles.iterator(); p.hasNext(); ) {
			TypeMappingProfile profile = (TypeMappingProfile) p.next();
			Name name = profile.name( binding );
			
			if ( name != null )
				return name;
		}
		
		return null;
	}
	
	public static final class GML2 extends FeatureTypeSchemaBuilder {
		
		public GML2( WFS wfs, GeoServerCatalog catalog ) {
			super(wfs, catalog);
			
			profiles.add( new GML2Profile() );
			gmlSchemaLocator = new org.geotools.gml2.bindings.GMLSchemaLocator();
			gmlNamespace = org.geotools.gml2.bindings.GML.NAMESPACE;
			gmlSchemaLocation = "gml/2.1.2/feature.xsd";
			
		}
		
	}
	
	public static final class GML3 extends FeatureTypeSchemaBuilder {
		
		public GML3( WFS wfs, GeoServerCatalog catalog ) {
			super( wfs, catalog );
			
			profiles.add( new GML3Profile() );
			
			gmlSchemaLocator = new org.geotools.gml3.bindings.GMLSchemaLocator();
			gmlNamespace = org.geotools.gml3.bindings.GML.NAMESPACE;
			gmlSchemaLocation = "gml/3.1.1/base/feature.xsd";
			
		}
	}
	
	
}
