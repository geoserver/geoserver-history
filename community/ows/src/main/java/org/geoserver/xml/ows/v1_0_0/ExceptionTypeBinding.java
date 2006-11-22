package org.geoserver.xml.ows.v1_0_0;


import org.geoserver.ows.ComplexEMFBinding;
import org.geotools.xml.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.opengis.ows.v1_0_0.ExceptionType;
import net.opengis.ows.v1_0_0.OWSFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/ows:ExceptionType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="ExceptionType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;An Exception element describes one detected error that a server chooses to convey to the client. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs="unbounded" minOccurs="0"
 *              name="ExceptionText" type="string"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Ordered sequence of text strings that describe this specific exception or error. The contents of these strings are left open to definition by each server implementation. A server is strongly encouraged to include at least one ExceptionText value, to provide more information about the detected error than provided by the exceptionCode. When included, multiple ExceptionText values shall provide hierarchical information about one detected error, with the most significant information listed first. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *      &lt;/sequence&gt;
 *      &lt;attribute name="exceptionCode" type="string" use="required"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;A code representing the type of this exception, which shall be selected from a set of exceptionCode values specified for the specific service operation and server. &lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attribute&gt;
 *      &lt;attribute name="locator" type="string" use="optional"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;When included, this locator shall indicate to the client where an exception was encountered in servicing the client's operation request. This locator should be included whenever meaningful information can be provided by the server. The contents of this locator will depend on the specific exceptionCode and OWS service, and shall be specified in the OWS Implementation Specification. &lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attribute&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class ExceptionTypeBinding extends ComplexEMFBinding {

	OWSFactory owsfactory;		
	
	public ExceptionTypeBinding( OWSFactory owsfactory ) {
		this.owsfactory = owsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return OWS.EXCEPTIONTYPE;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return ExceptionType.class;
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