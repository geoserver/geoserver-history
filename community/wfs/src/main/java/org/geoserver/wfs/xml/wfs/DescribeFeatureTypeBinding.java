package org.geoserver.wfs.xml.wfs;


import org.geotools.xml.*;

import net.opengis.wfs.WFSFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/wfs:DescribeFeatureType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:element name="DescribeFeatureType"
 *      type="wfs:DescribeFeatureTypeType"&gt;       &lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;             The DescribeFeatureType
 *              element is used to request that a Web             Feature
 *              Service generate a document describing one or more
 *              feature types.          &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;    &lt;/xsd:element&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class DescribeFeatureTypeBinding extends AbstractComplexBinding {

	WFSFactory wfsfactory;		
	public DescribeFeatureTypeBinding( WFSFactory wfsfactory ) {
		this.wfsfactory = wfsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WFS.DESCRIBEFEATURETYPE;
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