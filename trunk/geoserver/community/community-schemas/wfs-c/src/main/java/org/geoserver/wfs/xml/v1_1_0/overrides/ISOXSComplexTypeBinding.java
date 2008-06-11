/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002-2006, GeoTools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geoserver.wfs.xml.v1_1_0.overrides;

import org.geotools.gml3.bindings.GML;
import org.geotools.util.Converters;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xs.bindings.XS;
import org.opengis.feature.Attribute;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.namespace.QName;


/**
 * Binding object for the type http://www.w3.org/2001/XMLSchema:complexType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xs:complexType name="complexType" abstract="true"&gt;
 *      &lt;xs:complexContent&gt;
 *          &lt;xs:extension base="xs:annotated"&gt;
 *              &lt;xs:group ref="xs:complexTypeModel"/&gt;
 *              &lt;xs:attribute name="name" type="xs:NCName"&gt;
 *                  &lt;xs:annotation&gt;
 *                      &lt;xs:documentation&gt;       Will be restricted to
 *                          required or forbidden&lt;/xs:documentation&gt;
 *                  &lt;/xs:annotation&gt;
 *              &lt;/xs:attribute&gt;
 *              &lt;xs:attribute name="mixed" type="xs:boolean" use="optional" default="false"&gt;
 *                  &lt;xs:annotation&gt;
 *                      &lt;xs:documentation&gt;       Not allowed if
 *                          simpleContent child is chosen.       May be
 *                          overriden by setting on complexContent child.&lt;/xs:documentation&gt;
 *                  &lt;/xs:annotation&gt;
 *              &lt;/xs:attribute&gt;
 *              &lt;xs:attribute name="abstract" type="xs:boolean"
 *                  use="optional" default="false"/&gt;
 *              &lt;xs:attribute name="final" type="xs:derivationSet"/&gt;
 *              &lt;xs:attribute name="block" type="xs:derivationSet"/&gt;
 *          &lt;/xs:extension&gt;
 *      &lt;/xs:complexContent&gt;
 *  &lt;/xs:complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 */
public class ISOXSComplexTypeBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return XS.COMPLEXTYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     * <!-- begin-user-doc -->
     * This binding delegates to its parent binding, which returns objects of
     * type {@link Map}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Attribute.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {
    }

    /**
     * <!-- begin-user-doc -->
     * This binding delegates to its parent binding, which returns objects of
     * type {@link Map}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        return value; //all work done by super type
    }

    /**
     * Subclasses should ovverride this method if need be, the default implementation
     * returns <param>value</param>.
     *
     * @see ComplexBinding#encode(Object, Document, Element).
     */
    public Element encode(Object object, Document document, Element value)
        throws Exception {
        Attribute attribute = (Attribute) object;

        AttributeDescriptor descriptor = attribute.getDescriptor();
        Name name = descriptor.getName();
        String namespace = name.getNamespaceURI();
        String localName = name.getLocalPart();

        Element encoding = document.createElementNS(namespace, localName);

        String id = attribute.getID();

        if (id != null) {
            encoding.setAttributeNS(GML.NAMESPACE, "id", id);
        }

        Map definedAttributes = (Map) descriptor.getUserData(Attributes.class);

        if (definedAttributes != null) {
            Map.Entry entry;

            for (Iterator it = definedAttributes.entrySet().iterator(); it.hasNext();) {
                entry = (Entry) it.next();

                Name key = (Name) entry.getKey();
                Object attValue = entry.getValue();

                String namespaceURI = key.getNamespaceURI();
                String qualifiedName = key.getLocalPart();
                String strAttValue = (String) Converters.convert(attValue, String.class);

                encoding.setAttributeNS(namespaceURI, qualifiedName, strAttValue);
            }
        }

        String attValue = (String) Converters.convert(attribute.getValue(), String.class);

        if (attValue != null) {
            // figure out if the node has any text
            Text text = null;

            for (int i = 0; i < encoding.getChildNodes().getLength(); i++) {
                org.w3c.dom.Node node = encoding.getChildNodes().item(i);

                if (node instanceof Text) {
                    text = (Text) node;

                    break;
                }
            }

            // set the text of the node
            if (text == null) {
                text = document.createTextNode(attValue);
                encoding.appendChild(text);
            } else {
                text.setData(attValue);
            }
        }

        return encoding;
    }

    /**
     * Subclasses should override this method if need be, the default implementation
     * returns <code>null</code>.
     *
     * @see ComplexBinding#getProperty(Object, QName)
     */
    public Object getProperty(Object object, QName name)
        throws Exception {
        //do nothing, subclasses should override
        return null;
    }

    /**
     * Subclasses should override this method if need be, the default implementation
     * returns <code>null</code>.
     * <p>
     * Note that this method only needs to be implemented for schema types which
     * are open-ended in which the contents are not specifically specified by
     * the schema.
     * </p>
     *
     * @see ComplexBinding#getProperties(Object)
     */
    public List getProperties(Object object) throws Exception {
        // do nothing, subclasses should override.
        return null;
    }
}
