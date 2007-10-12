/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.ows;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Interpolation Methods Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs.ows.InterpolationMethodsType#getDefaultMethod <em>Default Method</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.InterpolationMethodsType#getOtherMethod <em>Other Method</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs.ows.owcsPackage#getInterpolationMethodsType()
 * @model extendedMetaData="name='InterpolationMethods_._type' kind='elementOnly'"
 * @generated
 */
public interface InterpolationMethodsType extends EObject {
    /**
     * Returns the value of the '<em><b>Default Method</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Spatial interpolation method used when client doesn’t specify one. This default interpolation method should be the recommended or normal method for this coverage range field. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Default Method</em>' containment reference.
     * @see #setDefaultMethod(InterpolationMethodType)
     * @see net.opengis.wcs.ows.owcsPackage#getInterpolationMethodsType_DefaultMethod()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='DefaultMethod' namespace='##targetNamespace'"
     * @generated
     */
    InterpolationMethodType getDefaultMethod();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.InterpolationMethodsType#getDefaultMethod <em>Default Method</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default Method</em>' containment reference.
     * @see #getDefaultMethod()
     * @generated
     */
    void setDefaultMethod(InterpolationMethodType value);

    /**
     * Returns the value of the '<em><b>Other Method</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wcs.ows.InterpolationMethodType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of identifiers of other spatial interpolation methods that server can apply to this range field. When the default interpolation method is “none”, no other methods should be listed. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Other Method</em>' containment reference list.
     * @see net.opengis.wcs.ows.owcsPackage#getInterpolationMethodsType_OtherMethod()
     * @model type="net.opengis.wcs.ows.InterpolationMethodType" containment="true"
     *        extendedMetaData="kind='element' name='OtherMethod' namespace='##targetNamespace'"
     * @generated
     */
    EList getOtherMethod();

} // InterpolationMethodsType
