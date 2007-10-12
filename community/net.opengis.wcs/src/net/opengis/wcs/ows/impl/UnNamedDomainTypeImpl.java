/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.ows.impl;

import java.util.Collection;

import net.opengis.wcs.ows.AllowedValuesType;
import net.opengis.wcs.ows.AnyValueType;
import net.opengis.wcs.ows.DomainMetadataType;
import net.opengis.wcs.ows.NoValuesType;
import net.opengis.wcs.ows.UnNamedDomainType;
import net.opengis.wcs.ows.ValueType;
import net.opengis.wcs.ows.ValuesReferenceType;
import net.opengis.wcs.ows.owcsPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Un Named Domain Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs.ows.impl.UnNamedDomainTypeImpl#getAllowedValues <em>Allowed Values</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.UnNamedDomainTypeImpl#getAnyValue <em>Any Value</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.UnNamedDomainTypeImpl#getNoValues <em>No Values</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.UnNamedDomainTypeImpl#getValuesReference <em>Values Reference</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.UnNamedDomainTypeImpl#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.UnNamedDomainTypeImpl#getMeaning <em>Meaning</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.UnNamedDomainTypeImpl#getDataType <em>Data Type</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.UnNamedDomainTypeImpl#getUOM <em>UOM</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.UnNamedDomainTypeImpl#getReferenceSystem <em>Reference System</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.UnNamedDomainTypeImpl#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UnNamedDomainTypeImpl extends EObjectImpl implements UnNamedDomainType {
    /**
     * The cached value of the '{@link #getAllowedValues() <em>Allowed Values</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAllowedValues()
     * @generated
     * @ordered
     */
    protected AllowedValuesType allowedValues;

    /**
     * The cached value of the '{@link #getAnyValue() <em>Any Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAnyValue()
     * @generated
     * @ordered
     */
    protected AnyValueType anyValue;

    /**
     * The cached value of the '{@link #getNoValues() <em>No Values</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNoValues()
     * @generated
     * @ordered
     */
    protected NoValuesType noValues;

    /**
     * The cached value of the '{@link #getValuesReference() <em>Values Reference</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValuesReference()
     * @generated
     * @ordered
     */
    protected ValuesReferenceType valuesReference;

    /**
     * The cached value of the '{@link #getDefaultValue() <em>Default Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefaultValue()
     * @generated
     * @ordered
     */
    protected ValueType defaultValue;

    /**
     * The cached value of the '{@link #getMeaning() <em>Meaning</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMeaning()
     * @generated
     * @ordered
     */
    protected DomainMetadataType meaning;

    /**
     * The cached value of the '{@link #getDataType() <em>Data Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDataType()
     * @generated
     * @ordered
     */
    protected DomainMetadataType dataType;

    /**
     * The cached value of the '{@link #getUOM() <em>UOM</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUOM()
     * @generated
     * @ordered
     */
    protected DomainMetadataType uOM;

    /**
     * The cached value of the '{@link #getReferenceSystem() <em>Reference System</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReferenceSystem()
     * @generated
     * @ordered
     */
    protected DomainMetadataType referenceSystem;

    /**
     * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMetadata()
     * @generated
     * @ordered
     */
    protected EList metadata;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected UnNamedDomainTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return owcsPackage.Literals.UN_NAMED_DOMAIN_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllowedValuesType getAllowedValues() {
        return allowedValues;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAllowedValues(AllowedValuesType newAllowedValues, NotificationChain msgs) {
        AllowedValuesType oldAllowedValues = allowedValues;
        allowedValues = newAllowedValues;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, owcsPackage.UN_NAMED_DOMAIN_TYPE__ALLOWED_VALUES, oldAllowedValues, newAllowedValues);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAllowedValues(AllowedValuesType newAllowedValues) {
        if (newAllowedValues != allowedValues) {
            NotificationChain msgs = null;
            if (allowedValues != null)
                msgs = ((InternalEObject)allowedValues).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - owcsPackage.UN_NAMED_DOMAIN_TYPE__ALLOWED_VALUES, null, msgs);
            if (newAllowedValues != null)
                msgs = ((InternalEObject)newAllowedValues).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - owcsPackage.UN_NAMED_DOMAIN_TYPE__ALLOWED_VALUES, null, msgs);
            msgs = basicSetAllowedValues(newAllowedValues, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, owcsPackage.UN_NAMED_DOMAIN_TYPE__ALLOWED_VALUES, newAllowedValues, newAllowedValues));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AnyValueType getAnyValue() {
        return anyValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAnyValue(AnyValueType newAnyValue, NotificationChain msgs) {
        AnyValueType oldAnyValue = anyValue;
        anyValue = newAnyValue;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, owcsPackage.UN_NAMED_DOMAIN_TYPE__ANY_VALUE, oldAnyValue, newAnyValue);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAnyValue(AnyValueType newAnyValue) {
        if (newAnyValue != anyValue) {
            NotificationChain msgs = null;
            if (anyValue != null)
                msgs = ((InternalEObject)anyValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - owcsPackage.UN_NAMED_DOMAIN_TYPE__ANY_VALUE, null, msgs);
            if (newAnyValue != null)
                msgs = ((InternalEObject)newAnyValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - owcsPackage.UN_NAMED_DOMAIN_TYPE__ANY_VALUE, null, msgs);
            msgs = basicSetAnyValue(newAnyValue, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, owcsPackage.UN_NAMED_DOMAIN_TYPE__ANY_VALUE, newAnyValue, newAnyValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NoValuesType getNoValues() {
        return noValues;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetNoValues(NoValuesType newNoValues, NotificationChain msgs) {
        NoValuesType oldNoValues = noValues;
        noValues = newNoValues;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, owcsPackage.UN_NAMED_DOMAIN_TYPE__NO_VALUES, oldNoValues, newNoValues);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNoValues(NoValuesType newNoValues) {
        if (newNoValues != noValues) {
            NotificationChain msgs = null;
            if (noValues != null)
                msgs = ((InternalEObject)noValues).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - owcsPackage.UN_NAMED_DOMAIN_TYPE__NO_VALUES, null, msgs);
            if (newNoValues != null)
                msgs = ((InternalEObject)newNoValues).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - owcsPackage.UN_NAMED_DOMAIN_TYPE__NO_VALUES, null, msgs);
            msgs = basicSetNoValues(newNoValues, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, owcsPackage.UN_NAMED_DOMAIN_TYPE__NO_VALUES, newNoValues, newNoValues));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValuesReferenceType getValuesReference() {
        return valuesReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValuesReference(ValuesReferenceType newValuesReference, NotificationChain msgs) {
        ValuesReferenceType oldValuesReference = valuesReference;
        valuesReference = newValuesReference;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, owcsPackage.UN_NAMED_DOMAIN_TYPE__VALUES_REFERENCE, oldValuesReference, newValuesReference);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValuesReference(ValuesReferenceType newValuesReference) {
        if (newValuesReference != valuesReference) {
            NotificationChain msgs = null;
            if (valuesReference != null)
                msgs = ((InternalEObject)valuesReference).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - owcsPackage.UN_NAMED_DOMAIN_TYPE__VALUES_REFERENCE, null, msgs);
            if (newValuesReference != null)
                msgs = ((InternalEObject)newValuesReference).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - owcsPackage.UN_NAMED_DOMAIN_TYPE__VALUES_REFERENCE, null, msgs);
            msgs = basicSetValuesReference(newValuesReference, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, owcsPackage.UN_NAMED_DOMAIN_TYPE__VALUES_REFERENCE, newValuesReference, newValuesReference));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueType getDefaultValue() {
        return defaultValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDefaultValue(ValueType newDefaultValue, NotificationChain msgs) {
        ValueType oldDefaultValue = defaultValue;
        defaultValue = newDefaultValue;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, owcsPackage.UN_NAMED_DOMAIN_TYPE__DEFAULT_VALUE, oldDefaultValue, newDefaultValue);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefaultValue(ValueType newDefaultValue) {
        if (newDefaultValue != defaultValue) {
            NotificationChain msgs = null;
            if (defaultValue != null)
                msgs = ((InternalEObject)defaultValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - owcsPackage.UN_NAMED_DOMAIN_TYPE__DEFAULT_VALUE, null, msgs);
            if (newDefaultValue != null)
                msgs = ((InternalEObject)newDefaultValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - owcsPackage.UN_NAMED_DOMAIN_TYPE__DEFAULT_VALUE, null, msgs);
            msgs = basicSetDefaultValue(newDefaultValue, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, owcsPackage.UN_NAMED_DOMAIN_TYPE__DEFAULT_VALUE, newDefaultValue, newDefaultValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainMetadataType getMeaning() {
        return meaning;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMeaning(DomainMetadataType newMeaning, NotificationChain msgs) {
        DomainMetadataType oldMeaning = meaning;
        meaning = newMeaning;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, owcsPackage.UN_NAMED_DOMAIN_TYPE__MEANING, oldMeaning, newMeaning);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMeaning(DomainMetadataType newMeaning) {
        if (newMeaning != meaning) {
            NotificationChain msgs = null;
            if (meaning != null)
                msgs = ((InternalEObject)meaning).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - owcsPackage.UN_NAMED_DOMAIN_TYPE__MEANING, null, msgs);
            if (newMeaning != null)
                msgs = ((InternalEObject)newMeaning).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - owcsPackage.UN_NAMED_DOMAIN_TYPE__MEANING, null, msgs);
            msgs = basicSetMeaning(newMeaning, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, owcsPackage.UN_NAMED_DOMAIN_TYPE__MEANING, newMeaning, newMeaning));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainMetadataType getDataType() {
        return dataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDataType(DomainMetadataType newDataType, NotificationChain msgs) {
        DomainMetadataType oldDataType = dataType;
        dataType = newDataType;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, owcsPackage.UN_NAMED_DOMAIN_TYPE__DATA_TYPE, oldDataType, newDataType);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDataType(DomainMetadataType newDataType) {
        if (newDataType != dataType) {
            NotificationChain msgs = null;
            if (dataType != null)
                msgs = ((InternalEObject)dataType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - owcsPackage.UN_NAMED_DOMAIN_TYPE__DATA_TYPE, null, msgs);
            if (newDataType != null)
                msgs = ((InternalEObject)newDataType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - owcsPackage.UN_NAMED_DOMAIN_TYPE__DATA_TYPE, null, msgs);
            msgs = basicSetDataType(newDataType, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, owcsPackage.UN_NAMED_DOMAIN_TYPE__DATA_TYPE, newDataType, newDataType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainMetadataType getUOM() {
        return uOM;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUOM(DomainMetadataType newUOM, NotificationChain msgs) {
        DomainMetadataType oldUOM = uOM;
        uOM = newUOM;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, owcsPackage.UN_NAMED_DOMAIN_TYPE__UOM, oldUOM, newUOM);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUOM(DomainMetadataType newUOM) {
        if (newUOM != uOM) {
            NotificationChain msgs = null;
            if (uOM != null)
                msgs = ((InternalEObject)uOM).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - owcsPackage.UN_NAMED_DOMAIN_TYPE__UOM, null, msgs);
            if (newUOM != null)
                msgs = ((InternalEObject)newUOM).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - owcsPackage.UN_NAMED_DOMAIN_TYPE__UOM, null, msgs);
            msgs = basicSetUOM(newUOM, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, owcsPackage.UN_NAMED_DOMAIN_TYPE__UOM, newUOM, newUOM));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainMetadataType getReferenceSystem() {
        return referenceSystem;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetReferenceSystem(DomainMetadataType newReferenceSystem, NotificationChain msgs) {
        DomainMetadataType oldReferenceSystem = referenceSystem;
        referenceSystem = newReferenceSystem;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, owcsPackage.UN_NAMED_DOMAIN_TYPE__REFERENCE_SYSTEM, oldReferenceSystem, newReferenceSystem);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReferenceSystem(DomainMetadataType newReferenceSystem) {
        if (newReferenceSystem != referenceSystem) {
            NotificationChain msgs = null;
            if (referenceSystem != null)
                msgs = ((InternalEObject)referenceSystem).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - owcsPackage.UN_NAMED_DOMAIN_TYPE__REFERENCE_SYSTEM, null, msgs);
            if (newReferenceSystem != null)
                msgs = ((InternalEObject)newReferenceSystem).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - owcsPackage.UN_NAMED_DOMAIN_TYPE__REFERENCE_SYSTEM, null, msgs);
            msgs = basicSetReferenceSystem(newReferenceSystem, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, owcsPackage.UN_NAMED_DOMAIN_TYPE__REFERENCE_SYSTEM, newReferenceSystem, newReferenceSystem));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getMetadata() {
        if (metadata == null) {
            metadata = new EDataTypeEList(Object.class, this, owcsPackage.UN_NAMED_DOMAIN_TYPE__METADATA);
        }
        return metadata;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__ALLOWED_VALUES:
                return basicSetAllowedValues(null, msgs);
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__ANY_VALUE:
                return basicSetAnyValue(null, msgs);
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__NO_VALUES:
                return basicSetNoValues(null, msgs);
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__VALUES_REFERENCE:
                return basicSetValuesReference(null, msgs);
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__DEFAULT_VALUE:
                return basicSetDefaultValue(null, msgs);
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__MEANING:
                return basicSetMeaning(null, msgs);
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__DATA_TYPE:
                return basicSetDataType(null, msgs);
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__UOM:
                return basicSetUOM(null, msgs);
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__REFERENCE_SYSTEM:
                return basicSetReferenceSystem(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__ALLOWED_VALUES:
                return getAllowedValues();
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__ANY_VALUE:
                return getAnyValue();
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__NO_VALUES:
                return getNoValues();
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__VALUES_REFERENCE:
                return getValuesReference();
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__DEFAULT_VALUE:
                return getDefaultValue();
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__MEANING:
                return getMeaning();
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__DATA_TYPE:
                return getDataType();
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__UOM:
                return getUOM();
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__REFERENCE_SYSTEM:
                return getReferenceSystem();
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__METADATA:
                return getMetadata();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__ALLOWED_VALUES:
                setAllowedValues((AllowedValuesType)newValue);
                return;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__ANY_VALUE:
                setAnyValue((AnyValueType)newValue);
                return;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__NO_VALUES:
                setNoValues((NoValuesType)newValue);
                return;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__VALUES_REFERENCE:
                setValuesReference((ValuesReferenceType)newValue);
                return;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__DEFAULT_VALUE:
                setDefaultValue((ValueType)newValue);
                return;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__MEANING:
                setMeaning((DomainMetadataType)newValue);
                return;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__DATA_TYPE:
                setDataType((DomainMetadataType)newValue);
                return;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__UOM:
                setUOM((DomainMetadataType)newValue);
                return;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__REFERENCE_SYSTEM:
                setReferenceSystem((DomainMetadataType)newValue);
                return;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__METADATA:
                getMetadata().clear();
                getMetadata().addAll((Collection)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(int featureID) {
        switch (featureID) {
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__ALLOWED_VALUES:
                setAllowedValues((AllowedValuesType)null);
                return;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__ANY_VALUE:
                setAnyValue((AnyValueType)null);
                return;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__NO_VALUES:
                setNoValues((NoValuesType)null);
                return;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__VALUES_REFERENCE:
                setValuesReference((ValuesReferenceType)null);
                return;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__DEFAULT_VALUE:
                setDefaultValue((ValueType)null);
                return;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__MEANING:
                setMeaning((DomainMetadataType)null);
                return;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__DATA_TYPE:
                setDataType((DomainMetadataType)null);
                return;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__UOM:
                setUOM((DomainMetadataType)null);
                return;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__REFERENCE_SYSTEM:
                setReferenceSystem((DomainMetadataType)null);
                return;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__METADATA:
                getMetadata().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__ALLOWED_VALUES:
                return allowedValues != null;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__ANY_VALUE:
                return anyValue != null;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__NO_VALUES:
                return noValues != null;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__VALUES_REFERENCE:
                return valuesReference != null;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__DEFAULT_VALUE:
                return defaultValue != null;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__MEANING:
                return meaning != null;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__DATA_TYPE:
                return dataType != null;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__UOM:
                return uOM != null;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__REFERENCE_SYSTEM:
                return referenceSystem != null;
            case owcsPackage.UN_NAMED_DOMAIN_TYPE__METADATA:
                return metadata != null && !metadata.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (metadata: ");
        result.append(metadata);
        result.append(')');
        return result.toString();
    }

} //UnNamedDomainTypeImpl
