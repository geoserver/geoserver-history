package org.geoserver.wcs.xml.v1_1_0;


import org.geotools.xml.*;
import org.geotools.xml.AbstractComplexEMFBinding;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1.1:InterpolationMethodType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="InterpolationMethodType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Identifier of a spatial interpolation method applicable to continuous grid coverages, plus the optional "null Resistance" parameter. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;simpleContent&gt;
 *          &lt;extension base="wcs:InterpolationMethodBaseType"&gt;
 *              &lt;attribute name="nullResistance" type="string" use="optional"&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;Identifier of how the server handles null values when spatially interpolating values in this field using this interpolation method. This identifier shall be selected from the referenced dictionary. This parameter shall be omitted when the rule for handling nulls is unknown. &lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/attribute&gt;
 *          &lt;/extension&gt;
 *      &lt;/simpleContent&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class InterpolationMethodTypeBinding extends AbstractComplexEMFBinding {

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
	public Object parse(ElementInstance instance, Node node, Object value) 
		throws Exception {
		
		//TODO: implement and remove call to super
		return super.parse(instance,node,value);
	}

}