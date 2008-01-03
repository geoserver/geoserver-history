/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs.kvp;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.opengis.wcs.v1_1_1.GetCoverageType;
import net.opengis.wcs.v1_1_1.OutputType;
import net.opengis.wcs.v1_1_1.Wcs111Factory;

import org.geoserver.ows.kvp.EMFKvpRequestReader;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.wcs.WcsException;

public class GetCoverageRequestReader extends EMFKvpRequestReader {

    Data catalog;

    public GetCoverageRequestReader(Data catalog) {
        super(GetCoverageType.class, Wcs111Factory.eINSTANCE);
        this.catalog = catalog;
    }
    
    @Override
    public Object read(Object request, Map kvp, Map rawKvp) throws Exception {
        GetCoverageType getCoverage = (GetCoverageType) super.read(request, kvp, rawKvp);
        
        // grab coverage info to perform further checks
        if(getCoverage.getIdentifier() == null)
            throw new WcsException("identifier parameter is mandatory", "MissingParameterValue", "identifier");
        CoverageInfo info = catalog.getCoverageInfo(getCoverage.getIdentifier().getValue());
        
        // build output element
        final OutputType output = Wcs111Factory.eINSTANCE.createOutputType();
        getCoverage.setOutput(output);
        
        // check and set store
        Boolean store = (Boolean) kvp.get("store");
        if(store != null)
            output.setStore(store.booleanValue());
        if(output.isStore())
            throw new WcsException("store is not supported", "InvalidParameterValue", "store");
        
        // check and set format
        String format = (String) kvp.get("format");
        if(format == null)
            throw new WcsException("format parameter is mandatory", "MissingParameterValue", "format");
        String declaredFormat = getDeclaredFormat(info.getSupportedFormats(), format); 
        if(declaredFormat == null)
            throw new WcsException("format " + format + " is not supported for this coverage" , "InvalidParameterValue", "format");
        output.setFormat(declaredFormat);
        
        return getCoverage;
    }

    /**
     * Checks if the supported format string list contains the specified format,
     * doing a case insensitive search. If found the declared output format name is
     * returned, otherwise null is returned.
     * @param supportedFormats
     * @param format
     * @return
     */
    private String getDeclaredFormat(List supportedFormats, String format) {
        for (Iterator it = supportedFormats.iterator(); it.hasNext();) {
            String sf = (String) it.next();
            if(sf.equalsIgnoreCase(format))
                return sf;
        }
        return null;
    }

    
}
