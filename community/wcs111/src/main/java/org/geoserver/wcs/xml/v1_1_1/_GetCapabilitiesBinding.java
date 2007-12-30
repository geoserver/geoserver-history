package org.geoserver.wcs.xml.v1_1_1;


import javax.xml.namespace.QName;

import net.opengis.wcs.v1_1_1.GetCapabilitiesType;
import net.opengis.wcs.v1_1_1.Wcs111Factory;

import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1.1:_GetCapabilities.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="_GetCapabilities"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="ows:GetCapabilitiesType"&gt;
 *              &lt;sequence/&gt;
 *              &lt;attribute fixed="WCS" name="service" type="ows:ServiceType" use="required"/&gt;
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
public class _GetCapabilitiesBinding extends AbstractComplexEMFBinding {

    public _GetCapabilitiesBinding(Wcs111Factory factory ) {
        super( factory );
    }
    
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
		
		//TODO: implement and remove call to super
		return super.parse(instance,node,value);
	}

}