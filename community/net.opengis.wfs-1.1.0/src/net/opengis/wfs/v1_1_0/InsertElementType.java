/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.v1_1_0;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Insert Element Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             An Insert element may contain a feature collection or one 
 *             or more feature instances to be inserted into the 
 *             repository.
 *          
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.v1_1_0.InsertElementType#getFeatureCollection <em>Feature Collection</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.InsertElementType#getFeature <em>Feature</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.InsertElementType#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.InsertElementType#getIdgen <em>Idgen</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.InsertElementType#getInputFormat <em>Input Format</em>}</li>
 *   <li>{@link net.opengis.wfs.v1_1_0.InsertElementType#getSrsName <em>Srs Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.v1_1_0.WFSPackage#getInsertElementType()
 * @model extendedMetaData="name='InsertElementType' kind='elementOnly'"
 * @generated
 */
public interface InsertElementType extends EObject {
	/**
	 * Returns the value of the '<em><b>Feature Collection</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature Collection</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature Collection</em>' attribute.
	 * @see #setFeatureCollection(Object)
	 * @see net.opengis.wfs.v1_1_0.WFSPackage#getInsertElementType_FeatureCollection()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
	 *        extendedMetaData="kind='element' name='_FeatureCollection' namespace='http://www.opengis.net/gml'"
	 * @generated
	 */
	Object getFeatureCollection();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.v1_1_0.InsertElementType#getFeatureCollection <em>Feature Collection</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Feature Collection</em>' attribute.
	 * @see #getFeatureCollection()
	 * @generated
	 */
	void setFeatureCollection(Object value);

	/**
	 * Returns the value of the '<em><b>Feature</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Object}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature</em>' attribute list.
	 * @see net.opengis.wfs.v1_1_0.WFSPackage#getInsertElementType_Feature()
	 * @model type="java.lang.Object" unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
	 *        extendedMetaData="kind='element' name='_Feature' namespace='http://www.opengis.net/gml'"
	 * @generated
	 */
	EList getFeature();

	/**
	 * Returns the value of the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                The handle attribute allows a client application
	 *                to assign a client-generated request identifier
	 *                to an Insert action.  The handle is included to
	 *                facilitate error reporting.  If an Insert action
	 *                in a Transaction request fails, then a WFS may
	 *                include the handle in an exception report to localize
	 *                the error.  If no handle is included of the offending
	 *                Insert element then a WFS may employee other means of
	 *                localizing the error (e.g. line number).
	 *             
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Handle</em>' attribute.
	 * @see #setHandle(String)
	 * @see net.opengis.wfs.v1_1_0.WFSPackage#getInsertElementType_Handle()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='handle'"
	 * @generated
	 */
	String getHandle();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.v1_1_0.InsertElementType#getHandle <em>Handle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Handle</em>' attribute.
	 * @see #getHandle()
	 * @generated
	 */
	void setHandle(String value);

	/**
	 * Returns the value of the '<em><b>Idgen</b></em>' attribute.
	 * The default value is <code>"GenerateNew"</code>.
	 * The literals are from the enumeration {@link net.opengis.wfs.v1_1_0.IdentifierGenerationOptionType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                The idgen attribute control how a WFS generates identifiers
	 *                from newly created feature instances using the Insert action.
	 *                The default action is to have the WFS generate a new id for
	 *                the features.  This is also backward compatible with WFS 1.0
	 *                where the only action was for the WFS to generate an new id.
	 *             
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Idgen</em>' attribute.
	 * @see net.opengis.wfs.v1_1_0.IdentifierGenerationOptionType
	 * @see #isSetIdgen()
	 * @see #unsetIdgen()
	 * @see #setIdgen(IdentifierGenerationOptionType)
	 * @see net.opengis.wfs.v1_1_0.WFSPackage#getInsertElementType_Idgen()
	 * @model default="GenerateNew" unique="false" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='idgen'"
	 * @generated
	 */
	IdentifierGenerationOptionType getIdgen();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.v1_1_0.InsertElementType#getIdgen <em>Idgen</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Idgen</em>' attribute.
	 * @see net.opengis.wfs.v1_1_0.IdentifierGenerationOptionType
	 * @see #isSetIdgen()
	 * @see #unsetIdgen()
	 * @see #getIdgen()
	 * @generated
	 */
	void setIdgen(IdentifierGenerationOptionType value);

	/**
	 * Unsets the value of the '{@link net.opengis.wfs.v1_1_0.InsertElementType#getIdgen <em>Idgen</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetIdgen()
	 * @see #getIdgen()
	 * @see #setIdgen(IdentifierGenerationOptionType)
	 * @generated
	 */
	void unsetIdgen();

	/**
	 * Returns whether the value of the '{@link net.opengis.wfs.v1_1_0.InsertElementType#getIdgen <em>Idgen</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Idgen</em>' attribute is set.
	 * @see #unsetIdgen()
	 * @see #getIdgen()
	 * @see #setIdgen(IdentifierGenerationOptionType)
	 * @generated
	 */
	boolean isSetIdgen();

	/**
	 * Returns the value of the '<em><b>Input Format</b></em>' attribute.
	 * The default value is <code>"text/xml; subtype=gml/3.1.1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                This inputFormat attribute is used to indicate 
	 *                the format used to encode a feature instance in
	 *                an Insert element.  The default value of
	 *                'text/xml; subtype=gml/3.1.1' is used to indicate
	 *                that feature encoding is GML3.  Another example
	 *                might be 'text/xml; subtype=gml/2.1.2' indicating
	 *                that the feature us encoded in GML2.  A WFS must
	 *                declare in the capabilities document, using a 
	 *                Parameter element, which version of GML it supports.
	 *             
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Input Format</em>' attribute.
	 * @see #isSetInputFormat()
	 * @see #unsetInputFormat()
	 * @see #setInputFormat(String)
	 * @see net.opengis.wfs.v1_1_0.WFSPackage#getInsertElementType_InputFormat()
	 * @model default="text/xml; subtype=gml/3.1.1" unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='inputFormat'"
	 * @generated
	 */
	String getInputFormat();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.v1_1_0.InsertElementType#getInputFormat <em>Input Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Input Format</em>' attribute.
	 * @see #isSetInputFormat()
	 * @see #unsetInputFormat()
	 * @see #getInputFormat()
	 * @generated
	 */
	void setInputFormat(String value);

	/**
	 * Unsets the value of the '{@link net.opengis.wfs.v1_1_0.InsertElementType#getInputFormat <em>Input Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetInputFormat()
	 * @see #getInputFormat()
	 * @see #setInputFormat(String)
	 * @generated
	 */
	void unsetInputFormat();

	/**
	 * Returns whether the value of the '{@link net.opengis.wfs.v1_1_0.InsertElementType#getInputFormat <em>Input Format</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Input Format</em>' attribute is set.
	 * @see #unsetInputFormat()
	 * @see #getInputFormat()
	 * @see #setInputFormat(String)
	 * @generated
	 */
	boolean isSetInputFormat();

	/**
	 * Returns the value of the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *       The optional srsName attribute is used to assert the SRS of the
	 *       incoming feature data, which can be useful if the incoming feature
	 *       data does not have an SRS declared for each geometry. If the
	 *       srsName attribute exists on an &lt;Insert&gt; element, its value shall
	 *       be equivalent to the value of &lt;DefaultSRS&gt; or any of the
	 *       &lt;OtherSRS&gt; of the relevant feature types. If, however, the SRS is
	 *       not supported, the WFS shall raise an exception as described in
	 *       subclause 7.7. If the srsName is not specified on the &lt;Insert&gt;
	 *       element, the WFS shall interpret this to mean that the feature
	 *       data is given in the &lt;DefaultSRS&gt; list, except where an SRS is
	 *       specified on the feature geometry. In this case, if the SRS for
	 *       such a geometry is one of the &lt;DefaultSRS&gt; or &lt;OtherSR&gt; values
	 *       for the respective feature types, it will be transformed as
	 *       required before insertion. However, if the aforesaid SRS is not
	 *       supported for the respective feature type, the entire transaction
	 *       shall fail and the WFS shall raise an exception as described in
	 *       subclause 7.7. If atomic transactions are not supported by the
	 *       underlying DBMS, the WFS shall skip any feature with an
	 *       unsupported SRS and continue
	 *            
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Srs Name</em>' attribute.
	 * @see #setSrsName(String)
	 * @see net.opengis.wfs.v1_1_0.WFSPackage#getInsertElementType_SrsName()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='srsName'"
	 * @generated
	 */
	String getSrsName();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.v1_1_0.InsertElementType#getSrsName <em>Srs Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Srs Name</em>' attribute.
	 * @see #getSrsName()
	 * @generated
	 */
	void setSrsName(String value);

} // InsertElementType
