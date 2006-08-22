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
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WFSPackage#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject {
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

} // DocumentRoot
