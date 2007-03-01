/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0;

import net.opengis.wfs.WFSFactory;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;
import javax.xml.namespace.QName;


/**
 * Binding object for the type http://www.opengis.net/wfs:Base_TypeNameListType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:simpleType name="Base_TypeNameListType"&gt;
 *      &lt;xsd:list itemType="xsd:QName"/&gt;
 *  &lt;/xsd:simpleType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 */
public class Base_TypeNameListTypeBinding extends AbstractSimpleBinding {
    WFSFactory wfsfactory;

    public Base_TypeNameListTypeBinding(WFSFactory wfsfactory) {
        this.wfsfactory = wfsfactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.BASE_TYPENAMELISTTYPE;
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
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        //TODO: implement
        return null;
    }
}
