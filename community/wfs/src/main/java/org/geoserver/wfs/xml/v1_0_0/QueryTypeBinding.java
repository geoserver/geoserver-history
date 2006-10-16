package org.geoserver.wfs.xml.v1_0_0;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geotools.xml.*;
import org.geotools.xs.bindings.XSQNameBinding;
import org.opengis.feature.Feature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.helpers.NamespaceSupport;

import net.opengis.wfs.QueryType;
import net.opengis.wfs.WFSFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wfs:QueryType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:complexType name="QueryType"&gt;       
 *  		&lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;           
 *              The Query element is of type
 *              QueryType.          
 *              &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;      
 *      &lt;xsd:sequence&gt;        
 *      		&lt;xsd:element
 *              maxOccurs="unbounded" minOccurs="0" ref="ogc:PropertyName"&gt;
 *                  &lt;xsd:annotation&gt;              &lt;xsd:documentation&gt;
 *                      The PropertyName element is used to specify one or
 *                      more                 properties of a feature whose
 *                      values are to be retrieved                 by a Web
 *                      Feature Service.
 *                      While a Web Feature Service should endeavour to
 *                      satisfy                 the exact request specified,
 *                      in some instance this may                 not be
 *                      possible.  Specifically, a Web Feature Service
 *                      must generate a valid GML2 response to a Query
 *                      operation.                 The schema used to
 *                      generate the output may include
 *                      properties that are mandatory.  In order that the
 *                      output                 validates, these mandatory
 *                      properties must be specified                 in the
 *                      request.  If they are not, a Web Feature Service
 *                      may add them automatically to the Query before
 *                      processing                 it.  Thus a client
 *                      application should, in general, be
 *                      prepared to receive more properties than it
 *                      requested.                  Of course, using the
 *                      DescribeFeatureType request, a client
 *                      application can determine which properties are
 *                      mandatory                 and request them in the
 *                      first place.              &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;        &lt;/xsd:element&gt;        &lt;xsd:element
 *              maxOccurs="1" minOccurs="0" ref="ogc:Filter"&gt;
 *                  &lt;xsd:annotation&gt;              &lt;xsd:documentation&gt;
 *                      The Filter element is used to define spatial and/or
 *                      non-spatial                 constraints on query.
 *                      Spatial constrains use GML2 to specify
 *                      the constraining geometry.  A full description of
 *                      the Filter                 element can be found in
 *                      the Filter Encoding Implementation
 *                      Specification.              &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;        &lt;/xsd:element&gt;      &lt;/xsd:sequence&gt;
 *          &lt;xsd:attribute name="handle" type="xsd:string" use="optional"/&gt;
 *          &lt;xsd:attribute name="typeName" type="xsd:QName" use="required"/&gt;
 *          &lt;xsd:attribute name="featureVersion" type="xsd:string"
 *          use="optional"&gt;         &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;               For systems that
 *                  implement versioning, the featureVersion
 *                  attribute is used to specify which version of a
 *                  particular               feature instance is to be
 *                  retrieved.  A value of ALL means               that all
 *                  versions should be retrieved.  An integer value
 *                  &apos;i&apos;, means that the ith version should be
 *                  retrieve if it               exists or the most recent
 *                  version otherwise.            &lt;/xsd:documentation&gt;
 *          &lt;/xsd:annotation&gt;      &lt;/xsd:attribute&gt;    &lt;/xsd:complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class QueryTypeBinding extends AbstractComplexBinding {

	/**
	 * Wfs Factory
	 */
	WFSFactory wfsfactory;
	/**
	 * namespace mappings
	 */
	NamespaceSupport namespaceSupport;
	
	public QueryTypeBinding( WFSFactory wfsfactory, NamespaceSupport namespaceSupport ) {
		this.wfsfactory = wfsfactory;
		this.namespaceSupport = namespaceSupport;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WFS.QUERYTYPE;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return QueryType.class;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(ElementInstance instance, Node node, Object value) 
		throws Exception {
		
		QueryType queryType = wfsfactory.createQueryType();
		
		//<xsd:element maxOccurs="unbounded" minOccurs="0" ref="ogc:PropertyName">
		//JD:difference in spec here, moved from ogc:PropertyName to QName
		List propertyNames = node.getChildren( PropertyName.class );
		for ( Iterator p = propertyNames.iterator(); p.hasNext(); ) {
			Node propertyName = (Node) p.next();
			
			//TODO: PropertyName binding throws away namespace prefix so we need
			// to use the raw unparsed text as the value to teh qname binding
			QName qName = (QName) new XSQNameBinding( namespaceSupport ).parse( 
				propertyName.getComponent(), propertyName.getComponent().getText() 	
			);
		
			queryType.getPropertyName().add( qName );
		}
		
		//<xsd:element maxOccurs="1" minOccurs="0" ref="ogc:Filter">
		Filter filter = (Filter) node.getChildValue( Filter.class );
		if ( filter == null ) {
			filter = (Filter) org.geotools.filter.Filter.NONE;
		}
		queryType.setFilter( filter );
		
		//<xsd:attribute name="handle" type="xsd:string" use="optional"/>
		queryType.setHandle( (String) node.getAttributeValue( "handle" ) );
		
		//<xsd:attribute name="typeName" type="xsd:QName" use="required"/>
		List typeNameList = new ArrayList();
		typeNameList.add( node.getAttributeValue( "typeName" ) );
        queryType.setTypeName( typeNameList );
		
		//<xsd:attribute name="featureVersion" type="xsd:string" use="optional">  
		queryType.setFeatureVersion( 
			(String) node.getAttributeValue( "featureVersion" )
		);
		
		return queryType;
	}

}