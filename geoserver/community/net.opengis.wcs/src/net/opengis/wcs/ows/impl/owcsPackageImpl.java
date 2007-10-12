/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.ows.impl;

import net.opengis.wcs.impl.wcsPackageImpl;

import net.opengis.wcs.ows.AbstractReferenceBaseType;
import net.opengis.wcs.ows.AcceptFormatsType;
import net.opengis.wcs.ows.AcceptVersionsType;
import net.opengis.wcs.ows.AllowedValuesType;
import net.opengis.wcs.ows.AnyValueType;
import net.opengis.wcs.ows.BasicIdentificationType;
import net.opengis.wcs.ows.CapabilitiesBaseType;
import net.opengis.wcs.ows.CoveragesType;
import net.opengis.wcs.ows.DCPType;
import net.opengis.wcs.ows.DescriptionType;
import net.opengis.wcs.ows.DocumentRoot;
import net.opengis.wcs.ows.DomainMetadataType;
import net.opengis.wcs.ows.DomainType;
import net.opengis.wcs.ows.GetCapabilitiesType;
import net.opengis.wcs.ows.HTTPType;
import net.opengis.wcs.ows.IdentificationType;
import net.opengis.wcs.ows.InterpolationMethodBaseType;
import net.opengis.wcs.ows.InterpolationMethodType;
import net.opengis.wcs.ows.InterpolationMethodsType;
import net.opengis.wcs.ows.ManifestType;
import net.opengis.wcs.ows.NoValuesType;
import net.opengis.wcs.ows.OperationType;
import net.opengis.wcs.ows.OperationsMetadataType;
import net.opengis.wcs.ows.RangeClosureType;
import net.opengis.wcs.ows.RangeType;
import net.opengis.wcs.ows.ReferenceGroupType;
import net.opengis.wcs.ows.ReferenceType;
import net.opengis.wcs.ows.RequestMethodType;
import net.opengis.wcs.ows.SectionsType;
import net.opengis.wcs.ows.ServiceIdentificationType;
import net.opengis.wcs.ows.ServiceReferenceType;
import net.opengis.wcs.ows.UnNamedDomainType;
import net.opengis.wcs.ows.ValueType;
import net.opengis.wcs.ows.ValuesReferenceType;
import net.opengis.wcs.ows.owcsFactory;
import net.opengis.wcs.ows.owcsPackage;

