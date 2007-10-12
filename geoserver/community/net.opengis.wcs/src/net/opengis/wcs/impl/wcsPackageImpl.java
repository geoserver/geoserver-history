/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.impl;

import net.opengis.wcs.AvailableKeysType;
import net.opengis.wcs.AxisSubsetType;
import net.opengis.wcs.AxisType;
import net.opengis.wcs.CapabilitiesType;
import net.opengis.wcs.ContentsType;
import net.opengis.wcs.CoverageDescriptionType;
import net.opengis.wcs.CoverageDescriptionsType;
import net.opengis.wcs.CoverageDomainType;
import net.opengis.wcs.CoverageSummaryType;
import net.opengis.wcs.DescribeCoverageType;
import net.opengis.wcs.DocumentRoot;
import net.opengis.wcs.DomainSubsetType;
import net.opengis.wcs.FieldSubsetType;
import net.opengis.wcs.FieldType;
import net.opengis.wcs.GetCapabilitiesType;
import net.opengis.wcs.GetCoverageType;
import net.opengis.wcs.GridCrsType;
import net.opengis.wcs.ImageCRSRefType;
import net.opengis.wcs.OutputType;
import net.opengis.wcs.RangeSubsetType;
import net.opengis.wcs.RangeType;
import net.opengis.wcs.RequestBaseType;
import net.opengis.wcs.SpatialDomainType;
import net.opengis.wcs.TimePeriodType;
import net.opengis.wcs.TimeSequenceType;

import net.opengis.wcs.ows.impl.owcsPackageImpl;

import net.opengis.wcs.ows.owcsPackage;

import net.opengis.wcs.util.wcsValidator;

