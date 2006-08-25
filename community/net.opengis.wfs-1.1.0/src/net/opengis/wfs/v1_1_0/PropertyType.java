/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.v1_1_0;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.v1_1_0.PropertyType#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.PropertyType#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.v1_1_0.WFSPackage#getPropertyType()
 * @model extendedMetaData="name='PropertyType' kind='elementOnly'"
 * @generated
 */
public interface PropertyType extends EObject{
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                   The Name element contains the name of a feature property
	 *                   to be updated.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(Object)
	 * @see net.opengis.wfs.v1_1_0.WFSPackage#getPropertyType_Name()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.QName" required="true"
	 *        extendedMetaData="kind='element' name='Name' namespace='##targetNamespace'"
	 * @generated
	 */
	Object getName();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.v1_1_0.PropertyType#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(Object value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                   The Value element contains the replacement value for the
	 *                   named property.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Value</em>' containment reference.
	 * @see #setValue(EObject)
	 * @see net.opengis.wfs.v1_1_0.WFSPackage#getPropertyType_Value()
	 * @model containment="true" resolveProxies="false"
	 *        extendedMetaData="kind='element' name='Value' namespace='##targetNamespace'"
	 * @generated
	 */
	EObject getValue();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.v1_1_0.PropertyType#getValue <em>Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' containment reference.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(EObject value);

} // PropertyType
