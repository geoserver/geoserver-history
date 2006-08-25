/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.v1_1_0.impl;

import java.util.Collection;

import net.opengis.wfs.v1_1_0.FeaturesNotLockedType;
import net.opengis.wfs.v1_1_0.WFSPackage;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Features Not Locked Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.FeaturesNotLockedTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.FeaturesNotLockedTypeImpl#getFeatureId <em>Feature Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FeaturesNotLockedTypeImpl extends EObjectImpl implements FeaturesNotLockedType {
	/**
	 * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroup()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap group = null;

	/**
	 * The default value of the '{@link #getFeatureId() <em>Feature Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatureId()
	 * @generated
	 * @ordered
	 */
	protected static final Object FEATURE_ID_EDEFAULT = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FeaturesNotLockedTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return WFSPackage.eINSTANCE.getFeaturesNotLockedType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getGroup() {
		if (group == null) {
			group = new BasicFeatureMap(this, WFSPackage.FEATURES_NOT_LOCKED_TYPE__GROUP);
		}
		return group;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getFeatureId() {
		return (Object)getGroup().get(WFSPackage.eINSTANCE.getFeaturesNotLockedType_FeatureId(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFeatureId(Object newFeatureId) {
		((FeatureMap.Internal)getGroup()).set(WFSPackage.eINSTANCE.getFeaturesNotLockedType_FeatureId(), newFeatureId);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case WFSPackage.FEATURES_NOT_LOCKED_TYPE__GROUP:
					return ((InternalEList)getGroup()).basicRemove(otherEnd, msgs);
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
			case WFSPackage.FEATURES_NOT_LOCKED_TYPE__GROUP:
				return getGroup();
			case WFSPackage.FEATURES_NOT_LOCKED_TYPE__FEATURE_ID:
				return getFeatureId();
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
			case WFSPackage.FEATURES_NOT_LOCKED_TYPE__GROUP:
				getGroup().clear();
				getGroup().addAll((Collection)newValue);
				return;
			case WFSPackage.FEATURES_NOT_LOCKED_TYPE__FEATURE_ID:
				setFeatureId((Object)newValue);
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
			case WFSPackage.FEATURES_NOT_LOCKED_TYPE__GROUP:
				getGroup().clear();
				return;
			case WFSPackage.FEATURES_NOT_LOCKED_TYPE__FEATURE_ID:
				setFeatureId(FEATURE_ID_EDEFAULT);
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
			case WFSPackage.FEATURES_NOT_LOCKED_TYPE__GROUP:
				return group != null && !group.isEmpty();
			case WFSPackage.FEATURES_NOT_LOCKED_TYPE__FEATURE_ID:
				return FEATURE_ID_EDEFAULT == null ? getFeatureId() != null : !FEATURE_ID_EDEFAULT.equals(getFeatureId());
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
		result.append(" (group: ");
		result.append(group);
		result.append(')');
		return result.toString();
	}

} //FeaturesNotLockedTypeImpl
