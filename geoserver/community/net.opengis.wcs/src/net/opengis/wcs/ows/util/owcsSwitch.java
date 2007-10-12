/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.ows.util;

import java.util.List;

import net.opengis.wcs.ows.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see net.opengis.wcs.ows.owcsPackage
 * @generated
 */
public class owcsSwitch {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static owcsPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public owcsSwitch() {
        if (modelPackage == null) {
            modelPackage = owcsPackage.eINSTANCE;
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    public Object doSwitch(EObject theEObject) {
        return doSwitch(theEObject.eClass(), theEObject);
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected Object doSwitch(EClass theEClass, EObject theEObject) {
        if (theEClass.eContainer() == modelPackage) {
            return doSwitch(theEClass.getClassifierID(), theEObject);
        }
        else {
            List eSuperTypes = theEClass.getESuperTypes();
            return
                eSuperTypes.isEmpty() ?
                    defaultCase(theEObject) :
                    doSwitch((EClass)eSuperTypes.get(0), theEObject);
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected Object doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case owcsPackage.ABSTRACT_REFERENCE_BASE_TYPE: {
                AbstractReferenceBaseType abstractReferenceBaseType = (AbstractReferenceBaseType)theEObject;
                Object result = caseAbstractReferenceBaseType(abstractReferenceBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.ACCEPT_FORMATS_TYPE: {
                AcceptFormatsType acceptFormatsType = (AcceptFormatsType)theEObject;
                Object result = caseAcceptFormatsType(acceptFormatsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.ACCEPT_VERSIONS_TYPE: {
                AcceptVersionsType acceptVersionsType = (AcceptVersionsType)theEObject;
                Object result = caseAcceptVersionsType(acceptVersionsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.ALLOWED_VALUES_TYPE: {
                AllowedValuesType allowedValuesType = (AllowedValuesType)theEObject;
                Object result = caseAllowedValuesType(allowedValuesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.ANY_VALUE_TYPE: {
                AnyValueType anyValueType = (AnyValueType)theEObject;
                Object result = caseAnyValueType(anyValueType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.BASIC_IDENTIFICATION_TYPE: {
                BasicIdentificationType basicIdentificationType = (BasicIdentificationType)theEObject;
                Object result = caseBasicIdentificationType(basicIdentificationType);
                if (result == null) result = caseDescriptionType(basicIdentificationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.CAPABILITIES_BASE_TYPE: {
                CapabilitiesBaseType capabilitiesBaseType = (CapabilitiesBaseType)theEObject;
                Object result = caseCapabilitiesBaseType(capabilitiesBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.COVERAGES_TYPE: {
                CoveragesType coveragesType = (CoveragesType)theEObject;
                Object result = caseCoveragesType(coveragesType);
                if (result == null) result = caseManifestType(coveragesType);
                if (result == null) result = caseBasicIdentificationType(coveragesType);
                if (result == null) result = caseDescriptionType(coveragesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.DCP_TYPE: {
                DCPType dcpType = (DCPType)theEObject;
                Object result = caseDCPType(dcpType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.DESCRIPTION_TYPE: {
                DescriptionType descriptionType = (DescriptionType)theEObject;
                Object result = caseDescriptionType(descriptionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.DOCUMENT_ROOT: {
                DocumentRoot documentRoot = (DocumentRoot)theEObject;
                Object result = caseDocumentRoot(documentRoot);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.DOMAIN_METADATA_TYPE: {
                DomainMetadataType domainMetadataType = (DomainMetadataType)theEObject;
                Object result = caseDomainMetadataType(domainMetadataType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.DOMAIN_TYPE: {
                DomainType domainType = (DomainType)theEObject;
                Object result = caseDomainType(domainType);
                if (result == null) result = caseUnNamedDomainType(domainType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.GET_CAPABILITIES_TYPE: {
                GetCapabilitiesType getCapabilitiesType = (GetCapabilitiesType)theEObject;
                Object result = caseGetCapabilitiesType(getCapabilitiesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.HTTP_TYPE: {
                HTTPType httpType = (HTTPType)theEObject;
                Object result = caseHTTPType(httpType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.IDENTIFICATION_TYPE: {
                IdentificationType identificationType = (IdentificationType)theEObject;
                Object result = caseIdentificationType(identificationType);
                if (result == null) result = caseBasicIdentificationType(identificationType);
                if (result == null) result = caseDescriptionType(identificationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.INTERPOLATION_METHOD_BASE_TYPE: {
                InterpolationMethodBaseType interpolationMethodBaseType = (InterpolationMethodBaseType)theEObject;
                Object result = caseInterpolationMethodBaseType(interpolationMethodBaseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.INTERPOLATION_METHODS_TYPE: {
                InterpolationMethodsType interpolationMethodsType = (InterpolationMethodsType)theEObject;
                Object result = caseInterpolationMethodsType(interpolationMethodsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.INTERPOLATION_METHOD_TYPE: {
                InterpolationMethodType interpolationMethodType = (InterpolationMethodType)theEObject;
                Object result = caseInterpolationMethodType(interpolationMethodType);
                if (result == null) result = caseInterpolationMethodBaseType(interpolationMethodType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.MANIFEST_TYPE: {
                ManifestType manifestType = (ManifestType)theEObject;
                Object result = caseManifestType(manifestType);
                if (result == null) result = caseBasicIdentificationType(manifestType);
                if (result == null) result = caseDescriptionType(manifestType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.NO_VALUES_TYPE: {
                NoValuesType noValuesType = (NoValuesType)theEObject;
                Object result = caseNoValuesType(noValuesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.OPERATIONS_METADATA_TYPE: {
                OperationsMetadataType operationsMetadataType = (OperationsMetadataType)theEObject;
                Object result = caseOperationsMetadataType(operationsMetadataType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.OPERATION_TYPE: {
                OperationType operationType = (OperationType)theEObject;
                Object result = caseOperationType(operationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.RANGE_TYPE: {
                RangeType rangeType = (RangeType)theEObject;
                Object result = caseRangeType(rangeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.REFERENCE_GROUP_TYPE: {
                ReferenceGroupType referenceGroupType = (ReferenceGroupType)theEObject;
                Object result = caseReferenceGroupType(referenceGroupType);
                if (result == null) result = caseBasicIdentificationType(referenceGroupType);
                if (result == null) result = caseDescriptionType(referenceGroupType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.REFERENCE_TYPE: {
                ReferenceType referenceType = (ReferenceType)theEObject;
                Object result = caseReferenceType(referenceType);
                if (result == null) result = caseAbstractReferenceBaseType(referenceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.REQUEST_METHOD_TYPE: {
                RequestMethodType requestMethodType = (RequestMethodType)theEObject;
                Object result = caseRequestMethodType(requestMethodType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.SECTIONS_TYPE: {
                SectionsType sectionsType = (SectionsType)theEObject;
                Object result = caseSectionsType(sectionsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.SERVICE_IDENTIFICATION_TYPE: {
                ServiceIdentificationType serviceIdentificationType = (ServiceIdentificationType)theEObject;
                Object result = caseServiceIdentificationType(serviceIdentificationType);
                if (result == null) result = caseDescriptionType(serviceIdentificationType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.SERVICE_REFERENCE_TYPE: {
                ServiceReferenceType serviceReferenceType = (ServiceReferenceType)theEObject;
                Object result = caseServiceReferenceType(serviceReferenceType);
                if (result == null) result = caseReferenceType(serviceReferenceType);
                if (result == null) result = caseAbstractReferenceBaseType(serviceReferenceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.UN_NAMED_DOMAIN_TYPE: {
                UnNamedDomainType unNamedDomainType = (UnNamedDomainType)theEObject;
                Object result = caseUnNamedDomainType(unNamedDomainType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.VALUES_REFERENCE_TYPE: {
                ValuesReferenceType valuesReferenceType = (ValuesReferenceType)theEObject;
                Object result = caseValuesReferenceType(valuesReferenceType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case owcsPackage.VALUE_TYPE: {
                ValueType valueType = (ValueType)theEObject;
                Object result = caseValueType(valueType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Reference Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Reference Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAbstractReferenceBaseType(AbstractReferenceBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Accept Formats Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Accept Formats Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAcceptFormatsType(AcceptFormatsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Accept Versions Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Accept Versions Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAcceptVersionsType(AcceptVersionsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Allowed Values Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Allowed Values Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAllowedValuesType(AllowedValuesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Any Value Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Any Value Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAnyValueType(AnyValueType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Basic Identification Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Basic Identification Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseBasicIdentificationType(BasicIdentificationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Capabilities Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Capabilities Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCapabilitiesBaseType(CapabilitiesBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Coverages Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Coverages Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseCoveragesType(CoveragesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>DCP Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>DCP Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDCPType(DCPType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Description Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Description Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDescriptionType(DescriptionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Document Root</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Document Root</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDocumentRoot(DocumentRoot object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Domain Metadata Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Domain Metadata Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDomainMetadataType(DomainMetadataType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Domain Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Domain Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDomainType(DomainType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Get Capabilities Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Get Capabilities Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseGetCapabilitiesType(GetCapabilitiesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>HTTP Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>HTTP Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseHTTPType(HTTPType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Identification Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Identification Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseIdentificationType(IdentificationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Interpolation Method Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Interpolation Method Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseInterpolationMethodBaseType(InterpolationMethodBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Interpolation Methods Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Interpolation Methods Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseInterpolationMethodsType(InterpolationMethodsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Interpolation Method Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Interpolation Method Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseInterpolationMethodType(InterpolationMethodType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Manifest Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Manifest Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseManifestType(ManifestType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>No Values Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>No Values Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseNoValuesType(NoValuesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Operations Metadata Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Operations Metadata Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseOperationsMetadataType(OperationsMetadataType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Operation Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Operation Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseOperationType(OperationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Range Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Range Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseRangeType(RangeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Reference Group Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Reference Group Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseReferenceGroupType(ReferenceGroupType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Reference Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Reference Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseReferenceType(ReferenceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Request Method Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Request Method Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseRequestMethodType(RequestMethodType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Sections Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Sections Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSectionsType(SectionsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Service Identification Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Service Identification Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseServiceIdentificationType(ServiceIdentificationType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Service Reference Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Service Reference Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseServiceReferenceType(ServiceReferenceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Un Named Domain Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Un Named Domain Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseUnNamedDomainType(UnNamedDomainType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Values Reference Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Values Reference Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseValuesReferenceType(ValuesReferenceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Value Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Value Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseValueType(ValueType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    public Object defaultCase(EObject object) {
        return null;
    }

} //owcsSwitch
