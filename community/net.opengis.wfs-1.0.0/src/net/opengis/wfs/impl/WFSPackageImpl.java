/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import javax.xml.namespace.QName;

import net.opengis.wfs.AllSomeType;
import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.DescribeFeatureTypeType;
import net.opengis.wfs.DocumentRoot;
import net.opengis.wfs.EmptyType;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.FeaturesLockedType;
import net.opengis.wfs.FeaturesNotLockedType;
import net.opengis.wfs.GetCapabilitiesType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.GetFeatureWithLockType;
import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.InsertResultType;
import net.opengis.wfs.LockFeatureType;
import net.opengis.wfs.LockType;
import net.opengis.wfs.NativeType;
import net.opengis.wfs.PropertyType;
import net.opengis.wfs.QueryType;
import net.opengis.wfs.StatusType;
import net.opengis.wfs.TransactionOperation;
import net.opengis.wfs.TransactionResultType;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.UpdateElementType;
import net.opengis.wfs.WFSFactory;
import net.opengis.wfs.WFSLockFeatureResponseType;
import net.opengis.wfs.WFSPackage;
import net.opengis.wfs.WFSTransactionResponseType;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.impl.EcorePackageImpl;

import org.eclipse.emf.ecore.xml.namespace.impl.XMLNamespacePackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.eclipse.emf.ecore.xml.type.impl.XMLTypePackageImpl;

import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;

