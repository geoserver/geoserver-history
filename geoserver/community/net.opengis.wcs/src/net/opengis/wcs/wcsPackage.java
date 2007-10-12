/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs;

import net.opengis.wcs.ows.owcsPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
 * This XML Schema Document defines the DescribeCoverage operation request and response XML elements and types, used by the OGC Web Coverage Service (WCS). 
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc, All Rights Reserved. 
 * This XML Schema Document encodes the elements and types that are shared by multiple WCS operations. 
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc, All Rights Reserved. 
 * This XML Schema Document defines a GridCRS element that is much simpler but otherwise similar to a specialization of gml:DerivedCRS. This GridCRS roughly corresponds to the CV_RectifiedGrid class in ISO 19123, without inheritance from CV_Grid. This GridCRS is designed for use by the OGC Web Coverage Service (WCS) and elsewhere. 
 * 		This XML Schema Document is not a GML Application Schema, although it uses the GML 3.1.1 CRS support profile that is specified in document OGC 05-094r1. 
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved. 
 * This XML Schema Document includes, directly and indirectly, all the OWS Common XML Schema Documents defined by the OGC Web Coverage Service (WCS). 
 * 			Copyright (c) 2006 Open Geospatial Consortium, Inc, All Rights Reserved. 
 * This XML Schema Document defines the GetCapabilities operation request and response XML elements and types, which are common to all OWSs. This XML Schema shall be edited by each OWS, for example, to specify a specific value for the "service" attribute.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved. 
 * This XML Schema Document encodes the common "ServiceIdentification" section of the GetCapabilities operation response, known as the Capabilities XML document. This section encodes the SV_ServiceIdentification class of ISO 19119 (OGC Abstract Specification Topic 12).
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved. 
 * This XML Schema Document encodes the parts of the MD_DataIdentification class of ISO 19115 (OGC Abstract Specification Topic 11) which are expected to be used for most datasets. This Schema also encodes the parts of this class that are expected to be useful for other metadata. Both are expected to be used within the Contents section of OWS service metadata (Capabilities) documents.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved. 
 * This XML Schema Document encodes the basic contents of the "OperationsMetadata" section of the GetCapabilities operation response, also known as the Capabilities XML document.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved. 
 * This XML Schema Document encodes the allowed values (or domain) of a quantity, usually for an input or output parameter to an OWS. Such a parameter is sometimes called a variable, quantity, literal, or typed literal. Such a quantity can use one of many data types, including double, integer, boolean, string, or URI. The allowed values can also be encoded for a quantity that is not explicit or not transferred, but is constrained by a server implementation.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved. 
 * This XML Schema Document specifies types and elements for groups of coverages, allowing each coverage to include or reference multiple files. 
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved. 
 * This XML Schema Document specifies types and elements for document or resource references and for package manifests that contain multiple references. The contents of each type and element specified here can be restricted and/or extended for each use in a specific OWS specification.
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc. All Rights Reserved. 
 * This XML Schema Document defines interpolation method elements and types, used by the OGC Web Coverage Service (WCS) version 1.1 and intended to be included in OWS Common version 1.1. 
 * 		Copyright (c) 2006 Open Geospatial Consortium, Inc, All Rights Reserved. 
 * <!-- end-model-doc -->
 * @see net.opengis.wcs.wcsFactory
 * @model kind="package"
 *        annotation="http://www.w3.org/XML/1998/namespace lang='en'"
 * @generated
 */
