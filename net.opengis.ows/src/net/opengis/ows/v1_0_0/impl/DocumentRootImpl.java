/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows.v1_0_0.impl;

import java.util.Collection;

import net.opengis.ows.v1_0_0.BoundingBoxType;
import net.opengis.ows.v1_0_0.CodeType;
import net.opengis.ows.v1_0_0.ContactType;
import net.opengis.ows.v1_0_0.DCPType;
import net.opengis.ows.v1_0_0.DocumentRoot;
import net.opengis.ows.v1_0_0.ExceptionReportType;
import net.opengis.ows.v1_0_0.ExceptionType;
import net.opengis.ows.v1_0_0.GetCapabilitiesType;
import net.opengis.ows.v1_0_0.HTTPType;
import net.opengis.ows.v1_0_0.KeywordsType;
import net.opengis.ows.v1_0_0.MetadataType;
import net.opengis.ows.v1_0_0.OWSPackage;
import net.opengis.ows.v1_0_0.OperationType;
import net.opengis.ows.v1_0_0.OperationsMetadataType;
import net.opengis.ows.v1_0_0.ResponsiblePartyType;
import net.opengis.ows.v1_0_0.ServiceIdentificationType;
import net.opengis.ows.v1_0_0.ServiceProviderType;
import net.opengis.ows.v1_0_0.WGS84BoundingBoxType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
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
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getContactInfo <em>Contact Info</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getIndividualName <em>Individual Name</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getKeywords <em>Keywords</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getOrganisationName <em>Organisation Name</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getPointOfContact <em>Point Of Contact</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getPositionName <em>Position Name</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getRole <em>Role</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getAbstractMetaData <em>Abstract Meta Data</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getAccessConstraints <em>Access Constraints</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getAvailableCRS <em>Available CRS</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getDcp <em>Dcp</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getException <em>Exception</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getExceptionReport <em>Exception Report</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getExtendedCapabilities <em>Extended Capabilities</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getFees <em>Fees</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getHttp <em>Http</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getLanguage <em>Language</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getOperation <em>Operation</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getOperationsMetadata <em>Operations Metadata</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getServiceIdentification <em>Service Identification</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getServiceProvider <em>Service Provider</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getSupportedCRS <em>Supported CRS</em>}</li>
 *   <li>{@link net.opengis.ows.v1_0_0.impl.DocumentRootImpl#getWgS84BoundingBox <em>Wg S84 Bounding Box</em>}</li>
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
	 * The default value of the '{@link #getAbstract() <em>Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAbstract()
	 * @generated
	 * @ordered
	 */
	protected static final String ABSTRACT_EDEFAULT = null;

	/**
	 * The default value of the '{@link #getIndividualName() <em>Individual Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndividualName()
	 * @generated
	 * @ordered
	 */
	protected static final String INDIVIDUAL_NAME_EDEFAULT = null;

	/**
	 * The default value of the '{@link #getOrganisationName() <em>Organisation Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOrganisationName()
	 * @generated
	 * @ordered
	 */
	protected static final String ORGANISATION_NAME_EDEFAULT = null;

	/**
	 * The default value of the '{@link #getPositionName() <em>Position Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPositionName()
	 * @generated
	 * @ordered
	 */
	protected static final String POSITION_NAME_EDEFAULT = null;

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
	protected static final String OUTPUT_FORMAT_EDEFAULT = null;

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
		return OWSPackage.eINSTANCE.getDocumentRoot();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getMixed() {
		if (mixed == null) {
			mixed = new BasicFeatureMap(this, OWSPackage.DOCUMENT_ROOT__MIXED);
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
			xMLNSPrefixMap = new EcoreEMap(EcorePackage.eINSTANCE.getEStringToStringMapEntry(), EStringToStringMapEntryImpl.class, this, OWSPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
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
			xSISchemaLocation = new EcoreEMap(EcorePackage.eINSTANCE.getEStringToStringMapEntry(), EStringToStringMapEntryImpl.class, this, OWSPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		}
		return xSISchemaLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAbstract() {
		return (String)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_Abstract(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAbstract(String newAbstract) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_Abstract(), newAbstract);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContactType getContactInfo() {
		return (ContactType)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_ContactInfo(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetContactInfo(ContactType newContactInfo, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_ContactInfo(), newContactInfo, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContactInfo(ContactType newContactInfo) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_ContactInfo(), newContactInfo);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIndividualName() {
		return (String)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_IndividualName(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIndividualName(String newIndividualName) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_IndividualName(), newIndividualName);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KeywordsType getKeywords() {
		return (KeywordsType)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_Keywords(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetKeywords(KeywordsType newKeywords, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_Keywords(), newKeywords, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKeywords(KeywordsType newKeywords) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_Keywords(), newKeywords);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOrganisationName() {
		return (String)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_OrganisationName(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOrganisationName(String newOrganisationName) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_OrganisationName(), newOrganisationName);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResponsiblePartyType getPointOfContact() {
		return (ResponsiblePartyType)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_PointOfContact(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPointOfContact(ResponsiblePartyType newPointOfContact, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_PointOfContact(), newPointOfContact, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPointOfContact(ResponsiblePartyType newPointOfContact) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_PointOfContact(), newPointOfContact);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPositionName() {
		return (String)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_PositionName(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPositionName(String newPositionName) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_PositionName(), newPositionName);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CodeType getRole() {
		return (CodeType)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_Role(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRole(CodeType newRole, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_Role(), newRole, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRole(CodeType newRole) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_Role(), newRole);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTitle() {
		return (String)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_Title(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTitle(String newTitle) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_Title(), newTitle);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getAbstractMetaData() {
		return (EObject)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_AbstractMetaData(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAbstractMetaData(EObject newAbstractMetaData, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_AbstractMetaData(), newAbstractMetaData, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAccessConstraints() {
		return (String)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_AccessConstraints(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAccessConstraints(String newAccessConstraints) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_AccessConstraints(), newAccessConstraints);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAvailableCRS() {
		return (String)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_AvailableCRS(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAvailableCRS(String newAvailableCRS) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_AvailableCRS(), newAvailableCRS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BoundingBoxType getBoundingBox() {
		return (BoundingBoxType)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_BoundingBox(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBoundingBox(BoundingBoxType newBoundingBox, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_BoundingBox(), newBoundingBox, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBoundingBox(BoundingBoxType newBoundingBox) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_BoundingBox(), newBoundingBox);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DCPType getDcp() {
		return (DCPType)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_Dcp(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDcp(DCPType newDcp, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_Dcp(), newDcp, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDcp(DCPType newDcp) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_Dcp(), newDcp);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExceptionType getException() {
		return (ExceptionType)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_Exception(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetException(ExceptionType newException, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_Exception(), newException, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setException(ExceptionType newException) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_Exception(), newException);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExceptionReportType getExceptionReport() {
		return (ExceptionReportType)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_ExceptionReport(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetExceptionReport(ExceptionReportType newExceptionReport, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_ExceptionReport(), newExceptionReport, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExceptionReport(ExceptionReportType newExceptionReport) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_ExceptionReport(), newExceptionReport);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getExtendedCapabilities() {
		return (EObject)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_ExtendedCapabilities(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetExtendedCapabilities(EObject newExtendedCapabilities, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_ExtendedCapabilities(), newExtendedCapabilities, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExtendedCapabilities(EObject newExtendedCapabilities) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_ExtendedCapabilities(), newExtendedCapabilities);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFees() {
		return (String)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_Fees(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFees(String newFees) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_Fees(), newFees);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GetCapabilitiesType getGetCapabilities() {
		return (GetCapabilitiesType)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_GetCapabilities(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGetCapabilities(GetCapabilitiesType newGetCapabilities, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_GetCapabilities(), newGetCapabilities, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGetCapabilities(GetCapabilitiesType newGetCapabilities) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_GetCapabilities(), newGetCapabilities);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HTTPType getHttp() {
		return (HTTPType)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_Http(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetHttp(HTTPType newHttp, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_Http(), newHttp, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHttp(HTTPType newHttp) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_Http(), newHttp);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CodeType getIdentifier() {
		return (CodeType)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_Identifier(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetIdentifier(CodeType newIdentifier, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_Identifier(), newIdentifier, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIdentifier(CodeType newIdentifier) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_Identifier(), newIdentifier);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLanguage() {
		return (String)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_Language(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLanguage(String newLanguage) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_Language(), newLanguage);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MetadataType getMetadata() {
		return (MetadataType)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_Metadata(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMetadata(MetadataType newMetadata, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_Metadata(), newMetadata, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMetadata(MetadataType newMetadata) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_Metadata(), newMetadata);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationType getOperation() {
		return (OperationType)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_Operation(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOperation(OperationType newOperation, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_Operation(), newOperation, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOperation(OperationType newOperation) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_Operation(), newOperation);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationsMetadataType getOperationsMetadata() {
		return (OperationsMetadataType)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_OperationsMetadata(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOperationsMetadata(OperationsMetadataType newOperationsMetadata, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_OperationsMetadata(), newOperationsMetadata, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOperationsMetadata(OperationsMetadataType newOperationsMetadata) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_OperationsMetadata(), newOperationsMetadata);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOutputFormat() {
		return (String)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_OutputFormat(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOutputFormat(String newOutputFormat) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_OutputFormat(), newOutputFormat);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ServiceIdentificationType getServiceIdentification() {
		return (ServiceIdentificationType)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_ServiceIdentification(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetServiceIdentification(ServiceIdentificationType newServiceIdentification, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_ServiceIdentification(), newServiceIdentification, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setServiceIdentification(ServiceIdentificationType newServiceIdentification) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_ServiceIdentification(), newServiceIdentification);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ServiceProviderType getServiceProvider() {
		return (ServiceProviderType)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_ServiceProvider(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetServiceProvider(ServiceProviderType newServiceProvider, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_ServiceProvider(), newServiceProvider, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setServiceProvider(ServiceProviderType newServiceProvider) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_ServiceProvider(), newServiceProvider);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSupportedCRS() {
		return (String)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_SupportedCRS(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSupportedCRS(String newSupportedCRS) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_SupportedCRS(), newSupportedCRS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WGS84BoundingBoxType getWgS84BoundingBox() {
		return (WGS84BoundingBoxType)getMixed().get(OWSPackage.eINSTANCE.getDocumentRoot_WgS84BoundingBox(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetWgS84BoundingBox(WGS84BoundingBoxType newWgS84BoundingBox, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(OWSPackage.eINSTANCE.getDocumentRoot_WgS84BoundingBox(), newWgS84BoundingBox, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWgS84BoundingBox(WGS84BoundingBoxType newWgS84BoundingBox) {
		((FeatureMap.Internal)getMixed()).set(OWSPackage.eINSTANCE.getDocumentRoot_WgS84BoundingBox(), newWgS84BoundingBox);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case OWSPackage.DOCUMENT_ROOT__MIXED:
					return ((InternalEList)getMixed()).basicRemove(otherEnd, msgs);
				case OWSPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
					return ((InternalEList)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
				case OWSPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
					return ((InternalEList)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
				case OWSPackage.DOCUMENT_ROOT__CONTACT_INFO:
					return basicSetContactInfo(null, msgs);
				case OWSPackage.DOCUMENT_ROOT__KEYWORDS:
					return basicSetKeywords(null, msgs);
				case OWSPackage.DOCUMENT_ROOT__POINT_OF_CONTACT:
					return basicSetPointOfContact(null, msgs);
				case OWSPackage.DOCUMENT_ROOT__ROLE:
					return basicSetRole(null, msgs);
				case OWSPackage.DOCUMENT_ROOT__ABSTRACT_META_DATA:
					return basicSetAbstractMetaData(null, msgs);
				case OWSPackage.DOCUMENT_ROOT__BOUNDING_BOX:
					return basicSetBoundingBox(null, msgs);
				case OWSPackage.DOCUMENT_ROOT__DCP:
					return basicSetDcp(null, msgs);
				case OWSPackage.DOCUMENT_ROOT__EXCEPTION:
					return basicSetException(null, msgs);
				case OWSPackage.DOCUMENT_ROOT__EXCEPTION_REPORT:
					return basicSetExceptionReport(null, msgs);
				case OWSPackage.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
					return basicSetExtendedCapabilities(null, msgs);
				case OWSPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
					return basicSetGetCapabilities(null, msgs);
				case OWSPackage.DOCUMENT_ROOT__HTTP:
					return basicSetHttp(null, msgs);
				case OWSPackage.DOCUMENT_ROOT__IDENTIFIER:
					return basicSetIdentifier(null, msgs);
				case OWSPackage.DOCUMENT_ROOT__METADATA:
					return basicSetMetadata(null, msgs);
				case OWSPackage.DOCUMENT_ROOT__OPERATION:
					return basicSetOperation(null, msgs);
				case OWSPackage.DOCUMENT_ROOT__OPERATIONS_METADATA:
					return basicSetOperationsMetadata(null, msgs);
				case OWSPackage.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
					return basicSetServiceIdentification(null, msgs);
				case OWSPackage.DOCUMENT_ROOT__SERVICE_PROVIDER:
					return basicSetServiceProvider(null, msgs);
				case OWSPackage.DOCUMENT_ROOT__WG_S84_BOUNDING_BOX:
					return basicSetWgS84BoundingBox(null, msgs);
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
			case OWSPackage.DOCUMENT_ROOT__MIXED:
				return getMixed();
			case OWSPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return getXMLNSPrefixMap();
			case OWSPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return getXSISchemaLocation();
			case OWSPackage.DOCUMENT_ROOT__ABSTRACT:
				return getAbstract();
			case OWSPackage.DOCUMENT_ROOT__CONTACT_INFO:
				return getContactInfo();
			case OWSPackage.DOCUMENT_ROOT__INDIVIDUAL_NAME:
				return getIndividualName();
			case OWSPackage.DOCUMENT_ROOT__KEYWORDS:
				return getKeywords();
			case OWSPackage.DOCUMENT_ROOT__ORGANISATION_NAME:
				return getOrganisationName();
			case OWSPackage.DOCUMENT_ROOT__POINT_OF_CONTACT:
				return getPointOfContact();
			case OWSPackage.DOCUMENT_ROOT__POSITION_NAME:
				return getPositionName();
			case OWSPackage.DOCUMENT_ROOT__ROLE:
				return getRole();
			case OWSPackage.DOCUMENT_ROOT__TITLE:
				return getTitle();
			case OWSPackage.DOCUMENT_ROOT__ABSTRACT_META_DATA:
				return getAbstractMetaData();
			case OWSPackage.DOCUMENT_ROOT__ACCESS_CONSTRAINTS:
				return getAccessConstraints();
			case OWSPackage.DOCUMENT_ROOT__AVAILABLE_CRS:
				return getAvailableCRS();
			case OWSPackage.DOCUMENT_ROOT__BOUNDING_BOX:
				return getBoundingBox();
			case OWSPackage.DOCUMENT_ROOT__DCP:
				return getDcp();
			case OWSPackage.DOCUMENT_ROOT__EXCEPTION:
				return getException();
			case OWSPackage.DOCUMENT_ROOT__EXCEPTION_REPORT:
				return getExceptionReport();
			case OWSPackage.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
				return getExtendedCapabilities();
			case OWSPackage.DOCUMENT_ROOT__FEES:
				return getFees();
			case OWSPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
				return getGetCapabilities();
			case OWSPackage.DOCUMENT_ROOT__HTTP:
				return getHttp();
			case OWSPackage.DOCUMENT_ROOT__IDENTIFIER:
				return getIdentifier();
			case OWSPackage.DOCUMENT_ROOT__LANGUAGE:
				return getLanguage();
			case OWSPackage.DOCUMENT_ROOT__METADATA:
				return getMetadata();
			case OWSPackage.DOCUMENT_ROOT__OPERATION:
				return getOperation();
			case OWSPackage.DOCUMENT_ROOT__OPERATIONS_METADATA:
				return getOperationsMetadata();
			case OWSPackage.DOCUMENT_ROOT__OUTPUT_FORMAT:
				return getOutputFormat();
			case OWSPackage.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
				return getServiceIdentification();
			case OWSPackage.DOCUMENT_ROOT__SERVICE_PROVIDER:
				return getServiceProvider();
			case OWSPackage.DOCUMENT_ROOT__SUPPORTED_CRS:
				return getSupportedCRS();
			case OWSPackage.DOCUMENT_ROOT__WG_S84_BOUNDING_BOX:
				return getWgS84BoundingBox();
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
			case OWSPackage.DOCUMENT_ROOT__MIXED:
				getMixed().clear();
				getMixed().addAll((Collection)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				getXMLNSPrefixMap().clear();
				getXMLNSPrefixMap().addAll((Collection)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				getXSISchemaLocation().clear();
				getXSISchemaLocation().addAll((Collection)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__ABSTRACT:
				setAbstract((String)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__CONTACT_INFO:
				setContactInfo((ContactType)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__INDIVIDUAL_NAME:
				setIndividualName((String)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__KEYWORDS:
				setKeywords((KeywordsType)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__ORGANISATION_NAME:
				setOrganisationName((String)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__POINT_OF_CONTACT:
				setPointOfContact((ResponsiblePartyType)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__POSITION_NAME:
				setPositionName((String)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__ROLE:
				setRole((CodeType)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__TITLE:
				setTitle((String)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__ACCESS_CONSTRAINTS:
				setAccessConstraints((String)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__AVAILABLE_CRS:
				setAvailableCRS((String)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__BOUNDING_BOX:
				setBoundingBox((BoundingBoxType)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__DCP:
				setDcp((DCPType)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__EXCEPTION:
				setException((ExceptionType)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__EXCEPTION_REPORT:
				setExceptionReport((ExceptionReportType)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
				setExtendedCapabilities((EObject)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__FEES:
				setFees((String)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
				setGetCapabilities((GetCapabilitiesType)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__HTTP:
				setHttp((HTTPType)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__IDENTIFIER:
				setIdentifier((CodeType)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__LANGUAGE:
				setLanguage((String)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__METADATA:
				setMetadata((MetadataType)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__OPERATION:
				setOperation((OperationType)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__OPERATIONS_METADATA:
				setOperationsMetadata((OperationsMetadataType)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__OUTPUT_FORMAT:
				setOutputFormat((String)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
				setServiceIdentification((ServiceIdentificationType)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__SERVICE_PROVIDER:
				setServiceProvider((ServiceProviderType)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__SUPPORTED_CRS:
				setSupportedCRS((String)newValue);
				return;
			case OWSPackage.DOCUMENT_ROOT__WG_S84_BOUNDING_BOX:
				setWgS84BoundingBox((WGS84BoundingBoxType)newValue);
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
			case OWSPackage.DOCUMENT_ROOT__MIXED:
				getMixed().clear();
				return;
			case OWSPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				getXMLNSPrefixMap().clear();
				return;
			case OWSPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				getXSISchemaLocation().clear();
				return;
			case OWSPackage.DOCUMENT_ROOT__ABSTRACT:
				setAbstract(ABSTRACT_EDEFAULT);
				return;
			case OWSPackage.DOCUMENT_ROOT__CONTACT_INFO:
				setContactInfo((ContactType)null);
				return;
			case OWSPackage.DOCUMENT_ROOT__INDIVIDUAL_NAME:
				setIndividualName(INDIVIDUAL_NAME_EDEFAULT);
				return;
			case OWSPackage.DOCUMENT_ROOT__KEYWORDS:
				setKeywords((KeywordsType)null);
				return;
			case OWSPackage.DOCUMENT_ROOT__ORGANISATION_NAME:
				setOrganisationName(ORGANISATION_NAME_EDEFAULT);
				return;
			case OWSPackage.DOCUMENT_ROOT__POINT_OF_CONTACT:
				setPointOfContact((ResponsiblePartyType)null);
				return;
			case OWSPackage.DOCUMENT_ROOT__POSITION_NAME:
				setPositionName(POSITION_NAME_EDEFAULT);
				return;
			case OWSPackage.DOCUMENT_ROOT__ROLE:
				setRole((CodeType)null);
				return;
			case OWSPackage.DOCUMENT_ROOT__TITLE:
				setTitle(TITLE_EDEFAULT);
				return;
			case OWSPackage.DOCUMENT_ROOT__ACCESS_CONSTRAINTS:
				setAccessConstraints(ACCESS_CONSTRAINTS_EDEFAULT);
				return;
			case OWSPackage.DOCUMENT_ROOT__AVAILABLE_CRS:
				setAvailableCRS(AVAILABLE_CRS_EDEFAULT);
				return;
			case OWSPackage.DOCUMENT_ROOT__BOUNDING_BOX:
				setBoundingBox((BoundingBoxType)null);
				return;
			case OWSPackage.DOCUMENT_ROOT__DCP:
				setDcp((DCPType)null);
				return;
			case OWSPackage.DOCUMENT_ROOT__EXCEPTION:
				setException((ExceptionType)null);
				return;
			case OWSPackage.DOCUMENT_ROOT__EXCEPTION_REPORT:
				setExceptionReport((ExceptionReportType)null);
				return;
			case OWSPackage.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
				setExtendedCapabilities((EObject)null);
				return;
			case OWSPackage.DOCUMENT_ROOT__FEES:
				setFees(FEES_EDEFAULT);
				return;
			case OWSPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
				setGetCapabilities((GetCapabilitiesType)null);
				return;
			case OWSPackage.DOCUMENT_ROOT__HTTP:
				setHttp((HTTPType)null);
				return;
			case OWSPackage.DOCUMENT_ROOT__IDENTIFIER:
				setIdentifier((CodeType)null);
				return;
			case OWSPackage.DOCUMENT_ROOT__LANGUAGE:
				setLanguage(LANGUAGE_EDEFAULT);
				return;
			case OWSPackage.DOCUMENT_ROOT__METADATA:
				setMetadata((MetadataType)null);
				return;
			case OWSPackage.DOCUMENT_ROOT__OPERATION:
				setOperation((OperationType)null);
				return;
			case OWSPackage.DOCUMENT_ROOT__OPERATIONS_METADATA:
				setOperationsMetadata((OperationsMetadataType)null);
				return;
			case OWSPackage.DOCUMENT_ROOT__OUTPUT_FORMAT:
				setOutputFormat(OUTPUT_FORMAT_EDEFAULT);
				return;
			case OWSPackage.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
				setServiceIdentification((ServiceIdentificationType)null);
				return;
			case OWSPackage.DOCUMENT_ROOT__SERVICE_PROVIDER:
				setServiceProvider((ServiceProviderType)null);
				return;
			case OWSPackage.DOCUMENT_ROOT__SUPPORTED_CRS:
				setSupportedCRS(SUPPORTED_CRS_EDEFAULT);
				return;
			case OWSPackage.DOCUMENT_ROOT__WG_S84_BOUNDING_BOX:
				setWgS84BoundingBox((WGS84BoundingBoxType)null);
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
			case OWSPackage.DOCUMENT_ROOT__MIXED:
				return mixed != null && !mixed.isEmpty();
			case OWSPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
			case OWSPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
			case OWSPackage.DOCUMENT_ROOT__ABSTRACT:
				return ABSTRACT_EDEFAULT == null ? getAbstract() != null : !ABSTRACT_EDEFAULT.equals(getAbstract());
			case OWSPackage.DOCUMENT_ROOT__CONTACT_INFO:
				return getContactInfo() != null;
			case OWSPackage.DOCUMENT_ROOT__INDIVIDUAL_NAME:
				return INDIVIDUAL_NAME_EDEFAULT == null ? getIndividualName() != null : !INDIVIDUAL_NAME_EDEFAULT.equals(getIndividualName());
			case OWSPackage.DOCUMENT_ROOT__KEYWORDS:
				return getKeywords() != null;
			case OWSPackage.DOCUMENT_ROOT__ORGANISATION_NAME:
				return ORGANISATION_NAME_EDEFAULT == null ? getOrganisationName() != null : !ORGANISATION_NAME_EDEFAULT.equals(getOrganisationName());
			case OWSPackage.DOCUMENT_ROOT__POINT_OF_CONTACT:
				return getPointOfContact() != null;
			case OWSPackage.DOCUMENT_ROOT__POSITION_NAME:
				return POSITION_NAME_EDEFAULT == null ? getPositionName() != null : !POSITION_NAME_EDEFAULT.equals(getPositionName());
			case OWSPackage.DOCUMENT_ROOT__ROLE:
				return getRole() != null;
			case OWSPackage.DOCUMENT_ROOT__TITLE:
				return TITLE_EDEFAULT == null ? getTitle() != null : !TITLE_EDEFAULT.equals(getTitle());
			case OWSPackage.DOCUMENT_ROOT__ABSTRACT_META_DATA:
				return getAbstractMetaData() != null;
			case OWSPackage.DOCUMENT_ROOT__ACCESS_CONSTRAINTS:
				return ACCESS_CONSTRAINTS_EDEFAULT == null ? getAccessConstraints() != null : !ACCESS_CONSTRAINTS_EDEFAULT.equals(getAccessConstraints());
			case OWSPackage.DOCUMENT_ROOT__AVAILABLE_CRS:
				return AVAILABLE_CRS_EDEFAULT == null ? getAvailableCRS() != null : !AVAILABLE_CRS_EDEFAULT.equals(getAvailableCRS());
			case OWSPackage.DOCUMENT_ROOT__BOUNDING_BOX:
				return getBoundingBox() != null;
			case OWSPackage.DOCUMENT_ROOT__DCP:
				return getDcp() != null;
			case OWSPackage.DOCUMENT_ROOT__EXCEPTION:
				return getException() != null;
			case OWSPackage.DOCUMENT_ROOT__EXCEPTION_REPORT:
				return getExceptionReport() != null;
			case OWSPackage.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
				return getExtendedCapabilities() != null;
			case OWSPackage.DOCUMENT_ROOT__FEES:
				return FEES_EDEFAULT == null ? getFees() != null : !FEES_EDEFAULT.equals(getFees());
			case OWSPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
				return getGetCapabilities() != null;
			case OWSPackage.DOCUMENT_ROOT__HTTP:
				return getHttp() != null;
			case OWSPackage.DOCUMENT_ROOT__IDENTIFIER:
				return getIdentifier() != null;
			case OWSPackage.DOCUMENT_ROOT__LANGUAGE:
				return LANGUAGE_EDEFAULT == null ? getLanguage() != null : !LANGUAGE_EDEFAULT.equals(getLanguage());
			case OWSPackage.DOCUMENT_ROOT__METADATA:
				return getMetadata() != null;
			case OWSPackage.DOCUMENT_ROOT__OPERATION:
				return getOperation() != null;
			case OWSPackage.DOCUMENT_ROOT__OPERATIONS_METADATA:
				return getOperationsMetadata() != null;
			case OWSPackage.DOCUMENT_ROOT__OUTPUT_FORMAT:
				return OUTPUT_FORMAT_EDEFAULT == null ? getOutputFormat() != null : !OUTPUT_FORMAT_EDEFAULT.equals(getOutputFormat());
			case OWSPackage.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
				return getServiceIdentification() != null;
			case OWSPackage.DOCUMENT_ROOT__SERVICE_PROVIDER:
				return getServiceProvider() != null;
			case OWSPackage.DOCUMENT_ROOT__SUPPORTED_CRS:
				return SUPPORTED_CRS_EDEFAULT == null ? getSupportedCRS() != null : !SUPPORTED_CRS_EDEFAULT.equals(getSupportedCRS());
			case OWSPackage.DOCUMENT_ROOT__WG_S84_BOUNDING_BOX:
				return getWgS84BoundingBox() != null;
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
