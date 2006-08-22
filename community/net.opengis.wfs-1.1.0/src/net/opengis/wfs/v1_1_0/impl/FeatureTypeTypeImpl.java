/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.v1_1_0.impl;

import java.util.Collection;

import net.opengis.ows.v1_0_0.KeywordsType;
import net.opengis.ows.v1_0_0.WGS84BoundingBoxType;

import net.opengis.wfs.v1_1_0.FeatureTypeType;
import net.opengis.wfs.v1_1_0.MetadataURLType;
import net.opengis.wfs.v1_1_0.NoSRSType;
import net.opengis.wfs.v1_1_0.OperationsType;
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

import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature Type Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.FeatureTypeTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.FeatureTypeTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.FeatureTypeTypeImpl#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.FeatureTypeTypeImpl#getKeywords <em>Keywords</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.FeatureTypeTypeImpl#getDefaultSRS <em>Default SRS</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.FeatureTypeTypeImpl#getOtherSRS <em>Other SRS</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.FeatureTypeTypeImpl#getNoSRS <em>No SRS</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.FeatureTypeTypeImpl#getOperations <em>Operations</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.FeatureTypeTypeImpl#getOutputFormats <em>Output Formats</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.FeatureTypeTypeImpl#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.impl.FeatureTypeTypeImpl#getMetadataURL <em>Metadata URL</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FeatureTypeTypeImpl extends EObjectImpl implements FeatureTypeType {
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
	 * The default value of the '{@link #getDefaultSRS() <em>Default SRS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultSRS()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFAULT_SRS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefaultSRS() <em>Default SRS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultSRS()
	 * @generated
	 * @ordered
	 */
	protected String defaultSRS = DEFAULT_SRS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getOtherSRS() <em>Other SRS</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOtherSRS()
	 * @generated
	 * @ordered
	 */
	protected EList otherSRS = null;

	/**
	 * The cached value of the '{@link #getNoSRS() <em>No SRS</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNoSRS()
	 * @generated
	 * @ordered
	 */
	protected NoSRSType noSRS = null;

	/**
	 * The cached value of the '{@link #getOperations() <em>Operations</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperations()
	 * @generated
	 * @ordered
	 */
	protected OperationsType operations = null;

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
	 * The cached value of the '{@link #getWGS84BoundingBox() <em>WGS84 Bounding Box</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWGS84BoundingBox()
	 * @generated
	 * @ordered
	 */
	protected EList wGS84BoundingBox = null;

	/**
	 * The cached value of the '{@link #getMetadataURL() <em>Metadata URL</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetadataURL()
	 * @generated
	 * @ordered
	 */
	protected EList metadataURL = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FeatureTypeTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return WFSPackage.eINSTANCE.getFeatureTypeType();
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
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.FEATURE_TYPE_TYPE__NAME, oldName, name));
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
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.FEATURE_TYPE_TYPE__TITLE, oldTitle, title));
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
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.FEATURE_TYPE_TYPE__ABSTRACT, oldAbstract, abstract_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getKeywords() {
		if (keywords == null) {
			keywords = new EObjectContainmentEList(KeywordsType.class, this, WFSPackage.FEATURE_TYPE_TYPE__KEYWORDS);
		}
		return keywords;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDefaultSRS() {
		return defaultSRS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultSRS(String newDefaultSRS) {
		String oldDefaultSRS = defaultSRS;
		defaultSRS = newDefaultSRS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.FEATURE_TYPE_TYPE__DEFAULT_SRS, oldDefaultSRS, defaultSRS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getOtherSRS() {
		if (otherSRS == null) {
			otherSRS = new EDataTypeEList(String.class, this, WFSPackage.FEATURE_TYPE_TYPE__OTHER_SRS);
		}
		return otherSRS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NoSRSType getNoSRS() {
		return noSRS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetNoSRS(NoSRSType newNoSRS, NotificationChain msgs) {
		NoSRSType oldNoSRS = noSRS;
		noSRS = newNoSRS;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WFSPackage.FEATURE_TYPE_TYPE__NO_SRS, oldNoSRS, newNoSRS);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNoSRS(NoSRSType newNoSRS) {
		if (newNoSRS != noSRS) {
			NotificationChain msgs = null;
			if (noSRS != null)
				msgs = ((InternalEObject)noSRS).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WFSPackage.FEATURE_TYPE_TYPE__NO_SRS, null, msgs);
			if (newNoSRS != null)
				msgs = ((InternalEObject)newNoSRS).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WFSPackage.FEATURE_TYPE_TYPE__NO_SRS, null, msgs);
			msgs = basicSetNoSRS(newNoSRS, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.FEATURE_TYPE_TYPE__NO_SRS, newNoSRS, newNoSRS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationsType getOperations() {
		return operations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOperations(OperationsType newOperations, NotificationChain msgs) {
		OperationsType oldOperations = operations;
		operations = newOperations;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WFSPackage.FEATURE_TYPE_TYPE__OPERATIONS, oldOperations, newOperations);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOperations(OperationsType newOperations) {
		if (newOperations != operations) {
			NotificationChain msgs = null;
			if (operations != null)
				msgs = ((InternalEObject)operations).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WFSPackage.FEATURE_TYPE_TYPE__OPERATIONS, null, msgs);
			if (newOperations != null)
				msgs = ((InternalEObject)newOperations).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WFSPackage.FEATURE_TYPE_TYPE__OPERATIONS, null, msgs);
			msgs = basicSetOperations(newOperations, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.FEATURE_TYPE_TYPE__OPERATIONS, newOperations, newOperations));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WFSPackage.FEATURE_TYPE_TYPE__OUTPUT_FORMATS, oldOutputFormats, newOutputFormats);
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
				msgs = ((InternalEObject)outputFormats).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WFSPackage.FEATURE_TYPE_TYPE__OUTPUT_FORMATS, null, msgs);
			if (newOutputFormats != null)
				msgs = ((InternalEObject)newOutputFormats).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WFSPackage.FEATURE_TYPE_TYPE__OUTPUT_FORMATS, null, msgs);
			msgs = basicSetOutputFormats(newOutputFormats, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WFSPackage.FEATURE_TYPE_TYPE__OUTPUT_FORMATS, newOutputFormats, newOutputFormats));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getWGS84BoundingBox() {
		if (wGS84BoundingBox == null) {
			wGS84BoundingBox = new EObjectContainmentEList(WGS84BoundingBoxType.class, this, WFSPackage.FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX);
		}
		return wGS84BoundingBox;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getMetadataURL() {
		if (metadataURL == null) {
			metadataURL = new EObjectContainmentEList(MetadataURLType.class, this, WFSPackage.FEATURE_TYPE_TYPE__METADATA_URL);
		}
		return metadataURL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case WFSPackage.FEATURE_TYPE_TYPE__KEYWORDS:
					return ((InternalEList)getKeywords()).basicRemove(otherEnd, msgs);
				case WFSPackage.FEATURE_TYPE_TYPE__NO_SRS:
					return basicSetNoSRS(null, msgs);
				case WFSPackage.FEATURE_TYPE_TYPE__OPERATIONS:
					return basicSetOperations(null, msgs);
				case WFSPackage.FEATURE_TYPE_TYPE__OUTPUT_FORMATS:
					return basicSetOutputFormats(null, msgs);
				case WFSPackage.FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX:
					return ((InternalEList)getWGS84BoundingBox()).basicRemove(otherEnd, msgs);
				case WFSPackage.FEATURE_TYPE_TYPE__METADATA_URL:
					return ((InternalEList)getMetadataURL()).basicRemove(otherEnd, msgs);
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
			case WFSPackage.FEATURE_TYPE_TYPE__NAME:
				return getName();
			case WFSPackage.FEATURE_TYPE_TYPE__TITLE:
				return getTitle();
			case WFSPackage.FEATURE_TYPE_TYPE__ABSTRACT:
				return getAbstract();
			case WFSPackage.FEATURE_TYPE_TYPE__KEYWORDS:
				return getKeywords();
			case WFSPackage.FEATURE_TYPE_TYPE__DEFAULT_SRS:
				return getDefaultSRS();
			case WFSPackage.FEATURE_TYPE_TYPE__OTHER_SRS:
				return getOtherSRS();
			case WFSPackage.FEATURE_TYPE_TYPE__NO_SRS:
				return getNoSRS();
			case WFSPackage.FEATURE_TYPE_TYPE__OPERATIONS:
				return getOperations();
			case WFSPackage.FEATURE_TYPE_TYPE__OUTPUT_FORMATS:
				return getOutputFormats();
			case WFSPackage.FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX:
				return getWGS84BoundingBox();
			case WFSPackage.FEATURE_TYPE_TYPE__METADATA_URL:
				return getMetadataURL();
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
			case WFSPackage.FEATURE_TYPE_TYPE__NAME:
				setName((Object)newValue);
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__TITLE:
				setTitle((String)newValue);
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__ABSTRACT:
				setAbstract((String)newValue);
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__KEYWORDS:
				getKeywords().clear();
				getKeywords().addAll((Collection)newValue);
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__DEFAULT_SRS:
				setDefaultSRS((String)newValue);
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__OTHER_SRS:
				getOtherSRS().clear();
				getOtherSRS().addAll((Collection)newValue);
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__NO_SRS:
				setNoSRS((NoSRSType)newValue);
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__OPERATIONS:
				setOperations((OperationsType)newValue);
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__OUTPUT_FORMATS:
				setOutputFormats((OutputFormatListType)newValue);
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX:
				getWGS84BoundingBox().clear();
				getWGS84BoundingBox().addAll((Collection)newValue);
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__METADATA_URL:
				getMetadataURL().clear();
				getMetadataURL().addAll((Collection)newValue);
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
			case WFSPackage.FEATURE_TYPE_TYPE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__TITLE:
				setTitle(TITLE_EDEFAULT);
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__ABSTRACT:
				setAbstract(ABSTRACT_EDEFAULT);
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__KEYWORDS:
				getKeywords().clear();
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__DEFAULT_SRS:
				setDefaultSRS(DEFAULT_SRS_EDEFAULT);
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__OTHER_SRS:
				getOtherSRS().clear();
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__NO_SRS:
				setNoSRS((NoSRSType)null);
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__OPERATIONS:
				setOperations((OperationsType)null);
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__OUTPUT_FORMATS:
				setOutputFormats((OutputFormatListType)null);
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX:
				getWGS84BoundingBox().clear();
				return;
			case WFSPackage.FEATURE_TYPE_TYPE__METADATA_URL:
				getMetadataURL().clear();
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
			case WFSPackage.FEATURE_TYPE_TYPE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case WFSPackage.FEATURE_TYPE_TYPE__TITLE:
				return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
			case WFSPackage.FEATURE_TYPE_TYPE__ABSTRACT:
				return ABSTRACT_EDEFAULT == null ? abstract_ != null : !ABSTRACT_EDEFAULT.equals(abstract_);
			case WFSPackage.FEATURE_TYPE_TYPE__KEYWORDS:
				return keywords != null && !keywords.isEmpty();
			case WFSPackage.FEATURE_TYPE_TYPE__DEFAULT_SRS:
				return DEFAULT_SRS_EDEFAULT == null ? defaultSRS != null : !DEFAULT_SRS_EDEFAULT.equals(defaultSRS);
			case WFSPackage.FEATURE_TYPE_TYPE__OTHER_SRS:
				return otherSRS != null && !otherSRS.isEmpty();
			case WFSPackage.FEATURE_TYPE_TYPE__NO_SRS:
				return noSRS != null;
			case WFSPackage.FEATURE_TYPE_TYPE__OPERATIONS:
				return operations != null;
			case WFSPackage.FEATURE_TYPE_TYPE__OUTPUT_FORMATS:
				return outputFormats != null;
			case WFSPackage.FEATURE_TYPE_TYPE__WGS84_BOUNDING_BOX:
				return wGS84BoundingBox != null && !wGS84BoundingBox.isEmpty();
			case WFSPackage.FEATURE_TYPE_TYPE__METADATA_URL:
				return metadataURL != null && !metadataURL.isEmpty();
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
		result.append(", defaultSRS: ");
		result.append(defaultSRS);
		result.append(", otherSRS: ");
		result.append(otherSRS);
		result.append(')');
		return result.toString();
	}

} //FeatureTypeTypeImpl
