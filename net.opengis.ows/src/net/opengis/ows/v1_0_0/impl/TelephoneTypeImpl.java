/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows.v1_0_0.impl;

import java.util.Collection;

import net.opengis.ows.v1_0_0.OWSPackage;
import net.opengis.ows.v1_0_0.TelephoneType;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Telephone Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.TelephoneTypeImpl#getVoice <em>Voice</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.TelephoneTypeImpl#getFacsimile <em>Facsimile</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TelephoneTypeImpl extends EObjectImpl implements TelephoneType {
	/**
	 * The cached value of the '{@link #getVoice() <em>Voice</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVoice()
	 * @generated
	 * @ordered
	 */
	protected EList voice = null;

	/**
	 * The cached value of the '{@link #getFacsimile() <em>Facsimile</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFacsimile()
	 * @generated
	 * @ordered
	 */
	protected EList facsimile = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TelephoneTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return OWSPackage.eINSTANCE.getTelephoneType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getVoice() {
		if (voice == null) {
			voice = new EDataTypeEList(String.class, this, OWSPackage.TELEPHONE_TYPE__VOICE);
		}
		return voice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getFacsimile() {
		if (facsimile == null) {
			facsimile = new EDataTypeEList(String.class, this, OWSPackage.TELEPHONE_TYPE__FACSIMILE);
		}
		return facsimile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case OWSPackage.TELEPHONE_TYPE__VOICE:
				return getVoice();
			case OWSPackage.TELEPHONE_TYPE__FACSIMILE:
				return getFacsimile();
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
			case OWSPackage.TELEPHONE_TYPE__VOICE:
				getVoice().clear();
				getVoice().addAll((Collection)newValue);
				return;
			case OWSPackage.TELEPHONE_TYPE__FACSIMILE:
				getFacsimile().clear();
				getFacsimile().addAll((Collection)newValue);
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
			case OWSPackage.TELEPHONE_TYPE__VOICE:
				getVoice().clear();
				return;
			case OWSPackage.TELEPHONE_TYPE__FACSIMILE:
				getFacsimile().clear();
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
			case OWSPackage.TELEPHONE_TYPE__VOICE:
				return voice != null && !voice.isEmpty();
			case OWSPackage.TELEPHONE_TYPE__FACSIMILE:
				return facsimile != null && !facsimile.isEmpty();
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
		result.append(" (voice: ");
		result.append(voice);
		result.append(", facsimile: ");
		result.append(facsimile);
		result.append(')');
		return result.toString();
	}

} //TelephoneTypeImpl
