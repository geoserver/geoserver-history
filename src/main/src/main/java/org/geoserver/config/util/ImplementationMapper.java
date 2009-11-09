/*
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.config.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import java.util.List;

/**
 * Provides implementation mapping for GeoServer interfaces.
 * Mainly used in XStream outputting.
 *
 * @author ETj <etj at geo-solutions.it>
 */
public interface ImplementationMapper {

    /** provides the implementation of a given GS (catalogInfo or serviceInfo) interface */
    Class getImpl(Class intface);

    /** returns a list of converters to be added to the GS default ones */
    List<Converter> getConverters(XStream xs);
}
