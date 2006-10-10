package org.geoserver.wfs.xml.wfs;


import java.util.List;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.FeatureType;
import org.geotools.xml.*;

import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.WFSFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wfs:InsertElementType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:complexType name="InsertElementType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="unbounded" ref="gml:_Feature"/&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attribute name="handle" type="xsd:string" use="optional"/&gt;
 *  &lt;/xsd:complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class InsertElementTypeBinding extends AbstractComplexBinding {

	WFSFactory wfsfactory;		
	
	public InsertElementTypeBinding( WFSFactory wfsfactory) {
		this.wfsfactory = wfsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WFS.INSERTELEMENTTYPE;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return InsertElementTypeBinding.class;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(ElementInstance instance, Node node, Object value) 
		throws Exception {
		
		InsertElementType insertElement = wfsfactory.createInsertElementType();
		
		//features
		List features = node.getChildValues( Feature.class );
		if ( !features.isEmpty() ) {
			Feature first = (Feature) features.iterator().next(); 
			FeatureType featureType = first.getFeatureType();
		
			FeatureCollection feature = new DefaultFeatureCollection( null, featureType ) {};
			feature.addAll( features );
			
			insertElement.setFeature( feature );
		}
		
		insertElement.getFeature().addAll( node.getChildValues( Feature.class ) );
		
		//handle
		if ( node.hasAttribute( "handle") )
			insertElement.setHandle( (String) node.getAttributeValue( "handle" ) );
		
		return insertElement;
		
	}

}