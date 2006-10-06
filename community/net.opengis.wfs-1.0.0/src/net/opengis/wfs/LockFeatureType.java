/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Lock Feature Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             This type defines the LockFeature operation.  The LockFeature
 *             element contains one or more Lock elements that define
 *             which features of a particular type should be locked.  A lock
 *             identifier (lockId) is returned to the client application which
 *             can be used by subsequent operations to reference the locked
 *             features.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.LockFeatureType#getLock <em>Lock</em>}</li>
 *   <li>{@link net.opengis.wfs.LockFeatureType#getExpiry <em>Expiry</em>}</li>
 *   <li>{@link net.opengis.wfs.LockFeatureType#getLockAction <em>Lock Action</em>}</li>
 *   <li>{@link net.opengis.wfs.LockFeatureType#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.wfs.LockFeatureType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WFSPackage#getLockFeatureType()
 * @model extendedMetaData="name='LockFeatureType' kind='elementOnly'"
 * @generated
 */
public interface LockFeatureType extends EObject{
	/**
	 * Returns the value of the '<em><b>Lock</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wfs.LockType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                   The lock element is used to indicate which feature
	 *                   instances of particular type are to be locked.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Lock</em>' containment reference list.
	 * @see net.opengis.wfs.WFSPackage#getLockFeatureType_Lock()
	 * @model type="net.opengis.wfs.LockType" containment="true" resolveProxies="false" required="true"
	 *        extendedMetaData="kind='element' name='Lock' namespace='##targetNamespace'"
	 * @generated
	 */
	EList getLock();

	/**
	 * Returns the value of the '<em><b>Expiry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expiry</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expiry</em>' attribute.
	 * @see #setExpiry(BigInteger)
	 * @see net.opengis.wfs.WFSPackage#getLockFeatureType_Expiry()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
	 *        extendedMetaData="kind='attribute' name='expiry'"
	 * @generated
	 */
	BigInteger getExpiry();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.LockFeatureType#getExpiry <em>Expiry</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expiry</em>' attribute.
	 * @see #getExpiry()
	 * @generated
	 */
	void setExpiry(BigInteger value);

	/**
	 * Returns the value of the '<em><b>Lock Action</b></em>' attribute.
	 * The default value is <code>"ALL"</code>.
	 * The literals are from the enumeration {@link net.opengis.wfs.AllSomeType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                The lockAction attribute is used to indicate what
	 *                a Web Feature Service should do when it encounters
	 *                a feature instance that has already been locked by
	 *                another client application.
	 * 
	 *                Valid values are ALL or SOME.
	 * 
	 *                ALL means that the Web Feature Service must acquire
	 *                locks on all the requested feature instances.  If it
	 *                cannot acquire those locks then the request should
	 *                fail.  In this instance, all locks acquired by the
	 *                operation should be released.
	 * 
	 *                SOME means that the Web Feature Service should lock
	 *                as many of the requested features as it can.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Lock Action</em>' attribute.
	 * @see net.opengis.wfs.AllSomeType
	 * @see #isSetLockAction()
	 * @see #unsetLockAction()
	 * @see #setLockAction(AllSomeType)
	 * @see net.opengis.wfs.WFSPackage#getLockFeatureType_LockAction()
	 * @model default="ALL" unique="false" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='lockAction'"
	 * @generated
	 */
	AllSomeType getLockAction();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.LockFeatureType#getLockAction <em>Lock Action</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lock Action</em>' attribute.
	 * @see net.opengis.wfs.AllSomeType
	 * @see #isSetLockAction()
	 * @see #unsetLockAction()
	 * @see #getLockAction()
	 * @generated
	 */
	void setLockAction(AllSomeType value);

	/**
	 * Unsets the value of the '{@link net.opengis.wfs.LockFeatureType#getLockAction <em>Lock Action</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetLockAction()
	 * @see #getLockAction()
	 * @see #setLockAction(AllSomeType)
	 * @generated
	 */
	void unsetLockAction();

	/**
	 * Returns whether the value of the '{@link net.opengis.wfs.LockFeatureType#getLockAction <em>Lock Action</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Lock Action</em>' attribute is set.
	 * @see #unsetLockAction()
	 * @see #getLockAction()
	 * @see #setLockAction(AllSomeType)
	 * @generated
	 */
	boolean isSetLockAction();

	/**
	 * Returns the value of the '<em><b>Service</b></em>' attribute.
	 * The default value is <code>"WFS"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Service</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Service</em>' attribute.
	 * @see #isSetService()
	 * @see #unsetService()
	 * @see #setService(String)
	 * @see net.opengis.wfs.WFSPackage#getLockFeatureType_Service()
	 * @model default="WFS" unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='service'"
	 * @generated
	 */
	String getService();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.LockFeatureType#getService <em>Service</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service</em>' attribute.
	 * @see #isSetService()
	 * @see #unsetService()
	 * @see #getService()
	 * @generated
	 */
	void setService(String value);

	/**
	 * Unsets the value of the '{@link net.opengis.wfs.LockFeatureType#getService <em>Service</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetService()
	 * @see #getService()
	 * @see #setService(String)
	 * @generated
	 */
	void unsetService();

	/**
	 * Returns whether the value of the '{@link net.opengis.wfs.LockFeatureType#getService <em>Service</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Service</em>' attribute is set.
	 * @see #unsetService()
	 * @see #getService()
	 * @see #setService(String)
	 * @generated
	 */
	boolean isSetService();

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
	 * @see net.opengis.wfs.WFSPackage#getLockFeatureType_Version()
	 * @model default="1.0.0" unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='version'"
	 * @generated
	 */
	String getVersion();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.LockFeatureType#getVersion <em>Version</em>}' attribute.
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
	 * Unsets the value of the '{@link net.opengis.wfs.LockFeatureType#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
	void unsetVersion();

	/**
	 * Returns whether the value of the '{@link net.opengis.wfs.LockFeatureType#getVersion <em>Version</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Version</em>' attribute is set.
	 * @see #unsetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
	boolean isSetVersion();

} // LockFeatureType
