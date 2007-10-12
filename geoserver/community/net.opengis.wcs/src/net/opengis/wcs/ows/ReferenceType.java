/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.ows;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Reference Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Complete reference to a remote or local resource, allowing including metadata about that resource. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs.ows.ReferenceType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.ReferenceType#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.ReferenceType#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.ReferenceType#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs.ows.owcsPackage#getReferenceType()
 * @model extendedMetaData="name='ReferenceType' kind='elementOnly'"
 * @generated
 */
public interface ReferenceType extends AbstractReferenceBaseType {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unique identifier of the referenced resource. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' attribute.
     * @see #setIdentifier(Object)
     * @see net.opengis.wcs.ows.owcsPackage#getReferenceType_Identifier()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
     *        extendedMetaData="kind='element' name='Identifier' namespace='##targetNamespace'"
     * @generated
     */
    Object getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.ReferenceType#getIdentifier <em>Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' attribute.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(Object value);

    /**
     * Returns the value of the '<em><b>Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract</em>' attribute.
     * @see #setAbstract(Object)
     * @see net.opengis.wcs.ows.owcsPackage#getReferenceType_Abstract()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
     *        extendedMetaData="kind='element' name='Abstract' namespace='http://www.opengis.net/ows'"
     * @generated
     */
    Object getAbstract();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.ReferenceType#getAbstract <em>Abstract</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Abstract</em>' attribute.
     * @see #getAbstract()
     * @generated
     */
    void setAbstract(Object value);

    /**
     * Returns the value of the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The format of the referenced resource. This element is omitted when the mime type is indicated in the http header of the reference. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Format</em>' attribute.
     * @see #setFormat(Object)
     * @see net.opengis.wcs.ows.owcsPackage#getReferenceType_Format()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
     *        extendedMetaData="kind='element' name='Format' namespace='##targetNamespace'"
     * @generated
     */
    Object getFormat();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.ReferenceType#getFormat <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Format</em>' attribute.
     * @see #getFormat()
     * @generated
     */
    void setFormat(Object value);

    /**
     * Returns the value of the '<em><b>Metadata</b></em>' attribute list.
     * The list contents are of type {@link java.lang.Object}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of additional metadata about this resource. A list of optional metadata elements for this ReferenceType could be specified in the Implementation Specification for each use of this type in a specific OWS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Metadata</em>' attribute list.
     * @see net.opengis.wcs.ows.owcsPackage#getReferenceType_Metadata()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
     *        extendedMetaData="kind='element' name='Metadata' namespace='http://www.opengis.net/ows'"
     * @generated
     */
    EList getMetadata();

} // ReferenceType
