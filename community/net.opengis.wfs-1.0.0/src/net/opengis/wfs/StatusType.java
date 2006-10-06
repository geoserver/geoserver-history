/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Status Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.StatusType#getSUCCESS <em>SUCCESS</em>}</li>
 *   <li>{@link net.opengis.wfs.StatusType#getFAILED <em>FAILED</em>}</li>
 *   <li>{@link net.opengis.wfs.StatusType#getPARTIAL <em>PARTIAL</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WFSPackage#getStatusType()
 * @model extendedMetaData="name='StatusType' kind='elementOnly'"
 * @generated
 */
public interface StatusType extends EObject{
	/**
	 * Returns the value of the '<em><b>SUCCESS</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>SUCCESS</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>SUCCESS</em>' containment reference.
	 * @see #setSUCCESS(EmptyType)
	 * @see net.opengis.wfs.WFSPackage#getStatusType_SUCCESS()
	 * @model containment="true" resolveProxies="false"
	 *        extendedMetaData="kind='element' name='SUCCESS' namespace='##targetNamespace'"
	 * @generated
	 */
	EmptyType getSUCCESS();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.StatusType#getSUCCESS <em>SUCCESS</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>SUCCESS</em>' containment reference.
	 * @see #getSUCCESS()
	 * @generated
	 */
	void setSUCCESS(EmptyType value);

	/**
	 * Returns the value of the '<em><b>FAILED</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>FAILED</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>FAILED</em>' containment reference.
	 * @see #setFAILED(EmptyType)
	 * @see net.opengis.wfs.WFSPackage#getStatusType_FAILED()
	 * @model containment="true" resolveProxies="false"
	 *        extendedMetaData="kind='element' name='FAILED' namespace='##targetNamespace'"
	 * @generated
	 */
	EmptyType getFAILED();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.StatusType#getFAILED <em>FAILED</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>FAILED</em>' containment reference.
	 * @see #getFAILED()
	 * @generated
	 */
	void setFAILED(EmptyType value);

	/**
	 * Returns the value of the '<em><b>PARTIAL</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>PARTIAL</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>PARTIAL</em>' containment reference.
	 * @see #setPARTIAL(EmptyType)
	 * @see net.opengis.wfs.WFSPackage#getStatusType_PARTIAL()
	 * @model containment="true" resolveProxies="false"
	 *        extendedMetaData="kind='element' name='PARTIAL' namespace='##targetNamespace'"
	 * @generated
	 */
	EmptyType getPARTIAL();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.StatusType#getPARTIAL <em>PARTIAL</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>PARTIAL</em>' containment reference.
	 * @see #getPARTIAL()
	 * @generated
	 */
	void setPARTIAL(EmptyType value);

} // StatusType
