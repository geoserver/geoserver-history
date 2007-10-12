/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.impl;

import net.opengis.wcs.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.emf.ecore.util.Diagnostician;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class wcsFactoryImpl extends EFactoryImpl implements wcsFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static wcsFactory init() {
        try {
            wcsFactory thewcsFactory = (wcsFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.opengis.net/wcs/1.1"); 
            if (thewcsFactory != null) {
                return thewcsFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new wcsFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public wcsFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case wcsPackage.AVAILABLE_KEYS_TYPE: return createAvailableKeysType();
            case wcsPackage.AXIS_SUBSET_TYPE: return createAxisSubsetType();
            case wcsPackage.AXIS_TYPE: return createAxisType();
            case wcsPackage.CAPABILITIES_TYPE: return createCapabilitiesType();
            case wcsPackage.CONTENTS_TYPE: return createContentsType();
            case wcsPackage.COVERAGE_DESCRIPTIONS_TYPE: return createCoverageDescriptionsType();
            case wcsPackage.COVERAGE_DESCRIPTION_TYPE: return createCoverageDescriptionType();
            case wcsPackage.COVERAGE_DOMAIN_TYPE: return createCoverageDomainType();
            case wcsPackage.COVERAGE_SUMMARY_TYPE: return createCoverageSummaryType();
            case wcsPackage.DESCRIBE_COVERAGE_TYPE: return createDescribeCoverageType();
            case wcsPackage.DOCUMENT_ROOT: return createDocumentRoot();
            case wcsPackage.DOMAIN_SUBSET_TYPE: return createDomainSubsetType();
            case wcsPackage.FIELD_SUBSET_TYPE: return createFieldSubsetType();
            case wcsPackage.FIELD_TYPE: return createFieldType();
            case wcsPackage.GET_CAPABILITIES_TYPE: return createGetCapabilitiesType();
            case wcsPackage.GET_COVERAGE_TYPE: return createGetCoverageType();
            case wcsPackage.GRID_CRS_TYPE: return createGridCrsType();
            case wcsPackage.IMAGE_CRS_REF_TYPE: return createImageCRSRefType();
            case wcsPackage.OUTPUT_TYPE: return createOutputType();
            case wcsPackage.RANGE_SUBSET_TYPE: return createRangeSubsetType();
            case wcsPackage.RANGE_TYPE: return createRangeType();
            case wcsPackage.REQUEST_BASE_TYPE: return createRequestBaseType();
            case wcsPackage.SPATIAL_DOMAIN_TYPE: return createSpatialDomainType();
            case wcsPackage.TIME_PERIOD_TYPE: return createTimePeriodType();
            case wcsPackage.TIME_SEQUENCE_TYPE: return createTimeSequenceType();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
            case wcsPackage.IDENTIFIER_TYPE:
                return createIdentifierTypeFromString(eDataType, initialValue);
            case wcsPackage.TIME_DURATION_TYPE:
                return createTimeDurationTypeFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
            case wcsPackage.IDENTIFIER_TYPE:
                return convertIdentifierTypeToString(eDataType, instanceValue);
            case wcsPackage.TIME_DURATION_TYPE:
                return convertTimeDurationTypeToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AvailableKeysType createAvailableKeysType() {
        AvailableKeysTypeImpl availableKeysType = new AvailableKeysTypeImpl();
        return availableKeysType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AxisSubsetType createAxisSubsetType() {
        AxisSubsetTypeImpl axisSubsetType = new AxisSubsetTypeImpl();
        return axisSubsetType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AxisType createAxisType() {
        AxisTypeImpl axisType = new AxisTypeImpl();
        return axisType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CapabilitiesType createCapabilitiesType() {
        CapabilitiesTypeImpl capabilitiesType = new CapabilitiesTypeImpl();
        return capabilitiesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ContentsType createContentsType() {
        ContentsTypeImpl contentsType = new ContentsTypeImpl();
        return contentsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageDescriptionsType createCoverageDescriptionsType() {
        CoverageDescriptionsTypeImpl coverageDescriptionsType = new CoverageDescriptionsTypeImpl();
        return coverageDescriptionsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageDescriptionType createCoverageDescriptionType() {
        CoverageDescriptionTypeImpl coverageDescriptionType = new CoverageDescriptionTypeImpl();
        return coverageDescriptionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageDomainType createCoverageDomainType() {
        CoverageDomainTypeImpl coverageDomainType = new CoverageDomainTypeImpl();
        return coverageDomainType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageSummaryType createCoverageSummaryType() {
        CoverageSummaryTypeImpl coverageSummaryType = new CoverageSummaryTypeImpl();
        return coverageSummaryType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DescribeCoverageType createDescribeCoverageType() {
        DescribeCoverageTypeImpl describeCoverageType = new DescribeCoverageTypeImpl();
        return describeCoverageType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DocumentRoot createDocumentRoot() {
        DocumentRootImpl documentRoot = new DocumentRootImpl();
        return documentRoot;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainSubsetType createDomainSubsetType() {
        DomainSubsetTypeImpl domainSubsetType = new DomainSubsetTypeImpl();
        return domainSubsetType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FieldSubsetType createFieldSubsetType() {
        FieldSubsetTypeImpl fieldSubsetType = new FieldSubsetTypeImpl();
        return fieldSubsetType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FieldType createFieldType() {
        FieldTypeImpl fieldType = new FieldTypeImpl();
        return fieldType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetCapabilitiesType createGetCapabilitiesType() {
        GetCapabilitiesTypeImpl getCapabilitiesType = new GetCapabilitiesTypeImpl();
        return getCapabilitiesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetCoverageType createGetCoverageType() {
        GetCoverageTypeImpl getCoverageType = new GetCoverageTypeImpl();
        return getCoverageType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GridCrsType createGridCrsType() {
        GridCrsTypeImpl gridCrsType = new GridCrsTypeImpl();
        return gridCrsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ImageCRSRefType createImageCRSRefType() {
        ImageCRSRefTypeImpl imageCRSRefType = new ImageCRSRefTypeImpl();
        return imageCRSRefType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OutputType createOutputType() {
        OutputTypeImpl outputType = new OutputTypeImpl();
        return outputType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeSubsetType createRangeSubsetType() {
        RangeSubsetTypeImpl rangeSubsetType = new RangeSubsetTypeImpl();
        return rangeSubsetType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeType createRangeType() {
        RangeTypeImpl rangeType = new RangeTypeImpl();
        return rangeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RequestBaseType createRequestBaseType() {
        RequestBaseTypeImpl requestBaseType = new RequestBaseTypeImpl();
        return requestBaseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SpatialDomainType createSpatialDomainType() {
        SpatialDomainTypeImpl spatialDomainType = new SpatialDomainTypeImpl();
        return spatialDomainType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimePeriodType createTimePeriodType() {
        TimePeriodTypeImpl timePeriodType = new TimePeriodTypeImpl();
        return timePeriodType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeSequenceType createTimeSequenceType() {
        TimeSequenceTypeImpl timeSequenceType = new TimeSequenceTypeImpl();
        return timeSequenceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createIdentifierTypeFromString(EDataType eDataType, String initialValue) {
        return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertIdentifierTypeToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object createTimeDurationTypeFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        Object result = null;
        RuntimeException exception = null;
        try {
            result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DURATION, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DECIMAL, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        if (result != null || exception == null) return result;
    
        throw exception;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertTimeDurationTypeToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        if (XMLTypePackage.Literals.DURATION.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DURATION, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (XMLTypePackage.Literals.DECIMAL.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DECIMAL, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public wcsPackage getwcsPackage() {
        return (wcsPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    public static wcsPackage getPackage() {
        return wcsPackage.eINSTANCE;
    }

} //wcsFactoryImpl
