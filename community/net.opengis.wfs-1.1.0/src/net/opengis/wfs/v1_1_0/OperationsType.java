/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.v1_1_0;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operations Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.v1_1_0.OperationsType#getOperation <em>Operation</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.v1_1_0.WFSPackage#getOperationsType()
 * @model extendedMetaData="name='OperationsType' kind='elementOnly'"
 * @generated
 */
public interface OperationsType extends EObject {
	/**
	 * Returns the value of the '<em><b>Operation</b></em>' attribute list.
	 * The list contents are of type {@link net.opengis.wfs.v1_1_0.OperationType}.
	 * The literals are from the enumeration {@link net.opengis.wfs.v1_1_0.OperationType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operation</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operation</em>' attribute list.
	 * @see net.opengis.wfs.v1_1_0.OperationType
	 * @see net.opengis.wfs.v1_1_0.WFSPackage#getOperationsType_Operation()
	 * @model type="net.opengis.wfs.v1_1_0.OperationType" default="Insert" unique="false" required="true"
	 *        extendedMetaData="kind='element' name='Operation' namespace='##targetNamespace'"
	 * @generated
	 */
	EList getOperation();

} // OperationsType
