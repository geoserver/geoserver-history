package org.geoserver.wcs.xml.v1_1_1;


import javax.xml.namespace.QName;

import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1.1:TimeDurationType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;simpleType name="TimeDurationType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation xml:lang="en"&gt;
 *        Base type for describing temporal length or distance. The value space is further 
 *        constrained by subtypes that conform to the ISO 8601 or ISO 11404 standards.
 *        &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;union memberTypes="duration decimal"/&gt;
 *  &lt;/simpleType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class TimeDurationTypeBinding extends AbstractSimpleBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.TimeDurationType;
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
		
		//TODO: implement and remove call to super
		return super.parse(instance,value);
	}

}