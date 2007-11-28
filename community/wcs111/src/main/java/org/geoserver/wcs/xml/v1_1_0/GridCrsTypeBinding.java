package org.geoserver.wcs.xml.v1_1_0;


import org.geotools.xml.*;
import org.geotools.xml.AbstractComplexEMFBinding;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1.1:GridCrsType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="GridCrsType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Definition of a coordinate reference system (CRS) for a quadrilateral grid that is defined in another CRS, where this grid is defined by its coordinate Conversion from the other CRS. This GridCRS is not a ProjectedCRS. However, like a ProjectedCRS, the coordinate system used is Cartesian. This GridCRS can use any type of baseCRS, including GeographicCRS, ProjectedCRS, ImageCRS, or a different GridCRS. &lt;/documentation&gt;
 *          &lt;documentation&gt;This GridCRS is a simplification and specialization of a gml:DerivedCRS. All elements and attributes not required to define this GridCRS are optional. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element minOccurs="0" ref="gml:srsName"/&gt;
 *          &lt;element ref="wcs:GridBaseCRS"/&gt;
 *          &lt;element minOccurs="0" ref="wcs:GridType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;When this GridType reference is omitted, the OperationMethod shall be the most commonly used method in a GridCRS, which is referenced by the default URN "urn:ogc:def:method:WCS:1.1:2dSimpleGrid". &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" ref="wcs:GridOrigin"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;When this GridOrigin position is omitted, the origin defaults be the most commonly used origin in a GridCRS used in the output part of a GetCapabilities operation request, namely "0 0". &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element ref="wcs:GridOffsets"/&gt;
 *          &lt;element minOccurs="0" ref="wcs:GridCS"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;When this GridCS reference is omitted, the GridCS defaults to the most commonly used grid coordinate system, which is referenced by the URN "urn:ogc:def:cs:OGC:0.0:Grid2dSquareCS". &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *      &lt;/sequence&gt;
 *      &lt;attribute ref="gml:id" use="optional"/&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class GridCrsTypeBinding extends AbstractComplexEMFBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.GridCrsType;
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