/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;
import org.opengis.filter.Filter;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Update Element Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.UpdateElementType#getProperty <em>Property</em>}</li>
 *   <li>{@link net.opengis.wfs.UpdateElementType#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.wfs.UpdateElementType#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.wfs.UpdateElementType#getTypeName <em>Type Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WFSPackage#getUpdateElementType()
 * @model extendedMetaData="name='UpdateElementType' kind='elementOnly'"
 * @generated
 */
public interface UpdateElementType extends TransactionOperation {
	/**
	 * Returns the value of the '<em><b>Property</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wfs.PropertyType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Property</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Property</em>' containment reference list.
	 * @see net.opengis.wfs.WFSPackage#getUpdateElementType_Property()
	 * @model type="net.opengis.wfs.PropertyType" containment="true" resolveProxies="false" required="true"
	 *        extendedMetaData="kind='element' name='Property' namespace='##targetNamespace'"
	 * @generated
	 */
	EList getProperty();

	/**
	 * Returns the value of the '<em><b>Filter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                   The Filter element is used to constrain the scope
	 *                   of the update operation to those features identified
	 *                   by the filter.  Feature instances can be specified
	 *                   explicitly and individually using the identifier of
	 *                   each feature instance OR a set of features to be
	 *                   operated on can be identified by specifying spatial
	 *                   and non-spatial constraints in the filter.
	 *                   If no filter is specified, then the update operation
	 *                   applies to all feature instances.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Filter</em>' attribute.
	 * @see #setFilter(Object)
	 * @see net.opengis.wfs.WFSPackage#getUpdateElementType_Filter()
	 * @model 
	 */
	Filter getFilter();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.UpdateElementType#getFilter <em>Filter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Filter</em>' attribute.
	 * @see #getFilter()
	 * @generated
	 */
	void setFilter(Filter value);

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
	 * @see net.opengis.wfs.WFSPackage#getUpdateElementType_Handle()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='handle'"
	 * @generated
	 */
	String getHandle();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.UpdateElementType#getHandle <em>Handle</em>}' attribute.
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
	 * @see net.opengis.wfs.WFSPackage#getUpdateElementType_TypeName()
	 * @model
	 */
	QName getTypeName();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.UpdateElementType#getTypeName <em>Type Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type Name</em>' attribute.
	 * @see #getTypeName()
	 * @generated
	 */
	void setTypeName(QName value);

} // UpdateElementType
