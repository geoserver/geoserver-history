package org.geoserver.wcs.xml.v1_1_1;


import javax.xml.namespace.QName;

import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the type http://www.opengis.net/wcs/1.1.1:OutputType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="OutputType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Asks for the GetCoverage response to be expressed in a particular CRS and encoded in a particular format. Can also ask for the response coverage to be stored remotely from the client at a URL, instead of being returned in the operation response. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element minOccurs="0" ref="wcs:GridCRS"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Optional definition of the GridCRS in which the GetCoverage response shall be expressed. When this GridCRS is not included, the output shall be in the ImageCRS or GridCRS of the offered image, as identified in the CoverageDescription.  To request no interpolation, this GridCRS should be omitted. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *      &lt;/sequence&gt;
 *      &lt;attribute name="format" type="ows:MimeType"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;Identifier of the format in which GetCoverage response shall be encoded. This identifier value shall be among those listed as SupportedFormats in the DescribeCoverage operation response. &lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attribute&gt;
 *      &lt;attribute default="false" name="store" type="boolean" use="optional"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;Specifies if the output coverage should be stored, remotely from the client at a network URL, instead of being returned with the operation response. This parameter should be included only if this operation parameter is supported by server, as encoded in the OperationsMetadata section of the Capabilities document. &lt;/documentation&gt;
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
public class OutputTypeBinding extends AbstractComplexEMFBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.OutputType;
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