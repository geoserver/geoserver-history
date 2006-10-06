package org.geoserver.wfs.xml.wfs;


import java.util.Map;

import org.geotools.xml.*;

import net.opengis.wfs.PropertyType;
import net.opengis.wfs.WFSFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wfs:PropertyType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:complexType name="PropertyType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element name="Name" type="xsd:string"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                    The Name element contains the name of a feature property
 *                    to be updated.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:element&gt;
 *          &lt;xsd:element minOccurs="0" name="Value"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                    The Value element contains the replacement value for the
 *                    named property.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:element&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class PropertyTypeBinding extends AbstractComplexBinding {

	WFSFactory wfsfactory;		
	public PropertyTypeBinding( WFSFactory wfsfactory ) {
		this.wfsfactory = wfsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WFS.PROPERTYTYPE;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return PropertyType.class;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(ElementInstance instance, Node node, Object value) 
		throws Exception {
		
		PropertyType property = wfsfactory.createPropertyType();
		
		//&lt;xsd:element name="Name" type="xsd:string"&gt;
		property.setName( (String) node.getChildValue( "Name" ) );
		
		//&lt;xsd:element minOccurs="0" name="Value"&gt;
		if ( node.hasChild( "Value" ) ) {
			Map map = (Map) node.getChildValue( "Value");
			if ( !map.isEmpty() ) {
				//first check for some test
				if ( map.containsKey( null ) ) {
					property.setValue( map.get( null ) );
				}
				else {
					//perhaps some other value
					property.setValue( map.values().iterator().next() );
				}	
			}
			
			
		}
		
		return property;
	}

}