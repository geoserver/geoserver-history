/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.ows.impl;

import java.util.Collection;

import net.opengis.wcs.ows.IdentificationType;
import net.opengis.wcs.ows.owcsPackage;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Identification Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs.ows.impl.IdentificationTypeImpl#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.IdentificationTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.IdentificationTypeImpl#getAvailableCRSGroup <em>Available CRS Group</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.IdentificationTypeImpl#getAvailableCRS <em>Available CRS</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IdentificationTypeImpl extends BasicIdentificationTypeImpl implements IdentificationType {
    /**
     * The cached value of the '{@link #getBoundingBox() <em>Bounding Box</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBoundingBox()
     * @generated
     * @ordered
     */
    protected EList boundingBox;

    /**
     * The cached value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
    protected EList outputFormat;

    /**
     * The cached value of the '{@link #getAvailableCRSGroup() <em>Available CRS Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAvailableCRSGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap availableCRSGroup;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected IdentificationTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return owcsPackage.Literals.IDENTIFICATION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getBoundingBox() {
        if (boundingBox == null) {
            boundingBox = new EDataTypeEList(Object.class, this, owcsPackage.IDENTIFICATION_TYPE__BOUNDING_BOX);
        }
        return boundingBox;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getOutputFormat() {
        if (outputFormat == null) {
            outputFormat = new EDataTypeEList(Object.class, this, owcsPackage.IDENTIFICATION_TYPE__OUTPUT_FORMAT);
        }
        return outputFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getAvailableCRSGroup() {
        if (availableCRSGroup == null) {
            availableCRSGroup = new BasicFeatureMap(this, owcsPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP);
        }
        return availableCRSGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getAvailableCRS() {
        return getAvailableCRSGroup().list(owcsPackage.Literals.IDENTIFICATION_TYPE__AVAILABLE_CRS);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case owcsPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
                return ((InternalEList)getAvailableCRSGroup()).basicRemove(otherEnd, msgs);
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
            case owcsPackage.IDENTIFICATION_TYPE__BOUNDING_BOX:
                return getBoundingBox();
            case owcsPackage.IDENTIFICATION_TYPE__OUTPUT_FORMAT:
                return getOutputFormat();
            case owcsPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
                if (coreType) return getAvailableCRSGroup();
                return ((FeatureMap.Internal)getAvailableCRSGroup()).getWrapper();
            case owcsPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS:
                return getAvailableCRS();
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
            case owcsPackage.IDENTIFICATION_TYPE__BOUNDING_BOX:
                getBoundingBox().clear();
                getBoundingBox().addAll((Collection)newValue);
                return;
            case owcsPackage.IDENTIFICATION_TYPE__OUTPUT_FORMAT:
                getOutputFormat().clear();
                getOutputFormat().addAll((Collection)newValue);
                return;
            case owcsPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
                ((FeatureMap.Internal)getAvailableCRSGroup()).set(newValue);
                return;
            case owcsPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS:
                getAvailableCRS().clear();
                getAvailableCRS().addAll((Collection)newValue);
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
            case owcsPackage.IDENTIFICATION_TYPE__BOUNDING_BOX:
                getBoundingBox().clear();
                return;
            case owcsPackage.IDENTIFICATION_TYPE__OUTPUT_FORMAT:
                getOutputFormat().clear();
                return;
            case owcsPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
                getAvailableCRSGroup().clear();
                return;
            case owcsPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS:
                getAvailableCRS().clear();
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
            case owcsPackage.IDENTIFICATION_TYPE__BOUNDING_BOX:
                return boundingBox != null && !boundingBox.isEmpty();
            case owcsPackage.IDENTIFICATION_TYPE__OUTPUT_FORMAT:
                return outputFormat != null && !outputFormat.isEmpty();
            case owcsPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP:
                return availableCRSGroup != null && !availableCRSGroup.isEmpty();
            case owcsPackage.IDENTIFICATION_TYPE__AVAILABLE_CRS:
                return !getAvailableCRS().isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (boundingBox: ");
        result.append(boundingBox);
        result.append(", outputFormat: ");
        result.append(outputFormat);
        result.append(", availableCRSGroup: ");
        result.append(availableCRSGroup);
        result.append(')');
        return result.toString();
    }

} //IdentificationTypeImpl