import net.opengis.wcs.wcsFactory;
import net.opengis.wcs.wcsPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class wcsPackageImpl extends EPackageImpl implements wcsPackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass availableKeysTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass axisSubsetTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass axisTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass capabilitiesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass contentsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coverageDescriptionsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coverageDescriptionTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coverageDomainTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coverageSummaryTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass describeCoverageTypeEClass = null;

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
    private EClass domainSubsetTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass fieldSubsetTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass fieldTypeEClass = null;

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
    private EClass getCoverageTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass gridCrsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass imageCRSRefTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass outputTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass rangeSubsetTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass rangeTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass requestBaseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass spatialDomainTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timePeriodTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass timeSequenceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType identifierTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType timeDurationTypeEDataType = null;

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
     * @see net.opengis.wcs.wcsPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private wcsPackageImpl() {
        super(eNS_URI, wcsFactory.eINSTANCE);
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
    public static wcsPackage init() {
        if (isInited) return (wcsPackage)EPackage.Registry.INSTANCE.getEPackage(wcsPackage.eNS_URI);

        // Obtain or create and register package
        wcsPackageImpl thewcsPackage = (wcsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof wcsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new wcsPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        XMLTypePackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        owcsPackageImpl theowcsPackage = (owcsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(owcsPackage.eNS_URI) instanceof owcsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(owcsPackage.eNS_URI) : owcsPackage.eINSTANCE);

        // Create package meta-data objects
        thewcsPackage.createPackageContents();
        theowcsPackage.createPackageContents();

        // Initialize created meta-data
        thewcsPackage.initializePackageContents();
        theowcsPackage.initializePackageContents();

        // Register package validator
        EValidator.Registry.INSTANCE.put
            (thewcsPackage, 
             new EValidator.Descriptor() {
                 public EValidator getEValidator() {
                     return wcsValidator.INSTANCE;
                 }
             });

        // Mark meta-data to indicate it can't be changed
        thewcsPackage.freeze();

        return thewcsPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAvailableKeysType() {
        return availableKeysTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAvailableKeysType_Key() {
        return (EAttribute)availableKeysTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAxisSubsetType() {
        return axisSubsetTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAxisSubsetType_Identifier() {
        return (EAttribute)axisSubsetTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAxisSubsetType_Key() {
        return (EAttribute)axisSubsetTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAxisType() {
        return axisTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAxisType_AvailableKeys() {
        return (EReference)axisTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAxisType_Meaning() {
        return (EReference)axisTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAxisType_DataType() {
        return (EReference)axisTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAxisType_UOM() {
        return (EReference)axisTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAxisType_ReferenceSystem() {
        return (EReference)axisTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAxisType_Metadata() {
        return (EAttribute)axisTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAxisType_Identifier() {
        return (EAttribute)axisTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCapabilitiesType() {
        return capabilitiesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCapabilitiesType_Contents() {
        return (EReference)capabilitiesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getContentsType() {
        return contentsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getContentsType_CoverageSummary() {
        return (EReference)contentsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContentsType_SupportedCRS() {
        return (EAttribute)contentsTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContentsType_SupportedFormat() {
        return (EAttribute)contentsTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getContentsType_OtherSource() {
        return (EAttribute)contentsTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoverageDescriptionsType() {
        return coverageDescriptionsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageDescriptionsType_CoverageDescription() {
        return (EReference)coverageDescriptionsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoverageDescriptionType() {
        return coverageDescriptionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoverageDescriptionType_Identifier() {
        return (EAttribute)coverageDescriptionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoverageDescriptionType_Metadata() {
        return (EAttribute)coverageDescriptionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageDescriptionType_Domain() {
        return (EReference)coverageDescriptionTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageDescriptionType_Range() {
        return (EReference)coverageDescriptionTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoverageDescriptionType_SupportedCRS() {
        return (EAttribute)coverageDescriptionTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoverageDescriptionType_SupportedFormat() {
        return (EAttribute)coverageDescriptionTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoverageDomainType() {
        return coverageDomainTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageDomainType_SpatialDomain() {
        return (EReference)coverageDomainTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageDomainType_TemporalDomain() {
        return (EReference)coverageDomainTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoverageSummaryType() {
        return coverageSummaryTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoverageSummaryType_Metadata() {
        return (EAttribute)coverageSummaryTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoverageSummaryType_WGS84BoundingBox() {
        return (EAttribute)coverageSummaryTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoverageSummaryType_SupportedCRS() {
        return (EAttribute)coverageSummaryTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoverageSummaryType_SupportedFormat() {
        return (EAttribute)coverageSummaryTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCoverageSummaryType_CoverageSummary() {
        return (EReference)coverageSummaryTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoverageSummaryType_Identifier() {
        return (EAttribute)coverageSummaryTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCoverageSummaryType_Identifier1() {
        return (EAttribute)coverageSummaryTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDescribeCoverageType() {
        return describeCoverageTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDescribeCoverageType_Identifier() {
        return (EAttribute)describeCoverageTypeEClass.getEStructuralFeatures().get(0);
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
    public EReference getDocumentRoot_AvailableKeys() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AxisSubset() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Capabilities() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Contents() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CoverageDescriptions() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_CoverageSummary() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DescribeCoverage() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GetCapabilities() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GetCoverage() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_GridBaseCRS() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GridCRS() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_GridCS() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_GridOffsets() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_GridOrigin() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_GridType() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Identifier() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TemporalDomain() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_TemporalSubset() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDomainSubsetType() {
        return domainSubsetTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDomainSubsetType_BoundingBox() {
        return (EAttribute)domainSubsetTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDomainSubsetType_TemporalSubset() {
        return (EReference)domainSubsetTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFieldSubsetType() {
        return fieldSubsetTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFieldSubsetType_Identifier() {
        return (EAttribute)fieldSubsetTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFieldSubsetType_InterpolationType() {
        return (EAttribute)fieldSubsetTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFieldSubsetType_AxisSubset() {
        return (EReference)fieldSubsetTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFieldType() {
        return fieldTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFieldType_Identifier() {
        return (EAttribute)fieldTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFieldType_Definition() {
        return (EReference)fieldTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getFieldType_NullValue() {
        return (EAttribute)fieldTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFieldType_InterpolationMethods() {
        return (EReference)fieldTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getFieldType_Axis() {
        return (EReference)fieldTypeEClass.getEStructuralFeatures().get(4);
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
    public EClass getGetCoverageType() {
        return getCoverageTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetCoverageType_Identifier() {
        return (EAttribute)getCoverageTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGetCoverageType_DomainSubset() {
        return (EReference)getCoverageTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGetCoverageType_RangeSubset() {
        return (EReference)getCoverageTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGetCoverageType_Output() {
        return (EReference)getCoverageTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGridCrsType() {
        return gridCrsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGridCrsType_SrsName() {
        return (EAttribute)gridCrsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGridCrsType_GridBaseCRS() {
        return (EAttribute)gridCrsTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGridCrsType_GridType() {
        return (EAttribute)gridCrsTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGridCrsType_GridOrigin() {
        return (EAttribute)gridCrsTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGridCrsType_GridOffsets() {
        return (EAttribute)gridCrsTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGridCrsType_GridCS() {
        return (EAttribute)gridCrsTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGridCrsType_Id() {
        return (EAttribute)gridCrsTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getImageCRSRefType() {
        return imageCRSRefTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getImageCRSRefType_ImageCRS() {
        return (EAttribute)imageCRSRefTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOutputType() {
        return outputTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOutputType_GridCRS() {
        return (EReference)outputTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOutputType_Format() {
        return (EAttribute)outputTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOutputType_Store() {
        return (EAttribute)outputTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRangeSubsetType() {
        return rangeSubsetTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRangeSubsetType_FieldSubset() {
        return (EReference)rangeSubsetTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRangeType() {
        return rangeTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRangeType_Field() {
        return (EReference)rangeTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRequestBaseType() {
        return requestBaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRequestBaseType_Service() {
        return (EAttribute)requestBaseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRequestBaseType_Version() {
        return (EAttribute)requestBaseTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSpatialDomainType() {
        return spatialDomainTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSpatialDomainType_BoundingBox() {
        return (EAttribute)spatialDomainTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSpatialDomainType_GridCRS() {
        return (EReference)spatialDomainTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSpatialDomainType_Transformation() {
        return (EAttribute)spatialDomainTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSpatialDomainType_ImageCRS() {
        return (EReference)spatialDomainTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSpatialDomainType_Polygon() {
        return (EAttribute)spatialDomainTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimePeriodType() {
        return timePeriodTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePeriodType_BeginPosition() {
        return (EAttribute)timePeriodTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePeriodType_EndPosition() {
        return (EAttribute)timePeriodTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePeriodType_TimeResolution() {
        return (EAttribute)timePeriodTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimePeriodType_Frame() {
        return (EAttribute)timePeriodTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getTimeSequenceType() {
        return timeSequenceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeSequenceType_Group() {
        return (EAttribute)timeSequenceTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getTimeSequenceType_TimePosition() {
        return (EAttribute)timeSequenceTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getTimeSequenceType_TimePeriod() {
        return (EReference)timeSequenceTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getIdentifierType() {
        return identifierTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getTimeDurationType() {
        return timeDurationTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public wcsFactory getwcsFactory() {
        return (wcsFactory)getEFactoryInstance();
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
        availableKeysTypeEClass = createEClass(AVAILABLE_KEYS_TYPE);
        createEAttribute(availableKeysTypeEClass, AVAILABLE_KEYS_TYPE__KEY);

        axisSubsetTypeEClass = createEClass(AXIS_SUBSET_TYPE);
        createEAttribute(axisSubsetTypeEClass, AXIS_SUBSET_TYPE__IDENTIFIER);
        createEAttribute(axisSubsetTypeEClass, AXIS_SUBSET_TYPE__KEY);

        axisTypeEClass = createEClass(AXIS_TYPE);
        createEReference(axisTypeEClass, AXIS_TYPE__AVAILABLE_KEYS);
        createEReference(axisTypeEClass, AXIS_TYPE__MEANING);
        createEReference(axisTypeEClass, AXIS_TYPE__DATA_TYPE);
        createEReference(axisTypeEClass, AXIS_TYPE__UOM);
        createEReference(axisTypeEClass, AXIS_TYPE__REFERENCE_SYSTEM);
        createEAttribute(axisTypeEClass, AXIS_TYPE__METADATA);
        createEAttribute(axisTypeEClass, AXIS_TYPE__IDENTIFIER);

        capabilitiesTypeEClass = createEClass(CAPABILITIES_TYPE);
        createEReference(capabilitiesTypeEClass, CAPABILITIES_TYPE__CONTENTS);

        contentsTypeEClass = createEClass(CONTENTS_TYPE);
        createEReference(contentsTypeEClass, CONTENTS_TYPE__COVERAGE_SUMMARY);
        createEAttribute(contentsTypeEClass, CONTENTS_TYPE__SUPPORTED_CRS);
        createEAttribute(contentsTypeEClass, CONTENTS_TYPE__SUPPORTED_FORMAT);
        createEAttribute(contentsTypeEClass, CONTENTS_TYPE__OTHER_SOURCE);

        coverageDescriptionsTypeEClass = createEClass(COVERAGE_DESCRIPTIONS_TYPE);
        createEReference(coverageDescriptionsTypeEClass, COVERAGE_DESCRIPTIONS_TYPE__COVERAGE_DESCRIPTION);

        coverageDescriptionTypeEClass = createEClass(COVERAGE_DESCRIPTION_TYPE);
        createEAttribute(coverageDescriptionTypeEClass, COVERAGE_DESCRIPTION_TYPE__IDENTIFIER);
        createEAttribute(coverageDescriptionTypeEClass, COVERAGE_DESCRIPTION_TYPE__METADATA);
        createEReference(coverageDescriptionTypeEClass, COVERAGE_DESCRIPTION_TYPE__DOMAIN);
        createEReference(coverageDescriptionTypeEClass, COVERAGE_DESCRIPTION_TYPE__RANGE);
        createEAttribute(coverageDescriptionTypeEClass, COVERAGE_DESCRIPTION_TYPE__SUPPORTED_CRS);
        createEAttribute(coverageDescriptionTypeEClass, COVERAGE_DESCRIPTION_TYPE__SUPPORTED_FORMAT);

        coverageDomainTypeEClass = createEClass(COVERAGE_DOMAIN_TYPE);
        createEReference(coverageDomainTypeEClass, COVERAGE_DOMAIN_TYPE__SPATIAL_DOMAIN);
        createEReference(coverageDomainTypeEClass, COVERAGE_DOMAIN_TYPE__TEMPORAL_DOMAIN);

        coverageSummaryTypeEClass = createEClass(COVERAGE_SUMMARY_TYPE);
        createEAttribute(coverageSummaryTypeEClass, COVERAGE_SUMMARY_TYPE__METADATA);
        createEAttribute(coverageSummaryTypeEClass, COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX);
        createEAttribute(coverageSummaryTypeEClass, COVERAGE_SUMMARY_TYPE__SUPPORTED_CRS);
        createEAttribute(coverageSummaryTypeEClass, COVERAGE_SUMMARY_TYPE__SUPPORTED_FORMAT);
        createEReference(coverageSummaryTypeEClass, COVERAGE_SUMMARY_TYPE__COVERAGE_SUMMARY);
        createEAttribute(coverageSummaryTypeEClass, COVERAGE_SUMMARY_TYPE__IDENTIFIER);
        createEAttribute(coverageSummaryTypeEClass, COVERAGE_SUMMARY_TYPE__IDENTIFIER1);

        describeCoverageTypeEClass = createEClass(DESCRIBE_COVERAGE_TYPE);
        createEAttribute(describeCoverageTypeEClass, DESCRIBE_COVERAGE_TYPE__IDENTIFIER);

        documentRootEClass = createEClass(DOCUMENT_ROOT);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__AVAILABLE_KEYS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__AXIS_SUBSET);
        createEReference(documentRootEClass, DOCUMENT_ROOT__CAPABILITIES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__CONTENTS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__COVERAGE_SUMMARY);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DESCRIBE_COVERAGE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_CAPABILITIES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_COVERAGE);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__GRID_BASE_CRS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GRID_CRS);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__GRID_CS);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__GRID_OFFSETS);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__GRID_ORIGIN);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__GRID_TYPE);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__IDENTIFIER);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TEMPORAL_DOMAIN);
        createEReference(documentRootEClass, DOCUMENT_ROOT__TEMPORAL_SUBSET);

        domainSubsetTypeEClass = createEClass(DOMAIN_SUBSET_TYPE);
        createEAttribute(domainSubsetTypeEClass, DOMAIN_SUBSET_TYPE__BOUNDING_BOX);
        createEReference(domainSubsetTypeEClass, DOMAIN_SUBSET_TYPE__TEMPORAL_SUBSET);

        fieldSubsetTypeEClass = createEClass(FIELD_SUBSET_TYPE);
        createEAttribute(fieldSubsetTypeEClass, FIELD_SUBSET_TYPE__IDENTIFIER);
        createEAttribute(fieldSubsetTypeEClass, FIELD_SUBSET_TYPE__INTERPOLATION_TYPE);
        createEReference(fieldSubsetTypeEClass, FIELD_SUBSET_TYPE__AXIS_SUBSET);

        fieldTypeEClass = createEClass(FIELD_TYPE);
        createEAttribute(fieldTypeEClass, FIELD_TYPE__IDENTIFIER);
        createEReference(fieldTypeEClass, FIELD_TYPE__DEFINITION);
        createEAttribute(fieldTypeEClass, FIELD_TYPE__NULL_VALUE);
        createEReference(fieldTypeEClass, FIELD_TYPE__INTERPOLATION_METHODS);
        createEReference(fieldTypeEClass, FIELD_TYPE__AXIS);

        getCapabilitiesTypeEClass = createEClass(GET_CAPABILITIES_TYPE);
        createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__SERVICE);

        getCoverageTypeEClass = createEClass(GET_COVERAGE_TYPE);
        createEAttribute(getCoverageTypeEClass, GET_COVERAGE_TYPE__IDENTIFIER);
        createEReference(getCoverageTypeEClass, GET_COVERAGE_TYPE__DOMAIN_SUBSET);
        createEReference(getCoverageTypeEClass, GET_COVERAGE_TYPE__RANGE_SUBSET);
        createEReference(getCoverageTypeEClass, GET_COVERAGE_TYPE__OUTPUT);

        gridCrsTypeEClass = createEClass(GRID_CRS_TYPE);
        createEAttribute(gridCrsTypeEClass, GRID_CRS_TYPE__SRS_NAME);
        createEAttribute(gridCrsTypeEClass, GRID_CRS_TYPE__GRID_BASE_CRS);
        createEAttribute(gridCrsTypeEClass, GRID_CRS_TYPE__GRID_TYPE);
        createEAttribute(gridCrsTypeEClass, GRID_CRS_TYPE__GRID_ORIGIN);
        createEAttribute(gridCrsTypeEClass, GRID_CRS_TYPE__GRID_OFFSETS);
        createEAttribute(gridCrsTypeEClass, GRID_CRS_TYPE__GRID_CS);
        createEAttribute(gridCrsTypeEClass, GRID_CRS_TYPE__ID);

        imageCRSRefTypeEClass = createEClass(IMAGE_CRS_REF_TYPE);
        createEAttribute(imageCRSRefTypeEClass, IMAGE_CRS_REF_TYPE__IMAGE_CRS);

        outputTypeEClass = createEClass(OUTPUT_TYPE);
        createEReference(outputTypeEClass, OUTPUT_TYPE__GRID_CRS);
        createEAttribute(outputTypeEClass, OUTPUT_TYPE__FORMAT);
        createEAttribute(outputTypeEClass, OUTPUT_TYPE__STORE);

        rangeSubsetTypeEClass = createEClass(RANGE_SUBSET_TYPE);
        createEReference(rangeSubsetTypeEClass, RANGE_SUBSET_TYPE__FIELD_SUBSET);

        rangeTypeEClass = createEClass(RANGE_TYPE);
        createEReference(rangeTypeEClass, RANGE_TYPE__FIELD);

        requestBaseTypeEClass = createEClass(REQUEST_BASE_TYPE);
        createEAttribute(requestBaseTypeEClass, REQUEST_BASE_TYPE__SERVICE);
        createEAttribute(requestBaseTypeEClass, REQUEST_BASE_TYPE__VERSION);

        spatialDomainTypeEClass = createEClass(SPATIAL_DOMAIN_TYPE);
        createEAttribute(spatialDomainTypeEClass, SPATIAL_DOMAIN_TYPE__BOUNDING_BOX);
        createEReference(spatialDomainTypeEClass, SPATIAL_DOMAIN_TYPE__GRID_CRS);
        createEAttribute(spatialDomainTypeEClass, SPATIAL_DOMAIN_TYPE__TRANSFORMATION);
        createEReference(spatialDomainTypeEClass, SPATIAL_DOMAIN_TYPE__IMAGE_CRS);
        createEAttribute(spatialDomainTypeEClass, SPATIAL_DOMAIN_TYPE__POLYGON);

        timePeriodTypeEClass = createEClass(TIME_PERIOD_TYPE);
        createEAttribute(timePeriodTypeEClass, TIME_PERIOD_TYPE__BEGIN_POSITION);
        createEAttribute(timePeriodTypeEClass, TIME_PERIOD_TYPE__END_POSITION);
        createEAttribute(timePeriodTypeEClass, TIME_PERIOD_TYPE__TIME_RESOLUTION);
        createEAttribute(timePeriodTypeEClass, TIME_PERIOD_TYPE__FRAME);

        timeSequenceTypeEClass = createEClass(TIME_SEQUENCE_TYPE);
        createEAttribute(timeSequenceTypeEClass, TIME_SEQUENCE_TYPE__GROUP);
        createEAttribute(timeSequenceTypeEClass, TIME_SEQUENCE_TYPE__TIME_POSITION);
        createEReference(timeSequenceTypeEClass, TIME_SEQUENCE_TYPE__TIME_PERIOD);

        // Create data types
        identifierTypeEDataType = createEDataType(IDENTIFIER_TYPE);
        timeDurationTypeEDataType = createEDataType(TIME_DURATION_TYPE);
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
        owcsPackage theowcsPackage = (owcsPackage)EPackage.Registry.INSTANCE.getEPackage(owcsPackage.eNS_URI);
        XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

        // Add supertypes to classes
        axisTypeEClass.getESuperTypes().add(theowcsPackage.getDescriptionType());
        capabilitiesTypeEClass.getESuperTypes().add(theowcsPackage.getCapabilitiesBaseType());
        coverageDescriptionTypeEClass.getESuperTypes().add(theowcsPackage.getDescriptionType());
        coverageSummaryTypeEClass.getESuperTypes().add(theowcsPackage.getDescriptionType());
        describeCoverageTypeEClass.getESuperTypes().add(this.getRequestBaseType());
        fieldTypeEClass.getESuperTypes().add(theowcsPackage.getDescriptionType());
        getCapabilitiesTypeEClass.getESuperTypes().add(theowcsPackage.getGetCapabilitiesType());
        getCoverageTypeEClass.getESuperTypes().add(this.getRequestBaseType());

        // Initialize classes and features; add operations and parameters
        initEClass(availableKeysTypeEClass, AvailableKeysType.class, "AvailableKeysType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAvailableKeysType_Key(), this.getIdentifierType(), "key", null, 1, -1, AvailableKeysType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(axisSubsetTypeEClass, AxisSubsetType.class, "AxisSubsetType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAxisSubsetType_Identifier(), this.getIdentifierType(), "identifier", null, 1, 1, AxisSubsetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAxisSubsetType_Key(), this.getIdentifierType(), "key", null, 1, -1, AxisSubsetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(axisTypeEClass, AxisType.class, "AxisType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAxisType_AvailableKeys(), this.getAvailableKeysType(), null, "availableKeys", null, 1, 1, AxisType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getAxisType_Meaning(), theowcsPackage.getDomainMetadataType(), null, "meaning", null, 0, 1, AxisType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getAxisType_DataType(), theowcsPackage.getDomainMetadataType(), null, "dataType", null, 0, 1, AxisType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getAxisType_UOM(), theowcsPackage.getDomainMetadataType(), null, "uOM", null, 0, 1, AxisType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getAxisType_ReferenceSystem(), theowcsPackage.getDomainMetadataType(), null, "referenceSystem", null, 0, 1, AxisType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAxisType_Metadata(), theXMLTypePackage.getAnySimpleType(), "metadata", null, 0, -1, AxisType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAxisType_Identifier(), this.getIdentifierType(), "identifier", null, 1, 1, AxisType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(capabilitiesTypeEClass, CapabilitiesType.class, "CapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCapabilitiesType_Contents(), this.getContentsType(), null, "contents", null, 0, 1, CapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(contentsTypeEClass, ContentsType.class, "ContentsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getContentsType_CoverageSummary(), this.getCoverageSummaryType(), null, "coverageSummary", null, 0, -1, ContentsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getContentsType_SupportedCRS(), theXMLTypePackage.getAnyURI(), "supportedCRS", null, 0, -1, ContentsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getContentsType_SupportedFormat(), theXMLTypePackage.getAnySimpleType(), "supportedFormat", null, 0, -1, ContentsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getContentsType_OtherSource(), theXMLTypePackage.getAnySimpleType(), "otherSource", null, 0, -1, ContentsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(coverageDescriptionsTypeEClass, CoverageDescriptionsType.class, "CoverageDescriptionsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCoverageDescriptionsType_CoverageDescription(), this.getCoverageDescriptionType(), null, "coverageDescription", null, 1, -1, CoverageDescriptionsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(coverageDescriptionTypeEClass, CoverageDescriptionType.class, "CoverageDescriptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCoverageDescriptionType_Identifier(), this.getIdentifierType(), "identifier", null, 1, 1, CoverageDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCoverageDescriptionType_Metadata(), theXMLTypePackage.getAnySimpleType(), "metadata", null, 0, -1, CoverageDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCoverageDescriptionType_Domain(), this.getCoverageDomainType(), null, "domain", null, 1, 1, CoverageDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCoverageDescriptionType_Range(), this.getRangeType(), null, "range", null, 1, 1, CoverageDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCoverageDescriptionType_SupportedCRS(), theXMLTypePackage.getAnyURI(), "supportedCRS", null, 1, -1, CoverageDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCoverageDescriptionType_SupportedFormat(), theXMLTypePackage.getAnySimpleType(), "supportedFormat", null, 1, -1, CoverageDescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(coverageDomainTypeEClass, CoverageDomainType.class, "CoverageDomainType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCoverageDomainType_SpatialDomain(), this.getSpatialDomainType(), null, "spatialDomain", null, 1, 1, CoverageDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCoverageDomainType_TemporalDomain(), this.getTimeSequenceType(), null, "temporalDomain", null, 0, 1, CoverageDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(coverageSummaryTypeEClass, CoverageSummaryType.class, "CoverageSummaryType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCoverageSummaryType_Metadata(), theXMLTypePackage.getAnySimpleType(), "metadata", null, 0, -1, CoverageSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCoverageSummaryType_WGS84BoundingBox(), theXMLTypePackage.getAnySimpleType(), "wGS84BoundingBox", null, 0, -1, CoverageSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCoverageSummaryType_SupportedCRS(), theXMLTypePackage.getAnyURI(), "supportedCRS", null, 0, -1, CoverageSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCoverageSummaryType_SupportedFormat(), theXMLTypePackage.getAnySimpleType(), "supportedFormat", null, 0, -1, CoverageSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCoverageSummaryType_CoverageSummary(), this.getCoverageSummaryType(), null, "coverageSummary", null, 0, -1, CoverageSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCoverageSummaryType_Identifier(), this.getIdentifierType(), "identifier", null, 0, 1, CoverageSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCoverageSummaryType_Identifier1(), this.getIdentifierType(), "identifier1", null, 0, 1, CoverageSummaryType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(describeCoverageTypeEClass, DescribeCoverageType.class, "DescribeCoverageType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDescribeCoverageType_Identifier(), this.getIdentifierType(), "identifier", null, 1, -1, DescribeCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AvailableKeys(), this.getAvailableKeysType(), null, "availableKeys", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AxisSubset(), this.getAxisSubsetType(), null, "axisSubset", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Capabilities(), this.getCapabilitiesType(), null, "capabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Contents(), this.getContentsType(), null, "contents", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_CoverageDescriptions(), this.getCoverageDescriptionsType(), null, "coverageDescriptions", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_CoverageSummary(), this.getCoverageSummaryType(), null, "coverageSummary", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DescribeCoverage(), this.getDescribeCoverageType(), null, "describeCoverage", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetCapabilities(), this.getGetCapabilitiesType(), null, "getCapabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetCoverage(), this.getGetCoverageType(), null, "getCoverage", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_GridBaseCRS(), theXMLTypePackage.getAnyURI(), "gridBaseCRS", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GridCRS(), this.getGridCrsType(), null, "gridCRS", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_GridCS(), theXMLTypePackage.getAnyURI(), "gridCS", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_GridOffsets(), theXMLTypePackage.getAnySimpleType(), "gridOffsets", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_GridOrigin(), theXMLTypePackage.getAnySimpleType(), "gridOrigin", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_GridType(), theXMLTypePackage.getAnyURI(), "gridType", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_Identifier(), this.getIdentifierType(), "identifier", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_TemporalDomain(), this.getTimeSequenceType(), null, "temporalDomain", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_TemporalSubset(), this.getTimeSequenceType(), null, "temporalSubset", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(domainSubsetTypeEClass, DomainSubsetType.class, "DomainSubsetType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDomainSubsetType_BoundingBox(), theXMLTypePackage.getAnySimpleType(), "boundingBox", null, 1, 1, DomainSubsetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDomainSubsetType_TemporalSubset(), this.getTimeSequenceType(), null, "temporalSubset", null, 0, 1, DomainSubsetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(fieldSubsetTypeEClass, FieldSubsetType.class, "FieldSubsetType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFieldSubsetType_Identifier(), theXMLTypePackage.getAnySimpleType(), "identifier", null, 1, 1, FieldSubsetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFieldSubsetType_InterpolationType(), theXMLTypePackage.getString(), "interpolationType", null, 0, 1, FieldSubsetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFieldSubsetType_AxisSubset(), this.getAxisSubsetType(), null, "axisSubset", null, 0, -1, FieldSubsetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(fieldTypeEClass, FieldType.class, "FieldType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFieldType_Identifier(), this.getIdentifierType(), "identifier", null, 1, 1, FieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFieldType_Definition(), theowcsPackage.getUnNamedDomainType(), null, "definition", null, 1, 1, FieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFieldType_NullValue(), theXMLTypePackage.getAnySimpleType(), "nullValue", null, 0, -1, FieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFieldType_InterpolationMethods(), theowcsPackage.getInterpolationMethodsType(), null, "interpolationMethods", null, 1, 1, FieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFieldType_Axis(), this.getAxisType(), null, "axis", null, 0, -1, FieldType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getCapabilitiesTypeEClass, GetCapabilitiesType.class, "GetCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGetCapabilitiesType_Service(), theowcsPackage.getServiceType(), "service", "WCS", 1, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getCoverageTypeEClass, GetCoverageType.class, "GetCoverageType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGetCoverageType_Identifier(), theXMLTypePackage.getAnySimpleType(), "identifier", null, 1, 1, GetCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGetCoverageType_DomainSubset(), this.getDomainSubsetType(), null, "domainSubset", null, 1, 1, GetCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGetCoverageType_RangeSubset(), this.getRangeSubsetType(), null, "rangeSubset", null, 0, 1, GetCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGetCoverageType_Output(), this.getOutputType(), null, "output", null, 1, 1, GetCoverageType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(gridCrsTypeEClass, GridCrsType.class, "GridCrsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGridCrsType_SrsName(), theXMLTypePackage.getAnySimpleType(), "srsName", null, 0, 1, GridCrsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGridCrsType_GridBaseCRS(), theXMLTypePackage.getAnyURI(), "gridBaseCRS", null, 1, 1, GridCrsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGridCrsType_GridType(), theXMLTypePackage.getAnyURI(), "gridType", null, 0, 1, GridCrsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGridCrsType_GridOrigin(), theXMLTypePackage.getAnySimpleType(), "gridOrigin", null, 0, 1, GridCrsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGridCrsType_GridOffsets(), theXMLTypePackage.getAnySimpleType(), "gridOffsets", null, 1, 1, GridCrsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGridCrsType_GridCS(), theXMLTypePackage.getAnyURI(), "gridCS", null, 0, 1, GridCrsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGridCrsType_Id(), theXMLTypePackage.getAnySimpleType(), "id", null, 0, 1, GridCrsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(imageCRSRefTypeEClass, ImageCRSRefType.class, "ImageCRSRefType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getImageCRSRefType_ImageCRS(), theXMLTypePackage.getAnySimpleType(), "imageCRS", null, 0, 1, ImageCRSRefType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(outputTypeEClass, OutputType.class, "OutputType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getOutputType_GridCRS(), this.getGridCrsType(), null, "gridCRS", null, 0, 1, OutputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getOutputType_Format(), theXMLTypePackage.getAnySimpleType(), "format", null, 0, 1, OutputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getOutputType_Store(), theXMLTypePackage.getBoolean(), "store", "false", 0, 1, OutputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(rangeSubsetTypeEClass, RangeSubsetType.class, "RangeSubsetType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRangeSubsetType_FieldSubset(), this.getFieldSubsetType(), null, "fieldSubset", null, 1, -1, RangeSubsetType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(rangeTypeEClass, RangeType.class, "RangeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRangeType_Field(), this.getFieldType(), null, "field", null, 1, -1, RangeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(requestBaseTypeEClass, RequestBaseType.class, "RequestBaseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getRequestBaseType_Service(), theXMLTypePackage.getString(), "service", "WCS", 1, 1, RequestBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRequestBaseType_Version(), theXMLTypePackage.getString(), "version", "1.1.0", 1, 1, RequestBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(spatialDomainTypeEClass, SpatialDomainType.class, "SpatialDomainType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSpatialDomainType_BoundingBox(), theXMLTypePackage.getAnySimpleType(), "boundingBox", null, 1, -1, SpatialDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSpatialDomainType_GridCRS(), this.getGridCrsType(), null, "gridCRS", null, 0, 1, SpatialDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSpatialDomainType_Transformation(), theXMLTypePackage.getAnySimpleType(), "transformation", null, 0, 1, SpatialDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSpatialDomainType_ImageCRS(), this.getImageCRSRefType(), null, "imageCRS", null, 0, 1, SpatialDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSpatialDomainType_Polygon(), theXMLTypePackage.getAnySimpleType(), "polygon", null, 0, -1, SpatialDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(timePeriodTypeEClass, TimePeriodType.class, "TimePeriodType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTimePeriodType_BeginPosition(), theXMLTypePackage.getAnySimpleType(), "beginPosition", null, 1, 1, TimePeriodType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTimePeriodType_EndPosition(), theXMLTypePackage.getAnySimpleType(), "endPosition", null, 1, 1, TimePeriodType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTimePeriodType_TimeResolution(), this.getTimeDurationType(), "timeResolution", null, 0, 1, TimePeriodType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTimePeriodType_Frame(), theXMLTypePackage.getAnyURI(), "frame", "#ISO-8601", 0, 1, TimePeriodType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(timeSequenceTypeEClass, TimeSequenceType.class, "TimeSequenceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTimeSequenceType_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, TimeSequenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTimeSequenceType_TimePosition(), theXMLTypePackage.getAnySimpleType(), "timePosition", null, 0, -1, TimeSequenceType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getTimeSequenceType_TimePeriod(), this.getTimePeriodType(), null, "timePeriod", null, 0, -1, TimeSequenceType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        // Initialize data types
        initEDataType(identifierTypeEDataType, String.class, "IdentifierType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(timeDurationTypeEDataType, Object.class, "TimeDurationType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

        // Create resource
        createResource(eNS_URI);

        // Create annotations
        // null
        createNullAnnotations();
        // http://www.w3.org/XML/1998/namespace
        createNamespaceAnnotations();
        // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
        createExtendedMetaDataAnnotations();
    }

    /**
     * Initializes the annotations for <b>null</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createNullAnnotations() {
        String source = null;			
        addAnnotation
          (this, 
           source, 
           new String[] {
             "appinfo", "wcsDescribeCoverage.xsd 2006-10-13\r\n\r\n\t\t\t<GMLProfileSchema>http://schemas.opengis.net/wcs/1.1.0/gml4wcs.xsd</GMLProfileSchema>\r\n\t\t\r\nwcsContents.xsd 2006-09-23\r\n\r\n\t\t\t<GMLProfileSchema>http://schemas.opengis.net/wcs/1.1.0/gml4wcs.xsd</GMLProfileSchema>\r\n\t\t\r\nwcsGridCRS.xsd 2006-09-23\r\n\r\n\t\t\t<GMLProfileSchema>http://schemas.opengis.net/gml/3.1.1/profiles/GridCRSs/1.0.0/gmlGridCRSsProfile.xsd</GMLProfileSchema>\r\n\t\t\r\nowcsAll.xsd 2006-10-05\r\nowsGetCapabilities.xsd 2006-09-27\r\nowsServiceIdentification.xsd 2006-09-27\r\nowsDataIdentification.xsd 2006-09-27\r\nowsOperationsMetadata.xsd 2006-09-27\r\nowsDomainType.xsd 2006-07-28\r\nwcsCoverageGroup.xsd 2006-10-05\r\nowsManifest.xsd 2006-09-27\r\nowsInterpolationMethod.xsd 2006-09-01"
           });																																																																																																																																																																																																																				
    }

    /**
     * Initializes the annotations for <b>http://www.w3.org/XML/1998/namespace</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void createNamespaceAnnotations() {
        String source = "http://www.w3.org/XML/1998/namespace";				
        addAnnotation
          (this, 
           source, 
           new String[] {
             "lang", "en"
           });																																																																																																																																																																																																																			
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
          (availableKeysTypeEClass, 
           source, 
           new String[] {
             "name", "AvailableKeys_._type",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getAvailableKeysType_Key(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Key",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (axisSubsetTypeEClass, 
           source, 
           new String[] {
             "name", "AxisSubset_._type",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getAxisSubsetType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getAxisSubsetType_Key(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Key",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (axisTypeEClass, 
           source, 
           new String[] {
             "name", "AxisType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getAxisType_AvailableKeys(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AvailableKeys",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getAxisType_Meaning(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Meaning",
             "namespace", "http://www.opengis.net/wcs/1.1/ows"
           });			
        addAnnotation
          (getAxisType_DataType(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DataType",
             "namespace", "http://www.opengis.net/wcs/1.1/ows"
           });			
        addAnnotation
          (getAxisType_UOM(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "UOM",
             "namespace", "http://www.opengis.net/wcs/1.1/ows"
           });			
        addAnnotation
          (getAxisType_ReferenceSystem(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ReferenceSystem",
             "namespace", "http://www.opengis.net/wcs/1.1/ows"
           });			
        addAnnotation
          (getAxisType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "http://www.opengis.net/ows"
           });			
        addAnnotation
          (getAxisType_Identifier(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "identifier"
           });		
        addAnnotation
          (capabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "Capabilities_._type",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getCapabilitiesType_Contents(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Contents",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (contentsTypeEClass, 
           source, 
           new String[] {
             "name", "Contents_._type",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getContentsType_CoverageSummary(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageSummary",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getContentsType_SupportedCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SupportedCRS",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getContentsType_SupportedFormat(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SupportedFormat",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getContentsType_OtherSource(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OtherSource",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (coverageDescriptionsTypeEClass, 
           source, 
           new String[] {
             "name", "CoverageDescriptions_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getCoverageDescriptionsType_CoverageDescription(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageDescription",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (coverageDescriptionTypeEClass, 
           source, 
           new String[] {
             "name", "CoverageDescriptionType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getCoverageDescriptionType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getCoverageDescriptionType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "http://www.opengis.net/ows"
           });		
        addAnnotation
          (getCoverageDescriptionType_Domain(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Domain",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getCoverageDescriptionType_Range(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Range",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getCoverageDescriptionType_SupportedCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SupportedCRS",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getCoverageDescriptionType_SupportedFormat(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SupportedFormat",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (coverageDomainTypeEClass, 
           source, 
           new String[] {
             "name", "CoverageDomainType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getCoverageDomainType_SpatialDomain(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SpatialDomain",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getCoverageDomainType_TemporalDomain(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TemporalDomain",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (coverageSummaryTypeEClass, 
           source, 
           new String[] {
             "name", "CoverageSummaryType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getCoverageSummaryType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "http://www.opengis.net/ows"
           });			
        addAnnotation
          (getCoverageSummaryType_WGS84BoundingBox(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "WGS84BoundingBox",
             "namespace", "http://www.opengis.net/ows"
           });			
        addAnnotation
          (getCoverageSummaryType_SupportedCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SupportedCRS",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getCoverageSummaryType_SupportedFormat(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SupportedFormat",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getCoverageSummaryType_CoverageSummary(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageSummary",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getCoverageSummaryType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getCoverageSummaryType_Identifier1(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (describeCoverageTypeEClass, 
           source, 
           new String[] {
             "name", "DescribeCoverage_._type",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getDescribeCoverageType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "##targetNamespace"
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
          (getDocumentRoot_AvailableKeys(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AvailableKeys",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_AxisSubset(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AxisSubset",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Capabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Capabilities",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Contents(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Contents",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_CoverageDescriptions(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageDescriptions",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_CoverageSummary(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "CoverageSummary",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_DescribeCoverage(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DescribeCoverage",
             "namespace", "##targetNamespace"
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
          (getDocumentRoot_GetCoverage(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GetCoverage",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_GridBaseCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GridBaseCRS",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_GridCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GridCRS",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_GridCS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GridCS",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_GridOffsets(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GridOffsets",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_GridOrigin(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GridOrigin",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_GridType(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GridType",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_TemporalDomain(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TemporalDomain",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_TemporalSubset(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TemporalSubset",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (domainSubsetTypeEClass, 
           source, 
           new String[] {
             "name", "DomainSubsetType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getDomainSubsetType_BoundingBox(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BoundingBox",
             "namespace", "http://www.opengis.net/ows"
           });			
        addAnnotation
          (getDomainSubsetType_TemporalSubset(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TemporalSubset",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (fieldSubsetTypeEClass, 
           source, 
           new String[] {
             "name", "FieldSubset_._type",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getFieldSubsetType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "http://www.opengis.net/wcs/1.1/ows"
           });			
        addAnnotation
          (getFieldSubsetType_InterpolationType(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "InterpolationType",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getFieldSubsetType_AxisSubset(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AxisSubset",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (fieldTypeEClass, 
           source, 
           new String[] {
             "name", "FieldType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getFieldType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getFieldType_Definition(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Definition",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getFieldType_NullValue(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "NullValue",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getFieldType_InterpolationMethods(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "InterpolationMethods",
             "namespace", "http://www.opengis.net/wcs/1.1/ows"
           });			
        addAnnotation
          (getFieldType_Axis(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Axis",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getCapabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "GetCapabilities_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getGetCapabilitiesType_Service(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "service"
           });		
        addAnnotation
          (getCoverageTypeEClass, 
           source, 
           new String[] {
             "name", "GetCoverage_._type",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getGetCoverageType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "http://www.opengis.net/wcs/1.1/ows"
           });		
        addAnnotation
          (getGetCoverageType_DomainSubset(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DomainSubset",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGetCoverageType_RangeSubset(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "RangeSubset",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getGetCoverageType_Output(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Output",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (gridCrsTypeEClass, 
           source, 
           new String[] {
             "name", "GridCrsType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getGridCrsType_SrsName(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "srsName",
             "namespace", "http://www.opengis.net/gml"
           });			
        addAnnotation
          (getGridCrsType_GridBaseCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GridBaseCRS",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGridCrsType_GridType(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GridType",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGridCrsType_GridOrigin(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GridOrigin",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGridCrsType_GridOffsets(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GridOffsets",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGridCrsType_GridCS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GridCS",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getGridCrsType_Id(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "id",
             "namespace", "http://www.opengis.net/gml"
           });			
        addAnnotation
          (identifierTypeEDataType, 
           source, 
           new String[] {
             "name", "IdentifierType",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#string",
             "pattern", ".+"
           });			
        addAnnotation
          (imageCRSRefTypeEClass, 
           source, 
           new String[] {
             "name", "ImageCRSRefType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getImageCRSRefType_ImageCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ImageCRS",
             "namespace", "http://www.opengis.net/gml"
           });			
        addAnnotation
          (outputTypeEClass, 
           source, 
           new String[] {
             "name", "OutputType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getOutputType_GridCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GridCRS",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getOutputType_Format(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "format"
           });			
        addAnnotation
          (getOutputType_Store(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "store"
           });			
        addAnnotation
          (rangeSubsetTypeEClass, 
           source, 
           new String[] {
             "name", "RangeSubsetType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getRangeSubsetType_FieldSubset(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "FieldSubset",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (rangeTypeEClass, 
           source, 
           new String[] {
             "name", "RangeType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getRangeType_Field(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Field",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (requestBaseTypeEClass, 
           source, 
           new String[] {
             "name", "RequestBaseType",
             "kind", "empty"
           });			
        addAnnotation
          (getRequestBaseType_Service(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "service"
           });			
        addAnnotation
          (getRequestBaseType_Version(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "version"
           });			
        addAnnotation
          (spatialDomainTypeEClass, 
           source, 
           new String[] {
             "name", "SpatialDomainType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getSpatialDomainType_BoundingBox(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BoundingBox",
             "namespace", "http://www.opengis.net/ows"
           });			
        addAnnotation
          (getSpatialDomainType_GridCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "GridCRS",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getSpatialDomainType_Transformation(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Transformation",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getSpatialDomainType_ImageCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ImageCRS",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getSpatialDomainType_Polygon(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Polygon",
             "namespace", "http://www.opengis.net/gml"
           });			
        addAnnotation
          (timeDurationTypeEDataType, 
           source, 
           new String[] {
             "name", "TimeDurationType",
             "memberTypes", "http://www.eclipse.org/emf/2003/XMLType#duration http://www.eclipse.org/emf/2003/XMLType#decimal"
           });			
        addAnnotation
          (timePeriodTypeEClass, 
           source, 
           new String[] {
             "name", "TimePeriodType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getTimePeriodType_BeginPosition(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BeginPosition",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getTimePeriodType_EndPosition(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "EndPosition",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getTimePeriodType_TimeResolution(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TimeResolution",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getTimePeriodType_Frame(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "frame"
           });			
        addAnnotation
          (timeSequenceTypeEClass, 
           source, 
           new String[] {
             "name", "TimeSequenceType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getTimeSequenceType_Group(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "group:0"
           });		
        addAnnotation
          (getTimeSequenceType_TimePosition(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "timePosition",
             "namespace", "http://www.opengis.net/gml",
             "group", "#group:0"
           });		
        addAnnotation
          (getTimeSequenceType_TimePeriod(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "TimePeriod",
             "namespace", "##targetNamespace",
             "group", "#group:0"
           });
    }

} //wcsPackageImpl
