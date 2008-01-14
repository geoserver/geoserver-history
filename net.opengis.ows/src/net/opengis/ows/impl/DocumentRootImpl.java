/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows.impl;

import net.opengis.ows.BoundingBoxType;
import net.opengis.ows.CodeType;
import net.opengis.ows.ContactType;
import net.opengis.ows.DCPType;
import net.opengis.ows.DocumentRoot;
import net.opengis.ows.ExceptionReportType;
import net.opengis.ows.ExceptionType;
import net.opengis.ows.GetCapabilitiesType;
import net.opengis.ows.HTTPType;
import net.opengis.ows.KeywordsType;
import net.opengis.ows.MetadataType;
import net.opengis.ows.OperationType;
import net.opengis.ows.OperationsMetadataType;
import net.opengis.ows.OwsPackage;
import net.opengis.ows.ResponsiblePartyType;
import net.opengis.ows.ServiceIdentificationType;
import net.opengis.ows.ServiceProviderType;
import net.opengis.ows.WGS84BoundingBoxType;

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
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getContactInfo <em>Contact Info</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getIndividualName <em>Individual Name</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getKeywords <em>Keywords</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getOrganisationName <em>Organisation Name</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getPointOfContact <em>Point Of Contact</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getPositionName <em>Position Name</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getRole <em>Role</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getAbstractMetaData <em>Abstract Meta Data</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getAccessConstraints <em>Access Constraints</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getAvailableCRS <em>Available CRS</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getDcp <em>Dcp</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getException <em>Exception</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getExceptionReport <em>Exception Report</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getExtendedCapabilities <em>Extended Capabilities</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getFees <em>Fees</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getHttp <em>Http</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getLanguage <em>Language</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getOperation <em>Operation</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getOperationsMetadata <em>Operations Metadata</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getServiceIdentification <em>Service Identification</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getServiceProvider <em>Service Provider</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getSupportedCRS <em>Supported CRS</em>}</li>
 *   <li>{@link net.opengis.ows.impl.DocumentRootImpl#getWgS84BoundingBox <em>Wg S84 Bounding Box</em>}</li>
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
	protected FeatureMap mixed= null;

    /**
     * The cached value of the '{@link #getXMLNSPrefixMap() <em>XMLNS Prefix Map</em>}' map.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getXMLNSPrefixMap()
     * @generated
     * @ordered
     */
	protected EMap xMLNSPrefixMap= null;

    /**
     * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema Location</em>}' map.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getXSISchemaLocation()
     * @generated
     * @ordered
     */
	protected EMap xSISchemaLocation= null;

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
        return OwsPackage.Literals.DOCUMENT_ROOT;
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public FeatureMap getMixed() {
        if (mixed == null) {
            mixed = new BasicFeatureMap(this, OwsPackage.DOCUMENT_ROOT__MIXED);
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
            xMLNSPrefixMap = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, OwsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
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
            xSISchemaLocation = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, OwsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        }
        return xSISchemaLocation;
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getAbstract() {
        return (String)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__ABSTRACT, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setAbstract(String newAbstract) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__ABSTRACT, newAbstract);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public ContactType getContactInfo() {
        return (ContactType)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__CONTACT_INFO, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetContactInfo(ContactType newContactInfo, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__CONTACT_INFO, newContactInfo, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setContactInfo(ContactType newContactInfo) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__CONTACT_INFO, newContactInfo);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getIndividualName() {
        return (String)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__INDIVIDUAL_NAME, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setIndividualName(String newIndividualName) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__INDIVIDUAL_NAME, newIndividualName);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public KeywordsType getKeywords() {
        return (KeywordsType)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__KEYWORDS, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetKeywords(KeywordsType newKeywords, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__KEYWORDS, newKeywords, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setKeywords(KeywordsType newKeywords) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__KEYWORDS, newKeywords);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getOrganisationName() {
        return (String)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__ORGANISATION_NAME, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setOrganisationName(String newOrganisationName) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__ORGANISATION_NAME, newOrganisationName);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public ResponsiblePartyType getPointOfContact() {
        return (ResponsiblePartyType)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__POINT_OF_CONTACT, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetPointOfContact(ResponsiblePartyType newPointOfContact, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__POINT_OF_CONTACT, newPointOfContact, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setPointOfContact(ResponsiblePartyType newPointOfContact) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__POINT_OF_CONTACT, newPointOfContact);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getPositionName() {
        return (String)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__POSITION_NAME, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setPositionName(String newPositionName) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__POSITION_NAME, newPositionName);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public CodeType getRole() {
        return (CodeType)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__ROLE, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetRole(CodeType newRole, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__ROLE, newRole, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setRole(CodeType newRole) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__ROLE, newRole);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getTitle() {
        return (String)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__TITLE, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setTitle(String newTitle) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__TITLE, newTitle);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EObject getAbstractMetaData() {
        return (EObject)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__ABSTRACT_META_DATA, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetAbstractMetaData(EObject newAbstractMetaData, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__ABSTRACT_META_DATA, newAbstractMetaData, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getAccessConstraints() {
        return (String)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__ACCESS_CONSTRAINTS, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setAccessConstraints(String newAccessConstraints) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__ACCESS_CONSTRAINTS, newAccessConstraints);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getAvailableCRS() {
        return (String)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__AVAILABLE_CRS, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setAvailableCRS(String newAvailableCRS) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__AVAILABLE_CRS, newAvailableCRS);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public BoundingBoxType getBoundingBox() {
        return (BoundingBoxType)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__BOUNDING_BOX, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetBoundingBox(BoundingBoxType newBoundingBox, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__BOUNDING_BOX, newBoundingBox, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setBoundingBox(BoundingBoxType newBoundingBox) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__BOUNDING_BOX, newBoundingBox);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public DCPType getDcp() {
        return (DCPType)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__DCP, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetDcp(DCPType newDcp, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__DCP, newDcp, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setDcp(DCPType newDcp) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__DCP, newDcp);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public ExceptionType getException() {
        return (ExceptionType)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__EXCEPTION, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetException(ExceptionType newException, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__EXCEPTION, newException, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setException(ExceptionType newException) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__EXCEPTION, newException);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public ExceptionReportType getExceptionReport() {
        return (ExceptionReportType)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__EXCEPTION_REPORT, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetExceptionReport(ExceptionReportType newExceptionReport, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__EXCEPTION_REPORT, newExceptionReport, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setExceptionReport(ExceptionReportType newExceptionReport) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__EXCEPTION_REPORT, newExceptionReport);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EObject getExtendedCapabilities() {
        return (EObject)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__EXTENDED_CAPABILITIES, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetExtendedCapabilities(EObject newExtendedCapabilities, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__EXTENDED_CAPABILITIES, newExtendedCapabilities, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setExtendedCapabilities(EObject newExtendedCapabilities) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__EXTENDED_CAPABILITIES, newExtendedCapabilities);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getFees() {
        return (String)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__FEES, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setFees(String newFees) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__FEES, newFees);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public GetCapabilitiesType getGetCapabilities() {
        return (GetCapabilitiesType)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetGetCapabilities(GetCapabilitiesType newGetCapabilities, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, newGetCapabilities, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setGetCapabilities(GetCapabilitiesType newGetCapabilities) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, newGetCapabilities);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public HTTPType getHttp() {
        return (HTTPType)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__HTTP, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetHttp(HTTPType newHttp, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__HTTP, newHttp, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setHttp(HTTPType newHttp) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__HTTP, newHttp);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public CodeType getIdentifier() {
        return (CodeType)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__IDENTIFIER, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetIdentifier(CodeType newIdentifier, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__IDENTIFIER, newIdentifier, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setIdentifier(CodeType newIdentifier) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__IDENTIFIER, newIdentifier);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getLanguage() {
        return (String)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__LANGUAGE, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setLanguage(String newLanguage) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__LANGUAGE, newLanguage);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public MetadataType getMetadata() {
        return (MetadataType)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__METADATA, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetMetadata(MetadataType newMetadata, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__METADATA, newMetadata, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setMetadata(MetadataType newMetadata) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__METADATA, newMetadata);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public OperationType getOperation() {
        return (OperationType)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__OPERATION, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetOperation(OperationType newOperation, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__OPERATION, newOperation, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setOperation(OperationType newOperation) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__OPERATION, newOperation);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public OperationsMetadataType getOperationsMetadata() {
        return (OperationsMetadataType)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__OPERATIONS_METADATA, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetOperationsMetadata(OperationsMetadataType newOperationsMetadata, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__OPERATIONS_METADATA, newOperationsMetadata, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setOperationsMetadata(OperationsMetadataType newOperationsMetadata) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__OPERATIONS_METADATA, newOperationsMetadata);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getOutputFormat() {
        return (String)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__OUTPUT_FORMAT, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setOutputFormat(String newOutputFormat) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__OUTPUT_FORMAT, newOutputFormat);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public ServiceIdentificationType getServiceIdentification() {
        return (ServiceIdentificationType)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__SERVICE_IDENTIFICATION, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetServiceIdentification(ServiceIdentificationType newServiceIdentification, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__SERVICE_IDENTIFICATION, newServiceIdentification, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setServiceIdentification(ServiceIdentificationType newServiceIdentification) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__SERVICE_IDENTIFICATION, newServiceIdentification);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public ServiceProviderType getServiceProvider() {
        return (ServiceProviderType)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__SERVICE_PROVIDER, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetServiceProvider(ServiceProviderType newServiceProvider, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__SERVICE_PROVIDER, newServiceProvider, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setServiceProvider(ServiceProviderType newServiceProvider) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__SERVICE_PROVIDER, newServiceProvider);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getSupportedCRS() {
        return (String)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__SUPPORTED_CRS, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setSupportedCRS(String newSupportedCRS) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__SUPPORTED_CRS, newSupportedCRS);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public WGS84BoundingBoxType getWgS84BoundingBox() {
        return (WGS84BoundingBoxType)getMixed().get(OwsPackage.Literals.DOCUMENT_ROOT__WG_S84_BOUNDING_BOX, true);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetWgS84BoundingBox(WGS84BoundingBoxType newWgS84BoundingBox, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(OwsPackage.Literals.DOCUMENT_ROOT__WG_S84_BOUNDING_BOX, newWgS84BoundingBox, msgs);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setWgS84BoundingBox(WGS84BoundingBoxType newWgS84BoundingBox) {
        ((FeatureMap.Internal)getMixed()).set(OwsPackage.Literals.DOCUMENT_ROOT__WG_S84_BOUNDING_BOX, newWgS84BoundingBox);
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case OwsPackage.DOCUMENT_ROOT__MIXED:
                return ((InternalEList)getMixed()).basicRemove(otherEnd, msgs);
            case OwsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return ((InternalEList)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
            case OwsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return ((InternalEList)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
            case OwsPackage.DOCUMENT_ROOT__CONTACT_INFO:
                return basicSetContactInfo(null, msgs);
            case OwsPackage.DOCUMENT_ROOT__KEYWORDS:
                return basicSetKeywords(null, msgs);
            case OwsPackage.DOCUMENT_ROOT__POINT_OF_CONTACT:
                return basicSetPointOfContact(null, msgs);
            case OwsPackage.DOCUMENT_ROOT__ROLE:
                return basicSetRole(null, msgs);
            case OwsPackage.DOCUMENT_ROOT__ABSTRACT_META_DATA:
                return basicSetAbstractMetaData(null, msgs);
            case OwsPackage.DOCUMENT_ROOT__BOUNDING_BOX:
                return basicSetBoundingBox(null, msgs);
            case OwsPackage.DOCUMENT_ROOT__DCP:
                return basicSetDcp(null, msgs);
            case OwsPackage.DOCUMENT_ROOT__EXCEPTION:
                return basicSetException(null, msgs);
            case OwsPackage.DOCUMENT_ROOT__EXCEPTION_REPORT:
                return basicSetExceptionReport(null, msgs);
            case OwsPackage.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
                return basicSetExtendedCapabilities(null, msgs);
            case OwsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                return basicSetGetCapabilities(null, msgs);
            case OwsPackage.DOCUMENT_ROOT__HTTP:
                return basicSetHttp(null, msgs);
            case OwsPackage.DOCUMENT_ROOT__IDENTIFIER:
                return basicSetIdentifier(null, msgs);
            case OwsPackage.DOCUMENT_ROOT__METADATA:
                return basicSetMetadata(null, msgs);
            case OwsPackage.DOCUMENT_ROOT__OPERATION:
                return basicSetOperation(null, msgs);
            case OwsPackage.DOCUMENT_ROOT__OPERATIONS_METADATA:
                return basicSetOperationsMetadata(null, msgs);
            case OwsPackage.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
                return basicSetServiceIdentification(null, msgs);
            case OwsPackage.DOCUMENT_ROOT__SERVICE_PROVIDER:
                return basicSetServiceProvider(null, msgs);
            case OwsPackage.DOCUMENT_ROOT__WG_S84_BOUNDING_BOX:
                return basicSetWgS84BoundingBox(null, msgs);
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
            case OwsPackage.DOCUMENT_ROOT__MIXED:
                if (coreType) return getMixed();
                return ((FeatureMap.Internal)getMixed()).getWrapper();
            case OwsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                if (coreType) return getXMLNSPrefixMap();
                else return getXMLNSPrefixMap().map();
            case OwsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                if (coreType) return getXSISchemaLocation();
                else return getXSISchemaLocation().map();
            case OwsPackage.DOCUMENT_ROOT__ABSTRACT:
                return getAbstract();
            case OwsPackage.DOCUMENT_ROOT__CONTACT_INFO:
                return getContactInfo();
            case OwsPackage.DOCUMENT_ROOT__INDIVIDUAL_NAME:
                return getIndividualName();
            case OwsPackage.DOCUMENT_ROOT__KEYWORDS:
                return getKeywords();
            case OwsPackage.DOCUMENT_ROOT__ORGANISATION_NAME:
                return getOrganisationName();
            case OwsPackage.DOCUMENT_ROOT__POINT_OF_CONTACT:
                return getPointOfContact();
            case OwsPackage.DOCUMENT_ROOT__POSITION_NAME:
                return getPositionName();
            case OwsPackage.DOCUMENT_ROOT__ROLE:
                return getRole();
            case OwsPackage.DOCUMENT_ROOT__TITLE:
                return getTitle();
            case OwsPackage.DOCUMENT_ROOT__ABSTRACT_META_DATA:
                return getAbstractMetaData();
            case OwsPackage.DOCUMENT_ROOT__ACCESS_CONSTRAINTS:
                return getAccessConstraints();
            case OwsPackage.DOCUMENT_ROOT__AVAILABLE_CRS:
                return getAvailableCRS();
            case OwsPackage.DOCUMENT_ROOT__BOUNDING_BOX:
                return getBoundingBox();
            case OwsPackage.DOCUMENT_ROOT__DCP:
                return getDcp();
            case OwsPackage.DOCUMENT_ROOT__EXCEPTION:
                return getException();
            case OwsPackage.DOCUMENT_ROOT__EXCEPTION_REPORT:
                return getExceptionReport();
            case OwsPackage.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
                return getExtendedCapabilities();
            case OwsPackage.DOCUMENT_ROOT__FEES:
                return getFees();
            case OwsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                return getGetCapabilities();
            case OwsPackage.DOCUMENT_ROOT__HTTP:
                return getHttp();
            case OwsPackage.DOCUMENT_ROOT__IDENTIFIER:
                return getIdentifier();
            case OwsPackage.DOCUMENT_ROOT__LANGUAGE:
                return getLanguage();
            case OwsPackage.DOCUMENT_ROOT__METADATA:
                return getMetadata();
            case OwsPackage.DOCUMENT_ROOT__OPERATION:
                return getOperation();
            case OwsPackage.DOCUMENT_ROOT__OPERATIONS_METADATA:
                return getOperationsMetadata();
            case OwsPackage.DOCUMENT_ROOT__OUTPUT_FORMAT:
                return getOutputFormat();
            case OwsPackage.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
                return getServiceIdentification();
            case OwsPackage.DOCUMENT_ROOT__SERVICE_PROVIDER:
                return getServiceProvider();
            case OwsPackage.DOCUMENT_ROOT__SUPPORTED_CRS:
                return getSupportedCRS();
            case OwsPackage.DOCUMENT_ROOT__WG_S84_BOUNDING_BOX:
                return getWgS84BoundingBox();
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
            case OwsPackage.DOCUMENT_ROOT__MIXED:
                ((FeatureMap.Internal)getMixed()).set(newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                ((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                ((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__ABSTRACT:
                setAbstract((String)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__CONTACT_INFO:
                setContactInfo((ContactType)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__INDIVIDUAL_NAME:
                setIndividualName((String)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__KEYWORDS:
                setKeywords((KeywordsType)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__ORGANISATION_NAME:
                setOrganisationName((String)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__POINT_OF_CONTACT:
                setPointOfContact((ResponsiblePartyType)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__POSITION_NAME:
                setPositionName((String)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__ROLE:
                setRole((CodeType)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__TITLE:
                setTitle((String)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__ACCESS_CONSTRAINTS:
                setAccessConstraints((String)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__AVAILABLE_CRS:
                setAvailableCRS((String)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__BOUNDING_BOX:
                setBoundingBox((BoundingBoxType)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__DCP:
                setDcp((DCPType)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__EXCEPTION:
                setException((ExceptionType)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__EXCEPTION_REPORT:
                setExceptionReport((ExceptionReportType)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
                setExtendedCapabilities((EObject)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__FEES:
                setFees((String)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                setGetCapabilities((GetCapabilitiesType)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__HTTP:
                setHttp((HTTPType)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__IDENTIFIER:
                setIdentifier((CodeType)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__LANGUAGE:
                setLanguage((String)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__METADATA:
                setMetadata((MetadataType)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__OPERATION:
                setOperation((OperationType)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__OPERATIONS_METADATA:
                setOperationsMetadata((OperationsMetadataType)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__OUTPUT_FORMAT:
                setOutputFormat((String)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
                setServiceIdentification((ServiceIdentificationType)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__SERVICE_PROVIDER:
                setServiceProvider((ServiceProviderType)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__SUPPORTED_CRS:
                setSupportedCRS((String)newValue);
                return;
            case OwsPackage.DOCUMENT_ROOT__WG_S84_BOUNDING_BOX:
                setWgS84BoundingBox((WGS84BoundingBoxType)newValue);
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
            case OwsPackage.DOCUMENT_ROOT__MIXED:
                getMixed().clear();
                return;
            case OwsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                getXMLNSPrefixMap().clear();
                return;
            case OwsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                getXSISchemaLocation().clear();
                return;
            case OwsPackage.DOCUMENT_ROOT__ABSTRACT:
                setAbstract(ABSTRACT_EDEFAULT);
                return;
            case OwsPackage.DOCUMENT_ROOT__CONTACT_INFO:
                setContactInfo((ContactType)null);
                return;
            case OwsPackage.DOCUMENT_ROOT__INDIVIDUAL_NAME:
                setIndividualName(INDIVIDUAL_NAME_EDEFAULT);
                return;
            case OwsPackage.DOCUMENT_ROOT__KEYWORDS:
                setKeywords((KeywordsType)null);
                return;
            case OwsPackage.DOCUMENT_ROOT__ORGANISATION_NAME:
                setOrganisationName(ORGANISATION_NAME_EDEFAULT);
                return;
            case OwsPackage.DOCUMENT_ROOT__POINT_OF_CONTACT:
                setPointOfContact((ResponsiblePartyType)null);
                return;
            case OwsPackage.DOCUMENT_ROOT__POSITION_NAME:
                setPositionName(POSITION_NAME_EDEFAULT);
                return;
            case OwsPackage.DOCUMENT_ROOT__ROLE:
                setRole((CodeType)null);
                return;
            case OwsPackage.DOCUMENT_ROOT__TITLE:
                setTitle(TITLE_EDEFAULT);
                return;
            case OwsPackage.DOCUMENT_ROOT__ACCESS_CONSTRAINTS:
                setAccessConstraints(ACCESS_CONSTRAINTS_EDEFAULT);
                return;
            case OwsPackage.DOCUMENT_ROOT__AVAILABLE_CRS:
                setAvailableCRS(AVAILABLE_CRS_EDEFAULT);
                return;
            case OwsPackage.DOCUMENT_ROOT__BOUNDING_BOX:
                setBoundingBox((BoundingBoxType)null);
                return;
            case OwsPackage.DOCUMENT_ROOT__DCP:
                setDcp((DCPType)null);
                return;
            case OwsPackage.DOCUMENT_ROOT__EXCEPTION:
                setException((ExceptionType)null);
                return;
            case OwsPackage.DOCUMENT_ROOT__EXCEPTION_REPORT:
                setExceptionReport((ExceptionReportType)null);
                return;
            case OwsPackage.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
                setExtendedCapabilities((EObject)null);
                return;
            case OwsPackage.DOCUMENT_ROOT__FEES:
                setFees(FEES_EDEFAULT);
                return;
            case OwsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                setGetCapabilities((GetCapabilitiesType)null);
                return;
            case OwsPackage.DOCUMENT_ROOT__HTTP:
                setHttp((HTTPType)null);
                return;
            case OwsPackage.DOCUMENT_ROOT__IDENTIFIER:
                setIdentifier((CodeType)null);
                return;
            case OwsPackage.DOCUMENT_ROOT__LANGUAGE:
                setLanguage(LANGUAGE_EDEFAULT);
                return;
            case OwsPackage.DOCUMENT_ROOT__METADATA:
                setMetadata((MetadataType)null);
                return;
            case OwsPackage.DOCUMENT_ROOT__OPERATION:
                setOperation((OperationType)null);
                return;
            case OwsPackage.DOCUMENT_ROOT__OPERATIONS_METADATA:
                setOperationsMetadata((OperationsMetadataType)null);
                return;
            case OwsPackage.DOCUMENT_ROOT__OUTPUT_FORMAT:
                setOutputFormat(OUTPUT_FORMAT_EDEFAULT);
                return;
            case OwsPackage.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
                setServiceIdentification((ServiceIdentificationType)null);
                return;
            case OwsPackage.DOCUMENT_ROOT__SERVICE_PROVIDER:
                setServiceProvider((ServiceProviderType)null);
                return;
            case OwsPackage.DOCUMENT_ROOT__SUPPORTED_CRS:
                setSupportedCRS(SUPPORTED_CRS_EDEFAULT);
                return;
            case OwsPackage.DOCUMENT_ROOT__WG_S84_BOUNDING_BOX:
                setWgS84BoundingBox((WGS84BoundingBoxType)null);
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
            case OwsPackage.DOCUMENT_ROOT__MIXED:
                return mixed != null && !mixed.isEmpty();
            case OwsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
            case OwsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
            case OwsPackage.DOCUMENT_ROOT__ABSTRACT:
                return ABSTRACT_EDEFAULT == null ? getAbstract() != null : !ABSTRACT_EDEFAULT.equals(getAbstract());
            case OwsPackage.DOCUMENT_ROOT__CONTACT_INFO:
                return getContactInfo() != null;
            case OwsPackage.DOCUMENT_ROOT__INDIVIDUAL_NAME:
                return INDIVIDUAL_NAME_EDEFAULT == null ? getIndividualName() != null : !INDIVIDUAL_NAME_EDEFAULT.equals(getIndividualName());
            case OwsPackage.DOCUMENT_ROOT__KEYWORDS:
                return getKeywords() != null;
            case OwsPackage.DOCUMENT_ROOT__ORGANISATION_NAME:
                return ORGANISATION_NAME_EDEFAULT == null ? getOrganisationName() != null : !ORGANISATION_NAME_EDEFAULT.equals(getOrganisationName());
            case OwsPackage.DOCUMENT_ROOT__POINT_OF_CONTACT:
                return getPointOfContact() != null;
            case OwsPackage.DOCUMENT_ROOT__POSITION_NAME:
                return POSITION_NAME_EDEFAULT == null ? getPositionName() != null : !POSITION_NAME_EDEFAULT.equals(getPositionName());
            case OwsPackage.DOCUMENT_ROOT__ROLE:
                return getRole() != null;
            case OwsPackage.DOCUMENT_ROOT__TITLE:
                return TITLE_EDEFAULT == null ? getTitle() != null : !TITLE_EDEFAULT.equals(getTitle());
            case OwsPackage.DOCUMENT_ROOT__ABSTRACT_META_DATA:
                return getAbstractMetaData() != null;
            case OwsPackage.DOCUMENT_ROOT__ACCESS_CONSTRAINTS:
                return ACCESS_CONSTRAINTS_EDEFAULT == null ? getAccessConstraints() != null : !ACCESS_CONSTRAINTS_EDEFAULT.equals(getAccessConstraints());
            case OwsPackage.DOCUMENT_ROOT__AVAILABLE_CRS:
                return AVAILABLE_CRS_EDEFAULT == null ? getAvailableCRS() != null : !AVAILABLE_CRS_EDEFAULT.equals(getAvailableCRS());
            case OwsPackage.DOCUMENT_ROOT__BOUNDING_BOX:
                return getBoundingBox() != null;
            case OwsPackage.DOCUMENT_ROOT__DCP:
                return getDcp() != null;
            case OwsPackage.DOCUMENT_ROOT__EXCEPTION:
                return getException() != null;
            case OwsPackage.DOCUMENT_ROOT__EXCEPTION_REPORT:
                return getExceptionReport() != null;
            case OwsPackage.DOCUMENT_ROOT__EXTENDED_CAPABILITIES:
                return getExtendedCapabilities() != null;
            case OwsPackage.DOCUMENT_ROOT__FEES:
                return FEES_EDEFAULT == null ? getFees() != null : !FEES_EDEFAULT.equals(getFees());
            case OwsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                return getGetCapabilities() != null;
            case OwsPackage.DOCUMENT_ROOT__HTTP:
                return getHttp() != null;
            case OwsPackage.DOCUMENT_ROOT__IDENTIFIER:
                return getIdentifier() != null;
            case OwsPackage.DOCUMENT_ROOT__LANGUAGE:
                return LANGUAGE_EDEFAULT == null ? getLanguage() != null : !LANGUAGE_EDEFAULT.equals(getLanguage());
            case OwsPackage.DOCUMENT_ROOT__METADATA:
                return getMetadata() != null;
            case OwsPackage.DOCUMENT_ROOT__OPERATION:
                return getOperation() != null;
            case OwsPackage.DOCUMENT_ROOT__OPERATIONS_METADATA:
                return getOperationsMetadata() != null;
            case OwsPackage.DOCUMENT_ROOT__OUTPUT_FORMAT:
                return OUTPUT_FORMAT_EDEFAULT == null ? getOutputFormat() != null : !OUTPUT_FORMAT_EDEFAULT.equals(getOutputFormat());
            case OwsPackage.DOCUMENT_ROOT__SERVICE_IDENTIFICATION:
                return getServiceIdentification() != null;
            case OwsPackage.DOCUMENT_ROOT__SERVICE_PROVIDER:
                return getServiceProvider() != null;
            case OwsPackage.DOCUMENT_ROOT__SUPPORTED_CRS:
                return SUPPORTED_CRS_EDEFAULT == null ? getSupportedCRS() != null : !SUPPORTED_CRS_EDEFAULT.equals(getSupportedCRS());
            case OwsPackage.DOCUMENT_ROOT__WG_S84_BOUNDING_BOX:
                return getWgS84BoundingBox() != null;
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