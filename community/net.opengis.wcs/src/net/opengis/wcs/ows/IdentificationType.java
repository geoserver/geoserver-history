/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.ows;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Identification Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Extended metadata identifying and describing a set of data. This type shall be extended if needed for each specific OWS to include additional metadata for each type of dataset. If needed, this type should first be restricted for each specific OWS to change the multiplicity (or optionality) of some elements. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs.ows.IdentificationType#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.IdentificationType#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.IdentificationType#getAvailableCRSGroup <em>Available CRS Group</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.IdentificationType#getAvailableCRS <em>Available CRS</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs.ows.owcsPackage#getIdentificationType()
 * @model extendedMetaData="name='IdentificationType' kind='elementOnly'"
 * @generated
 */
public interface IdentificationType extends BasicIdentificationType {
    /**
     * Returns the value of the '<em><b>Bounding Box</b></em>' attribute list.
     * The list contents are of type {@link java.lang.Object}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of zero or more bounding boxes whose union describes the extent of this dataset. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Box</em>' attribute list.
     * @see net.opengis.wcs.ows.owcsPackage#getIdentificationType_BoundingBox()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
     *        extendedMetaData="kind='element' name='BoundingBox' namespace='http://www.opengis.net/ows'"
     * @generated
     */
    EList getBoundingBox();

    /**
     * Returns the value of the '<em><b>Output Format</b></em>' attribute list.
     * The list contents are of type {@link java.lang.Object}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of zero or more references to data formats supported for server outputs. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Output Format</em>' attribute list.
     * @see net.opengis.wcs.ows.owcsPackage#getIdentificationType_OutputFormat()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
     *        extendedMetaData="kind='element' name='OutputFormat' namespace='##targetNamespace'"
     * @generated
     */
    EList getOutputFormat();

    /**
     * Returns the value of the '<em><b>Available CRS Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of zero or more available coordinate reference systems. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Available CRS Group</em>' attribute list.
     * @see net.opengis.wcs.ows.owcsPackage#getIdentificationType_AvailableCRSGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='AvailableCRS:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getAvailableCRSGroup();

    /**
     * Returns the value of the '<em><b>Available CRS</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of zero or more available coordinate reference systems. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Available CRS</em>' attribute list.
     * @see net.opengis.wcs.ows.owcsPackage#getIdentificationType_AvailableCRS()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AvailableCRS' namespace='##targetNamespace' group='AvailableCRS:group'"
     * @generated
     */
    EList getAvailableCRS();

} // IdentificationType
