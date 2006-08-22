/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

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
 * @see net.opengis.wfs.WFSFactory
 * @model kind="package"
 * @generated
 */
public interface WFSPackage extends EPackage{
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
	WFSPackage eINSTANCE = net.opengis.wfs.impl.WFSPackageImpl.init();

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.DescribeFeatureTypeTypeImpl <em>Describe Feature Type Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.DescribeFeatureTypeTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getDescribeFeatureTypeType()
	 * @generated
	 */
	int DESCRIBE_FEATURE_TYPE_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Type Name</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_FEATURE_TYPE_TYPE__TYPE_NAME = 0;

	/**
	 * The feature id for the '<em><b>Output Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_FEATURE_TYPE_TYPE__OUTPUT_FORMAT = 1;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_FEATURE_TYPE_TYPE__SERVICE = 2;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_FEATURE_TYPE_TYPE__VERSION = 3;

	/**
	 * The number of structural features of the the '<em>Describe Feature Type Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESCRIBE_FEATURE_TYPE_TYPE_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.DocumentRootImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getDocumentRoot()
	 * @generated
	 */
	int DOCUMENT_ROOT = 1;

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
	 * The feature id for the '<em><b>Describe Feature Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__DESCRIBE_FEATURE_TYPE = 3;

	/**
	 * The feature id for the '<em><b>Feature Collection</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__FEATURE_COLLECTION = 4;

	/**
	 * The feature id for the '<em><b>Get Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GET_CAPABILITIES = 5;

	/**
	 * The feature id for the '<em><b>Get Feature</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GET_FEATURE = 6;

	/**
	 * The feature id for the '<em><b>Query</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__QUERY = 7;

	/**
	 * The number of structural features of the the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT_FEATURE_COUNT = 8;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.FeatureCollectionTypeImpl <em>Feature Collection Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.FeatureCollectionTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getFeatureCollectionType()
	 * @generated
	 */
	int FEATURE_COLLECTION_TYPE = 2;

	/**
	 * The number of structural features of the the '<em>Feature Collection Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_COLLECTION_TYPE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.GetCapabilitiesTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getGetCapabilitiesType()
	 * @generated
	 */
	int GET_CAPABILITIES_TYPE = 3;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_CAPABILITIES_TYPE__SERVICE = 0;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_CAPABILITIES_TYPE__VERSION = 1;

	/**
	 * The number of structural features of the the '<em>Get Capabilities Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_CAPABILITIES_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.GetFeatureTypeImpl <em>Get Feature Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.GetFeatureTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getGetFeatureType()
	 * @generated
	 */
	int GET_FEATURE_TYPE = 4;

	/**
	 * The feature id for the '<em><b>Query</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_TYPE__QUERY = 0;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_TYPE__HANDLE = 1;

	/**
	 * The feature id for the '<em><b>Max Features</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_TYPE__MAX_FEATURES = 2;

	/**
	 * The feature id for the '<em><b>Output Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_TYPE__OUTPUT_FORMAT = 3;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_TYPE__SERVICE = 4;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_TYPE__VERSION = 5;

	/**
	 * The number of structural features of the the '<em>Get Feature Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_TYPE_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.QueryTypeImpl <em>Query Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.QueryTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getQueryType()
	 * @generated
	 */
	int QUERY_TYPE = 5;

	/**
	 * The feature id for the '<em><b>Property Name</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TYPE__PROPERTY_NAME = 0;

	/**
	 * The feature id for the '<em><b>Filter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TYPE__FILTER = 1;

	/**
	 * The feature id for the '<em><b>Feature Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TYPE__FEATURE_VERSION = 2;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TYPE__HANDLE = 3;

	/**
	 * The feature id for the '<em><b>Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TYPE__TYPE_NAME = 4;

	/**
	 * The number of structural features of the the '<em>Query Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TYPE_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '<em>Property Name</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.opengis.filter.expression.PropertyName
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getPropertyName()
	 * @generated
	 */
	int PROPERTY_NAME = 6;

	/**
	 * The meta object id for the '<em>Filter</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.opengis.filter.Filter
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getFilter()
	 * @generated
	 */
	int FILTER = 7;


