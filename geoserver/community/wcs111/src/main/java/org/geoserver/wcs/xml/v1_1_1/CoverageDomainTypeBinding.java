package org.geoserver.wcs.xml.v1_1_1;


import javax.xml.namespace.QName;

import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1.1:CoverageDomainType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="CoverageDomainType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Definition of the spatial-temporal domain of a coverage. The Domain shall include a SpatialDomain (describing the spatial locations for which coverages can be requested), and should included a TemporalDomain (describing the time instants or intervals for which coverages can be requested). &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element name="SpatialDomain" type="wcs:SpatialDomainType"/&gt;
 *          &lt;element minOccurs="0" ref="wcs:TemporalDomain"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Although optional, the TemporalDomain should be included whenever a value is known or a useful estimate is available. &lt;/documentation&gt;
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
public class CoverageDomainTypeBinding extends AbstractComplexEMFBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.CoverageDomainType;
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