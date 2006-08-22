/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.v1_1_0.util;

import java.util.List;

import net.opengis.ows.v1_0_0.CapabilitiesBaseType;

import net.opengis.wfs.v1_1_0.*;

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
 * @see net.opengis.wfs.v1_1_0.WFSPackage
 * @generated
 */
public class WFSSwitch {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static WFSPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WFSSwitch() {
		if (modelPackage == null) {
			modelPackage = WFSPackage.eINSTANCE;
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
			case WFSPackage.ACTION_TYPE: {
				ActionType actionType = (ActionType)theEObject;
				Object result = caseActionType(actionType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.BASE_REQUEST_TYPE: {
				BaseRequestType baseRequestType = (BaseRequestType)theEObject;
				Object result = caseBaseRequestType(baseRequestType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.DELETE_ELEMENT_TYPE: {
				DeleteElementType deleteElementType = (DeleteElementType)theEObject;
				Object result = caseDeleteElementType(deleteElementType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE: {
				DescribeFeatureTypeType describeFeatureTypeType = (DescribeFeatureTypeType)theEObject;
				Object result = caseDescribeFeatureTypeType(describeFeatureTypeType);
				if (result == null) result = caseBaseRequestType(describeFeatureTypeType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.DOCUMENT_ROOT: {
				DocumentRoot documentRoot = (DocumentRoot)theEObject;
				Object result = caseDocumentRoot(documentRoot);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.FEATURE_COLLECTION_TYPE: {
				FeatureCollectionType featureCollectionType = (FeatureCollectionType)theEObject;
				Object result = caseFeatureCollectionType(featureCollectionType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.FEATURES_LOCKED_TYPE: {
				FeaturesLockedType featuresLockedType = (FeaturesLockedType)theEObject;
				Object result = caseFeaturesLockedType(featuresLockedType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.FEATURES_NOT_LOCKED_TYPE: {
				FeaturesNotLockedType featuresNotLockedType = (FeaturesNotLockedType)theEObject;
				Object result = caseFeaturesNotLockedType(featuresNotLockedType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.FEATURE_TYPE_LIST_TYPE: {
				FeatureTypeListType featureTypeListType = (FeatureTypeListType)theEObject;
				Object result = caseFeatureTypeListType(featureTypeListType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.FEATURE_TYPE_TYPE: {
				FeatureTypeType featureTypeType = (FeatureTypeType)theEObject;
				Object result = caseFeatureTypeType(featureTypeType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.GET_CAPABILITIES_TYPE: {
				GetCapabilitiesType getCapabilitiesType = (GetCapabilitiesType)theEObject;
				Object result = caseGetCapabilitiesType(getCapabilitiesType);
				if (result == null) result = caseOWS_GetCapabilitiesType(getCapabilitiesType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.GET_FEATURE_TYPE: {
				GetFeatureType getFeatureType = (GetFeatureType)theEObject;
				Object result = caseGetFeatureType(getFeatureType);
				if (result == null) result = caseBaseRequestType(getFeatureType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.GET_FEATURE_WITH_LOCK_TYPE: {
				GetFeatureWithLockType getFeatureWithLockType = (GetFeatureWithLockType)theEObject;
				Object result = caseGetFeatureWithLockType(getFeatureWithLockType);
				if (result == null) result = caseBaseRequestType(getFeatureWithLockType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.GET_GML_OBJECT_TYPE: {
				GetGmlObjectType getGmlObjectType = (GetGmlObjectType)theEObject;
				Object result = caseGetGmlObjectType(getGmlObjectType);
				if (result == null) result = caseBaseRequestType(getGmlObjectType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.GML_OBJECT_TYPE_LIST_TYPE: {
				GMLObjectTypeListType gmlObjectTypeListType = (GMLObjectTypeListType)theEObject;
				Object result = caseGMLObjectTypeListType(gmlObjectTypeListType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.GML_OBJECT_TYPE_TYPE: {
				GMLObjectTypeType gmlObjectTypeType = (GMLObjectTypeType)theEObject;
				Object result = caseGMLObjectTypeType(gmlObjectTypeType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.INSERTED_FEATURE_TYPE: {
				InsertedFeatureType insertedFeatureType = (InsertedFeatureType)theEObject;
				Object result = caseInsertedFeatureType(insertedFeatureType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.INSERT_ELEMENT_TYPE: {
				InsertElementType insertElementType = (InsertElementType)theEObject;
				Object result = caseInsertElementType(insertElementType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.INSERT_RESULT_TYPE: {
				InsertResultType insertResultType = (InsertResultType)theEObject;
				Object result = caseInsertResultType(insertResultType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.LOCK_FEATURE_RESPONSE_TYPE: {
				LockFeatureResponseType lockFeatureResponseType = (LockFeatureResponseType)theEObject;
				Object result = caseLockFeatureResponseType(lockFeatureResponseType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.LOCK_FEATURE_TYPE: {
				LockFeatureType lockFeatureType = (LockFeatureType)theEObject;
				Object result = caseLockFeatureType(lockFeatureType);
				if (result == null) result = caseBaseRequestType(lockFeatureType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.LOCK_TYPE: {
				LockType lockType = (LockType)theEObject;
				Object result = caseLockType(lockType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.METADATA_URL_TYPE: {
				MetadataURLType metadataURLType = (MetadataURLType)theEObject;
				Object result = caseMetadataURLType(metadataURLType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.NATIVE_TYPE: {
				NativeType nativeType = (NativeType)theEObject;
				Object result = caseNativeType(nativeType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.NO_SRS_TYPE: {
				NoSRSType noSRSType = (NoSRSType)theEObject;
				Object result = caseNoSRSType(noSRSType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.OPERATIONS_TYPE: {
				OperationsType operationsType = (OperationsType)theEObject;
				Object result = caseOperationsType(operationsType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.OUTPUT_FORMAT_LIST_TYPE: {
				OutputFormatListType outputFormatListType = (OutputFormatListType)theEObject;
				Object result = caseOutputFormatListType(outputFormatListType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.PROPERTY_TYPE: {
				PropertyType propertyType = (PropertyType)theEObject;
				Object result = casePropertyType(propertyType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.QUERY_TYPE: {
				QueryType queryType = (QueryType)theEObject;
				Object result = caseQueryType(queryType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.TRANSACTION_RESPONSE_TYPE: {
				TransactionResponseType transactionResponseType = (TransactionResponseType)theEObject;
				Object result = caseTransactionResponseType(transactionResponseType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.TRANSACTION_RESULTS_TYPE: {
				TransactionResultsType transactionResultsType = (TransactionResultsType)theEObject;
				Object result = caseTransactionResultsType(transactionResultsType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.TRANSACTION_SUMMARY_TYPE: {
				TransactionSummaryType transactionSummaryType = (TransactionSummaryType)theEObject;
				Object result = caseTransactionSummaryType(transactionSummaryType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.TRANSACTION_TYPE: {
				TransactionType transactionType = (TransactionType)theEObject;
				Object result = caseTransactionType(transactionType);
				if (result == null) result = caseOWS_GetCapabilitiesType(transactionType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.UPDATE_ELEMENT_TYPE: {
				UpdateElementType updateElementType = (UpdateElementType)theEObject;
				Object result = caseUpdateElementType(updateElementType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WFSPackage.WFS_CAPABILITIES_TYPE: {
				WFSCapabilitiesType wfsCapabilitiesType = (WFSCapabilitiesType)theEObject;
				Object result = caseWFSCapabilitiesType(wfsCapabilitiesType);
				if (result == null) result = caseCapabilitiesBaseType(wfsCapabilitiesType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Action Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Action Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseActionType(ActionType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Base Request Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Base Request Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseBaseRequestType(BaseRequestType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Delete Element Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Delete Element Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseDeleteElementType(DeleteElementType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Describe Feature Type Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Describe Feature Type Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseDescribeFeatureTypeType(DescribeFeatureTypeType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Document Root</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Document Root</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseDocumentRoot(DocumentRoot object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Feature Collection Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Feature Collection Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseFeatureCollectionType(FeatureCollectionType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Features Locked Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Features Locked Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseFeaturesLockedType(FeaturesLockedType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Features Not Locked Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Features Not Locked Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseFeaturesNotLockedType(FeaturesNotLockedType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Feature Type List Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Feature Type List Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseFeatureTypeListType(FeatureTypeListType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Feature Type Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Feature Type Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseFeatureTypeType(FeatureTypeType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Get Capabilities Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Get Capabilities Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseGetCapabilitiesType(GetCapabilitiesType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Get Feature Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Get Feature Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseGetFeatureType(GetFeatureType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Get Feature With Lock Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Get Feature With Lock Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseGetFeatureWithLockType(GetFeatureWithLockType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Get Gml Object Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Get Gml Object Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseGetGmlObjectType(GetGmlObjectType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>GML Object Type List Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>GML Object Type List Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseGMLObjectTypeListType(GMLObjectTypeListType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>GML Object Type Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>GML Object Type Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseGMLObjectTypeType(GMLObjectTypeType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Inserted Feature Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Inserted Feature Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseInsertedFeatureType(InsertedFeatureType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Insert Element Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Insert Element Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseInsertElementType(InsertElementType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Insert Result Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Insert Result Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseInsertResultType(InsertResultType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Lock Feature Response Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Lock Feature Response Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseLockFeatureResponseType(LockFeatureResponseType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Lock Feature Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Lock Feature Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseLockFeatureType(LockFeatureType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Lock Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Lock Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseLockType(LockType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Metadata URL Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Metadata URL Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseMetadataURLType(MetadataURLType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Native Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Native Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseNativeType(NativeType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>No SRS Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>No SRS Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseNoSRSType(NoSRSType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Operations Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Operations Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseOperationsType(OperationsType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Output Format List Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Output Format List Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseOutputFormatListType(OutputFormatListType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Property Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Property Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object casePropertyType(PropertyType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Query Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Query Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseQueryType(QueryType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Transaction Response Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Transaction Response Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseTransactionResponseType(TransactionResponseType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Transaction Results Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Transaction Results Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseTransactionResultsType(TransactionResultsType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Transaction Summary Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Transaction Summary Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseTransactionSummaryType(TransactionSummaryType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Transaction Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Transaction Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseTransactionType(TransactionType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Update Element Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Update Element Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseUpdateElementType(UpdateElementType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Capabilities Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Capabilities Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseWFSCapabilitiesType(WFSCapabilitiesType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Get Capabilities Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Get Capabilities Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseOWS_GetCapabilitiesType(net.opengis.ows.v1_0_0.GetCapabilitiesType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Capabilities Base Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Capabilities Base Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseCapabilitiesBaseType(CapabilitiesBaseType object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public Object defaultCase(EObject object) {
		return null;
	}

} //WFSSwitch
