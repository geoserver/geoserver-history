package org.geoserver.wfs.xml.v1_1_0;


import org.geotools.xml.*;

import net.opengis.wfs.WFSFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wfs:InsertResultType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:complexType name="InsertResultType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation xml:lang="en"&gt;
 *              Reports the list of identifiers of all features created 
 *              by a transaction request.  New features are created using
 *              the Insert action and the list of idetifiers must be 
 *              presented in the same order as the Insert actions were
 *              encountered in the transaction request.  Features may
 *              optionally be correlated with identifiers using the 
 *              handle attribute (if it was specified on the Insert 
 *              element).
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="unbounded" name="Feature" type="wfs:InsertedFeatureType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class InsertResultTypeBinding extends AbstractComplexBinding {

	WFSFactory wfsfactory;		
	public InsertResultTypeBinding( WFSFactory wfsfactory ) {
		this.wfsfactory = wfsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WFS.INSERTRESULTTYPE;
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
	public Object parse(ElementInstance instance, Node node, Object value) 
		throws Exception {
		
		//TODO: implement
		return null;
	}

}