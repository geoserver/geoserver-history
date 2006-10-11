/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.util.Collection;

import net.opengis.ows.v1_0_0.AcceptFormatsType;
import net.opengis.ows.v1_0_0.AcceptVersionsType;
import net.opengis.ows.v1_0_0.SectionsType;

import net.opengis.ows.v1_0_0.impl.GetCapabilitiesTypeImpl;

import net.opengis.wfs.AllSomeType;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.WfsPackage;

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
public class TransactionTypeImpl extends GetCapabilitiesTypeImpl implements TransactionType {
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
		return WfsPackage.eINSTANCE.getTransactionType();
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
			eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.TRANSACTION_TYPE__LOCK_ID, oldLockId, lockId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getGroup() {
		if (group == null) {
			group = new BasicFeatureMap(this, WfsPackage.TRANSACTION_TYPE__GROUP);
		}
		return group;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getInsert() {
		return ((FeatureMap)getGroup()).list(WfsPackage.eINSTANCE.getTransactionType_Insert());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getUpdate() {
		return ((FeatureMap)getGroup()).list(WfsPackage.eINSTANCE.getTransactionType_Update());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getDelete() {
		return ((FeatureMap)getGroup()).list(WfsPackage.eINSTANCE.getTransactionType_Delete());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getNative() {
		return ((FeatureMap)getGroup()).list(WfsPackage.eINSTANCE.getTransactionType_Native());
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
			eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.TRANSACTION_TYPE__RELEASE_ACTION, oldReleaseAction, releaseAction, !oldReleaseActionESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, WfsPackage.TRANSACTION_TYPE__RELEASE_ACTION, oldReleaseAction, RELEASE_ACTION_EDEFAULT, oldReleaseActionESet));
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
				case WfsPackage.TRANSACTION_TYPE__ACCEPT_VERSIONS:
					return basicSetAcceptVersions(null, msgs);
				case WfsPackage.TRANSACTION_TYPE__SECTIONS:
					return basicSetSections(null, msgs);
				case WfsPackage.TRANSACTION_TYPE__ACCEPT_FORMATS:
					return basicSetAcceptFormats(null, msgs);
				case WfsPackage.TRANSACTION_TYPE__GROUP:
					return ((InternalEList)getGroup()).basicRemove(otherEnd, msgs);
				case WfsPackage.TRANSACTION_TYPE__INSERT:
					return ((InternalEList)getInsert()).basicRemove(otherEnd, msgs);
				case WfsPackage.TRANSACTION_TYPE__UPDATE:
					return ((InternalEList)getUpdate()).basicRemove(otherEnd, msgs);
				case WfsPackage.TRANSACTION_TYPE__DELETE:
					return ((InternalEList)getDelete()).basicRemove(otherEnd, msgs);
				case WfsPackage.TRANSACTION_TYPE__NATIVE:
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
			case WfsPackage.TRANSACTION_TYPE__ACCEPT_VERSIONS:
				return getAcceptVersions();
			case WfsPackage.TRANSACTION_TYPE__SECTIONS:
				return getSections();
			case WfsPackage.TRANSACTION_TYPE__ACCEPT_FORMATS:
				return getAcceptFormats();
			case WfsPackage.TRANSACTION_TYPE__UPDATE_SEQUENCE:
				return getUpdateSequence();
			case WfsPackage.TRANSACTION_TYPE__LOCK_ID:
				return getLockId();
			case WfsPackage.TRANSACTION_TYPE__GROUP:
				return getGroup();
			case WfsPackage.TRANSACTION_TYPE__INSERT:
				return getInsert();
			case WfsPackage.TRANSACTION_TYPE__UPDATE:
				return getUpdate();
			case WfsPackage.TRANSACTION_TYPE__DELETE:
				return getDelete();
			case WfsPackage.TRANSACTION_TYPE__NATIVE:
				return getNative();
			case WfsPackage.TRANSACTION_TYPE__RELEASE_ACTION:
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
			case WfsPackage.TRANSACTION_TYPE__ACCEPT_VERSIONS:
				setAcceptVersions((AcceptVersionsType)newValue);
				return;
			case WfsPackage.TRANSACTION_TYPE__SECTIONS:
				setSections((SectionsType)newValue);
				return;
			case WfsPackage.TRANSACTION_TYPE__ACCEPT_FORMATS:
				setAcceptFormats((AcceptFormatsType)newValue);
				return;
			case WfsPackage.TRANSACTION_TYPE__UPDATE_SEQUENCE:
				setUpdateSequence((String)newValue);
				return;
			case WfsPackage.TRANSACTION_TYPE__LOCK_ID:
				setLockId((String)newValue);
				return;
			case WfsPackage.TRANSACTION_TYPE__GROUP:
				getGroup().clear();
				getGroup().addAll((Collection)newValue);
				return;
			case WfsPackage.TRANSACTION_TYPE__INSERT:
				getInsert().clear();
				getInsert().addAll((Collection)newValue);
				return;
			case WfsPackage.TRANSACTION_TYPE__UPDATE:
				getUpdate().clear();
				getUpdate().addAll((Collection)newValue);
				return;
			case WfsPackage.TRANSACTION_TYPE__DELETE:
				getDelete().clear();
				getDelete().addAll((Collection)newValue);
				return;
			case WfsPackage.TRANSACTION_TYPE__NATIVE:
				getNative().clear();
				getNative().addAll((Collection)newValue);
				return;
			case WfsPackage.TRANSACTION_TYPE__RELEASE_ACTION:
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
			case WfsPackage.TRANSACTION_TYPE__ACCEPT_VERSIONS:
				setAcceptVersions((AcceptVersionsType)null);
				return;
			case WfsPackage.TRANSACTION_TYPE__SECTIONS:
				setSections((SectionsType)null);
				return;
			case WfsPackage.TRANSACTION_TYPE__ACCEPT_FORMATS:
				setAcceptFormats((AcceptFormatsType)null);
				return;
			case WfsPackage.TRANSACTION_TYPE__UPDATE_SEQUENCE:
				setUpdateSequence(UPDATE_SEQUENCE_EDEFAULT);
				return;
			case WfsPackage.TRANSACTION_TYPE__LOCK_ID:
				setLockId(LOCK_ID_EDEFAULT);
				return;
			case WfsPackage.TRANSACTION_TYPE__GROUP:
				getGroup().clear();
				return;
			case WfsPackage.TRANSACTION_TYPE__INSERT:
				getInsert().clear();
				return;
			case WfsPackage.TRANSACTION_TYPE__UPDATE:
				getUpdate().clear();
				return;
			case WfsPackage.TRANSACTION_TYPE__DELETE:
				getDelete().clear();
				return;
			case WfsPackage.TRANSACTION_TYPE__NATIVE:
				getNative().clear();
				return;
			case WfsPackage.TRANSACTION_TYPE__RELEASE_ACTION:
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
			case WfsPackage.TRANSACTION_TYPE__ACCEPT_VERSIONS:
				return acceptVersions != null;
			case WfsPackage.TRANSACTION_TYPE__SECTIONS:
				return sections != null;
			case WfsPackage.TRANSACTION_TYPE__ACCEPT_FORMATS:
				return acceptFormats != null;
			case WfsPackage.TRANSACTION_TYPE__UPDATE_SEQUENCE:
				return UPDATE_SEQUENCE_EDEFAULT == null ? updateSequence != null : !UPDATE_SEQUENCE_EDEFAULT.equals(updateSequence);
			case WfsPackage.TRANSACTION_TYPE__LOCK_ID:
				return LOCK_ID_EDEFAULT == null ? lockId != null : !LOCK_ID_EDEFAULT.equals(lockId);
			case WfsPackage.TRANSACTION_TYPE__GROUP:
				return group != null && !group.isEmpty();
			case WfsPackage.TRANSACTION_TYPE__INSERT:
				return !getInsert().isEmpty();
			case WfsPackage.TRANSACTION_TYPE__UPDATE:
				return !getUpdate().isEmpty();
			case WfsPackage.TRANSACTION_TYPE__DELETE:
				return !getDelete().isEmpty();
			case WfsPackage.TRANSACTION_TYPE__NATIVE:
				return !getNative().isEmpty();
			case WfsPackage.TRANSACTION_TYPE__RELEASE_ACTION:
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
