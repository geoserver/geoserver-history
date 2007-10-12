package org.geoserver.wcs.xml.v1_1_0;


import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1:RangeSubsetType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="RangeSubsetType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Selection of desired subset of the coverage's range fields, (optionally) the interpolation method applied to eachfield, and (optionally) field subsets. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs="unbounded" name="FieldSubset"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Unordered list of one or more desired subsets of range fields. TBD. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *              &lt;complexType name="RangeSubsetType_FieldSubset"&gt;
 *                  &lt;sequence&gt;
 *                      &lt;element ref="owcs:Identifier"&gt;
 *                          &lt;annotation&gt;
 *                              &lt;documentation&gt;Identifier of this requested Field. This identifier must be unique for this Coverage. &lt;/documentation&gt;
 *                          &lt;/annotation&gt;
 *                      &lt;/element&gt;
 *                      &lt;element minOccurs="0" name="InterpolationType" type="string"&gt;
 *                          &lt;annotation&gt;
 *                              &lt;documentation&gt;Optional identifier of the spatial interpolation type to be applied to this range field. This interpolation type shall be one that is identified for this Field in the CoverageDescription. When this parameter is omitted, the interpolation method used shall be the default method specified for this Field, if any. &lt;/documentation&gt;
 *                          &lt;/annotation&gt;
 *                      &lt;/element&gt;
 *                      &lt;element maxOccurs="unbounded" minOccurs="0" ref="wcs:AxisSubset"&gt;
 *                          &lt;annotation&gt;
 *                              &lt;documentation&gt;Unordered list of zero or more axis subsets for this field. TBD. &lt;/documentation&gt;
 *                          &lt;/annotation&gt;
 *                      &lt;/element&gt;
 *                  &lt;/sequence&gt;
 *              &lt;/complexType&gt;
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
public class RangeSubsetTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.RangeSubsetType;
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