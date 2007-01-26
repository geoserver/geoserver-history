/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows.v1_0_0.impl;

import java.util.Collection;

import net.opengis.ows.v1_0_0.AcceptFormatsType;
import net.opengis.ows.v1_0_0.OWSPackage;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Accept Formats Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.AcceptFormatsTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AcceptFormatsTypeImpl extends EObjectImpl implements AcceptFormatsType {
	/**
	 * The cached value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutputFormat()
	 * @generated
	 * @ordered
	 */
	protected EList outputFormat = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AcceptFormatsTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return OWSPackage.eINSTANCE.getAcceptFormatsType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getOutputFormat() {
		if (outputFormat == null) {
			outputFormat = new EDataTypeEList(String.class, this, OWSPackage.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT);
		}
		return outputFormat;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case OWSPackage.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT:
				return getOutputFormat();
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
			case OWSPackage.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT:
				getOutputFormat().clear();
				getOutputFormat().addAll((Collection)newValue);
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
			case OWSPackage.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT:
				getOutputFormat().clear();
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
			case OWSPackage.ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT:
				return outputFormat != null && !outputFormat.isEmpty();
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
		result.append(" (outputFormat: ");
		result.append(outputFormat);
		result.append(')');
		return result.toString();
	}

} //AcceptFormatsTypeImpl
