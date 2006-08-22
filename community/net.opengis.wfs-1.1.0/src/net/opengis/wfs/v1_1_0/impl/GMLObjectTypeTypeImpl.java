/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.v1_1_0.impl;

import java.util.Collection;

import net.opengis.ows.v1_0_0.KeywordsType;

import net.opengis.wfs.v1_1_0.GMLObjectTypeType;
import net.opengis.wfs.v1_1_0.OutputFormatListType;
import net.opengis.wfs.v1_1_0.WFSPackage;

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
 * An implementation of the model object '<em><b>GML Object Type Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.GMLObjectTypeTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.GMLObjectTypeTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.GMLObjectTypeTypeImpl#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.GMLObjectTypeTypeImpl#getKeywords <em>Keywords</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.GMLObjectTypeTypeImpl#getOutputFormats <em>Output Formats</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GMLObjectTypeTypeImpl extends EObjectImpl implements GMLObjectTypeType {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final Object NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected Object name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
	protected static final String TITLE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
	protected String title = TITLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getAbstract() <em>Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAbstract()
	 * @generated
	 * @ordered
	 */
	protected static final String ABSTRACT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAbstract() <em>Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAbstract()
	 * @generated
	 * @ordered
	 */
	protected String abstract_ = ABSTRACT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getKeywords() <em>Keywords</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKeywords()
	 * @generated
	 * @ordered
	 */
	protected EList keywords = null;

	/**
	 * The cached value of the '{@link #getOutputFormats() <em>Output Formats</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutputFormats()
	 * @generated
	 * @ordered
	 */
	protected OutputFormatListType outputFormats = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GMLObjectTypeTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return WFSPackage.eINSTANCE.getGMLObjectTypeType();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(Object newName) {
		Object oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.GML_OBJECT_TYPE_TYPE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTitle(String newTitle) {
		String oldTitle = title;
		title = newTitle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.GML_OBJECT_TYPE_TYPE__TITLE, oldTitle, title));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAbstract() {
		return abstract_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAbstract(String newAbstract) {
		String oldAbstract = abstract_;
		abstract_ = newAbstract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.GML_OBJECT_TYPE_TYPE__ABSTRACT, oldAbstract, abstract_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getKeywords() {
		if (keywords == null) {
			keywords = new EObjectContainmentEList(KeywordsType.class, this, WFSPackage.GML_OBJECT_TYPE_TYPE__KEYWORDS);
		}
		return keywords;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OutputFormatListType getOutputFormats() {
		return outputFormats;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOutputFormats(OutputFormatListType newOutputFormats, NotificationChain msgs) {
		OutputFormatListType oldOutputFormats = outputFormats;
		outputFormats = newOutputFormats;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WFSPackage.GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS, oldOutputFormats, newOutputFormats);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOutputFormats(OutputFormatListType newOutputFormats) {
		if (newOutputFormats != outputFormats) {
			NotificationChain msgs = null;
			if (outputFormats != null)
				msgs = ((InternalEObject)outputFormats).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WFSPackage.GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS, null, msgs);
			if (newOutputFormats != null)
				msgs = ((InternalEObject)newOutputFormats).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WFSPackage.GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS, null, msgs);
			msgs = basicSetOutputFormats(newOutputFormats, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS, newOutputFormats, newOutputFormats));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case WFSPackage.GML_OBJECT_TYPE_TYPE__KEYWORDS:
					return ((InternalEList)getKeywords()).basicRemove(otherEnd, msgs);
				case WFSPackage.GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS:
					return basicSetOutputFormats(null, msgs);
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
			case WFSPackage.GML_OBJECT_TYPE_TYPE__NAME:
				return getName();
			case WFSPackage.GML_OBJECT_TYPE_TYPE__TITLE:
				return getTitle();
			case WFSPackage.GML_OBJECT_TYPE_TYPE__ABSTRACT:
				return getAbstract();
			case WFSPackage.GML_OBJECT_TYPE_TYPE__KEYWORDS:
				return getKeywords();
			case WFSPackage.GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS:
				return getOutputFormats();
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
			case WFSPackage.GML_OBJECT_TYPE_TYPE__NAME:
				setName((Object)newValue);
				return;
			case WFSPackage.GML_OBJECT_TYPE_TYPE__TITLE:
				setTitle((String)newValue);
				return;
			case WFSPackage.GML_OBJECT_TYPE_TYPE__ABSTRACT:
				setAbstract((String)newValue);
				return;
			case WFSPackage.GML_OBJECT_TYPE_TYPE__KEYWORDS:
				getKeywords().clear();
				getKeywords().addAll((Collection)newValue);
				return;
			case WFSPackage.GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS:
				setOutputFormats((OutputFormatListType)newValue);
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
			case WFSPackage.GML_OBJECT_TYPE_TYPE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case WFSPackage.GML_OBJECT_TYPE_TYPE__TITLE:
				setTitle(TITLE_EDEFAULT);
				return;
			case WFSPackage.GML_OBJECT_TYPE_TYPE__ABSTRACT:
				setAbstract(ABSTRACT_EDEFAULT);
				return;
			case WFSPackage.GML_OBJECT_TYPE_TYPE__KEYWORDS:
				getKeywords().clear();
				return;
			case WFSPackage.GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS:
				setOutputFormats((OutputFormatListType)null);
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
			case WFSPackage.GML_OBJECT_TYPE_TYPE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case WFSPackage.GML_OBJECT_TYPE_TYPE__TITLE:
				return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
			case WFSPackage.GML_OBJECT_TYPE_TYPE__ABSTRACT:
				return ABSTRACT_EDEFAULT == null ? abstract_ != null : !ABSTRACT_EDEFAULT.equals(abstract_);
			case WFSPackage.GML_OBJECT_TYPE_TYPE__KEYWORDS:
				return keywords != null && !keywords.isEmpty();
			case WFSPackage.GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS:
				return outputFormats != null;
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
		result.append(", title: ");
		result.append(title);
		result.append(", abstract: ");
		result.append(abstract_);
		result.append(')');
		return result.toString();
	}

} //GMLObjectTypeTypeImpl
