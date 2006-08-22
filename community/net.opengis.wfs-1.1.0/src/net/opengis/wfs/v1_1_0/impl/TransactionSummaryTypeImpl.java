/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.v1_1_0.impl;

import java.math.BigInteger;

import net.opengis.wfs.v1_1_0.TransactionSummaryType;
import net.opengis.wfs.v1_1_0.WFSPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transaction Summary Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.TransactionSummaryTypeImpl#getTotalInserted <em>Total Inserted</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.TransactionSummaryTypeImpl#getTotalUpdated <em>Total Updated</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.TransactionSummaryTypeImpl#getTotalDeleted <em>Total Deleted</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TransactionSummaryTypeImpl extends EObjectImpl implements TransactionSummaryType {
	/**
	 * The default value of the '{@link #getTotalInserted() <em>Total Inserted</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalInserted()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger TOTAL_INSERTED_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTotalInserted() <em>Total Inserted</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalInserted()
	 * @generated
	 * @ordered
	 */
	protected BigInteger totalInserted = TOTAL_INSERTED_EDEFAULT;

	/**
	 * The default value of the '{@link #getTotalUpdated() <em>Total Updated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalUpdated()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger TOTAL_UPDATED_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTotalUpdated() <em>Total Updated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalUpdated()
	 * @generated
	 * @ordered
	 */
	protected BigInteger totalUpdated = TOTAL_UPDATED_EDEFAULT;

	/**
	 * The default value of the '{@link #getTotalDeleted() <em>Total Deleted</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalDeleted()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger TOTAL_DELETED_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTotalDeleted() <em>Total Deleted</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalDeleted()
	 * @generated
	 * @ordered
	 */
	protected BigInteger totalDeleted = TOTAL_DELETED_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TransactionSummaryTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return WFSPackage.eINSTANCE.getTransactionSummaryType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigInteger getTotalInserted() {
		return totalInserted;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTotalInserted(BigInteger newTotalInserted) {
		BigInteger oldTotalInserted = totalInserted;
		totalInserted = newTotalInserted;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED, oldTotalInserted, totalInserted));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigInteger getTotalUpdated() {
		return totalUpdated;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTotalUpdated(BigInteger newTotalUpdated) {
		BigInteger oldTotalUpdated = totalUpdated;
		totalUpdated = newTotalUpdated;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED, oldTotalUpdated, totalUpdated));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigInteger getTotalDeleted() {
		return totalDeleted;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTotalDeleted(BigInteger newTotalDeleted) {
		BigInteger oldTotalDeleted = totalDeleted;
		totalDeleted = newTotalDeleted;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED, oldTotalDeleted, totalDeleted));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case WFSPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED:
				return getTotalInserted();
			case WFSPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED:
				return getTotalUpdated();
			case WFSPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED:
				return getTotalDeleted();
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
			case WFSPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED:
				setTotalInserted((BigInteger)newValue);
				return;
			case WFSPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED:
				setTotalUpdated((BigInteger)newValue);
				return;
			case WFSPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED:
				setTotalDeleted((BigInteger)newValue);
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
			case WFSPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED:
				setTotalInserted(TOTAL_INSERTED_EDEFAULT);
				return;
			case WFSPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED:
				setTotalUpdated(TOTAL_UPDATED_EDEFAULT);
				return;
			case WFSPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED:
				setTotalDeleted(TOTAL_DELETED_EDEFAULT);
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
			case WFSPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED:
				return TOTAL_INSERTED_EDEFAULT == null ? totalInserted != null : !TOTAL_INSERTED_EDEFAULT.equals(totalInserted);
			case WFSPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED:
				return TOTAL_UPDATED_EDEFAULT == null ? totalUpdated != null : !TOTAL_UPDATED_EDEFAULT.equals(totalUpdated);
			case WFSPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED:
				return TOTAL_DELETED_EDEFAULT == null ? totalDeleted != null : !TOTAL_DELETED_EDEFAULT.equals(totalDeleted);
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
		result.append(" (totalInserted: ");
		result.append(totalInserted);
		result.append(", totalUpdated: ");
		result.append(totalUpdated);
		result.append(", totalDeleted: ");
		result.append(totalDeleted);
		result.append(')');
		return result.toString();
	}

} //TransactionSummaryTypeImpl
