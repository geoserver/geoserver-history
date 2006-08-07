/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.util.Collection;

import net.opengis.wfs.DescribeFeatureTypeType;
import net.opengis.wfs.DocumentRoot;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetCapabilitiesType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.QueryType;
import net.opengis.wfs.WFSPackage;

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
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getDescribeFeatureType <em>Describe Feature Type</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getFeatureCollection <em>Feature Collection</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getGetFeature <em>Get Feature</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getQuery <em>Query</em>}</li>
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
	protected FeatureMap mixed = null;

	/**
	 * The cached value of the '{@link #getXMLNSPrefixMap() <em>XMLNS Prefix Map</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getXMLNSPrefixMap()
	 * @generated
	 * @ordered
	 */
	protected EMap xMLNSPrefixMap = null;

	/**
	 * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema Location</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getXSISchemaLocation()
	 * @generated
	 * @ordered
	 */
	protected EMap xSISchemaLocation = null;

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
		return WFSPackage.eINSTANCE.getDocumentRoot();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getMixed() {
		if (mixed == null) {
			mixed = new BasicFeatureMap(this, WFSPackage.DOCUMENT_ROOT__MIXED);
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
			xMLNSPrefixMap = new EcoreEMap(EcorePackage.eINSTANCE.getEStringToStringMapEntry(), EStringToStringMapEntryImpl.class, this, WFSPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
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
			xSISchemaLocation = new EcoreEMap(EcorePackage.eINSTANCE.getEStringToStringMapEntry(), EStringToStringMapEntryImpl.class, this, WFSPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		}
		return xSISchemaLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DescribeFeatureTypeType getDescribeFeatureType() {
		return (DescribeFeatureTypeType)getMixed().get(WFSPackage.eINSTANCE.getDocumentRoot_DescribeFeatureType(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDescribeFeatureType(DescribeFeatureTypeType newDescribeFeatureType, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(WFSPackage.eINSTANCE.getDocumentRoot_DescribeFeatureType(), newDescribeFeatureType, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescribeFeatureType(DescribeFeatureTypeType newDescribeFeatureType) {
		((FeatureMap.Internal)getMixed()).set(WFSPackage.eINSTANCE.getDocumentRoot_DescribeFeatureType(), newDescribeFeatureType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureCollectionType getFeatureCollection() {
		return (FeatureCollectionType)getMixed().get(WFSPackage.eINSTANCE.getDocumentRoot_FeatureCollection(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFeatureCollection(FeatureCollectionType newFeatureCollection, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(WFSPackage.eINSTANCE.getDocumentRoot_FeatureCollection(), newFeatureCollection, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFeatureCollection(FeatureCollectionType newFeatureCollection) {
		((FeatureMap.Internal)getMixed()).set(WFSPackage.eINSTANCE.getDocumentRoot_FeatureCollection(), newFeatureCollection);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GetCapabilitiesType getGetCapabilities() {
		return (GetCapabilitiesType)getMixed().get(WFSPackage.eINSTANCE.getDocumentRoot_GetCapabilities(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGetCapabilities(GetCapabilitiesType newGetCapabilities, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(WFSPackage.eINSTANCE.getDocumentRoot_GetCapabilities(), newGetCapabilities, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGetCapabilities(GetCapabilitiesType newGetCapabilities) {
		((FeatureMap.Internal)getMixed()).set(WFSPackage.eINSTANCE.getDocumentRoot_GetCapabilities(), newGetCapabilities);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GetFeatureType getGetFeature() {
		return (GetFeatureType)getMixed().get(WFSPackage.eINSTANCE.getDocumentRoot_GetFeature(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGetFeature(GetFeatureType newGetFeature, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(WFSPackage.eINSTANCE.getDocumentRoot_GetFeature(), newGetFeature, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGetFeature(GetFeatureType newGetFeature) {
		((FeatureMap.Internal)getMixed()).set(WFSPackage.eINSTANCE.getDocumentRoot_GetFeature(), newGetFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QueryType getQuery() {
		return (QueryType)getMixed().get(WFSPackage.eINSTANCE.getDocumentRoot_Query(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetQuery(QueryType newQuery, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(WFSPackage.eINSTANCE.getDocumentRoot_Query(), newQuery, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setQuery(QueryType newQuery) {
		((FeatureMap.Internal)getMixed()).set(WFSPackage.eINSTANCE.getDocumentRoot_Query(), newQuery);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case WFSPackage.DOCUMENT_ROOT__MIXED:
					return ((InternalEList)getMixed()).basicRemove(otherEnd, msgs);
				case WFSPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
					return ((InternalEList)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
				case WFSPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
					return ((InternalEList)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
				case WFSPackage.DOCUMENT_ROOT__DESCRIBE_FEATURE_TYPE:
					return basicSetDescribeFeatureType(null, msgs);
				case WFSPackage.DOCUMENT_ROOT__FEATURE_COLLECTION:
					return basicSetFeatureCollection(null, msgs);
				case WFSPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
					return basicSetGetCapabilities(null, msgs);
				case WFSPackage.DOCUMENT_ROOT__GET_FEATURE:
					return basicSetGetFeature(null, msgs);
				case WFSPackage.DOCUMENT_ROOT__QUERY:
					return basicSetQuery(null, msgs);
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
			case WFSPackage.DOCUMENT_ROOT__MIXED:
				return getMixed();
			case WFSPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return getXMLNSPrefixMap();
			case WFSPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return getXSISchemaLocation();
			case WFSPackage.DOCUMENT_ROOT__DESCRIBE_FEATURE_TYPE:
				return getDescribeFeatureType();
			case WFSPackage.DOCUMENT_ROOT__FEATURE_COLLECTION:
				return getFeatureCollection();
			case WFSPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
				return getGetCapabilities();
			case WFSPackage.DOCUMENT_ROOT__GET_FEATURE:
				return getGetFeature();
			case WFSPackage.DOCUMENT_ROOT__QUERY:
				return getQuery();
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
			case WFSPackage.DOCUMENT_ROOT__MIXED:
				getMixed().clear();
				getMixed().addAll((Collection)newValue);
				return;
			case WFSPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				getXMLNSPrefixMap().clear();
				getXMLNSPrefixMap().addAll((Collection)newValue);
				return;
			case WFSPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				getXSISchemaLocation().clear();
				getXSISchemaLocation().addAll((Collection)newValue);
				return;
			case WFSPackage.DOCUMENT_ROOT__DESCRIBE_FEATURE_TYPE:
				setDescribeFeatureType((DescribeFeatureTypeType)newValue);
				return;
			case WFSPackage.DOCUMENT_ROOT__FEATURE_COLLECTION:
				setFeatureCollection((FeatureCollectionType)newValue);
				return;
			case WFSPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
				setGetCapabilities((GetCapabilitiesType)newValue);
				return;
			case WFSPackage.DOCUMENT_ROOT__GET_FEATURE:
				setGetFeature((GetFeatureType)newValue);
				return;
			case WFSPackage.DOCUMENT_ROOT__QUERY:
				setQuery((QueryType)newValue);
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
			case WFSPackage.DOCUMENT_ROOT__MIXED:
				getMixed().clear();
				return;
			case WFSPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				getXMLNSPrefixMap().clear();
				return;
			case WFSPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				getXSISchemaLocation().clear();
				return;
			case WFSPackage.DOCUMENT_ROOT__DESCRIBE_FEATURE_TYPE:
				setDescribeFeatureType((DescribeFeatureTypeType)null);
				return;
			case WFSPackage.DOCUMENT_ROOT__FEATURE_COLLECTION:
				setFeatureCollection((FeatureCollectionType)null);
				return;
			case WFSPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
				setGetCapabilities((GetCapabilitiesType)null);
				return;
			case WFSPackage.DOCUMENT_ROOT__GET_FEATURE:
				setGetFeature((GetFeatureType)null);
				return;
			case WFSPackage.DOCUMENT_ROOT__QUERY:
				setQuery((QueryType)null);
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
			case WFSPackage.DOCUMENT_ROOT__MIXED:
				return mixed != null && !mixed.isEmpty();
			case WFSPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
			case WFSPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
			case WFSPackage.DOCUMENT_ROOT__DESCRIBE_FEATURE_TYPE:
				return getDescribeFeatureType() != null;
			case WFSPackage.DOCUMENT_ROOT__FEATURE_COLLECTION:
				return getFeatureCollection() != null;
			case WFSPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
				return getGetCapabilities() != null;
			case WFSPackage.DOCUMENT_ROOT__GET_FEATURE:
				return getGetFeature() != null;
			case WFSPackage.DOCUMENT_ROOT__QUERY:
				return getQuery() != null;
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
		result.append(" (mixed: ");
		result.append(mixed);
		result.append(')');
		return result.toString();
	}

} //DocumentRootImpl
