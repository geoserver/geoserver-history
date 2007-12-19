package org.geoserver.wcs.xml.v1_1_1;


import org.geotools.xml.*;
import org.geotools.xml.AbstractComplexEMFBinding;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1.1:_InterpolationMethods.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="_InterpolationMethods"&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs="unbounded" name="InterpolationMethod" type="wcs:InterpolationMethodType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Unordered list of identifiers of spatial interpolation methods. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" name="Default" type="string"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Identifier of interpolation method that will be used if the client does not specify one. Should be included when a default exists and is known. &lt;/documentation&gt;
 *                  &lt;documentation&gt;(Arliss) Can any string be used to identify an interpolation method in a KVP encoded Get Coverage operation request? &lt;/documentation&gt;
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
public class _InterpolationMethodsBinding extends AbstractComplexEMFBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS._InterpolationMethods;
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