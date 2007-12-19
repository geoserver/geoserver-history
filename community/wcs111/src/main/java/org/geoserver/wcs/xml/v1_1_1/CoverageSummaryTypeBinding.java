package org.geoserver.wcs.xml.v1_1_1;


import org.geotools.xml.*;
import org.geotools.xml.AbstractComplexEMFBinding;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1.1:CoverageSummaryType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="CoverageSummaryType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Brief metadata describing one or more coverages available from this WCS server. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="ows:DescriptionType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="ows:Metadata"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Optional unordered list of more metadata elements about this coverage. A list of metadata elements for CoverageSummaries could be specified in a WCS Application Profile. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="ows:WGS84BoundingBox"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Unordered list of minimum bounding rectangles surrounding coverage data, using WGS 84 CRS with decimal degrees and longitude before latitude. These bounding boxes shall also apply to lower-level CoverageSummaries under this CoverageSummary, unless other bounding boxes are specified. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0"
 *                      name="SupportedCRS" type="anyURI"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Unordered list of references to CRSs in which GetCoverage operation requests and responses may be expressed. These CRSs shall also apply to all lower-level CoverageSummaries under this CoverageSummary, in addition to any other CRSs referenced. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0"
 *                      name="SupportedFormat" type="ows:MimeType"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Unordered list of identifiers of formats in which GetCoverage operation responses may be encoded. These formats shall also apply to all lower-level CoverageSummaries under this CoverageSummary, in addition to any other formats identified. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;choice&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Each coverage summary shall contain one or more lower-level CoverageSummaries and/or one identifier. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                      &lt;sequence&gt;
 *                          &lt;element maxOccurs="unbounded" ref="wcs:CoverageSummary"&gt;
 *                              &lt;annotation&gt;
 *                                  &lt;documentation&gt;Unordered list of lower-level CoverageSummaries under this CoverageSummary. &lt;/documentation&gt;
 *                              &lt;/annotation&gt;
 *                          &lt;/element&gt;
 *                          &lt;element minOccurs="0" ref="wcs:Identifier"&gt;
 *                              &lt;annotation&gt;
 *                                  &lt;documentation&gt;Optional identifier of this coverage. This identifier shall be included only when this coverage can be accessed by the GetCoverage operation, and the CoverageDescription can be accessed by the DescribeCoverage operation. This identifier must be unique for this WCS server. &lt;/documentation&gt;
 *                              &lt;/annotation&gt;
 *                          &lt;/element&gt;
 *                      &lt;/sequence&gt;
 *                      &lt;element ref="wcs:Identifier"&gt;
 *                          &lt;annotation&gt;
 *                              &lt;documentation&gt;Identifier of this coverage. This identifier must be unique for this WCS server. &lt;/documentation&gt;
 *                          &lt;/annotation&gt;
 *                      &lt;/element&gt;
 *                  &lt;/choice&gt;
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
public class CoverageSummaryTypeBinding extends AbstractComplexEMFBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.CoverageSummaryType;
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