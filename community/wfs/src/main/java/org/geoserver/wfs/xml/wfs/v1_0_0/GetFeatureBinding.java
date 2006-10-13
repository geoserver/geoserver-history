package org.geoserver.wfs.xml.wfs.v1_0_0;


import org.geotools.xml.*;

import net.opengis.wfs.WfsFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/wfs:GetFeature.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:element name="GetFeature" type="wfs:GetFeatureType"&gt;
 *          &lt;xsd:annotation&gt;          &lt;xsd:documentation&gt;             The
 *              GetFeature element is used to request that a Web Feature
 *              Service return feature instances of one or more feature
 *              types.          &lt;/xsd:documentation&gt;       &lt;/xsd:annotation&gt;    &lt;/xsd:element&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class GetFeatureBinding extends AbstractComplexBinding {

	WfsFactory wfsfactory;		
	public GetFeatureBinding( WfsFactory wfsfactory ) {
		this.wfsfactory = wfsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WFS.GETFEATURE;
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