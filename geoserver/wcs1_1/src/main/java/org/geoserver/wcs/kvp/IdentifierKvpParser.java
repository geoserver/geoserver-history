/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs.kvp;

import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.InvalidParameterValue;
import net.opengis.ows11.CodeType;
import net.opengis.ows11.Ows11Factory;

import org.geoserver.ows.KvpParser;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.wcs.WcsException;

public class IdentifierKvpParser extends KvpParser {

    private Data catalog;

    public IdentifierKvpParser(Data catalog) {
        super("identifier", CodeType.class);
        this.catalog = catalog;
    }

    @Override
    public Object parse(String value) throws Exception {
        Integer type = catalog.getLayerType(value);
        if (!Data.TYPE_RASTER.equals(type))
            throw new WcsException("Could not find coverage '" + value + "'", InvalidParameterValue, "identifier");
        CodeType result = Ows11Factory.eINSTANCE.createCodeType();
        result.setValue(value);
        return result;
    }

}
