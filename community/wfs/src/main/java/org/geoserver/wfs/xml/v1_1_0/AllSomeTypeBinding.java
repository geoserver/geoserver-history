package org.geoserver.wfs.xml.v1_1_0;


import org.geotools.xml.*;

import net.opengis.wfs.WFSFactory;		

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

	WFSFactory wfsfactory;		
	public AllSomeTypeBinding( WFSFactory wfsfactory ) {
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