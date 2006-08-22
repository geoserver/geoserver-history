package org.geoserver.xml.ows.v1_0_0;


import org.geotools.xml.*;

import net.opengis.ows.v1_0_0.OWSFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/ows:PositionType2D.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;simpleType name="PositionType2D"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Two-dimensional position instances hold the longitude and latitude coordinates of a position in the 2D WGS 84 coordinate reference system. The longitude value shall be listed first, followed by the latitude value, both in decimal degrees. Latitude values shall range from -90 to +90 degrees, and longitude values shall normally range from -180 to +180 degrees. For the longitude axis, special conditions apply when the bounding box is continuous across the +/- 180 degrees meridian longitude value discontinuity:
 *  a)  If the bounding box is continuous clear around the Earth, then longitude values of minus and plus infinity shall be used.
 *  b)  If the bounding box is continuous across the value discontinuity but is not continuous clear around the Earth, then some non-normal value can be used if specified for a specific OWS use of the WGS84BoundingBoxType. For more information, see Subclauses 10.4.5 and C.13. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;restriction base="ows:PositionType"&gt;
 *          &lt;length value="2"/&gt;
 *      &lt;/restriction&gt;
 *  &lt;/simpleType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class PositionType2DBinding extends AbstractSimpleBinding {

	OWSFactory owsfactory;		
	public PositionType2DBinding( OWSFactory owsfactory ) {
		this.owsfactory = owsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return OWS.POSITIONTYPE2D;
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
	public Object parse(InstanceComponent instance, Object value) 
		throws Exception {
		
		//TODO: implement
		return null;
	}

}