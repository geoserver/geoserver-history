package org.geoserver.wfs.v1_1_0;

import java.io.IOException;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.wfs.WFS;
import org.geoserver.xml.ows.v1_0_0.OWS;
import org.geotools.factory.FactoryFinder;
import org.geotools.filter.expression.FunctionExpression;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xlink.bindings.XLINK;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;

import com.vividsolutions.jts.geom.Envelope;

public class WFSCapabilitiesTransformer extends TransformerBase {

	/**
	 * Web Feature Service
	 */
	WFS wfs;
	/**
	 * The catalog
	 */
	GeoServerCatalog catalog;
	
	public WFSCapabilitiesTransformer( WFS wfs, GeoServerCatalog catalog ) {
		this.wfs = wfs;
		this.catalog = catalog;
	
		setNamespaceDeclarationEnabled(false);
	}
	
	public Translator createTranslator( ContentHandler handler ) {
		return new WFSCapabilitiesTranslator( handler );
	}

	class WFSCapabilitiesTranslator extends TranslatorSupport {

		public WFSCapabilitiesTranslator( ContentHandler handler ) {
			super( handler, null, null );
			
		}
		
		public void encode( Object object ) throws IllegalArgumentException {
			
			AttributesImpl attributes = attributes( 
				new String[] { 
					"version", "1.1.0", 
					"xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance",
					"xmlns", org.geoserver.xml.wfs.v1_1_0.WFS.NAMESPACE,
					"xmlns:ows", OWS.NAMESPACE,  
					"xmlns:ogc", OGC.NAMESPACE, 
					"xmlns:xlink", XLINK.NAMESPACE
				}
			);
            
			NamespaceSupport namespaces = catalog.getNamespaceSupport();
            for ( Enumeration p = namespaces.getPrefixes(); p.hasMoreElements(); ) {
            		String prefix = (String) p.nextElement();
            		String uri = namespaces.getURI( prefix );
            		
                String prefixDef = "xmlns:" + prefix;
                
                attributes.addAttribute("", prefixDef, prefixDef, "", uri);
            }

    			start( "WFS_Capabilities", attributes );
			
			serviceIdentification();
			serviceProvider();
			operationsMetadata();
			featureTypeList();
			supportsGMLObjectTypeList();
			filterCapabilities();
			
			end( "WFS_Capabilities" );
		
		}

		void serviceIdentification() {
			start( "ows:ServiceIdentification" );
		
			element( "ows:ServiceType", "WFS" );
			element( "ows:ServiceTypeVersion" , "1.1.0" );
			element( "ows:Title", wfs.getTitle() );
			element( "ows:Abstract", wfs.getAbstract() );
			
			keywords( wfs.getKeywords() );
			
			element( "ows:Fees", wfs.getFees() );
			element( "ows:AccessConstraints", wfs.getAccessConstraints() );
			
			end( "ows:ServiceIdentification" );
		}
		
		void serviceProvider() {
			start( "ows:ServiceProvider" );
			
			element( "ows:ProviderName", null );
			element( "ows:ProviderSite", null );
			
			start( "ows:ServiceContact" );
			element( "ows:ProviderName", null );
			element( "ows:ProviderSite", null );
			start( "ows:ContactInfo");
			start( "ows:Phone" );
			element( "ows:Voice", null );
			element( "ows:Facsimile", null );
			end( "ows:Phone" );
			start( "ows:Address" );
			end( "ows:Address" );
			end( "ows:ContactInfo");
			element( "ows:Role", null );
			end( "ows:ServiceContact" );
			
			end( "ows:ServiceProvider" );
		}
		
		void operationsMetadata() {
			start( "ows:OperationsMetadata" );
			
			getCapabilities();
			describeFeatureType();
    			getFeature();
    		
			if (wfs.getServiceLevel() >= WFS.TRANSACTIONAL) {
                //transaction();
            }

            if (wfs.getServiceLevel() == WFS.COMPLETE) { 
                //lock();
                //getFeatureWithLock();
            }

    			end( "ows:OperationsMetadata" );
		}
		
		void getCapabilities() {
			Map.Entry[] parameters = new Map.Entry[] {
				parameter( "AcceptVersions", new String[]{ "1.0.0", "1.1.0" } ),
				parameter( "AcceptFormats", new String[]{ "text/xml" } ),
				parameter( 
					"Sections", 
					new String[]{ 
						"ServiceIdentification", "ServiceProvider", "OperationsMetadata",
						"FeatureTypeList", "ServesGMLObjectTypeList", "SupportsGMLObjectTypeList", 
						"Filter_Capabilities"
					} 
				) 
			};
			operation( "GetCapabilities", parameters, false, true );
		}
		
