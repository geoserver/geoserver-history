/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import net.opengis.wfs.DescribeFeatureTypeType;
import net.opengis.wfs.DocumentRoot;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetCapabilitiesType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.QueryType;
import net.opengis.wfs.WFSFactory;
import net.opengis.wfs.WFSPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.impl.EcorePackageImpl;

import org.eclipse.emf.ecore.xml.namespace.impl.XMLNamespacePackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.eclipse.emf.ecore.xml.type.impl.XMLTypePackageImpl;

import org.opengis.filter.Filter;

import org.opengis.filter.expression.PropertyName;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class WFSPackageImpl extends EPackageImpl implements WFSPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass describeFeatureTypeTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass documentRootEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass featureCollectionTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass getCapabilitiesTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass getFeatureTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass queryTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType propertyNameEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType filterEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see net.opengis.wfs.WFSPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private WFSPackageImpl() {
		super(eNS_URI, WFSFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static WFSPackage init() {
		if (isInited) return (WFSPackage)EPackage.Registry.INSTANCE.getEPackage(WFSPackage.eNS_URI);

		// Obtain or create and register package
		WFSPackageImpl theWFSPackage = (WFSPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof WFSPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new WFSPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		EcorePackageImpl.init();
		XMLNamespacePackageImpl.init();
		XMLTypePackageImpl.init();
		EcorePackageImpl.init();
		XMLNamespacePackageImpl.init();
		XMLTypePackageImpl.init();

		// Create package meta-data objects
		theWFSPackage.createPackageContents();

		// Initialize created meta-data
		theWFSPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theWFSPackage.freeze();

		return theWFSPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDescribeFeatureTypeType() {
		return describeFeatureTypeTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDescribeFeatureTypeType_TypeName() {
		return (EAttribute)describeFeatureTypeTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDescribeFeatureTypeType_OutputFormat() {
		return (EAttribute)describeFeatureTypeTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDescribeFeatureTypeType_Service() {
		return (EAttribute)describeFeatureTypeTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDescribeFeatureTypeType_Version() {
		return (EAttribute)describeFeatureTypeTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDocumentRoot() {
		return documentRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_Mixed() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_XMLNSPrefixMap() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_XSISchemaLocation() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_DescribeFeatureType() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_FeatureCollection() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_GetCapabilities() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_GetFeature() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Query() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFeatureCollectionType() {
		return featureCollectionTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGetCapabilitiesType() {
		return getCapabilitiesTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetCapabilitiesType_Service() {
		return (EAttribute)getCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetCapabilitiesType_Version() {
		return (EAttribute)getCapabilitiesTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGetFeatureType() {
		return getFeatureTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGetFeatureType_Query() {
		return (EReference)getFeatureTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetFeatureType_Handle() {
		return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetFeatureType_MaxFeatures() {
		return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetFeatureType_OutputFormat() {
		return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetFeatureType_Service() {
		return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetFeatureType_Version() {
		return (EAttribute)getFeatureTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getQueryType() {
		return queryTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQueryType_PropertyName() {
		return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQueryType_Filter() {
		return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQueryType_FeatureVersion() {
		return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQueryType_Handle() {
		return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQueryType_TypeName() {
		return (EAttribute)queryTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getPropertyName() {
		return propertyNameEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getFilter() {
		return filterEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WFSFactory getWFSFactory() {
		return (WFSFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		describeFeatureTypeTypeEClass = createEClass(DESCRIBE_FEATURE_TYPE_TYPE);
		createEAttribute(describeFeatureTypeTypeEClass, DESCRIBE_FEATURE_TYPE_TYPE__TYPE_NAME);
		createEAttribute(describeFeatureTypeTypeEClass, DESCRIBE_FEATURE_TYPE_TYPE__OUTPUT_FORMAT);
		createEAttribute(describeFeatureTypeTypeEClass, DESCRIBE_FEATURE_TYPE_TYPE__SERVICE);
		createEAttribute(describeFeatureTypeTypeEClass, DESCRIBE_FEATURE_TYPE_TYPE__VERSION);

		documentRootEClass = createEClass(DOCUMENT_ROOT);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__DESCRIBE_FEATURE_TYPE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__FEATURE_COLLECTION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__GET_CAPABILITIES);
		createEReference(documentRootEClass, DOCUMENT_ROOT__GET_FEATURE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__QUERY);

		featureCollectionTypeEClass = createEClass(FEATURE_COLLECTION_TYPE);

		getCapabilitiesTypeEClass = createEClass(GET_CAPABILITIES_TYPE);
		createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__SERVICE);
		createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__VERSION);

		getFeatureTypeEClass = createEClass(GET_FEATURE_TYPE);
		createEReference(getFeatureTypeEClass, GET_FEATURE_TYPE__QUERY);
		createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__HANDLE);
		createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__MAX_FEATURES);
		createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__OUTPUT_FORMAT);
		createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__SERVICE);
		createEAttribute(getFeatureTypeEClass, GET_FEATURE_TYPE__VERSION);

		queryTypeEClass = createEClass(QUERY_TYPE);
		createEAttribute(queryTypeEClass, QUERY_TYPE__PROPERTY_NAME);
		createEAttribute(queryTypeEClass, QUERY_TYPE__FILTER);
		createEAttribute(queryTypeEClass, QUERY_TYPE__FEATURE_VERSION);
		createEAttribute(queryTypeEClass, QUERY_TYPE__HANDLE);
		createEAttribute(queryTypeEClass, QUERY_TYPE__TYPE_NAME);

		// Create data types
		propertyNameEDataType = createEDataType(PROPERTY_NAME);
		filterEDataType = createEDataType(FILTER);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		XMLTypePackageImpl theXMLTypePackage = (XMLTypePackageImpl)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);
		EcorePackageImpl theEcorePackage = (EcorePackageImpl)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(describeFeatureTypeTypeEClass, DescribeFeatureTypeType.class, "DescribeFeatureTypeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDescribeFeatureTypeType_TypeName(), theXMLTypePackage.getQName(), "typeName", null, 0, 1, DescribeFeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDescribeFeatureTypeType_OutputFormat(), theXMLTypePackage.getString(), "outputFormat", "XMLSCHEMA", 0, 1, DescribeFeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDescribeFeatureTypeType_Service(), theXMLTypePackage.getString(), "service", "WFS", 1, 1, DescribeFeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDescribeFeatureTypeType_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 1, 1, DescribeFeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDocumentRoot_Mixed(), theEcorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XMLNSPrefixMap(), theEcorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XSISchemaLocation(), theEcorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_DescribeFeatureType(), this.getDescribeFeatureTypeType(), null, "describeFeatureType", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_FeatureCollection(), this.getFeatureCollectionType(), null, "featureCollection", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_GetCapabilities(), this.getGetCapabilitiesType(), null, "getCapabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_GetFeature(), this.getGetFeatureType(), null, "getFeature", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Query(), this.getQueryType(), null, "query", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(featureCollectionTypeEClass, FeatureCollectionType.class, "FeatureCollectionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(getCapabilitiesTypeEClass, GetCapabilitiesType.class, "GetCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGetCapabilitiesType_Service(), theXMLTypePackage.getString(), "service", "WFS", 1, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetCapabilitiesType_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(getFeatureTypeEClass, GetFeatureType.class, "GetFeatureType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGetFeatureType_Query(), this.getQueryType(), null, "query", null, 1, -1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetFeatureType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetFeatureType_MaxFeatures(), theXMLTypePackage.getPositiveInteger(), "maxFeatures", null, 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetFeatureType_OutputFormat(), theXMLTypePackage.getString(), "outputFormat", "GML2", 0, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetFeatureType_Service(), theXMLTypePackage.getString(), "service", "WFS", 1, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetFeatureType_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 1, 1, GetFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(queryTypeEClass, QueryType.class, "QueryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getQueryType_PropertyName(), this.getPropertyName(), "propertyName", null, 0, -1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getQueryType_Filter(), this.getFilter(), "filter", null, 0, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getQueryType_FeatureVersion(), theXMLTypePackage.getString(), "featureVersion", null, 0, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getQueryType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getQueryType_TypeName(), theXMLTypePackage.getQName(), "typeName", null, 1, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(propertyNameEDataType, PropertyName.class, "PropertyName", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(filterEDataType, Filter.class, "Filter", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";		
		addAnnotation
		  (describeFeatureTypeTypeEClass, 
		   source, 
		   new String[] {
			 "name", "DescribeFeatureTypeType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getDescribeFeatureTypeType_TypeName(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "TypeName",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDescribeFeatureTypeType_OutputFormat(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "outputFormat"
		   });			
		addAnnotation
		  (getDescribeFeatureTypeType_Service(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "service"
		   });		
		addAnnotation
		  (getDescribeFeatureTypeType_Version(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "version"
		   });		
		addAnnotation
		  (documentRootEClass, 
		   source, 
		   new String[] {
			 "name", "",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getDocumentRoot_Mixed(), 
		   source, 
		   new String[] {
			 "kind", "elementWildcard",
			 "name", ":mixed"
		   });		
		addAnnotation
		  (getDocumentRoot_XMLNSPrefixMap(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xmlns:prefix"
		   });		
		addAnnotation
		  (getDocumentRoot_XSISchemaLocation(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xsi:schemaLocation"
		   });		
		addAnnotation
		  (getDocumentRoot_DescribeFeatureType(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "DescribeFeatureType",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_FeatureCollection(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "FeatureCollection",
			 "namespace", "##targetNamespace",
			 "affiliation", "http://www.opengis.net/gml#_FeatureCollection"
		   });			
		addAnnotation
		  (getDocumentRoot_GetCapabilities(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "GetCapabilities",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_GetFeature(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "GetFeature",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Query(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Query",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (featureCollectionTypeEClass, 
		   source, 
		   new String[] {
			 "name", "FeatureCollectionType",
			 "kind", "empty"
		   });			
		addAnnotation
		  (getCapabilitiesTypeEClass, 
		   source, 
		   new String[] {
			 "name", "GetCapabilitiesType",
			 "kind", "empty"
		   });			
		addAnnotation
		  (getGetCapabilitiesType_Service(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "service"
		   });		
		addAnnotation
		  (getGetCapabilitiesType_Version(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "version"
		   });		
		addAnnotation
		  (getFeatureTypeEClass, 
		   source, 
		   new String[] {
			 "name", "GetFeatureType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getGetFeatureType_Query(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Query",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getGetFeatureType_Handle(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "handle"
		   });		
		addAnnotation
		  (getGetFeatureType_MaxFeatures(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "maxFeatures"
		   });			
		addAnnotation
		  (getGetFeatureType_OutputFormat(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "outputFormat"
		   });			
		addAnnotation
		  (getGetFeatureType_Service(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "service"
		   });		
		addAnnotation
		  (getGetFeatureType_Version(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "version"
		   });		
		addAnnotation
		  (queryTypeEClass, 
		   source, 
		   new String[] {
			 "name", "QueryType",
			 "kind", "elementOnly"
		   });					
		addAnnotation
		  (getQueryType_FeatureVersion(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "featureVersion"
		   });			
		addAnnotation
		  (getQueryType_Handle(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "handle"
		   });		
		addAnnotation
		  (getQueryType_TypeName(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "typeName"
		   });
	}

} //WFSPackageImpl
