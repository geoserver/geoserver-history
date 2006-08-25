/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.v1_1_0;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Features Locked Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.v1_1_0.FeaturesLockedType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.FeaturesLockedType#getFeatureId <em>Feature Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.v1_1_0.WFSPackage#getFeaturesLockedType()
 * @model extendedMetaData="name='FeaturesLockedType' kind='elementOnly'"
 * @generated
 */
public interface FeaturesLockedType extends EObject{
	/**
	 * Returns the value of the '<em><b>Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' attribute list.
	 * @see net.opengis.wfs.v1_1_0.WFSPackage#getFeaturesLockedType_Group()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='group:0'"
	 * @generated
	 */
	FeatureMap getGroup();

	/**
	 * Returns the value of the '<em><b>Feature Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature Id</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature Id</em>' attribute.
	 * @see #setFeatureId(Object)
	 * @see net.opengis.wfs.v1_1_0.WFSPackage#getFeaturesLockedType_FeatureId()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType" required="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='FeatureId' namespace='http://www.opengis.net/ogc' group='#group:0'"
	 * @generated
	 */
	Object getFeatureId();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.v1_1_0.FeaturesLockedType#getFeatureId <em>Feature Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Feature Id</em>' attribute.
	 * @see #getFeatureId()
	 * @generated
	 */
	void setFeatureId(Object value);

} // FeaturesLockedType
