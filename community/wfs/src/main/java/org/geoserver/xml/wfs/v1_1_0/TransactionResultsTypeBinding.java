package org.geoserver.xml.wfs.v1_1_0;


import org.geotools.xml.*;

import net.opengis.wfs.v1_1_0.WFSFactory;		

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wfs:TransactionResultsType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;xsd:complexType name="TransactionResultsType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *              The TransactionResults element may be used to report exception
 *              codes and messages for all actions of a transaction that failed
 *              to complete successfully.
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="unbounded" minOccurs="0" name="Action" type="wfs:ActionType"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                    The Action element reports an exception code
 *                    and exception message indicating why the
 *                    corresponding action of a transaction request
 *                    failed.
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
public class TransactionResultsTypeBinding extends AbstractComplexBinding {

	WFSFactory wfsfactory;		
	public TransactionResultsTypeBinding( WFSFactory wfsfactory ) {
		this.wfsfactory = wfsfactory;
	}

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WFS.TRANSACTIONRESULTSTYPE;
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