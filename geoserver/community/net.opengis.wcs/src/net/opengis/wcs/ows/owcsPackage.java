/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.ows;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * This XML Schema Document specifies types and elements for document or resource references and for package manifests that contain multiple references. The contents of each type and element specified here can be restricted and/or extended for each use in a specific OWS specification.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved. 
 * This XML Schema Document encodes the parts of the MD_DataIdentification class of ISO 19115 (OGC Abstract Specification Topic 11) which are expected to be used for most datasets. This Schema also encodes the parts of this class that are expected to be useful for other metadata. Both are expected to be used within the Contents section of OWS service metadata (Capabilities) documents.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved. 
 * <!-- end-model-doc -->
 * @see net.opengis.wcs.ows.owcsFactory
 * @model kind="package"
 *        annotation="http://www.w3.org/XML/1998/namespace lang='en'"
 * @generated
 */
public interface owcsPackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "ows";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.opengis.net/wcs/1.1/ows";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "ows";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    owcsPackage eINSTANCE = net.opengis.wcs.ows.impl.owcsPackageImpl.init();

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.AbstractReferenceBaseTypeImpl <em>Abstract Reference Base Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.AbstractReferenceBaseTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getAbstractReferenceBaseType()
     * @generated
     */
    int ABSTRACT_REFERENCE_BASE_TYPE = 0;

    /**
     * The feature id for the '<em><b>Actuate</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_REFERENCE_BASE_TYPE__ACTUATE = 0;

    /**
     * The feature id for the '<em><b>Arcrole</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_REFERENCE_BASE_TYPE__ARCROLE = 1;

    /**
     * The feature id for the '<em><b>Href</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_REFERENCE_BASE_TYPE__HREF = 2;

    /**
     * The feature id for the '<em><b>Role</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_REFERENCE_BASE_TYPE__ROLE = 3;

    /**
     * The feature id for the '<em><b>Show</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_REFERENCE_BASE_TYPE__SHOW = 4;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_REFERENCE_BASE_TYPE__TITLE = 5;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_REFERENCE_BASE_TYPE__TYPE = 6;

    /**
     * The number of structural features of the '<em>Abstract Reference Base Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_REFERENCE_BASE_TYPE_FEATURE_COUNT = 7;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.AcceptFormatsTypeImpl <em>Accept Formats Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.AcceptFormatsTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getAcceptFormatsType()
     * @generated
     */
    int ACCEPT_FORMATS_TYPE = 1;

    /**
     * The feature id for the '<em><b>Output Format</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT = 0;

    /**
     * The number of structural features of the '<em>Accept Formats Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCEPT_FORMATS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.AcceptVersionsTypeImpl <em>Accept Versions Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.AcceptVersionsTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getAcceptVersionsType()
     * @generated
     */
    int ACCEPT_VERSIONS_TYPE = 2;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCEPT_VERSIONS_TYPE__VERSION = 0;

    /**
     * The number of structural features of the '<em>Accept Versions Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACCEPT_VERSIONS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.AllowedValuesTypeImpl <em>Allowed Values Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.AllowedValuesTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getAllowedValuesType()
     * @generated
     */
    int ALLOWED_VALUES_TYPE = 3;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALLOWED_VALUES_TYPE__GROUP = 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALLOWED_VALUES_TYPE__VALUE = 1;

    /**
     * The feature id for the '<em><b>Range</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALLOWED_VALUES_TYPE__RANGE = 2;

    /**
     * The number of structural features of the '<em>Allowed Values Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALLOWED_VALUES_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.AnyValueTypeImpl <em>Any Value Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.AnyValueTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getAnyValueType()
     * @generated
     */
    int ANY_VALUE_TYPE = 4;

    /**
     * The number of structural features of the '<em>Any Value Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANY_VALUE_TYPE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.DescriptionTypeImpl <em>Description Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.DescriptionTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getDescriptionType()
     * @generated
     */
    int DESCRIPTION_TYPE = 9;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIPTION_TYPE__TITLE = 0;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIPTION_TYPE__ABSTRACT = 1;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIPTION_TYPE__KEYWORDS = 2;

    /**
     * The number of structural features of the '<em>Description Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIPTION_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.BasicIdentificationTypeImpl <em>Basic Identification Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.BasicIdentificationTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getBasicIdentificationType()
     * @generated
     */
    int BASIC_IDENTIFICATION_TYPE = 5;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_IDENTIFICATION_TYPE__TITLE = DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_IDENTIFICATION_TYPE__ABSTRACT = DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_IDENTIFICATION_TYPE__KEYWORDS = DESCRIPTION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_IDENTIFICATION_TYPE__IDENTIFIER = DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_IDENTIFICATION_TYPE__METADATA = DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Basic Identification Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT = DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.CapabilitiesBaseTypeImpl <em>Capabilities Base Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.CapabilitiesBaseTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getCapabilitiesBaseType()
     * @generated
     */
    int CAPABILITIES_BASE_TYPE = 6;

    /**
     * The feature id for the '<em><b>Service Identification</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION = 0;

    /**
     * The feature id for the '<em><b>Service Provider</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER = 1;

    /**
     * The feature id for the '<em><b>Operations Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA = 2;

    /**
     * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_BASE_TYPE__UPDATE_SEQUENCE = 3;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_BASE_TYPE__VERSION = 4;

    /**
     * The number of structural features of the '<em>Capabilities Base Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_BASE_TYPE_FEATURE_COUNT = 5;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.ManifestTypeImpl <em>Manifest Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.ManifestTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getManifestType()
     * @generated
     */
    int MANIFEST_TYPE = 19;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MANIFEST_TYPE__TITLE = BASIC_IDENTIFICATION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MANIFEST_TYPE__ABSTRACT = BASIC_IDENTIFICATION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MANIFEST_TYPE__KEYWORDS = BASIC_IDENTIFICATION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MANIFEST_TYPE__IDENTIFIER = BASIC_IDENTIFICATION_TYPE__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MANIFEST_TYPE__METADATA = BASIC_IDENTIFICATION_TYPE__METADATA;

    /**
     * The feature id for the '<em><b>Reference Group Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MANIFEST_TYPE__REFERENCE_GROUP_GROUP = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Reference Group</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MANIFEST_TYPE__REFERENCE_GROUP = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Manifest Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MANIFEST_TYPE_FEATURE_COUNT = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.CoveragesTypeImpl <em>Coverages Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.CoveragesTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getCoveragesType()
     * @generated
     */
    int COVERAGES_TYPE = 7;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGES_TYPE__TITLE = MANIFEST_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGES_TYPE__ABSTRACT = MANIFEST_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGES_TYPE__KEYWORDS = MANIFEST_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGES_TYPE__IDENTIFIER = MANIFEST_TYPE__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGES_TYPE__METADATA = MANIFEST_TYPE__METADATA;

    /**
     * The feature id for the '<em><b>Reference Group Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGES_TYPE__REFERENCE_GROUP_GROUP = MANIFEST_TYPE__REFERENCE_GROUP_GROUP;

    /**
     * The feature id for the '<em><b>Reference Group</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGES_TYPE__REFERENCE_GROUP = MANIFEST_TYPE__REFERENCE_GROUP;

    /**
     * The number of structural features of the '<em>Coverages Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGES_TYPE_FEATURE_COUNT = MANIFEST_TYPE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.DCPTypeImpl <em>DCP Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.DCPTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getDCPType()
     * @generated
     */
    int DCP_TYPE = 8;

    /**
     * The feature id for the '<em><b>HTTP</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DCP_TYPE__HTTP = 0;

    /**
     * The number of structural features of the '<em>DCP Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DCP_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.DocumentRootImpl <em>Document Root</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.DocumentRootImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getDocumentRoot()
     * @generated
     */
    int DOCUMENT_ROOT = 10;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__MIXED = 0;

    /**
     * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

    /**
     * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

    /**
     * The feature id for the '<em><b>Abstract Reference Base</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ABSTRACT_REFERENCE_BASE = 3;

    /**
     * The feature id for the '<em><b>Access Constraints</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ACCESS_CONSTRAINTS = 4;

    /**
     * The feature id for the '<em><b>Allowed Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ALLOWED_VALUES = 5;

    /**
     * The feature id for the '<em><b>Any Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ANY_VALUE = 6;

    /**
     * The feature id for the '<em><b>Available CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__AVAILABLE_CRS = 7;

    /**
     * The feature id for the '<em><b>Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__COVERAGE = 8;

    /**
     * The feature id for the '<em><b>Reference Group</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__REFERENCE_GROUP = 9;

    /**
     * The feature id for the '<em><b>Coverages</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__COVERAGES = 10;

    /**
     * The feature id for the '<em><b>Manifest</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__MANIFEST = 11;

    /**
     * The feature id for the '<em><b>Data Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DATA_TYPE = 12;

    /**
     * The feature id for the '<em><b>DCP</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DCP = 13;

    /**
     * The feature id for the '<em><b>Default Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DEFAULT_VALUE = 14;

    /**
     * The feature id for the '<em><b>Extended Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__EXTENDED_CAPABILITIES = 15;

    /**
     * The feature id for the '<em><b>Fees</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__FEES = 16;

    /**
     * The feature id for the '<em><b>Get Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__GET_CAPABILITIES = 17;

    /**
     * The feature id for the '<em><b>HTTP</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__HTTP = 18;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__IDENTIFIER = 19;

    /**
     * The feature id for the '<em><b>Interpolation Methods</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__INTERPOLATION_METHODS = 20;

    /**
     * The feature id for the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__LANGUAGE = 21;

    /**
     * The feature id for the '<em><b>Maximum Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__MAXIMUM_VALUE = 22;

    /**
     * The feature id for the '<em><b>Meaning</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__MEANING = 23;

    /**
     * The feature id for the '<em><b>Minimum Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__MINIMUM_VALUE = 24;

    /**
     * The feature id for the '<em><b>No Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__NO_VALUES = 25;

    /**
     * The feature id for the '<em><b>Operation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__OPERATION = 26;

    /**
     * The feature id for the '<em><b>Operations Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__OPERATIONS_METADATA = 27;

    /**
     * The feature id for the '<em><b>Output Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__OUTPUT_FORMAT = 28;

    /**
     * The feature id for the '<em><b>Range</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__RANGE = 29;

    /**
     * The feature id for the '<em><b>Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__REFERENCE = 30;

    /**
     * The feature id for the '<em><b>Reference System</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__REFERENCE_SYSTEM = 31;

    /**
     * The feature id for the '<em><b>Service Identification</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SERVICE_IDENTIFICATION = 32;

    /**
     * The feature id for the '<em><b>Service Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SERVICE_REFERENCE = 33;

    /**
     * The feature id for the '<em><b>Spacing</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SPACING = 34;

    /**
     * The feature id for the '<em><b>Supported CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SUPPORTED_CRS = 35;

    /**
     * The feature id for the '<em><b>UOM</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__UOM = 36;

    /**
     * The feature id for the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__VALUE = 37;

    /**
     * The feature id for the '<em><b>Values Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__VALUES_REFERENCE = 38;

    /**
     * The feature id for the '<em><b>Range Closure</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__RANGE_CLOSURE = 39;

    /**
     * The feature id for the '<em><b>Reference1</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__REFERENCE1 = 40;

    /**
     * The number of structural features of the '<em>Document Root</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT_FEATURE_COUNT = 41;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.DomainMetadataTypeImpl <em>Domain Metadata Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.DomainMetadataTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getDomainMetadataType()
     * @generated
     */
    int DOMAIN_METADATA_TYPE = 11;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_METADATA_TYPE__VALUE = 0;

    /**
     * The feature id for the '<em><b>Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_METADATA_TYPE__REFERENCE = 1;

    /**
     * The number of structural features of the '<em>Domain Metadata Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_METADATA_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.UnNamedDomainTypeImpl <em>Un Named Domain Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.UnNamedDomainTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getUnNamedDomainType()
     * @generated
     */
    int UN_NAMED_DOMAIN_TYPE = 30;

    /**
     * The feature id for the '<em><b>Allowed Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__ALLOWED_VALUES = 0;

    /**
     * The feature id for the '<em><b>Any Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__ANY_VALUE = 1;

    /**
     * The feature id for the '<em><b>No Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__NO_VALUES = 2;

    /**
     * The feature id for the '<em><b>Values Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__VALUES_REFERENCE = 3;

    /**
     * The feature id for the '<em><b>Default Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__DEFAULT_VALUE = 4;

    /**
     * The feature id for the '<em><b>Meaning</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__MEANING = 5;

    /**
     * The feature id for the '<em><b>Data Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__DATA_TYPE = 6;

    /**
     * The feature id for the '<em><b>UOM</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__UOM = 7;

    /**
     * The feature id for the '<em><b>Reference System</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__REFERENCE_SYSTEM = 8;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE__METADATA = 9;

    /**
     * The number of structural features of the '<em>Un Named Domain Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UN_NAMED_DOMAIN_TYPE_FEATURE_COUNT = 10;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.DomainTypeImpl <em>Domain Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.DomainTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getDomainType()
     * @generated
     */
    int DOMAIN_TYPE = 12;

    /**
     * The feature id for the '<em><b>Allowed Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__ALLOWED_VALUES = UN_NAMED_DOMAIN_TYPE__ALLOWED_VALUES;

    /**
     * The feature id for the '<em><b>Any Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__ANY_VALUE = UN_NAMED_DOMAIN_TYPE__ANY_VALUE;

    /**
     * The feature id for the '<em><b>No Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__NO_VALUES = UN_NAMED_DOMAIN_TYPE__NO_VALUES;

    /**
     * The feature id for the '<em><b>Values Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__VALUES_REFERENCE = UN_NAMED_DOMAIN_TYPE__VALUES_REFERENCE;

    /**
     * The feature id for the '<em><b>Default Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__DEFAULT_VALUE = UN_NAMED_DOMAIN_TYPE__DEFAULT_VALUE;

    /**
     * The feature id for the '<em><b>Meaning</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__MEANING = UN_NAMED_DOMAIN_TYPE__MEANING;

    /**
     * The feature id for the '<em><b>Data Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__DATA_TYPE = UN_NAMED_DOMAIN_TYPE__DATA_TYPE;

    /**
     * The feature id for the '<em><b>UOM</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__UOM = UN_NAMED_DOMAIN_TYPE__UOM;

    /**
     * The feature id for the '<em><b>Reference System</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__REFERENCE_SYSTEM = UN_NAMED_DOMAIN_TYPE__REFERENCE_SYSTEM;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__METADATA = UN_NAMED_DOMAIN_TYPE__METADATA;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE__NAME = UN_NAMED_DOMAIN_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Domain Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_TYPE_FEATURE_COUNT = UN_NAMED_DOMAIN_TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.GetCapabilitiesTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getGetCapabilitiesType()
     * @generated
     */
    int GET_CAPABILITIES_TYPE = 13;

    /**
     * The feature id for the '<em><b>Accept Versions</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS = 0;

    /**
     * The feature id for the '<em><b>Sections</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__SECTIONS = 1;

    /**
     * The feature id for the '<em><b>Accept Formats</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__ACCEPT_FORMATS = 2;

    /**
     * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE = 3;

    /**
     * The number of structural features of the '<em>Get Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.HTTPTypeImpl <em>HTTP Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.HTTPTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getHTTPType()
     * @generated
     */
    int HTTP_TYPE = 14;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HTTP_TYPE__GROUP = 0;

    /**
     * The feature id for the '<em><b>Get</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HTTP_TYPE__GET = 1;

    /**
     * The feature id for the '<em><b>Post</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HTTP_TYPE__POST = 2;

    /**
     * The number of structural features of the '<em>HTTP Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int HTTP_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.IdentificationTypeImpl <em>Identification Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.IdentificationTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getIdentificationType()
     * @generated
     */
    int IDENTIFICATION_TYPE = 15;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__TITLE = BASIC_IDENTIFICATION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__ABSTRACT = BASIC_IDENTIFICATION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__KEYWORDS = BASIC_IDENTIFICATION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__IDENTIFIER = BASIC_IDENTIFICATION_TYPE__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__METADATA = BASIC_IDENTIFICATION_TYPE__METADATA;

    /**
     * The feature id for the '<em><b>Bounding Box</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__BOUNDING_BOX = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Output Format</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__OUTPUT_FORMAT = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Available CRS Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Available CRS</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE__AVAILABLE_CRS = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Identification Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDENTIFICATION_TYPE_FEATURE_COUNT = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.InterpolationMethodBaseTypeImpl <em>Interpolation Method Base Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.InterpolationMethodBaseTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getInterpolationMethodBaseType()
     * @generated
     */
    int INTERPOLATION_METHOD_BASE_TYPE = 16;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERPOLATION_METHOD_BASE_TYPE__VALUE = 0;

    /**
     * The number of structural features of the '<em>Interpolation Method Base Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERPOLATION_METHOD_BASE_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.InterpolationMethodsTypeImpl <em>Interpolation Methods Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.InterpolationMethodsTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getInterpolationMethodsType()
     * @generated
     */
    int INTERPOLATION_METHODS_TYPE = 17;

    /**
     * The feature id for the '<em><b>Default Method</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERPOLATION_METHODS_TYPE__DEFAULT_METHOD = 0;

    /**
     * The feature id for the '<em><b>Other Method</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERPOLATION_METHODS_TYPE__OTHER_METHOD = 1;

    /**
     * The number of structural features of the '<em>Interpolation Methods Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERPOLATION_METHODS_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.InterpolationMethodTypeImpl <em>Interpolation Method Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.InterpolationMethodTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getInterpolationMethodType()
     * @generated
     */
    int INTERPOLATION_METHOD_TYPE = 18;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERPOLATION_METHOD_TYPE__VALUE = INTERPOLATION_METHOD_BASE_TYPE__VALUE;

    /**
     * The feature id for the '<em><b>Null Resistance</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERPOLATION_METHOD_TYPE__NULL_RESISTANCE = INTERPOLATION_METHOD_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Interpolation Method Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTERPOLATION_METHOD_TYPE_FEATURE_COUNT = INTERPOLATION_METHOD_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.NoValuesTypeImpl <em>No Values Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.NoValuesTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getNoValuesType()
     * @generated
     */
    int NO_VALUES_TYPE = 20;

    /**
     * The number of structural features of the '<em>No Values Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NO_VALUES_TYPE_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.OperationsMetadataTypeImpl <em>Operations Metadata Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.OperationsMetadataTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getOperationsMetadataType()
     * @generated
     */
    int OPERATIONS_METADATA_TYPE = 21;

    /**
     * The feature id for the '<em><b>Operation</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATIONS_METADATA_TYPE__OPERATION = 0;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATIONS_METADATA_TYPE__PARAMETER = 1;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATIONS_METADATA_TYPE__CONSTRAINT = 2;

    /**
     * The feature id for the '<em><b>Extended Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES = 3;

    /**
     * The number of structural features of the '<em>Operations Metadata Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATIONS_METADATA_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.OperationTypeImpl <em>Operation Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.OperationTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getOperationType()
     * @generated
     */
    int OPERATION_TYPE = 22;

    /**
     * The feature id for the '<em><b>DCP</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_TYPE__DCP = 0;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_TYPE__PARAMETER = 1;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_TYPE__CONSTRAINT = 2;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_TYPE__METADATA = 3;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_TYPE__NAME = 4;

    /**
     * The number of structural features of the '<em>Operation Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_TYPE_FEATURE_COUNT = 5;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.RangeTypeImpl <em>Range Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.RangeTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getRangeType()
     * @generated
     */
    int RANGE_TYPE = 23;

    /**
     * The feature id for the '<em><b>Minimum Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_TYPE__MINIMUM_VALUE = 0;

    /**
     * The feature id for the '<em><b>Maximum Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_TYPE__MAXIMUM_VALUE = 1;

    /**
     * The feature id for the '<em><b>Spacing</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_TYPE__SPACING = 2;

    /**
     * The feature id for the '<em><b>Range Closure</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_TYPE__RANGE_CLOSURE = 3;

    /**
     * The number of structural features of the '<em>Range Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.ReferenceGroupTypeImpl <em>Reference Group Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.ReferenceGroupTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getReferenceGroupType()
     * @generated
     */
    int REFERENCE_GROUP_TYPE = 24;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_GROUP_TYPE__TITLE = BASIC_IDENTIFICATION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_GROUP_TYPE__ABSTRACT = BASIC_IDENTIFICATION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_GROUP_TYPE__KEYWORDS = BASIC_IDENTIFICATION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_GROUP_TYPE__IDENTIFIER = BASIC_IDENTIFICATION_TYPE__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_GROUP_TYPE__METADATA = BASIC_IDENTIFICATION_TYPE__METADATA;

    /**
     * The feature id for the '<em><b>Abstract Reference Base Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE_GROUP = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Abstract Reference Base</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Reference Group Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_GROUP_TYPE_FEATURE_COUNT = BASIC_IDENTIFICATION_TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.ReferenceTypeImpl <em>Reference Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.ReferenceTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getReferenceType()
     * @generated
     */
    int REFERENCE_TYPE = 25;

    /**
     * The feature id for the '<em><b>Actuate</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__ACTUATE = ABSTRACT_REFERENCE_BASE_TYPE__ACTUATE;

    /**
     * The feature id for the '<em><b>Arcrole</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__ARCROLE = ABSTRACT_REFERENCE_BASE_TYPE__ARCROLE;

    /**
     * The feature id for the '<em><b>Href</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__HREF = ABSTRACT_REFERENCE_BASE_TYPE__HREF;

    /**
     * The feature id for the '<em><b>Role</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__ROLE = ABSTRACT_REFERENCE_BASE_TYPE__ROLE;

    /**
     * The feature id for the '<em><b>Show</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__SHOW = ABSTRACT_REFERENCE_BASE_TYPE__SHOW;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__TITLE = ABSTRACT_REFERENCE_BASE_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__TYPE = ABSTRACT_REFERENCE_BASE_TYPE__TYPE;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__IDENTIFIER = ABSTRACT_REFERENCE_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__ABSTRACT = ABSTRACT_REFERENCE_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__FORMAT = ABSTRACT_REFERENCE_BASE_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE__METADATA = ABSTRACT_REFERENCE_BASE_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Reference Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REFERENCE_TYPE_FEATURE_COUNT = ABSTRACT_REFERENCE_BASE_TYPE_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.RequestMethodTypeImpl <em>Request Method Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.RequestMethodTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getRequestMethodType()
     * @generated
     */
    int REQUEST_METHOD_TYPE = 26;

    /**
     * The feature id for the '<em><b>Constraint</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_METHOD_TYPE__CONSTRAINT = 0;

    /**
     * The number of structural features of the '<em>Request Method Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_METHOD_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.SectionsTypeImpl <em>Sections Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.SectionsTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getSectionsType()
     * @generated
     */
    int SECTIONS_TYPE = 27;

    /**
     * The feature id for the '<em><b>Section</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SECTIONS_TYPE__SECTION = 0;

    /**
     * The number of structural features of the '<em>Sections Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SECTIONS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.ServiceIdentificationTypeImpl <em>Service Identification Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.ServiceIdentificationTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getServiceIdentificationType()
     * @generated
     */
    int SERVICE_IDENTIFICATION_TYPE = 28;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_IDENTIFICATION_TYPE__TITLE = DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_IDENTIFICATION_TYPE__ABSTRACT = DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_IDENTIFICATION_TYPE__KEYWORDS = DESCRIPTION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Service Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE = DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Service Type Version</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION = DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Profile</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_IDENTIFICATION_TYPE__PROFILE = DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Fees</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_IDENTIFICATION_TYPE__FEES = DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Access Constraints</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS = DESCRIPTION_TYPE_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Service Identification Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_IDENTIFICATION_TYPE_FEATURE_COUNT = DESCRIPTION_TYPE_FEATURE_COUNT + 5;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.ServiceReferenceTypeImpl <em>Service Reference Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.ServiceReferenceTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getServiceReferenceType()
     * @generated
     */
    int SERVICE_REFERENCE_TYPE = 29;

    /**
     * The feature id for the '<em><b>Actuate</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__ACTUATE = REFERENCE_TYPE__ACTUATE;

    /**
     * The feature id for the '<em><b>Arcrole</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__ARCROLE = REFERENCE_TYPE__ARCROLE;

    /**
     * The feature id for the '<em><b>Href</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__HREF = REFERENCE_TYPE__HREF;

    /**
     * The feature id for the '<em><b>Role</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__ROLE = REFERENCE_TYPE__ROLE;

    /**
     * The feature id for the '<em><b>Show</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__SHOW = REFERENCE_TYPE__SHOW;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__TITLE = REFERENCE_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__TYPE = REFERENCE_TYPE__TYPE;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__IDENTIFIER = REFERENCE_TYPE__IDENTIFIER;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__ABSTRACT = REFERENCE_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__FORMAT = REFERENCE_TYPE__FORMAT;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__METADATA = REFERENCE_TYPE__METADATA;

    /**
     * The feature id for the '<em><b>Request Message</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE = REFERENCE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Request Message Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE_REFERENCE = REFERENCE_TYPE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Service Reference Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_REFERENCE_TYPE_FEATURE_COUNT = REFERENCE_TYPE_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.ValuesReferenceTypeImpl <em>Values Reference Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.ValuesReferenceTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getValuesReferenceType()
     * @generated
     */
    int VALUES_REFERENCE_TYPE = 31;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUES_REFERENCE_TYPE__VALUE = 0;

    /**
     * The feature id for the '<em><b>Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUES_REFERENCE_TYPE__REFERENCE = 1;

    /**
     * The number of structural features of the '<em>Values Reference Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUES_REFERENCE_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.impl.ValueTypeImpl <em>Value Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.impl.ValueTypeImpl
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getValueType()
     * @generated
     */
    int VALUE_TYPE = 32;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_TYPE__VALUE = 0;

    /**
     * The number of structural features of the '<em>Value Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int VALUE_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs.ows.RangeClosureType <em>Range Closure Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.RangeClosureType
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getRangeClosureType()
     * @generated
     */
    int RANGE_CLOSURE_TYPE = 33;

    /**
     * The meta object id for the '<em>Interpolation Method Base Type Base</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.Object
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getInterpolationMethodBaseTypeBase()
     * @generated
     */
    int INTERPOLATION_METHOD_BASE_TYPE_BASE = 34;

    /**
     * The meta object id for the '<em>Range Closure Type Object</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.ows.RangeClosureType
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getRangeClosureTypeObject()
     * @generated
     */
    int RANGE_CLOSURE_TYPE_OBJECT = 35;

    /**
     * The meta object id for the '<em>Service Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getServiceType()
     * @generated
     */
    int SERVICE_TYPE = 36;

    /**
     * The meta object id for the '<em>Update Sequence Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getUpdateSequenceType()
     * @generated
     */
    int UPDATE_SEQUENCE_TYPE = 37;


    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.AbstractReferenceBaseType <em>Abstract Reference Base Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Abstract Reference Base Type</em>'.
     * @see net.opengis.wcs.ows.AbstractReferenceBaseType
     * @generated
     */
    EClass getAbstractReferenceBaseType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.AbstractReferenceBaseType#getActuate <em>Actuate</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Actuate</em>'.
     * @see net.opengis.wcs.ows.AbstractReferenceBaseType#getActuate()
     * @see #getAbstractReferenceBaseType()
     * @generated
     */
    EAttribute getAbstractReferenceBaseType_Actuate();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.AbstractReferenceBaseType#getArcrole <em>Arcrole</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Arcrole</em>'.
     * @see net.opengis.wcs.ows.AbstractReferenceBaseType#getArcrole()
     * @see #getAbstractReferenceBaseType()
     * @generated
     */
    EAttribute getAbstractReferenceBaseType_Arcrole();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.AbstractReferenceBaseType#getHref <em>Href</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Href</em>'.
     * @see net.opengis.wcs.ows.AbstractReferenceBaseType#getHref()
     * @see #getAbstractReferenceBaseType()
     * @generated
     */
    EAttribute getAbstractReferenceBaseType_Href();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.AbstractReferenceBaseType#getRole <em>Role</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Role</em>'.
     * @see net.opengis.wcs.ows.AbstractReferenceBaseType#getRole()
     * @see #getAbstractReferenceBaseType()
     * @generated
     */
    EAttribute getAbstractReferenceBaseType_Role();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.AbstractReferenceBaseType#getShow <em>Show</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Show</em>'.
     * @see net.opengis.wcs.ows.AbstractReferenceBaseType#getShow()
     * @see #getAbstractReferenceBaseType()
     * @generated
     */
    EAttribute getAbstractReferenceBaseType_Show();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.AbstractReferenceBaseType#getTitle <em>Title</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Title</em>'.
     * @see net.opengis.wcs.ows.AbstractReferenceBaseType#getTitle()
     * @see #getAbstractReferenceBaseType()
     * @generated
     */
    EAttribute getAbstractReferenceBaseType_Title();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.AbstractReferenceBaseType#getType <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Type</em>'.
     * @see net.opengis.wcs.ows.AbstractReferenceBaseType#getType()
     * @see #getAbstractReferenceBaseType()
     * @generated
     */
    EAttribute getAbstractReferenceBaseType_Type();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.AcceptFormatsType <em>Accept Formats Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Accept Formats Type</em>'.
     * @see net.opengis.wcs.ows.AcceptFormatsType
     * @generated
     */
    EClass getAcceptFormatsType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.AcceptFormatsType#getOutputFormat <em>Output Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Output Format</em>'.
     * @see net.opengis.wcs.ows.AcceptFormatsType#getOutputFormat()
     * @see #getAcceptFormatsType()
     * @generated
     */
    EAttribute getAcceptFormatsType_OutputFormat();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.AcceptVersionsType <em>Accept Versions Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Accept Versions Type</em>'.
     * @see net.opengis.wcs.ows.AcceptVersionsType
     * @generated
     */
    EClass getAcceptVersionsType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.AcceptVersionsType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Version</em>'.
     * @see net.opengis.wcs.ows.AcceptVersionsType#getVersion()
     * @see #getAcceptVersionsType()
     * @generated
     */
    EAttribute getAcceptVersionsType_Version();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.AllowedValuesType <em>Allowed Values Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Allowed Values Type</em>'.
     * @see net.opengis.wcs.ows.AllowedValuesType
     * @generated
     */
    EClass getAllowedValuesType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.AllowedValuesType#getGroup <em>Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see net.opengis.wcs.ows.AllowedValuesType#getGroup()
     * @see #getAllowedValuesType()
     * @generated
     */
    EAttribute getAllowedValuesType_Group();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.ows.AllowedValuesType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Value</em>'.
     * @see net.opengis.wcs.ows.AllowedValuesType#getValue()
     * @see #getAllowedValuesType()
     * @generated
     */
    EReference getAllowedValuesType_Value();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.ows.AllowedValuesType#getRange <em>Range</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Range</em>'.
     * @see net.opengis.wcs.ows.AllowedValuesType#getRange()
     * @see #getAllowedValuesType()
     * @generated
     */
    EReference getAllowedValuesType_Range();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.AnyValueType <em>Any Value Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Any Value Type</em>'.
     * @see net.opengis.wcs.ows.AnyValueType
     * @generated
     */
    EClass getAnyValueType();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.BasicIdentificationType <em>Basic Identification Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Basic Identification Type</em>'.
     * @see net.opengis.wcs.ows.BasicIdentificationType
     * @generated
     */
    EClass getBasicIdentificationType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.BasicIdentificationType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Identifier</em>'.
     * @see net.opengis.wcs.ows.BasicIdentificationType#getIdentifier()
     * @see #getBasicIdentificationType()
     * @generated
     */
    EAttribute getBasicIdentificationType_Identifier();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.BasicIdentificationType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Metadata</em>'.
     * @see net.opengis.wcs.ows.BasicIdentificationType#getMetadata()
     * @see #getBasicIdentificationType()
     * @generated
     */
    EAttribute getBasicIdentificationType_Metadata();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.CapabilitiesBaseType <em>Capabilities Base Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Capabilities Base Type</em>'.
     * @see net.opengis.wcs.ows.CapabilitiesBaseType
     * @generated
     */
    EClass getCapabilitiesBaseType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.CapabilitiesBaseType#getServiceIdentification <em>Service Identification</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Identification</em>'.
     * @see net.opengis.wcs.ows.CapabilitiesBaseType#getServiceIdentification()
     * @see #getCapabilitiesBaseType()
     * @generated
     */
    EReference getCapabilitiesBaseType_ServiceIdentification();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.CapabilitiesBaseType#getServiceProvider <em>Service Provider</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service Provider</em>'.
     * @see net.opengis.wcs.ows.CapabilitiesBaseType#getServiceProvider()
     * @see #getCapabilitiesBaseType()
     * @generated
     */
    EAttribute getCapabilitiesBaseType_ServiceProvider();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.CapabilitiesBaseType#getOperationsMetadata <em>Operations Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Operations Metadata</em>'.
     * @see net.opengis.wcs.ows.CapabilitiesBaseType#getOperationsMetadata()
     * @see #getCapabilitiesBaseType()
     * @generated
     */
    EReference getCapabilitiesBaseType_OperationsMetadata();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.CapabilitiesBaseType#getUpdateSequence <em>Update Sequence</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Update Sequence</em>'.
     * @see net.opengis.wcs.ows.CapabilitiesBaseType#getUpdateSequence()
     * @see #getCapabilitiesBaseType()
     * @generated
     */
    EAttribute getCapabilitiesBaseType_UpdateSequence();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.CapabilitiesBaseType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.wcs.ows.CapabilitiesBaseType#getVersion()
     * @see #getCapabilitiesBaseType()
     * @generated
     */
    EAttribute getCapabilitiesBaseType_Version();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.CoveragesType <em>Coverages Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Coverages Type</em>'.
     * @see net.opengis.wcs.ows.CoveragesType
     * @generated
     */
    EClass getCoveragesType();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.DCPType <em>DCP Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>DCP Type</em>'.
     * @see net.opengis.wcs.ows.DCPType
     * @generated
     */
    EClass getDCPType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DCPType#getHTTP <em>HTTP</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>HTTP</em>'.
     * @see net.opengis.wcs.ows.DCPType#getHTTP()
     * @see #getDCPType()
     * @generated
     */
    EReference getDCPType_HTTP();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.DescriptionType <em>Description Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Description Type</em>'.
     * @see net.opengis.wcs.ows.DescriptionType
     * @generated
     */
    EClass getDescriptionType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.DescriptionType#getTitle <em>Title</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Title</em>'.
     * @see net.opengis.wcs.ows.DescriptionType#getTitle()
     * @see #getDescriptionType()
     * @generated
     */
    EAttribute getDescriptionType_Title();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.DescriptionType#getAbstract <em>Abstract</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Abstract</em>'.
     * @see net.opengis.wcs.ows.DescriptionType#getAbstract()
     * @see #getDescriptionType()
     * @generated
     */
    EAttribute getDescriptionType_Abstract();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.DescriptionType#getKeywords <em>Keywords</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Keywords</em>'.
     * @see net.opengis.wcs.ows.DescriptionType#getKeywords()
     * @see #getDescriptionType()
     * @generated
     */
    EAttribute getDescriptionType_Keywords();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Document Root</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot
     * @generated
     */
    EClass getDocumentRoot();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.DocumentRoot#getMixed <em>Mixed</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getMixed()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Mixed();

    /**
     * Returns the meta object for the map '{@link net.opengis.wcs.ows.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getXMLNSPrefixMap()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XMLNSPrefixMap();

    /**
     * Returns the meta object for the map '{@link net.opengis.wcs.ows.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XSI Schema Location</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getXSISchemaLocation()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XSISchemaLocation();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getAbstractReferenceBase <em>Abstract Reference Base</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Abstract Reference Base</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getAbstractReferenceBase()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AbstractReferenceBase();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.DocumentRoot#getAccessConstraints <em>Access Constraints</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Access Constraints</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getAccessConstraints()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_AccessConstraints();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getAllowedValues <em>Allowed Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Allowed Values</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getAllowedValues()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AllowedValues();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getAnyValue <em>Any Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Any Value</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getAnyValue()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AnyValue();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.DocumentRoot#getAvailableCRS <em>Available CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Available CRS</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getAvailableCRS()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_AvailableCRS();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getCoverage <em>Coverage</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Coverage</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getCoverage()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Coverage();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getReferenceGroup <em>Reference Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Reference Group</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getReferenceGroup()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ReferenceGroup();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getCoverages <em>Coverages</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Coverages</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getCoverages()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Coverages();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getManifest <em>Manifest</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Manifest</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getManifest()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Manifest();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getDataType <em>Data Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Data Type</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getDataType()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DataType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getDCP <em>DCP</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>DCP</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getDCP()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DCP();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getDefaultValue <em>Default Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Default Value</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getDefaultValue()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DefaultValue();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getExtendedCapabilities <em>Extended Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Extended Capabilities</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getExtendedCapabilities()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ExtendedCapabilities();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.DocumentRoot#getFees <em>Fees</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Fees</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getFees()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Fees();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Get Capabilities</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getGetCapabilities()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_GetCapabilities();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getHTTP <em>HTTP</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>HTTP</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getHTTP()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_HTTP();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.DocumentRoot#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Identifier</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getIdentifier()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Identifier();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getInterpolationMethods <em>Interpolation Methods</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Interpolation Methods</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getInterpolationMethods()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_InterpolationMethods();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.DocumentRoot#getLanguage <em>Language</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Language</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getLanguage()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Language();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getMaximumValue <em>Maximum Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Maximum Value</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getMaximumValue()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_MaximumValue();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getMeaning <em>Meaning</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Meaning</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getMeaning()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Meaning();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getMinimumValue <em>Minimum Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Minimum Value</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getMinimumValue()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_MinimumValue();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getNoValues <em>No Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>No Values</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getNoValues()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_NoValues();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getOperation <em>Operation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Operation</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getOperation()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Operation();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getOperationsMetadata <em>Operations Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Operations Metadata</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getOperationsMetadata()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_OperationsMetadata();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.DocumentRoot#getOutputFormat <em>Output Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Output Format</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getOutputFormat()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_OutputFormat();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getRange <em>Range</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Range</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getRange()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Range();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getReference <em>Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Reference</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getReference()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Reference();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getReferenceSystem <em>Reference System</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Reference System</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getReferenceSystem()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ReferenceSystem();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getServiceIdentification <em>Service Identification</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Identification</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getServiceIdentification()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ServiceIdentification();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getServiceReference <em>Service Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Service Reference</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getServiceReference()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ServiceReference();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getSpacing <em>Spacing</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Spacing</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getSpacing()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Spacing();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.DocumentRoot#getSupportedCRS <em>Supported CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Supported CRS</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getSupportedCRS()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_SupportedCRS();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getUOM <em>UOM</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>UOM</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getUOM()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_UOM();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Value</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getValue()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Value();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.DocumentRoot#getValuesReference <em>Values Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Values Reference</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getValuesReference()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ValuesReference();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.DocumentRoot#getRangeClosure <em>Range Closure</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Range Closure</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getRangeClosure()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_RangeClosure();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.DocumentRoot#getReference1 <em>Reference1</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Reference1</em>'.
     * @see net.opengis.wcs.ows.DocumentRoot#getReference1()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Reference1();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.DomainMetadataType <em>Domain Metadata Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Domain Metadata Type</em>'.
     * @see net.opengis.wcs.ows.DomainMetadataType
     * @generated
     */
    EClass getDomainMetadataType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.DomainMetadataType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see net.opengis.wcs.ows.DomainMetadataType#getValue()
     * @see #getDomainMetadataType()
     * @generated
     */
    EAttribute getDomainMetadataType_Value();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.DomainMetadataType#getReference <em>Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Reference</em>'.
     * @see net.opengis.wcs.ows.DomainMetadataType#getReference()
     * @see #getDomainMetadataType()
     * @generated
     */
    EAttribute getDomainMetadataType_Reference();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.DomainType <em>Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Domain Type</em>'.
     * @see net.opengis.wcs.ows.DomainType
     * @generated
     */
    EClass getDomainType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.DomainType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.wcs.ows.DomainType#getName()
     * @see #getDomainType()
     * @generated
     */
    EAttribute getDomainType_Name();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Get Capabilities Type</em>'.
     * @see net.opengis.wcs.ows.GetCapabilitiesType
     * @generated
     */
    EClass getGetCapabilitiesType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.GetCapabilitiesType#getAcceptVersions <em>Accept Versions</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Accept Versions</em>'.
     * @see net.opengis.wcs.ows.GetCapabilitiesType#getAcceptVersions()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EReference getGetCapabilitiesType_AcceptVersions();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.GetCapabilitiesType#getSections <em>Sections</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Sections</em>'.
     * @see net.opengis.wcs.ows.GetCapabilitiesType#getSections()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EReference getGetCapabilitiesType_Sections();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.GetCapabilitiesType#getAcceptFormats <em>Accept Formats</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Accept Formats</em>'.
     * @see net.opengis.wcs.ows.GetCapabilitiesType#getAcceptFormats()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EReference getGetCapabilitiesType_AcceptFormats();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.GetCapabilitiesType#getUpdateSequence <em>Update Sequence</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Update Sequence</em>'.
     * @see net.opengis.wcs.ows.GetCapabilitiesType#getUpdateSequence()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EAttribute getGetCapabilitiesType_UpdateSequence();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.HTTPType <em>HTTP Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>HTTP Type</em>'.
     * @see net.opengis.wcs.ows.HTTPType
     * @generated
     */
    EClass getHTTPType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.HTTPType#getGroup <em>Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see net.opengis.wcs.ows.HTTPType#getGroup()
     * @see #getHTTPType()
     * @generated
     */
    EAttribute getHTTPType_Group();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.ows.HTTPType#getGet <em>Get</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Get</em>'.
     * @see net.opengis.wcs.ows.HTTPType#getGet()
     * @see #getHTTPType()
     * @generated
     */
    EReference getHTTPType_Get();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.ows.HTTPType#getPost <em>Post</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Post</em>'.
     * @see net.opengis.wcs.ows.HTTPType#getPost()
     * @see #getHTTPType()
     * @generated
     */
    EReference getHTTPType_Post();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.IdentificationType <em>Identification Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Identification Type</em>'.
     * @see net.opengis.wcs.ows.IdentificationType
     * @generated
     */
    EClass getIdentificationType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.IdentificationType#getBoundingBox <em>Bounding Box</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Bounding Box</em>'.
     * @see net.opengis.wcs.ows.IdentificationType#getBoundingBox()
     * @see #getIdentificationType()
     * @generated
     */
    EAttribute getIdentificationType_BoundingBox();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.IdentificationType#getOutputFormat <em>Output Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Output Format</em>'.
     * @see net.opengis.wcs.ows.IdentificationType#getOutputFormat()
     * @see #getIdentificationType()
     * @generated
     */
    EAttribute getIdentificationType_OutputFormat();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.IdentificationType#getAvailableCRSGroup <em>Available CRS Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Available CRS Group</em>'.
     * @see net.opengis.wcs.ows.IdentificationType#getAvailableCRSGroup()
     * @see #getIdentificationType()
     * @generated
     */
    EAttribute getIdentificationType_AvailableCRSGroup();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.IdentificationType#getAvailableCRS <em>Available CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Available CRS</em>'.
     * @see net.opengis.wcs.ows.IdentificationType#getAvailableCRS()
     * @see #getIdentificationType()
     * @generated
     */
    EAttribute getIdentificationType_AvailableCRS();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.InterpolationMethodBaseType <em>Interpolation Method Base Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Interpolation Method Base Type</em>'.
     * @see net.opengis.wcs.ows.InterpolationMethodBaseType
     * @generated
     */
    EClass getInterpolationMethodBaseType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.InterpolationMethodBaseType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see net.opengis.wcs.ows.InterpolationMethodBaseType#getValue()
     * @see #getInterpolationMethodBaseType()
     * @generated
     */
    EAttribute getInterpolationMethodBaseType_Value();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.InterpolationMethodsType <em>Interpolation Methods Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Interpolation Methods Type</em>'.
     * @see net.opengis.wcs.ows.InterpolationMethodsType
     * @generated
     */
    EClass getInterpolationMethodsType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.InterpolationMethodsType#getDefaultMethod <em>Default Method</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Default Method</em>'.
     * @see net.opengis.wcs.ows.InterpolationMethodsType#getDefaultMethod()
     * @see #getInterpolationMethodsType()
     * @generated
     */
    EReference getInterpolationMethodsType_DefaultMethod();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.ows.InterpolationMethodsType#getOtherMethod <em>Other Method</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Other Method</em>'.
     * @see net.opengis.wcs.ows.InterpolationMethodsType#getOtherMethod()
     * @see #getInterpolationMethodsType()
     * @generated
     */
    EReference getInterpolationMethodsType_OtherMethod();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.InterpolationMethodType <em>Interpolation Method Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Interpolation Method Type</em>'.
     * @see net.opengis.wcs.ows.InterpolationMethodType
     * @generated
     */
    EClass getInterpolationMethodType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.InterpolationMethodType#getNullResistance <em>Null Resistance</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Null Resistance</em>'.
     * @see net.opengis.wcs.ows.InterpolationMethodType#getNullResistance()
     * @see #getInterpolationMethodType()
     * @generated
     */
    EAttribute getInterpolationMethodType_NullResistance();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.ManifestType <em>Manifest Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Manifest Type</em>'.
     * @see net.opengis.wcs.ows.ManifestType
     * @generated
     */
    EClass getManifestType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.ManifestType#getReferenceGroupGroup <em>Reference Group Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Reference Group Group</em>'.
     * @see net.opengis.wcs.ows.ManifestType#getReferenceGroupGroup()
     * @see #getManifestType()
     * @generated
     */
    EAttribute getManifestType_ReferenceGroupGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.ows.ManifestType#getReferenceGroup <em>Reference Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Reference Group</em>'.
     * @see net.opengis.wcs.ows.ManifestType#getReferenceGroup()
     * @see #getManifestType()
     * @generated
     */
    EReference getManifestType_ReferenceGroup();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.NoValuesType <em>No Values Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>No Values Type</em>'.
     * @see net.opengis.wcs.ows.NoValuesType
     * @generated
     */
    EClass getNoValuesType();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.OperationsMetadataType <em>Operations Metadata Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Operations Metadata Type</em>'.
     * @see net.opengis.wcs.ows.OperationsMetadataType
     * @generated
     */
    EClass getOperationsMetadataType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.ows.OperationsMetadataType#getOperation <em>Operation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Operation</em>'.
     * @see net.opengis.wcs.ows.OperationsMetadataType#getOperation()
     * @see #getOperationsMetadataType()
     * @generated
     */
    EReference getOperationsMetadataType_Operation();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.ows.OperationsMetadataType#getParameter <em>Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Parameter</em>'.
     * @see net.opengis.wcs.ows.OperationsMetadataType#getParameter()
     * @see #getOperationsMetadataType()
     * @generated
     */
    EReference getOperationsMetadataType_Parameter();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.ows.OperationsMetadataType#getConstraint <em>Constraint</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Constraint</em>'.
     * @see net.opengis.wcs.ows.OperationsMetadataType#getConstraint()
     * @see #getOperationsMetadataType()
     * @generated
     */
    EReference getOperationsMetadataType_Constraint();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.OperationsMetadataType#getExtendedCapabilities <em>Extended Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Extended Capabilities</em>'.
     * @see net.opengis.wcs.ows.OperationsMetadataType#getExtendedCapabilities()
     * @see #getOperationsMetadataType()
     * @generated
     */
    EReference getOperationsMetadataType_ExtendedCapabilities();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.OperationType <em>Operation Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Operation Type</em>'.
     * @see net.opengis.wcs.ows.OperationType
     * @generated
     */
    EClass getOperationType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.ows.OperationType#getDCP <em>DCP</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>DCP</em>'.
     * @see net.opengis.wcs.ows.OperationType#getDCP()
     * @see #getOperationType()
     * @generated
     */
    EReference getOperationType_DCP();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.ows.OperationType#getParameter <em>Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Parameter</em>'.
     * @see net.opengis.wcs.ows.OperationType#getParameter()
     * @see #getOperationType()
     * @generated
     */
    EReference getOperationType_Parameter();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.ows.OperationType#getConstraint <em>Constraint</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Constraint</em>'.
     * @see net.opengis.wcs.ows.OperationType#getConstraint()
     * @see #getOperationType()
     * @generated
     */
    EReference getOperationType_Constraint();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.OperationType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Metadata</em>'.
     * @see net.opengis.wcs.ows.OperationType#getMetadata()
     * @see #getOperationType()
     * @generated
     */
    EAttribute getOperationType_Metadata();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.OperationType#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see net.opengis.wcs.ows.OperationType#getName()
     * @see #getOperationType()
     * @generated
     */
    EAttribute getOperationType_Name();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.RangeType <em>Range Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Range Type</em>'.
     * @see net.opengis.wcs.ows.RangeType
     * @generated
     */
    EClass getRangeType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.RangeType#getMinimumValue <em>Minimum Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Minimum Value</em>'.
     * @see net.opengis.wcs.ows.RangeType#getMinimumValue()
     * @see #getRangeType()
     * @generated
     */
    EReference getRangeType_MinimumValue();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.RangeType#getMaximumValue <em>Maximum Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Maximum Value</em>'.
     * @see net.opengis.wcs.ows.RangeType#getMaximumValue()
     * @see #getRangeType()
     * @generated
     */
    EReference getRangeType_MaximumValue();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.RangeType#getSpacing <em>Spacing</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Spacing</em>'.
     * @see net.opengis.wcs.ows.RangeType#getSpacing()
     * @see #getRangeType()
     * @generated
     */
    EReference getRangeType_Spacing();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.RangeType#getRangeClosure <em>Range Closure</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Range Closure</em>'.
     * @see net.opengis.wcs.ows.RangeType#getRangeClosure()
     * @see #getRangeType()
     * @generated
     */
    EAttribute getRangeType_RangeClosure();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.ReferenceGroupType <em>Reference Group Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Reference Group Type</em>'.
     * @see net.opengis.wcs.ows.ReferenceGroupType
     * @generated
     */
    EClass getReferenceGroupType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.ReferenceGroupType#getAbstractReferenceBaseGroup <em>Abstract Reference Base Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Abstract Reference Base Group</em>'.
     * @see net.opengis.wcs.ows.ReferenceGroupType#getAbstractReferenceBaseGroup()
     * @see #getReferenceGroupType()
     * @generated
     */
    EAttribute getReferenceGroupType_AbstractReferenceBaseGroup();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.ows.ReferenceGroupType#getAbstractReferenceBase <em>Abstract Reference Base</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Abstract Reference Base</em>'.
     * @see net.opengis.wcs.ows.ReferenceGroupType#getAbstractReferenceBase()
     * @see #getReferenceGroupType()
     * @generated
     */
    EReference getReferenceGroupType_AbstractReferenceBase();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.ReferenceType <em>Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Reference Type</em>'.
     * @see net.opengis.wcs.ows.ReferenceType
     * @generated
     */
    EClass getReferenceType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.ReferenceType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Identifier</em>'.
     * @see net.opengis.wcs.ows.ReferenceType#getIdentifier()
     * @see #getReferenceType()
     * @generated
     */
    EAttribute getReferenceType_Identifier();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.ReferenceType#getAbstract <em>Abstract</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Abstract</em>'.
     * @see net.opengis.wcs.ows.ReferenceType#getAbstract()
     * @see #getReferenceType()
     * @generated
     */
    EAttribute getReferenceType_Abstract();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.ReferenceType#getFormat <em>Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Format</em>'.
     * @see net.opengis.wcs.ows.ReferenceType#getFormat()
     * @see #getReferenceType()
     * @generated
     */
    EAttribute getReferenceType_Format();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.ReferenceType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Metadata</em>'.
     * @see net.opengis.wcs.ows.ReferenceType#getMetadata()
     * @see #getReferenceType()
     * @generated
     */
    EAttribute getReferenceType_Metadata();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.RequestMethodType <em>Request Method Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Request Method Type</em>'.
     * @see net.opengis.wcs.ows.RequestMethodType
     * @generated
     */
    EClass getRequestMethodType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.ows.RequestMethodType#getConstraint <em>Constraint</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Constraint</em>'.
     * @see net.opengis.wcs.ows.RequestMethodType#getConstraint()
     * @see #getRequestMethodType()
     * @generated
     */
    EReference getRequestMethodType_Constraint();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.SectionsType <em>Sections Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Sections Type</em>'.
     * @see net.opengis.wcs.ows.SectionsType
     * @generated
     */
    EClass getSectionsType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.SectionsType#getSection <em>Section</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Section</em>'.
     * @see net.opengis.wcs.ows.SectionsType#getSection()
     * @see #getSectionsType()
     * @generated
     */
    EAttribute getSectionsType_Section();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.ServiceIdentificationType <em>Service Identification Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Service Identification Type</em>'.
     * @see net.opengis.wcs.ows.ServiceIdentificationType
     * @generated
     */
    EClass getServiceIdentificationType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.ServiceIdentificationType#getServiceType <em>Service Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service Type</em>'.
     * @see net.opengis.wcs.ows.ServiceIdentificationType#getServiceType()
     * @see #getServiceIdentificationType()
     * @generated
     */
    EAttribute getServiceIdentificationType_ServiceType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.ServiceIdentificationType#getServiceTypeVersion <em>Service Type Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Service Type Version</em>'.
     * @see net.opengis.wcs.ows.ServiceIdentificationType#getServiceTypeVersion()
     * @see #getServiceIdentificationType()
     * @generated
     */
    EAttribute getServiceIdentificationType_ServiceTypeVersion();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.ServiceIdentificationType#getProfile <em>Profile</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Profile</em>'.
     * @see net.opengis.wcs.ows.ServiceIdentificationType#getProfile()
     * @see #getServiceIdentificationType()
     * @generated
     */
    EAttribute getServiceIdentificationType_Profile();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.ServiceIdentificationType#getFees <em>Fees</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Fees</em>'.
     * @see net.opengis.wcs.ows.ServiceIdentificationType#getFees()
     * @see #getServiceIdentificationType()
     * @generated
     */
    EAttribute getServiceIdentificationType_Fees();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.ServiceIdentificationType#getAccessConstraints <em>Access Constraints</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Access Constraints</em>'.
     * @see net.opengis.wcs.ows.ServiceIdentificationType#getAccessConstraints()
     * @see #getServiceIdentificationType()
     * @generated
     */
    EAttribute getServiceIdentificationType_AccessConstraints();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.ServiceReferenceType <em>Service Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Service Reference Type</em>'.
     * @see net.opengis.wcs.ows.ServiceReferenceType
     * @generated
     */
    EClass getServiceReferenceType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.ServiceReferenceType#getRequestMessage <em>Request Message</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Request Message</em>'.
     * @see net.opengis.wcs.ows.ServiceReferenceType#getRequestMessage()
     * @see #getServiceReferenceType()
     * @generated
     */
    EReference getServiceReferenceType_RequestMessage();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.ServiceReferenceType#getRequestMessageReference <em>Request Message Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Request Message Reference</em>'.
     * @see net.opengis.wcs.ows.ServiceReferenceType#getRequestMessageReference()
     * @see #getServiceReferenceType()
     * @generated
     */
    EAttribute getServiceReferenceType_RequestMessageReference();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.UnNamedDomainType <em>Un Named Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Un Named Domain Type</em>'.
     * @see net.opengis.wcs.ows.UnNamedDomainType
     * @generated
     */
    EClass getUnNamedDomainType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.UnNamedDomainType#getAllowedValues <em>Allowed Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Allowed Values</em>'.
     * @see net.opengis.wcs.ows.UnNamedDomainType#getAllowedValues()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_AllowedValues();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.UnNamedDomainType#getAnyValue <em>Any Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Any Value</em>'.
     * @see net.opengis.wcs.ows.UnNamedDomainType#getAnyValue()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_AnyValue();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.UnNamedDomainType#getNoValues <em>No Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>No Values</em>'.
     * @see net.opengis.wcs.ows.UnNamedDomainType#getNoValues()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_NoValues();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.UnNamedDomainType#getValuesReference <em>Values Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Values Reference</em>'.
     * @see net.opengis.wcs.ows.UnNamedDomainType#getValuesReference()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_ValuesReference();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.UnNamedDomainType#getDefaultValue <em>Default Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Default Value</em>'.
     * @see net.opengis.wcs.ows.UnNamedDomainType#getDefaultValue()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_DefaultValue();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.UnNamedDomainType#getMeaning <em>Meaning</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Meaning</em>'.
     * @see net.opengis.wcs.ows.UnNamedDomainType#getMeaning()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_Meaning();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.UnNamedDomainType#getDataType <em>Data Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Data Type</em>'.
     * @see net.opengis.wcs.ows.UnNamedDomainType#getDataType()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_DataType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.UnNamedDomainType#getUOM <em>UOM</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>UOM</em>'.
     * @see net.opengis.wcs.ows.UnNamedDomainType#getUOM()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_UOM();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.ows.UnNamedDomainType#getReferenceSystem <em>Reference System</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Reference System</em>'.
     * @see net.opengis.wcs.ows.UnNamedDomainType#getReferenceSystem()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EReference getUnNamedDomainType_ReferenceSystem();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ows.UnNamedDomainType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Metadata</em>'.
     * @see net.opengis.wcs.ows.UnNamedDomainType#getMetadata()
     * @see #getUnNamedDomainType()
     * @generated
     */
    EAttribute getUnNamedDomainType_Metadata();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.ValuesReferenceType <em>Values Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Values Reference Type</em>'.
     * @see net.opengis.wcs.ows.ValuesReferenceType
     * @generated
     */
    EClass getValuesReferenceType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.ValuesReferenceType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see net.opengis.wcs.ows.ValuesReferenceType#getValue()
     * @see #getValuesReferenceType()
     * @generated
     */
    EAttribute getValuesReferenceType_Value();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.ValuesReferenceType#getReference <em>Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Reference</em>'.
     * @see net.opengis.wcs.ows.ValuesReferenceType#getReference()
     * @see #getValuesReferenceType()
     * @generated
     */
    EAttribute getValuesReferenceType_Reference();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ows.ValueType <em>Value Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Value Type</em>'.
     * @see net.opengis.wcs.ows.ValueType
     * @generated
     */
    EClass getValueType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ows.ValueType#getValue <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see net.opengis.wcs.ows.ValueType#getValue()
     * @see #getValueType()
     * @generated
     */
    EAttribute getValueType_Value();

    /**
     * Returns the meta object for enum '{@link net.opengis.wcs.ows.RangeClosureType <em>Range Closure Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Range Closure Type</em>'.
     * @see net.opengis.wcs.ows.RangeClosureType
     * @generated
     */
    EEnum getRangeClosureType();

    /**
     * Returns the meta object for data type '{@link java.lang.Object <em>Interpolation Method Base Type Base</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Interpolation Method Base Type Base</em>'.
     * @see java.lang.Object
     * @model instanceClass="java.lang.Object"
     *        extendedMetaData="name='InterpolationMethodBaseType_._base' baseType='http://www.eclipse.org/emf/2003/XMLType#anySimpleType'"
     * @generated
     */
    EDataType getInterpolationMethodBaseTypeBase();

    /**
     * Returns the meta object for data type '{@link net.opengis.wcs.ows.RangeClosureType <em>Range Closure Type Object</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Range Closure Type Object</em>'.
     * @see net.opengis.wcs.ows.RangeClosureType
     * @model instanceClass="net.opengis.wcs.ows.RangeClosureType"
     *        extendedMetaData="name='rangeClosure_._type:Object' baseType='rangeClosure_._type'"
     * @generated
     */
    EDataType getRangeClosureTypeObject();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Service Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Service Type</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='ServiceType' baseType='http://www.eclipse.org/emf/2003/XMLType#string'"
     * @generated
     */
    EDataType getServiceType();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Update Sequence Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Update Sequence Type</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='UpdateSequenceType' baseType='http://www.eclipse.org/emf/2003/XMLType#string'"
     * @generated
     */
    EDataType getUpdateSequenceType();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    owcsFactory getowcsFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.AbstractReferenceBaseTypeImpl <em>Abstract Reference Base Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.AbstractReferenceBaseTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getAbstractReferenceBaseType()
         * @generated
         */
        EClass ABSTRACT_REFERENCE_BASE_TYPE = eINSTANCE.getAbstractReferenceBaseType();

        /**
         * The meta object literal for the '<em><b>Actuate</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_REFERENCE_BASE_TYPE__ACTUATE = eINSTANCE.getAbstractReferenceBaseType_Actuate();

        /**
         * The meta object literal for the '<em><b>Arcrole</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_REFERENCE_BASE_TYPE__ARCROLE = eINSTANCE.getAbstractReferenceBaseType_Arcrole();

        /**
         * The meta object literal for the '<em><b>Href</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_REFERENCE_BASE_TYPE__HREF = eINSTANCE.getAbstractReferenceBaseType_Href();

        /**
         * The meta object literal for the '<em><b>Role</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_REFERENCE_BASE_TYPE__ROLE = eINSTANCE.getAbstractReferenceBaseType_Role();

        /**
         * The meta object literal for the '<em><b>Show</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_REFERENCE_BASE_TYPE__SHOW = eINSTANCE.getAbstractReferenceBaseType_Show();

        /**
         * The meta object literal for the '<em><b>Title</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_REFERENCE_BASE_TYPE__TITLE = eINSTANCE.getAbstractReferenceBaseType_Title();

        /**
         * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_REFERENCE_BASE_TYPE__TYPE = eINSTANCE.getAbstractReferenceBaseType_Type();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.AcceptFormatsTypeImpl <em>Accept Formats Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.AcceptFormatsTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getAcceptFormatsType()
         * @generated
         */
        EClass ACCEPT_FORMATS_TYPE = eINSTANCE.getAcceptFormatsType();

        /**
         * The meta object literal for the '<em><b>Output Format</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT = eINSTANCE.getAcceptFormatsType_OutputFormat();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.AcceptVersionsTypeImpl <em>Accept Versions Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.AcceptVersionsTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getAcceptVersionsType()
         * @generated
         */
        EClass ACCEPT_VERSIONS_TYPE = eINSTANCE.getAcceptVersionsType();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ACCEPT_VERSIONS_TYPE__VERSION = eINSTANCE.getAcceptVersionsType_Version();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.AllowedValuesTypeImpl <em>Allowed Values Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.AllowedValuesTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getAllowedValuesType()
         * @generated
         */
        EClass ALLOWED_VALUES_TYPE = eINSTANCE.getAllowedValuesType();

        /**
         * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ALLOWED_VALUES_TYPE__GROUP = eINSTANCE.getAllowedValuesType_Group();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ALLOWED_VALUES_TYPE__VALUE = eINSTANCE.getAllowedValuesType_Value();

        /**
         * The meta object literal for the '<em><b>Range</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ALLOWED_VALUES_TYPE__RANGE = eINSTANCE.getAllowedValuesType_Range();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.AnyValueTypeImpl <em>Any Value Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.AnyValueTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getAnyValueType()
         * @generated
         */
        EClass ANY_VALUE_TYPE = eINSTANCE.getAnyValueType();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.BasicIdentificationTypeImpl <em>Basic Identification Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.BasicIdentificationTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getBasicIdentificationType()
         * @generated
         */
        EClass BASIC_IDENTIFICATION_TYPE = eINSTANCE.getBasicIdentificationType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BASIC_IDENTIFICATION_TYPE__IDENTIFIER = eINSTANCE.getBasicIdentificationType_Identifier();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BASIC_IDENTIFICATION_TYPE__METADATA = eINSTANCE.getBasicIdentificationType_Metadata();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.CapabilitiesBaseTypeImpl <em>Capabilities Base Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.CapabilitiesBaseTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getCapabilitiesBaseType()
         * @generated
         */
        EClass CAPABILITIES_BASE_TYPE = eINSTANCE.getCapabilitiesBaseType();

        /**
         * The meta object literal for the '<em><b>Service Identification</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION = eINSTANCE.getCapabilitiesBaseType_ServiceIdentification();

        /**
         * The meta object literal for the '<em><b>Service Provider</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER = eINSTANCE.getCapabilitiesBaseType_ServiceProvider();

        /**
         * The meta object literal for the '<em><b>Operations Metadata</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA = eINSTANCE.getCapabilitiesBaseType_OperationsMetadata();

        /**
         * The meta object literal for the '<em><b>Update Sequence</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CAPABILITIES_BASE_TYPE__UPDATE_SEQUENCE = eINSTANCE.getCapabilitiesBaseType_UpdateSequence();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CAPABILITIES_BASE_TYPE__VERSION = eINSTANCE.getCapabilitiesBaseType_Version();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.CoveragesTypeImpl <em>Coverages Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.CoveragesTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getCoveragesType()
         * @generated
         */
        EClass COVERAGES_TYPE = eINSTANCE.getCoveragesType();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.DCPTypeImpl <em>DCP Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.DCPTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getDCPType()
         * @generated
         */
        EClass DCP_TYPE = eINSTANCE.getDCPType();

        /**
         * The meta object literal for the '<em><b>HTTP</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DCP_TYPE__HTTP = eINSTANCE.getDCPType_HTTP();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.DescriptionTypeImpl <em>Description Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.DescriptionTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getDescriptionType()
         * @generated
         */
        EClass DESCRIPTION_TYPE = eINSTANCE.getDescriptionType();

        /**
         * The meta object literal for the '<em><b>Title</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DESCRIPTION_TYPE__TITLE = eINSTANCE.getDescriptionType_Title();

        /**
         * The meta object literal for the '<em><b>Abstract</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DESCRIPTION_TYPE__ABSTRACT = eINSTANCE.getDescriptionType_Abstract();

        /**
         * The meta object literal for the '<em><b>Keywords</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DESCRIPTION_TYPE__KEYWORDS = eINSTANCE.getDescriptionType_Keywords();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.DocumentRootImpl <em>Document Root</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.DocumentRootImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getDocumentRoot()
         * @generated
         */
        EClass DOCUMENT_ROOT = eINSTANCE.getDocumentRoot();

        /**
         * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__MIXED = eINSTANCE.getDocumentRoot_Mixed();

        /**
         * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getDocumentRoot_XMLNSPrefixMap();

        /**
         * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getDocumentRoot_XSISchemaLocation();

        /**
         * The meta object literal for the '<em><b>Abstract Reference Base</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ABSTRACT_REFERENCE_BASE = eINSTANCE.getDocumentRoot_AbstractReferenceBase();

        /**
         * The meta object literal for the '<em><b>Access Constraints</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__ACCESS_CONSTRAINTS = eINSTANCE.getDocumentRoot_AccessConstraints();

        /**
         * The meta object literal for the '<em><b>Allowed Values</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ALLOWED_VALUES = eINSTANCE.getDocumentRoot_AllowedValues();

        /**
         * The meta object literal for the '<em><b>Any Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ANY_VALUE = eINSTANCE.getDocumentRoot_AnyValue();

        /**
         * The meta object literal for the '<em><b>Available CRS</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__AVAILABLE_CRS = eINSTANCE.getDocumentRoot_AvailableCRS();

        /**
         * The meta object literal for the '<em><b>Coverage</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__COVERAGE = eINSTANCE.getDocumentRoot_Coverage();

        /**
         * The meta object literal for the '<em><b>Reference Group</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__REFERENCE_GROUP = eINSTANCE.getDocumentRoot_ReferenceGroup();

        /**
         * The meta object literal for the '<em><b>Coverages</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__COVERAGES = eINSTANCE.getDocumentRoot_Coverages();

        /**
         * The meta object literal for the '<em><b>Manifest</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__MANIFEST = eINSTANCE.getDocumentRoot_Manifest();

        /**
         * The meta object literal for the '<em><b>Data Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__DATA_TYPE = eINSTANCE.getDocumentRoot_DataType();

        /**
         * The meta object literal for the '<em><b>DCP</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__DCP = eINSTANCE.getDocumentRoot_DCP();

        /**
         * The meta object literal for the '<em><b>Default Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__DEFAULT_VALUE = eINSTANCE.getDocumentRoot_DefaultValue();

        /**
         * The meta object literal for the '<em><b>Extended Capabilities</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__EXTENDED_CAPABILITIES = eINSTANCE.getDocumentRoot_ExtendedCapabilities();

        /**
         * The meta object literal for the '<em><b>Fees</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__FEES = eINSTANCE.getDocumentRoot_Fees();

        /**
         * The meta object literal for the '<em><b>Get Capabilities</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__GET_CAPABILITIES = eINSTANCE.getDocumentRoot_GetCapabilities();

        /**
         * The meta object literal for the '<em><b>HTTP</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__HTTP = eINSTANCE.getDocumentRoot_HTTP();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__IDENTIFIER = eINSTANCE.getDocumentRoot_Identifier();

        /**
         * The meta object literal for the '<em><b>Interpolation Methods</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__INTERPOLATION_METHODS = eINSTANCE.getDocumentRoot_InterpolationMethods();

        /**
         * The meta object literal for the '<em><b>Language</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__LANGUAGE = eINSTANCE.getDocumentRoot_Language();

        /**
         * The meta object literal for the '<em><b>Maximum Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__MAXIMUM_VALUE = eINSTANCE.getDocumentRoot_MaximumValue();

        /**
         * The meta object literal for the '<em><b>Meaning</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__MEANING = eINSTANCE.getDocumentRoot_Meaning();

        /**
         * The meta object literal for the '<em><b>Minimum Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__MINIMUM_VALUE = eINSTANCE.getDocumentRoot_MinimumValue();

        /**
         * The meta object literal for the '<em><b>No Values</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__NO_VALUES = eINSTANCE.getDocumentRoot_NoValues();

        /**
         * The meta object literal for the '<em><b>Operation</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__OPERATION = eINSTANCE.getDocumentRoot_Operation();

        /**
         * The meta object literal for the '<em><b>Operations Metadata</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__OPERATIONS_METADATA = eINSTANCE.getDocumentRoot_OperationsMetadata();

        /**
         * The meta object literal for the '<em><b>Output Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__OUTPUT_FORMAT = eINSTANCE.getDocumentRoot_OutputFormat();

        /**
         * The meta object literal for the '<em><b>Range</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__RANGE = eINSTANCE.getDocumentRoot_Range();

        /**
         * The meta object literal for the '<em><b>Reference</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__REFERENCE = eINSTANCE.getDocumentRoot_Reference();

        /**
         * The meta object literal for the '<em><b>Reference System</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__REFERENCE_SYSTEM = eINSTANCE.getDocumentRoot_ReferenceSystem();

        /**
         * The meta object literal for the '<em><b>Service Identification</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__SERVICE_IDENTIFICATION = eINSTANCE.getDocumentRoot_ServiceIdentification();

        /**
         * The meta object literal for the '<em><b>Service Reference</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__SERVICE_REFERENCE = eINSTANCE.getDocumentRoot_ServiceReference();

        /**
         * The meta object literal for the '<em><b>Spacing</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__SPACING = eINSTANCE.getDocumentRoot_Spacing();

        /**
         * The meta object literal for the '<em><b>Supported CRS</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__SUPPORTED_CRS = eINSTANCE.getDocumentRoot_SupportedCRS();

        /**
         * The meta object literal for the '<em><b>UOM</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__UOM = eINSTANCE.getDocumentRoot_UOM();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__VALUE = eINSTANCE.getDocumentRoot_Value();

        /**
         * The meta object literal for the '<em><b>Values Reference</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__VALUES_REFERENCE = eINSTANCE.getDocumentRoot_ValuesReference();

        /**
         * The meta object literal for the '<em><b>Range Closure</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__RANGE_CLOSURE = eINSTANCE.getDocumentRoot_RangeClosure();

        /**
         * The meta object literal for the '<em><b>Reference1</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__REFERENCE1 = eINSTANCE.getDocumentRoot_Reference1();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.DomainMetadataTypeImpl <em>Domain Metadata Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.DomainMetadataTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getDomainMetadataType()
         * @generated
         */
        EClass DOMAIN_METADATA_TYPE = eINSTANCE.getDomainMetadataType();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOMAIN_METADATA_TYPE__VALUE = eINSTANCE.getDomainMetadataType_Value();

        /**
         * The meta object literal for the '<em><b>Reference</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOMAIN_METADATA_TYPE__REFERENCE = eINSTANCE.getDomainMetadataType_Reference();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.DomainTypeImpl <em>Domain Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.DomainTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getDomainType()
         * @generated
         */
        EClass DOMAIN_TYPE = eINSTANCE.getDomainType();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOMAIN_TYPE__NAME = eINSTANCE.getDomainType_Name();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.GetCapabilitiesTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getGetCapabilitiesType()
         * @generated
         */
        EClass GET_CAPABILITIES_TYPE = eINSTANCE.getGetCapabilitiesType();

        /**
         * The meta object literal for the '<em><b>Accept Versions</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS = eINSTANCE.getGetCapabilitiesType_AcceptVersions();

        /**
         * The meta object literal for the '<em><b>Sections</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GET_CAPABILITIES_TYPE__SECTIONS = eINSTANCE.getGetCapabilitiesType_Sections();

        /**
         * The meta object literal for the '<em><b>Accept Formats</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GET_CAPABILITIES_TYPE__ACCEPT_FORMATS = eINSTANCE.getGetCapabilitiesType_AcceptFormats();

        /**
         * The meta object literal for the '<em><b>Update Sequence</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE = eINSTANCE.getGetCapabilitiesType_UpdateSequence();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.HTTPTypeImpl <em>HTTP Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.HTTPTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getHTTPType()
         * @generated
         */
        EClass HTTP_TYPE = eINSTANCE.getHTTPType();

        /**
         * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute HTTP_TYPE__GROUP = eINSTANCE.getHTTPType_Group();

        /**
         * The meta object literal for the '<em><b>Get</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference HTTP_TYPE__GET = eINSTANCE.getHTTPType_Get();

        /**
         * The meta object literal for the '<em><b>Post</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference HTTP_TYPE__POST = eINSTANCE.getHTTPType_Post();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.IdentificationTypeImpl <em>Identification Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.IdentificationTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getIdentificationType()
         * @generated
         */
        EClass IDENTIFICATION_TYPE = eINSTANCE.getIdentificationType();

        /**
         * The meta object literal for the '<em><b>Bounding Box</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute IDENTIFICATION_TYPE__BOUNDING_BOX = eINSTANCE.getIdentificationType_BoundingBox();

        /**
         * The meta object literal for the '<em><b>Output Format</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute IDENTIFICATION_TYPE__OUTPUT_FORMAT = eINSTANCE.getIdentificationType_OutputFormat();

        /**
         * The meta object literal for the '<em><b>Available CRS Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP = eINSTANCE.getIdentificationType_AvailableCRSGroup();

        /**
         * The meta object literal for the '<em><b>Available CRS</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute IDENTIFICATION_TYPE__AVAILABLE_CRS = eINSTANCE.getIdentificationType_AvailableCRS();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.InterpolationMethodBaseTypeImpl <em>Interpolation Method Base Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.InterpolationMethodBaseTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getInterpolationMethodBaseType()
         * @generated
         */
        EClass INTERPOLATION_METHOD_BASE_TYPE = eINSTANCE.getInterpolationMethodBaseType();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INTERPOLATION_METHOD_BASE_TYPE__VALUE = eINSTANCE.getInterpolationMethodBaseType_Value();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.InterpolationMethodsTypeImpl <em>Interpolation Methods Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.InterpolationMethodsTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getInterpolationMethodsType()
         * @generated
         */
        EClass INTERPOLATION_METHODS_TYPE = eINSTANCE.getInterpolationMethodsType();

        /**
         * The meta object literal for the '<em><b>Default Method</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INTERPOLATION_METHODS_TYPE__DEFAULT_METHOD = eINSTANCE.getInterpolationMethodsType_DefaultMethod();

        /**
         * The meta object literal for the '<em><b>Other Method</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INTERPOLATION_METHODS_TYPE__OTHER_METHOD = eINSTANCE.getInterpolationMethodsType_OtherMethod();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.InterpolationMethodTypeImpl <em>Interpolation Method Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.InterpolationMethodTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getInterpolationMethodType()
         * @generated
         */
        EClass INTERPOLATION_METHOD_TYPE = eINSTANCE.getInterpolationMethodType();

        /**
         * The meta object literal for the '<em><b>Null Resistance</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INTERPOLATION_METHOD_TYPE__NULL_RESISTANCE = eINSTANCE.getInterpolationMethodType_NullResistance();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.ManifestTypeImpl <em>Manifest Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.ManifestTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getManifestType()
         * @generated
         */
        EClass MANIFEST_TYPE = eINSTANCE.getManifestType();

        /**
         * The meta object literal for the '<em><b>Reference Group Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MANIFEST_TYPE__REFERENCE_GROUP_GROUP = eINSTANCE.getManifestType_ReferenceGroupGroup();

        /**
         * The meta object literal for the '<em><b>Reference Group</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MANIFEST_TYPE__REFERENCE_GROUP = eINSTANCE.getManifestType_ReferenceGroup();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.NoValuesTypeImpl <em>No Values Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.NoValuesTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getNoValuesType()
         * @generated
         */
        EClass NO_VALUES_TYPE = eINSTANCE.getNoValuesType();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.OperationsMetadataTypeImpl <em>Operations Metadata Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.OperationsMetadataTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getOperationsMetadataType()
         * @generated
         */
        EClass OPERATIONS_METADATA_TYPE = eINSTANCE.getOperationsMetadataType();

        /**
         * The meta object literal for the '<em><b>Operation</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATIONS_METADATA_TYPE__OPERATION = eINSTANCE.getOperationsMetadataType_Operation();

        /**
         * The meta object literal for the '<em><b>Parameter</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATIONS_METADATA_TYPE__PARAMETER = eINSTANCE.getOperationsMetadataType_Parameter();

        /**
         * The meta object literal for the '<em><b>Constraint</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATIONS_METADATA_TYPE__CONSTRAINT = eINSTANCE.getOperationsMetadataType_Constraint();

        /**
         * The meta object literal for the '<em><b>Extended Capabilities</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES = eINSTANCE.getOperationsMetadataType_ExtendedCapabilities();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.OperationTypeImpl <em>Operation Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.OperationTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getOperationType()
         * @generated
         */
        EClass OPERATION_TYPE = eINSTANCE.getOperationType();

        /**
         * The meta object literal for the '<em><b>DCP</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATION_TYPE__DCP = eINSTANCE.getOperationType_DCP();

        /**
         * The meta object literal for the '<em><b>Parameter</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATION_TYPE__PARAMETER = eINSTANCE.getOperationType_Parameter();

        /**
         * The meta object literal for the '<em><b>Constraint</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATION_TYPE__CONSTRAINT = eINSTANCE.getOperationType_Constraint();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute OPERATION_TYPE__METADATA = eINSTANCE.getOperationType_Metadata();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute OPERATION_TYPE__NAME = eINSTANCE.getOperationType_Name();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.RangeTypeImpl <em>Range Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.RangeTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getRangeType()
         * @generated
         */
        EClass RANGE_TYPE = eINSTANCE.getRangeType();

        /**
         * The meta object literal for the '<em><b>Minimum Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RANGE_TYPE__MINIMUM_VALUE = eINSTANCE.getRangeType_MinimumValue();

        /**
         * The meta object literal for the '<em><b>Maximum Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RANGE_TYPE__MAXIMUM_VALUE = eINSTANCE.getRangeType_MaximumValue();

        /**
         * The meta object literal for the '<em><b>Spacing</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RANGE_TYPE__SPACING = eINSTANCE.getRangeType_Spacing();

        /**
         * The meta object literal for the '<em><b>Range Closure</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RANGE_TYPE__RANGE_CLOSURE = eINSTANCE.getRangeType_RangeClosure();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.ReferenceGroupTypeImpl <em>Reference Group Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.ReferenceGroupTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getReferenceGroupType()
         * @generated
         */
        EClass REFERENCE_GROUP_TYPE = eINSTANCE.getReferenceGroupType();

        /**
         * The meta object literal for the '<em><b>Abstract Reference Base Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE_GROUP = eINSTANCE.getReferenceGroupType_AbstractReferenceBaseGroup();

        /**
         * The meta object literal for the '<em><b>Abstract Reference Base</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE = eINSTANCE.getReferenceGroupType_AbstractReferenceBase();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.ReferenceTypeImpl <em>Reference Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.ReferenceTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getReferenceType()
         * @generated
         */
        EClass REFERENCE_TYPE = eINSTANCE.getReferenceType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REFERENCE_TYPE__IDENTIFIER = eINSTANCE.getReferenceType_Identifier();

        /**
         * The meta object literal for the '<em><b>Abstract</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REFERENCE_TYPE__ABSTRACT = eINSTANCE.getReferenceType_Abstract();

        /**
         * The meta object literal for the '<em><b>Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REFERENCE_TYPE__FORMAT = eINSTANCE.getReferenceType_Format();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REFERENCE_TYPE__METADATA = eINSTANCE.getReferenceType_Metadata();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.RequestMethodTypeImpl <em>Request Method Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.RequestMethodTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getRequestMethodType()
         * @generated
         */
        EClass REQUEST_METHOD_TYPE = eINSTANCE.getRequestMethodType();

        /**
         * The meta object literal for the '<em><b>Constraint</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference REQUEST_METHOD_TYPE__CONSTRAINT = eINSTANCE.getRequestMethodType_Constraint();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.SectionsTypeImpl <em>Sections Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.SectionsTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getSectionsType()
         * @generated
         */
        EClass SECTIONS_TYPE = eINSTANCE.getSectionsType();

        /**
         * The meta object literal for the '<em><b>Section</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SECTIONS_TYPE__SECTION = eINSTANCE.getSectionsType_Section();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.ServiceIdentificationTypeImpl <em>Service Identification Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.ServiceIdentificationTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getServiceIdentificationType()
         * @generated
         */
        EClass SERVICE_IDENTIFICATION_TYPE = eINSTANCE.getServiceIdentificationType();

        /**
         * The meta object literal for the '<em><b>Service Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE = eINSTANCE.getServiceIdentificationType_ServiceType();

        /**
         * The meta object literal for the '<em><b>Service Type Version</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION = eINSTANCE.getServiceIdentificationType_ServiceTypeVersion();

        /**
         * The meta object literal for the '<em><b>Profile</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SERVICE_IDENTIFICATION_TYPE__PROFILE = eINSTANCE.getServiceIdentificationType_Profile();

        /**
         * The meta object literal for the '<em><b>Fees</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SERVICE_IDENTIFICATION_TYPE__FEES = eINSTANCE.getServiceIdentificationType_Fees();

        /**
         * The meta object literal for the '<em><b>Access Constraints</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS = eINSTANCE.getServiceIdentificationType_AccessConstraints();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.ServiceReferenceTypeImpl <em>Service Reference Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.ServiceReferenceTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getServiceReferenceType()
         * @generated
         */
        EClass SERVICE_REFERENCE_TYPE = eINSTANCE.getServiceReferenceType();

        /**
         * The meta object literal for the '<em><b>Request Message</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE = eINSTANCE.getServiceReferenceType_RequestMessage();

        /**
         * The meta object literal for the '<em><b>Request Message Reference</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE_REFERENCE = eINSTANCE.getServiceReferenceType_RequestMessageReference();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.UnNamedDomainTypeImpl <em>Un Named Domain Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.UnNamedDomainTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getUnNamedDomainType()
         * @generated
         */
        EClass UN_NAMED_DOMAIN_TYPE = eINSTANCE.getUnNamedDomainType();

        /**
         * The meta object literal for the '<em><b>Allowed Values</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__ALLOWED_VALUES = eINSTANCE.getUnNamedDomainType_AllowedValues();

        /**
         * The meta object literal for the '<em><b>Any Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__ANY_VALUE = eINSTANCE.getUnNamedDomainType_AnyValue();

        /**
         * The meta object literal for the '<em><b>No Values</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__NO_VALUES = eINSTANCE.getUnNamedDomainType_NoValues();

        /**
         * The meta object literal for the '<em><b>Values Reference</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__VALUES_REFERENCE = eINSTANCE.getUnNamedDomainType_ValuesReference();

        /**
         * The meta object literal for the '<em><b>Default Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__DEFAULT_VALUE = eINSTANCE.getUnNamedDomainType_DefaultValue();

        /**
         * The meta object literal for the '<em><b>Meaning</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__MEANING = eINSTANCE.getUnNamedDomainType_Meaning();

        /**
         * The meta object literal for the '<em><b>Data Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__DATA_TYPE = eINSTANCE.getUnNamedDomainType_DataType();

        /**
         * The meta object literal for the '<em><b>UOM</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__UOM = eINSTANCE.getUnNamedDomainType_UOM();

        /**
         * The meta object literal for the '<em><b>Reference System</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference UN_NAMED_DOMAIN_TYPE__REFERENCE_SYSTEM = eINSTANCE.getUnNamedDomainType_ReferenceSystem();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute UN_NAMED_DOMAIN_TYPE__METADATA = eINSTANCE.getUnNamedDomainType_Metadata();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.ValuesReferenceTypeImpl <em>Values Reference Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.ValuesReferenceTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getValuesReferenceType()
         * @generated
         */
        EClass VALUES_REFERENCE_TYPE = eINSTANCE.getValuesReferenceType();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute VALUES_REFERENCE_TYPE__VALUE = eINSTANCE.getValuesReferenceType_Value();

        /**
         * The meta object literal for the '<em><b>Reference</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute VALUES_REFERENCE_TYPE__REFERENCE = eINSTANCE.getValuesReferenceType_Reference();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.impl.ValueTypeImpl <em>Value Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.impl.ValueTypeImpl
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getValueType()
         * @generated
         */
        EClass VALUE_TYPE = eINSTANCE.getValueType();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute VALUE_TYPE__VALUE = eINSTANCE.getValueType_Value();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.ows.RangeClosureType <em>Range Closure Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.RangeClosureType
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getRangeClosureType()
         * @generated
         */
        EEnum RANGE_CLOSURE_TYPE = eINSTANCE.getRangeClosureType();

        /**
         * The meta object literal for the '<em>Interpolation Method Base Type Base</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.Object
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getInterpolationMethodBaseTypeBase()
         * @generated
         */
        EDataType INTERPOLATION_METHOD_BASE_TYPE_BASE = eINSTANCE.getInterpolationMethodBaseTypeBase();

        /**
         * The meta object literal for the '<em>Range Closure Type Object</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.ows.RangeClosureType
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getRangeClosureTypeObject()
         * @generated
         */
        EDataType RANGE_CLOSURE_TYPE_OBJECT = eINSTANCE.getRangeClosureTypeObject();

        /**
         * The meta object literal for the '<em>Service Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getServiceType()
         * @generated
         */
        EDataType SERVICE_TYPE = eINSTANCE.getServiceType();

        /**
         * The meta object literal for the '<em>Update Sequence Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.wcs.ows.impl.owcsPackageImpl#getUpdateSequenceType()
         * @generated
         */
        EDataType UPDATE_SEQUENCE_TYPE = eINSTANCE.getUpdateSequenceType();

    }

} //owcsPackage
