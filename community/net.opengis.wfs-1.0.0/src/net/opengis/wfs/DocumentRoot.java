/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getDescribeFeatureType <em>Describe Feature Type</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getFeatureCollection <em>Feature Collection</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getGetFeature <em>Get Feature</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getQuery <em>Query</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getDelete <em>Delete</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getFailed <em>Failed</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getGetFeatureWithLock <em>Get Feature With Lock</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getInsert <em>Insert</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getLockFeature <em>Lock Feature</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getLockId <em>Lock Id</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getNative <em>Native</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getPartial <em>Partial</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getProperty <em>Property</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getSuccess <em>Success</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getTransaction <em>Transaction</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getUpdate <em>Update</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getWfsLockFeatureResponse <em>Wfs Lock Feature Response</em>}</li>
 *   <li>{@link net.opengis.wfs.DocumentRoot#getWfsTransactionResponse <em>Wfs Transaction Response</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WFSPackage#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject{
	/**
	 * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mixed</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mixed</em>' attribute list.
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_Mixed()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='elementWildcard' name=':mixed'"
	 * @generated
	 */
	FeatureMap getMixed();

	/**
	 * Returns the value of the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>XMLNS Prefix Map</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>XMLNS Prefix Map</em>' map.
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_XMLNSPrefixMap()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
	 *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
	 * @generated
	 */
	EMap getXMLNSPrefixMap();

	/**
	 * Returns the value of the '<em><b>XSI Schema Location</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>XSI Schema Location</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>XSI Schema Location</em>' map.
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_XSISchemaLocation()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
	 *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
	 * @generated
	 */
	EMap getXSISchemaLocation();

	/**
	 * Returns the value of the '<em><b>Describe Feature Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *             The DescribeFeatureType element is used to request that a Web
	 *             Feature Service generate a document describing one or more
	 *             feature types.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Describe Feature Type</em>' containment reference.
	 * @see #setDescribeFeatureType(DescribeFeatureTypeType)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_DescribeFeatureType()
	 * @model containment="true" resolveProxies="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='DescribeFeatureType' namespace='##targetNamespace'"
	 * @generated
	 */
	DescribeFeatureTypeType getDescribeFeatureType();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getDescribeFeatureType <em>Describe Feature Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Describe Feature Type</em>' containment reference.
	 * @see #getDescribeFeatureType()
	 * @generated
	 */
	void setDescribeFeatureType(DescribeFeatureTypeType value);

	/**
	 * Returns the value of the '<em><b>Feature Collection</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *             This element is a container for the response to a GetFeature
	 *             or GetFeatureWithLock (WFS-transaction.xsd) request.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Feature Collection</em>' containment reference.
	 * @see #setFeatureCollection(FeatureCollectionType)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_FeatureCollection()
	 * @model containment="true" resolveProxies="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='FeatureCollection' namespace='##targetNamespace' affiliation='http://www.opengis.net/gml#_FeatureCollection'"
	 * @generated
	 */
	FeatureCollectionType getFeatureCollection();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getFeatureCollection <em>Feature Collection</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Feature Collection</em>' containment reference.
	 * @see #getFeatureCollection()
	 * @generated
	 */
	void setFeatureCollection(FeatureCollectionType value);

	/**
	 * Returns the value of the '<em><b>Get Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *             The GetCapapbilities element is used to request that a Web Feature
	 *             Service generate an XML document describing the organization
	 *             providing the service, the WFS operations that the service
	 *             supports, a list of feature types that the service can operate
	 *             on and list of filtering capabilities that the service support.
	 *             Such an XML document is called a capabilities document.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Get Capabilities</em>' containment reference.
	 * @see #setGetCapabilities(GetCapabilitiesType)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_GetCapabilities()
	 * @model containment="true" resolveProxies="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='GetCapabilities' namespace='##targetNamespace'"
	 * @generated
	 */
	GetCapabilitiesType getGetCapabilities();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Get Capabilities</em>' containment reference.
	 * @see #getGetCapabilities()
	 * @generated
	 */
	void setGetCapabilities(GetCapabilitiesType value);

	/**
	 * Returns the value of the '<em><b>Get Feature</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *             The GetFeature element is used to request that a Web Feature
	 *             Service return feature instances of one or more feature types.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Get Feature</em>' containment reference.
	 * @see #setGetFeature(GetFeatureType)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_GetFeature()
	 * @model containment="true" resolveProxies="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='GetFeature' namespace='##targetNamespace'"
	 * @generated
	 */
	GetFeatureType getGetFeature();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getGetFeature <em>Get Feature</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Get Feature</em>' containment reference.
	 * @see #getGetFeature()
	 * @generated
	 */
	void setGetFeature(GetFeatureType value);

	/**
	 * Returns the value of the '<em><b>Query</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *             The Query element is used to describe a single query.
	 *             One or more Query elements can be specified inside a
	 *             GetFeature element so that multiple queries can be
	 *             executed in one request.  The output from the various
	 *             queries are combined in a wfs:FeatureCollection element
	 *             to form the response to the request.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Query</em>' containment reference.
	 * @see #setQuery(QueryType)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_Query()
	 * @model containment="true" resolveProxies="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Query' namespace='##targetNamespace'"
	 * @generated
	 */
	QueryType getQuery();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getQuery <em>Query</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Query</em>' containment reference.
	 * @see #getQuery()
	 * @generated
	 */
	void setQuery(QueryType value);

	/**
	 * Returns the value of the '<em><b>Delete</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *             The Delete element is used to indicate that one or more
	 *             feature instances should be removed from the feature
	 *             repository.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Delete</em>' containment reference.
	 * @see #setDelete(DeleteElementType)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_Delete()
	 * @model containment="true" resolveProxies="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Delete' namespace='##targetNamespace'"
	 * @generated
	 */
	DeleteElementType getDelete();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getDelete <em>Delete</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Delete</em>' containment reference.
	 * @see #getDelete()
	 * @generated
	 */
	void setDelete(DeleteElementType value);

	/**
	 * Returns the value of the '<em><b>Failed</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Failed</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Failed</em>' containment reference.
	 * @see #setFailed(EmptyType)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_Failed()
	 * @model containment="true" resolveProxies="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='FAILED' namespace='##targetNamespace'"
	 * @generated
	 */
	EmptyType getFailed();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getFailed <em>Failed</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Failed</em>' containment reference.
	 * @see #getFailed()
	 * @generated
	 */
	void setFailed(EmptyType value);

	/**
	 * Returns the value of the '<em><b>Get Feature With Lock</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *             This is the root element for the GetFeatureWithLock request.
	 *             The GetFeatureWithLock operation performs identically to a
	 *             GetFeature request except that the GetFeatureWithLock request
	 *             locks all the feature instances in the result set and returns
	 *             a lock identifier to a client application in the response.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Get Feature With Lock</em>' containment reference.
	 * @see #setGetFeatureWithLock(GetFeatureWithLockType)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_GetFeatureWithLock()
	 * @model containment="true" resolveProxies="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='GetFeatureWithLock' namespace='##targetNamespace'"
	 * @generated
	 */
	GetFeatureWithLockType getGetFeatureWithLock();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getGetFeatureWithLock <em>Get Feature With Lock</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Get Feature With Lock</em>' containment reference.
	 * @see #getGetFeatureWithLock()
	 * @generated
	 */
	void setGetFeatureWithLock(GetFeatureWithLockType value);

	/**
	 * Returns the value of the '<em><b>Insert</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *             The Insert element is used to indicate that the Web Feature
	 *             Service should create a new instance of a feature type.  The
	 *             feature instance is specified using GML2 and one or more
	 *             feature instances to be created can be contained inside the
	 *             Insert element.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Insert</em>' containment reference.
	 * @see #setInsert(InsertElementType)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_Insert()
	 * @model containment="true" resolveProxies="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Insert' namespace='##targetNamespace'"
	 * @generated
	 */
	InsertElementType getInsert();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getInsert <em>Insert</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Insert</em>' containment reference.
	 * @see #getInsert()
	 * @generated
	 */
	void setInsert(InsertElementType value);

	/**
	 * Returns the value of the '<em><b>Lock Feature</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *             This is the root element for a LockFeature request.
	 *             The LockFeature request can be used to lock one or
	 *             more feature instances.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Lock Feature</em>' containment reference.
	 * @see #setLockFeature(LockFeatureType)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_LockFeature()
	 * @model containment="true" resolveProxies="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='LockFeature' namespace='##targetNamespace'"
	 * @generated
	 */
	LockFeatureType getLockFeature();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getLockFeature <em>Lock Feature</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lock Feature</em>' containment reference.
	 * @see #getLockFeature()
	 * @generated
	 */
	void setLockFeature(LockFeatureType value);

	/**
	 * Returns the value of the '<em><b>Lock Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *             The LockId element contains the value of the lock identifier
	 *             obtained by a client application from a previous GetFeatureWithLock
	 *             or LockFeature request.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Lock Id</em>' attribute.
	 * @see #setLockId(String)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_LockId()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='LockId' namespace='##targetNamespace'"
	 * @generated
	 */
	String getLockId();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getLockId <em>Lock Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lock Id</em>' attribute.
	 * @see #getLockId()
	 * @generated
	 */
	void setLockId(String value);

	/**
	 * Returns the value of the '<em><b>Native</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *             Many times, a Web Feature Service interacts with a repository
	 *             that may have special vendor specific capabilities.  The native
	 *             element allows vendor specific command to be passed to the
	 *             repository via the Web Feature Service.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Native</em>' containment reference.
	 * @see #setNative(NativeType)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_Native()
	 * @model containment="true" resolveProxies="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Native' namespace='##targetNamespace'"
	 * @generated
	 */
	NativeType getNative();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getNative <em>Native</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Native</em>' containment reference.
	 * @see #getNative()
	 * @generated
	 */
	void setNative(NativeType value);

	/**
	 * Returns the value of the '<em><b>Partial</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Partial</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Partial</em>' containment reference.
	 * @see #setPartial(EmptyType)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_Partial()
	 * @model containment="true" resolveProxies="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='PARTIAL' namespace='##targetNamespace'"
	 * @generated
	 */
	EmptyType getPartial();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getPartial <em>Partial</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Partial</em>' containment reference.
	 * @see #getPartial()
	 * @generated
	 */
	void setPartial(EmptyType value);

	/**
	 * Returns the value of the '<em><b>Property</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *             The Property element is used to specify the new
	 *             value of a feature property inside an Update element.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Property</em>' containment reference.
	 * @see #setProperty(PropertyType)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_Property()
	 * @model containment="true" resolveProxies="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Property' namespace='##targetNamespace'"
	 * @generated
	 */
	PropertyType getProperty();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getProperty <em>Property</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Property</em>' containment reference.
	 * @see #getProperty()
	 * @generated
	 */
	void setProperty(PropertyType value);

	/**
	 * Returns the value of the '<em><b>Success</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Success</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Success</em>' containment reference.
	 * @see #setSuccess(EmptyType)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_Success()
	 * @model containment="true" resolveProxies="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='SUCCESS' namespace='##targetNamespace'"
	 * @generated
	 */
	EmptyType getSuccess();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getSuccess <em>Success</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Success</em>' containment reference.
	 * @see #getSuccess()
	 * @generated
	 */
	void setSuccess(EmptyType value);

	/**
	 * Returns the value of the '<em><b>Transaction</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *             This is the root element for a Transaction request.
	 *             A transaction request allows insert, update and
	 *             delete operations to be performed to create, change
	 *             or remove feature instances.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Transaction</em>' containment reference.
	 * @see #setTransaction(TransactionType)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_Transaction()
	 * @model containment="true" resolveProxies="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Transaction' namespace='##targetNamespace'"
	 * @generated
	 */
	TransactionType getTransaction();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getTransaction <em>Transaction</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transaction</em>' containment reference.
	 * @see #getTransaction()
	 * @generated
	 */
	void setTransaction(TransactionType value);

	/**
	 * Returns the value of the '<em><b>Update</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *             One or more existing feature instances can be changed by
	 *             using the Update element.  Changing a feature instance
	 *             means that the current value of one or more properties of
	 *             the feature are replaced with new values.  The Update
	 *             element contains  one or more Property elements.  A
	 *             Property element contains the name or a feature property
	 *             who's value is to be changed and the replacement value
	 *             for that property.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Update</em>' containment reference.
	 * @see #setUpdate(UpdateElementType)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_Update()
	 * @model containment="true" resolveProxies="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Update' namespace='##targetNamespace'"
	 * @generated
	 */
	UpdateElementType getUpdate();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getUpdate <em>Update</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Update</em>' containment reference.
	 * @see #getUpdate()
	 * @generated
	 */
	void setUpdate(UpdateElementType value);

	/**
	 * Returns the value of the '<em><b>Wfs Lock Feature Response</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *             The WFS_LockFeatureResponse element contains a report
	 *             about the completion status of a LockFeature request.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Wfs Lock Feature Response</em>' containment reference.
	 * @see #setWfsLockFeatureResponse(WFSLockFeatureResponseType)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_WfsLockFeatureResponse()
	 * @model containment="true" resolveProxies="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='WFS_LockFeatureResponse' namespace='##targetNamespace'"
	 * @generated
	 */
	WFSLockFeatureResponseType getWfsLockFeatureResponse();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getWfsLockFeatureResponse <em>Wfs Lock Feature Response</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Wfs Lock Feature Response</em>' containment reference.
	 * @see #getWfsLockFeatureResponse()
	 * @generated
	 */
	void setWfsLockFeatureResponse(WFSLockFeatureResponseType value);

	/**
	 * Returns the value of the '<em><b>Wfs Transaction Response</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *             The WFS_TransactionResponse element contains a report
	 *             about the completion status of a Transaction operation.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Wfs Transaction Response</em>' containment reference.
	 * @see #setWfsTransactionResponse(WFSTransactionResponseType)
	 * @see net.opengis.wfs.WFSPackage#getDocumentRoot_WfsTransactionResponse()
	 * @model containment="true" resolveProxies="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='WFS_TransactionResponse' namespace='##targetNamespace'"
	 * @generated
	 */
	WFSTransactionResponseType getWfsTransactionResponse();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.DocumentRoot#getWfsTransactionResponse <em>Wfs Transaction Response</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Wfs Transaction Response</em>' containment reference.
	 * @see #getWfsTransactionResponse()
	 * @generated
	 */
	void setWfsTransactionResponse(WFSTransactionResponseType value);

} // DocumentRoot
