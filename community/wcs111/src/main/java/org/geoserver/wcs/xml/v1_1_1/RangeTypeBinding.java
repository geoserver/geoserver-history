package org.geoserver.wcs.xml.v1_1_1;


import org.geotools.xml.*;
import org.geotools.xml.AbstractComplexEMFBinding;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1.1:RangeType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="RangeType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Defines the fields (categories, measures, or values) in the range records available for each location in the coverage domain. Each such field may be a scalar (numeric or text) value, such as population density, or a vector (compound or tensor) of many similar values, such as incomes by race, or radiances by wavelength. Each range field is typically an observable whose meaning and reference system are referenced by URIs. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs="unbounded" name="Field" type="wcs:FieldType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Unordered list of the Fields in the Range of a coverage. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *      &lt;/sequence&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class RangeTypeBinding extends AbstractComplexEMFBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.RangeType;
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