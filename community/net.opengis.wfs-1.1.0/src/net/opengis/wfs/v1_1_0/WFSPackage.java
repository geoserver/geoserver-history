/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.v1_1_0;

import net.opengis.ows.v1_0_0.OWSPackage;

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
 * This XML Schema Document defines the GetCapabilities operation request and response XML elements and types, which are common to all OWSs. This XML Schema shall be edited by each OWS, for example, to specify a specific value for the "service" attribute.
 * 		Copyright © 2005 Open Geospatial Consortium, Inc. All Rights Reserved. 
 * This XML Schema Document encodes the common "ServiceIdentification" section of the GetCapabilities operation response, known as the Capabilities XML document. This section encodes the SV_ServiceIdentification class of ISO 19119 (OGC Abstract Specification Topic 12).
 * 		Copyright © 2005 Open Geospatial Consortium, Inc. All Rights Reserved. 
 * This XML Schema Document encodes the parts of the MD_DataIdentification class of ISO 19115 (OGC Abstract Specification Topic 11) which are expected to be used for most datasets. This Schema also encodes the parts of this class that are expected to be useful for other metadata. Both are expected to be used within the Contents section of OWS service metadata (Capabilities) documents.
 * 		Copyright © 2005 Open Geospatial Consortium, Inc. All Rights Reserved. 
 * This XML Schema Document encodes various parameters and parameter types that can be used in OWS operation requests and responses.
 * 		Copyright © 2005 Open Geospatial Consortium, Inc. All Rights Reserved. 
 * This XML Schema Document encodes the parts of ISO 19115 used by the common "ServiceIdentification" and "ServiceProvider" sections of the GetCapabilities operation response, known as the service metadata XML document. The parts encoded here are the MD_Keywords, CI_ResponsibleParty, and related classes. This XML Schema largely follows the current draft for ISO 19139, with the addition of documentation text extracted and edited from Annex B of ISO 19115. The UML package prefixes were omitted from XML names, and the XML element names were all capitalized, for consistency with other OWS Schemas. Also, the optional smXML:id attributes were omitted, as not being useful in a service metadata document.
 * 		Copyright © 2005 Open Geospatial Consortium, Inc. All Rights Reserved. 
 * This XML Schema Document encodes the common "ServiceProvider" section of the GetCapabilities operation response, known as the Capabilities XML document. This section encodes the SV_ServiceProvider class of ISO 19119 (OGC Abstract Specification Topic 12).
 * 		Copyright © 2005 Open Geospatial Consortium, Inc. All Rights Reserved. 
 * This XML Schema Document encodes the basic contents of the "OperationsMetadata" section of the GetCapabilities operation response, also known as the Capabilities XML document.
 * 		Copyright © 2005 Open Geospatial Consortium, Inc. All Rights Reserved. 
 * <!-- end-model-doc -->
 * @see net.opengis.wfs.v1_1_0.WFSFactory
 * @model kind="package"
 * @generated
 */
