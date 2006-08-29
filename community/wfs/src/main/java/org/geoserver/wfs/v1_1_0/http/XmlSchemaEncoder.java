package org.geoserver.wfs.v1_1_0.http;

import java.io.IOException;
import java.io.OutputStream;
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
import org.eclipse.xsd.util.XSDResourceImpl;
import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.http.util.ResponseUtils;
import org.geoserver.ows.Operation;
import org.geoserver.ows.ServiceException;
import org.geoserver.ows.http.Response;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.http.FeatureTypeEncoder;
import org.geoserver.wfs.v1_1_0.DescribeFeatureType;
import org.geoserver.wfs.xml.GML3Profile;
import org.geoserver.wfs.xml.TypeMappingProfile;
import org.geoserver.wfs.xml.XSProfile;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.geotools.gml3.bindings.GML;
import org.opengis.feature.type.Name;

public class XmlSchemaEncoder extends FeatureTypeEncoder {
	
	/** wfs configuration */
	WFS wfs;
	/** the catalog */
	GeoServerCatalog catalog;
	
	/** profiles for type mapping */
	List profiles;
	
	public XmlSchemaEncoder( WFS wfs, GeoServerCatalog catalog ) {
		super( "text/xml; subtype=gml/3.1.1", "text/xml; subtype=gml/3.1.1" );
		this.wfs = wfs;
		this.catalog = catalog;
		
		profiles = new ArrayList();
		profiles.add( new XSProfile() );
		profiles.add( new GML3Profile() );
	}
	
	public void encode(FeatureTypeInfo[] metas, OutputStream output) throws IOException {
		XSDFactory factory = XSDFactory.eINSTANCE;
		XSDSchema schema = factory.createXSDSchema();
		schema.setSchemaForSchemaQNamePrefix( "xsd" );
		schema.getQNamePrefixToNamespaceMap().put( "xsd", XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001 );
		
		//group the feature types by namespace
		HashMap ns2metas = new HashMap();
		for ( int i = 0; i < metas.length; i++ ) {
			String prefix = metas[i].namespacePrefix();
			List l = (List) ns2metas.get( prefix );
			if ( l == null ) {
				l = new ArrayList();
			}
			l.add( metas[i] );
			
			ns2metas.put( prefix, l );
		}
		
		if ( ns2metas.entrySet().size() == 1 ) {
			//import gml schema
			XSDImport imprt = factory.createXSDImport();
			imprt.setNamespace( GML.NAMESPACE );
			imprt.setSchemaLocation( 
				ResponseUtils.appendPath( wfs.getSchemaBaseURL(), "gml/3.1.1/base/feature.xsd" )
			);
			schema.getContents().add( imprt );
			
			String targetPrefix = (String) ns2metas.keySet().iterator().next();
			String targetNamespace = catalog.getNamespaceSupport().getURI( targetPrefix );
			
			schema.setTargetNamespace( targetNamespace );
			//schema.getQNamePrefixToNamespaceMap().put( null, targetNamespace );
			schema.getQNamePrefixToNamespaceMap().put( targetPrefix, targetNamespace );
			schema.getQNamePrefixToNamespaceMap().put( "gml", GML.NAMESPACE );
			
			//all types in same namespace, write out the schema
			for ( int i = 0; i < metas.length; i++ ) {
				complexType( metas[i].featureType(), schema, factory );
				element( metas[i], schema, factory );
			}
		}
		else {
			//different namespaces, write out import statements
			for ( Iterator i = ns2metas.entrySet().iterator(); i.hasNext(); ) {
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
		
		//serialize
		schema.updateElement();
		XSDResourceImpl.serialize( output, schema.getElement() );
		
	}

	void element( FeatureTypeInfo meta, XSDSchema schema, XSDFactory factory ) {
		XSDElementDeclaration element = factory.createXSDElementDeclaration();
		element.setName( meta.getTypeName() );
		
		element.setSubstitutionGroupAffiliation( schema.resolveElementDeclaration( GML.NAMESPACE, "_Feature" ) );
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
			 schema.resolveComplexTypeDefinition( GML.NAMESPACE, "AbstractFeatureType" ) 
		 );
		 
		 XSDModelGroup group = factory.createXSDModelGroup();
		 group.setCompositor( XSDCompositor.SEQUENCE_LITERAL );
		 
		 XSProfile profile = new XSProfile();
		 
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
	
}
