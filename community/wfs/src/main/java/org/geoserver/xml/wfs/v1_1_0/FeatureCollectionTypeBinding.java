package org.geoserver.xml.wfs.v1_1_0;


import org.geotools.xml.*;

import net.opengis.wfs.v1_1_0.WFSFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wfs:FeatureCollectionType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:complexType name="FeatureCollectionType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *              This type defines a container for the response to a 
 *              GetFeature or GetFeatureWithLock request.  If the
 *              request is GetFeatureWithLock, the lockId attribute
 *              must be populated.  The lockId attribute can otherwise
 *              be safely ignored.
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="gml:AbstractFeatureCollectionType"&gt;
 *              &lt;xsd:attribute name="lockId" type="xsd:string" use="optional"&gt;
 *                  &lt;xsd:annotation&gt;
 *                      &lt;xsd:documentation&gt;
 *                    The value of the lockId attribute is an identifier
 *                    that a Web Feature Service generates when responding
 *                    to a GetFeatureWithLock request.  A client application
 *                    can use this value in subsequent operations (such as a
 *                    Transaction request) to reference the set of locked
 *                    features.
 *                 &lt;/xsd:documentation&gt;
 *                  &lt;/xsd:annotation&gt;
 *              &lt;/xsd:attribute&gt;
 *              &lt;xsd:attribute name="timeStamp" type="xsd:dateTime" use="optional"&gt;
 *                  &lt;xsd:annotation&gt;
 *                      &lt;xsd:documentation&gt;
 *                    The timeStamp attribute should contain the date and time
 *                    that the response was generated.
 *                 &lt;/xsd:documentation&gt;
 *                  &lt;/xsd:annotation&gt;
 *              &lt;/xsd:attribute&gt;
 *              &lt;xsd:attribute name="numberOfFeatures"
 *                  type="xsd:nonNegativeInteger" use="optional"&gt;
 *                  &lt;xsd:annotation&gt;
 *                      &lt;xsd:documentation&gt;
 *                    The numberOfFeatures attribute should contain a
 *                    count of the number of features in the response.
 *                    That is a count of all features elements dervied
 *                    from gml:AbstractFeatureType.
 *                 &lt;/xsd:documentation&gt;
 *                  &lt;/xsd:annotation&gt;
 *              &lt;/xsd:attribute&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class FeatureCollectionTypeBinding extends AbstractComplexBinding {

	WFSFactory wfsfactory;		
	public FeatureCollectionTypeBinding( WFSFactory wfsfactory ) {
		this.wfsfactory = wfsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WFS.FEATURECOLLECTIONTYPE;
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