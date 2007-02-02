/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_0_0;

import net.opengis.wfs.WFSFactory;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import javax.xml.namespace.QName;


/**
 * Binding object for the element http://www.opengis.net/wfs:GetFeature.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:element name="GetFeature" type="wfs:GetFeatureType"&gt;
 *          &lt;xsd:annotation&gt;          &lt;xsd:documentation&gt;             The
 *              GetFeature element is used to request that a Web Feature
 *              Service return feature instances of one or more feature
 *              types.          &lt;/xsd:documentation&gt;       &lt;/xsd:annotation&gt;    &lt;/xsd:element&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 */
public class GetFeatureBinding extends AbstractComplexBinding {
    WFSFactory wfsfactory;

    public GetFeatureBinding(WFSFactory wfsfactory) {
        this.wfsfactory = wfsfactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.GETFEATURE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //TODO: implement
        return null;
    }
}
