package org.geoserver.xml.ows.v1_0_0;


import org.geotools.xml.*;

import net.opengis.ows.v1_0_0.AcceptVersionsType;
import net.opengis.ows.v1_0_0.OWSFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/ows:AcceptVersionsType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="AcceptVersionsType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Prioritized sequence of one or more specification versions accepted by client, with preferred versions listed first. See Version negotiation subclause for more information. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs="unbounded" name="Version" type="ows:VersionType"/&gt;
 *      &lt;/sequence&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class AcceptVersionsTypeBinding extends AbstractComplexBinding {

	OWSFactory owsfactory;		
	public AcceptVersionsTypeBinding( OWSFactory owsfactory ) {
		this.owsfactory = owsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return OWS.ACCEPTVERSIONSTYPE;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return AcceptVersionsType.class;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(ElementInstance instance, Node node, Object value) 
		throws Exception {
		
		AcceptVersionsType acceptVersions = owsfactory.createAcceptVersionsType();
		acceptVersions.getVersion().addAll( node.getChildValues( "Version") );
		
		return acceptVersions;
	}

}