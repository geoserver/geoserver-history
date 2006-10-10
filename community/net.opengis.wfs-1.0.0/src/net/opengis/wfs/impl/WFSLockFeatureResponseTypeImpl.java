/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.util.Collection;

import net.opengis.wfs.WFSLockFeatureResponseType;
import net.opengis.wfs.WFSPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import org.opengis.filter.FeatureId;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Lock Feature Response Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.WFSLockFeatureResponseTypeImpl#getLockId <em>Lock Id</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.WFSLockFeatureResponseTypeImpl#getFeaturesLocked <em>Features Locked</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.WFSLockFeatureResponseTypeImpl#getFeaturesNotLocked <em>Features Not Locked</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class WFSLockFeatureResponseTypeImpl extends EObjectImpl implements WFSLockFeatureResponseType {
	/**
	 * The default value of the '{@link #getLockId() <em>Lock Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLockId()
	 * @generated
	 * @ordered
	 */
	protected static final String LOCK_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLockId() <em>Lock Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLockId()
	 * @generated
	 * @ordered
	 */
	protected String lockId = LOCK_ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getFeaturesLocked() <em>Features Locked</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeaturesLocked()
	 * @generated
	 * @ordered
	 */
	protected EList featuresLocked = null;

	/**
	 * The cached value of the '{@link #getFeaturesNotLocked() <em>Features Not Locked</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeaturesNotLocked()
	 * @generated
	 * @ordered
	 */
	protected EList featuresNotLocked = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected WFSLockFeatureResponseTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return WFSPackage.eINSTANCE.getWFSLockFeatureResponseType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLockId() {
		return lockId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLockId(String newLockId) {
		String oldLockId = lockId;
		lockId = newLockId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.WFS_LOCK_FEATURE_RESPONSE_TYPE__LOCK_ID, oldLockId, lockId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getFeaturesLocked() {
		if (featuresLocked == null) {
			featuresLocked = new EDataTypeUniqueEList(FeatureId.class, this, WFSPackage.WFS_LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED);
		}
		return featuresLocked;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getFeaturesNotLocked() {
		if (featuresNotLocked == null) {
			featuresNotLocked = new EDataTypeUniqueEList(FeatureId.class, this, WFSPackage.WFS_LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED);
		}
		return featuresNotLocked;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case WFSPackage.WFS_LOCK_FEATURE_RESPONSE_TYPE__LOCK_ID:
				return getLockId();
			case WFSPackage.WFS_LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED:
				return getFeaturesLocked();
			case WFSPackage.WFS_LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED:
				return getFeaturesNotLocked();
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
			case WFSPackage.WFS_LOCK_FEATURE_RESPONSE_TYPE__LOCK_ID:
				setLockId((String)newValue);
				return;
			case WFSPackage.WFS_LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED:
				getFeaturesLocked().clear();
				getFeaturesLocked().addAll((Collection)newValue);
				return;
			case WFSPackage.WFS_LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED:
				getFeaturesNotLocked().clear();
				getFeaturesNotLocked().addAll((Collection)newValue);
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
			case WFSPackage.WFS_LOCK_FEATURE_RESPONSE_TYPE__LOCK_ID:
				setLockId(LOCK_ID_EDEFAULT);
				return;
			case WFSPackage.WFS_LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED:
				getFeaturesLocked().clear();
				return;
			case WFSPackage.WFS_LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED:
				getFeaturesNotLocked().clear();
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
			case WFSPackage.WFS_LOCK_FEATURE_RESPONSE_TYPE__LOCK_ID:
				return LOCK_ID_EDEFAULT == null ? lockId != null : !LOCK_ID_EDEFAULT.equals(lockId);
			case WFSPackage.WFS_LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED:
				return featuresLocked != null && !featuresLocked.isEmpty();
			case WFSPackage.WFS_LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED:
				return featuresNotLocked != null && !featuresNotLocked.isEmpty();
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
		result.append(" (lockId: ");
		result.append(lockId);
		result.append(", featuresLocked: ");
		result.append(featuresLocked);
		result.append(", featuresNotLocked: ");
		result.append(featuresNotLocked);
		result.append(')');
		return result.toString();
	}

} //WFSLockFeatureResponseTypeImpl
