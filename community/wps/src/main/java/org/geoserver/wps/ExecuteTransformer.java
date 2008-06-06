/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
    @author lreed@refractions.net
*/

package org.geoserver.wps;

import java.util.Map;

import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.xml.sax.ContentHandler;

import net.opengis.wps.ExecuteType;

public abstract class ExecuteTransformer extends TransformerBase
{
    protected WPS  wps;

    public ExecuteTransformer(WPS wps)
    {
        super();

        this.wps  = wps;
    }

    public static class WPS1_0 extends ExecuteTransformer
    {
        public WPS1_0(WPS wps)
        {
            super(wps);
        }

        public Translator createTranslator(ContentHandler handler)
        {
            return new ExecuteTranslator1_0(handler);
        }

        public class ExecuteTranslator1_0 extends TranslatorSupport
        {
            public ExecuteTranslator1_0(ContentHandler handler)
            {
                super(handler, null, null);
            }

            public void encode(Object object) throws IllegalArgumentException
            {
            	// TODO XXX

            	Executor executor = new Executor((ExecuteType)object);

            	Map<String, Object> outputs = executor.execute();
            	
            	start("wps:ExecuteResponse");

            	end("wps:ExecuteResponse");
            }
        }
    }
}