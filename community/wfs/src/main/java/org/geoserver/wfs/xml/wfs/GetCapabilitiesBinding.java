package org.geoserver.wfs.xml.wfs;


import org.geotools.xml.*;

import net.opengis.wfs.WFSFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/wfs:GetCapabilities.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:element name="GetCapabilities" type="wfs:GetCapabilitiesType"&gt;
 *          &lt;xsd:annotation&gt;          &lt;xsd:documentation&gt;             The
 *              GetCapapbilities element is used to request that a Web
 *              Feature             Service generate an XML document
 *              describing the organization             providing the
 *              service, the WFS operations that the service
 *              supports, a list of feature types that the service can
 *              operate             on and list of filtering capabilities
 *              that the service support.             Such an XML document
 *              is called a capabilities document.
 *          &lt;/xsd:documentation&gt;       &lt;/xsd:annotation&gt;    &lt;/xsd:element&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class GetCapabilitiesBinding extends AbstractComplexBinding {

	WFSFactory wfsfactory;		
	public GetCapabilitiesBinding( WFSFactory wfsfactory ) {
		this.wfsfactory = wfsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WFS.GETCAPABILITIES;
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