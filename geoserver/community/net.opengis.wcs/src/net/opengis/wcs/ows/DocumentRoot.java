/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs.ows;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getAbstractReferenceBase <em>Abstract Reference Base</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getAccessConstraints <em>Access Constraints</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getAllowedValues <em>Allowed Values</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getAnyValue <em>Any Value</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getAvailableCRS <em>Available CRS</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getCoverage <em>Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getReferenceGroup <em>Reference Group</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getCoverages <em>Coverages</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getManifest <em>Manifest</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getDataType <em>Data Type</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getDCP <em>DCP</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getExtendedCapabilities <em>Extended Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getFees <em>Fees</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getHTTP <em>HTTP</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getInterpolationMethods <em>Interpolation Methods</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getLanguage <em>Language</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getMaximumValue <em>Maximum Value</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getMeaning <em>Meaning</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getMinimumValue <em>Minimum Value</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getNoValues <em>No Values</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getOperation <em>Operation</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getOperationsMetadata <em>Operations Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getRange <em>Range</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getReference <em>Reference</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getReferenceSystem <em>Reference System</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getServiceIdentification <em>Service Identification</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getServiceReference <em>Service Reference</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getSpacing <em>Spacing</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getSupportedCRS <em>Supported CRS</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getUOM <em>UOM</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getValuesReference <em>Values Reference</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getRangeClosure <em>Range Closure</em>}</li>
 *   <li>{@link net.opengis.wcs.ows.DocumentRoot#getReference1 <em>Reference1</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Mixed</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_Mixed()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' name=':mixed'"
     * @generated
     */
    FeatureMap getMixed();

    /**
     * Returns the value of the '<em><b>XMLNS Prefix Map</b></em>' map.
     * The key is of type {@link java.lang.String},
     * and the value is of type {@link java.lang.String},
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>XMLNS Prefix Map</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>XMLNS Prefix Map</em>' map.
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_XMLNSPrefixMap()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
     *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
     * @generated
     */
    EMap getXMLNSPrefixMap();

    /**
     * Returns the value of the '<em><b>XSI Schema Location</b></em>' map.
     * The key is of type {@link java.lang.String},
     * and the value is of type {@link java.lang.String},
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>XSI Schema Location</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>XSI Schema Location</em>' map.
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_XSISchemaLocation()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
     *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
     * @generated
     */
    EMap getXSISchemaLocation();

    /**
     * Returns the value of the '<em><b>Abstract Reference Base</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Reference Base</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Reference Base</em>' containment reference.
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_AbstractReferenceBase()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AbstractReferenceBase' namespace='##targetNamespace'"
     * @generated
     */
    AbstractReferenceBaseType getAbstractReferenceBase();

    /**
     * Returns the value of the '<em><b>Access Constraints</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Access constraint applied to assure the protection of privacy or intellectual property, or any other restrictions on retrieving or using data from or otherwise using this server. The reserved value NONE (case insensitive) shall be used to mean no access constraints are imposed. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Access Constraints</em>' attribute.
     * @see #setAccessConstraints(String)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_AccessConstraints()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AccessConstraints' namespace='##targetNamespace'"
     * @generated
     */
    String getAccessConstraints();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getAccessConstraints <em>Access Constraints</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Access Constraints</em>' attribute.
     * @see #getAccessConstraints()
     * @generated
     */
    void setAccessConstraints(String value);

    /**
     * Returns the value of the '<em><b>Allowed Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * List of all the valid values and/or ranges of values for this quantity. For numeric quantities, signed values shall be ordered from negative infinity to positive infinity. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Allowed Values</em>' containment reference.
     * @see #setAllowedValues(AllowedValuesType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_AllowedValues()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AllowedValues' namespace='##targetNamespace'"
     * @generated
     */
    AllowedValuesType getAllowedValues();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getAllowedValues <em>Allowed Values</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Allowed Values</em>' containment reference.
     * @see #getAllowedValues()
     * @generated
     */
    void setAllowedValues(AllowedValuesType value);

    /**
     * Returns the value of the '<em><b>Any Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Specifies that any value is allowed for this quantity.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Any Value</em>' containment reference.
     * @see #setAnyValue(AnyValueType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_AnyValue()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AnyValue' namespace='##targetNamespace'"
     * @generated
     */
    AnyValueType getAnyValue();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getAnyValue <em>Any Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Any Value</em>' containment reference.
     * @see #getAnyValue()
     * @generated
     */
    void setAnyValue(AnyValueType value);

    /**
     * Returns the value of the '<em><b>Available CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Available CRS</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Available CRS</em>' attribute.
     * @see #setAvailableCRS(String)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_AvailableCRS()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AvailableCRS' namespace='##targetNamespace'"
     * @generated
     */
    String getAvailableCRS();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getAvailableCRS <em>Available CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Available CRS</em>' attribute.
     * @see #getAvailableCRS()
     * @generated
     */
    void setAvailableCRS(String value);

    /**
     * Returns the value of the '<em><b>Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Complete data for one coverage, referencing each coverage file either remotely or locallly in the same message. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage</em>' containment reference.
     * @see #setCoverage(ReferenceGroupType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_Coverage()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Coverage' namespace='##targetNamespace' affiliation='ReferenceGroup'"
     * @generated
     */
    ReferenceGroupType getCoverage();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getCoverage <em>Coverage</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage</em>' containment reference.
     * @see #getCoverage()
     * @generated
     */
    void setCoverage(ReferenceGroupType value);

    /**
     * Returns the value of the '<em><b>Reference Group</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Reference Group</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Reference Group</em>' containment reference.
     * @see #setReferenceGroup(ReferenceGroupType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_ReferenceGroup()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ReferenceGroup' namespace='##targetNamespace'"
     * @generated
     */
    ReferenceGroupType getReferenceGroup();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getReferenceGroup <em>Reference Group</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference Group</em>' containment reference.
     * @see #getReferenceGroup()
     * @generated
     */
    void setReferenceGroup(ReferenceGroupType value);

    /**
     * Returns the value of the '<em><b>Coverages</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Coverages</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Coverages</em>' containment reference.
     * @see #setCoverages(CoveragesType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_Coverages()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Coverages' namespace='##targetNamespace' affiliation='Manifest'"
     * @generated
     */
    CoveragesType getCoverages();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getCoverages <em>Coverages</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverages</em>' containment reference.
     * @see #getCoverages()
     * @generated
     */
    void setCoverages(CoveragesType value);

    /**
     * Returns the value of the '<em><b>Manifest</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Manifest</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Manifest</em>' containment reference.
     * @see #setManifest(ManifestType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_Manifest()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Manifest' namespace='##targetNamespace'"
     * @generated
     */
    ManifestType getManifest();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getManifest <em>Manifest</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Manifest</em>' containment reference.
     * @see #getManifest()
     * @generated
     */
    void setManifest(ManifestType value);

    /**
     * Returns the value of the '<em><b>Data Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to the data type of this value or set of values. In this case, the '"reference" attribute can reference a URN for a well-known data type. For example, such a URN could be a data type identification URN defined in the "ogc" URN namespace. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Data Type</em>' containment reference.
     * @see #setDataType(DomainMetadataType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_DataType()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DataType' namespace='##targetNamespace'"
     * @generated
     */
    DomainMetadataType getDataType();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getDataType <em>Data Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data Type</em>' containment reference.
     * @see #getDataType()
     * @generated
     */
    void setDataType(DomainMetadataType value);

    /**
     * Returns the value of the '<em><b>DCP</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Information for one distributed Computing Platform (DCP) supported for this operation. At present, only the HTTP DCP is defined, so this element only includes the HTTP element.
     * 
     * <!-- end-model-doc -->
     * @return the value of the '<em>DCP</em>' containment reference.
     * @see #setDCP(DCPType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_DCP()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DCP' namespace='##targetNamespace'"
     * @generated
     */
    DCPType getDCP();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getDCP <em>DCP</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>DCP</em>' containment reference.
     * @see #getDCP()
     * @generated
     */
    void setDCP(DCPType value);

    /**
     * Returns the value of the '<em><b>Default Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The default value for a quantity for which multiple values are allowed. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Default Value</em>' containment reference.
     * @see #setDefaultValue(ValueType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_DefaultValue()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DefaultValue' namespace='##targetNamespace'"
     * @generated
     */
    ValueType getDefaultValue();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getDefaultValue <em>Default Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default Value</em>' containment reference.
     * @see #getDefaultValue()
     * @generated
     */
    void setDefaultValue(ValueType value);

    /**
     * Returns the value of the '<em><b>Extended Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Individual software vendors and servers can use this element to provide metadata about any additional server abilities. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Extended Capabilities</em>' containment reference.
     * @see #setExtendedCapabilities(EObject)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_ExtendedCapabilities()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ExtendedCapabilities' namespace='##targetNamespace'"
     * @generated
     */
    EObject getExtendedCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getExtendedCapabilities <em>Extended Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Extended Capabilities</em>' containment reference.
     * @see #getExtendedCapabilities()
     * @generated
     */
    void setExtendedCapabilities(EObject value);

    /**
     * Returns the value of the '<em><b>Fees</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Fees and terms for retrieving data from or otherwise using this server, including the monetary units as specified in ISO 4217. The reserved value NONE (case insensitive) shall be used to mean no fees or terms. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Fees</em>' attribute.
     * @see #setFees(String)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_Fees()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Fees' namespace='##targetNamespace'"
     * @generated
     */
    String getFees();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getFees <em>Fees</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Fees</em>' attribute.
     * @see #getFees()
     * @generated
     */
    void setFees(String value);

    /**
     * Returns the value of the '<em><b>Get Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Get Capabilities</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Get Capabilities</em>' containment reference.
     * @see #setGetCapabilities(GetCapabilitiesType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_GetCapabilities()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GetCapabilities' namespace='##targetNamespace'"
     * @generated
     */
    GetCapabilitiesType getGetCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Get Capabilities</em>' containment reference.
     * @see #getGetCapabilities()
     * @generated
     */
    void setGetCapabilities(GetCapabilitiesType value);

    /**
     * Returns the value of the '<em><b>HTTP</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Connect point URLs for the HTTP Distributed Computing Platform (DCP). Normally, only one Get and/or one Post is included in this element. More than one Get and/or Post is allowed to support including alternative URLs for uses such as load balancing or backup. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>HTTP</em>' containment reference.
     * @see #setHTTP(HTTPType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_HTTP()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='HTTP' namespace='##targetNamespace'"
     * @generated
     */
    HTTPType getHTTP();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getHTTP <em>HTTP</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>HTTP</em>' containment reference.
     * @see #getHTTP()
     * @generated
     */
    void setHTTP(HTTPType value);

    /**
     * Returns the value of the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unique identifier or name of this dataset. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' attribute.
     * @see #setIdentifier(Object)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_Identifier()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='##targetNamespace'"
     * @generated
     */
    Object getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getIdentifier <em>Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' attribute.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(Object value);

    /**
     * Returns the value of the '<em><b>Interpolation Methods</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Interpolation method(s) that can be used when continuous grid coverage resampling is needed. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Interpolation Methods</em>' containment reference.
     * @see #setInterpolationMethods(InterpolationMethodsType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_InterpolationMethods()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='InterpolationMethods' namespace='##targetNamespace'"
     * @generated
     */
    InterpolationMethodsType getInterpolationMethods();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getInterpolationMethods <em>Interpolation Methods</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Interpolation Methods</em>' containment reference.
     * @see #getInterpolationMethods()
     * @generated
     */
    void setInterpolationMethods(InterpolationMethodsType value);

    /**
     * Returns the value of the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of a language used by the data(set) contents. This language identifier shall be as specified in IETF RFC 1766. When this element is omitted, the language used is not identified. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Language</em>' attribute.
     * @see #setLanguage(String)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_Language()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.Language" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Language' namespace='##targetNamespace'"
     * @generated
     */
    String getLanguage();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getLanguage <em>Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Language</em>' attribute.
     * @see #getLanguage()
     * @generated
     */
    void setLanguage(String value);

    /**
     * Returns the value of the '<em><b>Maximum Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Maximum value of this numeric quantity. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Maximum Value</em>' containment reference.
     * @see #setMaximumValue(ValueType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_MaximumValue()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='MaximumValue' namespace='##targetNamespace'"
     * @generated
     */
    ValueType getMaximumValue();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getMaximumValue <em>Maximum Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Maximum Value</em>' containment reference.
     * @see #getMaximumValue()
     * @generated
     */
    void setMaximumValue(ValueType value);

    /**
     * Returns the value of the '<em><b>Meaning</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to the meaning or semantics of this value or set of values. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Meaning</em>' containment reference.
     * @see #setMeaning(DomainMetadataType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_Meaning()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Meaning' namespace='##targetNamespace'"
     * @generated
     */
    DomainMetadataType getMeaning();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getMeaning <em>Meaning</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Meaning</em>' containment reference.
     * @see #getMeaning()
     * @generated
     */
    void setMeaning(DomainMetadataType value);

    /**
     * Returns the value of the '<em><b>Minimum Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Minimum value of this numeric quantity. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Minimum Value</em>' containment reference.
     * @see #setMinimumValue(ValueType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_MinimumValue()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='MinimumValue' namespace='##targetNamespace'"
     * @generated
     */
    ValueType getMinimumValue();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getMinimumValue <em>Minimum Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Minimum Value</em>' containment reference.
     * @see #getMinimumValue()
     * @generated
     */
    void setMinimumValue(ValueType value);

    /**
     * Returns the value of the '<em><b>No Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Specifies that no values are allowed for this quantity.
     * <!-- end-model-doc -->
     * @return the value of the '<em>No Values</em>' containment reference.
     * @see #setNoValues(NoValuesType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_NoValues()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='NoValues' namespace='##targetNamespace'"
     * @generated
     */
    NoValuesType getNoValues();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getNoValues <em>No Values</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>No Values</em>' containment reference.
     * @see #getNoValues()
     * @generated
     */
    void setNoValues(NoValuesType value);

    /**
     * Returns the value of the '<em><b>Operation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Metadata for one operation that this server implements. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Operation</em>' containment reference.
     * @see #setOperation(OperationType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_Operation()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Operation' namespace='##targetNamespace'"
     * @generated
     */
    OperationType getOperation();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getOperation <em>Operation</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operation</em>' containment reference.
     * @see #getOperation()
     * @generated
     */
    void setOperation(OperationType value);

    /**
     * Returns the value of the '<em><b>Operations Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Metadata about the operations and related abilities specified by this service and implemented by this server, including the URLs for operation requests. The basic contents of this section shall be the same for all OWS types, but individual services can add elements and/or change the optionality of optional elements. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Operations Metadata</em>' containment reference.
     * @see #setOperationsMetadata(OperationsMetadataType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_OperationsMetadata()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='OperationsMetadata' namespace='##targetNamespace'"
     * @generated
     */
    OperationsMetadataType getOperationsMetadata();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getOperationsMetadata <em>Operations Metadata</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operations Metadata</em>' containment reference.
     * @see #getOperationsMetadata()
     * @generated
     */
    void setOperationsMetadata(OperationsMetadataType value);

    /**
     * Returns the value of the '<em><b>Output Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to a format in which this data can be encoded and transferred. More specific parameter names should be used by specific OWS specifications wherever applicable. More than one such parameter can be included for different purposes. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Output Format</em>' attribute.
     * @see #setOutputFormat(Object)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_OutputFormat()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='OutputFormat' namespace='##targetNamespace'"
     * @generated
     */
    Object getOutputFormat();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getOutputFormat <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Output Format</em>' attribute.
     * @see #getOutputFormat()
     * @generated
     */
    void setOutputFormat(Object value);

    /**
     * Returns the value of the '<em><b>Range</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Range</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Range</em>' containment reference.
     * @see #setRange(RangeType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_Range()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Range' namespace='##targetNamespace'"
     * @generated
     */
    RangeType getRange();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getRange <em>Range</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Range</em>' containment reference.
     * @see #getRange()
     * @generated
     */
    void setRange(RangeType value);

    /**
     * Returns the value of the '<em><b>Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Reference</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Reference</em>' containment reference.
     * @see #setReference(ReferenceType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_Reference()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Reference' namespace='##targetNamespace' affiliation='AbstractReferenceBase'"
     * @generated
     */
    ReferenceType getReference();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getReference <em>Reference</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference</em>' containment reference.
     * @see #getReference()
     * @generated
     */
    void setReference(ReferenceType value);

    /**
     * Returns the value of the '<em><b>Reference System</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to the reference system used by this set of values, including the unit of measure whenever applicable (as is normal). In this case, the '"reference" attribute can reference a URN for a well-known reference system, such as for a coordinate reference system (CRS). For example, such a URN could be a CRS identification URN defined in the "ogc" URN namespace. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Reference System</em>' containment reference.
     * @see #setReferenceSystem(DomainMetadataType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_ReferenceSystem()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ReferenceSystem' namespace='##targetNamespace'"
     * @generated
     */
    DomainMetadataType getReferenceSystem();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getReferenceSystem <em>Reference System</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference System</em>' containment reference.
     * @see #getReferenceSystem()
     * @generated
     */
    void setReferenceSystem(DomainMetadataType value);

    /**
     * Returns the value of the '<em><b>Service Identification</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * General metadata for this specific server. This XML Schema of this section shall be the same for all OWS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Service Identification</em>' containment reference.
     * @see #setServiceIdentification(ServiceIdentificationType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_ServiceIdentification()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ServiceIdentification' namespace='##targetNamespace'"
     * @generated
     */
    ServiceIdentificationType getServiceIdentification();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getServiceIdentification <em>Service Identification</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Service Identification</em>' containment reference.
     * @see #getServiceIdentification()
     * @generated
     */
    void setServiceIdentification(ServiceIdentificationType value);

    /**
     * Returns the value of the '<em><b>Service Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Service Reference</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Service Reference</em>' containment reference.
     * @see #setServiceReference(ServiceReferenceType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_ServiceReference()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ServiceReference' namespace='##targetNamespace' affiliation='Reference'"
     * @generated
     */
    ServiceReferenceType getServiceReference();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getServiceReference <em>Service Reference</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Service Reference</em>' containment reference.
     * @see #getServiceReference()
     * @generated
     */
    void setServiceReference(ServiceReferenceType value);

    /**
     * Returns the value of the '<em><b>Spacing</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The regular distance or spacing between the allowed values in a range. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Spacing</em>' containment reference.
     * @see #setSpacing(ValueType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_Spacing()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Spacing' namespace='##targetNamespace'"
     * @generated
     */
    ValueType getSpacing();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getSpacing <em>Spacing</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Spacing</em>' containment reference.
     * @see #getSpacing()
     * @generated
     */
    void setSpacing(ValueType value);

    /**
     * Returns the value of the '<em><b>Supported CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Coordinate reference system in which data from this data(set) or resource is available or supported. More specific parameter names should be used by specific OWS specifications wherever applicable. More than one such parameter can be included for different purposes. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Supported CRS</em>' attribute.
     * @see #setSupportedCRS(String)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_SupportedCRS()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='SupportedCRS' namespace='##targetNamespace' affiliation='AvailableCRS'"
     * @generated
     */
    String getSupportedCRS();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getSupportedCRS <em>Supported CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Supported CRS</em>' attribute.
     * @see #getSupportedCRS()
     * @generated
     */
    void setSupportedCRS(String value);

    /**
     * Returns the value of the '<em><b>UOM</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to the unit of measure of this value or set of values. In this case, the '"reference" attribute can reference a URN for a well-known unit of measure (uom). For example, such a URN could be a UOM identification URN defined in the "ogc" URN namespace. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>UOM</em>' containment reference.
     * @see #setUOM(DomainMetadataType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_UOM()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='UOM' namespace='##targetNamespace'"
     * @generated
     */
    DomainMetadataType getUOM();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getUOM <em>UOM</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>UOM</em>' containment reference.
     * @see #getUOM()
     * @generated
     */
    void setUOM(DomainMetadataType value);

    /**
     * Returns the value of the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' containment reference.
     * @see #setValue(ValueType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_Value()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Value' namespace='##targetNamespace'"
     * @generated
     */
    ValueType getValue();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getValue <em>Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' containment reference.
     * @see #getValue()
     * @generated
     */
    void setValue(ValueType value);

    /**
     * Returns the value of the '<em><b>Values Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to externally specified list of all the valid values and/or ranges of values for this quantity. (Informative: This element was simplified from the metaDataProperty element in GML 3.0.) 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Values Reference</em>' containment reference.
     * @see #setValuesReference(ValuesReferenceType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_ValuesReference()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ValuesReference' namespace='##targetNamespace'"
     * @generated
     */
    ValuesReferenceType getValuesReference();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getValuesReference <em>Values Reference</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Values Reference</em>' containment reference.
     * @see #getValuesReference()
     * @generated
     */
    void setValuesReference(ValuesReferenceType value);

    /**
     * Returns the value of the '<em><b>Range Closure</b></em>' attribute.
     * The default value is <code>"closed"</code>.
     * The literals are from the enumeration {@link net.opengis.wcs.ows.RangeClosureType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Specifies which of the minimum and maximum values are included in the range. Note that plus and minus infinity are considered closed bounds. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Range Closure</em>' attribute.
     * @see net.opengis.wcs.ows.RangeClosureType
     * @see #isSetRangeClosure()
     * @see #unsetRangeClosure()
     * @see #setRangeClosure(RangeClosureType)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_RangeClosure()
     * @model default="closed" unsettable="true"
     *        extendedMetaData="kind='attribute' name='rangeClosure' namespace='##targetNamespace'"
     * @generated
     */
    RangeClosureType getRangeClosure();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getRangeClosure <em>Range Closure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Range Closure</em>' attribute.
     * @see net.opengis.wcs.ows.RangeClosureType
     * @see #isSetRangeClosure()
     * @see #unsetRangeClosure()
     * @see #getRangeClosure()
     * @generated
     */
    void setRangeClosure(RangeClosureType value);

    /**
     * Unsets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getRangeClosure <em>Range Closure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetRangeClosure()
     * @see #getRangeClosure()
     * @see #setRangeClosure(RangeClosureType)
     * @generated
     */
    void unsetRangeClosure();

    /**
     * Returns whether the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getRangeClosure <em>Range Closure</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Range Closure</em>' attribute is set.
     * @see #unsetRangeClosure()
     * @see #getRangeClosure()
     * @see #setRangeClosure(RangeClosureType)
     * @generated
     */
    boolean isSetRangeClosure();

    /**
     * Returns the value of the '<em><b>Reference1</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to data or metadata recorded elsewhere, either external to this XML document or within it. Whenever practical, this attribute should be a URL from which this metadata can be electronically retrieved. Alternately, this attribute can reference a URN for well-known metadata. For example, such a URN could be a URN defined in the "ogc" URN namespace. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Reference1</em>' attribute.
     * @see #setReference1(String)
     * @see net.opengis.wcs.ows.owcsPackage#getDocumentRoot_Reference1()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='reference' namespace='##targetNamespace'"
     * @generated
     */
    String getReference1();

    /**
     * Sets the value of the '{@link net.opengis.wcs.ows.DocumentRoot#getReference1 <em>Reference1</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference1</em>' attribute.
     * @see #getReference1()
     * @generated
     */
    void setReference1(String value);

} // DocumentRoot
