/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows.v1_0_0.impl;

import java.util.Collection;

import net.opengis.ows.v1_0_0.DomainType;
import net.opengis.ows.v1_0_0.OWSPackage;
import net.opengis.ows.v1_0_0.OperationType;
import net.opengis.ows.v1_0_0.OperationsMetadataType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operations Metadata Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.OperationsMetadataTypeImpl#getOperation <em>Operation</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.OperationsMetadataTypeImpl#getParameter <em>Parameter</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.OperationsMetadataTypeImpl#getConstraint <em>Constraint</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.OperationsMetadataTypeImpl#getExtendedCapabilities <em>Extended Capabilities</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OperationsMetadataTypeImpl extends EObjectImpl implements OperationsMetadataType {
	/**
	 * The cached value of the '{@link #getOperation() <em>Operation</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperation()
	 * @generated
	 * @ordered
	 */
	protected EList operation = null;

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
	 * The cached value of the '{@link #getExtendedCapabilities() <em>Extended Capabilities</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtendedCapabilities()
	 * @generated
	 * @ordered
	 */
	protected EObject extendedCapabilities = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OperationsMetadataTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return OWSPackage.eINSTANCE.getOperationsMetadataType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getOperation() {
		if (operation == null) {
			operation = new EObjectContainmentEList(OperationType.class, this, OWSPackage.OPERATIONS_METADATA_TYPE__OPERATION);
		}
		return operation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getParameter() {
		if (parameter == null) {
			parameter = new EObjectContainmentEList(DomainType.class, this, OWSPackage.OPERATIONS_METADATA_TYPE__PARAMETER);
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
			constraint = new EObjectContainmentEList(DomainType.class, this, OWSPackage.OPERATIONS_METADATA_TYPE__CONSTRAINT);
		}
		return constraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getExtendedCapabilities() {
		return extendedCapabilities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetExtendedCapabilities(EObject newExtendedCapabilities, NotificationChain msgs) {
		EObject oldExtendedCapabilities = extendedCapabilities;
		extendedCapabilities = newExtendedCapabilities;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OWSPackage.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES, oldExtendedCapabilities, newExtendedCapabilities);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExtendedCapabilities(EObject newExtendedCapabilities) {
		if (newExtendedCapabilities != extendedCapabilities) {
			NotificationChain msgs = null;
			if (extendedCapabilities != null)
				msgs = ((InternalEObject)extendedCapabilities).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OWSPackage.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES, null, msgs);
			if (newExtendedCapabilities != null)
				msgs = ((InternalEObject)newExtendedCapabilities).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OWSPackage.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES, null, msgs);
			msgs = basicSetExtendedCapabilities(newExtendedCapabilities, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OWSPackage.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES, newExtendedCapabilities, newExtendedCapabilities));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case OWSPackage.OPERATIONS_METADATA_TYPE__OPERATION:
					return ((InternalEList)getOperation()).basicRemove(otherEnd, msgs);
				case OWSPackage.OPERATIONS_METADATA_TYPE__PARAMETER:
					return ((InternalEList)getParameter()).basicRemove(otherEnd, msgs);
				case OWSPackage.OPERATIONS_METADATA_TYPE__CONSTRAINT:
					return ((InternalEList)getConstraint()).basicRemove(otherEnd, msgs);
				case OWSPackage.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES:
					return basicSetExtendedCapabilities(null, msgs);
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
			case OWSPackage.OPERATIONS_METADATA_TYPE__OPERATION:
				return getOperation();
			case OWSPackage.OPERATIONS_METADATA_TYPE__PARAMETER:
				return getParameter();
			case OWSPackage.OPERATIONS_METADATA_TYPE__CONSTRAINT:
				return getConstraint();
			case OWSPackage.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES:
				return getExtendedCapabilities();
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
			case OWSPackage.OPERATIONS_METADATA_TYPE__OPERATION:
				getOperation().clear();
				getOperation().addAll((Collection)newValue);
				return;
			case OWSPackage.OPERATIONS_METADATA_TYPE__PARAMETER:
				getParameter().clear();
				getParameter().addAll((Collection)newValue);
				return;
			case OWSPackage.OPERATIONS_METADATA_TYPE__CONSTRAINT:
				getConstraint().clear();
				getConstraint().addAll((Collection)newValue);
				return;
			case OWSPackage.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES:
				setExtendedCapabilities((EObject)newValue);
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
			case OWSPackage.OPERATIONS_METADATA_TYPE__OPERATION:
				getOperation().clear();
				return;
			case OWSPackage.OPERATIONS_METADATA_TYPE__PARAMETER:
				getParameter().clear();
				return;
			case OWSPackage.OPERATIONS_METADATA_TYPE__CONSTRAINT:
				getConstraint().clear();
				return;
			case OWSPackage.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES:
				setExtendedCapabilities((EObject)null);
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
			case OWSPackage.OPERATIONS_METADATA_TYPE__OPERATION:
				return operation != null && !operation.isEmpty();
			case OWSPackage.OPERATIONS_METADATA_TYPE__PARAMETER:
				return parameter != null && !parameter.isEmpty();
			case OWSPackage.OPERATIONS_METADATA_TYPE__CONSTRAINT:
				return constraint != null && !constraint.isEmpty();
			case OWSPackage.OPERATIONS_METADATA_TYPE__EXTENDED_CAPABILITIES:
				return extendedCapabilities != null;
		}
		return eDynamicIsSet(eFeature);
	}

} //OperationsMetadataTypeImpl
