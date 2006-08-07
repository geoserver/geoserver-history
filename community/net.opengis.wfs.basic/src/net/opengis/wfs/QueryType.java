/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import org.opengis.filter.Filter;

import org.opengis.filter.expression.PropertyName;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Query Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             The Query element is of type QueryType.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.QueryType#getPropertyName <em>Property Name</em>}</li>
 *   <li>{@link net.opengis.wfs.QueryType#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.wfs.QueryType#getFeatureVersion <em>Feature Version</em>}</li>
 *   <li>{@link net.opengis.wfs.QueryType#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.wfs.QueryType#getTypeName <em>Type Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WFSPackage#getQueryType()
 * @model extendedMetaData="name='QueryType' kind='elementOnly'"
 * @generated
 */
public interface QueryType extends EObject{
	/**
	 * Returns the value of the '<em><b>Property Name</b></em>' attribute list.
	 * The list contents are of type {@link org.opengis.filter.expression.PropertyName}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                 The PropertyName element is used to specify one or more
	 *                 properties of a feature whose values are to be retrieved
	 *                 by a Web Feature Service.
	 * 
	 *                 While a Web Feature Service should endeavour to satisfy
	 *                 the exact request specified, in some instance this may
	 *                 not be possible.  Specifically, a Web Feature Service
	 *                 must generate a valid GML2 response to a Query operation.
	 *                 The schema used to generate the output may include
	 *                 properties that are mandatory.  In order that the output
	 *                 validates, these mandatory properties must be specified
	 *                 in the request.  If they are not, a Web Feature Service
	 *                 may add them automatically to the Query before processing
	 *                 it.  Thus a client application should, in general, be
	 *                 prepared to receive more properties than it requested.
	 * 
	 *                 Of course, using the DescribeFeatureType request, a client
	 *                 application can determine which properties are mandatory
	 *                 and request them in the first place.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Property Name</em>' attribute list.
	 * @see net.opengis.wfs.WFSPackage#getQueryType_PropertyName()
	 * @model type="org.opengis.filter.expression.PropertyName" dataType="net.opengis.wfs.PropertyName"
	 * @generated
	 */
	EList getPropertyName();

	/**
	 * Returns the value of the '<em><b>Filter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                 The Filter element is used to define spatial and/or non-spatial
	 *                 constraints on query.  Spatial constrains use GML2 to specify
	 *                 the constraining geometry.  A full description of the Filter
	 *                 element can be found in the Filter Encoding Implementation
	 *                 Specification.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Filter</em>' attribute.
	 * @see #setFilter(Filter)
	 * @see net.opengis.wfs.WFSPackage#getQueryType_Filter()
	 * @model unique="false" dataType="net.opengis.wfs.Filter"
	 * @generated
	 */
	Filter getFilter();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.QueryType#getFilter <em>Filter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Filter</em>' attribute.
	 * @see #getFilter()
	 * @generated
	 */
	void setFilter(Filter value);

	/**
	 * Returns the value of the '<em><b>Feature Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *               For systems that implement versioning, the featureVersion
	 *               attribute is used to specify which version of a particular
	 *               feature instance is to be retrieved.  A value of ALL means
	 *               that all versions should be retrieved.  An integer value
	 *               'i', means that the ith version should be retrieve if it
	 *               exists or the most recent version otherwise.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Feature Version</em>' attribute.
	 * @see #setFeatureVersion(String)
	 * @see net.opengis.wfs.WFSPackage#getQueryType_FeatureVersion()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='featureVersion'"
	 * @generated
	 */
	String getFeatureVersion();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.QueryType#getFeatureVersion <em>Feature Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Feature Version</em>' attribute.
	 * @see #getFeatureVersion()
	 * @generated
	 */
	void setFeatureVersion(String value);

	/**
	 * Returns the value of the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Handle</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Handle</em>' attribute.
	 * @see #setHandle(String)
	 * @see net.opengis.wfs.WFSPackage#getQueryType_Handle()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='handle'"
	 * @generated
	 */
	String getHandle();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.QueryType#getHandle <em>Handle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Handle</em>' attribute.
	 * @see #getHandle()
	 * @generated
	 */
	void setHandle(String value);

	/**
	 * Returns the value of the '<em><b>Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type Name</em>' attribute.
	 * @see #setTypeName(Object)
	 * @see net.opengis.wfs.WFSPackage#getQueryType_TypeName()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.QName" required="true"
	 *        extendedMetaData="kind='attribute' name='typeName'"
	 * @generated
	 */
	Object getTypeName();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.QueryType#getTypeName <em>Type Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type Name</em>' attribute.
	 * @see #getTypeName()
	 * @generated
	 */
	void setTypeName(Object value);

} // QueryType
