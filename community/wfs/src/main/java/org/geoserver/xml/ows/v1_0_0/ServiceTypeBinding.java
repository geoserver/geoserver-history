package org.geoserver.xml.ows.v1_0_0;


import org.geotools.xml.*;

import net.opengis.ows.v1_0_0.OWSFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/ows:ServiceType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;simpleType name="ServiceType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Service type identifier, where the string value is the OWS type abbreviation, such as "WMS" or "WFS". &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;restriction base="string"/&gt;
 *  &lt;/simpleType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class ServiceTypeBinding extends AbstractSimpleBinding {

	OWSFactory owsfactory;		
	public ServiceTypeBinding( OWSFactory owsfactory ) {
		this.owsfactory = owsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return OWS.SERVICETYPE;
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
	public Object parse(InstanceComponent instance, Object value) 
		throws Exception {
		
		//TODO: implement
		return null;
	}

}