/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows.v1_0_0.impl;

import java.math.BigInteger;

import java.util.List;

import net.opengis.ows.v1_0_0.OWSPackage;
import net.opengis.ows.v1_0_0.WGS84BoundingBoxType;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>WGS84 Bounding Box Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class WGS84BoundingBoxTypeImpl extends BoundingBoxTypeImpl implements WGS84BoundingBoxType {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected WGS84BoundingBoxTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return OWSPackage.eINSTANCE.getWGS84BoundingBoxType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case OWSPackage.WGS84_BOUNDING_BOX_TYPE__LOWER_CORNER:
				return getLowerCorner();
			case OWSPackage.WGS84_BOUNDING_BOX_TYPE__UPPER_CORNER:
				return getUpperCorner();
			case OWSPackage.WGS84_BOUNDING_BOX_TYPE__CRS:
				return getCrs();
			case OWSPackage.WGS84_BOUNDING_BOX_TYPE__DIMENSIONS:
				return getDimensions();
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
			case OWSPackage.WGS84_BOUNDING_BOX_TYPE__LOWER_CORNER:
				setLowerCorner((List)newValue);
				return;
			case OWSPackage.WGS84_BOUNDING_BOX_TYPE__UPPER_CORNER:
				setUpperCorner((List)newValue);
				return;
			case OWSPackage.WGS84_BOUNDING_BOX_TYPE__CRS:
				setCrs((String)newValue);
				return;
			case OWSPackage.WGS84_BOUNDING_BOX_TYPE__DIMENSIONS:
				setDimensions((BigInteger)newValue);
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
			case OWSPackage.WGS84_BOUNDING_BOX_TYPE__LOWER_CORNER:
				setLowerCorner(LOWER_CORNER_EDEFAULT);
				return;
			case OWSPackage.WGS84_BOUNDING_BOX_TYPE__UPPER_CORNER:
				setUpperCorner(UPPER_CORNER_EDEFAULT);
				return;
			case OWSPackage.WGS84_BOUNDING_BOX_TYPE__CRS:
				setCrs(CRS_EDEFAULT);
				return;
			case OWSPackage.WGS84_BOUNDING_BOX_TYPE__DIMENSIONS:
				setDimensions(DIMENSIONS_EDEFAULT);
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
			case OWSPackage.WGS84_BOUNDING_BOX_TYPE__LOWER_CORNER:
				return LOWER_CORNER_EDEFAULT == null ? lowerCorner != null : !LOWER_CORNER_EDEFAULT.equals(lowerCorner);
			case OWSPackage.WGS84_BOUNDING_BOX_TYPE__UPPER_CORNER:
				return UPPER_CORNER_EDEFAULT == null ? upperCorner != null : !UPPER_CORNER_EDEFAULT.equals(upperCorner);
			case OWSPackage.WGS84_BOUNDING_BOX_TYPE__CRS:
				return CRS_EDEFAULT == null ? crs != null : !CRS_EDEFAULT.equals(crs);
			case OWSPackage.WGS84_BOUNDING_BOX_TYPE__DIMENSIONS:
				return DIMENSIONS_EDEFAULT == null ? dimensions != null : !DIMENSIONS_EDEFAULT.equals(dimensions);
		}
		return eDynamicIsSet(eFeature);
	}

} //WGS84BoundingBoxTypeImpl