public interface wcsPackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "wcs";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.opengis.net/wcs/1.1";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "net.opengis.wcs";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    wcsPackage eINSTANCE = net.opengis.wcs.impl.wcsPackageImpl.init();

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.AvailableKeysTypeImpl <em>Available Keys Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.AvailableKeysTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getAvailableKeysType()
     * @generated
     */
    int AVAILABLE_KEYS_TYPE = 0;

    /**
     * The feature id for the '<em><b>Key</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVAILABLE_KEYS_TYPE__KEY = 0;

    /**
     * The number of structural features of the '<em>Available Keys Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AVAILABLE_KEYS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.AxisSubsetTypeImpl <em>Axis Subset Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.AxisSubsetTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getAxisSubsetType()
     * @generated
     */
    int AXIS_SUBSET_TYPE = 1;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS_SUBSET_TYPE__IDENTIFIER = 0;

    /**
     * The feature id for the '<em><b>Key</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS_SUBSET_TYPE__KEY = 1;

    /**
     * The number of structural features of the '<em>Axis Subset Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS_SUBSET_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.AxisTypeImpl <em>Axis Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.AxisTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getAxisType()
     * @generated
     */
    int AXIS_TYPE = 2;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS_TYPE__TITLE = owcsPackage.DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS_TYPE__ABSTRACT = owcsPackage.DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS_TYPE__KEYWORDS = owcsPackage.DESCRIPTION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Available Keys</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS_TYPE__AVAILABLE_KEYS = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Meaning</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS_TYPE__MEANING = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Data Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS_TYPE__DATA_TYPE = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>UOM</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS_TYPE__UOM = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Reference System</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS_TYPE__REFERENCE_SYSTEM = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS_TYPE__METADATA = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS_TYPE__IDENTIFIER = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 6;

    /**
     * The number of structural features of the '<em>Axis Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AXIS_TYPE_FEATURE_COUNT = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 7;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.CapabilitiesTypeImpl <em>Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.CapabilitiesTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getCapabilitiesType()
     * @generated
     */
    int CAPABILITIES_TYPE = 3;

    /**
     * The feature id for the '<em><b>Service Identification</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__SERVICE_IDENTIFICATION = owcsPackage.CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION;

    /**
     * The feature id for the '<em><b>Service Provider</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__SERVICE_PROVIDER = owcsPackage.CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER;

    /**
     * The feature id for the '<em><b>Operations Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__OPERATIONS_METADATA = owcsPackage.CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA;

    /**
     * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__UPDATE_SEQUENCE = owcsPackage.CAPABILITIES_BASE_TYPE__UPDATE_SEQUENCE;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__VERSION = owcsPackage.CAPABILITIES_BASE_TYPE__VERSION;

    /**
     * The feature id for the '<em><b>Contents</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE__CONTENTS = owcsPackage.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CAPABILITIES_TYPE_FEATURE_COUNT = owcsPackage.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.ContentsTypeImpl <em>Contents Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.ContentsTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getContentsType()
     * @generated
     */
    int CONTENTS_TYPE = 4;

    /**
     * The feature id for the '<em><b>Coverage Summary</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTENTS_TYPE__COVERAGE_SUMMARY = 0;

    /**
     * The feature id for the '<em><b>Supported CRS</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTENTS_TYPE__SUPPORTED_CRS = 1;

    /**
     * The feature id for the '<em><b>Supported Format</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTENTS_TYPE__SUPPORTED_FORMAT = 2;

    /**
     * The feature id for the '<em><b>Other Source</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTENTS_TYPE__OTHER_SOURCE = 3;

    /**
     * The number of structural features of the '<em>Contents Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTENTS_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.CoverageDescriptionsTypeImpl <em>Coverage Descriptions Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.CoverageDescriptionsTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getCoverageDescriptionsType()
     * @generated
     */
    int COVERAGE_DESCRIPTIONS_TYPE = 5;

    /**
     * The feature id for the '<em><b>Coverage Description</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTIONS_TYPE__COVERAGE_DESCRIPTION = 0;

    /**
     * The number of structural features of the '<em>Coverage Descriptions Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTIONS_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.CoverageDescriptionTypeImpl <em>Coverage Description Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.CoverageDescriptionTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getCoverageDescriptionType()
     * @generated
     */
    int COVERAGE_DESCRIPTION_TYPE = 6;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTION_TYPE__TITLE = owcsPackage.DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTION_TYPE__ABSTRACT = owcsPackage.DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTION_TYPE__KEYWORDS = owcsPackage.DESCRIPTION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTION_TYPE__IDENTIFIER = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTION_TYPE__METADATA = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTION_TYPE__DOMAIN = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Range</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTION_TYPE__RANGE = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Supported CRS</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTION_TYPE__SUPPORTED_CRS = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Supported Format</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTION_TYPE__SUPPORTED_FORMAT = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 5;

    /**
     * The number of structural features of the '<em>Coverage Description Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DESCRIPTION_TYPE_FEATURE_COUNT = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 6;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.CoverageDomainTypeImpl <em>Coverage Domain Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.CoverageDomainTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getCoverageDomainType()
     * @generated
     */
    int COVERAGE_DOMAIN_TYPE = 7;

    /**
     * The feature id for the '<em><b>Spatial Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DOMAIN_TYPE__SPATIAL_DOMAIN = 0;

    /**
     * The feature id for the '<em><b>Temporal Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DOMAIN_TYPE__TEMPORAL_DOMAIN = 1;

    /**
     * The number of structural features of the '<em>Coverage Domain Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_DOMAIN_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.CoverageSummaryTypeImpl <em>Coverage Summary Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.CoverageSummaryTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getCoverageSummaryType()
     * @generated
     */
    int COVERAGE_SUMMARY_TYPE = 8;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__TITLE = owcsPackage.DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__ABSTRACT = owcsPackage.DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__KEYWORDS = owcsPackage.DESCRIPTION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Metadata</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__METADATA = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>WGS84 Bounding Box</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Supported CRS</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__SUPPORTED_CRS = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Supported Format</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__SUPPORTED_FORMAT = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Coverage Summary</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__COVERAGE_SUMMARY = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__IDENTIFIER = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Identifier1</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE__IDENTIFIER1 = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 6;

    /**
     * The number of structural features of the '<em>Coverage Summary Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COVERAGE_SUMMARY_TYPE_FEATURE_COUNT = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 7;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.RequestBaseTypeImpl <em>Request Base Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.RequestBaseTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getRequestBaseType()
     * @generated
     */
    int REQUEST_BASE_TYPE = 21;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_BASE_TYPE__SERVICE = 0;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_BASE_TYPE__VERSION = 1;

    /**
     * The number of structural features of the '<em>Request Base Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REQUEST_BASE_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.DescribeCoverageTypeImpl <em>Describe Coverage Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.DescribeCoverageTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getDescribeCoverageType()
     * @generated
     */
    int DESCRIBE_COVERAGE_TYPE = 9;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_COVERAGE_TYPE__SERVICE = REQUEST_BASE_TYPE__SERVICE;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_COVERAGE_TYPE__VERSION = REQUEST_BASE_TYPE__VERSION;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_COVERAGE_TYPE__IDENTIFIER = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Describe Coverage Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DESCRIBE_COVERAGE_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.DocumentRootImpl <em>Document Root</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.DocumentRootImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getDocumentRoot()
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
     * The feature id for the '<em><b>Available Keys</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__AVAILABLE_KEYS = 3;

    /**
     * The feature id for the '<em><b>Axis Subset</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__AXIS_SUBSET = 4;

    /**
     * The feature id for the '<em><b>Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CAPABILITIES = 5;

    /**
     * The feature id for the '<em><b>Contents</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CONTENTS = 6;

    /**
     * The feature id for the '<em><b>Coverage Descriptions</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS = 7;

    /**
     * The feature id for the '<em><b>Coverage Summary</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__COVERAGE_SUMMARY = 8;

    /**
     * The feature id for the '<em><b>Describe Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DESCRIBE_COVERAGE = 9;

    /**
     * The feature id for the '<em><b>Get Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__GET_CAPABILITIES = 10;

    /**
     * The feature id for the '<em><b>Get Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__GET_COVERAGE = 11;

    /**
     * The feature id for the '<em><b>Grid Base CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__GRID_BASE_CRS = 12;

    /**
     * The feature id for the '<em><b>Grid CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__GRID_CRS = 13;

    /**
     * The feature id for the '<em><b>Grid CS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__GRID_CS = 14;

    /**
     * The feature id for the '<em><b>Grid Offsets</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__GRID_OFFSETS = 15;

    /**
     * The feature id for the '<em><b>Grid Origin</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__GRID_ORIGIN = 16;

    /**
     * The feature id for the '<em><b>Grid Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__GRID_TYPE = 17;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__IDENTIFIER = 18;

    /**
     * The feature id for the '<em><b>Temporal Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TEMPORAL_DOMAIN = 19;

    /**
     * The feature id for the '<em><b>Temporal Subset</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TEMPORAL_SUBSET = 20;

    /**
     * The number of structural features of the '<em>Document Root</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT_FEATURE_COUNT = 21;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.DomainSubsetTypeImpl <em>Domain Subset Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.DomainSubsetTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getDomainSubsetType()
     * @generated
     */
    int DOMAIN_SUBSET_TYPE = 11;

    /**
     * The feature id for the '<em><b>Bounding Box</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_SUBSET_TYPE__BOUNDING_BOX = 0;

    /**
     * The feature id for the '<em><b>Temporal Subset</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET = 1;

    /**
     * The number of structural features of the '<em>Domain Subset Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOMAIN_SUBSET_TYPE_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.FieldSubsetTypeImpl <em>Field Subset Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.FieldSubsetTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getFieldSubsetType()
     * @generated
     */
    int FIELD_SUBSET_TYPE = 12;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_SUBSET_TYPE__IDENTIFIER = 0;

    /**
     * The feature id for the '<em><b>Interpolation Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_SUBSET_TYPE__INTERPOLATION_TYPE = 1;

    /**
     * The feature id for the '<em><b>Axis Subset</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_SUBSET_TYPE__AXIS_SUBSET = 2;

    /**
     * The number of structural features of the '<em>Field Subset Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_SUBSET_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.FieldTypeImpl <em>Field Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.FieldTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getFieldType()
     * @generated
     */
    int FIELD_TYPE = 13;

    /**
     * The feature id for the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_TYPE__TITLE = owcsPackage.DESCRIPTION_TYPE__TITLE;

    /**
     * The feature id for the '<em><b>Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_TYPE__ABSTRACT = owcsPackage.DESCRIPTION_TYPE__ABSTRACT;

    /**
     * The feature id for the '<em><b>Keywords</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_TYPE__KEYWORDS = owcsPackage.DESCRIPTION_TYPE__KEYWORDS;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_TYPE__IDENTIFIER = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_TYPE__DEFINITION = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Null Value</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_TYPE__NULL_VALUE = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Interpolation Methods</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_TYPE__INTERPOLATION_METHODS = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Axis</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_TYPE__AXIS = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Field Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FIELD_TYPE_FEATURE_COUNT = owcsPackage.DESCRIPTION_TYPE_FEATURE_COUNT + 5;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.GetCapabilitiesTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getGetCapabilitiesType()
     * @generated
     */
    int GET_CAPABILITIES_TYPE = 14;

    /**
     * The feature id for the '<em><b>Accept Versions</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS = owcsPackage.GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS;

    /**
     * The feature id for the '<em><b>Sections</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__SECTIONS = owcsPackage.GET_CAPABILITIES_TYPE__SECTIONS;

    /**
     * The feature id for the '<em><b>Accept Formats</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__ACCEPT_FORMATS = owcsPackage.GET_CAPABILITIES_TYPE__ACCEPT_FORMATS;

    /**
     * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE = owcsPackage.GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE__SERVICE = owcsPackage.GET_CAPABILITIES_TYPE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Get Capabilities Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_CAPABILITIES_TYPE_FEATURE_COUNT = owcsPackage.GET_CAPABILITIES_TYPE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.GetCoverageTypeImpl <em>Get Coverage Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.GetCoverageTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getGetCoverageType()
     * @generated
     */
    int GET_COVERAGE_TYPE = 15;

    /**
     * The feature id for the '<em><b>Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_COVERAGE_TYPE__SERVICE = REQUEST_BASE_TYPE__SERVICE;

    /**
     * The feature id for the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_COVERAGE_TYPE__VERSION = REQUEST_BASE_TYPE__VERSION;

    /**
     * The feature id for the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_COVERAGE_TYPE__IDENTIFIER = REQUEST_BASE_TYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Domain Subset</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_COVERAGE_TYPE__DOMAIN_SUBSET = REQUEST_BASE_TYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Range Subset</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_COVERAGE_TYPE__RANGE_SUBSET = REQUEST_BASE_TYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Output</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_COVERAGE_TYPE__OUTPUT = REQUEST_BASE_TYPE_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Get Coverage Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GET_COVERAGE_TYPE_FEATURE_COUNT = REQUEST_BASE_TYPE_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.GridCrsTypeImpl <em>Grid Crs Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.GridCrsTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getGridCrsType()
     * @generated
     */
    int GRID_CRS_TYPE = 16;

    /**
     * The feature id for the '<em><b>Srs Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GRID_CRS_TYPE__SRS_NAME = 0;

    /**
     * The feature id for the '<em><b>Grid Base CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GRID_CRS_TYPE__GRID_BASE_CRS = 1;

    /**
     * The feature id for the '<em><b>Grid Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GRID_CRS_TYPE__GRID_TYPE = 2;

    /**
     * The feature id for the '<em><b>Grid Origin</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GRID_CRS_TYPE__GRID_ORIGIN = 3;

    /**
     * The feature id for the '<em><b>Grid Offsets</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GRID_CRS_TYPE__GRID_OFFSETS = 4;

    /**
     * The feature id for the '<em><b>Grid CS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GRID_CRS_TYPE__GRID_CS = 5;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GRID_CRS_TYPE__ID = 6;

    /**
     * The number of structural features of the '<em>Grid Crs Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GRID_CRS_TYPE_FEATURE_COUNT = 7;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.ImageCRSRefTypeImpl <em>Image CRS Ref Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.ImageCRSRefTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getImageCRSRefType()
     * @generated
     */
    int IMAGE_CRS_REF_TYPE = 17;

    /**
     * The feature id for the '<em><b>Image CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMAGE_CRS_REF_TYPE__IMAGE_CRS = 0;

    /**
     * The number of structural features of the '<em>Image CRS Ref Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMAGE_CRS_REF_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.OutputTypeImpl <em>Output Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.OutputTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getOutputType()
     * @generated
     */
    int OUTPUT_TYPE = 18;

    /**
     * The feature id for the '<em><b>Grid CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_TYPE__GRID_CRS = 0;

    /**
     * The feature id for the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_TYPE__FORMAT = 1;

    /**
     * The feature id for the '<em><b>Store</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_TYPE__STORE = 2;

    /**
     * The number of structural features of the '<em>Output Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OUTPUT_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.RangeSubsetTypeImpl <em>Range Subset Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.RangeSubsetTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getRangeSubsetType()
     * @generated
     */
    int RANGE_SUBSET_TYPE = 19;

    /**
     * The feature id for the '<em><b>Field Subset</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_SUBSET_TYPE__FIELD_SUBSET = 0;

    /**
     * The number of structural features of the '<em>Range Subset Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_SUBSET_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.RangeTypeImpl <em>Range Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.RangeTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getRangeType()
     * @generated
     */
    int RANGE_TYPE = 20;

    /**
     * The feature id for the '<em><b>Field</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_TYPE__FIELD = 0;

    /**
     * The number of structural features of the '<em>Range Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RANGE_TYPE_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.SpatialDomainTypeImpl <em>Spatial Domain Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.SpatialDomainTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getSpatialDomainType()
     * @generated
     */
    int SPATIAL_DOMAIN_TYPE = 22;

    /**
     * The feature id for the '<em><b>Bounding Box</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPATIAL_DOMAIN_TYPE__BOUNDING_BOX = 0;

    /**
     * The feature id for the '<em><b>Grid CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPATIAL_DOMAIN_TYPE__GRID_CRS = 1;

    /**
     * The feature id for the '<em><b>Transformation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPATIAL_DOMAIN_TYPE__TRANSFORMATION = 2;

    /**
     * The feature id for the '<em><b>Image CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPATIAL_DOMAIN_TYPE__IMAGE_CRS = 3;

    /**
     * The feature id for the '<em><b>Polygon</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPATIAL_DOMAIN_TYPE__POLYGON = 4;

    /**
     * The number of structural features of the '<em>Spatial Domain Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPATIAL_DOMAIN_TYPE_FEATURE_COUNT = 5;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.TimePeriodTypeImpl <em>Time Period Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.TimePeriodTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getTimePeriodType()
     * @generated
     */
    int TIME_PERIOD_TYPE = 23;

    /**
     * The feature id for the '<em><b>Begin Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TIME_PERIOD_TYPE__BEGIN_POSITION = 0;

    /**
     * The feature id for the '<em><b>End Position</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TIME_PERIOD_TYPE__END_POSITION = 1;

    /**
     * The feature id for the '<em><b>Time Resolution</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TIME_PERIOD_TYPE__TIME_RESOLUTION = 2;

    /**
     * The feature id for the '<em><b>Frame</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TIME_PERIOD_TYPE__FRAME = 3;

    /**
     * The number of structural features of the '<em>Time Period Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TIME_PERIOD_TYPE_FEATURE_COUNT = 4;

    /**
     * The meta object id for the '{@link net.opengis.wcs.impl.TimeSequenceTypeImpl <em>Time Sequence Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see net.opengis.wcs.impl.TimeSequenceTypeImpl
     * @see net.opengis.wcs.impl.wcsPackageImpl#getTimeSequenceType()
     * @generated
     */
    int TIME_SEQUENCE_TYPE = 24;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TIME_SEQUENCE_TYPE__GROUP = 0;

    /**
     * The feature id for the '<em><b>Time Position</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TIME_SEQUENCE_TYPE__TIME_POSITION = 1;

    /**
     * The feature id for the '<em><b>Time Period</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TIME_SEQUENCE_TYPE__TIME_PERIOD = 2;

    /**
     * The number of structural features of the '<em>Time Sequence Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TIME_SEQUENCE_TYPE_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '<em>Identifier Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see net.opengis.wcs.impl.wcsPackageImpl#getIdentifierType()
     * @generated
     */
    int IDENTIFIER_TYPE = 25;

    /**
     * The meta object id for the '<em>Time Duration Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.Object
     * @see net.opengis.wcs.impl.wcsPackageImpl#getTimeDurationType()
     * @generated
     */
    int TIME_DURATION_TYPE = 26;


    /**
     * Returns the meta object for class '{@link net.opengis.wcs.AvailableKeysType <em>Available Keys Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Available Keys Type</em>'.
     * @see net.opengis.wcs.AvailableKeysType
     * @generated
     */
    EClass getAvailableKeysType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.AvailableKeysType#getKey <em>Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Key</em>'.
     * @see net.opengis.wcs.AvailableKeysType#getKey()
     * @see #getAvailableKeysType()
     * @generated
     */
    EAttribute getAvailableKeysType_Key();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.AxisSubsetType <em>Axis Subset Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Axis Subset Type</em>'.
     * @see net.opengis.wcs.AxisSubsetType
     * @generated
     */
    EClass getAxisSubsetType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.AxisSubsetType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Identifier</em>'.
     * @see net.opengis.wcs.AxisSubsetType#getIdentifier()
     * @see #getAxisSubsetType()
     * @generated
     */
    EAttribute getAxisSubsetType_Identifier();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.AxisSubsetType#getKey <em>Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Key</em>'.
     * @see net.opengis.wcs.AxisSubsetType#getKey()
     * @see #getAxisSubsetType()
     * @generated
     */
    EAttribute getAxisSubsetType_Key();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.AxisType <em>Axis Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Axis Type</em>'.
     * @see net.opengis.wcs.AxisType
     * @generated
     */
    EClass getAxisType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.AxisType#getAvailableKeys <em>Available Keys</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Available Keys</em>'.
     * @see net.opengis.wcs.AxisType#getAvailableKeys()
     * @see #getAxisType()
     * @generated
     */
    EReference getAxisType_AvailableKeys();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.AxisType#getMeaning <em>Meaning</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Meaning</em>'.
     * @see net.opengis.wcs.AxisType#getMeaning()
     * @see #getAxisType()
     * @generated
     */
    EReference getAxisType_Meaning();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.AxisType#getDataType <em>Data Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Data Type</em>'.
     * @see net.opengis.wcs.AxisType#getDataType()
     * @see #getAxisType()
     * @generated
     */
    EReference getAxisType_DataType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.AxisType#getUOM <em>UOM</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>UOM</em>'.
     * @see net.opengis.wcs.AxisType#getUOM()
     * @see #getAxisType()
     * @generated
     */
    EReference getAxisType_UOM();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.AxisType#getReferenceSystem <em>Reference System</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Reference System</em>'.
     * @see net.opengis.wcs.AxisType#getReferenceSystem()
     * @see #getAxisType()
     * @generated
     */
    EReference getAxisType_ReferenceSystem();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.AxisType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Metadata</em>'.
     * @see net.opengis.wcs.AxisType#getMetadata()
     * @see #getAxisType()
     * @generated
     */
    EAttribute getAxisType_Metadata();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.AxisType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Identifier</em>'.
     * @see net.opengis.wcs.AxisType#getIdentifier()
     * @see #getAxisType()
     * @generated
     */
    EAttribute getAxisType_Identifier();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.CapabilitiesType <em>Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Capabilities Type</em>'.
     * @see net.opengis.wcs.CapabilitiesType
     * @generated
     */
    EClass getCapabilitiesType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.CapabilitiesType#getContents <em>Contents</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Contents</em>'.
     * @see net.opengis.wcs.CapabilitiesType#getContents()
     * @see #getCapabilitiesType()
     * @generated
     */
    EReference getCapabilitiesType_Contents();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ContentsType <em>Contents Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Contents Type</em>'.
     * @see net.opengis.wcs.ContentsType
     * @generated
     */
    EClass getContentsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.ContentsType#getCoverageSummary <em>Coverage Summary</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Coverage Summary</em>'.
     * @see net.opengis.wcs.ContentsType#getCoverageSummary()
     * @see #getContentsType()
     * @generated
     */
    EReference getContentsType_CoverageSummary();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ContentsType#getSupportedCRS <em>Supported CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Supported CRS</em>'.
     * @see net.opengis.wcs.ContentsType#getSupportedCRS()
     * @see #getContentsType()
     * @generated
     */
    EAttribute getContentsType_SupportedCRS();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ContentsType#getSupportedFormat <em>Supported Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Supported Format</em>'.
     * @see net.opengis.wcs.ContentsType#getSupportedFormat()
     * @see #getContentsType()
     * @generated
     */
    EAttribute getContentsType_SupportedFormat();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.ContentsType#getOtherSource <em>Other Source</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Other Source</em>'.
     * @see net.opengis.wcs.ContentsType#getOtherSource()
     * @see #getContentsType()
     * @generated
     */
    EAttribute getContentsType_OtherSource();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.CoverageDescriptionsType <em>Coverage Descriptions Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Coverage Descriptions Type</em>'.
     * @see net.opengis.wcs.CoverageDescriptionsType
     * @generated
     */
    EClass getCoverageDescriptionsType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.CoverageDescriptionsType#getCoverageDescription <em>Coverage Description</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Coverage Description</em>'.
     * @see net.opengis.wcs.CoverageDescriptionsType#getCoverageDescription()
     * @see #getCoverageDescriptionsType()
     * @generated
     */
    EReference getCoverageDescriptionsType_CoverageDescription();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.CoverageDescriptionType <em>Coverage Description Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Coverage Description Type</em>'.
     * @see net.opengis.wcs.CoverageDescriptionType
     * @generated
     */
    EClass getCoverageDescriptionType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.CoverageDescriptionType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Identifier</em>'.
     * @see net.opengis.wcs.CoverageDescriptionType#getIdentifier()
     * @see #getCoverageDescriptionType()
     * @generated
     */
    EAttribute getCoverageDescriptionType_Identifier();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.CoverageDescriptionType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Metadata</em>'.
     * @see net.opengis.wcs.CoverageDescriptionType#getMetadata()
     * @see #getCoverageDescriptionType()
     * @generated
     */
    EAttribute getCoverageDescriptionType_Metadata();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.CoverageDescriptionType#getDomain <em>Domain</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Domain</em>'.
     * @see net.opengis.wcs.CoverageDescriptionType#getDomain()
     * @see #getCoverageDescriptionType()
     * @generated
     */
    EReference getCoverageDescriptionType_Domain();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.CoverageDescriptionType#getRange <em>Range</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Range</em>'.
     * @see net.opengis.wcs.CoverageDescriptionType#getRange()
     * @see #getCoverageDescriptionType()
     * @generated
     */
    EReference getCoverageDescriptionType_Range();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.CoverageDescriptionType#getSupportedCRS <em>Supported CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Supported CRS</em>'.
     * @see net.opengis.wcs.CoverageDescriptionType#getSupportedCRS()
     * @see #getCoverageDescriptionType()
     * @generated
     */
    EAttribute getCoverageDescriptionType_SupportedCRS();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.CoverageDescriptionType#getSupportedFormat <em>Supported Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Supported Format</em>'.
     * @see net.opengis.wcs.CoverageDescriptionType#getSupportedFormat()
     * @see #getCoverageDescriptionType()
     * @generated
     */
    EAttribute getCoverageDescriptionType_SupportedFormat();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.CoverageDomainType <em>Coverage Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Coverage Domain Type</em>'.
     * @see net.opengis.wcs.CoverageDomainType
     * @generated
     */
    EClass getCoverageDomainType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.CoverageDomainType#getSpatialDomain <em>Spatial Domain</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Spatial Domain</em>'.
     * @see net.opengis.wcs.CoverageDomainType#getSpatialDomain()
     * @see #getCoverageDomainType()
     * @generated
     */
    EReference getCoverageDomainType_SpatialDomain();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.CoverageDomainType#getTemporalDomain <em>Temporal Domain</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Temporal Domain</em>'.
     * @see net.opengis.wcs.CoverageDomainType#getTemporalDomain()
     * @see #getCoverageDomainType()
     * @generated
     */
    EReference getCoverageDomainType_TemporalDomain();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.CoverageSummaryType <em>Coverage Summary Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Coverage Summary Type</em>'.
     * @see net.opengis.wcs.CoverageSummaryType
     * @generated
     */
    EClass getCoverageSummaryType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.CoverageSummaryType#getMetadata <em>Metadata</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Metadata</em>'.
     * @see net.opengis.wcs.CoverageSummaryType#getMetadata()
     * @see #getCoverageSummaryType()
     * @generated
     */
    EAttribute getCoverageSummaryType_Metadata();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.CoverageSummaryType#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>WGS84 Bounding Box</em>'.
     * @see net.opengis.wcs.CoverageSummaryType#getWGS84BoundingBox()
     * @see #getCoverageSummaryType()
     * @generated
     */
    EAttribute getCoverageSummaryType_WGS84BoundingBox();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.CoverageSummaryType#getSupportedCRS <em>Supported CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Supported CRS</em>'.
     * @see net.opengis.wcs.CoverageSummaryType#getSupportedCRS()
     * @see #getCoverageSummaryType()
     * @generated
     */
    EAttribute getCoverageSummaryType_SupportedCRS();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.CoverageSummaryType#getSupportedFormat <em>Supported Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Supported Format</em>'.
     * @see net.opengis.wcs.CoverageSummaryType#getSupportedFormat()
     * @see #getCoverageSummaryType()
     * @generated
     */
    EAttribute getCoverageSummaryType_SupportedFormat();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.CoverageSummaryType#getCoverageSummary <em>Coverage Summary</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Coverage Summary</em>'.
     * @see net.opengis.wcs.CoverageSummaryType#getCoverageSummary()
     * @see #getCoverageSummaryType()
     * @generated
     */
    EReference getCoverageSummaryType_CoverageSummary();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.CoverageSummaryType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Identifier</em>'.
     * @see net.opengis.wcs.CoverageSummaryType#getIdentifier()
     * @see #getCoverageSummaryType()
     * @generated
     */
    EAttribute getCoverageSummaryType_Identifier();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.CoverageSummaryType#getIdentifier1 <em>Identifier1</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Identifier1</em>'.
     * @see net.opengis.wcs.CoverageSummaryType#getIdentifier1()
     * @see #getCoverageSummaryType()
     * @generated
     */
    EAttribute getCoverageSummaryType_Identifier1();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.DescribeCoverageType <em>Describe Coverage Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Describe Coverage Type</em>'.
     * @see net.opengis.wcs.DescribeCoverageType
     * @generated
     */
    EClass getDescribeCoverageType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.DescribeCoverageType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Identifier</em>'.
     * @see net.opengis.wcs.DescribeCoverageType#getIdentifier()
     * @see #getDescribeCoverageType()
     * @generated
     */
    EAttribute getDescribeCoverageType_Identifier();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Document Root</em>'.
     * @see net.opengis.wcs.DocumentRoot
     * @generated
     */
    EClass getDocumentRoot();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.DocumentRoot#getMixed <em>Mixed</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see net.opengis.wcs.DocumentRoot#getMixed()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Mixed();

    /**
     * Returns the meta object for the map '{@link net.opengis.wcs.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
     * @see net.opengis.wcs.DocumentRoot#getXMLNSPrefixMap()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XMLNSPrefixMap();

    /**
     * Returns the meta object for the map '{@link net.opengis.wcs.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XSI Schema Location</em>'.
     * @see net.opengis.wcs.DocumentRoot#getXSISchemaLocation()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XSISchemaLocation();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.DocumentRoot#getAvailableKeys <em>Available Keys</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Available Keys</em>'.
     * @see net.opengis.wcs.DocumentRoot#getAvailableKeys()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AvailableKeys();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.DocumentRoot#getAxisSubset <em>Axis Subset</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Axis Subset</em>'.
     * @see net.opengis.wcs.DocumentRoot#getAxisSubset()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AxisSubset();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.DocumentRoot#getCapabilities <em>Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Capabilities</em>'.
     * @see net.opengis.wcs.DocumentRoot#getCapabilities()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Capabilities();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.DocumentRoot#getContents <em>Contents</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Contents</em>'.
     * @see net.opengis.wcs.DocumentRoot#getContents()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Contents();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.DocumentRoot#getCoverageDescriptions <em>Coverage Descriptions</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Coverage Descriptions</em>'.
     * @see net.opengis.wcs.DocumentRoot#getCoverageDescriptions()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CoverageDescriptions();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.DocumentRoot#getCoverageSummary <em>Coverage Summary</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Coverage Summary</em>'.
     * @see net.opengis.wcs.DocumentRoot#getCoverageSummary()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CoverageSummary();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.DocumentRoot#getDescribeCoverage <em>Describe Coverage</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Describe Coverage</em>'.
     * @see net.opengis.wcs.DocumentRoot#getDescribeCoverage()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DescribeCoverage();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Get Capabilities</em>'.
     * @see net.opengis.wcs.DocumentRoot#getGetCapabilities()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_GetCapabilities();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.DocumentRoot#getGetCoverage <em>Get Coverage</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Get Coverage</em>'.
     * @see net.opengis.wcs.DocumentRoot#getGetCoverage()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_GetCoverage();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.DocumentRoot#getGridBaseCRS <em>Grid Base CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Grid Base CRS</em>'.
     * @see net.opengis.wcs.DocumentRoot#getGridBaseCRS()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_GridBaseCRS();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.DocumentRoot#getGridCRS <em>Grid CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Grid CRS</em>'.
     * @see net.opengis.wcs.DocumentRoot#getGridCRS()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_GridCRS();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.DocumentRoot#getGridCS <em>Grid CS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Grid CS</em>'.
     * @see net.opengis.wcs.DocumentRoot#getGridCS()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_GridCS();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.DocumentRoot#getGridOffsets <em>Grid Offsets</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Grid Offsets</em>'.
     * @see net.opengis.wcs.DocumentRoot#getGridOffsets()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_GridOffsets();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.DocumentRoot#getGridOrigin <em>Grid Origin</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Grid Origin</em>'.
     * @see net.opengis.wcs.DocumentRoot#getGridOrigin()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_GridOrigin();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.DocumentRoot#getGridType <em>Grid Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Grid Type</em>'.
     * @see net.opengis.wcs.DocumentRoot#getGridType()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_GridType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.DocumentRoot#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Identifier</em>'.
     * @see net.opengis.wcs.DocumentRoot#getIdentifier()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Identifier();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.DocumentRoot#getTemporalDomain <em>Temporal Domain</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Temporal Domain</em>'.
     * @see net.opengis.wcs.DocumentRoot#getTemporalDomain()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_TemporalDomain();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.DocumentRoot#getTemporalSubset <em>Temporal Subset</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Temporal Subset</em>'.
     * @see net.opengis.wcs.DocumentRoot#getTemporalSubset()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_TemporalSubset();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.DomainSubsetType <em>Domain Subset Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Domain Subset Type</em>'.
     * @see net.opengis.wcs.DomainSubsetType
     * @generated
     */
    EClass getDomainSubsetType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.DomainSubsetType#getBoundingBox <em>Bounding Box</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Bounding Box</em>'.
     * @see net.opengis.wcs.DomainSubsetType#getBoundingBox()
     * @see #getDomainSubsetType()
     * @generated
     */
    EAttribute getDomainSubsetType_BoundingBox();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.DomainSubsetType#getTemporalSubset <em>Temporal Subset</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Temporal Subset</em>'.
     * @see net.opengis.wcs.DomainSubsetType#getTemporalSubset()
     * @see #getDomainSubsetType()
     * @generated
     */
    EReference getDomainSubsetType_TemporalSubset();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.FieldSubsetType <em>Field Subset Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Field Subset Type</em>'.
     * @see net.opengis.wcs.FieldSubsetType
     * @generated
     */
    EClass getFieldSubsetType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.FieldSubsetType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Identifier</em>'.
     * @see net.opengis.wcs.FieldSubsetType#getIdentifier()
     * @see #getFieldSubsetType()
     * @generated
     */
    EAttribute getFieldSubsetType_Identifier();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.FieldSubsetType#getInterpolationType <em>Interpolation Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Interpolation Type</em>'.
     * @see net.opengis.wcs.FieldSubsetType#getInterpolationType()
     * @see #getFieldSubsetType()
     * @generated
     */
    EAttribute getFieldSubsetType_InterpolationType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.FieldSubsetType#getAxisSubset <em>Axis Subset</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Axis Subset</em>'.
     * @see net.opengis.wcs.FieldSubsetType#getAxisSubset()
     * @see #getFieldSubsetType()
     * @generated
     */
    EReference getFieldSubsetType_AxisSubset();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.FieldType <em>Field Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Field Type</em>'.
     * @see net.opengis.wcs.FieldType
     * @generated
     */
    EClass getFieldType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.FieldType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Identifier</em>'.
     * @see net.opengis.wcs.FieldType#getIdentifier()
     * @see #getFieldType()
     * @generated
     */
    EAttribute getFieldType_Identifier();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.FieldType#getDefinition <em>Definition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Definition</em>'.
     * @see net.opengis.wcs.FieldType#getDefinition()
     * @see #getFieldType()
     * @generated
     */
    EReference getFieldType_Definition();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.FieldType#getNullValue <em>Null Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Null Value</em>'.
     * @see net.opengis.wcs.FieldType#getNullValue()
     * @see #getFieldType()
     * @generated
     */
    EAttribute getFieldType_NullValue();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.FieldType#getInterpolationMethods <em>Interpolation Methods</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Interpolation Methods</em>'.
     * @see net.opengis.wcs.FieldType#getInterpolationMethods()
     * @see #getFieldType()
     * @generated
     */
    EReference getFieldType_InterpolationMethods();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.FieldType#getAxis <em>Axis</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Axis</em>'.
     * @see net.opengis.wcs.FieldType#getAxis()
     * @see #getFieldType()
     * @generated
     */
    EReference getFieldType_Axis();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Get Capabilities Type</em>'.
     * @see net.opengis.wcs.GetCapabilitiesType
     * @generated
     */
    EClass getGetCapabilitiesType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.GetCapabilitiesType#getService <em>Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service</em>'.
     * @see net.opengis.wcs.GetCapabilitiesType#getService()
     * @see #getGetCapabilitiesType()
     * @generated
     */
    EAttribute getGetCapabilitiesType_Service();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.GetCoverageType <em>Get Coverage Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Get Coverage Type</em>'.
     * @see net.opengis.wcs.GetCoverageType
     * @generated
     */
    EClass getGetCoverageType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.GetCoverageType#getIdentifier <em>Identifier</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Identifier</em>'.
     * @see net.opengis.wcs.GetCoverageType#getIdentifier()
     * @see #getGetCoverageType()
     * @generated
     */
    EAttribute getGetCoverageType_Identifier();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.GetCoverageType#getDomainSubset <em>Domain Subset</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Domain Subset</em>'.
     * @see net.opengis.wcs.GetCoverageType#getDomainSubset()
     * @see #getGetCoverageType()
     * @generated
     */
    EReference getGetCoverageType_DomainSubset();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.GetCoverageType#getRangeSubset <em>Range Subset</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Range Subset</em>'.
     * @see net.opengis.wcs.GetCoverageType#getRangeSubset()
     * @see #getGetCoverageType()
     * @generated
     */
    EReference getGetCoverageType_RangeSubset();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.GetCoverageType#getOutput <em>Output</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Output</em>'.
     * @see net.opengis.wcs.GetCoverageType#getOutput()
     * @see #getGetCoverageType()
     * @generated
     */
    EReference getGetCoverageType_Output();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.GridCrsType <em>Grid Crs Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Grid Crs Type</em>'.
     * @see net.opengis.wcs.GridCrsType
     * @generated
     */
    EClass getGridCrsType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.GridCrsType#getSrsName <em>Srs Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Srs Name</em>'.
     * @see net.opengis.wcs.GridCrsType#getSrsName()
     * @see #getGridCrsType()
     * @generated
     */
    EAttribute getGridCrsType_SrsName();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.GridCrsType#getGridBaseCRS <em>Grid Base CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Grid Base CRS</em>'.
     * @see net.opengis.wcs.GridCrsType#getGridBaseCRS()
     * @see #getGridCrsType()
     * @generated
     */
    EAttribute getGridCrsType_GridBaseCRS();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.GridCrsType#getGridType <em>Grid Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Grid Type</em>'.
     * @see net.opengis.wcs.GridCrsType#getGridType()
     * @see #getGridCrsType()
     * @generated
     */
    EAttribute getGridCrsType_GridType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.GridCrsType#getGridOrigin <em>Grid Origin</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Grid Origin</em>'.
     * @see net.opengis.wcs.GridCrsType#getGridOrigin()
     * @see #getGridCrsType()
     * @generated
     */
    EAttribute getGridCrsType_GridOrigin();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.GridCrsType#getGridOffsets <em>Grid Offsets</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Grid Offsets</em>'.
     * @see net.opengis.wcs.GridCrsType#getGridOffsets()
     * @see #getGridCrsType()
     * @generated
     */
    EAttribute getGridCrsType_GridOffsets();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.GridCrsType#getGridCS <em>Grid CS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Grid CS</em>'.
     * @see net.opengis.wcs.GridCrsType#getGridCS()
     * @see #getGridCrsType()
     * @generated
     */
    EAttribute getGridCrsType_GridCS();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.GridCrsType#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see net.opengis.wcs.GridCrsType#getId()
     * @see #getGridCrsType()
     * @generated
     */
    EAttribute getGridCrsType_Id();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.ImageCRSRefType <em>Image CRS Ref Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Image CRS Ref Type</em>'.
     * @see net.opengis.wcs.ImageCRSRefType
     * @generated
     */
    EClass getImageCRSRefType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.ImageCRSRefType#getImageCRS <em>Image CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Image CRS</em>'.
     * @see net.opengis.wcs.ImageCRSRefType#getImageCRS()
     * @see #getImageCRSRefType()
     * @generated
     */
    EAttribute getImageCRSRefType_ImageCRS();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.OutputType <em>Output Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Output Type</em>'.
     * @see net.opengis.wcs.OutputType
     * @generated
     */
    EClass getOutputType();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.OutputType#getGridCRS <em>Grid CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Grid CRS</em>'.
     * @see net.opengis.wcs.OutputType#getGridCRS()
     * @see #getOutputType()
     * @generated
     */
    EReference getOutputType_GridCRS();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.OutputType#getFormat <em>Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Format</em>'.
     * @see net.opengis.wcs.OutputType#getFormat()
     * @see #getOutputType()
     * @generated
     */
    EAttribute getOutputType_Format();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.OutputType#isStore <em>Store</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Store</em>'.
     * @see net.opengis.wcs.OutputType#isStore()
     * @see #getOutputType()
     * @generated
     */
    EAttribute getOutputType_Store();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.RangeSubsetType <em>Range Subset Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Range Subset Type</em>'.
     * @see net.opengis.wcs.RangeSubsetType
     * @generated
     */
    EClass getRangeSubsetType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.RangeSubsetType#getFieldSubset <em>Field Subset</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Field Subset</em>'.
     * @see net.opengis.wcs.RangeSubsetType#getFieldSubset()
     * @see #getRangeSubsetType()
     * @generated
     */
    EReference getRangeSubsetType_FieldSubset();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.RangeType <em>Range Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Range Type</em>'.
     * @see net.opengis.wcs.RangeType
     * @generated
     */
    EClass getRangeType();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.RangeType#getField <em>Field</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Field</em>'.
     * @see net.opengis.wcs.RangeType#getField()
     * @see #getRangeType()
     * @generated
     */
    EReference getRangeType_Field();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.RequestBaseType <em>Request Base Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Request Base Type</em>'.
     * @see net.opengis.wcs.RequestBaseType
     * @generated
     */
    EClass getRequestBaseType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.RequestBaseType#getService <em>Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Service</em>'.
     * @see net.opengis.wcs.RequestBaseType#getService()
     * @see #getRequestBaseType()
     * @generated
     */
    EAttribute getRequestBaseType_Service();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.RequestBaseType#getVersion <em>Version</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Version</em>'.
     * @see net.opengis.wcs.RequestBaseType#getVersion()
     * @see #getRequestBaseType()
     * @generated
     */
    EAttribute getRequestBaseType_Version();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.SpatialDomainType <em>Spatial Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Spatial Domain Type</em>'.
     * @see net.opengis.wcs.SpatialDomainType
     * @generated
     */
    EClass getSpatialDomainType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.SpatialDomainType#getBoundingBox <em>Bounding Box</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Bounding Box</em>'.
     * @see net.opengis.wcs.SpatialDomainType#getBoundingBox()
     * @see #getSpatialDomainType()
     * @generated
     */
    EAttribute getSpatialDomainType_BoundingBox();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.SpatialDomainType#getGridCRS <em>Grid CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Grid CRS</em>'.
     * @see net.opengis.wcs.SpatialDomainType#getGridCRS()
     * @see #getSpatialDomainType()
     * @generated
     */
    EReference getSpatialDomainType_GridCRS();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.SpatialDomainType#getTransformation <em>Transformation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Transformation</em>'.
     * @see net.opengis.wcs.SpatialDomainType#getTransformation()
     * @see #getSpatialDomainType()
     * @generated
     */
    EAttribute getSpatialDomainType_Transformation();

    /**
     * Returns the meta object for the containment reference '{@link net.opengis.wcs.SpatialDomainType#getImageCRS <em>Image CRS</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Image CRS</em>'.
     * @see net.opengis.wcs.SpatialDomainType#getImageCRS()
     * @see #getSpatialDomainType()
     * @generated
     */
    EReference getSpatialDomainType_ImageCRS();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.SpatialDomainType#getPolygon <em>Polygon</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Polygon</em>'.
     * @see net.opengis.wcs.SpatialDomainType#getPolygon()
     * @see #getSpatialDomainType()
     * @generated
     */
    EAttribute getSpatialDomainType_Polygon();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.TimePeriodType <em>Time Period Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Time Period Type</em>'.
     * @see net.opengis.wcs.TimePeriodType
     * @generated
     */
    EClass getTimePeriodType();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.TimePeriodType#getBeginPosition <em>Begin Position</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Begin Position</em>'.
     * @see net.opengis.wcs.TimePeriodType#getBeginPosition()
     * @see #getTimePeriodType()
     * @generated
     */
    EAttribute getTimePeriodType_BeginPosition();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.TimePeriodType#getEndPosition <em>End Position</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>End Position</em>'.
     * @see net.opengis.wcs.TimePeriodType#getEndPosition()
     * @see #getTimePeriodType()
     * @generated
     */
    EAttribute getTimePeriodType_EndPosition();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.TimePeriodType#getTimeResolution <em>Time Resolution</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Time Resolution</em>'.
     * @see net.opengis.wcs.TimePeriodType#getTimeResolution()
     * @see #getTimePeriodType()
     * @generated
     */
    EAttribute getTimePeriodType_TimeResolution();

    /**
     * Returns the meta object for the attribute '{@link net.opengis.wcs.TimePeriodType#getFrame <em>Frame</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Frame</em>'.
     * @see net.opengis.wcs.TimePeriodType#getFrame()
     * @see #getTimePeriodType()
     * @generated
     */
    EAttribute getTimePeriodType_Frame();

    /**
     * Returns the meta object for class '{@link net.opengis.wcs.TimeSequenceType <em>Time Sequence Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Time Sequence Type</em>'.
     * @see net.opengis.wcs.TimeSequenceType
     * @generated
     */
    EClass getTimeSequenceType();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.TimeSequenceType#getGroup <em>Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see net.opengis.wcs.TimeSequenceType#getGroup()
     * @see #getTimeSequenceType()
     * @generated
     */
    EAttribute getTimeSequenceType_Group();

    /**
     * Returns the meta object for the attribute list '{@link net.opengis.wcs.TimeSequenceType#getTimePosition <em>Time Position</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Time Position</em>'.
     * @see net.opengis.wcs.TimeSequenceType#getTimePosition()
     * @see #getTimeSequenceType()
     * @generated
     */
    EAttribute getTimeSequenceType_TimePosition();

    /**
     * Returns the meta object for the containment reference list '{@link net.opengis.wcs.TimeSequenceType#getTimePeriod <em>Time Period</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Time Period</em>'.
     * @see net.opengis.wcs.TimeSequenceType#getTimePeriod()
     * @see #getTimeSequenceType()
     * @generated
     */
    EReference getTimeSequenceType_TimePeriod();

    /**
     * Returns the meta object for data type '{@link java.lang.String <em>Identifier Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Identifier Type</em>'.
     * @see java.lang.String
     * @model instanceClass="java.lang.String"
     *        extendedMetaData="name='IdentifierType' baseType='http://www.eclipse.org/emf/2003/XMLType#string' pattern='.+'"
     * @generated
     */
    EDataType getIdentifierType();

    /**
     * Returns the meta object for data type '{@link java.lang.Object <em>Time Duration Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Time Duration Type</em>'.
     * @see java.lang.Object
     * @model instanceClass="java.lang.Object"
     *        extendedMetaData="name='TimeDurationType' memberTypes='http://www.eclipse.org/emf/2003/XMLType#duration http://www.eclipse.org/emf/2003/XMLType#decimal'"
     * @generated
     */
    EDataType getTimeDurationType();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    wcsFactory getwcsFactory();

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
         * The meta object literal for the '{@link net.opengis.wcs.impl.AvailableKeysTypeImpl <em>Available Keys Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.AvailableKeysTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getAvailableKeysType()
         * @generated
         */
        EClass AVAILABLE_KEYS_TYPE = eINSTANCE.getAvailableKeysType();

        /**
         * The meta object literal for the '<em><b>Key</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute AVAILABLE_KEYS_TYPE__KEY = eINSTANCE.getAvailableKeysType_Key();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.AxisSubsetTypeImpl <em>Axis Subset Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.AxisSubsetTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getAxisSubsetType()
         * @generated
         */
        EClass AXIS_SUBSET_TYPE = eINSTANCE.getAxisSubsetType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute AXIS_SUBSET_TYPE__IDENTIFIER = eINSTANCE.getAxisSubsetType_Identifier();

        /**
         * The meta object literal for the '<em><b>Key</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute AXIS_SUBSET_TYPE__KEY = eINSTANCE.getAxisSubsetType_Key();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.AxisTypeImpl <em>Axis Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.AxisTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getAxisType()
         * @generated
         */
        EClass AXIS_TYPE = eINSTANCE.getAxisType();

        /**
         * The meta object literal for the '<em><b>Available Keys</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference AXIS_TYPE__AVAILABLE_KEYS = eINSTANCE.getAxisType_AvailableKeys();

        /**
         * The meta object literal for the '<em><b>Meaning</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference AXIS_TYPE__MEANING = eINSTANCE.getAxisType_Meaning();

        /**
         * The meta object literal for the '<em><b>Data Type</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference AXIS_TYPE__DATA_TYPE = eINSTANCE.getAxisType_DataType();

        /**
         * The meta object literal for the '<em><b>UOM</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference AXIS_TYPE__UOM = eINSTANCE.getAxisType_UOM();

        /**
         * The meta object literal for the '<em><b>Reference System</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference AXIS_TYPE__REFERENCE_SYSTEM = eINSTANCE.getAxisType_ReferenceSystem();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute AXIS_TYPE__METADATA = eINSTANCE.getAxisType_Metadata();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute AXIS_TYPE__IDENTIFIER = eINSTANCE.getAxisType_Identifier();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.CapabilitiesTypeImpl <em>Capabilities Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.CapabilitiesTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getCapabilitiesType()
         * @generated
         */
        EClass CAPABILITIES_TYPE = eINSTANCE.getCapabilitiesType();

        /**
         * The meta object literal for the '<em><b>Contents</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CAPABILITIES_TYPE__CONTENTS = eINSTANCE.getCapabilitiesType_Contents();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.ContentsTypeImpl <em>Contents Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.ContentsTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getContentsType()
         * @generated
         */
        EClass CONTENTS_TYPE = eINSTANCE.getContentsType();

        /**
         * The meta object literal for the '<em><b>Coverage Summary</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONTENTS_TYPE__COVERAGE_SUMMARY = eINSTANCE.getContentsType_CoverageSummary();

        /**
         * The meta object literal for the '<em><b>Supported CRS</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CONTENTS_TYPE__SUPPORTED_CRS = eINSTANCE.getContentsType_SupportedCRS();

        /**
         * The meta object literal for the '<em><b>Supported Format</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CONTENTS_TYPE__SUPPORTED_FORMAT = eINSTANCE.getContentsType_SupportedFormat();

        /**
         * The meta object literal for the '<em><b>Other Source</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CONTENTS_TYPE__OTHER_SOURCE = eINSTANCE.getContentsType_OtherSource();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.CoverageDescriptionsTypeImpl <em>Coverage Descriptions Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.CoverageDescriptionsTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getCoverageDescriptionsType()
         * @generated
         */
        EClass COVERAGE_DESCRIPTIONS_TYPE = eINSTANCE.getCoverageDescriptionsType();

        /**
         * The meta object literal for the '<em><b>Coverage Description</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COVERAGE_DESCRIPTIONS_TYPE__COVERAGE_DESCRIPTION = eINSTANCE.getCoverageDescriptionsType_CoverageDescription();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.CoverageDescriptionTypeImpl <em>Coverage Description Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.CoverageDescriptionTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getCoverageDescriptionType()
         * @generated
         */
        EClass COVERAGE_DESCRIPTION_TYPE = eINSTANCE.getCoverageDescriptionType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COVERAGE_DESCRIPTION_TYPE__IDENTIFIER = eINSTANCE.getCoverageDescriptionType_Identifier();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COVERAGE_DESCRIPTION_TYPE__METADATA = eINSTANCE.getCoverageDescriptionType_Metadata();

        /**
         * The meta object literal for the '<em><b>Domain</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COVERAGE_DESCRIPTION_TYPE__DOMAIN = eINSTANCE.getCoverageDescriptionType_Domain();

        /**
         * The meta object literal for the '<em><b>Range</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COVERAGE_DESCRIPTION_TYPE__RANGE = eINSTANCE.getCoverageDescriptionType_Range();

        /**
         * The meta object literal for the '<em><b>Supported CRS</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COVERAGE_DESCRIPTION_TYPE__SUPPORTED_CRS = eINSTANCE.getCoverageDescriptionType_SupportedCRS();

        /**
         * The meta object literal for the '<em><b>Supported Format</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COVERAGE_DESCRIPTION_TYPE__SUPPORTED_FORMAT = eINSTANCE.getCoverageDescriptionType_SupportedFormat();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.CoverageDomainTypeImpl <em>Coverage Domain Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.CoverageDomainTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getCoverageDomainType()
         * @generated
         */
        EClass COVERAGE_DOMAIN_TYPE = eINSTANCE.getCoverageDomainType();

        /**
         * The meta object literal for the '<em><b>Spatial Domain</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COVERAGE_DOMAIN_TYPE__SPATIAL_DOMAIN = eINSTANCE.getCoverageDomainType_SpatialDomain();

        /**
         * The meta object literal for the '<em><b>Temporal Domain</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COVERAGE_DOMAIN_TYPE__TEMPORAL_DOMAIN = eINSTANCE.getCoverageDomainType_TemporalDomain();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.CoverageSummaryTypeImpl <em>Coverage Summary Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.CoverageSummaryTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getCoverageSummaryType()
         * @generated
         */
        EClass COVERAGE_SUMMARY_TYPE = eINSTANCE.getCoverageSummaryType();

        /**
         * The meta object literal for the '<em><b>Metadata</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COVERAGE_SUMMARY_TYPE__METADATA = eINSTANCE.getCoverageSummaryType_Metadata();

        /**
         * The meta object literal for the '<em><b>WGS84 Bounding Box</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX = eINSTANCE.getCoverageSummaryType_WGS84BoundingBox();

        /**
         * The meta object literal for the '<em><b>Supported CRS</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COVERAGE_SUMMARY_TYPE__SUPPORTED_CRS = eINSTANCE.getCoverageSummaryType_SupportedCRS();

        /**
         * The meta object literal for the '<em><b>Supported Format</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COVERAGE_SUMMARY_TYPE__SUPPORTED_FORMAT = eINSTANCE.getCoverageSummaryType_SupportedFormat();

        /**
         * The meta object literal for the '<em><b>Coverage Summary</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COVERAGE_SUMMARY_TYPE__COVERAGE_SUMMARY = eINSTANCE.getCoverageSummaryType_CoverageSummary();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COVERAGE_SUMMARY_TYPE__IDENTIFIER = eINSTANCE.getCoverageSummaryType_Identifier();

        /**
         * The meta object literal for the '<em><b>Identifier1</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COVERAGE_SUMMARY_TYPE__IDENTIFIER1 = eINSTANCE.getCoverageSummaryType_Identifier1();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.DescribeCoverageTypeImpl <em>Describe Coverage Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.DescribeCoverageTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getDescribeCoverageType()
         * @generated
         */
        EClass DESCRIBE_COVERAGE_TYPE = eINSTANCE.getDescribeCoverageType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DESCRIBE_COVERAGE_TYPE__IDENTIFIER = eINSTANCE.getDescribeCoverageType_Identifier();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.DocumentRootImpl <em>Document Root</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.DocumentRootImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getDocumentRoot()
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
         * The meta object literal for the '<em><b>Available Keys</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__AVAILABLE_KEYS = eINSTANCE.getDocumentRoot_AvailableKeys();

        /**
         * The meta object literal for the '<em><b>Axis Subset</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__AXIS_SUBSET = eINSTANCE.getDocumentRoot_AxisSubset();

        /**
         * The meta object literal for the '<em><b>Capabilities</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__CAPABILITIES = eINSTANCE.getDocumentRoot_Capabilities();

        /**
         * The meta object literal for the '<em><b>Contents</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__CONTENTS = eINSTANCE.getDocumentRoot_Contents();

        /**
         * The meta object literal for the '<em><b>Coverage Descriptions</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS = eINSTANCE.getDocumentRoot_CoverageDescriptions();

        /**
         * The meta object literal for the '<em><b>Coverage Summary</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__COVERAGE_SUMMARY = eINSTANCE.getDocumentRoot_CoverageSummary();

        /**
         * The meta object literal for the '<em><b>Describe Coverage</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__DESCRIBE_COVERAGE = eINSTANCE.getDocumentRoot_DescribeCoverage();

        /**
         * The meta object literal for the '<em><b>Get Capabilities</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__GET_CAPABILITIES = eINSTANCE.getDocumentRoot_GetCapabilities();

        /**
         * The meta object literal for the '<em><b>Get Coverage</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__GET_COVERAGE = eINSTANCE.getDocumentRoot_GetCoverage();

        /**
         * The meta object literal for the '<em><b>Grid Base CRS</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__GRID_BASE_CRS = eINSTANCE.getDocumentRoot_GridBaseCRS();

        /**
         * The meta object literal for the '<em><b>Grid CRS</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__GRID_CRS = eINSTANCE.getDocumentRoot_GridCRS();

        /**
         * The meta object literal for the '<em><b>Grid CS</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__GRID_CS = eINSTANCE.getDocumentRoot_GridCS();

        /**
         * The meta object literal for the '<em><b>Grid Offsets</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__GRID_OFFSETS = eINSTANCE.getDocumentRoot_GridOffsets();

        /**
         * The meta object literal for the '<em><b>Grid Origin</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__GRID_ORIGIN = eINSTANCE.getDocumentRoot_GridOrigin();

        /**
         * The meta object literal for the '<em><b>Grid Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__GRID_TYPE = eINSTANCE.getDocumentRoot_GridType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__IDENTIFIER = eINSTANCE.getDocumentRoot_Identifier();

        /**
         * The meta object literal for the '<em><b>Temporal Domain</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__TEMPORAL_DOMAIN = eINSTANCE.getDocumentRoot_TemporalDomain();

        /**
         * The meta object literal for the '<em><b>Temporal Subset</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__TEMPORAL_SUBSET = eINSTANCE.getDocumentRoot_TemporalSubset();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.DomainSubsetTypeImpl <em>Domain Subset Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.DomainSubsetTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getDomainSubsetType()
         * @generated
         */
        EClass DOMAIN_SUBSET_TYPE = eINSTANCE.getDomainSubsetType();

        /**
         * The meta object literal for the '<em><b>Bounding Box</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOMAIN_SUBSET_TYPE__BOUNDING_BOX = eINSTANCE.getDomainSubsetType_BoundingBox();

        /**
         * The meta object literal for the '<em><b>Temporal Subset</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET = eINSTANCE.getDomainSubsetType_TemporalSubset();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.FieldSubsetTypeImpl <em>Field Subset Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.FieldSubsetTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getFieldSubsetType()
         * @generated
         */
        EClass FIELD_SUBSET_TYPE = eINSTANCE.getFieldSubsetType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FIELD_SUBSET_TYPE__IDENTIFIER = eINSTANCE.getFieldSubsetType_Identifier();

        /**
         * The meta object literal for the '<em><b>Interpolation Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FIELD_SUBSET_TYPE__INTERPOLATION_TYPE = eINSTANCE.getFieldSubsetType_InterpolationType();

        /**
         * The meta object literal for the '<em><b>Axis Subset</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FIELD_SUBSET_TYPE__AXIS_SUBSET = eINSTANCE.getFieldSubsetType_AxisSubset();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.FieldTypeImpl <em>Field Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.FieldTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getFieldType()
         * @generated
         */
        EClass FIELD_TYPE = eINSTANCE.getFieldType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FIELD_TYPE__IDENTIFIER = eINSTANCE.getFieldType_Identifier();

        /**
         * The meta object literal for the '<em><b>Definition</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FIELD_TYPE__DEFINITION = eINSTANCE.getFieldType_Definition();

        /**
         * The meta object literal for the '<em><b>Null Value</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute FIELD_TYPE__NULL_VALUE = eINSTANCE.getFieldType_NullValue();

        /**
         * The meta object literal for the '<em><b>Interpolation Methods</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FIELD_TYPE__INTERPOLATION_METHODS = eINSTANCE.getFieldType_InterpolationMethods();

        /**
         * The meta object literal for the '<em><b>Axis</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference FIELD_TYPE__AXIS = eINSTANCE.getFieldType_Axis();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.GetCapabilitiesTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getGetCapabilitiesType()
         * @generated
         */
        EClass GET_CAPABILITIES_TYPE = eINSTANCE.getGetCapabilitiesType();

        /**
         * The meta object literal for the '<em><b>Service</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_CAPABILITIES_TYPE__SERVICE = eINSTANCE.getGetCapabilitiesType_Service();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.GetCoverageTypeImpl <em>Get Coverage Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.GetCoverageTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getGetCoverageType()
         * @generated
         */
        EClass GET_COVERAGE_TYPE = eINSTANCE.getGetCoverageType();

        /**
         * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GET_COVERAGE_TYPE__IDENTIFIER = eINSTANCE.getGetCoverageType_Identifier();

        /**
         * The meta object literal for the '<em><b>Domain Subset</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GET_COVERAGE_TYPE__DOMAIN_SUBSET = eINSTANCE.getGetCoverageType_DomainSubset();

        /**
         * The meta object literal for the '<em><b>Range Subset</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GET_COVERAGE_TYPE__RANGE_SUBSET = eINSTANCE.getGetCoverageType_RangeSubset();

        /**
         * The meta object literal for the '<em><b>Output</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GET_COVERAGE_TYPE__OUTPUT = eINSTANCE.getGetCoverageType_Output();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.GridCrsTypeImpl <em>Grid Crs Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.GridCrsTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getGridCrsType()
         * @generated
         */
        EClass GRID_CRS_TYPE = eINSTANCE.getGridCrsType();

        /**
         * The meta object literal for the '<em><b>Srs Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GRID_CRS_TYPE__SRS_NAME = eINSTANCE.getGridCrsType_SrsName();

        /**
         * The meta object literal for the '<em><b>Grid Base CRS</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GRID_CRS_TYPE__GRID_BASE_CRS = eINSTANCE.getGridCrsType_GridBaseCRS();

        /**
         * The meta object literal for the '<em><b>Grid Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GRID_CRS_TYPE__GRID_TYPE = eINSTANCE.getGridCrsType_GridType();

        /**
         * The meta object literal for the '<em><b>Grid Origin</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GRID_CRS_TYPE__GRID_ORIGIN = eINSTANCE.getGridCrsType_GridOrigin();

        /**
         * The meta object literal for the '<em><b>Grid Offsets</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GRID_CRS_TYPE__GRID_OFFSETS = eINSTANCE.getGridCrsType_GridOffsets();

        /**
         * The meta object literal for the '<em><b>Grid CS</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GRID_CRS_TYPE__GRID_CS = eINSTANCE.getGridCrsType_GridCS();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GRID_CRS_TYPE__ID = eINSTANCE.getGridCrsType_Id();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.ImageCRSRefTypeImpl <em>Image CRS Ref Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.ImageCRSRefTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getImageCRSRefType()
         * @generated
         */
        EClass IMAGE_CRS_REF_TYPE = eINSTANCE.getImageCRSRefType();

        /**
         * The meta object literal for the '<em><b>Image CRS</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute IMAGE_CRS_REF_TYPE__IMAGE_CRS = eINSTANCE.getImageCRSRefType_ImageCRS();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.OutputTypeImpl <em>Output Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.OutputTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getOutputType()
         * @generated
         */
        EClass OUTPUT_TYPE = eINSTANCE.getOutputType();

        /**
         * The meta object literal for the '<em><b>Grid CRS</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OUTPUT_TYPE__GRID_CRS = eINSTANCE.getOutputType_GridCRS();

        /**
         * The meta object literal for the '<em><b>Format</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute OUTPUT_TYPE__FORMAT = eINSTANCE.getOutputType_Format();

        /**
         * The meta object literal for the '<em><b>Store</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute OUTPUT_TYPE__STORE = eINSTANCE.getOutputType_Store();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.RangeSubsetTypeImpl <em>Range Subset Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.RangeSubsetTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getRangeSubsetType()
         * @generated
         */
        EClass RANGE_SUBSET_TYPE = eINSTANCE.getRangeSubsetType();

        /**
         * The meta object literal for the '<em><b>Field Subset</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RANGE_SUBSET_TYPE__FIELD_SUBSET = eINSTANCE.getRangeSubsetType_FieldSubset();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.RangeTypeImpl <em>Range Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.RangeTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getRangeType()
         * @generated
         */
        EClass RANGE_TYPE = eINSTANCE.getRangeType();

        /**
         * The meta object literal for the '<em><b>Field</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RANGE_TYPE__FIELD = eINSTANCE.getRangeType_Field();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.RequestBaseTypeImpl <em>Request Base Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.RequestBaseTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getRequestBaseType()
         * @generated
         */
        EClass REQUEST_BASE_TYPE = eINSTANCE.getRequestBaseType();

        /**
         * The meta object literal for the '<em><b>Service</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REQUEST_BASE_TYPE__SERVICE = eINSTANCE.getRequestBaseType_Service();

        /**
         * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute REQUEST_BASE_TYPE__VERSION = eINSTANCE.getRequestBaseType_Version();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.SpatialDomainTypeImpl <em>Spatial Domain Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.SpatialDomainTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getSpatialDomainType()
         * @generated
         */
        EClass SPATIAL_DOMAIN_TYPE = eINSTANCE.getSpatialDomainType();

        /**
         * The meta object literal for the '<em><b>Bounding Box</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SPATIAL_DOMAIN_TYPE__BOUNDING_BOX = eINSTANCE.getSpatialDomainType_BoundingBox();

        /**
         * The meta object literal for the '<em><b>Grid CRS</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SPATIAL_DOMAIN_TYPE__GRID_CRS = eINSTANCE.getSpatialDomainType_GridCRS();

        /**
         * The meta object literal for the '<em><b>Transformation</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SPATIAL_DOMAIN_TYPE__TRANSFORMATION = eINSTANCE.getSpatialDomainType_Transformation();

        /**
         * The meta object literal for the '<em><b>Image CRS</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SPATIAL_DOMAIN_TYPE__IMAGE_CRS = eINSTANCE.getSpatialDomainType_ImageCRS();

        /**
         * The meta object literal for the '<em><b>Polygon</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SPATIAL_DOMAIN_TYPE__POLYGON = eINSTANCE.getSpatialDomainType_Polygon();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.TimePeriodTypeImpl <em>Time Period Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.TimePeriodTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getTimePeriodType()
         * @generated
         */
        EClass TIME_PERIOD_TYPE = eINSTANCE.getTimePeriodType();

        /**
         * The meta object literal for the '<em><b>Begin Position</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TIME_PERIOD_TYPE__BEGIN_POSITION = eINSTANCE.getTimePeriodType_BeginPosition();

        /**
         * The meta object literal for the '<em><b>End Position</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TIME_PERIOD_TYPE__END_POSITION = eINSTANCE.getTimePeriodType_EndPosition();

        /**
         * The meta object literal for the '<em><b>Time Resolution</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TIME_PERIOD_TYPE__TIME_RESOLUTION = eINSTANCE.getTimePeriodType_TimeResolution();

        /**
         * The meta object literal for the '<em><b>Frame</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TIME_PERIOD_TYPE__FRAME = eINSTANCE.getTimePeriodType_Frame();

        /**
         * The meta object literal for the '{@link net.opengis.wcs.impl.TimeSequenceTypeImpl <em>Time Sequence Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see net.opengis.wcs.impl.TimeSequenceTypeImpl
         * @see net.opengis.wcs.impl.wcsPackageImpl#getTimeSequenceType()
         * @generated
         */
        EClass TIME_SEQUENCE_TYPE = eINSTANCE.getTimeSequenceType();

        /**
         * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TIME_SEQUENCE_TYPE__GROUP = eINSTANCE.getTimeSequenceType_Group();

        /**
         * The meta object literal for the '<em><b>Time Position</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TIME_SEQUENCE_TYPE__TIME_POSITION = eINSTANCE.getTimeSequenceType_TimePosition();

        /**
         * The meta object literal for the '<em><b>Time Period</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TIME_SEQUENCE_TYPE__TIME_PERIOD = eINSTANCE.getTimeSequenceType_TimePeriod();

        /**
         * The meta object literal for the '<em>Identifier Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.String
         * @see net.opengis.wcs.impl.wcsPackageImpl#getIdentifierType()
         * @generated
         */
        EDataType IDENTIFIER_TYPE = eINSTANCE.getIdentifierType();

        /**
         * The meta object literal for the '<em>Time Duration Type</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.lang.Object
         * @see net.opengis.wcs.impl.wcsPackageImpl#getTimeDurationType()
         * @generated
         */
        EDataType TIME_DURATION_TYPE = eINSTANCE.getTimeDurationType();

    }

} //wcsPackage
