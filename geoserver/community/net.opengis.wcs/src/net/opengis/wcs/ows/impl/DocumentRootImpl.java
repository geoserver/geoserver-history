/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.ows.impl;

import net.opengis.wcs.ows.AbstractReferenceBaseType;
import net.opengis.wcs.ows.AllowedValuesType;
import net.opengis.wcs.ows.AnyValueType;
import net.opengis.wcs.ows.CoveragesType;
import net.opengis.wcs.ows.DCPType;
import net.opengis.wcs.ows.DocumentRoot;
import net.opengis.wcs.ows.DomainMetadataType;
import net.opengis.wcs.ows.GetCapabilitiesType;
import net.opengis.wcs.ows.HTTPType;
import net.opengis.wcs.ows.InterpolationMethodsType;
import net.opengis.wcs.ows.ManifestType;
import net.opengis.wcs.ows.NoValuesType;
import net.opengis.wcs.ows.OperationType;
import net.opengis.wcs.ows.OperationsMetadataType;
import net.opengis.wcs.ows.RangeClosureType;
import net.opengis.wcs.ows.RangeType;
import net.opengis.wcs.ows.ReferenceGroupType;
import net.opengis.wcs.ows.ReferenceType;
import net.opengis.wcs.ows.ServiceIdentificationType;
import net.opengis.wcs.ows.ServiceReferenceType;
import net.opengis.wcs.ows.ValueType;
import net.opengis.wcs.ows.ValuesReferenceType;
import net.opengis.wcs.ows.owcsPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getAbstractReferenceBase <em>Abstract Reference Base</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getAccessConstraints <em>Access Constraints</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getAllowedValues <em>Allowed Values</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getAnyValue <em>Any Value</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getAvailableCRS <em>Available CRS</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getCoverage <em>Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getReferenceGroup <em>Reference Group</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getCoverages <em>Coverages</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getManifest <em>Manifest</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getDataType <em>Data Type</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getDCP <em>DCP</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getExtendedCapabilities <em>Extended Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getFees <em>Fees</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getHTTP <em>HTTP</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getInterpolationMethods <em>Interpolation Methods</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getLanguage <em>Language</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getMaximumValue <em>Maximum Value</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getMeaning <em>Meaning</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getMinimumValue <em>Minimum Value</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getNoValues <em>No Values</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getOperation <em>Operation</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getOperationsMetadata <em>Operations Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getRange <em>Range</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getReference <em>Reference</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getReferenceSystem <em>Reference System</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getServiceIdentification <em>Service Identification</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getServiceReference <em>Service Reference</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getSpacing <em>Spacing</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getSupportedCRS <em>Supported CRS</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getUOM <em>UOM</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getValuesReference <em>Values Reference</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getRangeClosure <em>Range Closure</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.impl.DocumentRootImpl#getReference1 <em>Reference1</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DocumentRootImpl extends EObjectImpl implements DocumentRoot {
    /**
     * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMixed()
     * @generated
     * @ordered
     */
    protected FeatureMap mixed;

    /**
     * The cached value of the '{@link #getXMLNSPrefixMap() <em>XMLNS Prefix Map</em>}' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXMLNSPrefixMap()
     * @generated
     * @ordered
     */
    protected EMap xMLNSPrefixMap;

    /**
     * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema Location</em>}' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXSISchemaLocation()
     * @generated
     * @ordered
     */
    protected EMap xSISchemaLocation;

    /**
     * The default value of the '{@link #getAccessConstraints() <em>Access Constraints</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAccessConstraints()
     * @generated
     * @ordered
     */
    protected static final String ACCESS_CONSTRAINTS_EDEFAULT = null;

    /**
     * The default value of the '{@link #getAvailableCRS() <em>Available CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAvailableCRS()
     * @generated
     * @ordered
     */
    protected static final String AVAILABLE_CRS_EDEFAULT = null;

    /**
     * The default value of the '{@link #getFees() <em>Fees</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFees()
     * @generated
     * @ordered
     */
    protected static final String FEES_EDEFAULT = null;

    /**
     * The default value of the '{@link #getIdentifier() <em>Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected static final Object IDENTIFIER_EDEFAULT = null;

    /**
     * The default value of the '{@link #getLanguage() <em>Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLanguage()
     * @generated
     * @ordered
     */
    protected static final String LANGUAGE_EDEFAULT = null;

    /**
     * The default value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
    protected static final Object OUTPUT_FORMAT_EDEFAULT = null;

    /**
     * The default value of the '{@link #getSupportedCRS() <em>Supported CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSupportedCRS()
     * @generated
     * @ordered
     */
    protected static final String SUPPORTED_CRS_EDEFAULT = null;

    /**
     * The default value of the '{@link #getRangeClosure() <em>Range Closure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRangeClosure()
     * @generated
     * @ordered
     */
    protected static final RangeClosureType RANGE_CLOSURE_EDEFAULT = RangeClosureType.CLOSED_LITERAL;

    /**
     * The cached value of the '{@link #getRangeClosure() <em>Range Closure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRangeClosure()
     * @generated
     * @ordered
     */
    protected RangeClosureType rangeClosure = RANGE_CLOSURE_EDEFAULT;

    /**
     * This is true if the Range Closure attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean rangeClosureESet;

    /**
     * The default value of the '{@link #getReference1() <em>Reference1</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReference1()
     * @generated
     * @ordered
     */
    protected static final String REFERENCE1_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getReference1() <em>Reference1</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReference1()
     * @generated
     * @ordered
     */
    protected String reference1 = REFERENCE1_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DocumentRootImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return owcsPackage.Literals.DOCUMENT_ROOT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getMixed() {
        if (mixed == null) {
            mixed = new BasicFeatureMap(this, owcsPackage.DOCUMENT_ROOT__MIXED);
        }
        return mixed;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EMap getXMLNSPrefixMap() {
        if (xMLNSPrefixMap == null) {
            xMLNSPrefixMap = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, owcsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        }
        return xMLNSPrefixMap;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EMap getXSISchemaLocation() {
        if (xSISchemaLocation == null) {
            xSISchemaLocation = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, owcsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        }
        return xSISchemaLocation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractReferenceBaseType getAbstractReferenceBase() {
        return (AbstractReferenceBaseType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__ABSTRACT_REFERENCE_BASE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAbstractReferenceBase(AbstractReferenceBaseType newAbstractReferenceBase, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__ABSTRACT_REFERENCE_BASE, newAbstractReferenceBase, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAccessConstraints() {
        return (String)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__ACCESS_CONSTRAINTS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAccessConstraints(String newAccessConstraints) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__ACCESS_CONSTRAINTS, newAccessConstraints);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllowedValuesType getAllowedValues() {
        return (AllowedValuesType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__ALLOWED_VALUES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAllowedValues(AllowedValuesType newAllowedValues, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__ALLOWED_VALUES, newAllowedValues, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAllowedValues(AllowedValuesType newAllowedValues) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__ALLOWED_VALUES, newAllowedValues);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AnyValueType getAnyValue() {
        return (AnyValueType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__ANY_VALUE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAnyValue(AnyValueType newAnyValue, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__ANY_VALUE, newAnyValue, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAnyValue(AnyValueType newAnyValue) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__ANY_VALUE, newAnyValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAvailableCRS() {
        return (String)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__AVAILABLE_CRS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAvailableCRS(String newAvailableCRS) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__AVAILABLE_CRS, newAvailableCRS);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ReferenceGroupType getCoverage() {
        return (ReferenceGroupType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__COVERAGE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCoverage(ReferenceGroupType newCoverage, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__COVERAGE, newCoverage, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverage(ReferenceGroupType newCoverage) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__COVERAGE, newCoverage);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ReferenceGroupType getReferenceGroup() {
        return (ReferenceGroupType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__REFERENCE_GROUP, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetReferenceGroup(ReferenceGroupType newReferenceGroup, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__REFERENCE_GROUP, newReferenceGroup, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReferenceGroup(ReferenceGroupType newReferenceGroup) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__REFERENCE_GROUP, newReferenceGroup);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoveragesType getCoverages() {
        return (CoveragesType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__COVERAGES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCoverages(CoveragesType newCoverages, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__COVERAGES, newCoverages, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverages(CoveragesType newCoverages) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__COVERAGES, newCoverages);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ManifestType getManifest() {
        return (ManifestType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__MANIFEST, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetManifest(ManifestType newManifest, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__MANIFEST, newManifest, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setManifest(ManifestType newManifest) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__MANIFEST, newManifest);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainMetadataType getDataType() {
        return (DomainMetadataType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__DATA_TYPE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDataType(DomainMetadataType newDataType, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__DATA_TYPE, newDataType, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDataType(DomainMetadataType newDataType) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__DATA_TYPE, newDataType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DCPType getDCP() {
        return (DCPType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__DCP, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDCP(DCPType newDCP, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__DCP, newDCP, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDCP(DCPType newDCP) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__DCP, newDCP);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueType getDefaultValue() {
        return (ValueType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__DEFAULT_VALUE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDefaultValue(ValueType newDefaultValue, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__DEFAULT_VALUE, newDefaultValue, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefaultValue(ValueType newDefaultValue) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__DEFAULT_VALUE, newDefaultValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject getExtendedCapabilities() {
        return (EObject)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__EXTENDED_CAPABILITIES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetExtendedCapabilities(EObject newExtendedCapabilities, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__EXTENDED_CAPABILITIES, newExtendedCapabilities, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setExtendedCapabilities(EObject newExtendedCapabilities) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__EXTENDED_CAPABILITIES, newExtendedCapabilities);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFees() {
        return (String)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__FEES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFees(String newFees) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__FEES, newFees);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetCapabilitiesType getGetCapabilities() {
        return (GetCapabilitiesType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGetCapabilities(GetCapabilitiesType newGetCapabilities, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, newGetCapabilities, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGetCapabilities(GetCapabilitiesType newGetCapabilities) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, newGetCapabilities);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public HTTPType getHTTP() {
        return (HTTPType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__HTTP, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetHTTP(HTTPType newHTTP, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__HTTP, newHTTP, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHTTP(HTTPType newHTTP) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__HTTP, newHTTP);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getIdentifier() {
        return getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__IDENTIFIER, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIdentifier(Object newIdentifier) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__IDENTIFIER, newIdentifier);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InterpolationMethodsType getInterpolationMethods() {
        return (InterpolationMethodsType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__INTERPOLATION_METHODS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetInterpolationMethods(InterpolationMethodsType newInterpolationMethods, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__INTERPOLATION_METHODS, newInterpolationMethods, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInterpolationMethods(InterpolationMethodsType newInterpolationMethods) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__INTERPOLATION_METHODS, newInterpolationMethods);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLanguage() {
        return (String)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__LANGUAGE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLanguage(String newLanguage) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__LANGUAGE, newLanguage);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueType getMaximumValue() {
        return (ValueType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__MAXIMUM_VALUE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMaximumValue(ValueType newMaximumValue, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__MAXIMUM_VALUE, newMaximumValue, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMaximumValue(ValueType newMaximumValue) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__MAXIMUM_VALUE, newMaximumValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainMetadataType getMeaning() {
        return (DomainMetadataType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__MEANING, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMeaning(DomainMetadataType newMeaning, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__MEANING, newMeaning, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMeaning(DomainMetadataType newMeaning) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__MEANING, newMeaning);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueType getMinimumValue() {
        return (ValueType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__MINIMUM_VALUE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMinimumValue(ValueType newMinimumValue, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__MINIMUM_VALUE, newMinimumValue, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMinimumValue(ValueType newMinimumValue) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__MINIMUM_VALUE, newMinimumValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NoValuesType getNoValues() {
        return (NoValuesType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__NO_VALUES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetNoValues(NoValuesType newNoValues, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__NO_VALUES, newNoValues, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNoValues(NoValuesType newNoValues) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__NO_VALUES, newNoValues);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationType getOperation() {
        return (OperationType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__OPERATION, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOperation(OperationType newOperation, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__OPERATION, newOperation, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOperation(OperationType newOperation) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__OPERATION, newOperation);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationsMetadataType getOperationsMetadata() {
        return (OperationsMetadataType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__OPERATIONS_METADATA, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOperationsMetadata(OperationsMetadataType newOperationsMetadata, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__OPERATIONS_METADATA, newOperationsMetadata, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOperationsMetadata(OperationsMetadataType newOperationsMetadata) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__OPERATIONS_METADATA, newOperationsMetadata);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getOutputFormat() {
        return getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__OUTPUT_FORMAT, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOutputFormat(Object newOutputFormat) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__OUTPUT_FORMAT, newOutputFormat);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeType getRange() {
        return (RangeType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__RANGE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRange(RangeType newRange, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__RANGE, newRange, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRange(RangeType newRange) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__RANGE, newRange);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ReferenceType getReference() {
        return (ReferenceType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__REFERENCE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetReference(ReferenceType newReference, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__REFERENCE, newReference, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReference(ReferenceType newReference) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__REFERENCE, newReference);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainMetadataType getReferenceSystem() {
        return (DomainMetadataType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__REFERENCE_SYSTEM, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetReferenceSystem(DomainMetadataType newReferenceSystem, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__REFERENCE_SYSTEM, newReferenceSystem, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReferenceSystem(DomainMetadataType newReferenceSystem) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__REFERENCE_SYSTEM, newReferenceSystem);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceIdentificationType getServiceIdentification() {
        return (ServiceIdentificationType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__SERVICE_IDENTIFICATION, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetServiceIdentification(ServiceIdentificationType newServiceIdentification, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__SERVICE_IDENTIFICATION, newServiceIdentification, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServiceIdentification(ServiceIdentificationType newServiceIdentification) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__SERVICE_IDENTIFICATION, newServiceIdentification);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceReferenceType getServiceReference() {
        return (ServiceReferenceType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__SERVICE_REFERENCE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetServiceReference(ServiceReferenceType newServiceReference, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__SERVICE_REFERENCE, newServiceReference, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServiceReference(ServiceReferenceType newServiceReference) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__SERVICE_REFERENCE, newServiceReference);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueType getSpacing() {
        return (ValueType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__SPACING, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSpacing(ValueType newSpacing, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__SPACING, newSpacing, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSpacing(ValueType newSpacing) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__SPACING, newSpacing);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSupportedCRS() {
        return (String)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__SUPPORTED_CRS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSupportedCRS(String newSupportedCRS) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__SUPPORTED_CRS, newSupportedCRS);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainMetadataType getUOM() {
        return (DomainMetadataType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__UOM, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUOM(DomainMetadataType newUOM, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__UOM, newUOM, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUOM(DomainMetadataType newUOM) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__UOM, newUOM);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueType getValue() {
        return (ValueType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__VALUE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValue(ValueType newValue, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__VALUE, newValue, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValue(ValueType newValue) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__VALUE, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValuesReferenceType getValuesReference() {
        return (ValuesReferenceType)getMixed().get(owcsPackage.Literals.DOCUMENT_ROOT__VALUES_REFERENCE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValuesReference(ValuesReferenceType newValuesReference, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(owcsPackage.Literals.DOCUMENT_ROOT__VALUES_REFERENCE, newValuesReference, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValuesReference(ValuesReferenceType newValuesReference) {
        ((FeatureMap.Internal)getMixed()).set(owcsPackage.Literals.DOCUMENT_ROOT__VALUES_REFERENCE, newValuesReference);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeClosureType getRangeClosure() {
        return rangeClosure;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRangeClosure(RangeClosureType newRangeClosure) {
        RangeClosureType oldRangeClosure = rangeClosure;
        rangeClosure = newRangeClosure == null ? RANGE_CLOSURE_EDEFAULT : newRangeClosure;
        boolean oldRangeClosureESet = rangeClosureESet;
        rangeClosureESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, owcsPackage.DOCUMENT_ROOT__RANGE_CLOSURE, oldRangeClosure, rangeClosure, !oldRangeClosureESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetRangeClosure() {
        RangeClosureType oldRangeClosure = rangeClosure;
        boolean oldRangeClosureESet = rangeClosureESet;
        rangeClosure = RANGE_CLOSURE_EDEFAULT;
        rangeClosureESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, owcsPackage.DOCUMENT_ROOT__RANGE_CLOSURE, oldRangeClosure, RANGE_CLOSURE_EDEFAULT, oldRangeClosureESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetRangeClosure() {
        return rangeClosureESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getReference1() {
        return reference1;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReference1(String newReference1) {
        String oldReference1 = reference1;
        reference1 = newReference1;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, owcsPackage.DOCUMENT_ROOT__REFERENCE1, oldReference1, reference1));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case owcsPackage.DOCUMENT_ROOT__MIXED:
                return ((InternalEList)getMixed()).basicRemove(otherEnd, msgs);
            case owcsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return ((InternalEList)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
            case owcsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return ((InternalEList)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
            case owcsPackage.DOCUMENT_ROOT__ABSTRACT_REFERENCE_BASE:
                return basicSetAbstractReferenceBase(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__ALLOWED_VALUES:
                return basicSetAllowedValues(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__ANY_VALUE:
                return basicSetAnyValue(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__COVERAGE:
                return basicSetCoverage(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__REFERENCE_GROUP:
                return basicSetReferenceGroup(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__COVERAGES:
                return basicSetCoverages(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__MANIFEST:
                return basicSetManifest(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__DATA_TYPE:
                return basicSetDataType(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__DCP:
                return basicSetDCP(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__DEFAULT_VALUE:
                return basicSetDefaultValue(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
                return basicSetExtendedCapabilities(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                return basicSetGetCapabilities(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__HTTP:
                return basicSetHTTP(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__INTERPOLATION_METHODS:
                return basicSetInterpolationMethods(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__MAXIMUM_VALUE:
                return basicSetMaximumValue(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__MEANING:
                return basicSetMeaning(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__MINIMUM_VALUE:
                return basicSetMinimumValue(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__NO_VALUES:
                return basicSetNoValues(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__OPERATION:
                return basicSetOperation(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__OPERATIONS_METADATA:
                return basicSetOperationsMetadata(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__RANGE:
                return basicSetRange(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__REFERENCE:
                return basicSetReference(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__REFERENCE_SYSTEM:
                return basicSetReferenceSystem(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
                return basicSetServiceIdentification(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__SERVICE_REFERENCE:
                return basicSetServiceReference(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__SPACING:
                return basicSetSpacing(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__UOM:
                return basicSetUOM(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__VALUE:
                return basicSetValue(null, msgs);
            case owcsPackage.DOCUMENT_ROOT__VALUES_REFERENCE:
                return basicSetValuesReference(null, msgs);
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
            case owcsPackage.DOCUMENT_ROOT__MIXED:
                if (coreType) return getMixed();
                return ((FeatureMap.Internal)getMixed()).getWrapper();
            case owcsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                if (coreType) return getXMLNSPrefixMap();
                else return getXMLNSPrefixMap().map();
            case owcsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                if (coreType) return getXSISchemaLocation();
                else return getXSISchemaLocation().map();
            case owcsPackage.DOCUMENT_ROOT__ABSTRACT_REFERENCE_BASE:
                return getAbstractReferenceBase();
            case owcsPackage.DOCUMENT_ROOT__ACCESS_CONSTRAINTS:
                return getAccessConstraints();
            case owcsPackage.DOCUMENT_ROOT__ALLOWED_VALUES:
                return getAllowedValues();
            case owcsPackage.DOCUMENT_ROOT__ANY_VALUE:
                return getAnyValue();
            case owcsPackage.DOCUMENT_ROOT__AVAILABLE_CRS:
                return getAvailableCRS();
            case owcsPackage.DOCUMENT_ROOT__COVERAGE:
                return getCoverage();
            case owcsPackage.DOCUMENT_ROOT__REFERENCE_GROUP:
                return getReferenceGroup();
            case owcsPackage.DOCUMENT_ROOT__COVERAGES:
                return getCoverages();
            case owcsPackage.DOCUMENT_ROOT__MANIFEST:
                return getManifest();
            case owcsPackage.DOCUMENT_ROOT__DATA_TYPE:
                return getDataType();
            case owcsPackage.DOCUMENT_ROOT__DCP:
                return getDCP();
            case owcsPackage.DOCUMENT_ROOT__DEFAULT_VALUE:
                return getDefaultValue();
            case owcsPackage.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
                return getExtendedCapabilities();
            case owcsPackage.DOCUMENT_ROOT__FEES:
                return getFees();
            case owcsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                return getGetCapabilities();
            case owcsPackage.DOCUMENT_ROOT__HTTP:
                return getHTTP();
            case owcsPackage.DOCUMENT_ROOT__IDENTIFIER:
                return getIdentifier();
            case owcsPackage.DOCUMENT_ROOT__INTERPOLATION_METHODS:
                return getInterpolationMethods();
            case owcsPackage.DOCUMENT_ROOT__LANGUAGE:
                return getLanguage();
            case owcsPackage.DOCUMENT_ROOT__MAXIMUM_VALUE:
                return getMaximumValue();
            case owcsPackage.DOCUMENT_ROOT__MEANING:
                return getMeaning();
            case owcsPackage.DOCUMENT_ROOT__MINIMUM_VALUE:
                return getMinimumValue();
            case owcsPackage.DOCUMENT_ROOT__NO_VALUES:
                return getNoValues();
            case owcsPackage.DOCUMENT_ROOT__OPERATION:
                return getOperation();
            case owcsPackage.DOCUMENT_ROOT__OPERATIONS_METADATA:
                return getOperationsMetadata();
            case owcsPackage.DOCUMENT_ROOT__OUTPUT_FORMAT:
                return getOutputFormat();
            case owcsPackage.DOCUMENT_ROOT__RANGE:
                return getRange();
            case owcsPackage.DOCUMENT_ROOT__REFERENCE:
                return getReference();
            case owcsPackage.DOCUMENT_ROOT__REFERENCE_SYSTEM:
                return getReferenceSystem();
            case owcsPackage.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
                return getServiceIdentification();
            case owcsPackage.DOCUMENT_ROOT__SERVICE_REFERENCE:
                return getServiceReference();
            case owcsPackage.DOCUMENT_ROOT__SPACING:
                return getSpacing();
            case owcsPackage.DOCUMENT_ROOT__SUPPORTED_CRS:
                return getSupportedCRS();
            case owcsPackage.DOCUMENT_ROOT__UOM:
                return getUOM();
            case owcsPackage.DOCUMENT_ROOT__VALUE:
                return getValue();
            case owcsPackage.DOCUMENT_ROOT__VALUES_REFERENCE:
                return getValuesReference();
            case owcsPackage.DOCUMENT_ROOT__RANGE_CLOSURE:
                return getRangeClosure();
            case owcsPackage.DOCUMENT_ROOT__REFERENCE1:
                return getReference1();
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
            case owcsPackage.DOCUMENT_ROOT__MIXED:
                ((FeatureMap.Internal)getMixed()).set(newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                ((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                ((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__ACCESS_CONSTRAINTS:
                setAccessConstraints((String)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__ALLOWED_VALUES:
                setAllowedValues((AllowedValuesType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__ANY_VALUE:
                setAnyValue((AnyValueType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__AVAILABLE_CRS:
                setAvailableCRS((String)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__COVERAGE:
                setCoverage((ReferenceGroupType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__REFERENCE_GROUP:
                setReferenceGroup((ReferenceGroupType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__COVERAGES:
                setCoverages((CoveragesType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__MANIFEST:
                setManifest((ManifestType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__DATA_TYPE:
                setDataType((DomainMetadataType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__DCP:
                setDCP((DCPType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__DEFAULT_VALUE:
                setDefaultValue((ValueType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
                setExtendedCapabilities((EObject)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__FEES:
                setFees((String)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                setGetCapabilities((GetCapabilitiesType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__HTTP:
                setHTTP((HTTPType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__IDENTIFIER:
                setIdentifier(newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__INTERPOLATION_METHODS:
                setInterpolationMethods((InterpolationMethodsType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__LANGUAGE:
                setLanguage((String)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__MAXIMUM_VALUE:
                setMaximumValue((ValueType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__MEANING:
                setMeaning((DomainMetadataType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__MINIMUM_VALUE:
                setMinimumValue((ValueType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__NO_VALUES:
                setNoValues((NoValuesType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__OPERATION:
                setOperation((OperationType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__OPERATIONS_METADATA:
                setOperationsMetadata((OperationsMetadataType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__OUTPUT_FORMAT:
                setOutputFormat(newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__RANGE:
                setRange((RangeType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__REFERENCE:
                setReference((ReferenceType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__REFERENCE_SYSTEM:
                setReferenceSystem((DomainMetadataType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
                setServiceIdentification((ServiceIdentificationType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__SERVICE_REFERENCE:
                setServiceReference((ServiceReferenceType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__SPACING:
                setSpacing((ValueType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__SUPPORTED_CRS:
                setSupportedCRS((String)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__UOM:
                setUOM((DomainMetadataType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__VALUE:
                setValue((ValueType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__VALUES_REFERENCE:
                setValuesReference((ValuesReferenceType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__RANGE_CLOSURE:
                setRangeClosure((RangeClosureType)newValue);
                return;
            case owcsPackage.DOCUMENT_ROOT__REFERENCE1:
                setReference1((String)newValue);
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
            case owcsPackage.DOCUMENT_ROOT__MIXED:
                getMixed().clear();
                return;
            case owcsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                getXMLNSPrefixMap().clear();
                return;
            case owcsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                getXSISchemaLocation().clear();
                return;
            case owcsPackage.DOCUMENT_ROOT__ACCESS_CONSTRAINTS:
                setAccessConstraints(ACCESS_CONSTRAINTS_EDEFAULT);
                return;
            case owcsPackage.DOCUMENT_ROOT__ALLOWED_VALUES:
                setAllowedValues((AllowedValuesType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__ANY_VALUE:
                setAnyValue((AnyValueType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__AVAILABLE_CRS:
                setAvailableCRS(AVAILABLE_CRS_EDEFAULT);
                return;
            case owcsPackage.DOCUMENT_ROOT__COVERAGE:
                setCoverage((ReferenceGroupType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__REFERENCE_GROUP:
                setReferenceGroup((ReferenceGroupType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__COVERAGES:
                setCoverages((CoveragesType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__MANIFEST:
                setManifest((ManifestType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__DATA_TYPE:
                setDataType((DomainMetadataType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__DCP:
                setDCP((DCPType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__DEFAULT_VALUE:
                setDefaultValue((ValueType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
                setExtendedCapabilities((EObject)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__FEES:
                setFees(FEES_EDEFAULT);
                return;
            case owcsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                setGetCapabilities((GetCapabilitiesType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__HTTP:
                setHTTP((HTTPType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__IDENTIFIER:
                setIdentifier(IDENTIFIER_EDEFAULT);
                return;
            case owcsPackage.DOCUMENT_ROOT__INTERPOLATION_METHODS:
                setInterpolationMethods((InterpolationMethodsType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__LANGUAGE:
                setLanguage(LANGUAGE_EDEFAULT);
                return;
            case owcsPackage.DOCUMENT_ROOT__MAXIMUM_VALUE:
                setMaximumValue((ValueType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__MEANING:
                setMeaning((DomainMetadataType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__MINIMUM_VALUE:
                setMinimumValue((ValueType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__NO_VALUES:
                setNoValues((NoValuesType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__OPERATION:
                setOperation((OperationType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__OPERATIONS_METADATA:
                setOperationsMetadata((OperationsMetadataType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__OUTPUT_FORMAT:
                setOutputFormat(OUTPUT_FORMAT_EDEFAULT);
                return;
            case owcsPackage.DOCUMENT_ROOT__RANGE:
                setRange((RangeType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__REFERENCE:
                setReference((ReferenceType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__REFERENCE_SYSTEM:
                setReferenceSystem((DomainMetadataType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
                setServiceIdentification((ServiceIdentificationType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__SERVICE_REFERENCE:
                setServiceReference((ServiceReferenceType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__SPACING:
                setSpacing((ValueType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__SUPPORTED_CRS:
                setSupportedCRS(SUPPORTED_CRS_EDEFAULT);
                return;
            case owcsPackage.DOCUMENT_ROOT__UOM:
                setUOM((DomainMetadataType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__VALUE:
                setValue((ValueType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__VALUES_REFERENCE:
                setValuesReference((ValuesReferenceType)null);
                return;
            case owcsPackage.DOCUMENT_ROOT__RANGE_CLOSURE:
                unsetRangeClosure();
                return;
            case owcsPackage.DOCUMENT_ROOT__REFERENCE1:
                setReference1(REFERENCE1_EDEFAULT);
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
            case owcsPackage.DOCUMENT_ROOT__MIXED:
                return mixed != null && !mixed.isEmpty();
            case owcsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
            case owcsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
            case owcsPackage.DOCUMENT_ROOT__ABSTRACT_REFERENCE_BASE:
                return getAbstractReferenceBase() != null;
            case owcsPackage.DOCUMENT_ROOT__ACCESS_CONSTRAINTS:
                return ACCESS_CONSTRAINTS_EDEFAULT == null ? getAccessConstraints() != null : !ACCESS_CONSTRAINTS_EDEFAULT.equals(getAccessConstraints());
            case owcsPackage.DOCUMENT_ROOT__ALLOWED_VALUES:
                return getAllowedValues() != null;
            case owcsPackage.DOCUMENT_ROOT__ANY_VALUE:
                return getAnyValue() != null;
            case owcsPackage.DOCUMENT_ROOT__AVAILABLE_CRS:
                return AVAILABLE_CRS_EDEFAULT == null ? getAvailableCRS() != null : !AVAILABLE_CRS_EDEFAULT.equals(getAvailableCRS());
            case owcsPackage.DOCUMENT_ROOT__COVERAGE:
                return getCoverage() != null;
            case owcsPackage.DOCUMENT_ROOT__REFERENCE_GROUP:
                return getReferenceGroup() != null;
            case owcsPackage.DOCUMENT_ROOT__COVERAGES:
                return getCoverages() != null;
            case owcsPackage.DOCUMENT_ROOT__MANIFEST:
                return getManifest() != null;
            case owcsPackage.DOCUMENT_ROOT__DATA_TYPE:
                return getDataType() != null;
            case owcsPackage.DOCUMENT_ROOT__DCP:
                return getDCP() != null;
            case owcsPackage.DOCUMENT_ROOT__DEFAULT_VALUE:
                return getDefaultValue() != null;
            case owcsPackage.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
                return getExtendedCapabilities() != null;
            case owcsPackage.DOCUMENT_ROOT__FEES:
                return FEES_EDEFAULT == null ? getFees() != null : !FEES_EDEFAULT.equals(getFees());
            case owcsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                return getGetCapabilities() != null;
            case owcsPackage.DOCUMENT_ROOT__HTTP:
                return getHTTP() != null;
            case owcsPackage.DOCUMENT_ROOT__IDENTIFIER:
                return IDENTIFIER_EDEFAULT == null ? getIdentifier() != null : !IDENTIFIER_EDEFAULT.equals(getIdentifier());
            case owcsPackage.DOCUMENT_ROOT__INTERPOLATION_METHODS:
                return getInterpolationMethods() != null;
            case owcsPackage.DOCUMENT_ROOT__LANGUAGE:
                return LANGUAGE_EDEFAULT == null ? getLanguage() != null : !LANGUAGE_EDEFAULT.equals(getLanguage());
            case owcsPackage.DOCUMENT_ROOT__MAXIMUM_VALUE:
                return getMaximumValue() != null;
            case owcsPackage.DOCUMENT_ROOT__MEANING:
                return getMeaning() != null;
            case owcsPackage.DOCUMENT_ROOT__MINIMUM_VALUE:
                return getMinimumValue() != null;
            case owcsPackage.DOCUMENT_ROOT__NO_VALUES:
                return getNoValues() != null;
            case owcsPackage.DOCUMENT_ROOT__OPERATION:
                return getOperation() != null;
            case owcsPackage.DOCUMENT_ROOT__OPERATIONS_METADATA:
                return getOperationsMetadata() != null;
            case owcsPackage.DOCUMENT_ROOT__OUTPUT_FORMAT:
                return OUTPUT_FORMAT_EDEFAULT == null ? getOutputFormat() != null : !OUTPUT_FORMAT_EDEFAULT.equals(getOutputFormat());
            case owcsPackage.DOCUMENT_ROOT__RANGE:
                return getRange() != null;
            case owcsPackage.DOCUMENT_ROOT__REFERENCE:
                return getReference() != null;
            case owcsPackage.DOCUMENT_ROOT__REFERENCE_SYSTEM:
                return getReferenceSystem() != null;
            case owcsPackage.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
                return getServiceIdentification() != null;
            case owcsPackage.DOCUMENT_ROOT__SERVICE_REFERENCE:
                return getServiceReference() != null;
            case owcsPackage.DOCUMENT_ROOT__SPACING:
                return getSpacing() != null;
            case owcsPackage.DOCUMENT_ROOT__SUPPORTED_CRS:
                return SUPPORTED_CRS_EDEFAULT == null ? getSupportedCRS() != null : !SUPPORTED_CRS_EDEFAULT.equals(getSupportedCRS());
            case owcsPackage.DOCUMENT_ROOT__UOM:
                return getUOM() != null;
            case owcsPackage.DOCUMENT_ROOT__VALUE:
                return getValue() != null;
            case owcsPackage.DOCUMENT_ROOT__VALUES_REFERENCE:
                return getValuesReference() != null;
            case owcsPackage.DOCUMENT_ROOT__RANGE_CLOSURE:
                return isSetRangeClosure();
            case owcsPackage.DOCUMENT_ROOT__REFERENCE1:
                return REFERENCE1_EDEFAULT == null ? reference1 != null : !REFERENCE1_EDEFAULT.equals(reference1);
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
        result.append(" (mixed: ");
        result.append(mixed);
        result.append(", rangeClosure: ");
        if (rangeClosureESet) result.append(rangeClosure); else result.append("<unset>");
        result.append(", reference1: ");
        result.append(reference1);
        result.append(')');
        return result.toString();
    }

} //DocumentRootImpl
