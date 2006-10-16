package org.geoserver.wfs.xml.v1_0_0;


import org.geotools.xml.*;

import net.opengis.wfs.WFSFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wfs:FeaturesNotLockedType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:complexType name="FeaturesNotLockedType"&gt;
 *      &lt;xsd:sequence maxOccurs="unbounded"&gt;
 *          &lt;xsd:element ref="ogc:FeatureId"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class FeaturesNotLockedTypeBinding extends AbstractComplexBinding {

	WFSFactory wfsfactory;		
	public FeaturesNotLockedTypeBinding( WFSFactory wfsfactory ) {
		this.wfsfactory = wfsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WFS.FEATURESNOTLOCKEDTYPE;
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