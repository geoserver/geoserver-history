package org.geoserver.wcs.xml.v1_1_0;


import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1:TimePeriodType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="TimePeriodType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;This is a variation of the GML TimePeriod, which allows the beginning and end of a time-period to be expressed in short-form inline using the begin/endPosition element, which allows an identifiable TimeInstant to be defined simultaneously with using it, or by reference, using xlinks on the begin/end elements. &lt;/documentation&gt;
 *          &lt;documentation&gt;(Arliss) What does this mean? What do the TimeResolution and "frame" mean? &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element name="BeginPosition" type="gml:TimePositionType"/&gt;
 *          &lt;element name="EndPosition" type="gml:TimePositionType"/&gt;
 *          &lt;element minOccurs="0" name="TimeResolution" type="wcs:TimeDurationType"/&gt;
 *      &lt;/sequence&gt;
 *      &lt;attribute default="#ISO-8601" name="frame" type="anyURI" use="optional"/&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class TimePeriodTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.TimePeriodType;
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