package org.geoserver.wcs.xml.v1_1_1;


import net.opengis.ows.GetCapabilitiesType;

import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1.1:_Capabilities.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="_Capabilities"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="ows:CapabilitiesBaseType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element minOccurs="0" ref="wcs:Contents"/&gt;
 *              &lt;/sequence&gt;
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
public class _CapabilitiesBinding extends AbstractComplexEMFBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS._Capabilities;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return GetCapabilitiesType.class;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(ElementInstance instance, Node node, Object value) 
		throws Exception {
		return super.parse(instance,node,value);
	}

}