	/**
	 * The meta object id for the '<em>QName</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see javax.xml.namespace.QName
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getQName()
	 * @generated
	 */
	int QNAME = 8;


	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.DescribeFeatureTypeType <em>Describe Feature Type Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Describe Feature Type Type</em>'.
	 * @see net.opengis.wfs.DescribeFeatureTypeType
	 * @generated
	 */
	EClass getDescribeFeatureTypeType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.DescribeFeatureTypeType#getTypeName <em>Type Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Type Name</em>'.
	 * @see net.opengis.wfs.DescribeFeatureTypeType#getTypeName()
	 * @see #getDescribeFeatureTypeType()
	 * @generated
	 */
	EAttribute getDescribeFeatureTypeType_TypeName();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.DescribeFeatureTypeType#getOutputFormat <em>Output Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Output Format</em>'.
	 * @see net.opengis.wfs.DescribeFeatureTypeType#getOutputFormat()
	 * @see #getDescribeFeatureTypeType()
	 * @generated
	 */
	EAttribute getDescribeFeatureTypeType_OutputFormat();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.DescribeFeatureTypeType#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service</em>'.
	 * @see net.opengis.wfs.DescribeFeatureTypeType#getService()
	 * @see #getDescribeFeatureTypeType()
	 * @generated
	 */
	EAttribute getDescribeFeatureTypeType_Service();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.DescribeFeatureTypeType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wfs.DescribeFeatureTypeType#getVersion()
	 * @see #getDescribeFeatureTypeType()
	 * @generated
	 */
	EAttribute getDescribeFeatureTypeType_Version();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see net.opengis.wfs.DocumentRoot
	 * @generated
	 */
	EClass getDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link net.opengis.wfs.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link net.opengis.wfs.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.DocumentRoot#getDescribeFeatureType <em>Describe Feature Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Describe Feature Type</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getDescribeFeatureType()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_DescribeFeatureType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.DocumentRoot#getFeatureCollection <em>Feature Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Feature Collection</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getFeatureCollection()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_FeatureCollection();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Get Capabilities</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getGetCapabilities()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_GetCapabilities();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.DocumentRoot#getGetFeature <em>Get Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Get Feature</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getGetFeature()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_GetFeature();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.DocumentRoot#getQuery <em>Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Query</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getQuery()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Query();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.FeatureCollectionType <em>Feature Collection Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Feature Collection Type</em>'.
	 * @see net.opengis.wfs.FeatureCollectionType
	 * @generated
	 */
	EClass getFeatureCollectionType();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Get Capabilities Type</em>'.
	 * @see net.opengis.wfs.GetCapabilitiesType
	 * @generated
	 */
	EClass getGetCapabilitiesType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.GetCapabilitiesType#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service</em>'.
	 * @see net.opengis.wfs.GetCapabilitiesType#getService()
	 * @see #getGetCapabilitiesType()
	 * @generated
	 */
	EAttribute getGetCapabilitiesType_Service();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.GetCapabilitiesType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wfs.GetCapabilitiesType#getVersion()
	 * @see #getGetCapabilitiesType()
	 * @generated
	 */
	EAttribute getGetCapabilitiesType_Version();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.GetFeatureType <em>Get Feature Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Get Feature Type</em>'.
	 * @see net.opengis.wfs.GetFeatureType
	 * @generated
	 */
	EClass getGetFeatureType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.GetFeatureType#getQuery <em>Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Query</em>'.
	 * @see net.opengis.wfs.GetFeatureType#getQuery()
	 * @see #getGetFeatureType()
	 * @generated
	 */
	EReference getGetFeatureType_Query();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.GetFeatureType#getHandle <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle</em>'.
	 * @see net.opengis.wfs.GetFeatureType#getHandle()
	 * @see #getGetFeatureType()
	 * @generated
	 */
	EAttribute getGetFeatureType_Handle();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.GetFeatureType#getMaxFeatures <em>Max Features</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Features</em>'.
	 * @see net.opengis.wfs.GetFeatureType#getMaxFeatures()
	 * @see #getGetFeatureType()
	 * @generated
	 */
	EAttribute getGetFeatureType_MaxFeatures();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.GetFeatureType#getOutputFormat <em>Output Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Output Format</em>'.
	 * @see net.opengis.wfs.GetFeatureType#getOutputFormat()
	 * @see #getGetFeatureType()
	 * @generated
	 */
	EAttribute getGetFeatureType_OutputFormat();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.GetFeatureType#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service</em>'.
	 * @see net.opengis.wfs.GetFeatureType#getService()
	 * @see #getGetFeatureType()
	 * @generated
	 */
	EAttribute getGetFeatureType_Service();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.GetFeatureType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wfs.GetFeatureType#getVersion()
	 * @see #getGetFeatureType()
	 * @generated
	 */
	EAttribute getGetFeatureType_Version();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.QueryType <em>Query Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Query Type</em>'.
	 * @see net.opengis.wfs.QueryType
	 * @generated
	 */
	EClass getQueryType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.QueryType#getPropertyName <em>Property Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Property Name</em>'.
	 * @see net.opengis.wfs.QueryType#getPropertyName()
	 * @see #getQueryType()
	 * @generated
	 */
	EAttribute getQueryType_PropertyName();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.QueryType#getFilter <em>Filter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Filter</em>'.
	 * @see net.opengis.wfs.QueryType#getFilter()
	 * @see #getQueryType()
	 * @generated
	 */
	EAttribute getQueryType_Filter();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.QueryType#getFeatureVersion <em>Feature Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Feature Version</em>'.
	 * @see net.opengis.wfs.QueryType#getFeatureVersion()
	 * @see #getQueryType()
	 * @generated
	 */
	EAttribute getQueryType_FeatureVersion();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.QueryType#getHandle <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle</em>'.
	 * @see net.opengis.wfs.QueryType#getHandle()
	 * @see #getQueryType()
	 * @generated
	 */
	EAttribute getQueryType_Handle();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.QueryType#getTypeName <em>Type Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type Name</em>'.
	 * @see net.opengis.wfs.QueryType#getTypeName()
	 * @see #getQueryType()
	 * @generated
	 */
	EAttribute getQueryType_TypeName();

	/**
	 * Returns the meta object for data type '{@link org.opengis.filter.expression.PropertyName <em>Property Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Property Name</em>'.
	 * @see org.opengis.filter.expression.PropertyName
	 * @model instanceClass="org.opengis.filter.expression.PropertyName"
	 * @generated
	 */
	EDataType getPropertyName();

	/**
	 * Returns the meta object for data type '{@link org.opengis.filter.Filter <em>Filter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Filter</em>'.
	 * @see org.opengis.filter.Filter
	 * @model instanceClass="org.opengis.filter.Filter"
	 * @generated
	 */
	EDataType getFilter();

	/**
	 * Returns the meta object for data type '{@link javax.xml.namespace.QName <em>QName</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>QName</em>'.
	 * @see javax.xml.namespace.QName
	 * @model instanceClass="javax.xml.namespace.QName"
	 * @generated
	 */
	EDataType getQName();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	WFSFactory getWFSFactory();

} //WFSPackage
