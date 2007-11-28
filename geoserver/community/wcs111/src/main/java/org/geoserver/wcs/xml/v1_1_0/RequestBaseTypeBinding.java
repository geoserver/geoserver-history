package org.geoserver.wcs.xml.v1_1_0;


import org.geotools.xml.*;
import org.geotools.xml.AbstractComplexEMFBinding;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1.1:RequestBaseType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="RequestBaseType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;XML encoded WCS operation request base, for all operations except GetCapabilities. In this XML encoding, no "request" parameter is included, since the element name specifies the specific operation. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence/&gt;
 *      &lt;attribute fixed="WCS" name="service" type="string" use="required"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;Service type identifier, where the value is the OWS type abbreviation. For WCS operation requests, the value is "WCS". &lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attribute&gt;
 *      &lt;attribute fixed="1.1.1" name="version" type="string" use="required"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;Specification version for WCS version and operation. See Version parameter Subclause 7.3.1 of OWS Common for more information. &lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attribute&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class RequestBaseTypeBinding extends AbstractComplexEMFBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.RequestBaseType;
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
		
		//TODO: implement and remove call to super
		return super.parse(instance,node,value);
	}

}