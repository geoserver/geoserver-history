/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
 @author lreed@refractions.net
 */

package org.geoserver.wps;

import java.util.Locale;
import net.opengis.ows11.CodeType;
import net.opengis.wps.DescribeProcessType;
import org.vfny.geoserver.global.Data;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.geoserver.ows.xml.v1_0.OWS;
import org.geotools.process.Parameter;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;

public abstract class DescribeProcessTransformer extends TransformerBase
{
    protected WPS wps;
    protected Data data;

    protected static final String WPS_URI = "http://www.opengis.net/wps";
    protected static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";

    public DescribeProcessTransformer(WPS wps, Data data)
    {
        super();

        this.wps  = wps;
        this.data = data;
    }

    public static class WPS1_0 extends DescribeProcessTransformer
    {
        public WPS1_0(WPS wps, Data data)
        {
            super(wps, data);
        }

        public Translator createTranslator(ContentHandler handler)
        {
            return new DescribeProcessTranslator1_0(handler);
        }

        public class DescribeProcessTranslator1_0 extends TranslatorSupport
        {
            public DescribeProcessType request;

            private Locale locale;

            public DescribeProcessTranslator1_0(ContentHandler handler)
            {
                super(handler, null, null);
            }

            public void encode(Object object) throws IllegalArgumentException
            {
                this.request = (DescribeProcessType) object;

                if (null == this.request.getLanguage())
                {
                    this.locale = new Locale("en-CA");
                } else {
                    this.locale = new Locale(this.request.getLanguage());
                }

                AttributesImpl attributes = new AttributesImpl();
                attributes.addAttribute("", "xmlns:xsi", "xmlns:xsi", "", DescribeProcessTransformer.XSI_URI);
                attributes.addAttribute("", "xmlns", "xmlns", "", DescribeProcessTransformer.WPS_URI);
                attributes.addAttribute("", "xmlns:wps", "xmlns:wps", "", DescribeProcessTransformer.WPS_URI);
                attributes.addAttribute("", "xmlns:ows", "xmlns:ows", "", OWS.NAMESPACE);
                attributes.addAttribute("", "version", "version", "", "1.0.0");

                start("wps:ProcessDescriptions", attributes);

                for (Object identifier : this.request.getIdentifier())
                {
                    this.processDescription(((CodeType) identifier).getValue());
                }

                end("wps:ProcessDescriptions");
            }

            private void processDescription(String identifier)
            {
                if ("all".equals(identifier))
                {
                    this.processDescriptionAll();

                    return;
                }

                ProcessFactory pf = this.findProcessFactory(identifier);

                if (null == pf)
                {
                    throw new WPSException("Invalid identifier", "InvalidParameterValue");
                }

                this.processDescription(pf);
            }

            private void processDescription(ProcessFactory pf)
            {
                AttributesImpl attributes = new AttributesImpl();

                start("ProcessDescription", attributes);
	                element("ows:Identifier", pf.getName());
	                element("ows:Title",      pf.getTitle().toString(this.locale));
	                element("ows:Abstract",   pf.getDescription().toString(this.locale));
	                this.processOutputs(pf);
                end("ProcessDescription");
            }

            private void processDescriptionAll()
            {
                for (ProcessFactory pf : Processors.getProcessFactories())
                {
                    this.processDescription(pf);
                }
            }

            private ProcessFactory findProcessFactory(String name)
            {
                for (ProcessFactory pf : Processors.getProcessFactories())
                {
                    if (pf.getName().equals(name))
                    {
                        return pf;
                    }
                }

                return null;
            }

            private void processOutputs(ProcessFactory pf)
            {
                start("ProcessOutputs");
                for (Parameter inputIdentifier : pf.getResultInfo(null).values())
                {
                    start("Output");
	                    element("ows:Identifier", inputIdentifier.key);
	                    element("ows:Title",      "XXX Missing in Parameter.java");
	                    element("ows:Abstract",   inputIdentifier.description.toString(this.locale));
	                    start("ComplexOutput");
	                    	start("Default");
	                    		start("Format");
	                    			element("MimeType", "text/xml");
	                    			element("Encoding", "utf-8");
	                    			element("Schema",   "XXX/polygon.xsd");
	                    		end("Format");
	                    	end("Default");
	                    end("ComplexOutput");
                    end("Output");
                }
                end("ProcessOutputs");
            }
        }
    }
}