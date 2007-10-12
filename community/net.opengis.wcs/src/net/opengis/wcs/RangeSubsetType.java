/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Range Subset Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Selection of desired subset of the coverage's range fields, (optionally) the interpolation method applied to eachfield, and (optionally) field subsets. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs.RangeSubsetType#getFieldSubset <em>Field Subset</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs.wcsPackage#getRangeSubsetType()
 * @model extendedMetaData="name='RangeSubsetType' kind='elementOnly'"
 * @generated
 */
public interface RangeSubsetType extends EObject {
    /**
     * Returns the value of the '<em><b>Field Subset</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wcs.FieldSubsetType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of one or more desired subsets of range fields. TBD. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Field Subset</em>' containment reference list.
     * @see net.opengis.wcs.wcsPackage#getRangeSubsetType_FieldSubset()
     * @model type="net.opengis.wcs.FieldSubsetType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='FieldSubset' namespace='##targetNamespace'"
     * @generated
     */
    EList getFieldSubset();

} // RangeSubsetType
