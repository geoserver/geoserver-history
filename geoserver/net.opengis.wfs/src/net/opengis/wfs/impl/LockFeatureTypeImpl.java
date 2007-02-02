/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.math.BigInteger;

import java.util.Collection;

import net.opengis.wfs.AllSomeType;
import net.opengis.wfs.LockFeatureType;
import net.opengis.wfs.LockType;
import net.opengis.wfs.WFSPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Lock Feature Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.LockFeatureTypeImpl#getLock <em>Lock</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.LockFeatureTypeImpl#getExpiry <em>Expiry</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.LockFeatureTypeImpl#getLockAction <em>Lock Action</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LockFeatureTypeImpl extends BaseRequestTypeImpl implements LockFeatureType {
	/**
	 * The cached value of the '{@link #getLock() <em>Lock</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLock()
	 * @generated
	 * @ordered
	 */
	protected EList lock = null;

	/**
	 * The default value of the '{@link #getExpiry() <em>Expiry</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExpiry()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger EXPIRY_EDEFAULT = new BigInteger("5");

	/**
	 * The cached value of the '{@link #getExpiry() <em>Expiry</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExpiry()
	 * @generated
	 * @ordered
	 */
	protected BigInteger expiry = EXPIRY_EDEFAULT;

	/**
	 * This is true if the Expiry attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean expiryESet = false;

	/**
	 * The default value of the '{@link #getLockAction() <em>Lock Action</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLockAction()
	 * @generated
	 * @ordered
	 */
	protected static final AllSomeType LOCK_ACTION_EDEFAULT = AllSomeType.ALL_LITERAL;

	/**
	 * The cached value of the '{@link #getLockAction() <em>Lock Action</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLockAction()
	 * @generated
	 * @ordered
	 */
	protected AllSomeType lockAction = LOCK_ACTION_EDEFAULT;

	/**
	 * This is true if the Lock Action attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean lockActionESet = false;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LockFeatureTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return WFSPackage.eINSTANCE.getLockFeatureType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getLock() {
		if (lock == null) {
			lock = new EObjectContainmentEList(LockType.class, this, WFSPackage.LOCK_FEATURE_TYPE__LOCK);
		}
		return lock;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigInteger getExpiry() {
		return expiry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExpiry(BigInteger newExpiry) {
		BigInteger oldExpiry = expiry;
		expiry = newExpiry;
		boolean oldExpiryESet = expiryESet;
		expiryESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.LOCK_FEATURE_TYPE__EXPIRY, oldExpiry, expiry, !oldExpiryESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetExpiry() {
		BigInteger oldExpiry = expiry;
		boolean oldExpiryESet = expiryESet;
		expiry = EXPIRY_EDEFAULT;
		expiryESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, WFSPackage.LOCK_FEATURE_TYPE__EXPIRY, oldExpiry, EXPIRY_EDEFAULT, oldExpiryESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetExpiry() {
		return expiryESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AllSomeType getLockAction() {
		return lockAction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLockAction(AllSomeType newLockAction) {
		AllSomeType oldLockAction = lockAction;
		lockAction = newLockAction == null ? LOCK_ACTION_EDEFAULT : newLockAction;
		boolean oldLockActionESet = lockActionESet;
		lockActionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.LOCK_FEATURE_TYPE__LOCK_ACTION, oldLockAction, lockAction, !oldLockActionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetLockAction() {
		AllSomeType oldLockAction = lockAction;
		boolean oldLockActionESet = lockActionESet;
		lockAction = LOCK_ACTION_EDEFAULT;
		lockActionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, WFSPackage.LOCK_FEATURE_TYPE__LOCK_ACTION, oldLockAction, LOCK_ACTION_EDEFAULT, oldLockActionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetLockAction() {
		return lockActionESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case WFSPackage.LOCK_FEATURE_TYPE__LOCK:
					return ((InternalEList)getLock()).basicRemove(otherEnd, msgs);
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
			case WFSPackage.LOCK_FEATURE_TYPE__HANDLE:
				return getHandle();
			case WFSPackage.LOCK_FEATURE_TYPE__SERVICE:
				return getService();
			case WFSPackage.LOCK_FEATURE_TYPE__VERSION:
				return getVersion();
			case WFSPackage.LOCK_FEATURE_TYPE__LOCK:
				return getLock();
			case WFSPackage.LOCK_FEATURE_TYPE__EXPIRY:
				return getExpiry();
			case WFSPackage.LOCK_FEATURE_TYPE__LOCK_ACTION:
				return getLockAction();
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
			case WFSPackage.LOCK_FEATURE_TYPE__HANDLE:
				setHandle((String)newValue);
				return;
			case WFSPackage.LOCK_FEATURE_TYPE__SERVICE:
				setService((String)newValue);
				return;
			case WFSPackage.LOCK_FEATURE_TYPE__VERSION:
				setVersion((String)newValue);
				return;
			case WFSPackage.LOCK_FEATURE_TYPE__LOCK:
				getLock().clear();
				getLock().addAll((Collection)newValue);
				return;
			case WFSPackage.LOCK_FEATURE_TYPE__EXPIRY:
				setExpiry((BigInteger)newValue);
				return;
			case WFSPackage.LOCK_FEATURE_TYPE__LOCK_ACTION:
				setLockAction((AllSomeType)newValue);
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
			case WFSPackage.LOCK_FEATURE_TYPE__HANDLE:
				setHandle(HANDLE_EDEFAULT);
				return;
			case WFSPackage.LOCK_FEATURE_TYPE__SERVICE:
				unsetService();
				return;
			case WFSPackage.LOCK_FEATURE_TYPE__VERSION:
				unsetVersion();
				return;
			case WFSPackage.LOCK_FEATURE_TYPE__LOCK:
				getLock().clear();
				return;
			case WFSPackage.LOCK_FEATURE_TYPE__EXPIRY:
				unsetExpiry();
				return;
			case WFSPackage.LOCK_FEATURE_TYPE__LOCK_ACTION:
				unsetLockAction();
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
			case WFSPackage.LOCK_FEATURE_TYPE__HANDLE:
				return HANDLE_EDEFAULT == null ? handle != null : !HANDLE_EDEFAULT.equals(handle);
			case WFSPackage.LOCK_FEATURE_TYPE__SERVICE:
				return isSetService();
			case WFSPackage.LOCK_FEATURE_TYPE__VERSION:
				return isSetVersion();
			case WFSPackage.LOCK_FEATURE_TYPE__LOCK:
				return lock != null && !lock.isEmpty();
			case WFSPackage.LOCK_FEATURE_TYPE__EXPIRY:
				return isSetExpiry();
			case WFSPackage.LOCK_FEATURE_TYPE__LOCK_ACTION:
				return isSetLockAction();
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
		result.append(" (expiry: ");
		if (expiryESet) result.append(expiry); else result.append("<unset>");
		result.append(", lockAction: ");
		if (lockActionESet) result.append(lockAction); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //LockFeatureTypeImpl
