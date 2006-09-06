package org.geoserver.xml.ows.v1_0_0;


import org.geotools.xml.*;

import net.opengis.ows.v1_0_0.OWSFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/ows:UpdateSequenceType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;simpleType name="UpdateSequenceType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Service metadata document version, having values that are "increased" whenever any change is made in service metadata document. Values are selected by each server, and are always opaque to clients. See updateSequence parameter use subclause for more information. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;restriction base="string"/&gt;
 *  &lt;/simpleType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class UpdateSequenceTypeBinding extends AbstractSimpleBinding {

	OWSFactory owsfactory;		
	public UpdateSequenceTypeBinding( OWSFactory owsfactory ) {
		this.owsfactory = owsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return OWS.UPDATESEQUENCETYPE;
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