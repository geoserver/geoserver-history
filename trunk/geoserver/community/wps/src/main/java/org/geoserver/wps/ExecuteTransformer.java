/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
 *  @author lreed@refractions.net
 */

package org.geoserver.wps;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.geoserver.ows.util.RequestUtils;
import org.geoserver.ows.xml.v1_0.OWS;
import org.geotools.data.Parameter;
import org.geotools.process.ProcessFactory;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

import net.opengis.wps.ExecuteType;

public abstract class ExecuteTransformer extends TransformerBase
{
    protected static final String WPS_URI = "http://www.opengis.net/wps";
    protected static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";

    protected WPS  wps;

    public ExecuteTransformer(WPS wps)
    {
        super();

        this.wps  = wps;
    }

    public static class WPS1_0 extends ExecuteTransformer
    {
        private WPS wps;

        public WPS1_0(WPS wps)
        {
            super(wps);
            this.wps = wps;
        }

        public Translator createTranslator(ContentHandler handler)
        {
            return new ExecuteTranslator1_0(handler, this.wps);
        }

        public class ExecuteTranslator1_0 extends TranslatorSupport
        {
            private WPS             wps;
            private ExecuteType     request;
            private Locale          locale;
            private Executor        executor;
            private DataTransformer dataTransformer;

            public ExecuteTranslator1_0(ContentHandler handler, WPS wps)
            {
                super(handler, null, null);
                this.wps = wps;
            }

            public void encode(Object object) throws IllegalArgumentException
            {
                this.executor               = new Executor((ExecuteType)object, this.wps);
                Map<String, Object> outputs = executor.execute();
                this.dataTransformer        = new DataTransformer();

                this.request = (ExecuteType)object;

                if (null == this.request.getLanguage())
                {
                    this.locale = new Locale("en-CA");
                } else {
                    this.locale = new Locale(this.request.getLanguage());
                }

                String proxifiedBaseUrl = RequestUtils.proxifiedBaseURL(request.getBaseUrl(), wps.getGeoServer().getProxyBaseUrl());
                String serviceInstance  = proxifiedBaseUrl + "ows?service=WPS&request=GetCapabilities";

                AttributesImpl attributes = new AttributesImpl();
                attributes.addAttribute("", "xmlns:xsi",       "xmlns:xsi",       "", DescribeProcessTransformer.XSI_URI);
                attributes.addAttribute("", "xmlns",           "xmlns",           "", DescribeProcessTransformer.WPS_URI);
                attributes.addAttribute("", "xmlns:wps",       "xmlns:wps",       "", DescribeProcessTransformer.WPS_URI);
                attributes.addAttribute("", "xmlns:ows",       "xmlns:ows",       "", OWS.NAMESPACE);
                attributes.addAttribute("", "version",         "version",         "", "1.0.0");
                attributes.addAttribute("", "service",         "service",         "", "WPS");
                attributes.addAttribute("", "lang",            "lang",            "", this.locale.toString());
                attributes.addAttribute("", "serviceInstance", "serviceInstance", "", serviceInstance);

                start("wps:ExecuteResponse", attributes);
                    this.processBrief();
                    this.status();
                    this.outputs(outputs);
                end("wps:ExecuteResponse");
            }

            private void outputs(Map<String, Object> outputs)
            {
            	ProcessFactory pf = this.executor.getProcessFactory();

            	Map<String, Object> encoded = this.dataTransformer.encodeOutputs(outputs, pf.getResultInfo(null));

            	start("wps:ProcessOutputs");
            	for(String outputName : outputs.keySet())
            	{
            		Parameter<?> param = (pf.getResultInfo(null)).get(outputName);	// TODO remove null argument when no longer needed

            		start("wps:Output");
            			element("ows:Identifier", param.key);
            			element("ows:Identifier", param.title.toString(this.locale));
            			start("wps:Data");
            				
            			end("wps:Data");
            		end("wps:Output");
            	}
            	end("wps:ProcessOutputs");
            }

            private void processBrief()
            {
                ProcessFactory pf = this.executor.getProcessFactory();
                AttributesImpl attributes = new AttributesImpl();
                attributes.addAttribute("", "wps:processVersion", "wps:processVersion", "", pf.getVersion());

                start("wps:Process", attributes);
                    element("ows:Identifier", pf.getName());
                    element("ows:Title",      pf.getTitle().toString(this.locale));
                end("wps:Process");
            }

            private void status()
            {
                SimpleDateFormat fmt  = new SimpleDateFormat("yyyy-MM-dd:'T'HH:mm:ss");	// TODO UTC
                Date             date = new Date();
                String           time = fmt.format(date);

                AttributesImpl attributes = new AttributesImpl();
                attributes.addAttribute("", "creationTime", "creationTime", "", time);

                start("wps:Status", attributes);
                    element("wps:ProcessSucceded", "");
                end("wps:Status");
            }
        }
    }
}