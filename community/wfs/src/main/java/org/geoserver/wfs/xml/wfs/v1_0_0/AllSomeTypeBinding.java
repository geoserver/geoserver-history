package org.geoserver.wfs.xml.wfs.v1_0_0;


import org.geotools.xml.*;

import net.opengis.wfs.AllSomeType;
import net.opengis.wfs.WfsFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wfs:AllSomeType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:simpleType name="AllSomeType"&gt;
 *      &lt;xsd:restriction base="xsd:string"&gt;
 *          &lt;xsd:enumeration value="ALL"/&gt;
 *          &lt;xsd:enumeration value="SOME"/&gt;
 *      &lt;/xsd:restriction&gt;
 *  &lt;/xsd:simpleType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class AllSomeTypeBinding extends AbstractSimpleBinding {

	WfsFactory wfsfactory;		
	public AllSomeTypeBinding( WfsFactory wfsfactory ) {
		this.wfsfactory = wfsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WFS.ALLSOMETYPE;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return AllSomeType.class;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(InstanceComponent instance, Object value) 
		throws Exception {
		
		if ( "ALL".equals( value ) ) {
			return AllSomeType.ALL_LITERAL;
		}
		
		if ( "SOME".equals( value ) ) {
			return AllSomeType.SOME_LITERAL;
		}
		
		return null;
	}

}