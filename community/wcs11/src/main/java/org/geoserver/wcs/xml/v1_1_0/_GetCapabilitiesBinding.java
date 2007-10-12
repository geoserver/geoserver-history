package org.geoserver.wcs.xml.v1_1_0;


import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1:_GetCapabilities.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="_GetCapabilities"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="owcs:GetCapabilitiesType"&gt;
 *              &lt;sequence/&gt;
 *              &lt;attribute fixed="WCS" name="service"
 *                  type="owcs:ServiceType" use="required"/&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class _GetCapabilitiesBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS._GetCapabilities;
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