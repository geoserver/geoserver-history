/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows.impl;

import net.opengis.ows.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import java.util.List;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class OwsFactoryImpl extends EFactoryImpl implements OwsFactory {
    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OwsFactoryImpl() {
        super();
    }

    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static OwsFactory init() {
        try {
            OwsFactory theOwsFactory = (OwsFactory) EPackage.Registry.INSTANCE
                .getEFactory("http:///net/opengis/ows.ecore");

            if (theOwsFactory != null) {
                return theOwsFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }

        return new OwsFactoryImpl();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
        case OwsPackage.ACCEPT_FORMATS_TYPE:
            return createAcceptFormatsType();

        case OwsPackage.ACCEPT_VERSIONS_TYPE:
            return createAcceptVersionsType();

        case OwsPackage.ADDRESS_TYPE:
            return createAddressType();

        case OwsPackage.BOUNDING_BOX_TYPE:
            return createBoundingBoxType();

        case OwsPackage.CAPABILITIES_BASE_TYPE:
            return createCapabilitiesBaseType();

        case OwsPackage.CODE_TYPE:
            return createCodeType();

        case OwsPackage.CONTACT_TYPE:
            return createContactType();

        case OwsPackage.DCP_TYPE:
            return createDCPType();

        case OwsPackage.DESCRIPTION_TYPE:
            return createDescriptionType();

        case OwsPackage.DOCUMENT_ROOT:
            return createDocumentRoot();

        case OwsPackage.DOMAIN_TYPE:
            return createDomainType();

        case OwsPackage.EXCEPTION_REPORT_TYPE:
            return createExceptionReportType();

        case OwsPackage.EXCEPTION_TYPE:
            return createExceptionType();

        case OwsPackage.GET_CAPABILITIES_TYPE:
            return createGetCapabilitiesType();

        case OwsPackage.HTTP_TYPE:
            return createHTTPType();

        case OwsPackage.IDENTIFICATION_TYPE:
            return createIdentificationType();

        case OwsPackage.KEYWORDS_TYPE:
            return createKeywordsType();

        case OwsPackage.METADATA_TYPE:
            return createMetadataType();

        case OwsPackage.ONLINE_RESOURCE_TYPE:
            return createOnlineResourceType();

        case OwsPackage.OPERATION_TYPE:
            return createOperationType();

        case OwsPackage.OPERATIONS_METADATA_TYPE:
            return createOperationsMetadataType();

        case OwsPackage.REQUEST_METHOD_TYPE:
            return createRequestMethodType();

        case OwsPackage.RESPONSIBLE_PARTY_SUBSET_TYPE:
            return createResponsiblePartySubsetType();

        case OwsPackage.RESPONSIBLE_PARTY_TYPE:
            return createResponsiblePartyType();

        case OwsPackage.SECTIONS_TYPE:
            return createSectionsType();

        case OwsPackage.SERVICE_IDENTIFICATION_TYPE:
            return createServiceIdentificationType();

        case OwsPackage.SERVICE_PROVIDER_TYPE:
            return createServiceProviderType();

        case OwsPackage.TELEPHONE_TYPE:
            return createTelephoneType();

        case OwsPackage.WGS84_BOUNDING_BOX_TYPE:
            return createWGS84BoundingBoxType();

        default:
            throw new IllegalArgumentException("The class '" + eClass.getName()
                + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
        case OwsPackage.MIME_TYPE:
            return createMimeTypeFromString(eDataType, initialValue);

        case OwsPackage.VERSION_TYPE:
            return createVersionTypeFromString(eDataType, initialValue);

        case OwsPackage.POSITION_TYPE:
            return createPositionTypeFromString(eDataType, initialValue);

        case OwsPackage.UPDATE_SEQUENCE_TYPE:
            return createUpdateSequenceTypeFromString(eDataType, initialValue);

        default:
            throw new IllegalArgumentException("The datatype '"
                + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
        case OwsPackage.MIME_TYPE:
            return convertMimeTypeToString(eDataType, instanceValue);

        case OwsPackage.VERSION_TYPE:
            return convertVersionTypeToString(eDataType, instanceValue);

        case OwsPackage.POSITION_TYPE:
            return convertPositionTypeToString(eDataType, instanceValue);

        case OwsPackage.UPDATE_SEQUENCE_TYPE:
            return convertUpdateSequenceTypeToString(eDataType, instanceValue);

        default:
            throw new IllegalArgumentException("The datatype '"
                + eDataType.getName() + "' is not a valid classifier");
        }
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
    public AddressType createAddressType() {
        AddressTypeImpl addressType = new AddressTypeImpl();

        return addressType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BoundingBoxType createBoundingBoxType() {
        BoundingBoxTypeImpl boundingBoxType = new BoundingBoxTypeImpl();

        return boundingBoxType;
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
    public CodeType createCodeType() {
        CodeTypeImpl codeType = new CodeTypeImpl();

        return codeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ContactType createContactType() {
        ContactTypeImpl contactType = new ContactTypeImpl();

        return contactType;
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
    public DomainType createDomainType() {
        DomainTypeImpl domainType = new DomainTypeImpl();

        return domainType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExceptionReportType createExceptionReportType() {
        ExceptionReportTypeImpl exceptionReportType = new ExceptionReportTypeImpl();

        return exceptionReportType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExceptionType createExceptionType() {
        ExceptionTypeImpl exceptionType = new ExceptionTypeImpl();

        return exceptionType;
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
    public KeywordsType createKeywordsType() {
        KeywordsTypeImpl keywordsType = new KeywordsTypeImpl();

        return keywordsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MetadataType createMetadataType() {
        MetadataTypeImpl metadataType = new MetadataTypeImpl();

        return metadataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OnlineResourceType createOnlineResourceType() {
        OnlineResourceTypeImpl onlineResourceType = new OnlineResourceTypeImpl();

        return onlineResourceType;
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
    public OperationsMetadataType createOperationsMetadataType() {
        OperationsMetadataTypeImpl operationsMetadataType = new OperationsMetadataTypeImpl();

        return operationsMetadataType;
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
    public ResponsiblePartySubsetType createResponsiblePartySubsetType() {
        ResponsiblePartySubsetTypeImpl responsiblePartySubsetType = new ResponsiblePartySubsetTypeImpl();

        return responsiblePartySubsetType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResponsiblePartyType createResponsiblePartyType() {
        ResponsiblePartyTypeImpl responsiblePartyType = new ResponsiblePartyTypeImpl();

        return responsiblePartyType;
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
    public ServiceProviderType createServiceProviderType() {
        ServiceProviderTypeImpl serviceProviderType = new ServiceProviderTypeImpl();

        return serviceProviderType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TelephoneType createTelephoneType() {
        TelephoneTypeImpl telephoneType = new TelephoneTypeImpl();

        return telephoneType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WGS84BoundingBoxType createWGS84BoundingBoxType() {
        WGS84BoundingBoxTypeImpl wgs84BoundingBoxType = new WGS84BoundingBoxTypeImpl();

        return wgs84BoundingBoxType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createMimeTypeFromString(EDataType eDataType,
        String initialValue) {
        return (String) super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertMimeTypeToString(EDataType eDataType,
        Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createVersionTypeFromString(EDataType eDataType,
        String initialValue) {
        return (String) super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertVersionTypeToString(EDataType eDataType,
        Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List createPositionTypeFromString(EDataType eDataType,
        String initialValue) {
        return (List) super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertPositionTypeToString(EDataType eDataType,
        Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createUpdateSequenceTypeFromString(EDataType eDataType,
        String initialValue) {
        return (String) super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertUpdateSequenceTypeToString(EDataType eDataType,
        Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OwsPackage getOwsPackage() {
        return (OwsPackage) getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    public static OwsPackage getPackage() {
        return OwsPackage.eINSTANCE;
    }
} //OwsFactoryImpl
