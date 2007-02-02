/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows.v1_0_0.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.opengis.ows.v1_0_0.*;

import org.eclipse.emf.common.util.DiagnosticChain;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.eclipse.emf.ecore.xml.type.util.XMLTypeUtil;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see net.opengis.ows.v1_0_0.OWSPackage
 * @generated
 */
public class OWSValidator extends EObjectValidator {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final OWSValidator INSTANCE = new OWSValidator();

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "net.opengis.ows.v1_0_0";

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

	/**
	 * The cached base package validator.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected XMLTypeValidator xmlTypeValidator;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OWSValidator() {
		super();
		xmlTypeValidator = XMLTypeValidator.INSTANCE;
	}

	/**
	 * Returns the package of this validator switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EPackage getEPackage() {
	  return OWSPackage.eINSTANCE;
	}

	/**
	 * Calls <code>validateXXX</code> for the corresonding classifier of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map context) {
		switch (classifierID) {
			case OWSPackage.ACCEPT_FORMATS_TYPE:
				return validateAcceptFormatsType((AcceptFormatsType)value, diagnostics, context);
			case OWSPackage.ACCEPT_VERSIONS_TYPE:
				return validateAcceptVersionsType((AcceptVersionsType)value, diagnostics, context);
			case OWSPackage.ADDRESS_TYPE:
				return validateAddressType((AddressType)value, diagnostics, context);
			case OWSPackage.BOUNDING_BOX_TYPE:
				return validateBoundingBoxType((BoundingBoxType)value, diagnostics, context);
			case OWSPackage.CAPABILITIES_BASE_TYPE:
				return validateCapabilitiesBaseType((CapabilitiesBaseType)value, diagnostics, context);
			case OWSPackage.CODE_TYPE:
				return validateCodeType((CodeType)value, diagnostics, context);
			case OWSPackage.CONTACT_TYPE:
				return validateContactType((ContactType)value, diagnostics, context);
			case OWSPackage.DCP_TYPE:
				return validateDCPType((DCPType)value, diagnostics, context);
			case OWSPackage.DESCRIPTION_TYPE:
				return validateDescriptionType((DescriptionType)value, diagnostics, context);
			case OWSPackage.DOCUMENT_ROOT:
				return validateDocumentRoot((DocumentRoot)value, diagnostics, context);
			case OWSPackage.DOMAIN_TYPE:
				return validateDomainType((DomainType)value, diagnostics, context);
			case OWSPackage.EXCEPTION_REPORT_TYPE:
				return validateExceptionReportType((ExceptionReportType)value, diagnostics, context);
			case OWSPackage.EXCEPTION_TYPE:
				return validateExceptionType((ExceptionType)value, diagnostics, context);
			case OWSPackage.GET_CAPABILITIES_TYPE:
				return validateGetCapabilitiesType((GetCapabilitiesType)value, diagnostics, context);
			case OWSPackage.HTTP_TYPE:
				return validateHTTPType((HTTPType)value, diagnostics, context);
			case OWSPackage.IDENTIFICATION_TYPE:
				return validateIdentificationType((IdentificationType)value, diagnostics, context);
			case OWSPackage.KEYWORDS_TYPE:
				return validateKeywordsType((KeywordsType)value, diagnostics, context);
			case OWSPackage.METADATA_TYPE:
				return validateMetadataType((MetadataType)value, diagnostics, context);
			case OWSPackage.ONLINE_RESOURCE_TYPE:
				return validateOnlineResourceType((OnlineResourceType)value, diagnostics, context);
			case OWSPackage.OPERATIONS_METADATA_TYPE:
				return validateOperationsMetadataType((OperationsMetadataType)value, diagnostics, context);
			case OWSPackage.OPERATION_TYPE:
				return validateOperationType((OperationType)value, diagnostics, context);
			case OWSPackage.REQUEST_METHOD_TYPE:
				return validateRequestMethodType((RequestMethodType)value, diagnostics, context);
			case OWSPackage.RESPONSIBLE_PARTY_SUBSET_TYPE:
				return validateResponsiblePartySubsetType((ResponsiblePartySubsetType)value, diagnostics, context);
			case OWSPackage.RESPONSIBLE_PARTY_TYPE:
				return validateResponsiblePartyType((ResponsiblePartyType)value, diagnostics, context);
			case OWSPackage.SECTIONS_TYPE:
				return validateSectionsType((SectionsType)value, diagnostics, context);
			case OWSPackage.SERVICE_IDENTIFICATION_TYPE:
				return validateServiceIdentificationType((ServiceIdentificationType)value, diagnostics, context);
			case OWSPackage.SERVICE_PROVIDER_TYPE:
				return validateServiceProviderType((ServiceProviderType)value, diagnostics, context);
			case OWSPackage.TELEPHONE_TYPE:
				return validateTelephoneType((TelephoneType)value, diagnostics, context);
			case OWSPackage.WGS84_BOUNDING_BOX_TYPE:
				return validateWGS84BoundingBoxType((WGS84BoundingBoxType)value, diagnostics, context);
			case OWSPackage.MIME_TYPE:
				return validateMimeType((String)value, diagnostics, context);
			case OWSPackage.POSITION_TYPE:
				return validatePositionType((List)value, diagnostics, context);
			case OWSPackage.POSITION_TYPE2_D:
				return validatePositionType2D((List)value, diagnostics, context);
			case OWSPackage.SERVICE_TYPE:
				return validateServiceType((String)value, diagnostics, context);
			case OWSPackage.UPDATE_SEQUENCE_TYPE:
				return validateUpdateSequenceType((String)value, diagnostics, context);
			case OWSPackage.VERSION_TYPE:
				return validateVersionType((String)value, diagnostics, context);
			default: 
				return true;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAcceptFormatsType(AcceptFormatsType acceptFormatsType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(acceptFormatsType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAcceptVersionsType(AcceptVersionsType acceptVersionsType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(acceptVersionsType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAddressType(AddressType addressType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(addressType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateBoundingBoxType(BoundingBoxType boundingBoxType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(boundingBoxType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateCapabilitiesBaseType(CapabilitiesBaseType capabilitiesBaseType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(capabilitiesBaseType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateCodeType(CodeType codeType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(codeType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateContactType(ContactType contactType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(contactType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDCPType(DCPType dcpType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(dcpType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDescriptionType(DescriptionType descriptionType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(descriptionType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDocumentRoot(DocumentRoot documentRoot, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(documentRoot, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDomainType(DomainType domainType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(domainType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateExceptionReportType(ExceptionReportType exceptionReportType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(exceptionReportType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateExceptionType(ExceptionType exceptionType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(exceptionType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateGetCapabilitiesType(GetCapabilitiesType getCapabilitiesType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(getCapabilitiesType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateHTTPType(HTTPType httpType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(httpType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIdentificationType(IdentificationType identificationType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(identificationType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateKeywordsType(KeywordsType keywordsType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(keywordsType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateMetadataType(MetadataType metadataType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(metadataType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateOnlineResourceType(OnlineResourceType onlineResourceType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(onlineResourceType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateOperationsMetadataType(OperationsMetadataType operationsMetadataType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(operationsMetadataType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateOperationType(OperationType operationType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(operationType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateRequestMethodType(RequestMethodType requestMethodType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(requestMethodType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateResponsiblePartySubsetType(ResponsiblePartySubsetType responsiblePartySubsetType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(responsiblePartySubsetType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateResponsiblePartyType(ResponsiblePartyType responsiblePartyType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(responsiblePartyType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSectionsType(SectionsType sectionsType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(sectionsType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateServiceIdentificationType(ServiceIdentificationType serviceIdentificationType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(serviceIdentificationType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateServiceProviderType(ServiceProviderType serviceProviderType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(serviceProviderType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTelephoneType(TelephoneType telephoneType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(telephoneType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateWGS84BoundingBoxType(WGS84BoundingBoxType wgS84BoundingBoxType, DiagnosticChain diagnostics, Map context) {
		return validate_EveryDefaultConstraint(wgS84BoundingBoxType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateMimeType(String mimeType, DiagnosticChain diagnostics, Map context) {
		boolean result = validateMimeType_Pattern(mimeType, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @see #validateMimeType_Pattern
	 */
	public static final  PatternMatcher [][] MIME_TYPE__PATTERN__VALUES =
		new PatternMatcher [][] {
			new PatternMatcher [] {
				XMLTypeUtil.createPatternMatcher("(application|audio|image|text|video|message|multipart|model)/.+(;\\s*.+=.+)*")
			}
		};

