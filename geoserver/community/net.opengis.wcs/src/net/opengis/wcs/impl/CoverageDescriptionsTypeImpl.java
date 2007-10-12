/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.impl;

import java.util.Collection;

import net.opengis.wcs.CoverageDescriptionType;
import net.opengis.wcs.CoverageDescriptionsType;
import net.opengis.wcs.wcsPackage;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Coverage Descriptions Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs.impl.CoverageDescriptionsTypeImpl#getCoverageDescription <em>Coverage Description</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CoverageDescriptionsTypeImpl extends EObjectImpl implements CoverageDescriptionsType {
    /**
     * The cached value of the '{@link #getCoverageDescription() <em>Coverage Description</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageDescription()
     * @generated
     * @ordered
     */
    protected EList coverageDescription;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CoverageDescriptionsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return wcsPackage.Literals.COVERAGE_DESCRIPTIONS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getCoverageDescription() {
        if (coverageDescription == null) {
            coverageDescription = new EObjectContainmentEList(CoverageDescriptionType.class, this, wcsPackage.COVERAGE_DESCRIPTIONS_TYPE__COVERAGE_DESCRIPTION);
        }
        return coverageDescription;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wcsPackage.COVERAGE_DESCRIPTIONS_TYPE__COVERAGE_DESCRIPTION:
                return ((InternalEList)getCoverageDescription()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case wcsPackage.COVERAGE_DESCRIPTIONS_TYPE__COVERAGE_DESCRIPTION:
                return getCoverageDescription();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case wcsPackage.COVERAGE_DESCRIPTIONS_TYPE__COVERAGE_DESCRIPTION:
                getCoverageDescription().clear();
                getCoverageDescription().addAll((Collection)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(int featureID) {
        switch (featureID) {
            case wcsPackage.COVERAGE_DESCRIPTIONS_TYPE__COVERAGE_DESCRIPTION:
                getCoverageDescription().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case wcsPackage.COVERAGE_DESCRIPTIONS_TYPE__COVERAGE_DESCRIPTION:
                return coverageDescription != null && !coverageDescription.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //CoverageDescriptionsTypeImpl
