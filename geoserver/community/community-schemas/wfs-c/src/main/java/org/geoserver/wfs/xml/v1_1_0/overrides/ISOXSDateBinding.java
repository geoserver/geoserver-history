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

import com.sun.xml.bind.DatatypeConverterImpl;
import org.geotools.xml.InstanceComponent;
import org.geotools.xml.SimpleBinding;
import org.geotools.xs.bindings.XS;
import org.opengis.feature.Attribute;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;


/**
 * Binding object for the type http://www.w3.org/2001/XMLSchema:date.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xs:simpleType name=&quot;date&quot; id=&quot;date&quot;&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:appinfo&gt;
 *              &lt;hfp:hasFacet name=&quot;pattern&quot;/&gt;
 *              &lt;hfp:hasFacet name=&quot;enumeration&quot;/&gt;
 *              &lt;hfp:hasFacet name=&quot;whiteSpace&quot;/&gt;
 *              &lt;hfp:hasFacet name=&quot;maxInclusive&quot;/&gt;
 *              &lt;hfp:hasFacet name=&quot;maxExclusive&quot;/&gt;
 *              &lt;hfp:hasFacet name=&quot;minInclusive&quot;/&gt;
 *              &lt;hfp:hasFacet name=&quot;minExclusive&quot;/&gt;
 *              &lt;hfp:hasProperty name=&quot;ordered&quot; value=&quot;partial&quot;/&gt;
 *              &lt;hfp:hasProperty name=&quot;bounded&quot; value=&quot;false&quot;/&gt;
 *              &lt;hfp:hasProperty name=&quot;cardinality&quot; value=&quot;countably infinite&quot;/&gt;
 *              &lt;hfp:hasProperty name=&quot;numeric&quot; value=&quot;false&quot;/&gt;
 *          &lt;/xs:appinfo&gt;
 *          &lt;xs:documentation source=&quot;http://www.w3.org/TR/xmlschema-2/#date&quot;/&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:restriction base=&quot;xs:anySimpleType&quot;&gt;
 *          &lt;xs:whiteSpace value=&quot;collapse&quot; fixed=&quot;true&quot; id=&quot;date.whiteSpace&quot;/&gt;
 *      &lt;/xs:restriction&gt;
 *  &lt;/xs:simpleType&gt;
 * </code>
 *         </pre>
 *
 * </p>
 *
 * @generated
 */
public class ISOXSDateBinding implements SimpleBinding {
    public ISOXSDateBinding() {
        DatatypeConverter.setDatatypeConverter(DatatypeConverterImpl.theInstance);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return XS.DATE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     * <!-- begin-user-doc --> This binding returns objects of type {@link Date}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Date.class;
    }

    /**
     * <!-- begin-user-doc --> This binding returns objects of type
     * {@link Calendar}. <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        Calendar calendar = DatatypeConverter.parseDate((String) value);

        return calendar.getTime();
    }

    public String encode(Object object, String value) throws Exception {
        Attribute att = (Attribute) object;

        Date date = (Date) att.getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

        String printDateTime = null;

        if (date != null) {
            printDateTime = DatatypeConverter.printDate(calendar);
        }

        return printDateTime;
    }
}
