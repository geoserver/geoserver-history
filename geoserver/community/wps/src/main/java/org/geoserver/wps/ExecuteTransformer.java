/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
    @author lreed@refractions.net
*/

package org.geoserver.wps;

import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.vfny.geoserver.global.Data;
import org.xml.sax.ContentHandler;

public abstract class ExecuteTransformer extends TransformerBase
{
    protected WPS  wps;
    protected Data data;
    
    public ExecuteTransformer(WPS wps, Data data)
    {
        super();
        
        this.wps  = wps;
        this.data = data;
    }
    
    public static class WPS1_0 extends ExecuteTransformer
    {
        public WPS1_0(WPS wps, Data data)
        {
            super(wps, data);
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
                // TODO
            }
        }
    }
}