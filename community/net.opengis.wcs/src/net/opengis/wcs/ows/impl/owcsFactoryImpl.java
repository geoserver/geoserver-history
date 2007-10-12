/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.ows.impl;

import net.opengis.wcs.ows.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class owcsFactoryImpl extends EFactoryImpl implements owcsFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static owcsFactory init() {
        try {
            owcsFactory theowcsFactory = (owcsFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.opengis.net/wcs/1.1/ows"); 
            if (theowcsFactory != null) {
                return theowcsFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new owcsFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public owcsFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case owcsPackage.ABSTRACT_REFERENCE_BASE_TYPE: return createAbstractReferenceBaseType();
            case owcsPackage.ACCEPT_FORMATS_TYPE: return createAcceptFormatsType();
            case owcsPackage.ACCEPT_VERSIONS_TYPE: return createAcceptVersionsType();
            case owcsPackage.ALLOWED_VALUES_TYPE: return createAllowedValuesType();
            case owcsPackage.ANY_VALUE_TYPE: return createAnyValueType();
            case owcsPackage.BASIC_IDENTIFICATION_TYPE: return createBasicIdentificationType();
            case owcsPackage.CAPABILITIES_BASE_TYPE: return createCapabilitiesBaseType();
            case owcsPackage.COVERAGES_TYPE: return createCoveragesType();
            case owcsPackage.DCP_TYPE: return createDCPType();
            case owcsPackage.DESCRIPTION_TYPE: return createDescriptionType();
            case owcsPackage.DOCUMENT_ROOT: return createDocumentRoot();
            case owcsPackage.DOMAIN_METADATA_TYPE: return createDomainMetadataType();
            case owcsPackage.DOMAIN_TYPE: return createDomainType();
            case owcsPackage.GET_CAPABILITIES_TYPE: return createGetCapabilitiesType();
            case owcsPackage.HTTP_TYPE: return createHTTPType();
            case owcsPackage.IDENTIFICATION_TYPE: return createIdentificationType();
            case owcsPackage.INTERPOLATION_METHOD_BASE_TYPE: return createInterpolationMethodBaseType();
            case owcsPackage.INTERPOLATION_METHODS_TYPE: return createInterpolationMethodsType();
            case owcsPackage.INTERPOLATION_METHOD_TYPE: return createInterpolationMethodType();
            case owcsPackage.MANIFEST_TYPE: return createManifestType();
            case owcsPackage.NO_VALUES_TYPE: return createNoValuesType();
            case owcsPackage.OPERATIONS_METADATA_TYPE: return createOperationsMetadataType();
            case owcsPackage.OPERATION_TYPE: return createOperationType();
            case owcsPackage.RANGE_TYPE: return createRangeType();
            case owcsPackage.REFERENCE_GROUP_TYPE: return createReferenceGroupType();
            case owcsPackage.REFERENCE_TYPE: return createReferenceType();
            case owcsPackage.REQUEST_METHOD_TYPE: return createRequestMethodType();
            case owcsPackage.SECTIONS_TYPE: return createSectionsType();
            case owcsPackage.SERVICE_IDENTIFICATION_TYPE: return createServiceIdentificationType();
            case owcsPackage.SERVICE_REFERENCE_TYPE: return createServiceReferenceType();
            case owcsPackage.UN_NAMED_DOMAIN_TYPE: return createUnNamedDomainType();
            case owcsPackage.VALUES_REFERENCE_TYPE: return createValuesReferenceType();
            case owcsPackage.VALUE_TYPE: return createValueType();
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
            case owcsPackage.RANGE_CLOSURE_TYPE:
                return createRangeClosureTypeFromString(eDataType, initialValue);
            case owcsPackage.INTERPOLATION_METHOD_BASE_TYPE_BASE:
                return createInterpolationMethodBaseTypeBaseFromString(eDataType, initialValue);
            case owcsPackage.RANGE_CLOSURE_TYPE_OBJECT:
                return createRangeClosureTypeObjectFromString(eDataType, initialValue);
            case owcsPackage.SERVICE_TYPE:
                return createServiceTypeFromString(eDataType, initialValue);
            case owcsPackage.UPDATE_SEQUENCE_TYPE:
                return createUpdateSequenceTypeFromString(eDataType, initialValue);
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
            case owcsPackage.RANGE_CLOSURE_TYPE:
                return convertRangeClosureTypeToString(eDataType, instanceValue);
            case owcsPackage.INTERPOLATION_METHOD_BASE_TYPE_BASE:
                return convertInterpolationMethodBaseTypeBaseToString(eDataType, instanceValue);
            case owcsPackage.RANGE_CLOSURE_TYPE_OBJECT:
                return convertRangeClosureTypeObjectToString(eDataType, instanceValue);
            case owcsPackage.SERVICE_TYPE:
                return convertServiceTypeToString(eDataType, instanceValue);
            case owcsPackage.UPDATE_SEQUENCE_TYPE:
                return convertUpdateSequenceTypeToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractReferenceBaseType createAbstractReferenceBaseType() {
        AbstractReferenceBaseTypeImpl abstractReferenceBaseType = new AbstractReferenceBaseTypeImpl();
        return abstractReferenceBaseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AcceptFormatsType createAcceptFormatsType() {
        AcceptFormatsTypeImpl acceptFormatsType = new AcceptFormatsTypeImpl();
        return acceptFormatsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AcceptVersionsType createAcceptVersionsType() {
        AcceptVersionsTypeImpl acceptVersionsType = new AcceptVersionsTypeImpl();
        return acceptVersionsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllowedValuesType createAllowedValuesType() {
        AllowedValuesTypeImpl allowedValuesType = new AllowedValuesTypeImpl();
        return allowedValuesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AnyValueType createAnyValueType() {
        AnyValueTypeImpl anyValueType = new AnyValueTypeImpl();
        return anyValueType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BasicIdentificationType createBasicIdentificationType() {
        BasicIdentificationTypeImpl basicIdentificationType = new BasicIdentificationTypeImpl();
        return basicIdentificationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CapabilitiesBaseType createCapabilitiesBaseType() {
        CapabilitiesBaseTypeImpl capabilitiesBaseType = new CapabilitiesBaseTypeImpl();
        return capabilitiesBaseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoveragesType createCoveragesType() {
        CoveragesTypeImpl coveragesType = new CoveragesTypeImpl();
        return coveragesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DCPType createDCPType() {
        DCPTypeImpl dcpType = new DCPTypeImpl();
        return dcpType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DescriptionType createDescriptionType() {
        DescriptionTypeImpl descriptionType = new DescriptionTypeImpl();
        return descriptionType;
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
    public DomainMetadataType createDomainMetadataType() {
        DomainMetadataTypeImpl domainMetadataType = new DomainMetadataTypeImpl();
        return domainMetadataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainType createDomainType() {
        DomainTypeImpl domainType = new DomainTypeImpl();
        return domainType;
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
    public HTTPType createHTTPType() {
        HTTPTypeImpl httpType = new HTTPTypeImpl();
        return httpType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IdentificationType createIdentificationType() {
        IdentificationTypeImpl identificationType = new IdentificationTypeImpl();
        return identificationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InterpolationMethodBaseType createInterpolationMethodBaseType() {
        InterpolationMethodBaseTypeImpl interpolationMethodBaseType = new InterpolationMethodBaseTypeImpl();
        return interpolationMethodBaseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InterpolationMethodsType createInterpolationMethodsType() {
        InterpolationMethodsTypeImpl interpolationMethodsType = new InterpolationMethodsTypeImpl();
        return interpolationMethodsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InterpolationMethodType createInterpolationMethodType() {
        InterpolationMethodTypeImpl interpolationMethodType = new InterpolationMethodTypeImpl();
        return interpolationMethodType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ManifestType createManifestType() {
        ManifestTypeImpl manifestType = new ManifestTypeImpl();
        return manifestType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NoValuesType createNoValuesType() {
        NoValuesTypeImpl noValuesType = new NoValuesTypeImpl();
        return noValuesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationsMetadataType createOperationsMetadataType() {
        OperationsMetadataTypeImpl operationsMetadataType = new OperationsMetadataTypeImpl();
        return operationsMetadataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationType createOperationType() {
        OperationTypeImpl operationType = new OperationTypeImpl();
        return operationType;
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
    public ReferenceGroupType createReferenceGroupType() {
        ReferenceGroupTypeImpl referenceGroupType = new ReferenceGroupTypeImpl();
        return referenceGroupType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ReferenceType createReferenceType() {
        ReferenceTypeImpl referenceType = new ReferenceTypeImpl();
        return referenceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RequestMethodType createRequestMethodType() {
        RequestMethodTypeImpl requestMethodType = new RequestMethodTypeImpl();
        return requestMethodType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SectionsType createSectionsType() {
        SectionsTypeImpl sectionsType = new SectionsTypeImpl();
        return sectionsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceIdentificationType createServiceIdentificationType() {
        ServiceIdentificationTypeImpl serviceIdentificationType = new ServiceIdentificationTypeImpl();
        return serviceIdentificationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceReferenceType createServiceReferenceType() {
        ServiceReferenceTypeImpl serviceReferenceType = new ServiceReferenceTypeImpl();
        return serviceReferenceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UnNamedDomainType createUnNamedDomainType() {
        UnNamedDomainTypeImpl unNamedDomainType = new UnNamedDomainTypeImpl();
        return unNamedDomainType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValuesReferenceType createValuesReferenceType() {
        ValuesReferenceTypeImpl valuesReferenceType = new ValuesReferenceTypeImpl();
        return valuesReferenceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueType createValueType() {
        ValueTypeImpl valueType = new ValueTypeImpl();
        return valueType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeClosureType createRangeClosureTypeFromString(EDataType eDataType, String initialValue) {
        RangeClosureType result = RangeClosureType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertRangeClosureTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object createInterpolationMethodBaseTypeBaseFromString(EDataType eDataType, String initialValue) {
        return XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.ANY_SIMPLE_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertInterpolationMethodBaseTypeBaseToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.ANY_SIMPLE_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeClosureType createRangeClosureTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createRangeClosureTypeFromString(owcsPackage.Literals.RANGE_CLOSURE_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertRangeClosureTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertRangeClosureTypeToString(owcsPackage.Literals.RANGE_CLOSURE_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createServiceTypeFromString(EDataType eDataType, String initialValue) {
        return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertServiceTypeToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createUpdateSequenceTypeFromString(EDataType eDataType, String initialValue) {
        return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertUpdateSequenceTypeToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public owcsPackage getowcsPackage() {
        return (owcsPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    public static owcsPackage getPackage() {
        return owcsPackage.eINSTANCE;
    }

} //owcsFactoryImpl
