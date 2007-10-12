package org.geoserver.wcs.xml.v1_1_0;


import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1:SpatialDomainType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="SpatialDomainType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Definition of the spatial domain of a coverage. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs="unbounded" ref="ows:BoundingBox"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Unordered list of bounding boxes whose union covers the spatial domain of this coverage. One bounding box could simply duplicate the information in the ows:WGS84BoundingBox in the CoverageSummary; but the intent is to describe the spatial domain in more detail (e.g., in several different CRSs, or several rectangular areas instead of one overall bounding box). This bounding box shall use the ImageCRS of the stored coverage when that coverage is not georeferenced and not georectified. Notice that WCS use of this BoundingBox is further specified in specification Subclause 7.5. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" ref="wcs:GridCRS"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Definition of GridCRS of the stored coverage. This GridCRS shall be included when this coverage is georectified and is thus stored in a GridCRS. The definition of the GridCRS of a stored georectified coverage is included to inform clients of this GridCRS, for possible use in a GetCoverage operation request. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" name="Transformation" type="gml:AbstractCoordinateOperationType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Georeferencing coordinate transformation for unrectified coverage, which should be included when available for a coverage that is georeferenced but not georectified. To support use cases 4, 5, 9, and/or 10 specified in Annex H, a WCS server needs to use a georeferencing coordinate transformation for a georeferenced but not georectified coverage. That georeferencing transformation can be specified as a Transformation, or a ConcatenatedOperation that includes at least one Transformation. However, a WCS server may not support those use cases, not use a georeferencing transformation specified in that manner, or not make that transformation available to clients. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" name="ImageCRS" type="wcs:ImageCRSRefType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Association to ImageCRS of the stored coverage. This ImageCRS shall be included when this coverage is an image. The ImageCRS for an image coverage is referenced to inform clients of the ImageCRS, for possible use in a GetCoverage operation request. The definition of this ImageCRS shall be included unless the association reference URI completely specifies that ImageCRS (such as by using the URL of the definition or using a suitable URN). &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:Polygon"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Unordered list of polygons whose union covers the spatial domain of this coverage. Polygons are particularly useful for areas that are poorly approximated by a BoundingBox (such as satellite image swaths, island groups, other non-convex areas). &lt;/documentation&gt;
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
public class SpatialDomainTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.SpatialDomainType;
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