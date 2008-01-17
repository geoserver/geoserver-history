package org.geoserver.wcs.xml.v1_1_1;


import javax.xml.namespace.QName;

import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1.1:CoverageDescriptionType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="CoverageDescriptionType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Full description of one coverage available from a WCS server. This description shall include sufficient information to allow all valid GetCoverage operation requests to be prepared by a WCS client. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="ows:DescriptionType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element ref="wcs:Identifier"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Unambiguous identifier of this coverage, unique for this WCS server. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="ows:Metadata"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Optional unordered list of more metadata elements about this coverage. A list of metadata elements for CoverageDescriptions could be specified in a WCS Application Profile. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element name="Domain" type="wcs:CoverageDomainType"/&gt;
 *                  &lt;element name="Range" type="wcs:RangeType"/&gt;
 *                  &lt;element maxOccurs="unbounded" name="SupportedCRS" type="anyURI"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Unordered list of references to all the coordinate reference systems in which GetCoverage operation requests and responses can be encoded for this coverage. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element maxOccurs="unbounded" name="SupportedFormat" type="ows:MimeType"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Unordered list of identifiers of all the formats in which GetCoverage operation responses can be encoded for this coverage. &lt;/documentation&gt;
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
public class CoverageDescriptionTypeBinding extends AbstractComplexEMFBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.CoverageDescriptionType;
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