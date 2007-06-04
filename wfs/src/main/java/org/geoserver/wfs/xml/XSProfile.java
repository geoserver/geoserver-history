/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml;

import org.geotools.feature.Name;
import org.geotools.xs.XSSchema;
import org.geotools.xs.bindings.XS;
import java.util.HashSet;
import java.util.Set;
import javax.xml.namespace.QName;


/**
 * A profile of {@link XSSchema} which makes the java class to type
 * mapping unique.
 */
public class XSProfile extends TypeMappingProfile {
    static Set profile = new HashSet();

    static {
        profile.add(name(XS.BYTE)); //Byte.class
        profile.add(name(XS.HEXBINARY)); //byte[].class 
        profile.add(name(XS.SHORT)); //Short.class
        profile.add(name(XS.INT)); //Integer.class
        profile.add(name(XS.FLOAT)); //Float.class
        profile.add(name(XS.LONG)); //Long.class
        profile.add(name(XS.QNAME)); //Qname.class
        profile.add(name(XS.DATE)); //Date.class
        profile.add(name(XS.DATETIME)); //Calendar.class
        profile.add(name(XS.BOOLEAN)); //Boolean.class
        profile.add(name(XS.DOUBLE)); //Double.class
        profile.add(name(XS.STRING)); //String.class
        profile.add(name(XS.INTEGER)); //BigInteger.class
        profile.add(name(XS.DECIMAL)); //BigDecimal.class
        profile.add(name(XS.ANYURI)); //URI.class

        //profile.add(name(XS.ANYTYPE)); //Map.class
    }

    static Name name(QName qName) {
        return new Name(qName.getNamespaceURI(), qName.getLocalPart());
    }

    public XSProfile() {
        super(new XSSchema(), profile);
    }
}
