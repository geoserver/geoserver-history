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
 * A representation of the model object '<em><b>Accept Formats Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Prioritized sequence of zero or more GetCapabilities operation response formats desired by client, with preferred formats listed first. Each response format shall be identified by its MIME type. See AcceptFormats parameter use subclause for more information. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs.ows.AcceptFormatsType#getOutputFormat <em>Output Format</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs.ows.owcsPackage#getAcceptFormatsType()
 * @model extendedMetaData="name='AcceptFormatsType' kind='elementOnly'"
 * @generated
 */
public interface AcceptFormatsType extends EObject {
    /**
     * Returns the value of the '<em><b>Output Format</b></em>' attribute list.
     * The list contents are of type {@link java.lang.Object}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Output Format</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Output Format</em>' attribute list.
     * @see net.opengis.wcs.ows.owcsPackage#getAcceptFormatsType_OutputFormat()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
     *        extendedMetaData="kind='element' name='OutputFormat' namespace='##targetNamespace'"
     * @generated
     */
    EList getOutputFormat();

} // AcceptFormatsType
