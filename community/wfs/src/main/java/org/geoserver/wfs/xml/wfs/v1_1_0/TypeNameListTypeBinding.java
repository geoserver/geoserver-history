package org.geoserver.wfs.xml.wfs.v1_1_0;


import org.geotools.xml.*;

import net.opengis.wfs.WfsFactory;		

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

	WfsFactory wfsfactory;		
	public TypeNameListTypeBinding( WfsFactory wfsfactory ) {
		this.wfsfactory = wfsfactory;
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
		return null;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(InstanceComponent instance, Object value) 
		throws Exception {
		
		//TODO: implement
		return null;
	}

}