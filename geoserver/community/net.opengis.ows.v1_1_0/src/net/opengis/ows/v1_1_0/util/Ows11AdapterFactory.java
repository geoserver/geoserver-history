/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows.v1_1_0.util;

import net.opengis.ows.v1_1_0.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see net.opengis.ows.v1_1_0.Ows11Package
 * @generated
 */
public class Ows11AdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static Ows11Package modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Ows11AdapterFactory() {
        if (modelPackage == null) {
            modelPackage = Ows11Package.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch the delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected Ows11Switch modelSwitch =
        new Ows11Switch() {
            public Object caseAbstractReferenceBaseType(AbstractReferenceBaseType object) {
                return createAbstractReferenceBaseTypeAdapter();
            }
            public Object caseAcceptFormatsType(AcceptFormatsType object) {
                return createAcceptFormatsTypeAdapter();
            }
            public Object caseAcceptVersionsType(AcceptVersionsType object) {
                return createAcceptVersionsTypeAdapter();
            }
            public Object caseAddressType(AddressType object) {
                return createAddressTypeAdapter();
            }
            public Object caseAllowedValuesType(AllowedValuesType object) {
                return createAllowedValuesTypeAdapter();
            }
            public Object caseAnyValueType(AnyValueType object) {
                return createAnyValueTypeAdapter();
            }
            public Object caseBasicIdentificationType(BasicIdentificationType object) {
                return createBasicIdentificationTypeAdapter();
            }
            public Object caseBoundingBoxType(BoundingBoxType object) {
                return createBoundingBoxTypeAdapter();
            }
            public Object caseCapabilitiesBaseType(CapabilitiesBaseType object) {
                return createCapabilitiesBaseTypeAdapter();
            }
            public Object caseCodeType(CodeType object) {
                return createCodeTypeAdapter();
            }
            public Object caseContactType(ContactType object) {
                return createContactTypeAdapter();
            }
            public Object caseContentsBaseType(ContentsBaseType object) {
                return createContentsBaseTypeAdapter();
            }
            public Object caseDatasetDescriptionSummaryBaseType(DatasetDescriptionSummaryBaseType object) {
                return createDatasetDescriptionSummaryBaseTypeAdapter();
            }
            public Object caseDCPType(DCPType object) {
                return createDCPTypeAdapter();
            }
            public Object caseDescriptionType(DescriptionType object) {
                return createDescriptionTypeAdapter();
            }
            public Object caseDocumentRoot(DocumentRoot object) {
                return createDocumentRootAdapter();
            }
            public Object caseDomainMetadataType(DomainMetadataType object) {
                return createDomainMetadataTypeAdapter();
            }
            public Object caseDomainType(DomainType object) {
                return createDomainTypeAdapter();
            }
            public Object caseExceptionReportType(ExceptionReportType object) {
                return createExceptionReportTypeAdapter();
            }
            public Object caseExceptionType(ExceptionType object) {
                return createExceptionTypeAdapter();
            }
            public Object caseGetCapabilitiesType(GetCapabilitiesType object) {
                return createGetCapabilitiesTypeAdapter();
            }
            public Object caseGetResourceByIdType(GetResourceByIdType object) {
                return createGetResourceByIdTypeAdapter();
            }
            public Object caseHTTPType(HTTPType object) {
                return createHTTPTypeAdapter();
            }
            public Object caseIdentificationType(IdentificationType object) {
                return createIdentificationTypeAdapter();
            }
            public Object caseKeywordsType(KeywordsType object) {
                return createKeywordsTypeAdapter();
            }
            public Object caseLanguageStringType(LanguageStringType object) {
                return createLanguageStringTypeAdapter();
            }
            public Object caseManifestType(ManifestType object) {
                return createManifestTypeAdapter();
            }
            public Object caseMetadataType(MetadataType object) {
                return createMetadataTypeAdapter();
            }
            public Object caseNoValuesType(NoValuesType object) {
                return createNoValuesTypeAdapter();
            }
            public Object caseOnlineResourceType(OnlineResourceType object) {
                return createOnlineResourceTypeAdapter();
            }
            public Object caseOperationsMetadataType(OperationsMetadataType object) {
                return createOperationsMetadataTypeAdapter();
            }
            public Object caseOperationType(OperationType object) {
                return createOperationTypeAdapter();
            }
            public Object caseRangeType(RangeType object) {
                return createRangeTypeAdapter();
            }
            public Object caseReferenceGroupType(ReferenceGroupType object) {
                return createReferenceGroupTypeAdapter();
            }
            public Object caseReferenceType(ReferenceType object) {
                return createReferenceTypeAdapter();
            }
            public Object caseRequestMethodType(RequestMethodType object) {
                return createRequestMethodTypeAdapter();
            }
            public Object caseResponsiblePartySubsetType(ResponsiblePartySubsetType object) {
                return createResponsiblePartySubsetTypeAdapter();
            }
            public Object caseResponsiblePartyType(ResponsiblePartyType object) {
                return createResponsiblePartyTypeAdapter();
            }
            public Object caseSectionsType(SectionsType object) {
                return createSectionsTypeAdapter();
            }
            public Object caseServiceIdentificationType(ServiceIdentificationType object) {
                return createServiceIdentificationTypeAdapter();
            }
            public Object caseServiceProviderType(ServiceProviderType object) {
                return createServiceProviderTypeAdapter();
            }
            public Object caseServiceReferenceType(ServiceReferenceType object) {
                return createServiceReferenceTypeAdapter();
            }
            public Object caseTelephoneType(TelephoneType object) {
                return createTelephoneTypeAdapter();
            }
            public Object caseUnNamedDomainType(UnNamedDomainType object) {
                return createUnNamedDomainTypeAdapter();
            }
            public Object caseValuesReferenceType(ValuesReferenceType object) {
                return createValuesReferenceTypeAdapter();
            }
            public Object caseValueType(ValueType object) {
                return createValueTypeAdapter();
            }
            public Object caseWGS84BoundingBoxType(WGS84BoundingBoxType object) {
                return createWGS84BoundingBoxTypeAdapter();
            }
            public Object defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    public Adapter createAdapter(Notifier target) {
        return (Adapter)modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.AbstractReferenceBaseType <em>Abstract Reference Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.AbstractReferenceBaseType
     * @generated
     */
    public Adapter createAbstractReferenceBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.AcceptFormatsType <em>Accept Formats Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.AcceptFormatsType
     * @generated
     */
    public Adapter createAcceptFormatsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.AcceptVersionsType <em>Accept Versions Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.AcceptVersionsType
     * @generated
     */
    public Adapter createAcceptVersionsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.AddressType <em>Address Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.AddressType
     * @generated
     */
    public Adapter createAddressTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.AllowedValuesType <em>Allowed Values Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.AllowedValuesType
     * @generated
     */
    public Adapter createAllowedValuesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.AnyValueType <em>Any Value Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.AnyValueType
     * @generated
     */
    public Adapter createAnyValueTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.BasicIdentificationType <em>Basic Identification Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.BasicIdentificationType
     * @generated
     */
    public Adapter createBasicIdentificationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.BoundingBoxType <em>Bounding Box Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.BoundingBoxType
     * @generated
     */
    public Adapter createBoundingBoxTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.CapabilitiesBaseType <em>Capabilities Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.CapabilitiesBaseType
     * @generated
     */
    public Adapter createCapabilitiesBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.CodeType <em>Code Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.CodeType
     * @generated
     */
    public Adapter createCodeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.ContactType <em>Contact Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.ContactType
     * @generated
     */
    public Adapter createContactTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.ContentsBaseType <em>Contents Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.ContentsBaseType
     * @generated
     */
    public Adapter createContentsBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.DatasetDescriptionSummaryBaseType <em>Dataset Description Summary Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.DatasetDescriptionSummaryBaseType
     * @generated
     */
    public Adapter createDatasetDescriptionSummaryBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.DCPType <em>DCP Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.DCPType
     * @generated
     */
    public Adapter createDCPTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.DescriptionType <em>Description Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.DescriptionType
     * @generated
     */
    public Adapter createDescriptionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.DocumentRoot
     * @generated
     */
    public Adapter createDocumentRootAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.DomainMetadataType <em>Domain Metadata Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.DomainMetadataType
     * @generated
     */
    public Adapter createDomainMetadataTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.DomainType <em>Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.DomainType
     * @generated
     */
    public Adapter createDomainTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.ExceptionReportType <em>Exception Report Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.ExceptionReportType
     * @generated
     */
    public Adapter createExceptionReportTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.ExceptionType <em>Exception Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.ExceptionType
     * @generated
     */
    public Adapter createExceptionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.GetCapabilitiesType
     * @generated
     */
    public Adapter createGetCapabilitiesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.GetResourceByIdType <em>Get Resource By Id Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.GetResourceByIdType
     * @generated
     */
    public Adapter createGetResourceByIdTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.HTTPType <em>HTTP Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.HTTPType
     * @generated
     */
    public Adapter createHTTPTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.IdentificationType <em>Identification Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.IdentificationType
     * @generated
     */
    public Adapter createIdentificationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.KeywordsType <em>Keywords Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.KeywordsType
     * @generated
     */
    public Adapter createKeywordsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.LanguageStringType <em>Language String Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.LanguageStringType
     * @generated
     */
    public Adapter createLanguageStringTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.ManifestType <em>Manifest Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.ManifestType
     * @generated
     */
    public Adapter createManifestTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.MetadataType <em>Metadata Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.MetadataType
     * @generated
     */
    public Adapter createMetadataTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.NoValuesType <em>No Values Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.NoValuesType
     * @generated
     */
    public Adapter createNoValuesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.OnlineResourceType <em>Online Resource Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.OnlineResourceType
     * @generated
     */
    public Adapter createOnlineResourceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.OperationsMetadataType <em>Operations Metadata Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.OperationsMetadataType
     * @generated
     */
    public Adapter createOperationsMetadataTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.OperationType <em>Operation Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.OperationType
     * @generated
     */
    public Adapter createOperationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.RangeType <em>Range Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.RangeType
     * @generated
     */
    public Adapter createRangeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.ReferenceGroupType <em>Reference Group Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.ReferenceGroupType
     * @generated
     */
    public Adapter createReferenceGroupTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.ReferenceType <em>Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.ReferenceType
     * @generated
     */
    public Adapter createReferenceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.RequestMethodType <em>Request Method Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.RequestMethodType
     * @generated
     */
    public Adapter createRequestMethodTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.ResponsiblePartySubsetType <em>Responsible Party Subset Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.ResponsiblePartySubsetType
     * @generated
     */
    public Adapter createResponsiblePartySubsetTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.ResponsiblePartyType <em>Responsible Party Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.ResponsiblePartyType
     * @generated
     */
    public Adapter createResponsiblePartyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.SectionsType <em>Sections Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.SectionsType
     * @generated
     */
    public Adapter createSectionsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.ServiceIdentificationType <em>Service Identification Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.ServiceIdentificationType
     * @generated
     */
    public Adapter createServiceIdentificationTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.ServiceProviderType <em>Service Provider Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.ServiceProviderType
     * @generated
     */
    public Adapter createServiceProviderTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.ServiceReferenceType <em>Service Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.ServiceReferenceType
     * @generated
     */
    public Adapter createServiceReferenceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.TelephoneType <em>Telephone Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.TelephoneType
     * @generated
     */
    public Adapter createTelephoneTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.UnNamedDomainType <em>Un Named Domain Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.UnNamedDomainType
     * @generated
     */
    public Adapter createUnNamedDomainTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.ValuesReferenceType <em>Values Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.ValuesReferenceType
     * @generated
     */
    public Adapter createValuesReferenceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.ValueType <em>Value Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.ValueType
     * @generated
     */
    public Adapter createValueTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows.v1_1_0.WGS84BoundingBoxType <em>WGS84 Bounding Box Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows.v1_1_0.WGS84BoundingBoxType
     * @generated
     */
    public Adapter createWGS84BoundingBoxTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //Ows11AdapterFactory
