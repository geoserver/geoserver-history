package org.geoserver.wfs.xml.v1_1_0;


import java.util.ArrayList;
import java.util.List;

import org.geotools.xml.*;
import org.geotools.xs.bindings.XSQNameBinding;
import org.xml.sax.helpers.NamespaceSupport;

import net.opengis.wfs.WFSFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wfs:TypeNameListType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:simpleType name="TypeNameListType"&gt;
 *      &lt;xsd:restriction base="wfs:Base_TypeNameListType"&gt;
 *          &lt;xsd:pattern value="((\w:)?\w(=\w)?){1,}"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                    Example typeName attribute value might be:
 *  
 *                       typeName="ns1:Inwatera_1m=A, ns2:CoastL_1M=B"
 *  
 *                    In this example, A is an alias for ns1:Inwatera_1m
 *                    and B is an alias for ns2:CoastL_1M.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:pattern&gt;
 *      &lt;/xsd:restriction&gt;
 *  &lt;/xsd:simpleType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class TypeNameListTypeBinding extends AbstractSimpleBinding {

	WFSFactory wfsfactory;		
	NamespaceSupport namespaceSupport;
	
	public TypeNameListTypeBinding( WFSFactory wfsfactory, NamespaceSupport namespaceSupport ) {
		this.wfsfactory = wfsfactory;
		this.namespaceSupport = namespaceSupport;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WFS.TYPENAMELISTTYPE;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return List.class;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(InstanceComponent instance, Object value) 
		throws Exception {
		
		//TODO: implement list support in parser so that passed in value is a list
		//&lt;xsd:pattern value="((\w:)?\w(=\w)?){1,}"&gt;
		
		String[] tokens = ( (String) value ).split( "," );
		List qNames = new ArrayList();
		for ( int i = 0; i < tokens.length; i++ ) {
			//skip aliases for now
			int index = tokens[ i ].indexOf( "=" );
			if ( index != -1 ) {
				tokens[ i ] = tokens[ i ].substring( 0 , i );
			}
			
			qNames.add(  
				(QName) new XSQNameBinding( namespaceSupport ).parse( instance, tokens[ i ] )
			);
		}
		
		return qNames;
		
	}

}