package org.geoserver.wfs.xml.v1_1_0;


import org.geotools.xml.*;

import net.opengis.wfs.WFSFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wfs:OutputFormatListType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:complexType name="OutputFormatListType"&gt;
 *      &lt;xsd:sequence maxOccurs="unbounded"&gt;
 *          &lt;xsd:element name="Format" type="xsd:string"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class OutputFormatListTypeBinding extends AbstractComplexBinding {

	WFSFactory wfsfactory;		
	public OutputFormatListTypeBinding( WFSFactory wfsfactory ) {
		this.wfsfactory = wfsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WFS.OUTPUTFORMATLISTTYPE;
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