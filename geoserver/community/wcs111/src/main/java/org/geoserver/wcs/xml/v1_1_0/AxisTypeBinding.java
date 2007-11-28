package org.geoserver.wcs.xml.v1_1_0;


import org.geotools.xml.*;
import org.geotools.xml.AbstractComplexEMFBinding;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1.1:AxisType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="AxisType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Definition of one axis in a field for which there are a vector of values. &lt;/documentation&gt;
 *          &lt;documentation&gt;This type is largely a subset of the ows:DomainType as needed for a range field axis. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="ows:DescriptionType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element ref="wcs:AvailableKeys"/&gt;
 *                  &lt;element minOccurs="0" ref="ows:Meaning"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Meaning metadata, which should be referenced for this axis. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element minOccurs="0" ref="ows:DataType"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Data type metadata, which may be referenced for this axis. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;group minOccurs="0" ref="ows:ValuesUnit"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Unit of measure, which should be included when this axis has units or a more complete reference system. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/group&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="ows:Metadata"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Optional unordered list of other metadata elements about this axis. A list of required and optional other metadata elements for this quantity can be specified in a WCS Application Profile. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *              &lt;/sequence&gt;
 *              &lt;attribute name="identifier" type="wcs:IdentifierType" use="required"&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;Name or identifier of this axis. &lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/attribute&gt;
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
public class AxisTypeBinding extends AbstractComplexEMFBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.AxisType;
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