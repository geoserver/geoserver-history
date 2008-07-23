/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geotools.util;

import org.geotools.factory.Hints;
import org.opengis.feature.Attribute;


public class AttributeConverterFactory implements ConverterFactory {
    public Converter createConverter(Class source, Class target, Hints hints) {
        if (Attribute.class.isAssignableFrom(source)) {
            return new Converter() {
                    public Object convert(Object source, Class target)
                        throws Exception {
                        Attribute att = (Attribute) source;
                        Object value = att.getValue();

                        if (value == null) {
                            return null;
                        }

                        Object converted = Converters.convert(value, target);

                        return converted;
                    }
                };
        }

        return null;
    }
}
