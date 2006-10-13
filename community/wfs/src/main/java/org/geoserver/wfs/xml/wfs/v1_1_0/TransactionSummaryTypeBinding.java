package org.geoserver.wfs.xml.wfs.v1_1_0;


import org.geotools.xml.*;

import net.opengis.wfs.WfsFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wfs:TransactionSummaryType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:complexType name="TransactionSummaryType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation xml:lang="en"&gt;
 *              Reports the total number of features affected by some kind 
 *              of write action (i.e, insert, update, delete).
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element minOccurs="0" name="totalInserted" type="xsd:nonNegativeInteger"/&gt;
 *          &lt;xsd:element minOccurs="0" name="totalUpdated" type="xsd:nonNegativeInteger"/&gt;
 *          &lt;xsd:element minOccurs="0" name="totalDeleted" type="xsd:nonNegativeInteger"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class TransactionSummaryTypeBinding extends AbstractComplexBinding {

	WfsFactory wfsfactory;		
	public TransactionSummaryTypeBinding( WfsFactory wfsfactory ) {
		this.wfsfactory = wfsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WFS.TRANSACTIONSUMMARYTYPE;
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