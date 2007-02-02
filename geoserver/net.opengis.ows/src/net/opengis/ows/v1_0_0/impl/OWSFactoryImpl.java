/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows.v1_0_0.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import net.opengis.ows.v1_0_0.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class OWSFactoryImpl extends EFactoryImpl implements OWSFactory {
	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OWSFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case OWSPackage.ACCEPT_FORMATS_TYPE: return createAcceptFormatsType();
			case OWSPackage.ACCEPT_VERSIONS_TYPE: return createAcceptVersionsType();
			case OWSPackage.ADDRESS_TYPE: return createAddressType();
			case OWSPackage.BOUNDING_BOX_TYPE: return createBoundingBoxType();
			case OWSPackage.CAPABILITIES_BASE_TYPE: return createCapabilitiesBaseType();
			case OWSPackage.CODE_TYPE: return createCodeType();
			case OWSPackage.CONTACT_TYPE: return createContactType();
			case OWSPackage.DCP_TYPE: return createDCPType();
			case OWSPackage.DESCRIPTION_TYPE: return createDescriptionType();
			case OWSPackage.DOCUMENT_ROOT: return createDocumentRoot();
			case OWSPackage.DOMAIN_TYPE: return createDomainType();
			case OWSPackage.EXCEPTION_REPORT_TYPE: return createExceptionReportType();
			case OWSPackage.EXCEPTION_TYPE: return createExceptionType();
			case OWSPackage.GET_CAPABILITIES_TYPE: return createGetCapabilitiesType();
			case OWSPackage.HTTP_TYPE: return createHTTPType();
			case OWSPackage.IDENTIFICATION_TYPE: return createIdentificationType();
			case OWSPackage.KEYWORDS_TYPE: return createKeywordsType();
			case OWSPackage.METADATA_TYPE: return createMetadataType();
			case OWSPackage.ONLINE_RESOURCE_TYPE: return createOnlineResourceType();
			case OWSPackage.OPERATIONS_METADATA_TYPE: return createOperationsMetadataType();
			case OWSPackage.OPERATION_TYPE: return createOperationType();
			case OWSPackage.REQUEST_METHOD_TYPE: return createRequestMethodType();
			case OWSPackage.RESPONSIBLE_PARTY_SUBSET_TYPE: return createResponsiblePartySubsetType();
			case OWSPackage.RESPONSIBLE_PARTY_TYPE: return createResponsiblePartyType();
			case OWSPackage.SECTIONS_TYPE: return createSectionsType();
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE: return createServiceIdentificationType();
			case OWSPackage.SERVICE_PROVIDER_TYPE: return createServiceProviderType();
			case OWSPackage.TELEPHONE_TYPE: return createTelephoneType();
			case OWSPackage.WGS84_BOUNDING_BOX_TYPE: return createWGS84BoundingBoxType();
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
			case OWSPackage.MIME_TYPE:
				return createMimeTypeFromString(eDataType, initialValue);
			case OWSPackage.POSITION_TYPE:
				return createPositionTypeFromString(eDataType, initialValue);
			case OWSPackage.POSITION_TYPE2_D:
				return createPositionType2DFromString(eDataType, initialValue);
			case OWSPackage.SERVICE_TYPE:
				return createServiceTypeFromString(eDataType, initialValue);
			case OWSPackage.UPDATE_SEQUENCE_TYPE:
				return createUpdateSequenceTypeFromString(eDataType, initialValue);
			case OWSPackage.VERSION_TYPE:
				return createVersionTypeFromString(eDataType, initialValue);
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
			case OWSPackage.MIME_TYPE:
				return convertMimeTypeToString(eDataType, instanceValue);
			case OWSPackage.POSITION_TYPE:
				return convertPositionTypeToString(eDataType, instanceValue);
			case OWSPackage.POSITION_TYPE2_D:
				return convertPositionType2DToString(eDataType, instanceValue);
			case OWSPackage.SERVICE_TYPE:
				return convertServiceTypeToString(eDataType, instanceValue);
			case OWSPackage.UPDATE_SEQUENCE_TYPE:
				return convertUpdateSequenceTypeToString(eDataType, instanceValue);
			case OWSPackage.VERSION_TYPE:
				return convertVersionTypeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
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
		WGS84BoundingBoxTypeImpl wgS84BoundingBoxType = new WGS84BoundingBoxTypeImpl();
		return wgS84BoundingBoxType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String createMimeTypeFromString(EDataType eDataType, String initialValue) {
		return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.eINSTANCE.getString(), initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertMimeTypeToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.eINSTANCE.getString(), instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List createPositionTypeFromString(EDataType eDataType, String initialValue) {
		if (initialValue == null) return null;
		List result = new ArrayList();
		for (StringTokenizer stringTokenizer = new StringTokenizer(initialValue); stringTokenizer.hasMoreTokens(); ) {
			String item = stringTokenizer.nextToken();
			result.add(XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.eINSTANCE.getDouble(), item));
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPositionTypeToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) return null;
		List list = (List)instanceValue;
		if (list.isEmpty()) return "";
		StringBuffer result = new StringBuffer();
		for (Iterator i = list.iterator(); i.hasNext(); ) {
			result.append(XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.eINSTANCE.getDouble(), i.next()));
			result.append(' ');
		}
		return result.substring(0, result.length() - 1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List createPositionType2DFromString(EDataType eDataType, String initialValue) {
		return (List)OWSFactory.eINSTANCE.createFromString(OWSPackage.eINSTANCE.getPositionType(), initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPositionType2DToString(EDataType eDataType, Object instanceValue) {
		return OWSFactory.eINSTANCE.convertToString(OWSPackage.eINSTANCE.getPositionType(), instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String createServiceTypeFromString(EDataType eDataType, String initialValue) {
		return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.eINSTANCE.getString(), initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertServiceTypeToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.eINSTANCE.getString(), instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String createUpdateSequenceTypeFromString(EDataType eDataType, String initialValue) {
		return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.eINSTANCE.getString(), initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertUpdateSequenceTypeToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.eINSTANCE.getString(), instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String createVersionTypeFromString(EDataType eDataType, String initialValue) {
		return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.eINSTANCE.getString(), initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertVersionTypeToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.eINSTANCE.getString(), instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OWSPackage getOWSPackage() {
		return (OWSPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static OWSPackage getPackage() {
		return OWSPackage.eINSTANCE;
	}

} //OWSFactoryImpl