		void describeFeatureType() {
			Map.Entry[] parameters = new Map.Entry[] {
				parameter( "outputFormat" , new String[] { "text/gml; subtype=gml/3.1.1" } )
			};
			
			operation( "DescribeFeatureType", parameters, true, true );
		}
		
		void getFeature() {
			Map.Entry[] parameters = new Map.Entry[] {
				parameter( "resultType", new String[] { "results", "hits" } ),
				parameter( "outputFormat", new String[] { "text/gml; subtype=gml/3.1.1" } )
			};
			
			operation( "GetFeature", parameters, true, true );
		}
		
		void featureTypeList() {
			start( "FeatureTypeList" );
			
			start("Operations");

            if ((wfs.getServiceLevel() | WFS.SERVICE_BASIC) != 0) {
                element("Query", null);
            }

            if ((wfs.getServiceLevel() | WFS.SERVICE_INSERT) != 0) {
                element("Insert", null);
            }

            if ((wfs.getServiceLevel() | WFS.SERVICE_UPDATE) != 0) {
                element("Update", null);
            }

            if ((wfs.getServiceLevel() | WFS.SERVICE_DELETE) != 0) {
                element("Delete", null);
            }

            if ((wfs.getServiceLevel() | WFS.SERVICE_LOCKING) != 0) {
                element("Lock", null);
            }

            end("Operations");
			
            List featureTypes = null;
			try {
				featureTypes = catalog.featureTypes();
			} 
			catch (IOException e) {
				throw new RuntimeException( e );
			}
			
			for ( Iterator i = featureTypes.iterator(); i.hasNext(); ) {
				FeatureTypeInfo featureType = (FeatureTypeInfo) i.next();
				featureType( featureType );
			}
			end( "FeatureTypeList" );
		}

		void featureType( FeatureTypeInfo featureType ) {
			String prefix = featureType.namespacePrefix();
			String uri = catalog.getNamespaceSupport().getURI( prefix );
			
			start( "FeatureType", attributes( new String[] { "xmlns:" + prefix , uri } ) );
		
			element( "Name", featureType.name() );
			element( "Title", featureType.getTitle() );
			element( "Abstract", featureType.getAbstract() );
			keywords( featureType.getKeywords() );
			element( "DefaultSRS", "EPSG:" + featureType.getSRS() );
			
			start( "OutputFormats" );
			element( "OutputFormat", "text/gml; subtype=gml/3.1.1" );
			end( "OutputFormats" );
			
			Envelope bbox = null;
			try {
				 bbox = featureType.latLongBoundingBox();
			} 
			catch (IOException e) {
				throw new RuntimeException( e );
			}
			
			start( "ows:WGS84BoundingBox" );
			
			element( "ows:LowerCorner", bbox.getMinX() + " " + bbox.getMinY() );
			element( "ows:UpperCorner", bbox.getMaxX() + " " + bbox.getMaxY() );
			
			end( "ows:WGS84BoundingBox" );
			
			end( "FeatureType" );
		}

		void supportsGMLObjectTypeList() {
			element( "SupportsGMLObjectTypeList", null );
		}
		
		void filterCapabilities() {
			
            start("ogc:Filter_Capabilities");
            
            start("ogc:Spatial_Capabilities");
            
            start( "ogc:Geometry_Operands" );
            element( "ogc:GeometryOperand", "gml:Envelope" );
            element( "ogc:GeometryOperand", "gml:Point" );
            element( "ogc:GeometryOperand", "gml:LineString" );
            element( "ogc:GeometryOperand", "gml:Polygon" );
            end( "ogc:Geometry_Operands" );
            
            start("ogc:SpatialOperators");
            element("ogc:SpatialOperator", null, attributes( new String[]{ "name", "Disjoint" } ) );
            element("ogc:SpatialOperator", null, attributes( new String[]{ "name", "Equals" } ) );
            element("ogc:SpatialOperator", null, attributes( new String[]{ "name", "DWithin" } ) );
            element("ogc:SpatialOperator", null, attributes( new String[]{ "name", "Beyond" } ) );
            element("ogc:SpatialOperator", null, attributes( new String[]{ "name", "Intersect" } ) );
            element("ogc:SpatialOperator", null, attributes( new String[]{ "name", "Touches" } ) );
            element("ogc:SpatialOperator", null, attributes( new String[]{ "name", "Crosses" } ) );
            element("ogc:SpatialOperator", null, attributes( new String[]{ "name", "Contains" } ) );
            element("ogc:SpatialOperator", null, attributes( new String[]{ "name", "Overlaps" } ) );
            element("ogc:SpatialOperator", null, attributes( new String[]{ "name", "BBOX" } ) );
            end("ogc:SpatialOperators");
            
            end("ogc:Spatial_Capabilities");

            start("ogc:Scalar_Capabilities");
            
            element("ogc:LogicalOperators", null);
            
            start("ogc:ComparisonOperators");
            element("ogc:ComparisonOperator","LessThan" );
            element("ogc:ComparisonOperator","GreaterThan" );
            element("ogc:ComparisonOperator","LessThanOrEqualTo" );
            element("ogc:ComparisonOperator","GreaterThanOrEqualTo" );
            element("ogc:ComparisonOperator","EqualTo" );
            element("ogc:ComparisonOperator","NotEqualTo" );
            element("ogc:ComparisonOperator","Like" );
            element("ogc:ComparisonOperator","Between" );
            element("ogc:ComparisonOperator","NullCheck" );
            end("ogc:ComparisonOperators");
            
            start("ogc:ArithmeticOperators");
            element("ogc:SimpleArithmetic", null);
            
            functions();
            
            end("ogc:ArithmeticOperators");
            
            end("ogc:Scalar_Capabilities");
            end("ogc:Filter_Capabilities");
		}
		
