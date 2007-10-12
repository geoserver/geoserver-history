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
 * A representation of the model object '<em><b>Field Subset Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs.FieldSubsetType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wcs.FieldSubsetType#getInterpolationType <em>Interpolation Type</em>}</li>
 *   <li>{@link net.opengis.wcs.FieldSubsetType#getAxisSubset <em>Axis Subset</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs.wcsPackage#getFieldSubsetType()
 * @model extendedMetaData="name='FieldSubset_._type' kind='elementOnly'"
 * @generated
 */
public interface FieldSubsetType extends EObject {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of this requested Field. This identifier must be unique for this Coverage. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' attribute.
     * @see #setIdentifier(Object)
     * @see net.opengis.wcs.wcsPackage#getFieldSubsetType_Identifier()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType" required="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='http://www.opengis.net/wcs/1.1/ows'"
     * @generated
     */
    Object getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.wcs.FieldSubsetType#getIdentifier <em>Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' attribute.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(Object value);

    /**
     * Returns the value of the '<em><b>Interpolation Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional identifier of the spatial interpolation type to be applied to this range field. This interpolation type shall be one that is identified for this Field in the CoverageDescription. When this parameter is omitted, the interpolation method used shall be the default method specified for this Field, if any. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Interpolation Type</em>' attribute.
     * @see #setInterpolationType(String)
     * @see net.opengis.wcs.wcsPackage#getFieldSubsetType_InterpolationType()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='InterpolationType' namespace='##targetNamespace'"
     * @generated
     */
    String getInterpolationType();

    /**
     * Sets the value of the '{@link net.opengis.wcs.FieldSubsetType#getInterpolationType <em>Interpolation Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Interpolation Type</em>' attribute.
     * @see #getInterpolationType()
     * @generated
     */
    void setInterpolationType(String value);

    /**
     * Returns the value of the '<em><b>Axis Subset</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wcs.AxisSubsetType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of zero or more axis subsets for this field. TBD. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Axis Subset</em>' containment reference list.
     * @see net.opengis.wcs.wcsPackage#getFieldSubsetType_AxisSubset()
     * @model type="net.opengis.wcs.AxisSubsetType" containment="true"
     *        extendedMetaData="kind='element' name='AxisSubset' namespace='##targetNamespace'"
     * @generated
     */
    EList getAxisSubset();

} // FieldSubsetType
