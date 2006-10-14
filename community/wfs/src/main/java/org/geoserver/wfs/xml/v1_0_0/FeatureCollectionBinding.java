package org.geoserver.wfs.xml.v1_0_0;


import org.geotools.xml.*;

import net.opengis.wfs.WfsFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/wfs:FeatureCollection.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:element name="FeatureCollection"
 *      substitutionGroup="gml:_FeatureCollection"
 *      type="wfs:FeatureCollectionType"&gt;       &lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;             This element is a container
 *              for the response to a GetFeature             or
 *              GetFeatureWithLock (WFS-transaction.xsd) request.
 *          &lt;/xsd:documentation&gt;       &lt;/xsd:annotation&gt;    &lt;/xsd:element&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class FeatureCollectionBinding extends AbstractComplexBinding {

	WfsFactory wfsfactory;		
	public FeatureCollectionBinding( WfsFactory wfsfactory ) {
		this.wfsfactory = wfsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WFS.FEATURECOLLECTION;
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