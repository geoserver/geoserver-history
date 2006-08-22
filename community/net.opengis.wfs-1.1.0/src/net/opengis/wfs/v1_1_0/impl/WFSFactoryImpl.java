/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.v1_1_0.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import net.opengis.wfs.v1_1_0.*;

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
public class WFSFactoryImpl extends EFactoryImpl implements WFSFactory {
	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WFSFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case WFSPackage.ACTION_TYPE: return createActionType();
			case WFSPackage.DELETE_ELEMENT_TYPE: return createDeleteElementType();
			case WFSPackage.DESCRIBE_FEATURE_TYPE_TYPE: return createDescribeFeatureTypeType();
			case WFSPackage.DOCUMENT_ROOT: return createDocumentRoot();
			case WFSPackage.FEATURE_COLLECTION_TYPE: return createFeatureCollectionType();
			case WFSPackage.FEATURES_LOCKED_TYPE: return createFeaturesLockedType();
			case WFSPackage.FEATURES_NOT_LOCKED_TYPE: return createFeaturesNotLockedType();
			case WFSPackage.FEATURE_TYPE_LIST_TYPE: return createFeatureTypeListType();
			case WFSPackage.FEATURE_TYPE_TYPE: return createFeatureTypeType();
			case WFSPackage.GET_CAPABILITIES_TYPE: return createGetCapabilitiesType();
			case WFSPackage.GET_FEATURE_TYPE: return createGetFeatureType();
			case WFSPackage.GET_FEATURE_WITH_LOCK_TYPE: return createGetFeatureWithLockType();
			case WFSPackage.GET_GML_OBJECT_TYPE: return createGetGmlObjectType();
			case WFSPackage.GML_OBJECT_TYPE_LIST_TYPE: return createGMLObjectTypeListType();
			case WFSPackage.GML_OBJECT_TYPE_TYPE: return createGMLObjectTypeType();
			case WFSPackage.INSERTED_FEATURE_TYPE: return createInsertedFeatureType();
			case WFSPackage.INSERT_ELEMENT_TYPE: return createInsertElementType();
			case WFSPackage.INSERT_RESULT_TYPE: return createInsertResultType();
			case WFSPackage.LOCK_FEATURE_RESPONSE_TYPE: return createLockFeatureResponseType();
			case WFSPackage.LOCK_FEATURE_TYPE: return createLockFeatureType();
			case WFSPackage.LOCK_TYPE: return createLockType();
			case WFSPackage.METADATA_URL_TYPE: return createMetadataURLType();
			case WFSPackage.NATIVE_TYPE: return createNativeType();
			case WFSPackage.NO_SRS_TYPE: return createNoSRSType();
			case WFSPackage.OPERATIONS_TYPE: return createOperationsType();
			case WFSPackage.OUTPUT_FORMAT_LIST_TYPE: return createOutputFormatListType();
			case WFSPackage.PROPERTY_TYPE: return createPropertyType();
			case WFSPackage.QUERY_TYPE: return createQueryType();
			case WFSPackage.TRANSACTION_RESPONSE_TYPE: return createTransactionResponseType();
			case WFSPackage.TRANSACTION_RESULTS_TYPE: return createTransactionResultsType();
			case WFSPackage.TRANSACTION_SUMMARY_TYPE: return createTransactionSummaryType();
			case WFSPackage.TRANSACTION_TYPE: return createTransactionType();
			case WFSPackage.UPDATE_ELEMENT_TYPE: return createUpdateElementType();
			case WFSPackage.WFS_CAPABILITIES_TYPE: return createWFSCapabilitiesType();
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
			case WFSPackage.ALL_SOME_TYPE: {
				AllSomeType result = AllSomeType.get(initialValue);
				if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
				return result;
			}
			case WFSPackage.IDENTIFIER_GENERATION_OPTION_TYPE: {
				IdentifierGenerationOptionType result = IdentifierGenerationOptionType.get(initialValue);
				if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
				return result;
			}
			case WFSPackage.OPERATION_TYPE: {
				OperationType result = OperationType.get(initialValue);
				if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
				return result;
			}
			case WFSPackage.RESULT_TYPE_TYPE: {
				ResultTypeType result = ResultTypeType.get(initialValue);
				if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
				return result;
			}
			case WFSPackage.ALL_SOME_TYPE_OBJECT:
				return createAllSomeTypeObjectFromString(eDataType, initialValue);
			case WFSPackage.BASE_TYPE_NAME_LIST_TYPE:
				return createBaseTypeNameListTypeFromString(eDataType, initialValue);
			case WFSPackage.FORMAT_TYPE:
				return createFormatTypeFromString(eDataType, initialValue);
			case WFSPackage.IDENTIFIER_GENERATION_OPTION_TYPE_OBJECT:
				return createIdentifierGenerationOptionTypeObjectFromString(eDataType, initialValue);
			case WFSPackage.OPERATION_TYPE_OBJECT:
				return createOperationTypeObjectFromString(eDataType, initialValue);
			case WFSPackage.RESULT_TYPE_TYPE_OBJECT:
				return createResultTypeTypeObjectFromString(eDataType, initialValue);
			case WFSPackage.TYPE_NAME_LIST_TYPE:
				return createTypeNameListTypeFromString(eDataType, initialValue);
			case WFSPackage.TYPE_TYPE:
				return createTypeTypeFromString(eDataType, initialValue);
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
			case WFSPackage.ALL_SOME_TYPE:
				return instanceValue == null ? null : instanceValue.toString();
			case WFSPackage.IDENTIFIER_GENERATION_OPTION_TYPE:
				return instanceValue == null ? null : instanceValue.toString();
			case WFSPackage.OPERATION_TYPE:
				return instanceValue == null ? null : instanceValue.toString();
			case WFSPackage.RESULT_TYPE_TYPE:
				return instanceValue == null ? null : instanceValue.toString();
			case WFSPackage.ALL_SOME_TYPE_OBJECT:
				return convertAllSomeTypeObjectToString(eDataType, instanceValue);
			case WFSPackage.BASE_TYPE_NAME_LIST_TYPE:
				return convertBaseTypeNameListTypeToString(eDataType, instanceValue);
			case WFSPackage.FORMAT_TYPE:
				return convertFormatTypeToString(eDataType, instanceValue);
			case WFSPackage.IDENTIFIER_GENERATION_OPTION_TYPE_OBJECT:
				return convertIdentifierGenerationOptionTypeObjectToString(eDataType, instanceValue);
			case WFSPackage.OPERATION_TYPE_OBJECT:
				return convertOperationTypeObjectToString(eDataType, instanceValue);
			case WFSPackage.RESULT_TYPE_TYPE_OBJECT:
				return convertResultTypeTypeObjectToString(eDataType, instanceValue);
			case WFSPackage.TYPE_NAME_LIST_TYPE:
				return convertTypeNameListTypeToString(eDataType, instanceValue);
			case WFSPackage.TYPE_TYPE:
				return convertTypeTypeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ActionType createActionType() {
		ActionTypeImpl actionType = new ActionTypeImpl();
		return actionType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeleteElementType createDeleteElementType() {
		DeleteElementTypeImpl deleteElementType = new DeleteElementTypeImpl();
		return deleteElementType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DescribeFeatureTypeType createDescribeFeatureTypeType() {
		DescribeFeatureTypeTypeImpl describeFeatureTypeType = new DescribeFeatureTypeTypeImpl();
		return describeFeatureTypeType;
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
	public FeatureCollectionType createFeatureCollectionType() {
		FeatureCollectionTypeImpl featureCollectionType = new FeatureCollectionTypeImpl();
		return featureCollectionType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeaturesLockedType createFeaturesLockedType() {
		FeaturesLockedTypeImpl featuresLockedType = new FeaturesLockedTypeImpl();
		return featuresLockedType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeaturesNotLockedType createFeaturesNotLockedType() {
		FeaturesNotLockedTypeImpl featuresNotLockedType = new FeaturesNotLockedTypeImpl();
		return featuresNotLockedType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureTypeListType createFeatureTypeListType() {
		FeatureTypeListTypeImpl featureTypeListType = new FeatureTypeListTypeImpl();
		return featureTypeListType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureTypeType createFeatureTypeType() {
		FeatureTypeTypeImpl featureTypeType = new FeatureTypeTypeImpl();
		return featureTypeType;
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
	public GetFeatureType createGetFeatureType() {
		GetFeatureTypeImpl getFeatureType = new GetFeatureTypeImpl();
		return getFeatureType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GetFeatureWithLockType createGetFeatureWithLockType() {
		GetFeatureWithLockTypeImpl getFeatureWithLockType = new GetFeatureWithLockTypeImpl();
		return getFeatureWithLockType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GetGmlObjectType createGetGmlObjectType() {
		GetGmlObjectTypeImpl getGmlObjectType = new GetGmlObjectTypeImpl();
		return getGmlObjectType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GMLObjectTypeListType createGMLObjectTypeListType() {
		GMLObjectTypeListTypeImpl gmlObjectTypeListType = new GMLObjectTypeListTypeImpl();
		return gmlObjectTypeListType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GMLObjectTypeType createGMLObjectTypeType() {
		GMLObjectTypeTypeImpl gmlObjectTypeType = new GMLObjectTypeTypeImpl();
		return gmlObjectTypeType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InsertedFeatureType createInsertedFeatureType() {
		InsertedFeatureTypeImpl insertedFeatureType = new InsertedFeatureTypeImpl();
		return insertedFeatureType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InsertElementType createInsertElementType() {
		InsertElementTypeImpl insertElementType = new InsertElementTypeImpl();
		return insertElementType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InsertResultType createInsertResultType() {
		InsertResultTypeImpl insertResultType = new InsertResultTypeImpl();
		return insertResultType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LockFeatureResponseType createLockFeatureResponseType() {
		LockFeatureResponseTypeImpl lockFeatureResponseType = new LockFeatureResponseTypeImpl();
		return lockFeatureResponseType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LockFeatureType createLockFeatureType() {
		LockFeatureTypeImpl lockFeatureType = new LockFeatureTypeImpl();
		return lockFeatureType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LockType createLockType() {
		LockTypeImpl lockType = new LockTypeImpl();
		return lockType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MetadataURLType createMetadataURLType() {
		MetadataURLTypeImpl metadataURLType = new MetadataURLTypeImpl();
		return metadataURLType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NativeType createNativeType() {
		NativeTypeImpl nativeType = new NativeTypeImpl();
		return nativeType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NoSRSType createNoSRSType() {
		NoSRSTypeImpl noSRSType = new NoSRSTypeImpl();
		return noSRSType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationsType createOperationsType() {
		OperationsTypeImpl operationsType = new OperationsTypeImpl();
		return operationsType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OutputFormatListType createOutputFormatListType() {
		OutputFormatListTypeImpl outputFormatListType = new OutputFormatListTypeImpl();
		return outputFormatListType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PropertyType createPropertyType() {
		PropertyTypeImpl propertyType = new PropertyTypeImpl();
		return propertyType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QueryType createQueryType() {
		QueryTypeImpl queryType = new QueryTypeImpl();
		return queryType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransactionResponseType createTransactionResponseType() {
		TransactionResponseTypeImpl transactionResponseType = new TransactionResponseTypeImpl();
		return transactionResponseType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransactionResultsType createTransactionResultsType() {
		TransactionResultsTypeImpl transactionResultsType = new TransactionResultsTypeImpl();
		return transactionResultsType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransactionSummaryType createTransactionSummaryType() {
		TransactionSummaryTypeImpl transactionSummaryType = new TransactionSummaryTypeImpl();
		return transactionSummaryType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransactionType createTransactionType() {
		TransactionTypeImpl transactionType = new TransactionTypeImpl();
		return transactionType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UpdateElementType createUpdateElementType() {
		UpdateElementTypeImpl updateElementType = new UpdateElementTypeImpl();
		return updateElementType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WFSCapabilitiesType createWFSCapabilitiesType() {
		WFSCapabilitiesTypeImpl wfsCapabilitiesType = new WFSCapabilitiesTypeImpl();
		return wfsCapabilitiesType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AllSomeType createAllSomeTypeObjectFromString(EDataType eDataType, String initialValue) {
		return (AllSomeType)WFSFactory.eINSTANCE.createFromString(WFSPackage.eINSTANCE.getAllSomeType(), initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertAllSomeTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return WFSFactory.eINSTANCE.convertToString(WFSPackage.eINSTANCE.getAllSomeType(), instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List createBaseTypeNameListTypeFromString(EDataType eDataType, String initialValue) {
		if (initialValue == null) return null;
		List result = new ArrayList();
		for (StringTokenizer stringTokenizer = new StringTokenizer(initialValue); stringTokenizer.hasMoreTokens(); ) {
			String item = stringTokenizer.nextToken();
			result.add(XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.eINSTANCE.getQName(), item));
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertBaseTypeNameListTypeToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) return null;
		List list = (List)instanceValue;
		if (list.isEmpty()) return "";
		StringBuffer result = new StringBuffer();
		for (Iterator i = list.iterator(); i.hasNext(); ) {
			result.append(XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.eINSTANCE.getQName(), i.next()));
			result.append(' ');
		}
		return result.substring(0, result.length() - 1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String createFormatTypeFromString(EDataType eDataType, String initialValue) {
		return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.eINSTANCE.getNMTOKEN(), initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertFormatTypeToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.eINSTANCE.getNMTOKEN(), instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IdentifierGenerationOptionType createIdentifierGenerationOptionTypeObjectFromString(EDataType eDataType, String initialValue) {
		return (IdentifierGenerationOptionType)WFSFactory.eINSTANCE.createFromString(WFSPackage.eINSTANCE.getIdentifierGenerationOptionType(), initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertIdentifierGenerationOptionTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return WFSFactory.eINSTANCE.convertToString(WFSPackage.eINSTANCE.getIdentifierGenerationOptionType(), instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationType createOperationTypeObjectFromString(EDataType eDataType, String initialValue) {
		return (OperationType)WFSFactory.eINSTANCE.createFromString(WFSPackage.eINSTANCE.getOperationType(), initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertOperationTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return WFSFactory.eINSTANCE.convertToString(WFSPackage.eINSTANCE.getOperationType(), instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResultTypeType createResultTypeTypeObjectFromString(EDataType eDataType, String initialValue) {
		return (ResultTypeType)WFSFactory.eINSTANCE.createFromString(WFSPackage.eINSTANCE.getResultTypeType(), initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertResultTypeTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return WFSFactory.eINSTANCE.convertToString(WFSPackage.eINSTANCE.getResultTypeType(), instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List createTypeNameListTypeFromString(EDataType eDataType, String initialValue) {
		return (List)WFSFactory.eINSTANCE.createFromString(WFSPackage.eINSTANCE.getBaseTypeNameListType(), initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTypeNameListTypeToString(EDataType eDataType, Object instanceValue) {
		return WFSFactory.eINSTANCE.convertToString(WFSPackage.eINSTANCE.getBaseTypeNameListType(), instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String createTypeTypeFromString(EDataType eDataType, String initialValue) {
		return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.eINSTANCE.getNMTOKEN(), initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTypeTypeToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.eINSTANCE.getNMTOKEN(), instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WFSPackage getWFSPackage() {
		return (WFSPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static WFSPackage getPackage() {
		return WFSPackage.eINSTANCE;
	}

} //WFSFactoryImpl
