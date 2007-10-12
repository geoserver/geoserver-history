package org.geoserver.wcs.xml.v1_1_0;


import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1:DomainSubsetType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="DomainSubsetType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Definition of the desired subset of the domain of the coverage. Contains a spatial BoundingBox and optionally a TemporalSubset. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element ref="ows:BoundingBox"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Definition of desired spatial subset of a coverage domain. When the entire spatail extent of this coverage is desired, this BoundingBox can be copied from the Domain part of the Coverage Description. However, the entire spatial extent may be larger than a WCS server can output, in which case the server shall respond with an error message. Notice that WCS use of this BoundingBox is further specified in specification Subclause 7.5. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element minOccurs="0" ref="wcs:TemporalSubset"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Optional definition of desired temporal subset of a coverage domain. If this data structure is omitted, the entire Temporal domain shall be output. &lt;/documentation&gt;
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
public class DomainSubsetTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.DomainSubsetType;
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