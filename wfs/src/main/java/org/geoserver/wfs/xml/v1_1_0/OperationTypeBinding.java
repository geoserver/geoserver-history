package org.geoserver.wfs.xml.v1_1_0;


import javax.xml.namespace.QName;

import net.opengis.wfs.WFSFactory;

import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;

/**
 * Binding object for the type http://www.opengis.net/wfs:OperationType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:simpleType name="OperationType"&gt;
 *      &lt;xsd:restriction base="xsd:string"&gt;
 *          &lt;xsd:enumeration value="Insert"/&gt;
 *          &lt;xsd:enumeration value="Unsert"/&gt;
 *          &lt;xsd:enumeration value="Delete"/&gt;
 *          &lt;xsd:enumeration value="Query"/&gt;
 *          &lt;xsd:enumeration value="Lock"/&gt;
 *          &lt;xsd:enumeration value="GetGmlObject"/&gt;
 *      &lt;/xsd:restriction&gt;
 *  &lt;/xsd:simpleType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class OperationTypeBinding extends AbstractSimpleBinding {

	WFSFactory wfsfactory;		
	public OperationTypeBinding( WFSFactory wfsfactory ) {
		this.wfsfactory = wfsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WFS.OPERATIONTYPE;
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