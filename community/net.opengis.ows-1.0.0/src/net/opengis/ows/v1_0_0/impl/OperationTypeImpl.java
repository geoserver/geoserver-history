/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows.v1_0_0.impl;

import java.util.Collection;

import net.opengis.ows.v1_0_0.DCPType;
import net.opengis.ows.v1_0_0.DomainType;
import net.opengis.ows.v1_0_0.MetadataType;
import net.opengis.ows.v1_0_0.OWSPackage;
import net.opengis.ows.v1_0_0.OperationType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.OperationTypeImpl#getDCP <em>DCP</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.OperationTypeImpl#getParameter <em>Parameter</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.OperationTypeImpl#getConstraint <em>Constraint</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.OperationTypeImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.OperationTypeImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OperationTypeImpl extends EObjectImpl implements OperationType {
	/**
	 * The cached value of the '{@link #getDCP() <em>DCP</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDCP()
	 * @generated
	 * @ordered
	 */
	protected EList dCP = null;

	/**
	 * The cached value of the '{@link #getParameter() <em>Parameter</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameter()
	 * @generated
	 * @ordered
	 */
	protected EList parameter = null;

	/**
	 * The cached value of the '{@link #getConstraint() <em>Constraint</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraint()
	 * @generated
	 * @ordered
	 */
	protected EList constraint = null;

	/**
	 * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetadata()
	 * @generated
	 * @ordered
	 */
	protected EList metadata = null;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OperationTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return OWSPackage.eINSTANCE.getOperationType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getDCP() {
		if (dCP == null) {
			dCP = new EObjectContainmentEList(DCPType.class, this, OWSPackage.OPERATION_TYPE__DCP);
		}
		return dCP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getParameter() {
		if (parameter == null) {
			parameter = new EObjectContainmentEList(DomainType.class, this, OWSPackage.OPERATION_TYPE__PARAMETER);
		}
		return parameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getConstraint() {
		if (constraint == null) {
			constraint = new EObjectContainmentEList(DomainType.class, this, OWSPackage.OPERATION_TYPE__CONSTRAINT);
		}
		return constraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getMetadata() {
		if (metadata == null) {
			metadata = new EObjectContainmentEList(MetadataType.class, this, OWSPackage.OPERATION_TYPE__METADATA);
		}
		return metadata;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OWSPackage.OPERATION_TYPE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case OWSPackage.OPERATION_TYPE__DCP:
					return ((InternalEList)getDCP()).basicRemove(otherEnd, msgs);
				case OWSPackage.OPERATION_TYPE__PARAMETER:
					return ((InternalEList)getParameter()).basicRemove(otherEnd, msgs);
				case OWSPackage.OPERATION_TYPE__CONSTRAINT:
					return ((InternalEList)getConstraint()).basicRemove(otherEnd, msgs);
				case OWSPackage.OPERATION_TYPE__METADATA:
					return ((InternalEList)getMetadata()).basicRemove(otherEnd, msgs);
				default:
					return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
			}
		}
		return eBasicSetContainer(null, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case OWSPackage.OPERATION_TYPE__DCP:
				return getDCP();
			case OWSPackage.OPERATION_TYPE__PARAMETER:
				return getParameter();
			case OWSPackage.OPERATION_TYPE__CONSTRAINT:
				return getConstraint();
			case OWSPackage.OPERATION_TYPE__METADATA:
				return getMetadata();
			case OWSPackage.OPERATION_TYPE__NAME:
				return getName();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case OWSPackage.OPERATION_TYPE__DCP:
				getDCP().clear();
				getDCP().addAll((Collection)newValue);
				return;
			case OWSPackage.OPERATION_TYPE__PARAMETER:
				getParameter().clear();
				getParameter().addAll((Collection)newValue);
				return;
			case OWSPackage.OPERATION_TYPE__CONSTRAINT:
				getConstraint().clear();
				getConstraint().addAll((Collection)newValue);
				return;
			case OWSPackage.OPERATION_TYPE__METADATA:
				getMetadata().clear();
				getMetadata().addAll((Collection)newValue);
				return;
			case OWSPackage.OPERATION_TYPE__NAME:
				setName((String)newValue);
				return;
		}
		eDynamicSet(eFeature, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case OWSPackage.OPERATION_TYPE__DCP:
				getDCP().clear();
				return;
			case OWSPackage.OPERATION_TYPE__PARAMETER:
				getParameter().clear();
				return;
			case OWSPackage.OPERATION_TYPE__CONSTRAINT:
				getConstraint().clear();
				return;
			case OWSPackage.OPERATION_TYPE__METADATA:
				getMetadata().clear();
				return;
			case OWSPackage.OPERATION_TYPE__NAME:
				setName(NAME_EDEFAULT);
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case OWSPackage.OPERATION_TYPE__DCP:
				return dCP != null && !dCP.isEmpty();
			case OWSPackage.OPERATION_TYPE__PARAMETER:
				return parameter != null && !parameter.isEmpty();
			case OWSPackage.OPERATION_TYPE__CONSTRAINT:
				return constraint != null && !constraint.isEmpty();
			case OWSPackage.OPERATION_TYPE__METADATA:
				return metadata != null && !metadata.isEmpty();
			case OWSPackage.OPERATION_TYPE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //OperationTypeImpl
