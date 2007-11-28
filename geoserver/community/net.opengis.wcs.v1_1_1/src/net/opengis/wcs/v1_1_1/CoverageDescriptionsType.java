/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.v1_1_1;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coverage Descriptions Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs.v1_1_1.CoverageDescriptionsType#getCoverageDescription <em>Coverage Description</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs.v1_1_1.Wcs111Package#getCoverageDescriptionsType()
 * @model extendedMetaData="name='CoverageDescriptions_._type' kind='elementOnly'"
 * @generated
 */
public interface CoverageDescriptionsType extends EObject {
    /**
     * Returns the value of the '<em><b>Coverage Description</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wcs.v1_1_1.CoverageDescriptionType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Coverage Description</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Coverage Description</em>' containment reference list.
     * @see net.opengis.wcs.v1_1_1.Wcs111Package#getCoverageDescriptionsType_CoverageDescription()
     * @model type="net.opengis.wcs.v1_1_1.CoverageDescriptionType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='CoverageDescription' namespace='##targetNamespace'"
     * @generated
     */
    EList getCoverageDescription();

} // CoverageDescriptionsType
