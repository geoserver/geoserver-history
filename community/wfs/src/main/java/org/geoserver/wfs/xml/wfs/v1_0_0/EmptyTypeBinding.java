package org.geoserver.wfs.xml.wfs.v1_0_0;


import org.geotools.xml.*;

import net.opengis.wfs.WfsFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wfs:EmptyType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:complexType name="EmptyType"/&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class EmptyTypeBinding extends AbstractComplexBinding {

	WfsFactory wfsfactory;		
	public EmptyTypeBinding( WfsFactory wfsfactory ) {
		this.wfsfactory = wfsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WFS.EMPTYTYPE;
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