		void functions() {
			start("ogc:Functions");
        	 	start("ogc:FunctionNames");
        	 	
        	 	Iterator itr = FactoryFinder.factories( FunctionExpression.class );
        	 	
        	 	SortedSet sortedFunctions = new TreeSet(
    	 			new Comparator(){
	            	 	public int compare(Object o1, Object o2){
                        String n1 = ((FunctionExpression) o1).getName();
                        String n2 = ((FunctionExpression) o2).getName();
                        return n1.toLowerCase().compareTo(n2.toLowerCase());
	            	 	}
    	 			}
 			);
        	 	
             while (  itr.hasNext()   ) {
            	 	sortedFunctions.add( itr.next() ); 
             }
             
             //write them now that functions are sorted by name
             FunctionExpression exp = null;
             itr = sortedFunctions.iterator();
             while (  itr.hasNext()   ) {
                 FunctionExpression fe = (FunctionExpression) itr.next();
                 String name = fe.getName();
                 int nargs= fe.getArgCount();
                 
                 element( "ogc:FunctionName", name, attributes( new String[] { "nArgs", ""+nargs } ) );
                 
             }
             
             end( "ogc:FunctionNames");
             end( "ogc:Functions");
        }
		
		void keywords( String[] keywords ) {
			start( "ows:Keywords" );
			
			for ( int i = 0; keywords != null && i < keywords.length; i++ ) {
				element( "ows:Keyword", keywords[i] );
			}
			
			end( "ows:Keywords");
		}
		
		void operation( String name, Map.Entry[] parameters, boolean get, boolean post ) {
			start( "ows:Operation", attributes( new String[]{ "name", name } ) );
		
			//dcp
			start( "ows:DCP" );
			if ( get ) {
				String getURL = wfs.getOnlineResource().toString();
				if ( getURL.indexOf( '?' ) == -1 ) {
					getURL += "?";
				}
				else {
					getURL = getURL.endsWith( "?" ) ? getURL : getURL + "&";
				}
				
				element( "ows:Get", null, attributes( new String[] { "xlink:href", getURL } ) );
			}
			if ( post ) {
				String postURL = wfs.getOnlineResource().toString();
				if ( postURL.indexOf( '?' ) != -1 ) {
					postURL = postURL.substring( 0, postURL.indexOf( '?' ) );
				}
				element( "ows:Post", null, attributes( new String[] { "xlink:href", postURL } ) );
			}
			end( "ows:DCP" );
			
			//parameters
			for ( int i = 0; i < parameters.length; i++ ) {
				String pname = (String) parameters[i].getKey();
				String[] pvalues = (String[]) parameters[i].getValue();
				
				start( "ows:Parameter", attributes( new String[]{ "name", pname } ) );
				
				for ( int j = 0; j < pvalues.length; j++ ) {
					element( "ows:Value", pvalues[j] );
				}
				
				end( "ows:Parameter" );
			}
			end( "ows:Operation" );
		}
		
		AttributesImpl attributes( String[] nameValues ) {
			AttributesImpl atts = new AttributesImpl();
			
			for ( int i = 0; i < nameValues.length; i += 2 ) {
				String name = nameValues[ i ];
				String valu = nameValues[ i + 1 ];
				
				atts.addAttribute( null, null, name, null, valu );
			
			}
			
			return atts;
		}
		
		Map.Entry parameter( final String name, final String[] values ) {
			return new Map.Entry() {
				public Object getKey() {
					return name;
				}
				public Object getValue() {
					return values;
				}
				public Object setValue( Object value ) {
					return null;
				}
			};
		}
	}
}