public interface WFSPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "wfs";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.opengis.net/wfs";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "wfs";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	WFSPackage eINSTANCE = net.opengis.wfs.v1_1_0.impl.WFSPackageImpl.init();

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.ActionTypeImpl <em>Action Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.ActionTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getActionType()
	 * @generated
	 */
	int ACTION_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Message</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION_TYPE__MESSAGE = 0;

	/**
	 * The feature id for the '<em><b>Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION_TYPE__CODE = 1;

	/**
	 * The feature id for the '<em><b>Locator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION_TYPE__LOCATOR = 2;

	/**
	 * The number of structural features of the the '<em>Action Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.BaseRequestTypeImpl <em>Base Request Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.BaseRequestTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getBaseRequestType()
	 * @generated
	 */
	int BASE_REQUEST_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_REQUEST_TYPE__HANDLE = 0;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_REQUEST_TYPE__SERVICE = 1;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_REQUEST_TYPE__VERSION = 2;

	/**
	 * The number of structural features of the the '<em>Base Request Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_REQUEST_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.DeleteElementTypeImpl <em>Delete Element Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.DeleteElementTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getDeleteElementType()
	 * @generated
	 */
	int DELETE_ELEMENT_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Filter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETE_ELEMENT_TYPE__FILTER = 0;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETE_ELEMENT_TYPE__HANDLE = 1;

	/**
	 * The feature id for the '<em><b>Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETE_ELEMENT_TYPE__TYPE_NAME = 2;

	/**
	 * The number of structural features of the the '<em>Delete Element Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETE_ELEMENT_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.DescribeFeatureTypeTypeImpl <em>Describe Feature Type Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.DescribeFeatureTypeTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getDescribeFeatureTypeType()
	 * @generated
	 */
	int DESCRIBE_FEATURE_TYPE_TYPE = 3;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_FEATURE_TYPE_TYPE__HANDLE = BASE_REQUEST_TYPE__HANDLE;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_FEATURE_TYPE_TYPE__SERVICE = BASE_REQUEST_TYPE__SERVICE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_FEATURE_TYPE_TYPE__VERSION = BASE_REQUEST_TYPE__VERSION;

	/**
	 * The feature id for the '<em><b>Type Name</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_FEATURE_TYPE_TYPE__TYPE_NAME = BASE_REQUEST_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Output Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_FEATURE_TYPE_TYPE__OUTPUT_FORMAT = BASE_REQUEST_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the the '<em>Describe Feature Type Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_FEATURE_TYPE_TYPE_FEATURE_COUNT = BASE_REQUEST_TYPE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.DocumentRootImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getDocumentRoot()
	 * @generated
	 */
	int DOCUMENT_ROOT = 4;

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
	 * The feature id for the '<em><b>Delete</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__DELETE = 3;

	/**
	 * The feature id for the '<em><b>Describe Feature Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__DESCRIBE_FEATURE_TYPE = 4;

	/**
	 * The feature id for the '<em><b>Feature Collection</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__FEATURE_COLLECTION = 5;

	/**
	 * The feature id for the '<em><b>Feature Type List</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__FEATURE_TYPE_LIST = 6;

	/**
	 * The feature id for the '<em><b>Get Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GET_CAPABILITIES = 7;

	/**
	 * The feature id for the '<em><b>Get Feature</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GET_FEATURE = 8;

	/**
	 * The feature id for the '<em><b>Get Feature With Lock</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GET_FEATURE_WITH_LOCK = 9;

	/**
	 * The feature id for the '<em><b>Get Gml Object</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GET_GML_OBJECT = 10;

	/**
	 * The feature id for the '<em><b>Insert</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__INSERT = 11;

	/**
	 * The feature id for the '<em><b>Lock Feature</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__LOCK_FEATURE = 12;

	/**
	 * The feature id for the '<em><b>Lock Feature Response</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__LOCK_FEATURE_RESPONSE = 13;

	/**
	 * The feature id for the '<em><b>Lock Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__LOCK_ID = 14;

	/**
	 * The feature id for the '<em><b>Native</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__NATIVE = 15;

	/**
	 * The feature id for the '<em><b>Property</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__PROPERTY = 16;

	/**
	 * The feature id for the '<em><b>Propery Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__PROPERY_NAME = 17;

	/**
	 * The feature id for the '<em><b>Query</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__QUERY = 18;

	/**
	 * The feature id for the '<em><b>Serves GML Object Type List</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__SERVES_GML_OBJECT_TYPE_LIST = 19;

	/**
	 * The feature id for the '<em><b>Supports GML Object Type List</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__SUPPORTS_GML_OBJECT_TYPE_LIST = 20;

	/**
	 * The feature id for the '<em><b>Transaction</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__TRANSACTION = 21;

	/**
	 * The feature id for the '<em><b>Transaction Response</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__TRANSACTION_RESPONSE = 22;

	/**
	 * The feature id for the '<em><b>Update</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__UPDATE = 23;

	/**
	 * The feature id for the '<em><b>Wfs Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__WFS_CAPABILITIES = 24;

	/**
	 * The feature id for the '<em><b>Xlink Property Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__XLINK_PROPERTY_NAME = 25;

	/**
	 * The feature id for the '<em><b>Property Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__PROPERTY_NAME = 26;

	/**
	 * The number of structural features of the the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT_FEATURE_COUNT = 27;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.FeatureCollectionTypeImpl <em>Feature Collection Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.FeatureCollectionTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getFeatureCollectionType()
	 * @generated
	 */
	int FEATURE_COLLECTION_TYPE = 5;

	/**
	 * The number of structural features of the the '<em>Feature Collection Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_COLLECTION_TYPE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.FeaturesLockedTypeImpl <em>Features Locked Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.FeaturesLockedTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getFeaturesLockedType()
	 * @generated
	 */
	int FEATURES_LOCKED_TYPE = 6;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_LOCKED_TYPE__GROUP = 0;

	/**
	 * The feature id for the '<em><b>Feature Id</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_LOCKED_TYPE__FEATURE_ID = 1;

	/**
	 * The number of structural features of the the '<em>Features Locked Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_LOCKED_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.FeaturesNotLockedTypeImpl <em>Features Not Locked Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.FeaturesNotLockedTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getFeaturesNotLockedType()
	 * @generated
	 */
	int FEATURES_NOT_LOCKED_TYPE = 7;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_NOT_LOCKED_TYPE__GROUP = 0;

	/**
	 * The feature id for the '<em><b>Feature Id</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_NOT_LOCKED_TYPE__FEATURE_ID = 1;

	/**
	 * The number of structural features of the the '<em>Features Not Locked Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_NOT_LOCKED_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.FeatureTypeListTypeImpl <em>Feature Type List Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.FeatureTypeListTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getFeatureTypeListType()
	 * @generated
	 */
	int FEATURE_TYPE_LIST_TYPE = 8;

	/**
	 * The feature id for the '<em><b>Operations</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_TYPE_LIST_TYPE__OPERATIONS = 0;

	/**
	 * The feature id for the '<em><b>Feature Type</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_TYPE_LIST_TYPE__FEATURE_TYPE = 1;

	/**
	 * The number of structural features of the the '<em>Feature Type List Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_TYPE_LIST_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.FeatureTypeTypeImpl <em>Feature Type Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.FeatureTypeTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getFeatureTypeType()
	 * @generated
	 */
	int FEATURE_TYPE_TYPE = 9;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_TYPE_TYPE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_TYPE_TYPE__TITLE = 1;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_TYPE_TYPE__ABSTRACT = 2;

	/**
	 * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_TYPE_TYPE__KEYWORDS = 3;

	/**
	 * The feature id for the '<em><b>Default SRS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_TYPE_TYPE__DEFAULT_SRS = 4;

	/**
	 * The feature id for the '<em><b>Other SRS</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_TYPE_TYPE__OTHER_SRS = 5;

	/**
	 * The feature id for the '<em><b>No SRS</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_TYPE_TYPE__NO_SRS = 6;

	/**
	 * The feature id for the '<em><b>Operations</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_TYPE_TYPE__OPERATIONS = 7;

	/**
	 * The feature id for the '<em><b>Output Formats</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_TYPE_TYPE__OUTPUT_FORMATS = 8;

	/**
	 * The feature id for the '<em><b>WGS84 Bounding Box</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX = 9;

	/**
	 * The feature id for the '<em><b>Metadata URL</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_TYPE_TYPE__METADATA_URL = 10;

	/**
	 * The number of structural features of the the '<em>Feature Type Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_TYPE_TYPE_FEATURE_COUNT = 11;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.GetCapabilitiesTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getGetCapabilitiesType()
	 * @generated
	 */
	int GET_CAPABILITIES_TYPE = 10;

	/**
	 * The feature id for the '<em><b>Accept Versions</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS = OWSPackage.GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS;

	/**
	 * The feature id for the '<em><b>Sections</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_CAPABILITIES_TYPE__SECTIONS = OWSPackage.GET_CAPABILITIES_TYPE__SECTIONS;

	/**
	 * The feature id for the '<em><b>Accept Formats</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_CAPABILITIES_TYPE__ACCEPT_FORMATS = OWSPackage.GET_CAPABILITIES_TYPE__ACCEPT_FORMATS;

	/**
	 * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE = OWSPackage.GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_CAPABILITIES_TYPE__SERVICE = OWSPackage.GET_CAPABILITIES_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Get Capabilities Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_CAPABILITIES_TYPE_FEATURE_COUNT = OWSPackage.GET_CAPABILITIES_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.GetFeatureTypeImpl <em>Get Feature Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.GetFeatureTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getGetFeatureType()
	 * @generated
	 */
	int GET_FEATURE_TYPE = 11;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_TYPE__HANDLE = BASE_REQUEST_TYPE__HANDLE;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_TYPE__SERVICE = BASE_REQUEST_TYPE__SERVICE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_TYPE__VERSION = BASE_REQUEST_TYPE__VERSION;

	/**
	 * The feature id for the '<em><b>Query</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_TYPE__QUERY = BASE_REQUEST_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Max Features</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_TYPE__MAX_FEATURES = BASE_REQUEST_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Output Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_TYPE__OUTPUT_FORMAT = BASE_REQUEST_TYPE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Result Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_TYPE__RESULT_TYPE = BASE_REQUEST_TYPE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Traverse Xlink Depth</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_TYPE__TRAVERSE_XLINK_DEPTH = BASE_REQUEST_TYPE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Traverse Xlink Expiry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_TYPE__TRAVERSE_XLINK_EXPIRY = BASE_REQUEST_TYPE_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the the '<em>Get Feature Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_TYPE_FEATURE_COUNT = BASE_REQUEST_TYPE_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.GetFeatureWithLockTypeImpl <em>Get Feature With Lock Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.GetFeatureWithLockTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getGetFeatureWithLockType()
	 * @generated
	 */
	int GET_FEATURE_WITH_LOCK_TYPE = 12;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE__HANDLE = BASE_REQUEST_TYPE__HANDLE;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE__SERVICE = BASE_REQUEST_TYPE__SERVICE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE__VERSION = BASE_REQUEST_TYPE__VERSION;

	/**
	 * The feature id for the '<em><b>Query</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE__QUERY = BASE_REQUEST_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Expiry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE__EXPIRY = BASE_REQUEST_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Max Features</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE__MAX_FEATURES = BASE_REQUEST_TYPE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Output Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE__OUTPUT_FORMAT = BASE_REQUEST_TYPE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Result Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE__RESULT_TYPE = BASE_REQUEST_TYPE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Traverse Xlink Depth</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE__TRAVERSE_XLINK_DEPTH = BASE_REQUEST_TYPE_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Traverse Xlink Expiry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE__TRAVERSE_XLINK_EXPIRY = BASE_REQUEST_TYPE_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the the '<em>Get Feature With Lock Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE_FEATURE_COUNT = BASE_REQUEST_TYPE_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.GetGmlObjectTypeImpl <em>Get Gml Object Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.GetGmlObjectTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getGetGmlObjectType()
	 * @generated
	 */
	int GET_GML_OBJECT_TYPE = 13;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_GML_OBJECT_TYPE__HANDLE = BASE_REQUEST_TYPE__HANDLE;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_GML_OBJECT_TYPE__SERVICE = BASE_REQUEST_TYPE__SERVICE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_GML_OBJECT_TYPE__VERSION = BASE_REQUEST_TYPE__VERSION;

	/**
	 * The feature id for the '<em><b>Gml Object Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_GML_OBJECT_TYPE__GML_OBJECT_ID = BASE_REQUEST_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Output Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_GML_OBJECT_TYPE__OUTPUT_FORMAT = BASE_REQUEST_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Traverse Xlink Depth</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_GML_OBJECT_TYPE__TRAVERSE_XLINK_DEPTH = BASE_REQUEST_TYPE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Traverse Xlink Expiry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_GML_OBJECT_TYPE__TRAVERSE_XLINK_EXPIRY = BASE_REQUEST_TYPE_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the the '<em>Get Gml Object Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_GML_OBJECT_TYPE_FEATURE_COUNT = BASE_REQUEST_TYPE_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.GMLObjectTypeListTypeImpl <em>GML Object Type List Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.GMLObjectTypeListTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getGMLObjectTypeListType()
	 * @generated
	 */
	int GML_OBJECT_TYPE_LIST_TYPE = 14;

	/**
	 * The feature id for the '<em><b>GML Object Type</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GML_OBJECT_TYPE_LIST_TYPE__GML_OBJECT_TYPE = 0;

	/**
	 * The number of structural features of the the '<em>GML Object Type List Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GML_OBJECT_TYPE_LIST_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.GMLObjectTypeTypeImpl <em>GML Object Type Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.GMLObjectTypeTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getGMLObjectTypeType()
	 * @generated
	 */
	int GML_OBJECT_TYPE_TYPE = 15;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GML_OBJECT_TYPE_TYPE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GML_OBJECT_TYPE_TYPE__TITLE = 1;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GML_OBJECT_TYPE_TYPE__ABSTRACT = 2;

	/**
	 * The feature id for the '<em><b>Keywords</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GML_OBJECT_TYPE_TYPE__KEYWORDS = 3;

	/**
	 * The feature id for the '<em><b>Output Formats</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS = 4;

	/**
	 * The number of structural features of the the '<em>GML Object Type Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GML_OBJECT_TYPE_TYPE_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.InsertedFeatureTypeImpl <em>Inserted Feature Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.InsertedFeatureTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getInsertedFeatureType()
	 * @generated
	 */
	int INSERTED_FEATURE_TYPE = 16;

	/**
	 * The feature id for the '<em><b>Feature Id</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTED_FEATURE_TYPE__FEATURE_ID = 0;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTED_FEATURE_TYPE__HANDLE = 1;

	/**
	 * The number of structural features of the the '<em>Inserted Feature Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTED_FEATURE_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.InsertElementTypeImpl <em>Insert Element Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.InsertElementTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getInsertElementType()
	 * @generated
	 */
	int INSERT_ELEMENT_TYPE = 17;

	/**
	 * The feature id for the '<em><b>Feature Collection</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERT_ELEMENT_TYPE__FEATURE_COLLECTION = 0;

	/**
	 * The feature id for the '<em><b>Feature</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERT_ELEMENT_TYPE__FEATURE = 1;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERT_ELEMENT_TYPE__HANDLE = 2;

	/**
	 * The feature id for the '<em><b>Idgen</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERT_ELEMENT_TYPE__IDGEN = 3;

	/**
	 * The feature id for the '<em><b>Input Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERT_ELEMENT_TYPE__INPUT_FORMAT = 4;

	/**
	 * The feature id for the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERT_ELEMENT_TYPE__SRS_NAME = 5;

	/**
	 * The number of structural features of the the '<em>Insert Element Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERT_ELEMENT_TYPE_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.InsertResultTypeImpl <em>Insert Result Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.InsertResultTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getInsertResultType()
	 * @generated
	 */
	int INSERT_RESULT_TYPE = 18;

	/**
	 * The feature id for the '<em><b>Feature</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERT_RESULT_TYPE__FEATURE = 0;

	/**
	 * The number of structural features of the the '<em>Insert Result Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERT_RESULT_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.LockFeatureResponseTypeImpl <em>Lock Feature Response Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.LockFeatureResponseTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getLockFeatureResponseType()
	 * @generated
	 */
	int LOCK_FEATURE_RESPONSE_TYPE = 19;

	/**
	 * The feature id for the '<em><b>Lock Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_FEATURE_RESPONSE_TYPE__LOCK_ID = 0;

	/**
	 * The feature id for the '<em><b>Features Locked</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED = 1;

	/**
	 * The feature id for the '<em><b>Features Not Locked</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED = 2;

	/**
	 * The number of structural features of the the '<em>Lock Feature Response Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_FEATURE_RESPONSE_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.LockFeatureTypeImpl <em>Lock Feature Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.LockFeatureTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getLockFeatureType()
	 * @generated
	 */
	int LOCK_FEATURE_TYPE = 20;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_FEATURE_TYPE__HANDLE = BASE_REQUEST_TYPE__HANDLE;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_FEATURE_TYPE__SERVICE = BASE_REQUEST_TYPE__SERVICE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_FEATURE_TYPE__VERSION = BASE_REQUEST_TYPE__VERSION;

	/**
	 * The feature id for the '<em><b>Lock</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_FEATURE_TYPE__LOCK = BASE_REQUEST_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Expiry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_FEATURE_TYPE__EXPIRY = BASE_REQUEST_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Lock Action</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_FEATURE_TYPE__LOCK_ACTION = BASE_REQUEST_TYPE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the the '<em>Lock Feature Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_FEATURE_TYPE_FEATURE_COUNT = BASE_REQUEST_TYPE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.LockTypeImpl <em>Lock Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.LockTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getLockType()
	 * @generated
	 */
	int LOCK_TYPE = 21;

	/**
	 * The feature id for the '<em><b>Filter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_TYPE__FILTER = 0;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_TYPE__HANDLE = 1;

	/**
	 * The feature id for the '<em><b>Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_TYPE__TYPE_NAME = 2;

	/**
	 * The number of structural features of the the '<em>Lock Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.MetadataURLTypeImpl <em>Metadata URL Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.MetadataURLTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getMetadataURLType()
	 * @generated
	 */
	int METADATA_URL_TYPE = 22;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA_URL_TYPE__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA_URL_TYPE__FORMAT = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA_URL_TYPE__TYPE = 2;

	/**
	 * The number of structural features of the the '<em>Metadata URL Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA_URL_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.NativeTypeImpl <em>Native Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.NativeTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getNativeType()
	 * @generated
	 */
	int NATIVE_TYPE = 23;

	/**
	 * The feature id for the '<em><b>Safe To Ignore</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NATIVE_TYPE__SAFE_TO_IGNORE = 0;

	/**
	 * The feature id for the '<em><b>Vendor Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NATIVE_TYPE__VENDOR_ID = 1;

	/**
	 * The number of structural features of the the '<em>Native Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NATIVE_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.NoSRSTypeImpl <em>No SRS Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.NoSRSTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getNoSRSType()
	 * @generated
	 */
	int NO_SRS_TYPE = 24;

	/**
	 * The number of structural features of the the '<em>No SRS Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NO_SRS_TYPE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.OperationsTypeImpl <em>Operations Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.OperationsTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getOperationsType()
	 * @generated
	 */
	int OPERATIONS_TYPE = 25;

	/**
	 * The feature id for the '<em><b>Operation</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATIONS_TYPE__OPERATION = 0;

	/**
	 * The number of structural features of the the '<em>Operations Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATIONS_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.OutputFormatListTypeImpl <em>Output Format List Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.OutputFormatListTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getOutputFormatListType()
	 * @generated
	 */
	int OUTPUT_FORMAT_LIST_TYPE = 26;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_FORMAT_LIST_TYPE__GROUP = 0;

	/**
	 * The feature id for the '<em><b>Format</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_FORMAT_LIST_TYPE__FORMAT = 1;

	/**
	 * The number of structural features of the the '<em>Output Format List Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_FORMAT_LIST_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.PropertyTypeImpl <em>Property Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.PropertyTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getPropertyType()
	 * @generated
	 */
	int PROPERTY_TYPE = 27;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_TYPE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_TYPE__VALUE = 1;

	/**
	 * The number of structural features of the the '<em>Property Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.QueryTypeImpl <em>Query Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.QueryTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getQueryType()
	 * @generated
	 */
	int QUERY_TYPE = 28;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TYPE__GROUP = 0;

	/**
	 * The feature id for the '<em><b>Property Name</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TYPE__PROPERTY_NAME = 1;

	/**
	 * The feature id for the '<em><b>Function</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TYPE__FUNCTION = 2;

	/**
	 * The feature id for the '<em><b>Filter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TYPE__FILTER = 3;

	/**
	 * The feature id for the '<em><b>Sort By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TYPE__SORT_BY = 4;

	/**
	 * The feature id for the '<em><b>Feature Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TYPE__FEATURE_VERSION = 5;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TYPE__HANDLE = 6;

	/**
	 * The feature id for the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TYPE__SRS_NAME = 7;

	/**
	 * The feature id for the '<em><b>Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TYPE__TYPE_NAME = 8;

	/**
	 * The number of structural features of the the '<em>Query Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TYPE_FEATURE_COUNT = 9;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.TransactionResponseTypeImpl <em>Transaction Response Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.TransactionResponseTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getTransactionResponseType()
	 * @generated
	 */
	int TRANSACTION_RESPONSE_TYPE = 29;

	/**
	 * The feature id for the '<em><b>Transaction Summary</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_RESPONSE_TYPE__TRANSACTION_SUMMARY = 0;

	/**
	 * The feature id for the '<em><b>Transaction Results</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_RESPONSE_TYPE__TRANSACTION_RESULTS = 1;

	/**
	 * The feature id for the '<em><b>Insert Results</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_RESPONSE_TYPE__INSERT_RESULTS = 2;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_RESPONSE_TYPE__VERSION = 3;

	/**
	 * The number of structural features of the the '<em>Transaction Response Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_RESPONSE_TYPE_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.TransactionResultsTypeImpl <em>Transaction Results Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.TransactionResultsTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getTransactionResultsType()
	 * @generated
	 */
	int TRANSACTION_RESULTS_TYPE = 30;

	/**
	 * The feature id for the '<em><b>Action</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_RESULTS_TYPE__ACTION = 0;

	/**
	 * The number of structural features of the the '<em>Transaction Results Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_RESULTS_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.TransactionSummaryTypeImpl <em>Transaction Summary Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.TransactionSummaryTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getTransactionSummaryType()
	 * @generated
	 */
	int TRANSACTION_SUMMARY_TYPE = 31;

	/**
	 * The feature id for the '<em><b>Total Inserted</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED = 0;

	/**
	 * The feature id for the '<em><b>Total Updated</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED = 1;

	/**
	 * The feature id for the '<em><b>Total Deleted</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED = 2;

	/**
	 * The number of structural features of the the '<em>Transaction Summary Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_SUMMARY_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.TransactionTypeImpl <em>Transaction Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.TransactionTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getTransactionType()
	 * @generated
	 */
	int TRANSACTION_TYPE = 32;

	/**
	 * The feature id for the '<em><b>Accept Versions</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE__ACCEPT_VERSIONS = OWSPackage.GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS;

	/**
	 * The feature id for the '<em><b>Sections</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE__SECTIONS = OWSPackage.GET_CAPABILITIES_TYPE__SECTIONS;

	/**
	 * The feature id for the '<em><b>Accept Formats</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE__ACCEPT_FORMATS = OWSPackage.GET_CAPABILITIES_TYPE__ACCEPT_FORMATS;

	/**
	 * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE__UPDATE_SEQUENCE = OWSPackage.GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE;

	/**
	 * The feature id for the '<em><b>Lock Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE__LOCK_ID = OWSPackage.GET_CAPABILITIES_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE__GROUP = OWSPackage.GET_CAPABILITIES_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Insert</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE__INSERT = OWSPackage.GET_CAPABILITIES_TYPE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Update</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE__UPDATE = OWSPackage.GET_CAPABILITIES_TYPE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Delete</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE__DELETE = OWSPackage.GET_CAPABILITIES_TYPE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Native</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE__NATIVE = OWSPackage.GET_CAPABILITIES_TYPE_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Release Action</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE__RELEASE_ACTION = OWSPackage.GET_CAPABILITIES_TYPE_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the the '<em>Transaction Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE_FEATURE_COUNT = OWSPackage.GET_CAPABILITIES_TYPE_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.UpdateElementTypeImpl <em>Update Element Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.UpdateElementTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getUpdateElementType()
	 * @generated
	 */
	int UPDATE_ELEMENT_TYPE = 33;

	/**
	 * The feature id for the '<em><b>Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ELEMENT_TYPE__PROPERTY = 0;

	/**
	 * The feature id for the '<em><b>Filter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ELEMENT_TYPE__FILTER = 1;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ELEMENT_TYPE__HANDLE = 2;

	/**
	 * The feature id for the '<em><b>Input Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ELEMENT_TYPE__INPUT_FORMAT = 3;

	/**
	 * The feature id for the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ELEMENT_TYPE__SRS_NAME = 4;

	/**
	 * The feature id for the '<em><b>Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ELEMENT_TYPE__TYPE_NAME = 5;

	/**
	 * The number of structural features of the the '<em>Update Element Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ELEMENT_TYPE_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.impl.WFSCapabilitiesTypeImpl <em>Capabilities Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.impl.WFSCapabilitiesTypeImpl
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getWFSCapabilitiesType()
	 * @generated
	 */
	int WFS_CAPABILITIES_TYPE = 34;

	/**
	 * The feature id for the '<em><b>Service Identification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WFS_CAPABILITIES_TYPE__SERVICE_IDENTIFICATION = OWSPackage.CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION;

	/**
	 * The feature id for the '<em><b>Service Provider</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WFS_CAPABILITIES_TYPE__SERVICE_PROVIDER = OWSPackage.CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER;

	/**
	 * The feature id for the '<em><b>Operations Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WFS_CAPABILITIES_TYPE__OPERATIONS_METADATA = OWSPackage.CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA;

	/**
	 * The feature id for the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WFS_CAPABILITIES_TYPE__UPDATE_SEQUENCE = OWSPackage.CAPABILITIES_BASE_TYPE__UPDATE_SEQUENCE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WFS_CAPABILITIES_TYPE__VERSION = OWSPackage.CAPABILITIES_BASE_TYPE__VERSION;

	/**
	 * The feature id for the '<em><b>Feature Type List</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST = OWSPackage.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Serves GML Object Type List</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WFS_CAPABILITIES_TYPE__SERVES_GML_OBJECT_TYPE_LIST = OWSPackage.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Supports GML Object Type List</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WFS_CAPABILITIES_TYPE__SUPPORTS_GML_OBJECT_TYPE_LIST = OWSPackage.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Filter Capabilities</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WFS_CAPABILITIES_TYPE__FILTER_CAPABILITIES = OWSPackage.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the the '<em>Capabilities Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WFS_CAPABILITIES_TYPE_FEATURE_COUNT = OWSPackage.CAPABILITIES_BASE_TYPE_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.AllSomeType <em>All Some Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.AllSomeType
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getAllSomeType()
	 * @generated
	 */
	int ALL_SOME_TYPE = 35;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.IdentifierGenerationOptionType <em>Identifier Generation Option Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.IdentifierGenerationOptionType
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getIdentifierGenerationOptionType()
	 * @generated
	 */
	int IDENTIFIER_GENERATION_OPTION_TYPE = 36;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.OperationType <em>Operation Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.OperationType
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getOperationType()
	 * @generated
	 */
	int OPERATION_TYPE = 37;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.v1_1_0.ResultTypeType <em>Result Type Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.ResultTypeType
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getResultTypeType()
	 * @generated
	 */
	int RESULT_TYPE_TYPE = 38;

	/**
	 * The meta object id for the '<em>All Some Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.AllSomeType
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getAllSomeTypeObject()
	 * @generated
	 */
	int ALL_SOME_TYPE_OBJECT = 39;

	/**
	 * The meta object id for the '<em>Base Type Name List Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.List
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getBaseTypeNameListType()
	 * @generated
	 */
	int BASE_TYPE_NAME_LIST_TYPE = 40;

	/**
	 * The meta object id for the '<em>Format Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.String
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getFormatType()
	 * @generated
	 */
	int FORMAT_TYPE = 41;

	/**
	 * The meta object id for the '<em>Identifier Generation Option Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.IdentifierGenerationOptionType
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getIdentifierGenerationOptionTypeObject()
	 * @generated
	 */
	int IDENTIFIER_GENERATION_OPTION_TYPE_OBJECT = 42;

	/**
	 * The meta object id for the '<em>Operation Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.OperationType
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getOperationTypeObject()
	 * @generated
	 */
	int OPERATION_TYPE_OBJECT = 43;

	/**
	 * The meta object id for the '<em>Result Type Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.v1_1_0.ResultTypeType
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getResultTypeTypeObject()
	 * @generated
	 */
	int RESULT_TYPE_TYPE_OBJECT = 44;

	/**
	 * The meta object id for the '<em>Type Name List Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.List
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getTypeNameListType()
	 * @generated
	 */
	int TYPE_NAME_LIST_TYPE = 45;

	/**
	 * The meta object id for the '<em>Type Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.String
	 * @see net.opengis.wfs.v1_1_0.impl.WFSPackageImpl#getTypeType()
	 * @generated
	 */
	int TYPE_TYPE = 46;


	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.ActionType <em>Action Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Action Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.ActionType
	 * @generated
	 */
	EClass getActionType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.ActionType#getMessage <em>Message</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Message</em>'.
	 * @see net.opengis.wfs.v1_1_0.ActionType#getMessage()
	 * @see #getActionType()
	 * @generated
	 */
	EAttribute getActionType_Message();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.ActionType#getCode <em>Code</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Code</em>'.
	 * @see net.opengis.wfs.v1_1_0.ActionType#getCode()
	 * @see #getActionType()
	 * @generated
	 */
	EAttribute getActionType_Code();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.ActionType#getLocator <em>Locator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Locator</em>'.
	 * @see net.opengis.wfs.v1_1_0.ActionType#getLocator()
	 * @see #getActionType()
	 * @generated
	 */
	EAttribute getActionType_Locator();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.BaseRequestType <em>Base Request Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Base Request Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.BaseRequestType
	 * @generated
	 */
	EClass getBaseRequestType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.BaseRequestType#getHandle <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle</em>'.
	 * @see net.opengis.wfs.v1_1_0.BaseRequestType#getHandle()
	 * @see #getBaseRequestType()
	 * @generated
	 */
	EAttribute getBaseRequestType_Handle();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.BaseRequestType#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service</em>'.
	 * @see net.opengis.wfs.v1_1_0.BaseRequestType#getService()
	 * @see #getBaseRequestType()
	 * @generated
	 */
	EAttribute getBaseRequestType_Service();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.BaseRequestType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wfs.v1_1_0.BaseRequestType#getVersion()
	 * @see #getBaseRequestType()
	 * @generated
	 */
	EAttribute getBaseRequestType_Version();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.DeleteElementType <em>Delete Element Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Delete Element Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.DeleteElementType
	 * @generated
	 */
	EClass getDeleteElementType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.DeleteElementType#getFilter <em>Filter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Filter</em>'.
	 * @see net.opengis.wfs.v1_1_0.DeleteElementType#getFilter()
	 * @see #getDeleteElementType()
	 * @generated
	 */
	EAttribute getDeleteElementType_Filter();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.DeleteElementType#getHandle <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle</em>'.
	 * @see net.opengis.wfs.v1_1_0.DeleteElementType#getHandle()
	 * @see #getDeleteElementType()
	 * @generated
	 */
	EAttribute getDeleteElementType_Handle();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.DeleteElementType#getTypeName <em>Type Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type Name</em>'.
	 * @see net.opengis.wfs.v1_1_0.DeleteElementType#getTypeName()
	 * @see #getDeleteElementType()
	 * @generated
	 */
	EAttribute getDeleteElementType_TypeName();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.DescribeFeatureTypeType <em>Describe Feature Type Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Describe Feature Type Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.DescribeFeatureTypeType
	 * @generated
	 */
	EClass getDescribeFeatureTypeType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.v1_1_0.DescribeFeatureTypeType#getTypeName <em>Type Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Type Name</em>'.
	 * @see net.opengis.wfs.v1_1_0.DescribeFeatureTypeType#getTypeName()
	 * @see #getDescribeFeatureTypeType()
	 * @generated
	 */
	EAttribute getDescribeFeatureTypeType_TypeName();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.DescribeFeatureTypeType#getOutputFormat <em>Output Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Output Format</em>'.
	 * @see net.opengis.wfs.v1_1_0.DescribeFeatureTypeType#getOutputFormat()
	 * @see #getDescribeFeatureTypeType()
	 * @generated
	 */
	EAttribute getDescribeFeatureTypeType_OutputFormat();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot
	 * @generated
	 */
	EClass getDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getDelete <em>Delete</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Delete</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getDelete()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Delete();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getDescribeFeatureType <em>Describe Feature Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Describe Feature Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getDescribeFeatureType()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_DescribeFeatureType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getFeatureCollection <em>Feature Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Feature Collection</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getFeatureCollection()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_FeatureCollection();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getFeatureTypeList <em>Feature Type List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Feature Type List</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getFeatureTypeList()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_FeatureTypeList();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Get Capabilities</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getGetCapabilities()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_GetCapabilities();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getGetFeature <em>Get Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Get Feature</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getGetFeature()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_GetFeature();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getGetFeatureWithLock <em>Get Feature With Lock</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Get Feature With Lock</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getGetFeatureWithLock()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_GetFeatureWithLock();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getGetGmlObject <em>Get Gml Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Get Gml Object</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getGetGmlObject()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_GetGmlObject();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getInsert <em>Insert</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Insert</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getInsert()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Insert();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getLockFeature <em>Lock Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Lock Feature</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getLockFeature()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_LockFeature();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getLockFeatureResponse <em>Lock Feature Response</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Lock Feature Response</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getLockFeatureResponse()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_LockFeatureResponse();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getLockId <em>Lock Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lock Id</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getLockId()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_LockId();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getNative <em>Native</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Native</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getNative()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Native();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Property</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getProperty()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Property();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getProperyName <em>Propery Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Propery Name</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getProperyName()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_ProperyName();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getQuery <em>Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Query</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getQuery()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Query();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getServesGMLObjectTypeList <em>Serves GML Object Type List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Serves GML Object Type List</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getServesGMLObjectTypeList()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_ServesGMLObjectTypeList();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getSupportsGMLObjectTypeList <em>Supports GML Object Type List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Supports GML Object Type List</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getSupportsGMLObjectTypeList()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_SupportsGMLObjectTypeList();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getTransaction <em>Transaction</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Transaction</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getTransaction()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Transaction();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getTransactionResponse <em>Transaction Response</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Transaction Response</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getTransactionResponse()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_TransactionResponse();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getUpdate <em>Update</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Update</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getUpdate()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Update();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getWfsCapabilities <em>Wfs Capabilities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Wfs Capabilities</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getWfsCapabilities()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_WfsCapabilities();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getXlinkPropertyName <em>Xlink Property Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Xlink Property Name</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getXlinkPropertyName()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_XlinkPropertyName();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.DocumentRoot#getPropertyName <em>Property Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Property Name</em>'.
	 * @see net.opengis.wfs.v1_1_0.DocumentRoot#getPropertyName()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_PropertyName();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.FeatureCollectionType <em>Feature Collection Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Feature Collection Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeatureCollectionType
	 * @generated
	 */
	EClass getFeatureCollectionType();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.FeaturesLockedType <em>Features Locked Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Features Locked Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeaturesLockedType
	 * @generated
	 */
	EClass getFeaturesLockedType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.v1_1_0.FeaturesLockedType#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeaturesLockedType#getGroup()
	 * @see #getFeaturesLockedType()
	 * @generated
	 */
	EAttribute getFeaturesLockedType_Group();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.v1_1_0.FeaturesLockedType#getFeatureId <em>Feature Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Feature Id</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeaturesLockedType#getFeatureId()
	 * @see #getFeaturesLockedType()
	 * @generated
	 */
	EAttribute getFeaturesLockedType_FeatureId();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.FeaturesNotLockedType <em>Features Not Locked Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Features Not Locked Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeaturesNotLockedType
	 * @generated
	 */
	EClass getFeaturesNotLockedType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.v1_1_0.FeaturesNotLockedType#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeaturesNotLockedType#getGroup()
	 * @see #getFeaturesNotLockedType()
	 * @generated
	 */
	EAttribute getFeaturesNotLockedType_Group();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.v1_1_0.FeaturesNotLockedType#getFeatureId <em>Feature Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Feature Id</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeaturesNotLockedType#getFeatureId()
	 * @see #getFeaturesNotLockedType()
	 * @generated
	 */
	EAttribute getFeaturesNotLockedType_FeatureId();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.FeatureTypeListType <em>Feature Type List Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Feature Type List Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeatureTypeListType
	 * @generated
	 */
	EClass getFeatureTypeListType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.FeatureTypeListType#getOperations <em>Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Operations</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeatureTypeListType#getOperations()
	 * @see #getFeatureTypeListType()
	 * @generated
	 */
	EReference getFeatureTypeListType_Operations();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.v1_1_0.FeatureTypeListType#getFeatureType <em>Feature Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Feature Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeatureTypeListType#getFeatureType()
	 * @see #getFeatureTypeListType()
	 * @generated
	 */
	EReference getFeatureTypeListType_FeatureType();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.FeatureTypeType <em>Feature Type Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Feature Type Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeatureTypeType
	 * @generated
	 */
	EClass getFeatureTypeType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.FeatureTypeType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeatureTypeType#getName()
	 * @see #getFeatureTypeType()
	 * @generated
	 */
	EAttribute getFeatureTypeType_Name();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.FeatureTypeType#getTitle <em>Title</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Title</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeatureTypeType#getTitle()
	 * @see #getFeatureTypeType()
	 * @generated
	 */
	EAttribute getFeatureTypeType_Title();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.FeatureTypeType#getAbstract <em>Abstract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Abstract</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeatureTypeType#getAbstract()
	 * @see #getFeatureTypeType()
	 * @generated
	 */
	EAttribute getFeatureTypeType_Abstract();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.v1_1_0.FeatureTypeType#getKeywords <em>Keywords</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Keywords</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeatureTypeType#getKeywords()
	 * @see #getFeatureTypeType()
	 * @generated
	 */
	EReference getFeatureTypeType_Keywords();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.FeatureTypeType#getDefaultSRS <em>Default SRS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default SRS</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeatureTypeType#getDefaultSRS()
	 * @see #getFeatureTypeType()
	 * @generated
	 */
	EAttribute getFeatureTypeType_DefaultSRS();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.v1_1_0.FeatureTypeType#getOtherSRS <em>Other SRS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Other SRS</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeatureTypeType#getOtherSRS()
	 * @see #getFeatureTypeType()
	 * @generated
	 */
	EAttribute getFeatureTypeType_OtherSRS();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.FeatureTypeType#getNoSRS <em>No SRS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>No SRS</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeatureTypeType#getNoSRS()
	 * @see #getFeatureTypeType()
	 * @generated
	 */
	EReference getFeatureTypeType_NoSRS();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.FeatureTypeType#getOperations <em>Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Operations</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeatureTypeType#getOperations()
	 * @see #getFeatureTypeType()
	 * @generated
	 */
	EReference getFeatureTypeType_Operations();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.FeatureTypeType#getOutputFormats <em>Output Formats</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Output Formats</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeatureTypeType#getOutputFormats()
	 * @see #getFeatureTypeType()
	 * @generated
	 */
	EReference getFeatureTypeType_OutputFormats();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.v1_1_0.FeatureTypeType#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>WGS84 Bounding Box</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeatureTypeType#getWGS84BoundingBox()
	 * @see #getFeatureTypeType()
	 * @generated
	 */
	EReference getFeatureTypeType_WGS84BoundingBox();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.v1_1_0.FeatureTypeType#getMetadataURL <em>Metadata URL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Metadata URL</em>'.
	 * @see net.opengis.wfs.v1_1_0.FeatureTypeType#getMetadataURL()
	 * @see #getFeatureTypeType()
	 * @generated
	 */
	EReference getFeatureTypeType_MetadataURL();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Get Capabilities Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetCapabilitiesType
	 * @generated
	 */
	EClass getGetCapabilitiesType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GetCapabilitiesType#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetCapabilitiesType#getService()
	 * @see #getGetCapabilitiesType()
	 * @generated
	 */
	EAttribute getGetCapabilitiesType_Service();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.GetFeatureType <em>Get Feature Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Get Feature Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetFeatureType
	 * @generated
	 */
	EClass getGetFeatureType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.v1_1_0.GetFeatureType#getQuery <em>Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Query</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetFeatureType#getQuery()
	 * @see #getGetFeatureType()
	 * @generated
	 */
	EReference getGetFeatureType_Query();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GetFeatureType#getMaxFeatures <em>Max Features</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Features</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetFeatureType#getMaxFeatures()
	 * @see #getGetFeatureType()
	 * @generated
	 */
	EAttribute getGetFeatureType_MaxFeatures();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GetFeatureType#getOutputFormat <em>Output Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Output Format</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetFeatureType#getOutputFormat()
	 * @see #getGetFeatureType()
	 * @generated
	 */
	EAttribute getGetFeatureType_OutputFormat();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GetFeatureType#getResultType <em>Result Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Result Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetFeatureType#getResultType()
	 * @see #getGetFeatureType()
	 * @generated
	 */
	EAttribute getGetFeatureType_ResultType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GetFeatureType#getTraverseXlinkDepth <em>Traverse Xlink Depth</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Traverse Xlink Depth</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetFeatureType#getTraverseXlinkDepth()
	 * @see #getGetFeatureType()
	 * @generated
	 */
	EAttribute getGetFeatureType_TraverseXlinkDepth();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GetFeatureType#getTraverseXlinkExpiry <em>Traverse Xlink Expiry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Traverse Xlink Expiry</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetFeatureType#getTraverseXlinkExpiry()
	 * @see #getGetFeatureType()
	 * @generated
	 */
	EAttribute getGetFeatureType_TraverseXlinkExpiry();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.GetFeatureWithLockType <em>Get Feature With Lock Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Get Feature With Lock Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetFeatureWithLockType
	 * @generated
	 */
	EClass getGetFeatureWithLockType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.v1_1_0.GetFeatureWithLockType#getQuery <em>Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Query</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetFeatureWithLockType#getQuery()
	 * @see #getGetFeatureWithLockType()
	 * @generated
	 */
	EReference getGetFeatureWithLockType_Query();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GetFeatureWithLockType#getExpiry <em>Expiry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expiry</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetFeatureWithLockType#getExpiry()
	 * @see #getGetFeatureWithLockType()
	 * @generated
	 */
	EAttribute getGetFeatureWithLockType_Expiry();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GetFeatureWithLockType#getMaxFeatures <em>Max Features</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Features</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetFeatureWithLockType#getMaxFeatures()
	 * @see #getGetFeatureWithLockType()
	 * @generated
	 */
	EAttribute getGetFeatureWithLockType_MaxFeatures();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GetFeatureWithLockType#getOutputFormat <em>Output Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Output Format</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetFeatureWithLockType#getOutputFormat()
	 * @see #getGetFeatureWithLockType()
	 * @generated
	 */
	EAttribute getGetFeatureWithLockType_OutputFormat();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GetFeatureWithLockType#getResultType <em>Result Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Result Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetFeatureWithLockType#getResultType()
	 * @see #getGetFeatureWithLockType()
	 * @generated
	 */
	EAttribute getGetFeatureWithLockType_ResultType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GetFeatureWithLockType#getTraverseXlinkDepth <em>Traverse Xlink Depth</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Traverse Xlink Depth</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetFeatureWithLockType#getTraverseXlinkDepth()
	 * @see #getGetFeatureWithLockType()
	 * @generated
	 */
	EAttribute getGetFeatureWithLockType_TraverseXlinkDepth();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GetFeatureWithLockType#getTraverseXlinkExpiry <em>Traverse Xlink Expiry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Traverse Xlink Expiry</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetFeatureWithLockType#getTraverseXlinkExpiry()
	 * @see #getGetFeatureWithLockType()
	 * @generated
	 */
	EAttribute getGetFeatureWithLockType_TraverseXlinkExpiry();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.GetGmlObjectType <em>Get Gml Object Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Get Gml Object Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetGmlObjectType
	 * @generated
	 */
	EClass getGetGmlObjectType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GetGmlObjectType#getGmlObjectId <em>Gml Object Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Gml Object Id</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetGmlObjectType#getGmlObjectId()
	 * @see #getGetGmlObjectType()
	 * @generated
	 */
	EAttribute getGetGmlObjectType_GmlObjectId();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GetGmlObjectType#getOutputFormat <em>Output Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Output Format</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetGmlObjectType#getOutputFormat()
	 * @see #getGetGmlObjectType()
	 * @generated
	 */
	EAttribute getGetGmlObjectType_OutputFormat();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GetGmlObjectType#getTraverseXlinkDepth <em>Traverse Xlink Depth</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Traverse Xlink Depth</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetGmlObjectType#getTraverseXlinkDepth()
	 * @see #getGetGmlObjectType()
	 * @generated
	 */
	EAttribute getGetGmlObjectType_TraverseXlinkDepth();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GetGmlObjectType#getTraverseXlinkExpiry <em>Traverse Xlink Expiry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Traverse Xlink Expiry</em>'.
	 * @see net.opengis.wfs.v1_1_0.GetGmlObjectType#getTraverseXlinkExpiry()
	 * @see #getGetGmlObjectType()
	 * @generated
	 */
	EAttribute getGetGmlObjectType_TraverseXlinkExpiry();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.GMLObjectTypeListType <em>GML Object Type List Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>GML Object Type List Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.GMLObjectTypeListType
	 * @generated
	 */
	EClass getGMLObjectTypeListType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.v1_1_0.GMLObjectTypeListType#getGMLObjectType <em>GML Object Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>GML Object Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.GMLObjectTypeListType#getGMLObjectType()
	 * @see #getGMLObjectTypeListType()
	 * @generated
	 */
	EReference getGMLObjectTypeListType_GMLObjectType();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.GMLObjectTypeType <em>GML Object Type Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>GML Object Type Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.GMLObjectTypeType
	 * @generated
	 */
	EClass getGMLObjectTypeType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GMLObjectTypeType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see net.opengis.wfs.v1_1_0.GMLObjectTypeType#getName()
	 * @see #getGMLObjectTypeType()
	 * @generated
	 */
	EAttribute getGMLObjectTypeType_Name();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GMLObjectTypeType#getTitle <em>Title</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Title</em>'.
	 * @see net.opengis.wfs.v1_1_0.GMLObjectTypeType#getTitle()
	 * @see #getGMLObjectTypeType()
	 * @generated
	 */
	EAttribute getGMLObjectTypeType_Title();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.GMLObjectTypeType#getAbstract <em>Abstract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Abstract</em>'.
	 * @see net.opengis.wfs.v1_1_0.GMLObjectTypeType#getAbstract()
	 * @see #getGMLObjectTypeType()
	 * @generated
	 */
	EAttribute getGMLObjectTypeType_Abstract();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.v1_1_0.GMLObjectTypeType#getKeywords <em>Keywords</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Keywords</em>'.
	 * @see net.opengis.wfs.v1_1_0.GMLObjectTypeType#getKeywords()
	 * @see #getGMLObjectTypeType()
	 * @generated
	 */
	EReference getGMLObjectTypeType_Keywords();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.GMLObjectTypeType#getOutputFormats <em>Output Formats</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Output Formats</em>'.
	 * @see net.opengis.wfs.v1_1_0.GMLObjectTypeType#getOutputFormats()
	 * @see #getGMLObjectTypeType()
	 * @generated
	 */
	EReference getGMLObjectTypeType_OutputFormats();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.InsertedFeatureType <em>Inserted Feature Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Inserted Feature Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.InsertedFeatureType
	 * @generated
	 */
	EClass getInsertedFeatureType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.v1_1_0.InsertedFeatureType#getFeatureId <em>Feature Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Feature Id</em>'.
	 * @see net.opengis.wfs.v1_1_0.InsertedFeatureType#getFeatureId()
	 * @see #getInsertedFeatureType()
	 * @generated
	 */
	EAttribute getInsertedFeatureType_FeatureId();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.InsertedFeatureType#getHandle <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle</em>'.
	 * @see net.opengis.wfs.v1_1_0.InsertedFeatureType#getHandle()
	 * @see #getInsertedFeatureType()
	 * @generated
	 */
	EAttribute getInsertedFeatureType_Handle();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.InsertElementType <em>Insert Element Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Insert Element Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.InsertElementType
	 * @generated
	 */
	EClass getInsertElementType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.InsertElementType#getFeatureCollection <em>Feature Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Feature Collection</em>'.
	 * @see net.opengis.wfs.v1_1_0.InsertElementType#getFeatureCollection()
	 * @see #getInsertElementType()
	 * @generated
	 */
	EAttribute getInsertElementType_FeatureCollection();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.v1_1_0.InsertElementType#getFeature <em>Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Feature</em>'.
	 * @see net.opengis.wfs.v1_1_0.InsertElementType#getFeature()
	 * @see #getInsertElementType()
	 * @generated
	 */
	EAttribute getInsertElementType_Feature();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.InsertElementType#getHandle <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle</em>'.
	 * @see net.opengis.wfs.v1_1_0.InsertElementType#getHandle()
	 * @see #getInsertElementType()
	 * @generated
	 */
	EAttribute getInsertElementType_Handle();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.InsertElementType#getIdgen <em>Idgen</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Idgen</em>'.
	 * @see net.opengis.wfs.v1_1_0.InsertElementType#getIdgen()
	 * @see #getInsertElementType()
	 * @generated
	 */
	EAttribute getInsertElementType_Idgen();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.InsertElementType#getInputFormat <em>Input Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Input Format</em>'.
	 * @see net.opengis.wfs.v1_1_0.InsertElementType#getInputFormat()
	 * @see #getInsertElementType()
	 * @generated
	 */
	EAttribute getInsertElementType_InputFormat();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.InsertElementType#getSrsName <em>Srs Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Srs Name</em>'.
	 * @see net.opengis.wfs.v1_1_0.InsertElementType#getSrsName()
	 * @see #getInsertElementType()
	 * @generated
	 */
	EAttribute getInsertElementType_SrsName();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.InsertResultType <em>Insert Result Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Insert Result Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.InsertResultType
	 * @generated
	 */
	EClass getInsertResultType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.v1_1_0.InsertResultType#getFeature <em>Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Feature</em>'.
	 * @see net.opengis.wfs.v1_1_0.InsertResultType#getFeature()
	 * @see #getInsertResultType()
	 * @generated
	 */
	EReference getInsertResultType_Feature();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.LockFeatureResponseType <em>Lock Feature Response Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Lock Feature Response Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.LockFeatureResponseType
	 * @generated
	 */
	EClass getLockFeatureResponseType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.LockFeatureResponseType#getLockId <em>Lock Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lock Id</em>'.
	 * @see net.opengis.wfs.v1_1_0.LockFeatureResponseType#getLockId()
	 * @see #getLockFeatureResponseType()
	 * @generated
	 */
	EAttribute getLockFeatureResponseType_LockId();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.LockFeatureResponseType#getFeaturesLocked <em>Features Locked</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Features Locked</em>'.
	 * @see net.opengis.wfs.v1_1_0.LockFeatureResponseType#getFeaturesLocked()
	 * @see #getLockFeatureResponseType()
	 * @generated
	 */
	EReference getLockFeatureResponseType_FeaturesLocked();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.LockFeatureResponseType#getFeaturesNotLocked <em>Features Not Locked</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Features Not Locked</em>'.
	 * @see net.opengis.wfs.v1_1_0.LockFeatureResponseType#getFeaturesNotLocked()
	 * @see #getLockFeatureResponseType()
	 * @generated
	 */
	EReference getLockFeatureResponseType_FeaturesNotLocked();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.LockFeatureType <em>Lock Feature Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Lock Feature Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.LockFeatureType
	 * @generated
	 */
	EClass getLockFeatureType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.v1_1_0.LockFeatureType#getLock <em>Lock</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Lock</em>'.
	 * @see net.opengis.wfs.v1_1_0.LockFeatureType#getLock()
	 * @see #getLockFeatureType()
	 * @generated
	 */
	EReference getLockFeatureType_Lock();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.LockFeatureType#getExpiry <em>Expiry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expiry</em>'.
	 * @see net.opengis.wfs.v1_1_0.LockFeatureType#getExpiry()
	 * @see #getLockFeatureType()
	 * @generated
	 */
	EAttribute getLockFeatureType_Expiry();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.LockFeatureType#getLockAction <em>Lock Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lock Action</em>'.
	 * @see net.opengis.wfs.v1_1_0.LockFeatureType#getLockAction()
	 * @see #getLockFeatureType()
	 * @generated
	 */
	EAttribute getLockFeatureType_LockAction();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.LockType <em>Lock Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Lock Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.LockType
	 * @generated
	 */
	EClass getLockType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.LockType#getFilter <em>Filter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Filter</em>'.
	 * @see net.opengis.wfs.v1_1_0.LockType#getFilter()
	 * @see #getLockType()
	 * @generated
	 */
	EAttribute getLockType_Filter();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.LockType#getHandle <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle</em>'.
	 * @see net.opengis.wfs.v1_1_0.LockType#getHandle()
	 * @see #getLockType()
	 * @generated
	 */
	EAttribute getLockType_Handle();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.LockType#getTypeName <em>Type Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type Name</em>'.
	 * @see net.opengis.wfs.v1_1_0.LockType#getTypeName()
	 * @see #getLockType()
	 * @generated
	 */
	EAttribute getLockType_TypeName();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.MetadataURLType <em>Metadata URL Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Metadata URL Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.MetadataURLType
	 * @generated
	 */
	EClass getMetadataURLType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.MetadataURLType#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see net.opengis.wfs.v1_1_0.MetadataURLType#getValue()
	 * @see #getMetadataURLType()
	 * @generated
	 */
	EAttribute getMetadataURLType_Value();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.MetadataURLType#getFormat <em>Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Format</em>'.
	 * @see net.opengis.wfs.v1_1_0.MetadataURLType#getFormat()
	 * @see #getMetadataURLType()
	 * @generated
	 */
	EAttribute getMetadataURLType_Format();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.MetadataURLType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.MetadataURLType#getType()
	 * @see #getMetadataURLType()
	 * @generated
	 */
	EAttribute getMetadataURLType_Type();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.NativeType <em>Native Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Native Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.NativeType
	 * @generated
	 */
	EClass getNativeType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.NativeType#isSafeToIgnore <em>Safe To Ignore</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Safe To Ignore</em>'.
	 * @see net.opengis.wfs.v1_1_0.NativeType#isSafeToIgnore()
	 * @see #getNativeType()
	 * @generated
	 */
	EAttribute getNativeType_SafeToIgnore();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.NativeType#getVendorId <em>Vendor Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vendor Id</em>'.
	 * @see net.opengis.wfs.v1_1_0.NativeType#getVendorId()
	 * @see #getNativeType()
	 * @generated
	 */
	EAttribute getNativeType_VendorId();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.NoSRSType <em>No SRS Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>No SRS Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.NoSRSType
	 * @generated
	 */
	EClass getNoSRSType();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.OperationsType <em>Operations Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operations Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.OperationsType
	 * @generated
	 */
	EClass getOperationsType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.v1_1_0.OperationsType#getOperation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Operation</em>'.
	 * @see net.opengis.wfs.v1_1_0.OperationsType#getOperation()
	 * @see #getOperationsType()
	 * @generated
	 */
	EAttribute getOperationsType_Operation();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.OutputFormatListType <em>Output Format List Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Output Format List Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.OutputFormatListType
	 * @generated
	 */
	EClass getOutputFormatListType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.v1_1_0.OutputFormatListType#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see net.opengis.wfs.v1_1_0.OutputFormatListType#getGroup()
	 * @see #getOutputFormatListType()
	 * @generated
	 */
	EAttribute getOutputFormatListType_Group();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.v1_1_0.OutputFormatListType#getFormat <em>Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Format</em>'.
	 * @see net.opengis.wfs.v1_1_0.OutputFormatListType#getFormat()
	 * @see #getOutputFormatListType()
	 * @generated
	 */
	EAttribute getOutputFormatListType_Format();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.PropertyType <em>Property Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.PropertyType
	 * @generated
	 */
	EClass getPropertyType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.PropertyType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see net.opengis.wfs.v1_1_0.PropertyType#getName()
	 * @see #getPropertyType()
	 * @generated
	 */
	EAttribute getPropertyType_Name();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.PropertyType#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see net.opengis.wfs.v1_1_0.PropertyType#getValue()
	 * @see #getPropertyType()
	 * @generated
	 */
	EReference getPropertyType_Value();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.QueryType <em>Query Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Query Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.QueryType
	 * @generated
	 */
	EClass getQueryType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.v1_1_0.QueryType#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see net.opengis.wfs.v1_1_0.QueryType#getGroup()
	 * @see #getQueryType()
	 * @generated
	 */
	EAttribute getQueryType_Group();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.v1_1_0.QueryType#getPropertyName <em>Property Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Property Name</em>'.
	 * @see net.opengis.wfs.v1_1_0.QueryType#getPropertyName()
	 * @see #getQueryType()
	 * @generated
	 */
	EAttribute getQueryType_PropertyName();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.v1_1_0.QueryType#getFunction <em>Function</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Function</em>'.
	 * @see net.opengis.wfs.v1_1_0.QueryType#getFunction()
	 * @see #getQueryType()
	 * @generated
	 */
	EAttribute getQueryType_Function();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.QueryType#getFilter <em>Filter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Filter</em>'.
	 * @see net.opengis.wfs.v1_1_0.QueryType#getFilter()
	 * @see #getQueryType()
	 * @generated
	 */
	EAttribute getQueryType_Filter();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.QueryType#getSortBy <em>Sort By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sort By</em>'.
	 * @see net.opengis.wfs.v1_1_0.QueryType#getSortBy()
	 * @see #getQueryType()
	 * @generated
	 */
	EAttribute getQueryType_SortBy();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.QueryType#getFeatureVersion <em>Feature Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Feature Version</em>'.
	 * @see net.opengis.wfs.v1_1_0.QueryType#getFeatureVersion()
	 * @see #getQueryType()
	 * @generated
	 */
	EAttribute getQueryType_FeatureVersion();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.QueryType#getHandle <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle</em>'.
	 * @see net.opengis.wfs.v1_1_0.QueryType#getHandle()
	 * @see #getQueryType()
	 * @generated
	 */
	EAttribute getQueryType_Handle();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.QueryType#getSrsName <em>Srs Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Srs Name</em>'.
	 * @see net.opengis.wfs.v1_1_0.QueryType#getSrsName()
	 * @see #getQueryType()
	 * @generated
	 */
	EAttribute getQueryType_SrsName();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.QueryType#getTypeName <em>Type Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type Name</em>'.
	 * @see net.opengis.wfs.v1_1_0.QueryType#getTypeName()
	 * @see #getQueryType()
	 * @generated
	 */
	EAttribute getQueryType_TypeName();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.TransactionResponseType <em>Transaction Response Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Transaction Response Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionResponseType
	 * @generated
	 */
	EClass getTransactionResponseType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.TransactionResponseType#getTransactionSummary <em>Transaction Summary</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Transaction Summary</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionResponseType#getTransactionSummary()
	 * @see #getTransactionResponseType()
	 * @generated
	 */
	EReference getTransactionResponseType_TransactionSummary();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.TransactionResponseType#getTransactionResults <em>Transaction Results</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Transaction Results</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionResponseType#getTransactionResults()
	 * @see #getTransactionResponseType()
	 * @generated
	 */
	EReference getTransactionResponseType_TransactionResults();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.TransactionResponseType#getInsertResults <em>Insert Results</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Insert Results</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionResponseType#getInsertResults()
	 * @see #getTransactionResponseType()
	 * @generated
	 */
	EReference getTransactionResponseType_InsertResults();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.TransactionResponseType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionResponseType#getVersion()
	 * @see #getTransactionResponseType()
	 * @generated
	 */
	EAttribute getTransactionResponseType_Version();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.TransactionResultsType <em>Transaction Results Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Transaction Results Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionResultsType
	 * @generated
	 */
	EClass getTransactionResultsType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.v1_1_0.TransactionResultsType#getAction <em>Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Action</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionResultsType#getAction()
	 * @see #getTransactionResultsType()
	 * @generated
	 */
	EReference getTransactionResultsType_Action();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.TransactionSummaryType <em>Transaction Summary Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Transaction Summary Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionSummaryType
	 * @generated
	 */
	EClass getTransactionSummaryType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.TransactionSummaryType#getTotalInserted <em>Total Inserted</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Total Inserted</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionSummaryType#getTotalInserted()
	 * @see #getTransactionSummaryType()
	 * @generated
	 */
	EAttribute getTransactionSummaryType_TotalInserted();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.TransactionSummaryType#getTotalUpdated <em>Total Updated</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Total Updated</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionSummaryType#getTotalUpdated()
	 * @see #getTransactionSummaryType()
	 * @generated
	 */
	EAttribute getTransactionSummaryType_TotalUpdated();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.TransactionSummaryType#getTotalDeleted <em>Total Deleted</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Total Deleted</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionSummaryType#getTotalDeleted()
	 * @see #getTransactionSummaryType()
	 * @generated
	 */
	EAttribute getTransactionSummaryType_TotalDeleted();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.TransactionType <em>Transaction Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Transaction Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionType
	 * @generated
	 */
	EClass getTransactionType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.TransactionType#getLockId <em>Lock Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lock Id</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionType#getLockId()
	 * @see #getTransactionType()
	 * @generated
	 */
	EAttribute getTransactionType_LockId();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.v1_1_0.TransactionType#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionType#getGroup()
	 * @see #getTransactionType()
	 * @generated
	 */
	EAttribute getTransactionType_Group();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.v1_1_0.TransactionType#getInsert <em>Insert</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Insert</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionType#getInsert()
	 * @see #getTransactionType()
	 * @generated
	 */
	EReference getTransactionType_Insert();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.v1_1_0.TransactionType#getUpdate <em>Update</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Update</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionType#getUpdate()
	 * @see #getTransactionType()
	 * @generated
	 */
	EReference getTransactionType_Update();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.v1_1_0.TransactionType#getDelete <em>Delete</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Delete</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionType#getDelete()
	 * @see #getTransactionType()
	 * @generated
	 */
	EReference getTransactionType_Delete();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.v1_1_0.TransactionType#getNative <em>Native</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Native</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionType#getNative()
	 * @see #getTransactionType()
	 * @generated
	 */
	EReference getTransactionType_Native();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.TransactionType#getReleaseAction <em>Release Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Release Action</em>'.
	 * @see net.opengis.wfs.v1_1_0.TransactionType#getReleaseAction()
	 * @see #getTransactionType()
	 * @generated
	 */
	EAttribute getTransactionType_ReleaseAction();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.UpdateElementType <em>Update Element Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Update Element Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.UpdateElementType
	 * @generated
	 */
	EClass getUpdateElementType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.v1_1_0.UpdateElementType#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Property</em>'.
	 * @see net.opengis.wfs.v1_1_0.UpdateElementType#getProperty()
	 * @see #getUpdateElementType()
	 * @generated
	 */
	EReference getUpdateElementType_Property();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.UpdateElementType#getFilter <em>Filter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Filter</em>'.
	 * @see net.opengis.wfs.v1_1_0.UpdateElementType#getFilter()
	 * @see #getUpdateElementType()
	 * @generated
	 */
	EAttribute getUpdateElementType_Filter();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.UpdateElementType#getHandle <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle</em>'.
	 * @see net.opengis.wfs.v1_1_0.UpdateElementType#getHandle()
	 * @see #getUpdateElementType()
	 * @generated
	 */
	EAttribute getUpdateElementType_Handle();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.UpdateElementType#getInputFormat <em>Input Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Input Format</em>'.
	 * @see net.opengis.wfs.v1_1_0.UpdateElementType#getInputFormat()
	 * @see #getUpdateElementType()
	 * @generated
	 */
	EAttribute getUpdateElementType_InputFormat();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.UpdateElementType#getSrsName <em>Srs Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Srs Name</em>'.
	 * @see net.opengis.wfs.v1_1_0.UpdateElementType#getSrsName()
	 * @see #getUpdateElementType()
	 * @generated
	 */
	EAttribute getUpdateElementType_SrsName();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.UpdateElementType#getTypeName <em>Type Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type Name</em>'.
	 * @see net.opengis.wfs.v1_1_0.UpdateElementType#getTypeName()
	 * @see #getUpdateElementType()
	 * @generated
	 */
	EAttribute getUpdateElementType_TypeName();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.v1_1_0.WFSCapabilitiesType <em>Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Capabilities Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.WFSCapabilitiesType
	 * @generated
	 */
	EClass getWFSCapabilitiesType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.WFSCapabilitiesType#getFeatureTypeList <em>Feature Type List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Feature Type List</em>'.
	 * @see net.opengis.wfs.v1_1_0.WFSCapabilitiesType#getFeatureTypeList()
	 * @see #getWFSCapabilitiesType()
	 * @generated
	 */
	EReference getWFSCapabilitiesType_FeatureTypeList();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.WFSCapabilitiesType#getServesGMLObjectTypeList <em>Serves GML Object Type List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Serves GML Object Type List</em>'.
	 * @see net.opengis.wfs.v1_1_0.WFSCapabilitiesType#getServesGMLObjectTypeList()
	 * @see #getWFSCapabilitiesType()
	 * @generated
	 */
	EReference getWFSCapabilitiesType_ServesGMLObjectTypeList();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.v1_1_0.WFSCapabilitiesType#getSupportsGMLObjectTypeList <em>Supports GML Object Type List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Supports GML Object Type List</em>'.
	 * @see net.opengis.wfs.v1_1_0.WFSCapabilitiesType#getSupportsGMLObjectTypeList()
	 * @see #getWFSCapabilitiesType()
	 * @generated
	 */
	EReference getWFSCapabilitiesType_SupportsGMLObjectTypeList();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.v1_1_0.WFSCapabilitiesType#getFilterCapabilities <em>Filter Capabilities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Filter Capabilities</em>'.
	 * @see net.opengis.wfs.v1_1_0.WFSCapabilitiesType#getFilterCapabilities()
	 * @see #getWFSCapabilitiesType()
	 * @generated
	 */
	EAttribute getWFSCapabilitiesType_FilterCapabilities();

	/**
	 * Returns the meta object for enum '{@link net.opengis.wfs.v1_1_0.AllSomeType <em>All Some Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>All Some Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.AllSomeType
	 * @generated
	 */
	EEnum getAllSomeType();

	/**
	 * Returns the meta object for enum '{@link net.opengis.wfs.v1_1_0.IdentifierGenerationOptionType <em>Identifier Generation Option Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Identifier Generation Option Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.IdentifierGenerationOptionType
	 * @generated
	 */
	EEnum getIdentifierGenerationOptionType();

	/**
	 * Returns the meta object for enum '{@link net.opengis.wfs.v1_1_0.OperationType <em>Operation Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Operation Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.OperationType
	 * @generated
	 */
	EEnum getOperationType();

	/**
	 * Returns the meta object for enum '{@link net.opengis.wfs.v1_1_0.ResultTypeType <em>Result Type Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Result Type Type</em>'.
	 * @see net.opengis.wfs.v1_1_0.ResultTypeType
	 * @generated
	 */
	EEnum getResultTypeType();

	/**
	 * Returns the meta object for data type '{@link net.opengis.wfs.v1_1_0.AllSomeType <em>All Some Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>All Some Type Object</em>'.
	 * @see net.opengis.wfs.v1_1_0.AllSomeType
	 * @model instanceClass="net.opengis.wfs.v1_1_0.AllSomeType"
	 *        extendedMetaData="name='AllSomeType:Object' baseType='AllSomeType'" 
	 * @generated
	 */
	EDataType getAllSomeTypeObject();

	/**
	 * Returns the meta object for data type '{@link java.util.List <em>Base Type Name List Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Base Type Name List Type</em>'.
	 * @see java.util.List
	 * @model instanceClass="java.util.List"
	 *        extendedMetaData="name='Base_TypeNameListType' itemType='http://www.eclipse.org/emf/2003/XMLType#QName'" 
	 * @generated
	 */
	EDataType getBaseTypeNameListType();

	/**
	 * Returns the meta object for data type '{@link java.lang.String <em>Format Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Format Type</em>'.
	 * @see java.lang.String
	 * @model instanceClass="java.lang.String"
	 *        extendedMetaData="name='format_._type' baseType='http://www.eclipse.org/emf/2003/XMLType#NMTOKEN' enumeration='text/xml text/html text/sgml text/plain'" 
	 * @generated
	 */
	EDataType getFormatType();

	/**
	 * Returns the meta object for data type '{@link net.opengis.wfs.v1_1_0.IdentifierGenerationOptionType <em>Identifier Generation Option Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Identifier Generation Option Type Object</em>'.
	 * @see net.opengis.wfs.v1_1_0.IdentifierGenerationOptionType
	 * @model instanceClass="net.opengis.wfs.v1_1_0.IdentifierGenerationOptionType"
	 *        extendedMetaData="name='IdentifierGenerationOptionType:Object' baseType='IdentifierGenerationOptionType'" 
	 * @generated
	 */
	EDataType getIdentifierGenerationOptionTypeObject();

	/**
	 * Returns the meta object for data type '{@link net.opengis.wfs.v1_1_0.OperationType <em>Operation Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Operation Type Object</em>'.
	 * @see net.opengis.wfs.v1_1_0.OperationType
	 * @model instanceClass="net.opengis.wfs.v1_1_0.OperationType"
	 *        extendedMetaData="name='OperationType:Object' baseType='OperationType'" 
	 * @generated
	 */
	EDataType getOperationTypeObject();

	/**
	 * Returns the meta object for data type '{@link net.opengis.wfs.v1_1_0.ResultTypeType <em>Result Type Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Result Type Type Object</em>'.
	 * @see net.opengis.wfs.v1_1_0.ResultTypeType
	 * @model instanceClass="net.opengis.wfs.v1_1_0.ResultTypeType"
	 *        extendedMetaData="name='ResultTypeType:Object' baseType='ResultTypeType'" 
	 * @generated
	 */
	EDataType getResultTypeTypeObject();

	/**
	 * Returns the meta object for data type '{@link java.util.List <em>Type Name List Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Type Name List Type</em>'.
	 * @see java.util.List
	 * @model instanceClass="java.util.List"
	 *        extendedMetaData="name='TypeNameListType' baseType='Base_TypeNameListType' pattern='((\\w:)?\\w(=\\w)?){1,}'" 
	 * @generated
	 */
	EDataType getTypeNameListType();

	/**
	 * Returns the meta object for data type '{@link java.lang.String <em>Type Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Type Type</em>'.
	 * @see java.lang.String
	 * @model instanceClass="java.lang.String"
	 *        extendedMetaData="name='type_._type' baseType='http://www.eclipse.org/emf/2003/XMLType#NMTOKEN' enumeration='TC211 FGDC 19115 19139'" 
	 * @generated
	 */
	EDataType getTypeType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	WFSFactory getWFSFactory();

} //WFSPackage