	/**
	 * Validates the Pattern constraint of '<em>Mime Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateMimeType_Pattern(String mimeType, DiagnosticChain diagnostics, Map context) {
		return validatePattern(OWSPackage.eINSTANCE.getMimeType(), mimeType, MIME_TYPE__PATTERN__VALUES, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePositionType(List positionType, DiagnosticChain diagnostics, Map context) {
		boolean result = validatePositionType_ItemType(positionType, diagnostics, context);
		return result;
	}

	/**
	 * Validates the ItemType constraint of '<em>Position Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePositionType_ItemType(List positionType, DiagnosticChain diagnostics, Map context) {
		boolean result = true;
		for (Iterator i = positionType.iterator(); i.hasNext() && (result || diagnostics != null); ) {
			Object item = i.next();
			if (XMLTypePackage.eINSTANCE.getDouble().isInstance(item)) {
				result &= xmlTypeValidator.validateDouble(((Double)item).doubleValue(), diagnostics, context);
			}
			else {
				result = false;
				reportDataValueTypeViolation(XMLTypePackage.eINSTANCE.getDouble(), item, diagnostics, context);
			}
		}
	  return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePositionType2D(List positionType2D, DiagnosticChain diagnostics, Map context) {
		boolean result = validatePositionType_ItemType(positionType2D, diagnostics, context);
		if (result || diagnostics != null) result &= validatePositionType2D_MinLength(positionType2D, diagnostics, context);
		if (result || diagnostics != null) result &= validatePositionType2D_MaxLength(positionType2D, diagnostics, context);
		return result;
	}

	/**
	 * Validates the MinLength constraint of '<em>Position Type2 D</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePositionType2D_MinLength(List positionType2D, DiagnosticChain diagnostics, Map context) {
		int length = positionType2D.size();  
		boolean result = length >= 2;
		if (!result && diagnostics != null) 
			reportMinLengthViolation(OWSPackage.eINSTANCE.getPositionType2D(), positionType2D, length, 2, diagnostics, context);
		return result;
	}

	/**
	 * Validates the MaxLength constraint of '<em>Position Type2 D</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePositionType2D_MaxLength(List positionType2D, DiagnosticChain diagnostics, Map context) {
		int length = positionType2D.size();  
		boolean result = length <= 2;
		if (!result && diagnostics != null) 
			reportMaxLengthViolation(OWSPackage.eINSTANCE.getPositionType2D(), positionType2D, length, 2, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateServiceType(String serviceType, DiagnosticChain diagnostics, Map context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateUpdateSequenceType(String updateSequenceType, DiagnosticChain diagnostics, Map context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateVersionType(String versionType, DiagnosticChain diagnostics, Map context) {
		return true;
	}

} //OWSValidator
