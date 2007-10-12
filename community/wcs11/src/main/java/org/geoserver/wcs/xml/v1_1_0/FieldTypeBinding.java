package org.geoserver.wcs.xml.v1_1_0;


import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1:FieldType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="FieldType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Description of an individual field in a coverage range record. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="owcs:DescriptionType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element ref="wcs:Identifier"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Identifier of this Field. These field identifiers shall be unique in one CoverageDescription. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element name="Definition" type="owcs:UnNamedDomainType"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Further definition of this field, including meaning, units, etc. In this Definition, the AllowedValues should be used to encode the extent of possible values for this field, excluding the Null Value. If the range is not known, AnyValue should be used. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0"
 *                      name="NullValue" type="ows:CodeType"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Unordered list of the values used when valid Field values are not available for whatever reason. The coverage encoding itself may specify a fixed value for null (e.g. “–99999” or “N/A”), but often the choice is up to the provider and must be communicated to the client outside the coverage itself. Each null value shall be encoded as a string. The optional codeSpace attribute can reference a definition of the reason why this value is null. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element ref="owcs:InterpolationMethods"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Spatial interpolation method(s) that server can apply to this field. One of these interpolation methods shall be used when a GetCoverage operation request requires resampling. When the only interpolation method listed is ‘none’, clients may only retrieve coverages from this coverage in its native CRS at its native resolution. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" name="Axis" type="wcs:AxisType"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Unordered list of the axes in a vector field for which there are Field values. This list shall be included when this Field has a vector of values. Notice that the axes can be listed here in any order; however, the axis order listed here shall be used in the KVP encoding of a GetCoverage operation request (TBR). &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
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
public class FieldTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.FieldType;
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