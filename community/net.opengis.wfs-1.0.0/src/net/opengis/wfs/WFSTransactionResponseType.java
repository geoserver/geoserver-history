/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transaction Response Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             The WFS_TransactionResponseType defines the format of
 *             the XML document that a Web Feature Service generates
 *             in response to a Transaction request.  The response
 *             includes the completion status of the transaction
 *             and the feature identifiers of any newly created
 *             feature instances.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.WFSTransactionResponseType#getInsertResult <em>Insert Result</em>}</li>
 *   <li>{@link net.opengis.wfs.WFSTransactionResponseType#getTransactionResult <em>Transaction Result</em>}</li>
 *   <li>{@link net.opengis.wfs.WFSTransactionResponseType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WFSPackage#getWFSTransactionResponseType()
 * @model extendedMetaData="name='WFS_TransactionResponseType' kind='elementOnly'"
 * @generated
 */
public interface WFSTransactionResponseType extends EObject{
	/**
	 * Returns the value of the '<em><b>Insert Result</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wfs.InsertResultType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                   The InsertResult element contains a list of ogc:FeatureId
	 *                   elements that identify any newly created feature instances.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Insert Result</em>' containment reference list.
	 * @see net.opengis.wfs.WFSPackage#getWFSTransactionResponseType_InsertResult()
	 * @model type="net.opengis.wfs.InsertResultType" containment="true" resolveProxies="false"
	 *        extendedMetaData="kind='element' name='InsertResult' namespace='##targetNamespace'"
	 * @generated
	 */
	EList getInsertResult();

	/**
	 * Returns the value of the '<em><b>Transaction Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                   The TransactionResult element contains a Status element
	 *                   indicating the completion status of a transaction.  In
	 *                   the event that the transaction fails, additional element
	 *                   may be included to help locate which part of the transaction
	 *                   failed and why.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Transaction Result</em>' containment reference.
	 * @see #setTransactionResult(TransactionResultType)
	 * @see net.opengis.wfs.WFSPackage#getWFSTransactionResponseType_TransactionResult()
	 * @model containment="true" resolveProxies="false" required="true"
	 *        extendedMetaData="kind='element' name='TransactionResult' namespace='##targetNamespace'"
	 * @generated
	 */
	TransactionResultType getTransactionResult();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.WFSTransactionResponseType#getTransactionResult <em>Transaction Result</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transaction Result</em>' containment reference.
	 * @see #getTransactionResult()
	 * @generated
	 */
	void setTransactionResult(TransactionResultType value);

	/**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * The default value is <code>"1.0.0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #isSetVersion()
	 * @see #unsetVersion()
	 * @see #setVersion(String)
	 * @see net.opengis.wfs.WFSPackage#getWFSTransactionResponseType_Version()
	 * @model default="1.0.0" unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='version'"
	 * @generated
	 */
	String getVersion();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.WFSTransactionResponseType#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #isSetVersion()
	 * @see #unsetVersion()
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(String value);

	/**
	 * Unsets the value of the '{@link net.opengis.wfs.WFSTransactionResponseType#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
	void unsetVersion();

	/**
	 * Returns whether the value of the '{@link net.opengis.wfs.WFSTransactionResponseType#getVersion <em>Version</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Version</em>' attribute is set.
	 * @see #unsetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
	boolean isSetVersion();

} // WFSTransactionResponseType
