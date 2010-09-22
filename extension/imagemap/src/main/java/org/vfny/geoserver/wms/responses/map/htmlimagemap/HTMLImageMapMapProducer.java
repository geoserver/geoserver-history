/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.htmlimagemap;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.GetMapOutputFormat;
import org.geoserver.wms.WMSMapContext;
import org.geoserver.wms.map.AbstractMapOutputFormat;
import org.springframework.util.Assert;


/**
 * Handles a GetMap request that produces a map in HTMLImageMap format.
 *
 * @author Mauro Bartolomeoli
 */
public class HTMLImageMapMapProducer extends AbstractMapOutputFormat  {
    
    /**
     * The ImageMap is served as text/html: it is an HTML fragment, after all.   
     */
    static final String MIME_TYPE = "text/html";
    

    public HTMLImageMapMapProducer() {
		super(EncodeHTMLImageMap.class, MIME_TYPE);
		// TODO Auto-generated constructor stub
    }


    /**
     * Renders the map.
     *
     * @throws ServiceException if an error occurs during rendering
     * @see GetMapOutputFormat#produceMap(WMSMapContext)
     */
    public EncodeHTMLImageMap produceMap(WMSMapContext mapContext) throws ServiceException, IOException {
        if (mapContext == null) {
            throw new ServiceException("The map context is not set");
        }

        return new EncodeHTMLImageMap(mapContext);
    }

    /**
     * Writes the generated map to an OutputStream.
     *
     * @param out final output stream
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    @Override
    public void write(Object value, OutputStream output, Operation operation) throws IOException,
            ServiceException {
        Assert.isInstanceOf(EncodeHTMLImageMap.class, value);
        EncodeHTMLImageMap htmlImageMapEncoder = (EncodeHTMLImageMap) value;
        try {
            htmlImageMapEncoder.encode(output);
        } finally {
            htmlImageMapEncoder.dispose();
        }
    }


}
