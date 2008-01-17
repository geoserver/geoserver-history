package org.geoserver.wcs.xml.v1_1_1;


import javax.xml.namespace.QName;

import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1.1:_GetCoverage.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="_GetCoverage"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="wcs:RequestBaseType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element ref="ows:Identifier"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Identifier of the coverage that this GetCoverage operation request shall draw from. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element name="DomainSubset" type="wcs:DomainSubsetType"/&gt;
 *                  &lt;element minOccurs="0" name="RangeSubset" type="wcs:RangeSubsetType"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Optional selection of a subset of the coverage's range. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element name="Output" type="wcs:OutputType"/&gt;
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
public class _GetCoverageBinding extends AbstractComplexEMFBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS._GetCoverage;
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