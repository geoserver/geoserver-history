/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.util.Collection;

import net.opengis.wfs.InsertResultType;
import net.opengis.wfs.TransactionResultType;
import net.opengis.wfs.WFSPackage;
import net.opengis.wfs.WFSTransactionResponseType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transaction Response Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.WFSTransactionResponseTypeImpl#getInsertResult <em>Insert Result</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.WFSTransactionResponseTypeImpl#getTransactionResult <em>Transaction Result</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.WFSTransactionResponseTypeImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class WFSTransactionResponseTypeImpl extends EObjectImpl implements WFSTransactionResponseType {
	/**
	 * The cached value of the '{@link #getInsertResult() <em>Insert Result</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInsertResult()
	 * @generated
	 * @ordered
	 */
	protected EList insertResult = null;

	/**
	 * The cached value of the '{@link #getTransactionResult() <em>Transaction Result</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransactionResult()
	 * @generated
	 * @ordered
	 */
	protected TransactionResultType transactionResult = null;

	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_EDEFAULT = "1.0.0";

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected String version = VERSION_EDEFAULT;

	/**
	 * This is true if the Version attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean versionESet = false;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected WFSTransactionResponseTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return WFSPackage.eINSTANCE.getWFSTransactionResponseType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getInsertResult() {
		if (insertResult == null) {
			insertResult = new EObjectContainmentEList(InsertResultType.class, this, WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__INSERT_RESULT);
		}
		return insertResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransactionResultType getTransactionResult() {
		return transactionResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTransactionResult(TransactionResultType newTransactionResult, NotificationChain msgs) {
		TransactionResultType oldTransactionResult = transactionResult;
		transactionResult = newTransactionResult;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__TRANSACTION_RESULT, oldTransactionResult, newTransactionResult);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTransactionResult(TransactionResultType newTransactionResult) {
		if (newTransactionResult != transactionResult) {
			NotificationChain msgs = null;
			if (transactionResult != null)
				msgs = ((InternalEObject)transactionResult).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__TRANSACTION_RESULT, null, msgs);
			if (newTransactionResult != null)
				msgs = ((InternalEObject)newTransactionResult).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__TRANSACTION_RESULT, null, msgs);
			msgs = basicSetTransactionResult(newTransactionResult, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__TRANSACTION_RESULT, newTransactionResult, newTransactionResult));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		boolean oldVersionESet = versionESet;
		versionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__VERSION, oldVersion, version, !oldVersionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetVersion() {
		String oldVersion = version;
		boolean oldVersionESet = versionESet;
		version = VERSION_EDEFAULT;
		versionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__VERSION, oldVersion, VERSION_EDEFAULT, oldVersionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetVersion() {
		return versionESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__INSERT_RESULT:
					return ((InternalEList)getInsertResult()).basicRemove(otherEnd, msgs);
				case WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__TRANSACTION_RESULT:
					return basicSetTransactionResult(null, msgs);
				default:
					return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
			}
		}
		return eBasicSetContainer(null, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__INSERT_RESULT:
				return getInsertResult();
			case WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__TRANSACTION_RESULT:
				return getTransactionResult();
			case WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__VERSION:
				return getVersion();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__INSERT_RESULT:
				getInsertResult().clear();
				getInsertResult().addAll((Collection)newValue);
				return;
			case WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__TRANSACTION_RESULT:
				setTransactionResult((TransactionResultType)newValue);
				return;
			case WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__VERSION:
				setVersion((String)newValue);
				return;
		}
		eDynamicSet(eFeature, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__INSERT_RESULT:
				getInsertResult().clear();
				return;
			case WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__TRANSACTION_RESULT:
				setTransactionResult((TransactionResultType)null);
				return;
			case WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__VERSION:
				unsetVersion();
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__INSERT_RESULT:
				return insertResult != null && !insertResult.isEmpty();
			case WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__TRANSACTION_RESULT:
				return transactionResult != null;
			case WFSPackage.WFS_TRANSACTION_RESPONSE_TYPE__VERSION:
				return isSetVersion();
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (version: ");
		if (versionESet) result.append(version); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //WFSTransactionResponseTypeImpl
