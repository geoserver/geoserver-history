/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.math.BigInteger;

import java.util.Collection;

import net.opengis.wfs.GetFeatureWithLockType;
import net.opengis.wfs.QueryType;
import net.opengis.wfs.ResultTypeType;
import net.opengis.wfs.WfsPackage;

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
 * An implementation of the model object '<em><b>Get Feature With Lock Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.GetFeatureWithLockTypeImpl#getExpiry <em>Expiry</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetFeatureWithLockTypeImpl extends GetFeatureTypeImpl implements GetFeatureWithLockType {
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GetFeatureWithLockTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return WfsPackage.eINSTANCE.getGetFeatureWithLockType();
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
			eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__EXPIRY, oldExpiry, expiry, !oldExpiryESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__EXPIRY, oldExpiry, EXPIRY_EDEFAULT, oldExpiryESet));
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__QUERY:
					return ((InternalEList)getQuery()).basicRemove(otherEnd, msgs);
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
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__HANDLE:
				return getHandle();
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__SERVICE:
				return getService();
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__VERSION:
				return getVersion();
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__QUERY:
				return getQuery();
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__MAX_FEATURES:
				return getMaxFeatures();
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__OUTPUT_FORMAT:
				return getOutputFormat();
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__RESULT_TYPE:
				return getResultType();
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__TRAVERSE_XLINK_DEPTH:
				return getTraverseXlinkDepth();
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__TRAVERSE_XLINK_EXPIRY:
				return getTraverseXlinkExpiry();
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__EXPIRY:
				return getExpiry();
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
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__HANDLE:
				setHandle((String)newValue);
				return;
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__SERVICE:
				setService((String)newValue);
				return;
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__VERSION:
				setVersion((String)newValue);
				return;
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__QUERY:
				getQuery().clear();
				getQuery().addAll((Collection)newValue);
				return;
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__MAX_FEATURES:
				setMaxFeatures((BigInteger)newValue);
				return;
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__OUTPUT_FORMAT:
				setOutputFormat((String)newValue);
				return;
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__RESULT_TYPE:
				setResultType((ResultTypeType)newValue);
				return;
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__TRAVERSE_XLINK_DEPTH:
				setTraverseXlinkDepth((String)newValue);
				return;
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__TRAVERSE_XLINK_EXPIRY:
				setTraverseXlinkExpiry((BigInteger)newValue);
				return;
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__EXPIRY:
				setExpiry((BigInteger)newValue);
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
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__HANDLE:
				setHandle(HANDLE_EDEFAULT);
				return;
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__SERVICE:
				unsetService();
				return;
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__VERSION:
				unsetVersion();
				return;
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__QUERY:
				getQuery().clear();
				return;
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__MAX_FEATURES:
				setMaxFeatures(MAX_FEATURES_EDEFAULT);
				return;
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__OUTPUT_FORMAT:
				unsetOutputFormat();
				return;
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__RESULT_TYPE:
				unsetResultType();
				return;
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__TRAVERSE_XLINK_DEPTH:
				setTraverseXlinkDepth(TRAVERSE_XLINK_DEPTH_EDEFAULT);
				return;
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__TRAVERSE_XLINK_EXPIRY:
				setTraverseXlinkExpiry(TRAVERSE_XLINK_EXPIRY_EDEFAULT);
				return;
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__EXPIRY:
				unsetExpiry();
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
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__HANDLE:
				return HANDLE_EDEFAULT == null ? handle != null : !HANDLE_EDEFAULT.equals(handle);
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__SERVICE:
				return isSetService();
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__VERSION:
				return isSetVersion();
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__QUERY:
				return query != null && !query.isEmpty();
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__MAX_FEATURES:
				return MAX_FEATURES_EDEFAULT == null ? maxFeatures != null : !MAX_FEATURES_EDEFAULT.equals(maxFeatures);
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__OUTPUT_FORMAT:
				return isSetOutputFormat();
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__RESULT_TYPE:
				return isSetResultType();
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__TRAVERSE_XLINK_DEPTH:
				return TRAVERSE_XLINK_DEPTH_EDEFAULT == null ? traverseXlinkDepth != null : !TRAVERSE_XLINK_DEPTH_EDEFAULT.equals(traverseXlinkDepth);
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__TRAVERSE_XLINK_EXPIRY:
				return TRAVERSE_XLINK_EXPIRY_EDEFAULT == null ? traverseXlinkExpiry != null : !TRAVERSE_XLINK_EXPIRY_EDEFAULT.equals(traverseXlinkExpiry);
			case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__EXPIRY:
				return isSetExpiry();
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
		result.append(')');
		return result.toString();
	}

} //GetFeatureWithLockTypeImpl
