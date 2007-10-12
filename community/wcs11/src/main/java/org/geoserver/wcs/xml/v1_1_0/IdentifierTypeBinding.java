package org.geoserver.wcs.xml.v1_1_0;


import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1:IdentifierType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;simpleType name="IdentifierType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Unambiguous identifier. Although there is no formal restriction on characters included, these identifiers shall be directly usable in GetCoverage operation requests for the specific server, whether those requests are encoded in KVP or XML. Each of these encodings requires that certain characters be avoided, encoded, or escaped (TBR). &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;restriction base="string"&gt;
 *          &lt;pattern value=".+"/&gt;
 *      &lt;/restriction&gt;
 *  &lt;/simpleType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class IdentifierTypeBinding extends AbstractSimpleBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.IdentifierType;
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