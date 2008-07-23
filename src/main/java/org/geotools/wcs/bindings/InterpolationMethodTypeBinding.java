package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:InterpolationMethodType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;simpleType name="InterpolationMethodType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Codes that identify interpolation methods. The meanings of these codes are defined in Annex B of ISO 19123: Geographic information — Schema for coverage geometry and functions. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;restriction base="string"&gt;
 *          &lt;enumeration value="nearest neighbor"/&gt;
 *          &lt;enumeration value="bilinear"/&gt;
 *          &lt;enumeration value="bicubic"/&gt;
 *          &lt;enumeration value="lost area"/&gt;
 *          &lt;enumeration value="barycentric"/&gt;
 *          &lt;enumeration value="none"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;No interpolation. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/enumeration&gt;
 *      &lt;/restriction&gt;
 *  &lt;/simpleType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class InterpolationMethodTypeBinding extends AbstractSimpleBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.InterpolationMethodType;
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