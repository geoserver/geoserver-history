/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
 *    @author lreed@refractions.net
 */

package org.geoserver.wps;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetSchema
{
    public WPS wps;

    public GetSchema(WPS wps)
    {
        this.wps = wps;
    }

    public void run(HttpServletRequest request, HttpServletResponse response)
    {
        String name = request.getParameter("Identifier");    // XXX TODO case may be an issue

        if (null == name)
        {
            throw new WPSException("NoApplicableCode", "No Identifier key and value.");
        }

        InputStream stream = org.geoserver.wps.schemas.Stub.class.getResourceAsStream(name);

        if (null == stream)
        {
            throw new WPSException("NoApplicableCode", "No Schema '" + name + "'.");
        }

        BufferedReader bufReader = new BufferedReader(new InputStreamReader(stream));

        StringBuilder schema = new StringBuilder();

        String line = null;

        try
        {
            while(null != (line = bufReader.readLine()))
            {
                schema.append(line + "\n");
            }

            bufReader.close();
        } catch(Exception e) {
            throw new WPSException("NoApplicableCode", "Error reading schema on server.");
        }

        response.setContentType("text/xml");

        try
        {
            response.getOutputStream().print(schema.toString());
        } catch(Exception e) {
            throw new WPSException("NoApplicableCode", "Could not write schema to output.");
        }

        return;
    }
}