import net.opengis.wcs.wcsPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class owcsPackageImpl extends EPackageImpl implements owcsPackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractReferenceBaseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass acceptFormatsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass acceptVersionsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass allowedValuesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass anyValueTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass basicIdentificationTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass capabilitiesBaseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass coveragesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dcpTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass descriptionTypeEClass = null;

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
    private EClass domainMetadataTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass domainTypeEClass = null;

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
    private EClass httpTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass identificationTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass interpolationMethodBaseTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass interpolationMethodsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass interpolationMethodTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass manifestTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass noValuesTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass operationsMetadataTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass operationTypeEClass = null;

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
    private EClass referenceGroupTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass referenceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass requestMethodTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sectionsTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass serviceIdentificationTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass serviceReferenceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass unNamedDomainTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass valuesReferenceTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass valueTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum rangeClosureTypeEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType interpolationMethodBaseTypeBaseEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType rangeClosureTypeObjectEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType serviceTypeEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType updateSequenceTypeEDataType = null;

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
     * @see net.opengis.wcs.ows.owcsPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private owcsPackageImpl() {
        super(eNS_URI, owcsFactory.eINSTANCE);
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
    public static owcsPackage init() {
        if (isInited) return (owcsPackage)EPackage.Registry.INSTANCE.getEPackage(owcsPackage.eNS_URI);

        // Obtain or create and register package
        owcsPackageImpl theowcsPackage = (owcsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof owcsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new owcsPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        XMLTypePackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        wcsPackageImpl thewcsPackage = (wcsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(wcsPackage.eNS_URI) instanceof wcsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(wcsPackage.eNS_URI) : wcsPackage.eINSTANCE);

        // Create package meta-data objects
        theowcsPackage.createPackageContents();
        thewcsPackage.createPackageContents();

        // Initialize created meta-data
        theowcsPackage.initializePackageContents();
        thewcsPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theowcsPackage.freeze();

        return theowcsPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractReferenceBaseType() {
        return abstractReferenceBaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractReferenceBaseType_Actuate() {
        return (EAttribute)abstractReferenceBaseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractReferenceBaseType_Arcrole() {
        return (EAttribute)abstractReferenceBaseTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractReferenceBaseType_Href() {
        return (EAttribute)abstractReferenceBaseTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractReferenceBaseType_Role() {
        return (EAttribute)abstractReferenceBaseTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractReferenceBaseType_Show() {
        return (EAttribute)abstractReferenceBaseTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractReferenceBaseType_Title() {
        return (EAttribute)abstractReferenceBaseTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractReferenceBaseType_Type() {
        return (EAttribute)abstractReferenceBaseTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAcceptFormatsType() {
        return acceptFormatsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAcceptFormatsType_OutputFormat() {
        return (EAttribute)acceptFormatsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAcceptVersionsType() {
        return acceptVersionsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAcceptVersionsType_Version() {
        return (EAttribute)acceptVersionsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAllowedValuesType() {
        return allowedValuesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAllowedValuesType_Group() {
        return (EAttribute)allowedValuesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAllowedValuesType_Value() {
        return (EReference)allowedValuesTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAllowedValuesType_Range() {
        return (EReference)allowedValuesTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAnyValueType() {
        return anyValueTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBasicIdentificationType() {
        return basicIdentificationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBasicIdentificationType_Identifier() {
        return (EAttribute)basicIdentificationTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getBasicIdentificationType_Metadata() {
        return (EAttribute)basicIdentificationTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCapabilitiesBaseType() {
        return capabilitiesBaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCapabilitiesBaseType_ServiceIdentification() {
        return (EReference)capabilitiesBaseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCapabilitiesBaseType_ServiceProvider() {
        return (EAttribute)capabilitiesBaseTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCapabilitiesBaseType_OperationsMetadata() {
        return (EReference)capabilitiesBaseTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCapabilitiesBaseType_UpdateSequence() {
        return (EAttribute)capabilitiesBaseTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCapabilitiesBaseType_Version() {
        return (EAttribute)capabilitiesBaseTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCoveragesType() {
        return coveragesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDCPType() {
        return dcpTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDCPType_HTTP() {
        return (EReference)dcpTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDescriptionType() {
        return descriptionTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDescriptionType_Title() {
        return (EAttribute)descriptionTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDescriptionType_Abstract() {
        return (EAttribute)descriptionTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDescriptionType_Keywords() {
        return (EAttribute)descriptionTypeEClass.getEStructuralFeatures().get(2);
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
    public EReference getDocumentRoot_AbstractReferenceBase() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_AccessConstraints() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AllowedValues() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_AnyValue() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_AvailableCRS() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Coverage() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ReferenceGroup() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Coverages() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Manifest() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DataType() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DCP() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_DefaultValue() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ExtendedCapabilities() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Fees() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_GetCapabilities() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_HTTP() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Identifier() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_InterpolationMethods() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Language() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MaximumValue() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Meaning() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_MinimumValue() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(24);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_NoValues() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(25);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Operation() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(26);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_OperationsMetadata() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(27);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_OutputFormat() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(28);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Range() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(29);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Reference() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(30);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ReferenceSystem() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(31);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ServiceIdentification() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(32);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ServiceReference() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(33);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Spacing() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(34);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_SupportedCRS() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(35);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_UOM() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(36);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_Value() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(37);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDocumentRoot_ValuesReference() {
        return (EReference)documentRootEClass.getEStructuralFeatures().get(38);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_RangeClosure() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(39);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDocumentRoot_Reference1() {
        return (EAttribute)documentRootEClass.getEStructuralFeatures().get(40);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDomainMetadataType() {
        return domainMetadataTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDomainMetadataType_Value() {
        return (EAttribute)domainMetadataTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDomainMetadataType_Reference() {
        return (EAttribute)domainMetadataTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDomainType() {
        return domainTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDomainType_Name() {
        return (EAttribute)domainTypeEClass.getEStructuralFeatures().get(0);
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
    public EReference getGetCapabilitiesType_AcceptVersions() {
        return (EReference)getCapabilitiesTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGetCapabilitiesType_Sections() {
        return (EReference)getCapabilitiesTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGetCapabilitiesType_AcceptFormats() {
        return (EReference)getCapabilitiesTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGetCapabilitiesType_UpdateSequence() {
        return (EAttribute)getCapabilitiesTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getHTTPType() {
        return httpTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getHTTPType_Group() {
        return (EAttribute)httpTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getHTTPType_Get() {
        return (EReference)httpTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getHTTPType_Post() {
        return (EReference)httpTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getIdentificationType() {
        return identificationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIdentificationType_BoundingBox() {
        return (EAttribute)identificationTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIdentificationType_OutputFormat() {
        return (EAttribute)identificationTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIdentificationType_AvailableCRSGroup() {
        return (EAttribute)identificationTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIdentificationType_AvailableCRS() {
        return (EAttribute)identificationTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getInterpolationMethodBaseType() {
        return interpolationMethodBaseTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getInterpolationMethodBaseType_Value() {
        return (EAttribute)interpolationMethodBaseTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getInterpolationMethodsType() {
        return interpolationMethodsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getInterpolationMethodsType_DefaultMethod() {
        return (EReference)interpolationMethodsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getInterpolationMethodsType_OtherMethod() {
        return (EReference)interpolationMethodsTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getInterpolationMethodType() {
        return interpolationMethodTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getInterpolationMethodType_NullResistance() {
        return (EAttribute)interpolationMethodTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getManifestType() {
        return manifestTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getManifestType_ReferenceGroupGroup() {
        return (EAttribute)manifestTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getManifestType_ReferenceGroup() {
        return (EReference)manifestTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getNoValuesType() {
        return noValuesTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOperationsMetadataType() {
        return operationsMetadataTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationsMetadataType_Operation() {
        return (EReference)operationsMetadataTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationsMetadataType_Parameter() {
        return (EReference)operationsMetadataTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationsMetadataType_Constraint() {
        return (EReference)operationsMetadataTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationsMetadataType_ExtendedCapabilities() {
        return (EReference)operationsMetadataTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOperationType() {
        return operationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationType_DCP() {
        return (EReference)operationTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationType_Parameter() {
        return (EReference)operationTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationType_Constraint() {
        return (EReference)operationTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationType_Metadata() {
        return (EAttribute)operationTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOperationType_Name() {
        return (EAttribute)operationTypeEClass.getEStructuralFeatures().get(4);
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
    public EReference getRangeType_MinimumValue() {
        return (EReference)rangeTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRangeType_MaximumValue() {
        return (EReference)rangeTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRangeType_Spacing() {
        return (EReference)rangeTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRangeType_RangeClosure() {
        return (EAttribute)rangeTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getReferenceGroupType() {
        return referenceGroupTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceGroupType_AbstractReferenceBaseGroup() {
        return (EAttribute)referenceGroupTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getReferenceGroupType_AbstractReferenceBase() {
        return (EReference)referenceGroupTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getReferenceType() {
        return referenceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceType_Identifier() {
        return (EAttribute)referenceTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceType_Abstract() {
        return (EAttribute)referenceTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceType_Format() {
        return (EAttribute)referenceTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getReferenceType_Metadata() {
        return (EAttribute)referenceTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRequestMethodType() {
        return requestMethodTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRequestMethodType_Constraint() {
        return (EReference)requestMethodTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSectionsType() {
        return sectionsTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSectionsType_Section() {
        return (EAttribute)sectionsTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getServiceIdentificationType() {
        return serviceIdentificationTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getServiceIdentificationType_ServiceType() {
        return (EAttribute)serviceIdentificationTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getServiceIdentificationType_ServiceTypeVersion() {
        return (EAttribute)serviceIdentificationTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getServiceIdentificationType_Profile() {
        return (EAttribute)serviceIdentificationTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getServiceIdentificationType_Fees() {
        return (EAttribute)serviceIdentificationTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getServiceIdentificationType_AccessConstraints() {
        return (EAttribute)serviceIdentificationTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getServiceReferenceType() {
        return serviceReferenceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getServiceReferenceType_RequestMessage() {
        return (EReference)serviceReferenceTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getServiceReferenceType_RequestMessageReference() {
        return (EAttribute)serviceReferenceTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getUnNamedDomainType() {
        return unNamedDomainTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUnNamedDomainType_AllowedValues() {
        return (EReference)unNamedDomainTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUnNamedDomainType_AnyValue() {
        return (EReference)unNamedDomainTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUnNamedDomainType_NoValues() {
        return (EReference)unNamedDomainTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUnNamedDomainType_ValuesReference() {
        return (EReference)unNamedDomainTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUnNamedDomainType_DefaultValue() {
        return (EReference)unNamedDomainTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUnNamedDomainType_Meaning() {
        return (EReference)unNamedDomainTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUnNamedDomainType_DataType() {
        return (EReference)unNamedDomainTypeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUnNamedDomainType_UOM() {
        return (EReference)unNamedDomainTypeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUnNamedDomainType_ReferenceSystem() {
        return (EReference)unNamedDomainTypeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getUnNamedDomainType_Metadata() {
        return (EAttribute)unNamedDomainTypeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getValuesReferenceType() {
        return valuesReferenceTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuesReferenceType_Value() {
        return (EAttribute)valuesReferenceTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValuesReferenceType_Reference() {
        return (EAttribute)valuesReferenceTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getValueType() {
        return valueTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getValueType_Value() {
        return (EAttribute)valueTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getRangeClosureType() {
        return rangeClosureTypeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getInterpolationMethodBaseTypeBase() {
        return interpolationMethodBaseTypeBaseEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getRangeClosureTypeObject() {
        return rangeClosureTypeObjectEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getServiceType() {
        return serviceTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getUpdateSequenceType() {
        return updateSequenceTypeEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public owcsFactory getowcsFactory() {
        return (owcsFactory)getEFactoryInstance();
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
        abstractReferenceBaseTypeEClass = createEClass(ABSTRACT_REFERENCE_BASE_TYPE);
        createEAttribute(abstractReferenceBaseTypeEClass, ABSTRACT_REFERENCE_BASE_TYPE__ACTUATE);
        createEAttribute(abstractReferenceBaseTypeEClass, ABSTRACT_REFERENCE_BASE_TYPE__ARCROLE);
        createEAttribute(abstractReferenceBaseTypeEClass, ABSTRACT_REFERENCE_BASE_TYPE__HREF);
        createEAttribute(abstractReferenceBaseTypeEClass, ABSTRACT_REFERENCE_BASE_TYPE__ROLE);
        createEAttribute(abstractReferenceBaseTypeEClass, ABSTRACT_REFERENCE_BASE_TYPE__SHOW);
        createEAttribute(abstractReferenceBaseTypeEClass, ABSTRACT_REFERENCE_BASE_TYPE__TITLE);
        createEAttribute(abstractReferenceBaseTypeEClass, ABSTRACT_REFERENCE_BASE_TYPE__TYPE);

        acceptFormatsTypeEClass = createEClass(ACCEPT_FORMATS_TYPE);
        createEAttribute(acceptFormatsTypeEClass, ACCEPT_FORMATS_TYPE__OUTPUT_FORMAT);

        acceptVersionsTypeEClass = createEClass(ACCEPT_VERSIONS_TYPE);
        createEAttribute(acceptVersionsTypeEClass, ACCEPT_VERSIONS_TYPE__VERSION);

        allowedValuesTypeEClass = createEClass(ALLOWED_VALUES_TYPE);
        createEAttribute(allowedValuesTypeEClass, ALLOWED_VALUES_TYPE__GROUP);
        createEReference(allowedValuesTypeEClass, ALLOWED_VALUES_TYPE__VALUE);
        createEReference(allowedValuesTypeEClass, ALLOWED_VALUES_TYPE__RANGE);

        anyValueTypeEClass = createEClass(ANY_VALUE_TYPE);

        basicIdentificationTypeEClass = createEClass(BASIC_IDENTIFICATION_TYPE);
        createEAttribute(basicIdentificationTypeEClass, BASIC_IDENTIFICATION_TYPE__IDENTIFIER);
        createEAttribute(basicIdentificationTypeEClass, BASIC_IDENTIFICATION_TYPE__METADATA);

        capabilitiesBaseTypeEClass = createEClass(CAPABILITIES_BASE_TYPE);
        createEReference(capabilitiesBaseTypeEClass, CAPABILITIES_BASE_TYPE__SERVICE_IDENTIFICATION);
        createEAttribute(capabilitiesBaseTypeEClass, CAPABILITIES_BASE_TYPE__SERVICE_PROVIDER);
        createEReference(capabilitiesBaseTypeEClass, CAPABILITIES_BASE_TYPE__OPERATIONS_METADATA);
        createEAttribute(capabilitiesBaseTypeEClass, CAPABILITIES_BASE_TYPE__UPDATE_SEQUENCE);
        createEAttribute(capabilitiesBaseTypeEClass, CAPABILITIES_BASE_TYPE__VERSION);

        coveragesTypeEClass = createEClass(COVERAGES_TYPE);

        dcpTypeEClass = createEClass(DCP_TYPE);
        createEReference(dcpTypeEClass, DCP_TYPE__HTTP);

        descriptionTypeEClass = createEClass(DESCRIPTION_TYPE);
        createEAttribute(descriptionTypeEClass, DESCRIPTION_TYPE__TITLE);
        createEAttribute(descriptionTypeEClass, DESCRIPTION_TYPE__ABSTRACT);
        createEAttribute(descriptionTypeEClass, DESCRIPTION_TYPE__KEYWORDS);

        documentRootEClass = createEClass(DOCUMENT_ROOT);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ABSTRACT_REFERENCE_BASE);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__ACCESS_CONSTRAINTS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ALLOWED_VALUES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__ANY_VALUE);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__AVAILABLE_CRS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__COVERAGE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__REFERENCE_GROUP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__COVERAGES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__MANIFEST);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DATA_TYPE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DCP);
        createEReference(documentRootEClass, DOCUMENT_ROOT__DEFAULT_VALUE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__EXTENDED_CAPABILITIES);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__FEES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__GET_CAPABILITIES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__HTTP);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__IDENTIFIER);
        createEReference(documentRootEClass, DOCUMENT_ROOT__INTERPOLATION_METHODS);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__LANGUAGE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__MAXIMUM_VALUE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__MEANING);
        createEReference(documentRootEClass, DOCUMENT_ROOT__MINIMUM_VALUE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__NO_VALUES);
        createEReference(documentRootEClass, DOCUMENT_ROOT__OPERATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__OPERATIONS_METADATA);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__OUTPUT_FORMAT);
        createEReference(documentRootEClass, DOCUMENT_ROOT__RANGE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__REFERENCE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__REFERENCE_SYSTEM);
        createEReference(documentRootEClass, DOCUMENT_ROOT__SERVICE_IDENTIFICATION);
        createEReference(documentRootEClass, DOCUMENT_ROOT__SERVICE_REFERENCE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__SPACING);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__SUPPORTED_CRS);
        createEReference(documentRootEClass, DOCUMENT_ROOT__UOM);
        createEReference(documentRootEClass, DOCUMENT_ROOT__VALUE);
        createEReference(documentRootEClass, DOCUMENT_ROOT__VALUES_REFERENCE);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__RANGE_CLOSURE);
        createEAttribute(documentRootEClass, DOCUMENT_ROOT__REFERENCE1);

        domainMetadataTypeEClass = createEClass(DOMAIN_METADATA_TYPE);
        createEAttribute(domainMetadataTypeEClass, DOMAIN_METADATA_TYPE__VALUE);
        createEAttribute(domainMetadataTypeEClass, DOMAIN_METADATA_TYPE__REFERENCE);

        domainTypeEClass = createEClass(DOMAIN_TYPE);
        createEAttribute(domainTypeEClass, DOMAIN_TYPE__NAME);

        getCapabilitiesTypeEClass = createEClass(GET_CAPABILITIES_TYPE);
        createEReference(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS);
        createEReference(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__SECTIONS);
        createEReference(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__ACCEPT_FORMATS);
        createEAttribute(getCapabilitiesTypeEClass, GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE);

        httpTypeEClass = createEClass(HTTP_TYPE);
        createEAttribute(httpTypeEClass, HTTP_TYPE__GROUP);
        createEReference(httpTypeEClass, HTTP_TYPE__GET);
        createEReference(httpTypeEClass, HTTP_TYPE__POST);

        identificationTypeEClass = createEClass(IDENTIFICATION_TYPE);
        createEAttribute(identificationTypeEClass, IDENTIFICATION_TYPE__BOUNDING_BOX);
        createEAttribute(identificationTypeEClass, IDENTIFICATION_TYPE__OUTPUT_FORMAT);
        createEAttribute(identificationTypeEClass, IDENTIFICATION_TYPE__AVAILABLE_CRS_GROUP);
        createEAttribute(identificationTypeEClass, IDENTIFICATION_TYPE__AVAILABLE_CRS);

        interpolationMethodBaseTypeEClass = createEClass(INTERPOLATION_METHOD_BASE_TYPE);
        createEAttribute(interpolationMethodBaseTypeEClass, INTERPOLATION_METHOD_BASE_TYPE__VALUE);

        interpolationMethodsTypeEClass = createEClass(INTERPOLATION_METHODS_TYPE);
        createEReference(interpolationMethodsTypeEClass, INTERPOLATION_METHODS_TYPE__DEFAULT_METHOD);
        createEReference(interpolationMethodsTypeEClass, INTERPOLATION_METHODS_TYPE__OTHER_METHOD);

        interpolationMethodTypeEClass = createEClass(INTERPOLATION_METHOD_TYPE);
        createEAttribute(interpolationMethodTypeEClass, INTERPOLATION_METHOD_TYPE__NULL_RESISTANCE);

        manifestTypeEClass = createEClass(MANIFEST_TYPE);
        createEAttribute(manifestTypeEClass, MANIFEST_TYPE__REFERENCE_GROUP_GROUP);
        createEReference(manifestTypeEClass, MANIFEST_TYPE__REFERENCE_GROUP);

        noValuesTypeEClass = createEClass(NO_VALUES_TYPE);

        operationsMetadataTypeEClass = createEClass(OPERATIONS_METADATA_TYPE);
        createEReference(operationsMetadataTypeEClass, OPERATIONS_METADATA_TYPE__OPERATION);
        createEReference(operationsMetadataTypeEClass, OPERATIONS_METADATA_TYPE__PARAMETER);
        createEReference(operationsMetadataTypeEClass, OPERATIONS_METADATA_TYPE__CONSTRAINT);
        createEReference(operationsMetadataTypeEClass, OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES);

        operationTypeEClass = createEClass(OPERATION_TYPE);
        createEReference(operationTypeEClass, OPERATION_TYPE__DCP);
        createEReference(operationTypeEClass, OPERATION_TYPE__PARAMETER);
        createEReference(operationTypeEClass, OPERATION_TYPE__CONSTRAINT);
        createEAttribute(operationTypeEClass, OPERATION_TYPE__METADATA);
        createEAttribute(operationTypeEClass, OPERATION_TYPE__NAME);

        rangeTypeEClass = createEClass(RANGE_TYPE);
        createEReference(rangeTypeEClass, RANGE_TYPE__MINIMUM_VALUE);
        createEReference(rangeTypeEClass, RANGE_TYPE__MAXIMUM_VALUE);
        createEReference(rangeTypeEClass, RANGE_TYPE__SPACING);
        createEAttribute(rangeTypeEClass, RANGE_TYPE__RANGE_CLOSURE);

        referenceGroupTypeEClass = createEClass(REFERENCE_GROUP_TYPE);
        createEAttribute(referenceGroupTypeEClass, REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE_GROUP);
        createEReference(referenceGroupTypeEClass, REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE);

        referenceTypeEClass = createEClass(REFERENCE_TYPE);
        createEAttribute(referenceTypeEClass, REFERENCE_TYPE__IDENTIFIER);
        createEAttribute(referenceTypeEClass, REFERENCE_TYPE__ABSTRACT);
        createEAttribute(referenceTypeEClass, REFERENCE_TYPE__FORMAT);
        createEAttribute(referenceTypeEClass, REFERENCE_TYPE__METADATA);

        requestMethodTypeEClass = createEClass(REQUEST_METHOD_TYPE);
        createEReference(requestMethodTypeEClass, REQUEST_METHOD_TYPE__CONSTRAINT);

        sectionsTypeEClass = createEClass(SECTIONS_TYPE);
        createEAttribute(sectionsTypeEClass, SECTIONS_TYPE__SECTION);

        serviceIdentificationTypeEClass = createEClass(SERVICE_IDENTIFICATION_TYPE);
        createEAttribute(serviceIdentificationTypeEClass, SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE);
        createEAttribute(serviceIdentificationTypeEClass, SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION);
        createEAttribute(serviceIdentificationTypeEClass, SERVICE_IDENTIFICATION_TYPE__PROFILE);
        createEAttribute(serviceIdentificationTypeEClass, SERVICE_IDENTIFICATION_TYPE__FEES);
        createEAttribute(serviceIdentificationTypeEClass, SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS);

        serviceReferenceTypeEClass = createEClass(SERVICE_REFERENCE_TYPE);
        createEReference(serviceReferenceTypeEClass, SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE);
        createEAttribute(serviceReferenceTypeEClass, SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE_REFERENCE);

        unNamedDomainTypeEClass = createEClass(UN_NAMED_DOMAIN_TYPE);
        createEReference(unNamedDomainTypeEClass, UN_NAMED_DOMAIN_TYPE__ALLOWED_VALUES);
        createEReference(unNamedDomainTypeEClass, UN_NAMED_DOMAIN_TYPE__ANY_VALUE);
        createEReference(unNamedDomainTypeEClass, UN_NAMED_DOMAIN_TYPE__NO_VALUES);
        createEReference(unNamedDomainTypeEClass, UN_NAMED_DOMAIN_TYPE__VALUES_REFERENCE);
        createEReference(unNamedDomainTypeEClass, UN_NAMED_DOMAIN_TYPE__DEFAULT_VALUE);
        createEReference(unNamedDomainTypeEClass, UN_NAMED_DOMAIN_TYPE__MEANING);
        createEReference(unNamedDomainTypeEClass, UN_NAMED_DOMAIN_TYPE__DATA_TYPE);
        createEReference(unNamedDomainTypeEClass, UN_NAMED_DOMAIN_TYPE__UOM);
        createEReference(unNamedDomainTypeEClass, UN_NAMED_DOMAIN_TYPE__REFERENCE_SYSTEM);
        createEAttribute(unNamedDomainTypeEClass, UN_NAMED_DOMAIN_TYPE__METADATA);

        valuesReferenceTypeEClass = createEClass(VALUES_REFERENCE_TYPE);
        createEAttribute(valuesReferenceTypeEClass, VALUES_REFERENCE_TYPE__VALUE);
        createEAttribute(valuesReferenceTypeEClass, VALUES_REFERENCE_TYPE__REFERENCE);

        valueTypeEClass = createEClass(VALUE_TYPE);
        createEAttribute(valueTypeEClass, VALUE_TYPE__VALUE);

        // Create enums
        rangeClosureTypeEEnum = createEEnum(RANGE_CLOSURE_TYPE);

        // Create data types
        interpolationMethodBaseTypeBaseEDataType = createEDataType(INTERPOLATION_METHOD_BASE_TYPE_BASE);
        rangeClosureTypeObjectEDataType = createEDataType(RANGE_CLOSURE_TYPE_OBJECT);
        serviceTypeEDataType = createEDataType(SERVICE_TYPE);
        updateSequenceTypeEDataType = createEDataType(UPDATE_SEQUENCE_TYPE);
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
        XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

        // Add supertypes to classes
        basicIdentificationTypeEClass.getESuperTypes().add(this.getDescriptionType());
        coveragesTypeEClass.getESuperTypes().add(this.getManifestType());
        domainTypeEClass.getESuperTypes().add(this.getUnNamedDomainType());
        identificationTypeEClass.getESuperTypes().add(this.getBasicIdentificationType());
        interpolationMethodTypeEClass.getESuperTypes().add(this.getInterpolationMethodBaseType());
        manifestTypeEClass.getESuperTypes().add(this.getBasicIdentificationType());
        referenceGroupTypeEClass.getESuperTypes().add(this.getBasicIdentificationType());
        referenceTypeEClass.getESuperTypes().add(this.getAbstractReferenceBaseType());
        serviceIdentificationTypeEClass.getESuperTypes().add(this.getDescriptionType());
        serviceReferenceTypeEClass.getESuperTypes().add(this.getReferenceType());

        // Initialize classes and features; add operations and parameters
        initEClass(abstractReferenceBaseTypeEClass, AbstractReferenceBaseType.class, "AbstractReferenceBaseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAbstractReferenceBaseType_Actuate(), theXMLTypePackage.getAnySimpleType(), "actuate", null, 0, 1, AbstractReferenceBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractReferenceBaseType_Arcrole(), theXMLTypePackage.getAnySimpleType(), "arcrole", null, 0, 1, AbstractReferenceBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractReferenceBaseType_Href(), theXMLTypePackage.getAnySimpleType(), "href", null, 1, 1, AbstractReferenceBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractReferenceBaseType_Role(), theXMLTypePackage.getAnySimpleType(), "role", null, 0, 1, AbstractReferenceBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractReferenceBaseType_Show(), theXMLTypePackage.getAnySimpleType(), "show", null, 0, 1, AbstractReferenceBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractReferenceBaseType_Title(), theXMLTypePackage.getAnySimpleType(), "title", null, 0, 1, AbstractReferenceBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractReferenceBaseType_Type(), theXMLTypePackage.getString(), "type", "simple", 0, 1, AbstractReferenceBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(acceptFormatsTypeEClass, AcceptFormatsType.class, "AcceptFormatsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAcceptFormatsType_OutputFormat(), theXMLTypePackage.getAnySimpleType(), "outputFormat", null, 0, -1, AcceptFormatsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(acceptVersionsTypeEClass, AcceptVersionsType.class, "AcceptVersionsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAcceptVersionsType_Version(), theXMLTypePackage.getAnySimpleType(), "version", null, 1, -1, AcceptVersionsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(allowedValuesTypeEClass, AllowedValuesType.class, "AllowedValuesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAllowedValuesType_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, AllowedValuesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getAllowedValuesType_Value(), this.getValueType(), null, "value", null, 0, -1, AllowedValuesType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getAllowedValuesType_Range(), this.getRangeType(), null, "range", null, 0, -1, AllowedValuesType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(anyValueTypeEClass, AnyValueType.class, "AnyValueType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(basicIdentificationTypeEClass, BasicIdentificationType.class, "BasicIdentificationType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getBasicIdentificationType_Identifier(), theXMLTypePackage.getAnySimpleType(), "identifier", null, 0, 1, BasicIdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getBasicIdentificationType_Metadata(), theXMLTypePackage.getAnySimpleType(), "metadata", null, 0, -1, BasicIdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(capabilitiesBaseTypeEClass, CapabilitiesBaseType.class, "CapabilitiesBaseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCapabilitiesBaseType_ServiceIdentification(), this.getServiceIdentificationType(), null, "serviceIdentification", null, 0, 1, CapabilitiesBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCapabilitiesBaseType_ServiceProvider(), theXMLTypePackage.getAnySimpleType(), "serviceProvider", null, 0, 1, CapabilitiesBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCapabilitiesBaseType_OperationsMetadata(), this.getOperationsMetadataType(), null, "operationsMetadata", null, 0, 1, CapabilitiesBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCapabilitiesBaseType_UpdateSequence(), this.getUpdateSequenceType(), "updateSequence", null, 0, 1, CapabilitiesBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCapabilitiesBaseType_Version(), theXMLTypePackage.getAnySimpleType(), "version", null, 1, 1, CapabilitiesBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(coveragesTypeEClass, CoveragesType.class, "CoveragesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(dcpTypeEClass, DCPType.class, "DCPType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDCPType_HTTP(), this.getHTTPType(), null, "hTTP", null, 0, 1, DCPType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(descriptionTypeEClass, DescriptionType.class, "DescriptionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDescriptionType_Title(), theXMLTypePackage.getAnySimpleType(), "title", null, 0, 1, DescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDescriptionType_Abstract(), theXMLTypePackage.getAnySimpleType(), "abstract", null, 0, 1, DescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDescriptionType_Keywords(), theXMLTypePackage.getAnySimpleType(), "keywords", null, 0, -1, DescriptionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AbstractReferenceBase(), this.getAbstractReferenceBaseType(), null, "abstractReferenceBase", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_AccessConstraints(), theXMLTypePackage.getString(), "accessConstraints", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AllowedValues(), this.getAllowedValuesType(), null, "allowedValues", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_AnyValue(), this.getAnyValueType(), null, "anyValue", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_AvailableCRS(), theXMLTypePackage.getAnyURI(), "availableCRS", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Coverage(), this.getReferenceGroupType(), null, "coverage", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ReferenceGroup(), this.getReferenceGroupType(), null, "referenceGroup", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Coverages(), this.getCoveragesType(), null, "coverages", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Manifest(), this.getManifestType(), null, "manifest", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DataType(), this.getDomainMetadataType(), null, "dataType", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DCP(), this.getDCPType(), null, "dCP", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_DefaultValue(), this.getValueType(), null, "defaultValue", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ExtendedCapabilities(), ecorePackage.getEObject(), null, "extendedCapabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_Fees(), theXMLTypePackage.getString(), "fees", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_GetCapabilities(), this.getGetCapabilitiesType(), null, "getCapabilities", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_HTTP(), this.getHTTPType(), null, "hTTP", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_Identifier(), theXMLTypePackage.getAnySimpleType(), "identifier", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_InterpolationMethods(), this.getInterpolationMethodsType(), null, "interpolationMethods", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_Language(), theXMLTypePackage.getLanguage(), "language", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_MaximumValue(), this.getValueType(), null, "maximumValue", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Meaning(), this.getDomainMetadataType(), null, "meaning", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_MinimumValue(), this.getValueType(), null, "minimumValue", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_NoValues(), this.getNoValuesType(), null, "noValues", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Operation(), this.getOperationType(), null, "operation", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_OperationsMetadata(), this.getOperationsMetadataType(), null, "operationsMetadata", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_OutputFormat(), theXMLTypePackage.getAnySimpleType(), "outputFormat", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Range(), this.getRangeType(), null, "range", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Reference(), this.getReferenceType(), null, "reference", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ReferenceSystem(), this.getDomainMetadataType(), null, "referenceSystem", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ServiceIdentification(), this.getServiceIdentificationType(), null, "serviceIdentification", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ServiceReference(), this.getServiceReferenceType(), null, "serviceReference", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Spacing(), this.getValueType(), null, "spacing", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_SupportedCRS(), theXMLTypePackage.getAnyURI(), "supportedCRS", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_UOM(), this.getDomainMetadataType(), null, "uOM", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_Value(), this.getValueType(), null, "value", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDocumentRoot_ValuesReference(), this.getValuesReferenceType(), null, "valuesReference", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_RangeClosure(), this.getRangeClosureType(), "rangeClosure", "closed", 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDocumentRoot_Reference1(), theXMLTypePackage.getAnyURI(), "reference1", null, 0, 1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(domainMetadataTypeEClass, DomainMetadataType.class, "DomainMetadataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDomainMetadataType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, DomainMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDomainMetadataType_Reference(), theXMLTypePackage.getAnyURI(), "reference", null, 0, 1, DomainMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(domainTypeEClass, DomainType.class, "DomainType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDomainType_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, DomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(getCapabilitiesTypeEClass, GetCapabilitiesType.class, "GetCapabilitiesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getGetCapabilitiesType_AcceptVersions(), this.getAcceptVersionsType(), null, "acceptVersions", null, 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGetCapabilitiesType_Sections(), this.getSectionsType(), null, "sections", null, 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGetCapabilitiesType_AcceptFormats(), this.getAcceptFormatsType(), null, "acceptFormats", null, 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGetCapabilitiesType_UpdateSequence(), this.getUpdateSequenceType(), "updateSequence", null, 0, 1, GetCapabilitiesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(httpTypeEClass, HTTPType.class, "HTTPType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getHTTPType_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, HTTPType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getHTTPType_Get(), this.getRequestMethodType(), null, "get", null, 0, -1, HTTPType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getHTTPType_Post(), this.getRequestMethodType(), null, "post", null, 0, -1, HTTPType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(identificationTypeEClass, IdentificationType.class, "IdentificationType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getIdentificationType_BoundingBox(), theXMLTypePackage.getAnySimpleType(), "boundingBox", null, 0, -1, IdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIdentificationType_OutputFormat(), theXMLTypePackage.getAnySimpleType(), "outputFormat", null, 0, -1, IdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIdentificationType_AvailableCRSGroup(), ecorePackage.getEFeatureMapEntry(), "availableCRSGroup", null, 0, -1, IdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getIdentificationType_AvailableCRS(), theXMLTypePackage.getAnyURI(), "availableCRS", null, 0, -1, IdentificationType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(interpolationMethodBaseTypeEClass, InterpolationMethodBaseType.class, "InterpolationMethodBaseType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getInterpolationMethodBaseType_Value(), this.getInterpolationMethodBaseTypeBase(), "value", null, 0, 1, InterpolationMethodBaseType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(interpolationMethodsTypeEClass, InterpolationMethodsType.class, "InterpolationMethodsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getInterpolationMethodsType_DefaultMethod(), this.getInterpolationMethodType(), null, "defaultMethod", null, 1, 1, InterpolationMethodsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getInterpolationMethodsType_OtherMethod(), this.getInterpolationMethodType(), null, "otherMethod", null, 0, -1, InterpolationMethodsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(interpolationMethodTypeEClass, InterpolationMethodType.class, "InterpolationMethodType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getInterpolationMethodType_NullResistance(), theXMLTypePackage.getString(), "nullResistance", null, 0, 1, InterpolationMethodType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(manifestTypeEClass, ManifestType.class, "ManifestType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getManifestType_ReferenceGroupGroup(), ecorePackage.getEFeatureMapEntry(), "referenceGroupGroup", null, 1, -1, ManifestType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getManifestType_ReferenceGroup(), this.getReferenceGroupType(), null, "referenceGroup", null, 1, -1, ManifestType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(noValuesTypeEClass, NoValuesType.class, "NoValuesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(operationsMetadataTypeEClass, OperationsMetadataType.class, "OperationsMetadataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getOperationsMetadataType_Operation(), this.getOperationType(), null, "operation", null, 2, -1, OperationsMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getOperationsMetadataType_Parameter(), this.getDomainType(), null, "parameter", null, 0, -1, OperationsMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getOperationsMetadataType_Constraint(), this.getDomainType(), null, "constraint", null, 0, -1, OperationsMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getOperationsMetadataType_ExtendedCapabilities(), ecorePackage.getEObject(), null, "extendedCapabilities", null, 0, 1, OperationsMetadataType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(operationTypeEClass, OperationType.class, "OperationType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getOperationType_DCP(), this.getDCPType(), null, "dCP", null, 1, -1, OperationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getOperationType_Parameter(), this.getDomainType(), null, "parameter", null, 0, -1, OperationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getOperationType_Constraint(), this.getDomainType(), null, "constraint", null, 0, -1, OperationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getOperationType_Metadata(), theXMLTypePackage.getAnySimpleType(), "metadata", null, 0, -1, OperationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getOperationType_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, OperationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(rangeTypeEClass, RangeType.class, "RangeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRangeType_MinimumValue(), this.getValueType(), null, "minimumValue", null, 0, 1, RangeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getRangeType_MaximumValue(), this.getValueType(), null, "maximumValue", null, 0, 1, RangeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getRangeType_Spacing(), this.getValueType(), null, "spacing", null, 0, 1, RangeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRangeType_RangeClosure(), this.getRangeClosureType(), "rangeClosure", "closed", 0, 1, RangeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(referenceGroupTypeEClass, ReferenceGroupType.class, "ReferenceGroupType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getReferenceGroupType_AbstractReferenceBaseGroup(), ecorePackage.getEFeatureMapEntry(), "abstractReferenceBaseGroup", null, 1, -1, ReferenceGroupType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getReferenceGroupType_AbstractReferenceBase(), this.getAbstractReferenceBaseType(), null, "abstractReferenceBase", null, 1, -1, ReferenceGroupType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(referenceTypeEClass, ReferenceType.class, "ReferenceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getReferenceType_Identifier(), theXMLTypePackage.getAnySimpleType(), "identifier", null, 0, 1, ReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getReferenceType_Abstract(), theXMLTypePackage.getAnySimpleType(), "abstract", null, 0, 1, ReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getReferenceType_Format(), theXMLTypePackage.getAnySimpleType(), "format", null, 0, 1, ReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getReferenceType_Metadata(), theXMLTypePackage.getAnySimpleType(), "metadata", null, 0, -1, ReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(requestMethodTypeEClass, RequestMethodType.class, "RequestMethodType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRequestMethodType_Constraint(), this.getDomainType(), null, "constraint", null, 0, -1, RequestMethodType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(sectionsTypeEClass, SectionsType.class, "SectionsType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSectionsType_Section(), theXMLTypePackage.getString(), "section", null, 0, -1, SectionsType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(serviceIdentificationTypeEClass, ServiceIdentificationType.class, "ServiceIdentificationType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getServiceIdentificationType_ServiceType(), theXMLTypePackage.getAnySimpleType(), "serviceType", null, 1, 1, ServiceIdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getServiceIdentificationType_ServiceTypeVersion(), theXMLTypePackage.getAnySimpleType(), "serviceTypeVersion", null, 1, -1, ServiceIdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getServiceIdentificationType_Profile(), theXMLTypePackage.getAnyURI(), "profile", null, 0, -1, ServiceIdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getServiceIdentificationType_Fees(), theXMLTypePackage.getString(), "fees", null, 0, 1, ServiceIdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getServiceIdentificationType_AccessConstraints(), theXMLTypePackage.getString(), "accessConstraints", null, 0, -1, ServiceIdentificationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(serviceReferenceTypeEClass, ServiceReferenceType.class, "ServiceReferenceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getServiceReferenceType_RequestMessage(), ecorePackage.getEObject(), null, "requestMessage", null, 0, 1, ServiceReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getServiceReferenceType_RequestMessageReference(), theXMLTypePackage.getAnyURI(), "requestMessageReference", null, 0, 1, ServiceReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(unNamedDomainTypeEClass, UnNamedDomainType.class, "UnNamedDomainType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getUnNamedDomainType_AllowedValues(), this.getAllowedValuesType(), null, "allowedValues", null, 0, 1, UnNamedDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUnNamedDomainType_AnyValue(), this.getAnyValueType(), null, "anyValue", null, 0, 1, UnNamedDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUnNamedDomainType_NoValues(), this.getNoValuesType(), null, "noValues", null, 0, 1, UnNamedDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUnNamedDomainType_ValuesReference(), this.getValuesReferenceType(), null, "valuesReference", null, 0, 1, UnNamedDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUnNamedDomainType_DefaultValue(), this.getValueType(), null, "defaultValue", null, 0, 1, UnNamedDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUnNamedDomainType_Meaning(), this.getDomainMetadataType(), null, "meaning", null, 0, 1, UnNamedDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUnNamedDomainType_DataType(), this.getDomainMetadataType(), null, "dataType", null, 0, 1, UnNamedDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUnNamedDomainType_UOM(), this.getDomainMetadataType(), null, "uOM", null, 0, 1, UnNamedDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUnNamedDomainType_ReferenceSystem(), this.getDomainMetadataType(), null, "referenceSystem", null, 0, 1, UnNamedDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getUnNamedDomainType_Metadata(), theXMLTypePackage.getAnySimpleType(), "metadata", null, 0, -1, UnNamedDomainType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(valuesReferenceTypeEClass, ValuesReferenceType.class, "ValuesReferenceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getValuesReferenceType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, ValuesReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getValuesReferenceType_Reference(), theXMLTypePackage.getAnyURI(), "reference", null, 1, 1, ValuesReferenceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(valueTypeEClass, ValueType.class, "ValueType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getValueType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, ValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Initialize enums and add enum literals
        initEEnum(rangeClosureTypeEEnum, RangeClosureType.class, "RangeClosureType");
        addEEnumLiteral(rangeClosureTypeEEnum, RangeClosureType.CLOSED_LITERAL);
        addEEnumLiteral(rangeClosureTypeEEnum, RangeClosureType.OPEN_LITERAL);
        addEEnumLiteral(rangeClosureTypeEEnum, RangeClosureType.OPEN_CLOSED_LITERAL);
        addEEnumLiteral(rangeClosureTypeEEnum, RangeClosureType.CLOSED_OPEN_LITERAL);

        // Initialize data types
        initEDataType(interpolationMethodBaseTypeBaseEDataType, Object.class, "InterpolationMethodBaseTypeBase", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(rangeClosureTypeObjectEDataType, RangeClosureType.class, "RangeClosureTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
        initEDataType(serviceTypeEDataType, String.class, "ServiceType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(updateSequenceTypeEDataType, String.class, "UpdateSequenceType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

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
             "appinfo", "owsManifest.xsd 2006-09-27\r\nowsDataIdentification.xsd 2006-09-27"
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
          (abstractReferenceBaseTypeEClass, 
           source, 
           new String[] {
             "name", "AbstractReferenceBaseType",
             "kind", "empty"
           });			
        addAnnotation
          (getAbstractReferenceBaseType_Actuate(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "actuate",
             "namespace", "http://www.w3.org/1999/xlink"
           });			
        addAnnotation
          (getAbstractReferenceBaseType_Arcrole(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "arcrole",
             "namespace", "http://www.w3.org/1999/xlink"
           });			
        addAnnotation
          (getAbstractReferenceBaseType_Href(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "href",
             "namespace", "http://www.w3.org/1999/xlink"
           });			
        addAnnotation
          (getAbstractReferenceBaseType_Role(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "role",
             "namespace", "http://www.w3.org/1999/xlink"
           });			
        addAnnotation
          (getAbstractReferenceBaseType_Show(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "show",
             "namespace", "http://www.w3.org/1999/xlink"
           });			
        addAnnotation
          (getAbstractReferenceBaseType_Title(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "title",
             "namespace", "http://www.w3.org/1999/xlink"
           });		
        addAnnotation
          (getAbstractReferenceBaseType_Type(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "type",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (acceptFormatsTypeEClass, 
           source, 
           new String[] {
             "name", "AcceptFormatsType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getAcceptFormatsType_OutputFormat(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OutputFormat",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (acceptVersionsTypeEClass, 
           source, 
           new String[] {
             "name", "AcceptVersionsType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getAcceptVersionsType_Version(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Version",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (allowedValuesTypeEClass, 
           source, 
           new String[] {
             "name", "AllowedValues_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getAllowedValuesType_Group(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "group:0"
           });		
        addAnnotation
          (getAllowedValuesType_Value(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Value",
             "namespace", "##targetNamespace",
             "group", "#group:0"
           });		
        addAnnotation
          (getAllowedValuesType_Range(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Range",
             "namespace", "##targetNamespace",
             "group", "#group:0"
           });		
        addAnnotation
          (anyValueTypeEClass, 
           source, 
           new String[] {
             "name", "AnyValue_._type",
             "kind", "empty"
           });			
        addAnnotation
          (basicIdentificationTypeEClass, 
           source, 
           new String[] {
             "name", "BasicIdentificationType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getBasicIdentificationType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getBasicIdentificationType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "http://www.opengis.net/ows"
           });			
        addAnnotation
          (capabilitiesBaseTypeEClass, 
           source, 
           new String[] {
             "name", "CapabilitiesBaseType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getCapabilitiesBaseType_ServiceIdentification(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceIdentification",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getCapabilitiesBaseType_ServiceProvider(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceProvider",
             "namespace", "http://www.opengis.net/ows"
           });			
        addAnnotation
          (getCapabilitiesBaseType_OperationsMetadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OperationsMetadata",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getCapabilitiesBaseType_UpdateSequence(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "updateSequence"
           });		
        addAnnotation
          (getCapabilitiesBaseType_Version(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "version"
           });			
        addAnnotation
          (coveragesTypeEClass, 
           source, 
           new String[] {
             "name", "CoveragesType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (dcpTypeEClass, 
           source, 
           new String[] {
             "name", "DCP_._type",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getDCPType_HTTP(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "HTTP",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (descriptionTypeEClass, 
           source, 
           new String[] {
             "name", "DescriptionType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getDescriptionType_Title(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Title",
             "namespace", "http://www.opengis.net/ows"
           });		
        addAnnotation
          (getDescriptionType_Abstract(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Abstract",
             "namespace", "http://www.opengis.net/ows"
           });		
        addAnnotation
          (getDescriptionType_Keywords(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Keywords",
             "namespace", "http://www.opengis.net/ows"
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
          (getDocumentRoot_AbstractReferenceBase(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AbstractReferenceBase",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_AccessConstraints(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AccessConstraints",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_AllowedValues(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AllowedValues",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_AnyValue(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AnyValue",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_AvailableCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AvailableCRS",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Coverage(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Coverage",
             "namespace", "##targetNamespace",
             "affiliation", "ReferenceGroup"
           });		
        addAnnotation
          (getDocumentRoot_ReferenceGroup(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ReferenceGroup",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Coverages(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Coverages",
             "namespace", "##targetNamespace",
             "affiliation", "Manifest"
           });		
        addAnnotation
          (getDocumentRoot_Manifest(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Manifest",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_DataType(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DataType",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_DCP(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DCP",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_DefaultValue(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DefaultValue",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_ExtendedCapabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ExtendedCapabilities",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Fees(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Fees",
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
          (getDocumentRoot_HTTP(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "HTTP",
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
          (getDocumentRoot_InterpolationMethods(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "InterpolationMethods",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Language(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Language",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_MaximumValue(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "MaximumValue",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Meaning(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Meaning",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_MinimumValue(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "MinimumValue",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_NoValues(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "NoValues",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Operation(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Operation",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_OperationsMetadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OperationsMetadata",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_OutputFormat(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OutputFormat",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Range(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Range",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Reference(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Reference",
             "namespace", "##targetNamespace",
             "affiliation", "AbstractReferenceBase"
           });			
        addAnnotation
          (getDocumentRoot_ReferenceSystem(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ReferenceSystem",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_ServiceIdentification(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceIdentification",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_ServiceReference(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceReference",
             "namespace", "##targetNamespace",
             "affiliation", "Reference"
           });			
        addAnnotation
          (getDocumentRoot_Spacing(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Spacing",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_SupportedCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "SupportedCRS",
             "namespace", "##targetNamespace",
             "affiliation", "AvailableCRS"
           });			
        addAnnotation
          (getDocumentRoot_UOM(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "UOM",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getDocumentRoot_Value(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Value",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_ValuesReference(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ValuesReference",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_RangeClosure(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "rangeClosure",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getDocumentRoot_Reference1(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "reference",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (domainMetadataTypeEClass, 
           source, 
           new String[] {
             "name", "DomainMetadataType",
             "kind", "simple"
           });			
        addAnnotation
          (getDomainMetadataType_Value(), 
           source, 
           new String[] {
             "name", ":0",
             "kind", "simple"
           });			
        addAnnotation
          (getDomainMetadataType_Reference(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "reference",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (domainTypeEClass, 
           source, 
           new String[] {
             "name", "DomainType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getDomainType_Name(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "name"
           });			
        addAnnotation
          (getCapabilitiesTypeEClass, 
           source, 
           new String[] {
             "name", "GetCapabilitiesType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getGetCapabilitiesType_AcceptVersions(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AcceptVersions",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGetCapabilitiesType_Sections(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Sections",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGetCapabilitiesType_AcceptFormats(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AcceptFormats",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getGetCapabilitiesType_UpdateSequence(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "updateSequence"
           });		
        addAnnotation
          (httpTypeEClass, 
           source, 
           new String[] {
             "name", "HTTP_._type",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getHTTPType_Group(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "group:0"
           });			
        addAnnotation
          (getHTTPType_Get(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Get",
             "namespace", "##targetNamespace",
             "group", "#group:0"
           });			
        addAnnotation
          (getHTTPType_Post(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Post",
             "namespace", "##targetNamespace",
             "group", "#group:0"
           });			
        addAnnotation
          (identificationTypeEClass, 
           source, 
           new String[] {
             "name", "IdentificationType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getIdentificationType_BoundingBox(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "BoundingBox",
             "namespace", "http://www.opengis.net/ows"
           });			
        addAnnotation
          (getIdentificationType_OutputFormat(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OutputFormat",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getIdentificationType_AvailableCRSGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "AvailableCRS:group",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getIdentificationType_AvailableCRS(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AvailableCRS",
             "namespace", "##targetNamespace",
             "group", "AvailableCRS:group"
           });			
        addAnnotation
          (interpolationMethodBaseTypeEClass, 
           source, 
           new String[] {
             "name", "InterpolationMethodBaseType",
             "kind", "simple"
           });		
        addAnnotation
          (getInterpolationMethodBaseType_Value(), 
           source, 
           new String[] {
             "name", ":0",
             "kind", "simple"
           });		
        addAnnotation
          (interpolationMethodBaseTypeBaseEDataType, 
           source, 
           new String[] {
             "name", "InterpolationMethodBaseType_._base",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#anySimpleType"
           });		
        addAnnotation
          (interpolationMethodsTypeEClass, 
           source, 
           new String[] {
             "name", "InterpolationMethods_._type",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getInterpolationMethodsType_DefaultMethod(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DefaultMethod",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getInterpolationMethodsType_OtherMethod(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "OtherMethod",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (interpolationMethodTypeEClass, 
           source, 
           new String[] {
             "name", "InterpolationMethodType",
             "kind", "simple"
           });			
        addAnnotation
          (getInterpolationMethodType_NullResistance(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "nullResistance"
           });			
        addAnnotation
          (manifestTypeEClass, 
           source, 
           new String[] {
             "name", "ManifestType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getManifestType_ReferenceGroupGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "ReferenceGroup:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getManifestType_ReferenceGroup(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ReferenceGroup",
             "namespace", "##targetNamespace",
             "group", "ReferenceGroup:group"
           });		
        addAnnotation
          (noValuesTypeEClass, 
           source, 
           new String[] {
             "name", "NoValues_._type",
             "kind", "empty"
           });		
        addAnnotation
          (operationsMetadataTypeEClass, 
           source, 
           new String[] {
             "name", "OperationsMetadata_._type",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getOperationsMetadataType_Operation(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Operation",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getOperationsMetadataType_Parameter(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Parameter",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getOperationsMetadataType_Constraint(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Constraint",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getOperationsMetadataType_ExtendedCapabilities(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ExtendedCapabilities",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (operationTypeEClass, 
           source, 
           new String[] {
             "name", "Operation_._type",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getOperationType_DCP(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DCP",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getOperationType_Parameter(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Parameter",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getOperationType_Constraint(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Constraint",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getOperationType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "http://www.opengis.net/ows"
           });			
        addAnnotation
          (getOperationType_Name(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "name"
           });		
        addAnnotation
          (rangeClosureTypeEEnum, 
           source, 
           new String[] {
             "name", "rangeClosure_._type"
           });						
        addAnnotation
          (rangeClosureTypeObjectEDataType, 
           source, 
           new String[] {
             "name", "rangeClosure_._type:Object",
             "baseType", "rangeClosure_._type"
           });			
        addAnnotation
          (rangeTypeEClass, 
           source, 
           new String[] {
             "name", "RangeType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getRangeType_MinimumValue(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "MinimumValue",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getRangeType_MaximumValue(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "MaximumValue",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getRangeType_Spacing(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Spacing",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getRangeType_RangeClosure(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "rangeClosure",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (referenceGroupTypeEClass, 
           source, 
           new String[] {
             "name", "ReferenceGroupType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getReferenceGroupType_AbstractReferenceBaseGroup(), 
           source, 
           new String[] {
             "kind", "group",
             "name", "AbstractReferenceBase:group",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getReferenceGroupType_AbstractReferenceBase(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AbstractReferenceBase",
             "namespace", "##targetNamespace",
             "group", "AbstractReferenceBase:group"
           });			
        addAnnotation
          (referenceTypeEClass, 
           source, 
           new String[] {
             "name", "ReferenceType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getReferenceType_Identifier(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Identifier",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (getReferenceType_Abstract(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Abstract",
             "namespace", "http://www.opengis.net/ows"
           });			
        addAnnotation
          (getReferenceType_Format(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Format",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getReferenceType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "http://www.opengis.net/ows"
           });			
        addAnnotation
          (requestMethodTypeEClass, 
           source, 
           new String[] {
             "name", "RequestMethodType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getRequestMethodType_Constraint(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Constraint",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (sectionsTypeEClass, 
           source, 
           new String[] {
             "name", "SectionsType",
             "kind", "elementOnly"
           });		
        addAnnotation
          (getSectionsType_Section(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Section",
             "namespace", "##targetNamespace"
           });		
        addAnnotation
          (serviceIdentificationTypeEClass, 
           source, 
           new String[] {
             "name", "ServiceIdentification_._type",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getServiceIdentificationType_ServiceType(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceType",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getServiceIdentificationType_ServiceTypeVersion(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ServiceTypeVersion",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getServiceIdentificationType_Profile(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Profile",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getServiceIdentificationType_Fees(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Fees",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getServiceIdentificationType_AccessConstraints(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AccessConstraints",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (serviceReferenceTypeEClass, 
           source, 
           new String[] {
             "name", "ServiceReferenceType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getServiceReferenceType_RequestMessage(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "RequestMessage",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getServiceReferenceType_RequestMessageReference(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "RequestMessageReference",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (serviceTypeEDataType, 
           source, 
           new String[] {
             "name", "ServiceType",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#string"
           });			
        addAnnotation
          (unNamedDomainTypeEClass, 
           source, 
           new String[] {
             "name", "UnNamedDomainType",
             "kind", "elementOnly"
           });			
        addAnnotation
          (getUnNamedDomainType_AllowedValues(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AllowedValues",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getUnNamedDomainType_AnyValue(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "AnyValue",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getUnNamedDomainType_NoValues(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "NoValues",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getUnNamedDomainType_ValuesReference(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ValuesReference",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getUnNamedDomainType_DefaultValue(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DefaultValue",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getUnNamedDomainType_Meaning(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Meaning",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getUnNamedDomainType_DataType(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "DataType",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getUnNamedDomainType_UOM(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "UOM",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getUnNamedDomainType_ReferenceSystem(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "ReferenceSystem",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (getUnNamedDomainType_Metadata(), 
           source, 
           new String[] {
             "kind", "element",
             "name", "Metadata",
             "namespace", "http://www.opengis.net/ows"
           });			
        addAnnotation
          (updateSequenceTypeEDataType, 
           source, 
           new String[] {
             "name", "UpdateSequenceType",
             "baseType", "http://www.eclipse.org/emf/2003/XMLType#string"
           });		
        addAnnotation
          (valuesReferenceTypeEClass, 
           source, 
           new String[] {
             "name", "ValuesReference_._type",
             "kind", "simple"
           });			
        addAnnotation
          (getValuesReferenceType_Value(), 
           source, 
           new String[] {
             "name", ":0",
             "kind", "simple"
           });			
        addAnnotation
          (getValuesReferenceType_Reference(), 
           source, 
           new String[] {
             "kind", "attribute",
             "name", "reference",
             "namespace", "##targetNamespace"
           });			
        addAnnotation
          (valueTypeEClass, 
           source, 
           new String[] {
             "name", "ValueType",
             "kind", "simple"
           });		
        addAnnotation
          (getValueType_Value(), 
           source, 
           new String[] {
             "name", ":0",
             "kind", "simple"
           });
    }

} //owcsPackageImpl
