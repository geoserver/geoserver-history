/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.ows;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coverages Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Group of coverages that can be used as the response from the WCS GetCoverage operation, allowing each coverage to include or reference multiple files. This CoverageGroup element may also be used for outputs from, or inputs to, other OWS operations. 
 * This element is specified for use where the ManifestType contents are needed for an operation response or request that contains a group of one or more coverages, and the Manifest element name and contents are not considered to be specific enough. 
 * <!-- end-model-doc -->
 *
 *
 * @see net.opengis.wcs.ows.owcsPackage#getCoveragesType()
 * @model extendedMetaData="name='CoveragesType' kind='elementOnly'"
 * @generated
 */
public interface CoveragesType extends ManifestType {
} // CoveragesType
