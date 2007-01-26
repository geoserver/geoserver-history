package org.geoserver.ows.xml.v1_0;


import javax.xml.namespace.QName;

import net.opengis.ows.v1_0_0.OWSFactory;

import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;

/**
 * Binding object for the type http://www.opengis.net/ows:MimeType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;simpleType name="MimeType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;XML encoded identifier of a standard MIME type, possibly a parameterized MIME type. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;restriction base="string"&gt;
 *          &lt;pattern value="(application|audio|image|text|video|message|multipart|model)/.+(;\s*.+=.+)*"/&gt;
 *      &lt;/restriction&gt;
 *  &lt;/simpleType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class MimeTypeBinding extends AbstractSimpleBinding {

	OWSFactory owsfactory;		
	public MimeTypeBinding( OWSFactory owsfactory ) {
		this.owsfactory = owsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return OWS.MIMETYPE;
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