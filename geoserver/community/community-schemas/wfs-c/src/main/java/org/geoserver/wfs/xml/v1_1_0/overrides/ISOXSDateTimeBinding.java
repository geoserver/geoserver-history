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
import org.geoserver.wfs.xml.xs.DateBinding;
import org.geotools.xml.InstanceComponent;
import org.geotools.xml.SimpleBinding;
import org.geotools.xs.bindings.XS;
import org.geotools.xs.bindings.XSDateTimeBinding;
import org.opengis.feature.Attribute;
import java.util.Calendar;
import java.sql.Timestamp;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;


/**
 * Binding object for the type http://www.w3.org/2001/XMLSchema:dateTime.
 *
 * Adapted by Rob Atkinson for handling Attribute wrapped timestamps
 */
public class ISOXSDateTimeBinding extends XSDateTimeBinding {
    static {
        DatatypeConverter.setDatatypeConverter(DatatypeConverterImpl.theInstance);
    }

    /**
     * <!-- begin-user-doc -->
     * This binding returns objects of type {@link Calendar}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Timestamp.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public String encode(Object object, String value) {
        Attribute att = (Attribute) object;

        Timestamp date = (Timestamp) att.getValue();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
 //       calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

        String printDateTime = null;

        if (date != null) {
            printDateTime = DatatypeConverter.printDate(calendar);
        }
        

        return printDateTime;
    }
}
