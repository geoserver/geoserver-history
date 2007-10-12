/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.impl;

import net.opengis.wcs.AvailableKeysType;
import net.opengis.wcs.AxisSubsetType;
import net.opengis.wcs.CapabilitiesType;
import net.opengis.wcs.ContentsType;
import net.opengis.wcs.CoverageDescriptionsType;
import net.opengis.wcs.CoverageSummaryType;
import net.opengis.wcs.DescribeCoverageType;
import net.opengis.wcs.DocumentRoot;
import net.opengis.wcs.GetCapabilitiesType;
import net.opengis.wcs.GetCoverageType;
import net.opengis.wcs.GridCrsType;
import net.opengis.wcs.TimeSequenceType;
import net.opengis.wcs.wcsPackage;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

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
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getAvailableKeys <em>Available Keys</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getAxisSubset <em>Axis Subset</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getCapabilities <em>Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getContents <em>Contents</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getCoverageDescriptions <em>Coverage Descriptions</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getCoverageSummary <em>Coverage Summary</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getDescribeCoverage <em>Describe Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getGetCoverage <em>Get Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getGridBaseCRS <em>Grid Base CRS</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getGridCRS <em>Grid CRS</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getGridCS <em>Grid CS</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getGridOffsets <em>Grid Offsets</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getGridOrigin <em>Grid Origin</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getGridType <em>Grid Type</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getTemporalDomain <em>Temporal Domain</em>}</li>
 *   <li>{@link net.opengis.wcs.impl.DocumentRootImpl#getTemporalSubset <em>Temporal Subset</em>}</li>
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
     * The default value of the '{@link #getGridBaseCRS() <em>Grid Base CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridBaseCRS()
     * @generated
     * @ordered
     */
    protected static final String GRID_BASE_CRS_EDEFAULT = null;

    /**
     * The default value of the '{@link #getGridCS() <em>Grid CS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridCS()
     * @generated
     * @ordered
     */
    protected static final String GRID_CS_EDEFAULT = null;

    /**
     * The default value of the '{@link #getGridOffsets() <em>Grid Offsets</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridOffsets()
     * @generated
     * @ordered
     */
    protected static final Object GRID_OFFSETS_EDEFAULT = null;

    /**
     * The default value of the '{@link #getGridOrigin() <em>Grid Origin</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridOrigin()
     * @generated
     * @ordered
     */
    protected static final Object GRID_ORIGIN_EDEFAULT = null;

    /**
     * The default value of the '{@link #getGridType() <em>Grid Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridType()
     * @generated
     * @ordered
     */
    protected static final String GRID_TYPE_EDEFAULT = null;

    /**
     * The default value of the '{@link #getIdentifier() <em>Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected static final String IDENTIFIER_EDEFAULT = null;

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
        return wcsPackage.Literals.DOCUMENT_ROOT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getMixed() {
        if (mixed == null) {
            mixed = new BasicFeatureMap(this, wcsPackage.DOCUMENT_ROOT__MIXED);
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
            xMLNSPrefixMap = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, wcsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
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
            xSISchemaLocation = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, wcsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        }
        return xSISchemaLocation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AvailableKeysType getAvailableKeys() {
        return (AvailableKeysType)getMixed().get(wcsPackage.Literals.DOCUMENT_ROOT__AVAILABLE_KEYS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAvailableKeys(AvailableKeysType newAvailableKeys, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wcsPackage.Literals.DOCUMENT_ROOT__AVAILABLE_KEYS, newAvailableKeys, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAvailableKeys(AvailableKeysType newAvailableKeys) {
        ((FeatureMap.Internal)getMixed()).set(wcsPackage.Literals.DOCUMENT_ROOT__AVAILABLE_KEYS, newAvailableKeys);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AxisSubsetType getAxisSubset() {
        return (AxisSubsetType)getMixed().get(wcsPackage.Literals.DOCUMENT_ROOT__AXIS_SUBSET, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAxisSubset(AxisSubsetType newAxisSubset, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wcsPackage.Literals.DOCUMENT_ROOT__AXIS_SUBSET, newAxisSubset, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAxisSubset(AxisSubsetType newAxisSubset) {
        ((FeatureMap.Internal)getMixed()).set(wcsPackage.Literals.DOCUMENT_ROOT__AXIS_SUBSET, newAxisSubset);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CapabilitiesType getCapabilities() {
        return (CapabilitiesType)getMixed().get(wcsPackage.Literals.DOCUMENT_ROOT__CAPABILITIES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCapabilities(CapabilitiesType newCapabilities, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wcsPackage.Literals.DOCUMENT_ROOT__CAPABILITIES, newCapabilities, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCapabilities(CapabilitiesType newCapabilities) {
        ((FeatureMap.Internal)getMixed()).set(wcsPackage.Literals.DOCUMENT_ROOT__CAPABILITIES, newCapabilities);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ContentsType getContents() {
        return (ContentsType)getMixed().get(wcsPackage.Literals.DOCUMENT_ROOT__CONTENTS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetContents(ContentsType newContents, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wcsPackage.Literals.DOCUMENT_ROOT__CONTENTS, newContents, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setContents(ContentsType newContents) {
        ((FeatureMap.Internal)getMixed()).set(wcsPackage.Literals.DOCUMENT_ROOT__CONTENTS, newContents);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageDescriptionsType getCoverageDescriptions() {
        return (CoverageDescriptionsType)getMixed().get(wcsPackage.Literals.DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCoverageDescriptions(CoverageDescriptionsType newCoverageDescriptions, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wcsPackage.Literals.DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS, newCoverageDescriptions, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverageDescriptions(CoverageDescriptionsType newCoverageDescriptions) {
        ((FeatureMap.Internal)getMixed()).set(wcsPackage.Literals.DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS, newCoverageDescriptions);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageSummaryType getCoverageSummary() {
        return (CoverageSummaryType)getMixed().get(wcsPackage.Literals.DOCUMENT_ROOT__COVERAGE_SUMMARY, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCoverageSummary(CoverageSummaryType newCoverageSummary, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wcsPackage.Literals.DOCUMENT_ROOT__COVERAGE_SUMMARY, newCoverageSummary, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverageSummary(CoverageSummaryType newCoverageSummary) {
        ((FeatureMap.Internal)getMixed()).set(wcsPackage.Literals.DOCUMENT_ROOT__COVERAGE_SUMMARY, newCoverageSummary);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DescribeCoverageType getDescribeCoverage() {
        return (DescribeCoverageType)getMixed().get(wcsPackage.Literals.DOCUMENT_ROOT__DESCRIBE_COVERAGE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDescribeCoverage(DescribeCoverageType newDescribeCoverage, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wcsPackage.Literals.DOCUMENT_ROOT__DESCRIBE_COVERAGE, newDescribeCoverage, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDescribeCoverage(DescribeCoverageType newDescribeCoverage) {
        ((FeatureMap.Internal)getMixed()).set(wcsPackage.Literals.DOCUMENT_ROOT__DESCRIBE_COVERAGE, newDescribeCoverage);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetCapabilitiesType getGetCapabilities() {
        return (GetCapabilitiesType)getMixed().get(wcsPackage.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGetCapabilities(GetCapabilitiesType newGetCapabilities, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wcsPackage.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, newGetCapabilities, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGetCapabilities(GetCapabilitiesType newGetCapabilities) {
        ((FeatureMap.Internal)getMixed()).set(wcsPackage.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, newGetCapabilities);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetCoverageType getGetCoverage() {
        return (GetCoverageType)getMixed().get(wcsPackage.Literals.DOCUMENT_ROOT__GET_COVERAGE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGetCoverage(GetCoverageType newGetCoverage, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wcsPackage.Literals.DOCUMENT_ROOT__GET_COVERAGE, newGetCoverage, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGetCoverage(GetCoverageType newGetCoverage) {
        ((FeatureMap.Internal)getMixed()).set(wcsPackage.Literals.DOCUMENT_ROOT__GET_COVERAGE, newGetCoverage);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getGridBaseCRS() {
        return (String)getMixed().get(wcsPackage.Literals.DOCUMENT_ROOT__GRID_BASE_CRS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridBaseCRS(String newGridBaseCRS) {
        ((FeatureMap.Internal)getMixed()).set(wcsPackage.Literals.DOCUMENT_ROOT__GRID_BASE_CRS, newGridBaseCRS);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GridCrsType getGridCRS() {
        return (GridCrsType)getMixed().get(wcsPackage.Literals.DOCUMENT_ROOT__GRID_CRS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGridCRS(GridCrsType newGridCRS, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wcsPackage.Literals.DOCUMENT_ROOT__GRID_CRS, newGridCRS, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridCRS(GridCrsType newGridCRS) {
        ((FeatureMap.Internal)getMixed()).set(wcsPackage.Literals.DOCUMENT_ROOT__GRID_CRS, newGridCRS);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getGridCS() {
        return (String)getMixed().get(wcsPackage.Literals.DOCUMENT_ROOT__GRID_CS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridCS(String newGridCS) {
        ((FeatureMap.Internal)getMixed()).set(wcsPackage.Literals.DOCUMENT_ROOT__GRID_CS, newGridCS);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getGridOffsets() {
        return getMixed().get(wcsPackage.Literals.DOCUMENT_ROOT__GRID_OFFSETS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridOffsets(Object newGridOffsets) {
        ((FeatureMap.Internal)getMixed()).set(wcsPackage.Literals.DOCUMENT_ROOT__GRID_OFFSETS, newGridOffsets);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getGridOrigin() {
        return getMixed().get(wcsPackage.Literals.DOCUMENT_ROOT__GRID_ORIGIN, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridOrigin(Object newGridOrigin) {
        ((FeatureMap.Internal)getMixed()).set(wcsPackage.Literals.DOCUMENT_ROOT__GRID_ORIGIN, newGridOrigin);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getGridType() {
        return (String)getMixed().get(wcsPackage.Literals.DOCUMENT_ROOT__GRID_TYPE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridType(String newGridType) {
        ((FeatureMap.Internal)getMixed()).set(wcsPackage.Literals.DOCUMENT_ROOT__GRID_TYPE, newGridType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getIdentifier() {
        return (String)getMixed().get(wcsPackage.Literals.DOCUMENT_ROOT__IDENTIFIER, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIdentifier(String newIdentifier) {
        ((FeatureMap.Internal)getMixed()).set(wcsPackage.Literals.DOCUMENT_ROOT__IDENTIFIER, newIdentifier);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeSequenceType getTemporalDomain() {
        return (TimeSequenceType)getMixed().get(wcsPackage.Literals.DOCUMENT_ROOT__TEMPORAL_DOMAIN, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTemporalDomain(TimeSequenceType newTemporalDomain, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wcsPackage.Literals.DOCUMENT_ROOT__TEMPORAL_DOMAIN, newTemporalDomain, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTemporalDomain(TimeSequenceType newTemporalDomain) {
        ((FeatureMap.Internal)getMixed()).set(wcsPackage.Literals.DOCUMENT_ROOT__TEMPORAL_DOMAIN, newTemporalDomain);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeSequenceType getTemporalSubset() {
        return (TimeSequenceType)getMixed().get(wcsPackage.Literals.DOCUMENT_ROOT__TEMPORAL_SUBSET, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTemporalSubset(TimeSequenceType newTemporalSubset, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wcsPackage.Literals.DOCUMENT_ROOT__TEMPORAL_SUBSET, newTemporalSubset, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTemporalSubset(TimeSequenceType newTemporalSubset) {
        ((FeatureMap.Internal)getMixed()).set(wcsPackage.Literals.DOCUMENT_ROOT__TEMPORAL_SUBSET, newTemporalSubset);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wcsPackage.DOCUMENT_ROOT__MIXED:
                return ((InternalEList)getMixed()).basicRemove(otherEnd, msgs);
            case wcsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return ((InternalEList)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
            case wcsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return ((InternalEList)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
            case wcsPackage.DOCUMENT_ROOT__AVAILABLE_KEYS:
                return basicSetAvailableKeys(null, msgs);
            case wcsPackage.DOCUMENT_ROOT__AXIS_SUBSET:
                return basicSetAxisSubset(null, msgs);
            case wcsPackage.DOCUMENT_ROOT__CAPABILITIES:
                return basicSetCapabilities(null, msgs);
            case wcsPackage.DOCUMENT_ROOT__CONTENTS:
                return basicSetContents(null, msgs);
            case wcsPackage.DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS:
                return basicSetCoverageDescriptions(null, msgs);
            case wcsPackage.DOCUMENT_ROOT__COVERAGE_SUMMARY:
                return basicSetCoverageSummary(null, msgs);
            case wcsPackage.DOCUMENT_ROOT__DESCRIBE_COVERAGE:
                return basicSetDescribeCoverage(null, msgs);
            case wcsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                return basicSetGetCapabilities(null, msgs);
            case wcsPackage.DOCUMENT_ROOT__GET_COVERAGE:
                return basicSetGetCoverage(null, msgs);
            case wcsPackage.DOCUMENT_ROOT__GRID_CRS:
                return basicSetGridCRS(null, msgs);
            case wcsPackage.DOCUMENT_ROOT__TEMPORAL_DOMAIN:
                return basicSetTemporalDomain(null, msgs);
            case wcsPackage.DOCUMENT_ROOT__TEMPORAL_SUBSET:
                return basicSetTemporalSubset(null, msgs);
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
            case wcsPackage.DOCUMENT_ROOT__MIXED:
                if (coreType) return getMixed();
                return ((FeatureMap.Internal)getMixed()).getWrapper();
            case wcsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                if (coreType) return getXMLNSPrefixMap();
                else return getXMLNSPrefixMap().map();
            case wcsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                if (coreType) return getXSISchemaLocation();
                else return getXSISchemaLocation().map();
            case wcsPackage.DOCUMENT_ROOT__AVAILABLE_KEYS:
                return getAvailableKeys();
            case wcsPackage.DOCUMENT_ROOT__AXIS_SUBSET:
                return getAxisSubset();
            case wcsPackage.DOCUMENT_ROOT__CAPABILITIES:
                return getCapabilities();
            case wcsPackage.DOCUMENT_ROOT__CONTENTS:
                return getContents();
            case wcsPackage.DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS:
                return getCoverageDescriptions();
            case wcsPackage.DOCUMENT_ROOT__COVERAGE_SUMMARY:
                return getCoverageSummary();
            case wcsPackage.DOCUMENT_ROOT__DESCRIBE_COVERAGE:
                return getDescribeCoverage();
            case wcsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                return getGetCapabilities();
            case wcsPackage.DOCUMENT_ROOT__GET_COVERAGE:
                return getGetCoverage();
            case wcsPackage.DOCUMENT_ROOT__GRID_BASE_CRS:
                return getGridBaseCRS();
            case wcsPackage.DOCUMENT_ROOT__GRID_CRS:
                return getGridCRS();
            case wcsPackage.DOCUMENT_ROOT__GRID_CS:
                return getGridCS();
            case wcsPackage.DOCUMENT_ROOT__GRID_OFFSETS:
                return getGridOffsets();
            case wcsPackage.DOCUMENT_ROOT__GRID_ORIGIN:
                return getGridOrigin();
            case wcsPackage.DOCUMENT_ROOT__GRID_TYPE:
                return getGridType();
            case wcsPackage.DOCUMENT_ROOT__IDENTIFIER:
                return getIdentifier();
            case wcsPackage.DOCUMENT_ROOT__TEMPORAL_DOMAIN:
                return getTemporalDomain();
            case wcsPackage.DOCUMENT_ROOT__TEMPORAL_SUBSET:
                return getTemporalSubset();
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
            case wcsPackage.DOCUMENT_ROOT__MIXED:
                ((FeatureMap.Internal)getMixed()).set(newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                ((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                ((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__AVAILABLE_KEYS:
                setAvailableKeys((AvailableKeysType)newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__AXIS_SUBSET:
                setAxisSubset((AxisSubsetType)newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__CAPABILITIES:
                setCapabilities((CapabilitiesType)newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__CONTENTS:
                setContents((ContentsType)newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS:
                setCoverageDescriptions((CoverageDescriptionsType)newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__COVERAGE_SUMMARY:
                setCoverageSummary((CoverageSummaryType)newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__DESCRIBE_COVERAGE:
                setDescribeCoverage((DescribeCoverageType)newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                setGetCapabilities((GetCapabilitiesType)newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__GET_COVERAGE:
                setGetCoverage((GetCoverageType)newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__GRID_BASE_CRS:
                setGridBaseCRS((String)newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__GRID_CRS:
                setGridCRS((GridCrsType)newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__GRID_CS:
                setGridCS((String)newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__GRID_OFFSETS:
                setGridOffsets(newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__GRID_ORIGIN:
                setGridOrigin(newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__GRID_TYPE:
                setGridType((String)newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__IDENTIFIER:
                setIdentifier((String)newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__TEMPORAL_DOMAIN:
                setTemporalDomain((TimeSequenceType)newValue);
                return;
            case wcsPackage.DOCUMENT_ROOT__TEMPORAL_SUBSET:
                setTemporalSubset((TimeSequenceType)newValue);
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
            case wcsPackage.DOCUMENT_ROOT__MIXED:
                getMixed().clear();
                return;
            case wcsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                getXMLNSPrefixMap().clear();
                return;
            case wcsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                getXSISchemaLocation().clear();
                return;
            case wcsPackage.DOCUMENT_ROOT__AVAILABLE_KEYS:
                setAvailableKeys((AvailableKeysType)null);
                return;
            case wcsPackage.DOCUMENT_ROOT__AXIS_SUBSET:
                setAxisSubset((AxisSubsetType)null);
                return;
            case wcsPackage.DOCUMENT_ROOT__CAPABILITIES:
                setCapabilities((CapabilitiesType)null);
                return;
            case wcsPackage.DOCUMENT_ROOT__CONTENTS:
                setContents((ContentsType)null);
                return;
            case wcsPackage.DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS:
                setCoverageDescriptions((CoverageDescriptionsType)null);
                return;
            case wcsPackage.DOCUMENT_ROOT__COVERAGE_SUMMARY:
                setCoverageSummary((CoverageSummaryType)null);
                return;
            case wcsPackage.DOCUMENT_ROOT__DESCRIBE_COVERAGE:
                setDescribeCoverage((DescribeCoverageType)null);
                return;
            case wcsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                setGetCapabilities((GetCapabilitiesType)null);
                return;
            case wcsPackage.DOCUMENT_ROOT__GET_COVERAGE:
                setGetCoverage((GetCoverageType)null);
                return;
            case wcsPackage.DOCUMENT_ROOT__GRID_BASE_CRS:
                setGridBaseCRS(GRID_BASE_CRS_EDEFAULT);
                return;
            case wcsPackage.DOCUMENT_ROOT__GRID_CRS:
                setGridCRS((GridCrsType)null);
                return;
            case wcsPackage.DOCUMENT_ROOT__GRID_CS:
                setGridCS(GRID_CS_EDEFAULT);
                return;
            case wcsPackage.DOCUMENT_ROOT__GRID_OFFSETS:
                setGridOffsets(GRID_OFFSETS_EDEFAULT);
                return;
            case wcsPackage.DOCUMENT_ROOT__GRID_ORIGIN:
                setGridOrigin(GRID_ORIGIN_EDEFAULT);
                return;
            case wcsPackage.DOCUMENT_ROOT__GRID_TYPE:
                setGridType(GRID_TYPE_EDEFAULT);
                return;
            case wcsPackage.DOCUMENT_ROOT__IDENTIFIER:
                setIdentifier(IDENTIFIER_EDEFAULT);
                return;
            case wcsPackage.DOCUMENT_ROOT__TEMPORAL_DOMAIN:
                setTemporalDomain((TimeSequenceType)null);
                return;
            case wcsPackage.DOCUMENT_ROOT__TEMPORAL_SUBSET:
                setTemporalSubset((TimeSequenceType)null);
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
            case wcsPackage.DOCUMENT_ROOT__MIXED:
                return mixed != null && !mixed.isEmpty();
            case wcsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
            case wcsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
            case wcsPackage.DOCUMENT_ROOT__AVAILABLE_KEYS:
                return getAvailableKeys() != null;
            case wcsPackage.DOCUMENT_ROOT__AXIS_SUBSET:
                return getAxisSubset() != null;
            case wcsPackage.DOCUMENT_ROOT__CAPABILITIES:
                return getCapabilities() != null;
            case wcsPackage.DOCUMENT_ROOT__CONTENTS:
                return getContents() != null;
            case wcsPackage.DOCUMENT_ROOT__COVERAGE_DESCRIPTIONS:
                return getCoverageDescriptions() != null;
            case wcsPackage.DOCUMENT_ROOT__COVERAGE_SUMMARY:
                return getCoverageSummary() != null;
            case wcsPackage.DOCUMENT_ROOT__DESCRIBE_COVERAGE:
                return getDescribeCoverage() != null;
            case wcsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                return getGetCapabilities() != null;
            case wcsPackage.DOCUMENT_ROOT__GET_COVERAGE:
                return getGetCoverage() != null;
            case wcsPackage.DOCUMENT_ROOT__GRID_BASE_CRS:
                return GRID_BASE_CRS_EDEFAULT == null ? getGridBaseCRS() != null : !GRID_BASE_CRS_EDEFAULT.equals(getGridBaseCRS());
            case wcsPackage.DOCUMENT_ROOT__GRID_CRS:
                return getGridCRS() != null;
            case wcsPackage.DOCUMENT_ROOT__GRID_CS:
                return GRID_CS_EDEFAULT == null ? getGridCS() != null : !GRID_CS_EDEFAULT.equals(getGridCS());
            case wcsPackage.DOCUMENT_ROOT__GRID_OFFSETS:
                return GRID_OFFSETS_EDEFAULT == null ? getGridOffsets() != null : !GRID_OFFSETS_EDEFAULT.equals(getGridOffsets());
            case wcsPackage.DOCUMENT_ROOT__GRID_ORIGIN:
                return GRID_ORIGIN_EDEFAULT == null ? getGridOrigin() != null : !GRID_ORIGIN_EDEFAULT.equals(getGridOrigin());
            case wcsPackage.DOCUMENT_ROOT__GRID_TYPE:
                return GRID_TYPE_EDEFAULT == null ? getGridType() != null : !GRID_TYPE_EDEFAULT.equals(getGridType());
            case wcsPackage.DOCUMENT_ROOT__IDENTIFIER:
                return IDENTIFIER_EDEFAULT == null ? getIdentifier() != null : !IDENTIFIER_EDEFAULT.equals(getIdentifier());
            case wcsPackage.DOCUMENT_ROOT__TEMPORAL_DOMAIN:
                return getTemporalDomain() != null;
            case wcsPackage.DOCUMENT_ROOT__TEMPORAL_SUBSET:
                return getTemporalSubset() != null;
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
        result.append(')');
        return result.toString();
    }

} //DocumentRootImpl