import org.opengis.filter.FeatureId;
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
	private EClass deleteElementTypeEClass = null;

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
	private EClass emptyTypeEClass = null;

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
	private EClass featuresLockedTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass featuresNotLockedTypeEClass = null;

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
	private EClass getFeatureWithLockTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass insertElementTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass insertResultTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lockFeatureTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lockTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nativeTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass propertyTypeEClass = null;

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
	private EClass statusTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass transactionResultTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass transactionTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass updateElementTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass wfsLockFeatureResponseTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass wfsTransactionResponseTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass transactionOperationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum allSomeTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType allSomeTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType qNameEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType filterEDataType = null;

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
	private EDataType featureEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType featureCollectionEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType featureIdEDataType = null;

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
	public EClass getDeleteElementType() {
		return deleteElementTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDeleteElementType_Filter() {
		return (EAttribute)deleteElementTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDeleteElementType_Handle() {
		return (EAttribute)deleteElementTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDeleteElementType_TypeName() {
		return (EAttribute)deleteElementTypeEClass.getEStructuralFeatures().get(2);
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
	public EReference getDocumentRoot_Delete() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Failed() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_GetFeatureWithLock() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Insert() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_LockFeature() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_LockId() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Native() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Partial() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Property() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Success() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Transaction() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Update() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_WfsLockFeatureResponse() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(20);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_WfsTransactionResponse() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(21);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEmptyType() {
		return emptyTypeEClass;
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
	public EClass getFeaturesLockedType() {
		return featuresLockedTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFeaturesLockedType_Group() {
		return (EAttribute)featuresLockedTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFeaturesLockedType_FeatureId() {
		return (EAttribute)featuresLockedTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFeaturesNotLockedType() {
		return featuresNotLockedTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFeaturesNotLockedType_Group() {
		return (EAttribute)featuresNotLockedTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFeaturesNotLockedType_FeatureId() {
		return (EAttribute)featuresNotLockedTypeEClass.getEStructuralFeatures().get(1);
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
	public EClass getGetFeatureWithLockType() {
		return getFeatureWithLockTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGetFeatureWithLockType_Query() {
		return (EReference)getFeatureWithLockTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetFeatureWithLockType_Expiry() {
		return (EAttribute)getFeatureWithLockTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetFeatureWithLockType_Handle() {
		return (EAttribute)getFeatureWithLockTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetFeatureWithLockType_MaxFeatures() {
		return (EAttribute)getFeatureWithLockTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetFeatureWithLockType_OutputFormat() {
		return (EAttribute)getFeatureWithLockTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetFeatureWithLockType_Service() {
		return (EAttribute)getFeatureWithLockTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGetFeatureWithLockType_Version() {
		return (EAttribute)getFeatureWithLockTypeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInsertElementType() {
		return insertElementTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInsertElementType_Feature() {
		return (EAttribute)insertElementTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInsertElementType_Handle() {
		return (EAttribute)insertElementTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInsertResultType() {
		return insertResultTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInsertResultType_FeatureId() {
		return (EAttribute)insertResultTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInsertResultType_Handle() {
		return (EAttribute)insertResultTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLockFeatureType() {
		return lockFeatureTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLockFeatureType_Lock() {
		return (EReference)lockFeatureTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLockFeatureType_Expiry() {
		return (EAttribute)lockFeatureTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLockFeatureType_LockAction() {
		return (EAttribute)lockFeatureTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLockFeatureType_Service() {
		return (EAttribute)lockFeatureTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLockFeatureType_Version() {
		return (EAttribute)lockFeatureTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLockType() {
		return lockTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLockType_Filter() {
		return (EAttribute)lockTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLockType_Handle() {
		return (EAttribute)lockTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLockType_TypeName() {
		return (EAttribute)lockTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNativeType() {
		return nativeTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNativeType_SafeToIgnore() {
		return (EAttribute)nativeTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNativeType_VendorId() {
		return (EAttribute)nativeTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPropertyType() {
		return propertyTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPropertyType_Name() {
		return (EAttribute)propertyTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPropertyType_Value() {
		return (EAttribute)propertyTypeEClass.getEStructuralFeatures().get(1);
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
	public EClass getStatusType() {
		return statusTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStatusType_SUCCESS() {
		return (EReference)statusTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStatusType_FAILED() {
		return (EReference)statusTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStatusType_PARTIAL() {
		return (EReference)statusTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTransactionResultType() {
		return transactionResultTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTransactionResultType_Status() {
		return (EReference)transactionResultTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTransactionResultType_Locator() {
		return (EAttribute)transactionResultTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTransactionResultType_Message() {
		return (EAttribute)transactionResultTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTransactionResultType_Handle() {
		return (EAttribute)transactionResultTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTransactionType() {
		return transactionTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTransactionType_LockId() {
		return (EAttribute)transactionTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTransactionType_Group() {
		return (EAttribute)transactionTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTransactionType_Handle() {
		return (EAttribute)transactionTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTransactionType_ReleaseAction() {
		return (EAttribute)transactionTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTransactionType_Service() {
		return (EAttribute)transactionTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTransactionType_Version() {
		return (EAttribute)transactionTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTransactionType_Operation() {
		return (EReference)transactionTypeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUpdateElementType() {
		return updateElementTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUpdateElementType_Property() {
		return (EReference)updateElementTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUpdateElementType_Filter() {
		return (EAttribute)updateElementTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUpdateElementType_Handle() {
		return (EAttribute)updateElementTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUpdateElementType_TypeName() {
		return (EAttribute)updateElementTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getWFSLockFeatureResponseType() {
		return wfsLockFeatureResponseTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWFSLockFeatureResponseType_LockId() {
		return (EAttribute)wfsLockFeatureResponseTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWFSLockFeatureResponseType_FeaturesLocked() {
		return (EAttribute)wfsLockFeatureResponseTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWFSLockFeatureResponseType_FeaturesNotLocked() {
		return (EAttribute)wfsLockFeatureResponseTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getWFSTransactionResponseType() {
		return wfsTransactionResponseTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getWFSTransactionResponseType_InsertResult() {
		return (EReference)wfsTransactionResponseTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getWFSTransactionResponseType_TransactionResult() {
		return (EReference)wfsTransactionResponseTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWFSTransactionResponseType_Version() {
		return (EAttribute)wfsTransactionResponseTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTransactionOperation() {
		return transactionOperationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getAllSomeType() {
		return allSomeTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getAllSomeTypeObject() {
		return allSomeTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getQName() {
		return qNameEDataType;
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
	public EDataType getPropertyName() {
		return propertyNameEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getFeature() {
		return featureEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getFeatureCollection() {
		return featureCollectionEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getFeatureId() {
		return featureIdEDataType;
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
		deleteElementTypeEClass = createEClass(DELETE_ELEMENT_TYPE);
		createEAttribute(deleteElementTypeEClass, DELETE_ELEMENT_TYPE__FILTER);
		createEAttribute(deleteElementTypeEClass, DELETE_ELEMENT_TYPE__HANDLE);
		createEAttribute(deleteElementTypeEClass, DELETE_ELEMENT_TYPE__TYPE_NAME);

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
		createEReference(documentRootEClass, DOCUMENT_ROOT__DELETE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__FAILED);
		createEReference(documentRootEClass, DOCUMENT_ROOT__GET_FEATURE_WITH_LOCK);
		createEReference(documentRootEClass, DOCUMENT_ROOT__INSERT);
		createEReference(documentRootEClass, DOCUMENT_ROOT__LOCK_FEATURE);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__LOCK_ID);
		createEReference(documentRootEClass, DOCUMENT_ROOT__NATIVE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__PARTIAL);
		createEReference(documentRootEClass, DOCUMENT_ROOT__PROPERTY);
		createEReference(documentRootEClass, DOCUMENT_ROOT__SUCCESS);
		createEReference(documentRootEClass, DOCUMENT_ROOT__TRANSACTION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__UPDATE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__WFS_LOCK_FEATURE_RESPONSE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__WFS_TRANSACTION_RESPONSE);

		emptyTypeEClass = createEClass(EMPTY_TYPE);

		featureCollectionTypeEClass = createEClass(FEATURE_COLLECTION_TYPE);

		featuresLockedTypeEClass = createEClass(FEATURES_LOCKED_TYPE);
		createEAttribute(featuresLockedTypeEClass, FEATURES_LOCKED_TYPE__GROUP);
		createEAttribute(featuresLockedTypeEClass, FEATURES_LOCKED_TYPE__FEATURE_ID);

		featuresNotLockedTypeEClass = createEClass(FEATURES_NOT_LOCKED_TYPE);
		createEAttribute(featuresNotLockedTypeEClass, FEATURES_NOT_LOCKED_TYPE__GROUP);
		createEAttribute(featuresNotLockedTypeEClass, FEATURES_NOT_LOCKED_TYPE__FEATURE_ID);

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

		getFeatureWithLockTypeEClass = createEClass(GET_FEATURE_WITH_LOCK_TYPE);
		createEReference(getFeatureWithLockTypeEClass, GET_FEATURE_WITH_LOCK_TYPE__QUERY);
		createEAttribute(getFeatureWithLockTypeEClass, GET_FEATURE_WITH_LOCK_TYPE__EXPIRY);
		createEAttribute(getFeatureWithLockTypeEClass, GET_FEATURE_WITH_LOCK_TYPE__HANDLE);
		createEAttribute(getFeatureWithLockTypeEClass, GET_FEATURE_WITH_LOCK_TYPE__MAX_FEATURES);
		createEAttribute(getFeatureWithLockTypeEClass, GET_FEATURE_WITH_LOCK_TYPE__OUTPUT_FORMAT);
		createEAttribute(getFeatureWithLockTypeEClass, GET_FEATURE_WITH_LOCK_TYPE__SERVICE);
		createEAttribute(getFeatureWithLockTypeEClass, GET_FEATURE_WITH_LOCK_TYPE__VERSION);

		insertElementTypeEClass = createEClass(INSERT_ELEMENT_TYPE);
		createEAttribute(insertElementTypeEClass, INSERT_ELEMENT_TYPE__FEATURE);
		createEAttribute(insertElementTypeEClass, INSERT_ELEMENT_TYPE__HANDLE);

		insertResultTypeEClass = createEClass(INSERT_RESULT_TYPE);
		createEAttribute(insertResultTypeEClass, INSERT_RESULT_TYPE__FEATURE_ID);
		createEAttribute(insertResultTypeEClass, INSERT_RESULT_TYPE__HANDLE);

		lockFeatureTypeEClass = createEClass(LOCK_FEATURE_TYPE);
		createEReference(lockFeatureTypeEClass, LOCK_FEATURE_TYPE__LOCK);
		createEAttribute(lockFeatureTypeEClass, LOCK_FEATURE_TYPE__EXPIRY);
		createEAttribute(lockFeatureTypeEClass, LOCK_FEATURE_TYPE__LOCK_ACTION);
		createEAttribute(lockFeatureTypeEClass, LOCK_FEATURE_TYPE__SERVICE);
		createEAttribute(lockFeatureTypeEClass, LOCK_FEATURE_TYPE__VERSION);

		lockTypeEClass = createEClass(LOCK_TYPE);
		createEAttribute(lockTypeEClass, LOCK_TYPE__FILTER);
		createEAttribute(lockTypeEClass, LOCK_TYPE__HANDLE);
		createEAttribute(lockTypeEClass, LOCK_TYPE__TYPE_NAME);

		nativeTypeEClass = createEClass(NATIVE_TYPE);
		createEAttribute(nativeTypeEClass, NATIVE_TYPE__SAFE_TO_IGNORE);
		createEAttribute(nativeTypeEClass, NATIVE_TYPE__VENDOR_ID);

		propertyTypeEClass = createEClass(PROPERTY_TYPE);
		createEAttribute(propertyTypeEClass, PROPERTY_TYPE__NAME);
		createEAttribute(propertyTypeEClass, PROPERTY_TYPE__VALUE);

		queryTypeEClass = createEClass(QUERY_TYPE);
		createEAttribute(queryTypeEClass, QUERY_TYPE__PROPERTY_NAME);
		createEAttribute(queryTypeEClass, QUERY_TYPE__FILTER);
		createEAttribute(queryTypeEClass, QUERY_TYPE__FEATURE_VERSION);
		createEAttribute(queryTypeEClass, QUERY_TYPE__HANDLE);
		createEAttribute(queryTypeEClass, QUERY_TYPE__TYPE_NAME);

		statusTypeEClass = createEClass(STATUS_TYPE);
		createEReference(statusTypeEClass, STATUS_TYPE__SUCCESS);
		createEReference(statusTypeEClass, STATUS_TYPE__FAILED);
		createEReference(statusTypeEClass, STATUS_TYPE__PARTIAL);

		transactionResultTypeEClass = createEClass(TRANSACTION_RESULT_TYPE);
		createEReference(transactionResultTypeEClass, TRANSACTION_RESULT_TYPE__STATUS);
		createEAttribute(transactionResultTypeEClass, TRANSACTION_RESULT_TYPE__LOCATOR);
		createEAttribute(transactionResultTypeEClass, TRANSACTION_RESULT_TYPE__MESSAGE);
		createEAttribute(transactionResultTypeEClass, TRANSACTION_RESULT_TYPE__HANDLE);

		transactionTypeEClass = createEClass(TRANSACTION_TYPE);
		createEAttribute(transactionTypeEClass, TRANSACTION_TYPE__LOCK_ID);
		createEAttribute(transactionTypeEClass, TRANSACTION_TYPE__GROUP);
		createEAttribute(transactionTypeEClass, TRANSACTION_TYPE__HANDLE);
		createEAttribute(transactionTypeEClass, TRANSACTION_TYPE__RELEASE_ACTION);
		createEAttribute(transactionTypeEClass, TRANSACTION_TYPE__SERVICE);
		createEAttribute(transactionTypeEClass, TRANSACTION_TYPE__VERSION);
		createEReference(transactionTypeEClass, TRANSACTION_TYPE__OPERATION);

		updateElementTypeEClass = createEClass(UPDATE_ELEMENT_TYPE);
		createEReference(updateElementTypeEClass, UPDATE_ELEMENT_TYPE__PROPERTY);
		createEAttribute(updateElementTypeEClass, UPDATE_ELEMENT_TYPE__FILTER);
		createEAttribute(updateElementTypeEClass, UPDATE_ELEMENT_TYPE__HANDLE);
		createEAttribute(updateElementTypeEClass, UPDATE_ELEMENT_TYPE__TYPE_NAME);

		wfsLockFeatureResponseTypeEClass = createEClass(WFS_LOCK_FEATURE_RESPONSE_TYPE);
		createEAttribute(wfsLockFeatureResponseTypeEClass, WFS_LOCK_FEATURE_RESPONSE_TYPE__LOCK_ID);
		createEAttribute(wfsLockFeatureResponseTypeEClass, WFS_LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED);
		createEAttribute(wfsLockFeatureResponseTypeEClass, WFS_LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED);

		wfsTransactionResponseTypeEClass = createEClass(WFS_TRANSACTION_RESPONSE_TYPE);
		createEReference(wfsTransactionResponseTypeEClass, WFS_TRANSACTION_RESPONSE_TYPE__INSERT_RESULT);
		createEReference(wfsTransactionResponseTypeEClass, WFS_TRANSACTION_RESPONSE_TYPE__TRANSACTION_RESULT);
		createEAttribute(wfsTransactionResponseTypeEClass, WFS_TRANSACTION_RESPONSE_TYPE__VERSION);

		transactionOperationEClass = createEClass(TRANSACTION_OPERATION);

		// Create enums
		allSomeTypeEEnum = createEEnum(ALL_SOME_TYPE);

		// Create data types
		allSomeTypeObjectEDataType = createEDataType(ALL_SOME_TYPE_OBJECT);
		qNameEDataType = createEDataType(QNAME);
		filterEDataType = createEDataType(FILTER);
		propertyNameEDataType = createEDataType(PROPERTY_NAME);
		featureEDataType = createEDataType(FEATURE);
		featureCollectionEDataType = createEDataType(FEATURE_COLLECTION);
		featureIdEDataType = createEDataType(FEATURE_ID);
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

		// Add supertypes to classes
		deleteElementTypeEClass.getESuperTypes().add(this.getTransactionOperation());
		insertElementTypeEClass.getESuperTypes().add(this.getTransactionOperation());
		nativeTypeEClass.getESuperTypes().add(this.getTransactionOperation());
		updateElementTypeEClass.getESuperTypes().add(this.getTransactionOperation());

		// Initialize classes and features; add operations and parameters
		initEClass(deleteElementTypeEClass, DeleteElementType.class, "DeleteElementType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDeleteElementType_Filter(), this.getFilter(), "filter", null, 0, 1, DeleteElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDeleteElementType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, DeleteElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDeleteElementType_TypeName(), this.getQName(), "typeName", null, 0, 1, DeleteElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(describeFeatureTypeTypeEClass, DescribeFeatureTypeType.class, "DescribeFeatureTypeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDescribeFeatureTypeType_TypeName(), this.getQName(), "typeName", null, 0, -1, DescribeFeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDescribeFeatureTypeType_OutputFormat(), theXMLTypePackage.getString(), "outputFormat", "XMLSCHEMA", 0, 1, DescribeFeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDescribeFeatureTypeType_Service(), theXMLTypePackage.getString(), "service", "WFS", 1, 1, DescribeFeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDescribeFeatureTypeType_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 1, 1, DescribeFeatureTypeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_DescribeFeatureType(), this.getDescribeFeatureTypeType(), null, "describeFeatureType", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_FeatureCollection(), this.getFeatureCollectionType(), null, "featureCollection", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_GetCapabilities(), this.getGetCapabilitiesType(), null, "getCapabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_GetFeature(), this.getGetFeatureType(), null, "getFeature", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Query(), this.getQueryType(), null, "query", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Delete(), this.getDeleteElementType(), null, "delete", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Failed(), this.getEmptyType(), null, "failed", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_GetFeatureWithLock(), this.getGetFeatureWithLockType(), null, "getFeatureWithLock", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Insert(), this.getInsertElementType(), null, "insert", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_LockFeature(), this.getLockFeatureType(), null, "lockFeature", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_LockId(), theXMLTypePackage.getString(), "lockId", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Native(), this.getNativeType(), null, "native", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Partial(), this.getEmptyType(), null, "partial", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Property(), this.getPropertyType(), null, "property", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Success(), this.getEmptyType(), null, "success", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Transaction(), this.getTransactionType(), null, "transaction", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Update(), this.getUpdateElementType(), null, "update", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_WfsLockFeatureResponse(), this.getWFSLockFeatureResponseType(), null, "wfsLockFeatureResponse", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_WfsTransactionResponse(), this.getWFSTransactionResponseType(), null, "wfsTransactionResponse", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(emptyTypeEClass, EmptyType.class, "EmptyType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(featureCollectionTypeEClass, FeatureCollectionType.class, "FeatureCollectionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(featuresLockedTypeEClass, FeaturesLockedType.class, "FeaturesLockedType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFeaturesLockedType_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, FeaturesLockedType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFeaturesLockedType_FeatureId(), theXMLTypePackage.getAnySimpleType(), "featureId", null, 1, 1, FeaturesLockedType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(featuresNotLockedTypeEClass, FeaturesNotLockedType.class, "FeaturesNotLockedType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFeaturesNotLockedType_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, FeaturesNotLockedType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFeaturesNotLockedType_FeatureId(), theXMLTypePackage.getAnySimpleType(), "featureId", null, 1, 1, FeaturesNotLockedType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);

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

		initEClass(getFeatureWithLockTypeEClass, GetFeatureWithLockType.class, "GetFeatureWithLockType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGetFeatureWithLockType_Query(), this.getQueryType(), null, "query", null, 1, -1, GetFeatureWithLockType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetFeatureWithLockType_Expiry(), theXMLTypePackage.getPositiveInteger(), "expiry", null, 0, 1, GetFeatureWithLockType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetFeatureWithLockType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, GetFeatureWithLockType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetFeatureWithLockType_MaxFeatures(), theXMLTypePackage.getPositiveInteger(), "maxFeatures", null, 0, 1, GetFeatureWithLockType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetFeatureWithLockType_OutputFormat(), theXMLTypePackage.getString(), "outputFormat", "GML2", 0, 1, GetFeatureWithLockType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetFeatureWithLockType_Service(), theXMLTypePackage.getString(), "service", "WFS", 1, 1, GetFeatureWithLockType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGetFeatureWithLockType_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 1, 1, GetFeatureWithLockType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(insertElementTypeEClass, InsertElementType.class, "InsertElementType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getInsertElementType_Feature(), this.getFeatureCollection(), "feature", null, 0, 1, InsertElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInsertElementType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, InsertElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(insertResultTypeEClass, InsertResultType.class, "InsertResultType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getInsertResultType_FeatureId(), theXMLTypePackage.getAnySimpleType(), "featureId", null, 1, 1, InsertResultType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInsertResultType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, InsertResultType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lockFeatureTypeEClass, LockFeatureType.class, "LockFeatureType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLockFeatureType_Lock(), this.getLockType(), null, "lock", null, 1, -1, LockFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLockFeatureType_Expiry(), theXMLTypePackage.getPositiveInteger(), "expiry", null, 0, 1, LockFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLockFeatureType_LockAction(), this.getAllSomeType(), "lockAction", "ALL", 0, 1, LockFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLockFeatureType_Service(), theXMLTypePackage.getString(), "service", "WFS", 1, 1, LockFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLockFeatureType_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 1, 1, LockFeatureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lockTypeEClass, LockType.class, "LockType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLockType_Filter(), this.getFilter(), "filter", null, 0, 1, LockType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLockType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, LockType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLockType_TypeName(), this.getQName(), "typeName", null, 0, 1, LockType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(nativeTypeEClass, NativeType.class, "NativeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNativeType_SafeToIgnore(), theXMLTypePackage.getBoolean(), "safeToIgnore", null, 1, 1, NativeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNativeType_VendorId(), theXMLTypePackage.getString(), "vendorId", null, 1, 1, NativeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(propertyTypeEClass, PropertyType.class, "PropertyType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPropertyType_Name(), ecorePackage.getEString(), "name", null, 0, 1, PropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPropertyType_Value(), ecorePackage.getEJavaObject(), "value", null, 0, 1, PropertyType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(queryTypeEClass, QueryType.class, "QueryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getQueryType_PropertyName(), this.getPropertyName(), "propertyName", null, 0, -1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getQueryType_Filter(), this.getFilter(), "filter", null, 0, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getQueryType_FeatureVersion(), theXMLTypePackage.getString(), "featureVersion", null, 0, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getQueryType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getQueryType_TypeName(), this.getQName(), "typeName", null, 0, 1, QueryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(statusTypeEClass, StatusType.class, "StatusType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getStatusType_SUCCESS(), this.getEmptyType(), null, "sUCCESS", null, 0, 1, StatusType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getStatusType_FAILED(), this.getEmptyType(), null, "fAILED", null, 0, 1, StatusType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getStatusType_PARTIAL(), this.getEmptyType(), null, "pARTIAL", null, 0, 1, StatusType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(transactionResultTypeEClass, TransactionResultType.class, "TransactionResultType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTransactionResultType_Status(), this.getStatusType(), null, "status", null, 1, 1, TransactionResultType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransactionResultType_Locator(), theXMLTypePackage.getString(), "locator", null, 0, 1, TransactionResultType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransactionResultType_Message(), theXMLTypePackage.getString(), "message", null, 0, 1, TransactionResultType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransactionResultType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, TransactionResultType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(transactionTypeEClass, TransactionType.class, "TransactionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTransactionType_LockId(), theXMLTypePackage.getString(), "lockId", null, 0, 1, TransactionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransactionType_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, TransactionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransactionType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, TransactionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransactionType_ReleaseAction(), this.getAllSomeType(), "releaseAction", "ALL", 0, 1, TransactionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransactionType_Service(), theXMLTypePackage.getString(), "service", "WFS", 1, 1, TransactionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransactionType_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 1, 1, TransactionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTransactionType_Operation(), this.getTransactionOperation(), null, "operation", null, 0, -1, TransactionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(updateElementTypeEClass, UpdateElementType.class, "UpdateElementType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getUpdateElementType_Property(), this.getPropertyType(), null, "property", null, 1, -1, UpdateElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUpdateElementType_Filter(), this.getFilter(), "filter", null, 0, 1, UpdateElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUpdateElementType_Handle(), theXMLTypePackage.getString(), "handle", null, 0, 1, UpdateElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUpdateElementType_TypeName(), this.getQName(), "typeName", null, 0, 1, UpdateElementType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(wfsLockFeatureResponseTypeEClass, WFSLockFeatureResponseType.class, "WFSLockFeatureResponseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getWFSLockFeatureResponseType_LockId(), theXMLTypePackage.getString(), "lockId", null, 1, 1, WFSLockFeatureResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWFSLockFeatureResponseType_FeaturesLocked(), this.getFeatureId(), "featuresLocked", null, 0, -1, WFSLockFeatureResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWFSLockFeatureResponseType_FeaturesNotLocked(), this.getFeatureId(), "featuresNotLocked", null, 0, -1, WFSLockFeatureResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(wfsTransactionResponseTypeEClass, WFSTransactionResponseType.class, "WFSTransactionResponseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getWFSTransactionResponseType_InsertResult(), this.getInsertResultType(), null, "insertResult", null, 0, -1, WFSTransactionResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getWFSTransactionResponseType_TransactionResult(), this.getTransactionResultType(), null, "transactionResult", null, 1, 1, WFSTransactionResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWFSTransactionResponseType_Version(), theXMLTypePackage.getString(), "version", "1.0.0", 1, 1, WFSTransactionResponseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(transactionOperationEClass, TransactionOperation.class, "TransactionOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Initialize enums and add enum literals
		initEEnum(allSomeTypeEEnum, AllSomeType.class, "AllSomeType");
		addEEnumLiteral(allSomeTypeEEnum, AllSomeType.ALL_LITERAL);
		addEEnumLiteral(allSomeTypeEEnum, AllSomeType.SOME_LITERAL);

		// Initialize data types
		initEDataType(allSomeTypeObjectEDataType, AllSomeType.class, "AllSomeTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(qNameEDataType, QName.class, "QName", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(filterEDataType, Filter.class, "Filter", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(propertyNameEDataType, PropertyName.class, "PropertyName", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(featureEDataType, Feature.class, "Feature", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(featureCollectionEDataType, FeatureCollection.class, "FeatureCollection", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(featureIdEDataType, FeatureId.class, "FeatureId", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

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
		  (deleteElementTypeEClass, 
		   source, 
		   new String[] {
			 "name", "DeleteElementType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getDeleteElementType_Handle(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "handle"
		   });		
		addAnnotation
		  (describeFeatureTypeTypeEClass, 
		   source, 
		   new String[] {
			 "name", "DescribeFeatureTypeType",
			 "kind", "elementOnly"
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
		  (getDocumentRoot_Delete(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Delete",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Failed(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "FAILED",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_GetFeatureWithLock(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "GetFeatureWithLock",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Insert(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Insert",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_LockFeature(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "LockFeature",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_LockId(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "LockId",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Native(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Native",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Partial(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "PARTIAL",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Property(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Property",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Success(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "SUCCESS",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Transaction(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Transaction",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_Update(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Update",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_WfsLockFeatureResponse(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "WFS_LockFeatureResponse",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getDocumentRoot_WfsTransactionResponse(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "WFS_TransactionResponse",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (emptyTypeEClass, 
		   source, 
		   new String[] {
			 "name", "EmptyType",
			 "kind", "empty"
		   });		
		addAnnotation
		  (featureCollectionTypeEClass, 
		   source, 
		   new String[] {
			 "name", "FeatureCollectionType",
			 "kind", "empty"
		   });			
		addAnnotation
		  (featuresLockedTypeEClass, 
		   source, 
		   new String[] {
			 "name", "FeaturesLockedType",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getFeaturesLockedType_Group(), 
		   source, 
		   new String[] {
			 "kind", "group",
			 "name", "group:0"
		   });		
		addAnnotation
		  (getFeaturesLockedType_FeatureId(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "FeatureId",
			 "namespace", "http://www.opengis.net/ogc",
			 "group", "#group:0"
		   });		
		addAnnotation
		  (featuresNotLockedTypeEClass, 
		   source, 
		   new String[] {
			 "name", "FeaturesNotLockedType",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getFeaturesNotLockedType_Group(), 
		   source, 
		   new String[] {
			 "kind", "group",
			 "name", "group:0"
		   });		
		addAnnotation
		  (getFeaturesNotLockedType_FeatureId(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "FeatureId",
			 "namespace", "http://www.opengis.net/ogc",
			 "group", "#group:0"
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
		  (getFeatureWithLockTypeEClass, 
		   source, 
		   new String[] {
			 "name", "GetFeatureWithLockType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getGetFeatureWithLockType_Query(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Query",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getGetFeatureWithLockType_Expiry(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "expiry"
		   });		
		addAnnotation
		  (getGetFeatureWithLockType_Handle(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "handle"
		   });		
		addAnnotation
		  (getGetFeatureWithLockType_MaxFeatures(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "maxFeatures"
		   });		
		addAnnotation
		  (getGetFeatureWithLockType_OutputFormat(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "outputFormat"
		   });		
		addAnnotation
		  (getGetFeatureWithLockType_Service(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "service"
		   });		
		addAnnotation
		  (getGetFeatureWithLockType_Version(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "version"
		   });		
		addAnnotation
		  (insertElementTypeEClass, 
		   source, 
		   new String[] {
			 "name", "InsertElementType",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getInsertElementType_Handle(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "handle"
		   });		
		addAnnotation
		  (insertResultTypeEClass, 
		   source, 
		   new String[] {
			 "name", "InsertResultType",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getInsertResultType_FeatureId(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "FeatureId",
			 "namespace", "http://www.opengis.net/ogc"
		   });		
		addAnnotation
		  (getInsertResultType_Handle(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "handle"
		   });		
		addAnnotation
		  (lockFeatureTypeEClass, 
		   source, 
		   new String[] {
			 "name", "LockFeatureType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getLockFeatureType_Lock(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Lock",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getLockFeatureType_Expiry(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "expiry"
		   });		
		addAnnotation
		  (getLockFeatureType_LockAction(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "lockAction"
		   });			
		addAnnotation
		  (getLockFeatureType_Service(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "service"
		   });		
		addAnnotation
		  (getLockFeatureType_Version(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "version"
		   });		
		addAnnotation
		  (lockTypeEClass, 
		   source, 
		   new String[] {
			 "name", "LockType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getLockType_Handle(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "handle"
		   });		
		addAnnotation
		  (nativeTypeEClass, 
		   source, 
		   new String[] {
			 "name", "NativeType",
			 "kind", "empty"
		   });		
		addAnnotation
		  (getNativeType_SafeToIgnore(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "safeToIgnore"
		   });			
		addAnnotation
		  (getNativeType_VendorId(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "vendorId"
		   });			
		addAnnotation
		  (propertyTypeEClass, 
		   source, 
		   new String[] {
			 "name", "PropertyType",
			 "kind", "elementOnly"
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
		  (statusTypeEClass, 
		   source, 
		   new String[] {
			 "name", "StatusType",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getStatusType_SUCCESS(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "SUCCESS",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getStatusType_FAILED(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "FAILED",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getStatusType_PARTIAL(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "PARTIAL",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (transactionResultTypeEClass, 
		   source, 
		   new String[] {
			 "name", "TransactionResultType",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getTransactionResultType_Status(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Status",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getTransactionResultType_Locator(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Locator",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getTransactionResultType_Message(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Message",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getTransactionResultType_Handle(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "handle"
		   });		
		addAnnotation
		  (transactionTypeEClass, 
		   source, 
		   new String[] {
			 "name", "TransactionType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getTransactionType_LockId(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "LockId",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getTransactionType_Group(), 
		   source, 
		   new String[] {
			 "kind", "group",
			 "name", "group:1"
		   });		
		addAnnotation
		  (getTransactionType_Handle(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "handle"
		   });		
		addAnnotation
		  (getTransactionType_ReleaseAction(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "releaseAction"
		   });			
		addAnnotation
		  (getTransactionType_Service(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "service"
		   });		
		addAnnotation
		  (getTransactionType_Version(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "version"
		   });		
		addAnnotation
		  (updateElementTypeEClass, 
		   source, 
		   new String[] {
			 "name", "UpdateElementType",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getUpdateElementType_Property(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Property",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getUpdateElementType_Handle(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "handle"
		   });		
		addAnnotation
		  (wfsLockFeatureResponseTypeEClass, 
		   source, 
		   new String[] {
			 "name", "WFS_LockFeatureResponseType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getWFSLockFeatureResponseType_LockId(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "LockId",
			 "namespace", "##targetNamespace"
		   });					
		addAnnotation
		  (wfsTransactionResponseTypeEClass, 
		   source, 
		   new String[] {
			 "name", "WFS_TransactionResponseType",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getWFSTransactionResponseType_InsertResult(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "InsertResult",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getWFSTransactionResponseType_TransactionResult(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "TransactionResult",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getWFSTransactionResponseType_Version(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "version"
		   });		
		addAnnotation
		  (allSomeTypeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "AllSomeType:Object",
			 "baseType", "AllSomeType"
		   });
	}

} //WFSPackageImpl
