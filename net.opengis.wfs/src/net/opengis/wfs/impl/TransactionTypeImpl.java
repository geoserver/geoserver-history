/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.util.Collection;

import net.opengis.wfs.AllSomeType;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.WFSPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transaction Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.TransactionTypeImpl#getLockId <em>Lock Id</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.TransactionTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.TransactionTypeImpl#getInsert <em>Insert</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.TransactionTypeImpl#getUpdate <em>Update</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.TransactionTypeImpl#getDelete <em>Delete</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.TransactionTypeImpl#getNative <em>Native</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.TransactionTypeImpl#getReleaseAction <em>Release Action</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TransactionTypeImpl extends BaseRequestTypeImpl implements TransactionType {
	/**
	 * The default value of the '{@link #getLockId() <em>Lock Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLockId()
	 * @generated
	 * @ordered
	 */
	protected static final String LOCK_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLockId() <em>Lock Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLockId()
	 * @generated
	 * @ordered
	 */
	protected String lockId = LOCK_ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroup()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap group = null;

	/**
	 * The default value of the '{@link #getReleaseAction() <em>Release Action</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReleaseAction()
	 * @generated
	 * @ordered
	 */
	protected static final AllSomeType RELEASE_ACTION_EDEFAULT = AllSomeType.ALL_LITERAL;

	/**
	 * The cached value of the '{@link #getReleaseAction() <em>Release Action</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReleaseAction()
	 * @generated
	 * @ordered
	 */
	protected AllSomeType releaseAction = RELEASE_ACTION_EDEFAULT;

	/**
	 * This is true if the Release Action attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean releaseActionESet = false;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TransactionTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return WFSPackage.eINSTANCE.getTransactionType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLockId() {
		return lockId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLockId(String newLockId) {
		String oldLockId = lockId;
		lockId = newLockId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.TRANSACTION_TYPE__LOCK_ID, oldLockId, lockId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getGroup() {
		if (group == null) {
			group = new BasicFeatureMap(this, WFSPackage.TRANSACTION_TYPE__GROUP);
		}
		return group;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getInsert() {
		return ((FeatureMap)getGroup()).list(WFSPackage.eINSTANCE.getTransactionType_Insert());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getUpdate() {
		return ((FeatureMap)getGroup()).list(WFSPackage.eINSTANCE.getTransactionType_Update());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getDelete() {
		return ((FeatureMap)getGroup()).list(WFSPackage.eINSTANCE.getTransactionType_Delete());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getNative() {
		return ((FeatureMap)getGroup()).list(WFSPackage.eINSTANCE.getTransactionType_Native());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AllSomeType getReleaseAction() {
		return releaseAction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReleaseAction(AllSomeType newReleaseAction) {
		AllSomeType oldReleaseAction = releaseAction;
		releaseAction = newReleaseAction == null ? RELEASE_ACTION_EDEFAULT : newReleaseAction;
		boolean oldReleaseActionESet = releaseActionESet;
		releaseActionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.TRANSACTION_TYPE__RELEASE_ACTION, oldReleaseAction, releaseAction, !oldReleaseActionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetReleaseAction() {
		AllSomeType oldReleaseAction = releaseAction;
		boolean oldReleaseActionESet = releaseActionESet;
		releaseAction = RELEASE_ACTION_EDEFAULT;
		releaseActionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, WFSPackage.TRANSACTION_TYPE__RELEASE_ACTION, oldReleaseAction, RELEASE_ACTION_EDEFAULT, oldReleaseActionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetReleaseAction() {
		return releaseActionESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case WFSPackage.TRANSACTION_TYPE__GROUP:
					return ((InternalEList)getGroup()).basicRemove(otherEnd, msgs);
				case WFSPackage.TRANSACTION_TYPE__INSERT:
					return ((InternalEList)getInsert()).basicRemove(otherEnd, msgs);
				case WFSPackage.TRANSACTION_TYPE__UPDATE:
					return ((InternalEList)getUpdate()).basicRemove(otherEnd, msgs);
				case WFSPackage.TRANSACTION_TYPE__DELETE:
					return ((InternalEList)getDelete()).basicRemove(otherEnd, msgs);
				case WFSPackage.TRANSACTION_TYPE__NATIVE:
					return ((InternalEList)getNative()).basicRemove(otherEnd, msgs);
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
			case WFSPackage.TRANSACTION_TYPE__HANDLE:
				return getHandle();
			case WFSPackage.TRANSACTION_TYPE__SERVICE:
				return getService();
			case WFSPackage.TRANSACTION_TYPE__VERSION:
				return getVersion();
			case WFSPackage.TRANSACTION_TYPE__LOCK_ID:
				return getLockId();
			case WFSPackage.TRANSACTION_TYPE__GROUP:
				return getGroup();
			case WFSPackage.TRANSACTION_TYPE__INSERT:
				return getInsert();
			case WFSPackage.TRANSACTION_TYPE__UPDATE:
				return getUpdate();
			case WFSPackage.TRANSACTION_TYPE__DELETE:
				return getDelete();
			case WFSPackage.TRANSACTION_TYPE__NATIVE:
				return getNative();
			case WFSPackage.TRANSACTION_TYPE__RELEASE_ACTION:
				return getReleaseAction();
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
			case WFSPackage.TRANSACTION_TYPE__HANDLE:
				setHandle((String)newValue);
				return;
			case WFSPackage.TRANSACTION_TYPE__SERVICE:
				setService((String)newValue);
				return;
			case WFSPackage.TRANSACTION_TYPE__VERSION:
				setVersion((String)newValue);
				return;
			case WFSPackage.TRANSACTION_TYPE__LOCK_ID:
				setLockId((String)newValue);
				return;
			case WFSPackage.TRANSACTION_TYPE__GROUP:
				getGroup().clear();
				getGroup().addAll((Collection)newValue);
				return;
			case WFSPackage.TRANSACTION_TYPE__INSERT:
				getInsert().clear();
				getInsert().addAll((Collection)newValue);
				return;
			case WFSPackage.TRANSACTION_TYPE__UPDATE:
				getUpdate().clear();
				getUpdate().addAll((Collection)newValue);
				return;
			case WFSPackage.TRANSACTION_TYPE__DELETE:
				getDelete().clear();
				getDelete().addAll((Collection)newValue);
				return;
			case WFSPackage.TRANSACTION_TYPE__NATIVE:
				getNative().clear();
				getNative().addAll((Collection)newValue);
				return;
			case WFSPackage.TRANSACTION_TYPE__RELEASE_ACTION:
				setReleaseAction((AllSomeType)newValue);
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
			case WFSPackage.TRANSACTION_TYPE__HANDLE:
				setHandle(HANDLE_EDEFAULT);
				return;
			case WFSPackage.TRANSACTION_TYPE__SERVICE:
				unsetService();
				return;
			case WFSPackage.TRANSACTION_TYPE__VERSION:
				unsetVersion();
				return;
			case WFSPackage.TRANSACTION_TYPE__LOCK_ID:
				setLockId(LOCK_ID_EDEFAULT);
				return;
			case WFSPackage.TRANSACTION_TYPE__GROUP:
				getGroup().clear();
				return;
			case WFSPackage.TRANSACTION_TYPE__INSERT:
				getInsert().clear();
				return;
			case WFSPackage.TRANSACTION_TYPE__UPDATE:
				getUpdate().clear();
				return;
			case WFSPackage.TRANSACTION_TYPE__DELETE:
				getDelete().clear();
				return;
			case WFSPackage.TRANSACTION_TYPE__NATIVE:
				getNative().clear();
				return;
			case WFSPackage.TRANSACTION_TYPE__RELEASE_ACTION:
				unsetReleaseAction();
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
			case WFSPackage.TRANSACTION_TYPE__HANDLE:
				return HANDLE_EDEFAULT == null ? handle != null : !HANDLE_EDEFAULT.equals(handle);
			case WFSPackage.TRANSACTION_TYPE__SERVICE:
				return isSetService();
			case WFSPackage.TRANSACTION_TYPE__VERSION:
				return isSetVersion();
			case WFSPackage.TRANSACTION_TYPE__LOCK_ID:
				return LOCK_ID_EDEFAULT == null ? lockId != null : !LOCK_ID_EDEFAULT.equals(lockId);
			case WFSPackage.TRANSACTION_TYPE__GROUP:
				return group != null && !group.isEmpty();
			case WFSPackage.TRANSACTION_TYPE__INSERT:
				return !getInsert().isEmpty();
			case WFSPackage.TRANSACTION_TYPE__UPDATE:
				return !getUpdate().isEmpty();
			case WFSPackage.TRANSACTION_TYPE__DELETE:
				return !getDelete().isEmpty();
			case WFSPackage.TRANSACTION_TYPE__NATIVE:
				return !getNative().isEmpty();
			case WFSPackage.TRANSACTION_TYPE__RELEASE_ACTION:
				return isSetReleaseAction();
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
		result.append(" (lockId: ");
		result.append(lockId);
		result.append(", group: ");
		result.append(group);
		result.append(", releaseAction: ");
		if (releaseActionESet) result.append(releaseAction); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //TransactionTypeImpl
