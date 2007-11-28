package org.geoserver.wcs.xml.v1_1_0;


import org.geotools.xml.*;
import org.geotools.xml.AbstractComplexEMFBinding;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1.1:TimeSequenceType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="TimeSequenceType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;List of time positions and periods. The time positions and periods should be ordered from the oldest to the newest, but this is not required. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;choice maxOccurs="unbounded"&gt;
 *          &lt;element ref="gml:timePosition"/&gt;
 *          &lt;element name="TimePeriod" type="wcs:TimePeriodType"/&gt;
 *      &lt;/choice&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class TimeSequenceTypeBinding extends AbstractComplexEMFBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.TimeSequenceType;
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