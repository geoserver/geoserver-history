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
	 * The meta object id for the '{@link net.opengis.wfs.impl.TransactionOperationImpl <em>Transaction Operation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.TransactionOperationImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getTransactionOperation()
	 * @generated
	 */
	int TRANSACTION_OPERATION = 23;

	/**
	 * The number of structural features of the the '<em>Transaction Operation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_OPERATION_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.DeleteElementTypeImpl <em>Delete Element Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.DeleteElementTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getDeleteElementType()
	 * @generated
	 */
	int DELETE_ELEMENT_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Filter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETE_ELEMENT_TYPE__FILTER = TRANSACTION_OPERATION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETE_ELEMENT_TYPE__HANDLE = TRANSACTION_OPERATION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETE_ELEMENT_TYPE__TYPE_NAME = TRANSACTION_OPERATION_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the the '<em>Delete Element Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETE_ELEMENT_TYPE_FEATURE_COUNT = TRANSACTION_OPERATION_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.DescribeFeatureTypeTypeImpl <em>Describe Feature Type Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.DescribeFeatureTypeTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getDescribeFeatureTypeType()
	 * @generated
	 */
	int DESCRIBE_FEATURE_TYPE_TYPE = 1;

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
	int DOCUMENT_ROOT = 2;

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
	 * The feature id for the '<em><b>Delete</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__DELETE = 8;

	/**
	 * The feature id for the '<em><b>Failed</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__FAILED = 9;

	/**
	 * The feature id for the '<em><b>Get Feature With Lock</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GET_FEATURE_WITH_LOCK = 10;

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
	 * The feature id for the '<em><b>Lock Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__LOCK_ID = 13;

	/**
	 * The feature id for the '<em><b>Native</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__NATIVE = 14;

	/**
	 * The feature id for the '<em><b>Partial</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__PARTIAL = 15;

	/**
	 * The feature id for the '<em><b>Property</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__PROPERTY = 16;

	/**
	 * The feature id for the '<em><b>Success</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__SUCCESS = 17;

	/**
	 * The feature id for the '<em><b>Transaction</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__TRANSACTION = 18;

	/**
	 * The feature id for the '<em><b>Update</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__UPDATE = 19;

	/**
	 * The feature id for the '<em><b>Wfs Lock Feature Response</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__WFS_LOCK_FEATURE_RESPONSE = 20;

	/**
	 * The feature id for the '<em><b>Wfs Transaction Response</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__WFS_TRANSACTION_RESPONSE = 21;

	/**
	 * The number of structural features of the the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT_FEATURE_COUNT = 22;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.EmptyTypeImpl <em>Empty Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.EmptyTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getEmptyType()
	 * @generated
	 */
	int EMPTY_TYPE = 3;

	/**
	 * The number of structural features of the the '<em>Empty Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMPTY_TYPE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.FeatureCollectionTypeImpl <em>Feature Collection Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.FeatureCollectionTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getFeatureCollectionType()
	 * @generated
	 */
	int FEATURE_COLLECTION_TYPE = 4;

	/**
	 * The number of structural features of the the '<em>Feature Collection Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_COLLECTION_TYPE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.FeaturesLockedTypeImpl <em>Features Locked Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.FeaturesLockedTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getFeaturesLockedType()
	 * @generated
	 */
	int FEATURES_LOCKED_TYPE = 5;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_LOCKED_TYPE__GROUP = 0;

	/**
	 * The feature id for the '<em><b>Feature Id</b></em>' attribute.
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
	 * The meta object id for the '{@link net.opengis.wfs.impl.FeaturesNotLockedTypeImpl <em>Features Not Locked Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.FeaturesNotLockedTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getFeaturesNotLockedType()
	 * @generated
	 */
	int FEATURES_NOT_LOCKED_TYPE = 6;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_NOT_LOCKED_TYPE__GROUP = 0;

	/**
	 * The feature id for the '<em><b>Feature Id</b></em>' attribute.
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
	 * The meta object id for the '{@link net.opengis.wfs.impl.GetCapabilitiesTypeImpl <em>Get Capabilities Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.GetCapabilitiesTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getGetCapabilitiesType()
	 * @generated
	 */
	int GET_CAPABILITIES_TYPE = 7;

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
	int GET_FEATURE_TYPE = 8;

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
	 * The meta object id for the '{@link net.opengis.wfs.impl.GetFeatureWithLockTypeImpl <em>Get Feature With Lock Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.GetFeatureWithLockTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getGetFeatureWithLockType()
	 * @generated
	 */
	int GET_FEATURE_WITH_LOCK_TYPE = 9;

	/**
	 * The feature id for the '<em><b>Query</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE__QUERY = 0;

	/**
	 * The feature id for the '<em><b>Expiry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE__EXPIRY = 1;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE__HANDLE = 2;

	/**
	 * The feature id for the '<em><b>Max Features</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE__MAX_FEATURES = 3;

	/**
	 * The feature id for the '<em><b>Output Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE__OUTPUT_FORMAT = 4;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE__SERVICE = 5;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE__VERSION = 6;

	/**
	 * The number of structural features of the the '<em>Get Feature With Lock Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GET_FEATURE_WITH_LOCK_TYPE_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.InsertElementTypeImpl <em>Insert Element Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.InsertElementTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getInsertElementType()
	 * @generated
	 */
	int INSERT_ELEMENT_TYPE = 10;

	/**
	 * The feature id for the '<em><b>Feature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERT_ELEMENT_TYPE__FEATURE = TRANSACTION_OPERATION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERT_ELEMENT_TYPE__HANDLE = TRANSACTION_OPERATION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the the '<em>Insert Element Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERT_ELEMENT_TYPE_FEATURE_COUNT = TRANSACTION_OPERATION_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.InsertResultTypeImpl <em>Insert Result Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.InsertResultTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getInsertResultType()
	 * @generated
	 */
	int INSERT_RESULT_TYPE = 11;

	/**
	 * The feature id for the '<em><b>Feature Id</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERT_RESULT_TYPE__FEATURE_ID = 0;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERT_RESULT_TYPE__HANDLE = 1;

	/**
	 * The number of structural features of the the '<em>Insert Result Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERT_RESULT_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.LockFeatureTypeImpl <em>Lock Feature Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.LockFeatureTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getLockFeatureType()
	 * @generated
	 */
	int LOCK_FEATURE_TYPE = 12;

	/**
	 * The feature id for the '<em><b>Lock</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_FEATURE_TYPE__LOCK = 0;

	/**
	 * The feature id for the '<em><b>Expiry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_FEATURE_TYPE__EXPIRY = 1;

	/**
	 * The feature id for the '<em><b>Lock Action</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_FEATURE_TYPE__LOCK_ACTION = 2;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_FEATURE_TYPE__SERVICE = 3;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_FEATURE_TYPE__VERSION = 4;

	/**
	 * The number of structural features of the the '<em>Lock Feature Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCK_FEATURE_TYPE_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.LockTypeImpl <em>Lock Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.LockTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getLockType()
	 * @generated
	 */
	int LOCK_TYPE = 13;

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
	 * The meta object id for the '{@link net.opengis.wfs.impl.NativeTypeImpl <em>Native Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.NativeTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getNativeType()
	 * @generated
	 */
	int NATIVE_TYPE = 14;

	/**
	 * The feature id for the '<em><b>Safe To Ignore</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NATIVE_TYPE__SAFE_TO_IGNORE = TRANSACTION_OPERATION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Vendor Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NATIVE_TYPE__VENDOR_ID = TRANSACTION_OPERATION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the the '<em>Native Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NATIVE_TYPE_FEATURE_COUNT = TRANSACTION_OPERATION_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.PropertyTypeImpl <em>Property Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.PropertyTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getPropertyType()
	 * @generated
	 */
	int PROPERTY_TYPE = 15;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_TYPE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
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
	 * The meta object id for the '{@link net.opengis.wfs.impl.QueryTypeImpl <em>Query Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.QueryTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getQueryType()
	 * @generated
	 */
	int QUERY_TYPE = 16;

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
	 * The meta object id for the '{@link net.opengis.wfs.impl.StatusTypeImpl <em>Status Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.StatusTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getStatusType()
	 * @generated
	 */
	int STATUS_TYPE = 17;

	/**
	 * The feature id for the '<em><b>SUCCESS</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_TYPE__SUCCESS = 0;

	/**
	 * The feature id for the '<em><b>FAILED</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_TYPE__FAILED = 1;

	/**
	 * The feature id for the '<em><b>PARTIAL</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_TYPE__PARTIAL = 2;

	/**
	 * The number of structural features of the the '<em>Status Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.TransactionResultTypeImpl <em>Transaction Result Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.TransactionResultTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getTransactionResultType()
	 * @generated
	 */
	int TRANSACTION_RESULT_TYPE = 18;

	/**
	 * The feature id for the '<em><b>Status</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_RESULT_TYPE__STATUS = 0;

	/**
	 * The feature id for the '<em><b>Locator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_RESULT_TYPE__LOCATOR = 1;

	/**
	 * The feature id for the '<em><b>Message</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_RESULT_TYPE__MESSAGE = 2;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_RESULT_TYPE__HANDLE = 3;

	/**
	 * The number of structural features of the the '<em>Transaction Result Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_RESULT_TYPE_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.TransactionTypeImpl <em>Transaction Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.TransactionTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getTransactionType()
	 * @generated
	 */
	int TRANSACTION_TYPE = 19;

	/**
	 * The feature id for the '<em><b>Lock Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE__LOCK_ID = 0;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE__GROUP = 1;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE__HANDLE = 2;

	/**
	 * The feature id for the '<em><b>Release Action</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE__RELEASE_ACTION = 3;

	/**
	 * The feature id for the '<em><b>Service</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE__SERVICE = 4;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE__VERSION = 5;

	/**
	 * The feature id for the '<em><b>Operation</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE__OPERATION = 6;

	/**
	 * The number of structural features of the the '<em>Transaction Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSACTION_TYPE_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.UpdateElementTypeImpl <em>Update Element Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.UpdateElementTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getUpdateElementType()
	 * @generated
	 */
	int UPDATE_ELEMENT_TYPE = 20;

	/**
	 * The feature id for the '<em><b>Property</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ELEMENT_TYPE__PROPERTY = TRANSACTION_OPERATION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Filter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ELEMENT_TYPE__FILTER = TRANSACTION_OPERATION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ELEMENT_TYPE__HANDLE = TRANSACTION_OPERATION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ELEMENT_TYPE__TYPE_NAME = TRANSACTION_OPERATION_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the the '<em>Update Element Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_ELEMENT_TYPE_FEATURE_COUNT = TRANSACTION_OPERATION_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.WFSLockFeatureResponseTypeImpl <em>Lock Feature Response Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.WFSLockFeatureResponseTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getWFSLockFeatureResponseType()
	 * @generated
	 */
	int WFS_LOCK_FEATURE_RESPONSE_TYPE = 21;

	/**
	 * The feature id for the '<em><b>Lock Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WFS_LOCK_FEATURE_RESPONSE_TYPE__LOCK_ID = 0;

	/**
	 * The feature id for the '<em><b>Features Locked</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WFS_LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED = 1;

	/**
	 * The feature id for the '<em><b>Features Not Locked</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WFS_LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED = 2;

	/**
	 * The number of structural features of the the '<em>Lock Feature Response Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WFS_LOCK_FEATURE_RESPONSE_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.impl.WFSTransactionResponseTypeImpl <em>Transaction Response Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.impl.WFSTransactionResponseTypeImpl
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getWFSTransactionResponseType()
	 * @generated
	 */
	int WFS_TRANSACTION_RESPONSE_TYPE = 22;

	/**
	 * The feature id for the '<em><b>Insert Result</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WFS_TRANSACTION_RESPONSE_TYPE__INSERT_RESULT = 0;

	/**
	 * The feature id for the '<em><b>Transaction Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WFS_TRANSACTION_RESPONSE_TYPE__TRANSACTION_RESULT = 1;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WFS_TRANSACTION_RESPONSE_TYPE__VERSION = 2;

	/**
	 * The number of structural features of the the '<em>Transaction Response Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WFS_TRANSACTION_RESPONSE_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link net.opengis.wfs.AllSomeType <em>All Some Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.AllSomeType
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getAllSomeType()
	 * @generated
	 */
	int ALL_SOME_TYPE = 24;

	/**
	 * The meta object id for the '<em>All Some Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.opengis.wfs.AllSomeType
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getAllSomeTypeObject()
	 * @generated
	 */
	int ALL_SOME_TYPE_OBJECT = 25;

	/**
	 * The meta object id for the '<em>QName</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see javax.xml.namespace.QName
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getQName()
	 * @generated
	 */
	int QNAME = 26;

	/**
	 * The meta object id for the '<em>Filter</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.opengis.filter.Filter
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getFilter()
	 * @generated
	 */
	int FILTER = 27;

	/**
	 * The meta object id for the '<em>Property Name</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.opengis.filter.expression.PropertyName
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getPropertyName()
	 * @generated
	 */
	int PROPERTY_NAME = 28;

	/**
	 * The meta object id for the '<em>Feature</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.geotools.feature.Feature
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getFeature()
	 * @generated
	 */
	int FEATURE = 29;

	/**
	 * The meta object id for the '<em>Feature Collection</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.geotools.feature.FeatureCollection
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getFeatureCollection()
	 * @generated
	 */
	int FEATURE_COLLECTION = 30;

	/**
	 * The meta object id for the '<em>Feature Id</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.opengis.filter.FeatureId
	 * @see net.opengis.wfs.impl.WFSPackageImpl#getFeatureId()
	 * @generated
	 */
	int FEATURE_ID = 31;


	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.DeleteElementType <em>Delete Element Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Delete Element Type</em>'.
	 * @see net.opengis.wfs.DeleteElementType
	 * @generated
	 */
	EClass getDeleteElementType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.DeleteElementType#getFilter <em>Filter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Filter</em>'.
	 * @see net.opengis.wfs.DeleteElementType#getFilter()
	 * @see #getDeleteElementType()
	 * @generated
	 */
	EAttribute getDeleteElementType_Filter();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.DeleteElementType#getHandle <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle</em>'.
	 * @see net.opengis.wfs.DeleteElementType#getHandle()
	 * @see #getDeleteElementType()
	 * @generated
	 */
	EAttribute getDeleteElementType_Handle();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.DeleteElementType#getTypeName <em>Type Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type Name</em>'.
	 * @see net.opengis.wfs.DeleteElementType#getTypeName()
	 * @see #getDeleteElementType()
	 * @generated
	 */
	EAttribute getDeleteElementType_TypeName();

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
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.DocumentRoot#getDelete <em>Delete</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Delete</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getDelete()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Delete();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.DocumentRoot#getFailed <em>Failed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Failed</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getFailed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Failed();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.DocumentRoot#getGetFeatureWithLock <em>Get Feature With Lock</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Get Feature With Lock</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getGetFeatureWithLock()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_GetFeatureWithLock();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.DocumentRoot#getInsert <em>Insert</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Insert</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getInsert()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Insert();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.DocumentRoot#getLockFeature <em>Lock Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Lock Feature</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getLockFeature()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_LockFeature();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.DocumentRoot#getLockId <em>Lock Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lock Id</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getLockId()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_LockId();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.DocumentRoot#getNative <em>Native</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Native</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getNative()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Native();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.DocumentRoot#getPartial <em>Partial</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Partial</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getPartial()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Partial();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.DocumentRoot#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Property</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getProperty()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Property();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.DocumentRoot#getSuccess <em>Success</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Success</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getSuccess()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Success();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.DocumentRoot#getTransaction <em>Transaction</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Transaction</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getTransaction()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Transaction();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.DocumentRoot#getUpdate <em>Update</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Update</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getUpdate()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Update();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.DocumentRoot#getWfsLockFeatureResponse <em>Wfs Lock Feature Response</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Wfs Lock Feature Response</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getWfsLockFeatureResponse()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_WfsLockFeatureResponse();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.DocumentRoot#getWfsTransactionResponse <em>Wfs Transaction Response</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Wfs Transaction Response</em>'.
	 * @see net.opengis.wfs.DocumentRoot#getWfsTransactionResponse()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_WfsTransactionResponse();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.EmptyType <em>Empty Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Empty Type</em>'.
	 * @see net.opengis.wfs.EmptyType
	 * @generated
	 */
	EClass getEmptyType();

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
	 * Returns the meta object for class '{@link net.opengis.wfs.FeaturesLockedType <em>Features Locked Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Features Locked Type</em>'.
	 * @see net.opengis.wfs.FeaturesLockedType
	 * @generated
	 */
	EClass getFeaturesLockedType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.FeaturesLockedType#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see net.opengis.wfs.FeaturesLockedType#getGroup()
	 * @see #getFeaturesLockedType()
	 * @generated
	 */
	EAttribute getFeaturesLockedType_Group();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.FeaturesLockedType#getFeatureId <em>Feature Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Feature Id</em>'.
	 * @see net.opengis.wfs.FeaturesLockedType#getFeatureId()
	 * @see #getFeaturesLockedType()
	 * @generated
	 */
	EAttribute getFeaturesLockedType_FeatureId();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.FeaturesNotLockedType <em>Features Not Locked Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Features Not Locked Type</em>'.
	 * @see net.opengis.wfs.FeaturesNotLockedType
	 * @generated
	 */
	EClass getFeaturesNotLockedType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.FeaturesNotLockedType#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see net.opengis.wfs.FeaturesNotLockedType#getGroup()
	 * @see #getFeaturesNotLockedType()
	 * @generated
	 */
	EAttribute getFeaturesNotLockedType_Group();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.FeaturesNotLockedType#getFeatureId <em>Feature Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Feature Id</em>'.
	 * @see net.opengis.wfs.FeaturesNotLockedType#getFeatureId()
	 * @see #getFeaturesNotLockedType()
	 * @generated
	 */
	EAttribute getFeaturesNotLockedType_FeatureId();

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
	 * Returns the meta object for class '{@link net.opengis.wfs.GetFeatureWithLockType <em>Get Feature With Lock Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Get Feature With Lock Type</em>'.
	 * @see net.opengis.wfs.GetFeatureWithLockType
	 * @generated
	 */
	EClass getGetFeatureWithLockType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.GetFeatureWithLockType#getQuery <em>Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Query</em>'.
	 * @see net.opengis.wfs.GetFeatureWithLockType#getQuery()
	 * @see #getGetFeatureWithLockType()
	 * @generated
	 */
	EReference getGetFeatureWithLockType_Query();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.GetFeatureWithLockType#getExpiry <em>Expiry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expiry</em>'.
	 * @see net.opengis.wfs.GetFeatureWithLockType#getExpiry()
	 * @see #getGetFeatureWithLockType()
	 * @generated
	 */
	EAttribute getGetFeatureWithLockType_Expiry();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.GetFeatureWithLockType#getHandle <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle</em>'.
	 * @see net.opengis.wfs.GetFeatureWithLockType#getHandle()
	 * @see #getGetFeatureWithLockType()
	 * @generated
	 */
	EAttribute getGetFeatureWithLockType_Handle();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.GetFeatureWithLockType#getMaxFeatures <em>Max Features</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Features</em>'.
	 * @see net.opengis.wfs.GetFeatureWithLockType#getMaxFeatures()
	 * @see #getGetFeatureWithLockType()
	 * @generated
	 */
	EAttribute getGetFeatureWithLockType_MaxFeatures();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.GetFeatureWithLockType#getOutputFormat <em>Output Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Output Format</em>'.
	 * @see net.opengis.wfs.GetFeatureWithLockType#getOutputFormat()
	 * @see #getGetFeatureWithLockType()
	 * @generated
	 */
	EAttribute getGetFeatureWithLockType_OutputFormat();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.GetFeatureWithLockType#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service</em>'.
	 * @see net.opengis.wfs.GetFeatureWithLockType#getService()
	 * @see #getGetFeatureWithLockType()
	 * @generated
	 */
	EAttribute getGetFeatureWithLockType_Service();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.GetFeatureWithLockType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wfs.GetFeatureWithLockType#getVersion()
	 * @see #getGetFeatureWithLockType()
	 * @generated
	 */
	EAttribute getGetFeatureWithLockType_Version();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.InsertElementType <em>Insert Element Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Insert Element Type</em>'.
	 * @see net.opengis.wfs.InsertElementType
	 * @generated
	 */
	EClass getInsertElementType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.InsertElementType#getFeature <em>Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Feature</em>'.
	 * @see net.opengis.wfs.InsertElementType#getFeature()
	 * @see #getInsertElementType()
	 * @generated
	 */
	EAttribute getInsertElementType_Feature();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.InsertElementType#getHandle <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle</em>'.
	 * @see net.opengis.wfs.InsertElementType#getHandle()
	 * @see #getInsertElementType()
	 * @generated
	 */
	EAttribute getInsertElementType_Handle();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.InsertResultType <em>Insert Result Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Insert Result Type</em>'.
	 * @see net.opengis.wfs.InsertResultType
	 * @generated
	 */
	EClass getInsertResultType();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.InsertResultType#getFeatureId <em>Feature Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Feature Id</em>'.
	 * @see net.opengis.wfs.InsertResultType#getFeatureId()
	 * @see #getInsertResultType()
	 * @generated
	 */
	EAttribute getInsertResultType_FeatureId();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.InsertResultType#getHandle <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle</em>'.
	 * @see net.opengis.wfs.InsertResultType#getHandle()
	 * @see #getInsertResultType()
	 * @generated
	 */
	EAttribute getInsertResultType_Handle();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.LockFeatureType <em>Lock Feature Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Lock Feature Type</em>'.
	 * @see net.opengis.wfs.LockFeatureType
	 * @generated
	 */
	EClass getLockFeatureType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.LockFeatureType#getLock <em>Lock</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Lock</em>'.
	 * @see net.opengis.wfs.LockFeatureType#getLock()
	 * @see #getLockFeatureType()
	 * @generated
	 */
	EReference getLockFeatureType_Lock();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.LockFeatureType#getExpiry <em>Expiry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expiry</em>'.
	 * @see net.opengis.wfs.LockFeatureType#getExpiry()
	 * @see #getLockFeatureType()
	 * @generated
	 */
	EAttribute getLockFeatureType_Expiry();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.LockFeatureType#getLockAction <em>Lock Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lock Action</em>'.
	 * @see net.opengis.wfs.LockFeatureType#getLockAction()
	 * @see #getLockFeatureType()
	 * @generated
	 */
	EAttribute getLockFeatureType_LockAction();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.LockFeatureType#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service</em>'.
	 * @see net.opengis.wfs.LockFeatureType#getService()
	 * @see #getLockFeatureType()
	 * @generated
	 */
	EAttribute getLockFeatureType_Service();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.LockFeatureType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wfs.LockFeatureType#getVersion()
	 * @see #getLockFeatureType()
	 * @generated
	 */
	EAttribute getLockFeatureType_Version();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.LockType <em>Lock Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Lock Type</em>'.
	 * @see net.opengis.wfs.LockType
	 * @generated
	 */
	EClass getLockType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.LockType#getFilter <em>Filter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Filter</em>'.
	 * @see net.opengis.wfs.LockType#getFilter()
	 * @see #getLockType()
	 * @generated
	 */
	EAttribute getLockType_Filter();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.LockType#getHandle <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle</em>'.
	 * @see net.opengis.wfs.LockType#getHandle()
	 * @see #getLockType()
	 * @generated
	 */
	EAttribute getLockType_Handle();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.LockType#getTypeName <em>Type Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type Name</em>'.
	 * @see net.opengis.wfs.LockType#getTypeName()
	 * @see #getLockType()
	 * @generated
	 */
	EAttribute getLockType_TypeName();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.NativeType <em>Native Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Native Type</em>'.
	 * @see net.opengis.wfs.NativeType
	 * @generated
	 */
	EClass getNativeType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.NativeType#isSafeToIgnore <em>Safe To Ignore</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Safe To Ignore</em>'.
	 * @see net.opengis.wfs.NativeType#isSafeToIgnore()
	 * @see #getNativeType()
	 * @generated
	 */
	EAttribute getNativeType_SafeToIgnore();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.NativeType#getVendorId <em>Vendor Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vendor Id</em>'.
	 * @see net.opengis.wfs.NativeType#getVendorId()
	 * @see #getNativeType()
	 * @generated
	 */
	EAttribute getNativeType_VendorId();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.PropertyType <em>Property Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property Type</em>'.
	 * @see net.opengis.wfs.PropertyType
	 * @generated
	 */
	EClass getPropertyType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.PropertyType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see net.opengis.wfs.PropertyType#getName()
	 * @see #getPropertyType()
	 * @generated
	 */
	EAttribute getPropertyType_Name();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.PropertyType#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see net.opengis.wfs.PropertyType#getValue()
	 * @see #getPropertyType()
	 * @generated
	 */
	EAttribute getPropertyType_Value();

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
	 * Returns the meta object for class '{@link net.opengis.wfs.StatusType <em>Status Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Status Type</em>'.
	 * @see net.opengis.wfs.StatusType
	 * @generated
	 */
	EClass getStatusType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.StatusType#getSUCCESS <em>SUCCESS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>SUCCESS</em>'.
	 * @see net.opengis.wfs.StatusType#getSUCCESS()
	 * @see #getStatusType()
	 * @generated
	 */
	EReference getStatusType_SUCCESS();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.StatusType#getFAILED <em>FAILED</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>FAILED</em>'.
	 * @see net.opengis.wfs.StatusType#getFAILED()
	 * @see #getStatusType()
	 * @generated
	 */
	EReference getStatusType_FAILED();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.StatusType#getPARTIAL <em>PARTIAL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>PARTIAL</em>'.
	 * @see net.opengis.wfs.StatusType#getPARTIAL()
	 * @see #getStatusType()
	 * @generated
	 */
	EReference getStatusType_PARTIAL();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.TransactionResultType <em>Transaction Result Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Transaction Result Type</em>'.
	 * @see net.opengis.wfs.TransactionResultType
	 * @generated
	 */
	EClass getTransactionResultType();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.TransactionResultType#getStatus <em>Status</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Status</em>'.
	 * @see net.opengis.wfs.TransactionResultType#getStatus()
	 * @see #getTransactionResultType()
	 * @generated
	 */
	EReference getTransactionResultType_Status();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.TransactionResultType#getLocator <em>Locator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Locator</em>'.
	 * @see net.opengis.wfs.TransactionResultType#getLocator()
	 * @see #getTransactionResultType()
	 * @generated
	 */
	EAttribute getTransactionResultType_Locator();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.TransactionResultType#getMessage <em>Message</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Message</em>'.
	 * @see net.opengis.wfs.TransactionResultType#getMessage()
	 * @see #getTransactionResultType()
	 * @generated
	 */
	EAttribute getTransactionResultType_Message();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.TransactionResultType#getHandle <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle</em>'.
	 * @see net.opengis.wfs.TransactionResultType#getHandle()
	 * @see #getTransactionResultType()
	 * @generated
	 */
	EAttribute getTransactionResultType_Handle();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.TransactionType <em>Transaction Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Transaction Type</em>'.
	 * @see net.opengis.wfs.TransactionType
	 * @generated
	 */
	EClass getTransactionType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.TransactionType#getLockId <em>Lock Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lock Id</em>'.
	 * @see net.opengis.wfs.TransactionType#getLockId()
	 * @see #getTransactionType()
	 * @generated
	 */
	EAttribute getTransactionType_LockId();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.TransactionType#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see net.opengis.wfs.TransactionType#getGroup()
	 * @see #getTransactionType()
	 * @generated
	 */
	EAttribute getTransactionType_Group();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.TransactionType#getHandle <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle</em>'.
	 * @see net.opengis.wfs.TransactionType#getHandle()
	 * @see #getTransactionType()
	 * @generated
	 */
	EAttribute getTransactionType_Handle();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.TransactionType#getReleaseAction <em>Release Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Release Action</em>'.
	 * @see net.opengis.wfs.TransactionType#getReleaseAction()
	 * @see #getTransactionType()
	 * @generated
	 */
	EAttribute getTransactionType_ReleaseAction();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.TransactionType#getService <em>Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service</em>'.
	 * @see net.opengis.wfs.TransactionType#getService()
	 * @see #getTransactionType()
	 * @generated
	 */
	EAttribute getTransactionType_Service();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.TransactionType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wfs.TransactionType#getVersion()
	 * @see #getTransactionType()
	 * @generated
	 */
	EAttribute getTransactionType_Version();

	/**
	 * Returns the meta object for the reference list '{@link net.opengis.wfs.TransactionType#getOperation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Operation</em>'.
	 * @see net.opengis.wfs.TransactionType#getOperation()
	 * @see #getTransactionType()
	 * @generated
	 */
	EReference getTransactionType_Operation();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.UpdateElementType <em>Update Element Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Update Element Type</em>'.
	 * @see net.opengis.wfs.UpdateElementType
	 * @generated
	 */
	EClass getUpdateElementType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.UpdateElementType#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Property</em>'.
	 * @see net.opengis.wfs.UpdateElementType#getProperty()
	 * @see #getUpdateElementType()
	 * @generated
	 */
	EReference getUpdateElementType_Property();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.UpdateElementType#getFilter <em>Filter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Filter</em>'.
	 * @see net.opengis.wfs.UpdateElementType#getFilter()
	 * @see #getUpdateElementType()
	 * @generated
	 */
	EAttribute getUpdateElementType_Filter();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.UpdateElementType#getHandle <em>Handle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Handle</em>'.
	 * @see net.opengis.wfs.UpdateElementType#getHandle()
	 * @see #getUpdateElementType()
	 * @generated
	 */
	EAttribute getUpdateElementType_Handle();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.UpdateElementType#getTypeName <em>Type Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type Name</em>'.
	 * @see net.opengis.wfs.UpdateElementType#getTypeName()
	 * @see #getUpdateElementType()
	 * @generated
	 */
	EAttribute getUpdateElementType_TypeName();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.WFSLockFeatureResponseType <em>Lock Feature Response Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Lock Feature Response Type</em>'.
	 * @see net.opengis.wfs.WFSLockFeatureResponseType
	 * @generated
	 */
	EClass getWFSLockFeatureResponseType();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.WFSLockFeatureResponseType#getLockId <em>Lock Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lock Id</em>'.
	 * @see net.opengis.wfs.WFSLockFeatureResponseType#getLockId()
	 * @see #getWFSLockFeatureResponseType()
	 * @generated
	 */
	EAttribute getWFSLockFeatureResponseType_LockId();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.WFSLockFeatureResponseType#getFeaturesLocked <em>Features Locked</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Features Locked</em>'.
	 * @see net.opengis.wfs.WFSLockFeatureResponseType#getFeaturesLocked()
	 * @see #getWFSLockFeatureResponseType()
	 * @generated
	 */
	EAttribute getWFSLockFeatureResponseType_FeaturesLocked();

	/**
	 * Returns the meta object for the attribute list '{@link net.opengis.wfs.WFSLockFeatureResponseType#getFeaturesNotLocked <em>Features Not Locked</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Features Not Locked</em>'.
	 * @see net.opengis.wfs.WFSLockFeatureResponseType#getFeaturesNotLocked()
	 * @see #getWFSLockFeatureResponseType()
	 * @generated
	 */
	EAttribute getWFSLockFeatureResponseType_FeaturesNotLocked();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.WFSTransactionResponseType <em>Transaction Response Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Transaction Response Type</em>'.
	 * @see net.opengis.wfs.WFSTransactionResponseType
	 * @generated
	 */
	EClass getWFSTransactionResponseType();

	/**
	 * Returns the meta object for the containment reference list '{@link net.opengis.wfs.WFSTransactionResponseType#getInsertResult <em>Insert Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Insert Result</em>'.
	 * @see net.opengis.wfs.WFSTransactionResponseType#getInsertResult()
	 * @see #getWFSTransactionResponseType()
	 * @generated
	 */
	EReference getWFSTransactionResponseType_InsertResult();

	/**
	 * Returns the meta object for the containment reference '{@link net.opengis.wfs.WFSTransactionResponseType#getTransactionResult <em>Transaction Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Transaction Result</em>'.
	 * @see net.opengis.wfs.WFSTransactionResponseType#getTransactionResult()
	 * @see #getWFSTransactionResponseType()
	 * @generated
	 */
	EReference getWFSTransactionResponseType_TransactionResult();

	/**
	 * Returns the meta object for the attribute '{@link net.opengis.wfs.WFSTransactionResponseType#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see net.opengis.wfs.WFSTransactionResponseType#getVersion()
	 * @see #getWFSTransactionResponseType()
	 * @generated
	 */
	EAttribute getWFSTransactionResponseType_Version();

	/**
	 * Returns the meta object for class '{@link net.opengis.wfs.TransactionOperation <em>Transaction Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Transaction Operation</em>'.
	 * @see net.opengis.wfs.TransactionOperation
	 * @generated
	 */
	EClass getTransactionOperation();

	/**
	 * Returns the meta object for enum '{@link net.opengis.wfs.AllSomeType <em>All Some Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>All Some Type</em>'.
	 * @see net.opengis.wfs.AllSomeType
	 * @generated
	 */
	EEnum getAllSomeType();

	/**
	 * Returns the meta object for data type '{@link net.opengis.wfs.AllSomeType <em>All Some Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>All Some Type Object</em>'.
	 * @see net.opengis.wfs.AllSomeType
	 * @model instanceClass="net.opengis.wfs.AllSomeType"
	 *        extendedMetaData="name='AllSomeType:Object' baseType='AllSomeType'" 
	 * @generated
	 */
	EDataType getAllSomeTypeObject();

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
	 * Returns the meta object for data type '{@link org.geotools.feature.Feature <em>Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Feature</em>'.
	 * @see org.geotools.feature.Feature
	 * @model instanceClass="org.geotools.feature.Feature"
	 * @generated
	 */
	EDataType getFeature();

	/**
	 * Returns the meta object for data type '{@link org.geotools.feature.FeatureCollection <em>Feature Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Feature Collection</em>'.
	 * @see org.geotools.feature.FeatureCollection
	 * @model instanceClass="org.geotools.feature.FeatureCollection"
	 * @generated
	 */
	EDataType getFeatureCollection();

	/**
	 * Returns the meta object for data type '{@link org.opengis.filter.FeatureId <em>Feature Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Feature Id</em>'.
	 * @see org.opengis.filter.FeatureId
	 * @model instanceClass="org.opengis.filter.FeatureId"
	 * @generated
	 */
	EDataType getFeatureId();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	WFSFactory getWFSFactory();

} //WFSPackage
